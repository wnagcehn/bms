/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.biz.storage.web;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.omg.CORBA.OBJ_ADAPTER;
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
import com.jiuyescm.bms.base.file.entity.FileExportTaskEntity;
import com.jiuyescm.bms.base.file.service.IFileExportTaskService;
import com.jiuyescm.bms.base.system.BaseController;
import com.jiuyescm.bms.biz.storage.entity.BizPackStorageEntity;
import com.jiuyescm.bms.biz.storage.entity.ReturnData;
import com.jiuyescm.bms.biz.storage.service.IBizPackStorageService;
import com.jiuyescm.bms.common.constants.ExceptionConstant;
import com.jiuyescm.bms.common.constants.FileConstant;
import com.jiuyescm.bms.common.constants.MessageConstant;
import com.jiuyescm.bms.common.entity.ErrorMessageVo;
import com.jiuyescm.bms.common.enumtype.CalculateState;
import com.jiuyescm.bms.common.enumtype.FileTaskStateEnum;
import com.jiuyescm.bms.common.enumtype.FileTaskTypeEnum;
import com.jiuyescm.bms.common.log.entity.BmsErrorLogInfoEntity;
import com.jiuyescm.bms.common.log.service.IBmsErrorLogInfoService;
import com.jiuyescm.bms.common.sequence.service.SequenceService;
import com.jiuyescm.bms.fees.storage.entity.FeesReceiveStorageEntity;
import com.jiuyescm.bms.fees.storage.service.IFeesReceiveStorageService;
import com.jiuyescm.cfm.common.JAppContext;
import com.jiuyescm.cfm.common.sequence.SequenceGenerator;
import com.jiuyescm.common.ConstantInterface;
import com.jiuyescm.common.utils.BeanToMapUtil;
import com.jiuyescm.common.utils.DateUtil;
import com.jiuyescm.common.utils.FileOperationUtil;
import com.jiuyescm.common.utils.excel.FileReaderFactory;
import com.jiuyescm.common.utils.excel.IFileReader;
import com.jiuyescm.common.utils.excel.POISXSSUtil;
import com.jiuyescm.common.utils.upload.BaseDataType;
import com.jiuyescm.common.utils.upload.DataProperty;
import com.jiuyescm.common.utils.upload.PackStorageTemplateDataType;
import com.jiuyescm.mdm.customer.api.ICustomerService;
import com.jiuyescm.mdm.customer.vo.CustomerVo;
import com.jiuyescm.mdm.warehouse.api.IWarehouseService;
import com.jiuyescm.mdm.warehouse.vo.WarehouseVo;

/**
 * 耗材库存
 * @author stevenl
 * 
 */
@Controller("bizPackStorageController")
public class BizPackStorageController extends BaseController{

	private static final Logger logger = Logger.getLogger(BizPackStorageController.class.getName());

	@Resource private IBizPackStorageService bizPackStorageService;
	@Resource private IFeesReceiveStorageService feesReceiveStorageService;
	@Resource private SequenceService sequenceService;
	@Autowired private IWarehouseService warehouseService;
	@Resource private ISystemCodeService systemCodeService; //业务类型
	@Autowired private ICustomerService customerService;
	@Resource
	private IFileExportTaskService fileExportTaskService;
	
	@Resource
	private IBmsErrorLogInfoService bmsErrorLogInfoService;

	@DataProvider
	public BizPackStorageEntity findById(Long id) throws Exception {
		BizPackStorageEntity entity = null;
		entity = bizPackStorageService.findById(id);
		return entity;
	}

	/**
	 * 分页查询
	 * 
	 * @param page
	 * @param param
	 */
	@DataProvider
	public void query(Page<BizPackStorageEntity> page, Map<String, Object> param) {
		if(null!=param){
			param.put("delFlag", "0");
		}
		PageInfo<BizPackStorageEntity> pageInfo = bizPackStorageService.query(param, page.getPageNo(), page.getPageSize());
		if (pageInfo != null) {
			page.setEntities(pageInfo.getList());
			page.setEntityCount((int) pageInfo.getTotal());
		}
	}

