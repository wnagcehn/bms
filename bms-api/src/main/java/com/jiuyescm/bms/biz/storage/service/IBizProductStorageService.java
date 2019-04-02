/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.biz.storage.service;

import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.biz.storage.entity.BizProductStorageEntity;

/**
 * 
 * @author stevenl
 * 
 */
public interface IBizProductStorageService {

    PageInfo<BizProductStorageEntity> query(Map<String, Object> condition, int pageNo,
            int pageSize);

    BizProductStorageEntity findById(Long id);

    BizProductStorageEntity save(BizProductStorageEntity entity);

    BizProductStorageEntity update(BizProductStorageEntity entity);

    void delete(Long id);
    
	 /**
	  * 判断是否有计算异常的数据
	  * @param condition
	  * @return
	  */
	 public BizProductStorageEntity queryExceptionOne(Map<String,Object> condition);


	 Properties validRetry(Map<String, Object> param);
	 
	 int reCalculate(Map<String, Object> param);

	List<BizProductStorageEntity> queryList(Map<String, Object> condition);
}
