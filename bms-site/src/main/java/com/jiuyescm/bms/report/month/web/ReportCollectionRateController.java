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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.stereotype.Controller;

import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.uploader.DownloadFile;
import com.bstek.dorado.uploader.annotation.FileProvider;
import com.jiuyescm.bms.base.customer.service.IPubCustomerSaleMapperService;
import com.jiuyescm.bms.base.dictionary.entity.SystemCodeEntity;
import com.jiuyescm.bms.base.dictionary.service.ISystemCodeService;
import com.jiuyescm.bms.base.group.service.IBmsGroupCustomerService;
import com.jiuyescm.bms.base.group.service.IBmsGroupService;
import com.jiuyescm.bms.base.group.service.IBmsGroupUserService;
import com.jiuyescm.bms.base.group.vo.BmsGroupCustomerVo;
import com.jiuyescm.bms.base.group.vo.BmsGroupVo;
import com.jiuyescm.bms.common.constants.FileConstant;
import com.jiuyescm.bms.report.month.entity.ReportCollectionRateEntity;
import com.jiuyescm.bms.report.month.service.IReportCollectionRateService;
import com.jiuyescm.common.utils.excel.POISXSSUtil;
import com.jiuyescm.exception.BizException;
import com.jiuyescm.mdm.customer.api.ICustomerService;
import com.jiuyescm.mdm.customer.vo.CustomerVo;

