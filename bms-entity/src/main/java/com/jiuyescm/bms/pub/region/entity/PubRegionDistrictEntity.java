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
public class PubRegionDistrictEntity implements IEntity {

    // TODO Change serialVersionUID by eclipse
    private static final long serialVersionUID = -1;
    
	// ID
	private Integer id;
	// 父节点市编码
	private String parentCityCode;
	// 父节点市名称
	private String parentCityName;
	// 区编码
	private String districtCode;
	// 区名称
	private String districtName;

	public PubRegionDistrictEntity() {

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
     * 父节点市编码
     */
	public String getParentCityCode() {
		return this.parentCityCode;
	}

    /**
     * 父节点市编码
     *
     * @param parentCityCode
     */
	public void setParentCityCode(String parentCityCode) {
		this.parentCityCode = parentCityCode;
	}
	
	/**
     * 父节点市名称
     */
	public String getParentCityName() {
		return this.parentCityName;
	}

    /**
     * 父节点市名称
     *
     * @param parentCityName
     */
	public void setParentCityName(String parentCityName) {
		this.parentCityName = parentCityName;
	}

	public String getDistrictCode() {
		return districtCode;
	}

	public void setDistrictCode(String districtCode) {
		this.districtCode = districtCode;
	}

	public String getDistrictName() {
		return districtName;
	}

	public void setDistrictName(String districtName) {
		this.districtName = districtName;
	}
	 
}
