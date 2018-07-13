package com.jiuyescm.bms.quotation.transport.repository.impl;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.quotation.transport.entity.GenericTemplateEntity;
import com.jiuyescm.bms.quotation.transport.repository.IGenericTemplateRepository;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;

/**
 * 
 * @author wubangjun
 *
 */
@Repository("genericTemplateRepository")
public class GenericTemplateRepositoryImpl extends MyBatisDao<GenericTemplateEntity> implements IGenericTemplateRepository{

	private static final Logger logger = Logger.getLogger(GenericTemplateRepositoryImpl.class.getName());

	public GenericTemplateRepositoryImpl(){
		super();
	}
	
	@Override
	public PageInfo<GenericTemplateEntity> query(Map<String, Object> condition,
			int pageNo, int pageSize) {
		 List<GenericTemplateEntity> list = selectList("com.jiuyescm.bms.quotation.transport.mapper.GenericTemplateMapper.query", 
				 condition, new RowBounds(pageNo, pageSize));
		 PageInfo<GenericTemplateEntity> pageInfo = new PageInfo<GenericTemplateEntity>(list);
	     return pageInfo;
	}
	
	@Override
	public GenericTemplateEntity query(Map<String, Object> condition) {
		SqlSession session = getSqlSessionTemplate();
		return session.selectOne("com.jiuyescm.bms.quotation.transport.mapper.GenericTemplateMapper.queryOne", condition);
	}

	@Override
	public GenericTemplateEntity findById(Long id) {
		GenericTemplateEntity entity = selectOne("com.jiuyescm.bms.quotation.transport.mapper.GenericTemplateMapper.findById", id);
        return entity;
	}

	@Override
	public GenericTemplateEntity save(GenericTemplateEntity entity) {
		 insert("com.jiuyescm.bms.quotation.transport.mapper.GenericTemplateMapper.save", entity);
	     return entity;
	}

	@Override
	public GenericTemplateEntity update(GenericTemplateEntity entity) {
		 update("com.jiuyescm.bms.quotation.transport.mapper.GenericTemplateMapper.update", entity);
	     return entity;
	}

	@Override
	public void delete(Long id) {
        delete("com.jiuyescm.bms.quotation.transport.mapper.GenericTemplateMapper.delete", id);
		
	}

	@Override
	public void remove(Map<String, Object> map) {
		SqlSession session = getSqlSessionTemplate();
		session.update("com.jiuyescm.bms.quotation.transport.mapper.GenericTemplateMapper.remove",map);
	}
    
    

}
