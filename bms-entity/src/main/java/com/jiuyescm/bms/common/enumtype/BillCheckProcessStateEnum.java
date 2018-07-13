package com.jiuyescm.bms.common.enumtype;

import java.util.HashMap;
import java.util.Map;

/**
 * 处理状态
 * @author zhaofeng
 *
 */
public enum BillCheckProcessStateEnum {
	OPEN("OPEN", "开启"),
	PROCESSING("PROCESSING", "处理中"),
	PART_PROCESS("PART_PROCESS", "部分处理"),
	FINISH("FINISH", "处理完成");
	
	private String code;
	private String desc;
	private BillCheckProcessStateEnum(String code, String desc){
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
		maps.put(OPEN.getCode(), OPEN.getDesc());
		maps.put(PROCESSING.getCode(), PROCESSING.getDesc());
		maps.put(PART_PROCESS.getCode(), PART_PROCESS.getDesc());
		maps.put(FINISH.getCode(), FINISH.getDesc());
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
