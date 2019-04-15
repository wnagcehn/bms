package com.jiuyescm.bms.rpc.calculate.service;

import com.jiuyescm.bms.common.entity.CalculateVo;

/**
 * 费用结算
 * @author cjw 
 */

public interface IFeesCalculateRpcService {

	/**
	 * 计算费用
	 * @param vo 计算对象
	 * @return 计算对象
	 */
	CalculateVo calculate(CalculateVo vo);
}
