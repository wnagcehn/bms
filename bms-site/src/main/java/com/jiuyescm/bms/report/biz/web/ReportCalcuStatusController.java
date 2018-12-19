package com.jiuyescm.bms.report.biz.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.data.provider.Page;
import com.bstek.dorado.uploader.DownloadFile;
import com.bstek.dorado.uploader.annotation.FileProvider;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.jiuyescm.bms.base.dictionary.entity.BmsSubjectInfoEntity;
import com.jiuyescm.bms.base.dictionary.entity.SystemCodeEntity;
import com.jiuyescm.bms.base.dictionary.service.IBmsSubjectInfoService;
import com.jiuyescm.bms.base.dictionary.service.ISystemCodeService;
import com.jiuyescm.bms.common.constants.FileConstant;
import com.jiuyescm.bms.common.enumtype.CalculateState;
import com.jiuyescm.bms.report.biz.entity.ReportCalcuStatusEntity;
import com.jiuyescm.bms.report.month.entity.ReportOverdueUnaccountEntity;
import com.jiuyescm.bms.report.month.service.IReportCalcuStatusService;
import com.jiuyescm.common.utils.excel.POISXSSUtil;
import com.jiuyescm.exception.BizException;

/**
 * ..Controller
 * @author wangchen
 * 
 */
@Controller("reportCalcuStatusController")
public class ReportCalcuStatusController {

	private static final Logger logger = LoggerFactory.getLogger(ReportCalcuStatusController.class.getName());

	@Autowired
	private IReportCalcuStatusService reportCalcuStatusService;
	@Resource 
	private ISystemCodeService systemCodeService;
	@Resource
	private IBmsSubjectInfoService bmsSubjectInfoService;

	/**
	 * 根据id查询
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@DataProvider
	public ReportCalcuStatusEntity findById(Long id) throws Exception {
		return reportCalcuStatusService.findById(id);
	}

	/**
	 * 分页查询
	 * @param page
	 * @param param
	 */
	@DataProvider
	public void query(Page<ReportCalcuStatusEntity> page, Map<String, Object> param) {
		PageInfo<ReportCalcuStatusEntity> pageInfo = reportCalcuStatusService.queryAll(param, page.getPageNo(), page.getPageSize());	
		if (pageInfo != null) {
			page.setEntities(pageInfo.getList());
			page.setEntityCount((int) pageInfo.getTotal());
		}	
	}
	
	@DataProvider
	public List<ReportCalcuStatusEntity> queryDetail(Map<String, Object> param){
		return reportCalcuStatusService.queryDetail(param);
	}

	/**
	 * 保存
	 * @param entity
	 * @return
	 */
	@DataResolver
	public void save(ReportCalcuStatusEntity entity) {
		if (null == entity.getId()) {
			reportCalcuStatusService.save(entity);
		} else {
			reportCalcuStatusService.update(entity);
		}
	}

	/**
	 * 删除
	 * @param entity
	 */
	@DataResolver
	public void delete(ReportCalcuStatusEntity entity) {
		reportCalcuStatusService.delete(entity.getId());
	}
	
	/**
	 * 路径
	 * @return
	 */
	private String getPath(){
		SystemCodeEntity systemCodeEntity = systemCodeService.getSystemCode("GLOABL_PARAM", "EXPORT_CALCU_STATUS");
		if(systemCodeEntity == null){
			throw new BizException("请在系统参数中配置文件上传路径,参数GLOABL_PARAM,EXPORT_CALCU_STATUS");
		}
		return systemCodeEntity.getExtattr1();
	}
	
	public String getIsCalCuName(String code){
		if ("1".equals(code)) {
			return "成功";
		}else if ("2".equals(code)) {
			return "失败";
		}else {
			return "无数据";
		}
	}
	
