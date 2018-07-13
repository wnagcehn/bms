/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.quotation.out.transport.repository.impl;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.quotation.out.transport.entity.PriceTransportPayValueAddedEntity;
import com.jiuyescm.bms.quotation.out.transport.repository.IPriceTransportPayValueAddedRepository;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;

/**
 * 
 * @author wubangjun
 * 
 */
@Repository("priceTransportPayValueAddedRepository")
public class PriceTransportPayValueAddedRepositoryImpl extends MyBatisDao<PriceTransportPayValueAddedEntity> implements IPriceTransportPayValueAddedRepository {

	private static final Logger logger = Logger.getLogger(PriceTransportPayValueAddedRepositoryImpl.class.getName());

	public PriceTransportPayValueAddedRepositoryImpl() {
		super();
	}
	
	@Override
    public PageInfo<PriceTransportPayValueAddedEntity> query(Map<String, Object> condition, int pageNo, int pageSize) {
        List<PriceTransportPayValueAddedEntity> list = 
        		selectList("com.jiuyescm.bms.quotation.out.transport.mapper.PriceTransportPayValueAddedEntityMapper.query",
        		condition, new RowBounds(pageNo, pageSize));
        PageInfo<PriceTransportPayValueAddedEntity> pageInfo = new PageInfo<PriceTransportPayValueAddedEntity>(list);
        return pageInfo;
    }
	
	@Override
    public List<PriceTransportPayValueAddedEntity> query(Map<String, Object> condition) {
        List<PriceTransportPayValueAddedEntity> list = selectList("com.jiuyescm.bms.quotation.out.transport.mapper.PriceTransportPayValueAddedEntityMapper.query", condition);
        return list;
    }

    @Override
    public PriceTransportPayValueAddedEntity findById(Long id) {
    	PriceTransportPayValueAddedEntity entity = selectOne("com.jiuyescm.bms.quotation.out.transport.mapper.PriceTransportPayValueAddedEntityMapper.findById", id);
        return entity;
    }

    @Override
    public PriceTransportPayValueAddedEntity save(PriceTransportPayValueAddedEntity entity) {
        insert("com.jiuyescm.bms.quotation.out.transport.mapper.PriceTransportPayValueAddedEntityMapper.save", entity);
        return entity;
    }

    @Override
    public PriceTransportPayValueAddedEntity update(PriceTransportPayValueAddedEntity entity) {
        update("com.jiuyescm.bms.quotation.out.transport.mapper.PriceTransportPayValueAddedEntityMapper.update", entity);
        return entity;
    }

    @Override
    public void delete(Long id) {
        delete("com.jiuyescm.bms.quotation.out.transport.mapper.PriceTransportPayValueAddedEntityMapper.delete", id);
    }

	@Override
	public int saveList(List<PriceTransportPayValueAddedEntity> valueAddedList) {
		// TODO Auto-generated method stub
		return insertBatch("com.jiuyescm.bms.quotation.out.transport.mapper.PriceTransportPayValueAddedEntityMapper.save", valueAddedList);
	}
	
}
