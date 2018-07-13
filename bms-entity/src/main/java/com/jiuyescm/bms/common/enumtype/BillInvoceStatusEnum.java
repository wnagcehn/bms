package com.jiuyescm.bms.common.enumtype;

import java.util.HashMap;
import java.util.Map;

/**
 * 发票状态
 * @author yangss
 *
 */
public enum BillInvoceStatusEnum {
	
	UNSETTLED("UNSETTLED","未收款"),
	SETTLED("SETTLED","已收款");
	
	private String code;
	private String desc;
	private BillInvoceStatusEnum(String code, String desc){
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
		maps.put(UNSETTLED.getCode(), UNSETTLED.getDesc());
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
