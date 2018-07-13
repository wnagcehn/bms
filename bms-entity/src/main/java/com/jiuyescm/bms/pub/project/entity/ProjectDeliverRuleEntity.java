package com.jiuyescm.bms.pub.project.entity;

import com.jiuyescm.cfm.domain.IEntity;
import com.jiuyescm.bms.pub.deliver.entity.DeliverEntity;
import com.jiuyescm.bms.pub.info.entity.RegionEntity;

/**
 * 项目资源配置(宅配商)
 */
public class ProjectDeliverRuleEntity implements IEntity {

	private static final long serialVersionUID = 4529454253864144234L;
	// id
	private String id;
	// 项目ID
	private String projectid;
	// 区域ID
	private String regionid;
	// 宅配商ID
	private String deliverid;
	// 站点名称
	private String stationname;
	// 优先级
	private int priority;
	// 作废标记
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
	//区域助记码
	private String regioncode;
	
	private String province;
	private String city;
	private String district;
	
	/******************* 关联对象 *******************/
	//地址库
	private RegionEntity region;
	//宅配商
	private DeliverEntity deliver;
	//项目
	private ProjectEntity project;
	
	public ProjectDeliverRuleEntity() {

	}

	public String getId() {
		return this.id;
	}

	public void setId(String theId) {
		this.id = theId;
	}

	public String getProjectid() {
		return this.projectid;
	}

	public void setProjectid(String theProjectid) {
		this.projectid = theProjectid;
	}

	public String getRegionid() {
		return this.regionid;
	}

	public void setRegionid(String theRegionid) {
		this.regionid = theRegionid;
	}

	public String getDeliverid() {
		return this.deliverid;
	}

	public void setDeliverid(String theDeliverid) {
		this.deliverid = theDeliverid;
	}

	public String getStationname() {
		return this.stationname;
	}

	public void setStationname(String theStationname) {
		this.stationname = theStationname;
	}

	public int getPriority() {
		return this.priority;
	}

	public void setPriority(int thePriority) {
		this.priority = thePriority;
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
	
	public String getRegioncode() {
		return regioncode;
	}

	public void setRegioncode(String regioncode) {
		this.regioncode = regioncode;
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

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public RegionEntity getRegion() {
		return region;
	}

	public void setRegion(RegionEntity region) {
		this.region = region;
	}

	public DeliverEntity getDeliver() {
		return deliver;
	}

	public void setDeliver(DeliverEntity deliver) {
		this.deliver = deliver;
	}

	public ProjectEntity getProject() {
		return project;
	}

	public void setProject(ProjectEntity project) {
		this.project = project;
	}

	public String toString() {
		StringBuffer returnString = new StringBuffer();
		returnString
				.append("com.jiuyescm.bms.pub.project.entity.ProjectDeliverRuleEntity[");
		returnString.append("id = " + this.id + ";\n");
		returnString.append("projectid = " + this.projectid + ";\n");
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