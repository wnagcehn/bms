package com.jiuyescm.bms.correct.repository;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.correct.BmsProductsMaterialAccountEntity;

/**
 * ..Repository
 * @author wangchen
 * 
 */
public interface IBmsProductsMaterialAccountRepository {
	
	PageInfo<BmsProductsMaterialAccountEntity> query(Map<String, Object> condition,
		int pageNo, int pageSize);

	List<BmsProductsMaterialAccountEntity> query(Map<String, Object> condition);

}
