// Copyright (c) 2013 VMware, Inc. All rights reserved.

package com.vmware.vfra.batch.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

/**
 * An order mapped to reporting schema where field names and types are slightly
 * different from staging
 */
@SuppressWarnings("serial")
public class RptOrder implements Serializable {

	private String quoteSymbol;
	private BigDecimal quantity;
	private BigDecimal price;
	private Integer orderType;
	private String orderStatus;
	private Integer orderId;
	private BigDecimal orderFee;
	private Timestamp openDate;
	private Integer holdingId;
	private Timestamp completionDate;
	private Integer accountId;
	private Date jobTimeStamp;

	public String getQuoteSymbol() {
		return quoteSymbol;
	}

	public void setQuoteSymbol(String quoteSymbol) {
		this.quoteSymbol = quoteSymbol;
	}

	public BigDecimal getQuantity() {
		return quantity;
	}

	public void setQuantity(BigDecimal quantity) {
		this.quantity = quantity;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public Integer getOrderType() {
		return orderType;
	}

	public void setOrderType(Integer orderType) {
		this.orderType = orderType;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public BigDecimal getOrderFee() {
		return orderFee;
	}

	public void setOrderFee(BigDecimal orderFee) {
		this.orderFee = orderFee;
	}

	public Timestamp getOpenDate() {
		return openDate;
	}

	public void setOpenDate(Timestamp openDate) {
		this.openDate = openDate;
	}

	public Integer getHoldingId() {
		return holdingId;
	}

	public void setHoldingId(Integer holdingId) {
		this.holdingId = holdingId;
	}

	public Timestamp getCompletionDate() {
		return completionDate;
	}

	public void setCompletionDate(Timestamp completionDate) {
		this.completionDate = completionDate;
	}

	public Integer getAccountId() {
		return accountId;
	}

	public void setAccountId(Integer accountId) {
		this.accountId = accountId;
	}

	public Date getJobTimeStamp() {
		return jobTimeStamp;
	}

	public void setJobTimeStamp(Date jobTimeStamp) {
		this.jobTimeStamp = jobTimeStamp;
	}

}
