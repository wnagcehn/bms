package com.jiuyescm.bms.discount;

import java.io.Serializable;

public class BmsDiscountAccountEntity implements Serializable{
	
	private static final long serialVersionUID = 664206307229147587L;
	
	private String customerId;
	
	private String carrierId;
	
	private String orderCount;
	
	private String amount;

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getCarrierId() {
		return carrierId;
	}

	public void setCarrierId(String carrierId) {
		this.carrierId = carrierId;
	}

	public String getOrderCount() {
		return orderCount;
	}

	public void setOrderCount(String orderCount) {
		this.orderCount = orderCount;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}
	
	
}