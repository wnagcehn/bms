
package com.jiuyescm.bms.biz.dispatch.web;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.stereotype.Controller;

import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.base.dictionary.entity.SystemCodeEntity;
import com.jiuyescm.bms.base.dictionary.service.ISystemCodeService;
import com.jiuyescm.bms.base.file.entity.FileExportTaskEntity;
import com.jiuyescm.bms.base.file.service.IFileExportTaskService;
import com.jiuyescm.bms.base.system.BaseController;
import com.jiuyescm.bms.biz.dispatch.service.IBizDispatchBillService;
import com.jiuyescm.bms.biz.dispatch.vo.BizDispatchBillVo;
import com.jiuyescm.bms.common.constants.ExceptionConstant;
import com.jiuyescm.bms.common.constants.FileConstant;
import com.jiuyescm.bms.common.constants.MessageConstant;
import com.jiuyescm.bms.common.enumtype.CalculateState;
import com.jiuyescm.bms.common.enumtype.FileTaskStateEnum;
import com.jiuyescm.bms.common.enumtype.FileTaskTypeEnum;
import com.jiuyescm.bms.common.enumtype.OrderStatus;
import com.jiuyescm.bms.common.log.entity.BmsErrorLogInfoEntity;
import com.jiuyescm.bms.common.log.service.IBmsErrorLogInfoService;
import com.jiuyescm.cfm.common.JAppContext;
import com.jiuyescm.common.ConstantInterface;
import com.jiuyescm.common.utils.DateUtil;
import com.jiuyescm.common.utils.excel.POISXSSUtil;

/**
 * 应收运单导出
 * @author yangss
 */
@Controller("dispatchBillExportController")
public class DispatchBillExportController extends BaseController{
	
	private static final Logger logger = Logger.getLogger(DispatchBillExportController.class.getName());
	
	@Resource
	private IBizDispatchBillService bizDispatchBillService;
	@Resource
	private IFileExportTaskService fileExportTaskService;

	@Resource
	private IBmsErrorLogInfoService bmsErrorLogInfoService;
	
	@Resource
	private ISystemCodeService systemCodeService; //业务类型
	/**
	 * 导出
	 */
	@DataResolver
	public String asynExport(Map<String, Object> param) {
		if (null == param) {
			return MessageConstant.QUERY_PARAM_NULL_MSG;
		}
		
		String customerId ="";
		if(param.get("merchantId")!=null){
			customerId=param.get("merchantId").toString();
		}

		String customerName="";
		if(param.get("customerName")!=null){
			customerName=param.get("customerName").toString();
		}
        try {
        	String path = getBizReceiveExportPath();
        	String filepath=path+ FileConstant.SEPARATOR + 
        			FileTaskTypeEnum.BIZ_REC_DIS.getCode() + customerId + FileConstant.SUFFIX_XLSX;
        	
        	FileExportTaskEntity entity = new FileExportTaskEntity();
        	entity.setCustomerid(customerId);
        	entity.setStartTime(DateUtil.formatTimestamp(param.get("createTime")));
        	entity.setEndTime(DateUtil.formatTimestamp(param.get("endTime")));
            entity.setTaskName(FileTaskTypeEnum.BIZ_REC_DIS.getDesc()+customerName+"导出");
        	entity.setTaskType(FileTaskTypeEnum.BIZ_REC_DIS.getCode());
        	entity.setTaskState(FileTaskStateEnum.BEGIN.getCode());
        	entity.setProgress(0d);
        	entity.setFilePath(filepath);
        	entity.setCreator(JAppContext.currentUserName());
        	entity.setCreateTime(JAppContext.currentTimestamp());
        	entity.setDelFlag(ConstantInterface.DelFlag.NO);
        	entity = fileExportTaskService.save(entity);
        	
        	//生成费用文件
    		final Map<String, Object> condition = param;
    		final String taskId = entity.getTaskId();
    		final String filePath=filepath;
    		new Thread(){
    			public void run() {
    				try {
    					export(condition, taskId,filePath);
    				} catch (Exception e) {
    					fileExportTaskService.updateExportTask(taskId, FileTaskStateEnum.FAIL.getCode(), 0);
    					logger.error(ExceptionConstant.ASYN_REC_DISPATCH_FEE_EXCEL_EX_MSG, e);
    					//写入日志
    					BmsErrorLogInfoEntity bmsErrorLogInfoEntity=new BmsErrorLogInfoEntity();
    					bmsErrorLogInfoEntity.setClassName("DispatchBillExportController");
    					bmsErrorLogInfoEntity.setMethodName("asynExport");
    					bmsErrorLogInfoEntity.setErrorMsg(e.toString());
    					bmsErrorLogInfoEntity.setCreateTime(JAppContext.currentTimestamp());
    					bmsErrorLogInfoService.log(bmsErrorLogInfoEntity);	
    				}
    			};
    		}.start();
		} catch (Exception e) {
			logger.error(ExceptionConstant.ASYN_BIZ_EXCEL_EX_MSG, e);
			//写入日志
			BmsErrorLogInfoEntity bmsErrorLogInfoEntity=new BmsErrorLogInfoEntity();
			bmsErrorLogInfoEntity.setClassName("DispatchBillExportController");
			bmsErrorLogInfoEntity.setMethodName("asynExport");
			bmsErrorLogInfoEntity.setIdentify("线程启动失败");
			bmsErrorLogInfoEntity.setErrorMsg(e.toString());
			bmsErrorLogInfoEntity.setCreateTime(JAppContext.currentTimestamp());
			bmsErrorLogInfoService.log(bmsErrorLogInfoEntity);	
			return ExceptionConstant.ASYN_BIZ_EXCEL_EX_MSG;
		}
		return FileTaskTypeEnum.BIZ_REC_DIS.getDesc() +customerName + MessageConstant.EXPORT_TASK_BIZ_MSG;
	}
	
