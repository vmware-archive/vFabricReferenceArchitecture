package com.vmware.vfra.batch.master;

import java.util.Collection;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecutionException;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.partition.PartitionHandler;
import org.springframework.batch.core.partition.StepExecutionSplitter;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.integration.partition.StepExecutionRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

/**
 * A {@link PartitionHandler} that distributes {@link StepExecutionRequest}s to
 * remote workers/slaves using a {@link RemoteStepExecutionService}. The remote
 * worker is expected to fork a thread that will handle the step execution and
 * return immediately. This handler uses a {@link MasterPoller} to
 * poll the {@link JobRepository} to determine if the remote worker successfully
 * completed the {@link StepExecution}, based on updates made by the remote
 * worker to the {@link JobRepository}.
 */
public class MasterPollingPartitionHandler implements PartitionHandler {

	private static Log logger = LogFactory
			.getLog(MasterPollingPartitionHandler.class);

	private int partitionSize = 1;

	private String stepName;

	@Autowired
	private RemoteStepExecutionService remoteStepExecutionService;

	@Autowired
	private MasterPoller poller;

	public void setPoller(MasterPoller poller) {
		this.poller = poller;
	}

	/**
	 * Passed to the {@link StepExecutionSplitter} in the
	 * {@link #handle(StepExecutionSplitter, StepExecution)} method, instructing
	 * it how many {@link StepExecution} instances are required, ideally. The
	 * {@link StepExecutionSplitter} is allowed to ignore the grid size in the
	 * case of a restart, since the input data partitions must be preserved.
	 * 
	 * @param partitionSize
	 *            the number of step executions that will be created
	 */
	public void setPartitionSize(int partitionSize) {
		this.partitionSize = partitionSize;
	}

	/**
	 * The name of the {@link Step} that will be used to execute the partitioned
	 * {@link StepExecution}. This is a regular Spring Batch step, with all the
	 * business logic required to complete an execution based on the input
	 * parameters in its {@link StepExecution} context. The name will be
	 * translated into a {@link Step} instance by the remote worker.
	 * 
	 * @param stepName
	 *            the name of the {@link Step} instance to execute business
	 *            logic
	 */
	public void setStepName(String stepName) {
		this.stepName = stepName;
	}

	public void setRemoteStepExecutionService(
			RemoteStepExecutionService remoteStepExecutionService) {
		this.remoteStepExecutionService = remoteStepExecutionService;
	}

	public String getStepName() {
		return stepName;
	}

	public int getPartitionSize() {
		return partitionSize;
	}

	public RemoteStepExecutionService getRemoteStepExecutionService() {
		return remoteStepExecutionService;
	}

	public MasterPoller getPoller() {
		return poller;
	}

	public void afterPropertiesSet() throws Exception {
		Assert.notNull(stepName,
				"A step name must be provided for the remote workers.");
		Assert.notNull(
				remoteStepExecutionService,
				"A remote step execution service is required to distribute work to remote workers.");
		Assert.notNull(poller, "A poller is required.");
	}

	/**
	 * Sends {@link StepExecutionRequest} requests to a remote
	 * {@link WorkerRequestHandler}
	 * 
	 * @see PartitionHandler#handle(StepExecutionSplitter, StepExecution)
	 */
	public Collection<StepExecution> handle(
			StepExecutionSplitter stepExecutionSplitter,
			StepExecution masterStepExecution) throws Exception {

		Set<StepExecution> partition = stepExecutionSplitter.split(
				masterStepExecution, partitionSize);

		for (StepExecution stepExecution : partition) {
			StepExecutionRequest request = new StepExecutionRequest(stepName,
					stepExecution.getJobExecutionId(), stepExecution.getId());
			if (logger.isDebugEnabled()) {
				logger.debug("Sending request: " + request);
			}
			stepExecution.setStatus(BatchStatus.STARTED);
			try {
				remoteStepExecutionService.runStepExecution(request);
			} catch (Exception e) {
				logger.error("Exception encountered sending step execution request to remote worker: \nException: "
						+ e + "\nStep Execution: " + stepExecution);
				stepExecution.setStatus(BatchStatus.FAILED);
				stepExecution.addFailureException(e);
			}
		}
		if (logger.isDebugEnabled()) {
			logger.debug("Polling: master: " + masterStepExecution + " partition: "
					+ partition);
		}
		StepExecutionStatus status = poller.pollForStatus(
				masterStepExecution.getJobExecutionId(),
				masterStepExecution.getId(), partition.size());

		if (status.getRunningCount() != 0
				|| status.getSuccessfulCount() != partition.size()) {
			throw new JobExecutionException(
					"At least one remote step execution failed to complete successfully in the time allotted: status: "
							+ status);
		}
		return partition;
	}
}
