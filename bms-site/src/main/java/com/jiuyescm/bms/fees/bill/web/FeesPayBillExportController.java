package com.jiuyescm.bms.fees.bill.web;

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
import com.jiuyescm.bms.common.log.entity.BmsErrorLogInfoEntity;
import com.jiuyescm.bms.common.log.service.IBmsErrorLogInfoService;
import com.jiuyescm.bms.fees.abnormal.entity.FeesPayAbnormalEntity;
import com.jiuyescm.bms.fees.bill.out.entity.FeesPayBillEntity;
import com.jiuyescm.bms.fees.bill.out.service.IFeesPayBillService;
import com.jiuyescm.bms.fees.entity.FeesBillWareHouseEntity;
import com.jiuyescm.bms.fees.out.dispatch.entity.FeesPayDispatchEntity;
import com.jiuyescm.cfm.common.JAppContext;
import com.jiuyescm.common.utils.excel.POISXSSUtil;
import com.jiuyescm.exception.BizException;
import com.jiuyescm.mdm.warehouse.api.IWarehouseService;
import com.jiuyescm.mdm.warehouse.vo.WarehouseVo;

/**
 * 应付账单导出 宅配、异常，运输
 * @author yangss
 *
 */
@Controller("feesPayBillExportController")
public class FeesPayBillExportController {

	private static final Logger logger = Logger.getLogger(FeesPayBillExportController.class.getName());

	@Resource
	private IFeesPayBillService feesPayBillService;
	@Resource
	private ISystemCodeService systemCodeService;
	@Resource 
	private IWarehouseService warehouseService;
	@Resource
	private IFileExportTaskService fileExportTaskService;
	@Resource
	private IBmsErrorLogInfoService bmsErrorLogInfoService;
	@Resource
	private IBmsGroupSubjectService bmsGroupSubjectService;
	
	//费用类型 Map
	private Map<String,String> mapSystemcode;
	private Map<String,String> mapStatus;
	private Map<String,String> mapWarehouse;
	private Map<String,String> tempretureMap;
	private Map<String,String> mapSubjectMap;
	
	final int pageSize = 2000;
	
	/**
	 * 初始化需要使用的参数
	 */
	private void init(){
		mapSystemcode=getSystemcode();
		mapStatus=getStatus();
		mapWarehouse=getwarehouse();
		tempretureMap=getTemperatureMap();
		mapSubjectMap=getSubjectMap();
	}
	
	/**
	 * 宅配异步生成账单excel
	 * @param paramer
	 * @return
	 * @throws Exception
	 */
	@DataResolver
	public String asynExport(FeesPayBillEntity paramer)throws Exception{
		if (null == paramer) {
			return MessageConstant.BILL_INFO_ISNULL_MSG;
		}
		String billno=paramer.getBillNo();
		if (StringUtils.isBlank(billno)) {
			return MessageConstant.BILL_NO_ISNULL_MSG;
		}
		String deliverid=paramer.getDeliveryid();
		if(StringUtils.isBlank(deliverid)){
			return MessageConstant.BILL_DELIVER_ISNULL_MSG;
		}
		//校验该账单是否已生成账单文件
		Map<String, Object> queryEntity = new HashMap<String, Object>();
		queryEntity.put("billNo", billno);
		queryEntity.put("param2", deliverid);
		List<FileExportTaskEntity> taskList = fileExportTaskService.query(queryEntity);
		if (null != taskList && taskList.size() > 0) {
			return MessageConstant.BILL_FILE_ISEXIST_MSG;
		}
		
		String path = getPath();
		FileExportTaskEntity entity = new FileExportTaskEntity();
		entity.setTaskName(paramer.getBillName());
		entity.setBillNo(billno);
		entity.setStartTime(paramer.getStartTime());
		entity.setEndTime(paramer.getEndTime());
		entity.setTaskType(FileTaskTypeEnum.PAY_BILL.getCode());
		entity.setTaskState(FileTaskStateEnum.BEGIN.getCode());
		entity.setProgress(0d);
		entity.setFilePath(path+ FileConstant.SEPARATOR + 
				FileConstant.PAY_BILL_PREFIX + billno + FileConstant.SUFFIX_XLSX);
		entity.setCreator(JAppContext.currentUserName());
		entity.setCreateTime(JAppContext.currentTimestamp());
		entity.setDelFlag("0");
		entity.setParam2(deliverid);
		entity = fileExportTaskService.save(entity);
		
		//生成账单文件
		final FeesPayBillEntity condition = paramer;
		final String taskId = entity.getTaskId();
		new Thread(){
			public void run() {
				try {
					export(condition, taskId);
				} catch (Exception e) {
					updateExportTask(taskId, FileTaskStateEnum.FAIL.getCode(), 0);
					logger.error(ExceptionConstant.ASYN_PAY_DISPATCH_BILL_EXCEL_EX_MSG, e);
					//写入日志
					BmsErrorLogInfoEntity bmsErrorLogInfoEntity=new BmsErrorLogInfoEntity(this.getClass().getSimpleName(),Thread.currentThread().getStackTrace()[1].getMethodName(), "", e.toString());
					bmsErrorLogInfoService.log(bmsErrorLogInfoEntity);
				}
			};
		}.start();
		return MessageConstant.FILE_EXPORT_TASK_RECE_BILL_MSG;
	}
	
