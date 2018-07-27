package com.jiuyescm.bms.quotation.contract.repository.imp;

import java.util.List;
import java.util.Map;

import com.jiuyescm.bms.quotation.contract.entity.PriceContractDiscountItemEntity;
import com.jiuyescm.bms.quotation.discount.entity.BmsQuoteDiscountDetailEntity;

public interface IPriceContractDiscountRepository {
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
     * 查询签约的折扣科目
     */
    public List<BmsQuoteDiscountDetailEntity> queryDiscountPrice(Map<String,Object> condition);

}
