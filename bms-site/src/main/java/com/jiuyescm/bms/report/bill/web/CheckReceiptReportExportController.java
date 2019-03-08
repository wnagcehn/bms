
package com.jiuyescm.bms.report.bill.web;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.bstek.dorado.annotation.DataResolver;
import com.jiuyescm.bms.base.dictionary.entity.SystemCodeEntity;
import com.jiuyescm.bms.base.dictionary.service.ISystemCodeService;
import com.jiuyescm.bms.billcheck.BillCheckInfoEntity;
import com.jiuyescm.bms.billcheck.service.IBillCheckInfoService;
import com.jiuyescm.bms.common.constants.ExceptionConstant;
import com.jiuyescm.bms.common.constants.FileConstant;
import com.jiuyescm.bms.common.enumtype.FileTaskTypeEnum;
import com.jiuyescm.bms.report.bill.CheckReceiptEntity;
import com.jiuyescm.common.utils.DateUtil;
import com.jiuyescm.common.utils.excel.POISXSSUtil;
import com.jiuyescm.exception.BizException;
import com.jiuyescm.framework.fastdfs.client.StorageClient;
import com.jiuyescm.framework.fastdfs.model.StorePath;

@Controller("checkReceiptReportExportController")
public class CheckReceiptReportExportController{
	
	private static final Logger logger = Logger.getLogger(CheckReceiptReportExportController.class.getName());
	
	@Resource
	private ISystemCodeService systemCodeService;
	@Autowired
	private IBillCheckInfoService billCheckInfoService;
	@Autowired 
	private StorageClient storageClient;
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
        final List<String> dateList = DateUtil.getBetweenDate(startString,endString);
        //查询区域
		Map<String, Object> map = new HashMap<>();
		map.put("typeCode", "SALE_AREA");
		map.put("deptName", deptName);
		final List<SystemCodeEntity> codeEntities = systemCodeService.queryExtattr1(map);
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
			final Map<String,CheckReceiptEntity> reportMap = new HashMap<>();
	        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			for (CheckReceiptEntity entity : checkList) {
				String key = getKey(entity.getArea(),  sdf.format(entity.getExpectDate()));
				reportMap.put(key, entity);
			}

        try {
    		new Thread(){
    			public void run() {
    				try {
    					export(dateList,codeEntities,reportMap);
    				} catch(Exception e){
    					logger.error(e.getMessage());
    				}
    			};
    		}.start();
		} catch (Exception e) {
			logger.error(ExceptionConstant.ASYN_BIZ_EXCEL_EX_MSG, e);
			}
		}
	}
	/**
	 * 导出
	 */
	private void export(List<String> dateList,List<SystemCodeEntity> codeEntities,Map<String,CheckReceiptEntity> reportMap)throws Exception{
		long beginTime = System.currentTimeMillis();
		String path = "E://test";
		logger.info("====导出：");
		//如果存放上传文件的目录不存在就新建
    	File storeFolder=new File(path);
		if(!storeFolder.isDirectory()){
			storeFolder.mkdirs();
		}
		
    	logger.info("====回款追踪报表导出：写入Excel begin.");
    	POISXSSUtil poiUtil = new POISXSSUtil();
    	SXSSFWorkbook workbook = poiUtil.getXSSFWorkbook();
		int lineNo = 1;
		//头、内容信息
		List<Map<String, Object>> headDetailMapList = getHead(dateList); 
		List<Map<String, Object>> dataDetailList = getData(dateList,codeEntities,reportMap);
		poiUtil.exportExcel2FilePath(poiUtil, workbook, FileTaskTypeEnum.CHECK_RECEIPT.getDesc(),lineNo, headDetailMapList, dataDetailList);
    	//最后写到文件
    	poiUtil.write2FilePath(workbook, "E:/test/回款追踪报表.xlsx");
    	
		// 保存导入文件到fastDFS，获取文件路径
		StorePath storePath = storageClient.uploadFile(file.getInputStream(), file.getSize(), extendFileName);
		String fullPath = storePath.getFullPath();
    	logger.info("====导出：写入Excel end.==总耗时：" + (System.currentTimeMillis() - beginTime));
	}

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
	
	private List<Map<String, Object>> getData(List<String> dateList,List<SystemCodeEntity> codeEntities,
			Map<String,CheckReceiptEntity> reportMap){
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
        	Map<String,Object> totalMap = new HashMap<>();
        	totalMap.put("totalExpect", new BigDecimal(0));
        	totalMap.put("totalFinish", new BigDecimal(0));
        	//存放合计的每一行数据map
        	Map<String,Object> totalRowMap = new HashMap<>();
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
		        	//计算合计行的合计列
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
				//先放到map里，再从map取行插入dataList，这样保证了合计行在第一位置
				totalRowMap.put(i+"", dataItem);
			}
			//取合计行数据
			for(int i = 0	;i<4;i++){
				int index =3-i;
		        @SuppressWarnings("unchecked")
				Map<String, Object> row =(Map<String, Object>) totalRowMap.get(index+"");
				dataList.add(0,row);
			}
		return dataList;
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