package com.jiuyescm.bms.biz.dispatch.web;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Sheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StopWatch;

import com.bstek.bdf2.core.context.ContextHolder;
import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.annotation.Expose;
import com.bstek.dorado.data.provider.Page;
import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.asyn.service.IBmsCalcuTaskService;
import com.jiuyescm.bms.asyn.vo.BmsCalcuTaskVo;
import com.jiuyescm.bms.base.dictionary.entity.SystemCodeEntity;
import com.jiuyescm.bms.base.dictionary.service.ISystemCodeService;
import com.jiuyescm.bms.base.file.entity.FileExportTaskEntity;
import com.jiuyescm.bms.base.file.service.IFileExportTaskService;
import com.jiuyescm.bms.biz.diapatch.service.IBizDispatchPackageService;
import com.jiuyescm.bms.biz.diapatch.vo.BizDispatchPackageVo;
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
@Controller("bizDispatchPackageController")
public class BizDispatchPackageController {

	private static final Logger logger = LoggerFactory.getLogger(BizDispatchPackageController.class.getName());

	@Autowired
	private IBizDispatchPackageService bizDispatchPackageService;
	@Autowired 
    private IBmsCalcuTaskService bmsCalcuTaskService;
	@Autowired 
    private IBmsErrorLogInfoService bmsErrorLogInfoService;
	@Autowired
	private IFileExportTaskService fileExportTaskService;
	@Autowired
	private ISystemCodeService systemCodeService;
	@Autowired 
	private ICustomerService customerService;


	/**
	 * 分页查询
	 * @param page
	 * @param param
	 */
	@DataProvider
	public void query(Page<BizDispatchPackageVo> page, Map<String, Object> param) {
	    if (null == param) {
            param = new HashMap<String, Object>();
        }
        if (param.get("creTime") == null) {
            throw new BizException("创建时间不能为空!");
        }
        if (param.get("creEndTime") == null) {
            throw new BizException("结束时间不能为空!");
        }
	    try {
	        PageInfo<BizDispatchPackageVo> pageInfo = bizDispatchPackageService.query(param, page.getPageNo(), page.getPageSize());
	        if (pageInfo != null) {
	            page.setEntities(pageInfo.getList());
	            page.setEntityCount((int) pageInfo.getTotal());
	        }
        } catch (Exception e) {
            throw new BizException(e.getMessage());
        }	
	}

	/**
	 * 保存
	 * @param entity
	 * @return
	 */
	@DataResolver
	public void save(BizDispatchPackageEntity entity) {
		if (null == entity.getId()) {
			bizDispatchPackageService.save(entity);
		} else {
			bizDispatchPackageService.update(entity);
		}
	}
	
	/**
	 * 
	 * 重算
	 * 
	 * @author wangchen870
	 * @date 2019年4月16日 上午10:41:59
	 *
	 * @param param
	 * @return
	 */
	@Expose
    public String reCalculate(Map<String, Object> param){
        if(bizDispatchPackageService.reCalculate(param) <= 0){
            return "重算异常";
        }else{
            //对这些费用按照商家、科目、时间排序
            param.put("isCalculated", "99");
            List<BmsCalcuTaskVo> calList=bmsCalcuTaskService.queryPackageTask(param);
            for (BmsCalcuTaskVo vo : calList) {
                vo.setCrePerson(ContextHolder.getLoginUser().getCname());
                vo.setCrePersonId(ContextHolder.getLoginUserName());
                try {
                    bmsCalcuTaskService.sendTask(vo);
                    logger.info("mq发送成功,商家id:"+vo.getCustomerId()+",年月:"+vo.getCreMonth()+",科目id:"+vo.getSubjectCode());
                } catch (Exception e) {
                    logger.error("mq发送失败:", e);
                }   
            }
        }
        return "操作成功! 正在重算...";
    }
	
