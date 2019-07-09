package com.jiuyescm.bms.consumer.export;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.base.dictionary.entity.SystemCodeEntity;
import com.jiuyescm.bms.base.dictionary.service.ISystemCodeService;
import com.jiuyescm.bms.base.file.service.IFileExportTaskService;
import com.jiuyescm.bms.biz.storage.entity.BizOutstockPackmaterialEntity;
import com.jiuyescm.bms.biz.storage.service.IBizOutstockPackmaterialService;
import com.jiuyescm.bms.common.constants.FileConstant;
import com.jiuyescm.bms.common.enumtype.CalculateState;
import com.jiuyescm.bms.common.enumtype.FileTaskStateEnum;
import com.jiuyescm.bms.common.enumtype.FileTaskTypeEnum;
import com.jiuyescm.common.utils.DateUtil;
import com.jiuyescm.common.utils.excel.POISXSSUtil;
import com.jiuyescm.exception.BizException;

/**
 * 耗材出库明细导出
 * @author wangchen870
 *
 */

@Service("bizOutStockPackMaterialExportListener")
public class BizOutStockPackMaterialExportListener implements MessageListener{

	private static final Logger logger = LoggerFactory.getLogger(BizOutStockPackMaterialExportListener.class);
	
	@Resource
	private IFileExportTaskService fileExportTaskService;
	
	@Resource
	private ISystemCodeService systemCodeService;
	
	@Autowired
	private IBizOutstockPackmaterialService service;
	
	@Override
	public void onMessage(Message message) {
		logger.info("--------------------MQ处理操作日志开始---------------------------");
		long start = System.currentTimeMillis();
		logger.info("耗材导出异步处理");
		String json = "";
		try {
			json = ((TextMessage)message).getText();
			logger.info("消息Json【{}】", json);
		} catch (JMSException e1) {
			logger.error("取出消息失败",e1);
			return;
		}
		try {
			//导出
			export(json);
		} catch (Exception e1) {
			logger.error("文件导出失败",e1);
			return;
		}
		long end = System.currentTimeMillis();
		try {
			message.acknowledge();
		} catch (JMSException e) {
			logger.error("消息应答失败",e);
		}
		logger.info("--------------------MQ处理操作日志结束,耗时:"+(end-start)+"ms---------------");	
	}
	
	public void export(String json) throws IOException{
		logger.info("JSON开始解析……");
		Map<String, Object> map = resolveJsonToMap(json);
		if (null == map) {
			return;
		}
		//拿到TaskId,更新状态
		logger.info("====taskId:" + map.get("taskId").toString());
		String taskId = map.get("taskId").toString();
		String filePath = map.get("filePath").toString();
		fileExportTaskService.updateExportTask(taskId, FileTaskStateEnum.INPROCESS.getCode(), 10);
		String path = getBizReceiveExportPath();
		long beginTime = System.currentTimeMillis();
		
		logger.info("====应收商品按托库存导出：");
		//如果存放上传文件的目录不存在就新建
    	File storeFolder=new File(path);
		if(!storeFolder.isDirectory()){
			storeFolder.mkdirs();
		}
		
    	logger.info("====应收商品按托库存导出：写入Excel begin.");
    	fileExportTaskService.updateExportTask(taskId, null, 30);
    	POISXSSUtil poiUtil = new POISXSSUtil();
    	SXSSFWorkbook workbook = poiUtil.getXSSFWorkbook();
        //干线运单
    	fileExportTaskService.updateExportTask(taskId, null, 70);
    	try {
    		handBiz(poiUtil, workbook, filePath, map);
		} catch (Exception e) {
			fileExportTaskService.updateExportTask(taskId, FileTaskStateEnum.FAIL.getCode(), 99);
			return;
		}	
    	//最后写到文件
    	fileExportTaskService.updateExportTask(taskId, null, 90);
    	poiUtil.write2FilePath(workbook, filePath);
    	
    	fileExportTaskService.updateExportTask(taskId, FileTaskStateEnum.SUCCESS.getCode(), 100);
    	logger.info("====应收商品按托库存导出：写入Excel end.==总耗时：" + (System.currentTimeMillis() - beginTime));
	}
	
