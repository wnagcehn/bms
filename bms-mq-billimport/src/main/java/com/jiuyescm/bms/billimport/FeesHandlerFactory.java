package com.jiuyescm.bms.billimport;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

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
<<<<<<< HEAD
		else if("仓储".equals(sheetName)){
			return new ProductStorageHandler();
=======
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
>>>>>>> 7f8c92a317f8242f7c847f31fac24521b6599e93
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
