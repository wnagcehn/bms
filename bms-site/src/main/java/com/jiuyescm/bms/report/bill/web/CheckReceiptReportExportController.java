
package com.jiuyescm.bms.report.bill.web;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Controller;

import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.base.dictionary.entity.SystemCodeEntity;
import com.jiuyescm.bms.base.dictionary.service.ISystemCodeService;
import com.jiuyescm.bms.base.file.entity.FileExportTaskEntity;
import com.jiuyescm.bms.base.servicetype.service.ICarrierProductService;
import com.jiuyescm.bms.base.servicetype.vo.CarrierProductVo;
import com.jiuyescm.bms.base.system.BaseController;
import com.jiuyescm.bms.billcheck.BillCheckInfoEntity;
import com.jiuyescm.bms.billcheck.service.IBillCheckInfoService;
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
import com.jiuyescm.bms.common.sequence.service.SequenceService;
import com.jiuyescm.bms.report.bill.CheckReceiptEntity;
import com.jiuyescm.cfm.common.JAppContext;
import com.jiuyescm.common.ConstantInterface;
import com.jiuyescm.common.utils.DateUtil;
import com.jiuyescm.common.utils.excel.POISXSSUtil;
import com.jiuyescm.exception.BizException;

@Controller("checkReceiptReportExportController")
public class CheckReceiptReportExportController{
	
	private static final Logger logger = Logger.getLogger(CheckReceiptReportExportController.class.getName());
	
