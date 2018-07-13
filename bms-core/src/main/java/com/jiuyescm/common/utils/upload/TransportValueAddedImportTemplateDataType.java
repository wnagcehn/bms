package com.jiuyescm.common.utils.upload;

public class TransportValueAddedImportTemplateDataType extends BaseDataType{

	public TransportValueAddedImportTemplateDataType() {
		
		this.name = "九耶运输增值服务报价模板";

		this.dataProps.add(new DataProperty("carModelName", "车型"));
		this.dataProps.add(new DataProperty("weightLimit", "重量界限"));
		this.dataProps.add(new DataProperty("feeUnitName", "计费单位"));
		this.dataProps.add(new DataProperty("unitPrice", "单价"));
		this.dataProps.add(new DataProperty("subjectName", "费用科目"));
		this.dataProps.add(new DataProperty("extra1", "附加属性1"));
		this.dataProps.add(new DataProperty("extra2", "附加属性2"));
		this.dataProps.add(new DataProperty("extra3", "附加属性3"));
		this.dataProps.add(new DataProperty("extra4", "附加属性4"));
		this.dataProps.add(new DataProperty("extra5", "附加属性5"));
		this.dataProps.add(new DataProperty("remark", "备注"));
	}										


}
