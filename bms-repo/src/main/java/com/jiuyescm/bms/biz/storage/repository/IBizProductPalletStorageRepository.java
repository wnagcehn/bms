/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.biz.storage.repository;

import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.biz.storage.entity.BizProductPalletStorageEntity;

/**
 * 商品按托存储库存
 * @author wubangjun
 */
public interface IBizProductPalletStorageRepository {

	public PageInfo<BizProductPalletStorageEntity> query(Map<String, Object> condition,
            int pageNo, int pageSize);

    public BizProductPalletStorageEntity save(BizProductPalletStorageEntity entity);
    
    public int saveList(List<BizProductPalletStorageEntity> dataList);

    BizProductPalletStorageEntity findById(Long id);
    
    public BizProductPalletStorageEntity update(BizProductPalletStorageEntity entity);

    public void delete(Long id);
    
    public PageInfo<BizProductPalletStorageEntity> queryGroup(Map<String, Object> condition,
            int pageNo, int pageSize);

    Properties validRetry(Map<String, Object> param);
	 
	 int reCalculate(Map<String, Object> param);
	 
	 List<BizProductPalletStorageEntity> queryList(Map<String, Object> condition);
	 
	 int deleteList(List<BizProductPalletStorageEntity> list);
	 
	 int deleteFees(Map<String, Object> condition);
	 
	 PageInfo<Map<String,String>> queryByMonth(Map<String, Object> condition,int pageNo, int pageSize);
	 	 
	 PageInfo<Map<String,String>>  queryCustomeridF(Map<String, Object> condition,int pageNo, int pageSize);
	 
	 BizProductPalletStorageEntity  queryOneByParam(BizProductPalletStorageEntity entity);

	public List<BizProductPalletStorageEntity> queryAllBystockTime(
			Map<String, Object> parameter);
	 
	 int updateBatch(List<BizProductPalletStorageEntity> dataList);
	 
	 /**
	  * 查询商品存储托数差异
	  * @param condition
	  * @param pageNo
	  * @param pageSize
	  * @return
	  */
	 PageInfo<BizProductPalletStorageEntity> queryStorageDiff(Map<String, Object> condition, int pageNo, int pageSize);
		
	 List<BizProductPalletStorageEntity> queryStorageDiff(Map<String, Object> condition);
}
