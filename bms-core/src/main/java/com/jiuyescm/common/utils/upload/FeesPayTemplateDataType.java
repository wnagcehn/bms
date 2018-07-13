package com.jiuyescm.common.utils.upload;

public class FeesPayTemplateDataType extends BaseDataType{

	public FeesPayTemplateDataType() {
		this.name = "成本费用导入模板";
		this.dataProps.add(new DataProperty("bizType", "费用类别"));
		this.dataProps.add(new DataProperty("warehouseCode", "仓库"));
		this.dataProps.add(new DataProperty("subjectName", "费用名称"));
		this.dataProps.add(new DataProperty("amount", "金额"));		
	}
}
