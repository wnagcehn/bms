package com.jiuyescm.bms.jobhandler;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

import com.jiuyescm.cfm.common.JAppContext;

public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Calendar cal = Calendar.getInstance();
		String date="";
		int year=cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH);
		if(month<10){
			date=year+"0"+month;
		}else{
			date=year+""+month;
		}
		System.out.println(date);
	}

}
