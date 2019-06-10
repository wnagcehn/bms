/*
 * Copyright (c) 2019, Jiuye SCM and/or its affiliates. All rights reserved.
 */
package com.jiuyescm.bms.jobhandler;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.jiuyescm.bms.base.dict.api.IPubPackageDictService;
import com.jiuyescm.bms.base.dict.entity.PubPackageDictEntity;
import com.jiuyescm.bms.biz.dispatch.entity.BizDispatchPackageEntity;
import com.jiuyescm.bms.biz.storage.entity.BizOutstockPackmaterialCancelEntity;
import com.jiuyescm.bms.biz.storage.entity.BizOutstockPackmaterialEntity;
import com.jiuyescm.bms.biz.storage.repository.IBizOutstockPackmaterialRepository;
import com.jiuyescm.bms.biz.storage.service.IBizOutstockPackmaterialCancelService;
import com.jiuyescm.bms.common.JobParameterHandler;
import com.jiuyescm.bms.receivable.storage.service.IBizDispatchPackageService;
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
    private IPubPackageDictService pubPackageDictService;
    @Autowired
    private IBizOutstockPackmaterialRepository bizOutstockPackmaterialRepository;
    @Autowired
    private IBizDispatchPackageService bizDispatchPackageService;
    @Autowired
    private IBizOutstockPackmaterialCancelService bizOutstockPackmaterialCancelService;
    
    private static final String NODEL = "NODEL";
    private static final String DEL = "DEL";
    private static final Integer batchNum = 2000;
    
    @Override
    public ReturnT<String> execute(String... params) throws Exception {
        XxlJobLogger.log("MaterialToDelJob start.");
        XxlJobLogger.log("耗材作废任务");
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
        
        sw.start("从cancel表获取 状态为初始的数据");
        //1.从cancel表捞取 状态=BEGIN && 导入运单号=包材组运单号
        List<BizOutstockPackmaterialCancelEntity> cancelList = bizOutstockPackmaterialCancelService.queryNeedCancel(map);
        printTime(sw);
        if (CollectionUtils.isEmpty(cancelList)) {
            sw.start("结束1");
            return printLog("没有需要作废的运单", sw);
        }
        List<BizOutstockPackmaterialCancelEntity> delList = new ArrayList<BizOutstockPackmaterialCancelEntity>(); 
        List<String> waybillNoList = new ArrayList<String>();
        //修改配置表（如：去掉一个耗材），通过配置表中作废的耗材变成未作废，触发重算
        List<BizOutstockPackmaterialEntity> delToNoDels = new ArrayList<BizOutstockPackmaterialEntity>();
        //状态为"初始"，导入运单号和拉取运单号不相等，直接改为不作废
        List<BizOutstockPackmaterialCancelEntity> updateNoDelList = new ArrayList<BizOutstockPackmaterialCancelEntity>(); 
        
        for (BizOutstockPackmaterialCancelEntity cancelEntity : cancelList) {
            cancelEntity.setDescrip("");
            cancelEntity.setModTime(new Timestamp(System.currentTimeMillis()));
            if (!cancelEntity.getWaybillNoImport().equals(cancelEntity.getWaybillNoPackage())) {
                cancelEntity.setStatus(NODEL);
                updateNoDelList.add(cancelEntity);
            }else {
                cancelEntity.setStatus(DEL);
                delList.add(cancelEntity);
                waybillNoList.add(cancelEntity.getWaybillNo());
            }    
        }
   
        XxlJobLogger.log("统计不需要作废的，并将状态更新为'不作废'");
        if (CollectionUtils.isNotEmpty(updateNoDelList)) {
            bizOutstockPackmaterialCancelService.updateBatchStatus(updateNoDelList);
        }
        printTime(sw);

        //合同归属为"合同在线"的数据一条都没有，就没有需要作废的
        if (CollectionUtils.isEmpty(waybillNoList)) {
            sw.start("结束2");
            return printLog("没有需要作废的运单", sw);
        }
        
        //通过商家归属为'合同在线'的运单号，查出对应耗材
        sw.start("通过商家归属为'合同在线'的运单号，查出对应耗材");
        List<BizOutstockPackmaterialEntity> materialLists = bizOutstockPackmaterialRepository.queryByWaybillNo(waybillNoList);
        printTime(sw);
        
        sw.start("从配置表中查出所有的配置进行组装");
        //3.从配置表中查出所有的未作废的配置，开始匹配
        Map<String, Object> con = new HashMap<String, Object>();
        con.put("delFlag", "0");
        List<PubPackageDictEntity> packDickList = pubPackageDictService.query(con);
        //key为mark，value为需要作废的耗材
        Map<String, List<String>> dicMap = new HashMap<String, List<String>>();
        for (PubPackageDictEntity dictEntity : packDickList) {
            if (dicMap.containsKey(dictEntity.getPackMark())) {
                dicMap.get(dictEntity.getPackMark()).add(dictEntity.getMaterialType());
            }else {
                List<String> materialList = new LinkedList<String>();
                materialList.add(dictEntity.getMaterialType());
                dicMap.put(dictEntity.getPackMark(), materialList);
            }
        }
        //配置表一定会配置数据，这一步可能多余
        if (CollectionUtils.isEmpty(packDickList)) {
            sw.start("结束3");
            return printLog("配置表无配置数据", sw);
        }
        printTime(sw);
        
        sw.start("对运单进行匹配打分");
        //4.通过捞的运单号去标准包装方案表中查, 然后开始进行匹配打分
        List<BizDispatchPackageEntity> disPacList = bizDispatchPackageService.queryByWaybillNo(waybillNoList);
        //用来存储运单号和耗材的对应关系，用来作废
        List<BizOutstockPackmaterialEntity> delMaterialList = new ArrayList<BizOutstockPackmaterialEntity>();
        for (BizDispatchPackageEntity bizEntity : disPacList) {

            //获取所有level和对应的marks
            TreeMap<Integer, Set<String>> levelMap = getAllLevel(packDickList, bizEntity);  
             
            //取出最大level对应的marks，来获取需要作废的耗材（去重）
            Set<String> marks = levelMap.get(levelMap.lastKey());
            Set<String> materialTypes = new HashSet<String>();
            for (String mark : marks) {
                List<String> materialTypeList = dicMap.get(mark);
                for (String materialType : materialTypeList) {
                    materialTypes.add(materialType);
                }
            }

            //组装需要作废的运单数据
            for (String materialType : materialTypes) {
                BizOutstockPackmaterialEntity packmaterialEntity = new BizOutstockPackmaterialEntity();
                packmaterialEntity.setWaybillNo(bizEntity.getWaybillNo());
                packmaterialEntity.setMaterialType(materialType);
                packmaterialEntity.setDelFlag("3");
                delMaterialList.add(packmaterialEntity);
            }
            
        }
        printTime(sw);
        
        //5.根据运单号作废使用标准包装方案的耗材
        sw.start("作废耗材，作废费用，修改作废表状态...");
        try {
            bizOutstockPackmaterialCancelService.updateBatchStatusAndDelMaterial(delList, delMaterialList);
        } catch (Exception e) {
            XxlJobLogger.log(e.getMessage());
        }
        printTime(sw);
        
        //组装需要恢复成未作废状态的耗材（耗材不在需要作废耗材的里面并且之前作废状态是3）
        for (BizOutstockPackmaterialEntity material : materialLists) {
            boolean exe = false;
            for (BizOutstockPackmaterialEntity delEntity : delMaterialList) {
                if ((material.getWaybillNo()+material.getMaterialType()).equals(delEntity.getWaybillNo()+delEntity.getMaterialType())) {
                    exe = true;
                }
            }
            if (!exe && "3".equals(material.getDelFlag())) {
                material.setDelFlag("0");
                material.setIsCalculated("0");
                delToNoDels.add(material);
            }
        }   
            
        //将需要恢复成未作废状态的耗材的业务数据计算状态更新成'0'，然后走Job进行初始化重算
        if (CollectionUtils.isNotEmpty(delToNoDels)) {
            sw.start("部分耗材恢复成未作废状态");
            bizOutstockPackmaterialRepository.deleteOrRevertMaterialStatus(delToNoDels);
            printTime(sw);
        }
        
        sw.start("任务完成");
        return printLog("任务完成", sw);

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
     * 获取所有level和对应的marks
     * <功能描述>
     * 
     * @author wangchen870
     * @date 2019年5月18日 下午5:29:37
     *
     * @param packDickList
     * @param bizEntity
     * @param level
     * @return
     */
    private TreeMap<Integer, Set<String>> getAllLevel(List<PubPackageDictEntity> packDickList, BizDispatchPackageEntity bizEntity) {
        TreeMap<Integer, Set<String>> levelMap = new TreeMap<Integer, Set<String>>();
        for (PubPackageDictEntity dicEntity : packDickList) {
            int level = 0;
            //季节
            if (bizEntity.getSeason().equals(dicEntity.getSeason())) {
                level += 1;
            }else if (StringUtils.isNotBlank(dicEntity.getSeason()) && !bizEntity.getSeason().equals(dicEntity.getSeason())) {
                continue;
            }
            //保温时效
            if (bizEntity.getHoldingTime().equals(dicEntity.getHoldingTime())) {
                level += 1;
            }else if (StringUtils.isNotBlank(dicEntity.getHoldingTime()) && !bizEntity.getHoldingTime().equals(dicEntity.getHoldingTime())) {
                continue;
            }
            //配送温区
            if (bizEntity.getTransportTemperatureType().equals(dicEntity.getTransportTemperatureType())) {
                level += 1;
            }else if (StringUtils.isNotBlank(dicEntity.getTransportTemperatureType()) && !bizEntity.getTransportTemperatureType().equals(dicEntity.getTransportTemperatureType())) {
                continue;
            }
            //运输方式
            if (bizEntity.getTransportType().equals(dicEntity.getTransportType())) {
                level += 1;
            }else if (StringUtils.isNotBlank(dicEntity.getTransportType()) && !bizEntity.getTransportType().equals(dicEntity.getTransportType())) {
                continue;
            }
            //操作分类
            if (bizEntity.getPackOperateType().equals(dicEntity.getPackOperateType())) {
                level += 1;
            }else if (StringUtils.isNotBlank(dicEntity.getPackOperateType()) && !bizEntity.getPackOperateType().equals(dicEntity.getPackOperateType())) {
                continue;
            }
            
            //可能存在一个运单，有2个一样打分等级的mark，2个mark对应的耗材都作废
            if (levelMap.containsKey(level)) {
                levelMap.get(level).add(dicEntity.getPackMark());
            }else {
                Set<String> marks = new HashSet<String>();
                marks.add(dicEntity.getPackMark());
                levelMap.put(level, marks);
            }  
        }     
        return levelMap;
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
    private ReturnT<String> printLog(String log, StopWatch sw){
        sw.stop();
        XxlJobLogger.log(log + ", 共耗时：" + sw.getTotalTimeMillis() + "毫秒");
        return ReturnT.SUCCESS;
    }

}


