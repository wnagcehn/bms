package com.jiuyescm.bms.report.biz.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
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
import com.jiuyescm.bms.billcheck.vo.BillCheckInfoVo;
import com.jiuyescm.bms.common.constants.FileConstant;
import com.jiuyescm.bms.common.enumtype.type.BizTypeEnum;
import com.jiuyescm.bms.report.biz.service.IBizWarehouseImportReportService;
import com.jiuyescm.bms.report.vo.BizWarehouseImportReportVo;
import com.jiuyescm.bms.report.vo.BizWarehouseNotImportVo;
import com.jiuyescm.common.utils.excel.POISXSSUtil;
import com.jiuyescm.exception.BizException;
import com.jiuyescm.mdm.warehouse.api.IWarehouseService;
import com.jiuyescm.mdm.warehouse.vo.WarehouseVo;

/**
 * 各仓导入数据报表
 * @author yangss
 */
@Controller("bizWarehouseImportReportController")
public class BizWarehouseImportReportController {

	private static final Logger logger = Logger.getLogger(BizWarehouseImportReportController.class.getName());

	@Autowired
	private IWarehouseService warehouseService;
	@Autowired
	private IBizWarehouseImportReportService bizWarehouseImportReportService;
	@Autowired
	private ISystemCodeService systemCodeService;
	
	private Map<String,String> mapWarehouse;
	private Map<String,String> mapBizType;
	final int pageSize = 2000;

	@DataProvider
	public void query(Page<BizWarehouseImportReportVo> page, Map<String, Object> parameter) {
		//解决首次进页面importDate传递不准确问题
		int month = (int) parameter.get("month");
		int year = (int) parameter.get("year");
		String importDate = "";
		if(month<10){
			importDate=year+"-0"+month;
		}else{
			importDate=year+"-"+month;
		}
		parameter.put("importDate", importDate);
		
		PageInfo<BizWarehouseImportReportVo> tmpPageInfo = bizWarehouseImportReportService
				.query(parameter, page.getPageNo(), page.getPageSize());
		if (tmpPageInfo != null) {
			page.setEntities(tmpPageInfo.getList());
			page.setEntityCount((int) tmpPageInfo.getTotal());
		}
	}
	
	@FileProvider
	public DownloadFile export(Map<String,Object> param) throws Exception{
		long beginTime = System.currentTimeMillis();
		logger.info("====各仓导入数据报表导出：");
		
		String path = getPath();
		File storeFolder = new File(path);
		if(!storeFolder.isDirectory()){
			storeFolder.mkdirs();
		}
    	String fileName = "各仓导入数据报表" + FileConstant.SUFFIX_XLSX;
    	String filePath = path + FileConstant.SEPARATOR + fileName;
		File file = new File(filePath);
		if (file.exists()) {
			file.delete();
		}
		
    	logger.info("====各仓导入数据报表导出：写入Excel begin.");
		try {
			POISXSSUtil poiUtil = new POISXSSUtil();
	    	SXSSFWorkbook workbook = poiUtil.getXSSFWorkbook();
	    		    		    	
	        // 导出
	    	handExport(poiUtil, workbook, filePath, param);
	    	
	    	//最后写到文件
	    	poiUtil.write2FilePath(workbook, filePath);
	    		    	
	    	logger.info("====各仓导入数据报表导出：写入Excel end.==总耗时：" + (System.currentTimeMillis() - beginTime));

	    	InputStream is = new FileInputStream(filePath);
	    	return new DownloadFile(fileName, is);
		} catch (Exception e) {
			logger.error("各仓导入数据报表失败", e);
		}
		return null;
	}
	
