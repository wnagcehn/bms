package com.jiuyescm.bms.calculate.base;

import java.util.Map;

import com.jiuyescm.bms.base.calcu.vo.CalcuReqVo;
import com.jiuyescm.bms.base.calcu.vo.CalcuResultVo;

public interface IFeesCalcuService {

	/**
	 * 费用计算
	 * @param map
	 * @return
	 */
	public CalcuResultVo FeesCalcuService(CalcuReqVo vo);
	
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
