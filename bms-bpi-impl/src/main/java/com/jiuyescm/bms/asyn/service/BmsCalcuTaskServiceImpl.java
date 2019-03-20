package com.jiuyescm.bms.asyn.service;

import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.asyn.vo.BmsCalcuTaskVo;

public class BmsCalcuTaskServiceImpl implements IBmsCalcuTaskService{

	@Override
	public PageInfo<BmsCalcuTaskVo> query(Map<String, Object> condition,int pageNo, int pageSize) {
		
		return null;
	}

	@Override
	public BmsCalcuTaskVo sendTask(BmsCalcuTaskVo entity) throws Exception {
		return entity;
		
	}

	@Override
	public void update(BmsCalcuTaskVo entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateStatus(String taskId, int taskRate) {
		// TODO Auto-generated method stub
		
	}

}
