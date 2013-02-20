package com.vmware.vfra.batch.inttest;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Calendar;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
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

import com.vmware.vfra.batch.master.RestStepExecutionService;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration()
@ContextConfiguration()
@ActiveProfiles(profiles = { "dev", "property-datasource" })
public class SuccessfulStepTest {

	@Autowired
	private JobLauncherTestUtils jobLauncherTestUtils;

	@Autowired
	private RestStepExecutionService restStepExecutionService;

	@Autowired
	private JdbcTemplate reportingJdbcTemplate;

	@Autowired
	private JdbcTemplate jobJdbcTemplate;

	@Autowired
	private WebApplicationContext wac;

	@Before
	public void setup() {

		MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(this.wac)
				.alwaysExpect(status().isAccepted()).build();

		RestTemplate restTemplate = new RestTemplate(
				new MockMvcClientHttpRequestFactory(mockMvc));
		// used by MasterPollingPartitionHandler
		restStepExecutionService.setRestTemplate(restTemplate);
		restStepExecutionService
				.setRestUrl("/job/{jobExecutionId}/step/{stepId}/name/{stepName}");
	}

	/**
	 * Test using restTemplate wired above with mock request to send REST
	 * message to WorkerController running in MockMvc. Avoids overhead of
	 * embedded servlet (Winstone or Jetty), i.e. separate thread, 
	 * servlet container initialization, network layer communication.
	 */
	@Test
	public void testSuccessfulStep() throws Exception {
		JobExecution exec = jobLauncherTestUtils
				.launchJob(new JobParametersBuilder().addLong("jobTimeStamp",
						Calendar.getInstance().getTimeInMillis())
						.toJobParameters());
		Assert.assertEquals(BatchStatus.COMPLETED, exec.getStatus());
		Assert.assertEquals(10, reportingJdbcTemplate
				.queryForInt("select count(*) from reporting.orders"));
		Assert.assertEquals(
				1,
				reportingJdbcTemplate
						.queryForInt("select order_type from reporting.orders where order_id = 1"));
		Assert.assertEquals(
				2,
				reportingJdbcTemplate
						.queryForInt("select order_type from reporting.orders where order_id = 7"));

		Assert.assertEquals(
				0,
				reportingJdbcTemplate
						.queryForInt("select count(*) from reporting.orders where job_time_stamp = null"));

		Assert.assertEquals(
				0,
				jobJdbcTemplate
						.queryForInt("select count(*) from batch_step_execution where job_execution_id = "
								+ exec.getJobId()
								+ " and status <> 'COMPLETED'"));
		Assert.assertEquals(
				1,
				jobJdbcTemplate
						.queryForInt("select count(*) from batch_job_execution where job_execution_id = "
								+ exec.getJobId() + " and status = 'COMPLETED'"));

	}
}
