// Copyright (c) 2013 VMware, Inc. All rights reserved.
package com.vmware.vfra.batch.worker;

import java.util.Calendar;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecutionException;
import org.springframework.batch.core.JobInterruptedException;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.NoSuchStepException;
import org.springframework.batch.core.step.StepLocator;
import org.springframework.batch.integration.partition.StepExecutionRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.Async;

/**
 * Adapted from
 * {@link org.springframework.batch.integration.partition.StepExecutionRequestHandler}
 * to handle non-messaging based requests. Implements 
 * {@link StepExecutionRequestHandler#handle(StepExecutionRequest)} using @Async annotation
 * so that step execution will occur in separate thread and Controller can return
 * immediately, assuming that master process will poll {@link JobRepository} for
 * status of the step execution.
 * 
 **/
public class WorkerRequestHandler implements StepExecutionRequestHandler {

	private static Log logger = LogFactory.getLog(WorkerRequestHandler.class);

	@Autowired
	private JobExplorer jobExplorer;

	@Autowired
	private StepLocator stepLocator;

	@Autowired
	private JobRepository jobRepository;

	/**
	 * Used to locate a {@link Step} to execute for each request.
	 * 
	 * @param stepLocator
	 *            a {@link StepLocator}
	 */
	public void setStepLocator(StepLocator stepLocator) {
		this.stepLocator = stepLocator;
	}

	/**
	 * An explorer that should be used to check for {@link StepExecution}
	 * completion.
	 * 
	 * @param jobExplorer
	 *            a {@link JobExplorer} that is linked to the shared repository
	 *            used by all remote workers.
	 */
	public void setJobExplorer(JobExplorer jobExplorer) {
		this.jobExplorer = jobExplorer;
	}

	/**
	 * Used to update status of a {@link StepExecution} by the remote worker
	 * 
	 * @param jobRepository
	 *            a {@link jobRepository} that is linked to the shared
	 *            repository used by all remote workers.
	 */
	public void setJobRepository(JobRepository jobRepository) {
		this.jobRepository = jobRepository;
	}

	@Async
	public void handle(final StepExecutionRequest request) throws Exception {

		if (logger.isDebugEnabled()) {
			logger.debug("Executing step: " + request);
		}
		try {
			StepExecution stepExecution = getStepExecution(request);
			String stepName = request.getStepName();
			Step step = stepLocator.getStep(stepName);
			if (step == null) {
				throw new NoSuchStepException(String.format(
						"No Step with name [%s] could be located.", stepName));
			}
			initStepExecutionContext(request.getJobExecutionId(), stepExecution);
			step.execute(stepExecution);
			if (logger.isDebugEnabled()) {
				logger.debug("Step successfully executed: " + request);
			}
		} catch (JobInterruptedException e) {
			updateJobRepositoryWithFailure(request,
					"Job interrupted during execution", e, BatchStatus.STOPPED);
			logger.error(e);
			throw e;
		} catch (Exception e) {
			updateJobRepositoryWithFailure(request,
					"Step execution encountered exception", e,
					BatchStatus.FAILED);
			logger.error(e);
			throw e;
		}
	}

	private StepExecution getStepExecution(StepExecutionRequest request) {
		Long jobExecutionId = request.getJobExecutionId();
		Long stepExecutionId = request.getStepExecutionId();
		StepExecution stepExecution = jobExplorer.getStepExecution(
				jobExecutionId, stepExecutionId);
		if (stepExecution == null) {
			throw new NoSuchStepException(
					"No StepExecution could be located for this request: "
							+ request);
		}
		return stepExecution;
	}

	private void initStepExecutionContext(long jobExecutionId,
			StepExecution stepExecution) throws JobExecutionException {
		// stepExecution.getJobParameters() not working, so
		// transfer needed values to StepExecutionContext and reference
		// them there using late binding in job context xml
		// TODO how to copy Date value from JobParameters to
		// StepExecutionContext
		Long jobTimeStamp = jobExplorer.getJobExecution(jobExecutionId)
				.getJobInstance().getJobParameters().getLong("jobTimeStamp");
		if (jobTimeStamp == null) {
			throw new JobExecutionException(
					"jobTimeStamp must be defined in JobParameters");
		}
		stepExecution.getExecutionContext().putLong("jobTimeStamp",
				jobTimeStamp);
	}

	private void updateJobRepositoryWithFailure(StepExecutionRequest request,
			String exitDescription, Throwable t, BatchStatus status) {
		try {
			StepExecution stepExecution = getStepExecution(request);
			ExitStatus exitStatus = ExitStatus.FAILED
					.addExitDescription(exitDescription + t);
			stepExecution.setExitStatus(exitStatus);
			stepExecution.addFailureException(t);
			stepExecution.setStatus(status);
			stepExecution.setEndTime(Calendar.getInstance().getTime());
			jobRepository.update(stepExecution);
		} catch (Exception e) {
			logger.error("failed to update JobRepository with failure info: "
					+ e);
		}
	}
}
