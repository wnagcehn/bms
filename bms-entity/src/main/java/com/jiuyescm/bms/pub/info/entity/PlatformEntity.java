package com.jiuyescm.bms.pub.info.entity;

import com.jiuyescm.cfm.domain.IEntity;

/**
 * 平台信息
 */
public class PlatformEntity implements IEntity {

	private static final long serialVersionUID = 5412316285983411060L;
	// 平台ID
	private String platformid;
	// 平台编码
	private String platformcode;
	// 平台名称
	private String platformname;
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

	public PlatformEntity() {

	}

	public String getPlatformid() {
		return this.platformid;
	}

	public void setPlatformid(String aPlatformid) {
		this.platformid = aPlatformid;
	}

	public String getPlatformcode() {
		return this.platformcode;
	}

	public void setPlatformcode(String aPlatformcode) {
		this.platformcode = aPlatformcode;
	}

	public String getPlatformname() {
		return this.platformname;
	}

	public void setPlatformname(String aPlatformname) {
		this.platformname = aPlatformname;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String aRemark) {
		this.remark = aRemark;
	}

	public String getCrepersonid() {
		return this.crepersonid;
	}

	public void setCrepersonid(String aCrepersonid) {
		this.crepersonid = aCrepersonid;
	}

	public String getCreperson() {
		return this.creperson;
	}

	public void setCreperson(String aCreperson) {
		this.creperson = aCreperson;
	}

	public java.sql.Timestamp getCretime() {
		return this.cretime;
	}

	public void setCretime(java.sql.Timestamp aCretime) {
		this.cretime = aCretime;
	}

	public String getModpersonid() {
		return this.modpersonid;
	}

	public void setModpersonid(String aModpersonid) {
		this.modpersonid = aModpersonid;
	}

	public String getModperson() {
		return this.modperson;
	}

	public void setModperson(String aModperson) {
		this.modperson = aModperson;
	}

	public java.sql.Timestamp getModtime() {
		return this.modtime;
	}

	public void setModtime(java.sql.Timestamp aModtime) {
		this.modtime = aModtime;
	}

	public String toString() {
		StringBuffer returnString = new StringBuffer();
		returnString.append("com.jiuyescm.bms.pub.info.entity.PlatformEntity[");
		returnString.append("platformid = " + this.platformid + ";\n");
		returnString.append("platformcode = " + this.platformcode + ";\n");
		returnString.append("platformname = " + this.platformname + ";\n");
		returnString.append("remark = " + this.remark + ";\n");
		returnString.append("crepersonid = " + this.crepersonid + ";\n");
		returnString.append("creperson = " + this.creperson + ";\n");
		returnString.append("cretime = " + this.cretime + ";\n");
		returnString.append("modpersonid = " + this.modpersonid + ";\n");
		returnString.append("modperson = " + this.modperson + ";\n");
		returnString.append("modtime = " + this.modtime + ";\n");
		returnString.append("]\n");
		return returnString.toString();
	}

	/******************* 辅助方法 *******************/
	/*
	 * 实体Key字符
	 */
	public String getKey() {
		if (platformid == null || "".equals(platformid.trim())) {
			return null;
		}
		return platformid;
	}
}