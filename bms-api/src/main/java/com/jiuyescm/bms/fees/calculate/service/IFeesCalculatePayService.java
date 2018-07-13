package com.jiuyescm.bms.fees.calculate.service;


import com.jiuyescm.bms.common.entity.CalculateVo;

/**
 * 费用结算
 * @author cjw by 2017-06-08
 */

public interface IFeesCalculatePayService {

	/**
	 * 计算费用
	 * @param vo 计算对象
	 * @return 计算对象
	 */
	CalculateVo calculate(CalculateVo vo);
}
