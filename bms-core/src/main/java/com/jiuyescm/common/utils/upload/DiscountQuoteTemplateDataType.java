package com.jiuyescm.common.utils.upload;

public class DiscountQuoteTemplateDataType extends BaseDataType{
	
	public DiscountQuoteTemplateDataType(){
		this.name = "折扣报价模板";
		this.dataProps.add(new DataProperty("startTime", "开始时间"));
		this.dataProps.add(new DataProperty("endTime", "结束时间"));
		this.dataProps.add(new DataProperty("downLimit", "下限"));
		this.dataProps.add(new DataProperty("upLimit", "上限"));
		this.dataProps.add(new DataProperty("firstPrice", "折扣首价"));
		this.dataProps.add(new DataProperty("firstPriceRate", "首价折扣率(%)"));
		this.dataProps.add(new DataProperty("continuePrice", "折扣续价"));
		this.dataProps.add(new DataProperty("continuePirceRate", "续价折扣率(%)"));
		this.dataProps.add(new DataProperty("unitPrice", "折扣一口价"));
		this.dataProps.add(new DataProperty("unitPriceRate", "一口价折扣率(%)"));
	}
}
