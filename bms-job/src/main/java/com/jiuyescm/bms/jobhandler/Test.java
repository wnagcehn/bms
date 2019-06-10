package com.jiuyescm.bms.jobhandler;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.time.DateUtils;

import com.jiuyescm.common.utils.DateUtil;

public class Test {
	public static void main(String[] args) throws ParseException {
	    String startDate = DateUtil.getFirstDayOfMonth(0,"yyyy-MM-dd");
	    startDate+=" 00:00:00";
        String endDate = DateUtil.getFirstDayOfMonth(-1,"yyyy-MM-dd");
        endDate+=" 00:00:00";
        
        System.out.println(startDate+"+++++"+endDate);
	}
}