	@Resource
	private ISystemCodeService systemCodeService;
	@Autowired
	private IBillCheckInfoService billCheckInfoService;
	/**
	 * 导出
	 */
	@DataResolver
	public void asynExport(Map<String, Object> param) {
		//切割日期
		String deptName = (String) param.get("deptName");
		Date startDate = (Date) param.get("startDate");
		Date endDate = (Date) param.get("endDate");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String startString = format.format(startDate);
        String endString = format.format(endDate);
        List<String> dateList = DateUtil.getBetweenDate(startString,endString);
        //查询区域
		Map<String, Object> map = new HashMap<>();
		map.put("typeCode", "SALE_AREA");
		map.put("deptName", deptName);
		List<SystemCodeEntity> codeEntities = systemCodeService.queryExtattr1(map);
		//区域模型
		ArrayList<CheckReceiptEntity> checkList = new ArrayList<CheckReceiptEntity>();
		if (CollectionUtils.isNotEmpty(codeEntities)) {
			//查询快照
			map.put("startDate", startString);
			map.put("endDate", endString);
			List<BillCheckInfoEntity> checkInfoEntities =  billCheckInfoService.querySnapshot(map);
			//预计金额map
			Map<String,Object> expectMap = new HashMap<>();
			for (BillCheckInfoEntity billCheckInfoEntity : checkInfoEntities) {
				StringBuilder stringBuilder = new StringBuilder(billCheckInfoEntity.getArea());
				stringBuilder.append("-").append(format.format(billCheckInfoEntity.getExpectReceiptDate()));
				String key = stringBuilder.toString();
				if(expectMap.containsKey(key)){
					BigDecimal expectAmount = (BigDecimal) expectMap.get(key);
					BigDecimal add = expectAmount.add(billCheckInfoEntity.getExpectAmount());
					expectMap.put(key, add);
					continue;
				}
				expectMap.put(key, billCheckInfoEntity.getExpectAmount());
			}
			//查询回款
			List<BillCheckInfoEntity> receiptEntities =  billCheckInfoService.queryReceipt(map);
			//实际金额map
			Map<String,Object> receiptMap = new HashMap<>();
			for (BillCheckInfoEntity billCheckInfoEntity : receiptEntities) {
				StringBuilder stringBuilder = new StringBuilder(billCheckInfoEntity.getArea());
				String time = format.format(billCheckInfoEntity.getReceiptDate());
				stringBuilder.append("-").append(time);
				String key = stringBuilder.toString();
				if(receiptMap.containsKey(key)){
					BigDecimal receiptAmount = (BigDecimal) receiptMap.get(key);
					BigDecimal add = receiptAmount.add(billCheckInfoEntity.getReceiptAmount());
					receiptMap.put(key, add);
					continue;
				}
				receiptMap.put(key, billCheckInfoEntity.getReceiptAmount());
			}
			
			//初始化区域模型
			for (String dateString : dateList) {
				for (SystemCodeEntity entity : codeEntities) {
					CheckReceiptEntity checkReceiptEntity = new CheckReceiptEntity();
					checkReceiptEntity.setDeptName(deptName);
					try {
						checkReceiptEntity.setExpectDate(format.parse(dateString));
					} catch (ParseException e) {
						e.printStackTrace();
					}
					String areaString = entity.getCodeName();
					checkReceiptEntity.setArea(areaString);
					//通过key找出对应的预计金额和实际金额
					StringBuilder key = new StringBuilder(areaString);
					key.append("-").append(dateString);
					String keyString = key.toString();
					BigDecimal expectBigDecimal;
					BigDecimal receiptBigDecimal;
					if(expectMap.containsKey(keyString)){
						expectBigDecimal=(BigDecimal) expectMap.get(keyString);
					}else{
						expectBigDecimal=new BigDecimal(0);
					}
					if(receiptMap.containsKey(keyString)){
						receiptBigDecimal=(BigDecimal) receiptMap.get(keyString);
					}else{
						receiptBigDecimal=new BigDecimal(0);
					}
					checkReceiptEntity.setExpectAmount(expectBigDecimal);
					checkReceiptEntity.setFinishAmount(receiptBigDecimal);
					//计算完成率
					String finish =getFinishRate(expectBigDecimal,receiptBigDecimal);
					checkReceiptEntity.setFinishRate(finish);
					checkList.add(checkReceiptEntity);
				}
			}
			//回款追踪报表数据map
			Map<String,CheckReceiptEntity> reportMap = new HashMap<>();
	        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			for (CheckReceiptEntity entity : checkList) {
				String key = getKey(entity.getArea(),  sdf.format(entity.getExpectDate()));
				reportMap.put(key, entity);
			}
		
		

		
		
		
		
		
        try {
         	String key=sequenceService.getBillNoOne(CheckReceiptReportExportController.class.getName(), "RD", "000000000");   	
        	String path = getCheckReceiveExportPath();
        	String filepath=path+ FileConstant.SEPARATOR + 
        			FileTaskTypeEnum.BIZ_REC_DIS.getCode() + key + FileConstant.SUFFIX_XLSX;
        	
    		final Map<String, Object> condition = param;
    		final String filePath=filepath;
    		new Thread(){
    			public void run() {
    				try {
    					export(condition,filePath,dateList,codeEntities,reportMap);
    				} catch(Exception e){
    					logger.error(e.getMessage());
    				}
    			};
    		}.start();
    		
		} catch (Exception e) {
			logger.error(ExceptionConstant.ASYN_BIZ_EXCEL_EX_MSG, e);
		}
	}
	/**
	 * 异步导出
	 */
	private void export(Map<String, Object> param,String filePath,List<String> dateList,List<SystemCodeEntity> codeEntities,Map<String,CheckReceiptEntity> reportMap)throws Exception{
		String path = getCheckReceiveExportPath();
		long beginTime = System.currentTimeMillis();
		
		logger.info("====应收运单导出：");
		//如果存放上传文件的目录不存在就新建
    	File storeFolder=new File(path);
		if(!storeFolder.isDirectory()){
			storeFolder.mkdirs();
		}
		
    	logger.info("====回款追踪报表导出：写入Excel begin.");
    	POISXSSUtil poiUtil = new POISXSSUtil();
    	SXSSFWorkbook workbook = poiUtil.getXSSFWorkbook();
    	hand(poiUtil, workbook, filePath, param, dateList,codeEntities,reportMap);
    	//最后写到文件
    	poiUtil.write2FilePath(workbook, filePath);
    	
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
	private void hand(POISXSSUtil poiUtil, SXSSFWorkbook workbook,String path, Map<String, Object> myparam, List<String> dateList,List<SystemCodeEntity> codeEntities,Map<String,CheckReceiptEntity> reportMap)throws Exception{
		//物流商
		if(myparam.get("logistics") != null && 
				StringUtils.equalsIgnoreCase(myparam.get("logistics").toString(), "ALL")){
			myparam.put("logistics", null);
		}
		
		Map<String, String> temMap=getTemperatureTypeList();
		Map<String,String> b2bMap=getIstB();
		Map<String,String> orderStatusMap=getOrderStatus();
		Map<String,String> serviceMap=getServiceMap();
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
			//
			
			
			//头、内容信息
			List<Map<String, Object>> headDetailMapList = getHead(dateList); 
			List<Map<String, Object>> dataDetailList = getData(pageInfo.getList(),temMap,b2bMap,orderStatusMap,serviceMap, dateList,codeEntities,reportMap);
			
			poiUtil.exportExcel2FilePath(poiUtil, workbook, FileTaskTypeEnum.CHECK_RECEIPT.getDesc(), 
					lineNo, headDetailMapList, dataDetailList);
			if (null != pageInfo && pageInfo.getList().size() > 0) {
				lineNo += pageInfo.getList().size();
			}
		}
	}
	
	/**
	 * 运单
	 */
	public List<Map<String, Object>> getHead(List<String> dateList){
		List<Map<String, Object>> headInfoList = new ArrayList<Map<String,Object>>();
		Map<String, Object> itemMap = new HashMap<String, Object>();
		itemMap.put("title", "汇总");
		itemMap.put("columnWidth", 25);
		itemMap.put("dataKey", "area");
		headInfoList.add(itemMap);
		
		itemMap = new HashMap<String, Object>();
		itemMap.put("title", "汇总");
		itemMap.put("columnWidth", 25);
		itemMap.put("dataKey", "summary");
		headInfoList.add(itemMap);
		
		for (String date : dateList) {
			itemMap = new HashMap<String, Object>();
			itemMap.put("title", date);
			itemMap.put("columnWidth", 25);
			itemMap.put("dataKey", date);
			headInfoList.add(itemMap);
		}
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "合计");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "total");
        headInfoList.add(itemMap);
        
