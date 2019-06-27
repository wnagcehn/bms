/*
 * Copyright (c) 2019, Jiuye SCM and/or its affiliates. All rights reserved.
 */
package com.jiuyescm.bms.calculate.vo;

import java.sql.Timestamp;

import com.jiuyescm.cfm.domain.IEntity;

/**
 * <功能描述>
 * 
 * @author caojianwei
 * @date 2019年4月18日 下午12:41:45
 */
public class DiscountQuoteVo implements IEntity{
    private static final long serialVersionUID = -1L;
    private Integer id;
    // 整单折扣率
    private Double unitRate;
    // 整单折扣价
    private Double unitPrice;
    // 首量折扣率
    private Double firstRate;
    // 首量折扣价
    private Double firstPrice;
    // 续量折扣率
    private Double continueRate;
    // 续量折扣价
    private Double continuePrice;
    //报价生效时间
    private Timestamp startTime;
    //报价失效时间
    private Timestamp endTime;
    
  

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getUnitRate() {
        return unitRate;
    }

    public void setUnitRate(Double unitRate) {
        this.unitRate = unitRate;
    }

    public Double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Double getFirstRate() {
        return firstRate;
    }

    public void setFirstRate(Double firstRate) {
        this.firstRate = firstRate;
    }

    public Double getFirstPrice() {
        return firstPrice;
    }

    public void setFirstPrice(Double firstPrice) {
        this.firstPrice = firstPrice;
    }

    public Double getContinueRate() {
        return continueRate;
    }

    public void setContinueRate(Double continueRate) {
        this.continueRate = continueRate;
    }

    public Double getContinuePrice() {
        return continuePrice;
    }

    public void setContinuePrice(Double continuePrice) {
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


