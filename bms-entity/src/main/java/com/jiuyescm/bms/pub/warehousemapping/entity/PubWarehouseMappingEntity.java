/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.pub.warehousemapping.entity;

import java.sql.Timestamp;
import com.jiuyescm.cfm.domain.IEntity;

/**
 * 
 * @author stevenl
 * 
 */
public class PubWarehouseMappingEntity implements IEntity {

    // TODO Change serialVersionUID by eclipse
    private static final long serialVersionUID = -1;
    
	// 标识
	private String id;
	// 仓库ID
	private String warehouseid;
	// 仓库ID
	private String warehousecode;
	// 外部仓库编号
	private String externalwarehouseid;
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
	// 下发dms任务id
	private String todmsjobid;
	// 分物流商宅配商任务id
	private String carrierdeliverjobid;
	// OMS仓库名称
	private String warehousename;

	public PubWarehouseMappingEntity() {

	}
	
	public String getWarehousename() {
		return warehousename;
	}

	public void setWarehousename(String warehousename) {
		this.warehousename = warehousename;
	}

	/**
     * 标识
     */
	public String getId() {
		return this.id;
	}

    /**
     * 标识
     *
     * @param id
     */
	public void setId(String id) {
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
     * 外部仓库编号
     */
	public String getExternalwarehouseid() {
		return this.externalwarehouseid;
	}

    /**
     * 外部仓库编号
     *
     * @param externalwarehouseid
     */
	public void setExternalwarehouseid(String externalwarehouseid) {
		this.externalwarehouseid = externalwarehouseid;
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

	public String getTodmsjobid() {
		return todmsjobid;
	}

	public void setTodmsjobid(String todmsjobid) {
		this.todmsjobid = todmsjobid;
	}

	public String getWarehousecode() {
		return warehousecode;
	}

	public void setWarehousecode(String warehousecode) {
		this.warehousecode = warehousecode;
	}

	public String getCarrierdeliverjobid() {
		return carrierdeliverjobid;
	}

	public void setCarrierdeliverjobid(String carrierdeliverjobid) {
		this.carrierdeliverjobid = carrierdeliverjobid;
	}
	
    
}
