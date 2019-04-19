/*
 * Copyright (c) 2019, Jiuye SCM and/or its affiliates. All rights reserved.
 */
package com.jiuyescm.bms.calculate.api;

import com.jiuyescm.bms.calculate.vo.DiscountCustomerQuoteVo;
import com.jiuyescm.exception.BizException;

/**
 * <功能描述>
 * BMS折扣计算服务
 * @author caojianwei
 * @date 2019年4月18日 上午11:16:51
 */
public interface IBmsDiscountCalcuService {

    /**
     * 
     * <功能描述>
     * bms所有折扣统计
     * @author caojianwei
     * @date 2019年4月18日 上午11:18:53
     * @param customerId 商家ID
     * @param createMonth 折扣年月 格式 '2019-01'
     * @return
     * @throws BizException
     */
    DiscountCustomerQuoteVo discountReport(String customerId,String createMonth) throws BizException;
    
    /**
     * <功能描述>
     * bms配送指定物流商折扣统计
     * @author caojianwei
     * @date 2019年4月18日 下午1:10:31
     * @param customerId 商家ID
     * @param createMonth 折扣年月 格式 '2019-01'
     * @param carrierId 物流商ID
     * @return
     * @throws BizException
     */
    DiscountCustomerQuoteVo discountReportForCarrier(String customerId,String createMonth,String carrierId) throws BizException;
    
}


