package com.jiuyescm.bms.common.enumtype;

import java.util.HashMap;
import java.util.Map;
/**
 * 账单明细状态
 * @author Wuliangfeng
 *
 */
public enum BillFeesSubjectStatusEnum {
	DELETE("DELETE","已删除"),
	GENERATED("GENERATED","已生成"),
	UPDATE("UPDATE","已更新"),
	CONFIRM("CONFIRM","已确认");
	
	private String code;
	private String desc;
	private BillFeesSubjectStatusEnum(String code, String desc){
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
		maps.put(DELETE.getCode(), DELETE.getDesc());
		maps.put(GENERATED.getCode(), GENERATED.getDesc());
		maps.put(UPDATE.getCode(), UPDATE.getDesc());
		maps.put(CONFIRM.getCode(), CONFIRM.getDesc());
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
