package com.jiuyescm.bms.common.enumtype;

import java.util.HashMap;
import java.util.Map;

/**
 * 坏账类型
 * @author zhaofeng
 *
 */
public enum BadBillTypeEnum {
	WH_BACK("WH_BACK", "已退仓"),
	REPAY_DEPT("REPAY_DEPT", "以资抵债"),
	OTHER("OTHER", "其他");

	private String code;
	private String desc;
	private BadBillTypeEnum(String code, String desc){
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
		maps.put(WH_BACK.getCode(), WH_BACK.getDesc());
		maps.put(REPAY_DEPT.getCode(), REPAY_DEPT.getDesc());
		maps.put(OTHER.getCode(), OTHER.getDesc());
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
