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
import com.jiuyescm.bms.quotation.out.transport.entity.PriceTransportPayLineRangeEntity;
import com.jiuyescm.bms.quotation.out.transport.repository.IPriceTransportPayLineRangeRepository;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;

/**
 * 
 * @author stevenl
 * 
 */
@Repository("priceTransportPayLineRangeRepository")
public class PriceTransportPayLineRangeRepositoryImpl extends MyBatisDao<PriceTransportPayLineRangeEntity> implements IPriceTransportPayLineRangeRepository {

	private static final Logger logger = Logger.getLogger(PriceTransportPayLineRangeRepositoryImpl.class.getName());

	public PriceTransportPayLineRangeRepositoryImpl() {
		super();
	}
	
	@Override
    public PageInfo<PriceTransportPayLineRangeEntity> query(Map<String, Object> condition, int pageNo, int pageSize) {
        List<PriceTransportPayLineRangeEntity> list = 
        		selectList("com.jiuyescm.bms.quotation.out.transport.mapper.PriceTransportPayLineRangeEntityMapper.query",
        		condition, new RowBounds(pageNo, pageSize));
        PageInfo<PriceTransportPayLineRangeEntity> pageInfo = new PageInfo<PriceTransportPayLineRangeEntity>(list);
        return pageInfo;
    }

    @Override
    public PriceTransportPayLineRangeEntity findById(Long id) {
    	PriceTransportPayLineRangeEntity entity = selectOne("com.jiuyescm.bms.quotation.out.transport.mapper.PriceTransportPayLineRangeEntityMapper.findById", id);
        return entity;
    }

    @Override
    public PriceTransportPayLineRangeEntity save(PriceTransportPayLineRangeEntity entity) {
        insert("com.jiuyescm.bms.quotation.out.transport.mapper.PriceTransportPayLineRangeEntityMapper.save", entity);
        return entity;
    }

    @Override
    public PriceTransportPayLineRangeEntity update(PriceTransportPayLineRangeEntity entity) {
        update("com.jiuyescm.bms.quotation.out.transport.mapper.PriceTransportPayLineRangeEntityMapper.update", entity);
        return entity;
    }

    @Override
    public void delete(Long id) {
        delete("com.jiuyescm.bms.quotation.out.transport.mapper.PriceTransportPayLineRangeEntityMapper.delete", id);
    }

	@Override
	public List<PriceTransportPayLineRangeEntity> query(Map<String, Object> condition) {
		List<PriceTransportPayLineRangeEntity> list = selectList("com.jiuyescm.bms.quotation.out.transport.mapper.PriceTransportPayLineRangeEntityMapper.query", condition);
        return list;
	}

	@Override
	public int saveList(List<PriceTransportPayLineRangeEntity> rangeList) {
		// TODO Auto-generated method stub
		return insertBatch("com.jiuyescm.bms.quotation.out.transport.mapper.PriceTransportPayLineRangeEntityMapper.save", rangeList);
	}
	
}
