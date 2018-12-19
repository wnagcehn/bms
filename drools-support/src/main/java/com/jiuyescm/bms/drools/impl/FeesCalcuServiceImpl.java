package com.jiuyescm.bms.drools.impl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.jiuyescm.bms.drools.IDroolsCalcuService;
import com.jiuyescm.bms.drools.IFeesCalcuService;


@Service("feesCalcuService")
public class FeesCalcuServiceImpl implements IFeesCalcuService {

	private static final Logger logger = LoggerFactory.getLogger(FeesCalcuServiceImpl.class);

	
	@Resource private IDroolsCalcuService droolsCalcuServiceImpl;


	@Override                                           
	public Map<String, Object> ContractCalcuService(Object calcuObject,Object model,String rule,String ruleNo) {
		
		Map<String, Object> rtnMap = new HashMap<String, Object>();
		try{                                       
			droolsCalcuServiceImpl.excute(null,calcuObject,model,rule,ruleNo);
			logger.info("{}",calcuObject);
		}
		catch(Exception ex){
			rtnMap.put("succ", "false");
			logger.error("调用计费规则异常",ex);
		}
		return rtnMap;
	}

	@Override
	public Map<String, Object> getContractCond(Object calcuObject,Object model, String rule, String ruleNo) {
		String str = "calculate";
		droolsCalcuServiceImpl.excute(str,calcuObject,model,rule,ruleNo);
		@SuppressWarnings("unchecked")
		Map<String, Object> condMap = (Map<String, Object>)model;
		logger.info("{}",condMap);
		return condMap;
	}
	
	

}
