/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.billcheck.repository.impl;

import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;

import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;
import com.jiuyescm.bms.billcheck.BillAccountInfoEntity;
import com.jiuyescm.bms.billcheck.repository.IBillAccountInfoRepository;

/**
 * 
 * @author stevenl
 * 
 */
@Repository("billAccountInfoRepository")
public class BillAccountInfoRepositoryImpl extends MyBatisDao<BillAccountInfoEntity> implements IBillAccountInfoRepository {

	private static final Logger logger = Logger.getLogger(BillAccountInfoRepositoryImpl.class.getName());

	public BillAccountInfoRepositoryImpl() {
		super();
	}
	
	@Override
    public PageInfo<BillAccountInfoEntity> query(Map<String, Object> condition, int pageNo, int pageSize) {
        List<BillAccountInfoEntity> list = selectList("com.jiuyescm.bms.billcheck.BillAccountInfoMapper.query", condition, new RowBounds(
                pageNo, pageSize));
        PageInfo<BillAccountInfoEntity> pageInfo = new PageInfo<BillAccountInfoEntity>(list);
        return pageInfo;
    }

    @Override
    public BillAccountInfoEntity findByCustomerId(String customerId) {
        BillAccountInfoEntity entity = selectOne("com.jiuyescm.bms.billcheck.BillAccountInfoEntityMapper.findByCustomerId", customerId);
        return entity;
    }

    @Override
    public BillAccountInfoEntity save(BillAccountInfoEntity entity) {
        insert("com.jiuyescm.bms.billcheck.BillAccountInfoEntityMapper.save", entity);
        return entity;
    }

    @Override
    public BillAccountInfoEntity update(BillAccountInfoEntity entity) {
        update("com.jiuyescm.bms.billcheck.BillAccountInfoEntityMapper.update", entity);
        return entity;
    }

    @Override
    public void delete(Long id) {
        delete("com.jiuyescm.bms.billcheck.BillAccountInfoEntityMapper.delete", id);
    }
	
}
