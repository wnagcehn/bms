package com.jiuyescm.common.utils.upload;

public class MaterialTemplateDataType extends BaseDataType{

	public MaterialTemplateDataType() {
		this.name = "九耶耗材报价模板";
		this.dataProps.add(new DataProperty("warehouseId", "仓库名称"));
		this.dataProps.add(new DataProperty("materialType", "耗材类型"));
		this.dataProps.add(new DataProperty("specName", "规格名称"));
		this.dataProps.add(new DataProperty("materialCode", "耗材编码"));
		this.dataProps.add(new DataProperty("outsideDiameter", "外径/尺寸"));
		this.dataProps.add(new DataProperty("innerDiameter", "内径"));
		this.dataProps.add(new DataProperty("wallThickness", "壁厚"));
		this.dataProps.add(new DataProperty("unitPrice", "单价"));
		this.dataProps.add(new DataProperty("remark", "备注"));			
	}
}    

