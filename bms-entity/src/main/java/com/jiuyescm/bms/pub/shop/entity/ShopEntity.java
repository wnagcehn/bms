package com.jiuyescm.bms.pub.shop.entity;

import com.jiuyescm.cfm.domain.IEntity;
import com.jiuyescm.bms.pub.info.entity.PlatformEntity;

public class ShopEntity implements IEntity {

	private static final long serialVersionUID = 7007107007617911848L;

	// 店铺ID
	private String shopid;
	// 店铺名称
	private String shopname;
	// 店铺助记码
	private String shopcode;
	// 平台ID
	private int platformid;
	// 店铺登录账号
	private String shopaccount;
	// 店铺登录密码
	private String shoppwd;
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
	//平台信息
	private PlatformEntity platform;
	
	public ShopEntity() {

	}

	public String getShopid() {
		return this.shopid;
	}

	public void setShopid(String theShopid) {
		this.shopid = theShopid;
	}

	public String getShopname() {
		return this.shopname;
	}

	public void setShopname(String theShopname) {
		this.shopname = theShopname;
	}

	public String getShopcode() {
		return this.shopcode;
	}

	public void setShopcode(String theShopcode) {
		this.shopcode = theShopcode;
	}

	public int getPlatformid() {
		return this.platformid;
	}

	public void setPlatformid(int thePlatformid) {
		this.platformid = thePlatformid;
	}

	public String getShopaccount() {
		return this.shopaccount;
	}

	public void setShopaccount(String theShopaccount) {
		this.shopaccount = theShopaccount;
	}

	public String getShoppwd() {
		return this.shoppwd;
	}

	public void setShoppwd(String theShoppwd) {
		this.shoppwd = theShoppwd;
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

	public final PlatformEntity getPlatform() {
		return platform;
	}

	public final void setPlatform(PlatformEntity platform) {
		this.platform = platform;
	}

	public String toString() {
		StringBuffer returnString = new StringBuffer();
		returnString.append("com.jiuyescm.bms.pub.shop.entity.ShopEntity[");
		returnString.append("shopid = " + this.shopid + ";\n");
		returnString.append("shopname = " + this.shopname + ";\n");
		returnString.append("shopcode = " + this.shopcode + ";\n");
		returnString.append("platformid = " + this.platformid + ";\n");
		returnString.append("shopaccount = " + this.shopaccount + ";\n");
		returnString.append("shoppwd = " + this.shoppwd + ";\n");
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
		returnString.append("]\n");
		return returnString.toString();
	}

	/******************* 辅助方法 *******************/
	/*
	 * 实体Key字符
	 */
	public String getKey() {
		if (shopid == null || "".equals(shopid.trim())) {
			return null;
		}
		return shopid;
	}
}