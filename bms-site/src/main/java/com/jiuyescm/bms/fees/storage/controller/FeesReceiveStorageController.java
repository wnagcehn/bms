/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.fees.storage.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.annotation.Expose;
import com.bstek.dorado.data.provider.Page;
import com.bstek.dorado.uploader.DownloadFile;
import com.bstek.dorado.uploader.UploadFile;
import com.bstek.dorado.uploader.annotation.FileProvider;
import com.bstek.dorado.uploader.annotation.FileResolver;
import com.bstek.dorado.web.DoradoContext;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jiuyescm.bms.base.dictionary.entity.SystemCodeEntity;
import com.jiuyescm.bms.base.dictionary.service.ISystemCodeService;
import com.jiuyescm.bms.common.entity.ErrorMessageVo;
import com.jiuyescm.bms.common.log.entity.BmsErrorLogInfoEntity;
import com.jiuyescm.bms.common.log.service.IBmsErrorLogInfoService;
import com.jiuyescm.bms.common.sequence.service.SequenceService;
import com.jiuyescm.bms.common.web.SelectFromTablePR;
import com.jiuyescm.bms.fees.storage.entity.FeesReceiveStorageEntity;
import com.jiuyescm.bms.fees.storage.service.IFeesReceiveStorageService;
import com.jiuyescm.cfm.common.JAppContext;
import com.jiuyescm.cfm.common.sequence.SequenceGenerator;
import com.jiuyescm.common.ConstantInterface;
import com.jiuyescm.common.utils.BeanToMapUtil;
import com.jiuyescm.common.utils.FileOperationUtil;
import com.jiuyescm.common.utils.excel.FileReaderFactory;
import com.jiuyescm.common.utils.excel.IFileReader;
import com.jiuyescm.common.utils.excel.POIUtil;
import com.jiuyescm.common.utils.upload.BaseDataType;
import com.jiuyescm.common.utils.upload.DataProperty;
import com.jiuyescm.common.utils.upload.StorageFeeTemplateDataType;
import com.jiuyescm.exception.BizException;
import com.jiuyescm.mdm.customer.api.ICustomerService;
import com.jiuyescm.mdm.customer.api.IProjectService;
import com.jiuyescm.mdm.customer.vo.CusprojectRuleVo;
import com.jiuyescm.mdm.customer.vo.CustomerVo;
import com.jiuyescm.mdm.warehouse.api.IWarehouseService;
import com.jiuyescm.mdm.warehouse.vo.WarehouseVo;

/**
 * 
 * @author stevenl
 * 
 */
@Controller("feesReceiveStorageController")
public class FeesReceiveStorageController {

	private static final Logger logger = Logger.getLogger(FeesReceiveStorageController.class.getName());

	@Resource
	private IFeesReceiveStorageService feesReceiveStorageService;
	@Resource
	private SequenceService sequenceService;
	
	@Resource
	private ISystemCodeService systemCodeService;
	@Autowired
	private IWarehouseService warehouseService;
	
	@Autowired 
	private ICustomerService customerService;

	@Resource
	private IProjectService projectService;
	
	@Resource
	private IBmsErrorLogInfoService bmsErrorLogInfoService;
	
	@Resource
	private SelectFromTablePR selectFromTablePR;


