package com.jiuyescm.bms.consumer.upload;

import java.sql.Date;
import java.sql.Timestamp;

public class Test {

	public static void main(String[] args) {
		Timestamp time = new Timestamp(System.currentTimeMillis());
		Date date = new Date(time.getTime());
		System.out.println(date);
	}

}
