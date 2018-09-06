package com.jiuyescm.bms.consumer.upload;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.jiuyescm.bms.asyn.service.IBmsFileAsynTaskService;
import com.jiuyescm.bms.asyn.vo.BmsFileAsynTaskVo;
import com.jiuyescm.bms.base.dictionary.entity.SystemCodeEntity;
import com.jiuyescm.bms.base.dictionary.service.ISystemCodeService;
import com.jiuyescm.bms.biz.storage.entity.BizPackStorageTempEntity;
import com.jiuyescm.bms.biz.storage.entity.BizProductPalletStorageTempEntity;
import com.jiuyescm.bms.biz.storage.service.IBizPackStorageService;
import com.jiuyescm.bms.biz.storage.service.IBizPackStorageTempService;
import com.jiuyescm.bms.biz.storage.service.IBizProductPalletStorageService;
import com.jiuyescm.bms.biz.storage.service.IBizProductPalletStorageTempService;
import com.jiuyescm.bms.common.enumtype.status.FileAsynTaskStatusEnum;
import com.jiuyescm.bms.consumer.common.BmsMaterialImportTask;
import com.jiuyescm.bs.util.ExportUtil;
import com.jiuyescm.bs.util.StringUtil;
import com.jiuyescm.cfm.common.JAppContext;
import com.jiuyescm.common.utils.excel.POISXSSUtil;
import com.jiuyescm.framework.fastdfs.client.StorageClient;
import com.jiuyescm.framework.fastdfs.model.StorePath;
import com.jiuyescm.mdm.customer.api.ICustomerService;
import com.jiuyescm.mdm.customer.vo.CustomerVo;
import com.jiuyescm.mdm.warehouse.api.IWarehouseService;
import com.jiuyescm.mdm.warehouse.vo.WarehouseVo;

@Service("bmsPalletImportListener")
public class BmsPalletImportListener extends BmsCommonImportListener {

	private static final Logger logger = LoggerFactory.getLogger(BmsPalletImportListener.class);

	@Autowired private IBmsFileAsynTaskService bmsFileAsynTaskService;
	@Autowired private IWarehouseService warehouseService;
	@Autowired private ISystemCodeService systemCodeService; //业务类型
	@Autowired private ICustomerService customerService;
	@Autowired private IBizProductPalletStorageTempService bizProductPalletStorageTempService;
	@Autowired private IBizPackStorageTempService bizPackStorageTempService;
	@Autowired private StorageClient storageClient;
	@Autowired private IBizProductPalletStorageService bizProductPalletStorageService;
	@Autowired private IBizPackStorageService bizPackStorageService;
	private Map<Integer,String> originColumn = null; //源生表头信息
	private static final String REMARK = "导入数据不规范,请下载查看最后一列说明";
	//商品按托存储费和耗材存储费
	@Override
	protected List<String> initColumnNames() {
		String[] str = {"库存日期","仓库","商家全称","商品冷冻","商品冷藏","商品常温","商品恒温","耗材冷冻","耗材冷藏","耗材常温","耗材恒温"};
		return Arrays.asList(str); 
	}

	@Override
	protected String[] initColumnsNamesForNeed() {
		String[] str = {"库存日期","仓库","商家全称"};
		return str;
	}

