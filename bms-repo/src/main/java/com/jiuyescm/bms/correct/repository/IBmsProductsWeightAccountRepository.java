package com.jiuyescm.bms.correct.repository;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.correct.BmsProductsWeightAccountEntity;

/**
 * ..Repository
 * @author wangchen
 * 
 */
public interface IBmsProductsWeightAccountRepository {
	
	PageInfo<BmsProductsWeightAccountEntity> query(Map<String, Object> condition,
		int pageNo, int pageSize);

	List<BmsProductsWeightAccountEntity> query(Map<String, Object> condition);

}
