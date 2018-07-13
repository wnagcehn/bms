package com.jiuyescm.bms.report.month.entity;

import java.math.BigDecimal;

import com.jiuyescm.cfm.domain.IEntity;

public class BmsMaterialReportEntity implements IEntity {

	 // TODO Change serialVersionUID by eclipse
    private static final long serialVersionUID = -1;
    
	// id
	private Long id;
	// 仓库编号
	private String warehouseCode;
	// 仓库名称
	private String warehouseName;
	// 商家编号
	private String customerid;
	// 商家名称
	private String customerName;
	// 耗材编码
	private String consumerMaterialCode;
	// 耗材名称
	private String consumerMaterialName;
	// 耗材类别
	private String consumerMaterialType;
	// 单位
	private String unit;
	// 出库量
	private BigDecimal bmsNum;
	// 结算量
	private BigDecimal wmsNum;
	// 差异量
	private BigDecimal differentNum;
	// 年
	private String reportYear;
	// 月
	private String reportMonth;
	// 时间
	private Long reportTime;
	// 删除标志
	private String delFlag;

	public BmsMaterialReportEntity() {

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
     * 仓库编号
     */
	public String getWarehouseCode() {
		return this.warehouseCode;
	}

    /**
     * 仓库编号
     *
     * @param warehouseCode
     */
	public void setWarehouseCode(String warehouseCode) {
		this.warehouseCode = warehouseCode;
	}
	
	/**
     * 仓库名称
     */
	public String getWarehouseName() {
		return this.warehouseName;
	}

    /**
     * 仓库名称
     *
     * @param warehouseName
     */
	public void setWarehouseName(String warehouseName) {
		this.warehouseName = warehouseName;
	}
	
	/**
     * 商家编号
     */
	public String getCustomerid() {
		return this.customerid;
	}

    /**
     * 商家编号
     *
     * @param customerid
     */
	public void setCustomerid(String customerid) {
		this.customerid = customerid;
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
     * 耗材编码
     */
	public String getConsumerMaterialCode() {
		return this.consumerMaterialCode;
	}

    /**
     * 耗材编码
     *
     * @param consumerMaterialCode
     */
	public void setConsumerMaterialCode(String consumerMaterialCode) {
		this.consumerMaterialCode = consumerMaterialCode;
	}
	
	/**
     * 耗材名称
     */
	public String getConsumerMaterialName() {
		return this.consumerMaterialName;
	}

    /**
     * 耗材名称
     *
     * @param consumerMaterialName
     */
	public void setConsumerMaterialName(String consumerMaterialName) {
		this.consumerMaterialName = consumerMaterialName;
	}
	
	/**
     * 耗材类别
     */
	public String getConsumerMaterialType() {
		return this.consumerMaterialType;
	}

    /**
     * 耗材类别
     *
     * @param consumerMaterialType
     */
	public void setConsumerMaterialType(String consumerMaterialType) {
		this.consumerMaterialType = consumerMaterialType;
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
     * 出库量
     */
	public BigDecimal getWmsNum() {
		return this.wmsNum;
	}

    /**
     * 出库量
     *
     * @param wmsNum
     */
	public void setWmsNum(BigDecimal wmsNum) {
		this.wmsNum = wmsNum;
	}
	
	/**
     * 结算量
     */
	public BigDecimal getBmsNum() {
		return this.bmsNum;
	}

    /**
     * 结算量
     *
     * @param bmsNum
     */
	public void setBmsNum(BigDecimal bmsNum) {
		this.bmsNum = bmsNum;
	}
	
	/**
     * 差异量
     */
	public BigDecimal getDifferentNum() {
		return this.differentNum;
	}

    /**
     * 差异量
     *
     * @param differentNum
     */
	public void setDifferentNum(BigDecimal differentNum) {
		this.differentNum = differentNum;
	}
	
	/**
     * 年
     */
	public String getReportYear() {
		return this.reportYear;
	}

    /**
     * 年
     *
     * @param reportYear
     */
	public void setReportYear(String reportYear) {
		this.reportYear = reportYear;
	}
	
	/**
     * 月
     */
	public String getReportMonth() {
		return this.reportMonth;
	}

    /**
     * 月
     *
     * @param reportMonth
     */
	public void setReportMonth(String reportMonth) {
		this.reportMonth = reportMonth;
	}
	
	/**
     * 时间
     */
	public Long getReportTime() {
		return this.reportTime;
	}

    /**
     * 时间
     *
     * @param reportTime
     */
	public void setReportTime(Long reportTime) {
		this.reportTime = reportTime;
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
