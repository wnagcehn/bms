/*
 * Copyright (c) 2019, Jiuye SCM and/or its affiliates. All rights reserved.
 */
package com.jiuyescm.bms.fees.claim.controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StopWatch;

import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.data.provider.Page;
import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.base.file.entity.FileExportTaskEntity;
import com.jiuyescm.bms.base.file.service.IFileExportTaskService;
import com.jiuyescm.bms.common.constants.FileConstant;
import com.jiuyescm.bms.common.constants.MessageConstant;
import com.jiuyescm.bms.common.enumtype.FileTaskStateEnum;
import com.jiuyescm.bms.common.enumtype.FileTaskTypeEnum;
import com.jiuyescm.bms.common.log.entity.BmsErrorLogInfoEntity;
import com.jiuyescm.bms.common.log.service.IBmsErrorLogInfoService;
import com.jiuyescm.bms.excel.constants.ExportConstants;
import com.jiuyescm.bms.excel.write.ExcelExporterFactory;
import com.jiuyescm.bms.excel.write.IExcelExporter;
import com.jiuyescm.bms.feeclaim.service.IFeesClaimService;
import com.jiuyescm.bms.feeclaim.vo.FeesClaimsVo;
import com.jiuyescm.cfm.common.JAppContext;
import com.jiuyescm.common.ConstantInterface;
import com.jiuyescm.framework.fastdfs.client.StorageClient;
import com.jiuyescm.framework.fastdfs.model.StorePath;

/**
 * <功能描述>
 * 
 * @author zhaofeng
 * @date 2019年6月20日 上午11:14:09
 */
@Controller("feesClaimController")
public class FeesClaimController {
    private static final Logger logger = Logger.getLogger(FeesClaimController.class.getName());
    
    @Resource
    private IFeesClaimService feesClaimService;
    @Autowired 
    private IBmsErrorLogInfoService bmsErrorLogInfoService;
    @Autowired
    private IFileExportTaskService fileExportTaskService;
    @Autowired
    private StorageClient storageClient;

    /**
     * 应收理赔分页查询
     * 
     * @param page
     * @param param
     */
    @DataProvider
    public void query(Page<FeesClaimsVo> page, Map<String, Object> param) {
        PageInfo<FeesClaimsVo> pageInfo = feesClaimService.query(param, page.getPageNo(), page.getPageSize());
        if (pageInfo != null) {
            page.setEntities(pageInfo.getList());
            page.setEntityCount((int) pageInfo.getTotal());
        }
    }
    
    /**
     * 更新
     * @param 
     * @return
     */
    @DataResolver
    public String update(FeesClaimsVo p){ 
        try {
            p.setModPerson(JAppContext.currentUserName());
            p.setModPersonId(JAppContext.currentUserID());
            p.setModTime(JAppContext.currentTimestamp());
            feesClaimService.update(p);
            return "调整成功";
            
        } catch (Exception e) {
            //写入日志
            logger.info("数据库更新失败"+e.getMessage());
            return "调整失败";
        }
    }
    

    /**
     * 
     * <功能描述>
     * 
     * @author zhaofeng
     * @date 2019年6月21日 上午11:51:05
     *
     * @param param
     * @return
     */
    @DataResolver
    public String asynExport(Map<String, Object> param) {       
        try {    
            FileExportTaskEntity entity = new FileExportTaskEntity();
            entity.setTaskName("理赔费用");        
            entity.setTaskType(FileTaskTypeEnum.FEES_CLAIM.getCode());
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
                        fileExportTaskService.updateExportTask(taskId, FileTaskStateEnum.FAIL.getCode(), 99);
                        logger.error("异步生成理赔费用异常", e);
                        
                        //写入日志
                        BmsErrorLogInfoEntity bmsErrorLogInfoEntity=new BmsErrorLogInfoEntity();
                        bmsErrorLogInfoEntity.setClassName("FeesClaimController");
                        bmsErrorLogInfoEntity.setMethodName("asynExport");
                        bmsErrorLogInfoEntity.setErrorMsg(e.toString());
                        bmsErrorLogInfoEntity.setCreateTime(JAppContext.currentTimestamp());
                        bmsErrorLogInfoService.log(bmsErrorLogInfoEntity);  
                    }
                };
            }.start();
        }catch (Exception e) {
            logger.error("异步生成理赔费用异常", e);
            //写入日志
            BmsErrorLogInfoEntity bmsErrorLogInfoEntity=new BmsErrorLogInfoEntity();
            bmsErrorLogInfoEntity.setClassName("FeesClaimController");
            bmsErrorLogInfoEntity.setMethodName("asynExport");
            bmsErrorLogInfoEntity.setErrorMsg(e.toString());
            bmsErrorLogInfoEntity.setCreateTime(JAppContext.currentTimestamp());
            bmsErrorLogInfoService.log(bmsErrorLogInfoEntity);  
            return "异步生成理赔费用异常";
        }
        return FileTaskTypeEnum.FEES_CLAIM.getDesc() + MessageConstant.EXPORT_TASK_BIZ_MSG;
    }
    
    
   /**
   * 
   * <功能描述>
   * 
   * @author zhaofeng
   * @date 2019年6月21日 上午11:52:21
   *
   * @param param
   * @param taskId
   * @throws Exception
   */
    private void export(Map<String, Object> param, String taskId) throws Exception {
        fileExportTaskService.updateExportTask(taskId,FileTaskStateEnum.INPROCESS.getCode(), 10);
        String path = "";
        StopWatch sw = new StopWatch();
        sw.start();
        
        logger.info("====理赔费用导出：写入Excel begin.");
        fileExportTaskService.updateExportTask(taskId, null, 30);
        //理赔费用
        try {
            path = handBiz(param,taskId);
        } catch (Exception e) {
           logger.error("处理异常",e);
        }
        
        // 最后写到文件
        fileExportTaskService.updateExportTask(taskId, null, 90);
        sw.stop();
        
        fileExportTaskService.updateTask(taskId,FileTaskStateEnum.SUCCESS.getCode(), 100, path);
        logger.info("====理赔费用导出：写入Excel end.==总耗时：" + sw.getTotalTimeMillis());
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
        Sheet sheet =exporter.createSheetByAnno("理赔费用",1,FeesClaimsVo.class);
        int pageNo = 1;
        boolean doLoop = true;
        while (doLoop) {            
            PageInfo<FeesClaimsVo> pageInfo = feesClaimService.query(myparam, pageNo, FileConstant.EXPORTPAGESIZE);
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
    
}


