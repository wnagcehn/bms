package com.jiuyescm.bms.common.enumtype;

import java.util.HashMap;
import java.util.Map;

/**
 * 对账状态
 * @author zhaofeng
 *
 */
public enum BillCheckStateEnum {
	
	BEGIN("BEGIN", "初始"),
	WH_FOLLOW("WH_FOLLOW", "发仓库"),
	CUST_FOLLOW("CUST_FOLLOW", "发客户"),
	CUST_NO_FEEDBACK("CUST_NO_FEEDBACK", "客户未反馈"),
	CUST_CHECKING("CUST_CHECKING", "客户正在核对"),
	QUOTATION("QUOTATION", "报价问题"),
	WH_PROBLEM("WH_PROBLEM", "仓库问题"),
	PROJECT_PROBLEM("PROJECT_PROBLEM", "项目问题"),
	TS_PROBLEM("TS_PROBLEM","干线问题"),
	CONFIRMED("CONFIRMED", "已确认"),
	EXCEPTION("EXCEPTION","异常");
	private String code;
	private String desc;
	private BillCheckStateEnum(String code, String desc){
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
		maps.put(BEGIN.getCode(), BEGIN.getDesc());
		maps.put(WH_FOLLOW.getCode(), WH_FOLLOW.getDesc());
		maps.put(CUST_FOLLOW.getCode(), CUST_FOLLOW.getDesc());
		maps.put(CUST_NO_FEEDBACK.getCode(), CUST_NO_FEEDBACK.getDesc());
		maps.put(CUST_CHECKING.getCode(), CUST_CHECKING.getDesc());
		maps.put(QUOTATION.getCode(), QUOTATION.getDesc());
		maps.put(WH_PROBLEM.getCode(), WH_PROBLEM.getDesc());
		maps.put(PROJECT_PROBLEM.getCode(), PROJECT_PROBLEM.getDesc());
		maps.put(TS_PROBLEM.getCode(), TS_PROBLEM.getDesc());
		maps.put(CONFIRMED.getCode(), CONFIRMED.getDesc());
		maps.put(EXCEPTION.getCode(), EXCEPTION.getDesc());
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
