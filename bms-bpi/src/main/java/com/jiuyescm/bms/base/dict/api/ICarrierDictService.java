package com.jiuyescm.bms.base.dict.api;

import java.util.Map;

/**
 * 物流商管理与查询服务
 * @author caojianwei
 *
 */
public interface ICarrierDictService {

	/**
	 * 获取所有物流商 的物流商编码，物流商名称映射关系
	 * @return Map<物流商编码,物流商名称>
	 */
	Map<String, String> getCarrierDictForKey();
	
	/**
	 * 获取所有物流商 的 物流商名称,物流商编码映射关系
	 * @return Map<物流商名称,物流商编码>
	 */
	Map<String, String> getCarrierDictForValue();
	
	
	
	/**
	 * 根据物流商编码获取物流商名称
	 * @param code
	 * @return
	 */
	String getCarrierNameByCode(String code);
	
	/**
	 * 根据物流商名称获取物流商编码
	 * @param code
	 * @return
	 */
	String getCarrierCodeByName(String name);
	
}
