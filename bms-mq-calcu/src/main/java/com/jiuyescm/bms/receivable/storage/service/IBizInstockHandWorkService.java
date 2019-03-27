package com.jiuyescm.bms.receivable.storage.service;

import java.util.List;
import java.util.Map;




import com.jiuyescm.bms.biz.storage.entity.BizInStockDetailEntity;
import com.jiuyescm.bms.biz.storage.entity.BizInStockMasterEntity;
import com.jiuyescm.bms.biz.storage.entity.BizInstockHandworkEntity;

/**
 * 
 * @author cjw
 * 
 */
public interface IBizInstockHandWorkService {
	
	public List<BizInstockHandworkEntity> getInStockMasterList(Map<String, Object> condition);
	
	public void updateInstock(BizInstockHandworkEntity entity);
	
	public void updateInstockBatch(List<BizInstockHandworkEntity> entities);
		
}
