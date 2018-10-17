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
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.stereotype.Controller;

import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.data.provider.Page;
import com.bstek.dorado.uploader.DownloadFile;
import com.bstek.dorado.uploader.annotation.FileProvider;
import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.base.dictionary.entity.SystemCodeEntity;
import com.jiuyescm.bms.base.dictionary.service.ISystemCodeService;
import com.jiuyescm.bms.base.group.service.IBmsGroupCustomerService;
import com.jiuyescm.bms.base.group.service.IBmsGroupService;
import com.jiuyescm.bms.base.group.service.IBmsGroupUserService;
import com.jiuyescm.bms.base.group.vo.BmsGroupCustomerVo;
import com.jiuyescm.bms.base.group.vo.BmsGroupVo;
import com.jiuyescm.bms.common.constants.FileConstant;
import com.jiuyescm.bms.report.month.entity.ReportReceiptTargetEntity;
import com.jiuyescm.bms.report.month.service.IReportReceiptTargetService;
import com.jiuyescm.common.utils.excel.POISXSSUtil;
import com.jiuyescm.exception.BizException;

@Controller("reportReceiptTargetController")
public class ReportReceiptTargetController {
	
	private static final Logger logger = Logger.getLogger(ReportReceiptTargetController.class.getName());

	
	@Resource
	private IReportReceiptTargetService reportReceiptTargetService;
	
	@Resource
	private IBmsGroupUserService bmsGroupUserService;
	
	@Resource
	private IBmsGroupService bmsGroupService;
	
	@Resource
	private IBmsGroupCustomerService bmsGroupCustomerService;
	
	@Resource 
	private ISystemCodeService systemCodeService;
	
