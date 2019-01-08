package com.jiuyescm.bms.excel.common;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.time.DateUtils;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;

public class StringUtil {

	/**
     * 将excel中的内容转换为timestamp类型
     * @param value
     * @return
     * @throws ParseException 
     */
    public Timestamp changeValueToTimestamp(String value) throws ParseException{
    	if(isNumeric(value)){
    		Date date = HSSFDateUtil.getJavaDate(Double.valueOf(value));
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            value = dateFormat.format(date);
    	}
    	String[] dataPatterns = new String[] { "yyyy-MM-dd hh:mm:ss", "yyyy-MM-dd", "dd-MM月 -yy","yyyy/MM/dd","yyyy/MM/dd HH:mm:ss","yyyy年MM月dd日" };
		Date date = DateUtils.parseDate(value, dataPatterns);
		Timestamp ts=new Timestamp(date.getTime());
		return ts;
    }
	
    
    /**
	 * 验证字符串是否为数值类型；
	 * @param str
	 * @return
	 */
	public static boolean isNumeric(String str){ 
		if(str == null || str.length()==0){
			return false;
		}
		Pattern pattern = Pattern.compile("([1-9]+[0-9]*|0)(\\.[\\d]+)?"); 
		Matcher isNum = pattern.matcher(str);
		if( !isNum.matches() ){
		    return false; 
		} 
		return true; 
	}   
}
