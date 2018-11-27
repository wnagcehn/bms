package com.jiuyescm.common.utils;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;

import com.jiuyescm.bs.util.StringUtil;

public class DateUtil {
	
	 private static final Logger logger = Logger.getLogger(BeanToMapUtil.class.getName());
	
	private static final int TEN_MINUTES = 10;
	
	private static final DateFormat yyyymmdd = new SimpleDateFormat("yyyyMMdd");
	public static String DATE_FORMART_HHMMSS = "HHmmss";
	public static String DATE_FORMART_YYYYMMDD = "yyyyMMdd";
	public static String DATE_FORMART_YYYY_MM_DD = "yyyy-MM-dd";
	public static String DATE_FORMART_YYYYMMDDHHMMSS = "yyyyMMddHHmmss";
	public static String DATE_FORMART_YYYYMMDD_HHMMSS = "yyyy-MM-dd HH:mm:ss";
	public static String DATE_FORMART_SYMBOL_LINE = "-";
	public static String DATE_FORMART_SYMBOL_COLON = ":";
	
	private DateUtil() {
	}
	
	public static String getCurrentRandomDate(int dateute){
		Calendar c = Calendar.getInstance();  //得到当前日期和时间
		c.add(Calendar.DATE, dateute);//设置当前时间的前后日期
		Date newDate = c.getTime();
        return formatnormal(DATE_FORMART_YYYYMMDD,newDate);
	}
	
	public static String getCurrentRandomMonth(int monthute){
		Calendar c = Calendar.getInstance();  //得到当前日期和时间
		c.add(Calendar.MONTH, monthute);//设置当前时间的前后月份
		Date newDate = c.getTime();
        return formatnormal(DATE_FORMART_YYYYMMDD,newDate);
	}
	
	public static String formatnormal(String formatStr,Date formatDate){
		DateFormat df = new SimpleDateFormat(formatStr);
        return df.format(formatDate);
	}	
	
