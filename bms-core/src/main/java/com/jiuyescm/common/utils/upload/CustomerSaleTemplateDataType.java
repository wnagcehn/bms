package com.jiuyescm.common.utils.upload;

public class CustomerSaleTemplateDataType extends BaseDataType{

	public CustomerSaleTemplateDataType() {
		this.name = "原始销售员导入模板";
		this.dataProps.add(new DataProperty("customerName", "商家"));
		this.dataProps.add(new DataProperty("originSellerName", "原始销售人员"));
		
	}
}
