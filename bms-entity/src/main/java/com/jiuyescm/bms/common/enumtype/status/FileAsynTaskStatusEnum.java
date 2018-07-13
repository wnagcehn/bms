package com.jiuyescm.bms.common.enumtype.status;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 文件异步处理任务状态枚举类
 * @author yangss
 */
public enum FileAsynTaskStatusEnum {
	WAIT("WAIT", "等待"),
	PROCESS("PROCESS", "处理"),
	SUCCESS("SUCCESS", "成功"),
	EXCEPTION("EXCEPTION", "异常"),
	CANCEL("CANCEL", "取消"),
	FAIL("FAIL", "失败");

	private String code;
	private String desc;
	
	private FileAsynTaskStatusEnum(String code, String desc){
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
	
	private static Map<String,String> maps = new LinkedHashMap<String,String>();
	static{
		maps.put(WAIT.getCode(), WAIT.getDesc());
		maps.put(PROCESS.getCode(), PROCESS.getDesc());
		maps.put(EXCEPTION.getCode(), EXCEPTION.getDesc());
		maps.put(CANCEL.getCode(), CANCEL.getDesc());
		maps.put(FAIL.getCode(), FAIL.getDesc());
		maps.put(SUCCESS.getCode(), SUCCESS.getDesc());
	}
	public static Map<String,String> getMap(){
		return maps;
	}
	
	public static String getDesc(String code) {
		if (maps.containsKey(code))
		{
			return maps.get(code);
		}
		return null;
	}
}
