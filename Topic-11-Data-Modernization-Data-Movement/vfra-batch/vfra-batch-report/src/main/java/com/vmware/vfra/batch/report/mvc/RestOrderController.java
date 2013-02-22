// Copyright (c) 2013 VMware, Inc. All rights reserved.
package com.vmware.vfra.batch.report.mvc;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.vmware.vfra.batch.report.OrderRepository;
import com.vmware.vfra.batch.report.OrderRepositoryCustom.OrderType;


@Controller
@RequestMapping("/*")
public class RestOrderController {

	private static Log logger = LogFactory
			.getLog(RestOrderController.class);

	@Autowired
	private OrderRepository orderRepository;

	@RequestMapping(value = "/history", method = RequestMethod.GET)
	public @ResponseBody
	ResponseEntity<List<Object[]>> getOrderHistoryByType(@RequestParam() String type, @RequestParam() Integer timeStamps)
			throws Exception {

		logger.debug("type: " + type + " timeStamps: " + timeStamps);

		List<Object[]> result = 
				orderRepository.getOrderHistoryByType(OrderType.valueOf(type), timeStamps);
		for (Object[] i : result) {
			i[0] = DateFormat.getInstance().format((Timestamp)i[0]);
		}
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		return new ResponseEntity<List<Object[]>>(result, headers, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/report", method = RequestMethod.GET)
	public String redirect() {

		return "redirect:/order/resources/orderhist.html";
	}

}