package com.jiuyescm.bms.quotation.contract.service;

import java.util.List;
import java.util.Map;

import com.jiuyescm.bms.quotation.contract.entity.PriceContractDiscountItemEntity;

public interface IPriceContractDiscountService {
	 
    /**
     * 更新折扣
     * 
     */
    public int updateDiscountItem(Map<String,Object> condition);
    
    /**
     * 插入折扣科目
     */
    public int insertDiscountItem(List<PriceContractDiscountItemEntity> list);
}
