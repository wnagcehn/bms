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
import com.jiuyescm.mdm.warehouse.api.IWarehouseService;
import com.jiuyescm.mdm.warehouse.vo.WarehouseVo;


@Service("receiveBillImportListener")
public class ReceiveBillImportListener implements MessageListener{

	private static final Logger logger = LoggerFactory.getLogger(ReceiveBillImportListener.class);

	@Autowired 
	private StorageClient storageClient;
	
	@Autowired
	private IWarehouseService warehouseService;
	
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
		//File file = new File("E:\\user\\desktop\\wangchen870\\Desktop\\账单导入Test.xlsx");
		//InputStream inputStream = new FileInputStream(file);
		byte[] bytes = storageClient.downloadFile("path", new DownloadByteArray());
		InputStream inputStream = new ByteArrayInputStream(bytes);	
		try{
			xlsxReader = new ExcelXlsxReader(inputStream);
			List<OpcSheet> sheets = xlsxReader.getSheets();
			for (OpcSheet opcSheet : sheets) {
				String sheetName = opcSheet.getSheetName();
				logger.info("准备读取sheet - {0}",sheetName);
				
				//仓储--上海01仓，北京01仓...............
				WarehouseVo warehouseVo = warehouseService.queryWarehouseByWarehouseName(sheetName);
				if (null != warehouseVo.getWarehousename()) {
					sheetName = "仓储";
				}
				//耗材使用费
				if (sheetName.contains("耗材使用费")) {
					sheetName = "耗材使用费";
				}
				IFeesHandler handler = FeesHandlerFactory.getHandler(sheetName);
				if (null == handler) {
					continue;
				}
				//handler.getRows();
				handler.process(xlsxReader, opcSheet);
			}
			xlsxReader.close();
		}
		catch(Exception ex){
			logger.error("readExcel 异常 {}", ex);
		}
	}
	
	
}