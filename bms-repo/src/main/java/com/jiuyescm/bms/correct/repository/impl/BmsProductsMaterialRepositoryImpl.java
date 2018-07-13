package com.jiuyescm.bms.correct.repository.impl;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.google.common.collect.Maps;
import com.jiuyescm.bms.correct.BmsMarkingMaterialEntity;
import com.jiuyescm.bms.correct.BmsProductsMaterialAccountEntity;
import com.jiuyescm.bms.correct.repository.IBmsProductsMaterialRepository;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;

@Repository("bmsProductsMaterialRepository")
public class BmsProductsMaterialRepositoryImpl extends MyBatisDao<BmsProductsMaterialAccountEntity> implements IBmsProductsMaterialRepository{

	@Override
	public List<BmsProductsMaterialAccountEntity> queyAllMax(
			Map<String, Object> condition) {
		List<BmsProductsMaterialAccountEntity> list=selectList("com.jiuyescm.bms.correct.mapper.BmsProductsMaterialMapper.queyAllMax", condition);
		return list;
	}

	@Override
	public List<BmsMarkingMaterialEntity> queyNotMax(
			Map<String, Object> condition) {
		SqlSession session=this.getSqlSessionTemplate();
		List<BmsMarkingMaterialEntity> list=session.selectList("com.jiuyescm.bms.correct.mapper.BmsProductsMaterialMapper.queyNotMax", condition);
		return list;
	}
	
	@Override
	public List<BmsMarkingMaterialEntity> queryMark(Map<String, Object> condition) {
		SqlSession session=this.getSqlSessionTemplate();
		List<BmsMarkingMaterialEntity> list=session.selectList("com.jiuyescm.bms.correct.mapper.BmsProductsMaterialMapper.queryMark", condition);
		return list;
	}

	@Override
	public int updateMarkList(List<BmsMarkingMaterialEntity> list) {
		SqlSession session=this.getSqlSessionTemplate();
		return session.update("com.jiuyescm.bms.correct.mapper.BmsProductsMaterialMapper.update", list);
	}

	@Override
	public BmsMarkingMaterialEntity queryMarkVo(Map<String, Object> condition) {
		SqlSession session=this.getSqlSessionTemplate();
		return session.selectOne("com.jiuyescm.bms.correct.mapper.BmsProductsMaterialMapper.queryMarkVo", condition);
	}

	@Override
	public int markMaterial(Map<String, Object> condition) {
		SqlSession session = this.getSqlSessionTemplate();
		return session.insert("com.jiuyescm.bms.correct.mapper.BmsProductsMaterialMapper.markMaterial", condition);
	}

	@Override
	public List<BmsProductsMaterialAccountEntity> queyMaterialCount(
			Map<String, Object> condition) {
		// TODO Auto-generated method stub
		return selectList("com.jiuyescm.bms.correct.mapper.BmsProductsMaterialMapper.queyMaterialCount", condition);
	}

	@Override
	public int saveList(List<BmsProductsMaterialAccountEntity> list) {
		// TODO Auto-generated method stub
		return insertBatch("com.jiuyescm.bms.correct.mapper.BmsProductsMaterialMapper.saveList", list);
	}

	@Override
	public int saveMaterial(Map<String, Object> condition) {
		// TODO Auto-generated method stub
		SqlSession session = this.getSqlSessionTemplate();
		return session.insert("com.jiuyescm.bms.correct.mapper.BmsProductsMaterialMapper.saveMaterial", condition);
	}

	@Override
	public BmsMarkingMaterialEntity queryOneMark(Map<String, Object> condition) {
		// TODO Auto-generated method stub
		SqlSession session=this.getSqlSessionTemplate();
		return session.selectOne("com.jiuyescm.bms.correct.mapper.BmsProductsMaterialMapper.queryOneMark", condition);
	
	}

	@Override
	public int deleteMarkMaterialByWaybillNo(List<String> waybillNoList) {
		Map<String,Object> map=Maps.newHashMap();
		map.put("waybillNoList", waybillNoList);
		SqlSession session=this.getSqlSessionTemplate();
		return session.delete("com.jiuyescm.bms.correct.mapper.BmsProductsMaterialMapper.deleteMarkMaterialByWaybillNo",map);
	}

	@Override
	public int updateMaterialAccount(Map<String, Object> condition) {
		// TODO Auto-generated method stub
		SqlSession session=this.getSqlSessionTemplate();
		return session.update("com.jiuyescm.bms.correct.mapper.BmsProductsMaterialMapper.updateMaterialAccount", condition);
	}
	
}
