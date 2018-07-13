package com.jiuyescm.bms.common.enumtype;

import java.util.LinkedHashMap;
import java.util.Map;

public enum BothTypeEnum {
	Profits(1, "利润"),
	Cost(2, "成本"),
	Revenue(3, "收入");
	
	private Integer code;
	private String desc;
	
	private BothTypeEnum(Integer code, String desc){
		this.code = code;
		this.desc = desc;
	}

	public Integer getCode(){
		return code;
	}

	public void setCode(Integer code){
		this.code = code;
	}
	
	public String getDesc(){
		return desc;
	}

	public void setDesc(String desc){
		this.desc = desc;
	}
	
	private static Map<Integer,String> maps = new LinkedHashMap<Integer,String>();
	static{
		maps.put(Profits.getCode(), Profits.getDesc());
		maps.put(Cost.getCode(), Cost.getDesc());
		maps.put(Revenue.getCode(), Revenue.getDesc());
	}
	
	public static Map<Integer,String> getMap(){
		return maps;
	}
	
	public static String getDesc(Integer code)
	{
		if (maps.containsKey(code))
		{
			return maps.get(code);
		}
		return null;
	}
}
