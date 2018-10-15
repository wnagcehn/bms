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
public class ReportOverdueUnaccountEntity implements IEntity {


	/**
	 * 
	 */
	private static final long serialVersionUID = 628704265363704559L;
	
	private String sellerId;
	
	private String sellerName;
	
	private String area;
	
	//超期未收款金额
	private Double overdueUnaccount;
	
	//应收款金额
	private Double receiveAccount;
	
	//超期未收款占比
	private String overdueUnaccountRatio;
	
	private Integer createMonth;
	
	//未收款金额
	private Double unReceiptAmount;
	//收款金额
	private Double receiptAmount;
	
	//收款日期
	private Date receiptDate;
	
	//累加后的金额
	private Double totalAmount;
	
	//区域名
	private String groupName;
	
	public ReportOverdueUnaccountEntity() {

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


	public Double getOverdueUnaccount() {
		return overdueUnaccount;
	}


	public void setOverdueUnaccount(Double overdueUnaccount) {
		this.overdueUnaccount = overdueUnaccount;
	}


	public Double getReceiveAccount() {
		return receiveAccount;
	}


	public void setReceiveAccount(Double receiveAccount) {
		this.receiveAccount = receiveAccount;
	}


	public String getOverdueUnaccountRatio() {
		return overdueUnaccountRatio;
	}


	public void setOverdueUnaccountRatio(String overdueUnaccountRatio) {
		this.overdueUnaccountRatio = overdueUnaccountRatio;
	}


	public Integer getCreateMonth() {
		return createMonth;
	}


	public void setCreateMonth(Integer createMonth) {
		this.createMonth = createMonth;
	}


	public Double getUnReceiptAmount() {
		return unReceiptAmount;
	}


	public void setUnReceiptAmount(Double unReceiptAmount) {
		this.unReceiptAmount = unReceiptAmount;
	}


	public Double getReceiptAmount() {
		return receiptAmount;
	}


	public void setReceiptAmount(Double receiptAmount) {
		this.receiptAmount = receiptAmount;
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
	
	
    
}
