package com.jiuyescm.bms.fees.dispatch.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.bill.receive.repository.IBmsBillSubjectInfoRepository;
import com.jiuyescm.bms.common.enumtype.BillFeesSubjectStatusEnum;
import com.jiuyescm.bms.fees.dispatch.entity.FeesReceiveDispatchEntity;
import com.jiuyescm.bms.fees.dispatch.repository.IFeesReceiveDispatchRepository;
import com.jiuyescm.bms.fees.dispatch.service.IFeesReceiveDispatchService;
import com.jiuyescm.bms.fees.dispatch.vo.FeesReceiveDispatchVo;

/**
 * 应收费用-配送费 接口实现类
 * 
 * @author yangshuaishuai
 * 
 */
@Service("feesReceiveDispatchService")
public class FeesReceiveDispatchServiceImpl implements IFeesReceiveDispatchService {

	@Autowired
	private IFeesReceiveDispatchRepository repository;
	@Autowired
    private IBmsBillSubjectInfoRepository bmsBillSubjectInfoRepository;

	@Override
	public PageInfo<FeesReceiveDispatchEntity> query(Map<String, Object> condition, int pageNo, int pageSize) {
		return repository.query(condition, pageNo, pageSize);
	}

	@Override
	public List<FeesReceiveDispatchEntity> queryList(Map<String, Object> condition) {
		return repository.queryList(condition);
	}

	@Override
	public int updateBatchTmp(List<FeesReceiveDispatchEntity> list) {
		return repository.updateBatchTmp(list);
	}

	@Override
	public FeesReceiveDispatchEntity queryById(Long id) {
		return repository.queryById(id);
	}

	@Override
	public int insertBatchTmp(List<FeesReceiveDispatchEntity> list) {
		return repository.insertBatchTmp(list);
	}

	@Override
	public int insertOne(FeesReceiveDispatchEntity entity) {
		return repository.insertOne(entity);
	}

	@Override
	public List<FeesReceiveDispatchEntity> queryDispatchByBillNo(String billno) {
		return repository.querydistributionDetailByBillNo(billno);
	}

	@Override
	public List<FeesReceiveDispatchEntity> query(Map<String, Object> condition) {
		return repository.queryList(condition);
	}

	@Override
	public FeesReceiveDispatchEntity queryOne(Map<String, Object> condition) {
		// TODO Auto-generated method stub
		return repository.queryOne(condition);
	}

	@Override
	public PageInfo<FeesReceiveDispatchVo> query1(Map<String, Object> condition, int pageNo, int pageSize) {
		return repository.query1(condition, pageNo, pageSize);
	}

	@Override
	public PageInfo<FeesReceiveDispatchEntity> queryDispatchPage(
			Map<String, Object> parameter, int pageNo, int pageSize) {
		
		return repository.queryDispatchPage(parameter,pageNo,pageSize);
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
	public void derateBatchAmount(List<FeesReceiveDispatchEntity> list) {
		if(list!=null&&list.size()>0){
			FeesReceiveDispatchEntity entity=list.get(0);
			//批量更新费用表减免费用
			repository.derateBatchAmount(list);
			String status=BillFeesSubjectStatusEnum.UPDATE.getCode();
			//更新账单明细状态为 已更新
			bmsBillSubjectInfoRepository.updateStatus(entity.getBillNo(),entity.getWarehouseCode(),entity.getCarrierid(),status);
		}
		
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
	public void deleteBatchFees(List<FeesReceiveDispatchEntity> list) {
		if(list!=null&&list.size()>0){
			FeesReceiveDispatchEntity entity=list.get(0);
			//批量更新费用表减免费用
			repository.deleteBatchFees(list);
			String status=BillFeesSubjectStatusEnum.UPDATE.getCode();
			//更新账单明细状态为 已更新
			bmsBillSubjectInfoRepository.updateStatus(entity.getBillNo(),entity.getWarehouseCode(),entity.getCarrierid(),status);
		}
	}

	@Override
	public List<FeesReceiveDispatchEntity> queryAllByBillSubject(
			Map<String, Object> parameter) {
		
		return repository.queryAllByBillSubjectInfoMap(parameter);
	}

	@Override
	public PageInfo<FeesReceiveDispatchVo> queryFeesImport(Map<String, Object> condition, int pageNo, int pageSize) {
		return repository.queryFeesImport(condition, pageNo, pageSize);
	}
	/**
	 * jira BMS-383
	 */
	@Override
	public List<Map<String, Object>> queryGroup(Map<String, Object> param) {
		return repository.queryGroup(param);
	}

	@Override
	public PageInfo<FeesReceiveDispatchEntity> querydistributionDetailByBizData(
			Map<String, Object> condition,int pageNo, int pageSize) {
		return repository.querydistributionDetailByBizData(condition,pageNo,pageSize);
	}
}
