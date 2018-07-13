package com.jiuyescm.bms.biz.storage.service;

import java.util.Map;
import java.util.Properties;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.biz.storage.vo.BizInstockHandworkVo;

/**
 * 入库单
 * 
 * @author yangshuaishuai
 * 
 */
public interface IBizInStockHandWorkService {

	/**
	 * 入库单主表分页查询
	 * @param condition
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	PageInfo<BizInstockHandworkVo> queryMaster(Map<String, Object> condition,int pageNo, int pageSize);
	
	int updateMaster(BizInstockHandworkVo entity);
	
	Properties validRetry(Map<String, Object> param);
	 
	int reCalculate(Map<String, Object> param);
}
