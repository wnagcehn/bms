package com.jiuyescm.bms.calculate.base.dispatch;

import com.jiuyescm.bms.base.calcu.vo.CalcuReqVo;
import com.jiuyescm.bms.base.calcu.vo.CalcuResultVo;

public interface IDispatchReceiveCalcuService {

	public CalcuResultVo FeesCalcuService(CalcuReqVo vo,String[] apis);
}
