package com.jiuyescm.bms.calcu.receive.dispatch;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.jiuyescm.bms.asyn.vo.BmsCalcuTaskVo;
import com.jiuyescm.bms.biz.dispatch.entity.BizDispatchBillEntity;
import com.jiuyescm.bms.calcu.base.CalcuTaskListener;
import com.jiuyescm.bms.calculate.api.IBmsCalcuService;
import com.jiuyescm.bms.calculate.vo.BmsFeesQtyVo;
import com.jiuyescm.bms.general.entity.FeesReceiveDispatchEntity;

public class DispatchCalcuBase extends CalcuTaskListener<BizDispatchBillEntity,FeesReceiveDispatchEntity>{

	private Logger logger = LoggerFactory.getLogger(DispatchCalcuBase.class);
	
	@Autowired IBmsCalcuService bmsCalcuService;
	
	@Override
	protected void generalCalcu(BmsCalcuTaskVo taskVo, String contractAttr,Map<String, Object> map) {
		WebApplicationContext ctx = ContextLoader.getCurrentWebApplicationContext(); 
		try {
			DispatchCalcuJob dispatchCalcuJob = (DispatchCalcuJob) ctx.getBean("dispatchCalcuJob");
			dispatchCalcuJob.process(taskVo, contractAttr);
			dispatchCalcuJob.calcu(map);
		} catch (Exception e) {
			logger.error("spring 获取bean异常",e);
		}
	}

	@Override
	protected BmsFeesQtyVo feesCountReport(BmsCalcuTaskVo taskVo) {
		BmsFeesQtyVo feesQtyVo = bmsCalcuService.queryFeesQtyForDis(taskVo.getCustomerId(), taskVo.getSubjectCode(), taskVo.getCreMonth());
		return feesQtyVo;
	}

}
