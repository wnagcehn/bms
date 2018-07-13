package com.jiuyescm.bms.report.month.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.uploader.DownloadFile;
import com.bstek.dorado.uploader.annotation.FileProvider;
import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.base.dictionary.entity.SystemCodeEntity;
import com.jiuyescm.bms.base.dictionary.service.ISystemCodeService;
import com.jiuyescm.bms.common.constants.FileConstant;
import com.jiuyescm.bms.report.service.IMaterialReportService;
import com.jiuyescm.bms.report.vo.MaterialImportReportVo;
import com.jiuyescm.common.utils.DateUtil;
import com.jiuyescm.common.utils.excel.POISXSSUtil;
import com.jiuyescm.exception.BizException;
import com.jiuyescm.mdm.customer.api.ICustomerService;
import com.jiuyescm.mdm.customer.vo.CustomerVo;
import com.jiuyescm.mdm.warehouse.api.IWarehouseService;
import com.jiuyescm.mdm.warehouse.vo.WarehouseVo;

@Controller("materialImportReportController")
public class MaterialImportReportController {
	
	private static final Logger logger = Logger.getLogger(MaterialImportReportController.class.getName());

	@Resource
	private IMaterialReportService materialReportService;
	@Autowired 
	private ICustomerService customerService;
	@Autowired
	private IWarehouseService warehouseService;
	@Autowired
	private ISystemCodeService systemCodeService;
	
	private Map<String,String> mapWarehouse;
	private Map<String,String> mapCustomer;
	
	@DataProvider
	public List<MaterialImportReportVo> queryImportReport(Map<String,Object> parameter) throws Exception{
		return materialReportService.materialImportReport(parameter);
	}
	
	@FileProvider
	public DownloadFile export(Map<String,Object> param) throws Exception{
		long beginTime = System.currentTimeMillis();
    	logger.info("====耗材差异汇总报表导出：写入Excel begin.");
		try {
			String path = getPath();
			File storeFolder = new File(path);
			if(!storeFolder.isDirectory()){
				storeFolder.mkdirs();
			}
			
			// 如果文件存在直接删除，重新生成
			String fileName = "耗材差异汇总报表" + FileConstant.SUFFIX_XLSX;
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
	    		    	
	    	logger.info("====耗材差异汇总报表导出：写入Excel end.==总耗时：" + (System.currentTimeMillis() - beginTime));

	    	InputStream is = new FileInputStream(filePath);
	    	return new DownloadFile(fileName, is);
		} catch (Exception e) {
			logger.error("导出耗材差异汇总报表失败", e);
		}
		return null;
	}
	
	private void handExport(POISXSSUtil poiUtil, SXSSFWorkbook workbook, 
			String path, Map<String, Object> param)throws Exception{
		//初始化参数
		init();
		
		if (null != param) {
			if (null != param.get("startTime")) {
				param.put("startTime", Timestamp.valueOf(param.get("startTime").toString()));
			}
			if (null != param.get("endTime")) {
				param.put("endTime", Timestamp.valueOf(param.get("endTime").toString()));
			}
		}
		
		int lineNo = 1;
		logger.info("商品存储托数差异报表导出...");
		List<MaterialImportReportVo> list = materialReportService.materialImportReport(param);
		List<Map<String, Object>> headList = getHead();
		List<Map<String, Object>> dataList = getHeadItem(list);
		
		poiUtil.exportExcel2FilePath(poiUtil, workbook, "差异报表", 
				lineNo, headList, dataList);
		
		logger.info("商品存储托数差异报表导出生成sheet。。。");
	}
	
	/**
	 * 初始化需要使用的参数
	 */
	private void init(){
		// 获取仓库
		mapWarehouse = getwarehouse();
		// 查询商家信息
		mapCustomer = getCustomerMap();
	}

	
	public List<Map<String, Object>> getHead(){
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
        itemMap.put("title", "运单量");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "dispatchNum");
        headMapList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "耗材运单量");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "materialNum");
        headMapList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "差异单量");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "diffNum");
        headMapList.add(itemMap);
        
        return headMapList;
	}
	
	private List<Map<String, Object>> getHeadItem(List<MaterialImportReportVo> list){
		 List<Map<String, Object>> dataList = new ArrayList<Map<String,Object>>();
		 Map<String, Object> dataItem = null;
		 for(MaterialImportReportVo entity:list){
			dataItem=new HashMap<String, Object>();
			dataItem.put("customerName", mapCustomer.get(entity.getCustomerId()));
			dataItem.put("warehouseName", mapWarehouse.get(entity.getWarehouseCode()));
			dataItem.put("dispatchNum", entity.getDispatchNum());
			dataItem.put("materialNum", entity.getMaterialNum());
			dataItem.put("diffNum", entity.getDiffNum());
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
