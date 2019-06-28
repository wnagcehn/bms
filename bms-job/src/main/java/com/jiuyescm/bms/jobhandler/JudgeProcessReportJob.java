/*
 * Copyright (c) 2019, Jiuye SCM and/or its affiliates. All rights reserved.
 */
package com.jiuyescm.bms.jobhandler;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.google.common.collect.Maps;
import com.jiuyescm.bms.base.customer.entity.PubCustomerEntity;
import com.jiuyescm.bms.base.customer.repository.IPubCustomerRepository;
import com.jiuyescm.bms.base.file.entity.BillPrepareExportTaskEntity;
import com.jiuyescm.bms.bill.customer.BillCustomerDetailEntity;
import com.jiuyescm.bms.bill.customer.repository.IBillCustomerDetailRepository;
import com.jiuyescm.bms.billcheck.BillCheckInfoEntity;
import com.jiuyescm.bms.common.JobParameterHandler;
import com.jiuyescm.bms.correct.ElConditionEntity;
import com.jiuyescm.bms.correct.repository.ElConditionRepository;
import com.jiuyescm.bms.general.entity.BillCustomerMasterEntity;
import com.jiuyescm.bms.general.service.IBillCustomerMasterService;
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
    
    @Autowired
    private ElConditionRepository elConditionRepository;
    @Autowired
    private IBillCustomerMasterService billCustomerMasterService;
    @Autowired
    private IBillCustomerDetailRepository billCustomerDetailRepository;
    @Autowired
    private IPubCustomerRepository pubCustomerRepository;
    
    private static final Integer batchNum = 2000;
    private static final String SUCCESS = "SUCCESS";
    private static final String FAIL = "FAIL";
    private static SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

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
        
        // 从etl_condition表获取时间
        sw.start("从etl_condition表获取时间");
        Map<String, Object> elCondition = Maps.newLinkedHashMap();
        elCondition.put("pullType", "bill_customer_master");
        List<ElConditionEntity> elList =  elConditionRepository.query(elCondition);
        if(CollectionUtils.isEmpty(elList)) return printLog("etl_condition表未配置，请去配置", sw, FAIL);
        ElConditionEntity elEntity = elList.get(0);
        Timestamp startTime = elEntity.getLastTime();
        if(null == startTime) return printLog("etl_condition表未配置时间，请去配置", sw, FAIL);
        elCondition.put("lastTime", new Timestamp(System.currentTimeMillis()));
        printTime(sw);
        XxlJobLogger.log("etl表获取的时间为：" + startTime);
        
        // 汇总主表数据（BMS计算的和账单导入的）
        sw.start("汇总主表数据");
        map.put("startTime", startTime);
        map.put("endTime", new Timestamp(System.currentTimeMillis()));
        List<BillCustomerMasterEntity> taskAndCheckLogs = billCustomerMasterService.queryCalcuTaskLogAndCheckLog(map);
        for (BillCustomerMasterEntity masterEntity : taskAndCheckLogs) {
            masterEntity.setIsCalculated("0");
            masterEntity.setCreTime(new Timestamp(System.currentTimeMillis()));
            masterEntity.setModTime(new Timestamp(System.currentTimeMillis()));
        }
        if (CollectionUtils.isNotEmpty(taskAndCheckLogs)) {
            XxlJobLogger.log("汇总计算表、预账单、账单日志表，共：" + taskAndCheckLogs.size() + " 条");
            try {
                billCustomerMasterService.saveOrUpdate(taskAndCheckLogs);
            } catch (Exception e) {
                XxlJobLogger.log("主表操作异常：", e);
                elConditionRepository.updateByPullType(elCondition);
                return printLog("主表操作", sw, FAIL);
            }
        }
        printTime(sw);
        
        // 查询主表，组装子表数据
        sw.start("查询主表，组装子表数据");
        map.put("isCalculated", "0");
        List<BillCustomerMasterEntity> masterList = billCustomerMasterService.query(map);
        if (CollectionUtils.isEmpty(masterList)) return printLog("没有数据需要汇总", sw, SUCCESS);
        XxlJobLogger.log("主表需要处理的数据存在：" + masterList.size() + " 条");
        printTime(sw);
        List<BillCustomerDetailEntity> detailList = new ArrayList<BillCustomerDetailEntity>();
        for (BillCustomerMasterEntity masterEntity : masterList) {
            sw.start("统计商家:" + masterEntity.getMkId() + ",月份:" + masterEntity.getCreateMonth() + "数据");
            masterEntity.setIsCalculated("1");
            masterEntity.setModTime(new Timestamp(System.currentTimeMillis()));
            BillCustomerDetailEntity detail = new BillCustomerDetailEntity();      
            Map<String, Object> param = new LinkedHashMap<String, Object>();
            param.put("mkId", masterEntity.getMkId());
            param.put("createMonth", masterEntity.getCreateMonth()); 
            param.put("creMonth", masterEntity.getCreateMonth()); 
            param.put("delFlag", "0");
            try {
                //日期转换（预账单查询条件，原格式：201905）
                StringBuilder startB = new StringBuilder(String.valueOf(masterEntity.getCreateMonth()).substring(0,4)).
                        append("-").append(String.valueOf(masterEntity.getCreateMonth()).substring(4,6)).
                        append("-01 00:00:00"); // 2019-05-01 00:00:00
                Calendar cl = Calendar.getInstance();
                cl.setTime(sd.parse(startB.toString()));
                cl.add(Calendar.MONTH, 1);
                String endB = sd.format(cl.getTime()); // 2019-06-01 00:00:00
                param.put("startTime", Timestamp.valueOf(startB.toString()));
                param.put("endTime", Timestamp.valueOf(endB));
                
                // 删除子表的数据，重新组装
                billCustomerDetailRepository.deleteByMaster(param);
                detail.setMkId(masterEntity.getMkId());
                detail.setMkInvoiceName(masterEntity.getMkInvoiceName());
                detail.setCreateMonth(masterEntity.getCreateMonth());
                detail.setCreTime(new Timestamp(System.currentTimeMillis()));
                // 1.组装bms计算的数据
                BillCustomerDetailEntity bmsCalcuData = billCustomerMasterService.queryBmsCalcuData(param);
                if (null != bmsCalcuData) {
                    detail.setPrepareAmount(bmsCalcuData.getPrepareAmount()==null?0d:bmsCalcuData.getPrepareAmount());
                }else {
                    detail.setPrepareAmount(0d);
                }
                // 2.组装生成预账单的数据
                List<BillPrepareExportTaskEntity> prepareList = billCustomerMasterService.queryPrepareData(param);
                List<BillPrepareExportTaskEntity> prepareIsChildList = billCustomerMasterService.queryPrepareIsChildData(param);
                List<PubCustomerEntity> cusList = pubCustomerRepository.query(param);
                if (CollectionUtils.isEmpty(prepareList)) {
                    detail.setIsPrepare("0");
                }else {
                    detail.setPrepareTime(prepareList.get(0).getCreateTime());                   
                    detail.setBalanceName(prepareList.get(0).getCreator());
                    boolean exe = false;
                    for (BillPrepareExportTaskEntity prepareEntity : prepareList) {
                        if ("1".equals(prepareEntity.getIsChildCustomer())) {
                            exe = true;
                            break;
                        }
                    }
                    // 按主商家生成
                    if (exe) {
                        detail.setIsPrepare("1");
                    }
                    // 按子商家生成
                    else {
                        if (prepareIsChildList.size() >= cusList.size()) {
                            detail.setIsPrepare("1");
                        }else {
                            detail.setIsPrepare("2");
                        }  
                    }
                                        
                }
//                // 预账单总金额
//                BillCustomerDetailEntity prepareData = billCustomerMasterService.queryPrepareAmount(param);
//                if (null != prepareData) {
//                    detail.setPrepareAmount(prepareData.getPrepareAmount()==null?0d:prepareData.getPrepareAmount());
//                }else {
//                    detail.setPrepareAmount(0d);
//                }
                
                // 3.组装账单跟踪的数据                 201905 -> 1905
                param.put("createMonth", String.valueOf(masterEntity.getCreateMonth()).substring(2, 6));
                List<BillCheckInfoEntity> billCheckList = billCustomerMasterService.queryBillImportData(param);
                if (CollectionUtils.isNotEmpty(billCheckList)) {
                    detail.setIsImport("1");
                    if (billCheckList.size() > 1) {
                        for (int i = 1; i < billCheckList.size(); i++) {
                            billCheckList.get(i).setAccountAmount(BigDecimal.ZERO);
                        }
                    }else {
                        billCheckList.get(0).setAccountAmount(new BigDecimal(detail.getPrepareAmount()));
                    }
                    for (BillCheckInfoEntity billCheckInfoEntity : billCheckList) {
                        detail.setPrepareAmount(billCheckInfoEntity.getAccountAmount()==null?0d:billCheckInfoEntity.getAccountAmount().doubleValue());
                        detail.setBalanceName(billCheckInfoEntity.getBalanceName());
                        detail.setCheckId(billCheckInfoEntity.getId());
                        detailList.add(detail);
                    }
                }else {
                    detail.setIsImport("0");
                    detailList.add(detail);
                }      
            } catch (Exception e) {
                XxlJobLogger.log("商家:" + masterEntity.getMkId() + ",月份:" + masterEntity.getCreateMonth()+ "处理异常：{0}", e);
                masterEntity.setIsCalculated("2");
                printTime(sw);
                continue;
            }
            printTime(sw);
        }    
        
        sw.start("往detail表写入数据");
        try {
            billCustomerDetailRepository.saveBatch(detailList);  
        } catch (Exception e) {
            XxlJobLogger.log("保存detail表异常：", e); 
            return printLog("保存Detail表异常", sw, FAIL);
        }
        printTime(sw);
        
        sw.start("更新master表数据");
        try {
            billCustomerMasterService.updateBatch(masterList);
        } catch (Exception e) {
            XxlJobLogger.log("更新master表异常：", e); 
            return printLog("更新master表异常", sw, FAIL);
        }
        printTime(sw);
        
        elConditionRepository.updateByPullType(elCondition);
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


