/*
 * Copyright (c) 2019, Jiuye SCM and/or its affiliates. All rights reserved.
 */
package com.jiuyescm.bms.discount;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyescm.bms.base.dictionary.entity.SystemCodeEntity;
import com.jiuyescm.bms.base.dictionary.repository.ISystemCodeRepository;
import com.jiuyescm.bms.biz.discount.entity.BmsDiscountAsynTaskEntity;
import com.jiuyescm.bms.biz.discount.repository.IBmsDiscountAsynTaskRepository;
import com.jiuyescm.bms.calculate.api.IBmsDiscountCalcuService;
import com.jiuyescm.bms.calculate.vo.DiscountDispatchReportVo;
import com.jiuyescm.bms.calculate.vo.DiscountQuoteVo;
import com.jiuyescm.bms.discount.repository.IBmsDiscountRepository;
import com.jiuyescm.bms.quotation.contract.repository.imp.IPriceContractDiscountRepository;
import com.jiuyescm.bms.quotation.discount.entity.BmsQuoteDiscountDetailEntity;
import com.jiuyescm.bms.quotation.discount.entity.BmsQuoteDiscountTemplateEntity;
import com.jiuyescm.bms.quotation.discount.repository.IBmsQuoteDiscountDetailRepository;
import com.jiuyescm.contract.base.vo.ContractDiscountConfigVo;
import com.jiuyescm.exception.BizException;

/**
 * <功能描述>
 * 
 * @author caojianwei
 * @date 2019年4月18日 上午11:33:03
 */
@Service("bmsDiscountCalcuService")
public class BmsDiscountCalcuServiceImpl implements IBmsDiscountCalcuService {
    
    private static final Logger logger = Logger.getLogger(BmsDiscountCalcuServiceImpl.class.getName());


    @Autowired IPriceContractDiscountRepository priceContractDiscountRepository;
    @Autowired IBmsDiscountAsynTaskRepository bmsDiscountAsynTaskRepository;
    @Autowired IBmsQuoteDiscountDetailRepository bmsQuoteDiscountDetailRepository;
    @Autowired private IBmsDiscountRepository bmsDiscountRepository;
    @Autowired ISystemCodeRepository systemCodeRepository;

