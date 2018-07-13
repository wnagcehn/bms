package com.jiuyescm.bms.biz.dispatchtihuo.web;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.stereotype.Controller;

import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.data.provider.Page;
import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.base.file.entity.FileExportTaskEntity;
import com.jiuyescm.bms.base.file.service.IFileExportTaskService;
import com.jiuyescm.bms.base.system.BaseController;
import com.jiuyescm.bms.biz.dispatch.entity.BizTihuoBillEntity;
import com.jiuyescm.bms.biz.dispatch.service.IBizDispatchTihuoBillService;
import com.jiuyescm.bms.common.constants.ExceptionConstant;
import com.jiuyescm.bms.common.constants.FileConstant;
import com.jiuyescm.bms.common.constants.MessageConstant;
import com.jiuyescm.bms.common.enumtype.CalculateState;
import com.jiuyescm.bms.common.enumtype.FileTaskStateEnum;
import com.jiuyescm.bms.common.enumtype.FileTaskTypeEnum;
import com.jiuyescm.bms.common.log.entity.BmsErrorLogInfoEntity;
import com.jiuyescm.bms.common.log.service.IBmsErrorLogInfoService;
import com.jiuyescm.cfm.common.JAppContext;
import com.jiuyescm.common.ConstantInterface;
import com.jiuyescm.common.utils.DateUtil;
import com.jiuyescm.common.utils.excel.POISXSSUtil;

@Controller("dispatchTihuoController")
public class DispatchTihuoController extends BaseController{
	
	private static final Logger logger = Logger.getLogger(DispatchTihuoController.class.getName());
	
	@Resource
	private IBizDispatchTihuoBillService bizDispatchTihuoBillService;
	@Resource
	private IFileExportTaskService fileExportTaskService;
	@Resource
	private IBmsErrorLogInfoService bmsErrorLogInfoService;
	
	/**
	 * 分页查询
	 * 
	 * @param page
	 * @param param
	 */
	@DataProvider
	public void queryAll(Page<BizTihuoBillEntity> page, Map<String, Object> param) {
		if (param == null){
			param = new HashMap<String, Object>();
		}
		PageInfo<BizTihuoBillEntity> pageInfo = bizDispatchTihuoBillService.query(param, page.getPageNo(), page.getPageSize());
		if (pageInfo != null) {
			page.setEntities(pageInfo.getList());
			page.setEntityCount((int) pageInfo.getTotal());
		}
	}
	
	/**
	 * 导出
	 */
	@DataResolver
	public String asynExport(Map<String, Object> param) {
		if (null == param) {
			return MessageConstant.QUERY_PARAM_NULL_MSG;
		}
		
		String deliverid = param.get("deliverid").toString();
        try {
        	//校验该费用是否已生成Excel文件
        	Map<String, Object> queryEntity = new HashMap<String, Object>();
        	queryEntity.put("taskType", FileTaskTypeEnum.BIZ_TIHUO.getCode());
        	queryEntity.put("param2", deliverid);
        	String existDel = fileExportTaskService.isExistDeleteTask(queryEntity);
        	if (StringUtils.isNotEmpty(existDel)) {
        		return existDel;
        	}
        	
        	String path = getBizPayExportPath();
        	String filepath=path+ FileConstant.SEPARATOR + 
        			FileTaskTypeEnum.BIZ_TIHUO.getCode() + deliverid + FileConstant.SUFFIX_XLSX;
        	
        	FileExportTaskEntity entity = new FileExportTaskEntity();
        	entity.setParam2(deliverid);
        	entity.setStartTime(DateUtil.formatTimestamp(param.get("createTime")));
        	entity.setEndTime(DateUtil.formatTimestamp(param.get("endTime")));
        	entity.setTaskName(FileTaskTypeEnum.BIZ_TIHUO.getDesc() + deliverid);
        	entity.setTaskType(FileTaskTypeEnum.BIZ_TIHUO.getCode());
        	entity.setTaskState(FileTaskStateEnum.BEGIN.getCode());
        	entity.setProgress(0d);
        	entity.setFilePath(filepath);
        	entity.setCreator(JAppContext.currentUserName());
        	entity.setCreateTime(JAppContext.currentTimestamp());
        	entity.setDelFlag(ConstantInterface.DelFlag.NO);
        	entity = fileExportTaskService.save(entity);
        	
        	//生成账单文件
    		final Map<String, Object> condition = param;
    		final String taskId = entity.getTaskId();
    		final String filePath=filepath;
    		new Thread(){
    			public void run() {
    				try {
    					export(condition, taskId,filePath);
    				} catch (Exception e) {
    					fileExportTaskService.updateExportTask(taskId, FileTaskStateEnum.FAIL.getCode(), 0);
    					logger.error(ExceptionConstant.ASYN_REC_DISPATCH_FEE_EXCEL_EX_MSG, e);
    					//写入日志
    					BmsErrorLogInfoEntity bmsErrorLogInfoEntity=new BmsErrorLogInfoEntity();
    					bmsErrorLogInfoEntity.setClassName("DispatchTihuoController");
    					bmsErrorLogInfoEntity.setMethodName("asynExport");
    					bmsErrorLogInfoEntity.setErrorMsg(e.toString());
    					bmsErrorLogInfoEntity.setCreateTime(JAppContext.currentTimestamp());
    					bmsErrorLogInfoService.log(bmsErrorLogInfoEntity);	
    				}
    			};
    		}.start();
		} catch (Exception e) {
			logger.error(ExceptionConstant.ASYN_BIZ_EXCEL_EX_MSG, e);
			//写入日志
			BmsErrorLogInfoEntity bmsErrorLogInfoEntity=new BmsErrorLogInfoEntity();
			bmsErrorLogInfoEntity.setClassName("DispatchTihuoController");
			bmsErrorLogInfoEntity.setMethodName("asynExport");
			bmsErrorLogInfoEntity.setErrorMsg(e.toString());
			bmsErrorLogInfoEntity.setCreateTime(JAppContext.currentTimestamp());
			bmsErrorLogInfoService.log(bmsErrorLogInfoEntity);	
			return ExceptionConstant.ASYN_BIZ_EXCEL_EX_MSG;
		}
		return FileTaskTypeEnum.BIZ_TIHUO.getDesc() + deliverid + MessageConstant.EXPORT_TASK_BIZ_MSG;
	}
	
