package com.jiuyescm.bms.correct.repository.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.google.common.collect.Maps;
import com.jiuyescm.bms.biz.storage.entity.BizOutstockPackmaterialEntity;
import com.jiuyescm.bms.correct.BmsMarkingMaterialEntity;
import com.jiuyescm.bms.correct.BmsMaterialMarkOriginEntity;
import com.jiuyescm.bms.correct.BmsProductsMaterialAccountEntity;
import com.jiuyescm.bms.correct.repository.IBmsProductsMaterialRepository;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;

@Repository("bmsProductsMaterialRepository")
public class BmsProductsMaterialRepositoryImpl extends MyBatisDao implements IBmsProductsMaterialRepository{

	@Override
	public List<BmsProductsMaterialAccountEntity> queyAllMax(
			Map<String, Object> condition) {
		List<BmsProductsMaterialAccountEntity> list=selectList("com.jiuyescm.bms.correct.mapper.BmsProductsMaterialMapper.queyAllMax", condition);
		return list;
	}
	@Override
	public List<BmsProductsMaterialAccountEntity> queyAllPmxMax(
			Map<String, Object> condition) {
		// TODO Auto-generated method stub
		List<BmsProductsMaterialAccountEntity> list=selectList("com.jiuyescm.bms.correct.mapper.BmsProductsMaterialMapper.queyAllPmxMax", condition);
		return list;
	}

	@Override
	public List<BmsProductsMaterialAccountEntity> queyAllZxMax(
			Map<String, Object> condition) {
		// TODO Auto-generated method stub
		List<BmsProductsMaterialAccountEntity> list=selectList("com.jiuyescm.bms.correct.mapper.BmsProductsMaterialMapper.queyAllZxMax", condition);
		return list;
	}

