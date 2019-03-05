package com.jiuyescm.bms.correct.service;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.correct.BmsMarkingProductsEntity;
import com.jiuyescm.bms.correct.vo.BmsMarkingProductsVo;

/**
 * ..Service
 * @author wangchen
 * 
 */
public interface IBmsMarkingProductsService {

    PageInfo<BmsMarkingProductsVo> query(Map<String, Object> condition, int pageNo,
            int pageSize) throws Exception;

	List<BmsMarkingProductsEntity> query(Map<String, Object> condition);
	
	/**
	 * 查询该组合下不同运单重量对应的运单明细
	 * @param condition
	 * @param pageNo
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	PageInfo<BmsMarkingProductsVo> queryByWeight(Map<String, Object> condition, int pageNo, int pageSize)
			throws Exception;
	
	/**
	 * 查询不同耗材组合对应的耗材明细
	 * @param condition
	 * @param pageNo
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	PageInfo<BmsMarkingProductsVo> queryByMaterial(Map<String, Object> condition, int pageNo, int pageSize)
			throws Exception;

}
