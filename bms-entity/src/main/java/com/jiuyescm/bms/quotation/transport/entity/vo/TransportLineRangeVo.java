package com.jiuyescm.bms.quotation.transport.entity.vo;

import com.jiuyescm.cfm.domain.IEntity;

public class TransportLineRangeVo implements IEntity{
	// TODO Change serialVersionUID by eclipse
    private static final long serialVersionUID = -1;
    
	// id
	private Long id;
	// 模版编号
	private String lineId;
	private String temperatureTypeName;
	private String productTypeName;
	private String weightLowerLimit;
	private String weightUpperLimit;
	private String numLowerLimit;
	private String numUpperLimit;
	private String skuLowerLimit;
	private String skuUpperLimit;
	private String volumeLowerLimit;
	private String volumeUpperLimit;
	private String carModel;
	private String unitPrice;
	private String remark;

	public TransportLineRangeVo() {

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLineId() {
		return lineId;
	}

	public void setLineId(String lineId) {
		this.lineId = lineId;
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

	public String getWeightLowerLimit() {
		return weightLowerLimit;
	}

	public void setWeightLowerLimit(String weightLowerLimit) {
		this.weightLowerLimit = weightLowerLimit;
	}

	public String getWeightUpperLimit() {
		return weightUpperLimit;
	}

	public void setWeightUpperLimit(String weightUpperLimit) {
		this.weightUpperLimit = weightUpperLimit;
	}

	public String getNumLowerLimit() {
		return numLowerLimit;
	}

	public void setNumLowerLimit(String numLowerLimit) {
		this.numLowerLimit = numLowerLimit;
	}

	public String getNumUpperLimit() {
		return numUpperLimit;
	}

	public void setNumUpperLimit(String numUpperLimit) {
		this.numUpperLimit = numUpperLimit;
	}

	public String getSkuLowerLimit() {
		return skuLowerLimit;
	}

	public void setSkuLowerLimit(String skuLowerLimit) {
		this.skuLowerLimit = skuLowerLimit;
	}

	public String getSkuUpperLimit() {
		return skuUpperLimit;
	}

	public void setSkuUpperLimit(String skuUpperLimit) {
		this.skuUpperLimit = skuUpperLimit;
	}

	public String getVolumeLowerLimit() {
		return volumeLowerLimit;
	}

	public void setVolumeLowerLimit(String volumeLowerLimit) {
		this.volumeLowerLimit = volumeLowerLimit;
	}

	public String getVolumeUpperLimit() {
		return volumeUpperLimit;
	}

	public void setVolumeUpperLimit(String volumeUpperLimit) {
		this.volumeUpperLimit = volumeUpperLimit;
	}

	public String getCarModel() {
		return carModel;
	}

	public void setCarModel(String carModel) {
		this.carModel = carModel;
	}

	public String getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(String unitPrice) {
		this.unitPrice = unitPrice;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
}
