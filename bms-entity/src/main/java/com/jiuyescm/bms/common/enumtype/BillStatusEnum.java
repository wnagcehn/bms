package com.jiuyescm.bms.common.enumtype;

import java.util.HashMap;
import java.util.Map;

/**
 * 账单状态枚举类
 * @author yangss
 */
public enum BillStatusEnum {

	UNCONFIRMED("UNCONFIRMED", "未确认"), 
	CONFIRMED("CONFIRMED", "已确认"),
	PARTINVOICED("PARTINVOICED", "部分开票"),
	INVOICED("INVOICED", "已开票"),
	RECEIPTED("RECEIPTED", "已收款"),
	SETTLED("SETTLED", "已结账");

	private String code;
	private String desc;

	private BillStatusEnum(String code, String desc) {
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
		maps.put(UNCONFIRMED.getCode(), UNCONFIRMED.getDesc());
		maps.put(CONFIRMED.getCode(), CONFIRMED.getDesc());
		maps.put(PARTINVOICED.getCode(), PARTINVOICED.getDesc());
		maps.put(INVOICED.getCode(), INVOICED.getDesc());
		maps.put(RECEIPTED.getCode(), RECEIPTED.getDesc());
		maps.put(SETTLED.getCode(), SETTLED.getDesc());
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
