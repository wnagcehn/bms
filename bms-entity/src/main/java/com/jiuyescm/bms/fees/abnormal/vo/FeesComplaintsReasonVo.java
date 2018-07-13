/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.fees.abnormal.vo;

import java.sql.Timestamp;

import com.jiuyescm.cfm.domain.IEntity;

/**
 * 
 * @author stevenl
 * 
 */
public class FeesComplaintsReasonVo implements IEntity {

	private static final long serialVersionUID = -6631079875137915170L;
	
	// 客诉ID 自增
	private Integer id;
	// 父ID
	private Integer parentid;
	// 客诉原因
	private String reason;
	// 创建时间
	private Timestamp createTime;
	// 创建人
	private String createPerson;

	public FeesComplaintsReasonVo() {

	}
	
	/**
     * 客诉ID 自增
     */
	public Integer getId() {
		return this.id;
	}

    /**
     * 客诉ID 自增
     *
     * @param id
     */
	public void setId(Integer id) {
		this.id = id;
	}
	
	/**
     * 父ID
     */
	public Integer getParentid() {
		return this.parentid;
	}

    /**
     * 父ID
     *
     * @param parentid
     */
	public void setParentid(Integer parentid) {
		this.parentid = parentid;
	}
	
	/**
     * 客诉原因
     */
	public String getReason() {
		return this.reason;
	}

    /**
     * 客诉原因
     *
     * @param reason
     */
	public void setReason(String reason) {
		this.reason = reason;
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
     * 创建人
     */
	public String getCreatePerson() {
		return this.createPerson;
	}

    /**
     * 创建人
     *
     * @param createPerson
     */
	public void setCreatePerson(String createPerson) {
		this.createPerson = createPerson;
	}
    
}
