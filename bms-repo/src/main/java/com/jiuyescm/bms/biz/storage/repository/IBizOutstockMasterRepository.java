/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.biz.storage.repository;

import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.asyn.entity.BmsAsynCalcuTaskEntity;
import com.jiuyescm.bms.biz.storage.entity.BizOutstockMasterEntity;
import com.jiuyescm.bms.biz.storage.entity.BizOutstockPackmaterialEntity;

/**
 * 
 * @author stevenl
 * 
 */
public interface IBizOutstockMasterRepository {

	public PageInfo<BizOutstockMasterEntity> query(Map<String, Object> condition,
            int pageNo, int pageSize);

    
    PageInfo<BizOutstockMasterEntity> queryNew(Map<String, Object> condition, int pageNo,
            int pageSize);
	
    public BizOutstockMasterEntity findById(Long id);

    public BizOutstockMasterEntity save(BizOutstockMasterEntity entity);

    public int update(BizOutstockMasterEntity entity);

    public void delete(Long id);
    
    public PageInfo<BizOutstockMasterEntity> queryGroup(Map<String, Object> condition,
            int pageNo, int pageSize);

    Properties validRetry(Map<String, Object> param);
	 
	 int reCalculate(Map<String, Object> param);
	 
	 /**
	  * 判断是否有计算异常的数据
	  * @param condition
	  * @return
	  */
	 public BizOutstockMasterEntity queryExceptionOne(Map<String,Object> condition);

	public List<String> queryAllWarehouseId(Map<String, Object> condition);
	
	 int updateBatch(List<BizOutstockMasterEntity> list);

	List<BizOutstockMasterEntity> queryNewList(Map<String, Object> condition);


	void retryForCalcuFee(Map<String, Object> param);

	/**
	 * 查询出库需要发的任务
	 * @param condition
	 * @return
	 */
	List<BmsAsynCalcuTaskEntity> queryOutstockTask(Map<String, Object> condition);

}
