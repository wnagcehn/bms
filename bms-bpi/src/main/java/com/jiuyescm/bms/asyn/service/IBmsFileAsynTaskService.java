package com.jiuyescm.bms.asyn.service;

import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.asyn.vo.BmsFileAsynTaskVo;
import com.jiuyescm.bms.file.asyn.BmsFileAsynTaskEntity;

/**
 * bms文件 异步导入导出服务
 * @author cjw
 */
public interface IBmsFileAsynTaskService {

    PageInfo<BmsFileAsynTaskVo> query(Map<String, Object> condition, int pageNo,int pageSize) throws Exception;

    BmsFileAsynTaskVo findByTaskId(String taskId) throws Exception;

    int save(BmsFileAsynTaskVo entity) throws Exception;

    int update(BmsFileAsynTaskVo entity);

}
