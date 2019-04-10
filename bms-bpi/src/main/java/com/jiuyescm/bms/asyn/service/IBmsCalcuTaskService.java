package com.jiuyescm.bms.asyn.service;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.asyn.vo.BmsCalcuTaskVo;
import com.jiuyescm.exception.BizException;

public interface IBmsCalcuTaskService {
	
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
	
	void saveTask(BmsCalcuTaskVo vo) throws Exception;
	
	List<BmsCalcuTaskVo> queryByMap(Map<String, Object> condition);
	
	/**
	 * 汇总配送费用要发的MQ
	 * @param condition
	 * @return
	 */
	List<BmsCalcuTaskVo> queryDisByMap(Map<String, Object> condition);

	void saveTaskLog(BmsCalcuTaskVo vo) throws Exception;

	List<BmsCalcuTaskVo> query(Map<String, Object> condition);
	
	/**
	 * 界面查询（主）
	 * @param condition
	 * @param pageNo
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	PageInfo<BmsCalcuTaskVo> query(Map<String, Object> condition,int pageNo, int pageSize) throws Exception;
	
	/**
	 * 界面明细查询（子）
	 * @param map
	 * @return
	 * @throws BizException
	 */
	List<BmsCalcuTaskVo> queryDetail(Map<String, Object> map) throws Exception ;

	PageInfo<BmsCalcuTaskVo> queryPage(Map<String, Object> condition,
			int pageNo, int pageSize);

	/**
	 * 查询托数需要发送的任务
	 * @param condition
	 * @return
	 */
	List<BmsCalcuTaskVo> queryPalletTask(Map<String, Object> condition);

	/**
	 * 查询出库需要发送的任务
	 * @param condition
	 * @return
	 */
	List<BmsCalcuTaskVo> queryOutstockTask(Map<String, Object> condition);
	
}
