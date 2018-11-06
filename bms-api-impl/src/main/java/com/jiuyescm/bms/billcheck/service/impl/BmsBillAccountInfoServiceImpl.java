/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.billcheck.service.impl;

import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.billcheck.BillAccountInfoEntity;
import com.jiuyescm.bms.billcheck.repository.IBillAccountInfoRepository;
import com.jiuyescm.bms.billcheck.service.IBmsAccountInfoService;
import com.jiuyescm.bms.biz.dispatch.repository.IBizDispatchBillPayRepository;

/**
 * 
 * @author stevenl
 * 
 */
@Service("billAccountInfoService")
public class BmsBillAccountInfoServiceImpl implements IBmsAccountInfoService {

	private static final Logger logger = Logger.getLogger(BmsBillAccountInfoServiceImpl.class.getName());

	@Resource
	private IBillAccountInfoRepository billAccountInfoRepository;
	
	@Override
	public PageInfo<BillAccountInfoEntity> query(Map<String, Object> condition,
			int pageNo, int pageSize) {
		// TODO Auto-generated method stub
		return billAccountInfoRepository.query(condition, pageNo, pageSize);
	}

	@Override
	public BillAccountInfoEntity findByCustomerId(String customerId) {
		// TODO Auto-generated method stub
		return billAccountInfoRepository.findByCustomerId(customerId);
	}

	@Override
	public BillAccountInfoEntity save(BillAccountInfoEntity entity) {
		// TODO Auto-generated method stub
		return billAccountInfoRepository.save(entity);
	}

	@Override
	public BillAccountInfoEntity update(BillAccountInfoEntity entity) {
		// TODO Auto-generated method stub
		 return billAccountInfoRepository.update(entity);
	}

	@Override
	public void delete(Long id) {
		// TODO Auto-generated method stub
		
	}
	
	
	
}
