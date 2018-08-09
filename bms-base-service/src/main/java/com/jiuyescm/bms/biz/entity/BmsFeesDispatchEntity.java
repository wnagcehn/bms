/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.biz.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.jiuyescm.cfm.domain.IEntity;

/**
 * 
 * @author stevenl
 * 
 */
public class BmsFeesDispatchEntity implements IEntity {

    // TODO Change serialVersionUID by eclipse
    private static final long serialVersionUID = -1;
    
	// id
	private Long id;
	// 费用科目
	private String subjectCode;
	// 业务数据唯一主键
	private String uniqCode;
	// 计费重量
	private BigDecimal chargeWeight;
	// 计费箱数
	private BigDecimal chargeBox;
	// 计费商品数量
	private BigDecimal chargeQty;
	// 计费sku数量
	private BigDecimal chargeSku;
	// 计费物流商id
	private String chargeCarrierId;
	// 计费报价ID
	private String quoteId;
	// 金额
	private BigDecimal amount;
	// 减免金额
	private BigDecimal derateAmount;
	// 业务数据
	private Timestamp createTime;
	// 写入时间
	private Timestamp writeTime;
	// 计算状态 0-未计算 1-计算成功 2-计算异常 3-不计算
	private String isCalculated;
	// 计算状态二级编码 1-合同不存在 2-报价缺失 3-数据异常 4-系统错误
	private String calcuStatus;
	// 计算说明
	private String calcuMsg;
	// 备注
	private String remark;
	// 作废标识
	private String delFlag;
	// 计算时间
	private Timestamp calcuTime;

	public BmsFeesDispatchEntity() {

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
     * 业务数据唯一主键
     */
	public String getUniqCode() {
		return this.uniqCode;
	}

    /**
     * 业务数据唯一主键
     *
     * @param uniqCode
     */
	public void setUniqCode(String uniqCode) {
		this.uniqCode = uniqCode;
	}
	
	/**
     * 计费重量
     */
	public BigDecimal getChargeWeight() {
		return this.chargeWeight;
	}

    /**
     * 计费重量
     *
     * @param chargeWeight
     */
	public void setChargeWeight(BigDecimal chargeWeight) {
		this.chargeWeight = chargeWeight;
	}
	
	/**
     * 计费箱数
     */
	public BigDecimal getChargeBox() {
		return this.chargeBox;
	}

    /**
     * 计费箱数
     *
     * @param chargeBox
     */
	public void setChargeBox(BigDecimal chargeBox) {
		this.chargeBox = chargeBox;
	}
	
	/**
     * 计费商品数量
     */
	public BigDecimal getChargeQty() {
		return this.chargeQty;
	}

    /**
     * 计费商品数量
     *
     * @param chargeQty
     */
	public void setChargeQty(BigDecimal chargeQty) {
		this.chargeQty = chargeQty;
	}
	
	/**
     * 计费sku数量
     */
	public BigDecimal getChargeSku() {
		return this.chargeSku;
	}

    /**
     * 计费sku数量
     *
     * @param chargeSku
     */
	public void setChargeSku(BigDecimal chargeSku) {
		this.chargeSku = chargeSku;
	}
	
	/**
     * 计费物流商id
     */
	public String getChargeCarrierId() {
		return this.chargeCarrierId;
	}

    /**
     * 计费物流商id
     *
     * @param chargeCarrierId
     */
	public void setChargeCarrierId(String chargeCarrierId) {
		this.chargeCarrierId = chargeCarrierId;
	}
	
	/**
     * 计费报价ID
     */
	public String getQuoteId() {
		return this.quoteId;
	}

    /**
     * 计费报价ID
     *
     * @param quoteId
     */
	public void setQuoteId(String quoteId) {
		this.quoteId = quoteId;
	}
	
	/**
     * 金额
     */
	public BigDecimal getAmount() {
		return this.amount;
	}

    /**
     * 金额
     *
     * @param amount
     */
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	
	/**
     * 减免金额
     */
	public BigDecimal getDerateAmount() {
		return this.derateAmount;
	}

    /**
     * 减免金额
     *
     * @param derateAmount
     */
	public void setDerateAmount(BigDecimal derateAmount) {
		this.derateAmount = derateAmount;
	}
	
	/**
     * 业务数据
     */
	public Timestamp getCreateTime() {
		return this.createTime;
	}

    /**
     * 业务数据
     *
     * @param createTime
     */
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	
	/**
     * 写入时间
     */
	public Timestamp getWriteTime() {
		return this.writeTime;
	}

    /**
     * 写入时间
     *
     * @param writeTime
     */
	public void setWriteTime(Timestamp writeTime) {
		this.writeTime = writeTime;
	}
	
	/**
     * 计算状态 0-未计算 1-计算成功 2-计算异常 3-不计算
     */
	public String getIsCalculated() {
		return this.isCalculated;
	}

    /**
     * 计算状态 0-未计算 1-计算成功 2-计算异常 3-不计算
     *
     * @param isCalculated
     */
	public void setIsCalculated(String isCalculated) {
		this.isCalculated = isCalculated;
	}
	
	/**
     * 计算状态二级编码 1-合同不存在 2-报价缺失 3-数据异常 4-系统错误
     */
	public String getCalcuStatus() {
		return this.calcuStatus;
	}

    /**
     * 计算状态二级编码 1-合同不存在 2-报价缺失 3-数据异常 4-系统错误
     *
     * @param calcuStatus
     */
	public void setCalcuStatus(String calcuStatus) {
		this.calcuStatus = calcuStatus;
	}
	
	/**
     * 计算说明
     */
	public String getCalcuMsg() {
		return this.calcuMsg;
	}

    /**
     * 计算说明
     *
     * @param calcuMsg
     */
	public void setCalcuMsg(String calcuMsg) {
		this.calcuMsg = calcuMsg;
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
     * 作废标识
     */
	public String getDelFlag() {
		return this.delFlag;
	}

    /**
     * 作废标识
     *
     * @param delFlag
     */
	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}
	
	/**
     * 计算时间
     */
	public Timestamp getCalcuTime() {
		return this.calcuTime;
	}

    /**
     * 计算时间
     *
     * @param calcuTime
     */
	public void setCalcuTime(Timestamp calcuTime) {
		this.calcuTime = calcuTime;
	}
    
}
