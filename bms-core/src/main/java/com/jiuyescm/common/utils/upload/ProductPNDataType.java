package com.jiuyescm.common.utils.upload;

public class ProductPNDataType extends BaseDataType{

	public ProductPNDataType() {
		this.name = "商品PN";
		this.dataProps.add(new DataProperty("productid", "商品ID"));
		this.dataProps.add(new DataProperty("pncode", "PN码"));
	}
}
