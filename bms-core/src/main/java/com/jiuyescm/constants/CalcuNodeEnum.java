package com.jiuyescm.constants;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 计算状态枚举
 * @author caojianwei
 *
 */
public enum CalcuNodeEnum {
	
	BIZ("BIZINFO", "业务数据"),
	CHARGE("CHARGEINFO", "计费数据"),
	CONTRACT("CONTRACT", "合同信息"),
	QUOTE("QUOTE", "报价信息"),
	CALCU("CALCUINFO", "计费规则");
	
	
	private String code;
	private String desc;
	
	private CalcuNodeEnum(String code, String desc){
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
		maps.put(BIZ.getCode(), BIZ.getDesc());
		maps.put(CHARGE.getCode(), CHARGE.getDesc());
		maps.put(CONTRACT.getCode(), CONTRACT.getDesc());
		maps.put(QUOTE.getCode(), QUOTE.getDesc());
		maps.put(CALCU.getCode(),CALCU.getDesc());
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
