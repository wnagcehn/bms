package com.jiuyescm.bms.consumer.export;

import java.io.File;
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
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.bstek.dorado.annotation.DataProvider;
import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.base.dictionary.entity.SystemCodeEntity;
import com.jiuyescm.bms.base.dictionary.service.ISystemCodeService;
import com.jiuyescm.bms.base.file.service.IFileExportTaskService;
import com.jiuyescm.bms.base.servicetype.service.ICarrierProductService;
import com.jiuyescm.bms.biz.dispatch.service.IBizDispatchBillService;
import com.jiuyescm.bms.biz.dispatch.vo.BizDispatchBillVo;
import com.jiuyescm.bms.common.constants.FileConstant;
import com.jiuyescm.bms.common.enumtype.CalculateState;
import com.jiuyescm.bms.common.enumtype.FileTaskStateEnum;
import com.jiuyescm.bms.common.enumtype.FileTaskTypeEnum;
import com.jiuyescm.bms.common.enumtype.OrderStatus;
import com.jiuyescm.common.utils.DateUtil;
import com.jiuyescm.common.utils.excel.POISXSSUtil;
import com.jiuyescm.exception.BizException;

/**
 * 应收运单导出
 * @author wangchen870
 *
 */

@Service("dispatchBillExportListener")
public class DispatchBillExportListener implements MessageListener{

	private static final Logger logger = LoggerFactory.getLogger(DispatchBillExportListener.class);
	
	@Resource
	private IFileExportTaskService fileExportTaskService;
	
	@Resource
	private ISystemCodeService systemCodeService;
	
	@Resource
	private IBizDispatchBillService bizDispatchBillService;
	
	@Resource
	private ICarrierProductService carrierProductService;
	
