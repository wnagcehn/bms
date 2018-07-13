package com.jiuyescm.common.utils.upload;

public class DispatchQuoteTemplateDataType extends BaseDataType{

	public DispatchQuoteTemplateDataType() {
		this.name = "九耶配送报价模板";
		this.dataProps.add(new DataProperty("startWarehouseName", "始发仓名称"));
		this.dataProps.add(new DataProperty("provinceName", "目的省份"));
		this.dataProps.add(new DataProperty("cityName", "目的城市"));
		this.dataProps.add(new DataProperty("areaName", "目的地区"));
		this.dataProps.add(new DataProperty("weightDown", "重量下限"));
		this.dataProps.add(new DataProperty("weightUp", "重量上限"));
		this.dataProps.add(new DataProperty("unitPrice", "单价"));
		this.dataProps.add(new DataProperty("firstWeight", "首重重量(kg)"));
		this.dataProps.add(new DataProperty("firstWeightPrice", "首重价格"));
		this.dataProps.add(new DataProperty("continuedWeight", "续重重量(kg)"));
		this.dataProps.add(new DataProperty("continuedPrice", "续重价格"));
		this.dataProps.add(new DataProperty("timeliness", "时效"));
		this.dataProps.add(new DataProperty("temperatureTypeCode", "温度类型"));
		this.dataProps.add(new DataProperty("areaTypeCode", "地域类型"));
		this.dataProps.add(new DataProperty("productCase", "特殊计费商品"));
		this.dataProps.add(new DataProperty("deliverid", "特殊宅配商(全称)"));
		
	}
}