	/**
	 * 宅配导出
	 * @param paramer
	 * @param taskId
	 * @throws Exception
	 */
	private void export(FeesPayBillEntity parameter,String taskId)throws Exception{
		long beginTime = System.currentTimeMillis();
		updateExportTask(taskId, FileTaskStateEnum.INPROCESS.getCode(), 10);
		
		String billno=parameter.getBillNo();
		String billName=parameter.getBillName();
		String deliverid=parameter.getDeliveryid();
		logger.info("====应付配送账单导出："+billName + "["+billno+"]");
		String path = getPath();
		
		//初始化
		init();
		
    	File storeFolder=new File(path);
		//如果存放上传文件的目录不存在就新建
		if(!storeFolder.isDirectory()){
			storeFolder.mkdirs();
		}
		
		POISXSSUtil poiUtil = new POISXSSUtil();
		SXSSFWorkbook workbook = poiUtil.getXSSFWorkbook();
		String filePath = path+ FileConstant.SEPARATOR + 
    			FileConstant.PAY_BILL_PREFIX + billno + FileConstant.SUFFIX_XLSX;
		
		logger.info("====应付配送账单导出：" + "["+billno+"]" + "查询数据begin.");
		updateExportTask(taskId, null, 20);
		
		//账单信息
    	List<Map<String, Object>> headMapList=getDispatchBillFeeHead();
    	List<Map<String, Object>> dataMapList=getDispatchFeeHeadItem(parameter);
    	poiUtil.exportExcel2FilePath(poiUtil, workbook, MessageConstant.BILL_MSG, 
    			1, headMapList, dataMapList);
    	updateExportTask(taskId, null, 30);
    	
		//配送分仓费用
		List<FeesBillWareHouseEntity> dispatchWareHouseList=feesPayBillService.queryGroupDispatchAmountByDeliver(billno, deliverid);
    	List<Map<String, Object>> headDispatchMapList = getDispatchHead();
    	List<Map<String, Object>> dataDispatchList = getDispatchHeadItem(dispatchWareHouseList);
    	poiUtil.exportExcel2FilePath(poiUtil, workbook, MessageConstant.BILL_DISPATCH_GROUP_MSG, 
				1, headDispatchMapList, dataDispatchList);
    	updateExportTask(taskId, null, 40);
    	
		//异常分仓费用
		List<FeesBillWareHouseEntity> abnormalWareHouseList = feesPayBillService.queryGroupAbnormalAmountByDeliver(billno,deliverid);
    	List<Map<String, Object>> headAbnormalMapList = getAbnormalHead();
    	List<Map<String, Object>> dataAbnormalList = getAbnormalHeadItem(abnormalWareHouseList);
    	poiUtil.exportExcel2FilePath(poiUtil, workbook, MessageConstant.BILL_ABNORMAL_GROUP_MSG, 
				1, headAbnormalMapList, dataAbnormalList);
    	updateExportTask(taskId, null, 50);
    	
    	//配送明细
    	handDispatch(poiUtil, workbook, filePath, parameter);
    	updateExportTask(taskId, null, 70);
    	//异常明细
    	handAbnormal(poiUtil, workbook, filePath, parameter);
    	updateExportTask(taskId, null, 90);
    	
    	logger.info("====应付配送账单导出：" + "["+billno+"]" + "写入Excel begin.");
    	//最后写到文件
    	poiUtil.write2FilePath(workbook, filePath);
    	updateExportTask(taskId, FileTaskStateEnum.SUCCESS.getCode(), 100);
    	logger.info("====应付配送账单导出：" + "["+billno+"]" + "写入Excel end.==总耗时：" + (System.currentTimeMillis() - beginTime));
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
			String path,FeesPayBillEntity parameter)throws Exception{
		int pageNo = 1;
		int dispatchLineNo = 1;
		boolean doLoop = true;
		while (doLoop) {
			PageInfo<FeesPayDispatchEntity> distributionList = 
					feesPayBillService.queryDispatchDetailByBillNoAndDeliver(parameter, pageNo, pageSize);
			if (null != distributionList && distributionList.getList().size() > 0) {
				if (distributionList.getList().size() < pageSize) {
					doLoop = false;
				}else {
					pageNo += 1;
				}
				List<Map<String, Object>> headdistributionDetailMapList = getDispatchFeeHead();
				List<Map<String, Object>> datadistributionDetailList = getDispatchFeeHeadItem(distributionList.getList());
				
				poiUtil.exportExcel2FilePath(poiUtil, workbook, MessageConstant.BILL_DISPATCH_DETAIL_MSG, 
						dispatchLineNo, headdistributionDetailMapList, datadistributionDetailList);
				if (null != distributionList && distributionList.getList().size() > 0) {
					dispatchLineNo += distributionList.getList().size();
				}
			}else {
				doLoop = false;
			}
		}
	}
	private void handDispatchBatch(POISXSSUtil poiUtil, SXSSFWorkbook workbook, 
			String path,List<FeesPayBillEntity> list)throws Exception{
		int pageNo = 1;
		int dispatchLineNo = 1;
		boolean doLoop = true;
		while (doLoop) {
			PageInfo<FeesPayDispatchEntity> distributionList = 
					feesPayBillService.queryDispatchDetailBatch(list, pageNo, pageSize);
			if (null != distributionList && distributionList.getList().size() > 0) {
				if (distributionList.getList().size() < pageSize) {
					doLoop = false;
				}else {
					pageNo += 1;
				}
				List<Map<String, Object>> headdistributionDetailMapList = getDispatchFeeHead();
				List<Map<String, Object>> datadistributionDetailList = getDispatchFeeHeadItem(distributionList.getList());
				
				poiUtil.exportExcel2FilePath(poiUtil, workbook, MessageConstant.BILL_DISPATCH_DETAIL_MSG, 
						dispatchLineNo, headdistributionDetailMapList, datadistributionDetailList);
				if (null != distributionList && distributionList.getList().size() > 0) {
					dispatchLineNo += distributionList.getList().size();
				}
			}else {
				doLoop = false;
			}
			
			
		}
	}
	
