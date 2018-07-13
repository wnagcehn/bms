package com.jiuyescm.bms.fees.bill.web;

import java.io.File;
import java.io.IOException;
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
import com.jiuyescm.bms.fees.bill.out.entity.FeesPayBillEntity;
import com.jiuyescm.bms.fees.bill.out.service.IFeesPayBillService;
import com.jiuyescm.bms.fees.entity.FeesBillWareHouseEntity;
import com.jiuyescm.bms.fees.out.transport.entity.FeesPayTransportEntity;
import com.jiuyescm.cfm.common.JAppContext;
import com.jiuyescm.common.utils.excel.POISXSSUtil;
import com.jiuyescm.exception.BizException;
import com.jiuyescm.mdm.warehouse.api.IWarehouseService;

/**
 * 应付账单导出 宅配、异常，运输
 * @author yangss
 *
 */
@Controller("feesPayTransportBillExportController")
public class FeesPayTransportBillExportController {

	private static final Logger logger = Logger.getLogger(FeesPayTransportBillExportController.class.getName());

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
	private Map<String,String> transportProductTypeMap;
	private Map<String,String> mapSubjectMap;
	
	final int pageSize = 2000;
	
	/**
	 * 初始化需要使用的参数
	 */
	private void init(){
		mapSystemcode=getSystemcode();
		mapStatus=getStatus();
		transportProductTypeMap=getTransportProductTypeList();
		mapSubjectMap=getStatus();
	}
	
	/**
	 * 异步生成应付运输账单
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
		
		//校验该账单是否已生成账单文件
		Map<String, Object> queryEntity = new HashMap<String, Object>();
		queryEntity.put("billNo", billno);
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
		entity.setTaskType(FileTaskTypeEnum.PAY_TRANS_BILL.getCode());
		entity.setTaskState(FileTaskStateEnum.BEGIN.getCode());
		entity.setProgress(0d);
		entity.setFilePath(path+ FileConstant.SEPARATOR + 
				FileConstant.PAY_TRANS_BILL_PREFIX + billno + FileConstant.SUFFIX_XLSX);
		entity.setCreator(JAppContext.currentUserName());
		entity.setCreateTime(JAppContext.currentTimestamp());
		entity.setDelFlag("0");
		entity = fileExportTaskService.save(entity);
		
		//生成账单文件
		final FeesPayBillEntity condition = paramer;
		final String taskId = entity.getTaskId();
		new Thread(){
			public void run() {
				try {
					exportTransport(condition, taskId);
				} catch (Exception e) {
					updateExportTask(taskId, FileTaskStateEnum.FAIL.getCode(), 0);
					logger.error(ExceptionConstant.ASYN_PAY_TRANSPORT_BILL_EXCEL_EX_MSG, e);
					//写入日志
					BmsErrorLogInfoEntity bmsErrorLogInfoEntity=new BmsErrorLogInfoEntity(this.getClass().getSimpleName(),Thread.currentThread().getStackTrace()[1].getMethodName(), "", e.toString());
					bmsErrorLogInfoService.log(bmsErrorLogInfoEntity);
				}
			};
		}.start();
		return MessageConstant.FILE_EXPORT_TASK_RECE_BILL_MSG;
	}
	
	/**
	 * 运输导出
	 * @param paramer
	 * @param taskId
	 * @throws Exception
	 */
	private void exportTransport(FeesPayBillEntity parameter,String taskId)throws Exception{
		long beginTime = System.currentTimeMillis();
		updateExportTask(taskId, FileTaskStateEnum.INPROCESS.getCode(), 10);
		
		String billno=parameter.getBillNo();
		String billName=parameter.getBillName();
		logger.info("====应付运输账单导出："+billName + "["+billno+"]");
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
    			FileConstant.PAY_TRANS_BILL_PREFIX + billno + FileConstant.SUFFIX_XLSX;
		
		logger.info("====应付运输账单导出：" + "["+billno+"]" + "查询数据begin.");
		updateExportTask(taskId, null, 20);
		
		//账单信息
    	List<Map<String, Object>> headMapList=getTransportBillFeeHead();
    	List<Map<String, Object>> dataMapList=getTransportFeeHeadItem(parameter);
    	poiUtil.exportExcel2FilePath(poiUtil, workbook, MessageConstant.BILL_MSG, 
    			1, headMapList, dataMapList);
    	updateExportTask(taskId, null, 30);
    	
    	//运输分仓 费用
		List<FeesBillWareHouseEntity> transportWareHouseList=feesPayBillService.queryGroupTransportAmount(billno);
    	List<Map<String, Object>> headTransportMapList=getTransportHead();
    	List<Map<String, Object>> dataTransportList=getTransportHeadItem(transportWareHouseList);
    	poiUtil.exportExcel2FilePath(poiUtil, workbook, MessageConstant.BILL_TRANSPORT_GROUP_MSG, 
				1, headTransportMapList, dataTransportList);
    	updateExportTask(taskId, null, 50);
    	
    	//运输明细
    	handTransport(poiUtil, workbook, filePath, billno);
    	logger.info("====应付运输账单导出：" + "["+billno+"]" + "写入Excel begin.");
    	updateExportTask(taskId, null, 70);
    	//理赔明细
    	handAbnormal(poiUtil,workbook,filePath,billno);
    	logger.info("====应付理赔账单导出：" + "["+billno+"]" + "写入Excel begin.");
    	updateExportTask(taskId, null, 90);
    	//最后写到文件
    	poiUtil.write2FilePath(workbook, filePath);
    	updateExportTask(taskId, FileTaskStateEnum.SUCCESS.getCode(), 100);
    	logger.info("====应付运输账单导出：" + "["+billno+"]" + "写入Excel end.==总耗时：" + (System.currentTimeMillis() - beginTime));
	}
	
