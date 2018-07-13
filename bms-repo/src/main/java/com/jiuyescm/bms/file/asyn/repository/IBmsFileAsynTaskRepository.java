package com.jiuyescm.bms.file.asyn.repository;

import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.file.asyn.BmsFileAsynTaskEntity;

/**
 * 文件异步导入导出dao层
 * @author cjw
 * 
 */
public interface IBmsFileAsynTaskRepository {

	public PageInfo<BmsFileAsynTaskEntity> query(Map<String, Object> condition,int pageNo, int pageSize);

    public BmsFileAsynTaskEntity findByTaskId(String taskId);

    public int save(BmsFileAsynTaskEntity entity);

    public int update(BmsFileAsynTaskEntity entity);

}
