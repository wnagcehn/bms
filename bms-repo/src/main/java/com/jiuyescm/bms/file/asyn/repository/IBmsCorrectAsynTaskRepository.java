package com.jiuyescm.bms.file.asyn.repository;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.file.asyn.BmsCorrectAsynTaskEntity;

public interface IBmsCorrectAsynTaskRepository {

	PageInfo<BmsCorrectAsynTaskEntity> query(Map<String, Object> condition,
			int pageNo, int pageSize);
	
	List<BmsCorrectAsynTaskEntity> queryList(Map<String, Object> condition) throws Exception;

	BmsCorrectAsynTaskEntity findById(int id);

	int save(BmsCorrectAsynTaskEntity entity);

	int update(BmsCorrectAsynTaskEntity entity);

	List<String> queryCorrectCustomerList(Map<String, Object> conditionMap);

	boolean existTask(BmsCorrectAsynTaskEntity entity);

	int saveBatch(List<BmsCorrectAsynTaskEntity> list);

}
