package com.jiuyescm.bms.quotation.dispatch.entity.vo;

import com.jiuyescm.cfm.domain.IEntity;

public class PriceMainDispatchImportEntity implements IEntity{

	private static final long serialVersionUID = -3097791189817685521L;

	//始发仓名称
	private String startWarehouseName;
	
	//省份名称
	private String provinceName;
	
	private String cityName;
	
	//县区名称
	private String areaName;
	// 温度类型
	private String temperatureTypeCode;
	// extra1
	private String extra1;
	// extra2
	private String extra2;
	// extra3
	private String extra3;
	// extra4
	private String extra4;
	// 地域类型
	private String areaTypeCode;
	// 时效
	private String timeliness;
	// 重量界限
	private Double weightLimit;
	// 单价
	private Double unitPrice;
	// 首重重量
	private Double firstWeight;
	// 首重价格
	private Double firstWeightPrice;
	// 续重重量
	private Double continuedWeight;
	// 续重价格
	private Double continuedPrice;
	public String getStartWarehouseName() {
		return startWarehouseName;
	}
	public void setStartWarehouseName(String startWarehouseName) {
		this.startWarehouseName = startWarehouseName;
	}
	public String getProvinceName() {
		return provinceName;
	}
	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	public String getAreaName() {
		return areaName;
	}
	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}
	public String getTemperatureTypeCode() {
		return temperatureTypeCode;
	}
	public void setTemperatureTypeCode(String temperatureTypeCode) {
		this.temperatureTypeCode = temperatureTypeCode;
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
	public String getAreaTypeCode() {
		return areaTypeCode;
	}
	public void setAreaTypeCode(String areaTypeCode) {
		this.areaTypeCode = areaTypeCode;
	}
	public String getTimeliness() {
		return timeliness;
	}
	public void setTimeliness(String timeliness) {
		this.timeliness = timeliness;
	}
	public Double getWeightLimit() {
		return weightLimit;
	}
	public void setWeightLimit(Double weightLimit) {
		this.weightLimit = weightLimit;
	}
	public Double getUnitPrice() {
		return unitPrice;
	}
	public void setUnitPrice(Double unitPrice) {
		this.unitPrice = unitPrice;
	}
	public Double getFirstWeight() {
		return firstWeight;
	}
	public void setFirstWeight(Double firstWeight) {
		this.firstWeight = firstWeight;
	}
	public Double getFirstWeightPrice() {
		return firstWeightPrice;
	}
	public void setFirstWeightPrice(Double firstWeightPrice) {
		this.firstWeightPrice = firstWeightPrice;
	}
	public Double getContinuedWeight() {
		return continuedWeight;
	}
	public void setContinuedWeight(Double continuedWeight) {
		this.continuedWeight = continuedWeight;
	}
	public Double getContinuedPrice() {
		return continuedPrice;
	}
	public void setContinuedPrice(Double continuedPrice) {
		this.continuedPrice = continuedPrice;
	}
}
