package com.jiuyescm.bms.common.enumtype;

import java.util.HashMap;
import java.util.Map;

/**
 * 回款状态
 * @author zhaofeng
 *
 */
public enum BillReceiptFollowStateEnum {
	LEGAL_PROCESS("LEGAL_PROCESS", "法律程序"),
	FOLLOWING("FOLLOWING", "跟进中"),
	BAD_BILL("BAD_BILL", "坏账"),
	RECEIPTED("RECEIPTED", "已回款"),
	EXCEPTION("EXCEPTION", "异常"),
	PAUSE("PAUSE", "暂停");

	private String code;
	private String desc;
	private BillReceiptFollowStateEnum(String code, String desc){
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
		maps.put(LEGAL_PROCESS.getCode(), LEGAL_PROCESS.getDesc());
		maps.put(FOLLOWING.getCode(), FOLLOWING.getDesc());
		maps.put(BAD_BILL.getCode(), BAD_BILL.getDesc());
		maps.put(RECEIPTED.getCode(), RECEIPTED.getDesc());
		maps.put(EXCEPTION.getCode(), EXCEPTION.getDesc());
		maps.put(PAUSE.getCode(), PAUSE.getDesc());
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
