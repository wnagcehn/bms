package com.jiuyescm.bms.fees;

import java.util.List;

import com.jiuyescm.bms.biz.storage.entity.BizAddFeeEntity;
import com.jiuyescm.bms.biz.storage.entity.BizBaseFeeEntity;

/**
 * map参数中需要传入以下参数  商家id customerId  费用科目 subjectId 业务数据 entity
 * @param map
 * @return
 */
public interface INormalReqVoService<T> {
	
	
	/**
	 * 仓储基础报价
	 * @param map
	 * @return
	 */
	List<BizBaseFeeEntity> getStorageGeneralReqVo(List<BizBaseFeeEntity> list);
	
	/**
	 * 仓储其他报价
	 * @param map
	 * @return
	 */
	List<BizAddFeeEntity> getStorageExtraReqVo(List<BizAddFeeEntity> list);
	
}
