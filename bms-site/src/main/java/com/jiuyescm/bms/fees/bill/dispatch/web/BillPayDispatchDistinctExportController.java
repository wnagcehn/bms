package com.jiuyescm.bms.fees.bill.dispatch.web;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.uploader.DownloadFile;
import com.bstek.dorado.uploader.annotation.FileProvider;
import com.bstek.dorado.web.DoradoContext;
import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.base.dictionary.entity.SystemCodeEntity;
import com.jiuyescm.bms.base.dictionary.service.ISystemCodeService;
import com.jiuyescm.bms.base.file.entity.FileExportTaskEntity;
import com.jiuyescm.bms.base.file.service.IFileExportTaskService;
import com.jiuyescm.bms.common.constants.ExceptionConstant;
import com.jiuyescm.bms.common.constants.FileConstant;
import com.jiuyescm.bms.common.constants.MessageConstant;
import com.jiuyescm.bms.common.enumtype.FileTaskStateEnum;
import com.jiuyescm.bms.common.enumtype.FileTaskTypeEnum;
import com.jiuyescm.bms.common.log.service.IBmsErrorLogInfoService;
import com.jiuyescm.bms.common.sequence.service.SequenceService;
import com.jiuyescm.bms.fees.bill.dispatch.entity.BillPayDispatchDistinctEncapEntity;
import com.jiuyescm.bms.fees.bill.dispatch.service.IBillPayDispatchDistinctService;
import com.jiuyescm.cfm.common.JAppContext;
import com.jiuyescm.common.tool.MapTool;
import com.jiuyescm.common.utils.DateUtil;
import com.jiuyescm.common.utils.excel.POISXSSUtil;
import com.jiuyescm.exception.BizException;
import com.jiuyescm.mdm.customer.api.ICustomerService;
import com.jiuyescm.mdm.warehouse.api.IWarehouseService;

/**
 * 应收配送费用导出
 * @author zhaof
 */
@Controller("billPayDispatchDistinctExportController")
public class BillPayDispatchDistinctExportController {
	@Resource private SequenceService sequenceService;
	@Resource private ISystemCodeService systemCodeService;
	@Autowired private IBillPayDispatchDistinctService service;
	@Resource private IBmsErrorLogInfoService bmsErrorLogInfoService;
	@Resource private IFileExportTaskService fileExportTaskService;
	
	private static final Logger logger = Logger.getLogger(BillPayDispatchDistinctExportController.class.getName());

	final int pageSize = 2000;
	
