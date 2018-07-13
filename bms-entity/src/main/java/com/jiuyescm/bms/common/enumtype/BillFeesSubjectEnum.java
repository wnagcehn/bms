package com.jiuyescm.bms.common.enumtype;

import java.util.HashMap;
import java.util.Map;

/**
 * 账单费用科目枚举
 * @author yangss
 *
 */
public enum BillFeesSubjectEnum {

	STORAGE("STORAGE", "仓储"),
	DISPATCH("DISPATCH", "配送"),
	TRANSPORT("TRANSPORT", "运输");

	private String code;
	private String desc;

	private BillFeesSubjectEnum(String code, String desc) {
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
		maps.put(STORAGE.getCode(), STORAGE.getDesc());
		maps.put(DISPATCH.getCode(), DISPATCH.getDesc());
		maps.put(TRANSPORT.getCode(), TRANSPORT.getDesc());
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
