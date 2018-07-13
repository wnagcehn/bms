package com.jiuyescm.bms.common;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.time.FastDateFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DateUtil
{
	private static Logger logger = LoggerFactory.getLogger(DateUtil.class);
	
	private static final int TEN_MINUTES = 10;
	
	private static final DateFormat yyyymmdd = new SimpleDateFormat("yyyyMMdd");
	public static final String DATE_FORMART_HHMMSS = "HHmmss";
	public static final String DATE_FORMART_YYYYMMDD = "yyyyMMdd";
	public static final String DATE_FORMART_YYYY_MM_DD = "yyyy-MM-dd";
	public static final String DATE_FORMART_YYYYMMDDHHMMSS = "yyyyMMddHHmmss";
	public static final String DATE_FORMART_YYYYMMDD_HHMMSS = "yyyy-MM-dd HH:mm:ss";
	public static final String DATE_FORMART_SYMBOL_LINE = "-";
	public static final String DATE_FORMART_SYMBOL_COLON = ":";
	
	public static String getCurrentRandomDate(int dateute){
		Calendar c = Calendar.getInstance();  //得到当前日期和时间
		c.add(Calendar.DATE, dateute);//设置当前时间的前后日期
		Date newDate = c.getTime();
        return formatnormal(DATE_FORMART_YYYY_MM_DD,newDate);
	}
	
	public static String getCurrentRandomMonth(int monthute){
		Calendar c = Calendar.getInstance();  //得到当前日期和时间
		c.add(Calendar.MONTH, monthute);//设置当前时间的前后月份
		Date newDate = c.getTime();
        return formatnormal(DATE_FORMART_YYYY_MM_DD,newDate);
	}
	
	public static String formatnormal(String formatStr,Date formatDate){
		DateFormat df = new SimpleDateFormat(formatStr);
        return df.format(formatDate);
	}	
	/**
	 * @Title: getCurrentDateTime
	 * @Description: 获取当系统时间
	 * @return
	 */ 
	public static Date getCurrentDateTime(){
		Calendar c = Calendar.getInstance();  //得到当前日期和时间
        return c.getTime();
	}
	
	/**
	 * @Title: getCurrentDateTime
	 * @Description: 
	 * @param minute
	 * @return
	 */ 
	public static Date getTenMinutesLaterDateTime(){
        return getDateTime(TEN_MINUTES);
	}
	
	/**
	 * @Title: getCurrentDateMin
	 * @Description: 获取当系统时间,前后时间
	 * @param minute
	 * @return
	 */ 
	public static Date getDateTime(int minute){
		Calendar c = Calendar.getInstance();  //得到当前日期和时间
		c.add(Calendar.MINUTE, minute);
		return c.getTime();
	}
	
	public static Date getFormatDate(String dateStr){
		DateFormat df = new SimpleDateFormat(DATE_FORMART_YYYY_MM_DD);
		Date date = null;
		try {
			date = df.parse(dateStr);
		} catch (ParseException e) {
			logger.error("String format Date exception:", e);
		}
		return date;
	}
	
    /**
     * @Title: compare_date
     * @Description: date1大于date2返回1，date1小于date2返回-1
     * @param date1
     * @param date2
     * @return
     */ 
    public static int compareDate(Date date1, Date date2) {
         try {
             if (date1.getTime() > date2.getTime()) {
                 return 1;
             } else if (date1.getTime() < date2.getTime()) {
                 return -1;
             } else {
                 return 0;
             }
         } catch (Exception exception) {
        	 logger.error("时间比较出错！", exception);
         }
         return 0;
    }
    
    /**
     * @Description: date1大于date2返回1，date1小于date2返回-1
     * @param format_date
     * @param date1
     * @param date2
     * @return int
     */ 
    public static int compareDate(String format_date, String date1, String date2) {
         DateFormat df = new SimpleDateFormat(format_date);
         try {
             Date dt1 = df.parse(date1);
             Date dt2 = df.parse(date2);
             if (dt1.getTime() > dt2.getTime()) {
                 return 1;
             } else if (dt1.getTime() < dt2.getTime()) {
                 return -1;
             } else {
                 return 0;
             }
         } catch (Exception exception) {
             logger.error("时间比较出错！", exception);
         }
         return 0;
    }
    
	/**
	 * @Title: compareDate
	 * @Description: 大于当前系统时间返回true，小于当前系统时间返回false
	 * @param effectiveTime
	 * @return
	 */ 
	public static boolean compareDate(Date effectiveTime){
		int i = compareDate(effectiveTime, getCurrentDateTime());
		return i == 1 ? true : false;
	}
	
	/**
	 * @Title: formatYYYYMMDD
	 * @Description: 将date类型时间格式化成yyyyMMdd格式字符串
	 * @param date
	 * @return
	 */ 
	public static String formatYYYYMMDD(Date date) {
		if(date == null)
			 return null;
		return yyyymmdd.format(date);
	}
	
	/**
	 * 格式化日期  yyyyMMdd
	 */
	public static String formatYYYYMMDD(String dateStr)
	{
		String dateString = "";
		try {
			SimpleDateFormat format = new SimpleDateFormat(DATE_FORMART_YYYYMMDD);
			Date date = format.parse(dateStr);
			dateString = format.format(date);
		} catch (Exception e) {
			logger.error("formatYYYYMMDD exception", e);
		}
		return dateString;
	}
	
	/**
	 * 格式化日期  yyyy-MM-dd
	 */
	public static String formatyymmddLine(Date date) {
		SimpleDateFormat format = new SimpleDateFormat(DATE_FORMART_YYYY_MM_DD);
		return format.format(date);
	}
	
	/**
	 * 格式化日期  yyyy-MM-dd HH:mm:ss ->yyyyMMddHHmmss
	 */
	public static String parseLineToDateStr(String dateStr)
	{
		String dateString = "";
		try
		{
			SimpleDateFormat oldFormat = new SimpleDateFormat(DATE_FORMART_YYYYMMDD_HHMMSS);
			SimpleDateFormat newFormat = new SimpleDateFormat(DATE_FORMART_YYYYMMDDHHMMSS);
			dateString = newFormat.format(oldFormat.parse(dateStr));
		}
		catch (Exception e)
		{
			logger.error("parseLineToDateStr exception", e);
		}
		return dateString;
	}
	
	/**
	 * 将dateStr转成Date
	 */
	public static String parseDateStrToLine(String dateStr)
	{
		String dateString = "";
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMART_YYYYMMDD);
			dateString = formatyymmddLine(sdf.parse(dateStr));
		} catch (Exception e) {
			logger.error("parseDateStrToLine exception", e);
		}
		return dateString;
	}
	
	/**
	 * 获取指定月份的第一天，默认当前月的第一天
	 * @param quantity
	 * @return
	 */
	public static String getFirstDayOfMonth(int quantity) {
		FastDateFormat fdf = FastDateFormat.getInstance(DATE_FORMART_YYYY_MM_DD);
		Calendar cal = Calendar.getInstance();
		
		int month = 1;
		if (0 == quantity) {
			month = cal.get(Calendar.MONTH);
		}else {
			month = cal.get(Calendar.MONTH) - quantity;
		}
		
		cal.set(cal.get(Calendar.YEAR), month, 1);
		
		return fdf.format(cal.getTime());
	}
	
	/**
	 * 获取月份指定天数的日期
	 * @param quantity
	 * @return
	 */
	public static String getSpecifyDate(String dateStr, int quantity) {
		FastDateFormat fdf = FastDateFormat.getInstance(DATE_FORMART_YYYY_MM_DD);
		Calendar cal = Calendar.getInstance();
		
		try {
			Date date = fdf.parse(dateStr);
			cal.setTime(date);
			cal.add(Calendar.DATE, quantity);
		} catch (ParseException e) {
			logger.error("getSpecifyDate exception", e);
		}
		
		return fdf.format(cal.getTime());
	}
	
	/**
	 * 获取指定月份的天数
	 * @param dateStr
	 * @return
	 */
	public static int getDaysOfMonth(String dateStr) {
		FastDateFormat fdf = FastDateFormat.getInstance(DATE_FORMART_YYYY_MM_DD);
		Calendar cal = Calendar.getInstance();
		try {
			Date date = fdf.parse(dateStr);
			cal.setTime(date);
		} catch (ParseException e) {
			logger.error("getDaysOfMonth exception", e);
		}
		
		return cal.getActualMaximum(Calendar.DAY_OF_MONTH);
	}
	
}
