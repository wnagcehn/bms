package com.jiuyescm.bms.fees;

import java.util.Map;

import com.jiuyescm.bms.base.calcu.vo.CalcuReqVo;

public interface IStandardReqVoService<T> {

	CalcuReqVo<T> getStorageReqVo(String subjectID);
	
	CalcuReqVo<T> getMaterialReqVo(Map<String, Object> map);
	
	CalcuReqVo<T> getOtherReqVo(Map<String, Object> map);
	
	CalcuReqVo<T> getDispatchReceiveReqVo(String subjectId,String wareHouseId,String province);
	
	CalcuReqVo<T> getDispatchPayReqVo(String subjectId,String wareHouseId,String province);
	
}
