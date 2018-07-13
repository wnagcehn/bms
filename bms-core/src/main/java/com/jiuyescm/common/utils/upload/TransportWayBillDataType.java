package com.jiuyescm.common.utils.upload;

public class TransportWayBillDataType extends BaseDataType{

	public TransportWayBillDataType() {
		this.name = "九曳干线运单导入模板";
		this.dataProps.add(new DataProperty("customerName", "商家全称"));
		this.dataProps.add(new DataProperty("bizTypeCode", "业务类型"));
		this.dataProps.add(new DataProperty("orderNo", "订单号"));
		this.dataProps.add(new DataProperty("waybillNo", "运单号"));
		this.dataProps.add(new DataProperty("ganxianNo", "路单号"));
		this.dataProps.add(new DataProperty("warehouseName", "始发仓库"));
		this.dataProps.add(new DataProperty("sendProvinceId", "始发省份"));
		this.dataProps.add(new DataProperty("sendCityId", "始发城市"));
		this.dataProps.add(new DataProperty("sendDistrictId", "始发区"));
		this.dataProps.add(new DataProperty("receiverProvinceId", "目的省份"));
		this.dataProps.add(new DataProperty("receiverCityId", "目的城市"));
		this.dataProps.add(new DataProperty("receiverDistrictId", "目的区"));
		this.dataProps.add(new DataProperty("startStation", "始发站点"));
		this.dataProps.add(new DataProperty("endStation", "目的站点"));
		this.dataProps.add(new DataProperty("isLight", "是否泡货"));
		this.dataProps.add(new DataProperty("totalWeight", "重量"));
		this.dataProps.add(new DataProperty("totalVolume", "体积"));
		this.dataProps.add(new DataProperty("carModel", "车型"));
		this.dataProps.add(new DataProperty("sendTime", "发货日期"));
		this.dataProps.add(new DataProperty("signTime", "收货日期"));
		this.dataProps.add(new DataProperty("receiver", "收件人"));
		this.dataProps.add(new DataProperty("receiverTelephone", "收件人电话"));
		this.dataProps.add(new DataProperty("remark", "备注"));
	}										
}
