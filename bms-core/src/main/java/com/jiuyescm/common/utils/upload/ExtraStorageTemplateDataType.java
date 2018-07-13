package com.jiuyescm.common.utils.upload;

public class ExtraStorageTemplateDataType extends BaseDataType{

	public ExtraStorageTemplateDataType() {
		this.name = "仓储其他报价模板";
		this.dataProps.add(new DataProperty("subjectId", "费用科目"));
		this.dataProps.add(new DataProperty("unitPrice", "单价"));
		this.dataProps.add(new DataProperty("feeUnitCode", "计费单位"));
		
	}
}    

