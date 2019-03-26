package com.jiuyescm.bms.calcu.receive.storage.instock;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import com.jiuyescm.bms.biz.storage.service.IStoInstockService;
import com.jiuyescm.bms.biz.storage.vo.StoInstockVo;
import com.jiuyescm.bms.calcu.base.CalcuServiceStoBase;

public class InstockCalcuBase extends CalcuServiceStoBase<StoInstockVo>{

	@Autowired IStoInstockService stoInstockService;
	
	@Override
	public List<StoInstockVo> queryBizList(Map<String, Object> map){
		return stoInstockService.queryUnExeBiz(map);
	}
	
	@Override
	public StoInstockVo contractCalcu(StoInstockVo vo){
		return null;
	}

	@Override 
	public StoInstockVo bmsCalcu(StoInstockVo vo){
		return null;
	}
	
	@Override
	public StoInstockVo initChargeParam(StoInstockVo vo){
		return null;
		
	}
	
}
