package com.jiuyescm.constants;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * bms枚举类
 * @author caojianwei
 *
 */
public class BmsEnums {

	/**
	 * 是否开票 枚举
	 * @author caojianwei
	 * 0-否  1-是
	 */
	public enum isInvoice{
		Invoice("1","是"),unInvoice("0","否");
		private String code;
		private String desc;
		private isInvoice(String code, String desc){
			this.code = code;
			this.desc = desc;
		}
		private static Map<String,String> mapKey =new LinkedHashMap<String,String>();
		private static Map<String,String> mapValue =new LinkedHashMap<String,String>();
		static{
			mapKey.put(Invoice.code, Invoice.desc);
			mapKey.put(unInvoice.code, unInvoice.desc);
			mapValue.put(Invoice.desc,Invoice.code);
			mapValue.put(unInvoice.desc, unInvoice.code);
		}
		public static Map<String,String> getMap(){
			return mapKey;
		}
		
		public static String getDesc(String code)
		{
			if (mapKey.containsKey(code))
			{
				return mapKey.get(code);
			}
			return null;
		}
		
		public static String getCode(String desc)
		{
			if (mapValue.containsKey(desc))
			{
				return mapValue.get(desc);
			}
			return null;
		}	
	}
	
	/**
	 * 温度 枚举
	 * LD-冷冻  LC-冷藏 CW-常温 HW-恒温
	 */
	public enum tempretureType{
		LD("LD","冷冻"),LC("LC","冷藏"),CW("CW","常温"),HW("HW","恒温");
		private String code;
		private String desc;
		private tempretureType(String code, String desc){
			this.code = code;
			this.desc = desc;
		}
		private static Map<String,String> mapKey =new LinkedHashMap<String,String>();
		private static Map<String,String> mapValue =new LinkedHashMap<String,String>();
		static{
			mapKey.put(LD.code, LD.desc);
			mapKey.put(LC.code, LC.desc);
			mapKey.put(CW.code, CW.desc);
			mapKey.put(HW.code, HW.desc);
			mapValue.put(LD.desc,LD.code);
			mapValue.put(LC.desc, LC.code);
			mapValue.put(CW.desc, CW.code);
			mapValue.put(HW.desc, HW.code);
		}
		public static Map<String,String> getMap(){
			return mapKey;
		}
		
		public static String getDesc(String code)
		{
			if (mapKey.containsKey(code))
			{
				return mapKey.get(code);
			}
			return null;
		}
		
		public static String getCode(String desc)
		{
			if (mapValue.containsKey(desc))
			{
				return mapValue.get(desc);
			}
			return null;
		}	
	}
	
	/**
	 * 对账状态
	 * @author wangchen870
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
		CONFIRMED("CONFIRMED", "已确认");
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
}
