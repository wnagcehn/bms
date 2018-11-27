package com.jiuyescm.constants;

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
	 * 0-不开票  1-开票
	 */
	public enum isInvoice{
		Invoice("1","开票"),unInvoice("0","不开票");
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
}
