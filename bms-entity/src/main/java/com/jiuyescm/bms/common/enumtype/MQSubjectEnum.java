package com.jiuyescm.bms.common.enumtype;

import java.util.LinkedHashMap;
import java.util.Map;

public enum MQSubjectEnum {
	
	RValueadd("wh_value_add_subject", "BMS.QUEUE.CALCU.VALUE.VALUEADD"),
	RDispatch("de_delivery_amount", "BMS.QUEUE.CALCU.WAYBILL.DISPATCH"),	
	RInstockwork("wh_instock_work", "BMS.QUEUE.CALCU.INSTOCK.INSTOCKWORK"),
	RB2chandwork("wh_b2c_handwork", "BMS.QUEUE.CALCU.INSTOCK.B2CHANDWORK"),
	RMaterialuse("wh_material_use", "BMS.QUEUE.CALCU.MATERIAL.MATERIALUSE"),
	RB2cwork("wh_b2c_work", "BMS.QUEUE.CALCU.OUTSTOCK.B2CWORK"),
	RB2bwork("wh_b2b_work", "BMS.QUEUE.CALCU.OUTSTOCK.B2BWORK"),
	RB2bhandwork("wh_b2b_handwork", "BMS.QUEUE.CALCU.OUTSTOCK.B2BHANDWORK"),
	RDisposal("wh_disposal", "BMS.QUEUE.CALCU.PALLET.DISPOSAL"),
	RProductStorage("wh_product_storage", "BMS.QUEUE.CALCU.PALLET.PRODUCTSTORAGE"),
	RMaterialStorage("wh_material_storage", "BMS.QUEUE.CALCU.PALLET.MATERIALSTORAGE"),
	ROutStockPallet("outstock_pallet_vm", "BMS.QUEUE.CALCU.PALLET.OUTSTOCKPALLET"),
	RProductItemStorage("wh_product_storage_item", "BMS.QUEUE.CALCU.PRODUCT.PRODUCTSTORAGE"),
	RDispatchPackage("wh_stand_material_use", "BMS.QUEUE.CALCU.MATERIAL.DISPATCHPACKAGE");
	
	private String code;
	private String desc;
	
	private MQSubjectEnum(String code, String desc){
		this.code = code;
		this.desc = desc;
	}
	
	public String getCode(){
		return code;
	}

	public void setCode(String code){
		this.code = code;
	}
	
	public String getDesc(){
		return desc;
	}

	public void setDesc(String desc){
		this.desc = desc;
	}
	
	private static Map<String,String> maps = new LinkedHashMap<String,String>();
	static{
		maps.put(RValueadd.getCode(), RValueadd.getDesc());
		maps.put(RDispatch.getCode(), RDispatch.getDesc());
		maps.put(RInstockwork.getCode(), RInstockwork.getDesc());
		maps.put(RB2chandwork.getCode(), RB2chandwork.getDesc());
		maps.put(RMaterialuse.getCode(),RMaterialuse.getDesc());
		maps.put(RB2cwork.getCode(),RB2cwork.getDesc());
		maps.put(RB2bwork.getCode(),RB2bwork.getDesc());
		maps.put(RB2bhandwork.getCode(), RB2bhandwork.getDesc());
		maps.put(RDisposal.getCode(), RDisposal.getDesc());
		
		
		maps.put(RProductStorage.getCode(),RProductStorage.getDesc());
		maps.put(RMaterialStorage.getCode(), RMaterialStorage.getDesc());
		maps.put(ROutStockPallet.getCode(), ROutStockPallet.getDesc());
		maps.put(RProductItemStorage.getCode(), RProductItemStorage.getDesc());
	}
	
	public static Map<String,String> getMap(){
		return maps;
	}
	
	public static String getDesc(String code)
	{
		if (maps.containsKey(code))
		{
			return maps.get(code);
		}
		return null;
	}
}
