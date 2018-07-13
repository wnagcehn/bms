package com.jiuyescm.common.utils.upload;

public class TransportLineImportTemplateDataType extends BaseDataType{

	public TransportLineImportTemplateDataType() {
		
		this.name = "九耶运输路线模板";
		this.dataProps.add(new DataProperty("carrierName", "承运商"));
		this.dataProps.add(new DataProperty("transportLineName", "路线名称"));
		
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
		this.dataProps.add(new DataProperty("remark", "备注"));
		
		this.dataProps.add(new DataProperty("serviceTypeCode", "服务类型"));
		this.dataProps.add(new DataProperty("sendTime", "发车时间"));
		this.dataProps.add(new DataProperty("receiptAging", "回单时效"));
		this.dataProps.add(new DataProperty("businessTime", "营业时间"));
		this.dataProps.add(new DataProperty("orderAcceptAging", "订单受理时效(天)"));
		this.dataProps.add(new DataProperty("orderDeadLine", "截单时间点"));
		this.dataProps.add(new DataProperty("selfPickedAging", "到站自提时效(天)"));
		this.dataProps.add(new DataProperty("deliveryAging", "到站派送时效(天)"));
		this.dataProps.add(new DataProperty("w2bubbleRatio", "重泡比"));
		
	}										

}
