package com.jiuyescm.bms.fees.abnormal.controller;

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
import com.jiuyescm.bms.common.enumtype.CalculateState;
import com.jiuyescm.bms.common.enumtype.FileTaskStateEnum;
import com.jiuyescm.bms.common.enumtype.FileTaskTypeEnum;
import com.jiuyescm.bms.common.log.entity.BmsErrorLogInfoEntity;
import com.jiuyescm.bms.common.log.service.IBmsErrorLogInfoService;
import com.jiuyescm.bms.common.sequence.service.SequenceService;
import com.jiuyescm.bms.fees.abnormal.entity.FeesAbnormalEntity;
import com.jiuyescm.bms.fees.abnormal.service.IFeesAbnormalService;
import com.jiuyescm.cfm.common.JAppContext;
import com.jiuyescm.common.utils.DateUtil;
import com.jiuyescm.common.utils.excel.POISXSSUtil;
import com.jiuyescm.exception.BizException;
import com.jiuyescm.mdm.warehouse.api.IWarehouseService;
import com.jiuyescm.mdm.warehouse.vo.WarehouseVo;

/**
 * 异常费用导出
 * @author zhaof
 */
@Controller("feesAbnormalExportController")
public class FeesAbnormalExportController {

	@Resource 
	private SequenceService sequenceService;
	@Resource
	private ISystemCodeService systemCodeService;
	@Autowired
	private IWarehouseService warehouseService;
	@Autowired
	private IFeesAbnormalService service;
	
	@Resource
	private IFileExportTaskService fileExportTaskService;
	
	@Resource
	private IBmsErrorLogInfoService bmsErrorLogInfoService;
	
	private static final Logger logger = Logger.getLogger(FeesAbnormalExportController.class.getName());

