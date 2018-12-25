package com.jiuyescm.bms.report.vo;

import com.jiuyescm.cfm.domain.IEntity;

/**
 * 仓库未导入商家实体
 * 
 * @author liuzhicheng
 */
public class BizWarehouseNotImportVo implements IEntity {

	private static final long serialVersionUID = -6695014670946371212L;

	// 导入类型
	private String importType;

	// 商家编号
	private String customerId;

	// 商家名称
	private String customerName;

	// 商家简称
	private String shortName;

	public String getImportType() {
		return importType;
	}

	public void setImportType(String importType) {
		this.importType = importType;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

}
