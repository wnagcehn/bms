package com.jiuyescm.bms.base.dict.api;

import java.util.Map;

/**
 * 宅配商管理与查询服务
 * @author caojianwei
 *
 */
public interface IDeliverDictService {

	/**
	 * 获取所有宅配商 的宅配商编码，宅配商名称映射关系
	 * @return Map<宅配商编码,宅配商名称>
	 */
	Map<String, String> getDeliverDictForKey();
	
	/**
	 * 获取所有宅配商 的 宅配商名称,宅配商编码映射关系
	 * @return Map<宅配商名称,宅配商编码>
	 */
	Map<String, String> getDeliverDictForValue();
	
	/**
	 * 根据宅配商编码获取宅配商名称
	 * @param code
	 * @return
	 */
	String getDeliverNameByCode(String code);
	
	/**
	 * 根据宅配商名称获取宅配商编码
	 * @param code
	 * @return
	 */
	String getDeliverCodeByName(String name);
	
}
