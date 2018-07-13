package com.jiuyescm.bms.biz.storage.repository;

import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.biz.storage.entity.BizInStockDetailEntity;

/**
 * 入库单明细表
 * 
 * @author yangshuaishuai
 *
 */
public interface IBizInStockDetailRepository {
	
	PageInfo<BizInStockDetailEntity> query(Map<String, Object> condition, int pageNo, int pageSize);
	
}
