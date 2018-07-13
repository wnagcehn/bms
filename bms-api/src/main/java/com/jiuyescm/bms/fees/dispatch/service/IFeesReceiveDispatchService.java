package com.jiuyescm.bms.fees.dispatch.service;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.fees.dispatch.entity.FeesReceiveDispatchEntity;
import com.jiuyescm.bms.fees.dispatch.vo.FeesReceiveDispatchVo;

/**
 * 应收费用-配送费 接口
 * @author yangss
 */
public interface IFeesReceiveDispatchService {
	
	PageInfo<FeesReceiveDispatchEntity> query(Map<String, Object> condition, int pageNo, int pageSize);
	
	/**
	 * 宅配应付费用导出
	 */
	PageInfo<FeesReceiveDispatchVo> queryFeesImport(Map<String, Object> condition, int pageNo, int pageSize);
	
	PageInfo<FeesReceiveDispatchVo> query1(Map<String, Object> condition, int pageNo, int pageSize);
	
	List<FeesReceiveDispatchEntity> query(Map<String, Object> condition);
	
	FeesReceiveDispatchEntity queryById(Long id);
	
	List<FeesReceiveDispatchEntity> queryList(Map<String, Object> condition);
	
	int updateBatchTmp(List<FeesReceiveDispatchEntity> list);
	
	/**
	 * 批量插入账单数据
	 * @param list
	 * @return
	 */
	int insertBatchTmp(List<FeesReceiveDispatchEntity> list);
	
	/**
	 * 单条插入账单数据
	 * @param entity
	 * @return
	 */
	int insertOne(FeesReceiveDispatchEntity entity);
	
	List<FeesReceiveDispatchEntity> queryDispatchByBillNo(String billno);
	
	/**
	 * 根据条件查询单个费用
	 */
	FeesReceiveDispatchEntity queryOne(Map<String, Object> condition);

	PageInfo<FeesReceiveDispatchEntity> queryDispatchPage(
			Map<String, Object> parameter, int pageNo, int pageSize);

	void derateBatchAmount(List<FeesReceiveDispatchEntity> list);

	void deleteBatchFees(List<FeesReceiveDispatchEntity> list);

	List<FeesReceiveDispatchEntity> queryAllByBillSubject(
			Map<String, Object> parameter);

	List<Map<String, Object>> queryGroup(Map<String, Object> param);

	PageInfo<FeesReceiveDispatchEntity> querydistributionDetailByBizData(
			Map<String, Object> condition,int pageNo, int pageSize);

}
