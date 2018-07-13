package com.jiuyescm.bms.biz.storage.service.impl;

import java.util.Map;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.biz.storage.repository.IBizInStockHandWorkRepository;
import com.jiuyescm.bms.biz.storage.service.IBizInStockHandWorkService;
import com.jiuyescm.bms.biz.storage.vo.BizInstockHandworkVo;

/**
 * 入库单
 * 
 * @author yangshuaishuai
 *
 */
@Service("inStockHandWorkService")
public class BizInStockHandWorkServiceImpl implements IBizInStockHandWorkService{

	@Autowired
	private IBizInStockHandWorkRepository masterRepository;
	
	@Override
	public PageInfo<BizInstockHandworkVo> queryMaster(Map<String, Object> condition,int pageNo, int pageSize) {
		return masterRepository.query(condition, pageNo, pageSize);
	}
	
	@Override
	public int updateMaster(BizInstockHandworkVo entity) {
		return masterRepository.update(entity);
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
