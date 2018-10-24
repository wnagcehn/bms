package com.jiuyescm.bms.fees.abnormal.service.impl;

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

}
