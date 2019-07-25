/*
 * Copyright (c) 2019, Jiuye SCM and/or its affiliates. All rights reserved.
 */
package com.jiuyescm.bms.jobhandler;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import com.google.common.collect.Maps;
import com.jiuyescm.bms.billcheck.BillCheckReceiptEntity;
import com.jiuyescm.bms.billcheck.repository.IBillCheckReceiptRepository;
import com.jiuyescm.bms.billcheck.service.IBillCheckInfoService;
import com.jiuyescm.bms.correct.ElConditionEntity;
import com.jiuyescm.bms.correct.repository.ElConditionRepository;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHander;
import com.xxl.job.core.log.XxlJobLogger;

/**
  * 回款明细推送Crm
  * 
  * @author wangchen
  * @date 2019年7月5日 上午10:54:16
  */
@JobHander(value="crmBillCheckReceiptJob")
@Service
public class CrmBillCheckReceiptJob extends IJobHandler {
    
    @Autowired
    private ElConditionRepository elConditionRepository;
    @Autowired
    private IBillCheckInfoService billCheckInfoService;
    @Autowired
    private IBillCheckReceiptRepository billCheckReceiptRepository;
    
    private static final String SUCCESS = "SUCCESS";
    private static final String FAIL = "FAIL";
    
    @Override
    public ReturnT<String> execute(String... params) throws Exception {
        XxlJobLogger.log("CrmBillCheckReceiptJob start.");
        XxlJobLogger.log("开始回款明细推送到CRM");
        return CalcJob(params);
    }
    
    private ReturnT<String> CalcJob(String[] params) {
        StopWatch sw = new StopWatch();
        Map<String, Object> map = new HashMap<String, Object>();
        Map<String, Object> elCondition = Maps.newLinkedHashMap();     
        elCondition.put("pullType", "bill_check_receipt_crm");
        List<ElConditionEntity> elList =  elConditionRepository.query(elCondition);
        if(CollectionUtils.isEmpty(elList)) return printLog("etl_condition表未配置，请去配置", sw, FAIL);
        ElConditionEntity elEntity = elList.get(0);
        if(null == elEntity.getLastTime()) return printLog("etl_condition表未配置时间，请去配置", sw, FAIL);
        elCondition.put("lastTime", new Timestamp(System.currentTimeMillis()));
        map.put("startTime", elEntity.getLastTime());
        map.put("endTime", new Timestamp(System.currentTimeMillis()));
        
        try {
            XxlJobLogger.log("查询条件map:【{0}】  ",map);        
            //根据时间查询需要推送的回款明细
            List<BillCheckReceiptEntity> list=billCheckReceiptRepository.queryReceiptToCrm(map);
            for(BillCheckReceiptEntity entity:list){
                if ("0".equals(entity.getDelFlag())) {
                    //新增/修改
                    XxlJobLogger.log("开始调用新增/修改接口");
                    billCheckInfoService.saveReceiptToCrm(entity);
                }else {
                    //删除
                    XxlJobLogger.log("开始调用删除接口");
                    billCheckInfoService.deleteReceiptToCrm(entity);
                }
            }
        } catch (Exception e) {
            XxlJobLogger.log("推送异常：{0}", e); 
            elConditionRepository.updateByPullType(elCondition);
            return printLog("推送异常", sw, FAIL);
        }
        
        elConditionRepository.updateByPullType(elCondition);
        sw.start("任务完成");
        return printLog("任务完成", sw, SUCCESS);  
    }

    /*
     * 日志输出
     */
    private ReturnT<String> printLog(String log, StopWatch sw, String status){
        sw.stop();
        XxlJobLogger.log(log + ", 共耗时：" + sw.getTotalTimeMillis() + "毫秒");
        if (SUCCESS.equals(status)) {
            return ReturnT.SUCCESS;
        }else {
            return ReturnT.FAIL; 
        }
    }

}


