package com.jiuyescm.bms.bill.customer.vo;

import java.io.Serializable;
import java.sql.Timestamp;

public class BillCustomerInfoVo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2962059823814094997L;

	private int id;
	private String customerId;
	private String customerName;
	private String customerShortname;
	private String linkman;
	private String address;
	private String tel;
	private String phone;
	private String remark;
	private String delFlag;
	private String creator;
	private Timestamp createTime;
	private String lastModifier;
	private Timestamp lastModifyTime;
	private String sysCustomerId;
	private String sysCustomerName;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getCustomerShortname() {
		return customerShortname;
	}
	public void setCustomerShortname(String customerShortname) {
		this.customerShortname = customerShortname;
	}
	public String getLinkman() {
		return linkman;
	}
	public void setLinkman(String linkman) {
		this.linkman = linkman;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getDelFlag() {
		return delFlag;
	}
	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	public Timestamp getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	public String getLastModifier() {
		return lastModifier;
	}
	public void setLastModifier(String lastModifier) {
		this.lastModifier = lastModifier;
	}
	public Timestamp getLastModifyTime() {
		return lastModifyTime;
	}
	public void setLastModifyTime(Timestamp lastModifyTime) {
		this.lastModifyTime = lastModifyTime;
	}
	public String getSysCustomerName() {
		return sysCustomerName;
	}
	public void setSysCustomerName(String sysCustomerName) {
		this.sysCustomerName = sysCustomerName;
	}
	public String getSysCustomerId() {
		return sysCustomerId;
	}
	public void setSysCustomerId(String sysCustomerId) {
		this.sysCustomerId = sysCustomerId;
	}
	
}
