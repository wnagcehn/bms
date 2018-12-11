package com.jiuyescm.bms.base.customer.repository;

import java.util.Map;
import java.util.List;
import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.base.customer.entity.PubCustomerBaseEntity;

/**
 * ..Repository
 * @author liuzhicheng
 * 
 */
public interface IPubCustomerBaseRepository {

    PubCustomerBaseEntity findById(Long id);
	
	PageInfo<PubCustomerBaseEntity> query(Map<String, Object> condition,
		int pageNo, int pageSize);

	List<PubCustomerBaseEntity> query(Map<String, Object> condition);
}
