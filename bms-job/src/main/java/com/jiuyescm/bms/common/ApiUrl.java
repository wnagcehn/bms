package com.jiuyescm.bms.common;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

public class ApiUrl {
	
	private static final Logger logger = Logger.getLogger(ApiUrl.class.getName());
	
	private ApiUrl() {
	}

	/**
	 * 入库确认访问地址
	 */
	public static String INCONFIRMURL = "http://api.test.jiuyescm.com/apiman-gateway/jiuye/entry-order-confirm/1.0";
	
	/**
	 * 出库确认访问地址
	 */
	public static String OUTCONFIRMURL = "http://api.test.jiuyescm.com/apiman-gateway/jiuye/stock-out-confirm/1.0";
	
	/**
	 * OMS订单信息下发DMS 
	 */
	//public static String OMSTODMSURL = "http://120.55.88.152:8082/webYMXReceive/OMSReceive.aspx";
	public static String OMSTODMSURL = "http://120.55.88.152:8082/webDMSReceive/Receive.aspx";
	
	/**
	 * OMS推送流转信息到WLB
	 */
	public static String OMSTOWLBURL = "http://pac.partner.taobao.com/cooperate/gateway.do";
	
	/**
	 * 订单拦截地址参数 
	 */
	public static String INTERCEPT_ORDER_URI = "";
	//prod
	//public static final String OMSTODMSURL = "todms_url=http://apix.jiuyescm.com/webYMXReceive/OMSReceive.aspx";
	
	static{
		InputStream in = null;
		Properties pros = new Properties();
		try { 
			// 前提是资源文件必须和Mytest类在同一个包下
			in = ApiUrl.class.getResourceAsStream("/uri.properties"); 
			pros.load(in);
			INCONFIRMURL = pros.getProperty("INCONFIRMURL");
			OUTCONFIRMURL = pros.getProperty("OUTCONFIRMURL");
			OMSTODMSURL = pros.getProperty("OMSTODMSURL");
			OMSTOWLBURL = pros.getProperty("OMSTOWLBURL");//物流宝
			INTERCEPT_ORDER_URI = pros.getProperty("INTERCEPT_ORDER_URI");
			
		} catch (IOException e) {
			logger.error("获取参数异常", e);
		} finally {
			try {
				if (null != in) {
					in.close();
				}
			} catch (IOException e) {
				logger.error("关闭输入流异常", e);
			}
		}
	} 
	
}
