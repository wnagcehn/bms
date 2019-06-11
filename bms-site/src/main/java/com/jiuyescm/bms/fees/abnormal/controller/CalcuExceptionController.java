/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.fees.abnormal.controller;

import java.io.File;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Sheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StopWatch;

import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.data.provider.Page;
import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.base.dictionary.entity.SystemCodeEntity;
import com.jiuyescm.bms.base.dictionary.service.ISystemCodeService;
import com.jiuyescm.bms.base.file.entity.FileExportTaskEntity;
import com.jiuyescm.bms.base.file.service.IFileExportTaskService;
import com.jiuyescm.bms.calculate.api.IBmsCalcuExceptionService;
import com.jiuyescm.bms.calculate.vo.ExceptionDetailVo;
import com.jiuyescm.bms.common.constants.ExceptionConstant;
import com.jiuyescm.bms.common.constants.FileConstant;
import com.jiuyescm.bms.common.constants.MessageConstant;
import com.jiuyescm.bms.common.enumtype.FileTaskStateEnum;
import com.jiuyescm.bms.common.enumtype.FileTaskTypeEnum;
import com.jiuyescm.bms.common.log.entity.BmsErrorLogInfoEntity;
import com.jiuyescm.bms.common.log.service.IBmsErrorLogInfoService;
import com.jiuyescm.bms.common.sequence.service.SequenceService;
import com.jiuyescm.bms.excel.constants.ExportConstants;
import com.jiuyescm.bms.excel.write.ExcelExporterFactory;
import com.jiuyescm.bms.excel.write.IExcelExporter;
import com.jiuyescm.cfm.common.JAppContext;
import com.jiuyescm.common.ConstantInterface;
import com.jiuyescm.exception.BizException;

/**
 * 计算异常数据监控
 * <功能描述>
 * 
 * @author wangchen870
 * @date 2019年6月4日 上午11:01:39
 */
@Controller("calcuExceptionController")
public class CalcuExceptionController {
    
    private static final Logger logger = LoggerFactory.getLogger(CalcuExceptionController.class.getName());
    
    @Autowired
    private IBmsCalcuExceptionService bmsCalcuExceptionService;
    @Autowired 
    private IBmsErrorLogInfoService bmsErrorLogInfoService;
    @Autowired
    private IFileExportTaskService fileExportTaskService;
    @Autowired
    private ISystemCodeService systemCodeService;
    @Autowired
    private SequenceService sequenceService;
    
    /**
     * 分页查询
     * @param page
     * @param param
     * @throws ParseException 
     */
    @DataProvider
    public void query(Page<ExceptionDetailVo> page, Map<String, Object> param){
        if (param == null) {
            param = new HashMap<String, Object>();
        }
        if (param.get("startDate") == null) {
            throw new BizException("开始日期不能为空!");
        }
        if (param.get("endDate") == null) {
            throw new BizException("结束日期不能为空!");
        }
        
        //给结束日期加一天
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Calendar c1 = Calendar.getInstance();
            Calendar c2 = Calendar.getInstance();
            c1.setTime((Date) param.get("startDate"));
            c2.setTime((Date) param.get("endDate"));
            c2.add(Calendar.DAY_OF_MONTH, 1);
            param.put("startDate", sdf.format(c1.getTime()));
            param.put("endDate", sdf.format(c2.getTime()));
        } catch (Exception e) {
            logger.error("日期转换异常：", e);
            throw new BizException("日期转换异常!");
        }
    
