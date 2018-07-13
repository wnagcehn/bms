package com.jiuyescm.bms.report.month.entity;

import com.jiuyescm.cfm.domain.IEntity;

public class MaterialImportReportEntity  implements IEntity{
	
	private static final long serialVersionUID = 1L;

	/**
	 * 商家名称
	 */
	private String customerName;
	
	private String customerId;
	
	/**
	 * 仓库名称
	 */
	private String warehouseName;
	
	private String warehouseCode;
	
	/**
	 * 原始单量
	 */
	private Long dispatchNum;
	
	/**
	 * 耗材单量
	 */
	private Long materialNum;
	
	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getWarehouseName() {
		return warehouseName;
	}

	public void setWarehouseName(String warehouseName) {
		this.warehouseName = warehouseName;
	}

	public String getWarehouseCode() {
		return warehouseCode;
	}

	public void setWarehouseCode(String warehouseCode) {
		this.warehouseCode = warehouseCode;
	}

	public Long getDispatchNum() {
		return dispatchNum;
	}

	public void setDispatchNum(Long dispatchNum) {
		this.dispatchNum = dispatchNum;
	}

	public Long getMaterialNum() {
		return materialNum;
	}

	public void setMaterialNum(Long materialNum) {
		this.materialNum = materialNum;
	}

	public Long getDiffNum() {
		return diffNum;
	}

	public void setDiffNum(Long diffNum) {
		this.diffNum = diffNum;
	}

	/**
	 * 差异量
	 */
	private Long diffNum;
	
	
}
