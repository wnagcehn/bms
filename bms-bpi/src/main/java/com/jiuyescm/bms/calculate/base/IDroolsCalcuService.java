package com.jiuyescm.bms.calculate.base;

public interface IDroolsCalcuService {
	/**
	 * @param response	返回对象
	 * @param data 		业务数据
	 * @param rule		规则代码
	 * @param ruleNo	规则编号
	 */
	void excute(Object resultVo,Object reqVo,Object bizData,String rule,String ruleNo);
}
