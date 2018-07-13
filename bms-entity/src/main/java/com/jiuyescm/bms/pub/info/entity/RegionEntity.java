package com.jiuyescm.bms.pub.info.entity;

import com.jiuyescm.bms.pub.warehouse.entity.WarehouseEntity;
import com.jiuyescm.bms.pub.warehouse.entity.WarehouseRegionRuleEntity;


/**
 * 地址库
 */
public class RegionEntity  {
	// 区域ID
	private String regionid;
	// 区域编码
	private String regionno;
	// 区域助记码
	private String regioncode;
	// 省
	private String province;
	// 市
	private String city;
	// 区（县）
	private String district;
	// 乡（镇）
	private String town;
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
	//省代码
	private String provincecode;
	//市代码	
	private String citycode;
	//区(县)代码
	private String districtcode;
	//乡(镇)代码
	private String towncode;
	
	private String jianpin;
	
	// 仓库ID
	private String warehouseid;
	
	/******************* 关联对象 *******************/
	//仓库服务区域对照关系
	private WarehouseRegionRuleEntity warehouseRegionRule;
	//仓库
	private WarehouseEntity warehouse;
	
	private String carrierid;//物流商ID
	private String deliverid;//宅配商ID
	
	//区域
	private String areaText;
	
	private String areaTextInfo;
	
	private String pinyin;
	
	
	
	public RegionEntity() {

	}

	public String getRegionid() {
		return this.regionid;
	}

	public void setRegionid(String aRegionid) {
		this.regionid = aRegionid;
	}

	public String getRegionno() {
		return this.regionno;
	}

	public void setRegionno(String aRegionno) {
		this.regionno = aRegionno;
	}

	public String getRegioncode() {
		return this.regioncode;
	}

	public void setRegioncode(String aRegioncode) {
		this.regioncode = aRegioncode;
	}

	public String getProvince() {
		return this.province;
	}

	public void setProvince(String aProvince) {
		this.province = aProvince;
	}

	public String getCity() {
		return this.city;
	}

	public void setCity(String aCity) {
		this.city = aCity;
	}

	public String getDistrict() {
		return this.district;
	}

	public void setDistrict(String aDistrict) {
		this.district = aDistrict;
	}

	public String getTown() {
		return this.town;
	}

	public void setTown(String aTown) {
		this.town = aTown;
	}

	public int getDelflag() {
		return this.delflag;
	}

	public void setDelflag(int aDelflag) {
		this.delflag = aDelflag;
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

	public  String getProvincecode() {
		return provincecode;
	}

	public  void setProvincecode(String provincecode) {
		this.provincecode = provincecode;
	}

	public  String getCitycode() {
		return citycode;
	}

	public  void setCitycode(String citycode) {
		this.citycode = citycode;
	}

	public  String getDistrictcode() {
		return districtcode;
	}

	public  void setDistrictcode(String districtcode) {
		this.districtcode = districtcode;
	}

	public String getTowncode() {
		return towncode;
	}

	public void setTowncode(String towncode) {
		this.towncode = towncode;
	}

	
//	public String toString() {
//		StringBuffer returnString = new StringBuffer();
//		returnString.append("com.jiuyescm.bms.pub.info.entity.RegionEntity[");
//		returnString.append("regionid = " + this.regionid + ";\n");
//		returnString.append("regionno = " + this.regionno + ";\n");
//		returnString.append("regioncode = " + this.regioncode + ";\n");
//		returnString.append("province = " + this.province + ";\n");
//		returnString.append("city = " + this.city + ";\n");
//		returnString.append("district = " + this.district + ";\n");
//		returnString.append("town = " + this.town + ";\n");
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

	public String getWarehouseid() {
		return this.warehouseid;
	}

	public void setWarehouseid(String aWarehouseid) {
		this.warehouseid = aWarehouseid;
	}
	
	public WarehouseRegionRuleEntity getWarehouseRegionRule() {
		return warehouseRegionRule;
	}

	public void setWarehouseRegionRule(WarehouseRegionRuleEntity warehouseRegionRule) {
		this.warehouseRegionRule = warehouseRegionRule;
	}

	public WarehouseEntity getWarehouse() {
		return warehouse;
	}

	public void setWarehouse(WarehouseEntity warehouse) {
		this.warehouse = warehouse;
	}

	public String getCarrierid() {
		return carrierid;
	}

	public void setCarrierid(String carrierid) {
		this.carrierid = carrierid;
	}

	public String getDeliverid() {
		return deliverid;
	}

	public void setDeliverid(String deliverid) {
		this.deliverid = deliverid;
	}

	public String getAreaText() {
		return areaText;
	}

	public void setAreaText(String areaText) {
		this.areaText = areaText;
	}
	
	

	public String getAreaTextInfo() {
		return areaTextInfo;
	}

	public void setAreaTextInfo(String areaTextInfo) {
		this.areaTextInfo = areaTextInfo;
	}

	public String getJianpin() {
		return jianpin;
	}

	public void setJianpin(String jianpin) {
		this.jianpin = jianpin;
	}

	public String getPinyin() {
		return pinyin;
	}

	public void setPinyin(String pinyin) {
		this.pinyin = pinyin;
	}

	/******************* 辅助方法 *******************/
	/*
	 * 实体Key字符
	 */
	public String getKey() {
		if (regionid == null || "".equals(regionid.trim())) {
			return null;
		}
		return regionid;
	}
}