	private void handExport(POISXSSUtil poiUtil, SXSSFWorkbook workbook, 
			String path, Map<String, Object> param)throws Exception{
		// 获取仓库
		mapWarehouse = getwarehouse();
		mapBizType = getBizTypeMap();
		
		logger.info("各仓导入数据报表导出...");
        List<BizWarehouseImportReportVo> dataList = bizWarehouseImportReportService.queryAll(param);
		if(null == dataList || dataList.size() <= 0){
			return;
		}
		
		logger.info("各仓导入数据报表导出生成sheet。。。");
		Sheet sheet = poiUtil.getXSSFSheet(workbook,"各仓导入数据");
		
		sheet.setColumnWidth(0, 3500);
		sheet.setColumnWidth(1, 4500);
		sheet.setColumnWidth(2, 5000);
		sheet.setColumnWidth(3, 4000);
		sheet.setColumnWidth(4, 4000);	
		sheet.setColumnWidth(5, 4000);	
		
		Font font = workbook.createFont();
	    font.setBoldweight(Font.BOLDWEIGHT_BOLD);
		CellStyle style = workbook.createCellStyle();
		style.setAlignment(CellStyle.ALIGN_CENTER);
		style.setWrapText(true);
		style.setFont(font);
		 
		Row row0 = sheet.createRow(0);
		Cell cell0 = row0.createCell(0);
		cell0.setCellValue("日期");
		cell0.setCellStyle(style);
		Cell cell1 = row0.createCell(1);
		cell1.setCellValue("仓库名称");
		cell1.setCellStyle(style);
		Cell cell2 = row0.createCell(2);
		cell2.setCellStyle(style);
		cell2.setCellValue("业务类型");
		Cell cell3 = row0.createCell(3);
		cell3.setCellValue("应导入商家数");
		cell3.setCellStyle(style);
		Cell cell4 = row0.createCell(4);
		cell4.setCellValue("实际导入商家数");
		cell4.setCellStyle(style);
		Cell cell5 = row0.createCell(5);
		cell5.setCellValue("未导入商家数");
		cell5.setCellStyle(style);
		 
		logger.info("各仓导入数据报表导出给sheet赋值。。。");
		int RowIndex = 1;
		for(int i=0;i<dataList.size();i++){	
			BizWarehouseImportReportVo entity = dataList.get(i);
			Row row = sheet.createRow(RowIndex);
			RowIndex++;
			Cell cel0 = row.createCell(0);
			cel0.setCellValue(entity.getImportDate());
			Cell cel1 = row.createCell(1);
			cel1.setCellValue(mapWarehouse.get(entity.getWarehouseCode()));
			Cell cel2 = row.createCell(2);
			cel2.setCellValue(mapBizType.get(entity.getBizType()));
			Cell cel3 = row.createCell(3);
			cel3.setCellValue(entity.getTheoryNum());
			Cell cel4 = row.createCell(4);
			cel4.setCellValue(entity.getActualNum());
			Cell cel5 = row.createCell(5);
			cel5.setCellValue(entity.getMinusNum());
		}
	}
	
	@DataProvider
	public Map<String,String> getBizTypeMap(){
		return BizTypeEnum.getMap();
	}
	
	private Map<String,String> getwarehouse(){
		List<WarehouseVo> warehouseVos = warehouseService.queryAllWarehouse();
		Map<String, String> map = new LinkedHashMap<String,String>();
		for (WarehouseVo warehouseVo : warehouseVos) {
			map.put(warehouseVo.getWarehouseid(), warehouseVo.getWarehousename());
		}
		return map;
	}
	
	private String getPath(){
		SystemCodeEntity systemCodeEntity = systemCodeService.getSystemCode("GLOABL_PARAM", "EXPORT_WAREHOUSE_IMPORT");
		if(systemCodeEntity == null){
			throw new BizException("请在系统参数中配置文件上传路径,参数GLOABL_PARAM,EXPORT_PRODUCT_PALLET_DIFF");
		}
		return systemCodeEntity.getExtattr1();
	}
	
	//查询未导入商家
	@DataProvider
	public void queryNotImport(Page<BizWarehouseNotImportVo> page, Map<String, Object> param) {
		if (param == null){
			param = new HashMap<String, Object>();
		}
		PageInfo<BizWarehouseNotImportVo> pageInfo = bizWarehouseImportReportService.queryNotImport(param, page.getPageNo(), page.getPageSize());
		if (pageInfo != null) {
			page.setEntities(pageInfo.getList());
			page.setEntityCount((int) pageInfo.getTotal());
		}
	}

}
