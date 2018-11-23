package com.jiuyescm.bms.billimport;

import com.jiuyescm.bms.billimport.handler.DispatchHandler;
import com.jiuyescm.bms.billimport.handler.ProductStorageHandler;

public class FeesHandlerFactory {

	public static IFeesHandler getHandler(String sheetName){
		if("宅配".equals(sheetName)){
			return new DispatchHandler();
		}
		else if("仓储".equals(sheetName)){
			return new ProductStorageHandler();
		}
		else{
			return null;
		}
	}
}
