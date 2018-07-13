package com.jiuyescm.bms.report.month.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.data.provider.Page;
import com.bstek.dorado.uploader.DownloadFile;
import com.bstek.dorado.uploader.annotation.FileProvider;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jiuyescm.bms.base.dictionary.entity.BmsSubjectInfoEntity;
import com.jiuyescm.bms.base.dictionary.entity.SystemCodeEntity;
import com.jiuyescm.bms.base.dictionary.service.IBmsSubjectInfoService;
import com.jiuyescm.bms.base.dictionary.service.ISystemCodeService;
import com.jiuyescm.bms.common.constants.FileConstant;
import com.jiuyescm.bms.common.enumtype.biz.SellerSubjectCategoryEnum;
import com.jiuyescm.bms.report.service.IReportCustomerDailyIncomeService;
import com.jiuyescm.bms.report.vo.ReportCustomerDailyIncomeVo;
import com.jiuyescm.common.utils.DateUtil;
import com.jiuyescm.common.utils.excel.POISXSSUtil;
import com.jiuyescm.exception.BizException;

@Controller("customerDailyIncomeReportController")
public class CustomerDailyIncomeReportController {
	
	private static final Logger logger = Logger.getLogger(CustomerDailyIncomeReportController.class.getName());

	@Autowired
	private IReportCustomerDailyIncomeService reportCustomerDailyIncomeService;
	@Autowired
	private IBmsSubjectInfoService bmsSubjectInfoService;
	@Autowired
	private ISystemCodeService systemCodeService;
	
	@DataProvider
	public void queryGroup(Page<ReportCustomerDailyIncomeVo> page,Map<String,Object> parameter) throws Exception{
		PageInfo<ReportCustomerDailyIncomeVo> tmpPageInfo = reportCustomerDailyIncomeService.queryGroup(parameter, page.getPageNo(),page.getPageSize());
		if (tmpPageInfo != null) {
			page.setEntities(tmpPageInfo.getList());
			page.setEntityCount((int) tmpPageInfo.getTotal());
		}
	}
	
	@DataProvider
	public List<ReportCustomerDailyIncomeVo> queryDetail(Map<String,Object> parameter) throws Exception{
		if(parameter==null){
			return null;
		}
		return reportCustomerDailyIncomeService.queryDetail(parameter);
	}
	
	@DataProvider
	public Map<String,String> getSubjectMap(){
		Map<String,String> map=Maps.newHashMap();
		List<BmsSubjectInfoEntity> list=bmsSubjectInfoService.queryAllSubejct();
		if(list!=null&&list.size()>0){
			for(BmsSubjectInfoEntity entity:list){
				map.put(entity.getSubjectCode(), entity.getSubjectName());
			}
		}
		return map;
	}
	
	Map<String,Object> condition = null;
	Map<String,String> subjectMap = null;
	
	@FileProvider
	public DownloadFile export(Map<String,Object> param) throws Exception{
		long beginTime = System.currentTimeMillis();
		logger.info("====销售绩效报表导出：写入Excel begin.");
		try {
			subjectMap = getSubjectMap();
			
			String path = getPath();
			File storeFolder = new File(path);
			if(!storeFolder.isDirectory()){
				storeFolder.mkdirs();
			}
			
			// 如果文件存在直接删除，重新生成
			String fileName = "销售绩效报表" + FileConstant.SUFFIX_XLSX;
			String filePath = path + FileConstant.SEPARATOR + fileName;
			File file = new File(filePath);
			if (file.exists()) {
				file.delete();
			}
			
			POISXSSUtil poiUtil = new POISXSSUtil();
	    	SXSSFWorkbook workbook = poiUtil.getXSSFWorkbook();
	    		    		    	
	        // 导出汇总
	    	handExport(poiUtil, workbook, filePath, param);
	    	
	    	// 明细
	    	if (null != condition) {
				// 仓储
	    		condition.put("groupCode", SellerSubjectCategoryEnum.STORAGE.getCode());
	    		handExportDetail(poiUtil, workbook, filePath, condition, SellerSubjectCategoryEnum.STORAGE.getCode());
	    		
	    		// 运输
	    		condition.put("groupCode", SellerSubjectCategoryEnum.TRANSPORT.getCode());
	    		handExportDetail(poiUtil, workbook, filePath, condition, SellerSubjectCategoryEnum.TRANSPORT.getCode());
	    		
	    		// 配送
	    		condition.put("groupCode", SellerSubjectCategoryEnum.DELIVER.getCode());
	    		handExportDetail(poiUtil, workbook, filePath, condition, SellerSubjectCategoryEnum.DELIVER.getCode());
	    		
	    		// 耗材
	    		condition.put("groupCode", SellerSubjectCategoryEnum.MATERIAL.getCode());
	    		handExportDetail(poiUtil, workbook, filePath, condition, SellerSubjectCategoryEnum.MATERIAL.getCode());
			}
	    	
	    	//最后写到文件
	    	poiUtil.write2FilePath(workbook, filePath);
	    		    	
	    	logger.info("====销售绩效报表导出：写入Excel end.==总耗时：" + (System.currentTimeMillis() - beginTime));

	    	InputStream is = new FileInputStream(filePath);
	    	return new DownloadFile(fileName, is);
		} catch (Exception e) {
			logger.error("销售绩效报表失败", e);
		}
		return null;
	}
	