	/**
	 * 异常
	 * @param poiUtil
	 * @param workbook
	 * @param path
	 * @param billno
	 * @throws Exception
	 */
	private void handAbnormal(POISXSSUtil poiUtil, SXSSFWorkbook workbook, 
			String path,FeesPayBillEntity parameter)throws Exception{
		int pageNo = 1;
		int abnormalLineNo = 1;
		boolean doLoop = true;
		while (doLoop) {
			PageInfo<FeesPayAbnormalEntity> abnormalList = 
					feesPayBillService.queryAbnormalDetailByBillNoAndDeliver(parameter, pageNo, pageSize);
			if (null != abnormalList && abnormalList.getList().size() > 0) {
				if (abnormalList.getList().size() < pageSize) {
					doLoop = false;
				}else {
					pageNo += 1;
				}
				List<Map<String, Object>> headabnormalDetailMapList = getAbnormalFeeHead();
		    	List<Map<String, Object>> dataabnormalDetailList = getAbnormalFeeHeadItem(abnormalList.getList());
		    	
		    	poiUtil.exportExcel2FilePath(poiUtil, workbook, MessageConstant.BILL_ABNORMAL_DETAIL_MSG, 
		    			abnormalLineNo, headabnormalDetailMapList, dataabnormalDetailList);
				if (null != abnormalList && abnormalList.getList().size() > 0) {
					abnormalLineNo += abnormalList.getList().size();
				}
			}else {
				doLoop = false;
			}
			
			
		}
	}
	/**
	 * 异常
	 * @param poiUtil
	 * @param workbook
	 * @param path
	 * @param billno
	 * @throws Exception
	 */
	private void handAbnormalBatch(POISXSSUtil poiUtil, SXSSFWorkbook workbook, 
			String path,List<FeesPayBillEntity> list)throws Exception{
		int pageNo = 1;
		int abnormalLineNo = 1;
		boolean doLoop = true;
		while (doLoop) {
			PageInfo<FeesPayAbnormalEntity> abnormalList = 
					feesPayBillService.queryAbnormalDetailBatch(list, pageNo, pageSize);
			if (null != abnormalList && abnormalList.getList().size() > 0) {
				if (abnormalList.getList().size() < pageSize) {
					doLoop = false;
				}else {
					pageNo += 1;
				}
				List<Map<String, Object>> headabnormalDetailMapList = getAbnormalFeeHead();
		    	List<Map<String, Object>> dataabnormalDetailList = getAbnormalFeeHeadItem(abnormalList.getList());
		    	
		    	poiUtil.exportExcel2FilePath(poiUtil, workbook, MessageConstant.BILL_ABNORMAL_DETAIL_MSG, 
		    			abnormalLineNo, headabnormalDetailMapList, dataabnormalDetailList);
				if (null != abnormalList && abnormalList.getList().size() > 0) {
					abnormalLineNo += abnormalList.getList().size();
				}
			}else {
				doLoop = false;
			}
			
			
		}
	}
	
