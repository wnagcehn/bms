/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.base.file.service;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.base.file.entity.FileExportTaskEntity;

/**
 * 文件导出任务列表service接口
 * @author yangss
 */
public interface IFileExportTaskService {

    PageInfo<FileExportTaskEntity> query(Map<String, Object> condition, int pageNo,
            int pageSize);
    
    List<FileExportTaskEntity> query(Map<String, Object> condition);

    FileExportTaskEntity findById(Long id);

    FileExportTaskEntity save(FileExportTaskEntity entity)throws Exception;

    int update(FileExportTaskEntity entity);

    //是否存在下载任务，存在则删除任务、文件
    String isExistDeleteTask(Map<String, Object> condition);
    
    //更新进度
    int updateExportTask(String taskId, String taskState, double process);

	boolean checkFileHasDownLoad(Map<String, Object> queryEntity);

	PageInfo<FileExportTaskEntity> queryBillTask(Map<String, Object> param,
			int pageNo, int pageSize);

    int updateTask(String taskId, String taskState, double process, String filepath);
    
}
