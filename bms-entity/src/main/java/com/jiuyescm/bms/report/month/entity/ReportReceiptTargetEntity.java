/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.report.month.entity;

import com.jiuyescm.cfm.domain.IEntity;

/**
 * 
 * @author stevenl
 * 
 */
public class ReportReceiptTargetEntity implements IEntity {


	/**
	 * 
	 */
	private static final long serialVersionUID = -1;
	
	//销售员Id
	private String sellerId;
	
	//销售员
	private String sellerName;
	
	//区域
	private String area;
	
	//未收款（2个月以上）
	private Double unReceipt2;
	
	//未收款（1到2个月）
	private Double unReceipt1To2;
	
	//未收款（1个月）
	private Double unReceipt1;
	
	//业绩 （2个月以上）
	private Double yeji2;
	
	//业绩  （1到2个月）
	private Double yeji1To2;
	
	//业绩   （1个月）
	private Double yeji1;
	
	//收款指标
	private Double receiptTarget;
	
	//指标日期
	private int createMonth;
	
	//作废标识
	private String delFlag;

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

	public Double getUnReceipt2() {
		return unReceipt2;
	}

	public void setUnReceipt2(Double unReceipt2) {
		this.unReceipt2 = unReceipt2;
	}

	public Double getUnReceipt1To2() {
		return unReceipt1To2;
	}

	public void setUnReceipt1To2(Double unReceipt1To2) {
		this.unReceipt1To2 = unReceipt1To2;
	}

	public Double getUnReceipt1() {
		return unReceipt1;
	}

	public void setUnReceipt1(Double unReceipt1) {
		this.unReceipt1 = unReceipt1;
	}

	public Double getYeji2() {
		return yeji2;
	}

	public void setYeji2(Double yeji2) {
		this.yeji2 = yeji2;
	}

	public Double getYeji1To2() {
		return yeji1To2;
	}

	public void setYeji1To2(Double yeji1To2) {
		this.yeji1To2 = yeji1To2;
	}

	public Double getYeji1() {
		return yeji1;
	}

	public void setYeji1(Double yeji1) {
		this.yeji1 = yeji1;
	}

	public Double getReceiptTarget() {
		return receiptTarget;
	}

	public void setReceiptTarget(Double receiptTarget) {
		this.receiptTarget = receiptTarget;
	}

	public int getCreateMonth() {
		return createMonth;
	}

	public void setCreateMonth(int createMonth) {
		this.createMonth = createMonth;
	}

	public String getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}  
	
	
}
