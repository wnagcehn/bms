package com.jiuyescm.bms.base.jywarehouse.web;

import java.io.Serializable;

public class BmsWarehouseVo implements Serializable,Comparable<BmsWarehouseVo> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4662292443947281042L;
	private Integer displayLevel;
	private int isDropDisplay;//1 不显示 0-显示
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
	//省
	private String province;
	//市
	private String city;
	
	public Integer getDisplayLevel() {
		return displayLevel;
	}
	public void setDisplayLevel(Integer displayLevel) {
		this.displayLevel = displayLevel;
	}
	public int getIsDropDisplay() {
		return isDropDisplay;
	}
	public void setIsDropDisplay(int isDropDisplay) {
		this.isDropDisplay = isDropDisplay;
	}
	public String getWarehouseid() {
		return warehouseid;
	}
	public void setWarehouseid(String warehouseid) {
		this.warehouseid = warehouseid;
	}
	public String getWarehousecode() {
		return warehousecode;
	}
	public void setWarehousecode(String warehousecode) {
		this.warehousecode = warehousecode;
	}
	public String getWarehousename() {
		return warehousename;
	}
	public void setWarehousename(String warehousename) {
		this.warehousename = warehousename;
	}
	public String getRegionid() {
		return regionid;
	}
	public void setRegionid(String regionid) {
		this.regionid = regionid;
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
	public String getZipcode() {
		return zipcode;
	}
	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}
	public int getVirtualflag() {
		return virtualflag;
	}
	public void setVirtualflag(int virtualflag) {
		this.virtualflag = virtualflag;
	}
	public int getDelflag() {
		return delflag;
	}
	public void setDelflag(int delflag) {
		this.delflag = delflag;
	}
	public String getCrepersonid() {
		return crepersonid;
	}
	public void setCrepersonid(String crepersonid) {
		this.crepersonid = crepersonid;
	}
	public String getCreperson() {
		return creperson;
	}
	public void setCreperson(String creperson) {
		this.creperson = creperson;
	}
	public java.sql.Timestamp getCretime() {
		return cretime;
	}
	public void setCretime(java.sql.Timestamp cretime) {
		this.cretime = cretime;
	}
	public String getModpersonid() {
		return modpersonid;
	}
	public void setModpersonid(String modpersonid) {
		this.modpersonid = modpersonid;
	}
	public String getModperson() {
		return modperson;
	}
	public void setModperson(String modperson) {
		this.modperson = modperson;
	}
	public java.sql.Timestamp getModtime() {
		return modtime;
	}
	public void setModtime(java.sql.Timestamp modtime) {
		this.modtime = modtime;
	}
	
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	@Override
	public int compareTo(BmsWarehouseVo o) {
		return this.getDisplayLevel().compareTo(o.getDisplayLevel());
	}
	
}
