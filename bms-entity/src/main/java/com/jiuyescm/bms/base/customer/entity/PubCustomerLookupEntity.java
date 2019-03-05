package com.jiuyescm.bms.base.customer.entity;

import java.sql.Timestamp;

import com.jiuyescm.cfm.domain.IEntity;

 /**
 * ..Entity
 * @author liuzhicheng
 * 
 */
public class PubCustomerLookupEntity implements IEntity {
	private static final long serialVersionUID = -3385086537994228346L;
	// id
	private Integer id;
	// MkId
	private String mkInvoiceId;
	// CustomerId
	private String customerid;
	// CustomerName
	private String customername;
	// ShortName
	private String shortname;
	public String getShortname() {
		return shortname;
	}
	public void setShortname(String shortname) {
		this.shortname = shortname;
	}
	// creator
	private String creperson;
	// CreateTime
	private Timestamp cretime;
	// LastModifier
	private String modperson;
	// LastModifyTime
	private Timestamp modtime;
	// DelFlag
	private String delflag;
	// linkman
	private String linkman;
	// tel
	private String tel;
	// mobile
	private String mobile;
	// address
	private String address;
	// WriteTime
	private Timestamp writeTime;
	//合同归属
	private Integer contractAttr;
	//合同商家名称
	private String mkInvoiceName;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getMkInvoiceId() {
		return mkInvoiceId;
	}
	public void setMkInvoiceId(String mkInvoiceId) {
		this.mkInvoiceId = mkInvoiceId;
	}
	public String getCustomerid() {
		return customerid;
	}
	public void setCustomerid(String customerid) {
		this.customerid = customerid;
	}
	public String getCustomername() {
		return customername;
	}
	public void setCustomername(String customername) {
		this.customername = customername;
	}
	public String getCreperson() {
		return creperson;
	}
	public void setCreperson(String creperson) {
		this.creperson = creperson;
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
	public Timestamp getModtime() {
		return modtime;
	}
	public void setModtime(Timestamp modtime) {
		this.modtime = modtime;
	}
	public String getDelflag() {
		return delflag;
	}
	public void setDelflag(String delflag) {
		this.delflag = delflag;
	}
	public String getLinkman() {
		return linkman;
	}
	public void setLinkman(String linkman) {
		this.linkman = linkman;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public Timestamp getWriteTime() {
		return writeTime;
	}
	public void setWriteTime(Timestamp writeTime) {
		this.writeTime = writeTime;
	}
	public Integer getContractAttr() {
		return contractAttr;
	}
	public void setContractAttr(Integer contractAttr) {
		this.contractAttr = contractAttr;
	}
	public String getMkInvoiceName() {
		return mkInvoiceName;
	}
	public void setMkInvoiceName(String mkInvoiceName) {
		this.mkInvoiceName = mkInvoiceName;
	}
}
