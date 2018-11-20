package com.jiuyescm.bms.billimport;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyescm.framework.fastdfs.client.StorageClient;
import com.jiuyescm.framework.fastdfs.protocol.storage.callback.DownloadByteArray;


@Service("receiveBillImportListener")
public class ReceiveBillImportListener implements MessageListener{

	private static final Logger logger = LoggerFactory.getLogger(ReceiveBillImportListener.class);

	@Autowired private StorageClient storageClient;
	
	@Override
	public void onMessage(Message message) {
		logger.info("应收账单导入异步处理");
		String taskId = "";
		try {
			taskId = ((TextMessage)message).getText();
			logger.info("消息id【{}】",taskId);
		} catch (JMSException e1) {
			logger.info("获取消息失败-{}",e1);
			return;
		}
	}
	
	private void readExcel(String taskId){
		byte[] bytes = storageClient.downloadFile("path", new DownloadByteArray());
		InputStream inputStream = new ByteArrayInputStream(bytes);
		
	}
	
	
}