	@DataProvider
	public void query(Page<ReportReceiptTargetEntity> page, Map<String, Object> parameter) {
		
		if(parameter==null){
			parameter=new HashMap<String, Object>();
		}
		
		List<SystemCodeEntity> systemCodeList = systemCodeService.findEnumList("RECEIPT_TARGET");
		for(SystemCodeEntity code:systemCodeList){
			parameter.put(code.getCode(), code.getExtattr1());
		}
		
		//指定的商家
		try {
		Map<String, Object> map= new HashMap<String, Object>();
		map.put("groupCode", "error_customer");
		map.put("bizType", "group_customer");
		BmsGroupVo bmsGroup=bmsGroupService.queryOne(map);
		if(bmsGroup!=null){			
			List<BmsGroupCustomerVo> custList=bmsGroupCustomerService.queryAllByGroupId(bmsGroup.getId());
			List<String> billList=new ArrayList<String>();
			for(BmsGroupCustomerVo vo:custList){
				billList.add(vo.getMkInvoiceName());
			}
			//parameter.put("billList", billList);
		}
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//时间转换
		createMonthTran2(parameter);
		createMonthTran1To2(parameter);
		createMonthTran1(parameter);
		receiptDateTran(parameter);
		
/*		BmsGroupUserVo groupUser = bmsGroupUserService.queryEntityByUserId(JAppContext.currentUserID());
		if (null != groupUser) {
			List<String> userIds = bmsGroupUserService.queryContainUserIds(groupUser);
			if (userIds.size()>0) {
				parameter.put("userIds", userIds);*/
				PageInfo<ReportReceiptTargetEntity> tmpPageInfo =reportReceiptTargetService.queryAll(parameter, page.getPageNo(), page.getPageSize());
				if (tmpPageInfo != null) {
					page.setEntities(tmpPageInfo.getList());
					page.setEntityCount((int) tmpPageInfo.getTotal());
				}
		/*		
			}
		}*/
	}
	
	private void createMonthTran2(Map<String, Object> param) {
		Integer month = Integer.parseInt(param.get("month").toString())-2;
		if (month < 0) {
			Integer year = Integer.parseInt(param.get("year").toString())-1;
			param.put("createMonthTran2", String.valueOf(year).substring(2,4) + "0" + month);
		}else if (month > 10) {
			param.put("createMonthTran2", param.get("year").toString().substring(2, 4) + month.toString());
		}else {
			param.put("createMonthTran2", param.get("year").toString().substring(2, 4) + "0" + month.toString());
		}
	}
	
	private void createMonthTran1To2(Map<String, Object> param) {
		Integer month = Integer.parseInt(param.get("month").toString())-1;
		if (month < 0) {
			Integer year = Integer.parseInt(param.get("year").toString())-1;
			param.put("createMonthTran1To2", String.valueOf(year).substring(2,4) + "0" + month);
		}else if (month > 10) {
			param.put("createMonthTran1To2", param.get("year").toString().substring(2, 4) + month.toString());
		}else {
			param.put("createMonthTran1To2", param.get("year").toString().substring(2, 4) + "0" + month.toString());
		}
	}
	private void createMonthTran1(Map<String, Object> param) {
		Integer month = Integer.parseInt(param.get("month").toString());
		if(month>10){
			param.put("createMonthTran1", param.get("year").toString().substring(2, 4) + month);
		}else{
			param.put("createMonthTran1", param.get("year").toString().substring(2, 4) +"0"+ month);
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
	 * 收款指标导出
	 * @param param
	 * @return
	 * @throws Exception
	 */
	@FileProvider
	public DownloadFile export(Map<String,Object> param) throws Exception{
		long beginTime = System.currentTimeMillis();
    	logger.info("====收款指标报表导出：写入Excel begin.");
    	
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
			String fileName = "收款指标报表" + FileConstant.SUFFIX_XLSX;
			String filePath = path + FileConstant.SEPARATOR + fileName;
			File file = new File(filePath);
			if (file.exists()) {
				file.delete();
			}
			
			POISXSSUtil poiUtil = new POISXSSUtil();
	    	SXSSFWorkbook workbook = poiUtil.getXSSFWorkbook();
	    		    		    	
	        //导出方法
	    	hand(poiUtil, workbook, filePath, param,dictcodeMap);
	    	
	    	//最后写到文件
	    	poiUtil.write2FilePath(workbook, filePath);
	    		    	
	    	logger.info("====收款指标报表：写入Excel end.==总耗时：" + (System.currentTimeMillis() - beginTime));

	    	InputStream is = new FileInputStream(filePath);
	    	return new DownloadFile(fileName, is);
		} catch (Exception e) {
			//bmsErrorLogInfoService.
			logger.error("收款指标报表导出失败", e);
		}
		return null;
	}
	
	
	/**
	 * 收款指标报表导出
	 * @param poiUtil
	 * @param workbook
	 * @param path
	 * @param param
	 * @param dictcodeMap
	 * @throws Exception
	 */
	private void hand(POISXSSUtil poiUtil, SXSSFWorkbook workbook, 
			String path, Map<String, Object> param,Map<String, Object> dictcodeMap)throws Exception{
		
		PageInfo<ReportReceiptTargetEntity> tmpPageInfo =reportReceiptTargetService.queryAll(param, 0, Integer.MAX_VALUE);
		List<ReportReceiptTargetEntity> list=tmpPageInfo.getList();
		
		logger.info("收款指标报表导出...");
		Sheet sheet = poiUtil.getXSSFSheet(workbook,"超期未收款占比");
		sheet.setColumnWidth(2, 4000);
		sheet.setColumnWidth(3, 4000);
		sheet.setColumnWidth(4, 4000);
		sheet.setColumnWidth(5, 4000);
		sheet.setColumnWidth(6, 4000);
		sheet.setColumnWidth(7, 4000);
		
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
		Cell cell1 = row0.createCell(1);
		cell1.setCellValue("");
		cell1.setCellStyle(style);
		Cell cell2 = row0.createCell(2);
		cell2.setCellValue("未收款");
		cell2.setCellStyle(style);
		Cell cell3 = row0.createCell(5);
		cell3.setCellValue("业绩");
		cell3.setCellStyle(style);
		Cell cell4 = row0.createCell(8);
		cell4.setCellValue("");
		cell4.setCellStyle(style);
		
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 1));
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 2, 4));
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 5, 7));
		
		Row row1 = sheet.createRow(1);
		Cell cell20 = row1.createCell(0);
		cell20.setCellValue("销售");
		cell20.setCellStyle(style);
		Cell cell21 = row1.createCell(1);
		cell21.setCellValue("区域");
		cell21.setCellStyle(style);
		Cell cell22 = row1.createCell(2);
		cell22.setCellValue("2个月以上");
		cell22.setCellStyle(style);
		Cell cell23 = row1.createCell(3);
		cell23.setCellValue("1-2个月");
		cell23.setCellStyle(style);
		Cell cell24 = row1.createCell(4);
		cell24.setCellValue("1个月");
		cell24.setCellStyle(style);
		Cell cell25 = row1.createCell(5);
		cell25.setCellValue("2个月以上");
		cell25.setCellStyle(style);
		Cell cell26 = row1.createCell(6);
		cell26.setCellValue("1-2个月");
		cell26.setCellStyle(style);
		Cell cell27 = row1.createCell(7);
		cell27.setCellValue("1个月");
		cell27.setCellStyle(style);
		Cell cell28 = row1.createCell(8);
		cell28.setCellValue("收款指标");
		cell28.setCellStyle(style);
		
		
		//未收款（2个月以上）
		double unReceipt2=0d;		
		//未收款（1到2个月）
		double unReceipt1To2=0d;		
		//未收款（1个月）
		double unReceipt1=0d;		
		//业绩 （2个月以上）
		double yeji2=0d;	
		//业绩  （1到2个月）
		double yeji1To2=0d;	
		//业绩   （1个月）
		double yeji1=0d;		
		//收款指标
		double receiptTarget=0d;
		
		int RowIndex = 2;
		if(list.size()>0){
			for(int i=0;i<list.size();i++){	
				ReportReceiptTargetEntity entity = list.get(i);
				Row row = sheet.createRow(RowIndex);
				RowIndex++;
				Cell cel0 = row.createCell(0);
				cel0.setCellValue(entity.getSellerName());
				Cell cel1 = row.createCell(1);
				cel1.setCellValue(entity.getArea());
				Cell cel2 = row.createCell(2);
				cel2.setCellValue(entity.getUnReceipt2()==null?0d:entity.getUnReceipt2().doubleValue());
				Cell cel3 = row.createCell(3);
				cel3.setCellValue(entity.getUnReceipt1To2()==null?0d:entity.getUnReceipt1To2().doubleValue());
				Cell cel4 = row.createCell(4);
				cel4.setCellValue(entity.getUnReceipt1()==null?0d:entity.getUnReceipt1().doubleValue());
				
				Cell cel5 = row.createCell(5);
				cel5.setCellValue(entity.getYeji2()==null?0d:entity.getYeji2().doubleValue());
				Cell cel6 = row.createCell(6);
				cel6.setCellValue(entity.getYeji1To2()==null?0d:entity.getYeji1To2().doubleValue());
				Cell cel7 = row.createCell(7);
				cel7.setCellValue(entity.getYeji1()==null?0d:entity.getYeji1().doubleValue());
				Cell cel8 = row.createCell(8);
				cel8.setCellValue(entity.getReceiptTarget()==null?0d:entity.getReceiptTarget().doubleValue());
				
				//未收款（2个月以上）总计
				unReceipt2+=(entity.getUnReceipt2()==null?0d:entity.getUnReceipt2().doubleValue());
				//未收款（1到2个月）
				unReceipt1To2+=(entity.getUnReceipt1To2()==null?0d:entity.getUnReceipt1To2().doubleValue());
				//未收款（1个月）
				unReceipt1+=(entity.getUnReceipt1()==null?0d:entity.getUnReceipt1().doubleValue());
				//业绩 （2个月以上）
				yeji2+=(entity.getYeji2()==null?0d:entity.getYeji2().doubleValue());
				//业绩 （1到2个月）
				yeji1To2+=(entity.getYeji1To2()==null?0d:entity.getYeji1To2().doubleValue());
				//业绩 （1个月）
				yeji1+=(entity.getYeji1()==null?0d:entity.getYeji1().doubleValue());			
				receiptTarget+=(entity.getReceiptTarget()==null?0d:entity.getReceiptTarget().doubleValue());			
			}
		}
		
		Row lastRow = sheet.createRow(RowIndex);
		Cell cellast1 = lastRow.createCell(1);
		cellast1.setCellValue("合计：");
		Cell cellast2 = lastRow.createCell(2);
		cellast2.setCellValue(getCommaFormat(new BigDecimal(unReceipt2)));
		Cell cellast3 = lastRow.createCell(3);
		cellast3.setCellValue(getCommaFormat(new BigDecimal(unReceipt1To2)));
		Cell cellast4 = lastRow.createCell(4);
		cellast4.setCellValue(getCommaFormat(new BigDecimal(unReceipt1)));
		Cell cellast5 = lastRow.createCell(5);
		cellast5.setCellValue(getCommaFormat(new BigDecimal(yeji2)));
		Cell cellast6 = lastRow.createCell(6);
		cellast6.setCellValue(getCommaFormat(new BigDecimal(yeji1To2)));
		Cell cellast7 = lastRow.createCell(7);
		cellast7.setCellValue(getCommaFormat(new BigDecimal(yeji1)));
		Cell cellast8 = lastRow.createCell(8);
		cellast8.setCellValue(getCommaFormat(new BigDecimal(receiptTarget)));
	}
	
	private String getPath(){
		SystemCodeEntity systemCodeEntity = systemCodeService.getSystemCode("GLOABL_PARAM", "EXPORT_RECEIPT_TARGET");
		if(systemCodeEntity == null){
			throw new BizException("请在系统参数中配置文件上传路径,参数GLOABL_PARAM,EXPORT_RECEIPT_TARGET");
		}
		return systemCodeEntity.getExtattr1();
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
