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
public class PubRegionProvinceEntity implements IEntity {

    // TODO Change serialVersionUID by eclipse
    private static final long serialVersionUID = -1;
    
	// ID
	private Integer id;
	// 省编码
	private String provinceCode;
	// 省名称
	private String provinceName;

	public PubRegionProvinceEntity() {

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
     * 省编码
     */
	public String getProvinceCode() {
		return this.provinceCode;
	}

    /**
     * 省编码
     *
     * @param provinceCode
     */
	public void setProvinceCode(String provinceCode) {
		this.provinceCode = provinceCode;
	}
	
	/**
     * 省名称
     */
	public String getProvinceName() {
		return this.provinceName;
	}

    /**
     * 省名称
     *
     * @param provinceName
     */
	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}
    
}
