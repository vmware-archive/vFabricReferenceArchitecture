package com.vmware.vfra.batch.master;

import java.util.HashMap;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.integration.partition.StepExecutionRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

/**
* Send request using {@link RestTemplate#postForObject(java.net.URI, Object, Class)} and
* {@link RestStepExecutionRequest} as the request object.
*/
public class RestStepExecutionService implements RemoteStepExecutionService {

	private static Log logger = LogFactory
			.getLog(RestStepExecutionService.class);
	
	@Autowired
	private RestTemplate restTemplate;

	private String restUrl;

	public void setRestTemplate(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	public void setRestUrl(String restUrl) {
		this.restUrl = restUrl;
	}

	public RestTemplate getRestTemplate() {
		return restTemplate;
	}

	public String getRestUrl() {
		return restUrl;
	}

	public void afterPropertiesSet() throws Exception {
		Assert.state(restTemplate != null, "The restTemplate must be set");
		Assert.state(restUrl != null, "The restUrl must be set");
	}
	
	@Override
	public void runStepExecution(StepExecutionRequest request) {
		
		if (logger.isDebugEnabled()) {
			logger.debug("calling RestTemplate.put: restUrl: " + restUrl + " request:  " + request);
		}

		Map<String, String> vars = new HashMap<String,String>();
		vars.put("jobExecutionId", request.getJobExecutionId().toString());
		vars.put("stepId", request.getStepExecutionId().toString());
		vars.put("stepName", request.getStepName());
		restTemplate.put(restUrl, null, vars);
	}
}
