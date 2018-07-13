package com.jiuyescm.bms.common.enumtype;

import java.util.HashMap;
import java.util.Map;

/**
 * 是否轻货枚举
 * @author yangss
 */
public enum CargoIsLightEnum {

	YES("Y", "是"), NO("N", "否");

	private String code;
	private String desc;

	private CargoIsLightEnum(String code, String desc) {
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

	private static Map<String,String> maps = new HashMap<String,String>();
	static{
		maps.put(YES.getCode(), YES.getDesc());
		maps.put(NO.getCode(), NO.getDesc());
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