	@Override
	public List<BmsProductsMaterialAccountEntity> queyAllBwxMax(
			Map<String, Object> condition) {
		// TODO Auto-generated method stub
		List<BmsProductsMaterialAccountEntity> list=selectList("com.jiuyescm.bms.correct.mapper.BmsProductsMaterialMapper.queyAllBwxMax", condition);
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
	public List<BizOutstockPackmaterialEntity> queyNotMaxMaterial(
			Map<String, Object> condition) {
		// TODO Auto-generated method stubo
		SqlSession session=this.getSqlSessionTemplate();
		List<BizOutstockPackmaterialEntity> list=session.selectList("com.jiuyescm.bms.correct.mapper.BmsProductsMaterialMapper.queyNotMaxMaterial", condition);
		return list;
	}
	
	@Override
	public List<BizOutstockPackmaterialEntity> queyNotMaxPmx(
			Map<String, Object> condition) {
		// TODO Auto-generated method stub
		SqlSession session=this.getSqlSessionTemplate();
		List<BizOutstockPackmaterialEntity> list=session.selectList("com.jiuyescm.bms.correct.mapper.BmsProductsMaterialMapper.queyNotMaxPmx", condition);
		return list;
	}
	@Override
	public List<BizOutstockPackmaterialEntity> queyNotMaxZx(
			Map<String, Object> condition) {
		// TODO Auto-generated method stub
		SqlSession session=this.getSqlSessionTemplate();
		List<BizOutstockPackmaterialEntity> list=session.selectList("com.jiuyescm.bms.correct.mapper.BmsProductsMaterialMapper.queyNotMaxZx", condition);
		return list;
	}
	
	
	@Override
	public List<BizOutstockPackmaterialEntity> queyNotMaxBwd(
			Map<String, Object> condition) {
		// TODO Auto-generated method stubo
		SqlSession session=this.getSqlSessionTemplate();
		List<BizOutstockPackmaterialEntity> list=session.selectList("com.jiuyescm.bms.correct.mapper.BmsProductsMaterialMapper.queyNotMaxBwd", condition);
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
	public int markPmx(Map<String, Object> condition) {
		// TODO Auto-generated method stub
		SqlSession session = this.getSqlSessionTemplate();
		return session.insert("com.jiuyescm.bms.correct.mapper.BmsProductsMaterialMapper.markPmx", condition);
	}

	@Override
	public int markZx(Map<String, Object> condition) {
		// TODO Auto-generated method stub
		SqlSession session = this.getSqlSessionTemplate();
		return session.insert("com.jiuyescm.bms.correct.mapper.BmsProductsMaterialMapper.markZx", condition);
	}
	
	@Override
	public int markBwd(Map<String, Object> condition) {
		// TODO Auto-generated method stub
		SqlSession session = this.getSqlSessionTemplate();
		return session.insert("com.jiuyescm.bms.correct.mapper.BmsProductsMaterialMapper.markBwd", condition);
	
	}
	
	@Override
	public int saveMarkMaterial(Map<String, Object> condition) {
		// TODO Auto-generated method stub
		SqlSession session = this.getSqlSessionTemplate();
		return session.insert("com.jiuyescm.bms.correct.mapper.BmsProductsMaterialMapper.saveMarkMaterial", condition);
	
	}

	@Override
	public int saveMarkPmx(Map<String, Object> condition) {
		// TODO Auto-generated method stub
		SqlSession session = this.getSqlSessionTemplate();
		return session.insert("com.jiuyescm.bms.correct.mapper.BmsProductsMaterialMapper.saveMarkPmx", condition);
	
	}
	@Override
	public int saveMarkZx(Map<String, Object> condition) {
		// TODO Auto-generated method stub
		SqlSession session = this.getSqlSessionTemplate();
		return session.insert("com.jiuyescm.bms.correct.mapper.BmsProductsMaterialMapper.saveMarkZx", condition);
	}

	
	@Override
	public int saveMarkBwd(Map<String, Object> condition) {
		// TODO Auto-generated method stub
		SqlSession session = this.getSqlSessionTemplate();
		return session.insert("com.jiuyescm.bms.correct.mapper.BmsProductsMaterialMapper.saveMarkBwd", condition);
	
	}	

	@Override
	public List<BmsProductsMaterialAccountEntity> queyMaterialCount(
			Map<String, Object> condition) {
		// TODO Auto-generated method stub
		return selectList("com.jiuyescm.bms.correct.mapper.BmsProductsMaterialMapper.queyMaterialCount", condition);
	}

	@Override
	public List<BmsMaterialMarkOriginEntity> queryByMark(
			Map<String, Object> condition) {
		// TODO Auto-generated method stub
		return selectList("com.jiuyescm.bms.correct.mapper.BmsProductsMaterialMapper.queryByMark", condition);
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
	public int savePmx(Map<String, Object> condition) {
		// TODO Auto-generated method stub
		SqlSession session = this.getSqlSessionTemplate();
		return session.insert("com.jiuyescm.bms.correct.mapper.BmsProductsMaterialMapper.savePmx", condition);
	}

	@Override
	public int saveZx(Map<String, Object> condition) {
		// TODO Auto-generated method stub
		SqlSession session = this.getSqlSessionTemplate();
		return session.insert("com.jiuyescm.bms.correct.mapper.BmsProductsMaterialMapper.saveZx", condition);
	}
	
	@Override
	public int saveBwd(Map<String, Object> condition) {
		// TODO Auto-generated method stub
		SqlSession session = this.getSqlSessionTemplate();
		return session.insert("com.jiuyescm.bms.correct.mapper.BmsProductsMaterialMapper.saveBwd", condition);
	
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
		return session.update("com.jiuyescm.bms.correct.mapper.BmsProductsMaterialMapper.deleteMarkMaterialByWaybillNo",map);
	}

	@Override
	public int updateMaterialAccount(Map<String, Object> condition) {
		// TODO Auto-generated method stub
		SqlSession session=this.getSqlSessionTemplate();
		return session.update("com.jiuyescm.bms.correct.mapper.BmsProductsMaterialMapper.updateMaterialAccount", condition);
	}

	@Override
	public BizOutstockPackmaterialEntity queryBwdMaterial(
			Map<String, Object> condition) {
		// TODO Auto-generated method stub
		SqlSession session=this.getSqlSessionTemplate();
		return session.selectOne("com.jiuyescm.bms.correct.mapper.BmsProductsMaterialMapper.queryBwdMaterial", condition);
	
	}

	@Override
	public Map<String, String> getMaterialMap(Map<String, Object> condition) {
		// TODO Auto-generated method stub
		Map<String,String> result=new HashMap<String, String>();
		List<String> list=selectList("com.jiuyescm.bms.correct.mapper.BmsProductsMaterialMapper.getMaterialMap", condition);
		for(String sr:list){
			if(sr.indexOf("%")!=-1){
				result.put(sr.substring(0,sr.indexOf("%")), sr.substring(sr.indexOf("%")+1));
			}
		}
		return result;
	}

	@Override
	public int updatePmxzxMark(List<String> waybillNoList) {
		// TODO Auto-generated method stub
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("waybillnos", waybillNoList);
		return this.update("com.jiuyescm.bms.correct.mapper.BmsProductsMaterialMapper.updatePmxzxMark", map);
	
	}

	@Override
	public int updateBwdMark(List<String> waybillNoList) {
		// TODO Auto-generated method stub
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("waybillnos", waybillNoList);
		return this.update("com.jiuyescm.bms.correct.mapper.BmsProductsMaterialMapper.updateBwdMark", map);
	
	}
	@Override
	public int updatePmxMark(List<String> waybillNoList) {
		// TODO Auto-generated method stub
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("waybillnos", waybillNoList);
		return this.update("com.jiuyescm.bms.correct.mapper.BmsProductsMaterialMapper.updatePmxMark", map);
	}
	@Override
	public int updateZxMark(List<String> waybillNoList) {
		// TODO Auto-generated method stub
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("waybillnos", waybillNoList);
		return this.update("com.jiuyescm.bms.correct.mapper.BmsProductsMaterialMapper.updateZxMark", map);
	}

}
