package com.jiuyescm.bms.general.service;

import java.util.List;
import java.util.Map;

import com.jiuyescm.bms.general.entity.FeesPayDispatchEntity;


public interface IFeesPayDispatchService {

	/**
	 * 单个写入配送费用表
	 * @param entity
	 */
	public boolean Insert(FeesPayDispatchEntity entity);
	
	/**
	 * 更新费用表
	 * @param entity
	 * @return
	 */
	public boolean update(FeesPayDispatchEntity entity);
	
	/**
	 * 批量写入配送费用表
	 * @param entity
	 */
	public void InsertBatch(List<FeesPayDispatchEntity> entity);
	
	/**
	 * 单个删除配送费用表
	 * @param omsId 单号（oms内部单号-orderno）
	 */
	public void Delete(String omsId);
	
	/**
	 * 按照条件查找对应得费用记录
	 * @param aCondition
	 * @return
	 */
	public FeesPayDispatchEntity queryFees(Map<String,Object> aCondition);
	
	FeesPayDispatchEntity validFeesNo(FeesPayDispatchEntity entity);

	public void deleteBatch(Map<String, Object> feesMap);
	
}