	@DataResolver
	public String export(Map<String,Object> param) throws Exception{
		
        //将查询条件转为json，判断是否生成过文件，生成过则删 除原来的，生成新的
		Map<String, String> paramMap = new HashMap<String, String>();
        if(param.get("deliveryid")!=null){
        	paramMap.put("deliveryid",param.get("deliveryid").toString());
        }
        if(param.get("waybillNo")!=null){
        	paramMap.put("waybillNo",param.get("waybillNo").toString());
        }
        if(param.get("startTime")!=null){
        	paramMap.put("startTime",DateUtil.formatTimestamp(param.get("startTime")).toString());
        }
        if(param.get("endTime")!=null){
        	paramMap.put("endTime",DateUtil.formatTimestamp(param.get("endTime")).toString());
        }
        if(param.get("status")!=null){
        	paramMap.put("status",param.get("status").toString());
        }
        if(param.get("billNo")!=null){
        	paramMap.put("billNo",param.get("billNo").toString());
        }
        
        paramMap.put(FileTaskTypeEnum.BILL_PAY_DISPATCH_DISTINCT.getCode(),FileTaskTypeEnum.BILL_PAY_DISPATCH_DISTINCT.getDesc());
		String taskID = MapTool.getMD5String(paramMap);//对查询条件进行签名 唯一标识文件
		
		//判断是否已生成过Excel文件
		//校验该费用是否已生成Excel文件
		Map<String, Object> queryEntity = new HashMap<String, Object>();
		queryEntity.put("taskType", FileTaskTypeEnum.BILL_PAY_DISPATCH_DISTINCT.getCode());
		queryEntity.put("param1", taskID);
		List<FileExportTaskEntity> taskList = fileExportTaskService.query(queryEntity);
		
		if (null != taskList && taskList.size() > 0) {
			return taskID;
		}
		
		String taskName = param.get("billNo").toString()+"-宅配对账差异导出";
		String path=getPath();
		String filepath="";
		filepath = path + getName() + FileConstant.SUFFIX_XLSX;
		FileExportTaskEntity entity = new FileExportTaskEntity();
		if(param.get("startTime")!=null){
			entity.setStartTime(DateUtil.formatTimestamp(param.get("startTime")));
		}
		if(param.get("endTime")!=null){
			entity.setEndTime(DateUtil.formatTimestamp(param.get("endTime")));
		}
		entity.setTaskName(taskName);	
		//判断是否是账单费用导出	
		entity.setTaskId(taskID);
		entity.setTaskType(FileTaskTypeEnum.BILL_PAY_DISPATCH_DISTINCT.getCode());//导出类型 -- 应付宅配对账差异导出
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
		new Thread(){
			public void run() {
				try {
					export(condition, taskId,file);
				} catch (Exception e) {
					updateExportTask(taskId, FileTaskStateEnum.FAIL.getCode(), 0);
					logger.error(ExceptionConstant.ASYN_PAY_DISPATCH_DISTINCT_EXCEL_EX_MSG, e);
				}
			};
		}.start();
		return entity.getParam1();
			
	}
	
	
	private void export(Map<String, Object> param,String taskId,String file)throws Exception{
		
		updateExportTask(taskId, FileTaskStateEnum.INPROCESS.getCode(), 10);
		String path = getPath();
		long beginTime = System.currentTimeMillis();
		logger.info("====应付宅配对账差异导出：");
		
    	File storeFolder=new File(path);
		//如果存放上传文件的目录不存在就新建
		if(!storeFolder.isDirectory()){
			storeFolder.mkdirs();
		}
		
    	String filePath = file;
		
    	logger.info("====应付宅配对账差异导出：写入Excel begin.");
		
		// 所有转码Map
		Map<String, Object> dictcodeMap = new HashMap<String, Object>();
		//状态
		Map<String, String> statusMap = new HashMap<String, String>();
		statusMap.put("0", "未过账");
		statusMap.put("1", "已过账");
		dictcodeMap.put("statusMap", statusMap);
		try {
			
			if("全部".equals(param.get("status"))){
				param.put("status", null);
			}
			
			POISXSSUtil poiUtil = new POISXSSUtil();
	    	SXSSFWorkbook workbook = poiUtil.getXSSFWorkbook();
	    		    	
	    	updateExportTask(taskId, null, 30);
	    	
	        //宅配对账明细
	    	handDispatch(poiUtil, workbook, filePath, param,dictcodeMap);
	    	
	    	updateExportTask(taskId, null, 70);

	    	//最后写到文件
	    	poiUtil.write2FilePath(workbook, filePath);
	    	
	    	updateExportTask(taskId, FileTaskStateEnum.SUCCESS.getCode(), 100);
	    	
	    	logger.info("====应收配送费用导出：写入Excel end.==总耗时：" + (System.currentTimeMillis() - beginTime));

		} catch (Exception e) {
			//bmsErrorLogInfoService.
			logger.error("应收配送费用导出失败", e);
		}
	}
	
	
	/**
	 * 配送
	 * @param poiUtil
	 * @param workbook
	 * @param path
	 * @param billno
	 * @throws Exception
	 */
	private void handDispatch(POISXSSUtil poiUtil, SXSSFWorkbook workbook, 
			String path, Map<String, Object> myparam,Map<String, Object> dictcodeMap)throws Exception{
		int pageNo = 1;
		int dispatchLineNo = 1;
		boolean doLoop = true;
		while (doLoop) {
			PageInfo<BillPayDispatchDistinctEncapEntity> pageInfo = service.query(myparam, pageNo, pageSize);
			if (null != pageInfo && pageInfo.getList().size() > 0) {
				for(int i=0;i<pageInfo.getList().size();i++){
					BillPayDispatchDistinctEncapEntity entity=pageInfo.getList().get(i);
					//差额处理,差异账单金额不存在显示业务数据的金额
					if(null == entity.getAmount() || entity.getAmount().equals(0)){
						entity.setDiffAmount(entity.getFeeAmount());
					}
					if(entity.getWaybillNo()==null || "".equals(entity.getWaybillNo())){
						entity.setWaybillNo(entity.getFeeWayBillNo());
					}
				}		
				if (pageInfo.getList().size() < pageSize) {
					doLoop = false;
				}else {
					pageNo += 1; 
				}
			}else {
				doLoop = false;
			}
			
			List<Map<String, Object>> headdistributionDetailMapList = getDispatchFeeHead(); 
			List<Map<String, Object>> datadistributionDetailList = getDispatchFeeHeadItem(pageInfo.getList(),dictcodeMap);
			
			poiUtil.exportExcel2FilePath(poiUtil, workbook, MessageConstant.PAY_DISPATCH_DISTINCT, 
					dispatchLineNo, headdistributionDetailMapList, datadistributionDetailList);
			if (null != pageInfo && pageInfo.getList().size() > 0) {
				dispatchLineNo += pageInfo.getList().size();
			}
		}
	}
	
