package com.jiuyescm.common.utils.upload;

public class ProjectDeliverDataType extends BaseDataType{

	public ProjectDeliverDataType() {
		this.name = "项目宅配商";
		this.dataProps.add(new DataProperty("projectid", "项目ID"));
		this.dataProps.add(new DataProperty("province", "省"));
		this.dataProps.add(new DataProperty("city", "市"));
		this.dataProps.add(new DataProperty("district", "区（县）"));
		this.dataProps.add(new DataProperty("deliverid", "宅配商ID"));
		this.dataProps.add(new DataProperty("priority", "优先级"));
	}
}