package com.jiuyescm.bms.fees.entity;

import java.sql.Timestamp;

import com.jiuyescm.cfm.domain.IEntity;

/**
 * 
 * @author wuliangfeng 账单实体 20170601
 * 
 */
public class FeesBillEntity implements IEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8968420370324131241L;

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

	public String getModperson() {
		return modperson;
	}

	public void setModperson(String modperson) {
		this.modperson = modperson;
	}

	public String getModpersonname() {
		return modpersonname;
	}

	public void setModpersonname(String modpersonname) {
		this.modpersonname = modpersonname;
	}

	public Timestamp getModtime() {
		return modtime;
	}

	public void setModtime(Timestamp modtime) {
		this.modtime = modtime;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public Double getReceiptAmount() {
		return receiptAmount;
	}

	public void setReceiptAmount(Double receiptAmount) {
		this.receiptAmount = receiptAmount;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getFeesNo() {
		return feesNo;
	}

	public void setFeesNo(String feesNo) {
		this.feesNo = feesNo;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	private Integer id;
	private String billno;// 账单编号
	private String billname;// 账单名称
	private Timestamp billstarttime;// 账单起始时间
	private Timestamp billendtime;// 账单结束时间
	private String customerid;// 商家ID
	// 商家名称
	private String customerName;
	private double totleprice;// 总金额
	// 实收金额
	private Double receiptAmount;
	// 备注
	private String remark;
	private int billstatus;// 结算状态 0-未结算 1-已结算
	private int delflag;// 订单状态0-未作废 1-作废
	private String creperson;// 创建人ID
	private String crepersonname;// 创建人姓名
	private Timestamp cretime;// 创建时间
	private String modperson;// 修改人ID
	private String modpersonname;// 修改人姓名
	private Timestamp modtime;// 修改时间
	
	/**
	 * 剔除账单使用
	 */
	private String feesNo;
}
