/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.report.biz.web;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.time.DateUtils;
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
import com.jiuyescm.bms.report.service.IMaterialReportService;
import com.jiuyescm.bms.report.vo.MaterailOutReportVo;
import com.jiuyescm.cfm.common.JAppContext;
import com.jiuyescm.common.ConstantInterface;
import com.jiuyescm.exception.BizException;
import com.jiuyescm.framework.fastdfs.client.StorageClient;
import com.jiuyescm.framework.fastdfs.model.StorePath;

/**
 * 计算异常数据监控
 * <功能描述>
 * 
 * @author zhaofeng
 * @date 2019年8月9日 上午11:01:39
 */
@Controller("materialOutReportController")
public class MaterialOutReportController {
    
    private static final Logger logger = LoggerFactory.getLogger(MaterialOutReportController.class.getName());
    
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
    @Autowired
    private IMaterialReportService materialReportService;
    @Autowired
    private StorageClient storageClient;
    /**
     * 分页查询
     * @param page
     * @param param
     * @throws ParseException 
     */
    @DataProvider
    public void query(Page<MaterailOutReportVo> page, Map<String, Object> param){
        if (param == null) {
            param = new HashMap<String, Object>();
        }
       
        PageInfo<MaterailOutReportVo> pageInfo = materialReportService.query(param, page.getPageNo(), page.getPageSize());
        if (pageInfo != null) {
            page.setEntities(pageInfo.getList());
            page.setEntityCount((int) pageInfo.getTotal());
        }
    }
    
    /**
     * 导出
     * <功能描述>
     * 
     * @author
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
        if(param.get("year")==null){
            return "年份不能为空";
        }
        if(param.get("month")==null){
            return "月份不能为空";
        }
        try {
            FileExportTaskEntity entity = new FileExportTaskEntity();
            String year = param.get("year").toString();
            String month = param.get("month").toString();
            if (StringUtils.isNotBlank(year) && StringUtils.isNotBlank(month)) {
                String startDateStr = year + "-" + month + "-01 00:00:00";
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date startDate = sdf.parse(startDateStr);
                Date endDate = DateUtils.addMonths(startDate, 1);
                entity.setStartTime(new Timestamp(startDate.getTime()));
                entity.setEndTime(new Timestamp(endDate.getTime()));
            }
            entity.setTaskName(param.get("year").toString()+"年"+param.get("month").toString()+"月耗材统计");
            
            entity.setTaskType(FileTaskTypeEnum.MATERIAL_OUT.getCode());
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

        return FileTaskTypeEnum.MATERIAL_OUT.getDesc() + MessageConstant.EXPORT_TASK_BIZ_MSG;
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
            
        logger.info("====耗材统计数据导出：写入Excel begin.");
        fileExportTaskService.updateExportTask(taskId, null, 30);
       
        // 计费异常数据导出
        try {
            path = handBiz(param,taskId);
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
    private String handBiz(Map<String, Object> myparam,String taskId)throws Exception{
        fileExportTaskService.updateExportTask(taskId,"", 40);

        IExcelExporter exporter = ExcelExporterFactory.createExporter(ExportConstants.SXSSF);
        Sheet sheet =exporter.createSheetByAnno("耗材出库统计数据",1,MaterailOutReportVo.class);
        
        try {
            int pageNo = 1;
            boolean doLoop = true;
            while (doLoop) {            
                PageInfo<MaterailOutReportVo> pageInfo = materialReportService.query(myparam, pageNo, FileConstant.EXPORTPAGESIZE);
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
        } catch (Exception e) {
            // TODO: handle exception
            logger.error("查询异常",e);
        }
        
       

        fileExportTaskService.updateExportTask(taskId,null, 70);

        //上传到Fastdfs
        String filePath = "";
        InputStream inputStream = null;
        ByteArrayOutputStream out = null;

        try {
            out = new ByteArrayOutputStream();
            exporter.getWorkBook().write(out);
            inputStream = new ByteArrayInputStream(out.toByteArray());
            StorePath storePath = storageClient.uploadFile(inputStream, inputStream.available(), "xlsx");
            filePath = storePath.getFullPath();
        } catch (Exception e) {
            logger.error("上传到Fastdfs失败", e);
            fileExportTaskService.updateExportTask(taskId,FileTaskStateEnum.FAIL.getCode(), 99);
            return "";
        } finally {
            if (null != inputStream) {
                inputStream.close();
            }
            if (null != out) {
                out.close();
            }
        }
        fileExportTaskService.updateExportTask(taskId,null, 80);
        return filePath;
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
