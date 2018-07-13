package com.jiuyescm.bms.pub.deliver.entity;

import com.jiuyescm.cfm.domain.IEntity;

/**
 * 宅配商
 */
public class DeliverEntity implements IEntity {

	private static final long serialVersionUID = 2765039881378285618L;
	// 宅配商ID
	private String deliverid;
	// 宅配商名称
	private String delivername;
	// 宅配商编码
	private String delivercode;
	// 宅配商简称
	private String shortname;
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
	// 备注
	private String remark;
	// 创建者ID
	private String crepersonid;
	// 创建者
	private String creperson;
	// 创建时间
	private java.sql.Timestamp cretime;
	// 修改者ID
	private String modpersonid;
	// 修改者
	private String modperson;
	// 修改时间
	private java.sql.Timestamp modtime;

	//是否调用宅配
	private String iscall_station; 

	//是否支持拆箱
	private String issplitboxflag;
	//是否干线
	private String istrunkroute;
	
	public DeliverEntity() {

	}

	public String getDeliverid() {
		return this.deliverid;
	}

	public void setDeliverid(String theDeliverid) {
		this.deliverid = theDeliverid;
	}

	public String getDelivername() {
		return this.delivername;
	}

	public void setDelivername(String theDelivername) {
		this.delivername = theDelivername;
	}

	public String getDelivercode() {
		return delivercode;
	}

	public void setDelivercode(String delivercode) {
		this.delivercode = delivercode;
	}

	public String getShortname() {
		return this.shortname;
	}

	public void setShortname(String theShortname) {
		this.shortname = theShortname;
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

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String theRemark) {
		this.remark = theRemark;
	}

	public String getCrepersonid() {
		return this.crepersonid;
	}

	public void setCrepersonid(String theCrepersonid) {
		this.crepersonid = theCrepersonid;
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

	public String getModpersonid() {
		return this.modpersonid;
	}

	public void setModpersonid(String theModpersonid) {
		this.modpersonid = theModpersonid;
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
	
	public String getIscall_station() {
		return iscall_station;
	}

	public void setIscall_station(String iscall_station) {
		this.iscall_station = iscall_station;
	}

	public String getIssplitboxflag() {
		return issplitboxflag;
	}

	public void setIssplitboxflag(String issplitboxflag) {
		this.issplitboxflag = issplitboxflag;
	}
	
	
	

//	public String toString() {
//		StringBuffer returnString = new StringBuffer();
//		returnString.append("com.jiuyescm.bms.pub.deliver.entity.DeliverEntity[");
//		returnString.append("deliverid = " + this.deliverid + ";\n");
//		returnString.append("delivername = " + this.delivername + ";\n");
//		returnString.append("delivercode = " + this.delivercode + ";\n");
//		returnString.append("shortname = " + this.shortname + ";\n");
//		returnString.append("linkman = " + this.linkman + ";\n");
//		returnString.append("tel = " + this.tel + ";\n");
//		returnString.append("mobile = " + this.mobile + ";\n");
//		returnString.append("address = " + this.address + ";\n");
//		returnString.append("zipcode = " + this.zipcode + ";\n");
//		returnString.append("delflag = " + this.delflag + ";\n");
//		returnString.append("remark = " + this.remark + ";\n");
//		returnString.append("crepersonid = " + this.crepersonid + ";\n");
//		returnString.append("creperson = " + this.creperson + ";\n");
//		returnString.append("cretime = " + this.cretime + ";\n");
//		returnString.append("modpersonid = " + this.modpersonid + ";\n");
//		returnString.append("modperson = " + this.modperson + ";\n");
//		returnString.append("modtime = " + this.modtime + ";\n");
//		returnString.append("]\n");
//		return returnString.toString();
//	}

	public String getIstrunkroute() {
		return istrunkroute;
	}

	public void setIstrunkroute(String istrunkroute) {
		this.istrunkroute = istrunkroute;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	/******************* 辅助方法 *******************/
	/*
	 * 实体Key字符
	 */
	public String getKey() {
		if (deliverid == null || "".equals(deliverid.trim())) {
			return null;
		}
		return deliverid;
	}

}