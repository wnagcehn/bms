package com.jiuyescm.bms.consumer.export;

import javax.jms.Message;
import javax.jms.MessageListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * 应付运单导出
 * @author wangchen870
 *
 */
@Service("dispatchBillPayExportListener")
public class DispatchBillPayExportListener implements MessageListener{

	private static final Logger logger = LoggerFactory.getLogger(DispatchBillPayExportListener.class);
	
	@Override
	public void onMessage(Message message) {
		// TODO Auto-generated method stub
		
	}
	
}
