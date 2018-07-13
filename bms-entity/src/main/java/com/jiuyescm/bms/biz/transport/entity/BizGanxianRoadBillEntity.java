/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.biz.transport.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

import org.apache.commons.lang.StringUtils;

import com.jiuyescm.bms.common.BmsCommonAttribute;

/**
 * 干线路单
 * @author wubangjun
 */
public class BizGanxianRoadBillEntity extends BmsCommonAttribute {

	private static final long serialVersionUID = 1L;

	private String tmsId;
	private String roadbillNo;
	private String roadbillName;
	private String roadbillType;
	private String carrierId;
	private String carrierName;
	private String warehouseCode;
	private String warehouseName;
	private String sendProvinceId;
	private String sendCityId;
	private String sendDistrictId;
	private String receiverProvinceId;
	private String receiverCityId;
	private String receiverDistrictId;
	private String receiver;
	private String receiverTelephone;
	private String temperatureTypeCode;
	private String isLight;
	private String vehicleNo;
	private String carModel;
	private String transportType;
	private Double waybillNum;
	private Double totalBox;
	private Double totalWeight;
	// 调整重量
	private Double adjustWeight;
	private Double totalVolume;
	private String sendPerson;
	private java.sql.Timestamp sendTime;
	private java.sql.Timestamp finishedTime;
	private String remark;
	// 费用编号
	private String feesNo;
	// 结算状态
	private String isCalculated;
	// 写入BMS时间
	private Timestamp writeTime;
	// 费用计算时间
	private Timestamp calculateTime;
	
	//计算结果
	private BigDecimal calResult;
	private String calMethod;
	
	public BizGanxianRoadBillEntity() {

	}

	public String getRoadbillNo() {
		return roadbillNo;
	}

	public void setRoadbillNo(String roadbillNo) {
		this.roadbillNo = roadbillNo;
	}

	public String getRoadbillName() {
		return roadbillName;
	}

	public void setRoadbillName(String roadbillName) {
		this.roadbillName = roadbillName;
	}

	public String getRoadbillType() {
		return roadbillType;
	}

	public void setRoadbillType(String roadbillType) {
		this.roadbillType = roadbillType;
	}

	public String getTmsId() {
		return tmsId;
	}

	public void setTmsId(String tmsId) {
		this.tmsId = tmsId;
	}

	public String getCarrierName() {
		return carrierName;
	}

	public void setCarrierName(String carrierName) {
		this.carrierName = carrierName;
	}

	public String getSendProvinceId() {
		return sendProvinceId;
	}

	public void setSendProvinceId(String sendProvinceId) {
		this.sendProvinceId = sendProvinceId;
	}

	public String getSendCityId() {
		return sendCityId;
	}

	public void setSendCityId(String sendCityId) {
		this.sendCityId = sendCityId;
	}

	public String getSendDistrictId() {
		return sendDistrictId;
	}

	public void setSendDistrictId(String sendDistrictId) {
		this.sendDistrictId = sendDistrictId;
	}

	public String getReceiverProvinceId() {
		return receiverProvinceId;
	}

	public void setReceiverProvinceId(String receiverProvinceId) {
		this.receiverProvinceId = receiverProvinceId;
	}

	public String getReceiverCityId() {
		return receiverCityId;
	}

	public void setReceiverCityId(String receiverCityId) {
		this.receiverCityId = receiverCityId;
	}

	public String getReceiverDistrictId() {
		return receiverDistrictId;
	}

	public void setReceiverDistrictId(String receiverDistrictId) {
		this.receiverDistrictId = receiverDistrictId;
	}

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	public String getReceiverTelephone() {
		return receiverTelephone;
	}

	public void setReceiverTelephone(String receiverTelephone) {
		this.receiverTelephone = receiverTelephone;
	}

	public String getTemperatureTypeCode() {
		return temperatureTypeCode;
	}

	public void setTemperatureTypeCode(String temperatureTypeCode) {
		this.temperatureTypeCode = temperatureTypeCode;
	}

	public String getVehicleNo() {
		return vehicleNo;
	}

	public void setVehicleNo(String vehicleNo) {
		this.vehicleNo = vehicleNo;
	}

	public String getCarModel() {
		return carModel;
	}

	public void setCarModel(String carModel) {
		this.carModel = carModel;
	}

	public Double getWaybillNum() {
		return waybillNum;
	}

	public void setWaybillNum(Double waybillNum) {
		this.waybillNum = waybillNum;
	}

	public Double getTotalBox() {
		return totalBox;
	}

	public void setTotalBox(Double totalBox) {
		this.totalBox = totalBox;
	}

	public Double getTotalWeight() {
		return totalWeight;
	}

	public void setTotalWeight(Double totalWeight) {
		this.totalWeight = totalWeight;
	}

	public Double getTotalVolume() {
		return totalVolume;
	}

	public void setTotalVolume(Double totalVolume) {
		this.totalVolume = totalVolume;
	}

	public String getSendPerson() {
		return sendPerson;
	}

	public void setSendPerson(String sendPerson) {
		this.sendPerson = sendPerson;
	}

	public Timestamp getSendTime() {
		return sendTime;
	}

	public void setSendTime(Timestamp sendTime) {
		this.sendTime = sendTime;
	}

	public Timestamp getFinishedTime() {
		return finishedTime;
	}

	public void setFinishedTime(Timestamp finishedTime) {
		this.finishedTime = finishedTime;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		if (StringUtils.isNotEmpty(remark) && remark.length() > 128) {
			this.remark = remark.substring(0, 100);
		}else {
			this.remark = remark;
		}
	}

	public Double getAdjustWeight() {
		return adjustWeight;
	}

	public void setAdjustWeight(Double adjustWeight) {
		this.adjustWeight = adjustWeight;
	}

	public String getIsCalculated() {
		return isCalculated;
	}

	public void setIsCalculated(String isCalculated) {
		this.isCalculated = isCalculated;
	}

	public String getTransportType() {
		return transportType;
	}

	public void setTransportType(String transportType) {
		this.transportType = transportType;
	}

	public String getCarrierId() {
		return carrierId;
	}

	public void setCarrierId(String carrierId) {
		this.carrierId = carrierId;
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

	public String getIsLight() {
		return isLight;
	}

	public void setIsLight(String isLight) {
		this.isLight = isLight;
	}

	public String getFeesNo() {
		return feesNo;
	}

	public void setFeesNo(String feesNo) {
		this.feesNo = feesNo;
	}

	public Timestamp getWriteTime() {
		return writeTime;
	}

	public void setWriteTime(Timestamp writeTime) {
		this.writeTime = writeTime;
	}

	public Timestamp getCalculateTime() {
		return calculateTime;
	}

	public void setCalculateTime(Timestamp calculateTime) {
		this.calculateTime = calculateTime;
	}

	public BigDecimal getCalResult() {
		return calResult;
	}

	public void setCalResult(BigDecimal calResult) {
		this.calResult = calResult;
	}

	public String getCalMethod() {
		return calMethod;
	}

	public void setCalMethod(String calMethod) {
		this.calMethod = calMethod;
	}
	
}
