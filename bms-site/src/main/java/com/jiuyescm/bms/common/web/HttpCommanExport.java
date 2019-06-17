package com.jiuyescm.bms.common.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.bstek.dorado.uploader.DownloadFile;
import com.jiuyescm.bms.common.vo.ExportDataVoEntity;
import com.jiuyescm.common.utils.excel.POIUtil;
import com.jiuyescm.common.utils.upload.DataProperty;

/**
 * 公共导出方法
 * @author Wuliangfeng
 *
 */
public class HttpCommanExport {
	private String path;
	public HttpCommanExport(String path){
		this.path=path;
	}
	public void exportFileAsync(ExportDataVoEntity voEntity) throws Exception{
		try {
        	POIUtil poiUtil = new POIUtil();
        	HSSFWorkbook hssfWorkbook = poiUtil.getHSSFWorkbook();
        	this.appendExcelSheet(poiUtil,hssfWorkbook,path,voEntity);
        	poiUtil.write2FilePath(hssfWorkbook, path);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	public DownloadFile exportFile(ExportDataVoEntity voEntity) throws FileNotFoundException{
		try {
        	POIUtil poiUtil = new POIUtil();
        	HSSFWorkbook hssfWorkbook = poiUtil.getHSSFWorkbook();
        	this.appendExcelSheet(poiUtil,hssfWorkbook,path+File.separator+voEntity.getTitleName()+".xls",voEntity);

		} catch (Exception e) {
			e.printStackTrace();
		}
        
        InputStream is = new FileInputStream(path+File.separator+voEntity.getTitleName()+".xls");
		return new DownloadFile(""+voEntity.getTitleName()+".xls", is);
	}
	
	public DownloadFile exportXFile(ExportDataVoEntity voEntity) throws FileNotFoundException{
		try {
        	POIUtil poiUtil = new POIUtil();
        	HSSFWorkbook hssfWorkbook = poiUtil.getHSSFWorkbook();
        	this.appendExcelSheet(poiUtil,hssfWorkbook,path+File.separator+voEntity.getTitleName()+".xlsx",voEntity);

		} catch (Exception e) {
			e.printStackTrace();
		}
        
        InputStream is = new FileInputStream(path+File.separator+voEntity.getTitleName()+".xlsx");
		return new DownloadFile(""+voEntity.getTitleName()+".xlsx", is);
	}

	public DownloadFile exportFile(String fileName,List<ExportDataVoEntity> exportDataList) throws FileNotFoundException{
		try {
        	POIUtil poiUtil = new POIUtil();
        	HSSFWorkbook hssfWorkbook = poiUtil.getHSSFWorkbook();
        	for(ExportDataVoEntity vo:exportDataList){
        		this.appendExcelSheet(poiUtil,hssfWorkbook,path+File.separator+fileName+".xls",vo);
        	}
		} catch (Exception e) {
			e.printStackTrace();
		}
        
        InputStream is = new FileInputStream(path+File.separator+fileName+".xls");
		return new DownloadFile(""+fileName+".xls", is);
	}
	private void appendExcelSheet(POIUtil poiUtil, HSSFWorkbook hssfWorkbook,String path,ExportDataVoEntity voEntity) throws IOException{
	
		List<DataProperty> dataProps=voEntity.getBaseType().dataProps;
		List<Map<String,Object>> dataVoList=voEntity.getDataList();
		
		List<Map<String, Object>> headInfoList = new ArrayList<Map<String,Object>>();
		Map<String, Object> itemMap = null;
		for(DataProperty dataProp:dataProps){
			itemMap = new HashMap<String, Object>();	
			itemMap.put("title", dataProp.getPropertyName());
	        itemMap.put("columnWidth", 40);
	        itemMap.put("dataKey", dataProp.getPropertyId());
	        headInfoList.add(itemMap);
		}
		List<Map<String, Object>> dataList = new ArrayList<Map<String,Object>>();
        for(Map<String, Object> voMap:dataVoList){
        	dataList.add(voMap);
        }
        poiUtil.exportExcelFilePath(poiUtil,hssfWorkbook,voEntity.getTitleName(),path, headInfoList, dataList);
	}
}
