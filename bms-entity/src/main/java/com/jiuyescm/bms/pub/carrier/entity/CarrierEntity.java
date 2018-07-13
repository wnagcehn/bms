package com.jiuyescm.bms.pub.carrier.entity;

import com.jiuyescm.cfm.domain.IEntity;

public class CarrierEntity implements IEntity {

	private static final long serialVersionUID = 4835833820538356644L;

	// 物流商ID
	private String carrierid;
	// 物流商助记码
	private String carriercode;
	// 物流商简称
	private String shortname;
	// 物流商名称
	private String name;
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
	// 删除标记
	private int delflag;
	// 创建人
	private String creperson;
	// 创建时间
	private java.sql.Timestamp cretime;
	// 更新人
	private String modperson;
	// 更新时间
	private java.sql.Timestamp modtime;
	// 创建人ID
	private String crepersonid;
	// 更新人ID
	private String modpersonid;
	
	private String expressaddrule; //JY:久曳;SF:顺丰

	public CarrierEntity() {

	}

	public String getCarrierid() {
		return this.carrierid;
	}

	public void setCarrierid(String theCarrierid) {
		this.carrierid = theCarrierid;
	}

	public String getCarriercode() {
		return this.carriercode;
	}

	public void setCarriercode(String theCarriercode) {
		this.carriercode = theCarriercode;
	}

	public String getShortname() {
		return this.shortname;
	}

	public void setShortname(String theShortname) {
		this.shortname = theShortname;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String theName) {
		this.name = theName;
	}

	public String getLinkman() {
		return this.linkman;
	}

	public void setLinkman(String theLinkman) {
		this.linkman = theLinkman;
	}

	public String getTel() {
		return this.tel;
	}

	public void setTel(String theTel) {
		this.tel = theTel;
	}

	public String getMobile() {
		return this.mobile;
	}

	public void setMobile(String theMobile) {
		this.mobile = theMobile;
	}

	public String getAddress() {
		return this.address;
	}

	public void setAddress(String theAddress) {
		this.address = theAddress;
	}

	public String getZipcode() {
		return this.zipcode;
	}

	public void setZipcode(String theZipcode) {
		this.zipcode = theZipcode;
	}

	public int getDelflag() {
		return this.delflag;
	}

	public void setDelflag(int theDelflag) {
		this.delflag = theDelflag;
	}

	public String getCreperson() {
		return this.creperson;
	}

	public void setCreperson(String theCreperson) {
		this.creperson = theCreperson;
	}

	public java.sql.Timestamp getCretime() {
		return this.cretime;
	}

	public void setCretime(java.sql.Timestamp theCretime) {
		this.cretime = theCretime;
	}

	public String getModperson() {
		return this.modperson;
	}

	public void setModperson(String theModperson) {
		this.modperson = theModperson;
	}

	public java.sql.Timestamp getModtime() {
		return this.modtime;
	}

	public void setModtime(java.sql.Timestamp theModtime) {
		this.modtime = theModtime;
	}

	public String getCrepersonid() {
		return this.crepersonid;
	}

	public void setCrepersonid(String theCrepersonid) {
		this.crepersonid = theCrepersonid;
	}

	public String getModpersonid() {
		return this.modpersonid;
	}

	public void setModpersonid(String theModpersonid) {
		this.modpersonid = theModpersonid;
	}

	public String toString() {
		StringBuffer returnString = new StringBuffer();
		returnString.append("com.jiuyescm.bms.pub.carrier.entity.CarrierEntity[");
		returnString.append("carrierid = " + this.carrierid + ";\n");
		returnString.append("carriercode = " + this.carriercode + ";\n");
		returnString.append("shortname = " + this.shortname + ";\n");
		returnString.append("name = " + this.name + ";\n");
		returnString.append("linkman = " + this.linkman + ";\n");
		returnString.append("tel = " + this.tel + ";\n");
		returnString.append("mobile = " + this.mobile + ";\n");
		returnString.append("address = " + this.address + ";\n");
		returnString.append("zipcode = " + this.zipcode + ";\n");
		returnString.append("delflag = " + this.delflag + ";\n");
		returnString.append("creperson = " + this.creperson + ";\n");
		returnString.append("cretime = " + this.cretime + ";\n");
		returnString.append("modperson = " + this.modperson + ";\n");
		returnString.append("modtime = " + this.modtime + ";\n");
		returnString.append("crepersonid = " + this.crepersonid + ";\n");
		returnString.append("modpersonid = " + this.modpersonid + ";\n");
		returnString.append("]\n");
		return returnString.toString();
	}

	/******************* 辅助方法 *******************/
	/*
	 * 实体Key字符
	 */
	public String getKey() {
		if (carrierid == null || "".equals(carrierid.trim())) {
			return null;
		}
		return carrierid;
	}

	public String getExpressaddrule() {
		return expressaddrule;
	}

	public void setExpressaddrule(String expressaddrule) {
		this.expressaddrule = expressaddrule;
	}
	
}