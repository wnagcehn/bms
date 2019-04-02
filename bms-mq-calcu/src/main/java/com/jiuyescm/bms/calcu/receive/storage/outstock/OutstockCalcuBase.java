package com.jiuyescm.bms.calcu.receive.storage.outstock;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.jiuyescm.bms.asyn.vo.BmsCalcuTaskVo;
import com.jiuyescm.bms.biz.storage.entity.BizOutstockMasterEntity;
import com.jiuyescm.bms.calcu.base.CalcuTaskListener;
import com.jiuyescm.bms.calculate.api.IBmsCalcuService;
import com.jiuyescm.bms.calculate.vo.BmsFeesQtyVo;
import com.jiuyescm.bms.general.entity.FeesReceiveStorageEntity;


public class OutstockCalcuBase extends CalcuTaskListener<BizOutstockMasterEntity,FeesReceiveStorageEntity>{

	private static final Logger logger = LoggerFactory.getLogger(OutstockCalcuBase.class);
	
	protected String b2bFlag;
	
	@Autowired IBmsCalcuService bmsCalcuService;
	
	@Override
	protected void generalCalcu(BmsCalcuTaskVo taskVo, String contractAttr,Map<String, Object> map) {
		map.put("b2bFlag", b2bFlag);
		WebApplicationContext ctx = ContextLoader.getCurrentWebApplicationContext(); 
		try {
			OutstockCalcuJob outstockCalcuJob = (OutstockCalcuJob) ctx.getBean("outstockCalcuJob");
			outstockCalcuJob.process(taskVo, contractAttr);
			outstockCalcuJob.calcu(map);
		} catch (Exception e) {
			logger.error("spring 获取bean异常",e);
		}
	}
	
	@Override
	protected BmsFeesQtyVo feesCountReport(BmsCalcuTaskVo taskVo) {
		BmsFeesQtyVo feesQtyVo = bmsCalcuService.queryFeesQtyForStoOutstock(taskVo.getCustomerId(), taskVo.getSubjectCode(), taskVo.getCreMonth());
		return feesQtyVo;
	}

}
