/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.base.dictionary.entity;

import java.sql.Timestamp;
import com.jiuyescm.cfm.domain.IEntity;

/**
 * 
 * @author stevenl
 * 
 */
public class SystemCodeTypeEntity implements IEntity {

	// id
	private Long id;
	// timeflag
	private Timestamp timeflag;
	// 类型名称
	private String typeName;
	// 类型编码
	private String typeCode;
	// 状态 EFFECTIVE_STATUS
	private String typeStatus;
	// 描述
	private String typeDesc;
	// 创建人ID
	private String createId;
	// 创建时间
	private Timestamp createDt;
	// 修改人ID
	private String updateId;
	// 修改时间
	private Timestamp updateDt;
	// 扩展字段1
	private String extattr1;
	// 扩展字段2
	private String extattr2;
	// 扩展字段3
	private String extattr3;
	// 删除人
	private String deleteId;
	// 删除时间
	private Timestamp deleteDt;

	public SystemCodeTypeEntity() {

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
     * timeflag
     */
	public Timestamp getTimeflag() {
		return this.timeflag;
	}

    /**
     * timeflag
     *
     * @param timeflag
     */
	public void setTimeflag(Timestamp timeflag) {
		this.timeflag = timeflag;
	}
	
	/**
     * 类型名称
     */
	public String getTypeName() {
		return this.typeName;
	}

    /**
     * 类型名称
     *
     * @param typeName
     */
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	
	/**
     * 类型编码
     */
	public String getTypeCode() {
		return this.typeCode;
	}

    /**
     * 类型编码
     *
     * @param typeCode
     */
	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}
	
	/**
     * 状态 EFFECTIVE_STATUS
     */
	public String getTypeStatus() {
		return this.typeStatus;
	}

    /**
     * 状态 EFFECTIVE_STATUS
     *
     * @param typeStatus
     */
	public void setTypeStatus(String typeStatus) {
		this.typeStatus = typeStatus;
	}
	
	/**
     * 描述
     */
	public String getTypeDesc() {
		return this.typeDesc;
	}

    /**
     * 描述
     *
     * @param typeDesc
     */
	public void setTypeDesc(String typeDesc) {
		this.typeDesc = typeDesc;
	}
	
	/**
     * 创建人ID
     */
	public String getCreateId() {
		return this.createId;
	}

    /**
     * 创建人ID
     *
     * @param createrId
     */
	public void setCreateId(String createId) {
		this.createId = createId;
	}
	
	/**
     * 创建时间
     */
	public Timestamp getCreateDt() {
		return this.createDt;
	}

    /**
     * 创建时间
     *
     * @param createrDt
     */
	public void setCreateDt(Timestamp createDt) {
		this.createDt = createDt;
	}
	
	/**
     * 修改人ID
     */
	public String getUpdateId() {
		return this.updateId;
	}

    /**
     * 修改人ID
     *
     * @param updateId
     */
	public void setUpdateId(String updateId) {
		this.updateId = updateId;
	}
	
	/**
     * 修改时间
     */
	public Timestamp getUpdateDt() {
		return this.updateDt;
	}

    /**
     * 修改时间
     *
     * @param updateDt
     */
	public void setUpdateDt(Timestamp updateDt) {
		this.updateDt = updateDt;
	}
	
	/**
     * 扩展字段1
     */
	public String getExtattr1() {
		return this.extattr1;
	}

    /**
     * 扩展字段1
     *
     * @param extattr1
     */
	public void setExtattr1(String extattr1) {
		this.extattr1 = extattr1;
	}
	
	/**
     * 扩展字段2
     */
	public String getExtattr2() {
		return this.extattr2;
	}

    /**
     * 扩展字段2
     *
     * @param extattr2
     */
	public void setExtattr2(String extattr2) {
		this.extattr2 = extattr2;
	}
	
	/**
     * 扩展字段3
     */
	public String getExtattr3() {
		return this.extattr3;
	}

    /**
     * 扩展字段3
     *
     * @param extattr3
     */
	public void setExtattr3(String extattr3) {
		this.extattr3 = extattr3;
	}
	
	/**
     * 删除人
     */
	public String getDeleteId() {
		return this.deleteId;
	}

    /**
     * 删除人
     *
     * @param deleteId
     */
	public void setDeleteId(String deleteId) {
		this.deleteId = deleteId;
	}
	
	/**
     * 删除时间
     */
	public Timestamp getDeleteDt() {
		return this.deleteDt;
	}

    /**
     * 删除时间
     *
     * @param deleteDt
     */
	public void setDeleteDt(Timestamp deleteDt) {
		this.deleteDt = deleteDt;
	}
    
}
