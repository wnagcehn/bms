package com.jiuyescm.bms.fees.bill.web;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import com.jiuyescm.bms.biz.storage.service.IBizOutstockPackmaterialService;
import com.jiuyescm.bms.common.constants.ExceptionConstant;
import com.jiuyescm.bms.common.constants.FileConstant;
import com.jiuyescm.bms.common.constants.MessageConstant;
import com.jiuyescm.bms.common.enumtype.BizState;
import com.jiuyescm.bms.common.enumtype.FileTaskStateEnum;
import com.jiuyescm.bms.common.enumtype.FileTaskTypeEnum;
import com.jiuyescm.bms.common.enumtype.StorageSubjectEnum;
import com.jiuyescm.bms.common.log.entity.BmsErrorLogInfoEntity;
import com.jiuyescm.bms.common.log.service.IBmsErrorLogInfoService;
import com.jiuyescm.bms.fees.IFeesBillService;
import com.jiuyescm.bms.fees.abnormal.entity.FeesAbnormalEntity;
import com.jiuyescm.bms.fees.dispatch.entity.FeesReceiveDispatchEntity;
import com.jiuyescm.bms.fees.entity.FeesBillEntity;
import com.jiuyescm.bms.fees.entity.FeesBillWareHouseEntity;
import com.jiuyescm.bms.fees.entity.FeesReceiveDeliverEntity;
import com.jiuyescm.bms.fees.storage.entity.FeesReceiveStorageEntity;
import com.jiuyescm.bs.util.ExportUtil;
import com.jiuyescm.cfm.common.JAppContext;
import com.jiuyescm.common.utils.excel.POISXSSUtil;
import com.jiuyescm.exception.BizException;
import com.jiuyescm.mdm.warehouse.api.IWarehouseService;
import com.jiuyescm.mdm.warehouse.vo.WarehouseVo;

/**
 * 应收账单导出
 * @author yangss
 */
@Controller("feesBillExportController")
public class FeesBillExportController {
	
	private static final Logger logger = Logger.getLogger(FeesBillExportController.class.getName());
	
	@Resource
	private ISystemCodeService systemCodeService;
	@Resource
	private IFeesBillService feesBillService;
	@Autowired 
	private IWarehouseService warehouseService;
	@Resource
	private IFileExportTaskService fileExportTaskService;
	@Resource
	private IBizOutstockPackmaterialService  packMaterialService;
	@Resource
	private IBmsErrorLogInfoService bmsErrorLogInfoService;
	
	final int pageSize = 2000;
	
	//费用类型 Map
	private Map<String,String> mapSystemcode;
	private Map<String,String> mapStatus;
	private Map<String,String> mapWarehouse;
	private Map<String,String> costSubjectMap;
	private Map<String,String> tempretureMap;
	private Map<String,String> transportProductTypeMap;
	private Map<String,String> carrierIdMap;
	
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
	private Map<String,String> getSystemcode(){
		List<SystemCodeEntity> codeList=systemCodeService.queryCodeList(new HashMap<String,Object>());
		Map<String, String> map =new LinkedHashMap<String,String>();
		for (SystemCodeEntity systemCodeEntity : codeList) {
			map.put(systemCodeEntity.getCode(),systemCodeEntity.getCodeName());
		}
		return map;
	}
	private Map<String, String> getCostSubjectMap(){
		Map<String, String> costSubjectMap = new HashMap<String, String>();
		List<SystemCodeEntity> systemCodeList1 = systemCodeService.findEnumList("STORAGE_PRICE_SUBJECT");
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
		}
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
	/**
	 * 获取配送物流商
	 */
	private Map<String, String> getCarrierIdList(){
		List<SystemCodeEntity> systemCodeList = systemCodeService.findEnumList("DISPATCH_COMPANY");
		Map<String, String> map =new LinkedHashMap<String,String>();
		if(systemCodeList!=null && systemCodeList.size()>0){
			for(int i=0;i<systemCodeList.size();i++){
				if (null != systemCodeList.get(i).getExtattr1()) {
					map.put(systemCodeList.get(i).getExtattr1(), systemCodeList.get(i).getCodeName());
				}
			}
		}
		return map;
	}
	
	/**
	 * 初始化需要使用的参数
	 */
	private void init(){
		mapSystemcode=getSystemcode();
		mapStatus=getStatus();
		mapWarehouse=getwarehouse();
		costSubjectMap=getCostSubjectMap();
		tempretureMap=getTemperatureMap();
		transportProductTypeMap=getTransportProductTypeList();
		carrierIdMap=getCarrierIdList();
	}
	
