package com.jiuyescm.bms.quotation.contract.repository.imp;

import java.util.List;
import java.util.Map;

import com.jiuyescm.bms.quotation.contract.entity.PriceContractItemEntity;

/**
 * 
 * @author cjw by 2017-11-16
 * 
 */
public interface IPriceContractItemRepository {

	public List<PriceContractItemEntity> query(Map<String, Object> condition);


}
