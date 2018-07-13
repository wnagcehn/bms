package com.jiuyescm.bms.pub.project.entity;

import com.jiuyescm.cfm.domain.IEntity;
import com.jiuyescm.bms.pub.customer.entity.CustomerEntity;

/**
 * 商家项目对应关系
 */
public class CustomerProjectEntity implements IEntity {

	private static final long serialVersionUID = 757758845350070092L;
	// id
	private String id;
	// 商家ID
	private String customerid;
	// 项目ID
	private String projectid;
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
	//商家名称
	private String customername;
	
	/******************* 关联对象 *******************/
	//商家
	private CustomerEntity customer;
	//项目
	private ProjectEntity project;

	public CustomerProjectEntity() {

	}

	public String getId() {
		return this.id;
	}

	public void setId(String theId) {
		this.id = theId;
	}

	public String getCustomerid() {
		return this.customerid;
	}

	public void setCustomerid(String theCustomerid) {
		this.customerid = theCustomerid;
	}

	public String getProjectid() {
		return this.projectid;
	}

	public void setProjectid(String theProjectid) {
		this.projectid = theProjectid;
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
	
	public String getCustomername() {
		return customername;
	}

	public void setCustomername(String customername) {
		this.customername = customername;
	}

	public CustomerEntity getCustomer() {
		return customer;
	}

	public void setCustomer(CustomerEntity customer) {
		this.customer = customer;
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
				.append("com.jiuyescm.bms.pub.project.entity.CustomerProjectEntity[");
		returnString.append("id = " + this.id + ";\n");
		returnString.append("customerid = " + this.customerid + ";\n");
		returnString.append("projectid = " + this.projectid + ";\n");
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