/**
 * 收款达成率报表
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
	
	@Resource
	private ICustomerService customerService;
	
	@Resource
	private IBmsGroupService bmsGroupService;
	
	@Resource
	private IBmsGroupCustomerService bmsGroupCustomerService;
	
	@DataProvider
	public List<ReportCollectionRateEntity> queryAll(Map<String, Object> param){
		List<ReportCollectionRateEntity> list = null;
		List<ReportCollectionRateEntity> newList = new ArrayList<ReportCollectionRateEntity>();
		
		//指定的异常商家
		try {			
			Map<String,String> customerMap=customerMap();;
			Map<String, Object> map= new HashMap<String, Object>();
			map.put("groupCode", "error_customer");
			map.put("bizType", "group_customer");
			BmsGroupVo bmsGroup=bmsGroupService.queryOne(map);
			if(bmsGroup!=null){			
				List<BmsGroupCustomerVo> custList=bmsGroupCustomerService.queryAllByGroupId(bmsGroup.getId());
				List<String> billList=new ArrayList<String>();
				for(BmsGroupCustomerVo vo:custList){
					billList.add(customerMap.get(vo.getCustomerid()));
				}
				if (billList.size() > 0) {
					param.put("billList", billList);
				}		
			}	
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//日期转换
		try {
			//createMonthTran(param);
			receiptDateTran(param);
			reeciptTarget(param);
		} catch (Exception e) {
			logger.error("日期转换异常", e);
		}
					
	
//		double underOneYear = 0d;
//		double betweenOneAndTwo = 0d;
//		double overTwoTear = 0d;
//		double newSellerReceipt = 0d;
		DecimalFormat decimalFormat = new DecimalFormat("###################.###########");
		//查询月份和金额
		list = reportCollectionRateService.queryAmount(param);
		
		if (null != list && list.size()>0) {
			for (ReportCollectionRateEntity reportCollectionRateEntity : list) {
//				underOneYear = reportCollectionRateEntity.getReceiptWithinOneYear() == null ? 0d : reportCollectionRateEntity.getReceiptWithinOneYear();
//				betweenOneAndTwo = reportCollectionRateEntity.getReceiptBetweenOneAndTwoYear() == null ? 0d : reportCollectionRateEntity.getReceiptBetweenOneAndTwoYear();
//				overTwoTear = reportCollectionRateEntity.getReceiptOverTwoYear() == null ? 0d : reportCollectionRateEntity.getReceiptOverTwoYear();
//				newSellerReceipt = reportCollectionRateEntity.getHandoverCustomerReceipt() == null ? 0d : reportCollectionRateEntity.getHandoverCustomerReceipt();
//				//收款合计
//				reportCollectionRateEntity.setReceiptTotal(underOneYear+betweenOneAndTwo+overTwoTear+newSellerReceipt);
				
				//收款达成率
				if (reportCollectionRateEntity.getReceiptTarget() == null || reportCollectionRateEntity.getReceiptTarget() == 0 || reportCollectionRateEntity.getReceiptTotal() < 0) {
					reportCollectionRateEntity.setReceiptCollectionRate("0%");
				}else {
					reportCollectionRateEntity.setReceiptCollectionRate(decimalFormat.format(new BigDecimal(reportCollectionRateEntity.getReceiptTotal()).divide(new BigDecimal(reportCollectionRateEntity.getReceiptTarget()), 2, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)).doubleValue())+"%");
				}
				newList.add(reportCollectionRateEntity);
			}
			
			//拼接前端占比总计
			double totalRecAmount = 0d;
			double totalRecTarget = 0d;
			String totalCollecRate = "";
			for(int i=0;i<newList.size();i++){	
				ReportCollectionRateEntity entity = newList.get(i);
				//收款合计总计
				totalRecAmount+=(entity.getReceiptTotal()==null?0d:entity.getReceiptTotal().doubleValue());
				//收款指标总计
				totalRecTarget+=(entity.getReceiptTarget()==null?0d:entity.getReceiptTarget().doubleValue());
			}
			//收款达成率总计
			try {
				if (totalRecAmount == 0d || totalRecAmount < 0 || totalRecTarget == 0d || totalRecTarget < 0) {
					totalCollecRate = "0%";
				}else {
					totalCollecRate = decimalFormat.format(new BigDecimal(totalRecAmount).divide(new BigDecimal(totalRecTarget), 2, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)).doubleValue())+"%";
				}
			} catch (Exception e) {
				logger.error("达成率计算异常", e);
			}
			if (newList != null && newList.size() > 0) {
				newList.get(0).setTotalCollecRate(totalCollecRate);
			}	
		}	
		return newList;		
	}

//	private void createMonthTran(Map<String, Object> param) {
//		Integer month = Integer.parseInt(param.get("month").toString())-1;
//		if (month < 0) {
//			Integer year = Integer.parseInt(param.get("year").toString())-1;
//			param.put("createMonth", String.valueOf(year).substring(2,4) + "0" + month);
//		}else if (month > 10) {
//			param.put("createMonth", param.get("year").toString().substring(2, 4) + month.toString());
//		}else {
//			param.put("createMonth", param.get("year").toString().substring(2, 4) + "0" + month.toString());
//		}
//	}
	
	private void receiptDateTran(Map<String, Object> param) {
		Integer month = Integer.parseInt(param.get("month").toString())+1;
		if (Integer.parseInt(param.get("month").toString()) < 10) {
			param.put("receiptDate", param.get("year").toString() + "-0" + param.get("month").toString());
		}else {
			param.put("receiptDate", param.get("year").toString() + "-" + param.get("month").toString());
		}
		
		if (month > 12) {
			Integer year = Integer.parseInt(param.get("year").toString())+1;
			param.put("receiptEndDate", String.valueOf(year) + "-0" + (month-12));
		}else if(month < 10){
			param.put("receiptEndDate", param.get("year").toString() + "-0" + month.toString());
		}else {
			param.put("receiptEndDate", param.get("year").toString() + "-" + month.toString());
		}	
	}
	
	private void reeciptTarget(Map<String, Object> param) {
		Integer month = Integer.parseInt(param.get("month").toString())-1;
		if (month <= 0) {
			Integer year = Integer.parseInt(param.get("year").toString())-1;
			Integer newmonth=month+12;
			param.put("createMonth", String.valueOf(year).substring(2,4) +  newmonth);
		}else if (month >= 10) {
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
			
			CellStyle style2 = workbook.createCellStyle();
	        DataFormat df = workbook.createDataFormat(); // 此处设置数据格式
	        style2.setDataFormat(df.getFormat("###,###,###,##0.00"));//数据格式只显示整数
			
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
			
			//3位数逗号隔开
			DecimalFormat decimalFormat = new DecimalFormat("###################.###########");
			
			//区域code转name
			Map<String, String> mapValue = new LinkedHashMap<String, String>();
			List<SystemCodeEntity> tmscodels = systemCodeService.findEnumList("SALE_AREA");
			for (SystemCodeEntity SystemCodeEntity : tmscodels) {
				mapValue.put(SystemCodeEntity.getCode(), SystemCodeEntity.getCodeName());
			}
			
			int RowIndex = 1;
			for(int i=0;i<newList.size();i++){	
				ReportCollectionRateEntity entity = newList.get(i);
				Row row = sheet.createRow(RowIndex);
				RowIndex++;
				Cell cel0 = row.createCell(0);
				cel0.setCellValue(entity.getSellerName());
				Cell cel1 = row.createCell(1);
				cel1.setCellValue(mapValue.get(entity.getArea()));
				Cell cel2 = row.createCell(2);
				cel2.setCellValue(entity.getReceiptWithinOneYear()==null?0d:entity.getReceiptWithinOneYear());
				cel2.setCellStyle(style2);
				Cell cel3 = row.createCell(3);
				cel3.setCellValue(entity.getReceiptBetweenOneAndTwoYear()==null?0d:entity.getReceiptBetweenOneAndTwoYear());
				cel3.setCellStyle(style2);
				Cell cel4 = row.createCell(4);
				cel4.setCellValue(entity.getReceiptOverTwoYear()==null?0d:entity.getReceiptOverTwoYear());
				cel4.setCellStyle(style2);
				Cell cel5 = row.createCell(5);
				cel5.setCellValue(entity.getHandoverCustomerReceipt()==null?0d:entity.getHandoverCustomerReceipt());
				cel5.setCellStyle(style2);	
				Cell cel6 = row.createCell(6);
				cel6.setCellValue(entity.getReceiptTotal()==null?0d:entity.getReceiptTotal());
				cel6.setCellStyle(style2);			
				Cell cel7 = row.createCell(7);
				cel7.setCellValue(entity.getReceiptTarget()==null?0d:entity.getReceiptTarget());
				cel7.setCellStyle(style2);			
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
				if (totalReceiptTarget == 0d || totalReceiptAmount < 0) {
					totalCollectionRate = "0%";
				}
				totalCollectionRate = decimalFormat.format(new BigDecimal(totalReceiptAmount).divide(new BigDecimal(totalReceiptTarget), 2, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)).doubleValue())+"%";
			} catch (Exception e) {
				logger.error("达成率计算异常", e);
			}
			
			if (newList != null) {
				RowIndex++;
			}
			
			Row lastRow = sheet.createRow(RowIndex);
			Cell cellast = lastRow.createCell(0);
			cellast.setCellValue("合计：");
			
			Cell cellast0 = lastRow.createCell(2);
			cellast0.setCellValue(totalUnderOneYear);
			cellast0.setCellStyle(style2);
			Cell cellast1 = lastRow.createCell(3);
			cellast1.setCellValue(totalBetweenOneAndTwo);
			cellast1.setCellStyle(style2);
			Cell cellast2 = lastRow.createCell(4);
			cellast2.setCellValue(totalOverTwoYear);
			cellast2.setCellStyle(style2);
			Cell cellast3 = lastRow.createCell(5);
			cellast3.setCellValue(totalNewSellerReceipt);
			cellast3.setCellStyle(style2);
			Cell cellast4 = lastRow.createCell(6);
			cellast4.setCellValue(totalReceiptAmount);
			cellast4.setCellStyle(style2);
			Cell cellast5 = lastRow.createCell(7);
			cellast5.setCellValue(totalReceiptTarget);
			cellast5.setCellStyle(style2);			
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
    
    public Map<String,String> customerMap(){
    	Map<String,String> map=new HashMap<String,String>();
		List<CustomerVo> cusList=customerService.queryAll();
		for(CustomerVo vo:cusList){
			if (StringUtils.isNotBlank(vo.getMkInvoiceName())) {
				map.put(vo.getCustomerid(), vo.getMkInvoiceName());
			}		
		}	
		return map;
    }
}
