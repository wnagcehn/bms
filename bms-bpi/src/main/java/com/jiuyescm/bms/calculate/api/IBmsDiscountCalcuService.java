/*
 * Copyright (c) 2019, Jiuye SCM and/or its affiliates. All rights reserved.
 */
package com.jiuyescm.bms.calculate.api;

import java.util.List;
import java.util.Map;

import com.jiuyescm.bms.biz.discount.entity.BmsDiscountAsynTaskEntity;
import com.jiuyescm.bms.calculate.vo.DiscountDispatchReportVo;
import com.jiuyescm.bms.quotation.discount.entity.BmsQuoteDiscountDetailEntity;
import com.jiuyescm.bms.quotation.discount.entity.BmsQuoteDiscountTemplateEntity;
import com.jiuyescm.contract.base.vo.ContractDiscountConfigVo;
import com.jiuyescm.exception.BizException;

/**
 * <功能描述>
 * BMS折扣计算服务
 * @author caojianwei
 * @date 2019年4月18日 上午11:16:51
 */
public interface IBmsDiscountCalcuService {
 
    /**
     * BMS折扣
     * <功能描述>
     * 
     * @author zhaofeng
     * @date 2019年6月15日 下午12:15:48
     *
     * @param task
     * @param template
     * @return
     * @throws BizException
     */
    Map<String,DiscountDispatchReportVo> discountDispatchBms(BmsDiscountAsynTaskEntity task,List<BmsQuoteDiscountDetailEntity> priceList,BmsQuoteDiscountTemplateEntity template);
    
    /**
     * 合同在线折扣
     * <功能描述>
     * 
     * @author zhaofeng
     * @date 2019年6月15日 下午12:15:48
     *
     * @param task
     * @param template
     * @return
     * @throws BizException
     */
    Map<String,DiscountDispatchReportVo> discountDispatchContract(BmsDiscountAsynTaskEntity task,List<ContractDiscountConfigVo> priceList);
   
}


