package com.jiuyescm.bms.base.dictionary.repository.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.jiuyescm.bms.base.address.repository.impl.PubAddressRepositoryImpl;
import com.jiuyescm.bms.base.dictionary.entity.SystemCodeEntity;
import com.jiuyescm.bms.base.dictionary.repository.ISystemCodeRepository;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;

/**
 * 
 * @author cjw
 * 
 */
@Repository("systemCodeRepository")
public class SystemCodeRepositoryImpl extends MyBatisDao<SystemCodeEntity> implements ISystemCodeRepository {

	//private static final Logger logger = Logger.getLogger(SystemCodeRepositoryImpl.class.getName());
	
	private static final Logger logger = LoggerFactory.getLogger(SystemCodeRepositoryImpl.class.getName());

	public SystemCodeRepositoryImpl() {
		super();
	}
	
	@Override
    public PageInfo<SystemCodeEntity> query(Map<String, Object> condition, int pageNo, int pageSize) {
        List<SystemCodeEntity> list = selectList("com.jiuyescm.bms.base.dictionary.SystemCodeMapper.query", condition, new RowBounds(
                pageNo, pageSize));
        PageInfo<SystemCodeEntity> pageInfo = new PageInfo<SystemCodeEntity>(list);
        return pageInfo;
    }

    @Override
    public SystemCodeEntity findById(Long id) {
        SystemCodeEntity entity = selectOne("com.jiuyescm.bms.base.dictionary.SystemCodeMapper.findById", id);
        return entity;
    }

    @Override
    public SystemCodeEntity save(SystemCodeEntity entity) {
        insert("com.jiuyescm.bms.base.dictionary.SystemCodeMapper.save", entity);
        return entity;
    }

    @Override
    public SystemCodeEntity update(SystemCodeEntity entity) {
        update("com.jiuyescm.bms.base.dictionary.SystemCodeMapper.update", entity);
        return entity;
    }

    @Override
    public void delete(Long id) {
        delete("com.jiuyescm.bms.base.dictionary.SystemCodeMapper.delete", id);
    }

	@Override
	public List<SystemCodeEntity> findEnumList(String typeCode) {  
		return this.selectList("com.jiuyescm.bms.base.dictionary.SystemCodeMapper.getEnumList", typeCode);
	}
 

	@Override
	public SystemCodeEntity queryEntityByCode(String code) { 
		SystemCodeEntity entity = selectOne("com.jiuyescm.bms.base.dictionary.SystemCodeMapper.queryEntityByCode", code);
        return entity;
	}

	@Override
	public List<SystemCodeEntity> queryTempType(Map<String, Object> param) { 
		return selectList("com.jiuyescm.bms.base.dictionary.SystemCodeMapper.queryTemType", param);
	}

	@Override
	public List<SystemCodeEntity> queryCodeList(Map<String, Object> param) {
 
		return selectList("com.jiuyescm.bms.base.dictionary.SystemCodeMapper.query", param);
	}
	
	@Override
	public List<SystemCodeEntity> queryBySortNo(Map<String, Object> param) {
		
		return selectList("com.jiuyescm.bms.base.dictionary.SystemCodeMapper.queryBySortNo", param);
	}

	@Override
	public SystemCodeEntity getSystemCode(String typeCode,String code) {
		Map<String, Object> condition = new HashMap<String, Object>();
		condition.put("typeCode", typeCode);
		condition.put("code", code);
	    List<SystemCodeEntity> list = selectList("com.jiuyescm.bms.base.dictionary.SystemCodeMapper.query", condition); 
		return list.size() > 0 ? list.get(0) : null;
	}
	
	@Override
	public SystemCodeEntity getSystemCodeByattr(String extattr1,String extattr3) {
		Map<String, Object> condition = new HashMap<String, Object>();
		condition.put("extattr1", extattr1);
		condition.put("extattr3", extattr3);
	    List<SystemCodeEntity> list = selectList("com.jiuyescm.bms.base.dictionary.SystemCodeMapper.query", condition); 
		return list.size() > 0 ? list.get(0) : null;
	}

	@Override
	public int getPartOutboundStatus() {
		Map<String,Object> param=new HashMap<String,Object>();
		Object res= selectOne("com.jiuyescm.bms.base.dictionary.SystemCodeMapper.getPartOutboundStatus", param);
		return null==res?0:Integer.parseInt(res.toString());
	}

	@Override
	public List<SystemCodeEntity> queryValueAddList(
			Map<String, Object> mapCondition) {
		
		return selectList("com.jiuyescm.bms.base.dictionary.SystemCodeMapper.queryValueAddList", mapCondition);
	}
	
	@Override
    public List<SystemCodeEntity> queryExtattr1(Map<String, Object> condition) {
        List<SystemCodeEntity> list = selectList("com.jiuyescm.bms.base.dictionary.SystemCodeMapper.queryExtattr1", condition);
        return list;
    }

	@Override
	public List<SystemCodeEntity> queryDeptName() {
        List<SystemCodeEntity> list = selectList("com.jiuyescm.bms.base.dictionary.SystemCodeMapper.queryDeptName", null);
		return list;
	}
}