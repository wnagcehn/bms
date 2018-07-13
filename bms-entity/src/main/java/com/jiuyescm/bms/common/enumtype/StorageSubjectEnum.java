package com.jiuyescm.bms.common.enumtype;

import java.util.HashMap;
import java.util.Map;

/**
 * 仓储费用科目枚举类
 * 
 * @author yangss
 */
public enum StorageSubjectEnum {

	IN_WAREHOUSE("wh_instock_service","入库验收服务费"),
	PRODUCT_STOR("wh_product_storage","商品存储费"),
	MATERIAL_STOR("wh_material_storage","耗材存储费"),
	ORDER("wh_b2c_work","订单操作费"),
	RETURN_GOODS("wh_return_storage","退货费"),
	RETURN_WAREHOUSE("wh_product_out","退仓费"),
	B2B_OUT("wh_b2b_work","B2B出库费"),
	MATERIAL_USED("wh_material_use","耗材使用费"),
	CHECK("wh_full_check","质检费"),
	OTHER("wh_value_add_subject","增值费");
	
	private String code;
	private String desc;
	
	private StorageSubjectEnum(String code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	private static Map<String,String> maps = new HashMap<String,String>();
	static{
		maps.put(IN_WAREHOUSE.getCode(), IN_WAREHOUSE.getDesc());
		maps.put(PRODUCT_STOR.getCode(), PRODUCT_STOR.getDesc());
		maps.put(MATERIAL_STOR.getCode(), MATERIAL_STOR.getDesc());
		maps.put(ORDER.getCode(), ORDER.getDesc());
		maps.put(RETURN_GOODS.getCode(), RETURN_GOODS.getDesc());
		maps.put(RETURN_WAREHOUSE.getCode(), RETURN_WAREHOUSE.getDesc());
		maps.put(B2B_OUT.getCode(), B2B_OUT.getDesc());
		maps.put(MATERIAL_USED.getCode(), MATERIAL_USED.getDesc());
		maps.put(CHECK.getCode(), CHECK.getDesc());
		maps.put(OTHER.getCode(), OTHER.getDesc());
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
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
}
