/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.base.file.web;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.data.provider.Page;
import com.bstek.dorado.uploader.DownloadFile;
import com.bstek.dorado.uploader.annotation.FileProvider;
import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.base.file.entity.FileExportTaskEntity;
import com.jiuyescm.bms.base.file.service.IFileExportTaskService;
import com.jiuyescm.bms.common.constants.FileConstant;
import com.jiuyescm.bms.common.constants.MessageConstant;
import com.jiuyescm.cfm.common.JAppContext;
import com.jiuyescm.framework.fastdfs.client.StorageClient;
import com.jiuyescm.framework.fastdfs.protocol.storage.callback.DownloadByteArray;

/**
 * 文件导出任务列表
 * @author yangss
 */
@Controller("fileExportTaskController")
public class FileExportTaskController {

	private static final Logger logger = Logger.getLogger(FileExportTaskController.class.getName());

	@Resource
	private IFileExportTaskService fileExportTaskService;
	@Autowired
    private StorageClient storageClient;


	/**
	 * 分页查询
	 * @param page
	 * @param param
	 */
	@DataProvider
	public void query(Page<FileExportTaskEntity> page, Map<String, Object> param) {
		PageInfo<FileExportTaskEntity> pageInfo = fileExportTaskService.query(param, page.getPageNo(), page.getPageSize());
		if (pageInfo != null) {
			page.setEntities(pageInfo.getList());
			page.setEntityCount((int) pageInfo.getTotal());
		}
	}

	/**
	 * 导出文件
	 */
	@FileProvider
	public DownloadFile exportDetail(Map<String, Object> parameter)throws Exception{
		if (parameter == null || parameter.isEmpty()) {
			throw new Exception(MessageConstant.BILL_INFO_ISNULL_MSG);
		}
		String filePath=parameter.get("filePath").toString();
		if (StringUtils.isBlank(filePath)) {
			throw new Exception(MessageConstant.FILE_EXPORT_FILEPATH_NULL_MSG);
		}
		String taskName=parameter.get("taskName").toString();
		if (StringUtils.isBlank(taskName)) {
			throw new Exception(MessageConstant.FILE_EXPORT_TASKNAME_NULL_MSG);
		}
    	
        InputStream is = new FileInputStream(filePath);
        if(filePath.contains(FileConstant.SUFFIX_XLSX)){
        	return new DownloadFile(taskName + FileConstant.SUFFIX_XLSX, is);
        }else{
        	return new DownloadFile(taskName + FileConstant.SUFFIX_XLS, is);
        }
	
	}
	
	/**
     * 从fastdfs上导出文件
     * @throws Exception 
     */
    @FileProvider
    public DownloadFile exportForFastdfs(Map<String, String> parameter) throws Exception {
        if (parameter == null || parameter.isEmpty()) {
            throw new Exception(MessageConstant.BILL_INFO_ISNULL_MSG);
        }
        String filePath=parameter.get("filePath").toString();
        if (StringUtils.isBlank(filePath)) {
            throw new Exception(MessageConstant.FILE_EXPORT_FILEPATH_NULL_MSG);
        }
        final String taskName=parameter.get("taskName").toString();
        if (StringUtils.isBlank(taskName)) {
            throw new Exception(MessageConstant.FILE_EXPORT_TASKNAME_NULL_MSG);
        }
        //走服务器下载
        if (filePath.contains("/opt/export")) {
            InputStream is = new FileInputStream(filePath);
            if(filePath.contains(FileConstant.SUFFIX_XLSX)){
                return new DownloadFile(taskName + FileConstant.SUFFIX_XLSX, is);
            }else{
                return new DownloadFile(taskName + FileConstant.SUFFIX_XLS, is);
            }
        }
        //走fastdfs下载
        byte[] bytes=storageClient.downloadFile(parameter.get("filePath"),new DownloadByteArray());
        try{
            if(filePath.contains(FileConstant.SUFFIX_XLSX)){
                return new DownloadFile(taskName + FileConstant.SUFFIX_XLSX, new ByteArrayInputStream(bytes));
            }else {
                return new DownloadFile(taskName + FileConstant.SUFFIX_XLS, new ByteArrayInputStream(bytes));
            }
        }
        catch(Exception ex){
            logger.error("文件格式不正确", ex);
            return null;
        }
    }
	
	/**
	 * 删除导出任务
	 */
	@DataResolver
	public String delExportTask(FileExportTaskEntity entity){
		if (null == entity || StringUtils.isEmpty(entity.getTaskId())) {
			return MessageConstant.DELETE_INFO_NULL_MSG;
		}
		try {
			File file = new File(entity.getFilePath());
			if (file.exists()) {
				file.delete();
			}
			FileExportTaskEntity delEntity = new FileExportTaskEntity();
			delEntity.setTaskId(entity.getTaskId());
			delEntity.setDelFlag("1");
			delEntity.setLastModifier(JAppContext.currentUserName());
			delEntity.setLastModifyTime(JAppContext.currentTimestamp());
			int update = fileExportTaskService.update(delEntity);
			if (update > 0) {
				return MessageConstant.DELETE_INFO_SUCCESS_MSG;
			}else {
				return MessageConstant.DELETE_INFO_FAIL_MSG;
			}
		} catch (Exception e) {
			logger.error("删除导出任务异常：", e);
		}
		return null;
	}
}
