package com.jiuyescm.bms.common.enumtype;

import java.util.HashMap;
import java.util.Map;

/**
 * 收款状态
 * @author zhaofeng
 *
 */
public enum BillCheckReceiptStateEnum {
	UN_RECEIPT("UN_RECEIPT", "未收款"),
	RECEIPTED("RECEIPTED", "已收款"),
	PART_RECEIPT("PART_RECEIPT", "部分收款");

	private String code;
	private String desc;
	private BillCheckReceiptStateEnum(String code, String desc){
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
		maps.put(UN_RECEIPT.getCode(), UN_RECEIPT.getDesc());
		maps.put(RECEIPTED.getCode(), RECEIPTED.getDesc());
		maps.put(PART_RECEIPT.getCode(), PART_RECEIPT.getDesc());
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
