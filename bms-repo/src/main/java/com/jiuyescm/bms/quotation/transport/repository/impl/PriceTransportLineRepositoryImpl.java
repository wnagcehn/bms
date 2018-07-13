/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.quotation.transport.repository.impl;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.quotation.transport.entity.PriceTransportLineEntity;
import com.jiuyescm.bms.quotation.transport.repository.IPriceTransportLineRepository;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;

/**
 * 
 * @author wubangjun
 * 
 */
@Repository("priceTransportLineRepository")
public class PriceTransportLineRepositoryImpl extends MyBatisDao<PriceTransportLineEntity> implements IPriceTransportLineRepository {
	
	@Override
    public PageInfo<PriceTransportLineEntity> query(Map<String, Object> condition, int pageNo, int pageSize) {
        List<PriceTransportLineEntity> list = selectList("com.jiuyescm.bms.quotation.transport.PriceTransportLineMapper.query", condition, new RowBounds(
                pageNo, pageSize));
        PageInfo<PriceTransportLineEntity> pageInfo = new PageInfo<PriceTransportLineEntity>(list);
        return pageInfo;
    }

    @Override
    public PriceTransportLineEntity findById(Long id) {
        PriceTransportLineEntity entity = (PriceTransportLineEntity) selectOne("com.jiuyescm.bms.quotation.transport.PriceTransportLineMapper.findById", id);
        return entity;
    }

    @Override
    public PriceTransportLineEntity save(PriceTransportLineEntity entity) {
        insert("com.jiuyescm.bms.quotation.transport.PriceTransportLineMapper.save", entity);
        return entity;
    }

    @Override
    public PriceTransportLineEntity update(PriceTransportLineEntity entity) {
        update("com.jiuyescm.bms.quotation.transport.PriceTransportLineMapper.update", entity);
        return entity;
    }

    @Override
    public void delete(Long id) {
        delete("com.jiuyescm.bms.quotation.transport.PriceTransportLineMapper.delete", id);
    }

	@Override
	public List<PriceTransportLineEntity> query(Map<String, Object> condition) {
		  List<PriceTransportLineEntity> list = selectList("com.jiuyescm.bms.quotation.transport.PriceTransportLineMapper.query", condition);
	      return list;
	}

	@Override
	public int saveList(List<PriceTransportLineEntity> lineList) {
		return insertBatch("com.jiuyescm.bms.quotation.transport.PriceTransportLineMapper.save", lineList);
	
	}

	@Override
	public Integer findIdByLineNo(String lineNo) {
		SqlSession session = getSqlSessionTemplate();
		int id = (Integer)session.selectOne("com.jiuyescm.bms.quotation.transport.PriceTransportLineMapper.findIdByLineNo", lineNo);
		return id;
	}

	@Override
	public int deleteBatch(Long id) {
		delete("com.jiuyescm.bms.quotation.transport.PriceTransportLineMapper.deleteBatchDetail", id);
		return delete("com.jiuyescm.bms.quotation.transport.PriceTransportLineMapper.deleteBatch", id);
	}

	@Override
	public int deleteBatchRange(Long id) {
		return delete("com.jiuyescm.bms.quotation.transport.PriceTransportLineMapper.deleteBatchRange", id);
	}

	@Override
	public int deleteBatchList(List<PriceTransportLineEntity> lineList) {
		return deleteBatch("com.jiuyescm.bms.quotation.transport.PriceTransportLineMapper.delete", lineList);
	}

	@Override
	public List<PriceTransportLineEntity> queryToCity(Map<String, Object> condition) {
		return selectList("com.jiuyescm.bms.quotation.transport.PriceTransportLineMapper.queryToCity", condition);
	}
	
	@Override
	public List<PriceTransportLineEntity> queryToCityByProductType(Map<String, Object> condition) {
		return selectList("com.jiuyescm.bms.quotation.transport.PriceTransportLineMapper.queryToCityByProductType", condition);
	}

	@Override
	public List<PriceTransportLineEntity> queryStandardTemplateLine(Map<String, Object> condition) {
		return selectList("com.jiuyescm.bms.quotation.transport.PriceTransportLineMapper.queryStandardTemplateLine", condition);
	}

	@Override
	public List<PriceTransportLineEntity> queryTransportQuos(
			Map<String, Object> parameter) {
		return this.selectList("com.jiuyescm.bms.quotation.transport.PriceTransportLineMapper.queryTransportQuos", parameter);
	}

	@Override
	public List<PriceTransportLineEntity> queryTemplateLine(Map<String, Object> parameter) {
		return selectList("com.jiuyescm.bms.quotation.transport.PriceTransportLineMapper.queryTemplateLine", parameter);
	}
	
}
