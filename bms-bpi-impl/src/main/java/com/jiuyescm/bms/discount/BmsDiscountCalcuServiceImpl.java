/*
 * Copyright (c) 2019, Jiuye SCM and/or its affiliates. All rights reserved.
 */
package com.jiuyescm.bms.discount;

import org.springframework.beans.factory.annotation.Autowired;

import com.jiuyescm.bms.calculate.api.IBmsCalcuService;
import com.jiuyescm.bms.calculate.api.IBmsDiscountCalcuService;
import com.jiuyescm.bms.calculate.vo.DiscountCustomerQuoteVo;
import com.jiuyescm.bms.constant.common.ContractAttrConstant;
import com.jiuyescm.bs.util.StringUtil;
import com.jiuyescm.exception.BizException;

/**
 * <功能描述>
 * 
 * @author caojianwei
 * @date 2019年4月18日 上午11:33:03
 */
public class BmsDiscountCalcuServiceImpl implements IBmsDiscountCalcuService {

    @Autowired IBmsCalcuService bmsCalcuService;
    
    @Override
    public DiscountCustomerQuoteVo discountReport(String customerId, String createMonth) throws BizException {
      //判定合同归属
        //bms-查询bms折扣报价
        //contract-查询合同在线折扣报价
        //报价格式统一
        //折扣类目汇总
        //折扣类目统计
        //报价帅选
        //返回标准折扣报价格式
        return null;
    }

    @Override
    public DiscountCustomerQuoteVo discountReportForCarrier(String customerId, String createMonth, String carrierId)
            throws BizException {
        //判定合同归属
        String contractAttr = bmsCalcuService.queryContractAttr(customerId);
        if(StringUtil.isEmpty(contractAttr)){
            throw new BizException("合同归属不存在");
        }
        if(ContractAttrConstant.BMS.equals(contractAttr)){
            //查询所有bms折扣报价信息
            //如果报价列表中物流产品类型均不为空，物流商不需要折扣 isDiscount=false 否者需要折扣 isDiscount=true
            //如果报价列表中物流产品类型存在值，则需要对此物流产品类型进行折扣
              //DiscountDispatchReportVo isDiscount=true isRemove=true
            //如果物流产品类型包含九曳承诺达，无需查询数据字段
            //如果物流产品类型不包含九曳承诺达，查询数据字段，
              //如果配置，则将九曳承诺达加入DiscountDispatchReportVo isDiscount=false isRemove=true;
            //如果物流商维度需要折扣，统计
            //遍历DiscountDispatchReportVo，
               //如果isDiscount=false 并且 isRemove=false，则无需统计单量和金额，否则需要统计
               //统计完成后，更新DiscountDispatchReportVo
                 //遍历DiscountDispatchReportVo，将isRemove=true的单量相加 并和物流商单量做减法
        }
        else{
            
        }
        return null;
    }

    
    
    
}


