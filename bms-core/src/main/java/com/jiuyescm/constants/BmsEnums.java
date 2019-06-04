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
		
		public String getCode(){
			return code;
		}
		
		public String getDesc(){
			return desc;
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
		
		public String getCode(){
			return code;
		}
		
		public String getDesc(){
			return desc;
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
			this.code = code;
			this.desc = desc;
		}
		public String getCode() {
			return code;
		}
		public String getDesc() {
			return desc;
		}
		
		private static Map<String,String> maps = new HashMap<String,String>();
		private static Map<String,String> mapValue =new LinkedHashMap<String,String>();
		static{
			maps.put(BEGIN.code, BEGIN.desc);
			maps.put(WH_FOLLOW.code, WH_FOLLOW.desc);
			maps.put(CUST_FOLLOW.code, CUST_FOLLOW.desc);
			maps.put(CUST_NO_FEEDBACK.code, CUST_NO_FEEDBACK.desc);
			maps.put(CUST_CHECKING.code, CUST_CHECKING.desc);
			maps.put(QUOTATION.code, QUOTATION.desc);
			maps.put(WH_PROBLEM.code, WH_PROBLEM.desc);
			maps.put(PROJECT_PROBLEM.code, PROJECT_PROBLEM.desc);
			maps.put(TS_PROBLEM.code, TS_PROBLEM.desc);
			maps.put(CONFIRMED.code, CONFIRMED.desc);
			mapValue.put(BEGIN.desc, BEGIN.code);
			mapValue.put(WH_FOLLOW.desc, WH_FOLLOW.code);
			mapValue.put(CUST_FOLLOW.desc, CUST_FOLLOW.code);
			mapValue.put(CUST_NO_FEEDBACK.desc, CUST_NO_FEEDBACK.code);
			mapValue.put(CUST_CHECKING.desc, CUST_CHECKING.code);
			mapValue.put(QUOTATION.desc, QUOTATION.code);
			mapValue.put(WH_PROBLEM.desc, WH_PROBLEM.code);
			mapValue.put(PROJECT_PROBLEM.desc, PROJECT_PROBLEM.code);
			mapValue.put(TS_PROBLEM.desc, TS_PROBLEM.code);
			mapValue.put(CONFIRMED.desc, CONFIRMED.code);
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
	 * 任务状态
	 * @author wangchen870
	 * WAIT-等待，PROCESS-处理中，SUCCESS-成功，FAIL-失败，EXCEPTION-异常
	 */
	public enum taskStatus{
		WAIT("WAIT","等待"),PROCESS("PROCESS","处理中"),SUCCESS("SUCCESS","成功"),FAIL("FAIL","失败"),EXCEPTION("EXCEPTION","异常");
		private String code;
		private String desc;
		private taskStatus(String code, String desc){
			this.code = code;
			this.desc = desc;
		}
		
		public String getCode(){
			return code;
		}
		
		public String getDesc(){
			return desc;
		}
		
		private static Map<String,String> mapKey =new LinkedHashMap<String,String>();
		private static Map<String,String> mapValue =new LinkedHashMap<String,String>();
		static{
			mapKey.put(WAIT.code, WAIT.desc);
			mapKey.put(PROCESS.code, PROCESS.desc);
			mapKey.put(SUCCESS.code, SUCCESS.desc);
			mapKey.put(FAIL.code, FAIL.desc);
			mapKey.put(EXCEPTION.code, EXCEPTION.desc);
			mapValue.put(WAIT.desc,WAIT.code);
			mapValue.put(PROCESS.desc, PROCESS.code);
			mapValue.put(SUCCESS.desc, SUCCESS.code);
			mapValue.put(FAIL.desc, FAIL.code);
			mapValue.put(EXCEPTION.desc, EXCEPTION.code);
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
	 * 托数类型
	 * @author wangchen870
	 *
	 */
	public enum palletType{
		product("product","商品托数"),material("material","耗材托数"),instock("instock","入库托数"),outstock("outstock","出库托数");
		private String code;
		private String desc;
		private palletType(String code, String desc){
			this.code = code;
			this.desc = desc;
		}
		
		public String getCode(){
			return code;
		}
		
		public String getDesc(){
			return desc;
		}
		
		private static Map<String,String> mapKey =new LinkedHashMap<String,String>();
		private static Map<String,String> mapValue =new LinkedHashMap<String,String>();
		static{
			mapKey.put(product.code, product.desc);
			mapKey.put(material.code, material.desc);
			mapKey.put(instock.code, instock.desc);
			mapKey.put(outstock.code, outstock.desc);
			mapValue.put(product.desc,product.code);
			mapValue.put(material.desc, material.code);
			mapValue.put(instock.desc, instock.code);
			mapValue.put(outstock.desc, outstock.code);
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
     * 模板类型
     * @author wangchen
     * system-系统模板  wms-wms模板
     */
    public enum templateType{
        system("system","系统模板"),wms("wms","wms模板");
        private String code;
        private String desc;
        private templateType(String code, String desc){
            this.code = code;
            this.desc = desc;
        }
        
        public String getCode(){
            return code;
        }
        
        public String getDesc(){
            return desc;
        }
        
        private static Map<String,String> mapKey =new LinkedHashMap<String,String>();
        private static Map<String,String> mapValue =new LinkedHashMap<String,String>();
        static{
            mapKey.put(system.code, system.desc);
            mapKey.put(wms.code, wms.desc);
            mapValue.put(system.desc,system.code);
            mapValue.put(wms.desc, wms.code);
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
     * 异常计算状态
     * 
     */
    public enum errorCalcuStatus{
        All("", "全部"),
        Sys_Error("2", "系统错误"),
        Contract_Miss("3", "合同不存在"),
        Quote_Miss("4", "报价缺失"),
        No_Exe("5","不计算"),
        Other("10", "其他"),
        Quote_More("6", "多个报价"),
        Retry("99","待重算");
        
        private String code;
        private String desc;
        private errorCalcuStatus(String code, String desc){
            this.code = code;
            this.desc = desc;
        }
        
        public String getCode(){
            return code;
        }
        
        public String getDesc(){
            return desc;
        }
        
        private static Map<String,String> mapKey =new LinkedHashMap<String,String>();
        private static Map<String,String> mapValue =new LinkedHashMap<String,String>();
        static{
            mapKey.put(All.getCode(), All.getDesc());
            mapKey.put(Sys_Error.getCode(), Sys_Error.getDesc());
            mapKey.put(Contract_Miss.getCode(),Contract_Miss.getDesc());
            mapKey.put(Quote_Miss.getCode(),Quote_Miss.getDesc());
            mapKey.put(No_Exe.getCode(),No_Exe.getDesc());
            mapKey.put(Other.getCode(), Other.getDesc());
            mapKey.put(Retry.getCode(), Retry.getDesc());
            mapValue.put(All.getCode(), All.getDesc());
            mapValue.put(Sys_Error.getCode(), Sys_Error.getDesc());
            mapValue.put(Contract_Miss.getCode(),Contract_Miss.getDesc());
            mapValue.put(Quote_Miss.getCode(),Quote_Miss.getDesc());
            mapValue.put(No_Exe.getCode(),No_Exe.getDesc());
            mapValue.put(Other.getCode(), Other.getDesc());
            mapValue.put(Retry.getCode(), Retry.getDesc());
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
