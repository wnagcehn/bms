/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.pub.region.entity;

import com.jiuyescm.cfm.domain.IEntity;

/**
 * 
 * @author stevenl
 * 
 */
public class PubRegionCityEntity implements IEntity {

    // TODO Change serialVersionUID by eclipse
    private static final long serialVersionUID = -1;
    
	// ID
	private Integer id;
	// 父节点省编码
	private String parentProvinceCode;
	// 父节点省名称
	private String parentProvinceName;
	// 市编码
	private String cityCode;
	// 市名称
	private String cityName;

	public PubRegionCityEntity() {

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
     * 父节点省编码
     */
	public String getParentProvinceCode() {
		return this.parentProvinceCode;
	}

    /**
     * 父节点省编码
     *
     * @param parentProvinceCode
     */
	public void setParentProvinceCode(String parentProvinceCode) {
		this.parentProvinceCode = parentProvinceCode;
	}
	
	/**
     * 父节点省名称
     */
	public String getParentProvinceName() {
		return this.parentProvinceName;
	}

    /**
     * 父节点省名称
     *
     * @param parentProvinceName
     */
	public void setParentProvinceName(String parentProvinceName) {
		this.parentProvinceName = parentProvinceName;
	}
	
	/**
     * 市编码
     */
	public String getCityCode() {
		return this.cityCode;
	}

    /**
     * 市编码
     *
     * @param cityCode
     */
	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}
	
	/**
     * 市名称
     */
	public String getCityName() {
		return this.cityName;
	}

    /**
     * 市名称
     *
     * @param cityName
     */
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
    
}
