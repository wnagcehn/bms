/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.general.entity;

import java.sql.Timestamp;
import java.util.Date;

import com.jiuyescm.cfm.domain.IEntity;

/**
 * 
 * @author stevenl
 * 
 */
public class ReportWarehouseBizImportEntity implements IEntity {

	private static final long serialVersionUID = 3129057042459689317L;
	
	// id
	private Long id;
	// 日期
	private String importDate;
	// 仓库编号
	private String warehouseCode;
	// 业务类型
	private String bizType;
	// 导入类型(应导入/实际导入)
	private String importType;
	// 商家ID
	private String customerId;
	// 创建时间
	private Timestamp createTime;
	// 删除标识
	private String delFlag;
	// 日期
	private String importDateStr;

	public String getImportDateStr() {
		return importDateStr;
	}

	public void setImportDateStr(String importDateStr) {
		this.importDateStr = importDateStr;
	}

	public ReportWarehouseBizImportEntity() {

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
	
	
	public String getImportDate() {
        return importDate;
    }

    public void setImportDate(String importDate) {
        this.importDate = importDate;
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
     * 业务类型
     */
	public String getBizType() {
		return this.bizType;
	}

    /**
     * 业务类型
     *
     * @param bizType
     */
	public void setBizType(String bizType) {
		this.bizType = bizType;
	}
	
	/**
     * 导入类型(应导入/实际导入)
     */
	public String getImportType() {
		return this.importType;
	}

    /**
     * 导入类型(应导入/实际导入)
     *
     * @param importType
     */
	public void setImportType(String importType) {
		this.importType = importType;
	}
	
	/**
     * 商家ID
     */
	public String getCustomerId() {
		return this.customerId;
	}

    /**
     * 商家ID
     *
     * @param customerId
     */
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
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
     * 删除标识
     */
	public String getDelFlag() {
		return this.delFlag;
	}

    /**
     * 删除标识
     *
     * @param delFlag
     */
	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}
    
}
