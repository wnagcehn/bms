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

import org.apache.commons.lang3.StringUtils;
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
import com.jiuyescm.bms.base.dictionary.entity.SystemCodeEntity;
import com.jiuyescm.bms.base.dictionary.service.ISystemCodeService;
import com.jiuyescm.bms.base.group.service.IBmsGroupCustomerService;
import com.jiuyescm.bms.base.group.service.IBmsGroupService;
import com.jiuyescm.bms.base.group.service.IBmsGroupUserService;
import com.jiuyescm.bms.base.group.vo.BmsGroupCustomerVo;
import com.jiuyescm.bms.base.group.vo.BmsGroupVo;
import com.jiuyescm.bms.common.constants.FileConstant;
import com.jiuyescm.bms.report.month.entity.ReportOverdueUnaccountEntity;
import com.jiuyescm.bms.report.month.service.IReportOverdueUnaccountService;
import com.jiuyescm.common.utils.excel.POISXSSUtil;
import com.jiuyescm.exception.BizException;
import com.jiuyescm.mdm.customer.api.ICustomerService;
import com.jiuyescm.mdm.customer.vo.CustomerVo;

/**
 * 
 * @author stevenl
 * 
 */
@Controller("reportOverdueUnaccountController")
public class ReportOverdueUnaccountController {

	private static final Logger logger = Logger.getLogger(ReportOverdueUnaccountController.class.getName());

	@Resource
	private IReportOverdueUnaccountService reportOverdueUnaccountService;
	
	@Resource
	private IBmsGroupUserService bmsGroupUserService;
	
	@Resource 
	private ISystemCodeService systemCodeService;
	
	@Resource
	private IBmsGroupService bmsGroupService;
	
	@Resource
	private IBmsGroupCustomerService bmsGroupCustomerService;
	
	@Resource
	private ICustomerService customerService;
	
	@DataProvider
	public List<ReportOverdueUnaccountEntity> queryAll(Map<String, Object> param){
		List<ReportOverdueUnaccountEntity> unList = null;
		List<ReportOverdueUnaccountEntity> list = null;
		ReportOverdueUnaccountEntity newEntity = null;
		List<ReportOverdueUnaccountEntity> newList = new ArrayList<ReportOverdueUnaccountEntity>();
		
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
		    logger.error("异常:", e);
		}
		
		//超期未收款日期转换
		try {
			unCreateMonthTran(param);
			receiptDateTran(param);
		} catch (Exception e) {
			logger.error("日期转换异常", e);
		}
		
		//权限
//		BmsGroupUserVo groupUser = bmsGroupUserService.queryEntityByUserId(JAppContext.currentUserID());
//		if (null != groupUser) {
//			List<String> userIds = bmsGroupUserService.queryContainUserIds(groupUser);
//			if (userIds.size() == 0) {
//				return newList;	
//			}
//			param.put("userIds", userIds);

			//超期未收款金额
			try {
				unList = reportOverdueUnaccountService.queryTotalAmount(param);
			} catch (Exception e) {
				logger.error("超期未收款金额查询异常", e);
			}		
			
			//应收款日期转换
			createMonthTran(param);
			//应收款总额
			try {
				list = reportOverdueUnaccountService.queryTotalAmount(param);
			} catch (Exception e) {
				logger.error("应收款金额查询异常", e);
			}
			
