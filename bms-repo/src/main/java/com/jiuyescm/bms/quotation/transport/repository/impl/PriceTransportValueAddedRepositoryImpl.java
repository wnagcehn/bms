/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.quotation.transport.repository.impl;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.quotation.transport.entity.PriceTransportValueAddedEntity;
import com.jiuyescm.bms.quotation.transport.repository.IPriceTransportValueAddedRepository;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;

/**
 * 
 * @author wubangjun
 * 
 */
@Repository("priceTransportValueAddedRepository")
public class PriceTransportValueAddedRepositoryImpl extends MyBatisDao<PriceTransportValueAddedEntity> implements IPriceTransportValueAddedRepository {

	private static final Logger logger = Logger.getLogger(PriceTransportValueAddedRepositoryImpl.class.getName());

	public PriceTransportValueAddedRepositoryImpl() {
		super();
	}
	
	@Override
    public PageInfo<PriceTransportValueAddedEntity> query(Map<String, Object> condition, int pageNo, int pageSize) {
        List<PriceTransportValueAddedEntity> list = selectList("com.jiuyescm.bms.quotation.transport.PriceTransportValueAddedMapper.query", condition, new RowBounds(
                pageNo, pageSize));
        PageInfo<PriceTransportValueAddedEntity> pageInfo = new PageInfo<PriceTransportValueAddedEntity>(list);
        return pageInfo;
    }
	
	@Override
    public List<PriceTransportValueAddedEntity> query(Map<String, Object> condition) {
        List<PriceTransportValueAddedEntity> list = selectList("com.jiuyescm.bms.quotation.transport.PriceTransportValueAddedMapper.query", condition);
        return list;
    }

    @Override
    public PriceTransportValueAddedEntity findById(Long id) {
        PriceTransportValueAddedEntity entity = selectOne("com.jiuyescm.bms.quotation.transport.PriceTransportValueAddedMapper.findById", id);
        return entity;
    }

    @Override
    public PriceTransportValueAddedEntity save(PriceTransportValueAddedEntity entity) {
        insert("com.jiuyescm.bms.quotation.transport.PriceTransportValueAddedMapper.save", entity);
        return entity;
    }

    @Override
    public PriceTransportValueAddedEntity update(PriceTransportValueAddedEntity entity) {
        update("com.jiuyescm.bms.quotation.transport.PriceTransportValueAddedMapper.update", entity);
        return entity;
    }

    @Override
    public void delete(Long id) {
        delete("com.jiuyescm.bms.quotation.transport.PriceTransportValueAddedMapper.delete", id);
    }

	@Override
	public int saveList(List<PriceTransportValueAddedEntity> valueAddedList) {
		// TODO Auto-generated method stub
		return insertBatch("com.jiuyescm.bms.quotation.transport.PriceTransportValueAddedMapper.save", valueAddedList);
	}

	@Override
	public int deleteBatch(Long id) {
		return  delete("com.jiuyescm.bms.quotation.transport.PriceTransportValueAddedMapper.deleteBatch", id);
	}

	@Override
	public int deleteBatchList(List<PriceTransportValueAddedEntity> list) {
		return deleteBatch("com.jiuyescm.bms.quotation.transport.PriceTransportValueAddedMapper.delete", list);
	}
	
}