	private Map<String,String> getwarehouse(){
		List<WarehouseVo> warehouseVos = warehouseService.queryAllWarehouse();
		Map<String, String> map = new LinkedHashMap<String,String>();
		for (WarehouseVo warehouseVo : warehouseVos) {
			map.put(warehouseVo.getWarehouseid(), warehouseVo.getWarehousename());
		}
		return map;
	}
	
	private Map<String,String> getStatus(){
		Map<String,String> map=new HashMap<String,String>();
		map.put("0", "未过账");
		map.put("1", "已过账");
		return map;
	}
	
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
	
	private Map<String,String> getSystemcode(){
		List<SystemCodeEntity> codeList=systemCodeService.queryCodeList(new HashMap<String,Object>());
		Map<String, String> map =new LinkedHashMap<String,String>();
		for (SystemCodeEntity systemCodeEntity : codeList) {
			map.put(systemCodeEntity.getCode(),systemCodeEntity.getCodeName());
		}
		return map;
	}
	
	
	@DataProvider
	public Map<String,String> getSubjectMap(){
		// List<SystemCodeEntity> list=systemCodeService.findEnumList("TRANSPORT_TEMPLATE_PAY_TYPE");
		 Map<String, String> map =bmsGroupSubjectService.getSubject("pay_ts_contract_subject");

		 map.put("ts_abnormal_pay", "运输理赔");
		 return map;
	}
	
	@DataProvider
	public Map<String,String> getOtherSubjectMap(){
		 //List<SystemCodeEntity> baselist=systemCodeService.findEnumList("ts_base_subject");
		 //List<SystemCodeEntity> addlist=systemCodeService.findEnumList("ts_value_add_subject");
		 Map<String, String> map =bmsGroupSubjectService.getSubject("pay_transport_bill_subject");
		 return map;
	}
	