	/**
	 * 异步生成账单文件
	 * @param parameter
	 * @return
	 * @throws Exception
	 */
	@DataResolver
	public String asynExport(FeesBillEntity paramer)throws Exception{
		if (null == paramer) {
			return MessageConstant.BILL_INFO_ISNULL_MSG;
		}
		String billno=paramer.getBillno();
		if (StringUtils.isBlank(billno)) {
			return MessageConstant.BILL_NO_ISNULL_MSG;
		}
		
		//校验该账单是否已生成账单文件
		Map<String, Object> queryEntity = new HashMap<String, Object>();
		queryEntity.put("billNo", billno);

		PageInfo<FileExportTaskEntity> pageInfo = fileExportTaskService.query(queryEntity, 1, Integer.MAX_VALUE);
		if (null != pageInfo && pageInfo.getList().size() > 0) {
			return MessageConstant.BILL_FILE_ISEXIST_MSG;
		}
		
		String path = getPath();
		FileExportTaskEntity entity = new FileExportTaskEntity();
		entity.setTaskName(paramer.getBillname());
		entity.setBillNo(billno);
		entity.setStartTime(paramer.getBillstarttime());
		entity.setEndTime(paramer.getBillendtime());
		entity.setTaskType(FileTaskTypeEnum.REC_BILL.getCode());
		entity.setTaskState(FileTaskStateEnum.BEGIN.getCode());
		entity.setProgress(0d);
		entity.setFilePath(path+ FileConstant.SEPARATOR + 
				FileConstant.REC_BILL_PREFIX + billno + FileConstant.SUFFIX_XLSX);
		entity.setCreator(JAppContext.currentUserName());
		entity.setCreateTime(JAppContext.currentTimestamp());
		entity.setDelFlag("0");
		entity = fileExportTaskService.save(entity);
		
		//生成账单文件
		final FeesBillEntity condition = paramer;
		final String taskId = entity.getTaskId();
		new Thread(){
			public void run() {
				try {
					export(condition, taskId);
				} catch (Exception e) {
					updateExportTask(taskId, FileTaskStateEnum.FAIL.getCode(), 0);
					logger.error(ExceptionConstant.ASYN_REC_BILL_EXCEL_EX_MSG, e);
					//写入日志
					BmsErrorLogInfoEntity bmsErrorLogInfoEntity=new BmsErrorLogInfoEntity(this.getClass().getSimpleName(),Thread.currentThread().getStackTrace()[1].getMethodName(), "", e.toString());
					bmsErrorLogInfoService.log(bmsErrorLogInfoEntity);
				}
			};
		}.start();
		return MessageConstant.FILE_EXPORT_TASK_RECE_BILL_MSG;
	}
	
	private void export(FeesBillEntity paramer,String taskId)throws Exception{
		String billno = paramer.getBillno();
		
		updateExportTask(taskId, FileTaskStateEnum.INPROCESS.getCode(), 10);
		
		String billName=paramer.getBillname();
		String path = getPath();
		long beginTime = System.currentTimeMillis();
		logger.info("====应收账单导出："+billName + "["+billno+"]");
		
		//初始化参数
		init();
		
    	File storeFolder=new File(path);
		//如果存放上传文件的目录不存在就新建
		if(!storeFolder.isDirectory()){
			storeFolder.mkdirs();
		}
		
		POISXSSUtil poiUtil = new POISXSSUtil();
    	SXSSFWorkbook workbook = poiUtil.getXSSFWorkbook();
    	String filePath = path+ FileConstant.SEPARATOR + 
    			FileConstant.REC_BILL_PREFIX + billno + FileConstant.SUFFIX_XLSX;
    	
    	logger.info("====应收账单导出：" + "["+billno+"]" + "写入Excel begin.");
    	//账单信息
    	List<Map<String, Object>> headMapList = getFeeHead();
    	List<Map<String, Object>> dataMapList = getFeeHeadItem(paramer);
    	poiUtil.exportExcel2FilePath(poiUtil, workbook, MessageConstant.BILL_MSG, 
    			1, headMapList, dataMapList);
    	
		//仓储分仓费用
		List<FeesBillWareHouseEntity> storageWareHouseList=feesBillService.querywarehouseStorageAmount(billno);
		List<Map<String, Object>> headStorageMapList = getStorgeHead();
		List<Map<String, Object>> dataStorageList = getStorgeHeadItem(storageWareHouseList);
		poiUtil.exportExcel2FilePath(poiUtil, workbook, MessageConstant.BILL_STORAGE_GROUP_MSG, 
				1, headStorageMapList, dataStorageList);
		
		//运输费用 费用科目分组
		List<FeesBillWareHouseEntity> deliverWareHouseList=feesBillService.querywarehouseDeliverAmount(billno);
		List<Map<String, Object>> headdeliverMapList = getTransportHead();
		List<Map<String, Object>> datadeliverList = getTransportHeadItem(deliverWareHouseList);
		poiUtil.exportExcel2FilePath(poiUtil, workbook, MessageConstant.BILL_TRANSPORT_GROUP_MSG, 
				1, headdeliverMapList, datadeliverList);
		
		//配送费用 物流商分组
		List<FeesBillWareHouseEntity> distributeWareHouseList=feesBillService.querywarehouseDistributionAmount(billno);
		List<Map<String, Object>> headdistributionMapList = getDispatchHead();
		List<Map<String, Object>> datadistributionList = getDispatchHeadItem(distributeWareHouseList);
		poiUtil.exportExcel2FilePath(poiUtil, workbook, MessageConstant.BILL_DISPATCH_GROUP_MSG, 
				1, headdistributionMapList, datadistributionList);
		
		//异常费用 客诉原因分组
		List<FeesBillWareHouseEntity> abnormalWareHouseList = feesBillService.querywarehouseAbnormalAmount(billno);
    	List<Map<String, Object>> headabnormalMapList = getAbnormalHead();
    	List<Map<String, Object>> dataabnormalList = getAbnormalHeadItem(abnormalWareHouseList);
    	poiUtil.exportExcel2FilePath(poiUtil, workbook, MessageConstant.BILL_ABNORMAL_GROUP_MSG, 
    			1, headabnormalMapList, dataabnormalList);
    	
    	updateExportTask(taskId, null, 20);
    	
    	//商品存储明细费用
    	handProductStoragePallet(poiUtil, workbook, filePath, billno);
    	
    	updateExportTask(taskId, null, 30);
    	
    	//耗材存储明细费用
    	handPackStoragePallet(poiUtil, workbook, filePath, billno);
    	
    	updateExportTask(taskId, null, 40);
    	
    	//B2B出库明细费用
    	handB2BOutStock(poiUtil, workbook, filePath, billno);
    	
    	updateExportTask(taskId, null, 50);
    	
    	//仓储明细费用
    	handStoage(poiUtil, workbook, filePath, billno);
    	
    	//耗材使用明细费用
    	handPackMaterial(poiUtil, workbook, filePath, billno);
    	
    	updateExportTask(taskId, null, 60);
 
    	//运输明细费用
    	handTransport(poiUtil, workbook, filePath, billno);
    	
    	updateExportTask(taskId, null, 70);
    	
    	//配送明细费用
    	handDispatch(poiUtil, workbook, filePath, billno);
    	
    	updateExportTask(taskId, null, 80);
    	
    	//异常明细费用
    	handAbnormal(poiUtil, workbook, filePath, billno);
    	
    	updateExportTask(taskId, null, 90);
    	
    	//最后写到文件
    	poiUtil.write2FilePath(workbook, filePath);
    	
    	updateExportTask(taskId, FileTaskStateEnum.SUCCESS.getCode(), 100);
    	logger.info("====应收账单导出：" + "["+billno+"]" + "写入Excel end.==总耗时：" + (System.currentTimeMillis() - beginTime));
	}

