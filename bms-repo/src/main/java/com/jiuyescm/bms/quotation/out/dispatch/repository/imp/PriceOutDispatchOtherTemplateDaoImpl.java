package com.jiuyescm.bms.quotation.out.dispatch.repository.imp;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.quotation.out.dispatch.entity.PriceOutDispatchOtherTemplateEntity;
import com.jiuyescm.bms.quotation.out.dispatch.repository.IPriceOutDispatchOtherTemplateDao;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;

/**
 * 
 * @author wubangjun
 *
 */
@Repository("outDeliverOtherTemplateDao")
public class PriceOutDispatchOtherTemplateDaoImpl extends MyBatisDao<PriceOutDispatchOtherTemplateEntity> implements IPriceOutDispatchOtherTemplateDao{

	public PriceOutDispatchOtherTemplateDaoImpl(){
		super();
	}
	
	@Override
	public PageInfo<PriceOutDispatchOtherTemplateEntity> query(Map<String, Object> condition,
			int pageNo, int pageSize) {                               
		 List<PriceOutDispatchOtherTemplateEntity> list = selectList("com.jiuyescm.bms.quotation.out.dispatch.mapper.PriceOutDispatchOtherTemplateMapper.query",condition, new RowBounds(pageNo, pageSize));
		 PageInfo<PriceOutDispatchOtherTemplateEntity> pageInfo = new PageInfo<PriceOutDispatchOtherTemplateEntity>(list);
	     return pageInfo;
	}
	
	@Override
	public PriceOutDispatchOtherTemplateEntity query(Map<String, Object> condition) {                               
		SqlSession session = getSqlSessionTemplate();
		return session.selectOne("com.jiuyescm.bms.quotation.out.dispatch.mapper.PriceOutDispatchOtherTemplateMapper.queryOne", condition);
	}

	@Override
	public PriceOutDispatchOtherTemplateEntity findById(Long id) {
		PriceOutDispatchOtherTemplateEntity entity = selectOne("com.jiuyescm.bms.quotation.out.dispatch.mapper.PriceOutDispatchOtherTemplateMapper.findById", id);
        return entity;
	}

	@Override
	public PriceOutDispatchOtherTemplateEntity save(PriceOutDispatchOtherTemplateEntity entity) {
		 insert("com.jiuyescm.bms.quotation.out.dispatch.mapper.PriceOutDispatchOtherTemplateMapper.save", entity);
	     return entity;
	}

	@Override
	public PriceOutDispatchOtherTemplateEntity update(PriceOutDispatchOtherTemplateEntity entity) {
		 update("com.jiuyescm.bms.quotation.out.dispatch.mapper.PriceOutDispatchOtherTemplateMapper.update", entity);
	     return entity;
	}

	@Override
	public int delete(PriceOutDispatchOtherTemplateEntity entity) {
        return delete("com.jiuyescm.bms.quotation.out.dispatch.mapper.PriceOutDispatchOtherTemplateMapper.delete", entity);
		
	}

	@Override
	public List<PriceOutDispatchOtherTemplateEntity> queryDeliverTemplate(Map<String, Object> condition) {
		List<PriceOutDispatchOtherTemplateEntity> list = selectList("com.jiuyescm.bms.quotation.out.dispatch.mapper.PriceOutDispatchOtherTemplateMapper.query",condition);
		return list;
	}
    
    

}
