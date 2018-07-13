package com.jiuyescm.bms.general.service;

import java.util.List;
import java.util.Map;

import com.jiuyescm.bms.general.entity.FeesReceiveDispatchEntity;

public interface IFeesReceiveDispatchService {

	/**
	 * 单个写入配送费用表
	 * @param entity
	 */
	public boolean Insert(FeesReceiveDispatchEntity entity);
	
	/**
	 * 更新费用表
	 * @param entity
	 * @return
	 */
	public boolean update(FeesReceiveDispatchEntity entity);
	
	/**
	 * 批量写入配送费用表
	 * @param entity
	 */
	public void InsertBatch(List<FeesReceiveDispatchEntity> entity);
	
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
	public FeesReceiveDispatchEntity queryFees(Map<String,Object> aCondition);
	
	FeesReceiveDispatchEntity validFeesNo(FeesReceiveDispatchEntity entity);

	public void deleteBatch(Map<String, Object> feesMap);

	/**
	 * 根据运单号作废费用
	 * @param wayBillNoList
	 * @return
	 */
	public int deleteByWayBillNo(String waybillNo);
	
	/**
	 * 统计每天各商家、费用科目收入
	 * @param aCondition
	 * @return
	 */
	public List<FeesReceiveDispatchEntity> queryDailyFees(Map<String,Object> condition);
	
}
