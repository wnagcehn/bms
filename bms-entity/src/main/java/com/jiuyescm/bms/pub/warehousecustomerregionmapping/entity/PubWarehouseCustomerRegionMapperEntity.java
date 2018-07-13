/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.pub.warehousecustomerregionmapping.entity;

import java.sql.Timestamp;
import com.jiuyescm.cfm.domain.IEntity; 

/**
 * 
 * @author stevenl
 * 
 */
public class PubWarehouseCustomerRegionMapperEntity implements IEntity {

    // TODO Change serialVersionUID by eclipse
    private static final long serialVersionUID = -1;
    
	// 自增ID
	private Integer id;
	// 仓库ID
	private String warehouseid;
	// 客户编号
	private String customerid;
	// 客户编码
	private String customerCode;
	// 区域ID
	private String province;
	// 删除标记
	private Integer delflag;
	// 备注
	private String remark;
	// 创建者ID
	private String crepersonid;
	// 创建者
	private String creperson;
	// 创建时间
	private Timestamp cretime;
	// 修改者ID
	private String modpersonid;
	// 修改者
	private String modperson;
	// 修改时间
	private Timestamp modtime;

	public PubWarehouseCustomerRegionMapperEntity() {

	}
	
	/**
     * 自增ID
     */
	public Integer getId() {
		return this.id;
	}

    /**
     * 自增ID
     *
     * @param id
     */
	public void setId(Integer id) {
		this.id = id;
	}
	
	/**
     * 仓库ID
     */
	public String getWarehouseid() {
		return this.warehouseid;
	}

    /**
     * 仓库ID
     *
     * @param warehouseid
     */
	public void setWarehouseid(String warehouseid) {
		this.warehouseid = warehouseid;
	}
	
	/**
     * 客户编号
     */
	public String getCustomerid() {
		return this.customerid;
	}

    /**
     * 客户编号
     *
     * @param customerid
     */
	public void setCustomerid(String customerid) {
		this.customerid = customerid;
	}
	
	/**
     * 客户编码
     */
	public String getCustomerCode() {
		return this.customerCode;
	}

    /**
     * 客户编码
     *
     * @param customerCode
     */
	public void setCustomerCode(String customerCode) {
		this.customerCode = customerCode;
	}
	
	/**
     * 区域ID
     */
	public String getProvince() {
		return this.province;
	}

    /**
     * 区域ID
     *
     * @param province
     */
	public void setProvince(String province) {
		this.province = province;
	}
	
	/**
     * 删除标记
     */
	public Integer getDelflag() {
		return this.delflag;
	}

    /**
     * 删除标记
     *
     * @param delflag
     */
	public void setDelflag(Integer delflag) {
		this.delflag = delflag;
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
     * 创建者ID
     */
	public String getCrepersonid() {
		return this.crepersonid;
	}

    /**
     * 创建者ID
     *
     * @param crepersonid
     */
	public void setCrepersonid(String crepersonid) {
		this.crepersonid = crepersonid;
	}
	
	/**
     * 创建者
     */
	public String getCreperson() {
		return this.creperson;
	}

    /**
     * 创建者
     *
     * @param creperson
     */
	public void setCreperson(String creperson) {
		this.creperson = creperson;
	}
	
	/**
     * 创建时间
     */
	public Timestamp getCretime() {
		return this.cretime;
	}

    /**
     * 创建时间
     *
     * @param cretime
     */
	public void setCretime(Timestamp cretime) {
		this.cretime = cretime;
	}
	
	/**
     * 修改者ID
     */
	public String getModpersonid() {
		return this.modpersonid;
	}

    /**
     * 修改者ID
     *
     * @param modpersonid
     */
	public void setModpersonid(String modpersonid) {
		this.modpersonid = modpersonid;
	}
	
	/**
     * 修改者
     */
	public String getModperson() {
		return this.modperson;
	}

    /**
     * 修改者
     *
     * @param modperson
     */
	public void setModperson(String modperson) {
		this.modperson = modperson;
	}
	
	/**
     * 修改时间
     */
	public Timestamp getModtime() {
		return this.modtime;
	}

    /**
     * 修改时间
     *
     * @param modtime
     */
	public void setModtime(Timestamp modtime) {
		this.modtime = modtime;
	}
    
}
