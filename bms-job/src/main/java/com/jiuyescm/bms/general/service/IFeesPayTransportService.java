package com.jiuyescm.bms.general.service;

import java.util.List;

import com.jiuyescm.bms.fees.out.transport.entity.FeesPayTransportEntity;
import com.jiuyescm.bms.fees.out.transport.entity.FeesPayTransportQueryEntity;


public interface IFeesPayTransportService {

	/**
	 * 单个写入运输费用表
	 * @param entity
	 */
	public boolean insert(FeesPayTransportEntity entity);
	
	/**
	 * 批量写入运输费用表
	 * @param entity
	 */
	public void insertBatch(List<FeesPayTransportEntity> entity);
	
	/**
	 * 单个删除运输费用表
	 * @param omsId 单号（oms内部单号-orderno）
	 */
	public void delete(String omsId);
	
	
	/**
	 * 查询运输费用表
	 * @param entity
	 */
	public List<FeesPayTransportEntity> query(FeesPayTransportQueryEntity queryEntity);
	
	/**
	 * 更新运输费用表
	 * @param entity
	 */
	public boolean update(FeesPayTransportEntity entity);
}
