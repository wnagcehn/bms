/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.fees.storage.repository.impl;

import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;

import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;
import com.jiuyescm.bms.fees.storage.entity.FeesPayStorageEntity;
import com.jiuyescm.bms.fees.storage.repository.IFeesPayStorageRepository;

/**
 * 
 * @author stevenl
 * 
 */
@Repository("feesPayStorageRepository")
public class FeesPayStorageRepositoryImpl extends MyBatisDao<FeesPayStorageEntity> implements IFeesPayStorageRepository {

	private static final Logger logger = Logger.getLogger(FeesPayStorageRepositoryImpl.class.getName());

	public FeesPayStorageRepositoryImpl() {
		super();
	}
	
	@Override
    public PageInfo<FeesPayStorageEntity> query(Map<String, Object> condition, int pageNo, int pageSize) {
        List<FeesPayStorageEntity> list = selectList("com.jiuyescm.bms.fees.storage.FeesPayStorageEntityMapper.query", condition, new RowBounds(
                pageNo, pageSize));
        PageInfo<FeesPayStorageEntity> pageInfo = new PageInfo<FeesPayStorageEntity>(list);
        return pageInfo;
    }

    @Override
    public FeesPayStorageEntity findById(Long id) {
        FeesPayStorageEntity entity = selectOne("com.jiuyescm.bms.fees.storage.FeesPayStorageEntityMapper.findById", id);
        return entity;
    }

    @Override
    public FeesPayStorageEntity save(FeesPayStorageEntity entity) {
        insert("com.jiuyescm.bms.fees.storage.FeesPayStorageEntityMapper.save", entity);
        return entity;
    }

    @Override
    public FeesPayStorageEntity update(FeesPayStorageEntity entity) {
        update("com.jiuyescm.bms.fees.storage.FeesPayStorageEntityMapper.update", entity);
        return entity;
    }

    @Override
    public void delete(Long id) {
        delete("com.jiuyescm.bms.fees.storage.FeesPayStorageEntityMapper.delete", id);
    }
	
}
