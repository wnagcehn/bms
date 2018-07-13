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
public class OmsCustomerPackageEntity implements IEntity {

    // TODO Change serialVersionUID by eclipse
    
	/**
	 * 
	 */
	private static final long serialVersionUID = -7879392916913453440L;
	// 自增主键Id
	private Integer id;
	// 商家Id
	private String customerId;
	// 仓库Id
	private String warehouseId;
	// 创建人Id
	private String createPersonId;
	// 创建人姓名
	private String createPersonName;
	// 创建时间
	private Timestamp createTime;
	// 修改人Id
	private String modifyPersonId;
	// 修改人姓名
	private String modifyPersonName;
	// 修改时间
	private Timestamp modifyPersonTime;
	//备注
	private String remark;
	//版本号
	private String version;
	//包装要求
	private String packageDesc;

	public OmsCustomerPackageEntity() {

	}
	
	/**
     * 自增主键Id
     */
	public Integer getId() {
		return this.id;
	}

    /**
     * 自增主键Id
     *
     * @param id
     */
	public void setId(Integer id) {
		this.id = id;
	}
	
	/**
     * 商家Id
     */
	public String getCustomerId() {
		return this.customerId;
	}

    /**
     * 商家Id
     *
     * @param customerId
     */
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	
	/**
     * 仓库Id
     */
	public String getWarehouseId() {
		return this.warehouseId;
	}

    /**
     * 仓库Id
     *
     * @param warehouseId
     */
	public void setWarehouseId(String warehouseId) {
		this.warehouseId = warehouseId;
	}
	
	
	/**
     * 创建人Id
     */
	public String getCreatePersonId() {
		return this.createPersonId;
	}

    /**
     * 创建人Id
     *
     * @param createPersonId
     */
	public void setCreatePersonId(String createPersonId) {
		this.createPersonId = createPersonId;
	}
	
	/**
     * 创建人姓名
     */
	public String getCreatePersonName() {
		return this.createPersonName;
	}

    /**
     * 创建人姓名
     *
     * @param createPersonName
     */
	public void setCreatePersonName(String createPersonName) {
		this.createPersonName = createPersonName;
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
     * 修改人Id
     */
	public String getModifyPersonId() {
		return this.modifyPersonId;
	}

    /**
     * 修改人Id
     *
     * @param modifyPersonId
     */
	public void setModifyPersonId(String modifyPersonId) {
		this.modifyPersonId = modifyPersonId;
	}
	
	/**
     * 修改人姓名
     */
	public String getModifyPersonName() {
		return this.modifyPersonName;
	}

    /**
     * 修改人姓名
     *
     * @param modifyPersonName
     */
	public void setModifyPersonName(String modifyPersonName) {
		this.modifyPersonName = modifyPersonName;
	}
	
	/**
     * 修改时间
     */
	public Timestamp getModifyPersonTime() {
		return this.modifyPersonTime;
	}

    /**
     * 修改时间
     *
     * @param modifyPersonTime
     */
	public void setModifyPersonTime(Timestamp modifyPersonTime) {
		this.modifyPersonTime = modifyPersonTime;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getPackageDesc() {
		return packageDesc;
	}

	public void setPackageDesc(String packageDesc) {
		this.packageDesc = packageDesc;
	}

    
}
