package com.jiuyescm.bms.common.enumtype.biz;

import java.util.HashMap;
import java.util.Map;

/**
 * 销售员对应的科目类别
 * @author yangss
 */
public enum SellerSubjectCategoryEnum {
	STORAGE("sale_storage", "仓储费"),
	TRANSPORT("sale_transport", "运输费"),
	DELIVER("sale_deliver", "配送费"),
	MATERIAL("sale_material", "耗材费");

	private String code;
	private String desc;
	private SellerSubjectCategoryEnum(String code, String desc){
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
		maps.put(STORAGE.getCode(), STORAGE.getDesc());
		maps.put(TRANSPORT.getCode(), TRANSPORT.getDesc());
		maps.put(DELIVER.getCode(), DELIVER.getDesc());
		maps.put(MATERIAL.getCode(), MATERIAL.getDesc());
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
