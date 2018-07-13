package com.jiuyescm.bms.calculate.base.transport;

import com.jiuyescm.bms.base.calcu.vo.CalcuReqVo;
import com.jiuyescm.bms.base.calcu.vo.CalcuResultVo;

public interface ITransportPayCalcuService {
	public CalcuResultVo FeesCalcuService(CalcuReqVo vo,String[] apis);
}
