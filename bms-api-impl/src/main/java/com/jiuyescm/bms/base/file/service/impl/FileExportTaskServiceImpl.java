/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.base.file.service.impl;

import java.io.File;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.base.file.entity.FileExportTaskEntity;
import com.jiuyescm.bms.base.file.repository.IFileExportTaskRepository;
import com.jiuyescm.bms.base.file.service.IFileExportTaskService;
import com.jiuyescm.bms.common.constants.MessageConstant;
import com.jiuyescm.bms.common.sequence.service.SequenceService;
import com.jiuyescm.common.ConstantInterface;

/**
 * 文件导出任务列表接口实现类
 * @author yangss
 */
@Service("fileExportTaskService")
public class FileExportTaskServiceImpl implements IFileExportTaskService {

	@Resource 
	private SequenceService sequenceService;
	
	private static final Logger logger = Logger.getLogger(FileExportTaskServiceImpl.class.getName());
	
	@Autowired
    private IFileExportTaskRepository fileExportTaskRepository;

    @Override
    public PageInfo<FileExportTaskEntity> query(Map<String, Object> condition,
            int pageNo, int pageSize) {
        return fileExportTaskRepository.query(condition, pageNo, pageSize);
    }
    
    @Override
    public List<FileExportTaskEntity> query(Map<String, Object> condition) {
    	return fileExportTaskRepository.query(condition);
    }

    @Override
    public FileExportTaskEntity findById(Long id) {
        return fileExportTaskRepository.findById(id);
    }

    @Override
    public FileExportTaskEntity save(FileExportTaskEntity entity)throws Exception {
    	if (null != entity) {
    		String taskId = sequenceService.getBillNoOne(FileExportTaskEntity.class.getName(), "FT", "0000000000");
    		if (StringUtils.isBlank(taskId)) {
    			throw new Exception("生成导出文件编号失败,请稍后重试!");
    		}
    		entity.setTaskId(taskId);
		}
        return fileExportTaskRepository.save(entity);
    }

    @Override
    public int update(FileExportTaskEntity entity) {
        return fileExportTaskRepository.update(entity);
    }

    /**
     * 是否存在下载任务，存在则删除任务、文件
     */
	@Override
	public String isExistDeleteTask(Map<String, Object> condition) {
		if (null != condition) {
			List<FileExportTaskEntity> taskList = fileExportTaskRepository.query(condition);
			//如果存在了则删除下载任务
			if (null != taskList && taskList.size() > 0) {
				FileExportTaskEntity entity=taskList.get(0);
				entity.setDelFlag(ConstantInterface.DelFlag.YES);
				int num=fileExportTaskRepository.update(entity);
				if(num<=0){
					return MessageConstant.DELETE_FILE_TASK_FAIL_MSG;
				}
				//删除文件
				File file=new File(entity.getFilePath());
				if (file.exists()) {
					file.delete();
				}
			}
		}
		return null;
	}

	/**
	 * 更新进度
	 */
	@Override
	public int updateExportTask(String taskId, String taskState, double process) {

		FileExportTaskEntity entity = new FileExportTaskEntity();
		if (StringUtils.isNotEmpty(taskState)) {
			entity.setTaskState(taskState);
		}
		entity.setTaskId(taskId);
		entity.setProgress(process);
		return fileExportTaskRepository.update(entity);
	}
	
	   /**
     * 更新进度
     */
    @Override
    public int updateTask(String taskId, String taskState, double process, String filepath) {

        FileExportTaskEntity entity = new FileExportTaskEntity();
        if (StringUtils.isNotEmpty(taskState)) {
            entity.setTaskState(taskState);
        }
        entity.setFilePath(filepath);
        entity.setTaskId(taskId);
        entity.setProgress(process);
        return fileExportTaskRepository.update(entity);
    }

	@Override
	public boolean checkFileHasDownLoad(Map<String, Object> queryEntity) {
		return fileExportTaskRepository.checkFileHasDownLoad(queryEntity);
	}

	@Override
	public PageInfo<FileExportTaskEntity> queryBillTask(
			Map<String, Object> param, int pageNo, int pageSize) {
		return fileExportTaskRepository.queryBillTask(param,pageNo,pageSize);
	}
	
}
