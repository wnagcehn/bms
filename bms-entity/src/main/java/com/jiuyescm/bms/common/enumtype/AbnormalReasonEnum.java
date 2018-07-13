/**
 * 
 */
package com.jiuyescm.bms.common.enumtype;

import java.util.HashMap;
import java.util.Map;

/**
 * 理赔原因枚举
 * @author yangss
 */
public enum AbnormalReasonEnum {

	CUSTOMER("1", "顾客原因"),
	DELIVER("2", "承运商原因"),
	WAREHOUSE("3", "仓库原因"),
	SELLER("4", "商家原因"),
	COMPANY("56", "公司原因");
	
	private String code;
	private String desc;
	
	private AbnormalReasonEnum(String code, String desc) {
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
		maps.put(CUSTOMER.getCode(), CUSTOMER.getDesc());
		maps.put(DELIVER.getCode(), DELIVER.getDesc());
		maps.put(WAREHOUSE.getCode(), WAREHOUSE.getDesc());
		maps.put(SELLER.getCode(), SELLER.getDesc());
		maps.put(COMPANY.getCode(), COMPANY.getDesc());
	}
	
	public static Map<String,String> getMap() {
		return maps;
	}
	
	public static String getDesc(String code) {
		if (maps.containsKey(code)) {
			return maps.get(code);
		}
		return null;
	}
}
