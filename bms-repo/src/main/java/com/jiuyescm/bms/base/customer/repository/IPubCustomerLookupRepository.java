package com.jiuyescm.bms.base.customer.repository;

import java.util.Map;
import java.util.List;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.base.customer.entity.PubCustomerEntity;
import com.jiuyescm.bms.base.customer.entity.PubCustomerLookupEntity;

/**
 * ..Repository
 * @author liuzhicheng
 * 
 */
public interface IPubCustomerLookupRepository {
	
	PageInfo<PubCustomerLookupEntity> query(Map<String, Object> condition,
			int pageNo, int pageSize);
}
