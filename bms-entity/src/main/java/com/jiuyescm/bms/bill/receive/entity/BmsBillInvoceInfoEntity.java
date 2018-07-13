/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.bill.receive.entity;

import java.sql.Timestamp;

import com.jiuyescm.cfm.domain.IEntity;

/**
 * 发票实体
 * @author yangss
 *
 */
public class BmsBillInvoceInfoEntity implements IEntity {
    
	private static final long serialVersionUID = 7230272694411426924L;
	
	// id
	private Long id;
	// 账单编号
	private String billNo;
	// 是否需要发票(unneed:不需要，need：需要)
	private String isNeedInvoce;
	// 发票号码
	private String invoceNo;
	// 开票日期
	private Timestamp invoceTime;
	// 票据金额
	private Double invoceAmount;
	// 应收金额
	private Double receiveAmount;
	// 收款日期
	private Timestamp receiptTime;
	// 实收金额
	private Double receiptAmount;
	// 收款状态(unsettled:未收款，settled：已收款)
	private String receiptStatus;
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
	
	private String billName;
	private String customerName;
	//已开票未收款金额
	private Double unReceiveAmount;
	//未开票金额
	private Double unInvoceAmount;
	//账单实收金额
	private Double totalAmount;
	//账单折扣金额
	private Double discountAmount;
	private int version;

	public BmsBillInvoceInfoEntity() {

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
     * 是否需要发票(unneed:不需要，need：需要)
     */
	public String getIsNeedInvoce() {
		return this.isNeedInvoce;
	}

    /**
     * 是否需要发票(unneed:不需要，need：需要)
     *
     * @param isNeedInvoce
     */
	public void setIsNeedInvoce(String isNeedInvoce) {
		this.isNeedInvoce = isNeedInvoce;
	}
	
	/**
     * 发票号码
     */
	public String getInvoceNo() {
		return this.invoceNo;
	}

    /**
     * 发票号码
     *
     * @param invoceNo
     */
	public void setInvoceNo(String invoceNo) {
		this.invoceNo = invoceNo;
	}
	
	/**
     * 开票日期
     */
	public Timestamp getInvoceTime() {
		return this.invoceTime;
	}

    /**
     * 开票日期
     *
     * @param invoceTime
     */
	public void setInvoceTime(Timestamp invoceTime) {
		this.invoceTime = invoceTime;
	}
	
	/**
     * 票据金额
     */
	public Double getInvoceAmount() {
		return this.invoceAmount;
	}

    /**
     * 票据金额
     *
     * @param invoceAmount
     */
	public void setInvoceAmount(Double invoceAmount) {
		this.invoceAmount = invoceAmount;
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
     * 收款日期
     */
	public Timestamp getReceiptTime() {
		return this.receiptTime;
	}

    /**
     * 收款日期
     *
     * @param receiptTime
     */
	public void setReceiptTime(Timestamp receiptTime) {
		this.receiptTime = receiptTime;
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
     * 收款状态(unsettled:未收款，settled：已收款)
     */
	public String getReceiptStatus() {
		return this.receiptStatus;
	}

    /**
     * 收款状态(unsettled:未收款，settled：已收款)
     *
     * @param receiptStatus
     */
	public void setReceiptStatus(String receiptStatus) {
		this.receiptStatus = receiptStatus;
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

	public Double getUnReceiveAmount() {
		return unReceiveAmount;
	}

	public void setUnReceiveAmount(Double unReceiveAmount) {
		this.unReceiveAmount = unReceiveAmount;
	}

	public Double getUnInvoceAmount() {
		return unInvoceAmount;
	}

	public void setUnInvoceAmount(Double unInvoceAmount) {
		this.unInvoceAmount = unInvoceAmount;
	}

	public Double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public Double getDiscountAmount() {
		return discountAmount;
	}

	public void setDiscountAmount(Double discountAmount) {
		this.discountAmount = discountAmount;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}
    
}
