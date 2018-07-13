package com.jiuyescm.bms.pub.project.entity;

import java.util.ArrayList;
import java.util.List;

import com.jiuyescm.cfm.domain.IEntity;
import com.jiuyescm.bms.pub.carrier.entity.CarrierEntity;
import com.jiuyescm.bms.pub.info.entity.RegionEntity;
import com.jiuyescm.bms.pub.warehouse.entity.WarehouseEntity;

/**
 * 项目资源配置(物流商)
 */
public class ProjectCarrierRuleEntity implements IEntity {

	private static final long serialVersionUID = 4621840268337498814L;
	// id
	private String id;
	// 项目ID
	private String projectid;
	// 仓库ID
	private String oms_warehouseid;
	// 区域ID
	private String regionid;
	// 物流商ID
	private String carrierid;
	// 温度类别
	private String tempraturetype;
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
	
	private String regioncode;
	private String province;
	private String city;
	private String district;
	
	/******************* 关联对象 *******************/
	//地址库
	private RegionEntity region;
	//仓库
	private WarehouseEntity warehouse;
	//项目
	private ProjectEntity project;
	//物流商
	private CarrierEntity carrier;
	//发货服务类型  PTPS（普通配送），LLPS（冷链配送）， OTHER 其他
	private String deliverytype;

	private List<ProjectCarrierRuleEntity> list=new ArrayList<ProjectCarrierRuleEntity>();
	
	public ProjectCarrierRuleEntity() {

	}

	public String getDeliverytype() {
		return deliverytype;
	}

	public void setDeliverytype(String deliverytype) {
		this.deliverytype = deliverytype;
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

	public String getOms_warehouseid() {
		return this.oms_warehouseid;
	}

	public void setOms_warehouseid(String theOms_warehouseid) {
		this.oms_warehouseid = theOms_warehouseid;
	}

	public String getRegionid() {
		return this.regionid;
	}

	public void setRegionid(String theRegionid) {
		this.regionid = theRegionid;
	}

	public String getCarrierid() {
		return this.carrierid;
	}

	public void setCarrierid(String theCarrierid) {
		this.carrierid = theCarrierid;
	}

	public String getTempraturetype() {
		return this.tempraturetype;
	}

	public void setTempraturetype(String theTempraturetype) {
		this.tempraturetype = theTempraturetype;
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

	public WarehouseEntity getWarehouse() {
		return warehouse;
	}

	public void setWarehouse(WarehouseEntity warehouse) {
		this.warehouse = warehouse;
	}

	public ProjectEntity getProject() {
		return project;
	}

	public void setProject(ProjectEntity project) {
		this.project = project;
	}

	public CarrierEntity getCarrier() {
		return carrier;
	}

	public void setCarrier(CarrierEntity carrier) {
		this.carrier = carrier;
	}
	
	public List<ProjectCarrierRuleEntity> getList() {
		return list;
	}

	public void setList(List<ProjectCarrierRuleEntity> list) {
		this.list = list;
	}

	public String toString() {
		StringBuffer returnString = new StringBuffer();
		returnString
				.append("com.jiuyescm.bms.pub.project.entity.ProjectCarrierRuleEntity[");
		returnString.append("id = " + this.id + ";\n");
		returnString.append("projectid = " + this.projectid + ";\n");
		returnString
				.append("oms_warehouseid = " + this.oms_warehouseid + ";\n");
		returnString.append("regionid = " + this.regionid + ";\n");
		returnString.append("carrierid = " + this.carrierid + ";\n");
		returnString.append("tempraturetype = " + this.tempraturetype + ";\n");
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