	public static String formatDateTime(String formatStr, String dateTime){
		String dateString = "";
		try {
			SimpleDateFormat oldFormat = new SimpleDateFormat(DATE_FORMART_YYYYMMDD_HHMMSS);
			SimpleDateFormat newFormat = new SimpleDateFormat(formatStr);
			dateString = newFormat.format(oldFormat.parse(dateTime));
		} catch (Exception e) {
			e.printStackTrace();
		}
        return dateString;
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
			// TODO: handle exception
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
			e.printStackTrace();
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
			e.printStackTrace();
		}
		return dateString;
	}
	
	/**
	 * 格式化DateTime->Timestamp
	 * @param time
	 * @return
	 */
	public static Timestamp formatTimestamp(Object time){
		DateFormat formatter = new SimpleDateFormat(DATE_FORMART_YYYYMMDD_HHMMSS); 
		String dateString=formatter.format(time);
		return Timestamp.valueOf(dateString);
	}
	
	/**
	 * YYYY-MM-DD --> YYYY-MM-DD 00:00:00
	 * @param beginTime
	 * @return
	 */
	public static Timestamp formatYYYYMMDD2TimestampBegin(Timestamp beginTime){
		DateFormat formatter = new SimpleDateFormat(DATE_FORMART_YYYY_MM_DD);
		StringBuffer strBuf = new StringBuffer();
		strBuf.append(formatter.format(beginTime)).append(" 00:00:00");
		return Timestamp.valueOf(strBuf.toString());
	}
	
	/**
	 * YYYY-MM-DD --> YYYY-MM-DD 23:59:59
	 * @param endTime
	 * @return
	 */
	public static Timestamp formatYYYYMMDD2TimestampEnd(Timestamp endTime){
		
		DateFormat formatter = new SimpleDateFormat(DATE_FORMART_YYYY_MM_DD);
		StringBuffer strBuf = new StringBuffer();
		strBuf.append(formatter.format(endTime)).append(" 23:59:59");
		return Timestamp.valueOf(strBuf.toString());
	}
	
	public static int getCurrentYYYY(){
		Calendar now = Calendar.getInstance();
		int year = now.get(Calendar.YEAR);
		return year;
	}
	
	/**
	 * 获取两个日期相差的天数
	 * @param end
	 * @param start
	 * @return
	 */
	public static int getDays(Timestamp start,Timestamp end){  
        Calendar startC = Calendar.getInstance();  
        Calendar endC = Calendar.getInstance();  
        startC.setTime(start);  
        endC.setTime(end);  
        int days = 0;  
        //开始时间小于结束时间
        while(startC.before(endC)){  
            days++;  
            startC.add(Calendar.DAY_OF_YEAR, 1);  
        }  
        //开始时间大于结束时间
        if(days==0){  
            while(endC.before(startC)){  
                days--;  
                endC.add(Calendar.DAY_OF_YEAR, 1);  
            }  
        }  
        return days;  
    }  
	
	
	/**
	 * 当前时间增加小时
	 * @param ts
	 * @param hours
	 * @return
	 */
	public static Timestamp addHours(Timestamp ts, int hours){
		Calendar cl = Calendar.getInstance();
		cl.setTime(ts);  
		cl.add(Calendar.HOUR, hours);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateStr = sdf.format(cl.getTime());
		Timestamp ts1 = Timestamp.valueOf(dateStr);
		return ts1;
	}
	
	/**
	 * 当前时间增加天数
	 * @param ts
	 * @param days
	 * @return
	 */
	public static Timestamp addDays(Timestamp ts, int days){
		Calendar cl = Calendar.getInstance();
		cl.setTime(ts);  
		cl.add(Calendar.DATE, days);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateStr = sdf.format(cl.getTime());
		Timestamp ts1 = Timestamp.valueOf(dateStr);
		return ts1;
	}
	
	public static String addDays(String date, int days) throws ParseException{
		Calendar cl = Calendar.getInstance();
		FastDateFormat fdf = FastDateFormat.getInstance(DATE_FORMART_YYYY_MM_DD);
		cl.setTime(fdf.parse(date));
		cl.add(Calendar.DATE, days);
		FastDateFormat sdf = FastDateFormat.getInstance(DATE_FORMART_YYYY_MM_DD);
		String dateStr = sdf.format(cl.getTime());
		return dateStr;
	}
	
	/**
	 * 当前时间增加月份
	 * @param ts
	 * @param days
	 * @return
	 */
	public static Timestamp addMonths(Timestamp ts, int month){
		Calendar cl = Calendar.getInstance();
		cl.setTime(ts);  
		cl.add(Calendar.MONTH, month);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateStr = sdf.format(cl.getTime());
		Timestamp ts1 = Timestamp.valueOf(dateStr);
		return ts1;
	}
	
	public static int getYear(Timestamp ts){
		Calendar cl = Calendar.getInstance();
		cl.setTime(ts); 
		return cl.get(Calendar.YEAR);
	}
	
	public static int getMonth(Timestamp ts){
		Calendar cl = Calendar.getInstance();
		cl.setTime(ts); 
		return cl.get(Calendar.MONTH) + 1;
	}
	
	/**
	 * 获取指定月份第一天
	 * @param year
	 * @param month
	 * @return
	 */
	public static String getFirstDayOfMonth(int year, int month){
		Calendar cl = Calendar.getInstance();
		cl.set(Calendar.YEAR, year);
		cl.set(Calendar.MONTH, month-1);
		int firstDay = cl.getActualMinimum(Calendar.DAY_OF_MONTH);
		cl.set(Calendar.DAY_OF_MONTH, firstDay);
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMART_YYYY_MM_DD);
		return sdf.format(cl.getTime()); 
	}
	
	/**
	 * 获取指定月份第一天
	 * @param year
	 * @param month
	 * @return
	 */
	public static String getLastDayOfMonth(int year, int month){
		Calendar cl = Calendar.getInstance();
		cl.set(Calendar.YEAR, year);
		cl.set(Calendar.MONTH, month-1);
		int lastDay = cl.getActualMaximum(Calendar.DAY_OF_MONTH);
		cl.set(Calendar.DAY_OF_MONTH, lastDay);
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMART_YYYY_MM_DD);
		return sdf.format(cl.getTime()); 
	}
	
	/**
	 * 字符串转Timestamp格式
	 * @param value
	 * @return
	 * @throws ParseException
	 */
	public static Timestamp transStringToTimeStamp(String value) throws ParseException{
		if(StringUtil.isNumeric(value)){
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
	 * Timestamp转int类型YYMM格式
	 * @param value
	 * @return
	 * @throws ParseException
	 */
	public static int timeStamp2YYMM(Timestamp value) throws ParseException{
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(value);
        int month = calendar.get(Calendar.MONTH)+1;
        int year = calendar.get(Calendar.YEAR);
		int yymm = year%1000*100+month;
		return yymm;
	}
}
