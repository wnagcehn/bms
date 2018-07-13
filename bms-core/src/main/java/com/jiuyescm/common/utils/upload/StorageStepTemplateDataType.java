package com.jiuyescm.common.utils.upload;

public class StorageStepTemplateDataType extends BaseDataType{

	public StorageStepTemplateDataType() {
		this.name = "九曳仓储基础报价模板";
		this.dataProps.add(new DataProperty("temperatureTypeCode", "温度类型"));
		this.dataProps.add(new DataProperty("unitPrice", "单价"));
		this.dataProps.add(new DataProperty("continuedItem", "续件价格"));
		this.dataProps.add(new DataProperty("weightLower", "重量下限"));
		this.dataProps.add(new DataProperty("weightUpper", "重量上限"));
		this.dataProps.add(new DataProperty("numUpper", "数量上限"));
		this.dataProps.add(new DataProperty("numLower", "数量下限"));
		this.dataProps.add(new DataProperty("skuUpper", "sku上限"));
		this.dataProps.add(new DataProperty("skuLower", "sku下限"));
		this.dataProps.add(new DataProperty("volumeUpper", "体积上限"));
		this.dataProps.add(new DataProperty("volumeLower", "体积下限"));
		this.dataProps.add(new DataProperty("userDefine1", "扩展1"));
		this.dataProps.add(new DataProperty("userDefine2", "扩展2"));
		this.dataProps.add(new DataProperty("userDefine3", "扩展3"));
		this.dataProps.add(new DataProperty("userDefine4", "扩展4"));
		this.dataProps.add(new DataProperty("userDefine5", "扩展5"));
		
		
	}
}    