package com.jiuyescm.common.utils.upload;

public class TransportReceiveFeesDataType extends BaseDataType{

	public TransportReceiveFeesDataType() {
		
		this.name = "运输应收费用";
		
		this.dataProps.add(new DataProperty("operationtime", "操作时间"));	
		this.dataProps.add(new DataProperty("warehouseName", "仓库"));	
		this.dataProps.add(new DataProperty("customerName", "商家"));
		this.dataProps.add(new DataProperty("subjectCode", "产品类型"));	
		this.dataProps.add(new DataProperty("orderno", "订单号"));	
		this.dataProps.add(new DataProperty("waybillNo", "运单号"));
		this.dataProps.add(new DataProperty("originProvince", "始发省份"));	
		this.dataProps.add(new DataProperty("originatingcity", "始发市"));	
		this.dataProps.add(new DataProperty("originatingdistrict", "始发区"));	
		this.dataProps.add(new DataProperty("targetwarehouse", "目的仓"));
		this.dataProps.add(new DataProperty("targetProvince", "目的省份"));	
		this.dataProps.add(new DataProperty("targetcity", "目的市"));	
		this.dataProps.add(new DataProperty("targetdistrict", "目的区"));	
		this.dataProps.add(new DataProperty("temperaturetype", "温度类型"));	
		this.dataProps.add(new DataProperty("category", "品类"));	
		this.dataProps.add(new DataProperty("weight", "重量"));	
		this.dataProps.add(new DataProperty("volume", "体积"));	
		this.dataProps.add(new DataProperty("carmodel", "车型"));	
		this.dataProps.add(new DataProperty("islight", "是否轻货"));	
		this.dataProps.add(new DataProperty("unitprice", "单价"));
		this.dataProps.add(new DataProperty("quantity", "数量"));	
		this.dataProps.add(new DataProperty("totleprice", "金额"));	
		this.dataProps.add(new DataProperty("accepttime", "揽收时间"));	
		this.dataProps.add(new DataProperty("signtime", "签收时间"));


	}										


}