	private void handBiz(POISXSSUtil poiUtil, SXSSFWorkbook workbook, String filePath, Map<String, Object> myparam) throws ParseException, IOException {
		if("ALL".equals(myparam.get("isCalculated"))){
			myparam.put("isCalculated", "");
		}
		
		//切分时间
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy K:m:s a",Locale.ENGLISH);
		Date convertCreateTime = sdf.parse(myparam.get("startTime").toString());
		Date convertEndTime = sdf.parse(myparam.get("endTime").toString());
		String startTime =formatter.format(convertCreateTime) ;
		String endTime = formatter.format(convertEndTime);
		Map<String, String> diffMap = DateUtil.getSplitTime(startTime, endTime, 4);
		List<Map<String, Object>> headDetailMapList = getBizHead();
		int lineNo = 1;
		for (Map.Entry<String, String> entry : diffMap.entrySet()) { 
			logger.info("startTime:["+entry.getKey()+"] endTime["+entry.getValue()+"]");
			myparam.put("startTime", entry.getKey());
			myparam.put("endTime", entry.getValue());
			int pageNo = 1;
			boolean doLoop = true;
			while (doLoop) {			
				PageInfo<BizOutstockPackmaterialEntity> pageInfo = 
						service.query(myparam, pageNo, FileConstant.EXPORTPAGESIZE);
				if (null != pageInfo && pageInfo.getList().size() > 0) {
					if (pageInfo.getList().size() < FileConstant.EXPORTPAGESIZE) {
						doLoop = false;
					}else {
						pageNo += 1; 
					}
				}else {
					doLoop = false;
				}
				
				//内容信息
				List<Map<String, Object>> dataDetailList = getBizHeadItem(pageInfo.getList());
				
				poiUtil.exportExcel2FilePath(poiUtil, workbook, FileTaskTypeEnum.BIZ_PACK_OUTSTOCK.getDesc(), 
						lineNo, headDetailMapList, dataDetailList);
				if (null != pageInfo && pageInfo.getList().size() > 0) {
					lineNo += pageInfo.getList().size();
				}
			}
		}	
	}
	
	/**
	 * 干线运单
	 */
	public List<Map<String, Object>> getBizHead(){
		List<Map<String, Object>> headInfoList = new ArrayList<Map<String,Object>>();
		Map<String, Object> itemMap = new HashMap<String, Object>();
		itemMap.put("title", "仓库");
		itemMap.put("columnWidth", 25);
		itemMap.put("dataKey", "warehouseName");
		headInfoList.add(itemMap);
		
		itemMap = new HashMap<String, Object>();
		itemMap.put("title", "出库单号");
		itemMap.put("columnWidth", 25);
		itemMap.put("dataKey", "outstockNo");
		headInfoList.add(itemMap);
        
		itemMap = new HashMap<String, Object>();
        itemMap.put("title", "运单号");
        itemMap.put("columnWidth", 50);
        itemMap.put("dataKey", "waybillNo");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "商家");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "customerName");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "耗材编码");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "consumerMaterialCode");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "耗材名称");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "consumerMaterialName");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "规格说明");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "specDesc");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "数量");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "num");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "调整数量");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "adjustNum");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "创建时间");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "createTime");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "创建者");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "creator");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "状态");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "isCalculated");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "费用编号");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "feesNo");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "重量");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "weight");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "备注");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "remark");
        headInfoList.add(itemMap);
        return headInfoList;
	}
	
	private List<Map<String, Object>> getBizHeadItem(List<BizOutstockPackmaterialEntity> list){
		 List<Map<String, Object>> dataList = new ArrayList<Map<String,Object>>();
	        Map<String, Object> dataItem = null;
	        Map<String, String> calculateMap = CalculateState.getMap();
	        for (BizOutstockPackmaterialEntity entity : list) {
	        	dataItem = new HashMap<String, Object>();
	        	dataItem.put("warehouseName", entity.getWarehouseName());
	        	dataItem.put("outstockNo", entity.getOutstockNo());
	        	dataItem.put("waybillNo", entity.getWaybillNo());
	        	dataItem.put("customerName", entity.getCustomerName());
	        	dataItem.put("consumerMaterialCode", entity.getConsumerMaterialCode());
	        	dataItem.put("consumerMaterialName", entity.getConsumerMaterialName());
	        	dataItem.put("specDesc", entity.getSpecDesc());
	        	dataItem.put("num", entity.getNum());
	        	dataItem.put("adjustNum", entity.getAdjustNum());
	        	dataItem.put("createTime", entity.getCreateTime());
	        	dataItem.put("creator", entity.getCreator());
	        	dataItem.put("isCalculated", calculateMap.get(entity.getIsCalculated()));
	        	dataItem.put("feesNo", entity.getFeesNo());
	        	dataItem.put("weight", entity.getWeight());
	        	dataItem.put("remark", entity.getRemark());
	        	dataList.add(dataItem);
			}
	        
		return dataList;
	}

	/**
	 * 获取应收业务数据导出的文件路径
	 * @return
	 */
	public String getBizReceiveExportPath(){
		SystemCodeEntity systemCodeEntity = getSystemCode("GLOABL_PARAM", "EXPORT_RECEIVE_BIZ");
		return systemCodeEntity.getExtattr1();
	}
	
	/**
	 * 获取系统参数对象
	 * @param typeCode
	 * @param code
	 * @return
	 */
	public SystemCodeEntity getSystemCode(String typeCode, String code){
		if (StringUtils.isNotEmpty(typeCode) && StringUtils.isNotEmpty(code)) {
			SystemCodeEntity systemCodeEntity = systemCodeService.getSystemCode(typeCode, code);
			if(systemCodeEntity == null){
				throw new BizException("请在系统参数中配置文件上传路径,参数" + typeCode + "," + code);
			}
			return systemCodeEntity;
		}
		return null;
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
