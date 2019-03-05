package com.jiuyescm.bms.base.customer.repository.impl;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;
import com.jiuyescm.bms.base.customer.entity.PubCustomerLookupEntity;
import com.jiuyescm.bms.base.customer.repository.IPubCustomerLookupRepository;

/**
 * ..RepositoryImpl
 * @author liuzhicheng
 * 
 */
@Repository("pubCustomerLookupRepository")
public class PubCustomerLookupRepositoryImpl extends MyBatisDao<PubCustomerLookupEntity> implements IPubCustomerLookupRepository {
	@Override
	public PageInfo<PubCustomerLookupEntity> query(Map<String, Object> condition,
			int pageNo, int pageSize) {
        List<PubCustomerLookupEntity> list = selectList("com.jiuyescm.bms.base.customer.PubCustomerMapper.queryPageLookup", condition, new RowBounds(
                pageNo, pageSize));
        return new PageInfo<PubCustomerLookupEntity>(list);
	}
}