package com.jiuyescm.bms.common.enumtype;

import java.util.HashMap;
import java.util.Map;

public enum BillFeesSubjectAbormalEnum {
	Abnormal_Stroage("Abnormal_Storage","理赔"),
	Abnormal_DisChange("Abnormal_DisChange","改地址和退件费"),
	Abnormal_Dispatch("Abnormal_Dispatch","理赔"),
	Abnormal_Transport("Abnormal_Transport","理赔");
	private String code;
	private String desc;
	private BillFeesSubjectAbormalEnum(String code, String desc){
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
		maps.put(Abnormal_Stroage.getCode(), Abnormal_Stroage.getDesc());
		maps.put(Abnormal_DisChange.getCode(), Abnormal_DisChange.getDesc());
		maps.put(Abnormal_Dispatch.getCode(), Abnormal_Dispatch.getDesc());
		maps.put(Abnormal_Transport.getCode(), Abnormal_Transport.getDesc());
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
