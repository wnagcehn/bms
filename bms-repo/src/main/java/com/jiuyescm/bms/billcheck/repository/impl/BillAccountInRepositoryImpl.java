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
import com.jiuyescm.bms.billcheck.BillAccountInEntity;
import com.jiuyescm.bms.billcheck.repository.IBillAccountInRepository;

/**
 * 
 * @author chenwenxin
 * 
 */
@Repository("billAccountInRepository")
public class BillAccountInRepositoryImpl extends MyBatisDao<BillAccountInEntity> implements IBillAccountInRepository {

	private static final Logger logger = Logger.getLogger(BillAccountInRepositoryImpl.class.getName());

	public BillAccountInRepositoryImpl() {
		super();
	}
	
	@Override
    public PageInfo<BillAccountInEntity> query(Map<String, Object> condition, int pageNo, int pageSize) {
        List<BillAccountInEntity> list = selectList("com.jiuyescm.bms.billcheck.BillAccountInMapper.query", condition, new RowBounds(
                pageNo, pageSize));
        PageInfo<BillAccountInEntity> pageInfo = new PageInfo<BillAccountInEntity>(list);
        return pageInfo;
    }

    @Override
    public BillAccountInEntity findById(Long id) {
        BillAccountInEntity entity = selectOne("com.jiuyescm.bms.billcheck.BillAccountInMapper.findById", id);
        return entity;
    }

    @Override
    public BillAccountInEntity save(BillAccountInEntity entity) {
        insert("com.jiuyescm.bms.billcheck.BillAccountInMapper.save", entity);
        return entity;
    }

    @Override
    public BillAccountInEntity update(BillAccountInEntity entity) {
        update("com.jiuyescm.bms.billcheck.BillAccountInMapper.update", entity);
        return entity;
    }

    @Override
    public void delete(Long id) {
        delete("com.jiuyescm.bms.billcheck.BillAccountInMapper.delete", id);
    }
	
}
