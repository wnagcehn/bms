/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.base.file.repository;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.base.file.entity.FileExportTaskEntity;

/**
 * 
 * @author stevenl
 * 
 */
public interface IFileExportTaskRepository {

	public PageInfo<FileExportTaskEntity> query(Map<String, Object> condition,
            int pageNo, int pageSize);
	
	public List<FileExportTaskEntity> query(Map<String, Object> condition);

    public FileExportTaskEntity findById(Long id);

    public FileExportTaskEntity save(FileExportTaskEntity entity);

    public int update(FileExportTaskEntity entity);

	public boolean checkFileHasDownLoad(Map<String, Object> queryEntity);

	public PageInfo<FileExportTaskEntity> queryBillTask(
			Map<String, Object> param, int pageNo, int pageSize);

}
