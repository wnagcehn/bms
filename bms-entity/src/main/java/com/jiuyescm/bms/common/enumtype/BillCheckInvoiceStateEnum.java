package com.jiuyescm.bms.common.enumtype;

import java.util.HashMap;
import java.util.Map;

/**
 * 开票状态
 * @author zhaofeng
 *
 */
public enum BillCheckInvoiceStateEnum {
	NO_INVOICE("NO_INVOICE", "未开票"),
	PART_INVOICE("PART_INVOICE", "部分开票"),
	INVOICED("INVOICED", "已开票"),
	UNNEED_INVOICE("UNNEED_INVOICE", "不需要开票");

	private String code;
	private String desc;
	private BillCheckInvoiceStateEnum(String code, String desc){
		this.setCode(code);
		this.setDesc(desc);
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
		maps.put(NO_INVOICE.getCode(), NO_INVOICE.getDesc());
		maps.put(PART_INVOICE.getCode(), PART_INVOICE.getDesc());
		maps.put(INVOICED.getCode(), INVOICED.getDesc());
		maps.put(UNNEED_INVOICE.getCode(), UNNEED_INVOICE.getDesc());
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
