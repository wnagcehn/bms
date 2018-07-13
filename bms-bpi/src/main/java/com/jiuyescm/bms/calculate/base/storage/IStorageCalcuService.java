package com.jiuyescm.bms.calculate.base.storage;

import com.jiuyescm.bms.base.calcu.vo.CalcuReqVo;
import com.jiuyescm.bms.base.calcu.vo.CalcuResultVo;

public interface IStorageCalcuService {
	
	public CalcuResultVo FeesCalcuService(CalcuReqVo vo,String[] apis);
	
}
