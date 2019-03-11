package com.jiuyescm.bms.excel.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jiuyescm.bms.excel.data.SXSSFWorkSheet;
import com.jiuyescm.bms.excel.exception.ExcelHandlerException;

public class SXSSFExporter {

	private SXSSFWorkbook workbook;
	private String defalutPath;
	private String exportFilePath;
	
	private Map<String, SXSSFWorkSheet> sheets;
	
	//内存中缓存记录默认值
	private static final int rowAccessWindowSize = 1000;
	
	private Logger logger = LoggerFactory.getLogger(SXSSFExporter.class);
	
	/**
	 * 初始化SXSSF导出类
	 * @param startIndex 数据内容默认开始写入的行
	 */
	public SXSSFExporter(){
		workbook = new SXSSFWorkbook(rowAccessWindowSize);
		sheets = new HashMap<String, SXSSFWorkSheet>();
		initTempPath();
		logger.info("export initialize success!");
	}
	
	public SXSSFExporter(int windowSize){
		workbook = new SXSSFWorkbook(windowSize);
		sheets = new HashMap<String, SXSSFWorkSheet>();
		initTempPath();
		logger.info("export initialize success!");
	}
	
	private void initTempPath(){
		String classPath = this.getClass().getClassLoader().getResource("").getPath();
		defalutPath = classPath+"exportTemp/";
		File file = new File(defalutPath);
		if(!file.exists()) {
    		file.mkdirs();
    	}
	}
	
	/**
     * 创建 sheet
     * @param sheetName sheet 名称
     * @return
     */
    public Sheet getXSSFSheet(String sheetName){
    	Sheet sheet = workbook.createSheet(sheetName);
        return sheet;
    }
    
    /**
     * 创建sxssf sheet
     * @param sheetName  名称（不可重复）
     * @param startIndex 内容开始行
     * @param headInfoList 内容map
     * @throws ExcelHandlerException
     */
    public Sheet createSheet(String sheetName,int startIndex,List<Map<String, Object>> headInfoList) throws ExcelHandlerException{
    	if(headInfoList==null || headInfoList.size() == 0){
    		String msg = "sheet create fail,headInfoList can not be empty";
    		logger.error(msg);
    		throw new ExcelHandlerException(msg);
    	}
    	if(sheets.get(sheetName)!=null){
    		String msg = "sheet create fail,sheetName:"+sheetName+" is existed";
    		logger.error(msg);
    		throw new ExcelHandlerException(msg);
    	}
    	else{
    		Sheet sheet = workbook.createSheet(sheetName);
    		SXSSFWorkSheet workSheet = new SXSSFWorkSheet(sheetName,startIndex,headInfoList);
    		logger.info("sheetName:{} create success",sheetName);
    		sheets.put(sheetName, workSheet);
    		writeHeader(sheet,headInfoList);
    		return sheet;
    	}
    }
	
	/**
     * 写入表头信息
     * @param hssfSheet
     * @param headInfoList List<Map<String, Object>>
     *              key: title         列标题
     *                   columnWidth   列宽
     *                   dataKey       列对应的 dataList item key
     */
	@SuppressWarnings("static-access")
	private void writeHeader(Sheet sheet ,List<Map<String, Object>> headInfoList){
        CellStyle cs = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setFontHeightInPoints((short)12);
        font.setBoldweight(font.BOLDWEIGHT_BOLD);
        cs.setFont(font);
        cs.setAlignment(cs.ALIGN_CENTER);
        Row r = sheet.createRow(0);
        r.setHeight((short) 380);
        Cell c = null;
        Map<String, Object> headInfo = null;
        //处理excel表头
        for(int i=0, len = headInfoList.size(); i < len; i++){
            headInfo = headInfoList.get(i);
            c = r.createCell(i);
            c.setCellValue(headInfo.get("title").toString());
            c.setCellStyle(cs);
            if(headInfo.containsKey("columnWidth")){
            	sheet.setColumnWidth(i, (short)(((Integer)headInfo.get("columnWidth") * 8) / ((double) 1 / 20)));
            }
        }
    }
	
	/**
	 * 写入内容
	 * @param sheetName sheet 名称
	 * @param dataList  数据集
	 */
    public void writeContent(Sheet sheet, List<Map<String, Object>> dataList){
    	SXSSFWorkSheet workSheet = sheets.get(sheet.getSheetName());
		List<Map<String, Object>> headInfoList = workSheet.getHeadInfoList();
        Map<String, Object> headInfo = null;
        int startIndex = workSheet.getNextIndex();
        Row r = null;
        Cell c = null;
        //处理数据
        Map<String, Object> dataItem = null;
        Object v = null;
        for (int i=0, rownum = startIndex, len = (startIndex + dataList.size()); rownum < len; i++,rownum++){
            r = sheet.createRow(rownum);
            r.setHeightInPoints(16);
            dataItem = dataList.get(i);
            for(int j=0, jlen = headInfoList.size(); j < jlen; j++){
                headInfo = headInfoList.get(j);
                c = r.createCell(j);
                v = dataItem.get(headInfo.get("dataKey").toString());
                if(v == null){
                	v = "";
                }
                if (v instanceof String) {
                    c.setCellValue((String)v);
                }else if (v instanceof Boolean) {
                    c.setCellValue((Boolean)v);
                }else if (v instanceof Calendar) {
                    c.setCellValue((Calendar)v);
                }else if (v instanceof Double) {
                    c.setCellValue((Double)v);
                }else if (v instanceof Integer
                        || v instanceof Long
                        || v instanceof Short
                        || v instanceof Float) {
                    c.setCellValue(Double.parseDouble(v.toString()));
                }else if (v instanceof XSSFRichTextString) {
                    c.setCellValue((XSSFRichTextString)v);
                }else {
                    c.setCellValue(v.toString());
                }
            } 
        }
        startIndex += dataList.size();
        workSheet.setNextIndex(startIndex);
    }
    
    
    /**
     * 保存excel文件
     * @param filePath 文件路径 /usr/local/
     * @param fileName 文件名
     * @return
     * @throws IOException
     */
    public String saveFile(String filePath,String fileName) throws IOException{
        exportFilePath = filePath+fileName;
        return saveFile();
    }
    
    /**
     * 保存excel文件  默认文路径为类加载路径(/classes/)
     * @param fileName 文件名
     * @return
     * @throws IOException
     */
    public String saveFile(String fileName) throws IOException{
        exportFilePath = defalutPath+fileName;
        return saveFile();
    }
    
    private String saveFile() throws IOException{
    	FileOutputStream fileOut = null;
    	try{
    		fileOut = new FileOutputStream(exportFilePath);
            workbook.write(fileOut);
            workbook.dispose();
        }finally{
            if(fileOut != null){
                fileOut.close();
            }
        }
    	logger.info("export success!");
        return exportFilePath;
    }
    
    /**
     * 删除导出文件
     */
    public void delTempFile(){
    	File file = new File(exportFilePath); 
    	if (file.exists() && file.isFile()) {  
			if (file.delete()) {  
				logger.info("local file delete success!");
            } else {  
            	logger.info("local file delete file!");
            }  
		}
    }
}
