package com.jiuyescm.bms.common.enumtype;

import java.util.HashMap;
import java.util.Map;

/**
 * 运输运单类型枚举类
 * @author yangss
 */
public enum TransportWayBillTypeEnum {

	TC("TC","同城专列"),
	CJ("CJ","城际专列"),
	DSZL("DSZL","电商专列"),
	HXD("HXD","航鲜达");
	
	private String code;
	private String desc;
	
	private TransportWayBillTypeEnum(String code, String desc) {
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
		maps.put(TC.getCode(), TC.getDesc());
		maps.put(CJ.getCode(), CJ.getDesc());
		maps.put(DSZL.getCode(), DSZL.getDesc());
		maps.put(HXD.getCode(), HXD.getDesc());
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
