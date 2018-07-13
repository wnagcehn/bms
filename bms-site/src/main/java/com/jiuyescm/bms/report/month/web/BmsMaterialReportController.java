package com.jiuyescm.bms.report.month.web;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.stereotype.Controller;

import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.data.provider.Page;
import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.base.dictionary.entity.SystemCodeEntity;
import com.jiuyescm.bms.base.dictionary.service.ISystemCodeService;
import com.jiuyescm.bms.base.file.entity.FileExportTaskEntity;
import com.jiuyescm.bms.base.file.service.IFileExportTaskService;
import com.jiuyescm.bms.biz.storage.entity.BizOutstockPackmaterialEntity;
import com.jiuyescm.bms.biz.storage.entity.BizWmsOutstockPackmaterialEntity;
import com.jiuyescm.bms.common.constants.ExceptionConstant;
import com.jiuyescm.bms.common.constants.FileConstant;
import com.jiuyescm.bms.common.constants.MessageConstant;
import com.jiuyescm.bms.common.enumtype.FileTaskStateEnum;
import com.jiuyescm.bms.common.enumtype.FileTaskTypeEnum;
import com.jiuyescm.bms.common.sequence.service.SequenceService;
import com.jiuyescm.bms.report.month.entity.BmsMaterialReportEntity;
import com.jiuyescm.bms.report.month.service.IBmsMaterialReportService;
import com.jiuyescm.cfm.common.JAppContext;
import com.jiuyescm.common.tool.MapTool;
import com.jiuyescm.common.utils.excel.POISXSSUtil;
import com.jiuyescm.exception.BizException;
import com.jiuyescm.mdm.customer.api.IPubMaterialInfoService;
import com.jiuyescm.mdm.customer.vo.PubMaterialInfoVo;

@Controller("bmsReportMaterialController")
public class BmsMaterialReportController {
	@Resource private IBmsMaterialReportService bmsMaterialReportService;
	
	@Resource private IFileExportTaskService fileExportTaskService;

	@Resource private ISystemCodeService systemCodeService;
	
	@Resource private SequenceService sequenceService;
	
	@Resource
	private IPubMaterialInfoService pubMaterialInfoService;
	
	final int pageSize = 2000;
	
	private static final Logger logger = Logger.getLogger(BmsMaterialReportController.class.getName());

