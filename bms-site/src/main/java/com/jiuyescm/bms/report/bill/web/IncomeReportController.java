package com.jiuyescm.bms.report.bill.web;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.data.provider.Page;
import com.bstek.dorado.uploader.DownloadFile;
import com.bstek.dorado.uploader.annotation.FileProvider;
import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.billcheck.BillCheckInfoEntity;
import com.jiuyescm.bms.billcheck.service.IBillCheckInfoService;
import com.jiuyescm.bms.common.constants.FileConstant;
import com.jiuyescm.bms.excel.write.SXSSFExporter;
import com.jiuyescm.common.tool.ListTool;

@Controller("incomeReportController")
public class IncomeReportController {
	private static final Logger logger = Logger.getLogger(IncomeReportController.class.getName());

	@Autowired
	private IBillCheckInfoService billCheckInfoService;
	
	private  List<BillCheckInfoEntity> queryReport(Map<String, Object> parameter){
		//报表数据list
		List<BillCheckInfoEntity> reportList = new ArrayList<>();
		
		logger.info("前台传入信息："+parameter);
		String deptName = (String) parameter.get("deptName");
		String startDate = (String) parameter.get("startDate");
		String endDate =  (String) parameter.get("endDate");
		List<String> dateList =  getMonth(startDate,endDate);

		//销售人员不一样时，不应算新增收入
		//按照月份一次次查询
		//查询条件
		Map<String, Object> condition = new HashMap<>();
		condition.put("deptName", deptName);
		for (int i = 0; i < dateList.size()-1; i++) {
			//不考虑1901以前的数据
			String string = dateList.get(i);
			Integer integer = Integer.valueOf(string);
			if(integer<1812){
				continue;
			}
			condition.put("startBizDate", string);
			//查出新增商家
			List<BillCheckInfoEntity> bizEntities =  billCheckInfoService.queryIncomeReportBizCus(condition);
			//如果新增商家为空跳过本次循环
			if(CollectionUtils.isEmpty(bizEntities)){
				continue;
			}
			List<String> bizNameList = new ArrayList<>();
			//key为开票名称，销售id为值
			Map<String,String> bizNameMap = new HashMap<>();
			for (BillCheckInfoEntity entity : bizEntities) {
				String nameString = entity.getInvoiceName();
				bizNameList.add(nameString);
				bizNameMap.put(nameString, entity.getSellerId());
			}
			condition.put("nameList", bizNameList);
			condition.put("calculateFeeDate", dateList.get(i+1));
			//查出计算费用商家
			List<BillCheckInfoEntity> calEntities =  billCheckInfoService.queryIncomeReportCalCus(condition);
			//计算费用的商家
			List<String> calNameList = new ArrayList<>();
			
			for (BillCheckInfoEntity entity : calEntities) {
				String calName =entity.getInvoiceName();
				String lastSeller = bizNameMap.get(calName);
				//比对销售员
				if(lastSeller.equals(entity.getSellerId())){
					calNameList.add(calName);
				}
			}
			
			if(CollectionUtils.isNotEmpty(calNameList)){
				condition.put("calList", calNameList);
				//单月份查出来的数据
				List<BillCheckInfoEntity> entities =  billCheckInfoService.queryIncomeReport(condition);
				//放置到全局list
				for (BillCheckInfoEntity billCheckInfoEntity : entities) {
					reportList.add(billCheckInfoEntity);
				}
			}
		}
		return reportList;
	}
	
	/**
	 * 页面查询
	 */
	@DataProvider
	public void query(Page<BillCheckInfoEntity> page, Map<String, Object> parameter) {
		//报表数据list
		List<BillCheckInfoEntity> reportList = queryReport(parameter);
        //物理分页
		List<List<BillCheckInfoEntity>> list = ListTool.split(reportList, page.getPageSize());
		List<BillCheckInfoEntity> pageList =list.get(page.getPageNo()-1);
		page.setEntities(pageList);
		page.setEntityCount(reportList.size());
	}
	
	/**
	 * 明细查询
	 */
	@DataProvider
	public void queryDetail(Page<BillCheckInfoEntity> page, Map<String, Object> param) {
		logger.info("前台传入信息："+param);
		Integer createMonth = (Integer) param.get("createMonth");
		String startDate = String.valueOf(createMonth);
		String endDate =  String.valueOf(createMonth);
		List<String> list = getMonth(startDate,endDate);
		
		//查询条件
		Map<String, Object> condition = new HashMap<>();
		condition.put("startBizDate", list.get(0));
		//查出新增商家
		List<BillCheckInfoEntity> bizEntities =  billCheckInfoService.queryIncomeReportBizCus(condition);
		List<String> bizNameList = new ArrayList<>();
		//key为开票名称，销售id为值
		Map<String,String> bizNameMap = new HashMap<>();
		for (BillCheckInfoEntity entity : bizEntities) {
			String nameString = entity.getInvoiceName();
			bizNameList.add(nameString);
			bizNameMap.put(nameString, entity.getSellerId());
		}
		condition.put("nameList", bizNameList);
		condition.put("calculateFeeDate", list.get(1));
		//查出计算费用商家
		List<BillCheckInfoEntity> calEntities =  billCheckInfoService.queryIncomeReportCalCus(condition);
		//计算费用的商家
		List<String> calNameList = new ArrayList<>();
		
		for (BillCheckInfoEntity entity : calEntities) {
			String calName =entity.getInvoiceName();
			String lastSeller = bizNameMap.get(calName);
			//比对销售员
			if(lastSeller.equals(entity.getSellerId())){
				calNameList.add(calName);
			}
		}
		
		param.put("calList", calNameList);
		//单月份查出来的明细
		PageInfo<BillCheckInfoEntity> entities =  billCheckInfoService.queryIncomeDetail(param, page.getPageNo(), page.getPageSize());
		if (entities != null) {
			page.setEntities(entities.getList());
			page.setEntityCount((int) entities.getTotal());
		}
	}

