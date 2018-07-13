/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.quotation.out.transport.entity;

import java.util.List;

import com.jiuyescm.bms.common.BmsCommonAttribute;

/**
 * 应付运输路线
 * @author wubangjun
 */
public class PriceTransportPayLineEntity extends BmsCommonAttribute {
	
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * 线路编号
	 */
	private String transportLineNo;
	/**
	 * 始发仓
	 */
	private String fromWarehouseId;
	/**
	 * 始发省份
	 */
	private String fromProvinceId;
	/**
	 * 始发城市
	 */
	private String fromCityId;
	/**
	 * 始发地区
	 */
	private String fromDistrictId;
	/**
	 * 始发地址
	 */
	private String fromAddress;
	/**
	 * 目的仓
	 */
	private String endWarehouseId;
	/**
	 * 目的省份
	 */
	private String toProvinceId;
	
	/**
	 * 目的城市
	 */
	private String toCityId;
	/**
	 * 目的地区
	 */
	private String toDistrictId;
	/**
	 * 目的地址
	 */
	private String toAddress;
	/**
	 * 发车周期
	 */
	private String sendCycle;
	/**
	 * 时效
	 */
	private String timeliness;
	/**
	 * 备注
	 */
	private String remark;
	/**
	 * 对应模版
	 */
	private String templateId;
	
	private String fromWarehouseName;
	private String fromProvinceName;
	private String fromCityName;
	private String fromDistrictName;
	
	private String endWarehouseName;
	private String toProvinceName;
	private String toCityName;
	private String toDistrictName;

	private List<PriceTransportPayLineRangeEntity> child;
	
	public PriceTransportPayLineEntity() {

	}

	public List<PriceTransportPayLineRangeEntity> getChild() {
		return child;
	}

	public void setChild(List<PriceTransportPayLineRangeEntity> child) {
		this.child = child;
	}

	public String getTransportLineNo() {
		return transportLineNo;
	}

	public void setTransportLineNo(String transportLineNo) {
		this.transportLineNo = transportLineNo;
	}

	public String getFromWarehouseId() {
		return fromWarehouseId;
	}

	public void setFromWarehouseId(String fromWarehouseId) {
		this.fromWarehouseId = fromWarehouseId;
	}

	public String getFromCityId() {
		return fromCityId;
	}

	public void setFromCityId(String fromCityId) {
		this.fromCityId = fromCityId;
	}

	public String getFromDistrictId() {
		return fromDistrictId;
	}

	public void setFromDistrictId(String fromDistrictId) {
		this.fromDistrictId = fromDistrictId;
	}

	public String getFromAddress() {
		return fromAddress;
	}

	public void setFromAddress(String fromAddress) {
		this.fromAddress = fromAddress;
	}

	public String getEndWarehouseId() {
		return endWarehouseId;
	}

	public void setEndWarehouseId(String endWarehouseId) {
		this.endWarehouseId = endWarehouseId;
	}

	public String getToCityId() {
		return toCityId;
	}

	public void setToCityId(String toCityId) {
		this.toCityId = toCityId;
	}

	public String getToDistrictId() {
		return toDistrictId;
	}

	public void setToDistrictId(String toDistrictId) {
		this.toDistrictId = toDistrictId;
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

	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	public String getFromCityName() {
		return fromCityName;
	}

	public void setFromCityName(String fromCityName) {
		this.fromCityName = fromCityName;
	}

	public String getFromDistrictName() {
		return fromDistrictName;
	}

	public void setFromDistrictName(String fromDistrictName) {
		this.fromDistrictName = fromDistrictName;
	}

	public String getToCityName() {
		return toCityName;
	}

	public void setToCityName(String toCityName) {
		this.toCityName = toCityName;
	}

	public String getToDistrictName() {
		return toDistrictName;
	}

	public void setToDistrictName(String toDistrictName) {
		this.toDistrictName = toDistrictName;
	}

	public String getFromWarehouseName() {
		return fromWarehouseName;
	}

	public void setFromWarehouseName(String fromWarehouseName) {
		this.fromWarehouseName = fromWarehouseName;
	}

	public String getEndWarehouseName() {
		return endWarehouseName;
	}

	public void setEndWarehouseName(String endWarehouseName) {
		this.endWarehouseName = endWarehouseName;
	}

	public String getFromProvinceId() {
		return fromProvinceId;
	}

	public void setFromProvinceId(String fromProvinceId) {
		this.fromProvinceId = fromProvinceId;
	}

	public String getToProvinceId() {
		return toProvinceId;
	}

	public void setToProvinceId(String toProvinceId) {
		this.toProvinceId = toProvinceId;
	}

	public String getFromProvinceName() {
		return fromProvinceName;
	}

	public void setFromProvinceName(String fromProvinceName) {
		this.fromProvinceName = fromProvinceName;
	}

	public String getToProvinceName() {
		return toProvinceName;
	}

	public void setToProvinceName(String toProvinceName) {
		this.toProvinceName = toProvinceName;
	}
}
