package com.jiuyescm.common.utils.upload;

public class StorageFeeTemplateDataType extends BaseDataType{

	public StorageFeeTemplateDataType() {
		this.name = "增值费增加模板";
		this.dataProps.add(new DataProperty("customerName", "商家"));
		this.dataProps.add(new DataProperty("unitPrice", "单价"));
		this.dataProps.add(new DataProperty("warehouseName", "仓库"));
		this.dataProps.add(new DataProperty("cost", "金额"));
		this.dataProps.add(new DataProperty("orderType", "单据类型"));
		this.dataProps.add(new DataProperty("orderNo", "单据编号"));
		this.dataProps.add(new DataProperty("otherSubjectCode", "费用名称"));
		this.dataProps.add(new DataProperty("quantity", "数量"));
		this.dataProps.add(new DataProperty("operateTime", "产生日期"));
		
	}
}    