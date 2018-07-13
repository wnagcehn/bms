/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.biz.transport.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * 干线运单RPC实体类
 * 
 * @author yangss
 */
public class BizGanxianWaybillVo implements Serializable{

	private static final long serialVersionUID = -798968136520350891L;

	// 订单号
	private String orderNo;
	private String tmsId;
	// 商家
	private String customerId;
	private String customerName;
	// 路单号
	private String ganxianNo;
	// 运单号
	private String waybillNo;
	// 业务类型
	private String bizTypeCode;
	// 始发仓库CODE
	private String warehouseCode;
	// 始发仓库名称
	private String warehouseName;
	// 发货省
	private String sendProvinceId;
	// 发货市
	private String sendCityId;
	// 发货区县
	private String sendDistrictId;
	// 收货省
	private String receiverProvinceId;
	// 收货市
	private String receiverCityId;
	// 收货区县
	private String receiverDistrictId;
	// 收件人
	private String receiver;
	// 收件人电话
	private String receiverTelephone;
	// 始发站点
	private String startStation;
	// 目的站点
	private String endStation;
	// 总箱数
	private Double totalBox;
	// 总件数
	private Double totalPackage;
	// 总品种数
	private Double totalVarieties;
	// 总体积
	private Double totalVolume;
	// 总重量
	private Double totalWeight;
	// 调整重量
	private Double adjustWeight;
	// 温度类型
	private String temperatureTypeCode;
	// 是否轻货
	private String isLight;
	// 发货时间
	private Timestamp sendTime;
	// 收货时间
	private Timestamp signTime;
	// 备注
	private String remark;
	// 费用编号
	private String feesNo;
	// 状态
	private String accountState;
	// 结算状态
	private String isCalculated;;

	// 运输类型
	private String transportTypeCode;
	// 合同编码
	private String contractCode;
	// 报价模板编号
	private String transportTemplateCode;

	// 发货省
	private String sendProvinceName;
	// 发货市
	private String sendCityName;
	// 发货区县
	private String sendDistrictName;
	// 收货省
	private String receiverProvinceName;
	// 收货市
	private String receiverCityName;
	// 收货区县
	private String receiverDistrictName;

	// 写入BMS时间
	private Timestamp writeTime;
	// 费用计算时间
	private Timestamp calculateTime;

	// 计算结果
	private BigDecimal calResult;
	private String calMethod;
	// 车型
	private String carModel;
	private String transportType;

	private long num;

	// 始发站点编号
	private String startStationCode;
	// 计费类型
	private String chargeType;
	// 接口类型
	private String interfaceType;
	// 实际金额(TMS计算)
	private double amount;
	// 理论总金额(BMS计算)
	private double sysAmount;

	private String creator;
	private java.sql.Timestamp createTime;
	private String lastModifier;
	private java.sql.Timestamp lastModifyTime;
	private String delFlag;

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getTmsId() {
		return tmsId;
	}

