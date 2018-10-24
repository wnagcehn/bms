/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.fees.abnormal.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.stereotype.Controller;

import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.data.provider.Page;
import com.bstek.dorado.uploader.DownloadFile;
import com.bstek.dorado.uploader.annotation.FileProvider;
import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.base.dictionary.entity.SystemCodeEntity;
import com.jiuyescm.bms.base.dictionary.service.ISystemCodeService;
import com.jiuyescm.bms.common.constants.FileConstant;
import com.jiuyescm.bms.fees.abnormal.entity.FeesAbnormalEntity;
import com.jiuyescm.bms.fees.abnormal.entity.FeesPayAbnormalEntity;
import com.jiuyescm.bms.fees.abnormal.service.IFeesAbnormalNewService;
import com.jiuyescm.bms.fees.abnormal.service.IFeesAbnormalService;
import com.jiuyescm.bms.fees.abnormal.service.IFeesPayAbnormalService;
import com.jiuyescm.common.utils.excel.POISXSSUtil;
import com.jiuyescm.exception.BizException;
import com.jiuyescm.mdm.customer.api.IProjectService;
import com.jiuyescm.mdm.customer.vo.CusprojectRuleVo;

/**
 * 
 * @author zhangzw
 * 
 */
@Controller("feesAbnormalController")
public class FeesAbnormalController {

	private static final Logger logger = Logger.getLogger(FeesAbnormalController.class.getName());

	@Resource
	private IFeesAbnormalService feesAbnormalService;
	
	@Resource
	private IFeesAbnormalNewService feesAbnormalNewService;
	@Resource
	private IProjectService projectService;
	@Resource
	private IFeesPayAbnormalService feesPayAbnormalService;
	@Resource
	private ISystemCodeService systemCodeService;
	
	@DataProvider
	public FeesAbnormalEntity findById(Long id) throws Exception {
		FeesAbnormalEntity entity = null;
		entity = feesAbnormalService.findById(id);
		return entity;
	}

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
	public void queryPay(Page<FeesPayAbnormalEntity> page, Map<String, Object> param) {
		if(null!=param && "ALL".equals(param.get("reasonId"))){
			param.put("reasonId", "");
		}
		if(null!=param){
			param.put("payment", "1"); 
			if(param.get("projectId")!=null&&StringUtils.isNotBlank(param.get("projectId").toString())){
				List<String> customerIds=projectService.queryAllCustomerIdByProjectId(param.get("projectId").toString());
				if(customerIds==null||customerIds.size()==0){
					page.setEntities(null);
					page.setEntityCount(0);
					return;
				}
				param.put("customerIdList", customerIds);
			}
		}	
		PageInfo<FeesPayAbnormalEntity> pageInfo = feesPayAbnormalService.query(param, page.getPageNo(), page.getPageSize());
		if (pageInfo != null) {
			InitPayList(pageInfo.getList());
			page.setEntities(pageInfo.getList());
			page.setEntityCount((int) pageInfo.getTotal());
		}
	}
	
	private void InitPayList(List<FeesPayAbnormalEntity> list){
		List<CusprojectRuleVo> cusProjectRuleList=projectService.queryAllRule();
		for(FeesPayAbnormalEntity entity:list){
			if(entity.getCustomerId()!=null){
				for(CusprojectRuleVo vo:cusProjectRuleList){
					if(entity.getCustomerId().equals(vo.getCustomerid())){
						entity.setProjectId(vo.getProjectid());
						entity.setProjectName(vo.getProjectName());
						break;
					}
				}
			}
		}
	}
	
	private void InitList(List<FeesAbnormalEntity> list){
		List<CusprojectRuleVo> cusProjectRuleList=projectService.queryAllRule();
		for(FeesAbnormalEntity entity:list){
			if(entity.getCustomerId()!=null){
				for(CusprojectRuleVo vo:cusProjectRuleList){
					if(entity.getCustomerId().equals(vo.getCustomerid())){
						entity.setProjectId(vo.getProjectid());
						entity.setProjectName(vo.getProjectName());
						break;
					}
				}
			}
		}
	}
	@DataProvider
	public void queryReceive(Page<FeesAbnormalEntity> page, Map<String, Object> param) {
		if(param!=null){
			if("ALL".equals(param.get("reasonId"))){
				param.put("reasonId", "");
			}
			param.put("receive", "0"); 
		}		
		PageInfo<FeesAbnormalEntity> pageInfo = feesAbnormalService.query(param, page.getPageNo(), page.getPageSize());
		if (pageInfo != null) {
			InitList(pageInfo.getList());
			page.setEntities(pageInfo.getList());
			page.setEntityCount((int) pageInfo.getTotal());
		}
	}

	@DataResolver
	public void save(FeesAbnormalEntity entity) {
		if (entity.getId() == null) {
			feesAbnormalService.save(entity);
		} else {
			feesAbnormalService.update(entity);
		}
	}

	@DataResolver
	public void delete(FeesAbnormalEntity entity) {
		feesAbnormalService.delete(entity.getId());
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
	
		
		//未收款（2个月以上）
		double totalProductAmount=0d;		
		//未收款（1到2个月）
		double totalReturnedAmount=0d;		
		
		int RowIndex = 2;
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
				Cell cel14 = row.createCell(14);
				cel14.setCellValue(fee.getKesuConfirmTime());
				Cell cel15 = row.createCell(15);
				cel15.setCellValue(fee.getCreateTime());
				
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
}
