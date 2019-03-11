package com.jiuyescm.common.utils.excel;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;

/**
 * poi 导出excel 工具类
 */
public class POISXSSUtil {
	
	private int row =1;
	
    public synchronized void exportExcel2FilePath(POISXSSUtil poiUtil,SXSSFWorkbook xssfWorkbook,String sheetName,
             List<Map<String, Object>> headInfoList,
            List<Map<String, Object>> dataList) throws IOException {
    	Sheet xssfSheet = xssfWorkbook.getSheet(sheetName);
    	if (null != xssfSheet) {
    		//写入内容
			poiUtil.writeContent(xssfSheet, row, headInfoList, dataList);
		}else {
			xssfSheet = poiUtil.getXSSFSheet(xssfWorkbook, sheetName);
			//写入 head
			poiUtil.writeHeader(xssfWorkbook, xssfSheet, headInfoList);
			//写入内容
			poiUtil.writeContent(xssfSheet, row, headInfoList, dataList);
		}
		row+=dataList.size();
	}
	
	private static final Logger logger = Logger.getLogger(POISXSSUtil.class.getName());
	
	//内存中缓存记录数
	private static final int rowAccessWindowSize = 1000;

    /**
     * 1.创建 workbook
     * @return
     */
    public SXSSFWorkbook getXSSFWorkbook(){
        return new SXSSFWorkbook(rowAccessWindowSize);
    }
    
    /**
     * 2.创建 sheet
     * @param hssfWorkbook
     * @param sheetName sheet 名称
     * @return
     */
    public Sheet getXSSFSheet(SXSSFWorkbook xssfWorkbook, String sheetName){
        return xssfWorkbook.createSheet(sheetName);
    }

