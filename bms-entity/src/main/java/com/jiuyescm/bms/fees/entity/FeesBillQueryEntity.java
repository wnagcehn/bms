package com.jiuyescm.bms.fees.entity;

import java.sql.Timestamp;

import com.jiuyescm.cfm.domain.IEntity;
/**
 * 
 * @author wuliangfeng 账单查询实体
 *
 */
public class FeesBillQueryEntity implements IEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5192230242378621419L;
	private String billno;//账单编号
	private String billname;//账单名称
	private Timestamp billstarttime;//账单起始时间
	private Timestamp billendtime;//账单结束时间
	private String customerid;//商家ID
	private double totleprice;//总金额
	private int billstatus;//结算状态 0-未结算 1-已结算
	private int delflag;//订单状态0-未作废 1-作废
	private String creperson;//创建人ID
	private String crepersonname;//创建人姓名
	private Timestamp cretime;//创建时间
	private Timestamp crestime;//创建时间
	private Timestamp creetime;//创建时间止
	public String getBillno() {
		return billno;
	}
	public void setBillno(String billno) {
		this.billno = billno;
	}
	public String getBillname() {
		return billname;
	}
	public void setBillname(String billname) {
		this.billname = billname;
	}
	public Timestamp getBillstarttime() {
		return billstarttime;
	}
	public void setBillstarttime(Timestamp billstarttime) {
		this.billstarttime = billstarttime;
	}
	public Timestamp getBillendtime() {
		return billendtime;
	}
	public void setBillendtime(Timestamp billendtime) {
		this.billendtime = billendtime;
	}
	public String getCustomerid() {
		return customerid;
	}
	public void setCustomerid(String customerid) {
		this.customerid = customerid;
	}
	public double getTotleprice() {
		return totleprice;
	}
	public void setTotleprice(double totleprice) {
		this.totleprice = totleprice;
	}
	public int getBillstatus() {
		return billstatus;
	}
	public void setBillstatus(int billstatus) {
		this.billstatus = billstatus;
	}
	public int getDelflag() {
		return delflag;
	}
	public void setDelflag(int delflag) {
		this.delflag = delflag;
	}
	public String getCreperson() {
		return creperson;
	}
	public void setCreperson(String creperson) {
		this.creperson = creperson;
	}
	public String getCrepersonname() {
		return crepersonname;
	}
	public void setCrepersonname(String crepersonname) {
		this.crepersonname = crepersonname;
	}
	public Timestamp getCretime() {
		return cretime;
	}
	public void setCretime(Timestamp cretime) {
		this.cretime = cretime;
	}
	public Timestamp getCrestime() {
		return crestime;
	}
	public void setCrestime(Timestamp crestime) {
		this.crestime = crestime;
	}
	public Timestamp getCreetime() {
		return creetime;
	}
	public void setCreetime(Timestamp creetime) {
		this.creetime = creetime;
	}

}
