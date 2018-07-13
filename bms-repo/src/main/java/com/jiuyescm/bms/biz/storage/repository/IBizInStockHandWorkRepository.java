package com.jiuyescm.bms.biz.storage.repository;

import java.util.Map;
import java.util.Properties;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.biz.storage.vo.BizInstockHandworkVo;

/**
 * 入库单主表
 * 
 * @author yangshuaishuai
 *
 */
public interface IBizInStockHandWorkRepository {
	
	PageInfo<BizInstockHandworkVo> query(Map<String, Object> condition, int pageNo, int pageSize);
	
	int update(BizInstockHandworkVo entity);
	
	Properties validRetry(Map<String, Object> param);
	 
	int reCalculate(Map<String, Object> param);
	
}
