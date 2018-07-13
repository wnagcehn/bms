package com.jiuyescm.bms.common.enumtype;

import java.util.Map;

import com.google.common.collect.Maps;

public enum RecordLogUrlNameEnum {
	IN_STORAGE_BASE_PRICE("in_storage_base_price","应收仓储基础报价"),
	IN_STORAGE_OTHER_PRICE("in_storage_other_price","应收仓储其他报价"),
	IN_STORAGE_MATERIAL_PRICE("in_storage_material_price","应收耗材报价"),
	IN_TRANSPORT_BASE_PRICE("in_transport_base_price","应收运输基础报价"),
	IN_TRANSPORT_OTHER_PRICE("in_transport_other_price","应收运输其他报价"),
	IN_DELIVER_BASE_PRICE("in_deliver_base_price","应收宅配报价"),
	OUT_TRANSPORT_BASE_PRICE("out_transport_base_price","应付运输费报价"),
	OUT_TRANSPORT_OTHER_PRICE("out_transport_other_price","应付运输增值费报价"),
	OUT_DELIVER_BASE_PRICE("out_deliver_base_price","应付配送报价"),
	OUT_DELIVER_OTHER_PRICE("out_deliver_other_price","应付配送增值报价"),
	CONTACT_CUSTOMER("contact_customer","商家合同"),
	CONTACT_FORWARDER("contact_forwarder","承运商合同"),
	CONTACT_DELIVER("contact_deliver","宅配商合同");
	private String code;
	private String desc;
	private RecordLogUrlNameEnum(String code, String desc){
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
	private static Map<String,String> maps = Maps.newLinkedHashMap();
	static{
		maps.put(IN_STORAGE_BASE_PRICE.getCode(), IN_STORAGE_BASE_PRICE.getDesc());
		maps.put(IN_STORAGE_OTHER_PRICE.getCode(), IN_STORAGE_OTHER_PRICE.getDesc());
		maps.put(IN_STORAGE_MATERIAL_PRICE.getCode(), IN_STORAGE_MATERIAL_PRICE.getDesc());
		maps.put(IN_TRANSPORT_BASE_PRICE.getCode(), IN_TRANSPORT_BASE_PRICE.getDesc());
		maps.put(IN_TRANSPORT_OTHER_PRICE.getCode(), IN_TRANSPORT_OTHER_PRICE.getDesc());
		maps.put(IN_DELIVER_BASE_PRICE.getCode(), IN_DELIVER_BASE_PRICE.getDesc());
		maps.put(OUT_TRANSPORT_BASE_PRICE.getCode(), OUT_TRANSPORT_BASE_PRICE.getDesc());
		maps.put(OUT_TRANSPORT_OTHER_PRICE.getCode(), OUT_TRANSPORT_OTHER_PRICE.getDesc());
		maps.put(OUT_DELIVER_BASE_PRICE.getCode(), OUT_DELIVER_BASE_PRICE.getDesc());
		maps.put(OUT_DELIVER_OTHER_PRICE.getCode(), OUT_DELIVER_OTHER_PRICE.getDesc());
		maps.put(CONTACT_CUSTOMER.getCode(), CONTACT_CUSTOMER.getDesc());
		maps.put(CONTACT_FORWARDER.getCode(), CONTACT_FORWARDER.getDesc());
		maps.put(CONTACT_DELIVER.getCode(), CONTACT_DELIVER.getDesc());
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
