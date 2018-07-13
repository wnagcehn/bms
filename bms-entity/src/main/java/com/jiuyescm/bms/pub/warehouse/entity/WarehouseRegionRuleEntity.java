package com.jiuyescm.bms.pub.warehouse.entity;

import com.jiuyescm.cfm.domain.IEntity;
import com.jiuyescm.bms.pub.info.entity.RegionEntity;

/**
 * 仓库服务区域对照关系
 */
public class WarehouseRegionRuleEntity implements IEntity {

	private static final long serialVersionUID = 4136965412950066383L;
	// 标识
	private String id;
	// 仓库ID
	private String warehouseid;
	// 区域ID
	private String regionid;
	// 优先级
	private int priority;
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

	/******************* 关联对象 *******************/
	//仓库
	private WarehouseEntity warehouse;
	//地址库
	private RegionEntity region;
	
	public WarehouseRegionRuleEntity() {

	}

	public String getId() {
		return this.id;
	}

	public void setId(String aId) {
		this.id = aId;
	}

	public String getWarehouseid() {
		return this.warehouseid;
	}

	public void setWarehouseid(String aWarehouseid) {
		this.warehouseid = aWarehouseid;
	}

	public String getRegionid() {
		return this.regionid;
	}

	public void setRegionid(String aRegionid) {
		this.regionid = aRegionid;
	}

	public int getPriority() {
		return this.priority;
	}

	public void setPriority(int aPriority) {
		this.priority = aPriority;
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

	public WarehouseEntity getWarehouse() {
		return warehouse;
	}

	public void setWarehouse(WarehouseEntity warehouse) {
		this.warehouse = warehouse;
	}

	public RegionEntity getRegion() {
		return region;
	}

	public void setRegion(RegionEntity region) {
		this.region = region;
	}

	public String toString() {
		StringBuffer returnString = new StringBuffer();
		returnString.append("com.jiuyescm.bms.pub.warehouse.entity.WarehouseRegionRuleEntity[");
		returnString.append("id = " + this.id + ";\n");
		returnString.append("warehouseid = " + this.warehouseid + ";\n");
		returnString.append("regionid = " + this.regionid + ";\n");
		returnString.append("priority = " + this.priority + ";\n");
		returnString.append("delflag = " + this.delflag + ";\n");
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
		if (id == null || "".equals(id.trim())) {
			return null;
		}
		return id;
	}
}