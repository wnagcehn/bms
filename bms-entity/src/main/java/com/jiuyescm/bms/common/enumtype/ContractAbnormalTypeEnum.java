package com.jiuyescm.bms.common.enumtype;

import java.util.HashMap;
import java.util.Map;

/**
 * 合同异常类型枚举类
 * @author yangss
 */
public enum ContractAbnormalTypeEnum {
	
	NOTPRESERVE("NOTPRESERVE", "合同未维护"),
	WILLBEEXPIRE("WILLBEEXPIRE", "合同即将到期"), 
	NOTCONFIGPRICE("NOTCONFIGPRICE","合同未配置服务"),
	NOTCONFIGRULE("NOTCONFIGRULE", "合同报价未配置服务");

	private String code;
	private String desc;

	private ContractAbnormalTypeEnum(String code, String desc) {
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

	private static Map<String,String> maps = new HashMap<String,String>();
	static{
		maps.put(NOTPRESERVE.getCode(), NOTPRESERVE.getDesc());
		maps.put(WILLBEEXPIRE.getCode(), WILLBEEXPIRE.getDesc());
		maps.put(NOTCONFIGPRICE.getCode(), NOTCONFIGPRICE.getDesc());
		maps.put(NOTCONFIGRULE.getCode(), NOTCONFIGRULE.getDesc());
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
