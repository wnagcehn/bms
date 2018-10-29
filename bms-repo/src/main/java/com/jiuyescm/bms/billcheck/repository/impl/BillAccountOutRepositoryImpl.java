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
import com.jiuyescm.bms.billcheck.BillAccountOutEntity;
import com.jiuyescm.bms.billcheck.repository.IBillAccountOutRepository;


/**
 * 
 * @author liuzhicheng
 * 
 */
@Repository("billAccountOutRepository")
public class BillAccountOutRepositoryImpl extends MyBatisDao<BillAccountOutEntity> implements IBillAccountOutRepository {

	private static final Logger logger = Logger.getLogger(BillAccountOutRepositoryImpl.class.getName());

	public BillAccountOutRepositoryImpl() {
		super();
	}
	
	@Override
    public PageInfo<BillAccountOutEntity> query(Map<String, Object> condition, int pageNo, int pageSize) {
        List<BillAccountOutEntity> list = selectList("com.jiuyescm.bms.billcheck.BillAccountOutEntityMapper.query", condition, new RowBounds(
                pageNo, pageSize));
        PageInfo<BillAccountOutEntity> pageInfo = new PageInfo<BillAccountOutEntity>(list);
        return pageInfo;
    }

    @Override
    public BillAccountOutEntity findById(Long id) {
        BillAccountOutEntity entity = selectOne("com.jiuyescm.bms.entity.BillAccountOutEntityMapper.findById", id);
        return entity;
    }

    @Override
    public BillAccountOutEntity save(BillAccountOutEntity entity) {
        insert("com.jiuyescm.bms.entity.BillAccountOutEntityMapper.save", entity);
        return entity;
    }

    @Override
    public BillAccountOutEntity update(BillAccountOutEntity entity) {
        update("com.jiuyescm.bms.entity.BillAccountOutEntityMapper.update", entity);
        return entity;
    }

    @Override
    public void delete(Long id) {
        delete("com.jiuyescm.bms.entity.BillAccountOutEntityMapper.delete", id);
    }
	
}
