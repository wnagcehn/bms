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
import com.jiuyescm.bms.quotation.transport.entity.PriceTransportTemplateEntity;
import com.jiuyescm.bms.quotation.transport.repository.IPriceTransportTemplateRepository;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;

/**
 * 
 * @author wubangjun
 * 
 */
@Repository("priceTransportTemplateRepository")
@SuppressWarnings("rawtypes")
public class PriceTransportTemplateRepositoryImpl extends MyBatisDao implements IPriceTransportTemplateRepository {

	private static final Logger logger = Logger.getLogger(PriceTransportTemplateRepositoryImpl.class.getName());

	public PriceTransportTemplateRepositoryImpl() {
		super();
	}
	
	@Override
	@SuppressWarnings("unchecked")
    public PageInfo<PriceTransportTemplateEntity> query(Map<String, Object> condition, int pageNo, int pageSize) {
        List<PriceTransportTemplateEntity> list = selectList("com.jiuyescm.bms.quotation.transport.PriceTransportTemplateMapper.query", condition, new RowBounds(
                pageNo, pageSize));
        PageInfo<PriceTransportTemplateEntity> pageInfo = new PageInfo<PriceTransportTemplateEntity>(list);
        return pageInfo;
    }
	
	@Override
	public PriceTransportTemplateEntity query(Map<String, Object> condition) {
		SqlSession session = getSqlSessionTemplate();
		return session.selectOne("com.jiuyescm.bms.quotation.transport.PriceTransportTemplateMapper.queryOne", condition);
	}
	
    @Override
    @SuppressWarnings("unchecked")
    public PriceTransportTemplateEntity findById(Long id) {
        PriceTransportTemplateEntity entity = (PriceTransportTemplateEntity) selectOne("com.jiuyescm.bms.quotation.transport.PriceTransportTemplateMapper.findById", id);
        return entity;
    }

    @Override
    @SuppressWarnings("unchecked")
    public PriceTransportTemplateEntity save(PriceTransportTemplateEntity entity) {
        insert("com.jiuyescm.bms.quotation.transport.PriceTransportTemplateMapper.save", entity);
        return entity;
    }

    @Override
    @SuppressWarnings("unchecked")
    public PriceTransportTemplateEntity update(PriceTransportTemplateEntity entity) {
        update("com.jiuyescm.bms.quotation.transport.PriceTransportTemplateMapper.update", entity);
        return entity;
    }

    @Override
    public void delete(Long id) {
        delete("com.jiuyescm.bms.quotation.transport.PriceTransportTemplateMapper.delete", id);
    }

	@Override
	public Integer findIdByTemplateCode(String templateCode) {
		int id = (Integer)selectOne("com.jiuyescm.bms.quotation.transport.PriceTransportTemplateMapper.findIdByTemplateCode", templateCode);
		return id;
	}

	@Override
	public int saveList(List<PriceTransportTemplateEntity> transportTemplateList) {
		// TODO Auto-generated method stub
		//return insertBatch("com.jiuyescm.bms.quotation.transport.PriceTransportTemplateMapper.save", rangeList);
		return 0;	
	}

	@Override
	public int updateList(
			List<PriceTransportTemplateEntity> transportTemplateList) {
		// TODO Auto-generated method stub
		//return insertBatch("com.jiuyescm.bms.quotation.transport.PriceTransportTemplateMapper.save", rangeList);
		return 0;
	}
    
    
	
}
