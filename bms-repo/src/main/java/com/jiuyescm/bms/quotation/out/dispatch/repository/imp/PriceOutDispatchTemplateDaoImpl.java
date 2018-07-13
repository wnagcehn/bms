package com.jiuyescm.bms.quotation.out.dispatch.repository.imp;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.quotation.out.dispatch.entity.PriceOutDispacthTemplateEntity;
import com.jiuyescm.bms.quotation.out.dispatch.repository.IPriceOutDispatchTemplateDao;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;

/**
 * 
 * @author wubangjun
 *
 */
@Repository("outDeliverTemplateDao")
public class PriceOutDispatchTemplateDaoImpl extends MyBatisDao<PriceOutDispacthTemplateEntity> implements IPriceOutDispatchTemplateDao{

	public PriceOutDispatchTemplateDaoImpl(){
		super();
	}
	
	@Override
	public PageInfo<PriceOutDispacthTemplateEntity> query(Map<String, Object> condition,
			int pageNo, int pageSize) {
		 List<PriceOutDispacthTemplateEntity> list = selectList("com.jiuyescm.bms.quotation.out.dispatch.mapper.PriceOutDispatchTemplateMapper.query",condition, new RowBounds(pageNo, pageSize));
		 PageInfo<PriceOutDispacthTemplateEntity> pageInfo = new PageInfo<PriceOutDispacthTemplateEntity>(list);
	     return pageInfo;
	}
	
	@Override
	public PriceOutDispacthTemplateEntity query(Map<String, Object> condition) {
		SqlSession session = getSqlSessionTemplate();
		return session.selectOne("com.jiuyescm.bms.quotation.out.dispatch.mapper.PriceOutDispatchTemplateMapper.queryOne", condition);
	}

	@Override
	public PriceOutDispacthTemplateEntity findById(Long id) {
		PriceOutDispacthTemplateEntity entity = selectOne("com.jiuyescm.bms.quotation.out.dispatch.mapper.PriceOutDispatchTemplateMapper.findById", id);
        return entity;
	}

	@Override
	public PriceOutDispacthTemplateEntity save(PriceOutDispacthTemplateEntity entity) {
		 insert("com.jiuyescm.bms.quotation.out.dispatch.mapper.PriceOutDispatchTemplateMapper.save", entity);
	     return entity;
	}

	@Override
	public PriceOutDispacthTemplateEntity update(PriceOutDispacthTemplateEntity entity) {
		 update("com.jiuyescm.bms.quotation.out.dispatch.mapper.PriceOutDispatchTemplateMapper.update", entity);
	     return entity;
	}

	@Override
	public int delete(PriceOutDispacthTemplateEntity entity) {
        return delete("com.jiuyescm.bms.quotation.out.dispatch.mapper.PriceOutDispatchTemplateMapper.delete", entity);
		
	}

	@Override
	public List<PriceOutDispacthTemplateEntity> queryDeliverTemplate(Map<String, Object> condition) {
		List<PriceOutDispacthTemplateEntity> list = selectList("com.jiuyescm.bms.quotation.out.dispatch.mapper.PriceOutDispatchTemplateMapper.query",condition);
		return list;
	}
    
    

}
