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
public class BmsBillSubjectInfoEntity implements IEntity {

    // TODO Change serialVersionUID by eclipse
    private static final long serialVersionUID = -1;
    
	// id
	private Long id;
	//账单月份
	private String billMonth;
	// 账单编号
	private String billNo;
	// 仓库ID
	private String warehouseCode;
	// 费用科目
	private String subjectCode;
	// 费用名称
	private String subjectName;
	// 费用类型 仓储/配送/异常
	private String feesType;
	// 总金额
	private Double totalAmount;
	//折扣方式
	private String discountType;
	// 折扣率
	private Double discountRate;
	// 折扣金额
	private Double discountAmount;
	// 实收金额
	private Double receiptAmount;
	// 数量（订单数，托数等）
	private Double num;
	// 状态（ungenerated:未生成，generated:已生成）
	private String status;
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
	
	private String billName;//账单名称
	private String customerId;//商家Id
	private String customerName;//商家名称
	private Double derateAmount;//减免费用
	private String reasonId;//理赔原因ID

	public BmsBillSubjectInfoEntity() {

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
     * 费用科目
     */
	public String getSubjectCode() {
		return this.subjectCode;
	}

    /**
     * 费用科目
     *
     * @param subjectCode
     */
	public void setSubjectCode(String subjectCode) {
		this.subjectCode = subjectCode;
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
     * 数量（订单数，托数等）
     */
	public Double getNum() {
		return this.num;
	}

    /**
     * 数量（订单数，托数等）
     *
     * @param num
     */
	public void setNum(Double num) {
		this.num = num;
	}
	
	/**
     * 状态（ungenerated:未生成，generated:已生成）
     */
	public String getStatus() {
		return this.status;
	}

    /**
     * 状态（ungenerated:未生成，generated:已生成）
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

	public String getBillName() {
		return billName;
	}

	public void setBillName(String billName) {
		this.billName = billName;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getSubjectName() {
		return subjectName;
	}

	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}

	public String getWarehouseCode() {
		return warehouseCode;
	}

	public void setWarehouseCode(String warehouseCode) {
		this.warehouseCode = warehouseCode;
	}

	public Double getDerateAmount() {
		return derateAmount;
	}

	public void setDerateAmount(Double derateAmount) {
		this.derateAmount = derateAmount;
	}


	public String getFeesType() {
		return feesType;
	}

	public void setFeesType(String feesType) {
		this.feesType = feesType;
	}

	public String getReasonId() {
		return reasonId;
	}

	public void setReasonId(String reasonId) {
		this.reasonId = reasonId;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getDiscountType() {
		return discountType;
	}

	public void setDiscountType(String discountType) {
		this.discountType = discountType;
	}

	public String getBillMonth() {
		return billMonth;
	}

	public void setBillMonth(String billMonth) {
		this.billMonth = billMonth;
	}
    
	
	
}
