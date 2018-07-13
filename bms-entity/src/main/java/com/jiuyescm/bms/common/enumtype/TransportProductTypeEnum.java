package com.jiuyescm.bms.common.enumtype;

import java.util.HashMap;
import java.util.Map;

/**
 * 运输运单类型枚举类
 * @author yangss
 */
public enum TransportProductTypeEnum {

	TCLD("TCLD","同城零担"),
	TCZC("TCZC","同城整车"),
	CJLD("CJLD","城际零担"),
	CJZC("CJZC","城际整车"),

	TCZC_DSZL("TCZC_DSZL","电商专列"),
	TCZC_HXD("TCZC_HXD","航鲜达"),
	TCZC_TCZC("TCZC_TCZC","同城整车");
	
	private String code;
	private String desc;
	
	private TransportProductTypeEnum(String code, String desc) {
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
		maps.put(TCLD.getCode(), TCLD.getDesc());
		maps.put(TCZC.getCode(), TCZC.getDesc());
		maps.put(CJLD.getCode(), CJLD.getDesc());
		maps.put(CJZC.getCode(), CJZC.getDesc());
		maps.put(TCZC_DSZL.getCode(), TCZC_DSZL.getDesc());
		maps.put(TCZC_HXD.getCode(), TCZC_HXD.getDesc());
		maps.put(TCZC_TCZC.getCode(), TCZC_TCZC.getDesc());
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
