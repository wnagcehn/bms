package com.jiuyescm.bms.common.enumtype;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 文件导出任务状态枚举
 * @author yangss
 */
public enum FileTaskStateEnum {
	
	BEGIN("0", "已创建"),
	INPROCESS("1", "处理中"),
	SUCCESS("2", "成功"),
	FAIL("3", "失败");
	
	private String code;
	private String desc;
	
	private FileTaskStateEnum(String code, String desc){
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
		maps.put(BEGIN.getCode(), BEGIN.getDesc());
		maps.put(INPROCESS.getCode(), INPROCESS.getDesc());
		maps.put(SUCCESS.getCode(), SUCCESS.getDesc());
		maps.put(FAIL.getCode(), FAIL.getDesc());
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
