package com.jiuyescm.bms.calculate.api;

import com.jiuyescm.bms.calculate.vo.BmsFeesQtyVo;

public interface IBmsCalcuService {

	/**
	 * 仓储应收
	 */
	public final String StorageRec = "StorageRec";
	
	/**
	 * 配送应收
	 */
	public final String DispatchRec = "DispatchRec";
	
	/**
	 * 查询商家费用单量
	 * @param customerId 商家ID
	 * @param subjectCode 科目编码
	 * @param creMonth   业务月份 201901
	 * @return
	 */
	BmsFeesQtyVo queryFeesQtyForSto(String customerId,String subjectCode,Integer creMonth);
	
	BmsFeesQtyVo queryFeesQtyForDis(String customerId,String subjectCode,Integer creMonth);
	
}
