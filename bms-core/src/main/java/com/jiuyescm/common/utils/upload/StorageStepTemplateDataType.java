package com.jiuyescm.common.utils.upload;

public class StorageStepTemplateDataType extends BaseDataType{

	public StorageStepTemplateDataType() {
		this.name = "九曳仓储基础报价模板";
		this.dataProps.add(new DataProperty("warehouseCode", "仓库"));
		this.dataProps.add(new DataProperty("temperatureTypeCode", "温度类型"));
		this.dataProps.add(new DataProperty("numLower", "下限"));
		this.dataProps.add(new DataProperty("numUpper", "上限"));
		this.dataProps.add(new DataProperty("unitPrice", "单价"));
		this.dataProps.add(new DataProperty("firstNum", "首量"));
		this.dataProps.add(new DataProperty("firstPrice", "首价"));
		this.dataProps.add(new DataProperty("continuedItem", "续量"));
		this.dataProps.add(new DataProperty("continuedPrice", "续价"));
		this.dataProps.add(new DataProperty("remark", "备注"));		
	}
}    