package com.jiuyescm.bms.billimport;

import java.util.List;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.jiuyescm.bms.billimport.handler.DispatchHandler;
import com.jiuyescm.bms.billimport.handler.StorageHandler;
import com.jiuyescm.mdm.warehouse.api.IWarehouseService;
import com.jiuyescm.mdm.warehouse.vo.WarehouseVo;

public class FeesHandlerFactory implements ApplicationContextAware{
	
	/*@Autowired
	private static IWarehouseService warehouseService;*/
	
	private static ApplicationContext ctx;
	// 仓储-上海仓
	public static IFeesHandler getHandler(String sheetName){
		
		IFeesHandler handler = (IFeesHandler) ctx.getBean(sheetName);
		return handler;
		//List<WarehouseVo> warehouseList = warehouseService.queryAllWarehouse();
		/*if (warehouseList == null) {
		
		}
		for (WarehouseVo warehouseVo : warehouseList) {
			if (sheetName.equals(warehouseVo)) {
				return new StorageHandler();
			}
		}*/
		
		/*if("宅配".equals(sheetName)){
			handler = new DispatchHandler();
		}
		else if("宅配".equals(sheetName)){
			handler = new DispatchHandler();
		}
		else{
			handler = null;
		}
		return handler;*/
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.ctx = applicationContext;
	}
}
