package com.jiuyescm.common.utils.upload;

public class InnertransItemsDataType extends BaseDataType{

	public InnertransItemsDataType() {
		this.name = "转储明细";
		
		this.dataProps.add(new DataProperty("lineno", "行号"));
		this.dataProps.add(new DataProperty("fcustomerid", "转出商家ID"));
		this.dataProps.add(new DataProperty("tcustomerid", "转入商家ID"));
		this.dataProps.add(new DataProperty("fproductid", "转出商品ID"));
		this.dataProps.add(new DataProperty("tproductid", "转入商品ID"));
		this.dataProps.add(new DataProperty("fstockid", "转出库存类型编码"));
		this.dataProps.add(new DataProperty("tstockid", "转入库存类型编码"));
		this.dataProps.add(new DataProperty("fbatchcode", "转出批次编码"));
		this.dataProps.add(new DataProperty("tbatchcode", "转入批次编码"));
		this.dataProps.add(new DataProperty("qty", "转储数量"));
		this.dataProps.add(new DataProperty("fproductDate", "转出商品生产日期"));
		this.dataProps.add(new DataProperty("tproductDate", "转入商品生产日期"));
		this.dataProps.add(new DataProperty("fexpireDate", "转出商品过期日期"));
		this.dataProps.add(new DataProperty("texpireDate", "转入商品过期日期"));
		this.dataProps.add(new DataProperty("inTime", "转出商品入库时间"));
		
	}
}
