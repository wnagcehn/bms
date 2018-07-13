/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.pub.info.entity;

import com.jiuyescm.cfm.domain.IEntity; 

/**
 * 
 * @author stevenl
 * 
 */
public class PubRegionAliasEntity implements IEntity {

    // TODO Change serialVersionUID by eclipse
    private static final long serialVersionUID = -1;
    
	// ID
	private Integer id;
	// 标准父节点编码
	private String parentStandardCode;
	// 标准父节点名称
	private String parentStandardName;
	// 标准节点编码
	private String standardCode;
	// 标准区域名称
	private String standardName;
	// 别名
	private String alias;
	// zipcode
	private Integer zipcode;
	// 级别
	private Integer level;
	// 排序
	private Long sort;
	
	private String province;
	private String city;
	private String district;
	
	private String provinceCode;
	private String cityCode;
	private String districtCode; 
	
	private String levelName;

	public PubRegionAliasEntity() {

	}
	
	public PubRegionAliasEntity(Integer level, String levelName) {
		super();
		this.level = level;
		this.levelName = levelName;
	}

	/**
     * ID
     */
	public Integer getId() {
		return this.id;
	}

    /**
     * ID
     *
     * @param id
     */
	public void setId(Integer id) {
		this.id = id;
	}
	
	/**
     * 标准父节点编码
     */
	public String getParentStandardCode() {
		return this.parentStandardCode;
	}

    /**
     * 标准父节点编码
     *
     * @param parentStandardCode
     */
	public void setParentStandardCode(String parentStandardCode) {
		this.parentStandardCode = parentStandardCode;
	}
	
	/**
     * 标准父节点名称
     */
	public String getParentStandardName() {
		return this.parentStandardName;
	}

    /**
     * 标准父节点名称
     *
     * @param parentStandardName
     */
	public void setParentStandardName(String parentStandardName) {
		this.parentStandardName = parentStandardName;
	}
	
	/**
     * 标准节点编码
     */
	public String getStandardCode() {
		return this.standardCode;
	}

    /**
     * 标准节点编码
     *
     * @param standardCode
     */
	public void setStandardCode(String standardCode) {
		this.standardCode = standardCode;
	}
	
	/**
     * 标准区域名称
     */
	public String getStandardName() {
		return this.standardName;
	}

    /**
     * 标准区域名称
     *
     * @param standardName
     */
	public void setStandardName(String standardName) {
		this.standardName = standardName;
	}
	
	/**
     * 别名
     */
	public String getAlias() {
		return this.alias;
	}

    /**
     * 别名
     *
     * @param alias
     */
	public void setAlias(String alias) {
		this.alias = alias;
	}
	
	/**
     * zipcode
     */
	public Integer getZipcode() {
		return this.zipcode;
	}

    /**
     * zipcode
     *
     * @param zipcode
     */
	public void setZipcode(Integer zipcode) {
		this.zipcode = zipcode;
	}
	
	/**
     * 级别
     */
	public Integer getLevel() {
		return this.level;
	}

    /**
     * 级别
     *
     * @param level
     */
	public void setLevel(Integer level) {
		this.level = level;
	}
	
	/**
     * 排序
     */
	public Long getSort() {
		return this.sort;
	}

    /**
     * 排序
     *
     * @param sort
     */
	public void setSort(Long sort) {
		this.sort = sort;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public String getLevelName() {
		return levelName;
	}

	public void setLevelName(String levelName) {
		this.levelName = levelName;
	}

	public String getProvinceCode() {
		return provinceCode;
	}

	public void setProvinceCode(String provinceCode) {
		this.provinceCode = provinceCode;
	}

	public String getCityCode() {
		return cityCode;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}

	public String getDistrictCode() {
		return districtCode;
	}

	public void setDistrictCode(String districtCode) {
		this.districtCode = districtCode;
	}
    
}
