package com.jiuyescm.bms.common.enumtype;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 模版类型枚举
 * @author yangss
 */
public enum TemplateTypeEnum {
	
	STANDARD("S", "标准模版"),
	COMMON("C","常规模版");
	
	private String code;
	private String desc;
	
	private TemplateTypeEnum(String code, String desc){
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
		maps.put(STANDARD.getCode(), STANDARD.getDesc());
		maps.put(COMMON.getCode(), COMMON.getDesc());
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
