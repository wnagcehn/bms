package com.jiuyescm.bms.base.addunit.repository.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.jiuyescm.bms.base.addunit.repository.IPubAddvalueUnitRepository;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;

/**
 * ..RepositoryImpl
 * @author wangchen
 * 
 */
@SuppressWarnings("rawtypes")
@Repository("pubAddvalueUnitRepository")
public class PubAddvalueUnitRepositoryImpl extends MyBatisDao implements IPubAddvalueUnitRepository {

	@SuppressWarnings("unchecked")
    @Override
	public List<String> queryUnitBySecondSubject(Map<String, Object> condition){
	    return selectList("com.jiuyescm.bms.base.addunit.PubAddvalueUnitMapper.queryBySecondSubject", condition);
	}
	
}
