/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.entity.entity;

import java.sql.Timestamp;
import com.jiuyescm.cfm.domain.IEntity;

/**
 * 
 * @author stevenl
 * 
 */
public class BillAccountOutEntity implements IEntity {

    // TODO Change serialVersionUID by eclipse
    private static final long serialVersionUID = -1;
    
	// 自增标识
	private Long id;
	// 账单编号
	private String accountNo;
	// 出账类型 1-账单扣款 2-其他扣款
	private String outType;
	// 出账金额
	private BigDecimal amount;
	// 创建者ID
	private String creatorId;
	// 创建者
	private String creator;
	// 创建时间
	private Timestamp createTime;
	// 作废标识 0-未作废 1-已作废
	private String delFlag;

	public BillAccountOutEntity() {

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
     * 账单编号
     */
	public String getAccountNo() {
		return this.accountNo;
	}

    /**
     * 账单编号
     *
     * @param accountNo
     */
	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}
	
	/**
     * 出账类型 1-账单扣款 2-其他扣款
     */
	public String getOutType() {
		return this.outType;
	}

    /**
     * 出账类型 1-账单扣款 2-其他扣款
     *
     * @param outType
     */
	public void setOutType(String outType) {
		this.outType = outType;
	}
	
	/**
     * 出账金额
     */
	public BigDecimal getAmount() {
		return this.amount;
	}

    /**
     * 出账金额
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
