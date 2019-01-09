package com.jiuyescm.bms.base.file.repository;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.base.file.entity.BillPrepareExportTaskEntity;

public interface IBillPrepareExportTaskRepository {
	PageInfo<BillPrepareExportTaskEntity> queryBillTask(Map<String, Object> param,
			int pageNo, int pageSize);
	
	BillPrepareExportTaskEntity save(BillPrepareExportTaskEntity entity)throws Exception;
    
    int update(BillPrepareExportTaskEntity entity);
    
	boolean checkFileHasDownLoad(Map<String, Object> queryEntity);
	
    //通过主商家找到子商家
    List<Map<String,String>> getChildCustomer(String mkId);
}
