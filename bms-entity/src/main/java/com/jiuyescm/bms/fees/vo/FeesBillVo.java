/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.fees.vo;

import java.sql.Timestamp;

import com.jiuyescm.cfm.domain.IEntity;

/**
 * 
 * @author stevenl
 * 
 */
public class FeesBillVo implements IEntity {

	private static final long serialVersionUID = 330583296310493661L;
	
	// 账单编号
	private String billno;
	// 账单名称
	private String billname;
	// 账单起始时间
	private Timestamp billstarttime;
	// 账单结束时间
	private Timestamp billendtime;
	// 商家id
	private String customerid;
	// 总金额
	private Double totleprice;
	// 0-未结算 1-已结算
	private String billstatus;
	// 0-未作废 1-作废
	private String delflag;
	// 创建人ID
	private String creperson;
	// 创建人姓名
	private String crepersonname;
	// 创建时间
	private Timestamp cretime;
	// 修改人ID
	private String modperson;
	// 修改人姓名
	private String modpersonname;
	// 修改时间
	private Timestamp modtime;

	public FeesBillVo() {

	}
	
	/**
     * 账单编号
     */
	public String getBillno() {
		return this.billno;
	}

    /**
     * 账单编号
     *
     * @param billno
     */
	public void setBillno(String billno) {
		this.billno = billno;
	}
	
	/**
     * 账单名称
     */
	public String getBillname() {
		return this.billname;
	}

    /**
     * 账单名称
     *
     * @param billname
     */
	public void setBillname(String billname) {
		this.billname = billname;
	}
	
	/**
     * 账单起始时间
     */
	public Timestamp getBillstarttime() {
		return this.billstarttime;
	}

    /**
     * 账单起始时间
     *
     * @param billstarttime
     */
	public void setBillstarttime(Timestamp billstarttime) {
		this.billstarttime = billstarttime;
	}
	
	/**
     * 账单结束时间
     */
	public Timestamp getBillendtime() {
		return this.billendtime;
	}

    /**
     * 账单结束时间
     *
     * @param billendtime
     */
	public void setBillendtime(Timestamp billendtime) {
		this.billendtime = billendtime;
	}
	
	/**
     * 商家id
     */
	public String getCustomerid() {
		return this.customerid;
	}

    /**
     * 商家id
     *
     * @param customerid
     */
	public void setCustomerid(String customerid) {
		this.customerid = customerid;
	}
	
	/**
     * 总金额
     */
	public Double getTotleprice() {
		return this.totleprice;
	}

    /**
     * 总金额
     *
     * @param totleprice
     */
	public void setTotleprice(Double totleprice) {
		this.totleprice = totleprice;
	}
	
	/**
     * 0-未结算 1-已结算
     */
	public String getBillstatus() {
		return this.billstatus;
	}

    /**
     * 0-未结算 1-已结算
     *
     * @param billstatus
     */
	public void setBillstatus(String billstatus) {
		this.billstatus = billstatus;
	}
	
	/**
     * 0-未作废 1-作废
     */
	public String getDelflag() {
		return this.delflag;
	}

    /**
     * 0-未作废 1-作废
     *
     * @param delflag
     */
	public void setDelflag(String delflag) {
		this.delflag = delflag;
	}
	
	/**
     * 创建人ID
     */
	public String getCreperson() {
		return this.creperson;
	}

    /**
     * 创建人ID
     *
     * @param creperson
     */
	public void setCreperson(String creperson) {
		this.creperson = creperson;
	}
	
	/**
     * 创建人姓名
     */
	public String getCrepersonname() {
		return this.crepersonname;
	}

    /**
     * 创建人姓名
     *
     * @param crepersonname
     */
	public void setCrepersonname(String crepersonname) {
		this.crepersonname = crepersonname;
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
     * 修改人ID
     */
	public String getModperson() {
		return this.modperson;
	}

    /**
     * 修改人ID
     *
     * @param modperson
     */
	public void setModperson(String modperson) {
		this.modperson = modperson;
	}
	
	/**
     * 修改人姓名
     */
	public String getModpersonname() {
		return this.modpersonname;
	}

    /**
     * 修改人姓名
     *
     * @param modpersonname
     */
	public void setModpersonname(String modpersonname) {
		this.modpersonname = modpersonname;
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
