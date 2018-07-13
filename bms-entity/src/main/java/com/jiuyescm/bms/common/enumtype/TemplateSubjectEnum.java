package com.jiuyescm.bms.common.enumtype;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 模版科目枚举类
 * @author yangss
 *
 */
public enum TemplateSubjectEnum {
	
	TRANS_FEES("TRANSPORT_FEE", "运输费用模版"),
	TRANS_ADD("TRANSPORT_VALUEADDED", "运输增值费用模版");
	
	private String code;
	private String desc;
	
	private TemplateSubjectEnum(String code, String desc){
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
		maps.put(TRANS_FEES.getCode(), TRANS_FEES.getDesc());
		maps.put(TRANS_ADD.getCode(), TRANS_ADD.getDesc());
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
