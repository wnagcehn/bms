/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.biz.transport.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.jiuyescm.bms.common.BmsCommonAttribute;
import com.jiuyescm.bms.quotation.transport.entity.PriceTransportLineEntity;

/**
 * 干线运单
 * 
 * @author wubangjun
 */
public class BizGanxianWayBillEntity extends BmsCommonAttribute {

	private static final long serialVersionUID = 1L;
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
	// 产品类型
	private String productType;
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
	// 省份是否计算
	private String validProvince;
	//报价list
	private List<PriceTransportLineEntity> priceList;

	public BizGanxianWayBillEntity() {

	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
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
		if (totalWeight != null) {
			return totalWeight;
		} else {
			return 0.0d;
		}
	}

	public void setTotalWeight(Double totalWeight) {
		this.totalWeight = totalWeight;
	}

	public String getTemperatureTypeCode() {
		return temperatureTypeCode;
	}

	public void setTemperatureTypeCode(String temperatureTypeCode) {
		this.temperatureTypeCode = temperatureTypeCode;
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
		if (StringUtils.isNotEmpty(remark) && remark.length() > 128) {
			this.remark = remark.substring(0, 100);
		} else {
			this.remark = remark;
		}
	}

	public String getTmsId() {
		return tmsId;
	}

	public void setTmsId(String tmsId) {
		this.tmsId = tmsId;
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

	public Double getAdjustWeight() {
		if (adjustWeight != null) {
			return adjustWeight;
		} else {
			return 0.0d;
		}
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

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
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

	public Timestamp getSendTime() {
		return sendTime;
	}

	public void setSendTime(Timestamp sendTime) {
		this.sendTime = sendTime;
	}

	public String getFeesNo() {
		return feesNo;
	}

	public void setFeesNo(String feesNo) {
		this.feesNo = feesNo;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
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

	public String getAccountState() {
		return accountState;
	}

	public void setAccountState(String accountState) {
		this.accountState = accountState;
	}

	public String getValidProvince() {
		return validProvince;
	}

	public void setValidProvince(String validProvince) {
		this.validProvince = validProvince;
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

	public List<PriceTransportLineEntity> getPriceList() {
		return priceList;
	}

	public void setPriceList(List<PriceTransportLineEntity> priceList) {
		this.priceList = priceList;
	}

	public String getProductType() {
		return productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}
	
	
}
