package com.jiuyescm.bms.consumer.export;

import javax.jms.Message;
import javax.jms.MessageListener;

import org.springframework.stereotype.Service;

/**
 * 预账单导出
 * @author wangchen870
 *
 */
@Service("buinessDataExportListener")
public class BuinessDataListener implements MessageListener{

	@Override
	public void onMessage(Message message) {
		// TODO Auto-generated method stub
		
	}

}
