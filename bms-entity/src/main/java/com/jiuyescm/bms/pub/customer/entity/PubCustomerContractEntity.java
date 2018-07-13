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
public class PubCustomerContractEntity implements IEntity {

    // TODO Change serialVersionUID by eclipse
    private static final long serialVersionUID = -1;
    
	// 唯一标识
	private Integer id;
	// 商家ID
	private String customerId;
	// 合同开始时间
	private Timestamp contractStartTime;
	// 合同结束时间
	private Timestamp contractEndTime;
	// 附件：合同电子版/扫描件
	private String contractAttachment;
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

	public PubCustomerContractEntity() {

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
     * 合同开始时间
     */
	public Timestamp getContractStartTime() {
		return this.contractStartTime;
	}

    /**
     * 合同开始时间
     *
     * @param contractStartTime
     */
	public void setContractStartTime(Timestamp contractStartTime) {
		this.contractStartTime = contractStartTime;
	}
	
	/**
     * 合同结束时间
     */
	public Timestamp getContractEndTime() {
		return this.contractEndTime;
	}

    /**
     * 合同结束时间
     *
     * @param contractEndTime
     */
	public void setContractEndTime(Timestamp contractEndTime) {
		this.contractEndTime = contractEndTime;
	}
	
	/**
     * 附件：合同电子版/扫描件
     */
	public String getContractAttachment() {
		return this.contractAttachment;
	}

    /**
     * 附件：合同电子版/扫描件
     *
     * @param contractAttachment
     */
	public void setContractAttachment(String contractAttachment) {
		this.contractAttachment = contractAttachment;
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
