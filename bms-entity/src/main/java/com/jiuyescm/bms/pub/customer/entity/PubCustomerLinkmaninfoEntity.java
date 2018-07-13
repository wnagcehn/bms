/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.pub.customer.entity;

import java.sql.Timestamp;
import com.jiuyescm.cfm.domain.IEntity;

/**
 * 
 * @author stevenl
 * 
 */
public class PubCustomerLinkmaninfoEntity implements IEntity {

    // TODO Change serialVersionUID by eclipse
    private static final long serialVersionUID = -1;
    
	// 唯一标识
	private Integer id;
	// 商家ID
	private String customerId;
	// 类型
	private String infoType;
	// 姓名
	private String infoName;
	// 电话
	private String infoTel;
	// 邮箱
	private String infoMail;
	// 备注
	private String remark;
	// 数据版本
	private Integer version;
	// 创建人
	private String createPerson;
	// 创建人ID
	private String createPersonid;
	// 创建时间
	private Timestamp createTime;
	// 更新人
	private String modPerson;
	// 更新人ID
	private String modPersonid;
	// 更新时间
	private Timestamp modTime;

	public PubCustomerLinkmaninfoEntity() {

	}
	
	/**
     * 唯一标识
     */
	public Integer getId() {
		return this.id;
	}

    /**
     * 唯一标识
     *
     * @param id
     */
	public void setId(Integer id) {
		this.id = id;
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
     * 类型
     */
	public String getInfoType() {
		return this.infoType;
	}

    /**
     * 类型
     *
     * @param infoType
     */
	public void setInfoType(String infoType) {
		this.infoType = infoType;
	}
	
	/**
     * 姓名
     */
	public String getInfoName() {
		return this.infoName;
	}

    /**
     * 姓名
     *
     * @param infoName
     */
	public void setInfoName(String infoName) {
		this.infoName = infoName;
	}
	
	/**
     * 电话
     */
	public String getInfoTel() {
		return this.infoTel;
	}

    /**
     * 电话
     *
     * @param infoTel
     */
	public void setInfoTel(String infoTel) {
		this.infoTel = infoTel;
	}
	
	/**
     * 邮箱
     */
	public String getInfoMail() {
		return this.infoMail;
	}

    /**
     * 邮箱
     *
     * @param infoMail
     */
	public void setInfoMail(String infoMail) {
		this.infoMail = infoMail;
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
     * 数据版本
     */
	public Integer getVersion() {
		return this.version;
	}

    /**
     * 数据版本
     *
     * @param version
     */
	public void setVersion(Integer version) {
		this.version = version;
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
	
	/**
     * 创建人ID
     */
	public String getCreatePersonid() {
		return this.createPersonid;
	}

    /**
     * 创建人ID
     *
     * @param createPersonid
     */
	public void setCreatePersonid(String createPersonid) {
		this.createPersonid = createPersonid;
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
     * 更新人
     */
	public String getModPerson() {
		return this.modPerson;
	}

    /**
     * 更新人
     *
     * @param modPerson
     */
	public void setModPerson(String modPerson) {
		this.modPerson = modPerson;
	}
	
	/**
     * 更新人ID
     */
	public String getModPersonid() {
		return this.modPersonid;
	}

    /**
     * 更新人ID
     *
     * @param modPersonid
     */
	public void setModPersonid(String modPersonid) {
		this.modPersonid = modPersonid;
	}
	
	/**
     * 更新时间
     */
	public Timestamp getModTime() {
		return this.modTime;
	}

    /**
     * 更新时间
     *
     * @param modTime
     */
	public void setModTime(Timestamp modTime) {
		this.modTime = modTime;
	}
    
}