        return headInfoList;
	}
	
	private List<Map<String, Object>> getData(List<BizDispatchBillVo> list,Map<String, String> temMap,
			Map<String,String> b2bMap,Map<String,String> orderStatusMap,Map<String,String> serviceMap,List<String> dateList,
			List<SystemCodeEntity> codeEntities,Map<String,CheckReceiptEntity> reportMap){
		 List<Map<String, Object>> dataList = new ArrayList<Map<String,Object>>();	 
	        Map<String, Object> dataItem = null;
	        
	        //无区域
	        if(CollectionUtils.isEmpty(codeEntities)){
	    		return dataList;
	        }
	        //初始化列合计
        	Map<String,Object> totalColumnMap = new HashMap<String, Object>();
        	for (String date : dateList){
        		totalColumnMap.put(getKey("expect", date), new BigDecimal(0));
        		totalColumnMap.put(getKey("finish", date), new BigDecimal(0));
        	}
        	//遍历区域，每个区域遍历成4行，每行遍历时间成列
	        for (SystemCodeEntity areaEntities : codeEntities) {
	        	String area = areaEntities.getCodeName();
	        	//初始化行合计map（回款目标合计，实际完成合计）
	        	Map<String,Object> totalMap = new HashMap<String, Object>();
	        	totalMap.put("totalExpect", new BigDecimal(0));
	        	totalMap.put("totalFinish", new BigDecimal(0));
	        	//完成率合计
	        	String totalRate = "";
				for(int i = 0	;i<4;i++){
					//初始化行
		        	dataItem = new HashMap<String, Object>();
		        	//区域
		        	dataItem.put("area", area);
					if(i==0){
			        	dataItem.put("summary","回款目标");
			        	//时间列
			        	for (String date : dateList) {
							String key = getKey(area, date);
							BigDecimal expectAmount = reportMap.get(key).getExpectAmount();
				        	dataItem.put(date,expectAmount);
				        	//计算行合计
				        	BigDecimal lastTotal = (BigDecimal) totalMap.get("totalExpect");
							BigDecimal total = expectAmount.add(lastTotal);
							totalMap.put("totalExpect", total);
							//计算列合计
							String keyString =getKey("expect", date);
							BigDecimal lastColumnTotal =(BigDecimal) totalColumnMap.get(keyString);
							BigDecimal totalColumn = expectAmount.add(lastColumnTotal);
							totalColumnMap.put(keyString,totalColumn);
						}
			        	//合计列
			        	dataItem.put("total",(BigDecimal) totalMap.get("totalExpect"));
					}else if(i==1) {
			        	dataItem.put("summary","实际完成");
			        	for (String date : dateList) {
							String key = getKey(area, date);
							BigDecimal finishAmount = reportMap.get(key).getFinishAmount();
				        	dataItem.put(date,finishAmount);
				        	//计算行合计
				        	BigDecimal lastTotal = (BigDecimal) totalMap.get("totalFinish");
							BigDecimal total = finishAmount.add(lastTotal);
							totalMap.put("totalFinish", total);
							//计算列合计
							String keyString =getKey("finish", date);
							BigDecimal lastColumnTotal =(BigDecimal) totalColumnMap.get(keyString);
							BigDecimal totalColumn = finishAmount.add(lastColumnTotal);
							totalColumnMap.put(keyString,totalColumn);
						}
			        	dataItem.put("total",(BigDecimal) totalMap.get("totalFinish"));
					}else if(i==2) {
			        	dataItem.put("summary", "完成率");
			        	for (String date : dateList) {
							String key = getKey(area, date);
				        	dataItem.put(date,reportMap.get(key).getFinishRate());
						}
			        	dataItem.put("total",getFinishRate((BigDecimal) totalMap.get("totalExpect"),(BigDecimal) totalMap.get("totalFinish")));
					}else if(i==3) {
			        	dataItem.put("summary", "原因备注");
			        	for (String date : dateList) {
				        	dataItem.put(date,"");
						}
			        	dataItem.put("total","");
					}
					dataList.add(dataItem);
				}
			}
	        
	        //插入合计行
        	//初始化行合计map（回款目标合计，实际完成合计）
        	Map<String,Object> totalMap = new HashMap<String, Object>();
        	totalMap.put("totalExpect", new BigDecimal(0));
        	totalMap.put("totalFinish", new BigDecimal(0));
			for(int i = 0	;i<4;i++){
				//初始化行
	        	dataItem = new HashMap<String, Object>();
	        	dataItem.put("area", "合计");
				if(i==0){
		        	dataItem.put("summary","回款目标");
		        	//时间列
		        	for (String date : dateList) {
						String key = getKey("expect", date);
						BigDecimal expectAmount = (BigDecimal) totalColumnMap.get(key);
			        	dataItem.put(date,expectAmount);
			        	//计算行合计
			        	BigDecimal lastTotal = (BigDecimal) totalMap.get("totalExpect");
						BigDecimal total = expectAmount.add(lastTotal);
						totalMap.put("totalExpect", total);
					}
		        	//合计列
		        	dataItem.put("total",(BigDecimal) totalMap.get("totalExpect"));
				}else if(i==1) {
		        	dataItem.put("summary","实际完成");
		        	for (String date : dateList) {
						String key = getKey("finish", date);
						BigDecimal finishAmount = (BigDecimal) totalColumnMap.get(key);
			        	dataItem.put(date,finishAmount);
			        	//计算行合计
			        	BigDecimal lastTotal = (BigDecimal) totalMap.get("totalFinish");
						BigDecimal total = finishAmount.add(lastTotal);
						totalMap.put("totalFinish", total);
					}
		        	dataItem.put("total",(BigDecimal) totalMap.get("totalFinish"));
				}else if(i==2) {
		        	dataItem.put("summary", "完成率");
		        	for (String date : dateList) {
						String expectKey = getKey("expect", date);
						String finishKey = getKey("finish", date);
						BigDecimal expectAmount = (BigDecimal) totalColumnMap.get(expectKey);
						BigDecimal finishAmount = (BigDecimal) totalColumnMap.get(finishKey);
			        	dataItem.put(date,getFinishRate(expectAmount,finishAmount));
					}
					BigDecimal expectAmount = (BigDecimal) totalMap.get("totalExpect");
					BigDecimal finishAmount = (BigDecimal) totalMap.get("totalFinish");
		        	dataItem.put("total",getFinishRate(expectAmount,finishAmount));
				}else if(i==3) {
		        	dataItem.put("summary", "原因备注");
		        	for (String date : dateList) {
			        	dataItem.put(date,"");
					}
		        	dataItem.put("total","");
				}
				dataList.add(dataItem);
			}

	        
	        
	        
	        
	        
	        
	        
	        
	        
	        
	        
	        
	        
	        
	        
	        
	        for (BizDispatchBillVo entity : list) {
	        	dataItem = new HashMap<String, Object>();
	        	dataItem.put("warehouseName", entity.getWarehouseName());
	        	dataItem.put("customerName", entity.getCustomerName());
	        	dataItem.put("shopName", entity.getShopName());
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
	        	dataItem.put("carrierWeight", entity.getCarrierWeight());
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
	   
	        	String carrierId=StringUtils.isNotBlank(entity.getAdjustCarrierId())?entity.getAdjustCarrierId():entity.getCarrierId();        	
		        dataItem.put("servicename", serviceMap.get(entity.getCarrierId()+"&"+entity.getServiceTypeCode()));					
		        dataItem.put("adjustServiceTypeName", serviceMap.get(carrierId+"&"+entity.getAdjustServiceTypeCode()));				
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
	
	public Map<String,String> getServiceMap() throws Exception{
		Map<String,String> map=new HashMap<String,String>();
		Map<String,Object> con=new HashMap<String,Object>();
		con.put("delflag", "0");
		List<CarrierProductVo> list=carrierProductService.query(con);
		for(CarrierProductVo p:list){
			map.put(p.getCarrierid()+"&"+p.getServicecode(), p.getServicename());
		}
		return map;
	}
	
	/**
	 * 获取应收业务数据导出的文件路径
	 * @return
	 */
	public String getCheckReceiveExportPath(){
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
	
	static String getKey(String area, String date) {
		StringBuilder stringBuilder = new StringBuilder(area);
		stringBuilder.append("-").append(date);
		return stringBuilder.toString();
	}
	
	//完成率计算逻辑
	static String getFinishRate(BigDecimal expectBigDecimal, BigDecimal receiptBigDecimal) {
		String finish ="";
		if(expectBigDecimal.equals(BigDecimal.ZERO)){
			finish = "100%";
		}else if(receiptBigDecimal.equals(BigDecimal.ZERO)) {
			finish = "0%";
		}else {
			BigDecimal div = receiptBigDecimal.divide(expectBigDecimal,6, RoundingMode.HALF_UP);
			BigDecimal mul = div.multiply(new BigDecimal(100));
			String num = mul.toString();
			String numString = num.substring(0,num.length()-2);
			finish = numString+"%";
		}
		return finish;
	}
}