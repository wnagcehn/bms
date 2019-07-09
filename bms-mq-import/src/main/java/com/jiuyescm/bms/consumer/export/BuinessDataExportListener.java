/*
 * Copyright (c) 2019, Jiuye SCM and/or its affiliates. All rights reserved.
 */
package com.jiuyescm.bms.consumer.export;

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

import com.jiuyescm.bms.consumer.export.billhandler.PrepareBillHandler;

/**
 * 新预账单导出消费端
  * <功能描述>
  * 
  * @author wangchen870
  * @date 2019年4月29日 下午2:56:07
  */
@Service("buinessDataExportListener")
public class BuinessDataExportListener implements MessageListener{
    
    private static final Logger logger = LoggerFactory.getLogger(BuinessDataExportListener.class);

    @Override
    public void onMessage(Message message) {
        logger.info("--------------------MQ处理操作日志开始---------------------------");
        StopWatch sw = new StopWatch();
        sw.start();
        logger.info("预账单导出异步处理");
        String json = "";
        try {
            json = ((TextMessage)message).getText();
            logger.info("消息Json【{}】", json);
        } catch (JMSException e1) {
            logger.error("取出消息失败",e1);
            return;
        }
        //导出
        try {
            WebApplicationContext ctx = ContextLoader.getCurrentWebApplicationContext(); 
            PrepareBillHandler prepareBillHandler = (PrepareBillHandler)ctx.getBean("prepareBillHandler");
            prepareBillHandler.export(json);
        } catch (Exception e1) {
            logger.error("文件导出失败", e1);
            return;
        }
        try {
            message.acknowledge();
        } catch (JMSException e) {
            logger.error("消息应答失败",e);
        }
        sw.stop();
        logger.info("--------------------MQ处理操作日志结束,耗时:"+sw.getTotalTimeMillis()+"ms---------------");   
    }
    
}


