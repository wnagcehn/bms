package com.jiuyescm.common.utils.upload;

public class PalletDataType extends BaseDataType{
	
	public PalletDataType(){
		this.name = "托数导出";
		this.dataProps.add(new DataProperty("curTime", "库存日期"));
		this.dataProps.add(new DataProperty("warehouseName", "仓库"));
		this.dataProps.add(new DataProperty("customerName", "商家名称"));
		this.dataProps.add(new DataProperty("temperatureTypeCode", "温度类型"));
		this.dataProps.add(new DataProperty("subjectCode", "费用科目"));
		this.dataProps.add(new DataProperty("bizType", "托数类型"));
		this.dataProps.add(new DataProperty("chargeSource", "计费来源"));
		this.dataProps.add(new DataProperty("sysPalletNum", "系统托数"));
		this.dataProps.add(new DataProperty("palletNum", "导入托数"));
		this.dataProps.add(new DataProperty("diffPalletNum", "差异托数"));
		this.dataProps.add(new DataProperty("adjustPalletNum", "调整托数"));
		this.dataProps.add(new DataProperty("cost", "金额"));
		this.dataProps.add(new DataProperty("isCalculated", "计算状态"));
		this.dataProps.add(new DataProperty("remark", "备注"));
		this.dataProps.add(new DataProperty("feesNo", "费用编码"));
		this.dataProps.add(new DataProperty("creator", "创建者"));
		this.dataProps.add(new DataProperty("createTime", "创建时间"));
	}
}
