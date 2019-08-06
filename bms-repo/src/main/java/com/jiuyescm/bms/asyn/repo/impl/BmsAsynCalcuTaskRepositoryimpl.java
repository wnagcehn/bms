package com.jiuyescm.bms.asyn.repo.impl;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.asyn.entity.BmsAsynCalcuTaskEntity;
import com.jiuyescm.bms.asyn.repo.IBmsAsynCalcuTaskRepository;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;

@Repository("bmsAsynCalcuTaskRepositoryimpl")
public class BmsAsynCalcuTaskRepositoryimpl extends MyBatisDao<BmsAsynCalcuTaskEntity> implements IBmsAsynCalcuTaskRepository {

	@Override
	public PageInfo<BmsAsynCalcuTaskEntity> query(Map<String, Object> condition, int pageNo, int pageSize) {
		List<BmsAsynCalcuTaskEntity> list = selectList("com.jiuyescm.bms.asyn.entity.BmsAsynCalcuTaskMapper.query", condition, new RowBounds(
                pageNo, pageSize));
        PageInfo<BmsAsynCalcuTaskEntity> pageInfo = new PageInfo<BmsAsynCalcuTaskEntity>(list);
        return pageInfo;
	}
	
	@Override
	public BmsAsynCalcuTaskEntity queryOne(String taskId) {
		BmsAsynCalcuTaskEntity entity = selectOne("com.jiuyescm.bms.asyn.entity.BmsAsynCalcuTaskMapper.queryOne", taskId);
        return entity;
	}

	@Override
	public BmsAsynCalcuTaskEntity save(BmsAsynCalcuTaskEntity entity) {
		insert("com.jiuyescm.bms.asyn.entity.BmsAsynCalcuTaskMapper.save", entity);
        return entity;
	}

	@Override
	public BmsAsynCalcuTaskEntity update(BmsAsynCalcuTaskEntity entity) {
		update("com.jiuyescm.bms.asyn.entity.BmsAsynCalcuTaskMapper.update", entity);
        return entity;
	}

	@Override
	public int updateBatch(List<BmsAsynCalcuTaskEntity> list) {
		// TODO Auto-generated method stub
		return updateBatch("com.jiuyescm.bms.asyn.entity.BmsAsynCalcuTaskMapper.update", list);
	}
	
	@Override
	public List<BmsAsynCalcuTaskEntity> query(Map<String, Object> condition) {
		List<BmsAsynCalcuTaskEntity> list = selectList("com.jiuyescm.bms.asyn.entity.BmsAsynCalcuTaskMapper.query", condition);
		return list;
	}

	@Override
	public List<BmsAsynCalcuTaskEntity> queryUnfinish(Map<String, Object> condition) {
		List<BmsAsynCalcuTaskEntity> list = selectList("com.jiuyescm.bms.asyn.entity.BmsAsynCalcuTaskMapper.queryUnfinish", condition);
		return list;
	}

	@Override
	public List<BmsAsynCalcuTaskEntity> queryByMap(Map<String, Object> condition) {
		// TODO Auto-generated method stub
		List<BmsAsynCalcuTaskEntity> list = selectList("com.jiuyescm.bms.asyn.entity.BmsAsynCalcuTaskMapper.queryByMap", condition);
		return list;
	}
	
	@Override
	public List<BmsAsynCalcuTaskEntity> queryDisByMap(Map<String, Object> condition) {
		// TODO Auto-generated method stub
		List<BmsAsynCalcuTaskEntity> list = selectList("com.jiuyescm.bms.asyn.entity.BmsAsynCalcuTaskMapper.queryDisByMap", condition);
		return list;
	}
	
	@Override
	public PageInfo<BmsAsynCalcuTaskEntity> queryMain(Map<String, Object> condition, int pageNo, int pageSize) {
		List<BmsAsynCalcuTaskEntity> list = selectList("com.jiuyescm.bms.asyn.entity.BmsAsynCalcuTaskMapper.queryMain", condition, new RowBounds(
                pageNo, pageSize));
        PageInfo<BmsAsynCalcuTaskEntity> pageInfo = new PageInfo<BmsAsynCalcuTaskEntity>(list);
        return pageInfo;
	}
	
	@Override
	public List<BmsAsynCalcuTaskEntity> queryInfoByCustomerId(Map<String, Object> map){
		List<BmsAsynCalcuTaskEntity> list = selectList("com.jiuyescm.bms.asyn.entity.BmsAsynCalcuTaskMapper.queryInfoByCustomerId", map);
		return list;
	}
	
	
	@Override
	public List<BmsAsynCalcuTaskEntity> queryInfoByCustomerIdSe(Map<String, Object> map){
		List<BmsAsynCalcuTaskEntity> list = selectList("com.jiuyescm.bms.asyn.entity.BmsAsynCalcuTaskMapper.queryInfoByCustomerIdSe", map);
		return list;
	}
	
	@Override
	public List<BmsAsynCalcuTaskEntity> queryDetail(Map<String, Object> map){
		List<BmsAsynCalcuTaskEntity> list = selectList("com.jiuyescm.bms.asyn.entity.BmsAsynCalcuTaskMapper.queryDetail", map);
		return list;
	}

	@Override
	public BmsAsynCalcuTaskEntity saveLog(BmsAsynCalcuTaskEntity entity) {
		insert("com.jiuyescm.bms.asyn.entity.BmsAsynCalcuTaskMapper.saveLog", entity);
        return entity;
	}

	@Override
	public BmsAsynCalcuTaskEntity updateByTaskId(BmsAsynCalcuTaskEntity entity) {
		update("com.jiuyescm.bms.asyn.entity.BmsAsynCalcuTaskMapper.updateByTaskId", entity);
        return entity;
	}

	@Override
	public List<BmsAsynCalcuTaskEntity> queryGroupCustomer(
			Map<String, Object> condition, int pageNo, int pageSize) {
		List<BmsAsynCalcuTaskEntity> list = selectList("com.jiuyescm.bms.asyn.entity.BmsAsynCalcuTaskMapper.queryMain", condition, new RowBounds(
                pageNo, pageSize));
		return list;
	}

	@Override
	public List<BmsAsynCalcuTaskEntity> queryMainSe(
			Map<String, Object> condition, int pageNo, int pageSize) {
		// TODO Auto-generated method stub
		List<BmsAsynCalcuTaskEntity> list = selectList("com.jiuyescm.bms.asyn.entity.BmsAsynCalcuTaskMapper.queryMainSe", condition, new RowBounds(
                pageNo, pageSize));
		return list;
	}

	@Override
	public long queryMainSeCount(Map<String, Object> condition, int pageNo,
			int pageSize) {
		// TODO Auto-generated method stub
		return (long)(selectOneForInt("com.jiuyescm.bms.asyn.entity.BmsAsynCalcuTaskMapper.queryMainSeCount", condition));
	}
}