	@DataProvider
	public FeesReceiveStorageEntity findById(Long id) throws Exception {
		FeesReceiveStorageEntity entity = null;
		entity = feesReceiveStorageService.findById(id);
		return entity;
	}
	/**
	 * jira BMS-187 wuliangfeng
	 * @param param
	 * @return
	 */
	@DataProvider
	public List<Map<String,Object>> queryGroup(Map<String, Object> param){
		if(null!=param&&param.get("status")!=null)
		{
			if("全部".equals(param.get("status"))){
				param.put("status", "");
			}
		}
		if(null!=param && param.get("projectId")!=null&&StringUtils.isNotBlank(param.get("projectId").toString())){
			List<String> customerIds=projectService.queryAllCustomerIdByProjectId(param.get("projectId").toString());
			if(customerIds==null||customerIds.size()==0){
				return new ArrayList<Map<String,Object>>();
			}else{
				param.put("customerIdList", customerIds);
			}
		}
		List<Map<String,Object>> list=feesReceiveStorageService.queryGroup(param);
		List<SystemCodeEntity> baseSubjectCodeList=systemCodeService.findEnumList("STORAGE_PRICE_SUBJECT");
		List<SystemCodeEntity> otherSubjectCodeList=systemCodeService.findEnumList("wh_value_add_subject");
		for(Map<String,Object> map:list){
			String subjectCode="";
			String otherSubjectCode="";
			String subjectName="";
			if(map.get("subjectCode")!=null){
				subjectCode=map.get("subjectCode").toString();
			}
			if(map.get("otherSubjectCode")!=null){
				otherSubjectCode=map.get("otherSubjectCode").toString();
			}
			if(subjectCode.equals("wh_value_add_subject")){
				if(StringUtils.isBlank(otherSubjectCode)){
					map.put("subjectName", "");
				}else{
					for(SystemCodeEntity codeEntity:otherSubjectCodeList){
						if(codeEntity.getCode().equals(otherSubjectCode)){
							subjectName=codeEntity.getCodeName();
							map.put("subjectName","增值费-"+subjectName);
							break;
						}
					}
					if(StringUtils.isBlank(subjectName)){
						map.put("subjectName","增值费-"+otherSubjectCodeList);
					}
				}
				
			}else{
				if(StringUtils.isBlank(subjectCode)){
					map.put("subjectName", "");
				}else{
					for(SystemCodeEntity codeEntity:baseSubjectCodeList){
						if(codeEntity.getCode().equals(subjectCode)){
							subjectName=codeEntity.getCodeName();
							map.put("subjectName", subjectName);
							break;
						}
					}
					if(StringUtils.isBlank(subjectName)){
						map.put("subjectName",subjectCode);
					}
				}
			}
		}
		return list;
	}
	/**
	 * 分页查询
	 * 
	 * @param page
	 * @param param
	 */
	@DataProvider
	public void query(Page<FeesReceiveStorageEntity> page, Map<String, Object> param) {
		if(null!=param&&param.get("status")!=null)
		{
			if("全部".equals(param.get("status"))){
				param.put("status", "");
			}
		}
		if(null!=param && param.get("projectId")!=null&&StringUtils.isNotBlank(param.get("projectId").toString())){
			List<String> customerIds=projectService.queryAllCustomerIdByProjectId(param.get("projectId").toString());
			if(customerIds==null||customerIds.size()==0){
				page.setEntities(null);
				page.setEntityCount(0);
				return;
			}else{
				param.put("customerIdList", customerIds);
			}
		}
		PageInfo<FeesReceiveStorageEntity> pageInfo = feesReceiveStorageService.query(param, page.getPageNo(), page.getPageSize());
		if (pageInfo != null) {
			InitList(pageInfo.getList());
			page.setEntities(pageInfo.getList());
			page.setEntityCount((int) pageInfo.getTotal());
			
		}
	}
	private void InitList(List<FeesReceiveStorageEntity> list){
		List<CusprojectRuleVo> cusProjectRuleList=projectService.queryAllRule();
		for(FeesReceiveStorageEntity entity:list){
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
	@DataResolver
	public void saveZengzhi(FeesReceiveStorageEntity entity) {
		Map<String, Object> param =new HashMap<String,Object>();
		param.put("customerId", entity.getCustomerId());
		param.put("subjectCode", entity.getOtherSubjectCode());
		String unitPrice = feesReceiveStorageService.getUnitPrice(param);
		if (StringUtils.isEmpty(unitPrice)) {
			entity.setUnitPrice(null);
			entity.setCost(null);
			throw new BizException("未能获取到"+entity.getCustomerName()+"  此费用科目的单价");
		}
		entity.setUnitPrice(Double.valueOf(unitPrice));
		if (entity.getFeesNo() == null) {
			entity.setCreator(JAppContext.currentUserID());
			entity.setCreateTime(new Timestamp(new Date().getTime()));
			String feesNo = sequenceService.getBillNoOne(FeesReceiveStorageEntity.class.getName(), "ST", "0000000000");
			WarehouseVo warehouseVo = warehouseService.queryWarehouseByWarehouseid(entity.getWarehouseCode());
			entity.setWarehouseName(warehouseVo.getWarehousename());
			entity.setFeesNo(feesNo);
			entity.setCostType("FEE_TYPE_ADD");
			entity.setSubjectCode("wh_value_add_subject");
			entity.setStatus("0");
//			entity.setCost(BigDecimal.valueOf(entity.getQuantity() * entity.getUnitPrice()));
			entity.setCreateTime(new Timestamp(System.currentTimeMillis()));
			entity.setSubjectCode(entity.getOtherSubjectCode());
			feesReceiveStorageService.save(entity);
		}
	}

	@DataResolver
	public void delete(FeesReceiveStorageEntity entity) {
		// feesReceiveStorageService.delete(entity.getId());
	}

	@FileProvider
	public DownloadFile export(Map<String, Object> parameter) throws Exception {
		if (parameter == null || parameter.isEmpty()) {
			parameter = new HashMap<String, Object>();
		}
		SystemCodeEntity systemCodeEntity = systemCodeService.getSystemCode("GLOABL_PARAM", "EXPORT_PATH_STORAGE_FEES");
		if(systemCodeEntity == null){
			throw new BizException("请在系统参数中配置文件上传路径,参数GLOABL_PARAM,EXPORT_PATH_STORAGE_FEES");
		}
		List<FeesReceiveStorageEntity> feesReceiveStorageEntities = feesReceiveStorageService.queryAll(parameter);
		String path = systemCodeEntity.getExtattr1();
		String tmep = SequenceGenerator.uuidOf36String("exp");
    	File storeFolder=new File(path);
		//如果存放上传文件的目录不存在就新建
		if(!storeFolder.isDirectory()){
			storeFolder.mkdirs();
		}
		try {
			POIUtil poiUtil = new POIUtil();
			HSSFWorkbook hssfWorkbook = poiUtil.getHSSFWorkbook();
			// 所有转码Map
			Map<String, Object> dictcodeMap = new HashMap<String, Object>();
			Map<String, String> statusMap = new HashMap<String, String>();
			statusMap.put("0", "未过账");
			statusMap.put("1", "已过账");
        	dictcodeMap.put("statusMap", statusMap);
        	Map<String, String> costSubjectMap = new HashMap<String, String>();
        	List<SystemCodeEntity> systemCodeList1 = systemCodeService.findEnumList("STORAGE_PRICE_SUBJECT");
    		List<SystemCodeEntity> systemCodeList2 = systemCodeService.findEnumList("STORAGE_ADD_VALUE");
    		if(systemCodeList1!=null && systemCodeList1.size()>0){
    			for(int i=0;i<systemCodeList1.size();i++){
    				costSubjectMap.put(systemCodeList1.get(i).getCode(), systemCodeList1.get(i).getCodeName());
    			}
    		}
    		if(systemCodeList2!=null && systemCodeList2.size()>0){
    			for(int i=0;i<systemCodeList2.size();i++){
    				costSubjectMap.put(systemCodeList2.get(i).getCode(), systemCodeList2.get(i).getCodeName());
    			}
    		}
    		dictcodeMap.put("costSubjectMap", costSubjectMap);
    		
    		List<SystemCodeEntity> systemCodeList3 = systemCodeService.findEnumList("TEMPERATURE_TYPE");
    		Map<String, String> tempretureMap =new HashMap<String,String>();
    		if(systemCodeList3!=null && systemCodeList3.size()>0){
    			for(int i=0;i<systemCodeList3.size();i++){
    				tempretureMap.put(systemCodeList3.get(i).getCode(), systemCodeList3.get(i).getCodeName());
    			}
    		}
    		dictcodeMap.put("tempretureMap", tempretureMap);
    		
			this.appendSheet(poiUtil, hssfWorkbook, "应收费用仓储费", path + "\\BmsStorage_" + tmep + ".xls", feesReceiveStorageEntities, dictcodeMap);
		} catch (Exception e) {
			e.printStackTrace();
			//写入日志
			BmsErrorLogInfoEntity bmsErrorLogInfoEntity=new BmsErrorLogInfoEntity(this.getClass().getSimpleName(),Thread.currentThread().getStackTrace()[1].getMethodName(), "", e.toString());
			bmsErrorLogInfoService.log(bmsErrorLogInfoEntity);
		}

		InputStream is = new FileInputStream(path + "\\BmsStorage_" + tmep + ".xls");
		return new DownloadFile("应收费用仓储费" + tmep + ".xls", is);
	}

	private void appendSheet(POIUtil poiUtil, HSSFWorkbook hssfWorkbook,
			String sheetName, String path, List<FeesReceiveStorageEntity> feesReceiveStorageEntities,Map<String, Object> dictcodeMap) throws IOException {
		List<Map<String, Object>> headInfoList = new ArrayList<Map<String,Object>>();
		Map<String, Object> itemMap = new HashMap<String, Object>();
		
		itemMap.put("title", "费用编号");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "feesNo");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "商家名称");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "customerName");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "仓库名称");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "warehouseName");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "单据类型");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "orderType");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "单据编号");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "orderNo");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "费用类别");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "costType");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "费用科目");
        itemMap.put("columnWidth", 50);
        itemMap.put("dataKey", "costSubject");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "温度类型");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "tempretureType");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "商品类别");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "productType");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "商品编号");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "productNo");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "商品名称");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "productName");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "外部商品编号");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "externalProductNo");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "数量");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "quantity");
        headInfoList.add(itemMap);
		
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "单位");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "unit");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "重量");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "weight");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "体积");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "volume");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "品种数");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "varieties");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "单价");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "unitPrice");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "续件价格");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "continuedPrice");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "金额");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "cost");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "规则编号");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "ruleNo");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "账单编号");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "billNo");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "状态");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "status");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "创建时间");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "createTime");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "修改者");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "lastModifier");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "修改时间");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "lastModifyTime");
        headInfoList.add(itemMap);
        
		
        List<Map<String, Object>> dataList = new ArrayList<Map<String,Object>>();
        Map<String, Object> dataItem = null;
        
        for (FeesReceiveStorageEntity feesReceiveStorageEntity : feesReceiveStorageEntities) {
        	dataItem = new HashMap<String, Object>();
        	dataItem.put("feesNo", feesReceiveStorageEntity.getFeesNo());
        	dataItem.put("customerName", feesReceiveStorageEntity.getCustomerName());
        	dataItem.put("warehouseName", feesReceiveStorageEntity.getWarehouseName());
        	dataItem.put("orderType", feesReceiveStorageEntity.getOrderType());
        	dataItem.put("orderNo", feesReceiveStorageEntity.getOrderNo());
        	dataItem.put("costType", feesReceiveStorageEntity.getCostType());
        	dataItem.put("costSubject", ((Map)dictcodeMap.get("costSubjectMap")).get(feesReceiveStorageEntity.getSubjectCode()));
        	dataItem.put("tempretureType", ((Map)dictcodeMap.get("tempretureMap")).get(feesReceiveStorageEntity.getTempretureType()));
        	dataItem.put("productType", feesReceiveStorageEntity.getProductType());
        	dataItem.put("productNo", feesReceiveStorageEntity.getProductNo());
        	dataItem.put("productName", feesReceiveStorageEntity.getProductName());
        	dataItem.put("externalProductNo", feesReceiveStorageEntity.getExternalProductNo());
        	dataItem.put("quantity", feesReceiveStorageEntity.getQuantity());
        	dataItem.put("unit", feesReceiveStorageEntity.getUnit());
        	dataItem.put("weight", feesReceiveStorageEntity.getWeight());
        	dataItem.put("volume", feesReceiveStorageEntity.getVolume());
        	dataItem.put("varieties", feesReceiveStorageEntity.getVarieties());
        	dataItem.put("unitPrice", feesReceiveStorageEntity.getUnitPrice());
        	dataItem.put("continuedPrice", feesReceiveStorageEntity.getContinuedPrice());
        	dataItem.put("cost", feesReceiveStorageEntity.getCost());
        	dataItem.put("ruleNo", feesReceiveStorageEntity.getRuleNo());
        	dataItem.put("billNo", feesReceiveStorageEntity.getBillNo());
        	dataItem.put("status", ((Map)dictcodeMap.get("statusMap")).get(feesReceiveStorageEntity.getStatus()));
        	dataItem.put("createTime", feesReceiveStorageEntity.getCreateTime());
        	dataItem.put("lastModifier", feesReceiveStorageEntity.getLastModifier());
        	dataItem.put("lastModifyTime", feesReceiveStorageEntity.getLastModifyTime());
        	dataList.add(dataItem);
		}
        
        poiUtil.exportExcelFilePath(poiUtil,hssfWorkbook,sheetName,path, headInfoList, dataList);
	}
	
	@Expose
	public String getunitPrice(Map<String, Object> param){
		return feesReceiveStorageService.getUnitPrice(param);
	}
	
	@DataResolver
	public @ResponseBody  Object  recount(List<FeesReceiveStorageEntity> list)throws Exception{
		
		return feesReceiveStorageService.reCount(list);
		
	}
	
	@FileProvider
	public DownloadFile getDispatchTemplate(Map<String, String> parameter) throws IOException {
		InputStream is = DoradoContext.getCurrent().getServletContext().getResourceAsStream("/WEB-INF/templates/storage/addValue_template.xlsx");
		return new DownloadFile("仓储费用导入模板.xlsx", is);
	}
	
	@FileResolver
	public Map<String, Object> importDispatchTemplate(UploadFile file, Map<String, Object> parameter) throws Exception {
		
		DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 10);
		long starTime=System.currentTimeMillis();	//开始时间
		Map<String, Object> map = new HashMap<String, Object>();
		
		List<ErrorMessageVo> infoList = new ArrayList<ErrorMessageVo>();// 校验信息（报错提示）
		ErrorMessageVo errorVo = null;
				
		// 导入成功返回模板信息
		List<FeesReceiveStorageEntity> templateList = new ArrayList<FeesReceiveStorageEntity>();
		// 当期时间
		Timestamp currentTime = JAppContext.currentTimestamp();
		String userName = JAppContext.currentUserName();
		try {
			BaseDataType bs=new StorageFeeTemplateDataType();
			// 检查导入模板是否正确
			boolean isTemplate = FileOperationUtil.checkExcelTitle(file, bs);
			if (!isTemplate) {
				errorVo = new ErrorMessageVo();
				errorVo.setMsg("Excel导入格式错误请参考标准模板检查!");
				infoList.add(errorVo);
				map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
				return map;
			}
			logger.info("验证列名耗时："+FileOperationUtil.getOperationTime(starTime));
			DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 100);
			starTime=System.currentTimeMillis();	//开始时间
			
			// 解析Excel
			templateList = readExcelProduct(file,bs);
			if (null == templateList || templateList.size() <= 0) {
				errorVo = new ErrorMessageVo();
				errorVo.setMsg("导入的Excel数据为空或者数据格式不对!");
				infoList.add(errorVo);
				map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
				return map;
			}
			logger.info("excel解析耗时："+FileOperationUtil.getOperationTime(starTime));
			DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 600);
			starTime=System.currentTimeMillis();	//开始时间
			
			//支持导入基础费用和增值费用
		    //仓库信息
			List<WarehouseVo>  wareHouselist  = warehouseService.queryAllWarehouse();
			//商家信息
			Map<String, String>  customerMap = getCustomerMap(); 
			
			Map<String,Object> param = new HashMap<String,Object>();
			
			param.put("customerMap", customerMap);
			param.put("wareHouselist", wareHouselist);
			
			
			String currentNo = SequenceGenerator.uuidOf36String("t");// 当前操作ID
			// 模板信息必填项校验
			map = impExcelCheckInfoProduct(infoList, templateList, map, currentNo,param);
			if (map.get(ConstantInterface.ImportExcelStatus.IMP_ERROR) != null) { // 有基本的错误信息(必填，数据类型不正确)
				return map;
			}
			logger.info("必填项验证耗时："+FileOperationUtil.getOperationTime(starTime));
			DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 700);
			starTime=System.currentTimeMillis();	//开始时间
				
			//设置属性
			for(int i=0;i<templateList.size();i++){
				FeesReceiveStorageEntity p=templateList.get(i);
//				p.setDelFlag("0");// 设置为未作废
				String feesNo = sequenceService.getBillNoOne(FeesReceiveStorageEntity.class.getName(), "ST", "0000000000");
				p.setCostType("FEE_TYPE_ADD");
				p.setFeesNo(feesNo);
				p.setCreator(userName);
				p.setCreateTime(p.getOperateTime());
				p.setLastModifier(userName);
				p.setLastModifyTime(currentTime);
			}
			//插入正式表
			int insertNum = feesReceiveStorageService.insertBatchTmp(templateList);
			if (insertNum <= 0) {
				errorVo = new ErrorMessageVo();
				errorVo.setMsg("插入正式表失败!");
				infoList.add(errorVo);
				map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
				return map;
			}else{
				map.put(ConstantInterface.ImportExcelStatus.IMP_SUCC, "0");
				logger.info("写入表耗时："+FileOperationUtil.getOperationTime(starTime));
				DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 1000);
				return map;
			}
		} 
		catch (Exception e) {
			errorVo = new ErrorMessageVo();
			errorVo.setMsg("程序异常--:"+e.getMessage());
			infoList.add(errorVo);
			map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
			//写入日志
			BmsErrorLogInfoEntity bmsErrorLogInfoEntity=new BmsErrorLogInfoEntity(this.getClass().getSimpleName(),Thread.currentThread().getStackTrace()[1].getMethodName(), "", e.toString());
			bmsErrorLogInfoService.log(bmsErrorLogInfoEntity);
		}
		return map;
	}
	
	@Expose
	public int getProgress() {
		Object progressFlag = DoradoContext.getAttachedRequest().getSession().getAttribute("progressFlag");
		if (progressFlag == null){
			DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 0);
			return 1;
		}
		return (int)(DoradoContext.getAttachedRequest().getSession().getAttribute("progressFlag")); 
	}
    
	
	@Expose
	public void setProgress() {
		Object progressFlag = DoradoContext.getAttachedRequest().getSession().getAttribute("progressFlag");
		if (progressFlag == null){
			DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 0);
		 
		} else {
			DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 0);
		} 
	}
	
	@SuppressWarnings("unchecked")
	private Map<String, Object> impExcelCheckInfoProduct(List<ErrorMessageVo> infoList, List<FeesReceiveStorageEntity> prodList, Map<String, Object> map, String currentNo,Map<String,Object> param) {
		List<WarehouseVo>    wareHouselist = (List<WarehouseVo>)param.get("wareHouselist");
		Map<String, String>  customerMap = (Map<String, String>)param.get("customerMap");
		Map<String, Object> subjectListMap=selectFromTablePR.getStorageSubjectList();
		
		int lineNo = 0;
		for (int i = 0; i < prodList.size(); i++) {
			
			FeesReceiveStorageEntity p=prodList.get(i);
			lineNo=lineNo+1;
			
			if(StringUtils.isEmpty(p.getOtherSubjectCode())){
				setMessage(infoList, lineNo,"费用名称为空!");
			}
			
			if(StringUtils.isEmpty(p.getCustomerName())){
				setMessage(infoList, lineNo,"商家为空!");
			}
			
			if(StringUtils.isEmpty(p.getWaybillNo())){
				setMessage(infoList, lineNo,"仓库为空!");
			}
			
			if(null==p.getOperateTime()){
				setMessage(infoList, lineNo,"产生时间为空!");
			}
			
			if(null==p.getCost()){
				setMessage(infoList, lineNo,"金额为空!");
			}
			
			boolean otherSubjectCodeCheck = false;
			//是否是其他费用
			String subjectName=p.getOtherSubjectCode();
			if(StringUtils.isNotBlank(subjectName) && subjectListMap.containsKey(subjectName)){
				SystemCodeEntity entity=(SystemCodeEntity) subjectListMap.get(subjectName);
				if("STORAGE_PRICE_SUBJECT".equals(entity.getTypeCode())){
					//基础费用时直接写入费用明细
					p.setSubjectCode(entity.getCode());
					p.setOtherSubjectCode(entity.getCode());
					otherSubjectCodeCheck = true;
				}else{
					//增值费用
					p.setSubjectCode(entity.getTypeCode());
					p.setOtherSubjectCode(entity.getCode());
					otherSubjectCodeCheck = true;
				}
			}
			
			if(!otherSubjectCodeCheck){
				setMessage(infoList, lineNo,"系统没有维护该费用类型!");
			}
			
			for(WarehouseVo entity:wareHouselist){
				if(entity.getWarehousename().equals(p.getWarehouseName())){
					p.setWarehouseCode(entity.getWarehouseid());
				}
			}
			
			p.setCustomerId(customerMap.get(p.getCustomerName()));
			
			if (infoList != null && infoList.size() > 0) { // 有错误信息
				map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
			} else {
				map.put(ConstantInterface.ImportExcelStatus.IMP_SUCC, prodList); // 无基本错误信息
			}
			
		}
		return map;
	}
	
	private void setMessage(List<ErrorMessageVo> infoList, int lineNo,String msg) {
		ErrorMessageVo errorVo;
		errorVo =new ErrorMessageVo();
		errorVo.setLineNo(lineNo);
		errorVo.setMsg(msg);
		infoList.add(errorVo);
	}
	
	private List<FeesReceiveStorageEntity> readExcelProduct(UploadFile file,BaseDataType bs) {
		String fileSuffix = StringUtils.substringAfterLast(file.getFileName(), ".");
		IFileReader reader = FileReaderFactory.getFileReader(fileSuffix);
		try {
			List<Map<String, String>> list = reader.getFileContent(file.getInputStream());
			List<Map<String, String>> datas = Lists.newArrayList();
			//BaseDataType ddt = new DispatchTemplateDataType();
			List<DataProperty> props = bs.getDataProps();
			for (Map<String, String> map : list) {
				Map<String, String> data = Maps.newHashMap();
				for (DataProperty prop : props) {
					
					data.put(prop.getPropertyId(), map.get(prop.getPropertyName().toLowerCase()));
				}
				datas.add(data);
			}
			List<FeesReceiveStorageEntity> productList = Lists.newArrayList();
			for (Map<String, String> data : datas) {
				FeesReceiveStorageEntity p = (FeesReceiveStorageEntity) BeanToMapUtil.convertMapNull(FeesReceiveStorageEntity.class, data);
				productList.add(p);
			}
			return productList;
		} catch (Exception e) {
			e.printStackTrace();
			//写入日志
			BmsErrorLogInfoEntity bmsErrorLogInfoEntity=new BmsErrorLogInfoEntity(this.getClass().getSimpleName(),Thread.currentThread().getStackTrace()[1].getMethodName(), "", e.toString());
			bmsErrorLogInfoService.log(bmsErrorLogInfoEntity);
		}
		return null;
	}
	
	public Map<String, String> getCustomerMap(){
		Map<String,Object> parameter =new HashMap<String,Object>();
		
		String userid=JAppContext.currentUserID();
		parameter.put("userid", userid);
		PageInfo<CustomerVo> tmpPageInfo = customerService.query(parameter, 0, Integer.MAX_VALUE);
		Map<String, String> map =new LinkedHashMap<String,String>();
		if(tmpPageInfo !=null && tmpPageInfo.getTotal()>0){
			Iterator<CustomerVo> iter = tmpPageInfo.getList().iterator();
			while(iter.hasNext()){
				CustomerVo cvo = (CustomerVo) iter.next();
				map.put(cvo.getCustomername(), cvo.getCustomerid());
			}
		}
		return map;
	}
}
