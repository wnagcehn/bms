package com.jiuyescm.bms.common.enumtype.mq;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 耗材异步导入任务类型枚举类
 * @author yangss
 */
public enum BmsPackmaterialTaskTypeNewEnum {
	
	IMPORT("BMS.QUEUE.PACKMATERIALIMPORT.TASK", "耗材导入"),
	IMPORT_PACK("BMS.QUEUE.PRODUCT_PACK_STORAGE_IMPORT.TASK", "商品/耗材按托库存");

	private String code;
	private String desc;
	
	private BmsPackmaterialTaskTypeNewEnum(String code, String desc){
		this.setCode(code);
		this.setDesc(desc);
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
	
	private static Map<String,String> maps = new LinkedHashMap<String,String>();
	static{
		maps.put(IMPORT.getCode(), IMPORT.getDesc());
		maps.put(IMPORT_PACK.getCode(), IMPORT_PACK.getDesc());
	}
	public static Map<String,String> getMap(){
		return maps;
	}
	
	public static String getDesc(String code) {
		if (maps.containsKey(code))
		{
			return maps.get(code);
		}
		return null;
	}
}