	private void handAbnormal(POISXSSUtil poiUtil, SXSSFWorkbook workbook,
			String filePath, String billno) throws IOException {
		int pageNo = 1;
		int transportLineNo = 1;
		boolean doLoop = true;
		while (doLoop) {
			PageInfo<FeesPayTransportEntity> transportList = 
					feesPayBillService.queryAbnormalByBillNo(billno, pageNo, pageSize);
			if (null != transportList && transportList.getList().size() > 0) {
				if (transportList.getList().size() < pageSize) {
					doLoop = false;
				}else {
					pageNo += 1;
				}
				List<Map<String, Object>> headabnormalDetailMapList = getTransportFeeHead();
				List<Map<String, Object>> dataabnormalDetailList = getTransportFeeHeadItem(transportList.getList());
				
				poiUtil.exportExcel2FilePath(poiUtil, workbook, "运输理赔明细", 
						transportLineNo, headabnormalDetailMapList, dataabnormalDetailList);
				if (null != transportList && transportList.getList().size() > 0) {
					transportLineNo += transportList.getList().size();
				}
			}else {
				doLoop = false;
			}
		}
		
	}

	/**
	 * 运输明细费用
	 * @param poiUtil
	 * @param workbook
	 * @param path
	 * @param billno
	 * @throws Exception
	 */
	private void handTransport(POISXSSUtil poiUtil, SXSSFWorkbook workbook, 
			String path, String billno)throws Exception{
		int pageNo = 1;
		int transportLineNo = 1;
		boolean doLoop = true;
		while (doLoop) {
			PageInfo<FeesPayTransportEntity> transportList = 
					feesPayBillService.queryTransportDetailByBillNo(billno, pageNo, pageSize);
			if (null != transportList && transportList.getList().size() > 0) {
				if (transportList.getList().size() < pageSize) {
					doLoop = false;
				}else {
					pageNo += 1;
				}
				List<Map<String, Object>> headdeliverDetailMapList = getTransportFeeHead();
				List<Map<String, Object>> datadeliverDetailList = getTransportFeeHeadItem(transportList.getList());
				
				poiUtil.exportExcel2FilePath(poiUtil, workbook, MessageConstant.BILL_TRANSPORT_DETAIL_MSG, 
						transportLineNo, headdeliverDetailMapList, datadeliverDetailList);
				if (null != transportList && transportList.getList().size() > 0) {
					transportLineNo += transportList.getList().size();
				}
			}else {
				doLoop = false;
			}
		}
	}
	
	private Map<String,String> getStatus(){
		Map<String,String> map=new HashMap<String,String>();
		map.put("0", "未过账");
		map.put("1", "已过账");
		return map;
	}
	
	private Map<String,String> getSystemcode(){
		List<SystemCodeEntity> codeList=systemCodeService.queryCodeList(new HashMap<String,Object>());
		Map<String, String> map =new LinkedHashMap<String,String>();
		for (SystemCodeEntity systemCodeEntity : codeList) {
			map.put(systemCodeEntity.getCode(),systemCodeEntity.getCodeName());
		}
		return map;
	}
	