	@Override
	protected boolean batchHander(BmsFileAsynTaskVo taskEntity,BmsMaterialImportTask bmsMaterialImportTaskCommon) throws Exception{
		Map<String, String> wareHouseMap = new HashMap<String, String>();
		Map<String, String> customerMap = new HashMap<String, String>();
		Map<String, String> temperatureMap = new HashMap<String, String>();
		//仓库
		List<WarehouseVo> wareHouseList = warehouseService.queryAllWarehouse();
		if(wareHouseList != null && wareHouseList.size()>0){
			for(WarehouseVo wareHouse : wareHouseList){
				wareHouseMap.put(wareHouse.getWarehousename().trim(),wareHouse.getWarehouseid());
			}
		}
		//商家
		PageInfo<CustomerVo> tmpPageInfo = customerService.query(null, 0, Integer.MAX_VALUE);
		if (tmpPageInfo != null && tmpPageInfo.getList() != null && tmpPageInfo.getList().size()>0) {
			for(CustomerVo customer : tmpPageInfo.getList()){
				if(customer != null){
					customerMap.put(customer.getCustomername().trim(), customer.getCustomerid());
				}
			}
		}
		//温度类型
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("typeCode", "TEMPERATURE_TYPE");
		List<SystemCodeEntity> temperatureList = systemCodeService.queryCodeList(param);
		if(temperatureList != null && temperatureList.size()>0){
		    for(SystemCodeEntity scEntity : temperatureList){
		    	temperatureMap.put(scEntity.getCodeName().trim(), scEntity.getCode());
		    }
		}
		
		List<BizProductPalletStorageTempEntity> palletList = new ArrayList<BizProductPalletStorageTempEntity>();
		List<BizPackStorageTempEntity> packList = new ArrayList<BizPackStorageTempEntity>();
		
		for(Entry<Integer, Map<String, String>> row : reader.getContents().entrySet()) { 
			Map<String, String> cells = row.getValue();
			originColumn = reader.getOriginColumn();
			int rowNo = row.getKey(); 	//行号
			String errorMsg="";			//行错误信息
			String warehouseName = "";		//仓库
			String warehouseCode = "";
			String customerName  = "";		//商家
			String customerId  = "";
			Timestamp createTime = null; 	//库存时间
			//****************************************************************** 仓库校验
			warehouseName = cells.get("仓库");//仓库名称
			if(!StringUtil.isEmpty(warehouseName)){
				if(wareHouseMap.containsKey(warehouseName)){
					warehouseCode = wareHouseMap.get(warehouseName);
				}
				else{
					errorMsg += "仓库【"+warehouseName+"】不存在;";
				}
			}
			
			//****************************************************************** 商家校验
			customerName = cells.get("商家全称");//商家名称
			if(!StringUtil.isEmpty(warehouseName)){
				if(customerMap.containsKey(customerName)){
					customerId = customerMap.get(customerName);
				}
				else{
					errorMsg += "商家【"+customerName+"】不存在;";
				}
			}
			
			//****************************************************************** 日期校验
			String outTimeExcel=cells.get("库存日期");//出库日期
			try {
				createTime = reader.changeValueToTimestamp(outTimeExcel);
				DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String tsStr = sdf.format(createTime);
				reader.getContents().get(rowNo).put("出库日期", tsStr);
			} catch (Exception e) {
				errorMsg += "出库日期【"+outTimeExcel+"】格式不正确;";
			}
			
			//****************************************************************** 校验数量的类型
			String[] strs = {"商品冷冻","商品冷藏","商品常温","商品恒温","耗材冷冻","耗材冷藏","耗材常温","耗材恒温"};
			boolean isAllEmpty=false;
			for (String str : strs) {
				BizProductPalletStorageTempEntity product_entity = new BizProductPalletStorageTempEntity();
				BizPackStorageTempEntity pack_entity = new BizPackStorageTempEntity();
				product_entity.setRowExcelNo(rowNo);
				product_entity.setWarehouseCode(warehouseCode);
				product_entity.setWarehouseName(warehouseName);
				product_entity.setCustomerid(customerId);
				product_entity.setCustomerName(customerName);
				product_entity.setStockTime(createTime);
				product_entity.setCreateTime(createTime);
				product_entity.setIsCalculated("0");
				product_entity.setWriteTime(JAppContext.currentTimestamp());
				product_entity.setDelFlag("0");
				product_entity.setCreator(taskEntity.getCreator());
				product_entity.setTaskId(taskEntity.getTaskId());
				
				
				pack_entity.setRowExcelNo(rowNo);
				pack_entity.setWarehouseCode(warehouseCode);
				pack_entity.setWarehouseName(warehouseName);
				pack_entity.setCustomerid(customerId);
				pack_entity.setCustomerName(customerName);
				pack_entity.setCreateTime(createTime);
				pack_entity.setCurTime(createTime);
				pack_entity.setIsCalculated("0");
				pack_entity.setWriteTime(JAppContext.currentTimestamp());
				pack_entity.setDelFlag("0");
				pack_entity.setCreator(taskEntity.getCreator());
				pack_entity.setTaskId(taskEntity.getTaskId());
				
				if(StringUtils.isNotEmpty(cells.get(str)))
				{
					if(ExportUtil.isNumber(cells.get(str)))
					{
						isAllEmpty=true;
						product_entity.setPalletNum(Double.valueOf(cells.get(str)));//托数
						product_entity.setAdjustPalletNum(Double.valueOf(cells.get(str)));
						pack_entity.setPalletNum(Double.valueOf(cells.get(str)));//托数
						pack_entity.setAdjustPalletNum(Double.valueOf(cells.get(str)));
						if(str.equals("商品冷冻")){
							product_entity.setTemperatureTypeName("冷冻");
							product_entity.setTemperatureTypeCode(temperatureMap.get("冷冻"));
							palletList.add(product_entity);
						}
						else if(str.equals("商品冷藏")){
							product_entity.setTemperatureTypeName("冷藏");
							product_entity.setTemperatureTypeCode(temperatureMap.get("冷藏"));
							palletList.add(product_entity);
						}
						else if(str.equals("商品常温")){
							product_entity.setTemperatureTypeName("常温");
							product_entity.setTemperatureTypeCode(temperatureMap.get("常温"));
							palletList.add(product_entity);
						}
						else if(str.equals("商品恒温")){
							product_entity.setTemperatureTypeName("恒温");
							product_entity.setTemperatureTypeCode(temperatureMap.get("恒温"));
							palletList.add(product_entity);
						}
						else if(str.equals("耗材冷冻")){
							pack_entity.setTemperatureTypeName("冷冻");
							pack_entity.setTemperatureTypeCode(temperatureMap.get("冷冻"));
							packList.add(pack_entity);
						}
						else if(str.equals("耗材冷藏")){
							pack_entity.setTemperatureTypeName("冷藏");
							pack_entity.setTemperatureTypeCode(temperatureMap.get("冷藏"));
							packList.add(pack_entity);
						}
						else if(str.equals("耗材常温")){
							pack_entity.setTemperatureTypeName("常温");
							pack_entity.setTemperatureTypeCode(temperatureMap.get("常温"));
							packList.add(pack_entity);
						}
						else if(str.equals("耗材恒温")){
							pack_entity.setTemperatureTypeName("恒温");
							pack_entity.setTemperatureTypeCode(temperatureMap.get("恒温"));
							packList.add(pack_entity);
						}
						
					}else{
						errorMsg+="列【"+str+"】为非数字;";
					}
				}
				
			}
			if(!isAllEmpty){
				errorMsg+="数值列不能都为空;";
			}

			if(!StringUtil.isEmpty(errorMsg)){
				if(errMap.containsKey(rowNo)){
					errMap.put(rowNo, errMap.get(rowNo)+errorMsg);
				}
				else{
					errMap.put(rowNo, errorMsg);
				}
			}
		}
		
		//重复性校验
         if(palletList!=null&&palletList.size()>0){
        	  checkPallet(palletList);		
         }
  		 if(packList!=null&&packList.size()>0){
  			  checkPack(packList);
  		 }
		
  		//如果excel数据本身存在问题，直接生产结果文件返回给用户
 		if(errMap.size()>0){
 			createResultFile();
 			return false;
 		}
			
		if(errMap.size()==0){
			//如果excel数据本身存在问题，就没有将数据写入临时表的必要
			bizProductPalletStorageTempService.saveBatch(palletList);//保存到临时表
			bizPackStorageTempService.saveBatch(packList);
		}
		logger.info("所有数据写入临时表-成功");
		
		//如果excel数据本身存在问题，直接生产结果文件返回给用户
		if(errMap.size()>0){
			createResultFile();
			return false;
		}
		
		//数据库层面重复校验  false - 校验不通过 存在重复  原则上 商品按托（时间+仓库+商家+温度）只有一条  ，耗材库存（时间+仓库+商家+温度）只有一条  
		if(!dbCheck()){
			createResultFile();
			return false;
		}
		
		if(errMap.size()>0){
			createResultFile();
			return false;
		}		
		logger.info("************ OK **********");
		try{
			int k=saveDataFromTemp(taskEntity.getTaskId());
			if(k>0){
				logger.error("托数从临时表写入业务表成功");
				bmsMaterialImportTaskCommon.setTaskProcess(taskEntity.getTaskId(), 90);
				BmsFileAsynTaskVo updateEntity = new BmsFileAsynTaskVo(taskEntity.getTaskId(), 100,FileAsynTaskStatusEnum.SUCCESS.getCode(), null, JAppContext.currentTimestamp(), null, null, "导入成功");
				bmsFileAsynTaskService.update(updateEntity);
			}else{
				logger.error("未从临时表中保存数据到业务表，批次号【"+taskEntity.getTaskId()+"】,任务编号【"+taskEntity.getTaskId()+"】");
				bmsMaterialImportTaskCommon.setTaskStatus(taskEntity.getTaskId(),99, FileAsynTaskStatusEnum.FAIL.getCode(),"未从临时表中保存数据到业务表，批次号【"+taskEntity.getTaskId()+"】,任务编号【"+taskEntity.getTaskId()+"】");
				bizProductPalletStorageTempService.deleteBybatchNum(taskEntity.getTaskId());
				bizPackStorageTempService.deleteBybatchNum(taskEntity.getTaskId());
			}
		}catch(Exception e){
			logger.error("异步导入异常", e);
			bmsMaterialImportTaskCommon.setTaskStatus(taskEntity.getTaskId(),99, FileAsynTaskStatusEnum.EXCEPTION.getCode(),"从临时表中保存数据到业务表异常");
			bizProductPalletStorageTempService.deleteBybatchNum(taskEntity.getTaskId());
			bizPackStorageTempService.deleteBybatchNum(taskEntity.getTaskId());
		}
		//return;
		
		return false;
	}
	
