
package com.jiuyescm.bms.biz.dispatch.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.biz.dispatch.entity.BizDispatchBillPayEntity;
import com.jiuyescm.bms.biz.dispatch.repository.IBizDispatchBillPayRepository;
import com.jiuyescm.bms.biz.dispatch.service.IBizDispatchBillPayService;

@Service("bizDispatchBillPayService")
public class BizDispatchBillPayServiceImp implements IBizDispatchBillPayService{

	@Resource
	private IBizDispatchBillPayRepository bizRepository;
	
	@Override
	public PageInfo<BizDispatchBillPayEntity> queryAll(
			Map<String, Object> condition, int pageNo, int pageSize) {
		// TODO Auto-generated method stub
		return bizRepository.queryAll(condition, pageNo, pageSize);
	}
	
	@Override
	public PageInfo<BizDispatchBillPayEntity> queryAllToExport(
			Map<String, Object> condition, int pageNo, int pageSize) {
		// TODO Auto-generated method stub
		return bizRepository.queryAllToExport(condition, pageNo, pageSize);
	}

	/**
	 * 批量修改业务数据
	 */
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
	public int updateBill(List<BizDispatchBillPayEntity> aCondition) {
		// TODO Auto-generated method stub
		return bizRepository.updateBill(aCondition);
	}
	
	/**
	 * 单个修改业务数据
	 */
	@Override
	public int updateBillEntity(BizDispatchBillPayEntity aCondition) {
		// TODO Auto-generated method stub
		return bizRepository.updateBillEntity(aCondition);
	}
	
	@Override	
	public PageInfo<BizDispatchBillPayEntity> queryAllPrice(
			Map<String, Object> condition, int pageNo, int pageSize) {
		// TODO Auto-generated method stub
		return bizRepository.queryAllPrice(condition, pageNo, pageSize);
	}

	@Override
	public PageInfo<BizDispatchBillPayEntity> queryAllData(
			Map<String, Object> condition, int pageNo, int pageSize) {
		// TODO Auto-generated method stub
		return bizRepository.queryAllData(condition, pageNo, pageSize);
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
	public int saveList(List<BizDispatchBillPayEntity> list) {
		// TODO Auto-generated method stub
		return bizRepository.saveList(list);
	}

	@Override
	public List<BizDispatchBillPayEntity> queryBizData(List<String> aCondition) {
		// TODO Auto-generated method stub
		return bizRepository.queryBizData(aCondition);
	}

	
	@Override
	public int queryDispatch(Map<String, Object> param) {
		return  bizRepository.queryDispatch(param);
	}

	@Override
	public int updateBatchWeight(List<Map<String, Object>> list) {
		return bizRepository.updateBatchWeight(list);
	}

	@Override
	public BizDispatchBillPayEntity queryExceptionOne(Map<String,Object> condition) {
		// TODO Auto-generated method stub
		return bizRepository.queryExceptionOne(condition);
	}

	@Override
	public int adjustBillPayEntity(BizDispatchBillPayEntity temp) {
		return bizRepository.adjustBillPayEntity(temp);
	}
	
}