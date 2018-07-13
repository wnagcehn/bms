/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.quotation.dispatch.entity;

import java.sql.Timestamp;
import com.jiuyescm.cfm.domain.IEntity;

/**
 * 
 * @author stevenl
 * 
 */
public class PubWarehouseEntity implements IEntity {

    // TODO Change serialVersionUID by eclipse
    private static final long serialVersionUID = -1;
    
	// 仓库ID
	private String warehouseid;
	// 仓库助记码
	private String warehousecode;
	// 仓库名称
	private String warehousename;
	// 区域ID
	private String regionid;
	// 联系人姓名
	private String linkman;
	// 联系人电话
	private String tel;
	// 联系人移动电话
	private String mobile;
	// 联系人地址
	private String address;
	// 邮编
	private String zipcode;
	// 是否产地虚拟仓
	private Integer virtualflag;
	// 删除标记
	private Integer delflag;
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

	public PubWarehouseEntity() {

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
     * 仓库助记码
     */
	public String getWarehousecode() {
		return this.warehousecode;
	}

    /**
     * 仓库助记码
     *
     * @param warehousecode
     */
	public void setWarehousecode(String warehousecode) {
		this.warehousecode = warehousecode;
	}
	
	/**
     * 仓库名称
     */
	public String getWarehousename() {
		return this.warehousename;
	}

    /**
     * 仓库名称
     *
     * @param warehousename
     */
	public void setWarehousename(String warehousename) {
		this.warehousename = warehousename;
	}
	
	/**
     * 区域ID
     */
	public String getRegionid() {
		return this.regionid;
	}

    /**
     * 区域ID
     *
     * @param regionid
     */
	public void setRegionid(String regionid) {
		this.regionid = regionid;
	}
	
	/**
     * 联系人姓名
     */
	public String getLinkman() {
		return this.linkman;
	}

    /**
     * 联系人姓名
     *
     * @param linkman
     */
	public void setLinkman(String linkman) {
		this.linkman = linkman;
	}
	
	/**
     * 联系人电话
     */
	public String getTel() {
		return this.tel;
	}

    /**
     * 联系人电话
     *
     * @param tel
     */
	public void setTel(String tel) {
		this.tel = tel;
	}
	
	/**
     * 联系人移动电话
     */
	public String getMobile() {
		return this.mobile;
	}

    /**
     * 联系人移动电话
     *
     * @param mobile
     */
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	
	/**
     * 联系人地址
     */
	public String getAddress() {
		return this.address;
	}

    /**
     * 联系人地址
     *
     * @param address
     */
	public void setAddress(String address) {
		this.address = address;
	}
	
	/**
     * 邮编
     */
	public String getZipcode() {
		return this.zipcode;
	}

    /**
     * 邮编
     *
     * @param zipcode
     */
	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}
	
	/**
     * 是否产地虚拟仓
     */
	public Integer getVirtualflag() {
		return this.virtualflag;
	}

    /**
     * 是否产地虚拟仓
     *
     * @param virtualflag
     */
	public void setVirtualflag(Integer virtualflag) {
		this.virtualflag = virtualflag;
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