        PageInfo<ExceptionDetailVo> pageInfo = bmsCalcuExceptionService.query(param, page.getPageNo(), page.getPageSize());
        if (pageInfo != null) {
            page.setEntities(pageInfo.getList());
            page.setEntityCount((int) pageInfo.getTotal());
        }
    }
    
    /**
     * 导出
     * <功能描述>
     * 
     * @author wangchen870
     * @date 2019年6月4日 下午5:36:49
     *
     * @param param
     * @return
     */
    @DataResolver
    public String asynExport(Map<String, Object> param) {
        if (null == param) {
            return MessageConstant.QUERY_PARAM_NULL_MSG;
        }
        if (param.get("startDate") == null) {
            return "创建时间不能为空!";
        }
        if (param.get("endDate") == null) {
            return "结束时间不能为空!";
        }
        
        String today = "";
        //给结束日期加一天
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat sd = new SimpleDateFormat("yyyyMMdd");
            today = sd.format(new Date());
            Calendar c1 = Calendar.getInstance();
            Calendar c2 = Calendar.getInstance();
            c1.setTime((Date) param.get("startDate"));
            c2.setTime((Date) param.get("endDate"));
            c2.add(Calendar.DAY_OF_MONTH, 1);
            param.put("startDate", sdf.format(c1.getTime()));
            param.put("endDate", sdf.format(c2.getTime()));
        } catch (Exception e) {
            logger.error("日期转换异常：", e);
            throw new BizException("日期转换异常!");
        }
        
        try {
            FileExportTaskEntity entity = new FileExportTaskEntity();
            entity.setStartTime(Timestamp.valueOf(param.get("startDate") + " 00:00:00"));
            entity.setEndTime(Timestamp.valueOf(param.get("endDate")+ " 23:59:59"));
            
            String taskName = sequenceService.getBillNoOne(today, today, "000");
            entity.setTaskName(taskName);
            
            entity.setTaskType(FileTaskTypeEnum.Calcu_Error.getCode());
            entity.setTaskState(FileTaskStateEnum.BEGIN.getCode());
            entity.setProgress(0d);
            entity.setCreator(JAppContext.currentUserName());
            entity.setCreateTime(JAppContext.currentTimestamp());
            entity.setDelFlag(ConstantInterface.DelFlag.NO);
            entity = fileExportTaskService.save(entity);
            
            //生成账单文件
            final Map<String, Object> condition = param;
            final String taskId = entity.getTaskId();
            new Thread(){
                public void run() {
                    try {
                        export(condition, taskId);
                    } catch (Exception e) {
                        fileExportTaskService.updateExportTask(taskId, FileTaskStateEnum.FAIL.getCode(), 0);
                        logger.error(ExceptionConstant.ASYN_REC_DISPATCH_FEE_EXCEL_EX_MSG, e);
                        
                        //写入日志
                        BmsErrorLogInfoEntity bmsErrorLogInfoEntity=new BmsErrorLogInfoEntity();
                        bmsErrorLogInfoEntity.setClassName("CalcuExceptionController");
                        bmsErrorLogInfoEntity.setMethodName("asynExport");
                        bmsErrorLogInfoEntity.setErrorMsg(e.toString());
                        bmsErrorLogInfoEntity.setCreateTime(JAppContext.currentTimestamp());
                        bmsErrorLogInfoService.log(bmsErrorLogInfoEntity);  
                    }
                };
            }.start();
        }catch (Exception e) {
            logger.error(ExceptionConstant.ASYN_BIZ_EXCEL_EX_MSG, e);
            //写入日志
            BmsErrorLogInfoEntity bmsErrorLogInfoEntity=new BmsErrorLogInfoEntity();
            bmsErrorLogInfoEntity.setClassName("CalcuExceptionController");
            bmsErrorLogInfoEntity.setMethodName("asynExport");
            bmsErrorLogInfoEntity.setErrorMsg(e.toString());
            bmsErrorLogInfoEntity.setCreateTime(JAppContext.currentTimestamp());
            bmsErrorLogInfoService.log(bmsErrorLogInfoEntity);  
            return ExceptionConstant.ASYN_BIZ_EXCEL_EX_MSG;
        }

        return FileTaskTypeEnum.Calcu_Error.getDesc() + MessageConstant.EXPORT_TASK_BIZ_MSG;
    }
    
    /**
     * 异步导出
     * <功能描述>
     * 
     * @author wangchen870
     * @date 2019年4月16日 下午12:01:53
     *
     * @param param
     * @param taskId
     * @param filePath
     * @throws Exception
     */
    private void export(Map<String, Object> param, String taskId) throws Exception {
        fileExportTaskService.updateExportTask(taskId,FileTaskStateEnum.INPROCESS.getCode(), 10);
        String path = "";
        StopWatch sw = new StopWatch();
        sw.start();
        
        //如果存放上传文件的目录不存在就新建
        SystemCodeEntity sc = getSystemCode("GLOABL_PARAM","EXPORT_CALCU_ERROR");
        File storeFolder=new File(sc.getExtattr1());
        if(!storeFolder.exists()){
            storeFolder.mkdirs();
        }
        
        logger.info("====计费异常数据导出：写入Excel begin.");
        fileExportTaskService.updateExportTask(taskId, null, 30);
       
        fileExportTaskService.updateExportTask(taskId, null, 70);
        // 计费异常数据导出
        try {
            path = handBiz(param);
        } catch (Exception e) {
           logger.error(e.getMessage());
        }
        
        // 最后写到文件
        fileExportTaskService.updateExportTask(taskId, null, 90);
        sw.stop();
        
        fileExportTaskService.updateTask(taskId,FileTaskStateEnum.SUCCESS.getCode(), 100, path);
        logger.info("====计费异常数据导出：写入Excel end.==总耗时：" + sw.getTotalTimeMillis());
    }
    
    /**
     * 导出逻辑
     * <功能描述>
     * 
     * @author wangchen870
     * @date 2019年4月16日 下午12:33:27
     *
     * @param poiUtil
     * @param workbook
     * @param path
     * @param myparam
     * @throws Exception
     */
    private String handBiz(Map<String, Object> myparam)throws Exception{
        
        IExcelExporter exporter = ExcelExporterFactory.createExporter(ExportConstants.SXSSF);
        Sheet sheet =exporter.createSheetByAnno("计费异常数据",1,ExceptionDetailVo.class);

        int pageNo = 1;
        boolean doLoop = true;
        while (doLoop) {            
            PageInfo<ExceptionDetailVo> pageInfo = bmsCalcuExceptionService.query(myparam, pageNo, FileConstant.EXPORTPAGESIZE);
            if (null != pageInfo && pageInfo.getList().size() > 0) {
                if (pageInfo.getList().size() < FileConstant.EXPORTPAGESIZE) {
                    doLoop = false;
                }else {
                    pageNo += 1; 
                }
            }else {
                doLoop = false;
            }
            
            /*
             * 新Excel塞数据方式
             */
            if (null != pageInfo && pageInfo.getList().size() > 0) { 
                exporter.writeContentByAnno(sheet, pageInfo.getList());
            }
        }  

        SystemCodeEntity sc = getSystemCode("GLOABL_PARAM","EXPORT_CALCU_ERROR");
        return exporter.saveFile(sc.getExtattr1(), UUID.randomUUID().toString()+".xlsx");
    }
    
    /**
    * 获取系统参数对象
    * <功能描述>
    * 
    * @author wangchen870
    * @date 2019年4月16日 下午12:02:12
    *
    * @param typeCode
    * @param code
    * @return
    */
   public SystemCodeEntity getSystemCode(String typeCode, String code){
       if (StringUtils.isNotEmpty(typeCode) && StringUtils.isNotEmpty(code)) {
           SystemCodeEntity systemCodeEntity = systemCodeService.getSystemCode(typeCode, code);
           if(systemCodeEntity == null){
               throw new BizException("请在系统参数中配置文件上传路径,参数" + typeCode + "," + code);
           }
           return systemCodeEntity;
       }
       return null;
   }
    
}
