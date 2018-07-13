package com.jiuyescm.bms.biz.storage.service.impl;

import java.util.Map;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.biz.storage.entity.BizInStockDetailEntity;
import com.jiuyescm.bms.biz.storage.entity.BizInStockMasterEntity;
import com.jiuyescm.bms.biz.storage.repository.IBizInStockDetailRepository;
import com.jiuyescm.bms.biz.storage.repository.IBizInStockWorkRepository;
import com.jiuyescm.bms.biz.storage.service.IBizInStockWorkService;

/**
 * 入库单
 * 
 * @author yangshuaishuai
 *
 */
@Service("inStockWorkService")
public class BizInStockWorkServiceImpl implements IBizInStockWorkService{

	@Autowired
	private IBizInStockWorkRepository masterRepository;
	@Autowired
	private IBizInStockDetailRepository detailRepository;
	
	@Override
	public PageInfo<BizInStockMasterEntity> queryMaster(Map<String, Object> condition,int pageNo, int pageSize) {
		return masterRepository.query(condition, pageNo, pageSize);
	}
	
	@Override
	public int updateMaster(BizInStockMasterEntity entity) {
		return masterRepository.update(entity);
	}

	@Override
	public PageInfo<BizInStockDetailEntity> queryDetail(Map<String, Object> condition, int pageNo, int pageSize) {
		return detailRepository.query(condition, pageNo, pageSize);
	}

	@Override
	public Properties validRetry(Map<String, Object> param) {
		return masterRepository.validRetry(param);
	}

	@Override
	public int reCalculate(Map<String, Object> param) {
		return masterRepository.reCalculate(param);
	}

}
