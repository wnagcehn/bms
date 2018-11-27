package com.jiuyescm.bms.base.dict.api;

import java.util.Map;

/**
 * 商家管理与查询服务
 * @author caojianwei
 *
 */
public interface ICustomerDictService {

	/**
	 * 获取所有商家 的商家编码，商家名称映射关系
	 * @return Map<商家编码,商家名称>
	 */
	Map<String, String> getCustomerDictForKey();
	
	/**
	 * 获取所有商家 的 商家名称,商家编码映射关系
	 * @return Map<商家名称,商家编码>
	 */
	Map<String, String> getCustomerDictForValue();
	
	/**
	 * 根据商家编码获取商家名称
	 * @param code 商家ID
	 * @return
	 */
	String getCustomerNameByCode(String code);
	
	/**
	 * 根据商家名称获取商家编码
	 * @param name 商家全称
	 * @return
	 */
	String getCustomerCodeByName(String name);
	
}
