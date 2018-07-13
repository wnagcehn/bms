package com.jiuyescm.bms.quotation.transport.entity.vo;

import com.jiuyescm.cfm.domain.IEntity;

public class TransportLineVo implements IEntity{
	
    private static final long serialVersionUID = -1;
    
	// id
	private Long id;
	// 模版编号
	private String templateId;
	private String fromWarehouse;
	private String fromCity;
	private String fromDistrict;
	private String fromAddress;
	private String endWarehouse;
	private String toCity;
	private String toDistrict;
	private String toAddress;
	private String sendCycle;
	private String timeliness;
	private String remark;

	public TransportLineVo() {

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	public String getFromWarehouse() {
		return fromWarehouse;
	}

	public void setFromWarehouse(String fromWarehouse) {
		this.fromWarehouse = fromWarehouse;
	}

	public String getFromCity() {
		return fromCity;
	}

	public void setFromCity(String fromCity) {
		this.fromCity = fromCity;
	}

	public String getFromDistrict() {
		return fromDistrict;
	}

	public void setFromDistrict(String fromDistrict) {
		this.fromDistrict = fromDistrict;
	}

	public String getFromAddress() {
		return fromAddress;
	}

	public void setFromAddress(String fromAddress) {
		this.fromAddress = fromAddress;
	}

	public String getEndWarehouse() {
		return endWarehouse;
	}

	public void setEndWarehouse(String endWarehouse) {
		this.endWarehouse = endWarehouse;
	}

	public String getToCity() {
		return toCity;
	}

	public void setToCity(String toCity) {
		this.toCity = toCity;
	}

	public String getToDistrict() {
		return toDistrict;
	}

	public void setToDistrict(String toDistrict) {
		this.toDistrict = toDistrict;
	}

	public String getToAddress() {
		return toAddress;
	}

	public void setToAddress(String toAddress) {
		this.toAddress = toAddress;
	}

	public String getSendCycle() {
		return sendCycle;
	}

	public void setSendCycle(String sendCycle) {
		this.sendCycle = sendCycle;
	}

	public String getTimeliness() {
		return timeliness;
	}

	public void setTimeliness(String timeliness) {
		this.timeliness = timeliness;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
}
