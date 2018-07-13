/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.pub.zjs.entity;

import java.sql.Timestamp;

import com.jiuyescm.cfm.domain.IEntity;

/**
 * 
 * @author stevenl
 * 
 */
public class PubZjsExpressnumEntity implements IEntity {

    // TODO Change serialVersionUID by eclipse
    private static final long serialVersionUID = -1;
    
	// 主键ID
	private Long id;
	// 宅急送运单号
	private String expressnum;
	// 九耶运单号
	private String jyexpressnum;
	//订单号
	private String orderno;
	//大头笔 由: 目的城市区号 + 空格 + 走货方式 + 空格 + 分拣编码
	private String postCode;    //目的城市区号
	private String shipmentWay; //走货方式
	private String unitCode;    //分拣编码
	//是否禁航
	private String isEnbaled;
	// 作废标记
	private Integer delflag;
	// 使用状态
	private Integer useflag;
	// 创建人ID
	private String crepersonid;
	// 创建人
	private String creperson;
	// 创建时间
	private Timestamp cretime;
	// 修改时间
	private Timestamp modtime;
	// 是否已锁定
	private int lockflag;
	// 仓库编号
	private String warehouseid;
	

	public PubZjsExpressnumEntity() {

	}
	
	/**
     * 主键ID
     */
	public Long getId() {
		return this.id;
	}

    /**
     * 主键ID
     *
     * @param id
     */
	public void setId(Long id) {
		this.id = id;
	}
	
	/**
     * 宅急送运单号
     */
	public String getExpressnum() {
		return this.expressnum;
	}

    /**
     * 宅急送运单号
     *
     * @param expressnum
     */
	public void setExpressnum(String expressnum) {
		this.expressnum = expressnum;
	}
	
	/**
     * 作废标记
     */
	public Integer getDelflag() {
		return this.delflag;
	}

    /**
     * 作废标记
     *
     * @param delflag
     */
	public void setDelflag(Integer delflag) {
		this.delflag = delflag;
	}
	
	/**
     * 使用状态
     */
	public Integer getUseflag() {
		return this.useflag;
	}

    /**
     * 使用状态
     *
     * @param status
     */
	public void setUseflag(Integer useflag) {
		this.useflag = useflag;
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

	public String getJyexpressnum() {
		return jyexpressnum;
	}

	public void setJyexpressnum(String jyexpressnum) {
		this.jyexpressnum = jyexpressnum;
	}

	public String getOrderno() {
		return orderno;
	}

	public void setOrderno(String orderno) {
		this.orderno = orderno;
	}

	public Timestamp getModtime() {
		return modtime;
	}

	public void setModtime(Timestamp modtime) {
		this.modtime = modtime;
	}

	public String getPostCode() {
		return postCode;
	}

	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}

	public String getShipmentWay() {
		return shipmentWay;
	}

	public void setShipmentWay(String shipmentWay) {
		this.shipmentWay = shipmentWay;
	}

	public String getUnitCode() {
		return unitCode;
	}

	public void setUnitCode(String unitCode) {
		this.unitCode = unitCode;
	}

	public String getIsEnbaled() {
		return isEnbaled;
	}

	public void setIsEnbaled(String isEnbaled) {
		this.isEnbaled = isEnbaled;
	}

	public int getLockflag() {
		return lockflag;
	}

	public void setLockflag(int lockflag) {
		this.lockflag = lockflag;
	}

	public String getWarehouseid() {
		return warehouseid;
	}

	public void setWarehouseid(String warehouseid) {
		this.warehouseid = warehouseid;
	}
	
	
}
