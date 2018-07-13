package com.jiuyescm.bms.quotation.contract.entity;

import com.jiuyescm.cfm.domain.IEntity;

public class ContractManageEntity implements IEntity{
	private static final long serialVersionUID = -1;
	//商家id
	private String customerId;
	//商家名称
	private String customerName;
	//异常类型
	private String abnormalType;
	//备注
	private String remark;
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
	public String getAbnormalType() {
		return abnormalType;
	}
	public void setAbnormalType(String abnormalType) {
		this.abnormalType = abnormalType;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	
}
