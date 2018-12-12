package com.jiuyescm.bms.consumer.export;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
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
import com.jiuyescm.bms.base.servicetype.entity.PubCarrierServicetypeEntity;
import com.jiuyescm.bms.base.servicetype.service.ICarrierProductService;
import com.jiuyescm.bms.base.servicetype.vo.CarrierProductVo;
import com.jiuyescm.bms.biz.dispatch.entity.BizDispatchBillPayEntity;
import com.jiuyescm.bms.biz.dispatch.service.IBizDispatchBillPayService;
import com.jiuyescm.bms.common.constants.FileConstant;
import com.jiuyescm.bms.common.enumtype.FileTaskStateEnum;
import com.jiuyescm.bms.common.enumtype.FileTaskTypeEnum;
import com.jiuyescm.common.utils.DateUtil;
import com.jiuyescm.common.utils.excel.POISXSSUtil;
import com.jiuyescm.exception.BizException;
import com.jiuyescm.mdm.warehouse.api.IWarehouseService;
import com.jiuyescm.mdm.warehouse.vo.WarehouseVo;

/**
 * 应付运单导出
 * @author wangchen870
 *
 */
@Service("dispatchBillPayExportListener")
public class DispatchBillPayExportListener implements MessageListener{

	private static final Logger logger = LoggerFactory.getLogger(DispatchBillPayExportListener.class);
	
	@Resource
	private IFileExportTaskService fileExportTaskService;
	
	@Resource
	private ISystemCodeService systemCodeService;
	
	@Resource
	private ICarrierProductService carrierProductService;
	
	@Resource
	private IBizDispatchBillPayService bizDispatchBillPayService;
	
	@Autowired
	private IWarehouseService warehouseService;
	
