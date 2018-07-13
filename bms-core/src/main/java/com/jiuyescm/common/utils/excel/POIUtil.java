package com.jiuyescm.common.utils.excel;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 * poi 导出excel 工具类
 */
public class POIUtil {
	
	private static final Logger logger = Logger.getLogger(POIUtil.class.getName());

    /**
     * 1.创建 workbook
     * @return
     */
    public HSSFWorkbook getHSSFWorkbook(){
        return new HSSFWorkbook();
    }

    /**
     * 2.创建 sheet
     * @param hssfWorkbook
     * @param sheetName sheet 名称
     * @return
     */
    public HSSFSheet getHSSFSheet(HSSFWorkbook hssfWorkbook, String sheetName){
        return hssfWorkbook.createSheet(sheetName);
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
    public void writeHeader(HSSFWorkbook hssfWorkbook,HSSFSheet hssfSheet ,List<Map<String, Object>> headInfoList){
        HSSFCellStyle cs = hssfWorkbook.createCellStyle();
        HSSFFont font = hssfWorkbook.createFont();
        font.setFontHeightInPoints((short)12);
        font.setBoldweight(font.BOLDWEIGHT_BOLD);
        cs.setFont(font);
        cs.setAlignment(cs.ALIGN_CENTER);

        HSSFRow r = hssfSheet.createRow(0);
        r.setHeight((short) 380);
        HSSFCell c = null;
        Map<String, Object> headInfo = null;
        //处理excel表头
        for(int i=0, len = headInfoList.size(); i < len; i++){
            headInfo = headInfoList.get(i);
            c = r.createCell(i);
            c.setCellValue(headInfo.get("title").toString());
            c.setCellStyle(cs);
            if(headInfo.containsKey("columnWidth")){
                hssfSheet.setColumnWidth(i, (short)(((Integer)headInfo.get("columnWidth") * 8) / ((double) 1 / 20)));
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
    public void writeContent(HSSFSheet hssfSheet ,int startIndex,
                                     List<Map<String, Object>> headInfoList, List<Map<String, Object>> dataList){
        Map<String, Object> headInfo = null;
        HSSFRow r = null;
        HSSFCell c = null;
        //处理数据
        Map<String, Object> dataItem = null;
        Object v = null;
        for (int i=0, rownum = startIndex, len = (startIndex + dataList.size()); rownum < len; i++,rownum++){
            r = hssfSheet.createRow(rownum);
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
                }else if (v instanceof HSSFRichTextString) {
                    c.setCellValue((HSSFRichTextString)v);
                }else {
                    c.setCellValue(v.toString());
                }
            }
        }
    }

    public void write2FilePath(HSSFWorkbook hssfWorkbook, String filePath) throws IOException{
        FileOutputStream fileOut = null;
        try{
            fileOut = new FileOutputStream(filePath);
            hssfWorkbook.write(fileOut);
        }finally{
            if(fileOut != null){
                fileOut.close();
            }
        }
    }


    /**
     * 导出excel
     * code example:
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
         for(int i=0; i < 100; i++){
         dataItem = new HashMap<String, Object>();
         dataItem.put("XH1", "data" + i);
         dataItem.put("XH2", 88888888f);
         dataItem.put("XH3", "脉兜V5..");
         dataList.add(dataItem);
         }
         POIUtil.exportExcel2FilePath("test sheet 1","F:\\temp\\customer2.xls", headInfoList, dataList);

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
    public void exportExcelFilePath(POIUtil poiUtil,HSSFWorkbook hssfWorkbook,String sheetName, String filePath,
	            List<Map<String, Object>> headInfoList,
	            List<Map<String, Object>> dataList) throws IOException {

		HSSFSheet hssfSheet = poiUtil.getHSSFSheet(hssfWorkbook, sheetName);
		//写入 head
		poiUtil.writeHeader(hssfWorkbook, hssfSheet, headInfoList);
		//写入内容
		poiUtil.writeContent(hssfSheet, 1, headInfoList, dataList);
		//保存文件到filePath中
		poiUtil.write2FilePath(hssfWorkbook, filePath);
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
        for(int i=0; i < 100; i++){
        dataItem = new HashMap<String, Object>();
        dataItem.put("XH1", "data" + i);
        dataItem.put("XH2", 88888888f);
        dataItem.put("XH3", "脉兜V5..");
        dataList.add(dataItem);
        }
        try {
        	
        	POIUtil poiUtil = new POIUtil();
        	HSSFWorkbook hssfWorkbook = poiUtil.getHSSFWorkbook();
        	poiUtil.exportExcelFilePath(poiUtil,hssfWorkbook,"test sheet 1","e:\\temp\\customer2.xls", headInfoList, dataList);
        	poiUtil.exportExcelFilePath(poiUtil,hssfWorkbook,"test sheet 2","e:\\temp\\customer2.xls", headInfoList, dataList);
        	
		} catch (IOException e) {
			logger.error("写入文件异常", e);
		}

	}
    
}