	@FileProvider
	public DownloadFile downloadTemplate(Map<String, Object> param) throws IOException {
		InputStream is = DoradoContext.getCurrent().getServletContext().getResourceAsStream("/WEB-INF/templates/bill/billdispatchdistinct_template.xlsx");
		return new DownloadFile("九曳宅配对账单导入模板.xlsx", is);
	}
	
	
	@SuppressWarnings("deprecation")
	private String getPath(){
		SystemCodeEntity systemCodeEntity = systemCodeService.getSystemCode("GLOABL_PARAM", "EXPORT_PAY_DISPATCH_DISTINCT");
		if(systemCodeEntity == null){
			throw new BizException("请在系统参数中配置文件上传路径,参数GLOABL_PARAM,EXPORT_PAY_DISPATCH_DISTINCT");
		}
		return systemCodeEntity.getExtattr1();
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
	
	
	/**
	 * 配送费用明细
	 */
	public List<Map<String, Object>> getDispatchFeeHead(){
		List<Map<String, Object>> headInfoList = new ArrayList<Map<String,Object>>();
		Map<String, Object> itemMap = new HashMap<String, Object>();
		
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "账单编号");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "billNo");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "费用编号");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "feeNo");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "运单号");
        itemMap.put("columnWidth", 50);
        itemMap.put("dataKey", "feeWayBillNo");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "宅配商");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "feeDeliveryName");
        headInfoList.add(itemMap);
          
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "总重量");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "feeTotalWeight");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "首重重量");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "feeHeadWeight");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "首重价格");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "feeHeadPrice");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "续重重量");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "feeContinuedWeight");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "续重价格");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "feeContinuedPrice");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "重量界限");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "feeWeightLimit");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "单价");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "feeUnitPrice");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "金额");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "feeAmount");
        headInfoList.add(itemMap);
             
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "创建时间");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "createTime");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "总重量");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "totalWeight");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "首重重量");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "headWeight");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "首重价格");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "headPrice");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "续重重量");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "continuedWeight");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "续重价格");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "continuedPrice");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "重量界限");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "weightLimit");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "单价");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "unitPrice");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "金额");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "amount");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "差额");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "diffAmount");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "状态");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "status");
        headInfoList.add(itemMap);
    
        return headInfoList;
	}
	
	private List<Map<String, Object>> getDispatchFeeHeadItem(List<BillPayDispatchDistinctEncapEntity> list,Map<String, Object> dictcodeMap){
		 List<Map<String, Object>> dataList = new ArrayList<Map<String,Object>>();
	        Map<String, Object> dataItem = null;
	        for (BillPayDispatchDistinctEncapEntity entity : list) {
	        	dataItem = new HashMap<String, Object>();
	        	dataItem.put("billNo", entity.getBillNo());
	        	dataItem.put("feeNo", entity.getFeeNo());
	        	dataItem.put("feeWayBillNo", entity.getFeeWayBillNo());
	        	dataItem.put("feeDeliveryName", entity.getFeeDeliveryName());
	        	dataItem.put("feeTotalWeight", entity.getFeeTotalWeight());
	        	dataItem.put("feeHeadWeight", entity.getFeeHeadWeight());
	        	dataItem.put("feeHeadPrice", entity.getFeeHeadPrice());
	        	dataItem.put("feeContinuedWeight", entity.getFeeContinuedWeight());
	        	dataItem.put("feeContinuedPrice", entity.getFeeContinuedPrice());
	        	dataItem.put("feeWeightLimit", entity.getFeeWeightLimit());
	        	dataItem.put("feeUnitPrice", entity.getFeeUnitPrice());
	        	dataItem.put("feeAmount", entity.getFeeAmount());
	        	dataItem.put("createTime", entity.getCreateTime());
	        	dataItem.put("totalWeight", entity.getTotalWeight());
	        	dataItem.put("headWeight", entity.getHeadWeight());
	        	dataItem.put("headPrice", entity.getHeadPrice());
	        	dataItem.put("continuedWeight", entity.getChargedWeight());
	        	dataItem.put("continuedPrice", entity.getContinuedPrice());
	        	dataItem.put("weightLimit", entity.getWeightLimit());
	        	dataItem.put("unitPrice", entity.getUnitPrice());
	        	dataItem.put("amount", entity.getAmount());
	        	dataItem.put("diffAmount", entity.getDiffAmount());
	        	dataItem.put("status", entity.getStatus());
	        	dataList.add(dataItem);
			}
	        
		return dataList;
	}
	
	/**
	 * 费用文件唯一值
	 */
	public String getName(){
		return sequenceService.getBillNoOne(BillPayDispatchDistinctExportController.class.getName(), "PDD", "000000");
	}
}