	/**
	 * 耗材使用明细，要关联运单号
	 * @param poiUtil
	 * @param workbook
	 * @param path
	 * @param billno
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private void handPackMaterial(POISXSSUtil poiUtil, SXSSFWorkbook workbook, 
			String path, String billno)throws Exception{
		int pageNo = 1;
		int packMaterialLineNo = 1;
        int size = 20000;
		boolean doLoop = true;
		
		List<FeesReceiveStorageEntity> dataList = new  ArrayList<FeesReceiveStorageEntity>();
		while (doLoop) 
		{
			PageInfo<FeesReceiveStorageEntity> packMaterialList = feesBillService.queryPackMaterialByBillNo(billno, pageNo, size);
			if (null != packMaterialList && packMaterialList.getList().size() > 0) {
				if (packMaterialList.getList().size() < size) {
					doLoop = false;
				}else {
					pageNo += 1;
				}
				dataList.addAll(packMaterialList.getList());
				Map<String,Object> param = new HashMap<String,Object>();
				param.put("billNo", billno);
				List<Map<String,String>> ListHead = packMaterialService.getMaterialCode(param);
				
				Map<String,Object> headParam = getRealHead(ListHead);
				
				List<String> realList = (List<String>)headParam.get("headList");
				
				Integer extendNum = (Integer)headParam.get("extendNum");
				
				List<Map<String, Object>> headPackMaterialMapList = getNewPackMaterialFeeHead(realList,extendNum);
				
				boolean isAddColumn = false;
				if(extendNum>0){
					isAddColumn = true;
				}
				
				List<Map<String, Object>> dataPackMaterialList = getNewPackMaterialHeadItem(dataList,realList,isAddColumn);
				
				poiUtil.exportExcel2FilePath(poiUtil, workbook, MessageConstant.BILL_PACKMATERIAL_DETAIL_MSG,
						packMaterialLineNo, headPackMaterialMapList, dataPackMaterialList);
				if (null != packMaterialList && packMaterialList.getList().size() > 0) {
					packMaterialLineNo += packMaterialList.getList().size();
				}
			}else {
				doLoop = false;
			}
		}	
	}
	
	/**
	 * 商品存储明细费用
	 * @param poiUtil
	 * @param workbook
	 * @param path
	 * @param billno
	 * @throws Exception
	 */
	private void handProductStoragePallet(POISXSSUtil poiUtil, SXSSFWorkbook workbook, 
			String path, String billno)throws Exception{
		int pageNo = 1;
		int productLineNo = 1;
		boolean doLoop = true;
		while (doLoop) {
			PageInfo<FeesReceiveStorageEntity> productStoragePalletList = 
					feesBillService.queryStorageByBillNoSubjectId(billno, StorageSubjectEnum.PRODUCT_STOR.getCode(), pageNo, pageSize);
			if (null != productStoragePalletList && productStoragePalletList.getList().size()>0) {
				if (productStoragePalletList.getList().size() < pageSize) {
					doLoop = false;
				}else {
					pageNo += 1;	
				}
				List<Map<String, Object>> headProductStoragePalletMapList = getStorageFeeHead();
				List<Map<String, Object>> dataProductStoragePalletList = getStorageFeeHeadItem(productStoragePalletList.getList());
				
				poiUtil.exportExcel2FilePath(poiUtil, workbook, MessageConstant.BILL_PRODUCTPALLET_DETAIL_MSG, 
						productLineNo, headProductStoragePalletMapList, dataProductStoragePalletList);
				if (null != productStoragePalletList && productStoragePalletList.getList().size() > 0) {
					productLineNo += productStoragePalletList.getList().size();
				}
			}else {
				doLoop = false;
			}
			
		}
	}
	
	/**
	 * 耗材存储明细费用
	 * @param poiUtil
	 * @param workbook
	 * @param path
	 * @param billno
	 * @throws Exception
	 */
	private void handPackStoragePallet(POISXSSUtil poiUtil, SXSSFWorkbook workbook, 
			String path, String billno)throws Exception{
		int pageNo = 1;
		int packStorageLineNo = 1;
		boolean doLoop = true;
		while (doLoop) {
			PageInfo<FeesReceiveStorageEntity> packStoragePalletList = 
					feesBillService.queryStorageByBillNoSubjectId(billno, StorageSubjectEnum.MATERIAL_STOR.getCode(), pageNo, pageSize);
			if (null != packStoragePalletList && packStoragePalletList.getList().size()>0) {
				if (packStoragePalletList.getList().size() < pageSize) {
					doLoop = false;
				}else {
					pageNo += 1; 	
				}
				List<Map<String, Object>> headPackStoragePalletMapList = getStorageFeeHead();
				List<Map<String, Object>> dataPackStoragePalletList = getStorageFeeHeadItem(packStoragePalletList.getList());
				
				poiUtil.exportExcel2FilePath(poiUtil, workbook, MessageConstant.BILL_PACKPALLET_DETAIL_MSG, 
						packStorageLineNo, headPackStoragePalletMapList, dataPackStoragePalletList);
				if (null != packStoragePalletList && packStoragePalletList.getList().size() > 0) {
					packStorageLineNo += packStoragePalletList.getList().size();
				}
			}else {
				doLoop = false;
			}
			
		}
	}
	
