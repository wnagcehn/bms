package com.jiuyescm.bms.calcu.receive;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jiuyescm.bms.asyn.service.IBmsCalcuTaskService;
import com.jiuyescm.bms.asyn.vo.BmsCalcuTaskVo;
import com.jiuyescm.bms.calculate.api.IBmsCalcuService;

@Component("commonService")
public class CommonService {
	
	private Logger logger = LoggerFactory.getLogger(CommonService.class);

	@Autowired IBmsCalcuTaskService bmsCalcuTaskService;
	@Autowired IBmsCalcuService bmsCalcuService;
	
	public void taskCountReport(BmsCalcuTaskVo taskVo,String bizType){
		//统计计算状态
		taskVo.setTaskStatus(20);
		try{
			bmsCalcuTaskService.update(taskVo);
		}catch(Exception ex){
			logger.error("计算任务更新异常",ex);
		}
	}
}
