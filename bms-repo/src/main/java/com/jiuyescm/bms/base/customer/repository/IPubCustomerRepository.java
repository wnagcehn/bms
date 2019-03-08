package com.jiuyescm.bms.base.customer.repository;

import java.util.Map;
import java.util.List;
import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.base.customer.entity.PubCustomerEntity;

/**
 * ..Repository
 * @author liuzhicheng
 * 
 */
public interface IPubCustomerRepository {

    PubCustomerEntity findById(Long id);
	
	PageInfo<PubCustomerEntity> query(Map<String, Object> condition,
		int pageNo, int pageSize);

	List<PubCustomerEntity> query(Map<String, Object> condition);
	
	PageInfo<PubCustomerEntity> queryPage(Map<String, Object> condition,
			int pageNo, int pageSize);
}
