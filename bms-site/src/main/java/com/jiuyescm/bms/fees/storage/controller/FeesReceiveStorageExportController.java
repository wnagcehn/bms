package com.jiuyescm.bms.fees.storage.controller;

import java.io.File;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.base.dictionary.entity.SystemCodeEntity;
import com.jiuyescm.bms.base.dictionary.service.ISystemCodeService;
import com.jiuyescm.bms.base.file.entity.FileExportTaskEntity;
import com.jiuyescm.bms.base.file.service.IFileExportTaskService;
import com.jiuyescm.bms.base.group.service.IBmsGroupSubjectService;
import com.jiuyescm.bms.common.constants.ExceptionConstant;
import com.jiuyescm.bms.common.constants.FileConstant;
import com.jiuyescm.bms.common.constants.MessageConstant;
import com.jiuyescm.bms.common.enumtype.FileTaskStateEnum;
import com.jiuyescm.bms.common.enumtype.FileTaskTypeEnum;
import com.jiuyescm.bms.common.log.service.IBmsErrorLogInfoService;
import com.jiuyescm.bms.common.sequence.service.SequenceService;
import com.jiuyescm.bms.fees.storage.entity.FeesReceiveStorageEntity;
import com.jiuyescm.bms.fees.storage.service.IFeesReceiveStorageService;
import com.jiuyescm.cfm.common.JAppContext;
import com.jiuyescm.common.utils.DateUtil;
import com.jiuyescm.common.utils.excel.POISXSSUtil;
import com.jiuyescm.exception.BizException;
import com.jiuyescm.mdm.warehouse.api.IWarehouseService;
import com.jiuyescm.mdm.warehouse.vo.WarehouseVo;

/**
 * 应收仓储费用导出
 * @author zhaof
 */
@Controller("feesReceiveStorageExportController")
public class FeesReceiveStorageExportController {

	@Resource 
	private SequenceService sequenceService;
	@Resource
	private ISystemCodeService systemCodeService;
	@Autowired
	private IWarehouseService warehouseService;	
	@Resource
	private IFeesReceiveStorageService feesReceiveStorageService;
	
	@Resource
	private IFileExportTaskService fileExportTaskService;
	
	@Resource
	private IBmsErrorLogInfoService bmsErrorLogInfoService;
	
	@Resource
	private IBmsGroupSubjectService bmsGroupSubjectService;
	private static final Logger logger = Logger.getLogger(FeesReceiveStorageExportController.class.getName());

	final int pageSize = 2000;
	
	//费用类型 Map
	private Map<String,String> mapWarehouse;
	private Map<String,String> costSubjectMap;
	private Map<String,String> tempretureMap;
	private Map<String,String> mapStatus;
	private Map<String,String> extraSubjectMap;
	private Map<String,String> chargeTypeMap;
	
	
	@DataProvider
	public Map<String, String> getChargeTypeMap() {  
		Map<String, String> mapValue = new LinkedHashMap<String, String>();
		List<SystemCodeEntity> codeList = systemCodeService.findEnumList("STORAGE_CHARGE_TYPE");
		for (SystemCodeEntity code : codeList){
			mapValue.put(code.getCode(), code.getCodeName());
		}
		return mapValue;
	}
	@DataProvider
	public Map<String, String> getExtraList() {
				
		Map<String, String> mapValue = new LinkedHashMap<String, String>();
		
		mapValue=bmsGroupSubjectService.getExportSubject("receive_wh_valueadd_subject");

	
		/*List<SystemCodeEntity> tmscodels = systemCodeService.findEnumList("wh_value_add_subject");
		
		mapValue.put("ALL","全部");
		
		for (SystemCodeEntity SystemCodeEntity : tmscodels) {
			
			mapValue.put(SystemCodeEntity.getCode(), SystemCodeEntity.getCodeName());
		}
		
		if(containStr("wh_value_add_subject"))
		{
			mapValue.remove("wh_value_add_subject");
			mapValue.remove("wh_material_use");
		}*/
		
		return mapValue;
	}
	
	
	private Map<String,String> getStatus(){
		Map<String,String> map=new HashMap<String,String>();
		map.put("0", "未过账");
		map.put("1", "已过账");
		return map;
	}
	