	private void checkPallet(List<BizProductPalletStorageTempEntity> list){
		//验证导入数据有重复
		List<String> keyList=new ArrayList<String>();
		for(BizProductPalletStorageTempEntity temp:list){
			String key=getPalletKey(temp);
			if(!keyList.contains(key)){//excel数据无重复 验证与数据库对比
				keyList.add(key);
			}else{			
				errMap.put(temp.getRowExcelNo(), "Excel中数据重复");
			}
		}
	}
	
	private void checkPack(List<BizPackStorageTempEntity> list){
		//验证导入数据有重复
		List<String> keyList=new ArrayList<String>();
		for(BizPackStorageTempEntity temp:list){
			String key=getPackKey(temp);
			if(!keyList.contains(key)){//excel数据无重复 验证与数据库对比
				keyList.add(key);
			}else{			
				errMap.put(temp.getRowExcelNo(), "Excel中数据重复");
			}
		}
	}
	private String getPalletKey(BizProductPalletStorageTempEntity dataEntity){
		String key=dataEntity.getStockTime()+dataEntity.getWarehouseCode()+dataEntity.getCustomerid()+dataEntity.getTemperatureTypeCode();
		return key;
	}
	
	private String getPackKey(BizPackStorageTempEntity dataEntity){
		String key=dataEntity.getCurTime()+dataEntity.getWarehouseCode()+dataEntity.getCustomerid()+dataEntity.getTemperatureTypeCode();
		return key;
	}
	
