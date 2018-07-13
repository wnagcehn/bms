/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.quotation.system.entity;

import java.sql.Timestamp;

import com.jiuyescm.cfm.domain.IEntity;

/**
 * 
 * @author stevenl
 * 
 */
public class BmsJiuyeQuotationSystemEntity implements IEntity {

    // TODO Change serialVersionUID by eclipse
    private static final long serialVersionUID = -1;
    
	// id
	private Long id;
	// 版本编号--QS打头 如： QS0001 
	private String versionCode;
	// 版本名称
	private String versionName;
	// 业务类型-用户扩展
	private String typeName;
	// creator
	private String creator;
	// CreateTime
	private Timestamp createTime;
	// LastModifier
	private String lastModifier;
	// LastModifyTime
	private Timestamp lastModifyTime;
	// remark
	private String remark;
	//简称
	private String shortname;

	public String getShortname() {
		return shortname;
	}

	public void setShortname(String shortname) {
		this.shortname = shortname;
	}

	public BmsJiuyeQuotationSystemEntity() {

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
     * 版本编号--QS打头 如： QS0001 
     */
	public String getVersionCode() {
		return this.versionCode;
	}

    /**
     * 版本编号--QS打头 如： QS0001 
     *
     * @param versionCode
     */
	public void setVersionCode(String versionCode) {
		this.versionCode = versionCode;
	}
	
	/**
     * 版本名称
     */
	public String getVersionName() {
		return this.versionName;
	}

    /**
     * 版本名称
     *
     * @param versionName
     */
	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}
	
	/**
     * 业务类型-用户扩展
     */
	public String getTypeName() {
		return this.typeName;
	}

    /**
     * 业务类型-用户扩展
     *
     * @param typeName
     */
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	
	/**
     * creator
     */
	public String getCreator() {
		return this.creator;
	}

    /**
     * creator
     *
     * @param creator
     */
	public void setCreator(String creator) {
		this.creator = creator;
	}
	
	/**
     * CreateTime
     */
	public Timestamp getCreateTime() {
		return this.createTime;
	}

    /**
     * CreateTime
     *
     * @param createTime
     */
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	
	/**
     * LastModifier
     */
	public String getLastModifier() {
		return this.lastModifier;
	}

    /**
     * LastModifier
     *
     * @param lastModifier
     */
	public void setLastModifier(String lastModifier) {
		this.lastModifier = lastModifier;
	}
	
	/**
     * LastModifyTime
     */
	public Timestamp getLastModifyTime() {
		return this.lastModifyTime;
	}

    /**
     * LastModifyTime
     *
     * @param lastModifyTime
     */
	public void setLastModifyTime(Timestamp lastModifyTime) {
		this.lastModifyTime = lastModifyTime;
	}
	
	/**
     * remark
     */
	public String getRemark() {
		return this.remark;
	}

    /**
     * remark
     *
     * @param remark
     */
	public void setRemark(String remark) {
		this.remark = remark;
	}
    
}
