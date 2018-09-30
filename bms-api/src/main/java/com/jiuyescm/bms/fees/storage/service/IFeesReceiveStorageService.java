/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.fees.storage.service;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.biz.storage.entity.ReturnData;
import com.jiuyescm.bms.fees.storage.entity.FeesReceiveStorageEntity;

/**
 * 
 * @author stevenl
 * 
 */
public interface IFeesReceiveStorageService {

    PageInfo<FeesReceiveStorageEntity> query(Map<String, Object> condition, int pageNo,
            int pageSize);

    FeesReceiveStorageEntity findById(Long id);

    FeesReceiveStorageEntity save(FeesReceiveStorageEntity entity);

    FeesReceiveStorageEntity update(FeesReceiveStorageEntity entity);
    
	/**
	 * 根据条件查询单个费用
	 */
    FeesReceiveStorageEntity queryOne(Map<String, Object> condition);

    void delete(Long id);

	List<FeesReceiveStorageEntity> queryAll(Map<String, Object> parameter);

	String getUnitPrice(Map<String, Object> param);
	
	ReturnData reCount(List<FeesReceiveStorageEntity> list);
	
	int insertBatchTmp(List<FeesReceiveStorageEntity> list);
	
	PageInfo<FeesReceiveStorageEntity> queryFees(Map<String, Object> condition, int pageNo,
            int pageSize);
		
	int deleteEntity(String feesNo);

	/**
	 * 查询出库费用
	 * @param parameter
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	PageInfo<FeesReceiveStorageEntity> queryOutStockPage(
			Map<String, Object> parameter, int pageNo, int pageSize);
	
	PageInfo<FeesReceiveStorageEntity> queryProductStoragePage(
			Map<String, Object> parameter, int pageNo, int pageSize);

	PageInfo<FeesReceiveStorageEntity> queryMaterialPage(
			Map<String, Object> parameter, int pageNo, int pageSize);
	
	PageInfo<FeesReceiveStorageEntity> queryStorageAddFeePage(
			Map<String, Object> parameter, int pageNo, int pageSize);
	
	/**
	 * 预账单增值费
	 * @param parameter
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	PageInfo<FeesReceiveStorageEntity> queryPreBillStorageAddFee(
			Map<String, Object> parameter, int pageNo, int pageSize);
	
	int updateBatch(List<FeesReceiveStorageEntity> list);

	void derateBatchAmount(List<FeesReceiveStorageEntity> list);

	void deleteBatchFees(List<FeesReceiveStorageEntity> list);

	List<Map<String, Object>> queryGroup(Map<String, Object> param);
	
	public int updateByFeeNoList(Map<String, Object> condition);

	List<FeesReceiveStorageEntity> queryFeesData(Map<String, Object> condition);
	
	/**
	 * 预账单仓储费(按托)
	 * @param condition
	 * @return
	 */
	List<FeesReceiveStorageEntity> queryPreBillStorage(Map<String, Object> condition);
	
	/**
	 * 预账单耗材存储费
	 * @param condition
	 * @return
	 */
	List<FeesReceiveStorageEntity> queryPreBillMaterialStorage(Map<String, Object> condition);

	public int deleteBatch(Map<String, Object> feesNos);
	
	/**
	 * 查询预账单仓库
	 * @param param
	 * @return
	 */
	List<String> queryPreBillWarehouse(Map<String,Object> param);
	
	/**
	 * 根据费用编号查询
	 * @param FeesNo
	 * @return
	 */
	List<FeesReceiveStorageEntity> queryByFeesNo(String FeesNo);
	
	/**
	 * 预账单仓储费(按件)
	 * @param condition
	 * @return
	 */
	List<FeesReceiveStorageEntity> queryPreBillStorageByItems(Map<String, Object> condition);
	
	/**
	 * 处置费
	 * @param condition
	 * @return
	 */
	List<FeesReceiveStorageEntity> queryPreBillPallet(Map<String, Object> condition);
	
	/**
	 * 查询计算失败的费用
	 * @param parameter
	 * @return
	 */
	List<FeesReceiveStorageEntity> queryCalculateFail(Map<String, Object> parameter);
}
