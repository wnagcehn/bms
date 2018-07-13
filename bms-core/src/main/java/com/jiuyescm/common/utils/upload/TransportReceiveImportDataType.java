package com.jiuyescm.common.utils.upload;

public class TransportReceiveImportDataType extends BaseDataType{

	public TransportReceiveImportDataType() {
		
		this.name = "应收运输路线模板";
		this.dataProps.add(new DataProperty("fromWarehouseName", "始发仓名称"));
		this.dataProps.add(new DataProperty("fromProvinceName", "始发省份"));
		this.dataProps.add(new DataProperty("fromCityName", "始发城市"));
		this.dataProps.add(new DataProperty("fromDistrictName", "始发地区"));
		this.dataProps.add(new DataProperty("fromAddress", "始发地址"));
		
		this.dataProps.add(new DataProperty("endWarehouseName", "目的仓名称"));
		this.dataProps.add(new DataProperty("toProvinceName", "目的省份"));
		this.dataProps.add(new DataProperty("toCityName", "目的城市"));
		this.dataProps.add(new DataProperty("toDistrictName", "目的地区"));
		this.dataProps.add(new DataProperty("toAddress", "目的地址"));
		this.dataProps.add(new DataProperty("sendCycle", "发车周期"));
		this.dataProps.add(new DataProperty("timeliness", "时效"));
		this.dataProps.add(new DataProperty("remark", "路线备注"));
		//--------------------------------------------------------------------------
		this.dataProps.add(new DataProperty("temperatureTypeName", "温度类型"));
		this.dataProps.add(new DataProperty("productTypeName", "品类"));
		this.dataProps.add(new DataProperty("minWeightShipment", "最低起运"));
		this.dataProps.add(new DataProperty("weightLowerLimit", "重量下限"));
		this.dataProps.add(new DataProperty("weightUpperLimit", "重量上限"));
		
		this.dataProps.add(new DataProperty("minDistance", "距离下限"));
		this.dataProps.add(new DataProperty("maxDistance", "距离上限"));
		this.dataProps.add(new DataProperty("minTime", "时间下限"));
		this.dataProps.add(new DataProperty("maxTime", "时间上限"));
		
		this.dataProps.add(new DataProperty("numLowerLimit", "数量下限"));
		this.dataProps.add(new DataProperty("numUpperLimit", "数量上限"));
		this.dataProps.add(new DataProperty("skuLowerLimit", "SKU下限"));
		this.dataProps.add(new DataProperty("skuUpperLimit", "SKU上限"));
		this.dataProps.add(new DataProperty("volumeLowerLimit", "体积下限"));
		this.dataProps.add(new DataProperty("volumeUpperLimit", "体积上限"));
		this.dataProps.add(new DataProperty("carModelName", "车型"));
		this.dataProps.add(new DataProperty("bubbleWeight", "泡重"));
		this.dataProps.add(new DataProperty("unitPrice", "单价"));
		this.dataProps.add(new DataProperty("remark", "报价备注"));
	}										


}
