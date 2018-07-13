package com.jiuyescm.bms.common.enumtype;

import java.util.LinkedHashMap;
import java.util.Map;

public enum BizState {
	All("", "全部"),
	IsNomal("normal", "正常数据"),
	IsLoss("back", "遗漏数据");
	
	private String code;
	private String desc;
	
	private BizState(String code, String desc){
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
		maps.put(All.getCode(), All.getDesc());
		maps.put(IsNomal.getCode(), IsNomal.getDesc());
		maps.put(IsLoss.getCode(), IsLoss.getDesc());
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
