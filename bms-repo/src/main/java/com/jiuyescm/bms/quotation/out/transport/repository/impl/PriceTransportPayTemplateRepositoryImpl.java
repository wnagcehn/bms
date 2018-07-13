/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.quotation.out.transport.repository.impl;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.quotation.out.transport.entity.PriceTransportPayTemplateEntity;
import com.jiuyescm.bms.quotation.out.transport.repository.IPriceTransportPayTemplateRepository;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;

/**
 * 
 * @author wubangjun
 * 
 */
@Repository("priceTransportPayTemplateRepository")
@SuppressWarnings("rawtypes")
public class PriceTransportPayTemplateRepositoryImpl extends MyBatisDao implements IPriceTransportPayTemplateRepository {

	private static final Logger logger = Logger.getLogger(PriceTransportPayTemplateRepositoryImpl.class.getName());

	public PriceTransportPayTemplateRepositoryImpl() {
		super();
	}
	
	@Override
	@SuppressWarnings("unchecked")
    public PageInfo<PriceTransportPayTemplateEntity> query(Map<String, Object> condition, int pageNo, int pageSize) {
        List<PriceTransportPayTemplateEntity> list = 
        		selectList("com.jiuyescm.bms.quotation.out.transport.mapper.PriceTransportPayTemplateMapper.query", 
        				condition, new RowBounds(pageNo, pageSize));
        PageInfo<PriceTransportPayTemplateEntity> pageInfo = new PageInfo<PriceTransportPayTemplateEntity>(list);
        return pageInfo;
    }
	
    @Override
    @SuppressWarnings("unchecked")
    public PriceTransportPayTemplateEntity findById(Long id) {
    	PriceTransportPayTemplateEntity entity = (PriceTransportPayTemplateEntity) selectOne("com.jiuyescm.bms.quotation.out.transport.mapper.PriceTransportPayTemplateMapper.findById", id);
        return entity;
    }

    @Override
    @SuppressWarnings("unchecked")
    public PriceTransportPayTemplateEntity save(PriceTransportPayTemplateEntity entity) {
        insert("com.jiuyescm.bms.quotation.out.transport.mapper.PriceTransportPayTemplateMapper.save", entity);
        return entity;
    }

    @Override
    @SuppressWarnings("unchecked")
    public PriceTransportPayTemplateEntity update(PriceTransportPayTemplateEntity entity) {
        update("com.jiuyescm.bms.quotation.out.transport.mapper.PriceTransportPayTemplateMapper.update", entity);
        return entity;
    }

    @Override
    public void delete(Long id) {
        delete("com.jiuyescm.bms.quotation.out.transport.mapper.PriceTransportPayTemplateMapper.delete", id);
    }

	@Override
	public Integer findIdByTemplateCode(String templateCode) {
		int id = (Integer)selectOne("com.jiuyescm.bms.quotation.out.transport.mapper.PriceTransportPayTemplateMapper.findIdByTemplateCode", templateCode);
		return id;
	}

	@Override
	public int saveList(List<PriceTransportPayTemplateEntity> transportPayTemplateList) {
		// TODO Auto-generated method stub
		//return insertBatch("com.jiuyescm.bms.quotation.out.transport.mapper.PriceTransportPayTemplateMapper.save", rangeList);
		return 0;	
	}

	@Override
	public int updateList(
			List<PriceTransportPayTemplateEntity> transportPayTemplateList) {
		// TODO Auto-generated method stub
		//return insertBatch("com.jiuyescm.bms.quotation.out.transport.mapper.PriceTransportPayTemplateMapper.save", rangeList);
		return 0;
	}
    
    
	
}
