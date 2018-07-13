/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.pub.info.entity;

import java.sql.Timestamp;

import com.jiuyescm.cfm.domain.IEntity;

/**
 * 
 * @author cjw
 * 
 */
public class OmsStockOutInfoEntity implements IEntity {

    private static final long serialVersionUID = -1;
    
	// orderno
	private String orderno;
	// reference
	private String reference;
	// expressnum
	private String expressnum;
	// customerid
	private String customerid;
	// shortname
	private String shortname;
	// carrierid
	private String carrierid;
	// carriername
	private String carriername;
	
	private String warehouseId;
	private String warehousename;
	private String deliverid;
	private String delivername;
	private String receiverlinkman;
	private String receivermobile;
	private String address;
	private Timestamp cretime;
	

	public OmsStockOutInfoEntity() {

	}
	
	/**
     * orderno
     */
	public String getOrderno() {
		return this.orderno;
	}

    /**
     * orderno
     *
     * @param orderno
     */
	public void setOrderno(String orderno) {
		this.orderno = orderno;
	}
	
	/**
     * reference
     */
	public String getReference() {
		return this.reference;
	}

    /**
     * reference
     *
     * @param reference
     */
	public void setReference(String reference) {
		this.reference = reference;
	}
	
	/**
     * expressnum
     */
	public String getExpressnum() {
		return this.expressnum;
	}

    /**
     * expressnum
     *
     * @param expressnum
     */
	public void setExpressnum(String expressnum) {
		this.expressnum = expressnum;
	}
	
	/**
     * customerid
     */
	public String getCustomerid() {
		return this.customerid;
	}

    /**
     * customerid
     *
     * @param customerid
     */
	public void setCustomerid(String customerid) {
		this.customerid = customerid;
	}
	
	/**
     * shortname
     */
	public String getShortname() {
		return this.shortname;
	}

    /**
     * shortname
     *
     * @param shortname
     */
	public void setShortname(String shortname) {
		this.shortname = shortname;
	}
	
	/**
     * carrierid
     */
	public String getCarrierid() {
		return this.carrierid;
	}

    /**
     * carrierid
     *
     * @param carrierid
     */
	public void setCarrierid(String carrierid) {
		this.carrierid = carrierid;
	}
	
	/**
     * carriername
     */
	public String getCarriername() {
		return this.carriername;
	}

    /**
     * carriername
     *
     * @param carriername
     */
	public void setCarriername(String carriername) {
		this.carriername = carriername;
	}

	public String getWarehouseId() {
		return warehouseId;
	}

	public void setWarehouseId(String warehouseId) {
		this.warehouseId = warehouseId;
	}

	public String getWarehousename() {
		return warehousename;
	}

	public void setWarehousename(String warehousename) {
		this.warehousename = warehousename;
	}

	public String getDeliverid() {
		return deliverid;
	}

	public void setDeliverid(String deliverid) {
		this.deliverid = deliverid;
	}

	public String getDelivername() {
		return delivername;
	}

	public void setDelivername(String delivername) {
		this.delivername = delivername;
	}

	public String getReceiverlinkman() {
		return receiverlinkman;
	}

	public void setReceiverlinkman(String receiverlinkman) {
		this.receiverlinkman = receiverlinkman;
	}

	public String getReceivermobile() {
		return receivermobile;
	}

	public void setReceivermobile(String receivermobile) {
		this.receivermobile = receivermobile;
	}

	public Timestamp getCretime() {
		return cretime;
	}

	public void setCretime(Timestamp cretime) {
		this.cretime = cretime;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
    
}
