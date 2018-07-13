package com.jiuyescm.bms.pub.warehouse.entity;

import com.jiuyescm.cfm.domain.IEntity;
import com.jiuyescm.bms.pub.customer.entity.CustomerEntity;
import com.jiuyescm.bms.pub.info.entity.StockPlaceTypeEntity;

/**
 * 库存类型
 */
public class StockPlaceEntity implements IEntity {

	private static final long serialVersionUID = 3678617318673500674L;
	// 库存类型ID
	private String stockplaceid;
	// 库存类型名称
	private String stockplacename;
	// 仓库ID
	private String warehouseid;
	// 库存类型分类
	private String stockplacetypeid;
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
	
	private String customerid;
	
	/******************* 关联对象 *******************/
	//商家
	private CustomerEntity customer;
	//库存地类型
	private StockPlaceTypeEntity stockPlaceType;
	//仓库
	private WarehouseEntity warehouse;
	
	public StockPlaceEntity() {

	}

	public String getStockplaceid() {
		return this.stockplaceid;
	}

	public void setStockplaceid(String aStockplaceid) {
		this.stockplaceid = aStockplaceid;
	}

	public String getWarehouseid() {
		return this.warehouseid;
	}

	public void setWarehouseid(String aWarehouseid) {
		this.warehouseid = aWarehouseid;
	}

	public String getStockplacename() {
		return this.stockplacename;
	}

	public void setStockplacename(String aStockplacename) {
		this.stockplacename = aStockplacename;
	}

	public String getStockplacetypeid() {
		return this.stockplacetypeid;
	}

	public void setStockplacetypeid(String aStockplacetypeid) {
		this.stockplacetypeid = aStockplacetypeid;
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

	public CustomerEntity getCustomer() {
		return customer;
	}

	public void setCustomer(CustomerEntity customer) {
		this.customer = customer;
	}

	public StockPlaceTypeEntity getStockPlaceType() {
		return stockPlaceType;
	}

	public void setStockPlaceType(StockPlaceTypeEntity stockPlaceType) {
		this.stockPlaceType = stockPlaceType;
	}

	public WarehouseEntity getWarehouse() {
		return warehouse;
	}

	public void setWarehouse(WarehouseEntity warehouse) {
		this.warehouse = warehouse;
	}

	public String getCustomerid() {
		return customerid;
	}

	public void setCustomerid(String customerid) {
		this.customerid = customerid;
	}

	public String toString() {
		StringBuffer returnString = new StringBuffer();
		returnString.append("com.jiuyescm.bms.pub.customer.entity.StockPlaceEntity[");
		returnString.append("stockplaceid = " + this.stockplaceid + ";\n");
		returnString.append("warehouseid = " + this.warehouseid + ";\n");
		returnString.append("stockplacename = " + this.stockplacename + ";\n");
		returnString.append("stockplacetypeid = " + this.stockplacetypeid + ";\n");
		returnString.append("remark = " + this.remark + ";\n");
		returnString.append("delflag = " + this.delflag + ";\n");
		returnString.append("crepersonid = " + this.crepersonid + ";\n");
		returnString.append("creperson = " + this.creperson + ";\n");
		returnString.append("cretime = " + this.cretime + ";\n");
		returnString.append("modpersonid = " + this.modpersonid + ";\n");
		returnString.append("modperson = " + this.modperson + ";\n");
		returnString.append("modtime = " + this.modtime + ";\n");
		returnString.append("customerid = " + this.customerid + ";\n");
		returnString.append("]\n");
		return returnString.toString();
	}

	/******************* 辅助方法 *******************/
	/*
	 * 实体Key字符
	 */
	public String getKey() {
		if (stockplaceid == null || "".equals(stockplaceid.trim())) {
			return null;
		}
		return stockplaceid;
	}
}