package com.jiuyescm.common.utils.upload;

public class ProjectCarrierDataType extends BaseDataType{

	public ProjectCarrierDataType() {
		this.name = "项目物流商";
		this.dataProps.add(new DataProperty("projectid", "项目ID"));
		this.dataProps.add(new DataProperty("province", "省"));
		this.dataProps.add(new DataProperty("city", "市"));
		this.dataProps.add(new DataProperty("district", "区（县）"));
		this.dataProps.add(new DataProperty("oms_warehouseid", "仓库ID"));
		this.dataProps.add(new DataProperty("tempraturetype", "温度类别"));
		this.dataProps.add(new DataProperty("deliverytype", "发货服务类型"));
		this.dataProps.add(new DataProperty("carrierid", "物流商ID"));
		this.dataProps.add(new DataProperty("priority", "优先级"));
	}
}
