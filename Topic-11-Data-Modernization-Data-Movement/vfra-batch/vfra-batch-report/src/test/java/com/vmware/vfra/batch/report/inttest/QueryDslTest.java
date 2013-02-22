// Copyright (c) 2013 VMware, Inc. All rights reserved.
package com.vmware.vfra.batch.report.inttest;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import static com.mysema.query.collections.MiniApi.*;
import static org.hamcrest.Matchers.*;

import com.mysema.query.jpa.JPQLQuery;
import com.mysema.query.jpa.impl.JPAQuery;
import com.vmware.vfra.batch.report.OrderRepository;
import com.vmware.vfra.batch.report.OrderRepositoryCustom.OrderType;
import com.vmware.vfra.batch.report.domain.Order;
import com.vmware.vfra.batch.report.domain.QOrder;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration()
public class QueryDslTest {

	@PersistenceContext
	EntityManager entityManager;

	@Autowired
	OrderRepository orderRepository;

	@Test
	public void testQuery() throws Exception {
		// Can't quite pull this off:
		// select job_time_stamp, count(*), sum(order_type = 1) as buy,
		// sum(order_type = 2) as sell
		// from reporting.orders
		// group by job_time_stamp
		int distinctTimestamps = 5;
		QOrder order = QOrder.order;
		
		JPQLQuery query = new JPAQuery(entityManager).from(QOrder.order)
				.groupBy(order.key.jobTimeStamp).orderBy(order.key.jobTimeStamp.asc()).limit(distinctTimestamps);
		List<Object[]> result = query.where(order.orderType.eq(2)).list(
				order.key.jobTimeStamp, order.key.jobTimeStamp.count());
		Assert.assertTrue(result.size() == 3);
		Assert.assertTrue(((Long)result.get(0)[1]) == 2);
		Assert.assertTrue(((Long)result.get(1)[1]) == 1);		
		Assert.assertTrue(((Long)result.get(2)[1]) == 1);
		for (Object[] kv : result) {
			System.out.println("key: " + kv[0] + " value: " + kv[1]);
		}
	}

	@Test
	public void testRepository() throws Exception {
		List<Object[]> result = orderRepository.getOrderHistoryByType(
				OrderType.BUY, 3);
		Assert.assertTrue(result.size() == 3);
		Assert.assertTrue(((Long)result.get(0)[1]) == 2);
		Assert.assertTrue(((Long)result.get(1)[1]) == 2);		
		Assert.assertTrue(((Long)result.get(2)[1]) == 2);
		
		result = orderRepository.getOrderHistoryByType(
				OrderType.SELL, 3);
		Assert.assertTrue(result.size() == 3);
		Assert.assertTrue(((Long)result.get(0)[1]) == 2);
		Assert.assertTrue(((Long)result.get(1)[1]) == 1);		
		Assert.assertTrue(((Long)result.get(2)[1]) == 1);
		
		result = orderRepository.getOrderHistoryByType(
				OrderType.SELL, 2);
		Assert.assertTrue(result.size() == 2);
		Assert.assertTrue(((Long)result.get(0)[1]) == 2);
		Assert.assertTrue(((Long)result.get(1)[1]) == 1);		


	}
	
	@Test
	public void testRepositoryCrud() throws Exception {
		List<Order> l = orderRepository.findByOrderStatusAndQuoteSymbol("closed", "aapl");
		Assert.assertEquals(l.get(0).getOrderStatus(), "closed");
		Assert.assertEquals(l.get(0).getQuoteSymbol(), "aapl");
		Assert.assertTrue(l.get(0).getJobTimeStamp() != null);
	}
	/**
	 * Not working:
	 * http://localhost:8080/vfra-batch-report/orderx/order:> get search/findByOrderStatusAndQuoteSymbol --params "{orderStatus: 'closed', quoteSymbol: 'aapl'}"
	 * http://localhost:8080/vfra-batch-report/orderx/order:> get search/findByOrderStatusAndQuoteSymbol?quoteSymbol='aapl'&orderStatus='closed'
	 */
}


