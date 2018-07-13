package com.jiuyescm.bms.pub.warehouse.entity;

import com.jiuyescm.cfm.domain.IEntity;
import com.jiuyescm.bms.pub.info.entity.RegionEntity;

/**
 * 仓库
 */
public class WarehouseEntity implements IEntity {

	private static final long serialVersionUID = 3847070867488984820L;
	// 仓库ID
	private String warehouseid;
	// 仓库助记码
	private String warehousecode;
	// 仓库名称
	private String warehousename;
	// 区域ID
	private String regionid;
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
	// 是否产地虚拟仓
	private int virtualflag;
	// 删除标记
	private int delflag;
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
	
	/******************* 关联对象 *******************/
	//地址库
	private RegionEntity region;

	public WarehouseEntity() {

	}

	public String getWarehouseid() {
		return this.warehouseid;
	}

	public void setWarehouseid(String aWarehouseid) {
		this.warehouseid = aWarehouseid;
	}

	public String getWarehousecode() {
		return this.warehousecode;
	}

	public void setWarehousecode(String aWarehousecode) {
		this.warehousecode = aWarehousecode;
	}

	public String getWarehousename() {
		return this.warehousename;
	}

	public void setWarehousename(String aWarehousename) {
		this.warehousename = aWarehousename;
	}

	public String getRegionid() {
		return this.regionid;
	}

	public void setRegionid(String aRegionid) {
		this.regionid = aRegionid;
	}

	public String getLinkman() {
		return this.linkman;
	}

	public void setLinkman(String aLinkman) {
		this.linkman = aLinkman;
	}

	public String getTel() {
		return this.tel;
	}

	public void setTel(String aTel) {
		this.tel = aTel;
	}

	public String getMobile() {
		return this.mobile;
	}

	public void setMobile(String aMobile) {
		this.mobile = aMobile;
	}

	public String getAddress() {
		return this.address;
	}

	public void setAddress(String aAddress) {
		this.address = aAddress;
	}

	public String getZipcode() {
		return this.zipcode;
	}

	public void setZipcode(String aZipcode) {
		this.zipcode = aZipcode;
	}

	public int getVirtualflag() {
		return this.virtualflag;
	}

	public void setVirtualflag(int aVirtualflag) {
		this.virtualflag = aVirtualflag;
	}

	public int getDelflag() {
		return this.delflag;
	}

	public void setDelflag(int aDelflag) {
		this.delflag = aDelflag;
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
	
	public RegionEntity getRegion() {
		return region;
	}

	public void setRegion(RegionEntity region) {
		this.region = region;
	}

	public String toString() {
		StringBuffer returnString = new StringBuffer();
		returnString.append("com.jiuyescm.bms.pub.warehouse.entity.WarehouseEntity[");
		returnString.append("warehouseid = " + this.warehouseid + ";\n");
		returnString.append("warehousecode = " + this.warehousecode + ";\n");
		returnString.append("warehousename = " + this.warehousename + ";\n");
		returnString.append("regionid = " + this.regionid + ";\n");
		returnString.append("linkman = " + this.linkman + ";\n");
		returnString.append("tel = " + this.tel + ";\n");
		returnString.append("mobile = " + this.mobile + ";\n");
		returnString.append("address = " + this.address + ";\n");
		returnString.append("zipcode = " + this.zipcode + ";\n");
		returnString.append("virtualflag = " + this.virtualflag + ";\n");
		returnString.append("delflag = " + this.delflag + ";\n");
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
		if (warehouseid == null || "".equals(warehouseid.trim())) {
			return null;
		}
		return warehouseid;
	}
}