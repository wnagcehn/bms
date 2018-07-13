/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.quotation.transport.repository.impl;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.pub.transport.entity.PubTransportRegionEntity;
import com.jiuyescm.bms.quotation.transport.entity.PriceTransportLineRangeEntity;
import com.jiuyescm.bms.quotation.transport.repository.IPriceTransportLineRangeRepository;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;

/**
 * 
 * @author stevenl
 * 
 */
@Repository("priceTransportLineRangeRepository")
public class PriceTransportLineRangeRepositoryImpl extends MyBatisDao<PriceTransportLineRangeEntity> implements IPriceTransportLineRangeRepository {

	private static final Logger logger = Logger.getLogger(PriceTransportLineRangeRepositoryImpl.class.getName());

	public PriceTransportLineRangeRepositoryImpl() {
		super();
	}
	
	@Override
    public PageInfo<PriceTransportLineRangeEntity> query(Map<String, Object> condition, int pageNo, int pageSize) {
        List<PriceTransportLineRangeEntity> list = selectList("com.jiuyescm.bms.quotation.transport.PriceTransportLineRangeMapper.query", condition, new RowBounds(
                pageNo, pageSize));
        PageInfo<PriceTransportLineRangeEntity> pageInfo = new PageInfo<PriceTransportLineRangeEntity>(list);
        return pageInfo;
    }

    @Override
    public PriceTransportLineRangeEntity findById(Long id) {
        PriceTransportLineRangeEntity entity = selectOne("com.jiuyescm.bms.quotation.transport.PriceTransportLineRangeMapper.findById", id);
        return entity;
    }

    @Override
    public PriceTransportLineRangeEntity save(PriceTransportLineRangeEntity entity) {
        insert("com.jiuyescm.bms.quotation.transport.PriceTransportLineRangeMapper.save", entity);
        return entity;
    }

    @Override
    public PriceTransportLineRangeEntity update(PriceTransportLineRangeEntity entity) {
        update("com.jiuyescm.bms.quotation.transport.PriceTransportLineRangeMapper.update", entity);
        return entity;
    }

    @Override
    public void delete(Long id) {
        delete("com.jiuyescm.bms.quotation.transport.PriceTransportLineRangeMapper.delete", id);
    }

	@Override
	public List<PriceTransportLineRangeEntity> query(Map<String, Object> condition) {
		List<PriceTransportLineRangeEntity> list = selectList("com.jiuyescm.bms.quotation.transport.PriceTransportLineRangeMapper.query", condition);
        return list;
	}

	@Override
	public int saveList(List<PriceTransportLineRangeEntity> rangeList) {
		return insertBatch("com.jiuyescm.bms.quotation.transport.PriceTransportLineRangeMapper.save", rangeList);
	}

	@Override
	public List<PubTransportRegionEntity> queryQuotationRegion(List<String> list) {
		SqlSession session = getSqlSessionTemplate();
		return session.selectList("com.jiuyescm.bms.quotation.transport.PriceTransportLineRangeMapper.queryQuotationRegion", list);
	}
	
}
