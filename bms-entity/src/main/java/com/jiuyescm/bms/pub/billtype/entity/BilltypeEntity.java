package com.jiuyescm.bms.pub.billtype.entity;

import com.jiuyescm.cfm.domain.IEntity;

public class BilltypeEntity implements IEntity {

	private static final long serialVersionUID = 3041392640528171179L;

	// 单据类型ID
	private String ordertypeid;
	// 单据类型名称
	private String name;
	// 基本单据类型
	private String basetypeid; // RK:入库；CK:出库;ZP:产地直配;KJ:库间调拨;KN:库内调拨;YS:运输单;ZC:转储单;PD:盘点单
	// 删除标记
	private int delflag;
	// 控制代码
	private String ctrlcode;
	// 创建人
	private String creperson;
	// 创建人ID
	private String crepersonid;
	// 创建时间
	private java.sql.Timestamp cretime;
	// 更新人ID
	private String modpersonid;
	// 更新人
	private String modperson;
	// 更新时间
	private java.sql.Timestamp modtime;
	// 备注
	private String remark;
	// 业务类型
	private String businesstype;
		
	public BilltypeEntity() {

	}

	public String getOrdertypeid() {
		return this.ordertypeid;
	}

	public void setOrdertypeid(String theOrdertypeid) {
		this.ordertypeid = theOrdertypeid;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String theName) {
		this.name = theName;
	}

	public String getBasetypeid() {
		return this.basetypeid;
	}

	public void setBasetypeid(String theBasetypeid) {
		this.basetypeid = theBasetypeid;
	}

	public int getDelflag() {
		return this.delflag;
	}

	public void setDelflag(int theDelflag) {
		this.delflag = theDelflag;
	}

	public String getCtrlcode() {
		return this.ctrlcode;
	}

	public void setCtrlcode(String theCtrlcode) {
		this.ctrlcode = theCtrlcode;
	}

	public String getCreperson() {
		return this.creperson;
	}

	public void setCreperson(String theCreperson) {
		this.creperson = theCreperson;
	}

	public String getCrepersonid() {
		return this.crepersonid;
	}

	public void setCrepersonid(String theCrepersonid) {
		this.crepersonid = theCrepersonid;
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

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String theRemark) {
		this.remark = theRemark;
	}
	
	public String getBusinesstype() {
		return businesstype;
	}

	public void setBusinesstype(String businesstype) {
		this.businesstype = businesstype;
	}

	public String toString() {
		StringBuffer returnString = new StringBuffer();
		returnString.append("com.jiuyescm.bms.pub.billtype.entity.BilltypeEntity[");
		returnString.append("ordertypeid = " + this.ordertypeid + ";\n");
		returnString.append("name = " + this.name + ";\n");
		returnString.append("basetypeid = " + this.basetypeid + ";\n");
		returnString.append("delflag = " + this.delflag + ";\n");
		returnString.append("ctrlcode = " + this.ctrlcode + ";\n");
		returnString.append("creperson = " + this.creperson + ";\n");
		returnString.append("crepersonid = " + this.crepersonid + ";\n");
		returnString.append("cretime = " + this.cretime + ";\n");
		returnString.append("modpersonid = " + this.modpersonid + ";\n");
		returnString.append("modperson = " + this.modperson + ";\n");
		returnString.append("modtime = " + this.modtime + ";\n");
		returnString.append("remark = " + this.remark + ";\n");
		returnString.append("]\n");
		return returnString.toString();
	}

	/******************* 辅助方法 *******************/
	/*
	 * 实体Key字符
	 */
	public String getKey() {
		if (ordertypeid == null || "".equals(ordertypeid.trim())) {
			return null;
		}
		return ordertypeid;
	}
}