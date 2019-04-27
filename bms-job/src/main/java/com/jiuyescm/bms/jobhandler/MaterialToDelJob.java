/*
 * Copyright (c) 2019, Jiuye SCM and/or its affiliates. All rights reserved.
 */
package com.jiuyescm.bms.jobhandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.jiuyescm.bms.biz.storage.entity.BizOutstockPackmaterialTempEntity;
import com.jiuyescm.bms.biz.storage.repository.IBizOutstockPackmaterialRepository;
import com.jiuyescm.bms.biz.storage.repository.IBizOutstockPackmaterialTempRepository;
import com.jiuyescm.bms.correct.repository.ElConditionRepository;
import com.jiuyescm.bms.file.asyn.BmsFileAsynTaskEntity;
import com.jiuyescm.bms.file.asyn.repository.IBmsFileAsynTaskRepository;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHander;
import com.xxl.job.core.log.XxlJobLogger;

/**
 * 耗材作废Job
  * <作废使用标准包装方案的运单对应的耗材>
  * 
  * @author wangchen870
  * @date 2019年4月19日 上午10:23:21
  */
@JobHander(value="materialToDelJob")
@Service
public class MaterialToDelJob extends IJobHandler {

    @Autowired
    private ElConditionRepository elConditionRepository;
    @Autowired
    private IBmsFileAsynTaskRepository bmsFileAsynTaskRepository;
    @Autowired
    private IBizOutstockPackmaterialTempRepository bizOutstockPackmaterialTempRepository;
    @Autowired
    private IBizOutstockPackmaterialRepository bizOutstockPackmaterialRepository;
    
    private static Map<String, Object> map = new HashMap<>();
    static{
        map.put("pullType", "packageBill");
        map.put("taskType", "BMS.QUEUE.PACKMATERIALIMPORT.TASK");
    }
    
    @Override
    public ReturnT<String> execute(String... params) throws Exception {
        XxlJobLogger.log("MaterialToDelJob start.");
        XxlJobLogger.log("耗材作废任务");
        return calcJob(params);
    }
    
    private ReturnT<String> calcJob(String[] params) {
        StopWatch sw = new StopWatch();
        sw.start();
        //查出最近一次的taskId和lastTime
        BmsFileAsynTaskEntity taskEntity = bmsFileAsynTaskRepository.queryMinTask(map);
        if (null == taskEntity) {
            return printLog("没有需要执行的任务", sw);
        }
        
        //根据taskId找到需要对应的运单
        List<BizOutstockPackmaterialTempEntity> tempList = bizOutstockPackmaterialTempRepository.queryWaybillByTaskId(taskEntity.getTaskId());
        if (CollectionUtils.isEmpty(tempList)) {
            //更新etl表的时间
            updateLastTimeToEtl(taskEntity);
            return printLog("还未抽取运单", sw);
        }
        
        //统计需要作废的耗材的运单号
        List<String> waybillNos = new ArrayList<String>();
        for (BizOutstockPackmaterialTempEntity tempEntity : tempList) {
            //使用标准包装方案
            if (null != tempEntity.getPackGroupNo()) {
                waybillNos.add(tempEntity.getWaybillNo());
            }
        }
        
        //作废使用标准包装方案的耗材，不使用标准包装方案的不用管
        if (CollectionUtils.isEmpty(waybillNos)) {
            XxlJobLogger.log("没有运单需要使用标准包装方案，因此不需要作废");
        }else {       
            XxlJobLogger.log("开始作废使用标准包装方案运单的耗材，运单条数：{0}", waybillNos.size());
            bizOutstockPackmaterialRepository.deleteMaterialForUsePackage(waybillNos);
        }
        
        //更新etl表的时间
        updateLastTimeToEtl(taskEntity);
        
        return printLog("任务完成", sw);
    }

    private void updateLastTimeToEtl(BmsFileAsynTaskEntity taskEntity) {
        Map<String, Object> cond = new HashMap<String, Object>();
        cond.put("pullType", "packageBill");
        cond.put("lastTime", taskEntity.getCreateTime());
        elConditionRepository.updateByPullType(cond);
    }
    
    private ReturnT<String> printLog(String log, StopWatch sw){
        sw.stop();
        XxlJobLogger.log(log + ", 共耗时：" + sw.getTotalTimeMillis() + "毫秒");
        return ReturnT.SUCCESS;
    }

}


