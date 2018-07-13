package com.jiuyescm.bms.fees.out.dispatch.service.imp;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.fees.out.dispatch.entity.FeesPayDispatchEntity;
import com.jiuyescm.bms.fees.out.dispatch.repository.IFeesPayDispatchRepository;
import com.jiuyescm.bms.fees.out.dispatch.service.IFeesPayDispatchService;

/**
 * 应收费用-配送费 接口实现类
 * 
 * @author yangshuaishuai
 * 
 */
@Service("feesPayDispatchService")
public class FeesPayDispatchServiceImpl implements IFeesPayDispatchService {

	@Autowired
	private IFeesPayDispatchRepository repository;

	@Override
	public PageInfo<FeesPayDispatchEntity> query(
			Map<String, Object> condition, int pageNo, int pageSize) {
		return repository.query(condition, pageNo, pageSize);
	}

	@Override
	public List<FeesPayDispatchEntity> queryList(Map<String, Object> condition) {
		return repository.queryList(condition);
	}

	@Override
	public List<FeesPayDispatchEntity> queryDispatchByBillNo(String billno) {
		return repository.queryDispatchDetailByBillNo(billno);
	}

	@Override
	public FeesPayDispatchEntity queryOne(Map<String, Object> condition) {
		return repository.queryOne(condition);
	}

	@Override
	public boolean Insert(FeesPayDispatchEntity entity) {
		return repository.Insert(entity);
	}
	
	@Override
	public int insertBatchTmp(List<FeesPayDispatchEntity> list) {
		return repository.insertBatchTmp(list);
	}

	@Override
	public int deleteFeesByMap(Map<String, Object> condition) {
		return repository.deleteFeesByMap(condition);
	}
	
	@Override
	public int deleteFeesBillAbnormal(Map<String, Object> condition) {
		return repository.deleteFeesBillAbnormal(condition);
	}

	@Override
	public boolean InsertOne(FeesPayDispatchEntity entity) {
		return repository.InsertOne(entity);
	}
	
	
}
