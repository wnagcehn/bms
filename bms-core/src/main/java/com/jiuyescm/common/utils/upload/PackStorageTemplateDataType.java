package com.jiuyescm.common.utils.upload;

public class PackStorageTemplateDataType extends BaseDataType{

	public PackStorageTemplateDataType(){
		this.name = "耗材库存";
		this.dataProps.add(new DataProperty("curTime", "库存日期"));
		this.dataProps.add(new DataProperty("warehouseName", "仓库"));
		this.dataProps.add(new DataProperty("customerName", "商家"));
		this.dataProps.add(new DataProperty("temperatureTypeName", "温度"));
		this.dataProps.add(new DataProperty("palletNum", "托数"));
		
	}
}
