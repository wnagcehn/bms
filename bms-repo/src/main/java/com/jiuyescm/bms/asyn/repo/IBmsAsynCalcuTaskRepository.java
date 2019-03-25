package com.jiuyescm.bms.asyn.repo;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.asyn.entity.BmsAsynCalcuTaskEntity;

/**
 * 
 * @author cjw
 * 
 */
public interface IBmsAsynCalcuTaskRepository {

	public PageInfo<BmsAsynCalcuTaskEntity> query(Map<String, Object> condition,int pageNo, int pageSize);

	public List<BmsAsynCalcuTaskEntity> query(Map<String, Object> condition);
	
    public BmsAsynCalcuTaskEntity save(BmsAsynCalcuTaskEntity entity);

    public BmsAsynCalcuTaskEntity update(BmsAsynCalcuTaskEntity entity);

    BmsAsynCalcuTaskEntity queryOne(String taskId);
    
    List<BmsAsynCalcuTaskEntity> queryUnfinish(Map<String, Object> condition);
    
    List<BmsAsynCalcuTaskEntity> queryByMap(Map<String, Object> condition);
}
