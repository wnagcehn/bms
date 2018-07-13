/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.quotation.transport.entity;

import com.jiuyescm.bms.common.BmsCommonAttribute;

/**
 * 运输线路对应的运输梯度报价
 * @author wubangjun
 */
public class PriceTransportLineRangeEntity extends BmsCommonAttribute {

	private static final long serialVersionUID = 1L;
	/**
	 * 温度类型
	 */
	private String temperatureTypeCode;
	private String temperatureTypeName;
	/**
	 * 品类
	 */
	private String productTypeCode;
	private String productTypeName;
	/**
	 * 最低起运（kg）
	 */
	private Double minWeightShipment;
	// 最低起运价
	private Double minPriceShipment;
	/**
	 * 重量下限
	 */
	private Double weightLowerLimit;
	/**
	 * 重量上限
	 */
	private Double weightUpperLimit;
	/**
	 * 数量下限
	 */
	private Double numLowerLimit;
	/**
	 * 数量上限
	 */
	private Double numUpperLimit;
	/**
	 * SKU下限
	 */
	private Double skuLowerLimit;
	/**
	 * SKU上限
	 */
	private Double skuUpperLimit;
	/**
	 * 体积下限
	 */
	private Double volumeLowerLimit;
	/**
	 * 距离下限
	 */
	private Double minDistance;
	/**
	 * 距离上限
	 */
	private Double maxDistance;
	/**
	 * 时间下限
	 */
	private Double minTime;
	/**
	 * 时间上限
	 */
	private Double maxTime;
	
	/**
	 * 体积上限
	 */
	private Double volumeUpperLimit;
	/**
	 * 车型
	 */
	private String carModelCode;
	private String carModelName;
	/**
	 * 单价
	 */
	private Double unitPrice;
	
	private Boolean isLight;
	/**
	 * 备注
	 */
	private String remark;
	/**
	 * 对应线路
	 */
	private String lineId;
	
	/**
	 *  起运箱数
	 */
	private Double boxShipment;
	/**
	 *  箱数下限
	 */
	private Double boxLowerLimit;
	/**
	 *  箱数上限
	 */
	private Double boxUpperLimit;
	/**
	 *  起运点数
	 */
	private Double pointShipment;
	/**
	 *  点数下限
	 */
	private Double pointLowerLimit;
	/**
	 *  点数上限
	 */
	private Double pointUpperLimit;
	
	private String extra1;
	
	private String extra2;
	
	private String extra3;
	
	private String extra4;
	
	private String extra5;
	
	public PriceTransportLineRangeEntity() {

	}


	public String getTemperatureTypeCode() {
		return temperatureTypeCode;
	}


	public void setTemperatureTypeCode(String temperatureTypeCode) {
		this.temperatureTypeCode = temperatureTypeCode;
	}


	public String getProductTypeCode() {
		return productTypeCode;
	}


	public void setProductTypeCode(String productTypeCode) {
		this.productTypeCode = productTypeCode;
	}

	public String getCarModelCode() {
		return carModelCode;
	}


	public void setCarModelCode(String carModelCode) {
		this.carModelCode = carModelCode;
	}

	public String getRemark() {
		return remark;
	}


	public void setRemark(String remark) {
		this.remark = remark;
	}


	public String getLineId() {
		return lineId;
	}


	public void setLineId(String lineId) {
		this.lineId = lineId;
	}


	public Double getWeightLowerLimit() {
		return weightLowerLimit;
	}


	public void setWeightLowerLimit(Double weightLowerLimit) {
		this.weightLowerLimit = weightLowerLimit;
	}


	public Double getWeightUpperLimit() {
		return weightUpperLimit;
	}


	public void setWeightUpperLimit(Double weightUpperLimit) {
		this.weightUpperLimit = weightUpperLimit;
	}


	public Double getNumLowerLimit() {
		return numLowerLimit;
	}


	public void setNumLowerLimit(Double numLowerLimit) {
		this.numLowerLimit = numLowerLimit;
	}


	public Double getNumUpperLimit() {
		return numUpperLimit;
	}


	public void setNumUpperLimit(Double numUpperLimit) {
		this.numUpperLimit = numUpperLimit;
	}


	public Double getSkuLowerLimit() {
		return skuLowerLimit;
	}


	public void setSkuLowerLimit(Double skuLowerLimit) {
		this.skuLowerLimit = skuLowerLimit;
	}


