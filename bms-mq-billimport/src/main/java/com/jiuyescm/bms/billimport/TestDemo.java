package com.jiuyescm.bms.billimport;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


@Service("testDemo")
public class TestDemo implements MessageListener{

	private static final Logger logger = LoggerFactory.getLogger(TestDemo.class);

	@Override
	public void onMessage(Message message) {
		
		logger.info("处理mq消息...");
		try {
			String taskId = ((TextMessage)message).getText();
			logger.info("消息id【{}】",taskId);
		} catch (JMSException e1) {
			logger.info("获取消息失败-{}",e1);
			return;
		}
	}
}
