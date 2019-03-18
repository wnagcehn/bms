package com.jiuyescm.bms.excel.write;

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
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jiuyescm.bms.excel.data.WorkSheet;
import com.jiuyescm.bms.excel.exception.ExcelHandlerException;
import com.jiuyescm.bms.excel.util.ExcelAnnotionUtil;

public class ExportBase implements IExcelExporter{

	protected Workbook workbook;
	protected String defalutPath;
	protected String exportFilePath;
	
	protected Map<String, WorkSheet> sheets;
	
	private Logger logger = LoggerFactory.getLogger(ExportBase.class);
	
	public ExportBase(){
		sheets = new HashMap<String, WorkSheet>();
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
     * @param sheetName  名称（不可重复）
     * @param startIndex 内容开始行
     * @param headInfoList 内容map
     * @throws ExcelHandlerException
     */
	public Sheet createSheet(String sheetName,int startIndex,List<Map<String, Object>> headInfoList) throws ExcelHandlerException{
    	if(headInfoList==null || headInfoList.size() == 0){
    		String msg = "sheet create fail,headInfoList can't be empty";
    		logger.error(msg);
    		throw new ExcelHandlerException(msg);
    	}
    	if(sheets.get(sheetName)!=null){
    		String msg = "sheet create fail,sheet:"+sheetName+" is exist";
    		logger.error(msg);
    		throw new ExcelHandlerException(msg);
    	}
    	else{
    		Sheet sheet = workbook.createSheet(sheetName);
    		WorkSheet workSheet = new WorkSheet(sheetName,startIndex,headInfoList);
    		logger.info("sheet:{} create success",sheetName);
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
    	WorkSheet workSheet = sheets.get(sheet.getSheetName());
		List<Map<String, Object>> headInfoList = workSheet.getHeadInfoList();
        Map<String, Object> headInfo = null;
        int startIndex = workSheet.getNextIndex();
        List<String> vMergeColumn = workSheet.getvMergeColumn();
        Row r = null;
        Cell c = null;
        //处理数据
        Map<String, Object> dataItem = null;
        Object v = null;
        for (int i=0, rownum = startIndex, len = (startIndex + dataList.size()); rownum < len; i++,rownum++){
            r = sheet.createRow(rownum);
            r.setHeightInPoints(16);
            dataItem = dataList.get(i);
            if(vMergeColumn !=null){
            	workSheet.insertRow(sheet,dataItem);
            }
            for(int j=0, jlen = headInfoList.size(); j < jlen; j++){
                headInfo = headInfoList.get(j);
                c = r.createCell(j);
                String colname = headInfo.get("dataKey").toString();
                v = dataItem.get(colname);
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
    
    @Override
    public String saveFile(String filePath,String fileName) throws IOException{
        exportFilePath = filePath+fileName;
        return saveFile();
    }
    
    @Override
    public String saveFile(String fileName) throws IOException{
        exportFilePath = defalutPath+fileName; 
        /*for (Map.Entry<String, WorkSheet> entry : sheets.entrySet()) { 
        	Sheet sheet = workbook.getSheet(entry.getKey());
        	
        }*/
        return saveFile();
    }
    
    protected String saveFile() throws IOException{
    	FileOutputStream fileOut = null;
    	try{
    		fileOut = new FileOutputStream(exportFilePath);
            workbook.write(fileOut);
        }finally{
            if(fileOut != null){
                fileOut.close();
            }
        }
    	logger.info("export success!");
        return exportFilePath;
    }
    
    
    @Override
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

	@Override
	public void setvMergeColumn(Sheet sheet,String columns) {
		WorkSheet workSheet = sheets.get(sheet.getSheetName());
		workSheet.setvMergeColumn(columns);
	}
	
	/**
     * 注解类创建 sheet
     */
	public Sheet createSheetByAnno(String sheetName,int startIndex,Class clazz) throws ExcelHandlerException{
		List<Map<String, Object>> headInfoList = ExcelAnnotionUtil.getTitle(clazz);
		return createSheet(sheetName,startIndex,headInfoList);
    }
	
	/**
	 * 注解类写入内容
	 */
    public void writeContentByAnno(Sheet sheet, List list){
    	List<Map<String, Object>> dataList =ExcelAnnotionUtil.getDataList(list);
    	writeContent(sheet,dataList);
    }
}