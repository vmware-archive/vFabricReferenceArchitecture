package com.vmware.vfra.batch.inttest;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.util.HashMap;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.client.MockMvcClientHttpRequestFactory;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration
public class RestTemplateTest {

	@Autowired
	private WebApplicationContext wac;

	private RestTemplate restTemplate;

	@Before
	public void setup() {
		MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(wac)
				.alwaysExpect(status().isOk()).build();
		this.restTemplate = new RestTemplate(
				new MockMvcClientHttpRequestFactory(mockMvc));
	}

	@EnableWebMvc
	@Configuration
	@ComponentScan(basePackageClasses = RestTemplateTest.class)
	static class MyWebConfig extends WebMvcConfigurerAdapter {
	}

	@Test
	public void testPathVariable() throws Exception {

		String restUrl = "/job/{jobExecutionId}/step/{stepId}/name/{stepName}";
		Map<String, String> vars = new HashMap<String, String>();
		vars.put("jobExecutionId", "1");
		vars.put("stepId", "2");
		vars.put("stepName", "test");
		restTemplate.put(restUrl, null, vars);
		// Test will fail if status is not ok
	}

	@Controller
	@RequestMapping("/*")
	static class MyController {

		@RequestMapping(value = "job/{jobExecutionId}/step/{stepId}/name/{stepName}", method = RequestMethod.PUT)
		public @ResponseBody
		ResponseEntity<String> runStepExecution(
				@PathVariable Long jobExecutionId, @PathVariable Long stepId,
				@PathVariable String stepName) throws Exception {

			HttpStatus status = HttpStatus.OK;
			if (jobExecutionId != 1 || stepId != 2 || !stepName.equals("test")) {
				status = HttpStatus.CONFLICT;
			}

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.TEXT_PLAIN);
			return new ResponseEntity<String>("test", headers, status);
		}
	}
}
