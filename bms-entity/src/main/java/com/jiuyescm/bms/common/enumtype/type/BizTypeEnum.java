package com.jiuyescm.bms.common.enumtype.type;

import java.util.HashMap;
import java.util.Map;

/**
 * 业务数据类型枚举类
 * @author yangss
 */
public enum BizTypeEnum {
	PALLET("PALLET", "存储托数"),
	MATERIAL("MATERIAL", "耗材");

	private String code;
	private String desc;
	
	private BizTypeEnum(String code, String desc){
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
	
	private static Map<String,String> maps = new HashMap<String,String>();
	static{
		maps.put(PALLET.getCode(), PALLET.getDesc());
		maps.put(MATERIAL.getCode(), MATERIAL.getDesc());
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