	private static List<String> getMonth(String start,String end){
		List<String> list = new ArrayList<String>();
		try{
            SimpleDateFormat format = new SimpleDateFormat("yyMM");
            Date d1 = format.parse(start);//定义起始日期
            Date d2 = format.parse(end);//定义结束日期  可以去当前月也可以手动写日期。
            
            //获取三个月时间
            Calendar d = Calendar.getInstance();
            d.setTime(d1);
            d.add(Calendar.MONTH, -1);
            list.add(format.format(d.getTime()));
            
            Calendar dd = Calendar.getInstance();//定义日期实例
            dd.setTime(d1);//设置日期起始时间
            while (dd.getTime().before(d2)) {//判断是否到结束日期
                String str = format.format(dd.getTime());
        		logger.info("查询日期："+str);
                dd.add(Calendar.MONTH, 1);//进行当前日期月份加1
                list.add(str);
            }
    		logger.info("查询日期："+end);
            list.add(end);
        }catch (Exception e){
            logger.error("异常:", e);
        }
		return list;
	}
	
	/**
	 * 导出
	 */
	@FileProvider
	public DownloadFile asynExport(Map<String, Object> parameter) {
		//报表数据list
		List<BillCheckInfoEntity> reportList = queryReport(parameter);
		String path =null;
			try {
				path = export(reportList);
			} catch (Exception e) {
			    logger.error("异常:", e);
			}
			
			InputStream is = null;
			try {
				is = new FileInputStream(path);
			} catch (FileNotFoundException e) {
			    logger.error("异常:", e);
			}
	    	return new DownloadFile("新增收入报表" + FileConstant.SUFFIX_XLSX, is);
		}

	private String export(List<BillCheckInfoEntity> reportList )throws Exception{
		long beginTime = System.currentTimeMillis();
    	logger.info("====新增收入报表导出：写入Excel begin.");
    	SXSSFExporter exporter = new SXSSFExporter();
		//创建sheet，写入头信息
		List<Map<String, Object>> headDetailMapList = getHead(); 
		Sheet sheet =exporter.createSheet("新增收入报表",1,headDetailMapList);
		//内容
		List<Map<String, Object>> dataDetailList = getData(reportList);
		exporter.writeContent(sheet, dataDetailList);
		String path = exporter.saveFile(UUID.randomUUID().toString()+".xlsx");
//		String path = exporter.saveFile("E:/","test.xlsx");
    	logger.info("====新增收入报表临时文件路径："+path);
    	logger.info("====导出：写入Excel end.==总耗时：" + (System.currentTimeMillis() - beginTime));
    	return path;
	}

	public List<Map<String, Object>> getHead(){
		List<Map<String, Object>> headInfoList = new ArrayList<Map<String,Object>>();
		Map<String, Object> itemMap = new HashMap<String, Object>();
		itemMap.put("title", "业务月份");
		itemMap.put("columnWidth", 25);
		itemMap.put("dataKey", "createMonth");
		headInfoList.add(itemMap);
		
		itemMap = new HashMap<String, Object>();
		itemMap.put("title", "销售");
		itemMap.put("columnWidth", 25);
		itemMap.put("dataKey", "sellerName");
		headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "区域");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "area");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "新增收入");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "confirmAmount");
        headInfoList.add(itemMap);
        
        return headInfoList;
	}
	
	private List<Map<String, Object>> getData(List<BillCheckInfoEntity> reportList ){
			List<Map<String, Object>> dataList = new ArrayList<Map<String,Object>>();
	        //无数据
	        if(CollectionUtils.isEmpty(reportList)){
	    		return dataList;
	        }
	        for (BillCheckInfoEntity entity : reportList) {
	        	Map<String, Object> dataItem = new HashMap<String, Object>();
	        	dataItem.put("createMonth", entity.getCreateMonth());
	        	dataItem.put("sellerName", entity.getSellerName());
	        	dataItem.put("area", entity.getArea());
	        	dataItem.put("confirmAmount",  CheckReceiptReportExportController.getFormat(entity.getConfirmAmount()));
	        	dataList.add(dataItem);
			}
		return dataList;
	}
}