	private void handExport(POISXSSUtil poiUtil, SXSSFWorkbook workbook, 
			String path, Map<String, Object> param)throws Exception{
		int pageNo = 1;
		int lineNo = 1;
		boolean doLoop = true;
		logger.info("销售绩效汇总报表导出...");
		
        while (doLoop) {
			PageInfo<ReportCustomerDailyIncomeVo> pageInfo= reportCustomerDailyIncomeService.queryGroup(param, pageNo, FileConstant.PAGESIZE);
			if (null != pageInfo && pageInfo.getList().size() > 0) {
				// 处理返回结果中的商家和时间，查询各费用详细信息
				condition = obtainCondition(pageInfo.getList());
				
				if (pageInfo.getList().size() < FileConstant.PAGESIZE) {
					doLoop = false;
				}else {
					pageNo += 1; 
				}
				
				List<Map<String, Object>> headList = getHead();
				List<Map<String, Object>> dataList = getHeadItem(pageInfo.getList());
				
				poiUtil.exportExcel2FilePath(poiUtil, workbook, "汇总报表", 
						lineNo, headList, dataList);
				if (null != pageInfo && pageInfo.getList().size() > 0) {
					lineNo += pageInfo.getList().size();
				}
			}else {
				doLoop = false;
			}
		}
		logger.info("销售绩效汇总报表导出生成sheet。。。");
	}
	
