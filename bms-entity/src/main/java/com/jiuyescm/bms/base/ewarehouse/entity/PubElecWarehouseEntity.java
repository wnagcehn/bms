/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.base.ewarehouse.entity;

import java.sql.Timestamp;

import com.jiuyescm.cfm.domain.IEntity;

/**
 * 
 * @author stevenl
 * 
 */
public class PubElecWarehouseEntity implements IEntity {

    // TODO Change serialVersionUID by eclipse
    private static final long serialVersionUID = -1;
    
	// 主键ID
	private Long id;
	// 仓库编号
	private String warehouseCode;
	// 仓库名称
	private String warehouseName;
	// 电商编号
	private String elecBizCode;
	// 电商名称
	private String elecBizName;
	// 省份编号
	private String provinceCode;
	// 省份名称
	private String provinceName;
	// 城市编号
	private String cityCode;
	// 城市名称
	private String cityName;
	// 地区编号
	private String districtCode;
	// 地区名称
	private String districtName;
	// 详细地址
	private String detailAddress;
	// 备注
	private String remark;
	// 创建者编号
	private String creatorCode;
	// 创建时间
	private Timestamp createTime;
	// 修改者编号
	private String lastModifier;
	// 修改时间
	private Timestamp lastModifyTime;
	// 删除标志
	private String delFlag;

	public PubElecWarehouseEntity() {

	}
	
	/**
     * 主键ID
     */
	public Long getId() {
		return this.id;
	}

    /**
     * 主键ID
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
	
	/**
     * 省份编号
     */
	public String getProvinceCode() {
		return this.provinceCode;
	}

    /**
     * 省份编号
     *
     * @param provinceCode
     */
	public void setProvinceCode(String provinceCode) {
		this.provinceCode = provinceCode;
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
     * 城市编号
     */
	public String getCityCode() {
		return this.cityCode;
	}

    /**
     * 城市编号
     *
     * @param cityCode
     */
	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
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
     * 地区编号
     */
	public String getDistrictCode() {
		return this.districtCode;
	}

    /**
     * 地区编号
     *
     * @param districtCode
     */
	public void setDistrictCode(String districtCode) {
		this.districtCode = districtCode;
	}
	
	/**
     * 地区名称
     */
	public String getDistrictName() {
		return this.districtName;
	}

    /**
     * 地区名称
     *
     * @param districtName
     */
	public void setDistrictName(String districtName) {
		this.districtName = districtName;
	}
	
	/**
     * 详细地址
     */
	public String getDetailAddress() {
		return this.detailAddress;
	}

    /**
     * 详细地址
     *
     * @param detailAddress
     */
	public void setDetailAddress(String detailAddress) {
		this.detailAddress = detailAddress;
	}
	
	/**
     * 备注
     */
	public String getRemark() {
		return this.remark;
	}

    /**
     * 备注
     *
     * @param remark
     */
	public void setRemark(String remark) {
		this.remark = remark;
	}

	/**
     * 创建者编号
     */
	public String getCreatorCode() {
		return this.creatorCode;
	}

    /**
     * 创建者编号
     *
     * @param creatorCode
     */
	public void setCreatorCode(String creatorCode) {
		this.creatorCode = creatorCode;
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
     * 修改者编号
     */
	public String getLastModifier() {
		return this.lastModifier;
	}

    /**
     * 修改者编号
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