	/**
	 * 导出
	 * <功能描述>
	 * 
	 * @author wangchen870
	 * @date 2019年4月16日 上午11:29:56
	 *
	 * @param param
	 * @return
	 */
	@DataResolver
    public String asynExport(Map<String, Object> param) {
	    if (null == param) {
            return MessageConstant.QUERY_PARAM_NULL_MSG;
        }
	    if (param.get("creTime") == null) {
            return "创建时间不能为空!";
        }
        if (param.get("creEndTime") == null) {
            return "结束时间不能为空!";
        }
        String customerid = "";
        if (null != param.get("customerid")) {
            customerid = param.get("customerid").toString();
        }
        
        // 初始化商家
        Map<String, String> customerMap = getCustomer(); 
	    
	    try {    
            FileExportTaskEntity entity = new FileExportTaskEntity();
            entity.setStartTime(DateUtil.formatTimestamp(param.get("creTime")));
            entity.setEndTime(DateUtil.formatTimestamp(param.get("creEndTime")));
            if (customerMap.containsKey(customerid)) {
                entity.setTaskName(FileTaskTypeEnum.BIZ_PACKAGE_OUTSTOCK.getDesc() + customerMap.get(customerid));
            }else {
                entity.setTaskName(FileTaskTypeEnum.BIZ_PACKAGE_OUTSTOCK.getDesc() + customerid);
            }    
            entity.setTaskType(FileTaskTypeEnum.BIZ_PACKAGE_OUTSTOCK.getCode());
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
                        logger.error(ExceptionConstant.ASYN_REC_PACKAGE_FEE_EXCEL_EX_MSG, e);
                        
                        //写入日志
                        BmsErrorLogInfoEntity bmsErrorLogInfoEntity=new BmsErrorLogInfoEntity();
                        bmsErrorLogInfoEntity.setClassName("DispatchBillPayExportController");
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
            bmsErrorLogInfoEntity.setClassName("BizDispatchPackageController");
            bmsErrorLogInfoEntity.setMethodName("asynExport");
            bmsErrorLogInfoEntity.setErrorMsg(e.toString());
            bmsErrorLogInfoEntity.setCreateTime(JAppContext.currentTimestamp());
            bmsErrorLogInfoService.log(bmsErrorLogInfoEntity);  
            return ExceptionConstant.ASYN_BIZ_EXCEL_EX_MSG;
        }
	    if (customerMap.containsKey(customerid)) {
	        return FileTaskTypeEnum.BIZ_PACKAGE_OUTSTOCK.getDesc() + customerMap.get(customerid) + MessageConstant.EXPORT_TASK_BIZ_MSG;
        }
	    return FileTaskTypeEnum.BIZ_PACKAGE_OUTSTOCK.getDesc() + MessageConstant.EXPORT_TASK_BIZ_MSG;
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
        SystemCodeEntity sc = getSystemCode("GLOABL_PARAM","EXPORT_PACKAGE_BIZ");
        File storeFolder=new File(sc.getExtattr1());
        if(!storeFolder.exists()){
            storeFolder.mkdirs();
        }

        logger.info("====标准包装方案导出：写入Excel begin.");
        fileExportTaskService.updateExportTask(taskId, null, 30);
        
        fileExportTaskService.updateExportTask(taskId, null, 70);
        // 标准包装方案
        try {
            path = handBiz(param);
        } catch (Exception e) {
           logger.error(e.getMessage());
        }
        
        // 最后写到文件
        fileExportTaskService.updateExportTask(taskId, null, 90);
        sw.stop();
        
        fileExportTaskService.updateTask(taskId,FileTaskStateEnum.SUCCESS.getCode(), 100, path);
        logger.info("====标准包装方案导出：写入Excel end.==总耗时：" + sw.getTotalTimeMillis());
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
    
        Map<String, String> transportMap = getSystemCode("TRANSPORT_TYPE");
        Map<String, String> boxMap = getSystemCode("PLATIC_BOX_TYPE");
        Map<String, String> timeMap = getSystemCode("HOLDING_TIME");
        Map<String, String> operateTypeMap = getSystemCode("PACK_OPERATE_TYPE");
        
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String startTime =formatter.format(myparam.get("creTime")) ;
        String endTime = formatter.format( myparam.get("creEndTime"));
        Map<String, String> diffMap = DateUtil.getSplitTime(startTime, endTime, 1);

        IExcelExporter exporter = ExcelExporterFactory.createExporter(ExportConstants.SXSSF);
        Sheet sheet =exporter.createSheetByAnno("标准包装方案",1,BizDispatchPackageEntity.class);
        for (Map.Entry<String, String> entry : diffMap.entrySet()) {
            logger.info("startTime:["+entry.getKey()+"] endTime["+entry.getValue()+"]");
            myparam.put("creTime", entry.getKey());
            myparam.put("creEndTime", entry.getValue());
            int pageNo = 1;
            boolean doLoop = true;
            while (doLoop) {            
                PageInfo<BizDispatchPackageEntity> pageInfo = 
                        bizDispatchPackageService.queryToExport(myparam, pageNo, FileConstant.EXPORTPAGESIZE, transportMap, boxMap, timeMap, operateTypeMap);
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
        }

        SystemCodeEntity sc = getSystemCode("GLOABL_PARAM","EXPORT_PACKAGE_BIZ");
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
    
    public Map<String, String> getSystemCode(String typeCode){
        Map<String, String> map = new HashMap<>();
        List<SystemCodeEntity> list = systemCodeService.findEnumList(typeCode);
        for (SystemCodeEntity entity : list) {
            map.put(entity.getCode(), entity.getCodeName());
        }
        return map;
    }
	
}
