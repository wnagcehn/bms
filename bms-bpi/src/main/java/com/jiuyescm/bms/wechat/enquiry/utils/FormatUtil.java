package com.jiuyescm.bms.wechat.enquiry.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class FormatUtil {

	private static final DecimalFormat FORMAT = new DecimalFormat("0.00");
	
	/*
	 * 格式化金额，保留两位小数
	 */
	public static String formatPrice(BigDecimal price){
		return FORMAT.format(price);
	}
	
	/*
	 * 格式化金额，保留两位小数
	 */
	public static String formatPrice(Double price){
		return FORMAT.format(price);
	}
}