	@DataProvider  
	public void queryAll(Page<BmsMaterialReportEntity> page,Map<String,Object> parameter){
		if(parameter!=null){
			if(parameter.get("isDifferent")!=null && "true".equals(parameter.get("isDifferent"))){
				parameter.put("isDifferent", 2);
			}
			if(parameter.get("reportMonth")!=null){
				 String remonth="";
			     int month=Integer.parseInt(parameter.get("reportMonth").toString());
			     if (month >= 1 && month <= 9) {
		        	remonth = "0" + month;
			     }else{
			    	remonth=""+month;
			     }
			     parameter.put("reportMonth", remonth);
			}
		}
		
		PageInfo<BmsMaterialReportEntity> tmpPageInfo = bmsMaterialReportService.query(parameter, page.getPageNo(),page.getPageSize());
		if (tmpPageInfo != null) {
			page.setEntities(tmpPageInfo.getList());
			page.setEntityCount((int) tmpPageInfo.getTotal());
		}
	}
	
	
	@DataResolver
	public String exportBms(Map<String,Object> param) throws Exception{
		
		String type=param.get("type").toString();
        //将查询条件转为json，判断是否生成过文件，生成过则删 除原来的，生成新的
		Map<String, String> paramMap = new HashMap<String, String>();
		
        if(param.get("reportYear")!=null){
        	paramMap.put("reportYear",param.get("reportYear").toString());
        }
        if(param.get("reportMonth")!=null){
        	paramMap.put("reportMonth",param.get("reportMonth").toString());
        }
        
        if(param.get("warehouseCode")!=null){
        	paramMap.put("warehouseCode",param.get("warehouseCode").toString());
        }
        if(param.get("customerid")!=null){
        	paramMap.put("customerid",param.get("customerid").toString());
        }
       
        String remonth="";
        int month=Integer.parseInt(param.get("reportMonth").toString());
        if (month >= 1 && month <= 9) {
        	remonth = "-0" + month;
	    }else{
	    	remonth="-"+month;
	    }
        
        
        String time=param.get("reportYear").toString()+remonth;
        param.put("time", time);
        
        if("bms".equals(type)){
            paramMap.put(FileTaskTypeEnum.BIZ_BMS_OUTSTOCK.getCode(),FileTaskTypeEnum.BIZ_BMS_OUTSTOCK.getDesc());
        }else{
            paramMap.put(FileTaskTypeEnum.BIZ_WMS_OUTSTOCK.getCode(),FileTaskTypeEnum.BIZ_WMS_OUTSTOCK.getDesc());

        }
		String taskID = MapTool.getMD5String(paramMap);//对查询条件进行签名 唯一标识文件
		
		//判断是否已生成过Excel文件
		//校验该费用是否已生成Excel文件
		Map<String, Object> queryEntity = new HashMap<String, Object>();
		if("bms".equals(type)){
			queryEntity.put("taskType", FileTaskTypeEnum.BIZ_BMS_OUTSTOCK.getCode());
		}else{
			queryEntity.put("taskType", FileTaskTypeEnum.BIZ_WMS_OUTSTOCK.getCode());
		}
		queryEntity.put("param1", taskID);
		List<FileExportTaskEntity> taskList = fileExportTaskService.query(queryEntity);
		
		if (null != taskList && taskList.size() > 0) {
			return taskID;
		}
		
		String taskName="";
		if("bms".equals(type)){
			taskName="【"+time+"】BMS结算出库数据导出";
		}else{
			taskName ="【"+time+"】WMS原始出库数据导出";
		}
		
		String path=getPath();
		String filepath="";
		filepath = path + getBMSName() + FileConstant.SUFFIX_XLSX;

		FileExportTaskEntity entity = new FileExportTaskEntity();
		entity.setTaskName(taskName);
		if("bms".equals(type)){
			entity.setTaskType(FileTaskTypeEnum.BIZ_BMS_OUTSTOCK.getCode());
		}else{
			entity.setTaskType(FileTaskTypeEnum.BIZ_WMS_OUTSTOCK.getCode());
		}
		entity.setTaskId(taskID);
		entity.setTaskState(FileTaskStateEnum.BEGIN.getCode());
		entity.setProgress(0d);
		entity.setFilePath(filepath);
		entity.setCreator(JAppContext.currentUserName());
		entity.setCreateTime(JAppContext.currentTimestamp());
		entity.setParam1(taskID);
		entity.setDelFlag("0");
		entity = fileExportTaskService.save(entity);
		
		//生成账单文件
		final Map<String, Object> condition = param;
		final String taskId = entity.getTaskId();
		final String file=filepath;
		final String type12=type;
		new Thread(){
			public void run() {
				try {
					export(condition, taskId,file,type12);
				} catch (Exception e) {
					updateExportTask(taskId, FileTaskStateEnum.FAIL.getCode(), 0);
					logger.error(ExceptionConstant.ASYN_REC_DISPATCH_FEE_EXCEL_EX_MSG, e);
				}
			};
		}.start();
		return entity.getParam1();
			
	}
	
	
	private void export(Map<String, Object> param,String taskId,String file,String type)throws Exception{
		
		updateExportTask(taskId, FileTaskStateEnum.INPROCESS.getCode(), 10);
		String path = getPath();
		long beginTime = System.currentTimeMillis();
		
    	File storeFolder=new File(path);
		//如果存放上传文件的目录不存在就新建
		if(!storeFolder.isDirectory()){
			storeFolder.mkdirs();
		}
		
    	String filePath = file;
		
    	logger.info("====出库数据导出：写入Excel begin.");	
	
		try {		
			POISXSSUtil poiUtil = new POISXSSUtil();
	    	SXSSFWorkbook workbook = poiUtil.getXSSFWorkbook();
	    		    	
	    	updateExportTask(taskId, null, 30);
	    	
	        //耗材明细
	    	if("bms".equals(type)){
		    	handBMSOutStock(poiUtil, workbook, filePath, param);
	    	}else{
	    		handWMSOutStock(poiUtil, workbook, filePath, param);
	    	}
	    	
	    	updateExportTask(taskId, null, 70);

	    	//最后写到文件
	    	poiUtil.write2FilePath(workbook, filePath);
	    	
	    	updateExportTask(taskId, FileTaskStateEnum.SUCCESS.getCode(), 100);
	    	
	    	logger.info("====出库数据导出：写入Excel end.==总耗时：" + (System.currentTimeMillis() - beginTime));

		} catch (Exception e) {
			//bmsErrorLogInfoService.
			logger.error("出库数据导出导出失败", e);
		}
	}
	
	
	@SuppressWarnings("deprecation")
	private String getPath(){
		SystemCodeEntity systemCodeEntity = systemCodeService.getSystemCode("GLOABL_PARAM", "EXPORT_BIZ_BMS_OUTSTOCK");
		if(systemCodeEntity == null){
			throw new BizException("请在系统参数中配置文件上传路径,参数GLOABL_PARAM,EXPORT_BIZ_BMS_OUTSTOCK");
		}
		return systemCodeEntity.getExtattr1();
	}
	
	
	/**
	 * BMS耗材明细
	 * @param poiUtil
	 * @param workbook
	 * @param path
	 * @param billno
	 * @throws Exception
	 */
	private void handBMSOutStock(POISXSSUtil poiUtil, SXSSFWorkbook workbook, 
			String path, Map<String, Object> myparam)throws Exception{
		int pageNo = 1;
		int dispatchLineNo = 1;
		boolean doLoop = true;
		while (doLoop) {
			PageInfo<BizOutstockPackmaterialEntity> pageInfo = bmsMaterialReportService.queryBmsMaterial(myparam, pageNo, pageSize);
			if (null != pageInfo && pageInfo.getList().size() > 0) {
				if (pageInfo.getList().size() < pageSize) {
					doLoop = false;
				}else {
					pageNo += 1; 
				}
			}else {
				doLoop = false;
			}
			
			List<Map<String, Object>> headBmsOutStockMapList = getBmsOutstockFeeHead(); 
			List<Map<String, Object>> dataBmsOutStockDetailList = getBMsOutstockFeeHeadItem(pageInfo.getList());
			
			poiUtil.exportExcel2FilePath(poiUtil, workbook, MessageConstant.BIZ_BMS_OUTSTOCK_MSG, 
					dispatchLineNo, headBmsOutStockMapList, dataBmsOutStockDetailList);
			if (null != pageInfo && pageInfo.getList().size() > 0) {
				dispatchLineNo += pageInfo.getList().size();
			}
		}
	}
	
