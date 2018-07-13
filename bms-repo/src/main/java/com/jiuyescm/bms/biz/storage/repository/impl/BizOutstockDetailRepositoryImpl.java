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
import com.jiuyescm.bms.biz.storage.entity.BizOutstockDetailEntity;
import com.jiuyescm.bms.biz.storage.repository.IBizOutstockDetailRepository;

/**
 * 
 * @author stevenl
 * 
 */
@Repository("bizOutstockDetailRepository")
public class BizOutstockDetailRepositoryImpl extends MyBatisDao<BizOutstockDetailEntity> implements IBizOutstockDetailRepository {

	private static final Logger logger = Logger.getLogger(BizOutstockDetailRepositoryImpl.class.getName());

	public BizOutstockDetailRepositoryImpl() {
		super();
	}
	
	@Override
    public PageInfo<BizOutstockDetailEntity> query(Map<String, Object> condition, int pageNo, int pageSize) {
        List<BizOutstockDetailEntity> list = selectList("com.jiuyescm.bms.biz.storage.BizOutstockDetailEntityMapper.query", condition, new RowBounds(
                pageNo, pageSize));
        PageInfo<BizOutstockDetailEntity> pageInfo = new PageInfo<BizOutstockDetailEntity>(list);
        return pageInfo;
    }

    @Override
    public BizOutstockDetailEntity findById(Long id) {
        BizOutstockDetailEntity entity = selectOne("com.jiuyescm.bms.biz.storage.BizOutstockDetailEntityMapper.findById", id);
        return entity;
    }

    @Override
    public BizOutstockDetailEntity save(BizOutstockDetailEntity entity) {
        insert("com.jiuyescm.bms.biz.storage.BizOutstockDetailEntityMapper.save", entity);
        return entity;
    }

    @Override
    public int update(BizOutstockDetailEntity entity) {
       return update("com.jiuyescm.bms.biz.storage.BizOutstockDetailEntityMapper.update", entity);
    }

    @Override
    public void delete(Long id) {
        delete("com.jiuyescm.bms.biz.storage.BizOutstockDetailEntityMapper.delete", id);
    }
	
}
