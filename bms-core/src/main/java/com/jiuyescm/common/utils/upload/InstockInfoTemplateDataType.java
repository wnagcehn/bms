package com.jiuyescm.common.utils.upload;

public class InstockInfoTemplateDataType extends BaseDataType{

	public InstockInfoTemplateDataType() {
		this.name = "入库单信息模板";
		this.dataProps.add(new DataProperty("customerName", "商家"));
		this.dataProps.add(new DataProperty("warehouseName", "仓库名称"));
		this.dataProps.add(new DataProperty("instockNo", "入库单号"));
		this.dataProps.add(new DataProperty("externalNum", "外部单号"));
		this.dataProps.add(new DataProperty("instockDate", "收货确认时间"));
		this.dataProps.add(new DataProperty("receiver", "收货人"));
		this.dataProps.add(new DataProperty("instockType", "单据类型"));
		this.dataProps.add(new DataProperty("totalQty", "入库件数"));
		this.dataProps.add(new DataProperty("totalBox", "入库箱数"));
		this.dataProps.add(new DataProperty("totalWeight", "入库重量"));	
		this.dataProps.add(new DataProperty("adjustQty", "调整数量"));	
		this.dataProps.add(new DataProperty("adjustBox", "调整箱数"));	
		this.dataProps.add(new DataProperty("adjustWeight", "调整重量"));	
		this.dataProps.add(new DataProperty("instockWorkFee", "入库操作费"));	
		this.dataProps.add(new DataProperty("instockWorkFeeCalStatus", "入库操作费计算状态"));	
		this.dataProps.add(new DataProperty("instockWorkFeeRemark", "入库操作费计算备注"));
		this.dataProps.add(new DataProperty("instockXHFee", "入库卸货费"));	
		this.dataProps.add(new DataProperty("instockXHFeeCalStatus", "入库卸货费计算状态"));	
		this.dataProps.add(new DataProperty("instockXHFeeRemark", "入库卸货费计算备注"));	
	}
}    

