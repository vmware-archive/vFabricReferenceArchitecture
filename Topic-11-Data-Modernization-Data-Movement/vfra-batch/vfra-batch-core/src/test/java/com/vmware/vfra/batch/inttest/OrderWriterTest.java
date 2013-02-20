package com.vmware.vfra.batch.inttest;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration()
@ActiveProfiles(profiles = {"dev", "property-datasource"})
public class OrderWriterTest {
	@Autowired
	private JobLauncherTestUtils jobLauncherTestUtils;

	@Autowired
	private JdbcTemplate reportingJdbcTemplate;

	@Test
	public void testJdbc() throws Exception {
		JobExecution exec = jobLauncherTestUtils.launchJob();
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
	}

}