	/**
	 * 异步导出
	 * @param param
	 * @param taskId
	 * @param file
	 * @throws Exception
	 */
	private void export(Map<String, Object> param,String taskId,String filePath)throws Exception{
		fileExportTaskService.updateExportTask(taskId, FileTaskStateEnum.INPROCESS.getCode(), 10);
		String path = getBizPayExportPath();
		long beginTime = System.currentTimeMillis();
		
		logger.info("====应收商品按托库存导出：");
		//如果存放上传文件的目录不存在就新建
    	File storeFolder=new File(path);
		if(!storeFolder.isDirectory()){
			storeFolder.mkdirs();
		}
		
    	logger.info("====应收商品按托库存导出：写入Excel begin.");
    	fileExportTaskService.updateExportTask(taskId, null, 30);
    	POISXSSUtil poiUtil = new POISXSSUtil();
    	SXSSFWorkbook workbook = poiUtil.getXSSFWorkbook();
        //干线运单
    	fileExportTaskService.updateExportTask(taskId, null, 70);
    	handBiz(poiUtil, workbook, filePath, param);
    	//最后写到文件
    	fileExportTaskService.updateExportTask(taskId, null, 90);
    	poiUtil.write2FilePath(workbook, filePath);
    	
    	fileExportTaskService.updateExportTask(taskId, FileTaskStateEnum.SUCCESS.getCode(), 100);
    	logger.info("====应收商品按托库存导出：写入Excel end.==总耗时：" + (System.currentTimeMillis() - beginTime));
	}
	
	/**
	 * 商品按托库存
	 * @param poiUtil
	 * @param workbook
	 * @param path
	 * @param myparam
	 * @throws Exception
	 */
	private void handBiz(POISXSSUtil poiUtil, SXSSFWorkbook workbook, 
			String path, Map<String, Object> myparam)throws Exception{
		int pageNo = 1;
		int lineNo = 1;
		boolean doLoop = true;
		while (doLoop) {			
			PageInfo<BizTihuoBillEntity> pageInfo = 
					bizDispatchTihuoBillService.query(myparam, pageNo, FileConstant.EXPORTPAGESIZE);
			if (null != pageInfo && pageInfo.getList().size() > 0) {
				if (pageInfo.getList().size() < FileConstant.EXPORTPAGESIZE) {
					doLoop = false;
				}else {
					pageNo += 1; 
				}
			}else {
				doLoop = false;
			}
			
			//头、内容信息
			List<Map<String, Object>> headDetailMapList = getBizHead(); 
			List<Map<String, Object>> dataDetailList = getBizHeadItem(pageInfo.getList());
			
			poiUtil.exportExcel2FilePath(poiUtil, workbook, FileTaskTypeEnum.BIZ_TIHUO.getDesc(), 
					lineNo, headDetailMapList, dataDetailList);
			if (null != pageInfo && pageInfo.getList().size() > 0) {
				lineNo += pageInfo.getList().size();
			}
		}
	}
	
	/**
	 * 干线运单
	 */
	public List<Map<String, Object>> getBizHead(){
		List<Map<String, Object>> headInfoList = new ArrayList<Map<String,Object>>();
		Map<String, Object> itemMap = new HashMap<String, Object>();
		itemMap.put("title", "统计时间");
		itemMap.put("columnWidth", 25);
		itemMap.put("dataKey", "countDate");
		headInfoList.add(itemMap);
		
		itemMap = new HashMap<String, Object>();
		itemMap.put("title", "单量");
		itemMap.put("columnWidth", 25);
		itemMap.put("dataKey", "totalNum");
		headInfoList.add(itemMap);
		
		itemMap = new HashMap<String, Object>();
		itemMap.put("title", "宅配商名称");
		itemMap.put("columnWidth", 25);
		itemMap.put("dataKey", "deliverName");
		headInfoList.add(itemMap);
        
		itemMap = new HashMap<String, Object>();
        itemMap.put("title", "状态");
        itemMap.put("columnWidth", 50);
        itemMap.put("dataKey", "isCalculated");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "计算时间");
        itemMap.put("columnWidth", 50);
        itemMap.put("dataKey", "calculateTime");
        headInfoList.add(itemMap);
        
        return headInfoList;
	}
	
	private List<Map<String, Object>> getBizHeadItem(List<BizTihuoBillEntity> list){
		 List<Map<String, Object>> dataList = new ArrayList<Map<String,Object>>();
	        Map<String, Object> dataItem = null;
	        Map<String, String> calculateMap = CalculateState.getMap();
	        for (BizTihuoBillEntity entity : list) {
	        	dataItem = new HashMap<String, Object>();
	        	dataItem.put("countDate", entity.getCountDate());
	        	dataItem.put("totalNum", entity.getTotalNum());
	        	dataItem.put("deliverName", entity.getDeliverName());
	        	dataItem.put("isCalculated", calculateMap.get(entity.getIsCalculated()));
	        	dataItem.put("calculateTime", entity.getCalculateTime());
	        	dataList.add(dataItem);
			}
	        
		return dataList;
	}
	
}
