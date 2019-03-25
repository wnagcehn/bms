package com.jiuyescm.bms.calculate.repo.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.jiuyescm.bms.calculate.BmsFeesQtyEntity;
import com.jiuyescm.bms.calculate.repo.IBmsCalcuRepository;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;

@Repository("bmsCalcuRepositoryImpl")
public class BmsCalcuRepositoryImpl extends MyBatisDao<BmsFeesQtyEntity> implements IBmsCalcuRepository {

	@Override
	public BmsFeesQtyEntity queryTotalFeesQtyForSto(String customerId,String subjectCode, String startTime,String endTime) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("customerId", customerId);
		map.put("subjectCode", subjectCode);
		map.put("startTime", startTime);
		map.put("endTime", endTime);
		BmsFeesQtyEntity entity = (BmsFeesQtyEntity) selectOne("com.jiuyescm.bms.calculate.BmsCalcuMapper.queryTotalFeesQtyForSto", map);
        return entity;
		/*
		String creDate = creMonth.toString();
		int startYear = Integer.parseInt(creDate.substring(0, 4));
		int startMonth = Integer.parseInt(creDate.substring(4, 6));
		String startTime = startYear+"-"+startMonth;*/
		//return null;
	}

	@Override
	public List<BmsFeesQtyEntity> queryStatusFeesQtyForSto(String customerId,String subjectCode, String startTime,String endTime) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("customerId", customerId);
		map.put("subjectCode", subjectCode);
		map.put("startTime", startTime);
		map.put("endTime", endTime);
		List<BmsFeesQtyEntity> entity = selectList("com.jiuyescm.bms.calculate.BmsCalcuMapper.queryStatusFeesQtyForSto", map);
        return entity;
	}

	@Override
	public BmsFeesQtyEntity queryTotalFeesQtyForDis(String customerId,String subjectCode, String startTime,String endTime) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("customerId", customerId);
		map.put("subjectCode", subjectCode);
		map.put("startTime", startTime);
		map.put("endTime", endTime);
		BmsFeesQtyEntity entity = (BmsFeesQtyEntity) selectOne("com.jiuyescm.bms.calculate.BmsCalcuMapper.queryTotalFeesQtyForDis", map);
        return entity;
	}

	@Override
	public List<BmsFeesQtyEntity> queryStatusFeesQtyForDis(String customerId,String subjectCode, String startTime,String endTime) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("customerId", customerId);
		map.put("subjectCode", subjectCode);
		map.put("startTime", startTime);
		map.put("endTime", endTime);
		List<BmsFeesQtyEntity> entity = selectList("com.jiuyescm.bms.calculate.BmsCalcuMapper.queryStatusFeesQtyForDis", map);
        return entity;
	}



	

}
