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
public class BmsFeesStorageDiscountEntity implements IEntity {

    // TODO Change serialVersionUID by eclipse
    private static final long serialVersionUID = -1;
    
	// 自增标识
	private Long id;
	// 任务ID
	private String taskId;
	// 费用科目
	private String subjectCode;
	// 业务数据唯一主键
	private String uniqCode;
	// 折扣后金额
	private BigDecimal discountAmount;
	// 减免金额
	private BigDecimal derateAmount;
	// 报价id
	private Long quoteId;
	// 计算状态 0-未计算 1-计算成功 2-计算异常 3-不计算
	private String isCalculated;
	// 计算状态二级编码 1-合同不存在 2-报价缺失 3-数据异常 4-系统错误
	private String calcuStatus;
	// 计算说明
	private String calcuMsg;
	// 计算时间
	private Timestamp calcuTime;
	// 写入时间
	private Timestamp writeTime;

	public BmsFeesStorageDiscountEntity() {

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
     * 任务ID
     */
	public String getTaskId() {
		return this.taskId;
	}

    /**
     * 任务ID
     *
     * @param taskId
     */
	public void setTaskId(String taskId) {
		this.taskId = taskId;
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
     * 折扣后金额
     */
	public BigDecimal getDiscountAmount() {
		return this.discountAmount;
	}

    /**
     * 折扣后金额
     *
     * @param discountAmount
     */
	public void setDiscountAmount(BigDecimal discountAmount) {
		this.discountAmount = discountAmount;
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
     * 报价id
     */
	public Long getQuoteId() {
		return this.quoteId;
	}

    /**
     * 报价id
     *
     * @param quoteId
     */
	public void setQuoteId(Long quoteId) {
		this.quoteId = quoteId;
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
    
}
