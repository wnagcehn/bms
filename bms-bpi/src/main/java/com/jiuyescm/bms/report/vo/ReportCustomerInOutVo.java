/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.report.vo;

import java.sql.Timestamp;

import com.jiuyescm.cfm.domain.IEntity;

/**
 * 
 * @author stevenl
 * 
 */
public class ReportCustomerInOutVo implements IEntity {

    // TODO Change serialVersionUID by eclipse
    private static final long serialVersionUID = -1;
    
	// 自增标识
	private Integer id;
	// 统计年份
	private Integer createYear;
	// 统计月份
	private Integer createMonth;
	// 商家id
	private String customerId;
	// 商家全称
	private String customerName;
	
	private String shortName;
	
	//开票名称
	private String mkInvoiceName;
	
	// 上月库存 0-无库存 1-有库存
	private Integer lastMonthStorage;
	// 本月库存 0-无库存 1-有库存
	private Integer curMonthStorage;
	// 最早入仓时间
	private String minInTime;
	// 最晚出仓时间
	private String maxOutTime;
	// 计算状态  0-未计算（默认） 1-已计算 2-计算异常
	private String isCalculated;
	// 计算时间
	private Timestamp calculateTime;
	// 统计时间
	private Timestamp writeTime;

	public ReportCustomerInOutVo() {

	}
	
	/**
     * 自增标识
     */
	public Integer getId() {
		return this.id;
	}

    /**
     * 自增标识
     *
     * @param id
     */
	public void setId(Integer id) {
		this.id = id;
	}
	
	/**
     * 统计年份
     */
	public Integer getCreateYear() {
		return this.createYear;
	}

    /**
     * 统计年份
     *
     * @param createYear
     */
	public void setCreateYear(Integer createYear) {
		this.createYear = createYear;
	}
	
	/**
     * 统计月份
     */
	public Integer getCreateMonth() {
		return this.createMonth;
	}

    /**
     * 统计月份
     *
     * @param createMonth
     */
	public void setCreateMonth(Integer createMonth) {
		this.createMonth = createMonth;
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
     * 上月库存 0-无库存 1-有库存
     */
	public Integer getLastMonthStorage() {
		return this.lastMonthStorage;
	}

    /**
     * 上月库存 0-无库存 1-有库存
     *
     * @param lastMonthStorage
     */
	public void setLastMonthStorage(Integer lastMonthStorage) {
		this.lastMonthStorage = lastMonthStorage;
	}
	
	/**
     * 本月库存 0-无库存 1-有库存
     */
	public Integer getCurMonthStorage() {
		return this.curMonthStorage;
	}

    /**
     * 本月库存 0-无库存 1-有库存
     *
     * @param curMonthStorage
     */
	public void setCurMonthStorage(Integer curMonthStorage) {
		this.curMonthStorage = curMonthStorage;
	}
	
	/**
     * 最早入仓时间
     */
	public String getMinInTime() {
		return this.minInTime;
	}

    /**
     * 最早入仓时间
     *
     * @param minInTime
     */
	public void setMinInTime(String minInTime) {
		this.minInTime = minInTime;
	}
	
	/**
     * 最晚出仓时间
     */
	public String getMaxOutTime() {
		return this.maxOutTime;
	}

    /**
     * 最晚出仓时间
     *
     * @param maxOutTime
     */
	public void setMaxOutTime(String maxOutTime) {
		this.maxOutTime = maxOutTime;
	}
	
	/**
     * 计算状态  0-未计算（默认） 1-已计算 2-计算异常
     */
	public String getIsCalculated() {
		return this.isCalculated;
	}

    /**
     * 计算状态  0-未计算（默认） 1-已计算 2-计算异常
     *
     * @param isCalculated
     */
	public void setIsCalculated(String isCalculated) {
		this.isCalculated = isCalculated;
	}
	
	/**
     * 计算时间
     */
	public Timestamp getCalculateTime() {
		return this.calculateTime;
	}

    /**
     * 计算时间
     *
     * @param calculateTime
     */
	public void setCalculateTime(Timestamp calculateTime) {
		this.calculateTime = calculateTime;
	}
	
	/**
     * 统计时间
     */
	public Timestamp getWriteTime() {
		return this.writeTime;
	}

    /**
     * 统计时间
     *
     * @param writeTime
     */
	public void setWriteTime(Timestamp writeTime) {
		this.writeTime = writeTime;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public String getMkInvoiceName() {
		return mkInvoiceName;
	}

	public void setMkInvoiceName(String mkInvoiceName) {
		this.mkInvoiceName = mkInvoiceName;
	}
    
}
