/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.report.month.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.omg.CORBA.OBJ_ADAPTER;
import org.springframework.stereotype.Controller;

import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.uploader.DownloadFile;
import com.bstek.dorado.uploader.annotation.FileProvider;
import com.jiuyescm.bms.base.customer.entity.PubCustomerSaleMapperEntity;
import com.jiuyescm.bms.base.customer.service.IPubCustomerSaleMapperService;
import com.jiuyescm.bms.base.dictionary.entity.SystemCodeEntity;
import com.jiuyescm.bms.base.dictionary.service.ISystemCodeService;
import com.jiuyescm.bms.base.group.service.IBmsGroupUserService;
import com.jiuyescm.bms.base.group.vo.BmsGroupUserVo;
import com.jiuyescm.bms.common.constants.FileConstant;
import com.jiuyescm.bms.report.month.entity.ReportCollectionRateEntity;
import com.jiuyescm.bms.report.month.service.IReportCollectionRateService;
import com.jiuyescm.cfm.common.JAppContext;
import com.jiuyescm.common.utils.excel.POISXSSUtil;
import com.jiuyescm.exception.BizException;

/**
 * 
 * @author stevenl
 * 
 */
@Controller("reportCollectionRateController")
public class ReportCollectionRateController {

	private static final Logger logger = Logger.getLogger(ReportCollectionRateController.class.getName());

	@Resource
	private IReportCollectionRateService reportCollectionRateService;
	
	@Resource
	private IBmsGroupUserService bmsGroupUserService;
	
	@Resource 
	private ISystemCodeService systemCodeService;
	
	@Resource
	private IPubCustomerSaleMapperService pubCustomerSaleMapperService;
	
	@DataProvider
	public List<ReportCollectionRateEntity> queryAll(Map<String, Object> param){
		Map<String, Object> maps = new HashMap<String, Object>();
		List<ReportCollectionRateEntity> list = null;
		List<PubCustomerSaleMapperEntity> sellerList = null;
		ReportCollectionRateEntity newEntity = null;
		List<ReportCollectionRateEntity> newList = new ArrayList<ReportCollectionRateEntity>();
		//日期转换
		try {
			createMonthTran(param);
		} catch (Exception e) {
			logger.error("日期转换异常", e);
		}
		
		//如果区域不存在，加权限
		if (!param.containsKey("area")) {
			BmsGroupUserVo groupUser = bmsGroupUserService.queryEntityByUserId(JAppContext.currentUserID());
			if (null != groupUser) {
				List<String> userIds = bmsGroupUserService.queryContainUserIds(groupUser);
				if (userIds.size() == 0) {
					return newList;
				}
				param.put("userIds", userIds);			
			}
		}
		
		double underOneYear = 0d;
		double betweenOneAndTwo = 0d;
		double overTwoTear = 0d;
		double newSellerReceipt = 0d;
		//查询月份和金额
		list = reportCollectionRateService.queryAmount(param);
		
		if (null != list && list.size()>0) {
			for (ReportCollectionRateEntity reportCollectionRateEntity : list) {
				//newEntity = new ReportCollectionRateEntity();
				
//				//查询交换销售员
//				if (reportCollectionRateEntity.getCustomerName() != null) {
//					maps.put("customerName", reportCollectionRateEntity.getCustomerName());
//					sellerList = pubCustomerSaleMapperService.query(maps);
//				}
//				
//				//存在此商家的销售员，并且和原始销售员不相同
//				if (null != sellerList && !reportCollectionRateEntity.getSellerId().equals(sellerList.get(0).getOriginSellerId())) {
//					newEntity.setNewSellerReceipt(reportCollectionRateEntity.getReceiptAmount());
//					continue;
//				}
//				
//				//一年内的
//				if (reportCollectionRateEntity.getReceiptMonth() <= 12) {
//					newEntity.setReceiptWithinOneYear(reportCollectionRateEntity.getReceiptAmount());
//				}else if (reportCollectionRateEntity.getReceiptMonth() > 12 && reportCollectionRateEntity.getReceiptMonth() <= 24) {
//				//一年-两年	
//					newEntity.setReceiptBetweenOneAndTwoYear(reportCollectionRateEntity.getReceiptAmount());
//				}else if (reportCollectionRateEntity.getReceiptMonth() > 24) {
//				//两年以上的	
//					newEntity.setReceiptOverTwoYear(reportCollectionRateEntity.getReceiptAmount());
//				}else {
//					continue;
//				}
				underOneYear = reportCollectionRateEntity.getReceiptWithinOneYear() == null ? 0d : reportCollectionRateEntity.getReceiptWithinOneYear();
				betweenOneAndTwo = reportCollectionRateEntity.getReceiptBetweenOneAndTwoYear() == null ? 0d : reportCollectionRateEntity.getReceiptBetweenOneAndTwoYear();
				overTwoTear = reportCollectionRateEntity.getReceiptOverTwoYear() == null ? 0d : reportCollectionRateEntity.getReceiptOverTwoYear();
				newSellerReceipt = reportCollectionRateEntity.getHandoverCustomerReceipt() == null ? 0d : reportCollectionRateEntity.getHandoverCustomerReceipt();
				//收款合计
				reportCollectionRateEntity.setReceiptTotal(underOneYear+betweenOneAndTwo+overTwoTear+newSellerReceipt);
				
				//收款达成率
				if (reportCollectionRateEntity.getReceiptTarget() == null || reportCollectionRateEntity.getReceiptTarget() == 0) {
					reportCollectionRateEntity.setReceiptCollectionRate("0%");
				}else {
					reportCollectionRateEntity.setReceiptCollectionRate(new BigDecimal(reportCollectionRateEntity.getTotalAmount()).divide(new BigDecimal(reportCollectionRateEntity.getReceiptTarget()), BigDecimal.ROUND_HALF_UP).doubleValue()+"%");
				}
				newList.add(reportCollectionRateEntity);
			}
		}
		return newList;		
	}

