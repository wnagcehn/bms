package com.jiuyescm.bms.asyn.service;

import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.asyn.vo.BmsCalcuTaskVo;

public interface IBmsCalcuTaskService {

	PageInfo<BmsCalcuTaskVo> query(Map<String, Object> condition,int pageNo, int pageSize) throws Exception;
	
	BmsCalcuTaskVo queryCalcuTask(String taskId) throws Exception;
	
	/**
	 * 发送计算任务 
	 * @param vo 必填字段：商家id，商家名称，科目id，科目名称，业务年月201901,创建人，创建人ID
	 * @return
	 * @throws Exception
	 */
	BmsCalcuTaskVo sendTask(BmsCalcuTaskVo vo) throws Exception;

	void update(BmsCalcuTaskVo entity) throws Exception;
	
	void updateRate(String taskId,Integer taskRate) throws Exception;
	
	void saveTask(BmsCalcuTaskVo vo);
	
}
