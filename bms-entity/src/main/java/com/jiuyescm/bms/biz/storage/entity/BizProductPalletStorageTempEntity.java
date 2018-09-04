package com.jiuyescm.bms.biz.storage.entity;

import java.sql.Timestamp;

import com.jiuyescm.cfm.domain.IEntity;

public class BizProductPalletStorageTempEntity implements IEntity {

    private static final long serialVersionUID = -1;
    
	// id
	private Long id;
	// 任务ID
	private String taskId;
	// 库存日期
	private Timestamp stockTime;
	// 仓库Id
	private String warehouseCode;
	// WarehouseName
	private String warehouseName;
	// 商家Id
	private String customerid;
	// CustomerName
	private String customerName;
	// 温度类型
	private String temperatureTypeCode;
	// TemperatureTypeName
	private String temperatureTypeName;
	// PalletNum
	private Double palletNum;
	// IsCalculated
	private String isCalculated;
	// 创建者
	private String creator;
	// 创建时间
	private Timestamp createTime;
	// 删除标志
	private String delFlag;
	// 费用计算时间
	private Timestamp calculateTime;
	// 写入BMS时间
	private Timestamp writeTime;
	// AdjustPalletNum
	private Double adjustPalletNum;
	// excel行号
	private Integer rowExcelNo;
	// excel列名
	private String rowExcelName;

	public BizProductPalletStorageTempEntity() {

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
     * 库存日期
     */
	public Timestamp getStockTime() {
		return this.stockTime;
	}

    /**
     * 库存日期
     *
     * @param stockTime
     */
	public void setStockTime(Timestamp stockTime) {
		this.stockTime = stockTime;
	}
	
	/**
     * 仓库Id
     */
	public String getWarehouseCode() {
		return this.warehouseCode;
	}

    /**
     * 仓库Id
     *
     * @param warehouseCode
     */
	public void setWarehouseCode(String warehouseCode) {
		this.warehouseCode = warehouseCode;
	}
	
	/**
     * WarehouseName
     */
	public String getWarehouseName() {
		return this.warehouseName;
	}

    /**
     * WarehouseName
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
     * CustomerName
     */
	public String getCustomerName() {
		return this.customerName;
	}

    /**
     * CustomerName
     *
     * @param customerName
     */
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	
	/**
     * 温度类型
     */
	public String getTemperatureTypeCode() {
		return this.temperatureTypeCode;
	}

    /**
     * 温度类型
     *
     * @param temperatureTypeCode
     */
	public void setTemperatureTypeCode(String temperatureTypeCode) {
		this.temperatureTypeCode = temperatureTypeCode;
	}
	
	/**
     * TemperatureTypeName
     */
	public String getTemperatureTypeName() {
		return this.temperatureTypeName;
	}

    /**
     * TemperatureTypeName
     *
     * @param temperatureTypeName
     */
	public void setTemperatureTypeName(String temperatureTypeName) {
		this.temperatureTypeName = temperatureTypeName;
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
	
	/**
     * 费用计算时间
     */
	public Timestamp getCalculateTime() {
		return this.calculateTime;
	}

    /**
     * 费用计算时间
     *
     * @param calculateTime
     */
	public void setCalculateTime(Timestamp calculateTime) {
		this.calculateTime = calculateTime;
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
	

	public Double getPalletNum() {
		return palletNum;
	}

	public void setPalletNum(Double palletNum) {
		this.palletNum = palletNum;
	}

	public Double getAdjustPalletNum() {
		return adjustPalletNum;
	}

	public void setAdjustPalletNum(Double adjustPalletNum) {
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
