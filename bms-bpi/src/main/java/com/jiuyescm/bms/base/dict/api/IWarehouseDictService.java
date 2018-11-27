package com.jiuyescm.bms.base.dict.api;

import java.util.Map;

import com.jiuyescm.mdm.warehouse.vo.WarehouseVo;

/**
 * 仓库管理与查询服务
 * @author caojianwei
 *
 */
public interface IWarehouseDictService {

	/**
	 * 获取所有仓库 的仓库编码，仓库名称映射关系
	 * @return Map<仓库编码,仓库名称>
	 */
	Map<String, String> getWarehouseDictForKey();
	
	/**
	 * 获取所有仓库 的 仓库名称,仓库编码映射关系
	 * @return Map<仓库名称,仓库编码>
	 */
	Map<String, String> getWarehouseDictForValue();
	
	/**
	 * 根据仓库编码获取仓库名称
	 * @param code
	 * @return
	 */
	String getWarehouseNameByCode(String code);
	
	/**
	 * 根据仓库名称获取仓库编码
	 * @param code
	 * @return
	 */
	String getWarehouseCodeByName(String name);
	
	/**
	 * 根据仓库编号查询仓库信息
	 * @param code
	 * @return
	 */
	WarehouseVo getWarehouseByCode(String code);
	
	/**
	 * 根据仓库名称查询仓库信息
	 * @param name
	 * @return
	 */
	WarehouseVo getWarehouseByName(String name);
	
}
