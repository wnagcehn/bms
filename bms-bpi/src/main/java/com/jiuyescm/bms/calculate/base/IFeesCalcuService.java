package com.jiuyescm.bms.calculate.base;

import com.jiuyescm.bms.base.calcu.vo.CalcuReqVo;
import com.jiuyescm.bms.base.calcu.vo.CalcuResultVo;

public interface IFeesCalcuService {

	/**
	 * 费用计算
	 * @param map
	 * @return
	 */
	public CalcuResultVo FeesCalcuService(CalcuReqVo vo);

}
