/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.fees.bill.out.entity;

import java.sql.Timestamp;
import java.util.List;

import com.jiuyescm.bms.pub.deliver.entity.DeliverEntity;
import com.jiuyescm.cfm.domain.IEntity;

/**
 * 应付账单实体
 * 
 * @author yangshuaishuai
 */
public class FeesPayBillEntity implements IEntity {

	private static final long serialVersionUID = -5319123504671527353L;

	private int id;
	// 账单编号
	private String billNo;
	// 账单名称
	private String billName;
	// 账单起始时间
	private Timestamp startTime;
	// 账单结束时间
	private Timestamp endTime;
	// 承运商ID
	private String forwarderId;
	// 承运商名称
	private String forwarderName;
	// 宅配商id
	private String deliveryid;
	// 宅配商名称
	private String deliverName;
	// 总金额
	private Double totleprice;
	// 实收金额
	private Double receiptAmount;
	// 状态 0-未结算 1-已结算
	private String status;
	// 备注
	private String remark;
	// 账单类型(1：宅配、异常，2：运输)
	private String type;
	// 创建人姓名
	private String creator;
	// 创建时间
	private Timestamp createTime;
	// 修改人姓名
	private String lastModifier;
	// 修改时间
	private Timestamp lastModifyTime;
	// 删除标志 0-未作废 1-作废
	private String delFlag;

	/**
	 * 剔除账单使用
	 */
	private String feesNo;
	
	private String deliverSelectlist;
	
	private List<DeliverEntity> deliverList;
	//商家ID
	private String customerId;

	public FeesPayBillEntity() {

	}

	/**
	 * 账单编号
	 */
	public String getBillNo() {
		return this.billNo;
	}

	/**
	 * 账单编号
	 * 
	 * @param billNo
	 */
	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}

	/**
	 * 账单名称
	 */
	public String getBillName() {
		return this.billName;
	}

	/**
	 * 账单名称
	 * 
	 * @param billName
	 */
	public void setBillName(String billName) {
		this.billName = billName;
	}

	/**
	 * 账单起始时间
	 */
	public Timestamp getStartTime() {
		return this.startTime;
	}

	/**
	 * 账单起始时间
	 * 
	 * @param startTime
	 */
	public void setStartTime(Timestamp startTime) {
		this.startTime = startTime;
	}

	/**
	 * 账单结束时间
	 */
	public Timestamp getEndTime() {
		return this.endTime;
	}

	/**
	 * 账单结束时间
	 * 
	 * @param endTime
	 */
	public void setEndTime(Timestamp endTime) {
		this.endTime = endTime;
	}

	public String getForwarderId() {
		return forwarderId;
	}

	public void setForwarderId(String forwarderId) {
		this.forwarderId = forwarderId;
	}

	public String getForwarderName() {
		return forwarderName;
	}

	public void setForwarderName(String forwarderName) {
		this.forwarderName = forwarderName;
	}

	public String getDeliveryid() {
		return deliveryid;
	}

	public void setDeliveryid(String deliveryid) {
		this.deliveryid = deliveryid;
	}

	public String getDeliverName() {
		return deliverName;
	}

	public void setDeliverName(String deliverName) {
		this.deliverName = deliverName;
	}

	/**
	 * 总金额
	 */
	public Double getTotleprice() {
		return this.totleprice;
	}

	/**
	 * 总金额
	 * 
	 * @param totleprice
	 */
	public void setTotleprice(Double totleprice) {
		this.totleprice = totleprice;
	}

	/**
	 * 实收金额
	 */
	public Double getReceiptAmount() {
		return this.receiptAmount;
	}

	/**
	 * 实收金额
	 * 
	 * @param receiptAmount
	 */
	public void setReceiptAmount(Double receiptAmount) {
		this.receiptAmount = receiptAmount;
	}

	/**
	 * 状态 0-未结算 1-已结算
	 */
	public String getStatus() {
		return this.status;
	}

	/**
	 * 状态 0-未结算 1-已结算
	 * 
	 * @param status
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * 备注
	 */
	public String getRemark() {
		return this.remark;
	}

	/**
	 * 备注
	 * 
	 * @param remark
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}

	/**
	 * 账单类型(1：宅配、异常，2：运输)
	 */
	public String getType() {
		return this.type;
	}

	/**
	 * 账单类型(1：宅配、异常，2：运输)
	 * 
	 * @param type
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * 创建人姓名
	 */
	public String getCreator() {
		return this.creator;
	}

	/**
	 * 创建人姓名
	 * 
	 * @param creator
	 */
	public void setCreator(String creator) {
		this.creator = creator;
	}

	/**
	 * 创建时间
	 */
	public Timestamp getCreateTime() {
		return this.createTime;
	}

	/**
	 * 创建时间
	 * 
	 * @param createTime
	 */
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	/**
	 * 修改人姓名
	 */
	public String getLastModifier() {
		return this.lastModifier;
	}

	/**
	 * 修改人姓名
	 * 
	 * @param lastModifier
	 */
	public void setLastModifier(String lastModifier) {
		this.lastModifier = lastModifier;
	}

	/**
	 * 修改时间
	 */
	public Timestamp getLastModifyTime() {
		return this.lastModifyTime;
	}

	/**
	 * 修改时间
	 * 
	 * @param lastModifyTime
	 */
	public void setLastModifyTime(Timestamp lastModifyTime) {
		this.lastModifyTime = lastModifyTime;
	}

	/**
	 * 删除标志 0-未作废 1-作废
	 */
	public String getDelFlag() {
		return this.delFlag;
	}

	/**
	 * 删除标志 0-未作废 1-作废
	 * 
	 * @param delFlag
	 */
	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}

	public String getFeesNo() {
		return feesNo;
	}

	public void setFeesNo(String feesNo) {
		this.feesNo = feesNo;
	}

	public String getDeliverSelectlist() {
		return deliverSelectlist;
	}

	public void setDeliverSelectlist(String deliverSelectlist) {
		this.deliverSelectlist = deliverSelectlist;
	}

	public List<DeliverEntity> getDeliverList() {
		return deliverList;
	}

	public void setDeliverList(List<DeliverEntity> deliverList) {
		this.deliverList = deliverList;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

}