	/**
	 * 运输产品类型TRANSPORT_PRODUCT_TYPE： 城际专列、同城专列、电商专列、航线达 ； 
	 */
	private Map<String, String> getTransportProductTypeList(){
		List<SystemCodeEntity> systemCodeList = systemCodeService.findEnumList("TRANSPORT_PRODUCT_TYPE");
		Map<String, String> map =new LinkedHashMap<String,String>();
		if(systemCodeList!=null && systemCodeList.size()>0){
			for(int i=0;i<systemCodeList.size();i++){
				map.put(systemCodeList.get(i).getCode(), systemCodeList.get(i).getCodeName());
			}
		}
		return map;
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
	
	/*
	 * 账单  头 
	 */
	public List<Map<String,Object>> getTransportBillFeeHead(){
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
    	itemMap.put("title", "承运商");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "forwarderName");
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
	public List<Map<String,Object>> getTransportFeeHeadItem(FeesPayBillEntity paramer){
		 List<Map<String, Object>> dataList = new ArrayList<Map<String,Object>>();
		 Map<String,String> mapstatus=new HashMap<String,String>();
		 mapstatus.put("0", "未核销");
		 mapstatus.put("1", "已核销");
		 Map<String, Object> dataItem=new HashMap<String,Object>();
		 dataItem.put("billNo", paramer.getBillNo());
		 dataItem.put("billName", paramer.getBillName());
		 dataItem.put("deliverName", paramer.getDeliverName());
		 dataItem.put("forwarderName", paramer.getForwarderName());
		 dataItem.put("totleprice", paramer.getTotleprice());
		 dataItem.put("receiptAmount", paramer.getReceiptAmount());
		 dataItem.put("status", mapstatus.get(paramer.getStatus()));
		 dataItem.put("startTime", paramer.getStartTime());
		 dataItem.put("endTime", paramer.getEndTime());
		 dataItem.put("remark", paramer.getRemark());
		 dataList.add(dataItem);
		 return dataList;
	}
	
	/*
	 * 账单  头 运输
	 */
	public List<Map<String,Object>> getTransFeeHead(){
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
		itemMap.put("title", "承运商");
		itemMap.put("columnWidth", 25);
		itemMap.put("dataKey", "forwarderName");
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
	public List<Map<String,Object>> getTransFeeHeadItem(Map<String,Object> map){
		List<Map<String, Object>> dataList = new ArrayList<Map<String,Object>>();
		Map<String,String> mapstatus=new HashMap<String,String>();
		mapstatus.put("0", "未核销");
		mapstatus.put("1", "已核销");
		Map<String, Object> dataItem=new HashMap<String,Object>();
		dataItem.put("billNo", map.get("billNo"));
		dataItem.put("billName", map.get("billName"));
		dataItem.put("forwarderName", map.get("forwarderName"));
		dataItem.put("totleprice", map.get("totleprice"));
		dataItem.put("receiptAmount", map.get("receiptAmount"));
		dataItem.put("status", mapstatus.get(String.valueOf(map.get("status"))));
		dataItem.put("startTime", map.get("startTime"));
		dataItem.put("endTime", map.get("endTime"));
		dataItem.put("remark", map.get("remark"));
		dataList.add(dataItem);
		return dataList;
	}
	
	/**
	 * 运输费用 费用科目分组
	 */
	private List<Map<String, Object>> getTransportHead(){
		List<Map<String, Object>> headMapList=new ArrayList<Map<String, Object>>();
		Map<String, Object> itemMap = new HashMap<String, Object>();
        itemMap.put("title", "费用科目");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "subjectCode");
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
	private List<Map<String, Object>> getTransportHeadItem(List<FeesBillWareHouseEntity> list){
		List<Map<String, Object>> dataList = new ArrayList<Map<String,Object>>();
		 Map<String, Object> dataItem = null;
		 for(FeesBillWareHouseEntity entity:list){
			 dataItem=new HashMap<String, Object>();
			 dataItem.put("subjectCode", transportProductTypeMap.get(entity.getSubjectCode()));
			 dataItem.put("amount", entity.getAmount());
			 dataItem.put("billNo",entity.getBillNo());
			 dataList.add(dataItem);
		 }
		 return dataList;
	}
	
	
	/**
	 * 运输费用明细
	 */
	public List<Map<String, Object>> getTransportFeeHead(){
		List<Map<String, Object>> headMapList=new ArrayList<Map<String, Object>>();
		Map<String, Object> itemMap = new HashMap<String, Object>();
		
    	itemMap.put("title", "账单编号");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "billno");
        headMapList.add(itemMap);
        
		itemMap = new HashMap<String, Object>();
    	itemMap.put("title", "签收时间");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "signtime");
        headMapList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
    	itemMap.put("title", "订单号");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "orderno");
        headMapList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
    	itemMap.put("title", "运单号");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "expressnum");
        headMapList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "商品明细");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "productdetails");
        headMapList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
    	itemMap.put("title", "物流商");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "carrierName");
        headMapList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
		itemMap.put("title", "费用编号");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "feesNo");
        headMapList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
    	itemMap.put("title", "费用类型");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "feestype");
        headMapList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
    	itemMap.put("title", "费用科目");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "feessubject");
        headMapList.add(itemMap);
        
     /*   itemMap = new HashMap<String, Object>();
        itemMap.put("title", "增值费用科目");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "otherSubjectCode");
        headMapList.add(itemMap);*/
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "线路名称");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "linename");
        headMapList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "始发市");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "originatingcity");
        headMapList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "始发区");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "originatingdistrict");
        headMapList.add(itemMap);
        
