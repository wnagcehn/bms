/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.biz.storage.service;

import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.biz.storage.entity.BizOutstockMasterEntity;

/**
 * 
 * @author stevenl
 * 
 */
public interface IBizOutstockMasterService {

    PageInfo<BizOutstockMasterEntity> query(Map<String, Object> condition, int pageNo,
            int pageSize);
    
    PageInfo<BizOutstockMasterEntity> queryNew(Map<String, Object> condition, int pageNo,
            int pageSize);

    BizOutstockMasterEntity findById(Long id);

    BizOutstockMasterEntity save(BizOutstockMasterEntity entity);

    int update(BizOutstockMasterEntity entity);
    
    int updateBatch(List<BizOutstockMasterEntity> list);

    void delete(Long id);
    
    PageInfo<BizOutstockMasterEntity> queryGroup(Map<String, Object> condition, int pageNo,
            int pageSize);

    Properties validRetry(Map<String, Object> param);
	 
	int reCalculate(Map<String, Object> param);
	
	 /**
	  * 判断是否有计算异常的数据
	  * @param condition
	  * @return
	  */
	 public BizOutstockMasterEntity queryExceptionOne(Map<String,Object> condition);

	List<String> queryAllWarehouseId(Map<String, Object> condition);

	List<BizOutstockMasterEntity> queryNewList(Map<String, Object> condition);

	void retryForCalcuFee(Map<String, Object> param);
}
