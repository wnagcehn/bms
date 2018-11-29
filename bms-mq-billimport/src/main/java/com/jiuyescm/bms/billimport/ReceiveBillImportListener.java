package com.jiuyescm.bms.billimport;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.jiuyescm.bms.base.dict.api.IWarehouseDictService;
import com.jiuyescm.bms.bill.receive.entity.BillReceiveMasterEntity;
import com.jiuyescm.bms.billcheck.service.IBillReceiveMasterRecordService;
import com.jiuyescm.bms.billcheck.service.IBillReceiveMasterService;
import com.jiuyescm.bms.billcheck.vo.BillReceiveMasterVo;
import com.jiuyescm.bms.billimport.entity.BillFeesReceiveStorageTempEntity;
import com.jiuyescm.bms.excel.ExcelXlsxReader;
import com.jiuyescm.bms.excel.opc.OpcSheet;
import com.jiuyescm.framework.fastdfs.client.StorageClient;
import com.jiuyescm.mdm.warehouse.vo.WarehouseVo;
import com.jiuyescm.utils.JsonUtils;

@Service("receiveBillImportListener")
public class ReceiveBillImportListener implements MessageListener {

	private static final Logger logger = LoggerFactory
			.getLogger(ReceiveBillImportListener.class);

	@Autowired
	private StorageClient storageClient;

	@Autowired
	private IWarehouseDictService warehouseDictService;
	
	@Autowired
	private IBillReceiveMasterService billReceiveMasterService;

	private ExcelXlsxReader xlsxReader;

	@Override
	public void onMessage(Message message) {
		logger.info("应收账单导入异步处理");
		String json = "";
		try {
			json = ((TextMessage) message).getText();
			logger.info("消息Json【{}】", json);
			try {
				readExcel(json);
			} catch (Throwable e) {
				e.printStackTrace();
				logger.info("获取消息失败-{}", e);
			}
		} catch (JMSException e1) {
			logger.info("获取消息失败-{}", e1);
			return;
		}
	}


	public void readExcel(String json) throws Throwable {
		Map<String, Object> map = resolveJsonToMap(json);
		if (null == map) {
			return;
		}
		
		//MQ拿到消息，更新状态
		updateStatus(map, "PROCESS", 1);
		
		File file = new File(map.get("fullPath").toString());
		InputStream inputStream = new FileInputStream(file);
		/*
		 * byte[] bytes = storageClient.downloadFile(taskId, new
		 * DownloadByteArray()); InputStream inputStream = new
		 * ByteArrayInputStream(bytes);
		 */
		try {
			xlsxReader = new ExcelXlsxReader(inputStream);
			List<OpcSheet> sheets = xlsxReader.getSheets();
			for (OpcSheet opcSheet : sheets) {
				String sheetName = opcSheet.getSheetName();
				logger.info("准备读取sheet - {0}", sheetName);

				// 仓储--上海01仓，北京01仓...............
				WarehouseVo warehouseVo = warehouseDictService.getWarehouseByName(sheetName);
				if (null != warehouseVo.getWarehousename()) {
					sheetName = "仓储";
				}
				// 耗材使用费
				if (sheetName.contains("耗材使用费")) {
					sheetName = "耗材使用费";
				}
				IFeesHandler handler = FeesHandlerFactory.getHandler(sheetName);
				if (null == handler) {
					continue;
				}
				// handler.getRows();

				Map<String, Object> param = null;
				try {
					handler.process(xlsxReader, opcSheet, param);
				} catch (Exception ex) {
					updateStatus(map, "EXCEPTION", 99);
				}
				// saveAll 保存临时表数据到正式表
			}
			xlsxReader.close();
		} catch (Exception ex) {
			logger.error("readExcel 异常 {}", ex);
			updateStatus(map, "EXCEPTION", 99);
		}
	}

	/**
	 * 更新主表导入状态
	 * @param map
	 * @param status
	 */
	private void updateStatus(Map<String, Object> map, String status, int rate) {
		BillReceiveMasterVo entity = new BillReceiveMasterVo();
		entity.setBillNo(map.get("billNo").toString());
		entity.setTaskStatus(status);
		entity.setTaskRate(rate);
		billReceiveMasterService.update(entity);
	}

	
	/**
	 * 解析Json成Map
	 * @param json
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private Map<String, Object> resolveJsonToMap(String json) {
		//解析JSON
		Map<String, Object> map = null;
		try {
			map = (Map<String, Object>)JSON.parse(json);
		} catch (Exception e) {
			logger.error("JSON解析异常 {}", e);
			return null;
		}
		return map;
	}

}
