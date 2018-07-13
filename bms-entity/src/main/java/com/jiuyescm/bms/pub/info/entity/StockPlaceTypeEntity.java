package com.jiuyescm.bms.pub.info.entity;

import com.jiuyescm.cfm.domain.IEntity;

/**
 * 库存地类型
 */
public class StockPlaceTypeEntity implements IEntity {

	private static final long serialVersionUID = 4306363059999557385L;
	// 库存地类型
	private String stockplacetypeid;
	// 编码
	private String stockplacetypecode;
	// 名称
	private String stockplacetypename;
	// 控制代码
	private String ctrlcode;
	// 备注
	private String remark;
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

	public StockPlaceTypeEntity() {

	}

	public String getStockplacetypeid() {
		return this.stockplacetypeid;
	}

	public void setStockplacetypeid(String aStockplacetypeid) {
		this.stockplacetypeid = aStockplacetypeid;
	}

	public String getStockplacetypecode() {
		return this.stockplacetypecode;
	}

	public void setStockplacetypecode(String aStockplacetypecode) {
		this.stockplacetypecode = aStockplacetypecode;
	}

	public String getStockplacetypename() {
		return this.stockplacetypename;
	}

	public void setStockplacetypename(String aStockplacetypename) {
		this.stockplacetypename = aStockplacetypename;
	}

	public String getCtrlcode() {
		return this.ctrlcode;
	}

	public void setCtrlcode(String aCtrlcode) {
		this.ctrlcode = aCtrlcode;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String aRemark) {
		this.remark = aRemark;
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

	public String toString() {
		StringBuffer returnString = new StringBuffer();
		returnString.append("com.jiuyescm.bms.pub.info.entity.StockPlaceTypeEntity[");
		returnString.append("stockplacetypeid = " + this.stockplacetypeid + ";\n");
		returnString.append("stockplacetypecode = " + this.stockplacetypecode + ";\n");
		returnString.append("stockplacetypename = " + this.stockplacetypename + ";\n");
		returnString.append("ctrlcode = " + this.ctrlcode + ";\n");
		returnString.append("remark = " + this.remark + ";\n");
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
		if (stockplacetypeid == null || "".equals(stockplacetypeid.trim())) {
			return null;
		}
		return stockplacetypeid;
	}
}