	private int saveDataFromTemp(String taskId){
		int result=bizProductPalletStorageService.saveTempData(taskId);
		result+=bizPackStorageService.saveTempData(taskId);
		
		return result;
	}
	
	private boolean dbCheck(){
		
		List<BizProductPalletStorageTempEntity> productlist = bizProductPalletStorageTempService.queryInBiz(taskEntity.getTaskId());
		Map<String,String> map=Maps.newLinkedHashMap();
		if(productlist.size()> 0){			
			//存在重复记录
			for(BizProductPalletStorageTempEntity entity:productlist){
				String row=String.valueOf(entity.getRowExcelNo());
				String mes="";
				if(map.containsKey(row)){
					mes=map.get(row);
					mes+=",商品库存【"+entity.getStockTime()+"】【"+entity.getWarehouseName()+"】【"+entity.getCustomerName()+"】【"+entity.getTemperatureTypeName()+"】";
					map.put(row,mes);
				}else{
					mes="系统中已存在,商品库存【"+entity.getStockTime()+"】【"+entity.getWarehouseName()+"】【"+entity.getCustomerName()+"】【"+entity.getTemperatureTypeName()+"】";
					map.put(row,mes);
				}
			}
		}
	
		//存在重复记录
		List<BizPackStorageTempEntity> packlist = bizPackStorageTempService.queryInBiz(taskEntity.getTaskId());
		if(productlist.size()> 0){	
			for(BizPackStorageTempEntity entity:packlist){
				String row=String.valueOf(entity.getRowExcelNo());
				String mes="";
				if(map.containsKey(row)){
					mes=map.get(row);
					mes+=",耗材【"+entity.getCurTime()+"】【"+entity.getWarehouseName()+"】【"+entity.getCustomerName()+"】【"+entity.getTemperatureTypeName()+"】";
					map.put(row,mes);
				}else{
					mes="系统中已存在,耗材【"+entity.getCurTime()+"】【"+entity.getWarehouseName()+"】【"+entity.getCustomerName()+"】【"+entity.getTemperatureTypeName()+"】";
					map.put(row,mes);
				}
			}
		}
		
		
		Set<String> set=map.keySet();
		for(String key:set){
			Integer rowNum=Integer.valueOf(key);
			if(errMap.containsKey(rowNum)){
				errMap.put(rowNum, errMap.get(rowNum)+","+map.get(key));
			}else{
				errMap.put(rowNum, map.get(key));
			}
		}
		
		if(errMap.size()>0){
			return false;
		}else{
			return true;
		}
	}
	
	
	/**
	 * 生成结果文件
	 * @throws IOException 
	 */
	private void createResultFile() throws Exception{
		
		if(!StringUtil.isEmpty(taskEntity.getResultFilePath())){
			logger.info("删除历史结果文件");
			boolean resultF = storageClient.deleteFile(taskEntity.getResultFilePath());
			if(resultF){
				logger.info("删除历史结果文件-成功");
			}
			else{
				logger.info("删除历史结果文件-失败");
			}
		}
		
		List<String> exportColumns = new ArrayList<String>();
		
		for (Map.Entry<Integer, String> map : originColumn.entrySet()) {
			exportColumns.add(map.getValue());
		}
		
		POISXSSUtil poiUtil = new POISXSSUtil();
    	SXSSFWorkbook workbook = poiUtil.getXSSFWorkbook();
    	List<Map<String, Object>> headDetailMapList = getBizHead(exportColumns); 
		List<Map<String, Object>> dataDetailList = getBizHeadItem();
		poiUtil.exportExcel2FilePath(poiUtil, workbook, "Sheet1",1, headDetailMapList, dataDetailList);
		
		String resultFullPath="";	
		logger.info("上传结果文件到fastDfs");
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		workbook.write(os);
		byte[] b1 = os.toByteArray();
		StorePath resultStorePath = storageClient.uploadFile(new ByteArrayInputStream(b1), b1.length, "xlsx");
	    resultFullPath = resultStorePath.getFullPath();
	    logger.info("上传结果文件到FastDfs - 成功");
	    BmsFileAsynTaskVo updateEntity = new BmsFileAsynTaskVo(taskEntity.getTaskId(), 99,FileAsynTaskStatusEnum.FAIL.getCode(), null, JAppContext.currentTimestamp(), taskEntity.getOriginFileName(), resultFullPath, REMARK);
		bmsFileAsynTaskService.update(updateEntity);
		
	}
	
