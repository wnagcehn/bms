package com.jiuyescm.common.utils.upload;

public class BillPayDispatchDistinctDataType extends BaseDataType{
	public BillPayDispatchDistinctDataType() {
		this.name = "宅配对账单";
		this.dataProps.add(new DataProperty("waybillNo", "运单号"));
		this.dataProps.add(new DataProperty("totalWeight", "总重量"));
		this.dataProps.add(new DataProperty("headWeight", "首重重量"));
		this.dataProps.add(new DataProperty("headPrice", "首重价格"));
		this.dataProps.add(new DataProperty("continuedWeight", "续重重量"));
		this.dataProps.add(new DataProperty("continuedPrice", "续重价格"));
		this.dataProps.add(new DataProperty("weightLimit", "重量界限"));
		this.dataProps.add(new DataProperty("unitPrice", "单价"));
		this.dataProps.add(new DataProperty("amount", "金额"));
	}
}
