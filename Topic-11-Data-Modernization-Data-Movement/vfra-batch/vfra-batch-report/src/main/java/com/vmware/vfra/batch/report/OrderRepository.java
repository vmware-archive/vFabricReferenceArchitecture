// Copyright (c) 2013 VMware, Inc. All rights reserved.
package com.vmware.vfra.batch.report;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.stereotype.Repository;

import com.vmware.vfra.batch.report.domain.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Order.Key>,
		QueryDslPredicateExecutor<Order>, OrderRepositoryCustom {
	
	List<Order> findByOrderStatusAndQuoteSymbol(String orderStatus, String quoteSymbol);
}

