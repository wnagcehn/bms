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
	public List<BmsAsynCalcuTaskEntity> query(Map<String, Object> condition) {
		List<BmsAsynCalcuTaskEntity> list = selectList("com.jiuyescm.bms.asyn.entity.BmsAsynCalcuTaskMapper.query", condition);
		return list;
	}

	@Override
	public List<BmsAsynCalcuTaskEntity> queryUnfinish(Map<String, Object> condition) {
		List<BmsAsynCalcuTaskEntity> list = selectList("com.jiuyescm.bms.asyn.entity.BmsAsynCalcuTaskMapper.queryUnfinish", condition);
		return list;
	}

}