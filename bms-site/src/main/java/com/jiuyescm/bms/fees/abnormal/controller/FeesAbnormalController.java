/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.fees.abnormal.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Controller;

import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.data.provider.Page;
import com.bstek.dorado.uploader.DownloadFile;
import com.bstek.dorado.uploader.UploadFile;
import com.bstek.dorado.uploader.annotation.FileProvider;
import com.bstek.dorado.uploader.annotation.FileResolver;
import com.bstek.dorado.web.DoradoContext;
import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.base.dictionary.entity.SystemCodeEntity;
import com.jiuyescm.bms.base.dictionary.service.ISystemCodeService;
import com.jiuyescm.bms.common.constants.FileConstant;
import com.jiuyescm.bms.common.entity.ErrorMessageVo;
import com.jiuyescm.bms.common.tool.Session;
import com.jiuyescm.bms.common.web.EnumControlPR;
import com.jiuyescm.bms.fees.abnormal.entity.FeesAbnormalEntity;
import com.jiuyescm.bms.fees.abnormal.service.IFeesAbnormalNewService;
import com.jiuyescm.cfm.common.JAppContext;
import com.jiuyescm.common.ConstantInterface;
import com.jiuyescm.common.utils.excel.POISXSSUtil;
import com.jiuyescm.exception.BizException;

/**
 * 
 * @author zhangzw
 * 
 */
@Controller("feesAbnormalController")
public class FeesAbnormalController {

	private static final Logger logger = Logger.getLogger(FeesAbnormalController.class.getName());
	@Resource
	private IFeesAbnormalNewService feesAbnormalNewService;
	@Resource
	private ISystemCodeService systemCodeService;
	@Resource
	private EnumControlPR enumControlPR;
	

	/**
	 * 应收理赔分页查询
	 * 
	 * @param page
	 * @param param
	 */
	@DataProvider
	public void query(Page<FeesAbnormalEntity> page, Map<String, Object> param) {
		PageInfo<FeesAbnormalEntity> pageInfo = feesAbnormalNewService.query(param, page.getPageNo(), page.getPageSize());
		if (pageInfo != null) {
			page.setEntities(pageInfo.getList());
			page.setEntityCount((int) pageInfo.getTotal());
		}
	}
	
	/**
	 * 应付理赔分页查询
	 * 
	 * @param page
	 * @param param
	 */
	@DataProvider
	public void queryPay(Page<FeesAbnormalEntity> page, Map<String, Object> param) {	
		PageInfo<FeesAbnormalEntity> pageInfo = feesAbnormalNewService.queryPay(param, page.getPageNo(), page.getPageSize());
		if (pageInfo != null) {
			page.setEntities(pageInfo.getList());
			page.setEntityCount((int) pageInfo.getTotal());
		}
	}
	
	
	/**
	 * 应付理赔分页查询
	 * 
	 * @param page
	 * @param param
	 */
	@DataProvider
	public void queryCount(Page<FeesAbnormalEntity> page, Map<String, Object> param) {	
		PageInfo<FeesAbnormalEntity> pageInfo = feesAbnormalNewService.queryCount(param, page.getPageNo(), page.getPageSize());
		if (pageInfo != null) {
			page.setEntities(pageInfo.getList());
			page.setEntityCount((int) pageInfo.getTotal());
		}
	}
	
	/**
	 * 审核合同
	 * @param 
	 * @return
	 */
	@DataResolver
	public String update(FeesAbnormalEntity p){
		if(Session.isMissing()){
			return "长时间未操作，用户已失效，请重新登录再试！";
		}	
		try {
			p.setCloseTime(JAppContext.currentTimestamp());
			feesAbnormalNewService.updateOne(p);
			return "sucess";
			
		} catch (Exception e) {
			//写入日志
			logger.info("数据库更新失败"+e.getMessage());
			return "fail";
		}
	}
	
	/**
	 * 导入模板下载
	 * @throws IOException
	 */
	@FileProvider
	public DownloadFile downloadTemplate(Map<String, String> parameter) throws IOException {
		InputStream is = DoradoContext.getCurrent().getServletContext().getResourceAsStream("/WEB-INF/templates/fees_abnormal_template.xlsx");
		return new DownloadFile("确认模板下载.xlsx", is);
	}
	
	
	/**
	 * 模板导入
	 * @param file
	 * @param parameter
	 * @return
	 */
	@FileResolver
	public Map<String, Object> importExcel(UploadFile file, Map<String, Object> parameter){
		DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 10);
		// 校验信息（报错提示）
		List<ErrorMessageVo> infoList = new ArrayList<ErrorMessageVo>();
		ErrorMessageVo errorVo = null;
		Map<String, Object> map = new HashMap<String, Object>();
		
