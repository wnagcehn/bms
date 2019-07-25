package com.jiuyescm.bms.general.service;

import java.util.List;
import java.util.Map;

import com.jiuyescm.bms.general.entity.FeesReceiveStorageEntity;

public interface IFeesReceiveStorageService {

	/**
	 * 单个写入仓储费用表
	 * @param entity
	 */
	public boolean Insert(FeesReceiveStorageEntity entity);
	
	/**
	 * 批量写入仓储费用表
	 * @param entity
	 */
	public void InsertBatch(List<FeesReceiveStorageEntity> entity);
	
	public void updateBatch(List<FeesReceiveStorageEntity> entity);
	
	public void updateFee(List<FeesReceiveStorageEntity> entity);
	
	/**
	 * 单个删除仓储费用表
	 * @param omsId 单号（oms内部单号-orderno）
	 */
	public void Delete(FeesReceiveStorageEntity entity);
	
	public List<FeesReceiveStorageEntity> queryStorageFee(String feesNo);
	
	/**
	 * 统计每天各商家、费用科目收入
	 * @param condition
	 * @return
	 */
	public List<FeesReceiveStorageEntity> queryDailyStorageFee(Map<String, Object> condition);
	
	public boolean updateStorageFee(FeesReceiveStorageEntity entity);
	
	public FeesReceiveStorageEntity validFeesNo(FeesReceiveStorageEntity entity);
	
	public void deleteBatch(Map<String, Object> feesNos);
	
	public void updateOne(FeesReceiveStorageEntity entity);
	
	//作废导入耗材对应的费用
    public void updateImportFee(Map<String, Object> condition);

}
