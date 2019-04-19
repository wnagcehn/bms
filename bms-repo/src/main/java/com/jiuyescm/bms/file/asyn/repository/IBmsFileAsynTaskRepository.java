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

    /**
     * 查询最近一次的最小的taskId
     * <功能描述>
     * 
     * @author wangchen870
     * @date 2019年4月19日 上午10:48:45
     *
     * @param param
     * @return
     */
    BmsFileAsynTaskEntity queryMinTask(Map<String, Object> param);

}
