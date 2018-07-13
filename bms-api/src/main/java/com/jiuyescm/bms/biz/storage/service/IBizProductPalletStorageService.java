/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.biz.storage.service;

import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.biz.storage.entity.BizPackStorageEntity;
import com.jiuyescm.bms.biz.storage.entity.BizProductPalletStorageEntity;

/**
 * 商品按托存储库存
 * @author wubangjun
 */
public interface IBizProductPalletStorageService {

    PageInfo<BizProductPalletStorageEntity> query(Map<String, Object> condition, int pageNo,
            int pageSize);

    BizProductPalletStorageEntity findById(Long id);

    BizProductPalletStorageEntity save(BizProductPalletStorageEntity entity);
    
    public int saveList(List<BizProductPalletStorageEntity> dataList);

    BizProductPalletStorageEntity update(BizProductPalletStorageEntity entity);

    void delete(Long id);
    
    PageInfo<BizProductPalletStorageEntity> queryGroup(Map<String, Object> condition, int pageNo,int pageSize);
    
    Properties validRetry(Map<String, Object> param);
	 
	int reCalculate(Map<String, Object> param);
	
	List<BizProductPalletStorageEntity> queryList(Map<String, Object> condition);
	
	int deleteList(List<BizProductPalletStorageEntity> dataList);
	
	int deleteFees(Map<String, Object> condition);
	
	PageInfo<Map<String,String>> queryByMonth(Map<String, Object> condition, int pageNo, int pageSize);
		
	PageInfo<Map<String, String>> queryCustomeridF(Map<String, Object> condition,int pageNo, int pageSize);
	
	int saveListAll(List<BizProductPalletStorageEntity> palletaddList,List<BizPackStorageEntity> packaddList,List<BizProductPalletStorageEntity> palletupdateList,List<BizPackStorageEntity> packupdateList);
	
	BizProductPalletStorageEntity queryOneByParam(BizProductPalletStorageEntity entity);

	List<BizProductPalletStorageEntity> queryAllBystockTime(
			Map<String, Object> parameter);
	
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
