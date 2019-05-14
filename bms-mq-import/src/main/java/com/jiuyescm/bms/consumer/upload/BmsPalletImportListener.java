/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.consumer.upload;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.jiuyescm.bms.consumer.upload.importhandler.PalletHandler;

/**
 * <托数导入>
 * 
 * @author wangchen870
 * @date 2019年5月9日 上午9:58:40
 */
@Service("bmsPalletImportListener")
public class BmsPalletImportListener implements MessageListener{

	private static final Logger logger = LoggerFactory.getLogger(BmsPalletImportListener.class);
	
	@Override
	public void onMessage(Message message) {
		logger.info("--------------------MQ处理操作日志开始---------------------------");
		StopWatch sw = new StopWatch();
		sw.start();
		String taskId = "";
		
		try {
			taskId = ((TextMessage)message).getText();
		} catch (JMSException e1) {
			logger.error("读取消息->更新任务表失败", e1);
			return;
		}
		try {
		    WebApplicationContext ctx = ContextLoader.getCurrentWebApplicationContext(); 
		    PalletHandler palletHandler = (PalletHandler)ctx.getBean("palletHandler");
		    palletHandler.handImportFile(taskId);
		} catch (Exception e1) {
			logger.error("异步文件处理异常", e1);
			return;
		}	
		
		try {
			message.acknowledge();
		} catch (JMSException e) {
			logger.info("消息应答失败");
		}
		
		sw.stop();
		logger.info("--------------------MQ处理操作日志结束,耗时:"+sw.getTotalTimeMillis()+"ms---------------");
	}
	
}


