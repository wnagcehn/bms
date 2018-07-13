package com.jiuyescm.bms.common.enumtype;
import java.util.Map;

import com.google.common.collect.Maps;

public enum RecordLogOperateType {
	INSERT("insert","新增"),
	UPDATE("update","更新"),
	DELETE("delete","删除"),
	CANCEL("cancel","作废"),
	IMPORT("import","导入");
	private String code;
	private String desc;
	private RecordLogOperateType(String code, String desc){
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
	private static Map<String,String> maps =Maps.newLinkedHashMap();
	static{
		maps.put(INSERT.getCode(), INSERT.getDesc());
		maps.put(UPDATE.getCode(), UPDATE.getDesc());
		maps.put(DELETE.getCode(), DELETE.getDesc());
		maps.put(CANCEL.getCode(), CANCEL.getDesc());
		maps.put(IMPORT.getCode(), IMPORT.getDesc());
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