	final int pageSize = 2000;
	
	
	@DataResolver
	public String export(Map<String,Object> param) throws Exception{

		if (param == null){
			return MessageConstant.ABNORAML_FEES_INFO_ISNULL_MSG;
		}
		
        //将查询条件转为json，判断是否生成过文件，生成过则删除原来的，生成新的
  /*      String json1="";
        if(param.get("waybillNo")!=null){
        	json1+=param.get("waybillNo").toString();
        }
        if(param.get("outstockNo")!=null){
        	json1+=param.get("outstockNo").toString();
        }
        if(param.get("deliveryid")!=null){
        	json1+=param.get("deliveryid").toString();
        }
        if(param.get("customerId")!=null){
        	json1+=param.get("customerId").toString();
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
        }*/
      	
		/*//校验该费用是否已生成Excel文件
		Map<String, Object> queryEntity = new HashMap<String, Object>();
		queryEntity.put("taskType", FileTaskTypeEnum.FEE_REC_DISPATCH.getCode());
		queryEntity.put("param1", json1);
		List<FileExportTaskEntity> taskList = fileExportTaskService.query(queryEntity);
		//如果存在了则删除老的
		if (null != taskList && taskList.size() > 0) {
			FileExportTaskEntity entity=taskList.get(0);
			entity.setDelFlag("1");
			int num=fileExportTaskService.update(entity);
			if(num<=0){
				return "删除费用失败";
			}
		}*/
		
		String path=getPath();
		String filepath=path+ FileConstant.SEPARATOR + 
				FileConstant.FEES_ABNORMAL_PREFIX+ getName() + FileConstant.SUFFIX_XLSX;
		FileExportTaskEntity entity = new FileExportTaskEntity();
		
		if(param.get("startTime")!=null){
			entity.setStartTime(DateUtil.formatTimestamp(param.get("startTime")));
		}	
		if(param.get("endTime")!=null){
			entity.setEndTime(DateUtil.formatTimestamp(param.get("endTime")));
		}
		entity.setTaskName(param.get("customerName")+"异常费用导出");
		
		
		String subjectCode="";
		if(param.containsKey("subjectCode")){
			subjectCode=param.get("subjectCode").toString();
		}
		//判断是否是账单费用导出
		
		if(param.containsKey("feesType")){
			String feesType=param.get("feesType").toString();
			if(!StringUtils.isBlank(feesType)){
				List<String> reasonIds=null;
				switch(feesType){
				case "DISPATCH":
					if(subjectCode.equals("Abnormal_Dispatch")){
						reasonIds=getDispatchReasonId();
					}else if(subjectCode.equals("Abnormal_DisChange")){
						reasonIds=getDispatchChangeReasonId();
					}else{
						return "未知的费用类型【"+subjectCode.equals("Abnormal_Dispatch")+"】";
					}
					entity.setTaskType(FileTaskTypeEnum.BILL_ABNORMAL_DISPATCH.getCode());
					break;
				case "STORAGE":
					reasonIds=getStorageReasonId();
					entity.setTaskType(FileTaskTypeEnum.BILL_ABNORMAL_STORAGE.getCode());
					break;
				}
				param.put("reasonIds", reasonIds);
			}
		}
		
		entity.setTaskState(FileTaskStateEnum.BEGIN.getCode());
		entity.setProgress(0d);
		entity.setFilePath(filepath);
		entity.setCreator(JAppContext.currentUserName());
		entity.setCreateTime(JAppContext.currentTimestamp());
		/*entity.setParam1(json1);*/
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
					logger.error(ExceptionConstant.ASYN_Bill_ABNORAML_FEE_EXCEL_EX_MSG, e);
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
		logger.info("====异常费用导出：");
		
    	File storeFolder=new File(path);
		//如果存放上传文件的目录不存在就新建
		if(!storeFolder.isDirectory()){
			storeFolder.mkdirs();
		}
		
    	String filePath = file;
		
    	logger.info("====异常费用导出：写入Excel begin.");
		
		// 所有转码Map
		Map<String, Object> dictcodeMap = new HashMap<String, Object>();
		//状态
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
	    	
	        //异常明细
	    	handAbnormal(poiUtil, workbook, filePath, param,dictcodeMap);
	    	
	    	updateExportTask(taskId, null, 70);

	    	//最后写到文件
	    	poiUtil.write2FilePath(workbook, filePath);
	    	
	    	updateExportTask(taskId, FileTaskStateEnum.SUCCESS.getCode(), 100);
	    	
	    	logger.info("====应收配送费用导出：写入Excel end.==总耗时：" + (System.currentTimeMillis() - beginTime));

		} catch (Exception e) {
			e.printStackTrace();
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
	private void handAbnormal(POISXSSUtil poiUtil, SXSSFWorkbook workbook, 
			String path, Map<String, Object> myparam,Map<String, Object> dictcodeMap)throws Exception{
		int pageNo = 1;
		int dispatchLineNo = 1;
		boolean doLoop = true;
		while (doLoop) {			
			PageInfo<FeesAbnormalEntity> pageInfo = service.query(myparam, pageNo, pageSize);
			if (null != pageInfo && pageInfo.getList().size() > 0) {
				if (pageInfo.getList().size() < pageSize) {
					doLoop = false;
				}else {
					pageNo += 1; 
				}
			}else {
				doLoop = false;
			}
			
			List<Map<String, Object>> headdistributionDetailMapList = getAbnormalFeeHead(); 
			List<Map<String, Object>> datadistributionDetailList = getAbnormalFeeHeadItem(pageInfo.getList(),dictcodeMap);
			
			poiUtil.exportExcel2FilePath(poiUtil, workbook, MessageConstant.BILL_ABNORMAL_DETAIL_MSG, 
					dispatchLineNo, headdistributionDetailMapList, datadistributionDetailList);
			if (null != pageInfo && pageInfo.getList().size() > 0) {
				dispatchLineNo += pageInfo.getList().size();
			}
		}
	}	
	
	@SuppressWarnings("deprecation")
	private String getPath(){
		SystemCodeEntity systemCodeEntity = systemCodeService.getSystemCode("GLOABL_PARAM", "EXPORT_ABNORMAL");
		if(systemCodeEntity == null){
			throw new BizException("请在系统参数中配置文件上传路径,参数GLOABL_PARAM,EXPORT_ABNORMAL");
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
	 * 异常费用明细
	 */
	public List<Map<String, Object>> getAbnormalFeeHead(){
		List<Map<String, Object>> headInfoList = new ArrayList<Map<String,Object>>();
		Map<String, Object> itemMap = new HashMap<String, Object>();
		
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "费用编号");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "feesNo");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "赔付金额");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "payMoney");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "减免金额");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "derateAmount");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "实收金额");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "receiptAmount");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "是否免运费");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "isDeliveryFree");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "运费");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "deliveryCost");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "外部订单号");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "reference");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "运单号");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "expressnum");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "内部订单号");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "orderNo");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "原因归属");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "reason");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "原因详情");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "reasonDetail");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "备注");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "remark");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "是否争议");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "isConflictStr");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "承运商名称");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "carrierName");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "宅配商名称");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "deliverName");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "是否赔付");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "isPayStr");
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
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "创建人");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "createPersonName");
        headInfoList.add(itemMap);
        
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "创建时间");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "createTime");
        headInfoList.add(itemMap);
      
        return headInfoList;
	}
	
	private List<Map<String, Object>> getAbnormalFeeHeadItem(List<FeesAbnormalEntity> list,Map<String, Object> dictcodeMap){
		 List<Map<String, Object>> dataList = new ArrayList<Map<String,Object>>();
	        Map<String, Object> dataItem = null;
	        for (FeesAbnormalEntity entity : list) {
	        	dataItem=new HashMap<String,Object>();
	        	dataItem.put("feesNo", entity.getFeeNo());
	        	dataItem.put("payMoney", entity.getPayMoney());
	        	dataItem.put("derateAmount", entity.getDerateAmount());
	        	double payMoney=entity.getPayMoney()==null?0:entity.getPayMoney();
        		double derateAmount=entity.getDerateAmount()==null?0:entity.getDerateAmount();
        		double deliveryCost=entity.getDeliveryCost()==null?0:entity.getDeliveryCost();
        		String isDeliveryFree="";
	        	if(StringUtils.isNoneBlank(entity.getIsDeliveryFree())&&entity.getIsDeliveryFree().equals("0")){
	        		dataItem.put("receiptAmount", payMoney-derateAmount-deliveryCost);
	        		isDeliveryFree="是";
	        	}else{
	        		dataItem.put("receiptAmount", payMoney-derateAmount);
	        		isDeliveryFree="否";
	        	}
	        	dataItem.put("isDeliveryFree",isDeliveryFree);
	        	dataItem.put("deliveryCost",deliveryCost);
	        	dataItem.put("reference", entity.getReference());
	        	dataItem.put("expressnum", entity.getExpressnum());
	        	dataItem.put("orderNo", entity.getOrderNo());
	        	dataItem.put("reason", entity.getReason());
	        	dataItem.put("reasonDetail", entity.getReasonDetail());
	        	dataItem.put("remark", entity.getRemark());
	        	dataItem.put("isConflictStr", entity.getIsConflictStr());
	        	dataItem.put("carrierName", entity.getCarrierName());
	        	dataItem.put("deliverName", entity.getDeliverName());
	        	dataItem.put("isPayStr", entity.getIsPayStr());
	        	dataItem.put("billNo", entity.getBillNo());
	        	dataItem.put("status", CalculateState.getDesc(entity.getIsCalculated()));
	        	dataItem.put("createPersonName", entity.getCreatePersonName());
	        	dataItem.put("createTime", entity.getCreateTime());
	        	dataList.add(dataItem);
			}
	        
		return dataList;
	}
	
	/**
	 * 费用文件唯一值
	 */
	public String getName(){
		return sequenceService.getBillNoOne(FeesAbnormalExportController.class.getName(), "FY", "000000");
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
	
	/**
	 * 仓储理赔原因ID
	 * @return
	 */
	private List<String> getStorageReasonId(){
		List<String> list=new ArrayList<String>();
		list.add("56");//公司原因
		list.add("3");//仓库原因
		return list;
	}
	/**
	 * 配送理赔原因ID
	 * @return
	 */
	private List<String> getDispatchReasonId(){
		List<String> list=new ArrayList<String>();
		list.add("2");//承运商原因
		return list;
	}
	/**
	 * 配送理赔原因ID
	 * @return
	 */
	private List<String> getDispatchChangeReasonId(){
		List<String> list=new ArrayList<String>();
		list.add("4");//商家原因
		return list;
	}
}
