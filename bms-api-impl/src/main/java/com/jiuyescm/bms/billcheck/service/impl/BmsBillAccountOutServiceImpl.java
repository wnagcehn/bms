package com.jiuyescm.bms.billcheck.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.billcheck.BillAccountOutEntity;
import com.jiuyescm.bms.billcheck.BillCheckInfoEntity;
import com.jiuyescm.bms.billcheck.repository.IBillCheckInfoRepository;
import com.jiuyescm.bms.billcheck.service.IBmsAccountOutService;


@Service("bmsBillAccountOutService")
public class BmsBillAccountOutServiceImpl implements IBmsAccountOutService  {
	
	@Autowired
    private IBillCheckInfoRepository billCheckInfoRepository;

	@Override
	public PageInfo<BillCheckInfoEntity> query(Map<String, Object> condition,
			int pageNo, int pageSize) {
		return billCheckInfoRepository.query(condition, pageNo, pageSize);
	}

}
