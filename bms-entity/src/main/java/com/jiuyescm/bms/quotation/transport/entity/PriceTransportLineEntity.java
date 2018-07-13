/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.quotation.transport.entity;

import java.util.List;

import com.jiuyescm.bms.common.BmsCommonAttribute;

/**
 * 运输路线
 * 
 * @author wubangjun
 */
public class PriceTransportLineEntity extends BmsCommonAttribute {

	private static final long serialVersionUID = 3581849457665126286L;

	/**
	 * 承运商编号
	 */
	private String carrierCode;
	private String carrierName;
	/**
	 * 线路编号
	 */
	private String transportLineNo;
	/**
	 * 路线名称
	 */
	private String transportLineName;
	// 业务类型
	private String transportTypeCode;
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
	 * 服务类型
	 */
	private String serviceTypeCode;
	/**
	 * 发车周期
	 */
	private String sendCycle;
	/**
	 * 发车时间
	 */
	private java.sql.Timestamp sendTime;
	/**
	 * 时效
	 */
	private String timeliness;
	/**
	 * 回单时效
	 */
	private Double receiptAging;
	/**
	 * 营业时间
	 */
	private java.sql.Timestamp businessTime;
	/**
	 * 订单受理时效(天)
	 */
	private Double orderAcceptAging;
	/**
	 * 截单时间点
	 */
	private java.sql.Timestamp orderDeadLine;
	/**
	 * 到站自提时效(天)
	 */
	private Double selfPickedAging;
	/**
	 * 到站派送时效(天)
	 */
	private Double deliveryAging;
	/**
	 * 重泡比
	 */
	private String w2bubbleRatio;
	/**
	 *  货值比
	 */
	private String valueRatio;
	/**
	 * 始发站点
	 */
	private String startStation;
	/**
	 * 目的站点
	 */
	private String endStation;

	// 电商仓库code
	private String elecWarehouseCode;
	// 电商仓库
	private String elecWarehouseName;
	// 机场
	private String airPort;

	public String getStartStation() {
		return startStation;
	}

	public void setStartStation(String startStation) {
		this.startStation = startStation;
	}

	public String getEndStation() {
		return endStation;
	}

	public void setEndStation(String endStation) {
		this.endStation = endStation;
	}

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

	private List<PriceTransportLineRangeEntity> child;

	public PriceTransportLineEntity() {

	}

	public List<PriceTransportLineRangeEntity> getChild() {
		return child;
	}

	public void setChild(List<PriceTransportLineRangeEntity> child) {
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

	public String getCarrierCode() {
		return carrierCode;
	}

	public void setCarrierCode(String carrierCode) {
		this.carrierCode = carrierCode;
	}

	public String getTransportLineName() {
		return transportLineName;
	}

	public void setTransportLineName(String transportLineName) {
		this.transportLineName = transportLineName;
	}

	public String getServiceTypeCode() {
		return serviceTypeCode;
	}

	public void setServiceTypeCode(String serviceTypeCode) {
		this.serviceTypeCode = serviceTypeCode;
	}

	public java.sql.Timestamp getSendTime() {
		return sendTime;
	}

	public void setSendTime(java.sql.Timestamp sendTime) {
		this.sendTime = sendTime;
	}

	public Double getReceiptAging() {
		return receiptAging;
	}

	public void setReceiptAging(Double receiptAging) {
		this.receiptAging = receiptAging;
	}

	public java.sql.Timestamp getBusinessTime() {
		return businessTime;
	}

	public void setBusinessTime(java.sql.Timestamp businessTime) {
		this.businessTime = businessTime;
	}

	public Double getOrderAcceptAging() {
		return orderAcceptAging;
	}

	public void setOrderAcceptAging(Double orderAcceptAging) {
		this.orderAcceptAging = orderAcceptAging;
	}

	public java.sql.Timestamp getOrderDeadLine() {
		return orderDeadLine;
	}

	public void setOrderDeadLine(java.sql.Timestamp orderDeadLine) {
		this.orderDeadLine = orderDeadLine;
	}

	public Double getSelfPickedAging() {
		return selfPickedAging;
	}

	public void setSelfPickedAging(Double selfPickedAging) {
		this.selfPickedAging = selfPickedAging;
	}

	public Double getDeliveryAging() {
		return deliveryAging;
	}

	public void setDeliveryAging(Double deliveryAging) {
		this.deliveryAging = deliveryAging;
	}

	public String getCarrierName() {
		return carrierName;
	}

	public void setCarrierName(String carrierName) {
		this.carrierName = carrierName;
	}

	public String getW2bubbleRatio() {
		return w2bubbleRatio;
	}

	public void setW2bubbleRatio(String w2bubbleRatio) {
		this.w2bubbleRatio = w2bubbleRatio;
	}

	public String getTransportTypeCode() {
		return transportTypeCode;
	}

	public void setTransportTypeCode(String transportTypeCode) {
		this.transportTypeCode = transportTypeCode;
	}

	public String getElecWarehouseCode() {
		return elecWarehouseCode;
	}

	public void setElecWarehouseCode(String elecWarehouseCode) {
		this.elecWarehouseCode = elecWarehouseCode;
	}

	public String getAirPort() {
		return airPort;
	}

	public void setAirPort(String airPort) {
		this.airPort = airPort;
	}

	public String getElecWarehouseName() {
		return elecWarehouseName;
	}

	public void setElecWarehouseName(String elecWarehouseName) {
		this.elecWarehouseName = elecWarehouseName;
	}

	public String getValueRatio() {
		return valueRatio;
	}

	public void setValueRatio(String valueRatio) {
		this.valueRatio = valueRatio;
	}

	
}
