/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.billcheck.vo;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.jiuyescm.cfm.domain.IEntity;

/**
 * 
 * @author stevenl
 * 
 */
public class BillAccountInfoVo implements IEntity {

    // TODO Change serialVersionUID by eclipse
    private static final long serialVersionUID = -1;
    
	// 自增标识
	private Long id;
	// 商家id
	private String customerId;
	// 商家名称
	private String customerName;
	// 账号
	private String accountNo;
	// 账户余额
	private BigDecimal amount;
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

	public BillAccountInfoVo() {

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
     * 账号
     */
	public String getAccountNo() {
		return this.accountNo;
	}

    /**
     * 账号
     *
     * @param accountNo
     */
	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}
	
	/**
     * 账户余额
     */
	public BigDecimal getAmount() {
		return this.amount;
	}

    /**
     * 账户余额
     *
     * @param amount
     */
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
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
    
}
