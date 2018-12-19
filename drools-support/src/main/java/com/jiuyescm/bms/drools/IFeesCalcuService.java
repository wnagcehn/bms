package com.jiuyescm.bms.drools;

import java.util.Map;

public interface IFeesCalcuService {
	
	/**
	 * 执行合同在线规则
	 * @param bizObject 业务数据
	 * @param feeObject 费用数据
	 * @param quoteObject 报价数据
	 * @param rule	规则代码
	 * @param ruleNo 规则编号
	 * @return
	 */
	public Map<String, Object> ContractCalcuService(Object calcuObject,Object model,String rule,String ruleNo);
	
	public Map<String, Object> getContractCond(Object calcuObject,Object obj,String rule,String ruleNo);

}
