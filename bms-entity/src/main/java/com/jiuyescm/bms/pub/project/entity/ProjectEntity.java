package com.jiuyescm.bms.pub.project.entity;

import com.jiuyescm.cfm.domain.IEntity;

public class ProjectEntity implements IEntity {

	private static final long serialVersionUID = 500919576020732799L;

	// 项目ID
	private String projectid;
	// 项目助记码
	private String projectcode;
	// 项目简称
	private String shortname;
	// 项目名称
	private String projectname;
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
	private int delflag = 0;
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
	// 备注
	private String remark;
	
	private String proshortname; //临时字段，项目简称

	public ProjectEntity() {

	}

	public String getProjectid() {
		return projectid;
	}

	public void setProjectid(String projectid) {
		this.projectid = projectid;
	}
	public String getProjectcode() {
		return this.projectcode;
	}

	public void setProjectcode(String theProjectcode) {
		this.projectcode = theProjectcode;
	}

	public String getShortname() {
		return shortname;
	}

	public void setShortname(String shortname) {
		this.shortname = shortname;
	}

	public String getProjectname() {
		return this.projectname;
	}

	public void setProjectname(String theProjectname) {
		this.projectname = theProjectname;
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

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String theRemark) {
		this.remark = theRemark;
	}

	public String getProshortname() {
		return proshortname;
	}

	public void setProshortname(String proshortname) {
		this.proshortname = proshortname;
	}

	public String toString() {
		StringBuffer returnString = new StringBuffer();
		returnString.append("com.jiuyescm.bms.pub.project.entity.ProjectEntity[");
		returnString.append("projectid = " + this.projectid + ";\n");
		returnString.append("projectcode = " + this.projectcode + ";\n");
		returnString.append("shortname = " + this.shortname + ";\n");
		returnString.append("projectname = " + this.projectname + ";\n");
		returnString.append("linkman = " + this.linkman + ";\n");
		returnString.append("tel = " + this.tel + ";\n");
		returnString.append("mobile = " + this.mobile + ";\n");
		returnString.append("address = " + this.address + ";\n");
		returnString.append("zipcode = " + this.zipcode + ";\n");
		returnString.append("delflag = " + this.delflag + ";\n");
		returnString.append("crepersonid = " + this.crepersonid + ";\n");
		returnString.append("creperson = " + this.creperson + ";\n");
		returnString.append("cretime = " + this.cretime + ";\n");
		returnString.append("modpersonid = " + this.modpersonid + ";\n");
		returnString.append("modperson = " + this.modperson + ";\n");
		returnString.append("modtime = " + this.modtime + ";\n");
		returnString.append("remark = " + this.remark + ";\n");
		returnString.append("]\n");
		return returnString.toString();
	}

	/******************* 辅助方法 *******************/
	/*
	 * 实体Key字符
	 */
	public String getKey() {
//		if (projectid == 0) {
//			return null;
//		} else {
//			return String.valueOf(projectid);
//		}
		return projectid;
	}
}