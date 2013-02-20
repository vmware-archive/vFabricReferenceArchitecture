package com.vmware.vfra.batch.inttest;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Calendar;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionException;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.integration.partition.StepExecutionRequest;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.client.MockMvcClientHttpRequestFactory;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;
import com.vmware.vfra.batch.master.MasterPollingPartitionHandler;
import com.vmware.vfra.batch.master.MasterPoller;
import com.vmware.vfra.batch.master.RestStepExecutionService;
import com.vmware.vfra.batch.worker.StepExecutionRequestHandler;
import com.vmware.vfra.batch.worker.mvc.WorkerController;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration()
@ContextConfiguration()
@ActiveProfiles(profiles = { "dev", "property-datasource" })
public class FailedStepTest {

	@Autowired
	private JobLauncherTestUtils jobLauncherTestUtils;

	@Autowired
	private RestStepExecutionService restStepExecutionService;

	@Autowired
	MasterPollingPartitionHandler masterPartitionHandler;

	@Autowired
	private JdbcTemplate jobJdbcTemplate;

	@Autowired
	private WebApplicationContext wac;
	
	@Before
	public void setup() {

		MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(wac)
				.alwaysExpect(status().isAccepted()).build();
						
		WorkerController controller = wac.getBean(WorkerController.class);
		controller.setStepExecutionRequestHandler(new StepExecutionRequestHandler() {
			public void handle(final StepExecutionRequest request) throws Exception {
				if (true) throw new RuntimeException("failedStepTest: worker request handler intentionally throwing exception"); 
			}
		});
		
		MasterPoller poller = wac.getBean(MasterPoller.class);
		poller.setMaxPollTimeInSeconds(2);
		MasterPollingPartitionHandler handler = wac.getBean(MasterPollingPartitionHandler.class);
		handler.setPartitionSize(1);

		RestTemplate restTemplate = new RestTemplate(
				new MockMvcClientHttpRequestFactory(mockMvc));
		restStepExecutionService.setRestTemplate(restTemplate);
		restStepExecutionService
				.setRestUrl("/job/{jobExecutionId}/step/{stepId}/name/{stepName}");
	}

	@Test
	public void testFailedStep() throws Exception {
		JobExecution exec = null;
		try {
			exec = jobLauncherTestUtils.launchJob(new JobParametersBuilder()
					.addLong("jobTimeStamp",
							Calendar.getInstance().getTimeInMillis())
					.toJobParameters());
		} catch (JobExecutionException expected) {
		}
		Assert.assertEquals(BatchStatus.FAILED, exec.getStatus());
		Assert.assertEquals(
				0,
				jobJdbcTemplate
						.queryForInt("select count(*) from batch_step_execution where job_execution_id = "
								+ exec.getJobId() + " and status = 'COMPLETED'"));
		Assert.assertEquals(
				1,
				jobJdbcTemplate
						.queryForInt("select count(*) from batch_job_execution where job_execution_id = "
								+ exec.getJobId() + " and status = 'FAILED'"));

	}
}
