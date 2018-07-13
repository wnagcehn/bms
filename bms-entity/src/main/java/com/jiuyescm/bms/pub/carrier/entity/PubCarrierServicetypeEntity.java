/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.pub.carrier.entity;

import java.sql.Timestamp;
import com.jiuyescm.cfm.domain.IEntity;

/**
 * 
 * @author stevenl
 * 
 */
public class PubCarrierServicetypeEntity implements IEntity {

    
	/**
	 * 
	 */
	private static final long serialVersionUID = -3470603784659624351L;
	// 主键ID
	private Integer id;
	// 物流商ID
	private String carrierid;
	// 物流商编码
	private String carriercode;
	// 服务类型编码
	private String servicecode;
	// 服务类型名称
	private String servicename;
	// 备注
	private String remark;
	// 作废标记
	private String delflag;
	// 创建人
	private String creperson;
	// 创建时间
	private Timestamp cretime;
	// 修改人
	private String modperson;
	// 修改时间
	private Timestamp modtime;
	// 创建人ID
	private String crepersonid;
	// 修改人ID
	private String modpersonid;

	public PubCarrierServicetypeEntity() {

	}
	
	/**
     * 主键ID
     */
	public Integer getId() {
		return this.id;
	}

    /**
     * 主键ID
     *
     * @param id
     */
	public void setId(Integer id) {
		this.id = id;
	}
	
	/**
     * 物流商ID
     */
	public String getCarrierid() {
		return this.carrierid;
	}

    /**
     * 物流商ID
     *
     * @param carrierid
     */
	public void setCarrierid(String carrierid) {
		this.carrierid = carrierid;
	}
	
	/**
     * 物流商编码
     */
	public String getCarriercode() {
		return this.carriercode;
	}

    /**
     * 物流商编码
     *
     * @param carriercode
     */
	public void setCarriercode(String carriercode) {
		this.carriercode = carriercode;
	}
	
	/**
     * 服务类型编码
     */
	public String getServicecode() {
		return this.servicecode;
	}

    /**
     * 服务类型编码
     *
     * @param servicecode
     */
	public void setServicecode(String servicecode) {
		this.servicecode = servicecode;
	}
	
	/**
     * 服务类型名称
     */
	public String getServicename() {
		return this.servicename;
	}

    /**
     * 服务类型名称
     *
     * @param servicename
     */
	public void setServicename(String servicename) {
		this.servicename = servicename;
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
     * 作废标记
     */
	public String getDelflag() {
		return this.delflag;
	}

    /**
     * 作废标记
     *
     * @param delflag
     */
	public void setDelflag(String delflag) {
		this.delflag = delflag;
	}
	
	/**
     * 创建人
     */
	public String getCreperson() {
		return this.creperson;
	}

    /**
     * 创建人
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
     * 修改人
     */
	public String getModperson() {
		return this.modperson;
	}

    /**
     * 修改人
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
	
	/**
     * 创建人ID
     */
	public String getCrepersonid() {
		return this.crepersonid;
	}

    /**
     * 创建人ID
     *
     * @param crepersonid
     */
	public void setCrepersonid(String crepersonid) {
		this.crepersonid = crepersonid;
	}
	
	/**
     * 修改人ID
     */
	public String getModpersonid() {
		return this.modpersonid;
	}

    /**
     * 修改人ID
     *
     * @param modpersonid
     */
	public void setModpersonid(String modpersonid) {
		this.modpersonid = modpersonid;
	}
    
}
