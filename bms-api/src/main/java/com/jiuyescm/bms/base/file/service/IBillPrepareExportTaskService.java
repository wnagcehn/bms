package com.jiuyescm.bms.base.file.service;

import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.base.file.entity.BillPrepareExportTaskEntity;

public interface IBillPrepareExportTaskService {
	PageInfo<BillPrepareExportTaskEntity> queryBillTask(Map<String, Object> param,
			int pageNo, int pageSize);
	
	BillPrepareExportTaskEntity save(BillPrepareExportTaskEntity entity)throws Exception;
    
    int update(BillPrepareExportTaskEntity entity);
    
	boolean checkFileHasDownLoad(Map<String, Object> queryEntity);

    //更新进度
    int updateExportTask(String taskId, String taskState, double process);
}
