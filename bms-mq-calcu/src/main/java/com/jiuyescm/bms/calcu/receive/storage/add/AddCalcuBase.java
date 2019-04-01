package com.jiuyescm.bms.calcu.receive.storage.add;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.jiuyescm.bms.asyn.vo.BmsCalcuTaskVo;
import com.jiuyescm.bms.calcu.base.CalcuTaskListener;
import com.jiuyescm.bms.calculate.api.IBmsCalcuService;
import com.jiuyescm.bms.calculate.vo.BmsFeesQtyVo;
import com.jiuyescm.bms.general.entity.BizAddFeeEntity;
import com.jiuyescm.bms.general.entity.FeesReceiveStorageEntity;


public class AddCalcuBase extends CalcuTaskListener<BizAddFeeEntity,FeesReceiveStorageEntity>{

	private Logger logger = LoggerFactory.getLogger(AddCalcuBase.class);
	
	@Autowired IBmsCalcuService bmsCalcuService;
	
	@Override
	protected void generalCalcu(BmsCalcuTaskVo taskVo, String contractAttr,Map<String, Object> map) {
		WebApplicationContext ctx = ContextLoader.getCurrentWebApplicationContext(); 
		try {
			AddCalcuJob addCalcuJob = (AddCalcuJob) ctx.getBean("addCalcuJob");
			addCalcuJob.process(taskVo, contractAttr);
			addCalcuJob.calcu(map);
		} catch (Exception e) {
			logger.error("spring 获取bean异常",e);
		}
	}

	@Override
	protected BmsFeesQtyVo feesCountReport(BmsCalcuTaskVo taskVo) {
		BmsFeesQtyVo feesQtyVo = bmsCalcuService.queryFeesQtyForSto(taskVo.getCustomerId(), taskVo.getSubjectCode(), taskVo.getCreMonth());
		return feesQtyVo;
	}

	
}
