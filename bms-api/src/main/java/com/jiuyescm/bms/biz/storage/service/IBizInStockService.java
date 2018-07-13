package com.jiuyescm.bms.biz.storage.service;

import java.util.Map;
import java.util.Properties;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.biz.storage.entity.BizInStockDetailEntity;
import com.jiuyescm.bms.biz.storage.entity.BizInStockMasterEntity;

/**
 * 入库单
 * 
 * @author yangshuaishuai
 * 
 */
public interface IBizInStockService {

	/**
	 * 入库单主表分页查询
	 * @param condition
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	PageInfo<BizInStockMasterEntity> queryMaster(Map<String, Object> condition,int pageNo, int pageSize);
	
	int updateMaster(BizInStockMasterEntity entity);
	
	/**
	 * 入库单明细表分页查询
	 * @param condition
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	PageInfo<BizInStockDetailEntity> queryDetail(Map<String, Object> condition,int pageNo, int pageSize);
	
	Properties validRetry(Map<String, Object> param);
	 
	int reCalculate(Map<String, Object> param);
}
