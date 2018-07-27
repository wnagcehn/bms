package com.jiuyescm.bms.quotation.contract.service;

import java.util.List;
import java.util.Map;

import com.jiuyescm.bms.quotation.contract.entity.PriceContractDiscountItemEntity;
import com.jiuyescm.bms.quotation.discount.entity.BmsQuoteDiscountDetailEntity;

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
    
    /**
     * 查询是否签约折扣科目
     */
    public PriceContractDiscountItemEntity query(Map<String,Object> condition);
    
    /**
     * 查询签约的折扣报价
     */
    public List<BmsQuoteDiscountDetailEntity> queryDiscountPrice(Map<String,Object> condition);
}
