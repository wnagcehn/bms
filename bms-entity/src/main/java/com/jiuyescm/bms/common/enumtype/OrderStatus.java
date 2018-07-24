package com.jiuyescm.bms.common.enumtype;

import java.util.LinkedHashMap;
import java.util.Map;

public enum OrderStatus {
	All("", "全部"),
	IsNomal("NORMAL", "正常订单"),
	IsClose("CLOSE", "订单取消");
	
	private String code;
	private String desc;
	
	private OrderStatus(String code, String desc){
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
		maps.put(IsClose.getCode(), IsClose.getDesc());
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