		XSSFWorkbook xssfWorkbook = null;
		try {
			xssfWorkbook = new XSSFWorkbook(file.getInputStream());
		} catch (IOException e) {
			DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 999);
			errorVo = new ErrorMessageVo();
			errorVo.setMsg("Excel读取失败!");
			infoList.add(errorVo);
			map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
			return map;
		}
		XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(0);
		
		if(xssfSheet==null)
			return null;
		
		int cols = xssfSheet.getRow(0).getPhysicalNumberOfCells();
		
		if(cols>6){
			DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 999);
			errorVo = new ErrorMessageVo();
			errorVo.setMsg("Excel导入格式错误请参考标准模板检查!");
			infoList.add(errorVo);
			map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
			return map;
		}
		
		DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 100);
		
		Timestamp nowdate = JAppContext.currentTimestamp();
		
		List<FeesAbnormalEntity> infoLists = new ArrayList<FeesAbnormalEntity>();
		
		Map<String,Integer> checkMap=new HashMap<>();

        for (int rowNum = 1;  rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
        	
        	FeesAbnormalEntity  entity=new  FeesAbnormalEntity();
        	int lieshu = rowNum + 1;
			XSSFRow xssfRow = xssfSheet.getRow(rowNum);
			if(xssfRow==null){
				setMessage(infoList, rowNum+1,"第"+lieshu+"列空行！");
				map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
				continue;
			}
		
			String confirmYear = getCellValue(xssfRow.getCell(0));
			String confirmMonth= getCellValue(xssfRow.getCell(1));
			String deliverName=getCellValue(xssfRow.getCell(2));
			String expressnum=getCellValue(xssfRow.getCell(3));
			String confirmIsDeliveryFree=getCellValue(xssfRow.getCell(4));

			// 确认年份（必填）
			if(StringUtils.isEmpty(confirmYear)) {			
				setMessage(infoList, rowNum+1,"第"+lieshu+"列确认年份不能为空！");
				map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
				continue;
			}
			// 仓库名称（必填）
			if(StringUtils.isEmpty(confirmMonth)) {			
				setMessage(infoList, rowNum+1,"第"+lieshu+"列确认月份不能为空！");
				map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
				continue;
			}
			// 宅配商（必填）
			if(StringUtils.isEmpty(deliverName)) {			
				setMessage(infoList, rowNum+1,"第"+lieshu+"列商家不能为空！");
				map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
				continue;
			}
			// 运单号（必填）
			if(StringUtils.isEmpty(expressnum)) {			
				setMessage(infoList, rowNum+1,"第"+lieshu+"列运单号不能为空！");
				map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
				continue;
			}
			
			// 确认是否免运费（必填）
			if(StringUtils.isEmpty(confirmIsDeliveryFree)) {			
				setMessage(infoList, rowNum+1,"第"+lieshu+"列确认是否免运费不能为空！");
				map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
				continue;
			}
			
			Double confirmPayAmount=0d;
			// 确认理赔金额（必填）
			if(xssfRow.getCell(5)==null) {			
				setMessage(infoList, rowNum+1,"第"+lieshu+"列确认理赔金额不能为空！");
				map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
				continue;
			}else{
				try {
					confirmPayAmount=xssfRow.getCell(5).getNumericCellValue();;
				} catch (Exception e) {
					// TODO: handle exception
					setMessage(infoList, rowNum+1,"第"+lieshu+"列确认理赔金额不能为非数字！");
					map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
					continue;
				}
			}
			
			//Excel重复性校验
			if(checkMap.containsKey(deliverName+"&"+expressnum)){				
				setMessage(infoList, rowNum+1,"第"+lieshu+"行数据和第"+checkMap.get(deliverName+"&"+expressnum)+"行数据重复");
				map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
				checkMap.put(deliverName+"&"+expressnum, lieshu);
				continue;
			}else{
				checkMap.put(deliverName+"&"+expressnum, lieshu);
			}
			
			//数据表重复校验
			Map<String,Object> condition=new HashMap<String,Object>();
			condition.put("deliverName", deliverName);
			condition.put("expressnum", expressnum);
			
			try {
				String startDateStr = confirmYear + "-" + confirmMonth + "-01 00:00:00";
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date startDate;	
				startDate = sdf.parse(startDateStr);		
				Date endDate = DateUtils.addMonths(startDate, 1);
				condition.put("startDate", startDate);
				condition.put("endDate", endDate);		
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			FeesAbnormalEntity data=feesAbnormalNewService.queryOne(condition);
			if(data!=null){
				entity.setId(data.getId());
			}else{
				setMessage(infoList, rowNum+1,"第"+lieshu+"列宅配商:"+deliverName+",运单号:"+expressnum+"不存在");
				map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
				continue;
			}
			
			if("3".equals(data.getOrderStatus())){
				setMessage(infoList, rowNum+1,"第"+lieshu+"列宅配商:"+deliverName+",运单号:"+expressnum+"已关账，无法导入确认后的金额");
				map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
				continue;
			}
			
			//状态改为已确认
			entity.setOrderStatus("2");
			entity.setConfirmYear(confirmYear);
			entity.setConfirmMonth(confirmMonth);
			entity.setConfirmIsDeliveryFree(confirmIsDeliveryFree);
			entity.setConfirmPayAmount(confirmPayAmount);
			// 导入确认时间
			entity.setImportConfirmTime(nowdate);
			infoLists.add(entity);
		}
 
        //如果有错误信息
        if(map.get(ConstantInterface.ImportExcelStatus.IMP_ERROR)!=null){
        	return map;
        }
         
        DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 800);
        
        int num = 0;
        String message = null;
        try {
			num = feesAbnormalNewService.updateList(infoLists);
		} catch (Exception e) {
		     message = e.getMessage();
		     logger.error("更新失败", e);
		}
        if(num==0){
        	DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 999);
			errorVo = new ErrorMessageVo();
			if(message!=null){
				errorVo.setMsg(message);
			}else{
				errorVo.setMsg("更新失败!");
				errorVo.setLineNo(2);
			}
			infoList.add(errorVo);
			map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
			
			return map;
        }else{
        	DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 1000);
			map.put(ConstantInterface.ImportExcelStatus.IMP_SUCC, "0");
			return map;
        }
	}
	
	/**
	 * 应收理赔导出
	 * @param param
	 * @return
	 * @throws Exception
	 */
	@FileProvider
	public DownloadFile export(Map<String,Object> param) throws Exception{
		long beginTime = System.currentTimeMillis();
    	logger.info("====应收理赔导出：写入Excel begin.");
    	
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
			String fileName = "应收理赔" + FileConstant.SUFFIX_XLSX;
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
	    		    	
	    	logger.info("====应收理赔：写入Excel end.==总耗时：" + (System.currentTimeMillis() - beginTime));

	    	InputStream is = new FileInputStream(filePath);
	    	return new DownloadFile(fileName, is);
		} catch (Exception e) {
			//bmsErrorLogInfoService.
			logger.error("应收理赔导出失败", e);
		}
		return null;
	}
	
	/**
	 * 应收理赔导出
	 * @param poiUtil
	 * @param workbook
	 * @param path
	 * @param param
	 * @param dictcodeMap
	 * @throws Exception
	 */
	private void hand(POISXSSUtil poiUtil, SXSSFWorkbook workbook, 
			String path, Map<String, Object> param,Map<String, Object> dictcodeMap)throws Exception{
			
		if(param==null){
			param=new HashMap<String, Object>();
		}	
		
		PageInfo<FeesAbnormalEntity> tmpPageInfo =feesAbnormalNewService.query(param, 0, Integer.MAX_VALUE);
		List<FeesAbnormalEntity> list=tmpPageInfo.getList();
		
		logger.info("应收理赔导出...");
		Sheet sheet = poiUtil.getXSSFSheet(workbook,"应收理赔导出");
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
		cell0.setCellValue("商家");
		cell0.setCellStyle(style);
		Cell cell1 = row0.createCell(1);
		cell1.setCellValue("仓库");
		cell1.setCellStyle(style);
		Cell cell2 = row0.createCell(2);
		cell2.setCellValue("承运商名称");
		cell2.setCellStyle(style);
		Cell cell3 = row0.createCell(3);
		cell3.setCellValue("宅配商名称");
		cell3.setCellStyle(style);
		Cell cell4 = row0.createCell(4);
		cell4.setCellValue("出库单号");
		cell4.setCellStyle(style);
		Cell cell5 = row0.createCell(5);
		cell5.setCellValue("运单号");
		cell5.setCellStyle(style);
		Cell cell6 = row0.createCell(6);
		cell6.setCellValue("外部订单号");
		cell6.setCellStyle(style);
		Cell cell7 = row0.createCell(7);
		cell7.setCellValue("责任方");
		cell7.setCellStyle(style);
		Cell cell8 = row0.createCell(8);
		cell8.setCellValue("赔付类型");
		cell8.setCellStyle(style);		
		Cell cell9 = row0.createCell(9);
		cell9.setCellValue("备注");
		cell9.setCellStyle(style);		
		Cell cell10 = row0.createCell(10);
		cell10.setCellValue("理赔商品金额");
		cell10.setCellStyle(style);
		Cell cell11 = row0.createCell(11);
		cell11.setCellValue("是否免运费");
		cell11.setCellStyle(style);
		Cell cell12 = row0.createCell(12);
		cell12.setCellValue("改地址退件费");
		cell12.setCellStyle(style);
		Cell cell13 = row0.createCell(13);
		cell13.setCellValue("创建人");
		cell13.setCellStyle(style);
		Cell cell14 = row0.createCell(14);
		cell14.setCellValue("客诉确认时间");
		cell14.setCellStyle(style);
		Cell cell15 = row0.createCell(15);
		cell15.setCellValue("运单创建时间");
		cell15.setCellStyle(style);
	
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		
		//未收款（2个月以上）
		double totalProductAmount=0d;		
		//未收款（1到2个月）
		double totalReturnedAmount=0d;		
		
		int RowIndex = 1;
		if(list.size()>0){
			for(int i=0;i<list.size();i++){	
				FeesAbnormalEntity fee = list.get(i);
				Row row = sheet.createRow(RowIndex);
				RowIndex++;
				Cell cel0 = row.createCell(0);
				cel0.setCellValue(fee.getCustomerName());
				Cell cel1 = row.createCell(1);
				cel1.setCellValue(fee.getWarehouseName());
				Cell cel2 = row.createCell(2);
				cel2.setCellValue(fee.getCarrierName());
				Cell cel3 = row.createCell(3);
				cel3.setCellValue(fee.getDeliverName());
				Cell cel4 = row.createCell(4);
				cel4.setCellValue(fee.getOutstockNo());			
				Cell cel5 = row.createCell(5);
				cel5.setCellValue(fee.getExpressnum());
				Cell cel6 = row.createCell(6);
				cel6.setCellValue(fee.getReference());
				Cell cel7 = row.createCell(7);
				cel7.setCellValue(fee.getDutyType());
				Cell cel8 = row.createCell(8);
				cel8.setCellValue(fee.getPayType());
				Cell cel9 = row.createCell(9);
				cel9.setCellValue(fee.getRemark());
				Cell cel10 = row.createCell(10);
				cel10.setCellValue(fee.getProductAmountJ2c());
				Cell cel11 = row.createCell(11);
				cel11.setCellValue(fee.getIsDeliveryFreeJ2c());
				Cell cel12 = row.createCell(12);
				cel12.setCellValue(fee.getReturnedAmountC2j());
				Cell cel13 = row.createCell(13);
				cel13.setCellValue(fee.getCreatePersonName());
				
				if(fee.getCloseTime()!=null){
					Cell cel14 = row.createCell(14);
					cel14.setCellValue(sdf.format(fee.getKesuConfirmTime()));
				}
				if(fee.getCreateTime()!=null){
					Cell cel15 = row.createCell(15);
					cel15.setCellValue(sdf.format(fee.getCreateTime()));
				}	
				//理赔商品金额
				totalProductAmount+=(fee.getProductAmountJ2c()==null?0d:fee.getProductAmountJ2c().doubleValue());
				//改地址退件费
				totalReturnedAmount+=(fee.getReturnedAmountC2j()==null?0d:fee.getReturnedAmountC2j().doubleValue());		
			}
		}
		
		Row lastRow = sheet.createRow(RowIndex);
		Cell cellast1 = lastRow.createCell(0);
		cellast1.setCellValue("合计：");
		Cell cellast2 = lastRow.createCell(10);
		cellast2.setCellValue(totalProductAmount);
		Cell cellast3 = lastRow.createCell(12);
		cellast3.setCellValue(totalReturnedAmount);
	}
	
	private String getPath(){
		SystemCodeEntity systemCodeEntity = systemCodeService.getSystemCode("GLOABL_PARAM", "EXPORT_ABNORMAL");
		if(systemCodeEntity == null){
			throw new BizException("请在系统参数中配置文件上传路径,参数GLOABL_PARAM,EXPORT_ABNORMAL");
		}
		return systemCodeEntity.getExtattr1();
	}
	
	/**
	 * 应收理赔导出
	 * @param param
	 * @return
	 * @throws Exception
	 */
	@FileProvider
	public DownloadFile exportPay(Map<String,Object> param) throws Exception{
		long beginTime = System.currentTimeMillis();
    	logger.info("====应付理赔导出：写入Excel begin.");
    	
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
			String fileName = "应付理赔" + FileConstant.SUFFIX_XLSX;
			String filePath = path + FileConstant.SEPARATOR + fileName;
			File file = new File(filePath);
			if (file.exists()) {
				file.delete();
			}
			
			POISXSSUtil poiUtil = new POISXSSUtil();
	    	SXSSFWorkbook workbook = poiUtil.getXSSFWorkbook();
	    		    		    	
	        //导出方法
	    	handPay(poiUtil, workbook, filePath, param,dictcodeMap);
	    	
	    	//最后写到文件
	    	poiUtil.write2FilePath(workbook, filePath);
	    		    	
	    	logger.info("====应付理赔：写入Excel end.==总耗时：" + (System.currentTimeMillis() - beginTime));

	    	InputStream is = new FileInputStream(filePath);
	    	return new DownloadFile(fileName, is);
		} catch (Exception e) {
			//bmsErrorLogInfoService.
			logger.error("应付理赔导出失败", e);
		}
		return null;
	}
	
	
	/**
	 * 应付理赔导出
	 * @param poiUtil
	 * @param workbook
	 * @param path
	 * @param param
	 * @param dictcodeMap
	 * @throws Exception
	 */
	private void handPay(POISXSSUtil poiUtil, SXSSFWorkbook workbook, 
			String path, Map<String, Object> param,Map<String, Object> dictcodeMap)throws Exception{
			
		if(param==null){
			param=new HashMap<String, Object>();
		}	
		
		PageInfo<FeesAbnormalEntity> tmpPageInfo =feesAbnormalNewService.query(param, 0, Integer.MAX_VALUE);
		List<FeesAbnormalEntity> list=tmpPageInfo.getList();
		
		logger.info("应付理赔导出...");
		Sheet sheet = poiUtil.getXSSFSheet(workbook,"应付理赔导出");
		Font font = workbook.createFont();
		font.setBoldweight(Font.BOLDWEIGHT_BOLD);

		sheet.setColumnWidth(21, 4000);
		sheet.setColumnWidth(22, 4000);
		sheet.setColumnWidth(23, 4000);
		sheet.setColumnWidth(24, 4000);
		
		CellStyle style = workbook.createCellStyle();
		style.setAlignment(CellStyle.ALIGN_CENTER);
		style.setWrapText(true);
		style.setFont(font);
		
		Row row0 = sheet.createRow(0);	
		Cell cell0 = row0.createCell(0);
		cell0.setCellValue("承运商");
		cell0.setCellStyle(style);
		Cell cell1 = row0.createCell(1);
		cell1.setCellValue("宅配商");	
		cell1.setCellStyle(style);
		Cell cell2 = row0.createCell(2);
		cell2.setCellValue("商家");
		cell2.setCellStyle(style);
		Cell cell3 = row0.createCell(3);
		cell3.setCellValue("仓库");
		cell3.setCellStyle(style);
		Cell cell4 = row0.createCell(4);
		cell4.setCellValue("出库单号");
		cell4.setCellStyle(style);
		Cell cell5 = row0.createCell(5);
		cell5.setCellValue("运单号");
		cell5.setCellStyle(style);
		Cell cell6 = row0.createCell(6);
		cell6.setCellValue("外部订单号");
		cell6.setCellStyle(style);
		Cell cell7 = row0.createCell(7);
		cell7.setCellValue("责任方");
		cell7.setCellStyle(style);
		Cell cell8 = row0.createCell(8);
		cell8.setCellValue("赔付类型");
		cell8.setCellStyle(style);		
		Cell cell9 = row0.createCell(9);
		cell9.setCellValue("备注");
		cell9.setCellStyle(style);		
		Cell cell10 = row0.createCell(10);
		cell10.setCellValue("理赔商品金额");
		cell10.setCellStyle(style);
		Cell cell11 = row0.createCell(11);
		cell11.setCellValue("是否免运费");
		cell11.setCellStyle(style);
		Cell cell12 = row0.createCell(12);
		cell12.setCellValue("改地址退件费");
		cell12.setCellStyle(style);
		Cell cell13 = row0.createCell(13);
		cell13.setCellValue("处罚金额");
		cell13.setCellStyle(style);
		Cell cell14 = row0.createCell(14);
		cell14.setCellValue("理赔小计");
		cell14.setCellStyle(style);
		Cell cell15 = row0.createCell(15);
		cell15.setCellValue("确认理赔金额");
		cell15.setCellStyle(style);
		Cell cell16 = row0.createCell(16);
		cell16.setCellValue("确认是否免运费");
		cell16.setCellStyle(style);
		Cell cell17 = row0.createCell(17);
		cell17.setCellValue("单据状态");
		cell17.setCellStyle(style);
		Cell cell18 = row0.createCell(18);
		cell18.setCellValue("确认年份");
		cell18.setCellStyle(style);
		Cell cell19 = row0.createCell(19);
		cell19.setCellValue("确认月份");
		cell19.setCellStyle(style);		
		Cell cell20 = row0.createCell(20);
		cell20.setCellValue("创建人");
		cell20.setCellStyle(style);
		Cell cell21 = row0.createCell(21);
		cell21.setCellValue("运单创建时间");
		cell21.setCellStyle(style);
		Cell cell22 = row0.createCell(22);
		cell22.setCellValue("客诉确认时间");
		cell22.setCellStyle(style);
		Cell cell23 = row0.createCell(23);
		cell23.setCellValue("导入确认时间");
		cell23.setCellStyle(style);
		Cell cell24 = row0.createCell(24);
		cell24.setCellValue("关账时间");
		cell24.setCellStyle(style);

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		Map<String,String> statusMap=enumControlPR.getOrderStatusList();
		
		//理赔商品金额
		double totalProductAmount=0d;		
		//改地址退件费
		double totalReturnedAmount=0d;		
		//处罚金额
		double totalAmerceAmount=0d;
		//理赔小计
		double totalTotalPay=0d;
		//确认理赔金额
		double totalConfirmPayAmount=0d;
		int RowIndex = 1;
		if(list.size()>0){
			for(int i=0;i<list.size();i++){	
				FeesAbnormalEntity fee = list.get(i);
				Row row = sheet.createRow(RowIndex);
				RowIndex++;
				Cell cel0 = row.createCell(0);
				cel0.setCellValue(fee.getCarrierName());
				Cell cel1 = row.createCell(1);
				cel1.setCellValue(fee.getDeliverName());
				Cell cel2 = row.createCell(2);
				cel2.setCellValue(fee.getCustomerName());
				Cell cel3 = row.createCell(3);
				cel3.setCellValue(fee.getWarehouseName());
				Cell cel4 = row.createCell(4);
				cel4.setCellValue(fee.getOutstockNo());			
				Cell cel5 = row.createCell(5);
				cel5.setCellValue(fee.getExpressnum());
				Cell cel6 = row.createCell(6);
				cel6.setCellValue(fee.getReference());				
				Cell cel7 = row.createCell(7);
				cel7.setCellValue(fee.getDutyType());
				Cell cel8 = row.createCell(8);
				cel8.setCellValue(fee.getPayType());
				Cell cel9 = row.createCell(9);
				cel9.setCellValue(fee.getRemark());
				Cell cel10 = row.createCell(10);
				cel10.setCellValue(fee.getProductAmountD2j()==null?0d:fee.getProductAmountD2j());
				Cell cel11 = row.createCell(11);
				cel11.setCellValue(fee.getIsDeliveryFreeD2j());
				Cell cel12 = row.createCell(12);
				cel12.setCellValue(fee.getReturnedAmountJ2d()==null?0d:fee.getReturnedAmountJ2d());
				Cell cel13 = row.createCell(13);
				cel13.setCellValue(fee.getAmerceAmount()==null?0d:fee.getAmerceAmount());
				Cell cel14 = row.createCell(14);
				cel14.setCellValue(fee.getTotalPay()==null?0d:fee.getTotalPay());
				Cell cel15 = row.createCell(15);
				cel15.setCellValue(fee.getConfirmPayAmount()==null?0d:fee.getConfirmPayAmount());
				Cell cel16 = row.createCell(16);
				cel16.setCellValue(fee.getConfirmIsDeliveryFree());
				Cell cel17 = row.createCell(17);
				cel17.setCellValue(statusMap.get(fee.getOrderStatus()));
				Cell cel18 = row.createCell(18);
				cel18.setCellValue(fee.getConfirmYear());
				Cell cel19 = row.createCell(19);
				cel19.setCellValue(fee.getConfirmMonth());
				Cell cel20 = row.createCell(20);
				cel20.setCellValue(fee.getCreatePersonName());
				
				if(fee.getCreateTime()!=null){
					Cell cel21 = row.createCell(21);
					cel21.setCellValue(sdf.format(fee.getCreateTime()));
				}
				if(fee.getKesuConfirmTime()!=null){
					Cell cel22 = row.createCell(22);
					cel22.setCellValue(sdf.format(fee.getKesuConfirmTime()));
				}
				if(fee.getImportConfirmTime()!=null){
					Cell cel23 = row.createCell(23);
					cel23.setCellValue(sdf.format(fee.getImportConfirmTime()));
				}
				if(fee.getCloseTime()!=null){
					Cell cel24 = row.createCell(24);
					cel24.setCellValue(sdf.format(fee.getCloseTime()));
				}
				
				//理赔商品金额
				totalProductAmount+=(fee.getProductAmountD2j()==null?0d:fee.getProductAmountD2j().doubleValue());
				//改地址退件费
				totalReturnedAmount+=(fee.getReturnedAmountJ2d()==null?0d:fee.getReturnedAmountJ2d().doubleValue());		
				//处罚金额
				totalAmerceAmount+=(fee.getAmerceAmount()==null?0d:fee.getAmerceAmount().doubleValue());		
				//理赔小计
				totalTotalPay+=(fee.getTotalPay()==null?0d:fee.getTotalPay().doubleValue());		
				//确认理赔金额
				totalConfirmPayAmount+=(fee.getConfirmPayAmount()==null?0d:fee.getConfirmPayAmount().doubleValue());	
			}
		}
		
		Row lastRow = sheet.createRow(RowIndex);
		Cell cellast1 = lastRow.createCell(0);
		cellast1.setCellValue("合计：");
		Cell cellast2 = lastRow.createCell(10);
		cellast2.setCellValue(totalProductAmount);
		Cell cellast3 = lastRow.createCell(12);
		cellast3.setCellValue(totalReturnedAmount);
		Cell cellast4 = lastRow.createCell(13);
		cellast4.setCellValue(totalAmerceAmount);
		Cell cellast5 = lastRow.createCell(14);
		cellast5.setCellValue(totalTotalPay);
		Cell cellast6 = lastRow.createCell(15);
		cellast6.setCellValue(totalConfirmPayAmount);
	}
	
	private void setMessage(List<ErrorMessageVo> errorList, int lineNo,String msg) {
		ErrorMessageVo errorVo;
		errorVo =new ErrorMessageVo();
		errorVo.setLineNo(lineNo);
		errorVo.setMsg(msg);
		errorList.add(errorVo);
	}
	
	/**
	 * 获取单元格的值
	 * 
	 * @param cell
	 * @return
	 */
	public String getCellValue(Cell cell) {

		if (cell == null)
			return "";

		if (cell.getCellType() == Cell.CELL_TYPE_STRING) {

			return cell.getStringCellValue();

		} else if (cell.getCellType() == Cell.CELL_TYPE_BOOLEAN) {

			return String.valueOf(cell.getBooleanCellValue());

		} else if (cell.getCellType() == Cell.CELL_TYPE_FORMULA) {

			return cell.getCellFormula();

		} else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
			DecimalFormat df = new DecimalFormat("#.####");
			return String.valueOf(df.format(cell.getNumericCellValue()));

		}
		return "";
	}
}
