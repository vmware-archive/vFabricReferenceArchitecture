package com.vmware.vfra.batch.worker;

import org.springframework.batch.integration.partition.StepExecutionRequest;

/**
 * Interface implemented by a remote worker or slave that receives step
 * execution requests from a master process.
 */
public interface StepExecutionRequestHandler {

	/**
	 * Handle a request to execute a step execution sent by a master process.
	 * The remote worker retrieves the {@link StepExecutionContext} from the
	 * {@link JobRepository} to determine what work must be completed.
	 * 
	 * @param request
	 *            a {@link StepExecutionRequest} that contains the information
	 *            the remote worker uses to retrieve the
	 *            {@link StepExecutionContext} from the {@link JobRepository}.
	 */
	void handle(final StepExecutionRequest request) throws Exception;
}
