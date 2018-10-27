/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.billcheck;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.jiuyescm.cfm.domain.IEntity;

/**
 * 
 * @author stevenl
 * 
 */
public class BillAccountInEntity implements IEntity {

    // TODO Change serialVersionUID by eclipse
    private static final long serialVersionUID = -1;
    
	// 自增标识
	private Long id;
	// 商家id
	private String customerId;
	// 商家名称
	private String customerName;
	// 交易方式
	private String tradeType;
	// 交易摘要
	private String tradeDescrip;
	// 录入金额
	private BigDecimal amount;
	// 确认时间/入账时间
	private Timestamp confirmTime;
	// 确认状态 0-未确认 1-已确认
	private String confirmStatus;
	// 创建者ID
	private String creatorId;
	// 创建者
	private String creator;
	// 创建时间
	private Timestamp createTime;
	// 修改者id
	private String lastModifierId;
	// 修改者
	private String lastModifier;
	// 修改时间
	private Timestamp lastModifyTime;
	// 作废标识 0-未作废 1-已作废
	private String delFlag;
	// 备注
	private String remark;

	public BillAccountInEntity() {

	}
	
	/**
     * 自增标识
     */
	public Long getId() {
		return this.id;
	}

    /**
     * 自增标识
     *
     * @param id
     */
	public void setId(Long id) {
		this.id = id;
	}
	
	/**
     * 商家id
     */
	public String getCustomerId() {
		return this.customerId;
	}

    /**
     * 商家id
     *
     * @param customerId
     */
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	
	/**
     * 商家名称
     */
	public String getCustomerName() {
		return this.customerName;
	}

    /**
     * 商家名称
     *
     * @param customerName
     */
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	
	/**
     * 交易方式
     */
	public String getTradeType() {
		return this.tradeType;
	}

    /**
     * 交易方式
     *
     * @param tradeType
     */
	public void setTradeType(String tradeType) {
		this.tradeType = tradeType;
	}
	
	/**
     * 交易摘要
     */
	public String getTradeDescrip() {
		return this.tradeDescrip;
	}

    /**
     * 交易摘要
     *
     * @param tradeDescrip
     */
	public void setTradeDescrip(String tradeDescrip) {
		this.tradeDescrip = tradeDescrip;
	}
	
	/**
     * 录入金额
     */
	public BigDecimal getAmount() {
		return this.amount;
	}

    /**
     * 录入金额
     *
     * @param amount
     */
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	
	/**
     * 确认时间/入账时间
     */
	public Timestamp getConfirmTime() {
		return this.confirmTime;
	}

    /**
     * 确认时间/入账时间
     *
     * @param confirmTime
     */
	public void setConfirmTime(Timestamp confirmTime) {
		this.confirmTime = confirmTime;
	}
	
	/**
     * 确认状态 0-未确认 1-已确认
     */
	public String getConfirmStatus() {
		return this.confirmStatus;
	}

    /**
     * 确认状态 0-未确认 1-已确认
     *
     * @param confirmStatus
     */
	public void setConfirmStatus(String confirmStatus) {
		this.confirmStatus = confirmStatus;
	}
	
	/**
     * 创建者ID
     */
	public String getCreatorId() {
		return this.creatorId;
	}

    /**
     * 创建者ID
     *
     * @param creatorId
     */
	public void setCreatorId(String creatorId) {
		this.creatorId = creatorId;
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
     * 修改者id
     */
	public String getLastModifierId() {
		return this.lastModifierId;
	}

    /**
     * 修改者id
     *
     * @param lastModifierId
     */
	public void setLastModifierId(String lastModifierId) {
		this.lastModifierId = lastModifierId;
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
     * 作废标识 0-未作废 1-已作废
     */
	public String getDelFlag() {
		return this.delFlag;
	}

    /**
     * 作废标识 0-未作废 1-已作废
     *
     * @param delFlag
     */
	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
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
    
}