	private Map<String,String> getwarehouse(){
		List<WarehouseVo> warehouseVos = warehouseService.queryAllWarehouse();
		Map<String, String> map = new LinkedHashMap<String,String>();
		for (WarehouseVo warehouseVo : warehouseVos) {
			map.put(warehouseVo.getWarehouseid(), warehouseVo.getWarehousename());
		}
		return map;
	}
	
	private Map<String, String> getCostSubjectMap(){
		Map<String, String> costSubjectMap = new HashMap<String, String>();
		
		costSubjectMap=bmsGroupSubjectService.getExportSubject("receive_wh_detail_subject");

	/*	List<SystemCodeEntity> systemCodeList1 = systemCodeService.findEnumList("STORAGE_PRICE_SUBJECT");
		List<SystemCodeEntity> systemCodeList2 = systemCodeService.findEnumList("STORAGE_ADD_VALUE");
		if(systemCodeList1!=null && systemCodeList1.size()>0){
			for(int i=0;i<systemCodeList1.size();i++){
				costSubjectMap.put(systemCodeList1.get(i).getCode(), systemCodeList1.get(i).getCodeName());
			}
		}
		if(systemCodeList2!=null && systemCodeList2.size()>0){
			for(int i=0;i<systemCodeList2.size();i++){
				costSubjectMap.put(systemCodeList2.get(i).getCode(), systemCodeList2.get(i).getCodeName());
			}
		}*/
		return costSubjectMap;
	}
	//温度类型
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
	
	
	@DataResolver
	public String export(Map<String,Object> param) throws Exception{
		if (param == null){
			return MessageConstant.STORAGE_FEES_INFO_ISNULL_MSG;
		}		
		
		SimpleDateFormat  sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
		sdf.setTimeZone(TimeZone.getTimeZone("CST"));
		
		Timestamp startTime=DateUtil.formatTimestamp(param.get("createTimeBegin"));
		Timestamp endTime=DateUtil.formatTimestamp(param.get("createTimeEnd"));
		
        //将查询条件转为json，判断是否生成过文件，生成过则删除原来的，生成新的
        String json1="";
        if(param.get("customerId")!=null){
        	json1+=param.get("customerId").toString();
        }
        if(param.get("subjectCode")!=null){
        	json1+=param.get("subjectCode").toString();
        }
        if(param.get("status")!=null){
        	json1+=param.get("status").toString();
        }
        if(param.get("warehouseCode")!=null){
        	json1+=param.get("warehouseCode").toString();
        }
        if(param.get("createTimeBegin")!=null){
        	json1+=param.get("createTimeBegin").toString();
        }
        if(param.get("createTimeEnd")!=null){
        	json1+=param.get("createTimeEnd").toString();
        }
        if(param.get("otherSubjectCode")!=null){
        	json1+=param.get("otherSubjectCode").toString();
        }
        if(param.get("orderNo")!=null){
        	json1+=param.get("orderNo").toString();
        }
		
		
		//校验该费用是否已生成Excel文件
		Map<String, Object> queryEntity = new HashMap<String, Object>();
		queryEntity.put("taskType", FileTaskTypeEnum.FEE_STORAGE.getCode());
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
				FileConstant.FEES_STORAGE_PREFIX+ getName() + FileConstant.SUFFIX_XLSX;
		FileExportTaskEntity entity = new FileExportTaskEntity();
		
		entity.setStartTime(startTime);
		entity.setEndTime(endTime);
		entity.setTaskName("仓储费用导出");
		entity.setTaskType(FileTaskTypeEnum.FEE_STORAGE.getCode());
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
					logger.error(ExceptionConstant.ASYN_REC_STORAGE_FEE_EXCEL_EX_MSG, e);
					//写入日志
					bmsErrorLogInfoService.insertLog(this.getClass().getSimpleName(),Thread.currentThread().getStackTrace()[1].getMethodName(), "", e.toString());
				}
			};
		}.start();
		return MessageConstant.EXPORT_TASK_RECE_BILL_MSG;
			
	}
	
	
	private void export(Map<String, Object> param,String taskId,String file)throws Exception{
		updateExportTask(taskId, FileTaskStateEnum.INPROCESS.getCode(), 10);
		String path = getPath();
		long beginTime = System.currentTimeMillis();
		logger.info("====应收仓储费用导出：");
		
    	File storeFolder=new File(path);
		//如果存放上传文件的目录不存在就新建
		if(!storeFolder.isDirectory()){
			storeFolder.mkdirs();
		}
		
    	String filePath = file;
		
    	logger.info("====应收仓储费用导出：写入Excel begin.");
		
    	//初始化参数
    	init();
		try {
	
			if("全部".equals(param.get("status"))){
				param.put("status", null);
			}
						
			POISXSSUtil poiUtil = new POISXSSUtil();
	    	SXSSFWorkbook workbook = poiUtil.getXSSFWorkbook();
	    	
	    	updateExportTask(taskId, null, 30);
	    	
	        //配送明细
	    	handDispatch(poiUtil, workbook, filePath, param);
	    	
	    	updateExportTask(taskId, null, 70);

	    	//最后写到文件
	    	poiUtil.write2FilePath(workbook, filePath);
	    	
	    	updateExportTask(taskId, FileTaskStateEnum.SUCCESS.getCode(), 100);
	    	
	    	logger.info("====应收配送费用导出： 写入Excel end.==总耗时：" + (System.currentTimeMillis() - beginTime));

		} catch (Exception e) {
			//写入日志
			bmsErrorLogInfoService.insertLog(this.getClass().getSimpleName(),Thread.currentThread().getStackTrace()[1].getMethodName(), "", e.toString());
			logger.error(e);
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
			String path, Map<String, Object> myparam)throws Exception{
		int pageNo = 1;
		int dispatchLineNo = 1;
		boolean doLoop = true;
		while (doLoop) {	
			PageInfo<FeesReceiveStorageEntity> pageInfo = feesReceiveStorageService.queryFees(myparam, pageNo, pageSize);
			if (null != pageInfo && pageInfo.getList().size() > 0) {
				if (pageInfo.getList().size() < pageSize) {
					doLoop = false;
				}else {
					pageNo += 1; 
				}
			}else {
				doLoop = false;
			}
			
			List<Map<String, Object>> headdistributionDetailMapList = getStorageFeeHead(); 
			List<Map<String, Object>> datadistributionDetailList = getStorageFeeHeadItem(pageInfo.getList());
			
			poiUtil.exportExcel2FilePath(poiUtil, workbook, MessageConstant.BILL_STORAGE_DETAIL_MSG, 
					dispatchLineNo, headdistributionDetailMapList, datadistributionDetailList);
			if (null != pageInfo && pageInfo.getList().size() > 0) {
				dispatchLineNo += pageInfo.getList().size();
			}
		}
	}	
	
	@SuppressWarnings("deprecation")
	private String getPath(){
		SystemCodeEntity systemCodeEntity = systemCodeService.getSystemCode("GLOABL_PARAM", "EXPORT_PATH_STORAGE_FEES");
		if(systemCodeEntity == null){
			throw new BizException("请在系统参数中配置文件上传路径,参数GLOABL_PARAM,EXPORT_PATH_STORAGE_FEES");
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
	 * 仓储费用明细
	 */
	public List<Map<String, Object>> getStorageFeeHead(){
		List<Map<String, Object>> headMapList=new ArrayList<Map<String, Object>>();
		Map<String, Object> itemMap = new HashMap<String, Object>();
        
		itemMap = new HashMap<String, Object>();
        itemMap.put("title", "创建时间");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "createTime");
        headMapList.add(itemMap);
        
    	itemMap = new HashMap<String, Object>();
        itemMap.put("title", "费用编号");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "feesNo");
        headMapList.add(itemMap);
        
		
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "商家名称");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "customerName");
        headMapList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "仓库名称");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "warehouseName");
        headMapList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
    	itemMap.put("title", "单据类型");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "orderType");
        headMapList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
    	itemMap.put("title", "单据编号");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "orderNo");
        headMapList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
    	itemMap.put("title", "增值费");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "otherSubjectCode");
        headMapList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "费用科目");
        itemMap.put("columnWidth", 50);
        itemMap.put("dataKey", "costSubject");
        headMapList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "温度类型");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "tempretureType");
        headMapList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "商品类别");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "productType");
        headMapList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "商品编号");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "productNo");
        headMapList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "商品名称");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "productName");
        headMapList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "外部商品编号");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "externalProductNo");
        headMapList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "数量");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "quantity");
        headMapList.add(itemMap);
		
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "单位");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "unit");
        headMapList.add(itemMap);
		
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "重量");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "weight");
        headMapList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "体积");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "volume");
        headMapList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "品种数");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "varieties");
        headMapList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "单价");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "unitPrice");
        headMapList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "续件价格");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "continuedPrice");
        headMapList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
    	itemMap.put("title", "金额");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "cost");
        headMapList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
    	itemMap.put("title", "账单编号");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "billNo");
        headMapList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
    	itemMap.put("title", "状态");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "status");
        headMapList.add(itemMap);
        
        
        itemMap = new HashMap<String, Object>();
    	itemMap.put("title", "修改者");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "lastModifier");
        headMapList.add(itemMap);
        
        
        itemMap = new HashMap<String, Object>();
    	itemMap.put("title", "修改时间");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "lastModifyTime");
        headMapList.add(itemMap);
        
        
        
        return headMapList;
	}
	public List<Map<String, Object>> getStorageFeeHeadItem(List<FeesReceiveStorageEntity> list){
		List<Map<String, Object>> dataList = new ArrayList<Map<String,Object>>();
		 Map<String, Object> dataItem = null;
		 for(FeesReceiveStorageEntity entity:list){
			dataItem=new HashMap<String, Object>();
			dataItem.put("customerName", entity.getCustomerName());
			dataItem.put("warehouseName", mapWarehouse.get(entity.getWarehouseCode()));
			dataItem.put("orderType", entity.getOrderType());
			dataItem.put("costSubject", costSubjectMap.get(entity.getSubjectCode()));
			dataItem.put("tempretureType", tempretureMap.get(entity.getTempretureType()));
			dataItem.put("productType", entity.getProductType());
			dataItem.put("productNo", entity.getProductNo());
			dataItem.put("productName", entity.getProductName());
			dataItem.put("externalProductNo", entity.getExternalProductNo());
			dataItem.put("quantity", entity.getQuantity());
			dataItem.put("weight", entity.getWeight());
			dataItem.put("unitPrice", entity.getUnitPrice());
			dataItem.put("cost", entity.getCost());//金额
			dataItem.put("createTime", entity.getCreateTime());
			dataItem.put("lastModifier", entity.getLastModifier());
			dataItem.put("lastModifyTime", entity.getLastModifyTime());
			dataItem.put("feesNo", entity.getFeesNo());
			dataItem.put("orderNo", entity.getOrderNo());
			dataItem.put("unit", chargeTypeMap.get(entity.getUnit()));
			dataItem.put("varieties", entity.getVarieties());
			dataItem.put("continuedPrice", entity.getContinuedPrice());
			dataItem.put("volume", entity.getVolume());
			dataItem.put("billNo", entity.getBillNo());
			dataItem.put("status", mapStatus.get(entity.getStatus()));		
			dataItem.put("otherSubjectCode", extraSubjectMap.get(entity.getOtherSubjectCode()));
			dataList.add(dataItem);
		 }
		 return dataList;
	}
	
	
	/**
	 * 初始化需要使用的参数
	 */
	private void init(){
		mapStatus=getStatus();
		mapWarehouse=getwarehouse();
		costSubjectMap=getCostSubjectMap();
		tempretureMap=getTemperatureMap();
		extraSubjectMap=getExtraList();
		chargeTypeMap=getChargeTypeMap();
	}
	
	boolean containStr(String code){
		String str = "STORAGE_PRICE_SUBJECT";
		int index = str.indexOf(code);
		return index<0?false:true;
	} 
	
	/**
	 * 费用文件唯一值
	 */
	public String getName(){
		return sequenceService.getBillNoOne(FeesReceiveStorageExportController.class.getName(), "FY", "000000");
	}
}