			double unReceiptAmount = 0d;
			double receiptAmount = 0d;
			//超期未收款金额总计
			double totalOverdueUnaccount=0d;
			//应收款金额总计
			double totalReceiveAccount=0d;
			//超期未收款占比总计
			String totalOverdueUnaccountRatio="";
			DecimalFormat decimalFormat = new DecimalFormat("###################.###########");

			
			for (ReportOverdueUnaccountEntity unEntity : unList) {
				for (ReportOverdueUnaccountEntity entity : list) {
					if (unEntity.getSellerName().equals(entity.getSellerName())) {
						newEntity = new ReportOverdueUnaccountEntity();
						unReceiptAmount = unEntity.getTotalAmount()==null?0d:unEntity.getTotalAmount();
						receiptAmount = entity.getTotalAmount()==null?0d:entity.getTotalAmount();
						newEntity.setArea(unEntity.getArea());
						newEntity.setSellerId(unEntity.getSellerId());
						newEntity.setSellerName(unEntity.getSellerName());
						newEntity.setUnReceiptAmount(unEntity.getTotalAmount());
						newEntity.setReceiptAmount(entity.getTotalAmount());
						if (receiptAmount == 0 || unReceiptAmount < 0) {
							newEntity.setOverdueUnaccountRatio("0%");
						}else {
							newEntity.setOverdueUnaccountRatio(decimalFormat.format(new BigDecimal(unReceiptAmount).divide(new BigDecimal(receiptAmount), 2, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)).doubleValue())+"%");
						}
						newList.add(newEntity);
						break;
					}
				}
			}
			
			//拼接前端占比总计
			for(int i=0;i<newList.size();i++){	
				ReportOverdueUnaccountEntity entity = newList.get(i);
				//超期未收款金额总计
				totalOverdueUnaccount+=(entity.getUnReceiptAmount()==null?0d:entity.getUnReceiptAmount().doubleValue());
				//应收款金额总计
				totalReceiveAccount+=(entity.getReceiptAmount()==null?0d:entity.getReceiptAmount().doubleValue());
			}
			
			//超期未收款占比总计
			try {
				//分母为0
				if (totalReceiveAccount == 0d || totalOverdueUnaccount < 0) {
					totalOverdueUnaccountRatio = "0%";
				}else {
					totalOverdueUnaccountRatio = decimalFormat.format(new BigDecimal(totalOverdueUnaccount).divide(new BigDecimal(totalReceiveAccount), 2, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)).doubleValue())+"%";
				}
			} catch (Exception e) {
				logger.error("占比计算异常", e);
			}
			if (null != newList && newList.size() > 0) {
				newList.get(0).setTotalOverdueUnaccountRatio(totalOverdueUnaccountRatio);
			}	
		//}
		return newList;	
	}

	private void unCreateMonthTran(Map<String, Object> param) {
		Integer month = Integer.parseInt(param.get("month").toString())-2;
		if (month <= 0) {
			Integer year = Integer.parseInt(param.get("year").toString())-1;
			Integer newmonth=month+12;
			param.put("createMonth", String.valueOf(year).substring(2,4) +newmonth);
		}else if (month >= 10) {
			param.put("createMonth", param.get("year").toString().substring(2, 4) + month.toString());
		}else {
			param.put("createMonth", param.get("year").toString().substring(2, 4) + "0" + month.toString());
		}
	}
	
	private void createMonthTran(Map<String, Object> param) {
		Integer month = Integer.parseInt(param.get("month").toString())+1;
		if (month > 12) {
			Integer year = Integer.parseInt(param.get("year").toString())+1;
			param.put("createMonth", String.valueOf(year).substring(2,4) + "0" + (month-12));
		}else if (month < 10) {
			param.put("createMonth", param.get("year").toString().substring(2, 4) + "0" + month.toString());
		}else {
			param.put("createMonth", param.get("year").toString().substring(2, 4) + month.toString());
		}
	}
	
	private void receiptDateTran(Map<String, Object> param) {
		Integer month = Integer.parseInt(param.get("month").toString())+1;
		if (month > 12) {
			Integer year = Integer.parseInt(param.get("year").toString())+1;
			param.put("receiptDate", String.valueOf(year) + "-0" + (month-12));
		}else if(month < 10){
			param.put("receiptDate", param.get("year").toString() + "-0" + month.toString());
		}else {
			param.put("receiptDate", param.get("year").toString() + "-" + month.toString());
		}
	}
	
	/**
	 * 超期未收款占比导出
	 * @param param
	 * @return
	 * @throws Exception
	 */
	@FileProvider
	public DownloadFile export(Map<String,Object> param) throws Exception{
		long beginTime = System.currentTimeMillis();
    	logger.info("====超期未收款占比导出：写入Excel begin.");
    	
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
			String fileName = "超期未收款占比" + FileConstant.SUFFIX_XLSX;
			String filePath = path + FileConstant.SEPARATOR + fileName;
			File file = new File(filePath);
			if (file.exists()) {
				file.delete();
			}
			
			POISXSSUtil poiUtil = new POISXSSUtil();
	    	SXSSFWorkbook workbook = poiUtil.getXSSFWorkbook();
	    		    		    	
	        //导出方法
	    	exportOverdueUnaccount(poiUtil, workbook, filePath, param,dictcodeMap);
	    	
	    	//最后写到文件
	    	poiUtil.write2FilePath(workbook, filePath);
	    		    	
	    	logger.info("====超期未收款占比：写入Excel end.==总耗时：" + (System.currentTimeMillis() - beginTime));

	    	InputStream is = new FileInputStream(filePath);
	    	return new DownloadFile(fileName, is);
		} catch (Exception e) {
			//bmsErrorLogInfoService.
			logger.error("超期未收款占比导出失败", e);
		}
		return null;
	}
	
	private String getPath(){
		SystemCodeEntity systemCodeEntity = systemCodeService.getSystemCode("GLOABL_PARAM", "EXPORT_OVERDUE_UNACCOUNT");
		if(systemCodeEntity == null){
			throw new BizException("请在系统参数中配置文件上传路径,参数GLOABL_PARAM,EXPORT_OVERDUE_UNACCOUNT");
		}
		return systemCodeEntity.getExtattr1();
	}
	
	
	/**
	 * 超期未收款占比导出
	 * @param poiUtil
	 * @param workbook
	 * @param path
	 * @param param
	 * @param dictcodeMap
	 * @throws Exception
	 */
	private void exportOverdueUnaccount(POISXSSUtil poiUtil, SXSSFWorkbook workbook, 
			String path, Map<String, Object> param,Map<String, Object> dictcodeMap)throws Exception{
		logger.info("超期未收款占比信息导出...");
        
		//数据查询
		List<ReportOverdueUnaccountEntity> newList = this.queryAll(param);
		
//		List<ReportOverdueUnaccountEntity> unList = null;
//		List<ReportOverdueUnaccountEntity> list = null;
//		ReportOverdueUnaccountEntity newEntity = null;
//		List<ReportOverdueUnaccountEntity> newList = new ArrayList<ReportOverdueUnaccountEntity>();
//		//超期未收款日期转换
//		try {
//			unCreateMonthTran(param);
//			receiptDateTran(param);
//		} catch (Exception e) {
//			logger.error("日期转换异常", e);
//		}
//		
//		//如果区域不存在，加权限
//		if (!param.containsKey("area")) {
//			BmsGroupUserVo groupUser = bmsGroupUs  erService.queryEntityByUserId(JAppContext.currentUserID());
//			if (null != groupUser) {
//				List<String> userIds = bmsGroupUserService.queryContainUserIds(groupUser);
//				if (userIds.size() != 0) {
//					param.put("userIds", userIds);
//				}
//			}
//		}
//
//		//超期未收款金额
//		try {
//			unList = reportOverdueUnaccountService.queryTotalAmount(param);
//		} catch (Exception e) {
//			logger.error("超期未收款金额查询异常", e);
//		}		
//		
//		//应收款日期转换
//		createMonthTran(param);
//		//应收款总额
//		try {
//			list = reportOverdueUnaccountService.queryTotalAmount(param);
//		} catch (Exception e) {
//			logger.error("应收款金额查询异常", e);
//		}
//		
//		for (ReportOverdueUnaccountEntity unEntity : unList) {
//			for (ReportOverdueUnaccountEntity entity : list) {
//				if (unEntity.getSellerName().equals(entity.getSellerName())) {
//					//BmsGroupUserVo areaEntity = bmsGroupUserService.queryGroupNameByUserId(entity.getSellerId());
//					newEntity = new ReportOverdueUnaccountEntity();
//					newEntity.setArea(unEntity.getArea());
//					newEntity.setSellerId(unEntity.getSellerId());
//					newEntity.setSellerName(unEntity.getSellerName());
//					newEntity.setUnReceiptAmount(unEntity.getUnReceiptAmount());
//					newEntity.setReceiptAmount(entity.getReceiptAmount());
//					newEntity.setOverdueUnaccountRatio(new BigDecimal(unEntity.getUnReceiptAmount()).divide(new BigDecimal(entity.getReceiptAmount()), BigDecimal.ROUND_HALF_UP).doubleValue()+"%");
//					newList.add(newEntity);
//				}
//			}
//		}
				
		logger.info("超期未收款占比导出生成sheet。。。");
		Sheet sheet = poiUtil.getXSSFSheet(workbook,"超期未收款占比");
		
		sheet.setColumnWidth(1, 4000);
		sheet.setColumnWidth(2, 4000);
		sheet.setColumnWidth(3, 4000);
		sheet.setColumnWidth(4, 4000);
		sheet.setColumnWidth(5, 4000);
		
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
		 cell2.setCellValue("超期未收款");
		 Cell cell3 = row0.createCell(3);
		 cell3.setCellValue("应收款总额");
		 cell3.setCellStyle(style);
		 Cell cell4 = row0.createCell(4);
		 cell4.setCellValue("超期未收款占比");
		 cell4.setCellStyle(style);
		 
		logger.info("超期未收款占比导出给sheet赋值。。。");
		CellStyle style2 = workbook.createCellStyle();
        DataFormat df = workbook.createDataFormat(); // 此处设置数据格式
        style2.setDataFormat(df.getFormat("###,###,###,##0.00"));//数据格式只显示整数
		
		//超期未收款金额总计
		double totalOverdueUnaccount=0d;
		//应收款金额总计
		double totalReceiveAccount=0d;
		//超期未收款占比总计
		String totalOverdueUnaccountRatio="";
		
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
			ReportOverdueUnaccountEntity entity = newList.get(i);
			Row row = sheet.createRow(RowIndex);
			RowIndex++;
			Cell cel0 = row.createCell(0);
			cel0.setCellValue(entity.getSellerName());
			Cell cel1 = row.createCell(1);
			cel1.setCellValue(mapValue.get(entity.getArea()));
			Cell cel2 = row.createCell(2);
			cel2.setCellValue(entity.getUnReceiptAmount()==null?0d:entity.getUnReceiptAmount());
			cel2.setCellStyle(style2);
			Cell cel3 = row.createCell(3);
			cel3.setCellValue(entity.getReceiptAmount()==null?0d:entity.getReceiptAmount());
			cel3.setCellStyle(style2);
			Cell cel4 = row.createCell(4);
			cel4.setCellValue(entity.getOverdueUnaccountRatio());		
			//超期未收款金额总计
			totalOverdueUnaccount+=(entity.getUnReceiptAmount()==null?0d:entity.getUnReceiptAmount().doubleValue());
			//应收款金额总计
			totalReceiveAccount+=(entity.getReceiptAmount()==null?0d:entity.getReceiptAmount().doubleValue());
			

		}
		//超期未收款占比总计
		try {
			//分母为0
			if (totalReceiveAccount == 0d || totalOverdueUnaccount < 0) {
				totalOverdueUnaccountRatio = "0%";
			}else {
				totalOverdueUnaccountRatio = decimalFormat.format(new BigDecimal(totalOverdueUnaccount).divide(new BigDecimal(totalReceiveAccount), 2, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)).doubleValue())+"%";
			}
		} catch (Exception e) {
			logger.error("占比计算异常", e);
		}
		
		if (newList != null) {
			RowIndex++;
		}
		Row lastRow = sheet.createRow(RowIndex);
		Cell cellast = lastRow.createCell(0);
		cellast.setCellValue("合计：");
		
		Cell cellast0 = lastRow.createCell(2);
		cellast0.setCellValue(totalOverdueUnaccount);
		cellast0.setCellStyle(style2);
		Cell cellast1 = lastRow.createCell(3);
		cellast1.setCellValue(totalReceiveAccount);
		cellast1.setCellStyle(style2);
		Cell cellast2 = lastRow.createCell(4);
		cellast2.setCellValue(totalOverdueUnaccountRatio);
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