	/**
	 * 导出
	 * @param param
	 * @return
	 * @throws Exception
	 */
	@FileProvider
	public DownloadFile export(Map<String,Object> param) throws Exception{
		long beginTime = System.currentTimeMillis();
    	logger.info("====商家各科目计算状态导出：写入Excel begin.");
    	
    	Map<String, Object> dictcodeMap=new HashMap<String, Object>();
		try {
//			String filePath =getName() + FileConstant.SUFFIX_XLSX;
			//如果存放上传文件的目录不存在就新建
			String path = getPath();
			File storeFolder = new File(path);
			if(!storeFolder.isDirectory()){
				storeFolder.mkdirs();
			}
			
			// 如果文件存在直接删除，重新生成
			String fileName = "商家各科目计算状态" + FileConstant.SUFFIX_XLSX;
			String filePath = path + FileConstant.SEPARATOR + fileName;
			File file = new File(filePath);
			if (file.exists()) {
				file.delete();
			}
			
			POISXSSUtil poiUtil = new POISXSSUtil();
	    	SXSSFWorkbook workbook = poiUtil.getXSSFWorkbook();
	    		    		    	
	        //导出方法
	    	exportCustomerSubCal(poiUtil, workbook, filePath, param,dictcodeMap);
	    	
	    	//最后写到文件
	    	poiUtil.write2FilePath(workbook, filePath);
	    		    	
	    	logger.info("====商家各科目计算状态：写入Excel end.==总耗时：" + (System.currentTimeMillis() - beginTime));

	    	InputStream is = new FileInputStream(filePath);
	    	return new DownloadFile(fileName, is);
		} catch (Exception e) {
			//bmsErrorLogInfoService.
			logger.error("商家各科目计算状态导出失败", e);
		}
		return null;
	}
	
	/**
	 * 商家各科目计算状态导出
	 * @param poiUtil
	 * @param workbook
	 * @param path
	 * @param param
	 * @param dictcodeMap
	 * @throws Exception
	 */
	private void exportCustomerSubCal(POISXSSUtil poiUtil, SXSSFWorkbook workbook, 
			String path, Map<String, Object> param,Map<String, Object> dictcodeMap)throws Exception{
		logger.info("商家各科目计算状态导出...");
        
		//科目code转name
		Map<String, String> map = new HashMap<String, String>();
		List<BmsSubjectInfoEntity> subjectList = bmsSubjectInfoService.queryAll("INPUT");
		if (subjectList != null && subjectList.size() > 0) {
			for (BmsSubjectInfoEntity bmsSubjectInfoEntity : subjectList) {
				map.put(bmsSubjectInfoEntity.getSubjectCode(), bmsSubjectInfoEntity.getSubjectName());
			}
		}
		
		List<ReportCalcuStatusEntity> stoList = new ArrayList<ReportCalcuStatusEntity>();
		List<ReportCalcuStatusEntity> disList = new ArrayList<ReportCalcuStatusEntity>();
		List<ReportCalcuStatusEntity> traList = new ArrayList<ReportCalcuStatusEntity>();
		List<String> cusList = new ArrayList<String>();
		//Map<String, Integer> con = Maps.newLinkedHashMap();
		
		//主查询+明细查询
		Map<String, Object> maps = new HashMap<String, Object>();
		maps.put("createYear", param.get("createYear"));
		maps.put("createMonth", param.get("createMonth"));
		String isCal = "";
		if (param.containsKey("customerId")) {
			maps.put("customerId", param.get("customerId"));
		}
		if (param.containsKey("isCalculated")) {
			isCal = param.get("isCalculated").toString();
		}
		List<ReportCalcuStatusEntity> mList = reportCalcuStatusService.queryAll(param);
		List<ReportCalcuStatusEntity> list = reportCalcuStatusService.queryDetail(param);
		if (null != mList && mList.size() > 0) {
			//商家汇总
			for (ReportCalcuStatusEntity mEntity : mList) {
				cusList.add(mEntity.getCustomerName());
			}
			//仓储/配送/干线
			for (ReportCalcuStatusEntity reportCalcuStatusEntity : list) {
				if ("STORAGE".equals(reportCalcuStatusEntity.getBizType())) {
					stoList.add(reportCalcuStatusEntity);
				}else if ("DISPATCH".equals(reportCalcuStatusEntity.getBizType())) {
					disList.add(reportCalcuStatusEntity);
				}else if ("TRANSPORT".equals(reportCalcuStatusEntity.getBizType())) {
					traList.add(reportCalcuStatusEntity);
				}
			}
			
			logger.info("商家各科目计算状态导出生成sheet。。。");
			Sheet sheet = poiUtil.getXSSFSheet(workbook,"商家各科目计算状态");
			
			Font font = workbook.createFont();
		    font.setBoldweight(Font.BOLDWEIGHT_BOLD);
			CellStyle style = workbook.createCellStyle();
			style.setAlignment(CellStyle.ALIGN_CENTER);
			style.setWrapText(true);
			style.setFont(font);
			 
			Row row0 = sheet.createRow(0);
			Cell cell0 = row0.createCell(0);
			cell0.setCellValue("");
			cell0.setCellStyle(style);
			Cell cell1 = row0.createCell(3);
			cell1.setCellValue("仓储");
			cell1.setCellStyle(style);
			if (null != traList && traList.size() > 0) {
				Cell cell2 = row0.createCell(3+stoList.size());
				cell2.setCellValue("干线");
				cell2.setCellStyle(style);
			}
			if (null != disList && disList.size() > 0) {
				Cell cell3 = row0.createCell(3+stoList.size()+traList.size());
				cell3.setCellValue("配送");
				cell3.setCellStyle(style);
			}
			
			sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 2));
			if (stoList.size() > 0) {
				 sheet.addMergedRegion(new CellRangeAddress(0, 0, 3, 3+stoList.size()-1));
			}
			if (traList.size() > 0) {
				sheet.addMergedRegion(new CellRangeAddress(0, 0, 3+stoList.size(),3+stoList.size()+traList.size()-1));
			}
			if (disList.size() > 0) {
				sheet.addMergedRegion(new CellRangeAddress(0, 0, 3+stoList.size()+traList.size(),3+stoList.size()+traList.size()+disList.size()-1));
			}
			
