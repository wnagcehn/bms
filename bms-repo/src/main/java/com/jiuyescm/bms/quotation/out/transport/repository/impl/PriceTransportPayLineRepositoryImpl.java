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
import com.jiuyescm.bms.quotation.out.transport.entity.PriceTransportPayLineEntity;
import com.jiuyescm.bms.quotation.out.transport.repository.IPriceTransportPayLineRepository;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;

/**
 * 
 * @author wubangjun
 * 
 */
@Repository("priceTransportPayLineRepository")
public class PriceTransportPayLineRepositoryImpl extends MyBatisDao implements IPriceTransportPayLineRepository {

	private static final Logger logger = Logger.getLogger(PriceTransportPayLineRepositoryImpl.class.getName());

	public PriceTransportPayLineRepositoryImpl() {
		super();
	}
	
	@Override
	 @SuppressWarnings("unchecked")
    public PageInfo<PriceTransportPayLineEntity> query(Map<String, Object> condition, int pageNo, int pageSize) {
        List<PriceTransportPayLineEntity> list = selectList("com.jiuyescm.bms.quotation.out.transport.mapper.PriceTransportPayLineEntityMapper.query", condition, new RowBounds(
                pageNo, pageSize));
        PageInfo<PriceTransportPayLineEntity> pageInfo = new PageInfo<PriceTransportPayLineEntity>(list);
        return pageInfo;
    }

    @Override
    @SuppressWarnings("unchecked")
    public PriceTransportPayLineEntity findById(Long id) {
    	PriceTransportPayLineEntity entity = (PriceTransportPayLineEntity) selectOne("com.jiuyescm.bms.quotation.out.transport.mapper.PriceTransportPayLineEntityMapper.findById", id);
        return entity;
    }

    @Override
    @SuppressWarnings("unchecked")
    public PriceTransportPayLineEntity save(PriceTransportPayLineEntity entity) {
        insert("com.jiuyescm.bms.quotation.out.transport.mapper.PriceTransportPayLineEntityMapper.save", entity);
        return entity;
    }

    @Override
    @SuppressWarnings("unchecked")
    public PriceTransportPayLineEntity update(PriceTransportPayLineEntity entity) {
        update("com.jiuyescm.bms.quotation.out.transport.mapper.PriceTransportPayLineEntityMapper.update", entity);
        return entity;
    }

    @Override
    public void delete(Long id) {
        delete("com.jiuyescm.bms.quotation.out.transport.mapper.PriceTransportPayLineEntityMapper.delete", id);
    }

	@Override
	public List<PriceTransportPayLineEntity> query(Map<String, Object> condition) {
		  List<PriceTransportPayLineEntity> list = selectList("com.jiuyescm.bms.quotation.out.transport.mapper.PriceTransportPayLineEntityMapper.query", condition);
	      return list;
	}

	@Override
	@SuppressWarnings("unchecked")
	public int saveList(List<PriceTransportPayLineEntity> lineList) {
		// TODO Auto-generated method stub
		return insertBatch("com.jiuyescm.bms.quotation.out.transport.mapper.PriceTransportPayLineEntityMapper.save", lineList);
	
	}

	@Override
	@SuppressWarnings("unchecked")
	public Integer findIdByLineNo(String lineNo) {
		int id = (Integer)selectOne("com.jiuyescm.bms.quotation.out.transport.mapper.PriceTransportPayLineEntityMapper.findIdByLineNo", lineNo);
		return id;
	}
	
}
