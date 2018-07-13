/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.biz.storage.repository.impl;

import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;

import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;
import com.jiuyescm.bms.biz.storage.entity.BizBaseFeeEntity;
import com.jiuyescm.bms.biz.storage.repository.IBizBaseFeeRepository;

/**
 * 
 * @author stevenl
 * 
 */
@Repository("bizBaseFeeRepository")
public class BizBaseFeeRepositoryImpl extends MyBatisDao<BizBaseFeeEntity> implements IBizBaseFeeRepository {

	private static final Logger logger = Logger.getLogger(BizBaseFeeRepositoryImpl.class.getName());

	public BizBaseFeeRepositoryImpl() {
		super();
	}
	
	@Override
    public PageInfo<BizBaseFeeEntity> query(Map<String, Object> condition, int pageNo, int pageSize) {
        List<BizBaseFeeEntity> list = selectList("com.jiuyescm.bms.biz.storage.BizBaseFeeEntityMapper.query", condition, new RowBounds(
                pageNo, pageSize));
        PageInfo<BizBaseFeeEntity> pageInfo = new PageInfo<BizBaseFeeEntity>(list);
        return pageInfo;
    }

    @Override
    public BizBaseFeeEntity findById(Long id) {
        BizBaseFeeEntity entity = selectOne("com.jiuyescm.bms.biz.storage.BizBaseFeeEntityMapper.findById", id);
        return entity;
    }

    @Override
    public BizBaseFeeEntity save(BizBaseFeeEntity entity) {
        insert("com.jiuyescm.bms.biz.storage.BizBaseFeeEntityMapper.save", entity);
        return entity;
    }

    @Override
    public BizBaseFeeEntity update(BizBaseFeeEntity entity) {
        update("com.jiuyescm.bms.biz.storage.BizBaseFeeEntityMapper.update", entity);
        return entity;
    }

    @Override
    public void delete(Long id) {
        delete("com.jiuyescm.bms.biz.storage.BizBaseFeeEntityMapper.delete", id);
    }
	
}
