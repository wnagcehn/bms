package com.jiuyescm.bms.fees.transport.controller;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
import com.jiuyescm.bms.biz.dispatch.entity.BizDispatchPackageEntity;
import com.jiuyescm.bms.common.constants.ExceptionConstant;
import com.jiuyescm.bms.common.constants.FileConstant;
import com.jiuyescm.bms.common.constants.MessageConstant;
import com.jiuyescm.bms.common.enumtype.FileTaskStateEnum;
import com.jiuyescm.bms.common.enumtype.FileTaskTypeEnum;
import com.jiuyescm.bms.common.log.entity.BmsErrorLogInfoEntity;
import com.jiuyescm.bms.common.log.service.IBmsErrorLogInfoService;
import com.jiuyescm.bms.excel.constants.ExportConstants;
import com.jiuyescm.bms.excel.write.ExcelExporterFactory;
import com.jiuyescm.bms.excel.write.IExcelExporter;
import com.jiuyescm.bms.fees.transport.entity.FeesTransportMasterEntity;
import com.jiuyescm.bms.fees.transport.service.IFeesTransportMasterService;
import com.jiuyescm.cfm.common.JAppContext;
import com.jiuyescm.common.ConstantInterface;
import com.jiuyescm.common.utils.DateUtil;
import com.jiuyescm.exception.BizException;
import com.jiuyescm.mdm.customer.api.ICustomerService;
import com.jiuyescm.mdm.customer.vo.CustomerVo;

/**
 * ..Controller
 * @author wangchen
 * 
 */
@Controller("feesTransportMasterController")
public class FeesTransportMasterController {

	private static final Logger logger = LoggerFactory.getLogger(FeesTransportMasterController.class.getName());

	@Autowired
	private IFeesTransportMasterService feesTransportMasterService;
	@Autowired 
    private IBmsErrorLogInfoService bmsErrorLogInfoService;
    @Autowired
    private IFileExportTaskService fileExportTaskService;
    @Autowired
    private ISystemCodeService systemCodeService;
    @Autowired 
    private ICustomerService customerService;

	/**
	 * 根据id查询
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@DataProvider
	public FeesTransportMasterEntity findById(Long id) throws Exception {
		return feesTransportMasterService.findById(id);
	}

	/**
	 * 分页查询
	 * @param page
	 * @param param
	 */
	@DataProvider
	public void query(Page<FeesTransportMasterEntity> page, Map<String, Object> param) {
	    if (null == param) {
            param = new HashMap<String, Object>();
        }
        if (param.get("beginTime") == null) {
            throw new BizException("开始时间不能为空!");
        }
        if (param.get("endTime") == null) {
            throw new BizException("结束时间不能为空!");
        }
        param.put("delFlag", "0");
        try {
    		PageInfo<FeesTransportMasterEntity> pageInfo = feesTransportMasterService.query(param, page.getPageNo(), page.getPageSize());
    		if (pageInfo != null) {
    			page.setEntities(pageInfo.getList());
    			page.setEntityCount((int) pageInfo.getTotal());
    		}
        } catch (Exception e) {
            logger.error("查询异常：", e);
            throw new BizException("查询异常:", e);
        }
	}

	/**
	 * 保存
	 * @param entity
	 * @return
	 */
	@DataResolver
	public void save(FeesTransportMasterEntity entity) {
		if (null == entity.getId()) {
			feesTransportMasterService.save(entity);
		} else {
			feesTransportMasterService.update(entity);
		}
	}

	/**
	 * 删除
	 * @param entity
	 */
	@DataResolver
	public void delete(FeesTransportMasterEntity entity) {
		feesTransportMasterService.delete(entity.getId());
	}
	
