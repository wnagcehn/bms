package com.jiuyescm.bms.report.biz.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.data.provider.Page;
import com.bstek.dorado.uploader.DownloadFile;
import com.bstek.dorado.uploader.annotation.FileProvider;
import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.base.dictionary.entity.SystemCodeEntity;
import com.jiuyescm.bms.base.dictionary.service.ISystemCodeService;
import com.jiuyescm.bms.biz.storage.entity.BizProductPalletStorageEntity;
import com.jiuyescm.bms.biz.storage.service.IBizProductPalletStorageService;
import com.jiuyescm.bms.common.constants.FileConstant;
import com.jiuyescm.common.ConstantInterface;
import com.jiuyescm.common.utils.excel.POISXSSUtil;
import com.jiuyescm.exception.BizException;
import com.jiuyescm.mdm.customer.api.ICustomerService;
import com.jiuyescm.mdm.customer.vo.CustomerVo;
import com.jiuyescm.mdm.warehouse.api.IWarehouseService;
import com.jiuyescm.mdm.warehouse.vo.WarehouseVo;

/**
 * 商品存储托数差异报表
 * @author yangss
 */
@Controller("productStorageDiffReportController")
public class ProductStorageDiffReportController {

	private static final Logger logger = Logger.getLogger(ProductStorageDiffReportController.class.getName());

	@Autowired 
	private ICustomerService customerService;
	
	@Autowired
	private IWarehouseService warehouseService;
	
	@Autowired
	private ISystemCodeService systemCodeService;
	
	@Autowired
	private IBizProductPalletStorageService productPalletStorageService;

	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	@DataProvider
	public void query(Page<BizProductPalletStorageEntity> page, Map<String, Object> parameter) {
		if (null == parameter) {
			parameter = new HashMap<String, Object>();
			parameter.put("isDifferent", null);
		}
		
		PageInfo<BizProductPalletStorageEntity> tmpPageInfo = productPalletStorageService
				.queryStorageDiff(parameter, page.getPageNo(), page.getPageSize());
		if (tmpPageInfo != null) {
			page.setEntities(tmpPageInfo.getList());
			page.setEntityCount((int) tmpPageInfo.getTotal());
		}
	}
	
	private Map<String,String> mapWarehouse;
	private Map<String,String> mapCustomer;
	private Map<String, String> temperatureMap;
	private Map<String, String> differentMap;
	
	@FileProvider
	public DownloadFile export(Map<String,Object> param) throws Exception{
		long beginTime = System.currentTimeMillis();
    	logger.info("====商品存储托数差异报表导出：写入Excel begin.");
		try {
			String path = getPath();
			File storeFolder = new File(path);
			if(!storeFolder.isDirectory()){
				storeFolder.mkdirs();
			}
			
			// 如果文件存在直接删除，重新生成
			String fileName = "商品存储托数差异报表" + FileConstant.SUFFIX_XLSX;
			String filePath = path + FileConstant.SEPARATOR + fileName;
			File file = new File(filePath);
			if (file.exists()) {
				file.delete();
			}
			
			POISXSSUtil poiUtil = new POISXSSUtil();
	    	SXSSFWorkbook workbook = poiUtil.getXSSFWorkbook();
	    		    		    	
	        // 导出
	    	handExport(poiUtil, workbook, filePath, param);
	    	//最后写到文件
	    	poiUtil.write2FilePath(workbook, filePath);
	    		    	
	    	logger.info("====商品存储托数差异报表导出：写入Excel end.==总耗时：" + (System.currentTimeMillis() - beginTime));

	    	InputStream is = new FileInputStream(filePath);
	    	return new DownloadFile(fileName, is);
		} catch (Exception e) {
			logger.error("导出商品存储托数差异报表失败", e);
		}
		return null;
	}
	
	/**
	 * 初始化需要使用的参数
	 */
	private void init(){
		// 获取仓库
		mapWarehouse = getwarehouse();
		// 查询商家信息
		mapCustomer = getCustomerMap();
		
		 // 温度类型
        List<SystemCodeEntity> systemCodeList = systemCodeService.findEnumList("TEMPERATURE_TYPE");
		temperatureMap =new LinkedHashMap<String,String>();
		if(systemCodeList!=null && systemCodeList.size()>0){
			for(int i=0;i<systemCodeList.size();i++){
				temperatureMap.put(systemCodeList.get(i).getCode(), systemCodeList.get(i).getCodeName());
			}
		}
		// 是否差异
		differentMap = new LinkedHashMap<String, String>();
		differentMap.put(ConstantInterface.Issplitboxflag.YES, "是");
		differentMap.put(ConstantInterface.Issplitboxflag.NO, "否");
	}
	
