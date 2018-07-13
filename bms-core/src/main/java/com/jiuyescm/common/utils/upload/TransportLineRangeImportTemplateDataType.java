package com.jiuyescm.common.utils.upload;

public class TransportLineRangeImportTemplateDataType extends BaseDataType{

	public TransportLineRangeImportTemplateDataType() {
		this.name = "九耶运输梯度报价模板";
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
		
		this.dataProps.add(new DataProperty("unitPrice", "单价"));
		this.dataProps.add(new DataProperty("extra1", "附加属性1"));
		this.dataProps.add(new DataProperty("extra2", "附加属性2"));
		this.dataProps.add(new DataProperty("extra3", "附加属性3"));
		this.dataProps.add(new DataProperty("extra4", "附加属性4"));
		this.dataProps.add(new DataProperty("extra5", "附加属性5"));
		this.dataProps.add(new DataProperty("remark", "备注"));
	}
}
