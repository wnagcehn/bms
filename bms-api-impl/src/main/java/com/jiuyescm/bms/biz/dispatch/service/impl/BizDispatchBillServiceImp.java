
package com.jiuyescm.bms.biz.dispatch.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;

import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.biz.dispatch.entity.BizDispatchBillEntity;
import com.jiuyescm.bms.biz.dispatch.repository.IBizDispatchBillRepository;
import com.jiuyescm.bms.biz.dispatch.repository.imp.BizDispatchBillRepositoryImp;
import com.jiuyescm.bms.biz.dispatch.service.IBizDispatchBillService;
import com.jiuyescm.bms.biz.dispatch.vo.BizDispatchBillVo;
import com.jiuyescm.bms.biz.entity.BmsOutstockRecordEntity;
import com.jiuyescm.bms.biz.repo.IOutstockRecordRepository;
import com.jiuyescm.bms.biz.service.impl.OutstockInfoServiceImpl;
import com.jiuyescm.bms.fees.dispatch.repository.IFeesReceiveDispatchRepository;
import com.jiuyescm.exception.BizException;

@Service("bizDispatchBillService")
public class BizDispatchBillServiceImp implements IBizDispatchBillService{
	
	private static final Logger logger = LoggerFactory.getLogger(BizDispatchBillServiceImp.class.getName());
	
	@Resource
	private IBizDispatchBillRepository bizRepository;
	
	@Autowired 
	private IOutstockRecordRepository outstockRecordrepository;
	
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
	public BizDispatchBillEntity queryByWayNo(Map<String, String> condition) {
		return bizRepository.queryByWayNo(condition);
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

	@Transactional(readOnly = false, propagation=Propagation.REQUIRED)
	@Override
	public int updateBatchWeight(List<Map<String, Object>> list) {
		try {
			bizRepository.updateBatchWeight(list);
			bizRepository.updateIsCalcuByWaybillNo(list);
		} catch (Exception e) {
			logger.error("更新异常!", e);
			throw new BizException("更新异常!", e);
		}
		return 1;
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = { BizException.class })
	@Override
	public int adjustBillEntity(BizDispatchBillEntity temp) {
		
		//1:写入到记录表
		BmsOutstockRecordEntity record=new BmsOutstockRecordEntity();
		try {
			PropertyUtils.copyProperties(record, temp);
			record.setAdjustReceiveProvince(temp.getAdjustProvinceId());
			record.setAdjustReceiveCity(temp.getAdjustCityId());
			record.setAdjustReceiveArea(temp.getAdjustDistrictId());
			record.setAdjustCarrierId(temp.getAdjustCarrierId());
			record.setAdjustSendProvince(temp.getSendProvinceId());
			record.setAdjustSendCity(temp.getSendCityId());
			outstockRecordrepository.insert(record);
		} catch (Exception e) {
			logger.error("转换为记录实体类失败:{0}",e);
		}
		//2:更新主表和费用表
		Map<String, Object> map = new HashMap<String, Object>();
    	map.put("feesNo", temp.getFeesNo());
    	try {
    		bizRepository.adjustBillEntity(temp);
    		bizRepository.updateIsCalcuByFeesNo(map);
		} catch (Exception e) {
			logger.error("更新失败!", e);
			return 0;
		}
		return 1;	
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

	@Override
	public List<BizDispatchBillEntity> queryNotCalculate(
			Map<String, Object> condition) {
		// TODO Auto-generated method stub
		return bizRepository.queryNotCalculate(condition);
	}
}