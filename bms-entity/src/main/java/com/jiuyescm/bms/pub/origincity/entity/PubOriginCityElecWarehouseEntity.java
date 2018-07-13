/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.pub.origincity.entity;

import java.sql.Timestamp;

import com.jiuyescm.cfm.domain.IEntity;

/**
 * 始发城市到达电商仓库表
 * @author yangss
 */
public class PubOriginCityElecWarehouseEntity implements IEntity {

	private static final long serialVersionUID = -8598364147348452843L;
	
	// 主键
	private Long id;
	// 省份名称
	private String provinceName;
	// 城市名称
	private String cityName;
	// 仓库编号
	private String warehouseCode;
	// 仓库名称
	private String warehouseName;
	// 电商编号
	private String elecBizCode;
	// 电商名称
	private String elecBizName;
	// logo路径
	private String logo;
	// 参数1
	private String param1;
	// 参数2
	private String param2;
	// 创建者
	private String creator;
	// 创建时间
	private Timestamp createTime;
	// 修改者
	private String lastModifier;
	// 修改时间
	private Timestamp lastModifyTime;
	// 删除标志
	private String delFlag;

	public PubOriginCityElecWarehouseEntity() {

	}
	
	/**
     * 主键
     */
	public Long getId() {
		return this.id;
	}

    /**
     * 主键
     *
     * @param id
     */
	public void setId(Long id) {
		this.id = id;
	}
	
	/**
     * 省份名称
     */
	public String getProvinceName() {
		return this.provinceName;
	}

    /**
     * 省份名称
     *
     * @param provinceName
     */
	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}
	
	/**
     * 城市名称
     */
	public String getCityName() {
		return this.cityName;
	}

    /**
     * 城市名称
     *
     * @param cityName
     */
	public void setCityName(String cityName) {
		this.cityName = cityName;
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
     * 电商编号
     */
	public String getElecBizCode() {
		return this.elecBizCode;
	}

    /**
     * 电商编号
     *
     * @param elecBizCode
     */
	public void setElecBizCode(String elecBizCode) {
		this.elecBizCode = elecBizCode;
	}
	
	/**
     * 电商名称
     */
	public String getElecBizName() {
		return this.elecBizName;
	}

    /**
     * 电商名称
     *
     * @param elecBizName
     */
	public void setElecBizName(String elecBizName) {
		this.elecBizName = elecBizName;
	}
	
	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	/**
     * 参数1
     */
	public String getParam1() {
		return this.param1;
	}

    /**
     * 参数1
     *
     * @param param1
     */
	public void setParam1(String param1) {
		this.param1 = param1;
	}
	
	/**
     * 参数2
     */
	public String getParam2() {
		return this.param2;
	}

    /**
     * 参数2
     *
     * @param param2
     */
	public void setParam2(String param2) {
		this.param2 = param2;
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
     * 修改者
     */
	public String getLastModifier() {
		return this.lastModifier;
	}

    /**
     * 修改者
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
