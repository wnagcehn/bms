package com.jiuyescm.bms.consumer.upload;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.jiuyescm.bms.asyn.service.IBmsFileAsynTaskService;
import com.jiuyescm.bms.asyn.vo.BmsFileAsynTaskVo;
import com.jiuyescm.bms.common.enumtype.status.FileAsynTaskStatusEnum;
import com.jiuyescm.bms.consumer.common.BmsMaterialImportTask;
import com.jiuyescm.excel.util.Excel07Reader;
import com.jiuyescm.framework.fastdfs.client.StorageClient;
import com.jiuyescm.framework.fastdfs.protocol.storage.callback.DownloadByteArray;

public abstract class BmsCommonImportListener implements MessageListener{

	private static final Logger logger = LoggerFactory.getLogger(BmsCommonImportListener.class);
	
	@Autowired private BmsMaterialImportTask bmsMaterialImportTaskCommon;
	@Autowired private IBmsFileAsynTaskService bmsFileAsynTaskService;
	@Autowired private StorageClient storageClient;
	
	BmsFileAsynTaskVo taskEntity = new BmsFileAsynTaskVo();
	
	public Map<Integer, String> errMap = null;
	public Map<String,Integer> repeatMap = null;
	public Excel07Reader reader = null;
	public List<String> readColumnNames = null;
	public String[] neededColumnNames = null;
	
	/**
	 * 初始化读取的列
	 * @return
	 */
	protected abstract List<String> initColumnNames();
	
	protected abstract String[] initColumnsNamesForNeed();
	
	protected abstract boolean batchHander(BmsFileAsynTaskVo taskEntity,BmsMaterialImportTask bmsMaterialImportTaskCommon) throws Exception;
	
	@Override
	public void onMessage(Message message) {
		logger.info("--------------------MQ处理操作日志开始---------------------------");
		long start = System.currentTimeMillis();
		String taskId = null;
		try {
			taskId = ((TextMessage)message).getText();
		} catch (JMSException e1) {
			logger.error("读取消息->更新任务表失败", e1);
			return;
		}
		try {
			errMap = new HashMap<Integer, String>();
			repeatMap = new HashMap<String, Integer>();
			reader = new Excel07Reader();
			handImportFile(taskId);		// 处理导入文件
		} catch (Exception e1) {
			logger.error("异步文件处理异常", e1);
			bmsMaterialImportTaskCommon.setTaskStatus(taskId, 0, FileAsynTaskStatusEnum.EXCEPTION.getCode());
			return;
		}
		
		reader = null;
		errMap = null;
		repeatMap = null;
		long end = System.currentTimeMillis();
		try {
			message.acknowledge();
		} catch (JMSException e) {
			logger.error("消息应答失败",e);
		}
		logger.info("--------------------MQ处理操作日志结束,耗时:"+(end-start)+"ms---------------");
	}
	
	protected void handImportFile(String taskId) throws Exception{
		
		//----------查询任务
		taskEntity = bmsFileAsynTaskService.findByTaskId(taskId);
		if (null == taskEntity) {
			logger.error("任务不存在");
			return;
		}
		logger.info("已领取任务 任务ID【"+taskId+"】");
		bmsMaterialImportTaskCommon.setTaskProcess(taskId, 5);
		
		try{
			//初始化需要读取的列
			readColumnNames = initColumnNames();
			neededColumnNames = initColumnsNamesForNeed();
			long start = System.currentTimeMillis();
			byte[] bytes = storageClient.downloadFile(taskEntity.getOriginFilePath(), new DownloadByteArray());
			InputStream inputStream = new ByteArrayInputStream(bytes);
			reader = new Excel07Reader(inputStream, 1, readColumnNames);
			logger.info("excel读取完成，读取行数【"+reader.getRowCount()+"】");
			logger.info("表头信息"+reader.getHeadColumn());
			BmsFileAsynTaskVo updateEntity = new BmsFileAsynTaskVo(taskEntity.getTaskId(), 30,null, reader.getRowCount(), null, null, null, null);
			bmsFileAsynTaskService.update(updateEntity);
			long end = System.currentTimeMillis();
			logger.info("*****读取excel,耗时:" + (end-start)/1000 + "秒*****");
		}
		catch(Exception ex){
			logger.error("excel解析异常--",ex);
			bmsMaterialImportTaskCommon.setTaskStatus(taskId, 20, FileAsynTaskStatusEnum.EXCEPTION.getCode());
			return;
		}
		
		if(reader.getRowCount()<=0){
			logger.info("未从excel读取到任何数据");
			bmsMaterialImportTaskCommon.setTaskStatus(taskId, 20, FileAsynTaskStatusEnum.FAIL.getCode(),"未从excel读取到任何数据");
			return;
		}
		bmsMaterialImportTaskCommon.setTaskProcess(taskId, 35);
		
		if(!bmsMaterialImportTaskCommon.checkTitle(reader.getHeadColumn(),neededColumnNames)){
			String msg = "模板列格式错误,必须包含:";
			for (String str : neededColumnNames) {
				msg+=str;
			}
			logger.info(msg);
			bmsMaterialImportTaskCommon.setTaskStatus(taskId, 32, FileAsynTaskStatusEnum.FAIL.getCode(), msg);
			return;
		}
		
		batchHander(taskEntity,bmsMaterialImportTaskCommon);
		
	}
	
	
}
