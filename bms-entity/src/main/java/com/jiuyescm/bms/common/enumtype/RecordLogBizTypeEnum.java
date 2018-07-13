package com.jiuyescm.bms.common.enumtype;

import java.util.Map;

import com.google.common.collect.Maps;

public enum RecordLogBizTypeEnum {
	
	CONTACT("contact","合同"),
	PRICE("price","报价");
	private String code;
	private String desc;
	private RecordLogBizTypeEnum(String code, String desc){
		this.code = code;
		this.desc = desc;
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
	private static Map<String,String> maps =Maps.newLinkedHashMap();
	static{
		maps.put(CONTACT.getCode(), CONTACT.getDesc());
		maps.put(PRICE.getCode(),PRICE.getDesc());
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
