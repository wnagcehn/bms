package com.jiuyescm.bms.general.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.jiuyescm.cfm.domain.IEntity;
/**
 * 耗材成本实体
 * @author wuliangfeng
 *
 */
public class PackCostReportEntity implements IEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8919394084871710690L;
	private int id;
	private String warehouseNo;
	private int year;
	private int monthNum;
	private String dataType;
	private Timestamp startDate;
	private Timestamp endDate;
	private String materialNo;
	private String materialName;
	private String barcode;
	private String materialType;
	private String materialTypeName;
	private String unit;
	private BigDecimal initQty;
	private BigDecimal inQty;
	private BigDecimal initCost;
	private BigDecimal cost;
	private BigDecimal initMoney;
	private BigDecimal inMoney;
	private String crePerson;
	private String crePersonId;
	private Timestamp creTime;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getWarehouseNo() {
		return warehouseNo;
	}
	public void setWarehouseNo(String warehouseNo) {
		this.warehouseNo = warehouseNo;
	}
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	public int getMonthNum() {
		return monthNum;
	}
	public void setMonthNum(int monthNum) {
		this.monthNum = monthNum;
	}
	public String getDataType() {
		return dataType;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	public Timestamp getStartDate() {
		return startDate;
	}
	public void setStartDate(Timestamp startDate) {
		this.startDate = startDate;
	}
	public Timestamp getEndDate() {
		return endDate;
	}
	public void setEndDate(Timestamp endDate) {
		this.endDate = endDate;
	}
	public String getMaterialNo() {
		return materialNo;
	}
	public void setMaterialNo(String materialNo) {
		this.materialNo = materialNo;
	}
	public String getMaterialName() {
		return materialName;
	}
	public void setMaterialName(String materialName) {
		this.materialName = materialName;
	}
	public String getBarcode() {
		return barcode;
	}
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
	public String getMaterialType() {
		return materialType;
	}
	public void setMaterialType(String materialType) {
		this.materialType = materialType;
	}
	public String getMaterialTypeName() {
		return materialTypeName;
	}
	public void setMaterialTypeName(String materialTypeName) {
		this.materialTypeName = materialTypeName;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public BigDecimal getInitQty() {
		return initQty;
	}
	public void setInitQty(BigDecimal initQty) {
		this.initQty = initQty;
	}
	public BigDecimal getInQty() {
		return inQty;
	}
	public void setInQty(BigDecimal inQty) {
		this.inQty = inQty;
	}
	public BigDecimal getInitCost() {
		return initCost;
	}
	public void setInitCost(BigDecimal initCost) {
		this.initCost = initCost;
	}
	public BigDecimal getCost() {
		return cost;
	}
	public void setCost(BigDecimal cost) {
		this.cost = cost;
	}
	public BigDecimal getInitMoney() {
		return initMoney;
	}
	public void setInitMoney(BigDecimal initMoney) {
		this.initMoney = initMoney;
	}
	public BigDecimal getInMoney() {
		return inMoney;
	}
	public void setInMoney(BigDecimal inMoney) {
		this.inMoney = inMoney;
	}
	public String getCrePerson() {
		return crePerson;
	}
	public void setCrePerson(String crePerson) {
		this.crePerson = crePerson;
	}
	public String getCrePersonId() {
		return crePersonId;
	}
	public void setCrePersonId(String crePersonId) {
		this.crePersonId = crePersonId;
	}
	public Timestamp getCreTime() {
		return creTime;
	}
	public void setCreTime(Timestamp creTime) {
		this.creTime = creTime;
	}
}