	/**
	 * 结算耗材出库明细
	 */
	public List<Map<String, Object>> getBmsOutstockFeeHead(){
		List<Map<String, Object>> headInfoList = new ArrayList<Map<String,Object>>();
		Map<String, Object> itemMap = new HashMap<String, Object>();
		
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "运单号");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "waybillNo");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "出库日期");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "createTime");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "仓库");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "warehouseName");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "商家");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "customerName");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "耗材编号");
        itemMap.put("columnWidth", 50);
        itemMap.put("dataKey", "consumerMaterialCode");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "耗材名称");
        itemMap.put("columnWidth", 50);
        itemMap.put("dataKey", "consumerMaterialName");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "耗材类别");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "consumerMaterialType");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "结算数量");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "num");
        headInfoList.add(itemMap);
             
        return headInfoList;
	}
	
	private List<Map<String, Object>> getBMsOutstockFeeHeadItem(List<BizOutstockPackmaterialEntity> list){
		Map<String, String> materialMap=getAllMaterial();
		List<Map<String, Object>> dataList = new ArrayList<Map<String,Object>>();
	        Map<String, Object> dataItem = null;
	        for (BizOutstockPackmaterialEntity entity : list) {
	        	dataItem = new HashMap<String, Object>();
	        	dataItem.put("waybillNo", entity.getWaybillNo());
	        	dataItem.put("createTime", entity.getCreateTime());
	        	dataItem.put("warehouseName", entity.getWarehouseName());
	        	dataItem.put("customerName", entity.getCustomerName());
	        	dataItem.put("consumerMaterialCode", entity.getConsumerMaterialCode());
	        	dataItem.put("consumerMaterialName", entity.getConsumerMaterialName());
	        	dataItem.put("consumerMaterialType", materialMap.get(entity.getConsumerMaterialCode()));
	        	dataItem.put("num", entity.getNum());
	        	dataList.add(dataItem);
			}
	        
		return dataList;
	}
	
	/**
	 * WMS耗材明细
	 * @param poiUtil
	 * @param workbook
	 * @param path
	 * @param billno
	 * @throws Exception
	 */
	private void handWMSOutStock(POISXSSUtil poiUtil, SXSSFWorkbook workbook, 
			String path, Map<String, Object> myparam)throws Exception{
		int pageNo = 1;
		int dispatchLineNo = 1;
		boolean doLoop = true;
		while (doLoop) {
			PageInfo<BizWmsOutstockPackmaterialEntity> pageInfo = bmsMaterialReportService.queryWmsMaterial(myparam, pageNo, pageSize);
			if (null != pageInfo && pageInfo.getList().size() > 0) {
				if (pageInfo.getList().size() < pageSize) {
					doLoop = false;
				}else {
					pageNo += 1; 
				}
			}else {
				doLoop = false;
			}
			
			List<Map<String, Object>> headWmsOutStockMapList = getWmsOutstockFeeHead(); 
			List<Map<String, Object>> dataWmsOutStockDetailList = getWmsOutstockFeeHeadItem(pageInfo.getList());
			
			poiUtil.exportExcel2FilePath(poiUtil, workbook, MessageConstant.BIZ_BMS_OUTSTOCK_MSG, 
					dispatchLineNo, headWmsOutStockMapList, dataWmsOutStockDetailList);
			if (null != pageInfo && pageInfo.getList().size() > 0) {
				dispatchLineNo += pageInfo.getList().size();
			}
		}
	}
	
	/**
	 * Wms耗材出库明细
	 */
	public List<Map<String, Object>> getWmsOutstockFeeHead(){
		List<Map<String, Object>> headInfoList = new ArrayList<Map<String,Object>>();
		Map<String, Object> itemMap = new HashMap<String, Object>();
		
		itemMap = new HashMap<String, Object>();
        itemMap.put("title", "运单号");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "waybillNo");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "出库日期");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "createTime");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "仓库");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "warehouseName");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "商家");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "customerName");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "耗材编号");
        itemMap.put("columnWidth", 50);
        itemMap.put("dataKey", "consumerMaterialCode");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "耗材名称");
        itemMap.put("columnWidth", 50);
        itemMap.put("dataKey", "consumerMaterialName");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "耗材类别");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "consumerMaterialType");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "出库数量");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "num");
        headInfoList.add(itemMap);
             
             
        return headInfoList;
	}
	
	private List<Map<String, Object>> getWmsOutstockFeeHeadItem(List<BizWmsOutstockPackmaterialEntity> list){
		Map<String, String> materialMap=getAllMaterial();
		List<Map<String, Object>> dataList = new ArrayList<Map<String,Object>>();
	        Map<String, Object> dataItem = null;
	        for (BizWmsOutstockPackmaterialEntity entity : list) {
	        	dataItem = new HashMap<String, Object>();
	        	dataItem.put("waybillNo", entity.getWaybillNo());
	        	dataItem.put("createTime", entity.getCreateTime());
	        	dataItem.put("warehouseName", entity.getWarehouseName());
	        	dataItem.put("customerName", entity.getCustomerName());
	        	dataItem.put("consumerMaterialCode", entity.getConsumerMaterialCode());
	        	dataItem.put("consumerMaterialName", entity.getConsumerMaterialName());
	        	dataItem.put("consumerMaterialType", materialMap.get(entity.getConsumerMaterialCode()));
	        	dataItem.put("num", entity.getNum());
	        	dataList.add(dataItem);
			}
	        
		return dataList;
	}
	
	
	public String getBMSName(){
		return sequenceService.getBillNoOne(BmsMaterialReportController.class.getName(), "BMS", "000000");
	}
	
	/**
	 * 修改导出任务列表
	 * @param taskId
	 * @param process
	 * @param taskState
	 */
	private void updateExportTask(String taskId, String taskState, double process){
		FileExportTaskEntity entity = new FileExportTaskEntity();
		if (StringUtils.isNotEmpty(taskState)) {
			entity.setTaskState(taskState);
		}
		entity.setTaskId(taskId);
		entity.setProgress(process);
		fileExportTaskService.update(entity);
	}
	
	public Map<String, String> getAllMaterial(){
		Map<String, Object> con=new HashMap<String, Object>();
		List<PubMaterialInfoVo> materialList=pubMaterialInfoService.queryList(con);		
		Map<String, String> returnMap=new HashMap<String, String>();		
		for(PubMaterialInfoVo en:materialList){
			returnMap.put(en.getBarcode(), en.getMaterialType());
		}		
		return returnMap;
	}
}
