package com.jiuyescm.bms.calcu.receive.storage.dispatchpackage;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.jiuyescm.bms.asyn.vo.BmsCalcuTaskVo;
import com.jiuyescm.bms.biz.dispatch.entity.BizDispatchPackageEntity;
import com.jiuyescm.bms.calcu.base.CalcuTaskListener;
import com.jiuyescm.bms.calculate.api.IBmsCalcuService;
import com.jiuyescm.bms.calculate.vo.BmsFeesQtyVo;
import com.jiuyescm.bms.general.entity.FeesReceiveStorageEntity;

public class DispatchPackageCalcuBase extends CalcuTaskListener<BizDispatchPackageEntity,FeesReceiveStorageEntity>{

	@Autowired IBmsCalcuService bmsCalcuService;
	
	private Logger logger = LoggerFactory.getLogger(DispatchPackageCalcuBase.class);
	
	@Override
	protected void generalCalcu(BmsCalcuTaskVo taskVo, String contractAttr,Map<String, Object> map) {
		WebApplicationContext ctx = ContextLoader.getCurrentWebApplicationContext(); 
		try {
			DispatchPackageCalcuJob dispatchPackageCalcuJob = (DispatchPackageCalcuJob) ctx.getBean("dispatchPackageCalcuJob");
			dispatchPackageCalcuJob.process(taskVo, contractAttr);
			dispatchPackageCalcuJob.calcu(map);
		} catch (Exception e) {
			logger.error("spring 获取bean异常",e);
		}
	}

	@Override
	protected BmsFeesQtyVo feesCountReport(BmsCalcuTaskVo taskVo) {
		BmsFeesQtyVo feesQtyVo = bmsCalcuService.queryFeesQtyForStoStandMaterial(taskVo.getCustomerId(), taskVo.getSubjectCode(), taskVo.getCreMonth());
		return feesQtyVo;
	}
	
    @Override
    protected BmsFeesQtyVo totalAmountReport(BmsCalcuTaskVo taskVo) {
        BmsFeesQtyVo feesQtyVo = bmsCalcuService.queryTotalAmountForStoStandMaterial(taskVo.getCustomerId(), taskVo.getSubjectCode(), taskVo.getCreMonth());
        return feesQtyVo;
    }

}
