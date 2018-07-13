package com.jiuyescm.bms.common.enumtype;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 接口类型枚举类
 * @author yangss
 */
public enum InterfaceTypeEnum {
	
	TRY("TRY", "试算"),
	PUT("PUT","推送");
	
	private String code;
	private String desc;
	
	private InterfaceTypeEnum(String code, String desc){
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
		maps.put(TRY.getCode(), TRY.getDesc());
		maps.put(PUT.getCode(), PUT.getDesc());
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
