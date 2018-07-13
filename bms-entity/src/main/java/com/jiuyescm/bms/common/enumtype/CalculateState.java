package com.jiuyescm.bms.common.enumtype;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 计算状态枚举
 * @author caojianwei
 *
 */
public enum CalculateState {
	
	All("", "全部"),
	Begin("0", "未计算"),
	Finish("1", "计算成功"),
	Sys_Error("2", "系统错误"),
	Contract_Miss("3", "合同不存在"),
	Quote_Miss("4", "报价缺失"),
	No_Exe("5","不计算"),
	Other("10", "其他"),
	Quote_More("6", "多个报价"),
	Retry("99","待重算");
	
	private String code;
	private String desc;
	
	private CalculateState(String code, String desc){
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
		maps.put(All.getCode(), All.getDesc());
		maps.put(Begin.getCode(), Begin.getDesc());
		maps.put(Finish.getCode(), Finish.getDesc());
		maps.put(Sys_Error.getCode(), Sys_Error.getDesc());
		maps.put(Contract_Miss.getCode(),Contract_Miss.getDesc());
		maps.put(Quote_Miss.getCode(),Quote_Miss.getDesc());
		maps.put(No_Exe.getCode(),No_Exe.getDesc());
		maps.put(Other.getCode(), Other.getDesc());
		maps.put(Retry.getCode(), Retry.getDesc());
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
