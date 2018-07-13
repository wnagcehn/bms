package com.jiuyescm.common.utils.upload;

public class DispatchWayBillDataType extends BaseDataType{

	public DispatchWayBillDataType() {
		this.name = "应收配送运单导入模板";
		this.dataProps.add(new DataProperty("warehouseName", "仓库名称"));
		this.dataProps.add(new DataProperty("customerName", "商家名称"));
		this.dataProps.add(new DataProperty("carrierName", "物流商名称"));
		this.dataProps.add(new DataProperty("deliverName", "宅配商名称"));
		this.dataProps.add(new DataProperty("waybillNo", "运单号"));
		this.dataProps.add(new DataProperty("receiveProvinceName", "收货省"));
		this.dataProps.add(new DataProperty("receiveCityName", "收货市"));
		this.dataProps.add(new DataProperty("receiveDistrictName", "收货区县"));
		this.dataProps.add(new DataProperty("totalVolume", "体积"));
		this.dataProps.add(new DataProperty("temperatureTypeCode", "温度类型"));
		this.dataProps.add(new DataProperty("totalWeight", "重量"));
		this.dataProps.add(new DataProperty("createTime", "创建时间"));
	}										
}
