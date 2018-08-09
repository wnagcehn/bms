package com.jiuyescm.bms.biz.api;

import java.util.List;

import com.jiuyescm.bms.biz.vo.BmsDispatchVo;

public interface IOutstockService {
	/**
	 * 单个更新配送运单业务数据
	 * @param vo
	 * @return
	 */
	int updateBizData(BmsDispatchVo vos);
	
	/**
	 * 批量更新配送运单业务数据
	 * @param vo
	 * @return
	 */
	int updateBizDataBatch(List<BmsDispatchVo> vos);
	
}
