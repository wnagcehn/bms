package com.jiuyescm.bms.quotation.storage.repository.impl;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.jiuyescm.bms.quotation.storage.entity.PriceGeneralQuotationEntity;
import com.jiuyescm.bms.quotation.storage.repository.IPriceGeneralQuotationRepository;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;

/**
 * 
 * @author cjw
 * 
 */
@Repository("priceGeneralQuotationRepository")
public class PriceGeneralQuotationRepositoryImpl extends MyBatisDao<PriceGeneralQuotationEntity> implements IPriceGeneralQuotationRepository {

	private static final Logger logger = Logger.getLogger(PriceGeneralQuotationRepositoryImpl.class.getName());

	public PriceGeneralQuotationRepositoryImpl() {
		super();
	}
	
	@Override
    public PageInfo<PriceGeneralQuotationEntity> query(Map<String, Object> condition, int pageNo, int pageSize) {
        List<PriceGeneralQuotationEntity> list = selectList("com.jiuyescm.bms.quotation.storage.PriceGeneralQuotationMapper.query", condition, new RowBounds(
                pageNo, pageSize));
        PageInfo<PriceGeneralQuotationEntity> pageInfo = new PageInfo<PriceGeneralQuotationEntity>(list);
        return pageInfo;
    }
	
	@Override
	public List<PriceGeneralQuotationEntity> queryList(Map<String, Object> condition) {
		List<PriceGeneralQuotationEntity> list = selectList("com.jiuyescm.bms.quotation.storage.PriceGeneralQuotationMapper.query", condition);
		return list;
	}
	
	@Override
	public PriceGeneralQuotationEntity query(Map<String, Object> condition) {
		//SqlSession session = getSqlSessionTemplate();
		return (PriceGeneralQuotationEntity) selectOne("com.jiuyescm.bms.quotation.storage.PriceGeneralQuotationMapper.queryOne", condition);
	}

    @Override
    public PriceGeneralQuotationEntity findById(Long id) {
        PriceGeneralQuotationEntity entity = selectOne("com.jiuyescm.bms.quotation.storage.PriceGeneralQuotationMapper.findById", id);
        return entity;
    }

    @Override
    public PriceGeneralQuotationEntity save(PriceGeneralQuotationEntity entity) {
        insert("com.jiuyescm.bms.quotation.storage.PriceGeneralQuotationMapper.save", entity);
        return entity;
    }

    @Override
    public PriceGeneralQuotationEntity update(PriceGeneralQuotationEntity entity) {
        update("com.jiuyescm.bms.quotation.storage.PriceGeneralQuotationMapper.update", entity);
        return entity;
    }

    @Override
    public void delete(Long id) {
        delete("com.jiuyescm.bms.quotation.storage.PriceGeneralQuotationMapper.delete", id);
    }

	@Override
	public Integer insert(PriceGeneralQuotationEntity entity) {
		return super.insert("com.jiuyescm.bms.quotation.storage.PriceGeneralQuotationMapper.save", entity);
	}

	@Override
	public PriceGeneralQuotationEntity findByNo(String quotationNo) {
		 PriceGeneralQuotationEntity entity = selectOne("com.jiuyescm.bms.quotation.storage.PriceGeneralQuotationMapper.findByNo", quotationNo);
	     return entity;
	}

	@Override
	public List<PriceGeneralQuotationEntity> queryPriceGeneral(Map<String, Object> condition) {
		return super.selectList("com.jiuyescm.bms.quotation.storage.PriceGeneralQuotationMapper.queryPriceGeneral", condition);
	}

	@Override
	public void removeAll(Map<String, Object> map) {
		SqlSession session = getSqlSessionTemplate();
		session.update("com.jiuyescm.bms.quotation.storage.PriceGeneralQuotationMapper.remove",map);
	}


	@Override
	public List<PriceGeneralQuotationEntity> queryPriceStandardGeneral(Map<String, Object> condition) {
		return selectList("com.jiuyescm.bms.quotation.storage.PriceGeneralQuotationMapper.queryPriceStandardGeneral", condition);
	}

	@Override
	public List<PriceGeneralQuotationEntity> queryPriceGeneralByContract(Map<String, Object> map) {
		return this.selectList("com.jiuyescm.bms.quotation.storage.PriceGeneralQuotationMapper.queryPriceGeneralByContract", map);
	}

	@Override
	public List<PriceGeneralQuotationEntity> queryStandardModel(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PriceGeneralQuotationEntity queryByQuotationNo(String quotationNo) {
		Map<String,Object> map=Maps.newHashMap();
		map.put("quotationNo", quotationNo);
		Object obj=this.selectOne("com.jiuyescm.bms.quotation.storage.PriceGeneralQuotationMapper.queryByQuotationNo", map);
		if(obj!=null){
			return (PriceGeneralQuotationEntity)obj;
		}
		return null;
	}

	
	
	
	
}
