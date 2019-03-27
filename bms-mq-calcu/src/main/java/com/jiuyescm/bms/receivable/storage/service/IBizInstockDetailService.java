package com.jiuyescm.bms.receivable.storage.service;

import java.util.List;

import com.jiuyescm.bms.biz.storage.entity.BizInStockDetailEntity;

/**
 * 
 * @author cjw
 * 
 */
public interface IBizInstockDetailService {

	public List<BizInStockDetailEntity> getInstockDetailByMaster(String omsId);

}
