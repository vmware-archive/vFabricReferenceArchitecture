// Copyright (c) 2013 VMware, Inc. All rights reserved.
package com.vmware.vfra.batch.master;

import java.util.Collection;
import java.util.concurrent.CancellationException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Poller used by a master process to poll the {@link JobRepository} for the
 * status of particular job execution.
 */
public class MasterPoller {

	private static Log logger = LogFactory
			.getLog(MasterPoller.class);
	private int maxPollTimeInSeconds = 60;
	private int pollingIntervalInSeconds = 5;

	@Autowired
	JobExplorer jobExplorer;

	private final ScheduledExecutorService scheduler = Executors
			.newScheduledThreadPool(1);

	/**
	 * @return an integer representing the maximum number of seconds to poll the
	 *         {@link JobRepository} for status.
	 */
	public int getMaxPollTimeInSeconds() {
		return maxPollTimeInSeconds;
	}

	/**
	 * @return an integer representing the interval in seconds to poll the
	 *         {@link JobRepository} for status
	 */

	public int getPollingIntervalInSeconds() {
		return pollingIntervalInSeconds;
	}

	public ScheduledExecutorService getScheduler() {
		return scheduler;
	}

	public void setMaxPollTimeInSeconds(int maxPollTimeInSeconds) {
		this.maxPollTimeInSeconds = maxPollTimeInSeconds;
	}

	public void setPollingIntervalInSeconds(int pollingIntervalInSeconds) {
		this.pollingIntervalInSeconds = pollingIntervalInSeconds;
	}

	/**
	 * Polls the {@link JobRepository} for the status of a particular job
	 * execution and returns a {@link StepExecutionStatus} once all the remote
	 * workers have completed and saved a {@link BatchStatus} or a timeout has
	 * occurred as determined by {@link #getMaxPollTimeInSeconds()}
	 * 
	 * @param jobExecutionId
	 *            the job to poll
	 * @param partitionSize
	 *            the number of partitions, or splits created by the master that
	 *            determines the the number of step executions that are
	 *            distributed to remote workers and saved by remote workers in
	 *            the {@link JobRepository} .
	 * @return the status after either all remote workers have completed their
	 *         work or a timeout has occurred as configured by
	 *         {@link #getMaxPollTimeInSeconds()}
	 */
	public StepExecutionStatus pollForStatus(final long jobExecutionId,
			final long masterStepExecutionId, final int partitionSize) {

		final Runnable poller = new Runnable() {
			public void run() {
				if (logger.isDebugEnabled()) {
					logger.debug("polling...");
				}
				if (getStatus(jobExecutionId, masterStepExecutionId,
						partitionSize).getRunningCount() == 0) {
					if (logger.isDebugEnabled()) {
						logger.debug("steps finished running; stopping polling via CancellationException...");
					}
					throw new CancellationException();
				}
			}
		};
		final ScheduledFuture<?> pollerHandle = scheduler.scheduleAtFixedRate(
				poller, 0, pollingIntervalInSeconds, TimeUnit.SECONDS);
		try {
			pollerHandle.get(maxPollTimeInSeconds, TimeUnit.SECONDS);
		} catch (TimeoutException e) {
			logger.info("Timeout occurred while polling for status as determined "
					+ "by maxPollTimeInSeconds (" + maxPollTimeInSeconds + ")");
		} catch (Exception e) {
			if (e.getCause() != null
					&& e.getCause().getClass() != CancellationException.class) {
				logger.error("Exception encountered: " + e);
			}
		}
		return getStatus(jobExecutionId, masterStepExecutionId, partitionSize);
	}

	private StepExecutionStatus getStatus(long jobExecutionId,
			long masterStepExecutionId, int partitionSize) {
		
		Collection<StepExecution> steps = jobExplorer.getJobExecution(
				jobExecutionId).getStepExecutions();
		int runningCount = 0;
		int successfulCount = 0;
		for (StepExecution step : steps) {
			if (step.getId() == masterStepExecutionId) {
				continue;
			}
			if (step.getStatus().isRunning()) {
				runningCount++;
			} else if (!step.getStatus().isUnsuccessful()) {
				successfulCount++;
			}
		}
		StepExecutionStatus status = new StepExecutionStatus(runningCount,
				successfulCount, partitionSize);
		if (logger.isDebugEnabled()) {
			logger.debug("status: " + status);
		}
		return status;
	}
}
