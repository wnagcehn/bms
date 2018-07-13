/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.billcheck.vo;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

import com.jiuyescm.cfm.domain.IEntity;

/**
 * 
 * @author stevenl
 * 
 */
public class BillCheckAdjustInfoVo implements IEntity {

	 // TODO Change serialVersionUID by eclipse
    private static final long serialVersionUID = -1;
    
	// id
	private Integer id;
	// 对账主表id
	private Integer billCheckId;
	// 调整日期
	private Date adjustDate;
	// 调整金额
	private BigDecimal adjustAmount;
	// CreatorId
	private String creatorId;
	// creator
	private String creator;
	// CreateTime
	private Timestamp createTime;
	// LastModifierId
	private String lastModifierId;
	// LastModifier
	private String lastModifier;
	// LastModifyTime
	private Timestamp lastModifyTime;
	// DelFlag
	private String delFlag;
	// 调整原因
	private String remark;
	
	//开票名称
	private String invoiceName;
	//业务月份
	private int createMonth;
	//账单名称
	private String billName;

	public BillCheckAdjustInfoVo() {

	}
	
	/**
     * id
     */
	public Integer getId() {
		return this.id;
	}

    /**
     * id
     *
     * @param id
     */
	public void setId(Integer id) {
		this.id = id;
	}
	
	/**
     * 对账主表id
     */
	public Integer getBillCheckId() {
		return this.billCheckId;
	}

    /**
     * 对账主表id
     *
     * @param billCheckId
     */
	public void setBillCheckId(Integer billCheckId) {
		this.billCheckId = billCheckId;
	}
	

	public Date getAdjustDate() {
		return adjustDate;
	}

	public void setAdjustDate(Date adjustDate) {
		this.adjustDate = adjustDate;
	}

	/**
     * 调整金额
     */
	public BigDecimal getAdjustAmount() {
		return this.adjustAmount;
	}

    /**
     * 调整金额
     *
     * @param adjustAmount
     */
	public void setAdjustAmount(BigDecimal adjustAmount) {
		this.adjustAmount = adjustAmount;
	}
	
	/**
     * CreatorId
     */
	public String getCreatorId() {
		return this.creatorId;
	}

    /**
     * CreatorId
     *
     * @param creatorId
     */
	public void setCreatorId(String creatorId) {
		this.creatorId = creatorId;
	}
	
	/**
     * creator
     */
	public String getCreator() {
		return this.creator;
	}

    /**
     * creator
     *
     * @param creator
     */
	public void setCreator(String creator) {
		this.creator = creator;
	}
	
	/**
     * CreateTime
     */
	public Timestamp getCreateTime() {
		return this.createTime;
	}

    /**
     * CreateTime
     *
     * @param createTime
     */
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	
	/**
     * LastModifierId
     */
	public String getLastModifierId() {
		return this.lastModifierId;
	}

    /**
     * LastModifierId
     *
     * @param lastModifierId
     */
	public void setLastModifierId(String lastModifierId) {
		this.lastModifierId = lastModifierId;
	}
	
	/**
     * LastModifier
     */
	public String getLastModifier() {
		return this.lastModifier;
	}

    /**
     * LastModifier
     *
     * @param lastModifier
     */
	public void setLastModifier(String lastModifier) {
		this.lastModifier = lastModifier;
	}
	
	/**
     * LastModifyTime
     */
	public Timestamp getLastModifyTime() {
		return this.lastModifyTime;
	}

    /**
     * LastModifyTime
     *
     * @param lastModifyTime
     */
	public void setLastModifyTime(Timestamp lastModifyTime) {
		this.lastModifyTime = lastModifyTime;
	}
	
	/**
     * DelFlag
     */
	public String getDelFlag() {
		return this.delFlag;
	}

    /**
     * DelFlag
     *
     * @param delFlag
     */
	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}
	
	/**
     * 调整原因
     */
	public String getRemark() {
		return this.remark;
	}

    /**
     * 调整原因
     *
     * @param remark
     */
	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getInvoiceName() {
		return invoiceName;
	}

	public void setInvoiceName(String invoiceName) {
		this.invoiceName = invoiceName;
	}

	public int getCreateMonth() {
		return createMonth;
	}

	public void setCreateMonth(int createMonth) {
		this.createMonth = createMonth;
	}

	public String getBillName() {
		return billName;
	}

	public void setBillName(String billName) {
		this.billName = billName;
	}
	
}
