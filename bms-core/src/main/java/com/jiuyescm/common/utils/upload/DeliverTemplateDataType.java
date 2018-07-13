package com.jiuyescm.common.utils.upload;

public class DeliverTemplateDataType extends BaseDataType{

	public DeliverTemplateDataType() {
		this.name = "九耶配送报价模板";
		this.dataProps.add(new DataProperty("startWarehouseName", "始发仓名称"));
		this.dataProps.add(new DataProperty("areaTypeCode", "地域类型"));
		this.dataProps.add(new DataProperty("provinceName", "省份"));
		this.dataProps.add(new DataProperty("cityName", "城市"));
		this.dataProps.add(new DataProperty("areaName", "地区"));
		this.dataProps.add(new DataProperty("timeliness", "时效"));
		this.dataProps.add(new DataProperty("weightLimit", "重量界限"));
		this.dataProps.add(new DataProperty("unitPrice", "单价"));
		this.dataProps.add(new DataProperty("firstWeight", "首重重量"));
		this.dataProps.add(new DataProperty("firstWeightPrice", "首重价格"));
		this.dataProps.add(new DataProperty("continuedWeight", "续重重量"));
		this.dataProps.add(new DataProperty("continuedPrice", "续重价格"));
		this.dataProps.add(new DataProperty("temperatureTypeCode", "温度类别"));
		this.dataProps.add(new DataProperty("extra1", "扩展字段1"));
		this.dataProps.add(new DataProperty("extra2", "扩展字段2"));
		this.dataProps.add(new DataProperty("extra3", "扩展字段3"));
		this.dataProps.add(new DataProperty("extra4", "扩展字段4"));
		
	}
}