	@Override
	public void onMessage(Message message) {
		logger.info("--------------------MQ处理操作日志开始---------------------------");
		long start = System.currentTimeMillis();
		logger.info("应收运单导出异步处理");
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
	
	/**
	 * 异步导出
	 * @param json
	 * @throws Exception 
	 */
	private void export(String json) throws Exception{
		logger.info("JSON开始解析……");
		Map<String, Object> map = resolveJsonToMap(json);
		if (null == map) {
			return;
		}
		//拿到TaskId,更新状态
		logger.info("====taskId:" + map.get("taskId").toString());
		fileExportTaskService.updateExportTask(map.get("taskId").toString(), FileTaskStateEnum.INPROCESS.getCode(), 10);
		String path = getBizReceiveExportPath();
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
	 * 应收运单
	 * @param poiUtil
	 * @param workbook
	 * @param path
	 * @param myparam
	 * @throws Exception
	 */
	private void handBiz(POISXSSUtil poiUtil, SXSSFWorkbook workbook, 
			String path, Map<String, Object> myparam)throws Exception{
		//物流商
		if(myparam.get("logistics") != null && 
				StringUtils.equalsIgnoreCase(myparam.get("logistics").toString(), "ALL")){
			myparam.put("logistics", null);
		}
		
		Map<String, String> temMap=getTemperatureTypeList();
		Map<String,String> b2bMap=getIstB();
		Map<String,String> orderStatusMap=getOrderStatus();
		
		//切分时间
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy K:m:s a",Locale.ENGLISH);
		Date convertCreateTime = sdf.parse(myparam.get("createTime").toString());
		Date convertEndTime = sdf.parse(myparam.get("endTime").toString());
		String startTime =formatter.format(convertCreateTime) ;
		String endTime = formatter.format(convertEndTime);
		Map<String, String> diffMap = DateUtil.getSplitTime(startTime, endTime, 4);
		//抬头信息
		List<Map<String, Object>> headDetailMapList = getBizHead();
		
		int lineNo = 1;
		for (Map.Entry<String, String> entry : diffMap.entrySet()) { 
			logger.info("startTime:["+entry.getKey()+"] endTime["+entry.getValue()+"]");
			myparam.put("createTime", entry.getKey());
			myparam.put("endTime", entry.getValue());
			int pageNo = 1;
			boolean doLoop = true;
			while (doLoop) {			
				PageInfo<BizDispatchBillVo> pageInfo = 
						bizDispatchBillService.queryData(myparam, pageNo, FileConstant.EXPORTPAGESIZE);
				if (null != pageInfo && pageInfo.getList().size() > 0) {
					if (pageInfo.getList().size() < FileConstant.EXPORTPAGESIZE) {
						doLoop = false;
					}else {
						pageNo += 1; 
					}
				}else {
					doLoop = false;
				}
				
				//内容
				List<Map<String, Object>> dataDetailList = getBizHeadItem(pageInfo.getList(),temMap,b2bMap,orderStatusMap);
				
				poiUtil.exportExcel2FilePath(poiUtil, workbook, FileTaskTypeEnum.BIZ_REC_DIS.getDesc(), 
						lineNo, headDetailMapList, dataDetailList);
				if (null != pageInfo && pageInfo.getList().size() > 0) {
					lineNo += pageInfo.getList().size();
				}
			}
		}
	}
	
	/**
	 * 抬头
	 * @return
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
		itemMap.put("title", "九曳订单号");
		itemMap.put("columnWidth", 25);
		itemMap.put("dataKey", "outstockNo");
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
        itemMap.put("title", "转寄后运单号");
        itemMap.put("columnWidth", 50);
        itemMap.put("dataKey", "zexpressnum");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "原始重量");
        itemMap.put("columnWidth", 50);
        itemMap.put("dataKey", "originWeight");
        headInfoList.add(itemMap);
        
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
        itemMap.put("title", "订单状态");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "orderStatus");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "B2B标识");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "b2bFlag");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "温度类型");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "temperatureTypeCode");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "逻辑重量");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "systemWeight");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "运单重量");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "totalWeight");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "调整重量");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "adjustWeight");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "原始泡重");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "throwWeight");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "纠正泡重");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "correctThrowWeight");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "纠正重量");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "correctWeight");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "计费重量");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "chargeWeight");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "商品数量");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "totalqty");
        headInfoList.add(itemMap);
         
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "调整数量");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "resizeNum");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "总品种数");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "totalVarieties");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "装箱数");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "boxnum");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "调整箱数");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "adjustBoxnum");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "商品明细");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "productDetail");
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
        itemMap.put("title", "调整物流产品类型");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "adjustServiceTypeName");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "发件人省");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "sendProvinceId");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "发件人市");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "sendCityId");
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
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "运单生成时间");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "createTime");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "揽收时间");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "acceptTime");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "签收时间");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "signTime");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "宅配商名称");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "deliverName");
        headInfoList.add(itemMap);
              
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "实际物流商");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "carrierName");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "订单物流商");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "originCarrierName");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "调整物流商");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "adjustCarrierName");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "计费物流商");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "chargeCarrierName");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "责任方");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "dutyType");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "原因详情");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "updateReasonDetail");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "首重单价");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "headPrice");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "续重单价");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "continuedPrice");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "运费");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "dsAmount");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "折扣后运费");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "discountAmount");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "运费计算状态");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "dsIsCalculated");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "运费计算备注");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "dsRemark");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "操作费");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "orderAmount");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "折扣后操作费");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "operateAmount");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "操作费计算状态");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "orderIsCalculated");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "操作费计算备注");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "orderRemark");
        headInfoList.add(itemMap);
        
        return headInfoList;
	}
	
	/**
	 * 数据
	 * @param list
	 * @param temMap
	 * @param b2bMap
	 * @param orderStatusMap
	 * @return
	 * @throws Exception 
	 */
	private List<Map<String, Object>> getBizHeadItem(List<BizDispatchBillVo> list,Map<String, String> temMap,Map<String,String> b2bMap,Map<String,String> orderStatusMap) throws Exception{
		 List<Map<String, Object>> dataList = new ArrayList<Map<String,Object>>();	 
	        Map<String, Object> dataItem = null;
	        for (BizDispatchBillVo entity : list) {
	        	dataItem = new HashMap<String, Object>();
	        	dataItem.put("warehouseName", entity.getWarehouseName());
	        	dataItem.put("customerName", entity.getCustomerName());
	        	dataItem.put("outstockNo", entity.getOutstockNo());
	        	dataItem.put("externalNo", entity.getExternalNo());
	        	dataItem.put("waybillNo", entity.getWaybillNo());
	        	dataItem.put("zexpressnum", entity.getZexpressnum());
	        	dataItem.put("originWeight", entity.getOriginWeight());
	        	dataItem.put("bigstatus", entity.getBigstatus());
	        	dataItem.put("smallstatus", entity.getSmallstatus());
	        	if(StringUtils.isNotBlank(entity.getOrderStatus())){
		        	dataItem.put("orderStatus", orderStatusMap.get(entity.getOrderStatus()));
	        	}
	        	dataItem.put("b2bFlag",b2bMap.get(entity.getB2bFlag()));
	        	dataItem.put("temperatureTypeCode", temMap.get(entity.getTemperatureTypeCode()));
	        	dataItem.put("systemWeight", entity.getSystemWeight());
	        	dataItem.put("totalWeight", entity.getTotalWeight());
	        	dataItem.put("adjustWeight", entity.getAdjustWeight());
	        	dataItem.put("throwWeight", entity.getThrowWeight());
	        	dataItem.put("correctThrowWeight", entity.getCorrectThrowWeight());
	        	dataItem.put("correctWeight", entity.getCorrectWeight());       	
	        	dataItem.put("chargeWeight", entity.getChargeWeight());
	        	dataItem.put("totalqty", entity.getTotalqty());
	        	dataItem.put("resizeNum", entity.getResizeNum());
	        	dataItem.put("totalVarieties", entity.getTotalVarieties());
	        	dataItem.put("boxnum", entity.getBoxnum());
	        	dataItem.put("adjustBoxnum", entity.getAdjustBoxnum());
	        	dataItem.put("productDetail", entity.getProductDetail());
	        	dataItem.put("monthFeeCount", entity.getMonthFeeCount());
	        	//dataItem.put("expresstype", entity.getExpresstype());
	        	dataItem.put("sendProvinceId", entity.getSendProvinceId());
	        	dataItem.put("sendCityId", entity.getSendCityId());        	
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
        		dataItem.put("receiveCityId", cityId);
        		dataItem.put("receiveDistrictId",districtId);
	
	        	dataItem.put("createTime", entity.getCreateTime());	
	        	dataItem.put("acceptTime", entity.getAcceptTime());
	        	dataItem.put("signTime", entity.getSignTime());     	
	        	dataItem.put("deliverName", entity.getDeliverName());
	        	dataItem.put("carrierName", entity.getCarrierName());
	        	if (StringUtils.isNotBlank(entity.getAdjustCarrierId()) && StringUtils.isNotBlank(entity.getAdjustServiceTypeCode())) {
					dataItem.put("adjustServiceTypeName", carrierProductService.getCarrierNameById(entity.getAdjustCarrierId(), entity.getAdjustServiceTypeCode()));
				}else if (StringUtils.isBlank(entity.getAdjustCarrierId()) && StringUtils.isNotBlank(entity.getAdjustServiceTypeCode())) {
					dataItem.put("adjustServiceTypeName", carrierProductService.getCarrierNameById(entity.getCarrierId(), entity.getAdjustServiceTypeCode()));
				}else {
					dataItem.put("adjustServiceTypeName", "");
				}
	        	if (StringUtils.isBlank(entity.getAdjustCarrierId()) && StringUtils.isBlank(entity.getAdjustServiceTypeCode())) {
					dataItem.put("servicename", carrierProductService.getCarrierNameById(entity.getCarrierId(), entity.getServicecode()));
				}else if (StringUtils.isNotBlank(entity.getAdjustCarrierId()) && StringUtils.isBlank(entity.getAdjustServiceTypeCode())) {
					dataItem.put("servicename", carrierProductService.getCarrierNameById(entity.getAdjustCarrierId(), entity.getServicecode()));
				}else {
					dataItem.put("servicename", "");
				}
	        	dataItem.put("adjustCarrierName", entity.getAdjustCarrierName());
	        	dataItem.put("originCarrierName", entity.getOriginCarrierName());
	        	dataItem.put("chargeCarrierName", entity.getChargeCarrierName());
//	        	dataItem.put("servicename", entity.getServicename());
//	        	dataItem.put("adjustServiceTypeName", entity.getAdjustServiceTypeName());
	        	dataItem.put("operateAmount", entity.getOperateAmount());
        		dataItem.put("dutyType", entity.getDutyType());
	        	dataItem.put("updateReasonDetail", entity.getUpdateReasonDetail());
	        	dataItem.put("headPrice", entity.getHeadPrice());
	        	dataItem.put("continuedPrice", entity.getContinuedPrice());
	        	dataItem.put("dsAmount", entity.getDsAmount());
	        	dataItem.put("discountAmount", entity.getDiscountAmount());
	        	dataItem.put("dsIsCalculated", CalculateState.getMap().get(entity.getDsIsCalculated()));
	        	dataItem.put("dsRemark", entity.getDsRemark());
	        	dataItem.put("orderAmount", entity.getOrderAmount());
	        	dataItem.put("orderIsCalculated", CalculateState.getMap().get(entity.getOrderIsCalculated()));
	        	dataItem.put("orderRemark", entity.getOrderRemark());
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
			logger.error("JSON解析异常", e);
			return null;
		}
		return map;
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
	@SuppressWarnings("deprecation")
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
	 * 温度类型
	 * @return
	 */
	@DataProvider
	public Map<String, String> getTemperatureTypeList(){
		List<SystemCodeEntity> systemCodeList = systemCodeService.findEnumList("TEMPERATURE_TYPE");
		Map<String, String> map =new LinkedHashMap<String,String>();
		if(systemCodeList!=null && systemCodeList.size()>0){
			for(int i=0;i<systemCodeList.size();i++){
				map.put(systemCodeList.get(i).getCode(), systemCodeList.get(i).getCodeName());
			}
		}
		return map;
	}
	
	/**
	 * B2B标识
	 * @return
	 */
	@DataProvider
	public Map<String,String> getIstB(){
		Map<String, String> mapValue = new LinkedHashMap<String, String>();
		mapValue.put("1", "B2B");
		mapValue.put("0", "B2C");
		return mapValue;
	}
	
	/**
	 * B2B标识
	 * @return
	 */
	@DataProvider
	public Map<String,String> getOrderStatus(){	
		return OrderStatus.getMap();
	}

}
