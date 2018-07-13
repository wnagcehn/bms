package com.jiuyescm.bms.fees;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.fees.entity.FeesReceiveDeliverEntity;
import com.jiuyescm.bms.fees.entity.FeesReceiveDeliverQueryEntity;
/**
 * 
 *20170527 运输费用 service 层
 * @author Wuliangfeng
 *
 */
public interface IFeesReceiverDeliverService {
	//分页查找
	public PageInfo<FeesReceiveDeliverEntity> query(FeesReceiveDeliverQueryEntity queryEntity, int pageNo, int pageSize);
	
	public FeesReceiveDeliverEntity query(Map<String, Object> condition);
	
	//增加
	public int addFeesReceiveDeliver(FeesReceiveDeliverEntity entity);
	
	public int saveFeesList(List<FeesReceiveDeliverEntity> feesList);
	
	public String queryunitPrice(Map<String,String> condition);
	
	//根据运单号和时间查询
	public List<FeesReceiveDeliverEntity> queryList(Map<String,Object> condition);
	
	/**
	 * 插入或者更新
	 */
	public int insertOrUpdate(Map<String, Object> param);
	
	//分组统计费用
	public PageInfo<FeesReceiveDeliverEntity> queryFeeByGroup(Map<String,Object> param, int pageNo, int pageSize);
	
	//查询费用明细
	public List<FeesReceiveDeliverEntity> queryFeeDetail(Map<String,Object> param);
	
	
	PageInfo<FeesReceiveDeliverEntity> queryTransportPage(
			Map<String, Object> parameter, int pageNo, int pageSize);
	
	int updateBatch(List<FeesReceiveDeliverEntity> list);
	
	void derateBatchAmount(List<FeesReceiveDeliverEntity> list);
	
	void deleteBatchFees(List<FeesReceiveDeliverEntity> list);
	
	int updateBatchFees(List<FeesReceiveDeliverEntity> list);

	public PageInfo<FeesReceiveDeliverEntity> queryAbnormalFeeByGroup(
			Map<String, Object> parameter, int pageNo, int pageSize);

	public List<FeesReceiveDeliverEntity> queryAbnormalFeeDetail(
			Map<String, Object> parameter);

}
