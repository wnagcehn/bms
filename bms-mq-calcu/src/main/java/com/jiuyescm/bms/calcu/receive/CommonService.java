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
		/*BmsFeesQtyVo feesQtyVo = new BmsFeesQtyVo();
		switch (bizType) {
		case "STORAGE":
			feesQtyVo = bmsCalcuService.queryFeesQtyForSto(taskVo.getCustomerId(), taskVo.getSubjectCode(), taskVo.getCreMonth());
			break;
		case "DISPATCH":
			feesQtyVo = bmsCalcuService.queryFeesQtyForDis(taskVo.getCustomerId(), taskVo.getSubjectCode(), taskVo.getCreMonth());
			break;
		default:
			break;
		}
		taskVo.setFinishTime(JAppContext.currentTimestamp());
		taskVo.setFeesCount(feesQtyVo.getFeesCount());
		taskVo.setBeginCount(feesQtyVo.getBeginCount());
		taskVo.setFinishCount(feesQtyVo.getFinishCount());
		taskVo.setSysErrorCount(feesQtyVo.getSysErrorCount());
		taskVo.setContractMissCount(feesQtyVo.getContractMissCount());
		taskVo.setQuoteMissCount(feesQtyVo.getQuoteMissCount());
		taskVo.setNoExeCount(feesQtyVo.getNoExeCount());
		taskVo.setUncalcuCount(feesQtyVo.getUncalcuCount());
		taskVo.setTaskRate(100);
		try{
			bmsCalcuTaskService.update(taskVo);
		}catch(Exception ex){
			logger.error("计算任务更新异常",ex);
		}*/
		
	}
}
