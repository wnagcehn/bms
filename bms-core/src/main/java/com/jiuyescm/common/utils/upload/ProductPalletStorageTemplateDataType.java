package com.jiuyescm.common.utils.upload;

public class ProductPalletStorageTemplateDataType extends BaseDataType{

	public ProductPalletStorageTemplateDataType() {
		
		this.name = "商品按托库存";
		this.dataProps.add(new DataProperty("stockTime", "库存日期"));
		this.dataProps.add(new DataProperty("warehouseName", "仓库名"));
		this.dataProps.add(new DataProperty("customerName", "商家"));
		this.dataProps.add(new DataProperty("temperatureTypeName", "温度"));
		this.dataProps.add(new DataProperty("palletNum", "托数"));
	}										


}