	@DataResolver
    public String asynExport(Map<String, Object> param) {
        if (null == param) {
            return MessageConstant.QUERY_PARAM_NULL_MSG;
        }
        if (param.get("beginTime") == null) {
            return "开始时间不能为空!";
        }
        if (param.get("endTime") == null) {
            return "结束时间不能为空!";
        }
        String customerid = "";
        if (null != param.get("customerId")) {
            customerid = param.get("customerId").toString();
        }
        param.put("delFlag", "0");
        
        // 初始化商家
        Map<String, String> customerMap = getCustomer(); 
        
        try {    
            FileExportTaskEntity entity = new FileExportTaskEntity();
            entity.setStartTime(DateUtil.formatTimestamp(param.get("beginTime")));
            entity.setEndTime(DateUtil.formatTimestamp(param.get("endTime")));
            if (customerMap.containsKey(customerid)) {
                entity.setTaskName(FileTaskTypeEnum.BIZ_TRANSPORT.getDesc() + "运单" + customerMap.get(customerid));
            }else {
                entity.setTaskName(FileTaskTypeEnum.BIZ_TRANSPORT.getDesc()  + "运单" + customerid);
            }    
            entity.setTaskType(FileTaskTypeEnum.BIZ_TRANSPORT.getCode());
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
                        logger.error(ExceptionConstant.ASYN_REC_TRANSPORT_FEE_EXCEL_EX_MSG, e);
                        
                        //写入日志
                        BmsErrorLogInfoEntity bmsErrorLogInfoEntity=new BmsErrorLogInfoEntity();
                        bmsErrorLogInfoEntity.setClassName("FeesTransportMasterController");
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
            bmsErrorLogInfoEntity.setClassName("FeesTransportMasterController");
            bmsErrorLogInfoEntity.setMethodName("asynExport");
            bmsErrorLogInfoEntity.setErrorMsg(e.toString());
            bmsErrorLogInfoEntity.setCreateTime(JAppContext.currentTimestamp());
            bmsErrorLogInfoService.log(bmsErrorLogInfoEntity);  
            return ExceptionConstant.ASYN_BIZ_EXCEL_EX_MSG;
        }
        if (customerMap.containsKey(customerid)) {
            return FileTaskTypeEnum.BIZ_TRANSPORT.getDesc() + customerMap.get(customerid) + MessageConstant.EXPORT_TASK_BIZ_MSG;
        }
        return FileTaskTypeEnum.BIZ_TRANSPORT.getDesc() + "运单导出" + MessageConstant.EXPORT_TASK_BIZ_MSG;
    }
	
	/**
	 * 异步导出
	 * <功能描述>
	 * 
	 * @author wangchen870
	 * @date 2019年6月21日 下午5:15:34
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
        
        //如果存放上传文件的目录不存在就新建
        SystemCodeEntity sc = getSystemCode("GLOABL_PARAM","EXPORT_TRANSPORT");
        File storeFolder=new File(sc.getExtattr1());
        if(!storeFolder.exists()){
            storeFolder.mkdirs();
        }

        logger.info("====干线费用导出：写入Excel begin.");
        fileExportTaskService.updateExportTask(taskId, null, 30);
       
        fileExportTaskService.updateExportTask(taskId, null, 70);
        
        try {
            path = handBiz(param);
        } catch (Exception e) {
           logger.error("生成Excel文件异常：", e);
        }
        
        // 最后写到文件
        fileExportTaskService.updateExportTask(taskId, null, 90);
        sw.stop();
        
        fileExportTaskService.updateTask(taskId,FileTaskStateEnum.SUCCESS.getCode(), 100, path);
        logger.info("====干线费用导出：写入Excel end.==总耗时：" + sw.getTotalTimeMillis());
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
        Sheet sheet =exporter.createSheetByAnno("干线运单",1,FeesTransportMasterEntity.class);

        int pageNo = 1;
        boolean doLoop = true;
        while (doLoop) {            
            PageInfo<FeesTransportMasterEntity> pageInfo = feesTransportMasterService.queryToExport(myparam, pageNo, FileConstant.EXPORTPAGESIZE);
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

        SystemCodeEntity sc = getSystemCode("GLOABL_PARAM","EXPORT_TRANSPORT");
        return exporter.saveFile(sc.getExtattr1(), UUID.randomUUID().toString()+".xlsx");
    }
	
	/**
     * MDM查询所有商家
     * <功能描述>
     * 
     * @author wangchen870
     * @date 2019年4月16日 下午7:37:32
     *
     * @return
     */
    private Map<String, String> getCustomer() {
        Map<String, String> customerMap = new HashMap<String, String>();
        PageInfo<CustomerVo> tmpPageInfo = customerService.query(null, 0, Integer.MAX_VALUE);
        if (tmpPageInfo != null && tmpPageInfo.getList() != null && tmpPageInfo.getList().size()>0) {
            for(CustomerVo customer : tmpPageInfo.getList()){
                if(customer != null){
                    customerMap.put(customer.getCustomerid().trim(), customer.getCustomername().trim());
                }
            }
        }
        return customerMap;
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