	@DataResolver
	public void save(BizPackStorageEntity entity) {
		String username = JAppContext.currentUserName();
		if (entity.getId() == null) {
			entity.setCreator(username);
			bizPackStorageService.save(entity);
		} else {
			entity.setLastModifier(username);
			bizPackStorageService.update(entity);
		}
	}

	@DataResolver
	public void delete(BizPackStorageEntity entity) {
		entity.setLastModifier(JAppContext.currentUserName());
		entity.setDelFlag("1");
		try {
			bizPackStorageService.delete(entity);
		} catch (Exception e) {
			logger.error("删除异常", e);
		}
	}
	
	@DataResolver
	public int deleteBatch(List<BizPackStorageEntity> dataList) {
		List<BizPackStorageEntity> data = new ArrayList<BizPackStorageEntity>();
		PageInfo<FeesReceiveStorageEntity> pageInfo = null;
		FeesReceiveStorageEntity feesReceiveStorageEntity = null;
		
		for(BizPackStorageEntity entity:dataList){
			String feesNo = entity.getFeesNo();
			if(StringUtils.isNotBlank(feesNo))
			{
				Map<String,Object> param = new  HashMap<String,Object>();
				param.put("feesNo", feesNo);
			    pageInfo = feesReceiveStorageService.query(param, 0, Integer.MAX_VALUE);
				if (null != pageInfo && null!=pageInfo.getList() && pageInfo.getList().size()>0) 
				{
					feesReceiveStorageEntity=pageInfo.getList().get(0);
					//获取此时的费用状态
					String status = String.valueOf(feesReceiveStorageEntity.getStatus());
					if("1".equals(status)){
						data.add(entity);
					}
				}
			}
		}
		
		dataList.removeAll(data);
		
		int i = bizPackStorageService.deleteBatch(dataList);
		
		if(i>0){
			for(BizPackStorageEntity entity:dataList)
			{
				if(StringUtils.isNotEmpty(entity.getFeesNo()))
					feesReceiveStorageService.deleteEntity(entity.getFeesNo());
			}
		}
		
		return i;
	}
	
	@DataProvider
	public int queryDelete(Page<BizPackStorageEntity> page,Map<String, Object> param) {
		List<BizPackStorageEntity> list = bizPackStorageService.queryList(param);
		
		bizPackStorageService.deleteFees(param);
		
		return bizPackStorageService.deleteBatch(list);
	}
	
	@DataResolver
	public @ResponseBody  Object update(BizPackStorageEntity entity){
		
		ReturnData result = new ReturnData();
		Timestamp nowdate = JAppContext.currentTimestamp();
		String userid=JAppContext.currentUserName();
		
		//判断是否生成费用，判断费用的状态是否为未过账
		String feesNo = entity.getFeesNo();
		
		PageInfo<FeesReceiveStorageEntity> pageInfo = null;
		FeesReceiveStorageEntity feesReceiveStorageEntity = null;
		
		if(StringUtils.isNotBlank(feesNo)){
			Map<String,Object> param = new  HashMap<String,Object>();
			param.put("feesNo", feesNo);
		    pageInfo = feesReceiveStorageService.query(param, 0, Integer.MAX_VALUE);
			if (null != pageInfo && null!=pageInfo.getList() && pageInfo.getList().size()>0) {
				feesReceiveStorageEntity=pageInfo.getList().get(0);
				//获取此时的费用状态
				String status = String.valueOf(feesReceiveStorageEntity.getStatus());
				if(status.equals("1")){
					result.setCode("fail");
					result.setData("该费用已过账，无法调整托数");
					return result;
				}
			}
		}
			
		//此为修改业务数据
		//根据名字查出对应的id
		entity.setLastModifier(userid);
		entity.setLastModifyTime(nowdate);
		
		
		int i = 0;
		entity.setIsCalculated("0");
		i = bizPackStorageService.update(entity);
		
		if(i>0){
			result.setCode("SUCCESS");
		}else{
			result.setCode("fail");
			result.setData("更新失败");
		}
		
		return result;
	}
	
	
	/**
	 * 导入模板下载
	 * @param parameter
	 * @return
	 * @throws IOException
	 */
	@FileProvider
	public DownloadFile accquireTemplate(Map<String, String> parameter) throws IOException {
		InputStream is = DoradoContext.getCurrent().getServletContext().getResourceAsStream("/WEB-INF/templates/storage/pack_storage_template.xlsx");
		return new DownloadFile("耗材库存导入模板.xlsx", is);
	}
	
