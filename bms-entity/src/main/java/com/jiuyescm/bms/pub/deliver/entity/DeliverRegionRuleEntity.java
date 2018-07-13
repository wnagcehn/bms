package com.jiuyescm.bms.pub.deliver.entity;

import com.jiuyescm.cfm.domain.IEntity;
import com.jiuyescm.bms.pub.info.entity.RegionEntity;

/**
 * 宅配商服务区域规则
 */
public class DeliverRegionRuleEntity implements IEntity {

	private static final long serialVersionUID = 3375992954281017541L;
	// id
	private String id;
	// 区域ID
	private String regionid;
	// 宅配商ID
	private String deliverid;
	// 站点名称
	private String stationname;
	// 优先级
	private int priority;
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
	//宅配商
	private DeliverEntity deliver;

	public DeliverRegionRuleEntity() {

	}

	public String getId() {
		return this.id;
	}

	public void setId(String aId) {
		this.id = aId;
	}

	public String getRegionid() {
		return this.regionid;
	}

	public void setRegionid(String aRegionid) {
		this.regionid = aRegionid;
	}

	public String getDeliverid() {
		return this.deliverid;
	}

	public void setDeliverid(String aDeliverid) {
		this.deliverid = aDeliverid;
	}

	public String getStationname() {
		return this.stationname;
	}

	public void setStationname(String aStationname) {
		this.stationname = aStationname;
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

	public final RegionEntity getRegion() {
		return region;
	}

	public final void setRegion(RegionEntity region) {
		this.region = region;
	}

	public final DeliverEntity getDeliver() {
		return deliver;
	}

	public final void setDeliver(DeliverEntity deliver) {
		this.deliver = deliver;
	}

	public String toString() {
		StringBuffer returnString = new StringBuffer();
		returnString.append("com.jiuyescm.bms.pub.deliver.entity.DeliverRegionRuleEntity[");
		returnString.append("id = " + this.id + ";\n");
		returnString.append("regionid = " + this.regionid + ";\n");
		returnString.append("deliverid = " + this.deliverid + ";\n");
		returnString.append("stationname = " + this.stationname + ";\n");
		returnString.append("priority = " + this.priority + ";\n");
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
		if (id == null || "".equals(id.trim())) {
			return null;
		}
		return id;
	}
}