package com.jiuyescm.bms.fees.out.dispatch.web;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.bstek.dorado.annotation.DataResolver;
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
import com.jiuyescm.bms.common.log.entity.BmsErrorLogInfoEntity;
import com.jiuyescm.bms.common.log.service.IBmsErrorLogInfoService;
import com.jiuyescm.bms.common.sequence.service.SequenceService;
import com.jiuyescm.bms.fees.out.dispatch.entity.FeesPayDispatchEntity;
import com.jiuyescm.bms.fees.out.dispatch.service.IFeesPayDispatchService;
import com.jiuyescm.cfm.common.JAppContext;
import com.jiuyescm.common.utils.DateUtil;
import com.jiuyescm.common.utils.excel.POISXSSUtil;
import com.jiuyescm.exception.BizException;
import com.jiuyescm.mdm.warehouse.api.IWarehouseService;
import com.jiuyescm.mdm.warehouse.vo.WarehouseVo;

/**
 * 应收配送费用导出
 * @author zhaof
 */
@Controller("feesPayDispatchExportController")
public class FeesPayDispatchExportController {

	@Resource 
	private SequenceService sequenceService;
	@Resource
	private ISystemCodeService systemCodeService;
	@Autowired
	private IWarehouseService warehouseService;
	@Autowired
	private IFeesPayDispatchService service;
	
	@Resource
	private IFileExportTaskService fileExportTaskService;
	@Resource
	private IBmsErrorLogInfoService bmsErrorLogInfoService;
	
	private static final Logger logger = Logger.getLogger(IFeesPayDispatchService.class.getName());

	final int pageSize = 2000;
	
