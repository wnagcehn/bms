
package com.jiuyescm.bms.biz.dispatch.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.biz.dispatch.entity.BizDispatchBillEntity;
import com.jiuyescm.bms.biz.dispatch.repository.IBizDispatchBillRepository;
import com.jiuyescm.bms.biz.dispatch.service.IBizDispatchBillService;
import com.jiuyescm.bms.biz.dispatch.vo.BizDispatchBillVo;

@Service("bizDispatchBillService")
public class BizDispatchBillServiceImp implements IBizDispatchBillService{

	@Resource
	private IBizDispatchBillRepository bizRepository;
	
	@Override
	public PageInfo<BizDispatchBillEntity> queryAll(
			Map<String, Object> condition, int pageNo, int pageSize) {
		// TODO Auto-generated method stub
		return bizRepository.queryAll(condition, pageNo, pageSize);
	}

	@Override
	public PageInfo<BizDispatchBillVo> queryData(
			Map<String, Object> condition, int pageNo, int pageSize) {
		// TODO Auto-generated method stub
		return bizRepository.queryData(condition, pageNo, pageSize);
	}
	/**
	 * 批量修改业务数据
	 */
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
	public int updateBill(List<BizDispatchBillEntity> aCondition) {
		// TODO Auto-generated method stub
		return bizRepository.updateBill(aCondition);
	}
	
	/**
	 * 单个修改业务数据
	 */
	@Override
	public int updateBillEntity(BizDispatchBillEntity aCondition) {
		// TODO Auto-generated method stub
		return bizRepository.updateBillEntity(aCondition);
	}

	@Override	
	public PageInfo<BizDispatchBillEntity> queryAllPrice(
			Map<String, Object> condition, int pageNo, int pageSize) {
		// TODO Auto-generated method stub
		return bizRepository.queryAllPrice(condition, pageNo, pageSize);
	}

	@Override
	public PageInfo<BizDispatchBillEntity> queryAllData(
			Map<String, Object> condition, int pageNo, int pageSize) {
		// TODO Auto-generated method stub
		return bizRepository.queryAllData(condition, pageNo, pageSize);
	}
	
	@Override
	public PageInfo<BizDispatchBillEntity> queryAllCalculate(
			Map<String, Object> condition, int pageNo, int pageSize) {
		// TODO Auto-generated method stub
		return bizRepository.queryAllCalculate(condition, pageNo, pageSize);
	}
	

	@Override
	public Properties validRetry(Map<String, Object> param) {
		return bizRepository.validRetry(param);
	}

	@Override
	public int reCalculate(Map<String, Object> param) {
		return bizRepository.reCalculate(param);
	}

	@Override
	public int saveList(List<BizDispatchBillEntity> list) {
		// TODO Auto-generated method stub
		return bizRepository.saveList(list);
	}

	@Override
	public List<BizDispatchBillEntity> queryBizData(List<String> aCondition) {
		// TODO Auto-generated method stub
		return bizRepository.queryBizData(aCondition);
	}

	@Override
	public BizDispatchBillEntity queryExceptionOne(Map<String, Object> condition) {
		// TODO Auto-generated method stub
		return bizRepository.queryExceptionOne(condition);
	}

	@Override
	public int queryDispatch(Map<String, Object> param) {
		return bizRepository.queryDispatch(param);
	}

	@Override
	public int updateBatchWeight(List<Map<String, Object>> list) {
		return bizRepository.updateBatchWeight(list);
	}

	@Override
	public int adjustBillEntity(BizDispatchBillEntity temp) {
		return bizRepository.adjustBillEntity(temp);
	}

	@Override
	public int retryByWaybillNo(List<String> aCondition) {
		// TODO Auto-generated method stub
		return bizRepository.retryByWaybillNo(aCondition);
	}

	@Override
	public List<String> queryWayBillNo(Map<String, Object> condition) {
		// TODO Auto-generated method stub
		return bizRepository.queryWayBillNo(condition);
	}

	@Override
	public int retryByMaterialMark(Map<String, Object> condition) {
		// TODO Auto-generated method stub
		return bizRepository.retryByMaterialMark(condition);
	}
}