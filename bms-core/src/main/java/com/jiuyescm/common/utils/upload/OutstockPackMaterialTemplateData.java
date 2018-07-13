package com.jiuyescm.common.utils.upload;

public class OutstockPackMaterialTemplateData extends BaseDataType{

	public OutstockPackMaterialTemplateData() {
		this.name = "耗材出库";
		this.dataProps.add(new DataProperty("createTime", "出库日期"));
		this.dataProps.add(new DataProperty("warehouseName", "仓库"));
		this.dataProps.add(new DataProperty("customerName", "商家"));
		this.dataProps.add(new DataProperty("outstockNo", "出库单号"));
		this.dataProps.add(new DataProperty("waybillNo", "运单号"));
		this.dataProps.add(new DataProperty("consumerMaterialCode", "耗材编号"));
		this.dataProps.add(new DataProperty("consumerMaterialName", "耗材名称"));
		this.dataProps.add(new DataProperty("num", "数量"));
		this.dataProps.add(new DataProperty("weight", "重量"));
	}

	
}