			Row row1 = sheet.createRow(1);
			Cell cell4 = row1.createCell(0);
			cell4.setCellValue("年月");
			cell4.setCellStyle(style);
			Cell cell5 = row1.createCell(1);
			cell5.setCellValue("商家");
			cell5.setCellStyle(style);
			Cell cell6 = row1.createCell(2);
			cell6.setCellValue("状态");
			cell6.setCellStyle(style);
			
			sheet.setColumnWidth(0, 4000);
			sheet.setColumnWidth(1, 4000);
			sheet.setColumnWidth(2, 4000);
			
			//第一行规拼接抬头
			Integer col = 3;
			Map<String, Integer> map1 = new HashMap<>();
			Map<String, Integer> map2 = new HashMap<>();
			Map<String, Integer> map3 = new HashMap<>();
			for (ReportCalcuStatusEntity sEntity : stoList) {
				if (map1.containsKey(sEntity.getSubjectCode())) {
					continue;
				}else {
					map1.put(sEntity.getSubjectCode(), col);
					col++;
				}
			}
			for (ReportCalcuStatusEntity dEntity : disList) {
				if (map2.containsKey(dEntity.getSubjectCode())) {
					continue;
				}else {
					map2.put(dEntity.getSubjectCode(), col);
					col++;
				}
			}
			for (ReportCalcuStatusEntity tEntity : traList) {
				if (map3.containsKey(tEntity.getSubjectCode())) {
					continue;
				}else {
					map3.put(tEntity.getSubjectCode(), col);
					col++;
				}
			}
			//仓储
			for (String m1 : map1.keySet()) {
				Cell cell7 = row1.createCell(map1.get(m1));
				cell7.setCellValue(map.get(m1));
				cell7.setCellStyle(style);
				sheet.setColumnWidth(map1.get(m1), 4000);
			}
			//干线
			for (String m2 : map2.keySet()) {
				Cell cell8 = row1.createCell(map2.get(m2));
				cell8.setCellValue(map.get(m2));
				cell8.setCellStyle(style);
				sheet.setColumnWidth(map2.get(m2), 4000);
			}
			//配送
			for (String m3 : map3.keySet()) {
				Cell cell9 = row1.createCell(map3.get(m3));
				cell9.setCellValue(map.get(m3));
				cell9.setCellStyle(style);
				sheet.setColumnWidth(map3.get(m3), 4000);
			}
			
