package com.jiuyescm.bms.biz.service.impl;

import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyescm.bms.biz.api.IOutstockRecordService;
import com.jiuyescm.bms.biz.entity.BmsOutstockRecordEntity;
import com.jiuyescm.bms.biz.repo.IOutstockRecordRepository;
import com.jiuyescm.bms.biz.vo.OutstockInfoVo;

@Service("outstockRecordService")
public class OutstockRecordServiceImpl implements IOutstockRecordService{

	private static final Logger logger = LoggerFactory.getLogger(OutstockInfoServiceImpl.class.getName());

	@Autowired 
	private IOutstockRecordRepository repository;
	
	@Override
	public int insert(OutstockInfoVo vo) {
		// TODO Auto-generated method stub
		BmsOutstockRecordEntity record=new BmsOutstockRecordEntity();
		try {    		
            PropertyUtils.copyProperties(record, vo);
            	
		} catch (Exception ex) {
        	logger.error("转换失败:{0}",ex);
        }
		
		return repository.insert(record);
	}

}
