/*
 * Copyright (c) 2019, Jiuye SCM and/or its affiliates. All rights reserved.
 */
package com.jiuyescm.bms.jobhandler;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import com.jiuyescm.bms.common.JobParameterHandler;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHander;
import com.xxl.job.core.log.XxlJobLogger;

/**
  * 汇总结算进度报表需要的数据
  * 
  * @author wangchen870
  * @date 2019年6月17日 下午4:33:05
  */
@JobHander(value="judgeProcessReportJob")
@Service
public class JudgeProcessReportJob extends IJobHandler {
    
    private static final Integer batchNum = 2000;
    private static final String SUCCESS = "SUCCESS";
    private static final String FAIL = "FAIL";

    @Override
    public ReturnT<String> execute(String... params) throws Exception {
        XxlJobLogger.log("JudgeProcessReportJob start.");
        XxlJobLogger.log("汇总结算进度报表数据的任务");
        return calcJob(params);
    }
    
    private ReturnT<String> calcJob(String[] params) {
        StopWatch sw = new StopWatch();
        
        //获取定时任务参数
        sw.start("获取定时任务参数");
        Map<String, Object> map = new HashMap<String, Object>();
        if (params != null && params.length > 0) {
            try {
                map = JobParameterHandler.handler(params);// 处理定时任务参数
            } catch (Exception e) {
                sw.stop();
                XxlJobLogger.log("【终止异常】,解析Job配置的参数出现错误,原因:" + e.getMessage() + ",耗时："
                        + sw.getTotalTimeMillis() + "毫秒");
                return ReturnT.FAIL;
            }
        } else {
            // 未配置最多执行多少运单
            map.put("num", batchNum);
        }
        printTime(sw);
        
        
        
        sw.start("任务完成");
        return printLog("任务完成", sw, SUCCESS);
    }

    /**
     * 打印耗时
     * <功能描述>
     * 
     * @author wangchen870
     * @date 2019年6月5日 上午10:56:47
     *
     * @param sw
     */
    private void printTime(StopWatch sw) {
        sw.stop();
        XxlJobLogger.log(sw.getLastTaskName() + ", 耗时：" + sw.getLastTaskTimeMillis() + "毫秒");
    }
    
    /**
     * 日志输出
     * <功能描述>
     * 
     * @author wangchen870
     * @date 2019年5月18日 下午5:29:29
     *
     * @param log
     * @param sw
     * @return
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


