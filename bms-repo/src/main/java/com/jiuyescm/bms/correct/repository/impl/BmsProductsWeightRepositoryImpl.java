package com.jiuyescm.bms.correct.repository.impl;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.jiuyescm.bms.correct.BmsMarkingProductsEntity;
import com.jiuyescm.bms.correct.BmsProductsWeightAccountEntity;
import com.jiuyescm.bms.correct.repository.IBmsProductsWeightRepository;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;

@SuppressWarnings("rawtypes")
@Repository("bmsProductsWeightRepository")
public class BmsProductsWeightRepositoryImpl extends MyBatisDao implements IBmsProductsWeightRepository{

	@SuppressWarnings("unchecked")
	@Override
	public List<BmsProductsWeightAccountEntity> queyAllMax(
			Map<String, Object> condition) {
		// TODO Auto-generated method stub
		List<BmsProductsWeightAccountEntity> list=selectList("com.jiuyescm.bms.correct.mapper.BmsProductsWeightMapper.queyAllMax", condition);
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<BmsMarkingProductsEntity> queryMark(Map<String, Object> condition) {
		// TODO Auto-generated method stub
		List<BmsMarkingProductsEntity> list=selectList("com.jiuyescm.bms.correct.mapper.BmsProductsWeightMapper.queryMark", condition);
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public int updateMarkList(List<BmsMarkingProductsEntity> list) {
		// TODO Auto-generated method stub
		return updateBatch("com.jiuyescm.bms.correct.mapper.BmsProductsWeightMapper.update", list);
	}

	@SuppressWarnings("unchecked")
	@Override
	public BmsMarkingProductsEntity queryMarkVo(Map<String, Object> condition) {
		// TODO Auto-generated method stub
		return (BmsMarkingProductsEntity) selectOne("com.jiuyescm.bms.correct.mapper.BmsProductsWeightMapper.queryMarkVo", condition);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<BmsProductsWeightAccountEntity> queyWeightCount(
			Map<String, Object> condition) {
		// TODO Auto-generated method stub
		return selectList("com.jiuyescm.bms.correct.mapper.BmsProductsWeightMapper.queyWeightCount", condition);
	}

	@SuppressWarnings("unchecked")
	@Override
	public int saveList(List<BmsProductsWeightAccountEntity> list) {
		// TODO Auto-generated method stub
		return insertBatch("com.jiuyescm.bms.correct.mapper.BmsProductsWeightMapper.saveList", list);
	}

	@SuppressWarnings("unchecked")
	@Override
	public int updateMark(Map<String, Object> condition) {
		// TODO Auto-generated method stub
		return update("com.jiuyescm.bms.correct.mapper.BmsProductsWeightMapper.updateMark", condition);
	}

	@Override
	public int saveWeight(Map<String, Object> condition) {
		// TODO Auto-generated method stub
		SqlSession session = this.getSqlSessionTemplate();
		return session.insert("com.jiuyescm.bms.correct.mapper.BmsProductsWeightMapper.saveWeight", condition);
	
	}
	
}
