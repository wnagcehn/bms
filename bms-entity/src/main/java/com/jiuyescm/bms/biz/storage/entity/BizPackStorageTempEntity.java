package com.jiuyescm.bms.biz.storage.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.jiuyescm.cfm.domain.IEntity;

public class BizPackStorageTempEntity implements IEntity {

    // TODO Change serialVersionUID by eclipse
    private static final long serialVersionUID = -1;
    
	// id
	private Long id;
	// 任务ID
	private String taskId;
	// 日期(当前日期)
	private Timestamp curTime;
	// 仓库号
	private String warehouseCode;
	// 仓库名称
	private String warehouseName;
	// 商家Id
	private String customerid;
	// 商家名称
	private String customerName;
	// PalletNum
	private BigDecimal palletNum;
	// 创建者
	private String creator;
	// 创建时间
	private Timestamp createTime;
	// IsCalculated
	private String isCalculated;
	// 写入BMS时间
	private Timestamp writeTime;
	// 温度编码
	private String temperatureTypeCode;
	// 温度类型
	private String temperatureTypeName;
	// AdjustPalletNum
	private BigDecimal adjustPalletNum;
	// excel行号
	private Integer rowExcelNo;
	// excel列名
	private String rowExcelName;

	public BizPackStorageTempEntity() {

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
     * 日期(当前日期)
     */
	public Timestamp getCurTime() {
		return this.curTime;
	}

    /**
     * 日期(当前日期)
     *
     * @param curTime
     */
	public void setCurTime(Timestamp curTime) {
		this.curTime = curTime;
	}
	
	/**
     * 仓库号
     */
	public String getWarehouseCode() {
		return this.warehouseCode;
	}

    /**
     * 仓库号
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
     * 商家Id
     */
	public String getCustomerid() {
		return this.customerid;
	}

    /**
     * 商家Id
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
     * PalletNum
     */
	public BigDecimal getPalletNum() {
		return this.palletNum;
	}

    /**
     * PalletNum
     *
     * @param palletNum
     */
	public void setPalletNum(BigDecimal palletNum) {
		this.palletNum = palletNum;
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
     * IsCalculated
     */
	public String getIsCalculated() {
		return this.isCalculated;
	}

    /**
     * IsCalculated
     *
     * @param isCalculated
     */
	public void setIsCalculated(String isCalculated) {
		this.isCalculated = isCalculated;
	}
	
	/**
     * 写入BMS时间
     */
	public Timestamp getWriteTime() {
		return this.writeTime;
	}

    /**
     * 写入BMS时间
     *
     * @param writeTime
     */
	public void setWriteTime(Timestamp writeTime) {
		this.writeTime = writeTime;
	}
	
	/**
     * 温度编码
     */
	public String getTemperatureTypeCode() {
		return this.temperatureTypeCode;
	}

    /**
     * 温度编码
     *
     * @param temperatureTypeCode
     */
	public void setTemperatureTypeCode(String temperatureTypeCode) {
		this.temperatureTypeCode = temperatureTypeCode;
	}
	
	/**
     * 温度类型
     */
	public String getTemperatureTypeName() {
		return this.temperatureTypeName;
	}

    /**
     * 温度类型
     *
     * @param temperatureTypeName
     */
	public void setTemperatureTypeName(String temperatureTypeName) {
		this.temperatureTypeName = temperatureTypeName;
	}
	
	/**
     * AdjustPalletNum
     */
	public BigDecimal getAdjustPalletNum() {
		return this.adjustPalletNum;
	}

    /**
     * AdjustPalletNum
     *
     * @param adjustPalletNum
     */
	public void setAdjustPalletNum(BigDecimal adjustPalletNum) {
		this.adjustPalletNum = adjustPalletNum;
	}
	
	/**
     * excel行号
     */
	public Integer getRowExcelNo() {
		return this.rowExcelNo;
	}

    /**
     * excel行号
     *
     * @param rowExcelNo
     */
	public void setRowExcelNo(Integer rowExcelNo) {
		this.rowExcelNo = rowExcelNo;
	}
	
	/**
     * excel列名
     */
	public String getRowExcelName() {
		return this.rowExcelName;
	}

    /**
     * excel列名
     *
     * @param rowExcelName
     */
	public void setRowExcelName(String rowExcelName) {
		this.rowExcelName = rowExcelName;
	}
    
}
