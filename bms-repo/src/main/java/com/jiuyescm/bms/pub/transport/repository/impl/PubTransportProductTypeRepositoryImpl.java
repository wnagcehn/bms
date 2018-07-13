/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.pub.transport.repository.impl;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.pub.transport.entity.PubTransportProductTypeEntity;
import com.jiuyescm.bms.pub.transport.repository.IPubTransportProductTypeRepository;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;

/**
 * 运输产品类型信息
 * @author yangss
 *
 */
@Repository("pubTransportProductTypeRepository")
public class PubTransportProductTypeRepositoryImpl extends MyBatisDao<PubTransportProductTypeEntity> implements IPubTransportProductTypeRepository {

	private static final Logger logger = Logger.getLogger(PubTransportProductTypeRepositoryImpl.class.getName());
	
	@Override
    public PageInfo<PubTransportProductTypeEntity> query(Map<String, Object> condition, int pageNo, int pageSize) {
        List<PubTransportProductTypeEntity> list = selectList("com.jiuyescm.bms.pub.transport.mapper.PubTransportProductTypeMapper.query", condition, new RowBounds(
                pageNo, pageSize));
        PageInfo<PubTransportProductTypeEntity> pageInfo = new PageInfo<PubTransportProductTypeEntity>(list);
        return pageInfo;
    }
	
	@Override
	public List<PubTransportProductTypeEntity> query(Map<String, Object> condition) {
		return selectList("com.jiuyescm.bms.pub.transport.mapper.PubTransportProductTypeMapper.query", condition);
	}

    @Override
    public PubTransportProductTypeEntity findById(Long id) {
        PubTransportProductTypeEntity entity = selectOne("com.jiuyescm.bms.pub.transport.mapper.PubTransportProductTypeMapper.findById", id);
        return entity;
    }

    @Override
    public PubTransportProductTypeEntity save(PubTransportProductTypeEntity entity) {
        insert("com.jiuyescm.bms.pub.transport.mapper.PubTransportProductTypeMapper.save", entity);
        return entity;
    }

    @Override
    public PubTransportProductTypeEntity update(PubTransportProductTypeEntity entity) {
        update("com.jiuyescm.bms.pub.transport.mapper.PubTransportProductTypeMapper.update", entity);
        return entity;
    }

    @Override
    public void delete(Long id) {
        delete("com.jiuyescm.bms.pub.transport.mapper.PubTransportProductTypeMapper.delete", id);
    }
	
}
