// Copyright (c) 2013 VMware, Inc. All rights reserved.
package com.vmware.vfra.batch;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

import org.springframework.jdbc.core.RowMapper;

import com.vmware.vfra.batch.domain.RptOrder;

/**
 * This mapper performs some simple transformations between staging and
 * reporting schemas/models, such as changing Date type to Timestamp, and mapping
 * orderType from String to int. Another option would be to model separate
 * staging and reporting orders and use a
 * {@link org.springframework.batch.item.ItemProcessor} to perform the
 * transformations.
 */
public class OrderRowMapper implements RowMapper<RptOrder> {

	private Long jobTimeStamp = Calendar.getInstance().getTimeInMillis();

	public Long getJobTimeStamp() {
		return jobTimeStamp;
	}

	public void setJobTimeStamp(Long jobTimeStamp) {
		this.jobTimeStamp = jobTimeStamp;
	}

	public RptOrder mapRow(ResultSet rs, int rowNum) throws SQLException {
		RptOrder order = new RptOrder();
		order.setAccountId(rs.getInt("account_accountid"));
		order.setCompletionDate(new Timestamp(rs.getDate("completiondate")
				.getTime()));
		order.setHoldingId(rs.getInt("holding_holdingid"));
		order.setOpenDate(new Timestamp(rs.getDate("opendate").getTime()));
		order.setOrderFee(rs.getBigDecimal("orderfee"));
		order.setOrderId(rs.getInt("orderid"));
		order.setOrderStatus(rs.getString("orderstatus"));
		order.setOrderType(getOrderTypeAsInt(rs.getString("ordertype")));
		order.setPrice(rs.getBigDecimal("price"));
		order.setQuantity(rs.getBigDecimal("quantity"));
		order.setQuoteSymbol(rs.getString("quote_symbol"));
		order.setJobTimeStamp(new Date(jobTimeStamp));
		return order;
	}

	static int getOrderTypeAsInt(String orderType) {
		if (orderType.equalsIgnoreCase("buy")) {
			return 1;
		} else if (orderType.equalsIgnoreCase("sell")) {
			return 2;
		} else
			throw new IllegalArgumentException("unknown orderType: "
					+ orderType);
	}
}
