package com.jiuyescm.bms.calcu.receive.storage.pallet;

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
import com.jiuyescm.bms.general.entity.BizPalletInfoEntity;
import com.jiuyescm.bms.general.entity.FeesReceiveStorageEntity;

public class PalletCalcuBase extends CalcuTaskListener<BizPalletInfoEntity,FeesReceiveStorageEntity>{

	private static final Logger logger = LoggerFactory.getLogger(PalletCalcuBase.class);
	
	protected String bizType;
	
	@Autowired IBmsCalcuService bmsCalcuService;
	
	@Override
	protected void generalCalcu(BmsCalcuTaskVo taskVo, String contractAttr,Map<String, Object> map) {
		map.put("bizType", bizType);
		WebApplicationContext ctx = ContextLoader.getCurrentWebApplicationContext(); 
		try {
			PalletCalcuJob palletCalcuJob = (PalletCalcuJob) ctx.getBean("palletCalcuJob");
			palletCalcuJob.process(taskVo, contractAttr);
			palletCalcuJob.calcu(map);
		} catch (Exception e) {
			logger.error("spring 获取bean异常",e);
		}
		
	}

	@Override
	protected BmsFeesQtyVo feesCountReport(BmsCalcuTaskVo taskVo) {
		BmsFeesQtyVo feesQtyVo = bmsCalcuService.queryFeesQtyForStoPallet(taskVo.getCustomerId(), taskVo.getSubjectCode(), taskVo.getCreMonth());
		return feesQtyVo;
	}

    @Override
    protected BmsFeesQtyVo totalAmountReport(BmsCalcuTaskVo taskVo) {
        BmsFeesQtyVo feesQtyVo = bmsCalcuService.queryTotalAmountForStoPallet(taskVo.getCustomerId(), taskVo.getSubjectCode(), taskVo.getCreMonth());
        return feesQtyVo;
    }

}