	/**
	 * B2B出库明细费用
	 * @param poiUtil
	 * @param workbook
	 * @param path
	 * @param billno
	 * @throws Exception
	 */
	private void handB2BOutStock(POISXSSUtil poiUtil, SXSSFWorkbook workbook, 
			String path, String billno)throws Exception{
		int pageNo = 1;
		int b2bLineNo = 1;
		boolean doLoop = true;
		while (doLoop) {
			PageInfo<FeesReceiveStorageEntity> b2BOutStockList = 
					feesBillService.queryStorageByBillNoSubjectId(billno, StorageSubjectEnum.B2B_OUT.getCode(), pageNo, pageSize);
			if (null != b2BOutStockList && b2BOutStockList.getList().size()>0) {
				if (b2BOutStockList.getList().size() < pageSize) {
					doLoop = false;
				}else {
					pageNo += 1; 
				}
				List<Map<String, Object>> headB2BOutStockMapList = getStorageFeeHead();
				List<Map<String, Object>> dataB2BOutStockList = getStorageFeeHeadItem(b2BOutStockList.getList());
				
				poiUtil.exportExcel2FilePath(poiUtil, workbook, MessageConstant.BILL_B2BOUTSTOCK_DETAIL_MSG, 
						b2bLineNo, headB2BOutStockMapList, dataB2BOutStockList);
				if (null != b2BOutStockList && b2BOutStockList.getList().size() > 0) {
					b2bLineNo += b2BOutStockList.getList().size();
				}
			}else {
				doLoop = false;
			}
			
		}
	}
	
	/**
	 * 仓储明细费用
	 * @param poiUtil
	 * @param workbook
	 * @param path
	 * @param billno
	 * @throws Exception
	 */
	private void handStoage(POISXSSUtil poiUtil, SXSSFWorkbook workbook, 
			String path, String billno)throws Exception{
		int pageNo = 1;
		int otherLineNo = 1;
		boolean doLoop = true;
		while (doLoop) {
			PageInfo<FeesReceiveStorageEntity> otherStorageList = 
					feesBillService.querystorageDetailByBillNo(billno, pageNo, pageSize);
			if (null != otherStorageList && otherStorageList.getList().size()>0) {
				if (otherStorageList.getList().size() < pageSize) {
					doLoop = false;
				}else {
					pageNo += 1; 
				}
				List<Map<String, Object>> headOtherStorageMapList = getStorageFeeHead();
				List<Map<String, Object>> dataOtherStorageList = getStorageFeeHeadItem(otherStorageList.getList());
				
				poiUtil.exportExcel2FilePath(poiUtil, workbook, MessageConstant.BILL_STORAGE_DETAIL_MSG, 
						otherLineNo, headOtherStorageMapList, dataOtherStorageList);
				if (null != otherStorageList && otherStorageList.getList().size() > 0) {
					otherLineNo += otherStorageList.getList().size();
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
			PageInfo<FeesReceiveDeliverEntity> deliverList = 
					feesBillService.querydeliverDetailByBillNo(billno, pageNo, pageSize);
			if (null != deliverList && deliverList.getList().size() > 0) {
				if (deliverList.getList().size() < pageSize) {
					doLoop = false;
				}else {
					pageNo += 1; 
				}
				List<Map<String, Object>> headdeliverDetailMapList = getTransportFeeHead();
				List<Map<String, Object>> datadeliverDetailList = getTransportFeeHeadItem(deliverList.getList());
				
				poiUtil.exportExcel2FilePath(poiUtil, workbook, MessageConstant.BILL_TRANSPORT_DETAIL_MSG, 
						transportLineNo, headdeliverDetailMapList, datadeliverDetailList);
				if (null != deliverList && deliverList.getList().size() > 0) {
					transportLineNo += deliverList.getList().size();
				}
			}else {
				doLoop = false;
			}
			
			
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
			String path, String billno)throws Exception{
		int pageNo = 1;
		int dispatchLineNo = 1;
		boolean doLoop = true;
		while (doLoop) {
			PageInfo<FeesReceiveDispatchEntity> distributionList = 
					feesBillService.querydistributionDetailByBillNo(billno, pageNo, pageSize);
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
			String path, String billno)throws Exception{
		int pageNo = 1;
		int abnormalLineNo = 1;
		boolean doLoop = true;
		while (doLoop) {
			PageInfo<FeesAbnormalEntity> abnormalList = 
					feesBillService.queryabnormalDetailByBillNo(billno, pageNo, pageSize);
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
	
	/*
	 * 账单  头
	 */
	public List<Map<String,Object>> getFeeHead(){
		List<Map<String, Object>> headMapList=new ArrayList<Map<String, Object>>();
		Map<String, Object> itemMap = new HashMap<String, Object>();
		itemMap.put("title", "账单编号");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "billno");
        headMapList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
    	itemMap.put("title", "账单名称");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "billname");
        headMapList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
    	itemMap.put("title", "商家ID");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "customerid");
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
        itemMap.put("dataKey", "billstatus");
        headMapList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
    	itemMap.put("title", "开始时间");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "billstarttime");
        headMapList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
    	itemMap.put("title", "结束时间");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "billendtime");
        headMapList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "备注");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "remark");
        headMapList.add(itemMap);
        
