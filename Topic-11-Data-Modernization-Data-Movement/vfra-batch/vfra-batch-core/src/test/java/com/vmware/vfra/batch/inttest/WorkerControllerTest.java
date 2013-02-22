// Copyright (c) 2013 VMware, Inc. All rights reserved.
package com.vmware.vfra.batch.inttest;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.integration.partition.StepExecutionRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.vmware.vfra.batch.worker.StepExecutionRequestHandler;
import com.vmware.vfra.batch.worker.mvc.WorkerController;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration()
@ContextConfiguration("/com/vmware/vfra/batch/web/servlet-context.xml")
@ActiveProfiles(profiles = { "dev", "property-datasource" })
public class WorkerControllerTest {

	private MockMvc mockMvc;

	@Autowired
	private WebApplicationContext wac;
	
	@Before
	public void setup() {

		mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();

		WorkerController controller = wac.getBean(WorkerController.class);
		controller
				.setStepExecutionRequestHandler(new StepExecutionRequestHandler() {
					public void handle(final StepExecutionRequest request)
							throws Exception {
						return;
					}
				});
	}

	/**
	 * Example of using fluent API to test REST controller. See:
	 * http://blog.springsource.org/2012/11/12/spring-framework-3-2-rc1-spring-mvc-test-framework/
	 */
	@Test
	public void testController() throws Exception {
		mockMvc.perform(
				put("/job/1/step/1/name/step1").accept(MediaType.TEXT_PLAIN))
				.andExpect(status().isAccepted())
				.andExpect(content().contentType(MediaType.TEXT_PLAIN))
				.andExpect(
						content()
								.string("Step execution started; check job repository for status"));
	}
}
