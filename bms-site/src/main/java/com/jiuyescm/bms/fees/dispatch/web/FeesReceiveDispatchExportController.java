package com.jiuyescm.bms.fees.dispatch.web;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
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
import com.jiuyescm.bms.fees.dispatch.service.IFeesReceiveDispatchService;
import com.jiuyescm.bms.fees.dispatch.vo.FeesReceiveDispatchVo;
import com.jiuyescm.cfm.common.JAppContext;
import com.jiuyescm.common.tool.MapTool;
import com.jiuyescm.common.utils.DateUtil;
import com.jiuyescm.common.utils.excel.POISXSSUtil;
import com.jiuyescm.exception.BizException;
import com.jiuyescm.mdm.customer.api.ICustomerService;
import com.jiuyescm.mdm.customer.vo.CustomerVo;
import com.jiuyescm.mdm.warehouse.api.IWarehouseService;
import com.jiuyescm.mdm.warehouse.vo.WarehouseVo;

/**
 * 应收配送费用导出
 * @author zhaof
 */
@Controller("feesReceiveDispatchExportController")
public class FeesReceiveDispatchExportController {

	@Resource private SequenceService sequenceService;
	@Resource private ISystemCodeService systemCodeService;
	@Autowired private IWarehouseService warehouseService;
	@Autowired private IFeesReceiveDispatchService service;
	@Resource private IBmsErrorLogInfoService bmsErrorLogInfoService;
	@Resource private IFileExportTaskService fileExportTaskService;
	@Autowired private ICustomerService customerService;
	
	private static final Logger logger = Logger.getLogger(FeesReceiveDispatchExportController.class.getName());

	final int pageSize = 2000;
	