        return headMapList;
	}
	public List<Map<String,Object>> getFeeHeadItem(Map<String,Object> map){
		 List<Map<String, Object>> dataList = new ArrayList<Map<String,Object>>();
		 Map<String,String> mapstatus=new HashMap<String,String>();
		 mapstatus.put("0", "未核销");
		 mapstatus.put("1", "已核销");
		 Map<String, Object> dataItem=new HashMap<String,Object>();
		 dataItem.put("billno", map.get("billno"));
		 dataItem.put("billname", map.get("billname"));
		 dataItem.put("customerid", map.get("customerid"));
		 dataItem.put("totleprice", map.get("totleprice"));
		 dataItem.put("receiptAmount", map.get("receiptAmount"));
		 dataItem.put("billstatus", mapstatus.get(String.valueOf(map.get("billstatus"))));
		 dataItem.put("billstarttime", map.get("billstarttime"));
		 dataItem.put("billendtime", map.get("billendtime"));
		 dataItem.put("remark", map.get("remark"));
		 dataList.add(dataItem);
		 return dataList;
	}
	public List<Map<String,Object>> getFeeHeadItem(FeesBillEntity paramer){
		List<Map<String, Object>> dataList = new ArrayList<Map<String,Object>>();
		Map<String,String> mapstatus=new HashMap<String,String>();
		mapstatus.put("0", "未核销");
		mapstatus.put("1", "已核销");
		Map<String, Object> dataItem=new HashMap<String,Object>();
		dataItem.put("billno", paramer.getBillno());
		dataItem.put("billname", paramer.getBillname());
		dataItem.put("customerid", paramer.getCustomerid());
		dataItem.put("totleprice", paramer.getTotleprice());
		dataItem.put("receiptAmount", paramer.getReceiptAmount());
		dataItem.put("billstatus", mapstatus.get(String.valueOf(paramer.getBillstatus())));
		dataItem.put("billstarttime", paramer.getBillstarttime());
		dataItem.put("billendtime", paramer.getBillendtime());
		dataItem.put("remark", paramer.getRemark());
		dataList.add(dataItem);
		return dataList;
	}
	/**
	 * 获取仓储费用  导出文件头
	 */
	private List<Map<String, Object>> getStorgeHead(){
		List<Map<String, Object>> headMapList=new ArrayList<Map<String, Object>>();
		Map<String, Object> itemMap = new HashMap<String, Object>();
		itemMap.put("title", "账单编号");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "billNo");
        headMapList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
    	itemMap.put("title", "仓库");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "warehouseid");
        headMapList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
    	itemMap.put("title", "金额");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "amount");
        headMapList.add(itemMap);
        
        return headMapList;
	}
	private List<Map<String, Object>> getStorgeHeadItem(List<FeesBillWareHouseEntity> list){
		List<Map<String, Object>> dataList = new ArrayList<Map<String,Object>>();
		 Map<String, Object> dataItem = null;
		 for(FeesBillWareHouseEntity entity:list){
			 dataItem=new HashMap<String, Object>();
			 dataItem.put("billNo", entity.getBillNo());
			 dataItem.put("warehouseid",mapWarehouse.get(entity.getWarehouseCode()));
			 dataItem.put("amount", entity.getAmount());
			 dataList.add(dataItem);
		 }
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
	 * 配送费用 物流商分组
	 */
	private List<Map<String, Object>> getDispatchHead(){
		List<Map<String, Object>> headMapList=new ArrayList<Map<String, Object>>();
		Map<String, Object> itemMap = new HashMap<String, Object>();
        itemMap.put("title", "物流商");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "carrierid");
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
			 dataItem.put("carrierid", carrierIdMap.get(entity.getCarrierid()));
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
			 dataItem.put("reason", entity.getReason());
			 dataItem.put("amount", entity.getAmount());
			 dataItem.put("billNo",entity.getBillNo());
			 dataList.add(dataItem);
		 }
		 return dataList;
	}
	
	/**
	 * 仓储费用明细
	 */
	public List<Map<String, Object>> getStorageFeeHead(){
		List<Map<String, Object>> headMapList=new ArrayList<Map<String, Object>>();
		Map<String, Object> itemMap = new HashMap<String, Object>();
        
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
        itemMap.put("title", "重量");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "weight");
        headMapList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "单价");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "unitPrice");
        headMapList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
    	itemMap.put("title", "金额");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "cost");
        headMapList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
    	itemMap.put("title", "数据类型");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "bizType");
        headMapList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "创建时间");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "createTime");
        headMapList.add(itemMap);
        
        return headMapList;
	}
	public List<Map<String, Object>> getStorageFeeHeadItem( List<FeesReceiveStorageEntity> list){
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
			dataItem.put("bizType", BizState.getDesc(entity.getBizType()));
			dataItem.put("createTime", entity.getCreateTime());
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
        
    	itemMap.put("title", "操作时间");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "operationtime");
        headMapList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
    	itemMap.put("title", "商家ID");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "customerid");
        headMapList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "仓库");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "warehouseName");
        headMapList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
		itemMap.put("title", "费用编号");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "feesid");
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
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "增值费用科目");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "otherSubjectCode");
        headMapList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "线路名称");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "linename");
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
        itemMap.put("title", "始发市");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "originatingcity");
        headMapList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "始发区");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "originatingdistrict");
        headMapList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "目的仓");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "targetwarehouse");
        headMapList.add(itemMap);
        
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
        itemMap.put("title", "温度类型");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "temperaturetype");
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
	private List<Map<String, Object>> getTransportFeeHeadItem(List<FeesReceiveDeliverEntity> deliverList){
		 List<Map<String, Object>> dataList = new ArrayList<Map<String,Object>>();
		 Map<String, Object> dataItem = null;
		 for(FeesReceiveDeliverEntity entity:deliverList){
			 dataItem=new HashMap<String, Object>();
			 dataItem.put("operationtime", entity.getOperationtime());
			 dataItem.put("customerid", entity.getCustomerid());
			 dataItem.put("warehouseName", mapWarehouse.get(entity.getWarehouseCode()));
			 dataItem.put("feesid", entity.getFeesNo());
			 dataItem.put("feestype", mapSystemcode.get(entity.getCostType()));
			 dataItem.put("feessubject", mapSystemcode.get(entity.getSubjectCode()));
			 dataItem.put("otherSubjectCode", mapSystemcode.get(entity.getOtherSubjectCode()));
			 dataItem.put("linename", entity.getLinename());
			 dataItem.put("orderno", entity.getOrderno());
			 dataItem.put("expressnum", entity.getWaybillNo());
			 dataItem.put("originatingcity", entity.getOriginatingcity());
			 dataItem.put("originatingdistrict", entity.getOriginatingdistrict());
			 dataItem.put("targetwarehouse", mapWarehouse.get(entity.getTargetwarehouse()));
			 dataItem.put("targetcity", entity.getTargetcity());
			 dataItem.put("targetdistrict", entity.getTargetdistrict());
			 dataItem.put("temperaturetype", entity.getTemperaturetype());
			 dataItem.put("category", entity.getCategory());
			 dataItem.put("weight", entity.getWeight());
			 dataItem.put("volume", entity.getVolume());
			 dataItem.put("kilometers", entity.getKilometers());
			 dataItem.put("spendtime", entity.getSpendtime());
			 dataItem.put("carmodel", entity.getCarmodel());
			 dataItem.put("templatenum", entity.getTemplatenum());
			 dataItem.put("islight", entity.getIslight()==0?"否":"是");
			 dataItem.put("unitprice", entity.getUnitprice());
			 dataItem.put("quantity", entity.getQuantity());
			 dataItem.put("totleprice", entity.getTotleprice());
			 dataItem.put("state",mapStatus.get(String.valueOf(entity.getState())));
			 dataItem.put("accepttime", entity.getAccepttime());
			 dataItem.put("signtime", entity.getSigntime());
			 dataItem.put("cretime", entity.getCretime());
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
        itemMap.put("dataKey", "delivery");
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
        itemMap.put("title", "商品数量");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "productNums");
        headMapList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "商品明细");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "productDetail");
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
        itemMap.put("title", "重量界限");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "weightLimit");
        headMapList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "单价");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "unitPrice");
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
    	itemMap.put("title", "配送金额");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "amount");
        headMapList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "温度类型");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "temperatureType");
        headMapList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
    	itemMap.put("title", "订单操作费金额");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "orderAmount");
        headMapList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "业务类型");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "billTypeName");
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
        itemMap.put("title", "数据类型");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "bizType");
        headMapList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "创建时间");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "createTime");
        headMapList.add(itemMap);
        
        return headMapList;
	}
	private List<Map<String, Object>> getDispatchFeeHeadItem(List<FeesReceiveDispatchEntity> list){
		 List<Map<String, Object>> dataList = new ArrayList<Map<String,Object>>();
		 Map<String, Object> dataItem = null;
		 for(FeesReceiveDispatchEntity entity:list){
			dataItem=new HashMap<String, Object>();
			dataItem.put("customerName", entity.getCustomerName());
			dataItem.put("warehouseName", mapWarehouse.get(entity.getWarehouseCode()));
			dataItem.put("carrierName", entity.getCarrierName());
			dataItem.put("delivery", entity.getDeliverName());
			dataItem.put("outstockNo", entity.getOutstockNo());
			dataItem.put("externalNo", entity.getExternalNo());
			dataItem.put("waybillNo", entity.getWaybillNo());
			dataItem.put("toProvinceName", entity.getToProvinceName());
			dataItem.put("toCityName", entity.getToCityName());
			dataItem.put("toDistrictName", entity.getToDistrictName());
			dataItem.put("productNums", entity.getTotalQuantity());
			dataItem.put("productDetail", entity.getProductDetail());
			dataItem.put("totalWeight", entity.getTotalWeight());
			dataItem.put("chargedWeight", entity.getChargedWeight());
			dataItem.put("weightLimit", entity.getWeightLimit());
			dataItem.put("unitPrice", entity.getUnitPrice());
			dataItem.put("headWeight", entity.getHeadWeight());
			dataItem.put("continuedWeight", entity.getContinuedWeight());
			dataItem.put("headPrice", entity.getHeadPrice());
			dataItem.put("continuedPrice", entity.getContinuedPrice());
			dataItem.put("amount", entity.getAmount());
        	dataItem.put("temperatureType", tempretureMap.get(entity.getTemperatureType()));
			dataItem.put("orderAmount", entity.getOrderOperatorAmount());
			dataItem.put("billTypeName", entity.getBillTypeName());
			dataItem.put("bigstatus", entity.getBigstatus());
        	dataItem.put("smallstatus", entity.getSmallstatus());
        	dataItem.put("bizType", BizState.getDesc(entity.getBizType()));
			dataItem.put("createTime", entity.getCreateTime());
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
    	itemMap.put("title", "创建时间");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "createTime");
        headMapList.add(itemMap);
        
        return headMapList;
	}
	private List<Map<String, Object>> getAbnormalFeeHeadItem(List<FeesAbnormalEntity> list){
		 List<Map<String, Object>> dataList = new ArrayList<Map<String,Object>>();
		 Map<String, Object> dataItem = null;
		 for(FeesAbnormalEntity entity:list){
			 dataItem=new HashMap<String, Object>();
			 dataItem.put("customerName", entity.getCustomerName());
			 dataItem.put("warehouseName", mapWarehouse.get(entity.getWarehouseId()));
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
			 dataItem.put("createTime", entity.getCreateTime());
			 dataList.add(dataItem);
		 }
		 return dataList;
	}
	
	@SuppressWarnings("deprecation")
	private String getPath(){
		SystemCodeEntity systemCodeEntity = systemCodeService.getSystemCode("GLOABL_PARAM", "EXPORT_RECEIVE_BILL");
		if(systemCodeEntity == null){
			throw new BizException("请在系统参数中配置文件上传路径,参数GLOABL_PARAM,EXPORT_RECEIVE_BILL");
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
	
	public List<Map<String, Object>> getNewPackMaterialFeeHead(List<String> realList,Integer extendNum){
	
		Map<String,String>  materialMap = ExportUtil.getMaterialMap();
		
		List<Map<String, Object>> headMapList=new ArrayList<Map<String, Object>>();
		Map<String, Object> itemMap = new HashMap<String, Object>();
        
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
        itemMap.put("title", "费用科目");
        itemMap.put("columnWidth", 50);
        itemMap.put("dataKey", "costSubject");
        headMapList.add(itemMap);
                
        itemMap = new HashMap<String, Object>();
    	itemMap.put("title", "运单号");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "waybillNo");
        headMapList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
    	itemMap.put("title", "金额");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "cost");
        headMapList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "创建时间");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "createTime");
        headMapList.add(itemMap);
        
        for(int i=0;i<realList.size();i++)
        {
        	String str = realList.get(i);
        	
        	  String bm = str+"_"+"bm";
          	  String name = str+"_"+"name";
          	  String wbbh = str+"_"+"bh";
          	  String sl = str + "_"+ "sl";
          	  String dj = str + "_"+"dj";
      		
      		  itemMap = new HashMap<String, Object>();
              itemMap.put("title", "编码");
              itemMap.put("columnWidth", 25);
              itemMap.put("dataKey", bm);
              headMapList.add(itemMap);
              
              itemMap = new HashMap<String, Object>();
              itemMap.put("title", materialMap.get(str));
              itemMap.put("columnWidth", 25);
              itemMap.put("dataKey", name);
              headMapList.add(itemMap);
              
              itemMap = new HashMap<String, Object>();
              itemMap.put("title", "外部商品编号");
              itemMap.put("columnWidth", 25);
              itemMap.put("dataKey", wbbh);
              headMapList.add(itemMap);
              
              itemMap = new HashMap<String, Object>();
              if("GB".equals(str)){
            	  itemMap.put("title", "重量");
              }else{
            	  itemMap.put("title", "数量");
              }
              itemMap.put("columnWidth", 25);
              itemMap.put("dataKey", sl);
              headMapList.add(itemMap);
              
              itemMap = new HashMap<String, Object>();
              itemMap.put("title", "单价");
              itemMap.put("columnWidth", 25);
              itemMap.put("dataKey", dj);
              headMapList.add(itemMap);
        	
        }
        
 
        for(int j=0;j<extendNum;j++){
        	 itemMap = new HashMap<String, Object>();
             itemMap.put("title", "商品编号");
             itemMap.put("columnWidth", 25);
             itemMap.put("dataKey", "productNo"+j);
             headMapList.add(itemMap);
             
             itemMap = new HashMap<String, Object>();
             itemMap.put("title", "商品名称");
             itemMap.put("columnWidth", 25);
             itemMap.put("dataKey", "productName"+j);
             headMapList.add(itemMap);
             
             itemMap = new HashMap<String, Object>();
             itemMap.put("title", "外部商品编号");
             itemMap.put("columnWidth", 25);
             itemMap.put("dataKey", "externalProductNo"+j);
             headMapList.add(itemMap);
             
             itemMap = new HashMap<String, Object>();
             itemMap.put("title", "数量");
             itemMap.put("columnWidth", 25);
             itemMap.put("dataKey", "quantity"+j);
             headMapList.add(itemMap);
     		             
             itemMap = new HashMap<String, Object>();
             itemMap.put("title", "单价");
             itemMap.put("columnWidth", 25);
             itemMap.put("dataKey", "unitPrice"+j);
             headMapList.add(itemMap);
        }
        return headMapList;
	}
	
	public Map<String,Object> getRealHead(List<Map<String,String>> listHead)
	{
		Map<String,Object>  map = new HashMap<String,Object>();
		Integer extendNum = 0;
		Map<String,String>  materialMap = ExportUtil.getMaterialMap();
		
		List<String> headMapList=new ArrayList<String>();
        
        Set<String> set = materialMap.keySet();
                        
        for(int j=0;j<listHead.size();j++)
        {
        	boolean fu = false;
        	Map<String,String> temp = listHead.get(j);
        	
        	String code = temp.get("code").trim();
        	
        	for (String str : set) 
        	{  
        	      if(code.indexOf(str)>=0)
        	      {
        	    	  fu = true;
        	    	  if(!headMapList.contains(str)){
        	    		  headMapList.add(str);
        	    	  }
        	    	 
        	      }
        	} 
        	
        	if(!fu)
        	{
        		extendNum = extendNum +1;
        	}
        	
        }
        
        map.put("extendNum", extendNum);
        map.put("headList", headMapList);
        
        return map;
	}
	
	public List<Map<String, Object>> getNewPackMaterialHeadItem( List<FeesReceiveStorageEntity> list,List<String> realList,boolean isAddColumn){
		
		 List<Map<String, Object>> dataList = new ArrayList<Map<String,Object>>();
		 Map<String, Object> dataItem = null;
		 String tempWillBillNo = "";
		 BigDecimal cost = null;
		 int i=0;
		 int j = 0;
		 
		 List<FeesReceiveStorageEntity> exlist = new  ArrayList<FeesReceiveStorageEntity>();
		 
		 for(FeesReceiveStorageEntity entity:list)
		 {
			 if(StringUtils.isEmpty(entity.getWaybillNo()))
			 {//空的运单号不做处理，也没有办法搞定
					continue;
			 }else{
				 exlist.add(entity);
			 }
		 }
		 
		 for(FeesReceiveStorageEntity entity:exlist)
		 {
			 j = j+1;
			 if(StringUtils.isEmpty(tempWillBillNo))
			 {
				tempWillBillNo = entity.getWaybillNo();
				dataItem=new HashMap<String, Object>();
				cost = new BigDecimal(0);
				
				dataItem.put("customerName", entity.getCustomerName());
				dataItem.put("warehouseName", mapWarehouse.get(entity.getWarehouseCode()));
				dataItem.put("orderType", entity.getOrderType());
				dataItem.put("costSubject", costSubjectMap.get(entity.getSubjectCode()));
				dataItem.put("createTime", entity.getCreateTime());
				dataItem.put("waybillNo", tempWillBillNo);
				
				for(String str:realList)
				{
					if(entity.getProductNo().indexOf(str)>=0)
					{
						  String bm = str+"_"+"bm";
	                  	  String name = str+"_"+"name";
	                  	  String wbbh = str+"_"+"bh";
	                  	  String sl = str + "_"+ "sl";
	                  	  String dj = str + "_"+"dj";
	                  	  
	                  	dataItem.put(bm, entity.getProductNo());
						dataItem.put(name, entity.getProductName());
						dataItem.put(wbbh, entity.getExternalProductNo());
						if(entity.getProductNo().indexOf("GB")>=0){
							dataItem.put(sl, entity.getWeight());
						}else{
							dataItem.put(sl, entity.getQuantity());
						}
						dataItem.put(dj, entity.getUnitPrice());
						
						cost = cost.add(entity.getCost());
						dataItem.put("cost", cost);//金额
						
						continue;
					}
				}
				
				if(isAddColumn)
				{
					dataItem.put("productNo"+i, entity.getProductNo());
					dataItem.put("productName"+i, entity.getProductName());
					dataItem.put("externalProductNo"+i, entity.getExternalProductNo());
					dataItem.put("quantity"+i, entity.getQuantity());
					dataItem.put("unitPrice"+i, entity.getUnitPrice());
					
					cost = cost.add(entity.getCost());
					dataItem.put("cost", cost);//金额
					
				}
				
				 //添加最后的数据
				 if(exlist.size()==j)
				 {
					 dataList.add(dataItem);
				 }
								
				continue;//不能再往下走
				
			 }
		
			 if(!entity.getWaybillNo().equals(tempWillBillNo))
			 {
				i = 0;//复位
				dataList.add(dataItem);//添加一个运单的数据
				 
				tempWillBillNo = entity.getWaybillNo();
				dataItem=new HashMap<String, Object>();//清零
				cost = new BigDecimal(0);//清零
				
				dataItem.put("customerName", entity.getCustomerName());
				dataItem.put("warehouseName", mapWarehouse.get(entity.getWarehouseCode()));
				dataItem.put("orderType", entity.getOrderType());
				dataItem.put("costSubject", costSubjectMap.get(entity.getSubjectCode()));
				dataItem.put("createTime", entity.getCreateTime());
				dataItem.put("waybillNo", tempWillBillNo);
				
				for(String str:realList)
				{
					if(entity.getProductNo().indexOf(str)>=0)
					{
						  String bm = str+"_"+"bm";
	                  	  String name = str+"_"+"name";
	                  	  String wbbh = str+"_"+"bh";
	                  	  String sl = str + "_"+ "sl";
	                  	  String dj = str + "_"+"dj";
	                  	  
	                  	dataItem.put(bm, entity.getProductNo());
						dataItem.put(name, entity.getProductName());
						dataItem.put(wbbh, entity.getExternalProductNo());
						if(entity.getProductNo().indexOf("GB")>=0){
							dataItem.put(sl, entity.getWeight());
						}else{
							dataItem.put(sl, entity.getQuantity());
						}
						dataItem.put(dj, entity.getUnitPrice());
						
						cost = cost.add(entity.getCost());
						dataItem.put("cost", cost);//金额
												
					}
				}
				
				if(isAddColumn)
				{
					dataItem.put("productNo"+i, entity.getProductNo());
					dataItem.put("productName"+i, entity.getProductName());
					dataItem.put("externalProductNo"+i, entity.getExternalProductNo());
					dataItem.put("quantity"+i, entity.getQuantity());
					dataItem.put("unitPrice"+i, entity.getUnitPrice());
					
					cost = cost.add(entity.getCost());
					dataItem.put("cost", cost);//金额
				}
				
			 }else{
				i = i+1;
				for(String str:realList)
				{
					if(entity.getProductNo().indexOf(str)>=0)
					{
						  String bm = str+"_"+"bm";
	                  	  String name = str+"_"+"name";
	                  	  String wbbh = str+"_"+"bh";
	                  	  String sl = str + "_"+ "sl";
	                  	  String dj = str + "_"+"dj";
	                  	  
	                  	dataItem.put(bm, entity.getProductNo());
						dataItem.put(name, entity.getProductName());
						dataItem.put(wbbh, entity.getExternalProductNo());
						if(entity.getProductNo().indexOf("GB")>=0){
							dataItem.put(sl, entity.getWeight());
						}else{
							dataItem.put(sl, entity.getQuantity());
						}
						dataItem.put(dj, entity.getUnitPrice());
						
						cost = cost.add(entity.getCost());
						dataItem.put("cost", cost);//金额
						
					}
				}
				
				if(isAddColumn)
				{
					dataItem.put("productNo"+i, entity.getProductNo());
					dataItem.put("productName"+i, entity.getProductName());
					dataItem.put("externalProductNo"+i, entity.getExternalProductNo());
					dataItem.put("quantity"+i, entity.getQuantity());
					dataItem.put("unitPrice"+i, entity.getUnitPrice());
					
					cost = cost.add(entity.getCost());
					dataItem.put("cost", cost);//金额
					
					
				}
				
				
			 }
			 //添加最后的数据
			 if(exlist.size()==j)
			 {
				 dataList.add(dataItem);
			 }
			
		 }
		 return dataList;
	}
}