	@Override
	public void onMessage(Message message) {
		logger.info("--------------------MQ处理操作日志开始---------------------------");
		long start = System.currentTimeMillis();
		logger.info("应付运单导出异步处理");
		String json = "";
		try {
			json = ((TextMessage)message).getText();
			logger.info("消息Json【{}】", json);
		} catch (JMSException e1) {
			logger.info("取出消息失败");
			return;
		}
		try {
			//导出
			export(json);
		} catch (Exception e1) {
			logger.info("文件导出失败");
			return;
		}
		long end = System.currentTimeMillis();
		try {
			message.acknowledge();
		} catch (JMSException e) {
			logger.info("消息应答失败");
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
		fileExportTaskService.updateExportTask(map.get("taskId").toString(), FileTaskStateEnum.INPROCESS.getCode(), 10);
		String path = getBizPayExportPath();
		long beginTime = System.currentTimeMillis();
		
		logger.info("====应收运单导出：");
		//如果存放上传文件的目录不存在就新建
    	File storeFolder=new File(path);
		if(!storeFolder.isDirectory()){
			storeFolder.mkdirs();
		}
		
    	logger.info("====应收运单导出：写入Excel begin.");
    	fileExportTaskService.updateExportTask(map.get("taskId").toString(), null, 30);
    	POISXSSUtil poiUtil = new POISXSSUtil();
    	SXSSFWorkbook workbook = poiUtil.getXSSFWorkbook();
        //应收运单
    	fileExportTaskService.updateExportTask(map.get("taskId").toString(), null, 70);
    	try {
    		handBiz(poiUtil, workbook, map.get("filePath").toString(), map);
		} catch (Exception e) {
			fileExportTaskService.updateExportTask(map.get("taskId").toString(), FileTaskStateEnum.FAIL.getCode(), 99);
			return;
		}
    	//最后写到文件
    	fileExportTaskService.updateExportTask(map.get("taskId").toString(), null, 90);
    	poiUtil.write2FilePath(workbook, map.get("filePath").toString());
    	
    	fileExportTaskService.updateExportTask(map.get("taskId").toString(), FileTaskStateEnum.SUCCESS.getCode(), 100);
    	logger.info("====应收运单导出：写入Excel end.==总耗时：" + (System.currentTimeMillis() - beginTime));
	}
	
	/**
	 * 应付运单
	 * @param poiUtil
	 * @param workbook
	 * @param path
	 * @param myparam
	 * @throws Exception
	 */
	private void handBiz(POISXSSUtil poiUtil, SXSSFWorkbook workbook, String string, Map<String, Object> myparam) throws Exception {
		//物流商
		if(myparam.get("logistics") != null && StringUtils.equalsIgnoreCase(myparam.get("logistics").toString(), "ALL")){
			myparam.put("logistics", null);
		}
		
		//Map<String,String> serviceMap=getServiceMap();

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy K:m:s a",Locale.ENGLISH);
		Date convertCreateTime = sdf.parse(myparam.get("createTime").toString());
		Date convertEndTime = sdf.parse(myparam.get("endTime").toString());
		String startTime =formatter.format(convertCreateTime) ;
		String endTime = formatter.format(convertEndTime);
		Map<String, String> diffMap = DateUtil.getSplitTime(startTime, endTime, 4);
		List<Map<String, Object>> headDetailMapList = getBizHead(); 
		int lineNo = 1;
		for (Map.Entry<String, String> entry : diffMap.entrySet()) { 
			logger.info("startTime:["+entry.getKey()+"] endTime["+entry.getValue()+"]");
			myparam.put("createTime", entry.getKey());
			myparam.put("endTime", entry.getValue());
			int pageNo = 1;
			boolean doLoop = true;
			while (doLoop) {			
				PageInfo<BizDispatchBillPayEntity> pageInfo = bizDispatchBillPayService.queryAllToExport(myparam, pageNo, FileConstant.EXPORTPAGESIZE);
				if (null != pageInfo && pageInfo.getList().size() > 0) {
					if (pageInfo.getList().size() < FileConstant.EXPORTPAGESIZE) {
						doLoop = false;
					}else {
						pageNo += 1; 
					}
				}else {
					doLoop = false;
				}
				
				//头、内容信息
				
				List<Map<String, Object>> dataDetailList = getBizHeadItem(pageInfo.getList());
				logger.info("lineNo:"+lineNo);
				poiUtil.exportExcel2FilePath(poiUtil, workbook, FileTaskTypeEnum.BIZ_PAY_DIS.getDesc(), lineNo, headDetailMapList, dataDetailList);
				if(dataDetailList !=null){
					lineNo += dataDetailList.size();
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
		itemMap.put("title", "商家名称");
		itemMap.put("columnWidth", 25);
		itemMap.put("dataKey", "customerName");
		headInfoList.add(itemMap);
		
		itemMap = new HashMap<String, Object>();
		itemMap.put("title", "商家订单号");
		itemMap.put("columnWidth", 50);
		itemMap.put("dataKey", "externalNo");
		headInfoList.add(itemMap);
        
		itemMap = new HashMap<String, Object>();
        itemMap.put("title", "运单号");
        itemMap.put("columnWidth", 50);
        itemMap.put("dataKey", "waybillNo");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "运单数量");
        itemMap.put("columnWidth", 50);
        itemMap.put("dataKey", "waybillNum");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "运单生成时间");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "createTime");
        headInfoList.add(itemMap);
        
        /*itemMap = new HashMap<String, Object>();
        itemMap.put("title", "转寄后运单号");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "zexpressnum");
        headInfoList.add(itemMap);*/
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "大状态");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "bigstatus");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "小状态");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "smallstatus");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "系统逻辑重量");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "systemWeight");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "运单重量");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "totalWeight");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "泡重");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "originThrowWeight");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "商品数量");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "totalqty");
        headInfoList.add(itemMap);

        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "商品明细");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "productDetail");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "宅配商");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "deliverName");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "月结账号");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "monthFeeCount");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "物流产品类型");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "servicename");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "快递业务类型");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "expresstype");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "收件人");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "receiveName");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "收件人省");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "receiveProvinceId");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "收件人市");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "receiveCityId");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "收件人区县");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "receiveDistrictId");
        headInfoList.add(itemMap);
		
        return headInfoList;
	}
	
	private List<Map<String, Object>> getBizHeadItem(List<BizDispatchBillPayEntity> list) throws Exception{
		 List<Map<String, Object>> dataList = new ArrayList<Map<String,Object>>();
	        Map<String, Object> dataItem = null;
	        Map<String, String> warehouseMap = getWarehouse();
	        for (BizDispatchBillPayEntity entity : list) {
	        	dataItem = new HashMap<String, Object>();
	        	dataItem.put("warehouseName", warehouseMap.get(entity.getWarehouseCode()));
	        	dataItem.put("customerName", entity.getCustomerName());
	        	dataItem.put("outstockNo", entity.getOutstockNo());
	        	dataItem.put("externalNo", entity.getExternalNo());
	        	dataItem.put("waybillNo", entity.getWaybillNo());
	        	dataItem.put("waybillNum", entity.getWaybillNum());
	        	dataItem.put("createTime", entity.getCreateTime());
//	        	dataItem.put("zexpressnum", entity.getZexpressnum());
	        	dataItem.put("bigstatus", entity.getBigstatus());
	        	dataItem.put("smallstatus", entity.getSmallstatus());
	        	dataItem.put("systemWeight", entity.getSystemWeight());
	        	dataItem.put("totalWeight", entity.getTotalWeight());
	        	dataItem.put("originThrowWeight", entity.getOriginThrowWeight());
	        	dataItem.put("totalqty", entity.getTotalqty());
	        	dataItem.put("productDetail", entity.getProductDetail());
	        	dataItem.put("deliverName", entity.getDeliverName());
	        	dataItem.put("monthFeeCount", entity.getMonthFeeCount());
	        	dataItem.put("servicename", carrierProductService.getCarrierNameById(entity.getCarrierId(), entity.getServiceTypeCode()));	        	
	        	dataItem.put("expresstype", entity.getExpresstype());
	        	dataItem.put("receiveName", entity.getReceiveName());
	        	String provinceId="";
	    		String cityId="";
	    		String districtId="";
	    		//调整省市区不为空
	    		if(StringUtils.isNotBlank(entity.getAdjustProvinceId())||
	    				StringUtils.isNotBlank(entity.getAdjustCityId())||
	    				StringUtils.isNotBlank(entity.getAdjustDistrictId())){
	    			provinceId=entity.getAdjustProvinceId();
	    			cityId=entity.getAdjustCityId();
	    			districtId=entity.getAdjustDistrictId();
	    		}else{
	    			provinceId=entity.getReceiveProvinceId();
	    			cityId=entity.getReceiveCityId();
	    			districtId=entity.getReceiveDistrictId();
	    		}
	        	dataItem.put("receiveProvinceId", provinceId);
	        	dataItem.put("receiveCityId",cityId);
	        	dataItem.put("receiveDistrictId", districtId);
	        	dataList.add(dataItem);
			}
	        
		return dataList;
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
	
	/**
	 * 获取应付业务数据导出的文件路径
	 * @return
	 */
	public String getBizPayExportPath(){
		SystemCodeEntity systemCodeEntity = getSystemCode("GLOABL_PARAM", "EXPORT_PAY_BIZ");
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
	 * 获取仓库
	 * @return
	 */
	public Map<String,String> getWarehouse(){
		Map<String,String> map = new LinkedHashMap<String,String>();
		//仓库信息
		List<WarehouseVo> warehouseVos = warehouseService.queryAllWarehouse();
		for (WarehouseVo warehouseVo : warehouseVos) {
			map.put(warehouseVo.getWarehouseid(), warehouseVo.getWarehousename());
		}
		return map;
	}
	
}
