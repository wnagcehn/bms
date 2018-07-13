package com.jiuyescm.bms.base.dictionary.repository.impl;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.base.dictionary.entity.SystemCodeTypeEntity;
import com.jiuyescm.bms.base.dictionary.repository.ISystemCodeTypeRepository;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;

/**
 * 
 * @author cjw
 * 
 */
@SuppressWarnings("rawtypes")
@Repository("systemCodeTypeRepository")
public class SystemCodeTypeRepositoryImpl extends MyBatisDao implements ISystemCodeTypeRepository {

	private static final Logger logger = Logger.getLogger(SystemCodeTypeRepositoryImpl.class.getName());

	public SystemCodeTypeRepositoryImpl() {
		super();
	}
	
	@SuppressWarnings("unchecked")
	@Override
    public PageInfo<SystemCodeTypeEntity> query(Map<String, Object> condition, int pageNo, int pageSize) {
        List<SystemCodeTypeEntity> list = selectList("com.jiuyescm.bms.base.dictionary.SystemCodeTypeMapper.query", condition, new RowBounds(
                pageNo, pageSize));
        PageInfo<SystemCodeTypeEntity> pageInfo = new PageInfo<SystemCodeTypeEntity>(list);
        return pageInfo;
    }

    @Override
    public SystemCodeTypeEntity findById(Long id) {
        SystemCodeTypeEntity entity = (SystemCodeTypeEntity) selectOne("com.jiuyescm.bms.base.dictionary.SystemCodeTypeMapper.findById", id);
        return entity;
    }

    @SuppressWarnings("unchecked")
	@Override
    public SystemCodeTypeEntity save(SystemCodeTypeEntity entity) {
        insert("com.jiuyescm.bms.base.dictionary.SystemCodeTypeMapper.save", entity);
        return entity;
    }

    @SuppressWarnings("unchecked")
	@Override
    public SystemCodeTypeEntity update(SystemCodeTypeEntity entity) {
        update("com.jiuyescm.bms.base.dictionary.SystemCodeTypeMapper.update", entity);
        return entity;
    }

    @Override
    public void delete(Long id) {
        delete("com.jiuyescm.bms.base.dictionary.SystemCodeTypeMapper.delete", id);
    }

	@Override
	public SystemCodeTypeEntity findByTypeCode(String typeCode) {
		 return (SystemCodeTypeEntity) selectOne("com.jiuyescm.bms.base.dictionary.SystemCodeTypeMapper.findByTypeCode", typeCode);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SystemCodeTypeEntity> query(Map<String, Object> condition) {  
		return selectList("com.jiuyescm.bms.base.dictionary.SystemCodeTypeMapper.query", condition);
	}

	@SuppressWarnings("unchecked")
	@Override
	public int updateByParam(Map<String, Object> condition)
			throws Exception {
		// TODO Auto-generated method stub
		return update("com.jiuyescm.bms.base.dictionary.SystemCodeTypeMapper.updateByParam",condition);
	}


}
