/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.base.file.repository.impl;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.base.file.entity.FileExportTaskEntity;
import com.jiuyescm.bms.base.file.repository.IFileExportTaskRepository;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;

/**
 * 文件导出任务列表
 * @author yangss
 */
@Repository("fileExportTaskRepository")
public class FileExportTaskRepositoryImpl extends MyBatisDao<FileExportTaskEntity> implements IFileExportTaskRepository {

	private static final Logger logger = Logger.getLogger(FileExportTaskRepositoryImpl.class.getName());

	public FileExportTaskRepositoryImpl() {
		super();
	}
	
	@Override
    public PageInfo<FileExportTaskEntity> query(Map<String, Object> condition, int pageNo, int pageSize) {
        List<FileExportTaskEntity> list = selectList("com.jiuyescm.bms.base.file.mapper.FileExportTaskMapper.query", 
        		condition, new RowBounds(pageNo, pageSize));
        return new PageInfo<FileExportTaskEntity>(list);
    }
	
	@Override
	public List<FileExportTaskEntity> query(Map<String, Object> condition) {
		List<FileExportTaskEntity> list = selectList("com.jiuyescm.bms.base.file.mapper.FileExportTaskMapper.query", condition);
		return list;
	}

    @Override
    public FileExportTaskEntity findById(Long id) {
        FileExportTaskEntity entity = selectOne("com.jiuyescm.bms.base.file.mapper.FileExportTaskMapper.findById", id);
        return entity;
    }

    @Override
    public FileExportTaskEntity save(FileExportTaskEntity entity) {
        insert("com.jiuyescm.bms.base.file.mapper.FileExportTaskMapper.save", entity);
        return entity;
    }

    @Override
    public int update(FileExportTaskEntity entity) {
        return update("com.jiuyescm.bms.base.file.mapper.FileExportTaskMapper.update", entity);
    }

	@Override
	public boolean checkFileHasDownLoad(Map<String, Object> queryEntity) {
		Object obj=this.selectOneForObject("com.jiuyescm.bms.base.file.mapper.FileExportTaskMapper.checkFileHasDownLoad", queryEntity);
		if(obj!=null){
			int k=Integer.valueOf(obj.toString());
			if(k>0){
				return true;
			}else{
				return false;
			}
		}
		return false;
	}

	@Override
	public PageInfo<FileExportTaskEntity> queryBillTask(
			Map<String, Object> param, int pageNo, int pageSize) {
		List<FileExportTaskEntity> list = selectList("com.jiuyescm.bms.base.file.mapper.FileExportTaskMapper.queryBillTask", 
				param, new RowBounds(pageNo, pageSize));
	    return new PageInfo<FileExportTaskEntity>(list);
	}
	
}
