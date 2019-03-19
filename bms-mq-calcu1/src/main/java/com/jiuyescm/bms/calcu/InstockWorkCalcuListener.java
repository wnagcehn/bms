package com.jiuyescm.bms.calcu;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

@Service("instockWorkCalcuListener")
public class InstockWorkCalcuListener implements MessageListener{

	private static final Logger logger = Logger.getLogger(InstockWorkCalcuListener.class.getName());
	
	@Override
	public void onMessage(Message message) {
		logger.info("--------------------MQ入库操作费---------------------------");
		try {
			String taskId = ((TextMessage)message).getText();
			logger.info(taskId);
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