	/**
	 * 导入数据
	 * @param file
	 * @param parameter
	 * @return
	 * @throws Exception
	 */
	@FileResolver
	public Map<String, Object> importPackStorage(UploadFile file, Map<String, Object> parameter) throws Exception {
		
		DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 10);
		Map<String, Object> map = new HashMap<String, Object>();
		// 校验信息（报错提示）
		List<ErrorMessageVo> infoList = new ArrayList<ErrorMessageVo>();
		ErrorMessageVo errorVo = null;
		// 导入成功返回模板信息
		List<BizPackStorageEntity> dataList = new ArrayList<BizPackStorageEntity>();
		// 当期时间
		Timestamp currentTime = JAppContext.currentTimestamp();
		String userName = JAppContext.currentUserName();
		try {
			BaseDataType bs = new PackStorageTemplateDataType();
			// 检查导入模板是否正确
			boolean isTemplate = FileOperationUtil.checkExcelTitle(file, bs);
			if (!isTemplate) {
				DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 999);
				errorVo = new ErrorMessageVo();
				errorVo.setMsg("Excel导入格式错误请参考标准模板检查!");
				infoList.add(errorVo);
				map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
				return map;
			}
			DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 100);
			
			// 解析Excel
			dataList = readExcel(infoList,file,bs);
			if (null == dataList || dataList.size() <= 0) {
				DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 999);
				errorVo = new ErrorMessageVo();
				errorVo.setMsg("导入的Excel数据为空或者数据格式不对!");
				infoList.add(errorVo);
				map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
				return map;
			}
			DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 300);
			
			String currentNo = SequenceGenerator.uuidOf36String("t");// 当前操作ID
			
			// 模板信息必填项校验
			map = impExcelCheckInfoProduct(infoList, dataList, map, currentNo);
			if (map.get(ConstantInterface.ImportExcelStatus.IMP_ERROR) != null) { // 有基本的错误信息(必填，数据类型不正确)
				DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 999);
				return map;
			}
			DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 700);
			
			// 获得初步校验后和转换后的数据
			dataList = (List<BizPackStorageEntity>) map.get(ConstantInterface.ImportExcelStatus.IMP_SUCC);
			DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 750);
			//设置属性
			String lineNo = null;
			for(int i=0;i<dataList.size();i++){
				BizPackStorageEntity data  =dataList.get(i);
				String dataNum = sequenceService.getBillNoOne(BizPackStorageEntity.class.getName(), "BMPS", "0000000000");
				data.setDataNum(dataNum);
				data.setDelFlag("0");// 设置为未作废
				data.setCreator(userName);
				data.setCreateTime(currentTime);
				data.setLastModifier(userName);
				data.setLastModifyTime(currentTime);
			}
			DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 800);
			
			//插入正式表
			int insertNum = 0;
			int dupNum = 0;
			try {
				insertNum = bizPackStorageService.saveList(dataList);
			} catch (Exception e) {
				if((e.getMessage().indexOf("Duplicate entry"))>0){
					dupNum = 1;
				}
				logger.error(e.getMessage());
				//写入日志
				BmsErrorLogInfoEntity bmsErrorLogInfoEntity=new BmsErrorLogInfoEntity(this.getClass().getSimpleName(),Thread.currentThread().getStackTrace()[1].getMethodName(), "", e.toString());
				bmsErrorLogInfoService.log(bmsErrorLogInfoEntity);
			}
					
			DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 950);
			
			if (insertNum <= 0) {
				DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 999);
				errorVo = new ErrorMessageVo();
				if(dupNum>0){
					errorVo.setMsg("违反唯一性校验,重复的数据不会导入");
				}else{
					errorVo.setMsg("耗材存储写入失败!");
				}
				
				infoList.add(errorVo);
				map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
				return map;
			}else{
				DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 1000);
				map.put(ConstantInterface.ImportExcelStatus.IMP_SUCC, "0");
				return map;
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("导入耗材库存异常", e);
			DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 999);
			//写入日志
			BmsErrorLogInfoEntity bmsErrorLogInfoEntity=new BmsErrorLogInfoEntity(this.getClass().getSimpleName(),Thread.currentThread().getStackTrace()[1].getMethodName(), "导入耗材库存异常", e.toString());
			bmsErrorLogInfoService.log(bmsErrorLogInfoEntity);
		} 
		/*
		finally{
			PropertiesUtil proUtil = new PropertiesUtil();
			String path = proUtil.getImpExcelFilePath();
			File storeFolder = new File(path);
			// 如果存放上传文件的目录不存在就新建
			if (!storeFolder.isDirectory()) {
				storeFolder.mkdirs();
			}
			File destFile = FileOperationUtil.getDestFile(file.getFileName(), storeFolder);
			// 将文件放到相应目录
			file.transferTo(destFile);
		}
		 */
		return map;
	}
	
	/**
	 * 读取Excel中的数据（不包含列名）
	 * @param file
	 * @return
	 */
	private List<BizPackStorageEntity> readExcel(List<ErrorMessageVo> infoList, UploadFile file,BaseDataType bs) {
		
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
			List<BizPackStorageEntity> dataList = Lists.newArrayList();
			for (Map<String, String> data : datas) {
				BizPackStorageEntity p = (BizPackStorageEntity) BeanToMapUtil.convertMapNull(BizPackStorageEntity.class, data);
				dataList.add(p);
			}
			return dataList;
		} catch (Exception e) {
			setMessage(infoList, 0, e.getMessage());
			//写入日志
			BmsErrorLogInfoEntity bmsErrorLogInfoEntity=new BmsErrorLogInfoEntity(this.getClass().getSimpleName(),Thread.currentThread().getStackTrace()[1].getMethodName(), "", e.toString());
			bmsErrorLogInfoService.log(bmsErrorLogInfoEntity);
		}
		return null;
	}
		
	
	/**
	 * 校验Excel里面的内容合法性
	 * @param infoList
	 * @param list
	 * @param map
	 * @param objList
	 * @param currentNo
	 * @return
	 */
	private Map<String, Object> impExcelCheckInfoProduct(List<ErrorMessageVo> infoList, List<BizPackStorageEntity> dataList, Map<String, Object> map, String currentNo) {
		int lineNo = 0;
		Map<String, String> wareHouseMap = new HashMap<String, String>();
		Map<String, String> temperatureMap = new HashMap<String, String>();
		Map<String, String> customerMap = new HashMap<String, String>();
		//当导入数据量达到多少时,先一次加载再判断;否则数据量小的时候每次检索校验.
		//仓库
		List<WarehouseVo> wareHouseList = warehouseService.queryAllWarehouse();
		if(wareHouseList != null && wareHouseList.size()>0){
			for(WarehouseVo wareHouse : wareHouseList){
				wareHouseMap.put(wareHouse.getWarehousename(), wareHouse.getWarehouseid());
			}
		}
		//温度类型
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("typeCode", "TEMPERATURE_TYPE");
		List<SystemCodeEntity> temperatureList = systemCodeService.queryCodeList(param);
		if(temperatureList != null && temperatureList.size()>0){
		    for(SystemCodeEntity scEntity : temperatureList){
		    	temperatureMap.put(scEntity.getCodeName(), scEntity.getCode());
		    }
		}
		//商家
		PageInfo<CustomerVo> tmpPageInfo = customerService.query(null, 0, Integer.MAX_VALUE);
		if (tmpPageInfo != null && tmpPageInfo.getList() != null && tmpPageInfo.getList().size()>0) {
			for(CustomerVo customer : tmpPageInfo.getList()){
				if(customer != null){
					customerMap.put(customer.getCustomername(), customer.getCustomerid());
				}
			}
		}
		for (int i = 0; i < dataList.size(); i++) {
			BizPackStorageEntity bpps = dataList.get(i);
			if (null == bpps) {
				continue;
			}
			bpps.setAdjustPalletNum(bpps.getPalletNum());
			lineNo = lineNo+1;
			try{
				Timestamp stockTimeInput = bpps.getCurTime();
				if(null != stockTimeInput) {
					bpps.setCurTime(stockTimeInput);
				}else {
					setMessage(infoList, lineNo,"库存日期不能为空!");
				}
			}catch(Exception e){
				setMessage(infoList, lineNo,"输入的库存日期格式不符合(yyyy-MM-dd)!");
				//写入日志
				BmsErrorLogInfoEntity bmsErrorLogInfoEntity=new BmsErrorLogInfoEntity(this.getClass().getSimpleName(),Thread.currentThread().getStackTrace()[1].getMethodName(), "输入的库存日期格式不符合(yyyy-MM-dd)!", e.toString());
				bmsErrorLogInfoService.log(bmsErrorLogInfoEntity);
			}
			
			//判断仓库id是否在仓库表中维护 并将此仓库返回,将id回填
			String warehouseName= bpps.getWarehouseName().trim();
			if(StringUtils.isNotBlank(warehouseName)){
				if(wareHouseMap.containsKey(warehouseName) && StringUtils.isNotBlank(wareHouseMap.get(warehouseName))){
					//从缓存wareHouseMap中直接取
					bpps.setWarehouseName(warehouseName);
					bpps.setWarehouseCode(wareHouseMap.get(warehouseName));
				}else{
					//否则从数据库中取
					setMessage(infoList, lineNo,"仓库名["+warehouseName+"]没有在仓库表中维护!");
				}
			}else {
				setMessage(infoList, lineNo,"仓库名称不能为空!");
			}
			
			//商家全称
			if(StringUtils.isNotBlank(bpps.getCustomerName())){
				if(customerMap.containsKey(bpps.getCustomerName()) && StringUtils.isNotBlank(customerMap.get(bpps.getCustomerName()))){
					bpps.setCustomerName(bpps.getCustomerName());
					bpps.setCustomerid(customerMap.get(bpps.getCustomerName()));
				}else{
					setMessage(infoList, lineNo,"商家["+bpps.getCustomerName()+"]没有在表中维护!");
				}
			}else {
				setMessage(infoList, lineNo,"商家不能为空!");
			}
			
			//判断温度类型是否在时效表中维护
			String temperatureTypeName= bpps.getTemperatureTypeName().trim();
			if(StringUtils.isNotBlank(temperatureTypeName)){
				if(temperatureMap.containsKey(temperatureTypeName) && StringUtils.isNotBlank(temperatureMap.get(temperatureTypeName))){
					bpps.setTemperatureTypeName(temperatureTypeName);
					bpps.setTemperatureTypeCode(temperatureMap.get(temperatureTypeName));
				}else{
					setMessage(infoList, lineNo,"温度类型["+temperatureTypeName+"]没有在表中维护!");
				}
			}else {
				setMessage(infoList, lineNo,"温度类型不能为空!");
			}
			//托数
			if(bpps.getPalletNum() == null){
				bpps.setPalletNum(0.0d);
				bpps.setAdjustPalletNum(0.0d);
			}
			if(bpps.getPalletNum() != null && bpps.getPalletNum()<0){
				setMessage(infoList, lineNo,"托数不能小于0!");
			}
			
			//数量
			if(bpps.getQty() == null){
				bpps.setQty(0.0d);
			}
			if(bpps.getQty() != null && bpps.getQty()<0){
				setMessage(infoList, lineNo,"数量不能小于0!");
			}
			
			//校验数据库中是否存在
			int num = bizPackStorageService.checkIsNotExist(dataList.get(i));
			if (num > 0) {
				setMessage(infoList, lineNo,"数据库中已存在，请勿重复导入!");
			}
			
			bpps.setIsCalculated(String.valueOf(ConstantInterface.Calculate.CALCULATE_NO));
			
			if (infoList != null && infoList.size() > 0) { // 有错误信息
				map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
			} else {
				map.put(ConstantInterface.ImportExcelStatus.IMP_SUCC, dataList); // 无基本错误信息
			}
			DecimalFormat decimalFormat=new DecimalFormat("0");
			double addNum = ((double)(i+1)/dataList.size())*400;
			DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 300+Integer.valueOf(decimalFormat.format(addNum)));
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
	
	@DataProvider
	public void queryGroup(Page<BizPackStorageEntity> page, Map<String, Object> param) {
		if(null!=param){
			param.put("delFlag", "0");
		}
		PageInfo<BizPackStorageEntity> pageInfo = bizPackStorageService.queryGroup(param, page.getPageNo(), page.getPageSize());
		if (pageInfo != null) {
			page.setEntities(pageInfo.getList());
			page.setEntityCount((int) pageInfo.getTotal());
		}
	}
	
	
	/**
	 * 
	 * @param param
	 * @return Billed-已在账单中存在,不能重算,建议删除账单后重试;Calculated-已计算,是否继续重算;Error-系统错误;OK-可重算
	 */
	@Expose
	public Properties validRetry(Map<String, Object> param) {
		Properties ret = bizPackStorageService.validRetry(param);
		return ret;
	}
	
	@Expose
	public String reCalculate(Map<String, Object> param){
		if(bizPackStorageService.reCalculate(param) == 0){
			return "重算异常";
		}
		return "操作成功! 正在重算...";
	}
	
	@DataProvider
	public void packQueryTj(Page<Map<String,String>> page, Map<String, Object> param){
		PageInfo<Map<String,String>> pageInfo = null;
		if("1".equals(param.get("isCalculatedF"))){
			 pageInfo = bizPackStorageService.queryCustomeridF(param, page.getPageNo(), page.getPageSize());
		}else{
			pageInfo = bizPackStorageService.queryByMonth(param, page.getPageNo(), page.getPageSize());
		}
		
		if (pageInfo != null) {
			page.setEntities(pageInfo.getList());
			page.setEntityCount((int) pageInfo.getTotal());
		}
	}
	
	/**
	 * 导出
	 */
	@DataResolver
	public String asynExport(Map<String, Object> param) {
		if (null == param) {
			return MessageConstant.QUERY_PARAM_NULL_MSG;
		}
		
		String customerId = param.get("customerid").toString();
        try {
        	//校验该费用是否已生成Excel文件
        	Map<String, Object> queryEntity = new HashMap<String, Object>();
        	queryEntity.put("taskType", FileTaskTypeEnum.BIZ_PACK.getCode());
        	queryEntity.put("customerid", customerId);
        	String existDel = fileExportTaskService.isExistDeleteTask(queryEntity);
        	if (StringUtils.isNotEmpty(existDel)) {
        		return existDel;
        	}
        	
        	String path = getBizReceiveExportPath();
        	String filepath=path+ FileConstant.SEPARATOR + 
        			FileTaskTypeEnum.BIZ_PACK.getCode() + customerId + FileConstant.SUFFIX_XLSX;
        	
        	FileExportTaskEntity entity = new FileExportTaskEntity();
        	entity.setCustomerid(customerId);
        	entity.setStartTime(DateUtil.formatTimestamp(param.get("startTime")));
        	entity.setEndTime(DateUtil.formatTimestamp(param.get("endTime")));
        	entity.setTaskName(FileTaskTypeEnum.BIZ_PACK.getDesc() + customerId);
        	entity.setTaskType(FileTaskTypeEnum.BIZ_PACK.getCode());
        	entity.setTaskState(FileTaskStateEnum.BEGIN.getCode());
        	entity.setProgress(0d);
        	entity.setFilePath(filepath);
        	entity.setCreator(JAppContext.currentUserName());
        	entity.setCreateTime(JAppContext.currentTimestamp());
        	entity.setDelFlag(ConstantInterface.DelFlag.NO);
        	entity = fileExportTaskService.save(entity);
        	
        	//生成账单文件
    		final Map<String, Object> condition = param;
    		final String taskId = entity.getTaskId();
    		final String filePath=filepath;
    		new Thread(){
    			public void run() {
    				try {
    					export(condition, taskId,filePath);
    				} catch (Exception e) {
    					fileExportTaskService.updateExportTask(taskId, FileTaskStateEnum.FAIL.getCode(), 0);
    					logger.error(ExceptionConstant.ASYN_REC_DISPATCH_FEE_EXCEL_EX_MSG, e);
    					//写入日志
    					BmsErrorLogInfoEntity bmsErrorLogInfoEntity=new BmsErrorLogInfoEntity(this.getClass().getSimpleName(),Thread.currentThread().getStackTrace()[1].getMethodName(), "", e.toString());
    					bmsErrorLogInfoService.log(bmsErrorLogInfoEntity);
    				}
    			};
    		}.start();
		} catch (Exception e) {
			logger.error(ExceptionConstant.ASYN_BIZ_EXCEL_EX_MSG, e);
			//写入日志
			BmsErrorLogInfoEntity bmsErrorLogInfoEntity=new BmsErrorLogInfoEntity(this.getClass().getSimpleName(),Thread.currentThread().getStackTrace()[1].getMethodName(), "", e.toString());
			bmsErrorLogInfoService.log(bmsErrorLogInfoEntity);
			return ExceptionConstant.ASYN_BIZ_EXCEL_EX_MSG;
		}
		return FileTaskTypeEnum.BIZ_PACK.getDesc() + customerId + MessageConstant.EXPORT_TASK_BIZ_MSG;
	}
	
	/**
	 * 异步导出
	 * @param param
	 * @param taskId
	 * @param file
	 * @throws Exception
	 */
	private void export(Map<String, Object> param,String taskId,String filePath)throws Exception{
		fileExportTaskService.updateExportTask(taskId, FileTaskStateEnum.INPROCESS.getCode(), 10);
		String path = getBizReceiveExportPath();
		long beginTime = System.currentTimeMillis();
		
		logger.info("====应收耗材库存导出：");
		//如果存放上传文件的目录不存在就新建
    	File storeFolder=new File(path);
		if(!storeFolder.isDirectory()){
			storeFolder.mkdirs();
		}
		
    	logger.info("====应收耗材库存导出：写入Excel begin.");
    	fileExportTaskService.updateExportTask(taskId, null, 30);
    	POISXSSUtil poiUtil = new POISXSSUtil();
    	SXSSFWorkbook workbook = poiUtil.getXSSFWorkbook();
        //耗材库存
    	fileExportTaskService.updateExportTask(taskId, null, 70);
    	handBiz(poiUtil, workbook, filePath, param);
    	//最后写到文件
    	fileExportTaskService.updateExportTask(taskId, null, 90);
    	poiUtil.write2FilePath(workbook, filePath);
    	
    	fileExportTaskService.updateExportTask(taskId, FileTaskStateEnum.SUCCESS.getCode(), 100);
    	logger.info("====应收耗材库存导出：写入Excel end.==总耗时：" + (System.currentTimeMillis() - beginTime));
	}
	
	/**
	 * 耗材库存
	 * @param poiUtil
	 * @param workbook
	 * @param path
	 * @param myparam
	 * @throws Exception
	 */
	private void handBiz(POISXSSUtil poiUtil, SXSSFWorkbook workbook, 
			String path, Map<String, Object> myparam)throws Exception{
		if("ALL".equals(myparam.get("isCalculated"))){
			myparam.put("isCalculated", "");
		}
		
		int pageNo = 1;
		int lineNo = 1;
		boolean doLoop = true;
		while (doLoop) {			
			PageInfo<BizPackStorageEntity> pageInfo = 
					bizPackStorageService.query(myparam, pageNo, FileConstant.EXPORTPAGESIZE);
			if (null != pageInfo && pageInfo.getList().size() > 0) {
				if (pageInfo.getList().size() < FileConstant.EXPORTPAGESIZE) {
					doLoop = false;
				}else {
					pageNo += 1; 
				}
			}else {
				doLoop = false;
			}
			
			//头、内容信息
			List<Map<String, Object>> headDetailMapList = getBizHead(); 
			List<Map<String, Object>> dataDetailList = getBizHeadItem(pageInfo.getList());
			
			poiUtil.exportExcel2FilePath(poiUtil, workbook, FileTaskTypeEnum.BIZ_PACK.getDesc(), 
					lineNo, headDetailMapList, dataDetailList);
			if (null != pageInfo && pageInfo.getList().size() > 0) {
				lineNo += pageInfo.getList().size();
			}
		}
	}
	
	/**
	 * 干线运单
	 */
	public List<Map<String, Object>> getBizHead(){
		List<Map<String, Object>> headInfoList = new ArrayList<Map<String,Object>>();
		Map<String, Object> itemMap = new HashMap<String, Object>();
		itemMap.put("title", "仓库");
		itemMap.put("columnWidth", 25);
		itemMap.put("dataKey", "warehouseName");
		headInfoList.add(itemMap);
		
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "费用编号");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "feesNo");
        headInfoList.add(itemMap);

		itemMap = new HashMap<String, Object>();
		itemMap.put("title", "数据编号");
		itemMap.put("columnWidth", 25);
		itemMap.put("dataKey", "dataNum");
		headInfoList.add(itemMap);
		
		itemMap = new HashMap<String, Object>();
		itemMap.put("title", "耗材编号");
		itemMap.put("columnWidth", 25);
		itemMap.put("dataKey", "packNo");
		headInfoList.add(itemMap);
		
		itemMap = new HashMap<String, Object>();
		itemMap.put("title", "SKU名称");
		itemMap.put("columnWidth", 25);
		itemMap.put("dataKey", "packName");
		headInfoList.add(itemMap);
        
		itemMap = new HashMap<String, Object>();
		itemMap.put("title", "数量");
		itemMap.put("columnWidth", 50);
		itemMap.put("dataKey", "qty");
		headInfoList.add(itemMap);
		
		itemMap = new HashMap<String, Object>();
		itemMap.put("title", "商家");
		itemMap.put("columnWidth", 25);
		itemMap.put("dataKey", "customerName");
		headInfoList.add(itemMap);
		
		itemMap = new HashMap<String, Object>();
		itemMap.put("title", "温度类型");
		itemMap.put("columnWidth", 25);
		itemMap.put("dataKey", "temperatureTypeName");
		headInfoList.add(itemMap);
		
		itemMap = new HashMap<String, Object>();
		itemMap.put("title", "托数");
		itemMap.put("columnWidth", 25);
		itemMap.put("dataKey", "palletNum");
		headInfoList.add(itemMap);
		
		itemMap = new HashMap<String, Object>();
		itemMap.put("title", "状态");
		itemMap.put("columnWidth", 25);
		itemMap.put("dataKey", "isCalculated");
		headInfoList.add(itemMap);
		
		itemMap = new HashMap<String, Object>();
        itemMap.put("title", "库存日期");
        itemMap.put("columnWidth", 50);
        itemMap.put("dataKey", "curTime");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "创建者");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "creator");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "费用计算时间");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "calculateTime");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "备注");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "remark");
        headInfoList.add(itemMap);
        
        return headInfoList;
	}
	
	private List<Map<String, Object>> getBizHeadItem(List<BizPackStorageEntity> list){
		 List<Map<String, Object>> dataList = new ArrayList<Map<String,Object>>();
	        Map<String, Object> dataItem = null;
	        Map<String, String> calculateMap = CalculateState.getMap();
	        for (BizPackStorageEntity entity : list) {
	        	dataItem = new HashMap<String, Object>();
	        	dataItem.put("warehouseName", entity.getWarehouseName());
	        	dataItem.put("feesNo", entity.getFeesNo());
	        	dataItem.put("dataNum", entity.getDataNum());
	        	dataItem.put("packNo", entity.getPackNo());
	        	dataItem.put("packName", entity.getPackName());
	        	dataItem.put("qty", entity.getQty());
	        	dataItem.put("customerName", entity.getCustomerName());
	        	dataItem.put("temperatureTypeName", entity.getTemperatureTypeName());
	        	dataItem.put("palletNum", entity.getPalletNum());
	        	dataItem.put("isCalculated", calculateMap.get(entity.getIsCalculated()));
	        	dataItem.put("curTime", entity.getCurTime());
	        	dataItem.put("creator", entity.getCreator());
	        	dataItem.put("calculateTime", entity.getCalculateTime());
	        	dataItem.put("remark", entity.getRemark());
	        	dataList.add(dataItem);
			}
	        
		return dataList;
	}
}
