package com.jiuyescm.bms.base.dict.api;

import java.util.Map;

import com.jiuyescm.bms.base.dict.vo.PubMaterialVo;


public interface IMaterialDictService {

	/**
	 * 获取所有仓库 的仓库编码，仓库名称映射关系
	 * @return Map<仓库编码,仓库名称>
	 */
	Map<String, String> getMaterialDictForKey();
	
	/**
	 * 获取所有仓库 的 仓库名称,仓库编码映射关系
	 * @return Map<仓库名称,仓库编码>
	 */
	Map<String, String> getMaterialDictForValue();
	
	/**
	 * 根据仓库编码获取仓库名称
	 * @param code
	 * @return
	 */
	String getMaterialNameByCode(String code);
	
	/**
	 * 根据仓库名称获取仓库编码
	 * @param code
	 * @return
	 */
	String getMaterialCodeByName(String name);
	
	/**
	 * 根据仓库编号查询仓库信息
	 * @param code
	 * @return
	 */
	PubMaterialVo getMaterialByCode(String code);
	
	/**
	 * 根据仓库名称查询仓库信息
	 * @param name
	 * @return
	 */
	PubMaterialVo getMaterialByName(String name);
	
}
