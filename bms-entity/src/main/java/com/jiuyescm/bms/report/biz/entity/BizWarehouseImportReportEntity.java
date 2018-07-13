package com.jiuyescm.bms.report.biz.entity;

import com.jiuyescm.cfm.domain.IEntity;

/**
 * 各仓导入数据状况实体
 * 
 * @author yangss
 */
public class BizWarehouseImportReportEntity implements IEntity {

	private static final long serialVersionUID = 2157080948269552701L;

	// 日期
	private String importDate;

	// 仓库编号
	private String warehouseCode;

	// 仓库名称
	private String warehouseName;

	// 业务类型
	private String bizType;

	// 应导入商家数
	private int theoryNum;

	// 实际导入商家数
	private int actualNum;

	// 未导入商家数
	private int minusNum;

	public String getImportDate() {
		return importDate;
	}

	public void setImportDate(String importDate) {
		this.importDate = importDate;
	}

	public String getWarehouseCode() {
		return warehouseCode;
	}

	public void setWarehouseCode(String warehouseCode) {
		this.warehouseCode = warehouseCode;
	}

	public String getWarehouseName() {
		return warehouseName;
	}

	public void setWarehouseName(String warehouseName) {
		this.warehouseName = warehouseName;
	}

	public String getBizType() {
		return bizType;
	}

	public void setBizType(String bizType) {
		this.bizType = bizType;
	}

	public int getTheoryNum() {
		return theoryNum;
	}

	public void setTheoryNum(int theoryNum) {
		this.theoryNum = theoryNum;
	}

	public int getActualNum() {
		return actualNum;
	}

	public void setActualNum(int actualNum) {
		this.actualNum = actualNum;
	}

	public int getMinusNum() {
		return minusNum;
	}

	public void setMinusNum(int minusNum) {
		this.minusNum = minusNum;
	}

}
