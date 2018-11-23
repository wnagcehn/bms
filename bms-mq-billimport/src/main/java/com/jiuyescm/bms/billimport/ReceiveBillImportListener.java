package com.jiuyescm.bms.billimport;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyescm.bms.excel.ExcelXlsxReader;
import com.jiuyescm.bms.excel.opc.OpcSheet;
import com.jiuyescm.framework.fastdfs.client.StorageClient;
import com.jiuyescm.framework.fastdfs.protocol.storage.callback.DownloadByteArray;


@Service("receiveBillImportListener")
public class ReceiveBillImportListener implements MessageListener{

	private static final Logger logger = LoggerFactory.getLogger(ReceiveBillImportListener.class);

	/*@Autowired 
	private StorageClient storageClient;*/
	
	private ExcelXlsxReader xlsxReader;
	
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
	
	@SuppressWarnings("unused")
	public void readExcel(String taskId) throws Throwable{
		File file = new File("E:\\user\\desktop\\wangchen870\\Desktop\\账单导入Test.xlsx");
		//byte[] bytes = storageClient.downloadFile("E:\\user\\desktop\\wangchen870\\Desktop\\账单导入Test.xlsx", new DownloadByteArray());
		//InputStream inputStream = new ByteArrayInputStream(bytes);
		InputStream inputStream = new FileInputStream(file);
		try{
			xlsxReader = new ExcelXlsxReader(inputStream);
			List<OpcSheet> sheets = xlsxReader.getSheets();
			for (OpcSheet opcSheet : sheets) {
				String sheetName = opcSheet.getSheetName();
				logger.info("准备读取sheet - {0}",sheetName);
				IFeesHandler handler = FeesHandlerFactory.getHandler(sheetName);
				handler.getRows();
				//handler.process(xlsxReader, opcSheet);
			}
			xlsxReader.close();
		}
		catch(Exception ex){
			logger.error("readExcel 异常 {}", ex);
		}
	}
	
	
}