    @Override
    public Map<String,DiscountDispatchReportVo> discountDispatchBms(BmsDiscountAsynTaskEntity task,List<BmsQuoteDiscountDetailEntity> discountList,BmsQuoteDiscountTemplateEntity template)
            throws BizException {
        
        Map<String,DiscountDispatchReportVo> reportMap=new HashMap<String,DiscountDispatchReportVo>();
        try {
            Map<String,Object> map=new HashMap<>();
            List<DiscountDispatchReportVo> newDiscountList=new ArrayList<>();
            //特殊物流产品类型
            List<String> specialServiceType=getService(task.getCarrierId());
    
            //报价中是否有空的物流产品类型
            boolean flag=false;
            //得到折扣报价中所有的物流产品类型
            List<String> serviceList=new ArrayList<>();
            for(BmsQuoteDiscountDetailEntity discount:discountList){
                if(StringUtils.isNotBlank(discount.getServiceTypeCode())){
                    serviceList.add(discount.getServiceTypeCode());
                }else{
                    flag=true;
                }
            }
            
            Map<String,BmsDiscountAccountEntity> accountMap=new HashMap<>();
            //所有需要折扣的物流产品类型对应得总单量、总金额
            if(serviceList.size()>0){
                map.put("startTime", task.getStartDate());
                map.put("endTime", task.getEndDate());
                map.put("customerId", task.getCustomerId());
                map.put("carrierId", task.getCarrierId());
                map.put("serviceList", serviceList);
                List<BmsDiscountAccountEntity> accountList=bmsDiscountRepository.queryServiceAccount(map);
                for(BmsDiscountAccountEntity en:accountList){
                    accountMap.put(en.getServiceTypeCode(), en);
                }
            }
           
            
            //如果折扣报价中有物流产品类型为空时，需要统计除特殊物流物流产品类型以外的总单量、总金额
            if(flag){
                map.put("serviceList", null);
                map.put("notServiceList", specialServiceType);
                BmsDiscountAccountEntity discountAccountVo=bmsDiscountRepository.queryAccount(map);
                accountMap.put("", discountAccountVo);
            }
      
            //循环所有折扣报价，拼接新的折扣报价
            for(BmsQuoteDiscountDetailEntity discount:discountList){
                String serviceTypeCode=discount.getServiceTypeCode();            
                //判断是否有统计记录
                if(accountMap.containsKey(serviceTypeCode)){
                    BmsDiscountAccountEntity account=accountMap.get(serviceTypeCode);
                    //判断折扣方式
                    Double count=0d;
                    if("MONTH_COUNT".equals(task.getDiscountType())){
                        count=Double.valueOf(account.getOrderCount());
                    }else if("MONTH_AMOUNT".equals(task.getDiscountType())){
                        count=Double.valueOf(account.getAmount());
                    }
                    //如果匹配了
                    if(count>=discount.getDownLimit() && count<discount.getUpLimit()){                        
                        DiscountQuoteVo quotevo=new DiscountQuoteVo();
                        quotevo.setId(discount.getId());
                        quotevo.setUnitPrice(discount.getUnitPrice());
                        quotevo.setUnitRate(discount.getUnitPriceRate());
                        quotevo.setFirstPrice(discount.getFirstPrice());
                        quotevo.setFirstRate(discount.getFirstPriceRate());
                        quotevo.setContinuePrice(discount.getContinuePrice());
                        quotevo.setContinueRate(discount.getContinuePirceRate());
                        quotevo.setStartTime(discount.getStartTime());
                        quotevo.setEndTime(discount.getEndTime());                     
                        
                        if(reportMap.containsKey(serviceTypeCode)){
                            DiscountDispatchReportVo vo=reportMap.get(serviceTypeCode);
                            if(vo.getQuotes()!=null){
                                vo.getQuotes().add(quotevo);
                            }
                        }else{
                            DiscountDispatchReportVo vo=new DiscountDispatchReportVo();   
                            vo.setServiceTypeCode(serviceTypeCode);                       
                            List<DiscountQuoteVo> quotes=new ArrayList<DiscountQuoteVo>();
                            quotes.add(quotevo);
                            vo.setQuotes(quotes);
                            reportMap.put(serviceTypeCode, vo);
                            newDiscountList.add(vo);
                        }
                    }
                    
                }
                
            }

        } catch (Exception e) {
            // TODO: handle exception
            logger.error("BMS折扣报价处理失败", e);
            return null;
        }
        return reportMap;
    }

    
    @Override
    public Map<String, DiscountDispatchReportVo> discountDispatchContract(BmsDiscountAsynTaskEntity task,
            List<ContractDiscountConfigVo> discountList) throws BizException{
        // TODO Auto-generated method stub
        Map<String,DiscountDispatchReportVo> reportMap=new HashMap<String,DiscountDispatchReportVo>();
        try {   
            Map<String,Object> map=new HashMap<>();
            List<DiscountDispatchReportVo> newDiscountList=new ArrayList<>();
            //特殊物流产品类型
            List<String> specialServiceType=getService(task.getCarrierId());
            //报价中是否有空的物流产品类型
            boolean flag=false;
            //得到折扣报价中所有的物流产品类型
            List<String> serviceList=new ArrayList<>();
            for(ContractDiscountConfigVo discount:discountList){
                if(StringUtils.isNotBlank(discount.getCarrierServiceType())){
                    serviceList.add(discount.getCarrierServiceType());
                }else{
                    flag=true;
                }
            }
                   
            //所有需要折扣的物流产品类型对应得总单量、总金额
            Map<String,BmsDiscountAccountEntity> accountMap=new HashMap<>();
            if(serviceList.size()>0){
                map.put("startTime", task.getStartDate());
                map.put("endTime", task.getEndDate());
                map.put("customerId", task.getCustomerId());
                map.put("carrierId", task.getCarrierId());
                map.put("serviceList", serviceList);
                List<BmsDiscountAccountEntity> accountList=bmsDiscountRepository.queryServiceAccount(map);
                for(BmsDiscountAccountEntity en:accountList){
                    accountMap.put(en.getServiceTypeCode(), en);
                }
            }

            
            //如果折扣报价中有物流产品类型为空时，需要统计除特殊物流物流产品类型以外的总单量、总金额
            if(flag){
                map.put("serviceList", null);
                map.put("notServiceList", specialServiceType);
                BmsDiscountAccountEntity discountAccountVo=bmsDiscountRepository.queryAccount(map);
                accountMap.put("", discountAccountVo);
            }
      
            //循环所有折扣报价，拼接新的折扣报价
            for(ContractDiscountConfigVo discount:discountList){
                String serviceTypeCode=discount.getCarrierServiceType();            
                //判断是否有统计记录
                if(accountMap.containsKey(serviceTypeCode)){
                    BmsDiscountAccountEntity account=accountMap.get(serviceTypeCode);
                    //判断折扣方式
                    BigDecimal count=new BigDecimal(0d);
                    if("MONTH_COUNT".equals(task.getDiscountType())){
                        count=new BigDecimal(account.getOrderCount());
                    }else if("MONTH_AMOUNT".equals(task.getDiscountType())){
                        count=new BigDecimal(account.getAmount());
                    }
                    //如果匹配了
                    if(count.compareTo(discount.getLowerLimit())>=0 && discount.getUpperLimit().compareTo(count)>0){                        
                        DiscountQuoteVo quotevo=new DiscountQuoteVo();
                        quotevo.setId(discount.getId());
                        if(discount.getTotalDiscountPrice()!=null){
                            quotevo.setUnitPrice(discount.getTotalDiscountPrice().doubleValue());
                        }
                        if(discount.getTotalDiscountRate()!=null){
                            quotevo.setUnitRate(discount.getTotalDiscountRate().doubleValue());
                        }
                        if(discount.getFirstWeightDiscountPrice()!=null){
                            quotevo.setFirstPrice(discount.getFirstWeightDiscountPrice().doubleValue());
                        }
                        if(discount.getFirstWeightDiscountRate()!=null){
                            quotevo.setFirstRate(discount.getFirstWeightDiscountRate().doubleValue());
                        }
                        if(discount.getContinueWeightDiscountPrice()!=null){
                            quotevo.setContinuePrice(discount.getContinueWeightDiscountPrice().doubleValue());
                        }                        
                        if(discount.getContinueWeightDiscountRate()!=null){
                            quotevo.setContinueRate(discount.getContinueWeightDiscountRate().doubleValue());
                        }
          
                        if(reportMap.containsKey(serviceTypeCode)){
                            DiscountDispatchReportVo vo=reportMap.get(serviceTypeCode);
                            if(vo.getQuotes()!=null){
                                vo.getQuotes().add(quotevo);
                            }
                        }else{
                            DiscountDispatchReportVo vo=new DiscountDispatchReportVo();   
                            vo.setServiceTypeCode(serviceTypeCode);                       
                            List<DiscountQuoteVo> quotes=new ArrayList<DiscountQuoteVo>();
                            quotes.add(quotevo);
                            vo.setQuotes(quotes);
                            reportMap.put(serviceTypeCode, vo);
                            newDiscountList.add(vo);
                        }
                    }               
                }          
            }
        } catch (Exception e) {
            // TODO: handle exception
            logger.error("合同在线折扣报价处理失败", e);
        }
        return reportMap;
    }
    
    private List<String> getService(String carrierid){
        List<String> serviceList=new ArrayList<String>();
        Map<String,Object> map=new HashMap<String,Object>();
        map.put("typeCode", "DISCOUNT_SERVICE_CODE");
        map.put("code", carrierid);
        List<SystemCodeEntity> discountServiceList = systemCodeRepository.queryCodeList(map);
        for(SystemCodeEntity s:discountServiceList){
            serviceList.add(s.getExtattr1());
        }       
        return serviceList;
    }


    
}