	public List<Map<String, Object>> getHead(){
		List<Map<String, Object>> headMapList=new ArrayList<Map<String, Object>>();
		Map<String, Object> itemMap = new HashMap<String, Object>();
		
    	itemMap.put("title", "日期");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "feesDate");
        headMapList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "商家");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "customerName");
        headMapList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "区域");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "regionName");
        headMapList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "项目销售人员");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "seller");
        headMapList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "项目管理人员");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "manager");
        headMapList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "项目结算人员");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "settleOfficer");
        headMapList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "仓储费");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "storageAmount");
        headMapList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "运输费");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "transportAmount");
        headMapList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "配送费");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "deliverAmount");
        headMapList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "耗材费");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "materialAmount");
        headMapList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "总费用");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "amount");
        headMapList.add(itemMap);
        
        return headMapList;
	}
	
	private List<Map<String, Object>> getHeadItem(List<ReportCustomerDailyIncomeVo> list){
		 List<Map<String, Object>> dataList = new ArrayList<Map<String,Object>>();
		 Map<String, Object> dataItem = null;
		 for(ReportCustomerDailyIncomeVo entity:list){
			dataItem=new HashMap<String, Object>();
			dataItem.put("feesDate", DateUtil.formatyymmddLine(entity.getFeesDate()));
			dataItem.put("customerName", entity.getCustomerName());
			dataItem.put("regionName", entity.getRegionName());
			dataItem.put("seller", entity.getSeller());
			dataItem.put("manager", entity.getManager());
			dataItem.put("settleOfficer", entity.getSettleOfficer());
			dataItem.put("storageAmount", entity.getStorageAmount());
			dataItem.put("transportAmount", entity.getTransportAmount());
			dataItem.put("deliverAmount", entity.getDeliverAmount());
			dataItem.put("materialAmount", entity.getMaterialAmount());
			dataItem.put("amount", entity.getAmount());
			dataList.add(dataItem);
		 }
		 return dataList;
	}
	
	private void handExportDetail(POISXSSUtil poiUtil, SXSSFWorkbook workbook, 
			String path, Map<String, Object> param, String subjectCode)throws Exception{
		int pageNo = 1;
		int lineNo = 1;
		boolean doLoop = true;
		logger.info("销售绩效明细报表【"+ subjectCode + "】导出...");
		
        while (doLoop) {
			PageInfo<ReportCustomerDailyIncomeVo> pageInfo= reportCustomerDailyIncomeService.queryDetailList(param, pageNo, FileConstant.PAGESIZE);
			if (null != pageInfo && pageInfo.getList().size() > 0) {
				if (pageInfo.getList().size() < FileConstant.PAGESIZE) {
					doLoop = false;
				}else {
					pageNo += 1; 
				}
				
				List<Map<String, Object>> headList = getDetailHead();
				List<Map<String, Object>> dataList = getDetailHeadItem(pageInfo.getList());
				
				poiUtil.exportExcel2FilePath(poiUtil, workbook, SellerSubjectCategoryEnum.getDesc(subjectCode), 
						lineNo, headList, dataList);
				if (null != pageInfo && pageInfo.getList().size() > 0) {
					lineNo += pageInfo.getList().size();
				}
			}else {
				doLoop = false;
			}
		}
		logger.info("销售绩效明细报表【"+ subjectCode + "】导出生成sheet。。。");
	}
	
	public List<Map<String, Object>> getDetailHead(){
		List<Map<String, Object>> headMapList=new ArrayList<Map<String, Object>>();
		Map<String, Object> itemMap = new HashMap<String, Object>();
		
    	itemMap.put("title", "日期");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "feesDate");
        headMapList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "商家");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "customerName");
        headMapList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "费用科目");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "subjectCode");
        headMapList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "总费用");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "amount");
        headMapList.add(itemMap);
        
        return headMapList;
	}
	
	private List<Map<String, Object>> getDetailHeadItem(List<ReportCustomerDailyIncomeVo> list){
		 List<Map<String, Object>> dataList = new ArrayList<Map<String,Object>>();
		 Map<String, Object> dataItem = null;
		 for(ReportCustomerDailyIncomeVo entity:list){
			dataItem=new HashMap<String, Object>();
			dataItem.put("feesDate", DateUtil.formatyymmddLine(entity.getFeesDate()));
			dataItem.put("customerName", entity.getCustomerName());
			dataItem.put("subjectCode", subjectMap.get(entity.getSubjectCode()));
			dataItem.put("amount", entity.getAmount());
			dataList.add(dataItem);
		 }
		 return dataList;
	}
	
	/**
	 * 从统计结果中获取商家、时间信息
	 * @param list
	 * @return
	 */
	private Map<String, Object> obtainCondition(List<ReportCustomerDailyIncomeVo> list){
		Map<String, Object> condition = null;
		if (null == list || list.size() <= 0) {
			return condition;
		}
		
		List<String> customerIds = Lists.newArrayList();
		List<Date> feesDates = Lists.newArrayList();
		for (ReportCustomerDailyIncomeVo vo : list) {
			String customerId = vo.getCustomerId();
			Date feesDate = vo.getFeesDate();
			if (!customerIds.contains(customerId)) {
				customerIds.add(customerId);
			}
			if (!feesDates.contains(feesDate)) {
				feesDates.add(feesDate);
			}
		}
		
		condition = Maps.newHashMap();
		condition.put("customerIds", customerIds);
		condition.put("feesDates", feesDates);
		return condition;
	}
	
	private String getPath(){
		SystemCodeEntity systemCodeEntity = systemCodeService.getSystemCode("GLOABL_PARAM", "EXPORT_INCOME_REPORT");
		if(systemCodeEntity == null){
			throw new BizException("请在系统参数中配置文件上传路径,参数GLOABL_PARAM,EXPORT_PRODUCT_PALLET_DIFF");
		}
		return systemCodeEntity.getExtattr1();
	}
}
