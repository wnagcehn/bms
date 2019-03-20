package com.jiuyescm.bms.asyn.service;

import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.asyn.vo.BmsCalcuTaskVo;

public interface IBmsCalcuTaskService {

	PageInfo<BmsCalcuTaskVo> query(Map<String, Object> condition,int pageNo, int pageSize);
	
	BmsCalcuTaskVo sendTask(BmsCalcuTaskVo entity) throws Exception;

	void update(BmsCalcuTaskVo entity);
	
	void updateStatus(String taskId,int taskRate);
	
}