//        itemMap = new HashMap<String, Object>();
//        itemMap.put("title", "目的仓");
//        itemMap.put("columnWidth", 25);
//        itemMap.put("dataKey", "targetwarehouse");
//        headMapList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "目的市");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "targetcity");
        headMapList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "目的区");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "targetdistrict");
        headMapList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "品类");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "category");
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
        itemMap.put("title", "公里数");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "kilometers");
        headMapList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "花费时间");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "spendtime");
        headMapList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "车型");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "carmodel");
        headMapList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "模版编号");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "templatenum");
        headMapList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "是否轻货");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "islight");
        headMapList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "单价");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "unitprice");
        headMapList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "重量");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "quantity");
        headMapList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
    	itemMap.put("title", "金额");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "totleprice");
        headMapList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
    	itemMap.put("title", "状态");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "state");
        headMapList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "揽收时间");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "accepttime");
        headMapList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "签收时间");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "signtime");
        headMapList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "创建时间");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "cretime");
        headMapList.add(itemMap);
        
        return headMapList;
	}
	private List<Map<String, Object>> getTransportFeeHeadItem(List<FeesPayTransportEntity> deliverList){
		 List<Map<String, Object>> dataList = new ArrayList<Map<String,Object>>();
		 Map<String, Object> dataItem = null;
		 for(FeesPayTransportEntity entity:deliverList){
			 dataItem=new HashMap<String, Object>();
			 dataItem.put("billno", entity.getBillno());
			 dataItem.put("signtime", entity.getCretime());
			 dataItem.put("orderno", entity.getOrderno());
			 dataItem.put("expressnum", entity.getWaybillNo());
			 dataItem.put("productdetails", entity.getProductdetails());
			 dataItem.put("carrierName", entity.getForwarderName());
			 dataItem.put("feesNo", entity.getFeesNo());
			 dataItem.put("feestype", mapSystemcode.get(entity.getCostType()));
			 dataItem.put("feessubject", getSubjectMap().get(entity.getSubjectCode()));
			/* dataItem.put("otherSubjectCode", getOtherSubjectMap().get(entity.getOtherSubjectCode()));*/
			 dataItem.put("linename", entity.getLinename());
			 dataItem.put("originatingcity", entity.getOriginatingcity());
			 dataItem.put("originatingdistrict", entity.getOriginatingdistrict());
//			 dataItem.put("targetwarehouse", mapWarehouse.get(entity.getTargetwarehouse()));
			 dataItem.put("targetcity", entity.getTargetcity());
			 dataItem.put("targetdistrict", entity.getTargetdistrict());
			 dataItem.put("category", entity.getCategory());
			 dataItem.put("weight", entity.getWeight());
			 dataItem.put("volume", entity.getVolume());
			 dataItem.put("kilometers", entity.getKilometers());
			 dataItem.put("spendtime", entity.getSpendtime());
			 dataItem.put("carmodel", entity.getCarmodel());
			 dataItem.put("templatenum", entity.getTemplatenum());
			 if (null != entity.getIslight()) {
				 dataItem.put("islight", entity.getIslight()==0?"否":"是");
			 }else {
				 dataItem.put("islight", entity.getIslight());
			 }
			 dataItem.put("unitprice", entity.getUnitprice());
			 dataItem.put("quantity", entity.getQuantity());
			 dataItem.put("totleprice", entity.getTotleprice());
			 dataItem.put("state", mapStatus.get(String.valueOf(entity.getState())));
			 dataItem.put("accepttime", entity.getAccepttime());
			 dataItem.put("signtime", entity.getSigntime());
			 dataItem.put("cretime", entity.getCretime());
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
}
