package com.vmware.vfra.batch.master;

import org.springframework.batch.integration.partition.StepExecutionRequest;
import org.springframework.batch.core.explore.JobExplorer;

public interface RemoteStepExecutionService {

	/**
	 * Contract for sending a {@link StepExecutionRequest} to a remote worker. Assumes that sender
	 * will check the {@link JobExplorer} for status of the remote step execution.
	 **/
	void runStepExecution(StepExecutionRequest request);

}
