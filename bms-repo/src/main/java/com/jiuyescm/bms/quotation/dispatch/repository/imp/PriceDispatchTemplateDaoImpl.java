package com.jiuyescm.bms.quotation.dispatch.repository.imp;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.quotation.dispatch.entity.PriceDispatchTemplateEntity;
import com.jiuyescm.bms.quotation.dispatch.repository.IPriceDispatchTemplateDao;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;

/**
 * 
 * @author wubangjun
 *
 */
@Repository("deliverTemplateDao")
public class PriceDispatchTemplateDaoImpl extends MyBatisDao<PriceDispatchTemplateEntity> implements IPriceDispatchTemplateDao{

	private static final Logger logger = Logger.getLogger(PriceDispatchTemplateDaoImpl.class.getName());

	public PriceDispatchTemplateDaoImpl(){
		super();
	}
	
	@Override
	public PageInfo<PriceDispatchTemplateEntity> query(Map<String, Object> condition,
			int pageNo, int pageSize) {
		 List<PriceDispatchTemplateEntity> list = selectList("com.jiuyescm.bms.quotation.dispatch.mapper.PriceDispatchTemplateMapper.query",condition, new RowBounds(pageNo, pageSize));
		 PageInfo<PriceDispatchTemplateEntity> pageInfo = new PageInfo<PriceDispatchTemplateEntity>(list);
	     return pageInfo;
	}
	
	@Override
	public PriceDispatchTemplateEntity query(Map<String, Object> condition) {
		SqlSession session = getSqlSessionTemplate();
		return session.selectOne("com.jiuyescm.bms.quotation.dispatch.mapper.PriceDispatchTemplateMapper.queryOne", condition);
	}

	@Override
	public PriceDispatchTemplateEntity findById(Long id) {
		PriceDispatchTemplateEntity entity = selectOne("com.jiuyescm.bms.quotation.dispatch.mapper.PriceDispatchTemplateMapper.findById", id);
        return entity;
	}

	@Override
	public PriceDispatchTemplateEntity save(PriceDispatchTemplateEntity entity) {
		 insert("com.jiuyescm.bms.quotation.dispatch.mapper.PriceDispatchTemplateMapper.save", entity);
	     return entity;
	}

	@Override
	public PriceDispatchTemplateEntity update(PriceDispatchTemplateEntity entity) {
		 update("com.jiuyescm.bms.quotation.dispatch.mapper.PriceDispatchTemplateMapper.update", entity);
	     return entity;
	}

	@Override
	public int delete(PriceDispatchTemplateEntity entity) {
        return delete("com.jiuyescm.bms.quotation.dispatch.mapper.PriceDispatchTemplateMapper.delete", entity);
		
	}

	@Override
	public List<PriceDispatchTemplateEntity> queryDeliverTemplate(Map<String, Object> condition) {
		List<PriceDispatchTemplateEntity> list = selectList("com.jiuyescm.bms.quotation.dispatch.mapper.PriceDispatchTemplateMapper.query",condition);
		return list;
	}
    
    

}
