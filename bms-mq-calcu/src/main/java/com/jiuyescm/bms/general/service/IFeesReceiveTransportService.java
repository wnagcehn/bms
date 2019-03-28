package com.jiuyescm.bms.general.service;

import java.util.List;
import java.util.Map;

import com.jiuyescm.bms.fees.entity.FeesReceiveDeliverQueryEntity;
import com.jiuyescm.bms.general.entity.FeesReceiveTransportEntity;

public interface IFeesReceiveTransportService {

	/**
	 * 单个写入运输费用表
	 * @param entity
	 */
	public boolean Insert(FeesReceiveTransportEntity entity);
	
	/**
	 * 批量写入运输费用表
	 * @param entity
	 */
	public void InsertBatch(List<FeesReceiveTransportEntity> entity);
	
	/**
	 * 单个删除运输费用表
	 * @param omsId 单号（oms内部单号-orderno）
	 */
	public void Delete(String omsId);
	
	
	/**
	 * 查询运输费用表
	 * @param entity
	 */
	public List<FeesReceiveTransportEntity> query(FeesReceiveDeliverQueryEntity queryEntity);
	
	/**
	 * 更新运输费用表
	 * @param entity
	 */
	public boolean update(FeesReceiveTransportEntity entity);
	
	/**
	 * 统计每天各商家、费用科目收入
	 * @param condition
	 * @return
	 */
	public List<FeesReceiveTransportEntity> queryDailyFees(Map<String, Object> condition);
}