			//第二行数据行		
			int rowIndex = 2;
			Map<String, Object> condition = null;
			for (int i = 0; i < cusList.size(); i++) {
				Row row2 = sheet.createRow(rowIndex);
				rowIndex++;
				
				//红色字体样式
				Font redFont = workbook.createFont();
				redFont.setColor(IndexedColors.RED.getIndex());
				CellStyle style2 = workbook.createCellStyle();
				style2.setFont(redFont);
				
				condition = new HashMap<String, Object>();
				condition.put("customerName", cusList.get(i));
				condition.put("createYear", param.get("createYear"));
				condition.put("createMonth", param.get("createMonth"));
				List<ReportCalcuStatusEntity> detailList = reportCalcuStatusService.queryDetail(condition);
				
				//年月
				CellStyle style3 = workbook.createCellStyle();
				style3.setAlignment(CellStyle.ALIGN_RIGHT);
				String createTime = "";
				if (Integer.valueOf(param.get("createMonth").toString()) < 10) {
					createTime = param.get("createYear").toString().substring(2, 4) + "0" + param.get("createMonth");
				}else{
					createTime = param.get("createYear").toString().substring(2, 4) + param.get("createMonth");
				}

				Cell cell10 = row2.createCell(0);
				cell10.setCellValue(createTime);
				cell10.setCellStyle(style3);
				
				//商家
				Cell cell11 = row2.createCell(1);
				cell11.setCellValue(cusList.get(i));
				
				//总状态
				Cell cell12 = row2.createCell(2);
				boolean exe = false;
				for (ReportCalcuStatusEntity reportCalcuStatusEntity : detailList) {
					if ("2".equals(reportCalcuStatusEntity.getIsCalculated())) {
						exe = true;
						break;
					}
				}
				if (exe) {
					cell12.setCellValue("失败");
					cell12.setCellStyle(style2);
				}else {
					cell12.setCellValue("成功");
				}
				//仓储
				for (String m1 : map1.keySet()) {
					condition.put("subjectCode", m1);
					List<ReportCalcuStatusEntity> rcsList = reportCalcuStatusService.queryDetail(condition);
					if (null != rcsList && rcsList.size() > 0) {
						Cell cell13 = row2.createCell(map1.get(m1));
						cell13.setCellValue(getIsCalCuName(rcsList.get(0).getIsCalculated()));
						if (null != rcsList.get(0).getIsCalculated() && "2".equals(rcsList.get(0).getIsCalculated())) {
							cell13.setCellStyle(style2);
						}
						sheet.setColumnWidth(map1.get(m1), 4000);
					}
				}
				//干线
				for (String m2 : map2.keySet()) {
					condition.put("subjectCode", m2);
					List<ReportCalcuStatusEntity> rcsList = reportCalcuStatusService.queryDetail(condition);
					if (null != rcsList && rcsList.size() > 0) {
						Cell cell14 = row2.createCell(map2.get(m2));
						cell14.setCellValue(getIsCalCuName(rcsList.get(0).getIsCalculated()));
						if (null != rcsList.get(0).getIsCalculated() && "2".equals(rcsList.get(0).getIsCalculated())) {
							cell14.setCellStyle(style2);
						}
						sheet.setColumnWidth(map2.get(m2), 4000);
					}
				}
				//配送
				for (String m3 : map3.keySet()) {
					condition.put("subjectCode", m3);
					List<ReportCalcuStatusEntity> rcsList = reportCalcuStatusService.queryDetail(condition);
					if (null != rcsList && rcsList.size() > 0) {
						Cell cell15 = row2.createCell(map3.get(m3));
						cell15.setCellValue(getIsCalCuName(rcsList.get(0).getIsCalculated()));
						if (null != rcsList.get(0).getIsCalculated() && "2".equals(rcsList.get(0).getIsCalculated())) {
							cell15.setCellStyle(style2);
						}
						sheet.setColumnWidth(map3.get(m3), 4000);
					}
				}
		}
		
			
		}else {
			Sheet sheet = poiUtil.getXSSFSheet(workbook,"商家各科目计算状态");
			Row row1 = sheet.createRow(0);
			Cell cell4 = row1.createCell(0);
			cell4.setCellValue("无商家有数据");
		}
	}
	
	/**
	 * 导出金额
	 * @param param
	 * @return
	 * @throws Exception
	 */
	@FileProvider
	public DownloadFile exportCost(Map<String,Object> param) throws Exception{
		long beginTime = System.currentTimeMillis();
    	logger.info("====商家各科目计算金额导出：写入Excel begin.");
    	
    	Map<String, Object> dictcodeMap=new HashMap<String, Object>();
		try {
//			String filePath =getName() + FileConstant.SUFFIX_XLSX;
			//如果存放上传文件的目录不存在就新建
			String path = getPath();
			File storeFolder = new File(path);
			if(!storeFolder.isDirectory()){
				storeFolder.mkdirs();
			}
			
			// 如果文件存在直接删除，重新生成
			String fileName = "商家各科目计算金额" + FileConstant.SUFFIX_XLSX;
			String filePath = path + FileConstant.SEPARATOR + fileName;
			File file = new File(filePath);
			if (file.exists()) {
				file.delete();
			}
			
			POISXSSUtil poiUtil = new POISXSSUtil();
	    	SXSSFWorkbook workbook = poiUtil.getXSSFWorkbook();
	    		    		    	
	        //导出方法
	    	exportCalCost(poiUtil, workbook, filePath, param,dictcodeMap);
	    	
	    	//最后写到文件
	    	poiUtil.write2FilePath(workbook, filePath);
	    		    	
	    	logger.info("====商家各科目计算金额：写入Excel end.==总耗时：" + (System.currentTimeMillis() - beginTime));

	    	InputStream is = new FileInputStream(filePath);
	    	return new DownloadFile(fileName, is);
		} catch (Exception e) {
			//bmsErrorLogInfoService.
			logger.error("商家各科目计算金额导出失败", e);
		}
		return null;
	}
	
	/**
	 * 商家各科目计算金额导出
	 * @param poiUtil
	 * @param workbook
	 * @param path
	 * @param param
	 * @param dictcodeMap
	 * @throws Exception
	 */
	private void exportCalCost(POISXSSUtil poiUtil, SXSSFWorkbook workbook, 
			String path, Map<String, Object> param,Map<String, Object> dictcodeMap)throws Exception{
		logger.info("商家各科目计算状金额导出...");
        
		//科目code转name
		Map<String, String> map = new HashMap<String, String>();
		List<BmsSubjectInfoEntity> subjectList = bmsSubjectInfoService.queryAll("INPUT");
		if (subjectList != null && subjectList.size() > 0) {
			for (BmsSubjectInfoEntity bmsSubjectInfoEntity : subjectList) {
				map.put(bmsSubjectInfoEntity.getSubjectCode(), bmsSubjectInfoEntity.getSubjectName());
			}
		}
		
		List<ReportCalcuStatusEntity> stoList = new ArrayList<ReportCalcuStatusEntity>();
		List<ReportCalcuStatusEntity> disList = new ArrayList<ReportCalcuStatusEntity>();
		List<ReportCalcuStatusEntity> traList = new ArrayList<ReportCalcuStatusEntity>();
		List<String> cusList = new ArrayList<String>();
		//Map<String, Integer> con = Maps.newLinkedHashMap();
		
		//主查询+明细查询
		List<ReportCalcuStatusEntity> mList = reportCalcuStatusService.queryAll(param);
		List<ReportCalcuStatusEntity> list = reportCalcuStatusService.queryDetail(param);
		if (null != mList && mList.size() > 0) {
			//商家汇总
			for (ReportCalcuStatusEntity mEntity : mList) {
				cusList.add(mEntity.getCustomerName());
			}
			//仓储/配送/干线
			for (ReportCalcuStatusEntity reportCalcuStatusEntity : list) {
				if ("STORAGE".equals(reportCalcuStatusEntity.getBizType())) {
					stoList.add(reportCalcuStatusEntity);
				}else if ("DISPATCH".equals(reportCalcuStatusEntity.getBizType())) {
					disList.add(reportCalcuStatusEntity);
				}else if ("TRANSPORT".equals(reportCalcuStatusEntity.getBizType())) {
					traList.add(reportCalcuStatusEntity);
				}
			}
			
			logger.info("商家各科目计算金额导出生成sheet。。。");
			Sheet sheet = poiUtil.getXSSFSheet(workbook,"商家各科目计算金额");
			
			Font font = workbook.createFont();
		    font.setBoldweight(Font.BOLDWEIGHT_BOLD);
			CellStyle style = workbook.createCellStyle();
			style.setAlignment(CellStyle.ALIGN_CENTER);
			style.setWrapText(true);
			style.setFont(font);
			 
			Row row0 = sheet.createRow(0);
			Cell cell0 = row0.createCell(0);
			cell0.setCellValue("");
			cell0.setCellStyle(style);
			Cell cell1 = row0.createCell(3);
			cell1.setCellValue("仓储");
			cell1.setCellStyle(style);
			if (null != traList && traList.size() > 0) {
				Cell cell2 = row0.createCell(3+stoList.size());
				cell2.setCellValue("干线");
				cell2.setCellStyle(style);
			}		
			if (null != disList && disList.size() > 0) {
				Cell cell3 = row0.createCell(3+stoList.size()+traList.size());
				cell3.setCellValue("配送");
				cell3.setCellStyle(style);
			}	
			sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 2));
			if (stoList.size() > 0) {
				 sheet.addMergedRegion(new CellRangeAddress(0, 0, 3, 3+stoList.size()-1));
			}
			if (traList.size() > 0) {
				sheet.addMergedRegion(new CellRangeAddress(0, 0, 3+stoList.size(),3+stoList.size()+traList.size()-1));
			}
			if (disList.size() > 0) {
				sheet.addMergedRegion(new CellRangeAddress(0, 0, 3+stoList.size()+traList.size(),3+stoList.size()+traList.size()+disList.size()-1));
			}
			
			Row row1 = sheet.createRow(1);
			Cell cell4 = row1.createCell(0);
			cell4.setCellValue("年月");
			cell4.setCellStyle(style);
			Cell cell5 = row1.createCell(1);
			cell5.setCellValue("商家");
			cell5.setCellStyle(style);
			Cell cell6 = row1.createCell(2);
			cell6.setCellValue("小计");
			cell6.setCellStyle(style);
			
			sheet.setColumnWidth(0, 4000);
			sheet.setColumnWidth(1, 4000);
			sheet.setColumnWidth(2, 4000);
			
			//第一行规拼接抬头
			Integer col = 3;
			Map<String, Integer> map1 = new HashMap<>();
			Map<String, Integer> map2 = new HashMap<>();
			Map<String, Integer> map3 = new HashMap<>();
			for (ReportCalcuStatusEntity sEntity : stoList) {
				if (map1.containsKey(sEntity.getSubjectCode())) {
					continue;
				}else {
					map1.put(sEntity.getSubjectCode(), col);
					col++;
				}
			}
			for (ReportCalcuStatusEntity dEntity : disList) {
				if (map2.containsKey(dEntity.getSubjectCode())) {
					continue;
				}else {
					map2.put(dEntity.getSubjectCode(), col);
					col++;
				}
			}
			for (ReportCalcuStatusEntity tEntity : traList) {
				if (map3.containsKey(tEntity.getSubjectCode())) {
					continue;
				}else {
					map3.put(tEntity.getSubjectCode(), col);
					col++;
				}
			}
			//仓储
			for (String m1 : map1.keySet()) {
				Cell cell7 = row1.createCell(map1.get(m1));
				cell7.setCellValue(map.get(m1));
				cell7.setCellStyle(style);
				sheet.setColumnWidth(map1.get(m1), 4000);
			}
			//干线
			for (String m2 : map2.keySet()) {
				Cell cell8 = row1.createCell(map2.get(m2));
				cell8.setCellValue(map.get(m2));
				cell8.setCellStyle(style);
				sheet.setColumnWidth(map2.get(m2), 4000);
			}
			//配送
			for (String m3 : map3.keySet()) {
				Cell cell9 = row1.createCell(map3.get(m3));
				cell9.setCellValue(map.get(m3));
				cell9.setCellStyle(style);
				sheet.setColumnWidth(map3.get(m3), 4000);
			}
			
			//第二行数据行		
			int rowIndex = 2;
			double sumAmount = 0.0;
			Map<String, Object> condition = null;
			Map<String, Object> condi = null;
			List<ReportCalcuStatusEntity> detailList = null;
			for (int i = 0; i < cusList.size(); i++) {
				Row row2 = sheet.createRow(rowIndex);
				rowIndex++;
				
				condition = new HashMap<String, Object>();
				condition.put("customerName", cusList.get(i));
				condition.put("createYear", param.get("createYear"));
				condition.put("createMonth", param.get("createMonth"));
				
				//年月
				CellStyle style3 = workbook.createCellStyle();
				style3.setAlignment(CellStyle.ALIGN_RIGHT);
				String createTime = "";
				if (Integer.valueOf(param.get("createMonth").toString()) < 10) {
					createTime = param.get("createYear").toString().substring(2, 4) + "0" + param.get("createMonth");
				}else{
					createTime = param.get("createYear").toString().substring(2, 4) + param.get("createMonth");
				}

				Cell cell10 = row2.createCell(0);
				cell10.setCellValue(createTime);
				cell10.setCellStyle(style3);
				
				//商家
				Cell cell11 = row2.createCell(1);
				cell11.setCellValue(cusList.get(i));
				
				//小计
				Cell cell12 = row2.createCell(2);		
				param.put("customerName", cusList.get(i));
				List<ReportCalcuStatusEntity> customerList = reportCalcuStatusService.query(param);
				if (null != customerList) {
					if (null != customerList.get(0).getSumAmount() && !"".equals(customerList.get(0).getSumAmount())) {
						cell12.setCellValue(Double.parseDouble(customerList.get(0).getSumAmount()));
					}else {
						cell12.setCellValue(customerList.get(0).getSumAmount());
					}			
					if (!"".equals(customerList.get(0).getSumAmount())) {
						sumAmount = sumAmount + Double.parseDouble(customerList.get(0).getSumAmount());	
					}	
				}
				
					
				//仓储
				for (String m1 : map1.keySet()) {
					condition.put("subjectCode", m1);
					List<ReportCalcuStatusEntity> rcsList = reportCalcuStatusService.queryDetail(condition);
					if (null != rcsList && rcsList.size() > 0) {
						Cell cell13 = row2.createCell(map1.get(m1));
						if (null != rcsList.get(0).getSumAmount() && !"".equals(rcsList.get(0).getSumAmount())) {
							cell13.setCellValue(Double.parseDouble(rcsList.get(0).getSumAmount()));
						}else {
							cell13.setCellValue(rcsList.get(0).getSumAmount());
						}			
						sheet.setColumnWidth(map1.get(m1), 4000);
					}
				}
				//干线
				for (String m2 : map2.keySet()) {
					condition.put("subjectCode", m2);
					List<ReportCalcuStatusEntity> rcsList = reportCalcuStatusService.queryDetail(condition);
					if (null != rcsList && rcsList.size() > 0) {
						Cell cell14 = row2.createCell(map2.get(m2));
						if (null != rcsList.get(0).getSumAmount() && !"".equals(rcsList.get(0).getSumAmount())) {
							cell14.setCellValue(Double.parseDouble(rcsList.get(0).getSumAmount()));
						}else {
							cell14.setCellValue(rcsList.get(0).getSumAmount());
						}		
						sheet.setColumnWidth(map2.get(m2), 4000);
					}
				}
				//配送
				for (String m3 : map3.keySet()) {
					condition.put("subjectCode", m3);
					List<ReportCalcuStatusEntity> rcsList = reportCalcuStatusService.queryDetail(condition);
					if (null != rcsList && rcsList.size() > 0) {
						Cell cell15 = row2.createCell(map3.get(m3));
						if (null != rcsList.get(0).getSumAmount() && !"".equals(rcsList.get(0).getSumAmount())) {
							cell15.setCellValue(Double.parseDouble(rcsList.get(0).getSumAmount()));
						}else {
							cell15.setCellValue(rcsList.get(0).getSumAmount());
						}
						sheet.setColumnWidth(map3.get(m3), 4000);
					}
				}
				
			}
			Row row3 = sheet.createRow(rowIndex);
			//合计
			Cell cell16 = row3.createCell(1);
			cell16.setCellValue("合计");
			//合计中的小计
			Cell cell17 = row3.createCell(2);
			cell17.setCellValue(sumAmount);
			
			for (int i = 0; i < cusList.size(); i++) {			
				condi = new HashMap<String, Object>();
				condi.put("createYear", param.get("createYear"));
				condi.put("createMonth", param.get("createMonth"));
				if (param.containsKey("customerId")) {
					condi.put("customerId", param.get("customerId"));
				}
				
				//仓储的累计
				for (String m1 : map1.keySet()) {
					condi.put("subjectCode", m1);
					detailList = reportCalcuStatusService.queryDetail(condi);
					Cell cell18 = row3.createCell(map1.get(m1));
					if (null != detailList.get(0).getSumAmount() && !"".equals(detailList.get(0).getSumAmount())) {
						cell18.setCellValue(Double.parseDouble(detailList.get(0).getSumAmount()));
					}else {
						cell18.setCellValue(detailList.get(0).getSumAmount());	
					}	
				}
				//干线的累计
				for (String m2 : map2.keySet()) {
					condi.put("subjectCode", m2);
					Cell cell19 = row3.createCell(map2.get(m2));
					detailList = reportCalcuStatusService.queryDetail(condi);
					if (null != detailList.get(0).getSumAmount() && !"".equals(detailList.get(0).getSumAmount())) {
						cell19.setCellValue(Double.parseDouble(detailList.get(0).getSumAmount()));
					}else {
						cell19.setCellValue(detailList.get(0).getSumAmount());
					}		
				}
				//干线的累计
				for (String m3 : map3.keySet()) {
					condi.put("subjectCode", m3);
					detailList = reportCalcuStatusService.queryDetail(condi);
					Cell cell20 = row3.createCell(map3.get(m3));
					if (null != detailList.get(0).getSumAmount() && !"".equals(detailList.get(0).getSumAmount())) {
						cell20.setCellValue(Double.parseDouble(detailList.get(0).getSumAmount()));
					}else {
						cell20.setCellValue(detailList.get(0).getSumAmount());
					}	
				}
			}
		}else {
			Sheet sheet = poiUtil.getXSSFSheet(workbook,"商家各科目计算状态");
			Row row1 = sheet.createRow(0);
			Cell cell4 = row1.createCell(0);
			cell4.setCellValue("无商家有数据");
		}
		
		
	}
	
}
