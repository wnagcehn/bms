/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.biz.storage.repository;

import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.biz.storage.entity.BizPackStorageEntity;

/**
 * 
 * @author stevenl
 * 
 */
public interface IBizPackStorageRepository {

	public PageInfo<BizPackStorageEntity> query(Map<String, Object> condition,
            int pageNo, int pageSize);

    public BizPackStorageEntity findById(Long id);

    public BizPackStorageEntity save(BizPackStorageEntity entity);

    public int update(BizPackStorageEntity entity);
    
    int saveList(List<BizPackStorageEntity> list);
    
    public PageInfo<BizPackStorageEntity> queryGroup(Map<String, Object> condition,
            int pageNo, int pageSize);
    
    Properties validRetry(Map<String, Object> param);
	 
	int reCalculate(Map<String, Object> param);
	
	int deleteList(List<BizPackStorageEntity> list);
	
	List<BizPackStorageEntity> queryList(Map<String, Object> condition);
	
	int deleteFees(Map<String, Object> condition);
	
    PageInfo<Map<String,String>> queryByMonth(Map<String, Object> condition,int pageNo, int pageSize);
	 	 
    PageInfo<Map<String,String>>  queryCustomeridF(Map<String, Object> condition,int pageNo, int pageSize);
	 /**
	  * 判断是否有计算异常的数据
	  * @param condition
	  * @return
	  */
	 public BizPackStorageEntity queryExceptionOne(Map<String,Object> condition);
	 
	 BizPackStorageEntity queryOneByParam(BizPackStorageEntity entity);
	 
	 int updateBatch(List<BizPackStorageEntity> list);

	public List<BizPackStorageEntity> queryAllBycurTime(Map<String, Object> map);
	int saveTempData(String taskId);

	int delete(BizPackStorageEntity entity);
	
	/**
	 * 校验是否存在
	 * @param param
	 * @return
	 */
	int checkIsNotExist(BizPackStorageEntity entity);
}