	public void setTmsId(String tmsId) {
		this.tmsId = tmsId;
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

	public String getGanxianNo() {
		return ganxianNo;
	}

	public void setGanxianNo(String ganxianNo) {
		this.ganxianNo = ganxianNo;
	}

	public String getWaybillNo() {
		return waybillNo;
	}

	public void setWaybillNo(String waybillNo) {
		this.waybillNo = waybillNo;
	}

	public String getBizTypeCode() {
		return bizTypeCode;
	}

	public void setBizTypeCode(String bizTypeCode) {
		this.bizTypeCode = bizTypeCode;
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

	public Double getTotalBox() {
		return totalBox;
	}

	public void setTotalBox(Double totalBox) {
		this.totalBox = totalBox;
	}

	public Double getTotalPackage() {
		return totalPackage;
	}

	public void setTotalPackage(Double totalPackage) {
		this.totalPackage = totalPackage;
	}

	public Double getTotalVarieties() {
		return totalVarieties;
	}

	public void setTotalVarieties(Double totalVarieties) {
		this.totalVarieties = totalVarieties;
	}

	public Double getTotalVolume() {
		return totalVolume;
	}

	public void setTotalVolume(Double totalVolume) {
		this.totalVolume = totalVolume;
	}

	public Double getTotalWeight() {
		return totalWeight;
	}

	public void setTotalWeight(Double totalWeight) {
		this.totalWeight = totalWeight;
	}

	public Double getAdjustWeight() {
		return adjustWeight;
	}

	public void setAdjustWeight(Double adjustWeight) {
		this.adjustWeight = adjustWeight;
	}

	public String getTemperatureTypeCode() {
		return temperatureTypeCode;
	}

	public void setTemperatureTypeCode(String temperatureTypeCode) {
		this.temperatureTypeCode = temperatureTypeCode;
	}

	public String getIsLight() {
		return isLight;
	}

	public void setIsLight(String isLight) {
		this.isLight = isLight;
	}

	public Timestamp getSendTime() {
		return sendTime;
	}

	public void setSendTime(Timestamp sendTime) {
		this.sendTime = sendTime;
	}

	public Timestamp getSignTime() {
		return signTime;
	}

	public void setSignTime(Timestamp signTime) {
		this.signTime = signTime;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getFeesNo() {
		return feesNo;
	}

	public void setFeesNo(String feesNo) {
		this.feesNo = feesNo;
	}

	public String getAccountState() {
		return accountState;
	}

	public void setAccountState(String accountState) {
		this.accountState = accountState;
	}

	public String getIsCalculated() {
		return isCalculated;
	}

	public void setIsCalculated(String isCalculated) {
		this.isCalculated = isCalculated;
	}

	public String getTransportTypeCode() {
		return transportTypeCode;
	}

	public void setTransportTypeCode(String transportTypeCode) {
		this.transportTypeCode = transportTypeCode;
	}

	public String getContractCode() {
		return contractCode;
	}

	public void setContractCode(String contractCode) {
		this.contractCode = contractCode;
	}

	public String getTransportTemplateCode() {
		return transportTemplateCode;
	}

	public void setTransportTemplateCode(String transportTemplateCode) {
		this.transportTemplateCode = transportTemplateCode;
	}

	public String getSendProvinceName() {
		return sendProvinceName;
	}

	public void setSendProvinceName(String sendProvinceName) {
		this.sendProvinceName = sendProvinceName;
	}

	public String getSendCityName() {
		return sendCityName;
	}

	public void setSendCityName(String sendCityName) {
		this.sendCityName = sendCityName;
	}

	public String getSendDistrictName() {
		return sendDistrictName;
	}

	public void setSendDistrictName(String sendDistrictName) {
		this.sendDistrictName = sendDistrictName;
	}

	public String getReceiverProvinceName() {
		return receiverProvinceName;
	}

	public void setReceiverProvinceName(String receiverProvinceName) {
		this.receiverProvinceName = receiverProvinceName;
	}

	public String getReceiverCityName() {
		return receiverCityName;
	}

	public void setReceiverCityName(String receiverCityName) {
		this.receiverCityName = receiverCityName;
	}

	public String getReceiverDistrictName() {
		return receiverDistrictName;
	}

	public void setReceiverDistrictName(String receiverDistrictName) {
		this.receiverDistrictName = receiverDistrictName;
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

	public String getCarModel() {
		return carModel;
	}

	public void setCarModel(String carModel) {
		this.carModel = carModel;
	}

	public String getTransportType() {
		return transportType;
	}

	public void setTransportType(String transportType) {
		this.transportType = transportType;
	}

	public long getNum() {
		return num;
	}

	public void setNum(long num) {
		this.num = num;
	}

	public String getStartStationCode() {
		return startStationCode;
	}

	public void setStartStationCode(String startStationCode) {
		this.startStationCode = startStationCode;
	}

	public String getChargeType() {
		return chargeType;
	}

	public void setChargeType(String chargeType) {
		this.chargeType = chargeType;
	}

	public String getInterfaceType() {
		return interfaceType;
	}

	public void setInterfaceType(String interfaceType) {
		this.interfaceType = interfaceType;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public double getSysAmount() {
		return sysAmount;
	}

	public void setSysAmount(double sysAmount) {
		this.sysAmount = sysAmount;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public java.sql.Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(java.sql.Timestamp createTime) {
		this.createTime = createTime;
	}

	public String getLastModifier() {
		return lastModifier;
	}

	public void setLastModifier(String lastModifier) {
		this.lastModifier = lastModifier;
	}

	public java.sql.Timestamp getLastModifyTime() {
		return lastModifyTime;
	}

	public void setLastModifyTime(java.sql.Timestamp lastModifyTime) {
		this.lastModifyTime = lastModifyTime;
	}

	public String getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}

}