	private void createMonthTran(Map<String, Object> param) {
		Integer month = Integer.parseInt(param.get("month").toString());
		if (month >= 10) {
			param.put("createMonth", param.get("year").toString().substring(2, 4) + month.toString());
		}else {
			param.put("createMonth", param.get("year").toString().substring(2, 4) + "0" + month.toString());
		}
	}
	
	/**
	 * 收款达成率导出
	 * @param param
	 * @return
	 * @throws Exception
	 */
	@FileProvider
	public DownloadFile export(Map<String,Object> param) throws Exception{
		long beginTime = System.currentTimeMillis();
    	logger.info("====收款达成率导出：写入Excel begin.");
    	
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
			String fileName = "收款达成率" + FileConstant.SUFFIX_XLSX;
			String filePath = path + FileConstant.SEPARATOR + fileName;
			File file = new File(filePath);
			if (file.exists()) {
				file.delete();
			}
			
			POISXSSUtil poiUtil = new POISXSSUtil();
	    	SXSSFWorkbook workbook = poiUtil.getXSSFWorkbook();
	    		    		    	
	        //导出方法
	    	exportCollectionRate(poiUtil, workbook, filePath, param,dictcodeMap);
	    	
	    	//最后写到文件
	    	poiUtil.write2FilePath(workbook, filePath);
	    		    	
	    	logger.info("====收款达成率：写入Excel end.==总耗时：" + (System.currentTimeMillis() - beginTime));

	    	InputStream is = new FileInputStream(filePath);
	    	return new DownloadFile(fileName, is);
		} catch (Exception e) {
			//bmsErrorLogInfoService.
			logger.error("收款达成率导出失败", e);
		}
		return null;
	}
	
	private String getPath(){
		SystemCodeEntity systemCodeEntity = systemCodeService.getSystemCode("GLOABL_PARAM", "EXPORT_COLLECTION_RATE");
		if(systemCodeEntity == null){
			throw new BizException("请在系统参数中配置文件上传路径,参数GLOABL_PARAM,EXPORT_COLLECTION_RATE");
		}
		return systemCodeEntity.getExtattr1();
	}
	
	
	/**
	 * 收款达成率导出
	 * @param poiUtil
	 * @param workbook
	 * @param path
	 * @param param
	 * @param dictcodeMap
	 * @throws Exception
	 */
	private void exportCollectionRate(POISXSSUtil poiUtil, SXSSFWorkbook workbook, 
			String path, Map<String, Object> param,Map<String, Object> dictcodeMap)throws Exception{
			logger.info("收款达成率信息导出...");
        
			List<ReportCollectionRateEntity> newList = this.queryAll(param);
					
			logger.info("收款达成率导出生成sheet。。。");
			Sheet sheet = poiUtil.getXSSFSheet(workbook,"收款达成率");
			
			sheet.setColumnWidth(0, 3500);
			sheet.setColumnWidth(1, 4000);
			sheet.setColumnWidth(2, 6000);
			sheet.setColumnWidth(3, 6000);
			sheet.setColumnWidth(4, 6000);
			sheet.setColumnWidth(5, 4000);
			sheet.setColumnWidth(6, 4000);
			sheet.setColumnWidth(7, 4000);
			sheet.setColumnWidth(8, 4000);
			
			Font font = workbook.createFont();
		    font.setBoldweight(Font.BOLDWEIGHT_BOLD);
			 CellStyle style = workbook.createCellStyle();
			 style.setAlignment(CellStyle.ALIGN_CENTER);
			 style.setWrapText(true);
			 style.setFont(font);
			 
			 Row row0 = sheet.createRow(0);
			 Cell cell0 = row0.createCell(0);
			 cell0.setCellValue("销售");
			 cell0.setCellStyle(style);
			 Cell cell1 = row0.createCell(1);
			 cell1.setCellValue("区域");
			 cell1.setCellStyle(style);
			 Cell cell2 = row0.createCell(2);
			 cell2.setCellStyle(style);
			 cell2.setCellValue("启动时间一年以内收款");
			 Cell cell3 = row0.createCell(3);
			 cell3.setCellValue("启动时间1-2年的客户收款");
			 cell3.setCellStyle(style);
			 Cell cell4 = row0.createCell(4);
			 cell4.setCellValue("启动时间超过2年的收款");
			 cell4.setCellStyle(style);
			 Cell cell5 = row0.createCell(5);
			 cell5.setCellValue("交接客户收款");
			 cell5.setCellStyle(style);
			 Cell cell6 = row0.createCell(6);
			 cell6.setCellValue("收款合计");
			 cell6.setCellStyle(style);
			 Cell cell7 = row0.createCell(7);
			 cell7.setCellValue("收款指标");
			 cell7.setCellStyle(style);
			 Cell cell8 = row0.createCell(8);
			 cell8.setCellValue("收款达成率");
			 cell8.setCellStyle(style);
			 
			logger.info("收款达成率导出给sheet赋值。。。");
			//启动时间一年以内收款总计
			double totalUnderOneYear=0d;
			//启动时间1-2年的客户收款总计
			double totalBetweenOneAndTwo=0d;
			//启动时间超过2年的收款总计
			double totalOverTwoYear=0d;
			//交接客户收款总计
			double totalNewSellerReceipt=0d;
			//收款合计总计
			double totalReceiptAmount=0d;
			//收款指标总计
			double totalReceiptTarget=0d;
			//收款达成率总计
			String totalCollectionRate="";

			int RowIndex = 1;
			for(int i=0;i<newList.size();i++){	
				ReportCollectionRateEntity entity = newList.get(i);
				Row row = sheet.createRow(RowIndex);
				RowIndex++;
				Cell cel0 = row.createCell(0);
				cel0.setCellValue(entity.getSellerName());
				Cell cel1 = row.createCell(1);
				cel1.setCellValue(entity.getArea());
				Cell cel2 = row.createCell(2);
				cel2.setCellValue(entity.getReceiptWithinOneYear());
				Cell cel3 = row.createCell(3);
				cel3.setCellValue(entity.getReceiptBetweenOneAndTwoYear());
				Cell cel4 = row.createCell(4);
				cel4.setCellValue(entity.getReceiptOverTwoYear());
				Cell cel5 = row.createCell(5);
				cel5.setCellValue(entity.getHandoverCustomerReceipt());
				Cell cel6 = row.createCell(6);
				cel6.setCellValue(entity.getReceiptTotal());
				Cell cel7 = row.createCell(7);
				cel7.setCellValue(entity.getReceiptTarget());
				Cell cel8 = row.createCell(8);
				cel8.setCellValue(entity.getReceiptCollectionRate());
				
				//启动时间一年以内收款总计
				totalUnderOneYear+=(entity.getReceiptWithinOneYear()==null?0d:entity.getReceiptWithinOneYear().doubleValue());
				//启动时间1-2年的客户收款总计
				totalBetweenOneAndTwo+=(entity.getReceiptBetweenOneAndTwoYear()==null?0d:entity.getReceiptBetweenOneAndTwoYear().doubleValue());
				//启动时间超过2年的收款总计
				totalOverTwoYear+=(entity.getReceiptOverTwoYear()==null?0d:entity.getReceiptOverTwoYear().doubleValue());
				//交接客户收款总计
				totalNewSellerReceipt+=(entity.getHandoverCustomerReceipt()==null?0d:entity.getHandoverCustomerReceipt().doubleValue());
				//收款合计总计
				totalReceiptAmount+=(entity.getReceiptTotal()==null?0d:entity.getReceiptTotal().doubleValue());
				//收款指标总计
				totalReceiptTarget+=(entity.getReceiptTarget()==null?0d:entity.getReceiptTarget().doubleValue());

			}
			//收款达成率总计
			try {
				if (totalReceiptTarget == 0d) {
					totalCollectionRate = "0%";
				}
				totalCollectionRate = new BigDecimal(totalReceiptAmount).divide(new BigDecimal(totalReceiptTarget), BigDecimal.ROUND_HALF_UP).intValue()+"%";
			} catch (Exception e) {
				logger.error("达成率计算异常", e);
			}
			
			if (newList != null || newList.size() == 0) {
				RowIndex++;
			}
			
			Row lastRow = sheet.createRow(RowIndex);
			Cell cellast = lastRow.createCell(0);
			cellast.setCellValue("合计：");
			
			Cell cellast0 = lastRow.createCell(2);
			cellast0.setCellValue(ReportCollectionRateController.getCommaFormat(new BigDecimal(totalUnderOneYear)));
			Cell cellast1 = lastRow.createCell(3);
			cellast1.setCellValue(ReportCollectionRateController.getCommaFormat(new BigDecimal(totalBetweenOneAndTwo)));
			Cell cellast2 = lastRow.createCell(4);
			cellast2.setCellValue(ReportCollectionRateController.getCommaFormat(new BigDecimal(totalOverTwoYear)));
			Cell cellast3 = lastRow.createCell(5);
			cellast3.setCellValue(ReportCollectionRateController.getCommaFormat(new BigDecimal(totalNewSellerReceipt)));
			Cell cellast4 = lastRow.createCell(6);
			cellast4.setCellValue(ReportCollectionRateController.getCommaFormat(new BigDecimal(totalReceiptAmount)));
			Cell cellast5 = lastRow.createCell(7);
			cellast5.setCellValue(ReportCollectionRateController.getCommaFormat(new BigDecimal(totalReceiptTarget)));
			Cell cellast6 = lastRow.createCell(8);
			cellast6.setCellValue(totalCollectionRate);
	}

	
	//每3位中间添加逗号的格式化显示 
	public static String getCommaFormat(BigDecimal value){  
        return getFormat(",###.##",value);  
    }
	
	//自定义数字格式方法  
    public static String getFormat(String style,BigDecimal value){  
        DecimalFormat df = new DecimalFormat();  
        df.applyPattern(style);// 将格式应用于格式化器  
        return df.format(value.doubleValue());  
    }  
}