	/**
	 * 异步导出
	 * @param param
	 * @param taskId
	 * @param file
	 * @throws Exception
	 */
	private void export(Map<String, Object> param,String taskId,String filePath)throws Exception{
		fileExportTaskService.updateExportTask(taskId, FileTaskStateEnum.INPROCESS.getCode(), 10);
		String path = getBizReceiveExportPath();
		long beginTime = System.currentTimeMillis();
		
		logger.info("====应收运单导出：");
		//如果存放上传文件的目录不存在就新建
    	File storeFolder=new File(path);
		if(!storeFolder.isDirectory()){
			storeFolder.mkdirs();
		}
		
    	logger.info("====应收运单导出：写入Excel begin.");
    	fileExportTaskService.updateExportTask(taskId, null, 30);
    	POISXSSUtil poiUtil = new POISXSSUtil();
    	SXSSFWorkbook workbook = poiUtil.getXSSFWorkbook();
        //应收运单
    	fileExportTaskService.updateExportTask(taskId, null, 70);
    	handBiz(poiUtil, workbook, filePath, param);
    	//最后写到文件
    	fileExportTaskService.updateExportTask(taskId, null, 90);
    	poiUtil.write2FilePath(workbook, filePath);
    	
    	fileExportTaskService.updateExportTask(taskId, FileTaskStateEnum.SUCCESS.getCode(), 100);
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
		
		int pageNo = 1;
		int lineNo = 1;
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
			
			//头、内容信息
			List<Map<String, Object>> headDetailMapList = getBizHead(); 
			List<Map<String, Object>> dataDetailList = getBizHeadItem(pageInfo.getList(),temMap,b2bMap,orderStatusMap);
			
			poiUtil.exportExcel2FilePath(poiUtil, workbook, FileTaskTypeEnum.BIZ_REC_DIS.getDesc(), 
					lineNo, headDetailMapList, dataDetailList);
			if (null != pageInfo && pageInfo.getList().size() > 0) {
				lineNo += pageInfo.getList().size();
			}
		}
	}
	
	/**
	 * 运单
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
        itemMap.put("title", "泡重");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "throwWeight");
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
	
	private List<Map<String, Object>> getBizHeadItem(List<BizDispatchBillVo> list,Map<String, String> temMap,Map<String,String> b2bMap,Map<String,String> orderStatusMap){
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
	        	dataItem.put("correctWeight", entity.getCorrectWeight());       	
	        	dataItem.put("chargeWeight", entity.getChargeWeight());
	        	dataItem.put("totalqty", entity.getTotalqty());
	        	dataItem.put("resizeNum", entity.getResizeNum());
	        	dataItem.put("totalVarieties", entity.getTotalVarieties());
	        	dataItem.put("boxnum", entity.getBoxnum());
	        	dataItem.put("adjustBoxnum", entity.getAdjustBoxnum());
	        	dataItem.put("productDetail", entity.getProductDetail());
	        	dataItem.put("monthFeeCount", entity.getMonthFeeCount());
	        	dataItem.put("expresstype", entity.getExpresstype());
	        	dataItem.put("servicename", entity.getServicename());
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
	        	dataItem.put("adjustCarrierName", entity.getAdjustCarrierName());
	        	dataItem.put("originCarrierName", entity.getOriginCarrierName());
	        	dataItem.put("chargeCarrierName", entity.getChargeCarrierName());
	        	
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