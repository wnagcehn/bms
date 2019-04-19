package com.jiuyescm.bms.correct.repository.impl;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;
import com.jiuyescm.bms.billcheck.BillAccountOutEntity;
import com.jiuyescm.bms.correct.BmsOrderProductEntity;
import com.jiuyescm.bms.correct.BmsProductsWeightAccountEntity;
import com.jiuyescm.bms.correct.ElConditionEntity;
import com.jiuyescm.bms.correct.repository.ElConditionRepository;
import com.jiuyescm.bms.correct.repository.IBmsOrderProductRepository;

/**
 * ..RepositoryImpl
 * @author liuzhicheng
 * 
 */
@Repository("elConditionRepository")
public class ElConditionRepositoryImpl extends MyBatisDao implements ElConditionRepository {

	@Override
    public List<ElConditionEntity> query(Map<String, Object> condition){
		return selectList("com.jiuyescm.bms.correct.ElConditionMapper.query", condition);
	}

	@Override
	public int save(Map<String, Object> param) {
		return insert("com.jiuyescm.bms.correct.ElConditionMapper.save", param);
	}
	
    @Override
    public ElConditionEntity update(ElConditionEntity entity) {
        update("com.jiuyescm.bms.correct.ElConditionMapper.update", entity);
        return entity;
    }
    
    @Override
    public int updateByPullType(Map<String, Object> condition) {
        return this.update("com.jiuyescm.bms.correct.ElConditionMapper.updateByPullType", condition);
    }
}
