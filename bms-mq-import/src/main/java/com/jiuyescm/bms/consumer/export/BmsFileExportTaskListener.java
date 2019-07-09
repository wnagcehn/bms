package com.jiuyescm.bms.consumer.export;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import javax.jms.JMSException;

import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Service("bmsFileExportTaskListener")
public class BmsFileExportTaskListener implements MessageListener{

	private static final Logger logger = LoggerFactory.getLogger(BmsFileExportTaskListener.class);

	@Override
	public void onMessage(Message message) {
		
		logger.info("--------------------MQ处理操作日志开始---------------------------");
		long start = System.currentTimeMillis();
		String taskId = null;
		try {
			taskId = ((TextMessage)message).getText();
		} catch (JMSException e1) {
			logger.error("取出消息失败",e1);
			return;
		}
		try {
			logger.info("正在消费"+taskId);
			Thread.sleep(3000);
		} catch (Exception e1) {
			logger.error("文件导出失败",e1);
			try {
				
			} catch (Exception e2) {
				logger.error("保存mq错误日志失败，错误日志：{}",e2);
			}
			return;
		}
		long end = System.currentTimeMillis();
		try {
			message.acknowledge();
		} catch (JMSException e) {
			logger.error("消息应答失败",e);
		}
		logger.info("--------------------MQ处理操作日志结束,耗时:"+(end-start)+"ms---------------");
	}
	
}
