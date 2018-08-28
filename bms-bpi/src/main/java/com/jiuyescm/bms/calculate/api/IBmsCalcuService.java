package com.jiuyescm.bms.calculate.api;

public interface IBmsCalcuService {

	/**
	 * 
	 * @param subjectCode 费用科目
	 * @param Id		     业务数据ID
	 * @return
	 */
	public Double tryCalcuForContract(String subjectCode,String Id);
	
	
	
}
