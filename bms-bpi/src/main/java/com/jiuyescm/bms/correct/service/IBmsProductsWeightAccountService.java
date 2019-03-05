package com.jiuyescm.bms.correct.service;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.correct.BmsProductsWeightAccountEntity;
import com.jiuyescm.bms.correct.vo.BmsProductsWeightAccountVo;

/**
 * ..Service
 * @author wangchen
 * 
 */
public interface IBmsProductsWeightAccountService {
	
    PageInfo<BmsProductsWeightAccountVo> query(Map<String, Object> condition, int pageNo,
            int pageSize) throws Exception;

	List<BmsProductsWeightAccountEntity> query(Map<String, Object> condition);

}
