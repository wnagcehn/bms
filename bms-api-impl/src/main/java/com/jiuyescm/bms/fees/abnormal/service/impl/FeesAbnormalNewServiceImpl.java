package com.jiuyescm.bms.fees.abnormal.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.fees.abnormal.entity.FeesAbnormalEntity;
import com.jiuyescm.bms.fees.abnormal.repository.IFeesAbnormalNewRepository;
import com.jiuyescm.bms.fees.abnormal.service.IFeesAbnormalNewService;

@Service("feesAbnormalNewService")
public class FeesAbnormalNewServiceImpl implements IFeesAbnormalNewService{

	@Autowired
    private IFeesAbnormalNewRepository feesAbnormalNewRepository;
	
	@Override
	public PageInfo<FeesAbnormalEntity> query(Map<String, Object> condition,
			int pageNo, int pageSize) {
		// TODO Auto-generated method stub
		return feesAbnormalNewRepository.query(condition, pageNo, pageSize);
	}

	@Override
	public PageInfo<FeesAbnormalEntity> queryPay(Map<String, Object> condition,
			int pageNo, int pageSize) {
		// TODO Auto-generated method stub
		return feesAbnormalNewRepository.queryPay(condition, pageNo, pageSize);
	}

	@Override
	public FeesAbnormalEntity queryOne(Map<String, Object> condition) {
		// TODO Auto-generated method stub
		return feesAbnormalNewRepository.queryOne(condition);
	}

	@Override
	public int updateList(List<FeesAbnormalEntity> list) {
		// TODO Auto-generated method stub
		return feesAbnormalNewRepository.updateList(list);
	}

	@Override
	public int updateOne(FeesAbnormalEntity entity) {
		// TODO Auto-generated method stub
		return feesAbnormalNewRepository.updateOne(entity);
	}

	@Override
	public PageInfo<FeesAbnormalEntity> queryCount(
			Map<String, Object> condition, int pageNo, int pageSize) {
		// TODO Auto-generated method stub
		return feesAbnormalNewRepository.queryCount(condition, pageNo, pageSize);
	}

}
