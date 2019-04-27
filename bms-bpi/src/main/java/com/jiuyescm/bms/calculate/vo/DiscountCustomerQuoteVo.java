/*
 * Copyright (c) 2019, Jiuye SCM and/or its affiliates. All rights reserved.
 */
package com.jiuyescm.bms.calculate.vo;

import java.math.BigDecimal;
import java.util.Map;

import com.jiuyescm.cfm.domain.IEntity;

/**
 * <功能描述>
 * 
 * @author caojianwei
 * @date 2019年4月18日 上午11:38:13
 */
public class DiscountCustomerQuoteVo implements IEntity{

    private static long serialVersionUID = -5044284311403590905L;
    
    private String customerId;
    
    private String customerName;
    private String createMonth;
    private String carrierId;
    private Integer sumNum;
    private BigDecimal sumAmount;
    
    private Map<String, DiscountDispatchReportVo> dispatchDetailsMap;
    
    private boolean isDiscount;
    
    /**
     * 商家ID
     */
    public String getCustomerId() {
        return customerId;
    }
    
    /**
     * 商家ID
     */
    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }
    
    /**
     * 商家名称
     */
    public String getCustomerName() {
        return customerName;
    }
    
    /**
     * 商家名称
     */
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
    
    /**
     * 折扣年月
     */
    public String getCreateMonth() {
        return createMonth;
    }
    
    /**
     * 折扣年月
     */
    public void setCreateMonth(String createMonth) {
        this.createMonth = createMonth;
    }
    
    /**
     * 物流商ID
     */
    public String getCarrierId() {
        return carrierId;
    }
    
    /**
     * 物流商ID
     */
    public void setCarrierId(String carrierId) {
        this.carrierId = carrierId;
    }
    
    /**
     * 总单量
     */
    public Integer getSumNum() {
        return sumNum;
    }
    
    /**
     * 总单量
     */
    public void setSumNum(Integer sumNum) {
        this.sumNum = sumNum;
    }
    
    /**
     * 总金额
     */
    public BigDecimal getSumAmount() {
        return sumAmount;
    }
    
    /**
     * 总金额
     */
    public void setSumAmount(BigDecimal sumAmount) {
        this.sumAmount = sumAmount;
    }
    /**
     *是否折扣 true-折扣 false-不折扣
     */
    public boolean isDiscount() {
        return isDiscount;
    }

    /**
     *是否折扣 true-折扣 false-不折扣
     */
    public void setDiscount(boolean isDiscount) {
        this.isDiscount = isDiscount;
    }

    /**
     * 配送折扣汇总
     */
    public Map<String, DiscountDispatchReportVo> getDispatchDetailsMap() {
        return dispatchDetailsMap;
    }

    /**
     * 配送折扣汇总
     */
    public void setDispatchDetailsMap(Map<String, DiscountDispatchReportVo> dispatchDetailsMap) {
        this.dispatchDetailsMap = dispatchDetailsMap;
    }
    

}