	@DataResolver
	public String export(Map<String,Object> param) throws Exception{
		if (param == null){
			return MessageConstant.DISPATCH_FEES_INFO_ISNULL_MSG;
		}
				
        //将查询条件转为json，判断是否生成过文件，生成过则删除原来的，生成新的
        String json1="";
        if(param.get("waybillNo")!=null){
        	json1+=param.get("waybillNo").toString();
        }
        if(param.get("outstockNo")!=null){
        	json1+=param.get("outstockNo").toString();
        }
        if(param.get("deliveryid")!=null){
        	json1+=param.get("deliveryid").toString();
        }
        if(param.get("customerid")!=null){
        	json1+=param.get("customerid").toString();
        }
        if(param.get("warehouseCode")!=null){
        	json1+=param.get("warehouseCode").toString();
        }
        if(param.get("status")!=null){
        	json1+=param.get("status").toString();
        }
        if(param.get("startTime")!=null){
        	json1+=param.get("startTime").toString();
        }
        if(param.get("endTime")!=null){
        	json1+=param.get("endTime").toString();
        }
		
		//校验该费用是否已生成Excel文件
		Map<String, Object> queryEntity = new HashMap<String, Object>();
		queryEntity.put("taskType", FileTaskTypeEnum.FEE_PAY_DISPATCH.getCode());
		queryEntity.put("param1", json1);
		List<FileExportTaskEntity> taskList = fileExportTaskService.query(queryEntity);
		
		if (null != taskList && taskList.size() > 0) {
			FileExportTaskEntity entity=taskList.get(0);
			entity.setDelFlag("1");
			int num=fileExportTaskService.update(entity);
			if(num<=0){
				return "删除费用失败";
			}
		}
		
		String path=getPath();
		String filePath=path+ FileConstant.SEPARATOR + 
				FileConstant.PAY_FEES_DISPATCH_PREFIX+ getName() + FileConstant.SUFFIX_XLSX;
		FileExportTaskEntity entity = new FileExportTaskEntity();
		
		if(param.get("startTime")!=null){
			entity.setStartTime(DateUtil.formatTimestamp(param.get("startTime")));
		}	
		if(param.get("endTime")!=null){
			entity.setEndTime(DateUtil.formatTimestamp(param.get("endTime")));
		}	
		
		entity.setTaskName("配送费用导出");
		entity.setTaskType(FileTaskTypeEnum.FEE_PAY_DISPATCH.getCode());
		entity.setTaskState(FileTaskStateEnum.BEGIN.getCode());
		entity.setProgress(0d);
		entity.setFilePath(filePath);
		entity.setCreator(JAppContext.currentUserName());
		entity.setCreateTime(JAppContext.currentTimestamp());
		entity.setParam1(json1);
		entity.setDelFlag("0");
		entity = fileExportTaskService.save(entity);
		
		//生成账单文件
		final Map<String, Object> condition = param;
		final String taskId = entity.getTaskId();
		final String file=filePath;
		new Thread(){
			public void run() {
				try {
					export(condition, taskId,file);
				} catch (Exception e) {
					updateExportTask(taskId, FileTaskStateEnum.FAIL.getCode(), 0);
					logger.error(ExceptionConstant.ASYN_PAY_DISPATCH_FEE_EXCEL_EX_MSG, e);
					//写入日志
					BmsErrorLogInfoEntity bmsErrorLogInfoEntity=new BmsErrorLogInfoEntity(this.getClass().getSimpleName(),Thread.currentThread().getStackTrace()[1].getMethodName(), "", e.toString());
					bmsErrorLogInfoService.log(bmsErrorLogInfoEntity);
				}
			};
		}.start();
		return MessageConstant.EXPORT_TASK_RECE_BILL_MSG;
			
	}
	
	
	private void export(Map<String, Object> param,String taskId,String file)throws Exception{
		updateExportTask(taskId, FileTaskStateEnum.INPROCESS.getCode(), 10);
		String path = getPath();
		long beginTime = System.currentTimeMillis();
		logger.info("====应付宅配费用导出：");
		
    	File storeFolder=new File(path);
		//如果存放上传文件的目录不存在就新建
		if(!storeFolder.isDirectory()){
			storeFolder.mkdirs();
		}
		
    	String filePath = file;
		
    	logger.info("====应付宅配费用导出：写入Excel begin.");
		
		// 所有转码Map
		Map<String, Object> dictcodeMap = new HashMap<String, Object>();
		//状态
		Map<String, String> statusMap = new HashMap<String, String>();
		statusMap.put("0", "未过账");
		statusMap.put("1", "已过账");
		dictcodeMap.put("statusMap", statusMap);
		try {
			//温度类型
			Map<String, String> temMap=getTemperatureMap();
			dictcodeMap.put("temMap", temMap);
			//仓库
			List<WarehouseVo> warehouseVos = warehouseService.queryAllWarehouse();
			Map<String, String> warehouseMap = new LinkedHashMap<String,String>();
			for (WarehouseVo warehouseVo : warehouseVos) {
				warehouseMap.put(warehouseVo.getWarehouseid(), warehouseVo.getWarehousename());
			}
			dictcodeMap.put("warehouseMap", warehouseMap);
			
			if("全部".equals(param.get("status"))){
				param.put("status", null);
			}
			
			POISXSSUtil poiUtil = new POISXSSUtil();
	    	SXSSFWorkbook workbook = poiUtil.getXSSFWorkbook();
	    			    	
	    	updateExportTask(taskId, null, 30);
	    	
	        //配送明细
	    	handDispatch(poiUtil, workbook, filePath, param,dictcodeMap);
	    	
	    	updateExportTask(taskId, null, 70);

	    	//最后写到文件
	    	poiUtil.write2FilePath(workbook, filePath);
	    	
	    	updateExportTask(taskId, FileTaskStateEnum.SUCCESS.getCode(), 100);
	    	
	    	logger.info("====应付配送费用导出：写入Excel end.==总耗时：" + (System.currentTimeMillis() - beginTime));

		} catch (Exception e) {
		    logger.error(e);
			//写入日志
			BmsErrorLogInfoEntity bmsErrorLogInfoEntity=new BmsErrorLogInfoEntity(this.getClass().getSimpleName(),Thread.currentThread().getStackTrace()[1].getMethodName(), "", e.toString());
			bmsErrorLogInfoService.log(bmsErrorLogInfoEntity);
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
			PageInfo<FeesPayDispatchEntity> pageInfo = service.query(myparam, pageNo, pageSize);
			if (null != pageInfo && pageInfo.getList().size() > 0) {
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
			
			poiUtil.exportExcel2FilePath(poiUtil, workbook, MessageConstant.BILL_DISPATCH_DETAIL_MSG, 
					dispatchLineNo, headdistributionDetailMapList, datadistributionDetailList);
			if (null != pageInfo && pageInfo.getList().size() > 0) {
				dispatchLineNo += pageInfo.getList().size();
			}
		}
	}
		
	@SuppressWarnings("deprecation")
	private String getPath(){
		SystemCodeEntity systemCodeEntity = systemCodeService.getSystemCode("GLOABL_PARAM", "EXPORT_PAY_DISPATCH_FEES");
		if(systemCodeEntity == null){
			throw new BizException("请在系统参数中配置文件上传路径,参数GLOABL_PARAM,EXPORT_PAY_DISPATCH_FEES");
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
		
		itemMap.put("title", "ID");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "id");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "创建时间");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "createTime");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "费用编号");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "feesNo");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "仓库");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "warehouseId");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "商家ID");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "customerid");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "商家名称");
        itemMap.put("columnWidth", 50);
        itemMap.put("dataKey", "customerName");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "物流商");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "carrierName");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "出库单号");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "outstockNo");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "外部单号");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "externalNo");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "运单号");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "waybillNo");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "目的省");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "toProvinceName");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "目的市");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "toCityName");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "目的区县");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "toDistrictName");
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
        itemMap.put("title", "续重重量");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "continuedWeight");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "首重价格");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "headPrice");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "续重价格");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "continuedPrice");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "模板编号");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "templateId");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "金额");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "amount");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "温度类型");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "temperatureType");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "揽收时间");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "lianshouTime");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "签收时间");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "signTime");
        headInfoList.add(itemMap);
        
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "大状态");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "bigstatus");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "小状态");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "smallstatus");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "参数1");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "param1");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "参数2");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "param2");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "参数3");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "param3");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "参数4");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "param4");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "参数5");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "param5");
        headInfoList.add(itemMap);
        
       
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "规则编号");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "ruleNo");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "账单编号");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "billNo");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "状态");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "status");
        headInfoList.add(itemMap);
		
        return headInfoList;
	}
	
	@SuppressWarnings("rawtypes")
	private List<Map<String, Object>> getDispatchFeeHeadItem(List<FeesPayDispatchEntity> list,Map<String, Object> dictcodeMap){
		 List<Map<String, Object>> dataList = new ArrayList<Map<String,Object>>();
	        Map<String, Object> dataItem = null;
	        for (FeesPayDispatchEntity distributionEntity : list) {
	        	dataItem = new HashMap<String, Object>();
	        	dataItem.put("id", distributionEntity.getId());
	        	dataItem.put("createTime", distributionEntity.getCreateTime());
	        	dataItem.put("feesNo", distributionEntity.getFeesNo());
	        	dataItem.put("warehouseId", ((Map)dictcodeMap.get("warehouseMap")).get(distributionEntity.getWarehouseCode()));
	        	dataItem.put("customerid", distributionEntity.getCustomerid());
	        	dataItem.put("customerName", distributionEntity.getCustomerName());
	        	dataItem.put("carrierName", distributionEntity.getCarrierName());
	        	dataItem.put("outstockNo", distributionEntity.getOutstockNo());
	        	dataItem.put("externalNo", distributionEntity.getExternalNo());
	        	dataItem.put("waybillNo", distributionEntity.getWaybillNo());
	        	dataItem.put("toProvinceName", distributionEntity.getToProvinceName());
	        	dataItem.put("toCityName", distributionEntity.getToCityName());
	        	dataItem.put("toDistrictName", distributionEntity.getToDistrictName());
	        	dataItem.put("totalWeight", distributionEntity.getTotalWeight());
	        	dataItem.put("headWeight", distributionEntity.getHeadWeight());
	        	dataItem.put("continuedWeight", distributionEntity.getContinuedWeight());
	        	dataItem.put("headPrice", distributionEntity.getHeadPrice());
	        	dataItem.put("continuedPrice", distributionEntity.getContinuedPrice());
	        	dataItem.put("templateId", distributionEntity.getTemplateId());
	        	dataItem.put("amount", distributionEntity.getAmount());
	        	dataItem.put("temperatureType", ((Map)dictcodeMap.get("temMap")).get(distributionEntity.getTemperatureType()));
	        	dataItem.put("lianshouTime", distributionEntity.getAcceptTime());
	        	dataItem.put("signTime", distributionEntity.getSignTime());
	        	dataItem.put("param1", distributionEntity.getParam1());
	        	dataItem.put("param2", distributionEntity.getParam2());
	        	dataItem.put("param3", distributionEntity.getParam3());
	        	dataItem.put("param4", distributionEntity.getParam4());
	        	dataItem.put("param5", distributionEntity.getParam5());
	        	dataItem.put("billNo", distributionEntity.getBillNo());
	        	dataItem.put("ruleNo", distributionEntity.getRuleNo());
	        	dataItem.put("status", ((Map)dictcodeMap.get("statusMap")).get(distributionEntity.getStatus()));        	
	        	dataItem.put("bigstatus", distributionEntity.getBigstatus());
	        	dataItem.put("smallstatus", distributionEntity.getSmallstatus());
	        	dataList.add(dataItem);
			}
	        
		return dataList;
	}
	
	/**
	 * 费用文件唯一值
	 */
	public String getName(){
		return sequenceService.getBillNoOne(FeesPayDispatchExportController.class.getName(), "FY", "000000");
	}
	
	/**
	 * 温度类型
	 * @return
	 */
	private Map<String, String> getTemperatureMap(){
		List<SystemCodeEntity> systemCodeList3 = systemCodeService.findEnumList("TEMPERATURE_TYPE");
		Map<String, String> tempretureMap =new HashMap<String,String>();
		if(systemCodeList3!=null && systemCodeList3.size()>0){
			for(int i=0;i<systemCodeList3.size();i++){
				tempretureMap.put(systemCodeList3.get(i).getCode(), systemCodeList3.get(i).getCodeName());
			}
		}
		return tempretureMap;
	}
}