	@DataResolver
	public String export(Map<String,Object> param) throws Exception{
		
        //将查询条件转为json，判断是否生成过文件，生成过则删 除原来的，生成新的
		Map<String, String> paramMap = new HashMap<String, String>();
		String json1 = "";
        if(param.get("waybillNo")!=null){
        	json1+="【运单号:"+param.get("waybillNo").toString()+"】";
        	paramMap.put("waybillNo",param.get("waybillNo").toString());
        }
        if(param.get("outstockNo")!=null){
        	json1+="【出库单号:"+param.get("outstockNo").toString()+"】";
        	paramMap.put("outstockNo",param.get("outstockNo").toString());
        }
        if(param.get("deliveryid")!=null){
        	json1+="【宅配商:"+param.get("deliveryid").toString()+"】";
        	paramMap.put("deliveryid",param.get("deliveryid").toString());
        }
        if(param.get("customerid")!=null){
        	json1+="【商家:"+param.get("customerid").toString()+"】";
        	paramMap.put("customerid",param.get("customerid").toString());
        }
        if(param.get("warehouseCode")!=null){
        	json1+="【仓库:"+param.get("warehouseCode").toString()+"】";
        	paramMap.put("warehouseCode",param.get("warehouseCode").toString());
        }
        if(param.get("status")!=null){
        	json1+="【状态:"+param.get("status").toString()+"】";
        	paramMap.put("status",param.get("status").toString());
        }
        if(param.get("startTime")!=null){
        	json1+="【开始时间:"+DateUtil.formatTimestamp(param.get("startTime")).toString()+"】";
        	paramMap.put("startTime",DateUtil.formatTimestamp(param.get("startTime")).toString());
        }
        if(param.get("endTime")!=null){
        	paramMap.put("endTime",DateUtil.formatTimestamp(param.get("endTime")).toString());
        	json1+="【结束时间:"+DateUtil.formatTimestamp(param.get("endTime")).toString()+"】";
        }
        paramMap.put(FileTaskTypeEnum.FEE_REC_DISPATCH.getCode(),FileTaskTypeEnum.FEE_REC_DISPATCH.getDesc());
        CustomerVo vo = customerService.queryByCustomerId(param.get("customerid").toString());
		String taskID = MapTool.getMD5String(paramMap);//对查询条件进行签名 唯一标识文件
		
		//判断是否已生成过Excel文件
		//校验该费用是否已生成Excel文件
		Map<String, Object> queryEntity = new HashMap<String, Object>();
		queryEntity.put("taskType", FileTaskTypeEnum.FEE_REC_DISPATCH.getCode());
		queryEntity.put("param1", taskID);
		List<FileExportTaskEntity> taskList = fileExportTaskService.query(queryEntity);
		
		if (null != taskList && taskList.size() > 0) {
			return taskID;
		}
		
		String taskName = vo.getCustomername()+"-配送应收费用导出";
		String path=getPath();
		String filepath="";
		filepath = path + getName() + FileConstant.SUFFIX_XLSX;
		/*if(param.get("isBill")!=null){
			filepath=path+ FileConstant.SEPARATOR + 
					FileConstant.BILL_REC_FEES_DISPATCH_PREFIX+ getName() + FileConstant.SUFFIX_XLSX;
		}else{
			filepath=path+ FileConstant.SEPARATOR + 
					FileConstant.REC_FEES_DISPATCH_PREFIX+ getName() + FileConstant.SUFFIX_XLSX;
		}
		*/
		FileExportTaskEntity entity = new FileExportTaskEntity();
		entity.setStartTime(DateUtil.formatTimestamp(param.get("startTime")));
		entity.setEndTime(DateUtil.formatTimestamp(param.get("endTime")));
		entity.setTaskName(taskName);
		
		//判断是否是账单费用导出
		if(param.get("isBill")!=null){
			entity.setTaskType(FileTaskTypeEnum.BILL_REC_DISPATCH.getCode());
		}else{
			entity.setTaskType(FileTaskTypeEnum.FEE_REC_DISPATCH.getCode());
		}
		entity.setTaskId(taskID);
/*		entity.setTaskType(FileTaskTypeEnum.FEE_REC_DISPATCH.getCode());//导出类型 -- 应收配送费用导出
*/		entity.setTaskState(FileTaskStateEnum.BEGIN.getCode());
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
					logger.error(ExceptionConstant.ASYN_REC_DISPATCH_FEE_EXCEL_EX_MSG, e);
				}
			};
		}.start();
		return entity.getParam1();
			
	}
	
	
	private void export(Map<String, Object> param,String taskId,String file)throws Exception{
		
		updateExportTask(taskId, FileTaskStateEnum.INPROCESS.getCode(), 10);
		String path = getPath();
		long beginTime = System.currentTimeMillis();
		logger.info("====应收宅配费用导出：");
		
    	File storeFolder=new File(path);
		//如果存放上传文件的目录不存在就新建
		if(!storeFolder.isDirectory()){
			storeFolder.mkdirs();
		}
		
    	String filePath = file;
		
    	logger.info("====应收宅配费用导出：写入Excel begin.");
		
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
			PageInfo<FeesReceiveDispatchVo> pageInfo = service.queryFeesImport(myparam, pageNo, pageSize);
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
	
	@FileProvider
	public DownloadFile downloadTemplate(Map<String, Object> param) throws IOException {
		InputStream is = DoradoContext.getCurrent().getServletContext().getResourceAsStream("/WEB-INF/templates/bill/billdispatchdistinct_template.xlsx");
		return new DownloadFile("九曳宅配对账单导入模板.xlsx", is);
	}
	
	
	@SuppressWarnings("deprecation")
	private String getPath(){
		SystemCodeEntity systemCodeEntity = systemCodeService.getSystemCode("GLOABL_PARAM", "EXPORT_RECEIVE_DISPATCH_FEES");
		if(systemCodeEntity == null){
			throw new BizException("请在系统参数中配置文件上传路径,参数GLOABL_PARAM,EXPORT_RECEIVE_DISPATCH_FEES");
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
        itemMap.put("title", "创建时间");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "createTime");
        headInfoList.add(itemMap);
        
       /* itemMap = new HashMap<String, Object>();
        itemMap.put("title", "费用编号");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "feesNo");
        headInfoList.add(itemMap);*/
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "仓库");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "warehouseId");
        headInfoList.add(itemMap);
        
        /*itemMap = new HashMap<String, Object>();
        itemMap.put("title", "商家ID");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "customerid");
        headInfoList.add(itemMap);*/
        
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
        itemMap.put("title", "计费重量");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "chargeWeight");
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
        
        /*itemMap = new HashMap<String, Object>();
        itemMap.put("title", "模板编号");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "templateId");
        headInfoList.add(itemMap);*/
        
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
        itemMap.put("title", "模板类型");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "param1");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "算法");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "param2");
        headInfoList.add(itemMap);
        
        /*itemMap = new HashMap<String, Object>();
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
        headInfoList.add(itemMap);*/
        
       
        
        /*itemMap = new HashMap<String, Object>();
        itemMap.put("title", "规则编号");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "ruleNo");
        headInfoList.add(itemMap);*/
        
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
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "计算状态");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "isCalculated");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "计算备注");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "remark");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "商品明细");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "productDetail");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "责任方");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "dutyType");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "原因详情");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "updateReasonDetail");
        headInfoList.add(itemMap);
     
        return headInfoList;
	}
	
	@SuppressWarnings("rawtypes")
	private List<Map<String, Object>> getDispatchFeeHeadItem(List<FeesReceiveDispatchVo> list,Map<String, Object> dictcodeMap){
		 List<Map<String, Object>> dataList = new ArrayList<Map<String,Object>>();
	        Map<String, Object> dataItem = null;
	        for (FeesReceiveDispatchVo distributionEntity : list) {
	        	dataItem = new HashMap<String, Object>();
	        	dataItem.put("createTime", distributionEntity.getCreateTime());
	        	//dataItem.put("feesNo", distributionEntity.getFeesNo());
	        	dataItem.put("warehouseId", ((Map)dictcodeMap.get("warehouseMap")).get(distributionEntity.getWarehouseCode()));
	        	//dataItem.put("customerid", distributionEntity.getCustomerid());
	        	dataItem.put("customerName", distributionEntity.getCustomerName());
	        	dataItem.put("carrierName", distributionEntity.getCarrierName());
	        	dataItem.put("outstockNo", distributionEntity.getOutstockNo());
	        	dataItem.put("externalNo", distributionEntity.getExternalNo());
	        	dataItem.put("waybillNo", distributionEntity.getWaybillNo());
	        	dataItem.put("toProvinceName", distributionEntity.getToProvinceName());
	        	dataItem.put("toCityName", distributionEntity.getToCityName());
	        	dataItem.put("toDistrictName", distributionEntity.getToDistrictName());
	        	dataItem.put("totalWeight", distributionEntity.getTotalWeight());
	        	dataItem.put("chargeWeight", distributionEntity.getChargedWeight());
	        	dataItem.put("headWeight", distributionEntity.getHeadWeight());
	        	dataItem.put("continuedWeight", distributionEntity.getContinuedWeight());
	        	dataItem.put("headPrice", distributionEntity.getHeadPrice());
	        	dataItem.put("continuedPrice", distributionEntity.getContinuedPrice());
	        	//dataItem.put("templateId", distributionEntity.getTemplateId());
	        	dataItem.put("amount", distributionEntity.getAmount());
	        	dataItem.put("temperatureType", ((Map)dictcodeMap.get("temMap")).get(distributionEntity.getTemperatureType()));
	        	dataItem.put("lianshouTime", distributionEntity.getAcceptTime());
	        	dataItem.put("signTime", distributionEntity.getSignTime());
	        	dataItem.put("billNo", distributionEntity.getBillNo());
	        	//dataItem.put("ruleNo", distributionEntity.getRuleNo());
	        	dataItem.put("status", ((Map)dictcodeMap.get("statusMap")).get(distributionEntity.getStatus()));        	
	        	dataItem.put("bigstatus", distributionEntity.getBigstatus());
	        	dataItem.put("smallstatus", distributionEntity.getSmallstatus());
	        	dataItem.put("param1", distributionEntity.getParam1());
	        	dataItem.put("param2", distributionEntity.getParam2());
	        	//dataItem.put("param3", distributionEntity.getParam3());
	        	//dataItem.put("param4", distributionEntity.getParam4());
	        	//dataItem.put("param5", distributionEntity.getParam5());
	        	dataItem.put("isCalculated", distributionEntity.getIsCalculated());
	        	dataItem.put("remark", distributionEntity.getRemark());
	        	dataItem.put("productDetail", distributionEntity.getProductDetail());
	        	dataItem.put("dutyType", distributionEntity.getDutyType());
	        	dataItem.put("updateReasonDetail", distributionEntity.getUpdateReasonDetail());
	        	dataList.add(dataItem);
			}
	        
		return dataList;
	}
	
	/**
	 * 费用文件唯一值
	 */
	public String getName(){
		return sequenceService.getBillNoOne(FeesReceiveDispatchExportController.class.getName(), "FY", "000000");
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
