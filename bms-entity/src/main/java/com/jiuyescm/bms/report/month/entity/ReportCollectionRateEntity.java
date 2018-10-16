/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.report.month.entity;

import java.util.Date;

import com.jiuyescm.cfm.domain.IEntity;

/**
 * 
 * @author stevenl
 * 
 */
public class ReportCollectionRateEntity implements IEntity {


	/**
	 * 
	 */
	private static final long serialVersionUID = 628704265363704559L;
	
	private String sellerId;
	
	private String sellerName;
	
	private String area;
	
	
	//超期未收款占比
	private Double receiptAmount;
	
	private Integer createMonth;
	
	//收款合计
	private Double receiptTotal;
	
	//收款指标;
	private Double receiptTarget;
	
	//收款达成率
	private String receiptCollectionRate;
	
	//收款日期
	private Date receiptDate;
	
	//累加后的金额
	private Double totalAmount;
	
	//区域名
	private String groupName;
	
	//启动时间一年以内收款
	private Double receiptWithinOneYear;
	//启动时间1-2年收款
	private Double receiptBetweenOneAndTwoYear;
	//启动时间超过2年的收款
	private Double receiptOverTwoYear;
	//交接客户收款
	private Double handoverCustomerReceipt;
	
	private int receiptMonth;
	
	private String customerName;
	
	//交接客户收款
	private Double newSellerReceipt;
	
	public ReportCollectionRateEntity() {

	}

	public String getSellerId() {
		return sellerId;
	}

	public void setSellerId(String sellerId) {
		this.sellerId = sellerId;
	}

	public String getSellerName() {
		return sellerName;
	}

	public void setSellerName(String sellerName) {
		this.sellerName = sellerName;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}


	public Double getReceiptAmount() {
		return receiptAmount;
	}

	public void setReceiptAmount(Double receiptAmount) {
		this.receiptAmount = receiptAmount;
	}

	public Integer getCreateMonth() {
		return createMonth;
	}

	public void setCreateMonth(Integer createMonth) {
		this.createMonth = createMonth;
	}

	public Double getReceiptTotal() {
		return receiptTotal;
	}

	public void setReceiptTotal(Double receiptTotal) {
		this.receiptTotal = receiptTotal;
	}

	public Double getReceiptTarget() {
		return receiptTarget;
	}

	public void setReceiptTarget(Double receiptTarget) {
		this.receiptTarget = receiptTarget;
	}

	public String getReceiptCollectionRate() {
		return receiptCollectionRate;
	}

	public void setReceiptCollectionRate(String receiptCollectionRate) {
		this.receiptCollectionRate = receiptCollectionRate;
	}

	public Date getReceiptDate() {
		return receiptDate;
	}

	public void setReceiptDate(Date receiptDate) {
		this.receiptDate = receiptDate;
	}

	public Double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public Double getReceiptWithinOneYear() {
		return receiptWithinOneYear;
	}

	public void setReceiptWithinOneYear(Double receiptWithinOneYear) {
		this.receiptWithinOneYear = receiptWithinOneYear;
	}

	public Double getReceiptBetweenOneAndTwoYear() {
		return receiptBetweenOneAndTwoYear;
	}

	public void setReceiptBetweenOneAndTwoYear(Double receiptBetweenOneAndTwoYear) {
		this.receiptBetweenOneAndTwoYear = receiptBetweenOneAndTwoYear;
	}

	public Double getReceiptOverTwoYear() {
		return receiptOverTwoYear;
	}

	public void setReceiptOverTwoYear(Double receiptOverTwoYear) {
		this.receiptOverTwoYear = receiptOverTwoYear;
	}

	public Double getHandoverCustomerReceipt() {
		return handoverCustomerReceipt;
	}

	public void setHandoverCustomerReceipt(Double handoverCustomerReceipt) {
		this.handoverCustomerReceipt = handoverCustomerReceipt;
	}

	public int getReceiptMonth() {
		return receiptMonth;
	}

	public void setReceiptMonth(int receiptMonth) {
		this.receiptMonth = receiptMonth;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public Double getNewSellerReceipt() {
		return newSellerReceipt;
	}

	public void setNewSellerReceipt(Double newSellerReceipt) {
		this.newSellerReceipt = newSellerReceipt;
	}

	
    
}
