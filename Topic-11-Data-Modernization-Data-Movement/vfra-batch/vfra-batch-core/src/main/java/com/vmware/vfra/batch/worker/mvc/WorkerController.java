// Copyright (c) 2013 VMware, Inc. All rights reserved.
package com.vmware.vfra.batch.worker.mvc;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.integration.partition.StepExecutionRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.PathVariable;

import com.vmware.vfra.batch.worker.StepExecutionRequestHandler;

@Controller
@RequestMapping("/*")
public class WorkerController {

	private static Log logger = LogFactory
			.getLog(WorkerController.class);

	@Autowired
	private StepExecutionRequestHandler stepExecutionRequestHandler;

	public StepExecutionRequestHandler getStepExecutionRequestHandler() {
		return stepExecutionRequestHandler;
	}

	public void setStepExecutionRequestHandler(
			StepExecutionRequestHandler stepExecutionRequestHandler) {
		this.stepExecutionRequestHandler = stepExecutionRequestHandler;
	}
	
	@RequestMapping(value = "ping", method = RequestMethod.GET)
	public @ResponseBody
	ResponseEntity<String> ping()
			throws Exception {

		logger.debug("pinged...");
		return new ResponseEntity<String>("ready for work\n", HttpStatus.OK);
	}

	@RequestMapping(value = "job/{jobExecutionId}/step/{stepId}/name/{stepName}", method = RequestMethod.PUT)
	public @ResponseBody
	ResponseEntity<String> runStepExecution(@PathVariable Long jobExecutionId,
			@PathVariable Long stepId, @PathVariable String stepName)
			throws Exception {

		logger.debug("jobExecutionId: " + jobExecutionId + " stepId: " + stepId
				+ " stepName: " + stepName);

		final StepExecutionRequest request = new StepExecutionRequest(stepName,
				jobExecutionId, stepId);

		stepExecutionRequestHandler.handle(request);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.TEXT_PLAIN);
		return new ResponseEntity<String>(
				"Step execution started; check job repository for status",
				headers, HttpStatus.ACCEPTED);
	}
}