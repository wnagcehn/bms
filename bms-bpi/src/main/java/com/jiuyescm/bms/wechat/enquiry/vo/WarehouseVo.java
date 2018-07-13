package com.jiuyescm.bms.wechat.enquiry.vo;

import java.io.Serializable;

/**
 * 仓库返回实体Vo
 * @author yangss
 * 
 */
public class WarehouseVo implements Serializable {

	private static final long serialVersionUID = 2981038370405198189L;

	// 仓库编号
	private String warehouseCode;
	// 仓库名称
	private String warehouseName;

	public WarehouseVo(String warehouseCode, String warehouseName) {
		super();
		this.warehouseCode = warehouseCode;
		this.warehouseName = warehouseName;
	}

	public WarehouseVo() {
		super();
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

}
