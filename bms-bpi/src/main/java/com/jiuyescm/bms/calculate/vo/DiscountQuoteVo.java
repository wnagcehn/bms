/*
 * Copyright (c) 2019, Jiuye SCM and/or its affiliates. All rights reserved.
 */
package com.jiuyescm.bms.calculate.vo;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * <功能描述>
 * 
 * @author caojianwei
 * @date 2019年4月18日 下午12:41:45
 */
public class DiscountQuoteVo {

    // 整单折扣率
    private BigDecimal unitRate;
    // 整单折扣价
    private BigDecimal unitPrice;
    // 首量折扣率
    private BigDecimal firstRate;
    // 首量折扣价
    private BigDecimal firstPrice;
    // 续量折扣率
    private BigDecimal continueRate;
    // 续量折扣价
    private BigDecimal continuePrice;
    //报价生效时间
    private Timestamp startTime;
    //报价失效时间
    private Timestamp endTime;
    
    /**
     *整单折扣率
     */
    public BigDecimal getUnitRate() {
        return unitRate;
    }
    
    /**
     *整单折扣率
     */
    public void setUnitRate(BigDecimal unitRate) {
        this.unitRate = unitRate;
    }
    
    /**
     *整单折扣价
     */
    public BigDecimal getUnitPrice() {
        return unitPrice;
    }
    
    /**
     *首量折扣价
     */
    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }
    
    /**
     *首量折扣价
     */
    public BigDecimal getFirstRate() {
        return firstRate;
    }
    
    /**
     *首量折扣率
     */
    public void setFirstRate(BigDecimal firstRate) {
        this.firstRate = firstRate;
    }
    
    /**
     *首量折扣价
     */
    public BigDecimal getFirstPrice() {
        return firstPrice;
    }
    
    /**
     *首量折扣价
     */
    public void setFirstPrice(BigDecimal firstPrice) {
        this.firstPrice = firstPrice;
    }
    
    /**
     *续量折扣率
     */
    public BigDecimal getContinueRate() {
        return continueRate;
    }
    
    /**
     *续量折扣率
     */
    public void setContinueRate(BigDecimal continueRate) {
        this.continueRate = continueRate;
    }
    
    /**
     *续量折扣价
     */
    public BigDecimal getContinuePrice() {
        return continuePrice;
    }
    
    /**
     *续量折扣价
     */
    public void setContinuePrice(BigDecimal continuePrice) {
        this.continuePrice = continuePrice;
    }
    
    /**
     *折扣启用时间
     */
    public Timestamp getStartTime() {
        return startTime;
    }
    
    /**
     *折扣启用时间
     */
    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }
    
    /**
     *折扣弃用时间
     */
    public Timestamp getEndTime() {
        return endTime;
    }
    
    /**
     *折扣弃用时间
     */
    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }
    
}


