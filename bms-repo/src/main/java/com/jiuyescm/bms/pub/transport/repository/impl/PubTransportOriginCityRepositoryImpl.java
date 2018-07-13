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
import com.jiuyescm.bms.pub.transport.entity.PubTransportOriginCityEntity;
import com.jiuyescm.bms.pub.transport.repository.IPubTransportOriginCityRepository;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;

/**
 * 运输始发城市信息
 * @author yangss
 *
 */
@Repository("pubTransportOriginCityRepository")
public class PubTransportOriginCityRepositoryImpl extends MyBatisDao<PubTransportOriginCityEntity> implements IPubTransportOriginCityRepository {

	private static final Logger logger = Logger.getLogger(PubTransportOriginCityRepositoryImpl.class.getName());
	
	@Override
    public PageInfo<PubTransportOriginCityEntity> query(Map<String, Object> condition, int pageNo, int pageSize) {
        List<PubTransportOriginCityEntity> list = selectList("com.jiuyescm.bms.pub.transport.mapper.PubTransportOriginCityMapper.query", condition, new RowBounds(
                pageNo, pageSize));
        PageInfo<PubTransportOriginCityEntity> pageInfo = new PageInfo<PubTransportOriginCityEntity>(list);
        return pageInfo;
    }

	@Override
	public List<PubTransportOriginCityEntity> queryList(Map<String, Object> condition) {
		return selectList("com.jiuyescm.bms.pub.transport.mapper.PubTransportOriginCityMapper.query", condition);
	}

    @Override
    public PubTransportOriginCityEntity findById(Long id) {
        PubTransportOriginCityEntity entity = selectOne("com.jiuyescm.bms.pub.transport.mapper.PubTransportOriginCityMapper.findById", id);
        return entity;
    }

    @Override
    public PubTransportOriginCityEntity save(PubTransportOriginCityEntity entity) {
        insert("com.jiuyescm.bms.pub.transport.mapper.PubTransportOriginCityMapper.save", entity);
        return entity;
    }

    @Override
    public PubTransportOriginCityEntity update(PubTransportOriginCityEntity entity) {
        update("com.jiuyescm.bms.pub.transport.mapper.PubTransportOriginCityMapper.update", entity);
        return entity;
    }

    @Override
    public void delete(Long id) {
        delete("com.jiuyescm.bms.pub.transport.mapper.PubTransportOriginCityMapper.delete", id);
    }
	
}
