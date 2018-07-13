/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.fees.storage.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.jiuyescm.cfm.domain.IEntity;

/**
 * 
 * @author stevenl
 * 
 */
public class PackCostReportEntity implements IEntity {

    
	/**
	 * 
	 */
	private static final long serialVersionUID = -9219217998894548688L;
	// id
	private int id;
	// 仓库号
	private String warehouseNo;
	// 年
	private Integer year;
	// 月
	private Integer monthNum;
	// 时间类型：年(Y)月（M）季度(S)
	private String dateType;
	// 开始日期
	private Timestamp startDate;
	// 结束日期
	private Timestamp endDate;
	// 耗材编号
	private String materialNo;
	// 耗材名称
	private String materialName;
	// 耗材条码
	private String barcode;
	// 耗材类型
	private String materialType;
	// 耗材类型名称
	private String materialTypeName;
	// 单位
	private String unit;
	// InitQty
	private BigDecimal initQty;
	// InQty
	private BigDecimal inQty;
	// InitCost
	private BigDecimal initCost;
	// cost
	private BigDecimal cost;
	// 期初金额
	private BigDecimal initMoney;
	// InMoney
	private BigDecimal inMoney;
	// 创建人
	private String crePerson;
	// 创建人id
	private String crePersonId;
	// 创建时间
	private Timestamp creTime;

	public PackCostReportEntity() {

	}
	
	/**
     * id
     */
	public int getId() {
		return this.id;
	}

    /**
     * id
     *
     * @param id
     */
	public void setId(int id) {
		this.id = id;
	}
	
	/**
     * 仓库号
     */
	public String getWarehouseNo() {
		return this.warehouseNo;
	}

    /**
     * 仓库号
     *
     * @param warehouseNo
     */
	public void setWarehouseNo(String warehouseNo) {
		this.warehouseNo = warehouseNo;
	}
	
	/**
     * 年
     */
	public Integer getYear() {
		return this.year;
	}

    /**
     * 年
     *
     * @param year
     */
	public void setYear(Integer year) {
		this.year = year;
	}
	
	/**
     * 月
     */
	public Integer getMonthNum() {
		return this.monthNum;
	}

    /**
     * 月
     *
     * @param monthNum
     */
	public void setMonthNum(Integer monthNum) {
		this.monthNum = monthNum;
	}
	
	/**
     * 时间类型：年(Y)月（M）季度(S)
     */
	public String getDateType() {
		return this.dateType;
	}

    /**
     * 时间类型：年(Y)月（M）季度(S)
     *
     * @param dateType
     */
	public void setDateType(String dateType) {
		this.dateType = dateType;
	}
	
	/**
     * 开始日期
     */
	public Timestamp getStartDate() {
		return this.startDate;
	}

    /**
     * 开始日期
     *
     * @param startDate
     */
	public void setStartDate(Timestamp startDate) {
		this.startDate = startDate;
	}
	
	/**
     * 结束日期
     */
	public Timestamp getEndDate() {
		return this.endDate;
	}

    /**
     * 结束日期
     *
     * @param endDate
     */
	public void setEndDate(Timestamp endDate) {
		this.endDate = endDate;
	}
	
	/**
     * 耗材编号
     */
	public String getMaterialNo() {
		return this.materialNo;
	}

    /**
     * 耗材编号
     *
     * @param materialNo
     */
	public void setMaterialNo(String materialNo) {
		this.materialNo = materialNo;
	}
	
	/**
     * 耗材名称
     */
	public String getMaterialName() {
		return this.materialName;
	}

    /**
     * 耗材名称
     *
     * @param materialName
     */
	public void setMaterialName(String materialName) {
		this.materialName = materialName;
	}
	
	/**
     * 耗材条码
     */
	public String getBarcode() {
		return this.barcode;
	}

    /**
     * 耗材条码
     *
     * @param barcode
     */
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
	
	/**
     * 耗材类型
     */
	public String getMaterialType() {
		return this.materialType;
	}

    /**
     * 耗材类型
     *
     * @param materialType
     */
	public void setMaterialType(String materialType) {
		this.materialType = materialType;
	}
	
	/**
     * 耗材类型名称
     */
	public String getMaterialTypeName() {
		return this.materialTypeName;
	}

    /**
     * 耗材类型名称
     *
     * @param materialTypeName
     */
	public void setMaterialTypeName(String materialTypeName) {
		this.materialTypeName = materialTypeName;
	}
	
	/**
     * 单位
     */
	public String getUnit() {
		return this.unit;
	}

    /**
     * 单位
     *
     * @param unit
     */
	public void setUnit(String unit) {
		this.unit = unit;
	}
	
	/**
     * InitQty
     */
	public BigDecimal getInitQty() {
		return this.initQty;
	}

    /**
     * InitQty
     *
     * @param initQty
     */
	public void setInitQty(BigDecimal initQty) {
		this.initQty = initQty;
	}
	
	/**
     * InQty
     */
	public BigDecimal getInQty() {
		return this.inQty;
	}

    /**
     * InQty
     *
     * @param inQty
     */
	public void setInQty(BigDecimal inQty) {
		this.inQty = inQty;
	}
	
	/**
     * InitCost
     */
	public BigDecimal getInitCost() {
		return this.initCost;
	}

    /**
     * InitCost
     *
     * @param initCost
     */
	public void setInitCost(BigDecimal initCost) {
		this.initCost = initCost;
	}
	
	/**
     * cost
     */
	public BigDecimal getCost() {
		return this.cost;
	}

    /**
     * cost
     *
     * @param cost
     */
	public void setCost(BigDecimal cost) {
		this.cost = cost;
	}
	
	/**
     * 期初金额
     */
	public BigDecimal getInitMoney() {
		return this.initMoney;
	}

    /**
     * 期初金额
     *
     * @param initMoney
     */
	public void setInitMoney(BigDecimal initMoney) {
		this.initMoney = initMoney;
	}
	
	/**
     * InMoney
     */
	public BigDecimal getInMoney() {
		return this.inMoney;
	}

    /**
     * InMoney
     *
     * @param inMoney
     */
	public void setInMoney(BigDecimal inMoney) {
		this.inMoney = inMoney;
	}
	
	/**
     * 创建人
     */
	public String getCrePerson() {
		return this.crePerson;
	}

    /**
     * 创建人
     *
     * @param crePerson
     */
	public void setCrePerson(String crePerson) {
		this.crePerson = crePerson;
	}
	
	/**
     * 创建人id
     */
	public String getCrePersonId() {
		return this.crePersonId;
	}

    /**
     * 创建人id
     *
     * @param crePersonId
     */
	public void setCrePersonId(String crePersonId) {
		this.crePersonId = crePersonId;
	}
	
	/**
     * 创建时间
     */
	public Timestamp getCreTime() {
		return this.creTime;
	}

    /**
     * 创建时间
     *
     * @param creTime
     */
	public void setCreTime(Timestamp creTime) {
		this.creTime = creTime;
	}
    
}
