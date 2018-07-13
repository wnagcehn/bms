package com.jiuyescm.common.utils.upload;

public class TransportPayFeesDataType  extends BaseDataType{
	public TransportPayFeesDataType(){
		this.name = "运输费用导入模板";
		this.dataProps.add(new DataProperty("orderno", "订单号"));
		this.dataProps.add(new DataProperty("waybillno", "运单号"));
		this.dataProps.add(new DataProperty("customername", "商家名称"));
		this.dataProps.add(new DataProperty("forwardername", "承运商"));
		this.dataProps.add(new DataProperty("orgaddress", "始发地"));//市/区
		this.dataProps.add(new DataProperty("targetadress", "到达地"));//市/区
		this.dataProps.add(new DataProperty("productdetails", "商品明细"));
		this.dataProps.add(new DataProperty("boxnum", "订单箱数"));
		this.dataProps.add(new DataProperty("ordernum", "订单件数"));
		this.dataProps.add(new DataProperty("weight", "重量"));
		this.dataProps.add(new DataProperty("volume", "体积"));
		this.dataProps.add(new DataProperty("carmodel", "车型"));
		this.dataProps.add(new DataProperty("quantity", "数量"));
		this.dataProps.add(new DataProperty("distributiontype", "配送类型"));
		this.dataProps.add(new DataProperty("accepttime", "发货日期"));
		this.dataProps.add(new DataProperty("signtime", "签收日期"));
		this.dataProps.add(new DataProperty("backnum", "退货数量"));
		this.dataProps.add(new DataProperty("receivenum", "实收件数"));
		this.dataProps.add(new DataProperty("hasreceipt", "有无回单"));
		this.dataProps.add(new DataProperty("pickupcharge", "提货费"));
		this.dataProps.add(new DataProperty("freight", "运费"));
		this.dataProps.add(new DataProperty("deliverycharges", "送货费"));
		this.dataProps.add(new DataProperty("dischargingcharges", "卸货费"));
		this.dataProps.add(new DataProperty("reverselogisticsfee", "逆向物流费"));
		this.dataProps.add(new DataProperty("compensation", "赔偿费用"));

	}
}
