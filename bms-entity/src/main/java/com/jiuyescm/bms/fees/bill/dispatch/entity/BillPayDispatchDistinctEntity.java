/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.fees.bill.dispatch.entity;

import java.sql.Timestamp;

import com.jiuyescm.cfm.domain.IEntity;

/**
 * 应付账单宅配对账差异
 * @author yangshuaishuai
 */
public class BillPayDispatchDistinctEntity implements IEntity {

	private static final long serialVersionUID = 5887469190525013608L;
	
	// id
	private Long id;
	// 运单号
	private String waybillNo;
	// 宅配商
	private String deliveryid;
	// 费用编号
	private String feesNo;
	// 账单编号
	private String billNo;
	// 首重重量
	private Double headWeight;
	// 首重价格
	private Double headPrice;
	// 续重重量
	private Double continuedWeight;
	// 续重价格
	private Double continuedPrice;
	// 计费重量
	private Double chargedWeight;
	// 总重量
	private Double totalWeight;
	// 重量界限
	private Double weightLimit;
	// 单价
	private Double unitPrice;
	// 金额
	private Double amount;
	// 差额
	private Double diffAmount;
	// 状态
	private String status;
	// 创建者
	private String creator;
	// 创建时间
	private Timestamp createTime;
	// 修改者
	private String lastModifier;
	// 修改时间
	private Timestamp lastModifyTime;
	// 删除标志
	private String delFlag;

	public BillPayDispatchDistinctEntity() {

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
     * 运单号
     */
	public String getWaybillNo() {
		return this.waybillNo;
	}

    /**
     * 运单号
     *
     * @param waybillNo
     */
	public void setWaybillNo(String waybillNo) {
		this.waybillNo = waybillNo;
	}
	
	/**
     * 宅配商
     */
	public String getDeliveryid() {
		return this.deliveryid;
	}

    /**
     * 宅配商
     *
     * @param deliveryid
     */
	public void setDeliveryid(String deliveryid) {
		this.deliveryid = deliveryid;
	}
	
	/**
     * 费用编号
     */
	public String getFeesNo() {
		return this.feesNo;
	}

    /**
     * 费用编号
     *
     * @param feesNo
     */
	public void setFeesNo(String feesNo) {
		this.feesNo = feesNo;
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
     * 首重重量
     */
	public Double getHeadWeight() {
		return this.headWeight;
	}

    /**
     * 首重重量
     *
     * @param headWeight
     */
	public void setHeadWeight(Double headWeight) {
		this.headWeight = headWeight;
	}
	
	/**
     * 首重价格
     */
	public Double getHeadPrice() {
		return this.headPrice;
	}

    /**
     * 首重价格
     *
     * @param headPrice
     */
	public void setHeadPrice(Double headPrice) {
		this.headPrice = headPrice;
	}
	
	/**
     * 续重重量
     */
	public Double getContinuedWeight() {
		return this.continuedWeight;
	}

    /**
     * 续重重量
     *
     * @param continuedWeight
     */
	public void setContinuedWeight(Double continuedWeight) {
		this.continuedWeight = continuedWeight;
	}
	
	/**
     * 续重价格
     */
	public Double getContinuedPrice() {
		return this.continuedPrice;
	}

    /**
     * 续重价格
     *
     * @param continuedPrice
     */
	public void setContinuedPrice(Double continuedPrice) {
		this.continuedPrice = continuedPrice;
	}
	
	/**
     * 计费重量
     */
	public Double getChargedWeight() {
		return this.chargedWeight;
	}

    /**
     * 计费重量
     *
     * @param chargedWeight
     */
	public void setChargedWeight(Double chargedWeight) {
		this.chargedWeight = chargedWeight;
	}
	
	/**
     * 总重量
     */
	public Double getTotalWeight() {
		return this.totalWeight;
	}

    /**
     * 总重量
     *
     * @param totalWeight
     */
	public void setTotalWeight(Double totalWeight) {
		this.totalWeight = totalWeight;
	}
	
	/**
     * 重量界限
     */
	public Double getWeightLimit() {
		return this.weightLimit;
	}

    /**
     * 重量界限
     *
     * @param weightLimit
     */
	public void setWeightLimit(Double weightLimit) {
		this.weightLimit = weightLimit;
	}
	
	/**
     * 单价
     */
	public Double getUnitPrice() {
		return this.unitPrice;
	}

    /**
     * 单价
     *
     * @param unitPrice
     */
	public void setUnitPrice(Double unitPrice) {
		this.unitPrice = unitPrice;
	}
	
	/**
     * 金额
     */
	public Double getAmount() {
		return this.amount;
	}

    /**
     * 金额
     *
     * @param amount
     */
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	
	/**
     * 差额
     */
	public Double getDiffAmount() {
		return this.diffAmount;
	}

    /**
     * 差额
     *
     * @param diffAmount
     */
	public void setDiffAmount(Double diffAmount) {
		this.diffAmount = diffAmount;
	}
	
	/**
     * 状态
     */
	public String getStatus() {
		return this.status;
	}

    /**
     * 状态
     *
     * @param status
     */
	public void setStatus(String status) {
		this.status = status;
	}
	
	/**
     * 创建者
     */
	public String getCreator() {
		return this.creator;
	}

    /**
     * 创建者
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
     * 修改者
     */
	public String getLastModifier() {
		return this.lastModifier;
	}

    /**
     * 修改者
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
     * 删除标志
     */
	public String getDelFlag() {
		return this.delFlag;
	}

    /**
     * 删除标志
     *
     * @param delFlag
     */
	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}
    
}
