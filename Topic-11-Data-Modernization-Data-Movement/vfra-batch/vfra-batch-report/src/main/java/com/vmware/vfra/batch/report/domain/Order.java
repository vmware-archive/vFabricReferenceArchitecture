// Copyright (c) 2013 VMware, Inc. All rights reserved.
package com.vmware.vfra.batch.report.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

import org.springframework.data.rest.repository.annotation.RestResource;

/**
 * An order mapped to reporting Order table
 */
@SuppressWarnings("serial")
@RestResource
@Entity(name="Orders")
public class Order implements Serializable {

	private @EmbeddedId Key key;
	
	@Embeddable
	public static class Key implements Serializable {
		public Integer orderId;
		public Date jobTimeStamp;
		public String toString() { return orderId + ":" + jobTimeStamp.getTime(); }
	}
	//@Id
	//private Integer orderId;
	private String quoteSymbol;
	private BigDecimal quantity;
	private BigDecimal price;
	private Integer orderType;
	private String orderStatus;
	private BigDecimal orderFee;
	private Timestamp openDate;
	private Integer holdingId;
	private Timestamp completionDate;
	private Integer accountId;
	//@Id	
	//private Date jobTimeStamp;

	public String getQuoteSymbol() {
		return quoteSymbol;
	}

	public BigDecimal getQuantity() {
		return quantity;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public Integer getOrderType() {
		return orderType;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public Integer getOrderId() {
		return key.orderId;
	}

	public BigDecimal getOrderFee() {
		return orderFee;
	}

	public Timestamp getOpenDate() {
		return openDate;
	}

	public Integer getHoldingId() {
		return holdingId;
	}

	public Timestamp getCompletionDate() {
		return completionDate;
	}

	public Integer getAccountId() {
		return accountId;
	}

	public Date getJobTimeStamp() {
		return key.jobTimeStamp;
	}	
}
