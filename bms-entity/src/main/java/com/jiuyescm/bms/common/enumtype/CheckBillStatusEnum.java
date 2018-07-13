package com.jiuyescm.bms.common.enumtype;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 账单状态
 * @author zhaofeng
 *
 */
public enum CheckBillStatusEnum {
	TB_CONFIRMED("TB_CONFIRMED", "待确认"),
	TB_INVOICE("TB_INVOICE", "待开票"),
	TB_RECEIPT("TB_RECEIPT", "待收款"),
	RECEIPTED("RECEIPTED", "已收款"),
	INVALIDATE("INVALIDATE","已作废"),
	BAD_BILL("BAD_BILL", "坏账");
	
	private String code;
	private String desc;
	
	private CheckBillStatusEnum(String code, String desc){
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
		maps.put(TB_CONFIRMED.getCode(), TB_CONFIRMED.getDesc());
		maps.put(TB_INVOICE.getCode(), TB_INVOICE.getDesc());
		maps.put(TB_RECEIPT.getCode(), TB_RECEIPT.getDesc());
		maps.put(RECEIPTED.getCode(), RECEIPTED.getDesc());
		maps.put(INVALIDATE.getCode(), INVALIDATE.getDesc());
		maps.put(BAD_BILL.getCode(), BAD_BILL.getDesc());
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