	private List<Map<String, Object>> getBizHead(List<String> exportColumns){
		List<Map<String, Object>> headInfoList = new ArrayList<Map<String,Object>>();
		Map<String, Object> itemMap = null;
		for (String colName : exportColumns) {
			itemMap = new HashMap<String, Object>();
			itemMap.put("title", colName);
			itemMap.put("columnWidth", 25);
			itemMap.put("dataKey", colName);
			headInfoList.add(itemMap);
		}
		itemMap = new HashMap<String, Object>();
		itemMap.put("title", "备注");
		itemMap.put("columnWidth", 25);
		itemMap.put("dataKey", "备注");
		headInfoList.add(itemMap);
		return headInfoList;
	}
	
	private List<Map<String, Object>> getBizHeadItem(){
		List<Map<String, Object>> dataList = new ArrayList<Map<String,Object>>();	 
        Map<String, Object> dataItem = null;
        for (Entry<Integer, Map<String, String>> row : reader.getContents().entrySet()) {
        	dataItem = new HashMap<String, Object>();
        	for (Map.Entry<String, String> map : row.getValue().entrySet()) {
				dataItem.put(map.getKey(), map.getValue());
			}
        	if(errMap.containsKey(row.getKey())){
        		dataItem.put("备注", errMap.get(row.getKey()));
        	}
        	dataList.add(dataItem);
        }
        return dataList;
	}
	
	
}


