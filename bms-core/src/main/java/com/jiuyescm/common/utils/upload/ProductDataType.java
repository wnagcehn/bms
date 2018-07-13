package com.jiuyescm.common.utils.upload;

public class ProductDataType extends BaseDataType{

	public ProductDataType() {
		this.name = "商品资料";
		this.dataProps.add(new DataProperty("customerid", "商家ID"));
		this.dataProps.add(new DataProperty("shortname", "商品简称"));
		this.dataProps.add(new DataProperty("productname", "商品名称"));
		this.dataProps.add(new DataProperty("extendid", "外部商品编码"));
		this.dataProps.add(new DataProperty("shelflife", "是否保质期管理"));
		this.dataProps.add(new DataProperty("shelflifehour", "保质期(天)"));
		this.dataProps.add(new DataProperty("shelfalertday", "近效期(天)"));
		this.dataProps.add(new DataProperty("shelflockupday", "禁售天数(天)"));
		this.dataProps.add(new DataProperty("batch", "是否批次管理"));
		this.dataProps.add(new DataProperty("tempraturetype", "温度类别"));
		this.dataProps.add(new DataProperty("uomid", "度量单位ID"));
		this.dataProps.add(new DataProperty("grossweight", "毛重KG"));
		this.dataProps.add(new DataProperty("netweight", "净重KG"));
		this.dataProps.add(new DataProperty("length", "长CM"));
		this.dataProps.add(new DataProperty("width", "宽CM"));
		this.dataProps.add(new DataProperty("height", "高CM"));
	}
}