	private void handExport(POISXSSUtil poiUtil, SXSSFWorkbook workbook, 
			String path, Map<String, Object> param)throws Exception{
		//初始化参数
		init();
				
		int pageNo = 1;
		int lineNo = 1;
		boolean doLoop = true;
		logger.info("商品存储托数差异报表导出...");
		
        while (doLoop) {
			PageInfo<BizProductPalletStorageEntity> pageInfo=new PageInfo<BizProductPalletStorageEntity>();
			if (param == null){
				param = new HashMap<String, Object>();
				param.put("isDifferent", null);
			}
				
			pageInfo= productPalletStorageService.queryStorageDiff(param, pageNo, FileConstant.PAGESIZE);
			if (null != pageInfo && pageInfo.getList().size() > 0) {
				if (pageInfo.getList().size() < FileConstant.PAGESIZE) {
					doLoop = false;
				}else {
					pageNo += 1; 
				}
				
				List<Map<String, Object>> headList = getHead();
				List<Map<String, Object>> dataList = getHeadItem(pageInfo.getList());
				
				poiUtil.exportExcel2FilePath(poiUtil, workbook, "差异报表", 
						lineNo, headList, dataList);
				if (null != pageInfo && pageInfo.getList().size() > 0) {
					lineNo += pageInfo.getList().size();
				}
			}else {
				doLoop = false;
			}
		}
		
		logger.info("商品存储托数差异报表导出生成sheet。。。");
	}
	
	public List<Map<String, Object>> getHead(){
		List<Map<String, Object>> headMapList=new ArrayList<Map<String, Object>>();
		Map<String, Object> itemMap = new HashMap<String, Object>();
		
    	itemMap.put("title", "库存日期");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "stockTime");
        headMapList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "仓库ID");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "warehouseCode");
        headMapList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "仓库名称");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "warehouseName");
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
        itemMap.put("title", "温度类型");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "temperatureTypeName");
        headMapList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "商品件数");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "aqty");
        headMapList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "托数");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "palletNum");
        headMapList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "是否差异");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "isDifferent");
        headMapList.add(itemMap);
        
        return headMapList;
	}
	
	private List<Map<String, Object>> getHeadItem(List<BizProductPalletStorageEntity> list){
		 List<Map<String, Object>> dataList = new ArrayList<Map<String,Object>>();
		 Map<String, Object> dataItem = null;
		 for(BizProductPalletStorageEntity entity:list){
			dataItem=new HashMap<String, Object>();
			dataItem.put("stockTime", sdf.format(entity.getStockTime()));
			dataItem.put("warehouseCode", entity.getWarehouseCode());
			dataItem.put("warehouseName", mapWarehouse.get(entity.getWarehouseCode()));
			dataItem.put("customerId", entity.getCustomerId());
			dataItem.put("customerName", mapCustomer.get(entity.getCustomerId()));
			dataItem.put("temperatureTypeName", temperatureMap.get(entity.getTemperatureTypeCode()));
			dataItem.put("aqty", entity.getAqty());
			dataItem.put("palletNum", entity.getPalletNum());
			dataItem.put("isDifferent", differentMap.get(entity.getIsDifferent()));
			dataList.add(dataItem);
		 }
		 return dataList;
	}
	
	private Map<String,String> getwarehouse(){
		List<WarehouseVo> warehouseVos = warehouseService.queryAllWarehouse();
		Map<String, String> map = new LinkedHashMap<String,String>();
		for (WarehouseVo warehouseVo : warehouseVos) {
			map.put(warehouseVo.getWarehouseid(), warehouseVo.getWarehousename());
		}
		return map;
	}
	
	private Map<String, String> getCustomerMap(){
		Map<String,Object> parameter =new HashMap<String,Object>();
		
		PageInfo<CustomerVo> tmpPageInfo = customerService.query(parameter, 0, Integer.MAX_VALUE);
		Map<String, String> map =new LinkedHashMap<String,String>();
		if(tmpPageInfo !=null && tmpPageInfo.getTotal()>0){
			Iterator<CustomerVo> iter = tmpPageInfo.getList().iterator();
			while(iter.hasNext()){
				CustomerVo cvo = (CustomerVo) iter.next();
				map.put(cvo.getCustomerid(), cvo.getCustomername());
			}
		}
		return map;
	}
	
	private String getPath(){
		SystemCodeEntity systemCodeEntity = systemCodeService.getSystemCode("GLOABL_PARAM", "EXPORT_PRODUCT_PALLET_DIFF");
		if(systemCodeEntity == null){
			throw new BizException("请在系统参数中配置文件上传路径,参数GLOABL_PARAM,EXPORT_PRODUCT_PALLET_DIFF");
		}
		return systemCodeEntity.getExtattr1();
	}

}
