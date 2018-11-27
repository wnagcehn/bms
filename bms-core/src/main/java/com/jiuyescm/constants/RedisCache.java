package com.jiuyescm.constants;

public class RedisCache {

	/**
	 * 默认过期时间  30分钟
	 */
	public static int expiredTime = 1800;  
	public static int halfHour = 1800;// 半小时
	public static int oneHour = 3600;// 1小时
	public static int fiveMinutes = 300;//5分钟
			
	
	/**
	 * BMS 仓库信息缓存地址
	 */
	public static final String WAREHOUSECODE_SPACE = "BMS.JIUYESCM.COM.CACHE.WAREHOUSECODE";
	
	/**
	 * BMS 仓库信息缓存地址
	 */
	public static final String WAREHOUSENAME_SPACE = "BMS.JIUYESCM.COM.CACHE.WAREHOUSENAME";
	
	/**
	 * BMS 商家信息缓存地址
	 */
	public static final String CUSTOMERCODE_SPACE = "BMS.JIUYESCM.COM.CACHE.CUSTOMERCODE";
	
	/**
	 * BMS 商家信息缓存地址
	 */
	public static final String CUSTOMERNAME_SPACE = "BMS.JIUYESCM.COM.CACHE.CUSTOMERNAME";
	
	/**
	 * BMS 物流商信息缓存地址
	 */
	public static final String CARRIERCODE_SPACE = "BMS.JIUYESCM.COM.CACHE.CARRIERCODE";
	
	/**
	 * BMS 物流商信息缓存地址
	 */
	public static final String CARRIERNAME_SPACE = "BMS.JIUYESCM.COM.CACHE.CARRIERNAME";
	
	/**
	 * BMS 宅配商信息缓存地址
	 */
	public static final String DELIVERCODE_SPACE = "BMS.JIUYESCM.COM.CACHE.DELIVERCODE";
	
	/**
	 * BMS 宅配商信息缓存地址
	 */
	public static final String DELIVERNAME_SPACE = "BMS.JIUYESCM.COM.CACHE.DELIVERNAME";
	
	
	/**
	 * BMS 费用科目缓存地址
	 */
	public static final String SUBJECTCODE_SPACE = "BMS.JIUYESCM.COM.CACHE.SUBJECTCODE";
	
	/**
	 * BMS 费用科目缓存地址
	 */
	public static final String SUBJECTNAME_SPACE = "BMS.JIUYESCM.COM.CACHE.SUBJECTNAME";
}
