package com.jiuyescm.bs.util;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.bstek.dorado.uploader.UploadFile;

public class ExportUtil {
	
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
                } else {                                            //纯数字  
                    cell.setCellType(Cell.CELL_TYPE_STRING);  
					String temp = cell.getStringCellValue();  
					if(temp.indexOf(".")>-1){  
						value = String.valueOf(new Double(temp)).trim();  
					}else{  
						value = temp.trim();  
					}  
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
                }  
                break;  
            default:  
                value = cell.getStringCellValue().toString();  
                break;  
        }  
        return value;  
    } 
   
    /**
     * 获取excel头是否和给定的字符串一致
     * @param file
     * @param str
     * @return
     */
   public static boolean checkTitle(UploadFile file,String[] str){
    	
		if(file.getFileName().contains("xlsx"))
    	{
    		try {
    			XSSFWorkbook xssfWorkbook = new XSSFWorkbook(file.getInputStream());
    			XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(0);
    			if(xssfSheet==null)
    				return false;
    			XSSFRow xssfRow = xssfSheet.getRow(0);
    			
    			if(xssfRow.getPhysicalNumberOfCells()<str.length)
    			  return false;
    			for(int i = 0;i<str.length;i++){
    				
    				if(!str[i].equals(xssfRow.getCell(i).getStringCellValue())){
    					return false;
    				}
    				
    			}
    			
    		} catch (IOException e) {
    			return false;
    		}
    	}else{
    		
    		try {
				HSSFWorkbook workbook = new HSSFWorkbook(file.getInputStream());
				
				HSSFSheet xssfSheet = workbook.getSheetAt(0);
				
				if(xssfSheet==null)
    				return false;
				
				HSSFRow xssfRow = xssfSheet.getRow(0);
				 
				if(xssfRow.getPhysicalNumberOfCells()<str.length)
				      return false;
				
				for(int i = 0;i<str.length;i++)
				{
					if(!str[i].equals(xssfRow.getCell(i).getStringCellValue()))
					{
						return false;
					}
				}
				
			} catch (IOException e) {
				return false;
			}
			    		
    	}
    	
    	return true;
    	
    }
   /**
    * 去掉不必要的换行符等
    * @param str
    * @return
    */
   public static String replaceBlank(String str)
   {
       String dest = "";
       if (str!=null) {
           Pattern p = Pattern.compile("\\s*|\t|\r|\n");
           Matcher m = p.matcher(str);
           dest = m.replaceAll("");
       }
       return dest;
   }
   
   /**
    * 是否数字
    */
   public static boolean isNumber(String str){  
       String reg = "^-?[0-9]+(.[0-9]+)?$";  
       return str.matches(reg);  
   } 
   
   public static Map<String,String>  getMaterialMap(){
		Map<String,String>  map = new  HashMap<String,String>();
		map.put("ZX", "纸箱");
		map.put("PMX", "泡沫箱");
		map.put("BD", "冰袋");
		map.put("GB", "干冰");
		map.put("FSD", "防水袋");
		map.put("BWD", "保温袋");

		map.put("LCB", "冷藏标");
		map.put("LDB", "冷冻标");
		map.put("QPD", "气泡袋");
		map.put("QPM", "气泡膜");
		map.put("QZD", "气柱袋");
		map.put("MD", "面单");
		
		map.put("WT", "网套");
		map.put("CRM", "缠绕膜");
		map.put("HLQ", "葫芦球");
		map.put("JD", "胶带");
		map.put("HZT", "好字帖");
		map.put("WHK", "问候卡");
		map.put("SFD", "速封袋");
		
		return map;
   }
   
   
}
