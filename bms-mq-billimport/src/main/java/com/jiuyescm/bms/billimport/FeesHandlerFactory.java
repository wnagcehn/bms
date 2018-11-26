package com.jiuyescm.bms.billimport;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

public class FeesHandlerFactory {
	
	public static IFeesHandler getHandler(String sheetName){
		IFeesHandler handler = null;
		WebApplicationContext ctx = ContextLoader.getCurrentWebApplicationContext(); 
		
		handler = (IFeesHandler) ctx.getBean(sheetName);
		if (null == handler) {
			return null;
		}
		return handler;
	}

}