    /**
     * 3.写入表头信息
     * @param hssfWorkbook
     * @param hssfSheet
     * @param headInfoList List<Map<String, Object>>
     *              key: title         列标题
     *                   columnWidth   列宽
     *                   dataKey       列对应的 dataList item key
     */
    @SuppressWarnings("static-access")
	public void writeHeader(SXSSFWorkbook xssfWorkbook,Sheet xssfSheet ,List<Map<String, Object>> headInfoList){
        CellStyle cs = xssfWorkbook.createCellStyle();
        Font font = xssfWorkbook.createFont();
        font.setFontHeightInPoints((short)12);
        font.setBoldweight(font.BOLDWEIGHT_BOLD);
        cs.setFont(font);
        cs.setAlignment(cs.ALIGN_CENTER);

        Row r = xssfSheet.createRow(0);
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
                xssfSheet.setColumnWidth(i, (short)(((Integer)headInfo.get("columnWidth") * 8) / ((double) 1 / 20)));
            }
        }
    }

    /**
     * 4.写入内容部分
     * @param hssfSheet
     * @param startIndex 从1开始，多次调用需要加上前一次的dataList.size()
     * @param headInfoList List<Map<String, Object>>
     *              key: title         列标题
     *                   columnWidth   列宽
     *                   dataKey       列对应的 dataList item key
     * @param dataList
     */
    public void writeContent(Sheet xssfSheet ,int startIndex,
                                     List<Map<String, Object>> headInfoList, List<Map<String, Object>> dataList){
        Map<String, Object> headInfo = null;
        Row r = null;
        Cell c = null;
        //处理数据
        Map<String, Object> dataItem = null;
        Object v = null;
        for (int i=0, rownum = startIndex, len = (startIndex + dataList.size()); rownum < len; i++,rownum++){
            r = xssfSheet.createRow(rownum);
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
    }

    public void writeFilePath(SXSSFWorkbook xssfWorkbook, String filePath) throws IOException{
        FileOutputStream fileOut = new FileOutputStream(filePath);
        xssfWorkbook.write(fileOut);
        if(fileOut != null){
            fileOut.close();
        }
    }
    
    public void write2FilePath(SXSSFWorkbook xssfWorkbook, String filePath) throws IOException{
        FileOutputStream fileOut = null;
        try{
            fileOut = new FileOutputStream(filePath);
            xssfWorkbook.write(fileOut);
        }finally{
            if(fileOut != null){
                fileOut.close();
            }
        }
    }


    /**
     * 导出excel
     * code example:
     * @param poiUtil   工具类对象
     * @param hssfWorkbook   excel对象
     * @param sheetName   sheet名称
     * @param filePath   文件存储路径， 如：f:/a.xls
     * @param headInfoList List<Map<String, Object>>
     *                           key: title         列标题
     *                                columnWidth   列宽
     *                                dataKey       列对应的 dataList item key
     * @param dataList  List<Map<String, Object>> 导出的数据
     * @throws java.io.IOException
     *
     */
    public void exportExcelFilePath(POISXSSUtil poiUtil,SXSSFWorkbook xssfWorkbook,String sheetName,
            List<Map<String, Object>> headInfoList,
            List<Map<String, Object>> dataList) throws IOException {
		Sheet xssfSheet = poiUtil.getXSSFSheet(xssfWorkbook, sheetName);
		//写入 head
		poiUtil.writeHeader(xssfWorkbook, xssfSheet, headInfoList);
		//写入内容
		poiUtil.writeContent(xssfSheet, 1, headInfoList, dataList);
	}

    public synchronized void exportExcel2FilePath(POISXSSUtil poiUtil,SXSSFWorkbook xssfWorkbook,String sheetName,
            int startIndex, List<Map<String, Object>> headInfoList,
            List<Map<String, Object>> dataList) throws IOException {
    	Sheet xssfSheet = xssfWorkbook.getSheet(sheetName);
    	if (null != xssfSheet) {
    		//写入内容
			poiUtil.writeContent(xssfSheet, startIndex, headInfoList, dataList);
		}else {
			xssfSheet = poiUtil.getXSSFSheet(xssfWorkbook, sheetName);
			//写入 head
			poiUtil.writeHeader(xssfWorkbook, xssfSheet, headInfoList);
			//写入内容
			poiUtil.writeContent(xssfSheet, 1, headInfoList, dataList);
		}
	}
    
    /**  
     * @获取Excel中某个单元格的值 
     * @param cell      EXCLE单元格对象 
     * @param evaluator EXCLE单元格公式 
     * @return          单元格内容 
     */  
    public static String getValue(Cell cell,FormulaEvaluator evaluator) {   
        String value = "";  
        if(cell==null)
        	return value;
        switch (cell.getCellType()) {  
            case HSSFCell.CELL_TYPE_NUMERIC:                        //数值型  
                if (HSSFDateUtil.isCellDateFormatted(cell)) {       //如果是时间类型  
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
                    value = format.format(cell.getDateCellValue());  
                } else { 
                	cell.setCellType(Cell.CELL_TYPE_STRING);  
                	String temp = cell.getStringCellValue();  
					if(temp.indexOf(".")>-1){  
						value = String.valueOf(new Double(temp)).trim();  
					}else{  
						value = temp.trim();  
					}  
                	 //纯数字  
                    value = cell.toString();
                }  
                break;  
            case HSSFCell.CELL_TYPE_STRING:                         //字符串型  
                value = cell.getStringCellValue();  
                break;  
            case HSSFCell.CELL_TYPE_BOOLEAN:                        //布尔  
                value = " " + cell.getBooleanCellValue();  
                break;  
            case HSSFCell.CELL_TYPE_BLANK:                          //空值  
                value = "";  
                break;  
            case HSSFCell.CELL_TYPE_ERROR:                          //故障  
                value = "";  
                break;  
            case HSSFCell.CELL_TYPE_FORMULA:                        //公式型  
                try {  
                    CellValue cellValue;  
                    cellValue = evaluator.evaluate(cell);  
                    switch (cellValue.getCellType()) {              //判断公式类型  
                        case Cell.CELL_TYPE_BOOLEAN:  
                            value  = String.valueOf(cellValue.getBooleanValue());  
                            break;  
                        case Cell.CELL_TYPE_NUMERIC:  
                            // 处理日期    
                            if (DateUtil.isCellDateFormatted(cell)) {    
                               SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");    
                               Date date = cell.getDateCellValue();    
                               value = format.format(date);  
                            } else {    
                               value  = String.valueOf(cellValue.getNumberValue());  
                            }  
                            break;  
                        case Cell.CELL_TYPE_STRING:  
                            value  = cellValue.getStringValue();  
                            break;  
                        case Cell.CELL_TYPE_BLANK:  
                            value = "";  
                            break;  
                        case Cell.CELL_TYPE_ERROR:  
                            value = "";  
                            break;  
                        case Cell.CELL_TYPE_FORMULA:  
                            value = "";  
                            break;  
                    }  
                } catch (Exception e) {  
                    value = cell.getStringCellValue().toString();  
                    cell.getCellFormula(); 
                    logger.error("获取Excel中某个单元格的值异常", e);
                }  
                break;  
            default:  
                value = cell.getStringCellValue().toString();  
                break;  
        }  
        return value;  
    }

    public static void main(String[] args) {
    	
    	List<Map<String, Object>> headInfoList = new ArrayList<Map<String,Object>>();
        Map<String, Object> itemMap = new HashMap<String, Object>();
        itemMap.put("title", "序号1");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "XH1");
        headInfoList.add(itemMap);

        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "序号2");
        itemMap.put("columnWidth", 50);
        itemMap.put("dataKey", "XH2");
        headInfoList.add(itemMap);

        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "序号3");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "XH3");
        headInfoList.add(itemMap);

        List<Map<String, Object>> dataList = new ArrayList<Map<String,Object>>();
        Map<String, Object> dataItem = null;
        for(int i=0; i < 10; i++){
        dataItem = new HashMap<String, Object>();
        dataItem.put("XH1", "data" + i);
        dataItem.put("XH2", 88888888f);
        dataItem.put("XH3", "脉兜V5..");
        dataList.add(dataItem);
        }
        try {
        	POISXSSUtil poiUtil = new POISXSSUtil();
        	SXSSFWorkbook hssfWorkbook = poiUtil.getXSSFWorkbook();
        	poiUtil.exportExcel2FilePath(poiUtil,hssfWorkbook,"test sheet 1",1, headInfoList, dataList);
        	poiUtil.exportExcel2FilePath(poiUtil,hssfWorkbook,"test sheet 1",dataList.size()+1, headInfoList, dataList);
//        	poiUtil.exportExcelFilePath(poiUtil,hssfWorkbook,"test sheet 2","e:\\tmp\\customer2.xlsx", headInfoList, dataList);
        	poiUtil.write2FilePath(hssfWorkbook, "e:\\my-test.xlsx");
		} catch (IOException e) {
			logger.error("写入文件异常", e);
		}

	}
}


