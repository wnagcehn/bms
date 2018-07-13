/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.common.log.entity;

import java.sql.Timestamp;

import com.jiuyescm.cfm.domain.IEntity;



/**
 * 
 * @author stevenl
 * 
 */
public class BmsBillLogRecordEntity implements IEntity {

	private static final long serialVersionUID = 5641188297452375621L;
	
	// 主键ID
	private Long id;
	// 账单编号
	private String billNo;
	// 账单名称
	private String billName;
	// 操作
	private String operate;
	// 审批人
	private String approver;
	// 审批方式
	private String approvalWay;
	// 审批时间
	private Timestamp approvalTime;
	// 发票编号
	private String invoceNo;
	// 开票金额
	private Double invoceAmount;
	// 收款金额(元)
	private Double receiptAmount;
	// 备注（此处可用来存储操作异常信息）
	private String remark;
	// 操作人
	private String creator;
	// 创建时间
	private Timestamp createTime;

	public BmsBillLogRecordEntity() {

	}
	
	/**
     * 主键ID
     */
	public Long getId() {
		return this.id;
	}

    /**
     * 主键ID
     *
     * @param id
     */
	public void setId(Long id) {
		this.id = id;
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
	
	public String getOperate() {
		return operate;
	}

	public void setOperate(String operate) {
		this.operate = operate;
	}

	/**
     * 审批人
     */
	public String getApprover() {
		return this.approver;
	}

    /**
     * 审批人
     *
     * @param approver
     */
	public void setApprover(String approver) {
		this.approver = approver;
	}
	
	/**
     * 审批方式
     */
	public String getApprovalWay() {
		return this.approvalWay;
	}

    /**
     * 审批方式
     *
     * @param approvalWay
     */
	public void setApprovalWay(String approvalWay) {
		this.approvalWay = approvalWay;
	}
	
	/**
     * 审批时间
     */
	public Timestamp getApprovalTime() {
		return this.approvalTime;
	}

    /**
     * 审批时间
     *
     * @param approvalTime
     */
	public void setApprovalTime(Timestamp approvalTime) {
		this.approvalTime = approvalTime;
	}
	
	/**
     * 发票编号
     */
	public String getInvoceNo() {
		return this.invoceNo;
	}

    /**
     * 发票编号
     *
     * @param invoceNo
     */
	public void setInvoceNo(String invoceNo) {
		this.invoceNo = invoceNo;
	}
	
	/**
     * 开票金额
     */
	public Double getInvoceAmount() {
		return this.invoceAmount;
	}

    /**
     * 开票金额
     *
     * @param invoceAmount
     */
	public void setInvoceAmount(Double invoceAmount) {
		this.invoceAmount = invoceAmount;
	}
	
	/**
     * 收款金额(元)
     */
	public Double getReceiptAmount() {
		return this.receiptAmount;
	}

    /**
     * 收款金额(元)
     *
     * @param receiptAmount
     */
	public void setReceiptAmount(Double receiptAmount) {
		this.receiptAmount = receiptAmount;
	}
	
	/**
     * 备注（此处可用来存储操作异常信息）
     */
	public String getRemark() {
		return this.remark;
	}

    /**
     * 备注（此处可用来存储操作异常信息）
     *
     * @param remark
     */
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	/**
     * 操作人
     */
	public String getCreator() {
		return this.creator;
	}

    /**
     * 操作人
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
    
}
