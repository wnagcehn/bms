package com.jiuyescm.bs.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {
	
	public static boolean isEmpty(String string) {
		
		return string == null ? true : "".equals(string.trim());
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
	
	/**
	 * 验证字符串是否为日期格式
	 * @param strDate
	 * @return
	 * 2018-01-01  -> true
	 * 2018/01/01  -> true
	 * 2018-01-01 00:00:00 -> true
	 * 2018/01/01 00:00:00 -> true
	 */
	public static boolean isDate(String strDate) {
        Pattern pattern = Pattern.compile("^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))(\\s(((0?[0-9])|([1-2][0-3]))\\:([0-5]?[0-9])((\\s)|(\\:([0-5]?[0-9])))))?$");
        Matcher m = pattern.matcher(strDate);
        if (m.matches()) {
            return true;
        } else {
            return false;
        }
    }

}
