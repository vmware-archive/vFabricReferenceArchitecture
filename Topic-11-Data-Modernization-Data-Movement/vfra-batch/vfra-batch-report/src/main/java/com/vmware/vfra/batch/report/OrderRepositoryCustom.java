// Copyright (c) 2013 VMware, Inc. All rights reserved.
package com.vmware.vfra.batch.report;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface OrderRepositoryCustom {

	enum OrderType {
		BUY(1), SELL(2);

		public final int value;

		OrderType(int value) {
			this.value = value;
		}
	}

	/**
	 * Return a list of key/values (Object array w/ two elements) that maps
	 * distinct timestamps to counts of buy or sell order counts. To be used in
	 * graph that shows timestamp on x axis and buy or sell counts on y axis. The
	 * timestamps are unique per set of orders copied from the staging database
	 * by Spring Batch
	 * 
	 * Equivalent to query: select jobTimestamp, count(*) from orders where
	 * orderType = buy/sell group by jobTimestamp order by jobTimestamp
	 * limit(distincTimestamps)
	 * 
	 * @param type
	 *            the order type to filter (buy or sell)
	 * @param distinctTimestamps
	 *            the number of timestamps from to include starting from most
	 *            recent
	 * @return a List of key/values mapping timestamp to buy/sell counts
	 */
	List<Object[]> getOrderHistoryByType(OrderType type,
			Integer distinctTimestamps);
}
