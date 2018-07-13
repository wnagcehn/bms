/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.fees.bill.out.vo;

import java.sql.Timestamp;
import com.jiuyescm.cfm.domain.IEntity;

/**
 * 应付账单vo
 * @author yangshuaishuai
 */
public class FeesOutBillVo implements IEntity {
    
	private static final long serialVersionUID = -5491940887351230099L;
	
	// 账单编号
	private String billNo;
	// 账单名称
	private String billName;
	// 账单起始时间
	private Timestamp startTime;
	// 账单结束时间
	private Timestamp endTime;
	// 商家id
	private String customerid;
	// 总金额
	private Double totleprice;
	// 状态 0-未结算 1-已结算
	private String status;
	// 删除标志 0-未作废 1-作废
	private String delFlag;
	// 创建人ID
	private String creatorId;
	// 创建人姓名
	private String creator;
	// 创建时间
	private Timestamp createTime;
	// 修改人ID
	private String lastModifierId;
	// 修改人姓名
	private String lastModifier;
	// 修改时间
	private Timestamp lastModifyTime;

	public FeesOutBillVo() {

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
     * 账单起始时间
     */
	public Timestamp getStartTime() {
		return this.startTime;
	}

    /**
     * 账单起始时间
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
     * 商家id
     */
	public String getCustomerid() {
		return this.customerid;
	}

    /**
     * 商家id
     *
     * @param customerid
     */
	public void setCustomerid(String customerid) {
		this.customerid = customerid;
	}
	
	/**
     * 总金额
     */
	public Double getTotleprice() {
		return this.totleprice;
	}

    /**
     * 总金额
     *
     * @param totleprice
     */
	public void setTotleprice(Double totleprice) {
		this.totleprice = totleprice;
	}
	
	/**
     * 状态 0-未结算 1-已结算
     */
	public String getStatus() {
		return this.status;
	}

    /**
     * 状态 0-未结算 1-已结算
     *
     * @param status
     */
	public void setStatus(String status) {
		this.status = status;
	}
	
	/**
     * 删除标志 0-未作废 1-作废
     */
	public String getDelFlag() {
		return this.delFlag;
	}

    /**
     * 删除标志 0-未作废 1-作废
     *
     * @param delFlag
     */
	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}
	
	/**
     * 创建人ID
     */
	public String getCreatorId() {
		return this.creatorId;
	}

    /**
     * 创建人ID
     *
     * @param creatorId
     */
	public void setCreatorId(String creatorId) {
		this.creatorId = creatorId;
	}
	
	/**
     * 创建人姓名
     */
	public String getCreator() {
		return this.creator;
	}

    /**
     * 创建人姓名
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
     * 修改人ID
     */
	public String getLastModifierId() {
		return this.lastModifierId;
	}

    /**
     * 修改人ID
     *
     * @param lastModifierId
     */
	public void setLastModifierId(String lastModifierId) {
		this.lastModifierId = lastModifierId;
	}
	
	/**
     * 修改人姓名
     */
	public String getLastModifier() {
		return this.lastModifier;
	}

    /**
     * 修改人姓名
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
    
}
