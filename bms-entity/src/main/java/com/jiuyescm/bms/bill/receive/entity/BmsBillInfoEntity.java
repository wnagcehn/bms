/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.bill.receive.entity;

import java.sql.Timestamp;

import com.jiuyescm.cfm.domain.IEntity;

/**
 * 
 * @author stevenl
 * 
 */
public class BmsBillInfoEntity implements IEntity {

    // TODO Change serialVersionUID by eclipse
    private static final long serialVersionUID = -1;
    
	// id
	private Long id;
	// 账单编号
	private String billNo;
	// 账单名称
	private String billName;
	// 商家ID
	private String customerId;
	// 商家简称
	private String customerName;
	// 账单开始时间
	private Timestamp startTime;
	// 账单结束时间
	private Timestamp endTime;
	// 总金额
	private Double totalAmount;
	// 折扣方式 金额,折扣率
	private String discountType;
	// 账单折扣率
	private Double discountRate;
	// 账单折扣金额
	private Double discountAmount;
	// 费用科目折扣金额
	private Double subjectDiscountAmount;
	// 减免金额
	private Double derateAmount;
	// 费用折扣金额
	private Double feesDiscountAmount;
	// 应收金额
	private Double receiveAmount;
	// 实收金额
	private Double receiptAmount;
	// 状态（unconfirmed：未确认，confirmed：已确认，part_invoiced：部分开票，invoiced：已开票，part_settled：部分结账，settled：已结账）
	private String status;
	// 审批人
	private String approver;
	// 审批方式
	private String approvalWay;
	// 审批编号
	private String approvalNo;
	// 审批时间
	private Timestamp approvalTime;
	// 邮箱
	private String email;
	// 备注
	private String remark;
	// 参数1
	private String param1;
	// 参数2
	private String param2;
	// 参数3
	private String param3;
	// 参数4
	private String param4;
	// 参数5
	private String param5;
	// 版本号
	private int version;
	// 创建人
	private String creator;
	// 创建时间
	private Timestamp createTime;
	// 修改人
	private String lastModifier;
	// 修改时间
	private Timestamp lastModifyTime;
	// 删除标志（0：未删除，1：已删除）
	private String delFlag;
	// 项目ID
	private String  projectId;
	
	private String  projectName;

	public BmsBillInfoEntity() {

	}
	
	/**
     * id
     */
	public Long getId() {
		return this.id;
	}

    /**
     * id
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
	
	/**
     * 商家ID
     */
	public String getCustomerId() {
		return this.customerId;
	}

    /**
     * 商家ID
     *
     * @param customerId
     */
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	
	/**
     * 商家简称
     */
	public String getCustomerName() {
		return this.customerName;
	}

    /**
     * 商家简称
     *
     * @param customerName
     */
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	
	/**
     * 账单开始时间
     */
	public Timestamp getStartTime() {
		return this.startTime;
	}

    /**
     * 账单开始时间
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
	
	/**
     * 总金额
     */
	public Double getTotalAmount() {
		return this.totalAmount;
	}

    /**
     * 总金额
     *
     * @param totalAmount
     */
	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}
	
	/**
     * 折扣率
     */
	public Double getDiscountRate() {
		return this.discountRate;
	}

    /**
     * 折扣率
     *
     * @param discountRate
     */
	public void setDiscountRate(Double discountRate) {
		this.discountRate = discountRate;
	}
	
	/**
     * 折扣金额
     */
	public Double getDiscountAmount() {
		return this.discountAmount;
	}

    /**
     * 折扣金额
     *
     * @param discountAmount
     */
	public void setDiscountAmount(Double discountAmount) {
		this.discountAmount = discountAmount;
	}
	
	public Double getDerateAmount() {
		return derateAmount;
	}

	public void setDerateAmount(Double derateAmount) {
		this.derateAmount = derateAmount;
	}

	public Double getFeesDiscountAmount() {
		return feesDiscountAmount;
	}

	public void setFeesDiscountAmount(Double feesDiscountAmount) {
		this.feesDiscountAmount = feesDiscountAmount;
	}

	/**
     * 应收金额
     */
	public Double getReceiveAmount() {
		return this.receiveAmount;
	}

    /**
     * 应收金额
     *
     * @param receiveAmount
     */
	public void setReceiveAmount(Double receiveAmount) {
		this.receiveAmount = receiveAmount;
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
     * 状态（unconfirmed：未确认，confirmed：已确认，part_invoiced：部分开票，invoiced：已开票，part_settled：部分结账，settled：已结账）
     */
	public String getStatus() {
		return this.status;
	}

    /**
     * 状态（unconfirmed：未确认，confirmed：已确认，part_invoiced：部分开票，invoiced：已开票，part_settled：部分结账，settled：已结账）
     *
     * @param status
     */
	public void setStatus(String status) {
		this.status = status;
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
     * 审批编号
     */
	public String getApprovalNo() {
		return this.approvalNo;
	}

    /**
     * 审批编号
     *
     * @param approvalNo
     */
	public void setApprovalNo(String approvalNo) {
		this.approvalNo = approvalNo;
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
     * 邮箱
     */
	public String getEmail() {
		return this.email;
	}

    /**
     * 邮箱
     *
     * @param email
     */
	public void setEmail(String email) {
		this.email = email;
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
     * 参数1
     */
	public String getParam1() {
		return this.param1;
	}

    /**
     * 参数1
     *
     * @param param1
     */
	public void setParam1(String param1) {
		this.param1 = param1;
	}
	
	/**
     * 参数2
     */
	public String getParam2() {
		return this.param2;
	}

    /**
     * 参数2
     *
     * @param param2
     */
	public void setParam2(String param2) {
		this.param2 = param2;
	}
	
	/**
     * 参数3
     */
	public String getParam3() {
		return this.param3;
	}

    /**
     * 参数3
     *
     * @param param3
     */
	public void setParam3(String param3) {
		this.param3 = param3;
	}
	
	/**
     * 参数4
     */
	public String getParam4() {
		return this.param4;
	}

    /**
     * 参数4
     *
     * @param param4
     */
	public void setParam4(String param4) {
		this.param4 = param4;
	}
	
	/**
     * 参数5
     */
	public String getParam5() {
		return this.param5;
	}

    /**
     * 参数5
     *
     * @param param5
     */
	public void setParam5(String param5) {
		this.param5 = param5;
	}
	
	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	/**
     * 创建人
     */
	public String getCreator() {
		return this.creator;
	}

    /**
     * 创建人
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
     * 修改人
     */
	public String getLastModifier() {
		return this.lastModifier;
	}

    /**
     * 修改人
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
     * 删除标志（0：未删除，1：已删除）
     */
	public String getDelFlag() {
		return this.delFlag;
	}

    /**
     * 删除标志（0：未删除，1：已删除）
     *
     * @param delFlag
     */
	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	public String getDiscountType() {
		return discountType;
	}

	public void setDiscountType(String discountType) {
		this.discountType = discountType;
	}

	public Double getSubjectDiscountAmount() {
		return subjectDiscountAmount;
	}

	public void setSubjectDiscountAmount(Double subjectDiscountAmount) {
		this.subjectDiscountAmount = subjectDiscountAmount;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
    
}
