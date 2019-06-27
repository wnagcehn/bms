/*
 * Copyright (c) 2019, Jiuye SCM and/or its affiliates. All rights reserved.
 */
package com.jiuyescm.bms.calculate.vo;

import java.math.BigDecimal;
import java.util.List;

import com.jiuyescm.cfm.domain.IEntity;

/**
 * <功能描述>
 * 
 * @author caojianwei
 * @date 2019年4月18日 下午12:48:12
 */
public class DiscountDispatchReportVo implements IEntity{
    private static final long serialVersionUID = -1L;

    private String serviceTypeCode;
    private Integer sumNum;
    private BigDecimal sumAmount;
    List<DiscountQuoteVo> quotes;
    private String discountType;
    private boolean isDiscount;
    private boolean isRemove;
    
    /**
     *物流产品类型
     */
    public String getServiceTypeCode() {
        return serviceTypeCode;
    }
    
    /**
     *物流产品类型
     */
    public void setServiceTypeCode(String serviceTypeCode) {
        this.serviceTypeCode = serviceTypeCode;
    }
    
    /**
     *总单量
     */
    public Integer getSumNum() {
        return sumNum;
    }
    
    /**
     *总单量
     */
    public void setSumNum(Integer sumNum) {
        this.sumNum = sumNum;
    }
    
    /**
     *总金额
     */
    public BigDecimal getSumAmount() {
        return sumAmount;
    }
    
    /**
     *总金额
     */
    public void setSumAmount(BigDecimal sumAmount) {
        this.sumAmount = sumAmount;
    }
    
    /**
     *折扣报价明细
     */
    public List<DiscountQuoteVo> getQuotes() {
        return quotes;
    }
    
    /**
     *折扣报价明细
     */
    public void setQuotes(List<DiscountQuoteVo> quotes) {
        this.quotes = quotes;
    }
    
    /**
     *是否折扣  true-折扣  false-不折扣
     */
    public boolean isDiscount() {
        return isDiscount;
    }
    
    /**
     *是否折扣  true-折扣  false-不折扣
     */
    public void setDiscount(boolean isDiscount) {
        this.isDiscount = isDiscount;
    }

    /**
     * 是否从上级单量中排除此级单量 true-排除  false-不排除
     */
    public boolean isRemove() {
        return isRemove;
    }

    /**
     * 是否从上级单量中排除此级单量 true-排除  false-不排除
     */
    public void setRemove(boolean isRemove) {
        this.isRemove = isRemove;
    }

    public String getDiscountType() {
        return discountType;
    }

    public void setDiscountType(String discountType) {
        this.discountType = discountType;
    }
    
    
    
}


