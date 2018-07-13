package com.jiuyescm.bms.calculate.base.transport;

import com.jiuyescm.bms.base.calcu.vo.CalcuReqVo;
import com.jiuyescm.bms.base.calcu.vo.CalcuResultVo;

/**
 * 运输应收
 * @author caojianwei
 */
public interface ITransportReceiveCalcuService {

	public CalcuResultVo FeesCalcuService(CalcuReqVo vo,String[] apis);
}
