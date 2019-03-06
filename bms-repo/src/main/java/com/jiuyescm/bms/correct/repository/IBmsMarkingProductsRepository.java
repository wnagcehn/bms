package com.jiuyescm.bms.correct.repository;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.correct.BmsMarkingProductsEntity;

/**
 * ..Repository
 * @author wangchen
 * 
 */
public interface IBmsMarkingProductsRepository {

	PageInfo<BmsMarkingProductsEntity> query(Map<String, Object> condition,
		int pageNo, int pageSize);

	List<BmsMarkingProductsEntity> query(Map<String, Object> condition);
	
	/**
	 * 查询该组合下不同运单重量对应的运单明细
	 * @param condition
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	PageInfo<BmsMarkingProductsEntity> queryByWeight(Map<String, Object> condition, int pageNo, int pageSize);

	/**
	 * 查询不同耗材组合对应的耗材明细
	 * @param condition
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	PageInfo<BmsMarkingProductsEntity> queryByMaterial(Map<String, Object> condition, int pageNo, int pageSize);

}
