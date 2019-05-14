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

import com.jiuyescm.bms.consumer.upload.importhandler.MaterialHandler;

/**
 * <耗材系统导入>
 * 
 * @author wangchen870
 * @date 2019年5月9日 上午9:58:40
 */
@Service("bmsPackmaterialImportTaskListener")
public class BmsPackmaterialImportTaskListener implements MessageListener{

    private static final Logger logger = LoggerFactory.getLogger(BmsPackmaterialImportTaskListener.class);
	
	@Override
	public void onMessage(Message message) {	
		logger.info("--------------------MQ处理操作日志开始---------------------------");
		StopWatch sw = new StopWatch();
		sw.start();
		String taskId = "";
		
		try {
			taskId = ((TextMessage)message).getText();
		} catch (JMSException e1) {
			logger.error("任务ID【{}】 -> 读取消息->更新任务表失败",taskId, e1);
			return;
		}
		try {
		    WebApplicationContext ctx = ContextLoader.getCurrentWebApplicationContext(); 
		    MaterialHandler materialHandler = (MaterialHandler)ctx.getBean("materialHandler");
		    materialHandler.handImportFile(taskId);
		} catch (Exception e1) {
			logger.error("任务ID【{}】 -> 异步文件处理异常{}",taskId,e1);
			return;
		}
		
		try {
			message.acknowledge();
		} catch (JMSException e) {
			logger.info("任务ID【{}】 -> 消息应答异常{}",taskId,e);
		}

		sw.stop();
		logger.info("任务ID【{}】 -> MQ处理操作日志结束,耗时【{}】毫秒",taskId,sw.getLastTaskTimeMillis());
	}

}