	/*
	 * 账单  头 
	 */
	public List<Map<String,Object>> getDispatchBillFeeHead(){
		List<Map<String, Object>> headMapList=new ArrayList<Map<String, Object>>();
		Map<String, Object> itemMap = new HashMap<String, Object>();
		itemMap.put("title", "账单编号");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "billNo");
        headMapList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
    	itemMap.put("title", "账单名称");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "billName");
        headMapList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
    	itemMap.put("title", "宅配商");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "deliverName");
        headMapList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
    	itemMap.put("title", "总金额");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "totleprice");
        headMapList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
    	itemMap.put("title", "实收金额");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "receiptAmount");
        headMapList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
    	itemMap.put("title", "结算状态");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "status");
        headMapList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
    	itemMap.put("title", "开始时间");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "startTime");
        headMapList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
    	itemMap.put("title", "结束时间");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "endTime");
        headMapList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "备注");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "remark");
        headMapList.add(itemMap);
        
        return headMapList;
	}
	public List<Map<String,Object>> getDispatchFeeHeadItem(FeesPayBillEntity paramer){
		 List<Map<String, Object>> dataList = new ArrayList<Map<String,Object>>();
		 Map<String,String> mapstatus=new HashMap<String,String>();
		 mapstatus.put("0", "未核销");
		 mapstatus.put("1", "已核销");
		 Map<String, Object> dataItem=new HashMap<String,Object>();
		 dataItem.put("billNo", paramer.getBillNo());
		 dataItem.put("billName", paramer.getBillName());
		 dataItem.put("deliverName", paramer.getDeliverName());
		 dataItem.put("totleprice", paramer.getTotleprice());
		 dataItem.put("receiptAmount", paramer.getReceiptAmount());
		 dataItem.put("status", mapstatus.get(paramer.getStatus()));
		 dataItem.put("startTime", paramer.getStartTime());
		 dataItem.put("endTime", paramer.getEndTime());
		 dataItem.put("remark", paramer.getRemark());
		 dataList.add(dataItem);
		 return dataList;
	}
	
	public List<Map<String,Object>> getDispatchFeeHeadItemList(List<FeesPayBillEntity> list){
		 List<Map<String, Object>> dataList = new ArrayList<Map<String,Object>>();
		 for(FeesPayBillEntity entity:list){
			 Map<String,String> mapstatus=new HashMap<String,String>();
			 mapstatus.put("0", "未核销");
			 mapstatus.put("1", "已核销");
			 Map<String, Object> dataItem=new HashMap<String,Object>();
			 dataItem.put("billNo", entity.getBillNo());
			 dataItem.put("billName", entity.getBillName());
			 dataItem.put("deliverName", entity.getDeliverName());
			 dataItem.put("totleprice", entity.getTotleprice());
			 dataItem.put("receiptAmount", entity.getReceiptAmount());
			 dataItem.put("status", mapstatus.get(entity.getStatus()));
			 dataItem.put("startTime", entity.getStartTime());
			 dataItem.put("endTime", entity.getEndTime());
			 dataItem.put("remark", entity.getRemark());
			 dataList.add(dataItem);
		 }
		 return dataList;
	}
	
	/**
	 * 配送费用 物流商分组
	 */
	private List<Map<String, Object>> getDispatchHead(){
		List<Map<String, Object>> headMapList=new ArrayList<Map<String, Object>>();
		Map<String, Object> itemMap = new HashMap<String, Object>();
		itemMap.put("title", "宅配商");
		itemMap.put("columnWidth", 25);
		itemMap.put("dataKey", "deliverName");
        headMapList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "金额");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "amount");
        headMapList.add(itemMap);
        
        return headMapList;
	}
	private List<Map<String, Object>> getDispatchHeadItem(List<FeesBillWareHouseEntity> list){
		List<Map<String, Object>> dataList = new ArrayList<Map<String,Object>>();
		 Map<String, Object> dataItem = null;
		 for(FeesBillWareHouseEntity entity:list){
			 dataItem=new HashMap<String, Object>();
			 dataItem.put("deliverName", entity.getDeliveryName());
			 dataItem.put("amount", entity.getAmount());
			 dataList.add(dataItem);
		 }
		 return dataList;
	}
	
	/**
	 * 异常费用 客诉原因分组
	 */
	private List<Map<String, Object>> getAbnormalHead(){
		List<Map<String, Object>> headMapList=new ArrayList<Map<String, Object>>();
		Map<String, Object> itemMap = new HashMap<String, Object>();
		itemMap.put("title", "宅配商");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "deliverName");
        headMapList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
		itemMap.put("title", "客诉原因");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "reason");
        headMapList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
    	itemMap.put("title", "金额");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "amount");
        headMapList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "账单编号");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "billNo");
        headMapList.add(itemMap);
        
        return headMapList;
	}
	private List<Map<String, Object>> getAbnormalHeadItem(List<FeesBillWareHouseEntity> list){
		List<Map<String, Object>> dataList = new ArrayList<Map<String,Object>>();
		 Map<String, Object> dataItem = null;
		 for(FeesBillWareHouseEntity entity:list){
			 dataItem=new HashMap<String, Object>();
			 dataItem.put("deliverName", entity.getDeliveryName());
			 dataItem.put("reason", entity.getReason());
			 dataItem.put("amount", entity.getAmount());
			 dataItem.put("billNo",entity.getBillNo());
			 dataList.add(dataItem);
		 }
		 return dataList;
	}
	
	
	/**
	 * 配送费用明细
	 */
	public List<Map<String, Object>> getDispatchFeeHead(){
		List<Map<String, Object>> headMapList=new ArrayList<Map<String, Object>>();
		Map<String, Object> itemMap = new HashMap<String, Object>();
		
		itemMap = new HashMap<String, Object>();
		itemMap.put("title", "商家");
		itemMap.put("columnWidth", 25);
		itemMap.put("dataKey", "customerName");
		headMapList.add(itemMap);
        
		itemMap = new HashMap<String, Object>();
		itemMap.put("title", "仓库");
		itemMap.put("columnWidth", 25);
		itemMap.put("dataKey", "warehouseName");
		headMapList.add(itemMap);
		
		itemMap = new HashMap<String, Object>();
        itemMap.put("title", "物流商");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "carrierName");
        headMapList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "宅配商");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "deliverName");
        headMapList.add(itemMap);
	        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "出库单号");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "outstockNo");
        headMapList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "外部单号");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "externalNo");
        headMapList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "运单号");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "waybillNo");
        headMapList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "目的省");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "toProvinceName");
        headMapList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "目的市");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "toCityName");
        headMapList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "目的区县");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "toDistrictName");
        headMapList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "总重量");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "totalWeight");
        headMapList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "计费重量");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "chargedWeight");
        headMapList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "首重重量");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "headWeight");
        headMapList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "续重重量");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "continuedWeight");
        headMapList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "首重价格");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "headPrice");
        headMapList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "续重价格");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "continuedPrice");
        headMapList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
    	itemMap.put("title", "金额");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "amount");
        headMapList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "温度类型");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "temperatureType");
        headMapList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
    	itemMap.put("title", "操作时间");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "createTime");
        headMapList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "揽收时间");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "lianshouTime");
        headMapList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "签收时间");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "signTime");
        headMapList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
    	itemMap.put("title", "单据类型");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "billType");
        headMapList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
    	itemMap.put("title", "状态");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "status");
        headMapList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "大状态");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "bigstatus");
        headMapList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "小状态");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "smallstatus");
        headMapList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
    	itemMap.put("title", "费用类型");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "subjectType");
        headMapList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
    	itemMap.put("title", "费用科目");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "subjectCode");
        headMapList.add(itemMap);
        
        
        return headMapList;
	}
	private List<Map<String, Object>> getDispatchFeeHeadItem(List<FeesPayDispatchEntity> list){
		 List<Map<String, Object>> dataList = new ArrayList<Map<String,Object>>();
		 Map<String, Object> dataItem = null;
		 for(FeesPayDispatchEntity entity : list){
			dataItem=new HashMap<String, Object>();
			dataItem.put("customerName", entity.getCustomerName());
			dataItem.put("warehouseName", mapWarehouse.get(entity.getWarehouseCode()));
			dataItem.put("carrierName", entity.getCarrierName());
			dataItem.put("deliverName", entity.getDeliverName());
			dataItem.put("outstockNo", entity.getOutstockNo());
			dataItem.put("externalNo", entity.getExternalNo());
			dataItem.put("waybillNo", entity.getWaybillNo());
			dataItem.put("toProvinceName", entity.getToProvinceName());
			dataItem.put("toCityName", entity.getToCityName());
			dataItem.put("toDistrictName", entity.getToDistrictName());
			dataItem.put("totalWeight", entity.getTotalWeight());
			dataItem.put("chargedWeight", entity.getChargedWeight());
			dataItem.put("headWeight", entity.getHeadWeight());
			dataItem.put("continuedWeight", entity.getContinuedWeight());
			dataItem.put("headPrice", entity.getHeadPrice());
			dataItem.put("continuedPrice", entity.getContinuedPrice());
			dataItem.put("amount", entity.getAmount());
			dataItem.put("temperatureType", tempretureMap.get(entity.getTemperatureType()));
			dataItem.put("createTime", entity.getCreateTime());
			dataItem.put("lianshouTime", entity.getAcceptTime());
        	dataItem.put("signTime", entity.getSignTime());
			dataItem.put("billType", entity.getBillType());
			dataItem.put("status", mapStatus.get(entity.getStatus()));
			dataItem.put("bigstatus", entity.getBigstatus());
        	dataItem.put("smallstatus", entity.getSmallstatus());
			dataItem.put("subjectCode", mapSubjectMap.get(entity.getSubjectCode()));
			dataItem.put("subjectType", mapSystemcode.get(entity.getSubjectType()));
			dataList.add(dataItem);
		 }
		 return dataList;
	}
	
	/**
	 * 异常费用明细
	 */
	public List<Map<String, Object>> getAbnormalFeeHead(){
		List<Map<String, Object>> headMapList=new ArrayList<Map<String, Object>>();
		Map<String, Object> itemMap = new HashMap<String, Object>();
		
        itemMap = new HashMap<String, Object>();
    	itemMap.put("title", "账单编号");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "billNo");
        headMapList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
    	itemMap.put("title", "操作时间");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "operateTime");
        headMapList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "商家ID");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "customerId");
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
		itemMap.put("title", "费用编号");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "feeNo");
        headMapList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "外部订单号");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "reference");
        headMapList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "运单号");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "expressnum");
        headMapList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "出库单号");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "orderNo");
        headMapList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "原因");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "reason");
        headMapList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "原因详情");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "reasonDetail");
        headMapList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "备注");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "remark");
        headMapList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "承运商名称");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "carrierName");
        headMapList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "宅配商名称");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "deliverName");
        headMapList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
    	itemMap.put("title", "是否赔付");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "ispay");
        headMapList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
    	itemMap.put("title", "金额");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "payMoney");
        headMapList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "是否争议");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "isConflictStr");
        headMapList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
    	itemMap.put("title", "状态");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "status");
        headMapList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
    	itemMap.put("title", "是否免运费");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "isDeliveryFree");
        headMapList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
    	itemMap.put("title", "运费");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "deliveryCost");
        headMapList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "罚款金额");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "amerceAmount");
        headMapList.add(itemMap);
        
        return headMapList;
	}
	private List<Map<String, Object>> getAbnormalFeeHeadItem(List<FeesPayAbnormalEntity> list){
		 List<Map<String, Object>> dataList = new ArrayList<Map<String,Object>>();
		 Map<String, Object> dataItem = null;
		 for(FeesPayAbnormalEntity entity:list){
			 dataItem=new HashMap<String, Object>();
			 dataItem.put("billNo", entity.getBillNo());
			 dataItem.put("operateTime", entity.getCreateTime());
			 dataItem.put("customerId", entity.getCustomerId());
			 dataItem.put("customerName", entity.getCustomerName());
			 dataItem.put("warehouseName", mapWarehouse.get(entity.getWarehouseId()));
			 dataItem.put("feeNo", entity.getFeeNo());
			 dataItem.put("reference", entity.getReference());
			 dataItem.put("expressnum", entity.getExpressnum());
			 dataItem.put("orderNo", entity.getOrderNo());
			 dataItem.put("reason", entity.getReason());
			 dataItem.put("reasonDetail", entity.getReasonDetail());
			 dataItem.put("remark", entity.getRemark());
			 dataItem.put("carrierName", entity.getCarrierName());
			 dataItem.put("deliverName", entity.getDeliverName());
			 dataItem.put("ispay", entity.getIsPayStr());//是否赔付
			 dataItem.put("payMoney", entity.getPayMoney());
			 dataItem.put("isConflictStr", entity.getIsConflictStr());//是否争议
			 dataItem.put("status", entity.getIsCalculated());
			 dataItem.put("isDeliveryFree", "1".equals(entity.getIsDeliveryFree())?"否":"是");
			 if(entity.getDeliveryCost()==null){
				 dataItem.put("deliveryCost", "0");
			 }else{
				 dataItem.put("deliveryCost", entity.getDeliveryCost());
			 }
			 dataItem.put("amerceAmount", entity.getAmerceAmount());
			 
			 dataList.add(dataItem);
		 }
		 return dataList;
	}
	
	@SuppressWarnings("deprecation")
	private String getPath(){
		SystemCodeEntity systemCodeEntity = systemCodeService.getSystemCode("GLOABL_PARAM", "EXPORT_PAY_BILL");
		if(systemCodeEntity == null){
			throw new BizException("请在系统参数中配置文件上传路径,参数GLOABL_PARAM,EXPORT_PAY_BILL");
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
	 * 宅配异步生成账单excel
	 * @param paramer
	 * @return
	 * @throws Exception
	 */
	@DataResolver
	public String asynExportAll(List<FeesPayBillEntity> paramer)throws Exception
	{
		if (null == paramer) {
			return MessageConstant.BILL_INFO_ISNULL_MSG;
		}
		String deliverid="";
		for(FeesPayBillEntity feesPayEntity:paramer){
			deliverid+=feesPayEntity.getDeliveryid()+",";
		}
		//校验该账单是否已生成账单文件
		Map<String, Object> queryEntity = new HashMap<String, Object>();
		queryEntity.put("billNo", paramer.get(0).getBillNo());
		queryEntity.put("param2", deliverid);
		List<FileExportTaskEntity> taskList = fileExportTaskService.query(queryEntity);
		if (null != taskList && taskList.size() > 0) {
			return MessageConstant.BILL_FILE_ISEXIST_MSG;
		}
		
		String path = getPath();
		FileExportTaskEntity entity = new FileExportTaskEntity();
		entity.setTaskName(paramer.get(0).getBillName());
		entity.setBillNo(paramer.get(0).getBillNo());
		entity.setStartTime(paramer.get(0).getStartTime());
		entity.setEndTime(paramer.get(0).getEndTime());
		entity.setTaskType(FileTaskTypeEnum.PAY_BILL.getCode());
		entity.setTaskState(FileTaskStateEnum.BEGIN.getCode());
		entity.setProgress(0d);
		entity.setFilePath(path+ FileConstant.SEPARATOR + 
				FileConstant.PAY_BILL_PREFIX + paramer.get(0).getBillNo() + FileConstant.SUFFIX_XLSX);
		entity.setCreator(JAppContext.currentUserName());
		entity.setCreateTime(JAppContext.currentTimestamp());
		entity.setDelFlag("0");
		entity.setParam2(deliverid);
		entity = fileExportTaskService.save(entity);
		
		//生成账单文件
		final List<FeesPayBillEntity> condition = paramer;
		final String taskId = entity.getTaskId();
		new Thread(){
			public void run() {
				try {
					exportAll(condition, taskId);
				} catch (Exception e) {
					updateExportTask(taskId, FileTaskStateEnum.FAIL.getCode(), 0);
					logger.error(ExceptionConstant.ASYN_PAY_DISPATCH_BILL_EXCEL_EX_MSG, e);
					//写入日志
					BmsErrorLogInfoEntity bmsErrorLogInfoEntity=new BmsErrorLogInfoEntity(this.getClass().getSimpleName(),Thread.currentThread().getStackTrace()[1].getMethodName(), "", e.toString());
					bmsErrorLogInfoService.log(bmsErrorLogInfoEntity);
				}
			};
		}.start();
		return MessageConstant.FILE_EXPORT_TASK_RECE_BILL_MSG;
	}
	
	/**
	 * 宅配导出
	 * @param paramer
	 * @param taskId
	 * @throws Exception
	 */
	private void exportAll(List<FeesPayBillEntity> parameter,String taskId)throws Exception{
		long beginTime = System.currentTimeMillis();
		updateExportTask(taskId, FileTaskStateEnum.INPROCESS.getCode(), 10);
		
		String billno=parameter.get(0).getBillNo();
		String billName=parameter.get(0).getBillName();
		logger.info("====应付配送账单导出："+billName + "["+billno+"]");
		String path = getPath();
		
		//初始化
		init();
		List<String> deliverIdList=new ArrayList<String>();
		for(FeesPayBillEntity entity:parameter){
			deliverIdList.add(entity.getDeliveryid());
		}
    	File storeFolder=new File(path);
		//如果存放上传文件的目录不存在就新建
		if(!storeFolder.isDirectory()){
			storeFolder.mkdirs();
		}
		
		POISXSSUtil poiUtil = new POISXSSUtil();
		SXSSFWorkbook workbook = poiUtil.getXSSFWorkbook();
		String filePath = path+ FileConstant.SEPARATOR + 
    			FileConstant.PAY_BILL_PREFIX + billno + FileConstant.SUFFIX_XLSX;
		
		logger.info("====应付配送账单导出：" + "["+billno+"]" + "查询数据begin.");
		updateExportTask(taskId, null, 20);
		
		//账单信息
    	List<Map<String, Object>> headMapList=getDispatchBillFeeHead();
    	List<Map<String, Object>> dataMapList=getDispatchFeeHeadItemList(parameter);
    	poiUtil.exportExcel2FilePath(poiUtil, workbook, MessageConstant.BILL_MSG, 
    			1, headMapList, dataMapList);
    	updateExportTask(taskId, null, 30);
    	
		//配送分仓费用
		List<FeesBillWareHouseEntity> dispatchWareHouseList=feesPayBillService.queryGroupDispatchAmountSelect(billno, deliverIdList);
    	List<Map<String, Object>> headDispatchMapList = getDispatchHead();
    	List<Map<String, Object>> dataDispatchList = getDispatchHeadItem(dispatchWareHouseList);
    	poiUtil.exportExcel2FilePath(poiUtil, workbook, MessageConstant.BILL_DISPATCH_GROUP_MSG, 
				1, headDispatchMapList, dataDispatchList);
    	updateExportTask(taskId, null, 40);
    	
		//异常分仓费用
		List<FeesBillWareHouseEntity> abnormalWareHouseList = feesPayBillService.queryGroupAbnormalAmountSelect(billno,deliverIdList);
    	List<Map<String, Object>> headAbnormalMapList = getAbnormalHead();
    	List<Map<String, Object>> dataAbnormalList = getAbnormalHeadItem(abnormalWareHouseList);
    	poiUtil.exportExcel2FilePath(poiUtil, workbook, MessageConstant.BILL_ABNORMAL_GROUP_MSG, 
				1, headAbnormalMapList, dataAbnormalList);
    	updateExportTask(taskId, null, 50);
    	
    	//配送明细
    	handDispatchBatch(poiUtil, workbook, filePath, parameter);
    	updateExportTask(taskId, null, 70);
    	//异常明细
    	handAbnormalBatch(poiUtil, workbook, filePath, parameter);
    	updateExportTask(taskId, null, 90);
    	
    	logger.info("====应付配送账单导出：" + "["+billno+"]" + "写入Excel begin.");
    	//最后写到文件
    	poiUtil.write2FilePath(workbook, filePath);
    	updateExportTask(taskId, FileTaskStateEnum.SUCCESS.getCode(), 100);
    	logger.info("====应付配送账单导出：" + "["+billno+"]" + "写入Excel end.==总耗时：" + (System.currentTimeMillis() - beginTime));
	}
	
}