	public Double getSkuUpperLimit() {
		return skuUpperLimit;
	}


	public void setSkuUpperLimit(Double skuUpperLimit) {
		this.skuUpperLimit = skuUpperLimit;
	}


	public Double getVolumeLowerLimit() {
		return volumeLowerLimit;
	}


	public void setVolumeLowerLimit(Double volumeLowerLimit) {
		this.volumeLowerLimit = volumeLowerLimit;
	}


	public Double getVolumeUpperLimit() {
		return volumeUpperLimit;
	}


	public void setVolumeUpperLimit(Double volumeUpperLimit) {
		this.volumeUpperLimit = volumeUpperLimit;
	}


	public Double getUnitPrice() {
		return unitPrice;
	}


	public void setUnitPrice(Double unitPrice) {
		this.unitPrice = unitPrice;
	}


	public String getTemperatureTypeName() {
		return temperatureTypeName;
	}


	public void setTemperatureTypeName(String temperatureTypeName) {
		this.temperatureTypeName = temperatureTypeName;
	}


	public String getProductTypeName() {
		return productTypeName;
	}


	public void setProductTypeName(String productTypeName) {
		this.productTypeName = productTypeName;
	}


	public String getCarModelName() {
		return carModelName;
	}


	public void setCarModelName(String carModelName) {
		this.carModelName = carModelName;
	}


	public String getExtra1() {
		return extra1;
	}


	public void setExtra1(String extra1) {
		this.extra1 = extra1;
	}


	public String getExtra2() {
		return extra2;
	}


	public void setExtra2(String extra2) {
		this.extra2 = extra2;
	}


	public String getExtra3() {
		return extra3;
	}


	public void setExtra3(String extra3) {
		this.extra3 = extra3;
	}


	public String getExtra4() {
		return extra4;
	}


	public void setExtra4(String extra4) {
		this.extra4 = extra4;
	}


	public String getExtra5() {
		return extra5;
	}


	public void setExtra5(String extra5) {
		this.extra5 = extra5;
	}


	public Double getMinWeightShipment() {
		return minWeightShipment;
	}


	public void setMinWeightShipment(Double minWeightShipment) {
		this.minWeightShipment = minWeightShipment;
	}


	public Double getMinDistance() {
		return minDistance;
	}


	public void setMinDistance(Double minDistance) {
		this.minDistance = minDistance;
	}


	public Double getMaxDistance() {
		return maxDistance;
	}


	public void setMaxDistance(Double maxDistance) {
		this.maxDistance = maxDistance;
	}


	public Double getMinTime() {
		return minTime;
	}


	public void setMinTime(Double minTime) {
		this.minTime = minTime;
	}


	public Double getMaxTime() {
		return maxTime;
	}


	public void setMaxTime(Double maxTime) {
		this.maxTime = maxTime;
	}


	public Boolean getIsLight() {
		return isLight;
	}


	public void setIsLight(Boolean isLight) {
		this.isLight = isLight;
	}


	public Double getMinPriceShipment() {
		return minPriceShipment;
	}


	public void setMinPriceShipment(Double minPriceShipment) {
		this.minPriceShipment = minPriceShipment;
	}


	public Double getBoxShipment() {
		return boxShipment;
	}


	public void setBoxShipment(Double boxShipment) {
		this.boxShipment = boxShipment;
	}


	public Double getBoxLowerLimit() {
		return boxLowerLimit;
	}


	public void setBoxLowerLimit(Double boxLowerLimit) {
		this.boxLowerLimit = boxLowerLimit;
	}


	public Double getBoxUpperLimit() {
		return boxUpperLimit;
	}


	public void setBoxUpperLimit(Double boxUpperLimit) {
		this.boxUpperLimit = boxUpperLimit;
	}


	public Double getPointShipment() {
		return pointShipment;
	}


	public void setPointShipment(Double pointShipment) {
		this.pointShipment = pointShipment;
	}


	public Double getPointLowerLimit() {
		return pointLowerLimit;
	}


	public void setPointLowerLimit(Double pointLowerLimit) {
		this.pointLowerLimit = pointLowerLimit;
	}


	public Double getPointUpperLimit() {
		return pointUpperLimit;
	}


	public void setPointUpperLimit(Double pointUpperLimit) {
		this.pointUpperLimit = pointUpperLimit;
	}
	
}
