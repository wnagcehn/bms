package com.jiuyescm.bms.file.asyn.repository.impl;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.file.asyn.BmsCorrectAsynTaskEntity;
import com.jiuyescm.bms.file.asyn.repository.IBmsCorrectAsynTaskRepository;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;

@Repository("bmsCorrectAsynTaskRepository")
public class BmsCorrectAsynTaskRepositoryImpl extends MyBatisDao implements IBmsCorrectAsynTaskRepository {

	@Override
	public PageInfo<BmsCorrectAsynTaskEntity> query(
			Map<String, Object> condition, int pageNo, int pageSize) {
		List<BmsCorrectAsynTaskEntity> list=this.selectList("com.jiuyescm.bms.file.asyn.BmsCorrectAsynTaskMapper.query", condition,new RowBounds(pageNo,pageSize));
		return new PageInfo<BmsCorrectAsynTaskEntity>(list);
	}

	@Override
	public BmsCorrectAsynTaskEntity findById(int id) {
		
		List<BmsCorrectAsynTaskEntity> list=this.selectList("com.jiuyescm.bms.file.asyn.BmsCorrectAsynTaskMapper.findById", id);
		if(list!=null&&list.size()>0){
			return list.get(0);
		}
		return null;
	}

	@Override
	public int save(BmsCorrectAsynTaskEntity entity) {
		return this.insert("com.jiuyescm.bms.file.asyn.BmsCorrectAsynTaskMapper.save", entity);
	}

	@Override
	public int update(BmsCorrectAsynTaskEntity entity) {
		return this.update("com.jiuyescm.bms.file.asyn.BmsCorrectAsynTaskMapper.update",entity);
	}

	@Override
	public List<String> queryCorrectCustomerList(
			Map<String, Object> conditionMap) {
		return this.selectList("com.jiuyescm.bms.file.asyn.BmsCorrectAsynTaskMapper.queryCorrectCustomerList", conditionMap);
	}

	@Override
	public boolean existTask(BmsCorrectAsynTaskEntity entity) {
		Object obj=this.selectOneForObject("com.jiuyescm.bms.file.asyn.BmsCorrectAsynTaskMapper.existTask", entity);
		if(obj!=null){
			int k=Integer.valueOf(obj.toString());
			if(k>0){
				return true;
			}else{
				return false;
			}
		}
		return false;
	}

	@Override
	public int saveBatch(List<BmsCorrectAsynTaskEntity> list) {
		return this.insertBatch("com.jiuyescm.bms.file.asyn.BmsCorrectAsynTaskMapper.save", list);
	}

	@Override
	public List<BmsCorrectAsynTaskEntity> queryList(
			Map<String, Object> condition) throws Exception {
		// TODO Auto-generated method stub
		List<BmsCorrectAsynTaskEntity> list=this.selectList("com.jiuyescm.bms.file.asyn.BmsCorrectAsynTaskMapper.query", condition);
		return list;
	}

}
