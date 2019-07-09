/**
 * 
 */
package com.jiuyescm.bms.file.upload.web;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Controller;

import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.data.provider.Page;
import com.bstek.dorado.uploader.DownloadFile;
import com.bstek.dorado.uploader.annotation.FileProvider;
import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.asyn.service.IBmsFileAsynTaskService;
import com.jiuyescm.bms.asyn.vo.BmsFileAsynTaskVo;
import com.jiuyescm.bms.common.constants.MessageConstant;
import com.jiuyescm.bms.common.enumtype.mq.BmsPackmaterialTaskTypeEnum;
import com.jiuyescm.bms.common.enumtype.mq.BmsPackmaterialTaskTypeNewEnum;
import com.jiuyescm.bms.common.enumtype.status.FileAsynTaskStatusEnum;
import com.jiuyescm.framework.fastdfs.client.StorageClient;
import com.jiuyescm.framework.fastdfs.protocol.storage.callback.DownloadByteArray;

/**
 * BMS异步导入任务
 * @author yangss
 */
@Controller("bmsFileAsynTaskController")
public class BmsFileAsynTaskController {

	private static final Logger logger = Logger.getLogger(BmsFileAsynTaskController.class.getName());
	
	@Autowired 
	private StorageClient storageClient;
	@Autowired
	private IBmsFileAsynTaskService bmsFileAsynTaskService;
	@Resource
	private JmsTemplate jmsQueueTemplate;

	/**
	 * 分页查询
	 * 
	 * @param page
	 * @param param
	 */
	@DataProvider
	public void query(Page<BmsFileAsynTaskVo> page, Map<String, Object> param) {
		if (null == param) {
			param = new HashMap<String, Object>();
			param.put("bizType", "IMPORT");
		}
		
		PageInfo<BmsFileAsynTaskVo> pageInfo = null;
		try {
			pageInfo = bmsFileAsynTaskService.query(param, page.getPageNo(), page.getPageSize());
			if (pageInfo != null) {
				page.setEntities(pageInfo.getList());
				page.setEntityCount((int) pageInfo.getTotal());
			}
		} catch (Exception e) {
			logger.error("BMS异步任务查询异常：", e);
		}
	}
	
	/**
	 * 下载
	 * @param parameter
	 * @return
	 * @throws IOException
	 */
	@FileProvider
	public DownloadFile downloadExcel(Map<String, String> parameter){
		final String fileName=parameter.get("excelName");
		byte[] bytes=storageClient.downloadFile(parameter.get("url"),new DownloadByteArray());
		try {
			return new DownloadFile(fileName, new ByteArrayInputStream(bytes));
		} catch (Exception e) {
			logger.error("文件下载异常：", e);
		}
		return null;
	}
	
	@DataResolver
	public String reImport(Map<String, Object> param) {
		if (null == param || StringUtils.isBlank(param.get("taskId").toString())) {
			return MessageConstant.PAGE_PARAM_ERROR_MSG;
		}
		
		final String taskId = param.get("taskId").toString();
		try {
			BmsFileAsynTaskVo taskEntity = bmsFileAsynTaskService.findByTaskId(taskId);
			if (null == taskEntity) {
				return "未查询到符合条件的任务!";
			}
			
			// 更新任务状态
			BmsFileAsynTaskVo updateEntity = new BmsFileAsynTaskVo();
			updateEntity.setTaskId(taskId);
			updateEntity.setTaskStatus(FileAsynTaskStatusEnum.WAIT.getCode());
			updateEntity.setFileRows(0);
			updateEntity.setTaskRate(0);
			updateEntity.setRemark("");
			bmsFileAsynTaskService.update(updateEntity);
			
			String taskType = taskEntity.getTaskType();
			// 写入MQ
			jmsQueueTemplate.send(taskType, new MessageCreator() {
				@Override
				public Message createMessage(Session session) throws JMSException {
					return session.createTextMessage(taskId);
				}
			});
		} catch (Exception e) {
			logger.error("再次导入异常", e);
			return "再次导入异常,请稍后重试!";
		}
				
		return "再次导入操作完成!";
	}
	
	/**
	 * 获取文件类型
	 * @return
	 */
	@DataProvider
	public Map<String,String> getFileAsynTaskTypeMap(){
		return BmsPackmaterialTaskTypeEnum.getMap();
	}
	
	/**
	 * 获取文件类型
	 * @return
	 */
	@DataProvider
	public Map<String,String> getFileAsynTaskTypeNewMap(){
		return BmsPackmaterialTaskTypeNewEnum.getMap();
	}
	
	/**
	 * 获取文件状态
	 * @return
	 */
	@DataProvider
	public Map<String,String> getFileAsynTaskStatusMap(){
		return FileAsynTaskStatusEnum.getMap();
	}
}
