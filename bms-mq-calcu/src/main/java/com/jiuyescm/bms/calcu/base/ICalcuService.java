package com.jiuyescm.bms.calcu.base;

import java.util.List;
import java.util.Map;

import com.jiuyescm.bms.calculate.vo.BmsFeesQtyVo;

public interface ICalcuService<T,F> {

	/**
	 * 单量统计
	 * @param customerId
	 * @param subjectCode
	 * @param creMonth
	 * @return
	 */
	BmsFeesQtyVo feesCountReport(String customerId, String subjectCode,Integer creMonth);
	
	/**
	 * 查询业务数据
	 * @param map
	 * @return
	 */
	List<T> queryBizList(Map<String, Object> map);
	
	/**
	 * 费用计算
	 * @return
	 */
	void updateFees(List<F> list);
	
	/**
	 * 合同在线计算
	 * @return
	 */
	F contractCalcu(T t);
	
	/**
	 * BMS计算
	 * @return
	 */
	F bmsCalcu(T t);
	
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
	abstract F initChargeParam(T t);
	
	
	
	
}
