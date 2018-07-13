package com.jiuyescm.common.utils;
public class StaticInitProperties {
	
	static PropertiesHandle handle = new PropertiesHandle("/application.properties");
	private StaticInitProperties(){
		//禁止实例化
	}
	
	public static String getParamValue(String paramName)
	{
		String value = handle.getValue(paramName);
		return value;
	}
	
	public static String getParamValue(String paramName,String defaultValue)
	{
		String value = handle.getValue(paramName);
		return "".equals(value)?defaultValue:value;
	}
	
}