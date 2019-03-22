package com.jiuyescm.bms.calcu.base;

public interface ICalcuService<T,F> {

	/**
	 * 是否计算费用
	 * @param t 业务数据对象
	 * @return true-计算费用  false-不计算你费用 状态未不计算，金额至0
	 */
	boolean isCalcu(T t);
	
	/**
	 * 筛选计费参数
	 * @param t 业务数据对象
	 * @return 费用数据对象
	 */
	F initChargeParam(T t);
	
	/**
	 * 判定合同归属
	 * @param t 业务数据对象
	 * @return CONTRACT-合同在线   BMS-bms
	 */
	String queryContractAttr(T t);
	
	
	
	
}
