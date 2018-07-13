package com.jiuyescm.bms.common.enumtype.mq;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 耗材异步导入任务类型枚举类
 * @author yangss
 */
public enum BmsPackmaterialTaskTypeEnum {
	
	IMPORTBATCH("BMS.QUEUE.PACKMATERIALIMPORTBATCH.TASK", "耗材导入-系统模板"),
	IMPORTWMS("BMS.QUEUE.PACKMATERIALIMPORTWMS.TASK", "耗材导入-wms模板");

	private String code;
	private String desc;
	
	private BmsPackmaterialTaskTypeEnum(String code, String desc){
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
		maps.put(IMPORTBATCH.getCode(), IMPORTBATCH.getDesc());
		maps.put(IMPORTWMS.getCode(), IMPORTWMS.getDesc());
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
