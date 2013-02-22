// Copyright (c) 2013 VMware, Inc. All rights reserved.
package com.vmware.vfra.batch.report;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.data.jpa.repository.support.QueryDslRepositorySupport;

import com.mysema.query.jpa.JPQLQuery;
import com.mysema.query.jpa.impl.JPAQuery;
import com.vmware.vfra.batch.report.domain.Order;
import com.vmware.vfra.batch.report.domain.QOrder;

public class OrderRepositoryCustomImpl extends QueryDslRepositorySupport
		implements OrderRepositoryCustom {

	@PersistenceContext
	EntityManager entityManager;

	public OrderRepositoryCustomImpl() {
		super(Order.class);
	}

	@Override
	public List<Object[]> getOrderHistoryByType(OrderType type,
			Integer distinctTimestamps) {
		QOrder order = QOrder.order;
		JPQLQuery query = new JPAQuery(entityManager).from(order)
				.groupBy(order.key.jobTimeStamp)
				.orderBy(order.key.jobTimeStamp.asc())
				.limit(distinctTimestamps);
		List<Object[]> result = query.where(order.orderType.eq(type.value))
				.list(order.key.jobTimeStamp, order.key.jobTimeStamp.count());
		return result;
	}
}
