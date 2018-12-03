package com.jiuyescm.bms.billimport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

public class FeesHandlerFactory {
	
	private static final Logger logger = LoggerFactory.getLogger(FeesHandlerFactory.class);

	public static IFeesHandler getHandler(String sheetName){
		IFeesHandler handler = null;
		WebApplicationContext ctx = ContextLoader.getCurrentWebApplicationContext(); 
		try {
			handler = (IFeesHandler) ctx.getBean(sheetName);
		} catch (Exception e) {
			// TODO: handle exception
			logger.info("获取handel异常: {}",e.getMessage());
			return null;
		}
		if (null == handler) {
			return null;
		}
		return handler;
	}

}
