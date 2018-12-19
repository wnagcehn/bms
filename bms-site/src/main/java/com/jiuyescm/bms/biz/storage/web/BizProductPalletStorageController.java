/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.biz.storage.web;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.log4j.Logger;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bstek.bdf2.core.context.ContextHolder;
import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.annotation.Expose;
import com.bstek.dorado.data.entity.EntityState;
import com.bstek.dorado.data.entity.EntityUtils;
import com.bstek.dorado.data.provider.Page;
import com.bstek.dorado.uploader.DownloadFile;
import com.bstek.dorado.uploader.UploadFile;
import com.bstek.dorado.uploader.annotation.FileProvider;
import com.bstek.dorado.uploader.annotation.FileResolver;
import com.bstek.dorado.web.DoradoContext;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jiuyescm.bms.asyn.service.IBmsFileAsynTaskService;
import com.jiuyescm.bms.asyn.vo.BmsFileAsynTaskVo;
import com.jiuyescm.bms.base.dictionary.entity.SystemCodeEntity;
import com.jiuyescm.bms.base.dictionary.service.ISystemCodeService;
import com.jiuyescm.bms.biz.storage.entity.BizPackStorageEntity;
import com.jiuyescm.bms.biz.storage.entity.BizProductPalletStorageEntity;
import com.jiuyescm.bms.biz.storage.entity.ReturnData;
import com.jiuyescm.bms.biz.storage.service.IBizPackStorageService;
import com.jiuyescm.bms.biz.storage.service.IBizProductPalletStorageService;
import com.jiuyescm.bms.common.entity.CalculateVo;
import com.jiuyescm.bms.common.entity.ErrorMessageVo;
import com.jiuyescm.bms.common.enumtype.mq.BmsPackmaterialTaskTypeNewEnum;
import com.jiuyescm.bms.common.enumtype.status.FileAsynTaskStatusEnum;
import com.jiuyescm.bms.common.enumtype.type.ExeclOperateTypeEnum;
import com.jiuyescm.bms.common.log.entity.BmsErrorLogInfoEntity;
import com.jiuyescm.bms.common.log.service.IBmsErrorLogInfoService;
import com.jiuyescm.bms.common.sequence.service.SequenceService;
import com.jiuyescm.bms.common.tool.Tools;
import com.jiuyescm.bms.fees.storage.entity.FeesReceiveStorageEntity;
import com.jiuyescm.bms.fees.storage.service.IFeesReceiveStorageService;
import com.jiuyescm.bms.file.asyn.BmsFileAsynTaskEntity;
import com.jiuyescm.bms.quotation.contract.entity.PriceContractInfoEntity;
import com.jiuyescm.bms.quotation.contract.service.IPriceContractService;
import com.jiuyescm.bms.quotation.dispatch.service.IPriceDispatchService;
import com.jiuyescm.bs.util.ExportUtil;
import com.jiuyescm.cfm.common.JAppContext;
import com.jiuyescm.cfm.common.sequence.SequenceGenerator;
import com.jiuyescm.common.ConstantInterface;
import com.jiuyescm.common.utils.BeanToMapUtil;
import com.jiuyescm.common.utils.FileOperationUtil;
import com.jiuyescm.common.utils.excel.FileReaderFactory;
import com.jiuyescm.common.utils.excel.IFileReader;
import com.jiuyescm.common.utils.upload.BaseDataType;
import com.jiuyescm.common.utils.upload.DataProperty;
import com.jiuyescm.common.utils.upload.ProductPalletStorageTemplateDataType;
import com.jiuyescm.framework.fastdfs.client.StorageClient;
import com.jiuyescm.framework.fastdfs.model.StorePath;
import com.jiuyescm.framework.lock.Lock;
import com.jiuyescm.framework.lock.LockCallback;
import com.jiuyescm.framework.lock.LockCantObtainException;
import com.jiuyescm.framework.lock.LockInsideExecutedException;
import com.jiuyescm.framework.redis.client.IRedisClient;
import com.jiuyescm.mdm.customer.api.ICustomerService;
import com.jiuyescm.mdm.customer.vo.CustomerVo;
import com.jiuyescm.mdm.warehouse.api.IWarehouseService;
import com.jiuyescm.mdm.warehouse.vo.WarehouseVo;

/**
 * 商品按托存储库存
 * @author wubangjun
 */
@Controller("productPalletStorageController")
public class BizProductPalletStorageController{

	private static final Logger logger = Logger.getLogger(BizProductPalletStorageController.class.getName());

	@Resource
	private IBizProductPalletStorageService bizProductPalletStorageService;
	@Resource 
	private IBizPackStorageService bizPackStorageService;
	
	@Resource 
	private SequenceService sequenceService;
	
	@Resource
	IPriceDispatchService priceDispatchService;
	
	@Resource
	private ISystemCodeService systemCodeService; //业务类型
	
	@Autowired 
	private ICustomerService customerService;
	
	@Autowired
	private IFeesReceiveStorageService feesReceiveStorageService;
	
	@Autowired
	private IWarehouseService warehouseService;
	
	@Resource
	private IPriceContractService contractService;
	
	@Resource
	private IFeesReceiveStorageService feesReceiveService;
	
	@Resource
	private Lock lock;
	
	@Resource
	private IBizPackStorageService packStorageService;
	
	@Resource
	private IBmsErrorLogInfoService bmsErrorLogInfoService;
	@Resource 
	private IRedisClient redisClient;
	
	@Autowired 
	private StorageClient storageClient;
	
	@Resource
	private JmsTemplate jmsQueueTemplate;
	
	@Autowired
	private IBmsFileAsynTaskService bmsFileAsynTaskService;
		
	String sessionId=JAppContext.currentUserID()+"_import_productPalletStorage";
	final String nameSpace="com.jiuyescm.bms.biz.storage.web.BizProductPalletStorageController";

	@DataProvider
	public BizProductPalletStorageEntity findById(Long id) throws Exception {
		BizProductPalletStorageEntity entity = null;
		entity = bizProductPalletStorageService.findById(id);
		return entity;
	}

	/**
	 * 查询
	 * 
	 * @param page
	 * @param param
	 */
	@DataProvider
	public void query(Page<BizProductPalletStorageEntity> page, Map<String, Object> param) {
		if(param!=null && param.containsKey("isCalculated")){
			if(StringUtils.equalsIgnoreCase(String.valueOf(param.get("isCalculated")), "ALL")){
				param.put("isCalculated", "");
			}else{
				param.put("isCalculated", String.valueOf(param.get("isCalculated")));
			}
		}
		PageInfo<BizProductPalletStorageEntity> pageInfo = bizProductPalletStorageService.query(param, page.getPageNo(), page.getPageSize());
		if (pageInfo != null) {
			page.setEntities(pageInfo.getList());
			page.setEntityCount((int) pageInfo.getTotal());
		}
	}

	/**
	 * 查询所有的电仓仓库
	 * @return
	 */
	@DataProvider
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
				map.put(cvo.getCustomerid(), cvo.getCustomername());
			}
		}
		return map;
	}
	
	@DataResolver
	public String save(BizProductPalletStorageEntity entity) throws ParseException {
		if(com.jiuyescm.bms.common.tool.Session.isMissing()){
			return "长时间未操作，用户已失效，请重新登录再试！";
		}else if(entity == null){
			return "页面传递参数有误！";
		}
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
		
		  String[] dataPatterns = new String[] { "yyyy-MM-dd" };
		  Date date = DateUtils.parseDate(sdf.format(entity.getStockTime()), dataPatterns);
		  entity.setStockTime(new Timestamp(date.getTime()));
		if (entity.getId() == null) {
			String dataNum =sequenceService.getBillNoOne(BizProductPalletStorageEntity.class.getName(), "BPPS", "00000");
			entity.setDataNum(dataNum);
			entity.setCreateTime(JAppContext.currentTimestamp());
			entity.setCreator(JAppContext.currentUserName());
			entity.setLastModifier(JAppContext.currentUserName());
			entity.setLastModifyTime(JAppContext.currentTimestamp());
			entity.setDelFlag("0");
			try {
				bizProductPalletStorageService.save(entity);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				if(null!=e.getMessage()&&(e.getMessage().indexOf("Duplicate entry"))>0){
					//写入日志
					BmsErrorLogInfoEntity bmsErrorLogInfoEntity=new BmsErrorLogInfoEntity(this.getClass().getSimpleName(),Thread.currentThread().getStackTrace()[1].getMethodName(), "违反唯一性约束", e.toString());
					bmsErrorLogInfoService.log(bmsErrorLogInfoEntity);
					throw new RuntimeException("违反唯一性约束");
				}
				//写入日志
				BmsErrorLogInfoEntity bmsErrorLogInfoEntity=new BmsErrorLogInfoEntity(this.getClass().getSimpleName(),Thread.currentThread().getStackTrace()[1].getMethodName(), "", e.toString());
				bmsErrorLogInfoService.log(bmsErrorLogInfoEntity);
			}
		} else {
			entity.setLastModifier(JAppContext.currentUserName());
			entity.setLastModifyTime(JAppContext.currentTimestamp());
			try {
				bizProductPalletStorageService.update(entity);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				if(null!=e.getMessage()&&(e.getMessage().indexOf("Duplicate entry"))>0){
					throw new RuntimeException("违反唯一性约束");
				}
				//写入日志
				BmsErrorLogInfoEntity bmsErrorLogInfoEntity=new BmsErrorLogInfoEntity(this.getClass().getSimpleName(),Thread.currentThread().getStackTrace()[1].getMethodName(), "", e.toString());
				bmsErrorLogInfoService.log(bmsErrorLogInfoEntity);
			}
		}
		return null;
	}
	
	@DataResolver
	public String saveAll(Collection<BizProductPalletStorageEntity> datas) throws ParseException {
		if (datas == null) {
			return "没有数据要保存";
		}
		for (BizProductPalletStorageEntity temp : datas) {
			temp.setLastModifier(JAppContext.currentUserName());
			temp.setLastModifyTime(JAppContext.currentTimestamp());
			if (EntityState.NEW.equals(EntityUtils.getState(temp))) {
				this.save(temp);
			} else if (EntityState.MODIFIED.equals(EntityUtils.getState(temp))) {
				//判断是否生成费用，判断费用的状态是否为未过账
				String feeCode = temp.getFeesNo();
				if(StringUtils.isNotBlank(feeCode)){
					Map<String, Object> param = new HashMap<String,Object>();
					param.put("feesNo", feeCode);
					PageInfo<FeesReceiveStorageEntity> pageInfo = feesReceiveStorageService.query(param,  0, Integer.MAX_VALUE);
					if (null != pageInfo && null!=pageInfo.getList() && pageInfo.getList().size()>0) {
						FeesReceiveStorageEntity feesReceiveStorageEntity = pageInfo.getList().get(0);
						//获取此时的费用状态
						String status = String.valueOf(feesReceiveStorageEntity.getStatus());
						if(status.equals("1")){
							return "该费用已过账,无法修改!";
						}
					}
				}
				//如果没有过账的话,就允许调整重量,且调整完后,状态重置为未计算,定时任务重新扫描到并重新生成应收费用.
				temp.setIsCalculated("0");
				try {
					bizProductPalletStorageService.update(temp);
				} catch (Exception e) {
					e.printStackTrace();
					if((e.getMessage().indexOf("Duplicate entry"))>0){
						//写入日志
						BmsErrorLogInfoEntity bmsErrorLogInfoEntity=new BmsErrorLogInfoEntity(this.getClass().getSimpleName(),Thread.currentThread().getStackTrace()[1].getMethodName(), "违反唯一性约束", e.toString());
						bmsErrorLogInfoService.log(bmsErrorLogInfoEntity);
						return "违反唯一性约束";
					}
					//写入日志
					BmsErrorLogInfoEntity bmsErrorLogInfoEntity=new BmsErrorLogInfoEntity(this.getClass().getSimpleName(),Thread.currentThread().getStackTrace()[1].getMethodName(), "", e.toString());
					bmsErrorLogInfoService.log(bmsErrorLogInfoEntity);
				}
			} else if (EntityState.DELETED.equals(EntityUtils.getState(temp))) {
				bizProductPalletStorageService.delete(temp.getId());
			} else {
				// do nothing;
			}
		}
		return "保存成功";
	}

	@DataResolver
	public String delete(BizProductPalletStorageEntity entity) {
		try{
			if(entity != null){
				entity.setLastModifier(JAppContext.currentUserName());
				entity.setLastModifyTime(JAppContext.currentTimestamp());
				entity.setDelFlag("1");
			}
			//判断是否生成费用，判断费用的状态是否为未过账
			String feeCode = entity.getFeesNo();
			if(StringUtils.isNotBlank(feeCode)){
				Map<String, Object> param = new HashMap<String,Object>();
				param.put("feesNo", feeCode);
				PageInfo<FeesReceiveStorageEntity> pageInfo = feesReceiveStorageService.query(param,  0, Integer.MAX_VALUE);
				if (null != pageInfo && null!=pageInfo.getList() && pageInfo.getList().size()>0) {
					FeesReceiveStorageEntity feesReceiveStorageEntity = pageInfo.getList().get(0);
					//获取此时的费用状态
					String status = String.valueOf(feesReceiveStorageEntity.getStatus());
					if(status.equals("1")){
						return "该费用已过账，无法删除";
					}
				}
			}
			bizProductPalletStorageService.update(entity);
			return "SUCCESS";
		}
		catch(Exception ex){
			ex.printStackTrace();
			//写入日志
			BmsErrorLogInfoEntity bmsErrorLogInfoEntity=new BmsErrorLogInfoEntity(this.getClass().getSimpleName(),Thread.currentThread().getStackTrace()[1].getMethodName(), "", ex.toString());
			bmsErrorLogInfoService.log(bmsErrorLogInfoEntity);
			return "数据库操作失败";
		}
	}
	
	
	/**
	 * 导入模板下载
	 * @param parameter
	 * @return
	 * @throws IOException
	 */
	@FileProvider
	public DownloadFile accquireTemplate(Map<String, String> parameter) throws IOException {
		InputStream is = DoradoContext.getCurrent().getServletContext().getResourceAsStream("/WEB-INF/templates/storage/product_pallet_storage_template.xlsx");
		return new DownloadFile("仓库库存导入模板.xlsx", is);
	}
	
	
	/**
	 * 导入数据
	 * @param file
	 * @param parameter
	 * @return
	 * @throws Exception
	 */
	@FileResolver
	public Map<String, Object> importProductPalletStorage(UploadFile file, Map<String, Object> parameter) throws Exception {
		DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 10);
		
		Map<String, Object> map = new HashMap<String, Object>();
		// 校验信息（报错提示）
		List<ErrorMessageVo> infoList = new ArrayList<ErrorMessageVo>();
		ErrorMessageVo errorVo = null;
		
		// 导入成功返回模板信息
		List<BizProductPalletStorageEntity> dataList = new ArrayList<BizProductPalletStorageEntity>();
		// 当期时间
		Timestamp currentTime = JAppContext.currentTimestamp();
		String userName = JAppContext.currentUserName();
		try {
			BaseDataType bs = new ProductPalletStorageTemplateDataType();
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
			dataList = (List<BizProductPalletStorageEntity>) map.get(ConstantInterface.ImportExcelStatus.IMP_SUCC);
			DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 750);
			//设置属性
			String lineNo = null;
			for(int i=0;i<dataList.size();i++){
				BizProductPalletStorageEntity data  =dataList.get(i);
				String dataNum = sequenceService.getBillNoOne(BizProductPalletStorageEntity.class.getName(), "BPPS", "000000000000000");
				data.setDataNum(dataNum);
				data.setDelFlag("0");// 设置为未作废
				data.setCreator(userName);
				data.setCreateTime(data.getStockTime());
				data.setWriteTime(currentTime);
				data.setLastModifier(userName);
				data.setLastModifyTime(currentTime);
			}
			DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 800);
			
			//插入正式表
			int insertNum = 0;
			
			int dupNum = 0;
			
			try {
				insertNum = bizProductPalletStorageService.saveList(dataList);
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
					errorVo.setMsg("存档商品按托库存失败!");
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
			DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 999);
			logger.error(e.getMessage());
			//写入日志
			BmsErrorLogInfoEntity bmsErrorLogInfoEntity=new BmsErrorLogInfoEntity(this.getClass().getSimpleName(),Thread.currentThread().getStackTrace()[1].getMethodName(), "", e.toString());
			bmsErrorLogInfoService.log(bmsErrorLogInfoEntity);
		} 
		return map;
	}
	
	/**
	 * 读取Excel中的数据（不包含列名）
	 * @param file
	 * @return
	 */
	private List<BizProductPalletStorageEntity> readExcel(List<ErrorMessageVo> infoList, UploadFile file,BaseDataType bs) {
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
			List<BizProductPalletStorageEntity> dataList = Lists.newArrayList();
			for (Map<String, String> data : datas) {
				BizProductPalletStorageEntity p = (BizProductPalletStorageEntity) BeanToMapUtil.convertMapNull(BizProductPalletStorageEntity.class, data);
				dataList.add(p);
			}
			return dataList;
		} catch (Exception e) {
			e.printStackTrace();
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
	private Map<String, Object> impExcelCheckInfoProduct(List<ErrorMessageVo> infoList, List<BizProductPalletStorageEntity> dataList, Map<String, Object> map, String currentNo) {
		int lineNo = 0;
		Map<String, String> wareHouseMap = new HashMap<String, String>();
		Map<String, String> temperatureMap = new HashMap<String, String>();
		Map<String, String> customerMap = new HashMap<String, String>();
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
			BizProductPalletStorageEntity bpps = dataList.get(i);
			//将调整托数默认设置为托数
			bpps.setAdjustPalletNum(bpps.getPalletNum());
			
			lineNo = lineNo+1;
			try{
				Timestamp stockTimeInput = bpps.getStockTime();
				//SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd");
				if(stockTimeInput != null) {
					bpps.setStockTime(stockTimeInput);
				}else {
					setMessage(infoList, lineNo,"库存日期不能为空!");
				}
			}catch(Exception e){
				e.printStackTrace();
				setMessage(infoList, lineNo,"输入的库存日期格式不符合(yyyy-MM-dd)!");
				//写入日志
				BmsErrorLogInfoEntity bmsErrorLogInfoEntity=new BmsErrorLogInfoEntity(this.getClass().getSimpleName(),Thread.currentThread().getStackTrace()[1].getMethodName(), "输入的库存日期格式不符合(yyyy-MM-dd)!", e.toString());
				bmsErrorLogInfoService.log(bmsErrorLogInfoEntity);
			}
			
			//判断仓库id是否在仓库表中维护 并将此仓库返回,将id回填
			String warehouseName= bpps.getWarehouseName();
			if(StringUtils.isNotBlank(warehouseName)){
				if(wareHouseMap.containsKey(warehouseName.trim()) && StringUtils.isNotBlank(wareHouseMap.get(warehouseName.trim()))){
					//从缓存wareHouseMap中直接取
					bpps.setWarehouseName(warehouseName);
					bpps.setWarehouseCode(wareHouseMap.get(warehouseName.trim()));
				}else{
					setMessage(infoList, lineNo,"仓库名没有在仓库表中维护!");
				}
			}else {
				setMessage(infoList, lineNo,"仓库名称不能为空!");
			}
			
			//判断温度类型是否在时效表中维护
			String temperatureTypeName= bpps.getTemperatureTypeName();
			if(StringUtils.isNotBlank(temperatureTypeName)){
				if(temperatureMap.containsKey(temperatureTypeName) && StringUtils.isNotBlank(temperatureMap.get(temperatureTypeName))){
					bpps.setTemperatureTypeName(temperatureTypeName);
					bpps.setTemperatureTypeCode(temperatureMap.get(temperatureTypeName));
				}else{
					setMessage(infoList, lineNo,"该温度类型没有在表中维护!");
				}
			}else {
				setMessage(infoList, lineNo,"温度类型不能为空!");
			}
			//托数
			if(null == bpps || null == bpps.getPalletNum()){
				bpps.setPalletNum(0.0d);
				bpps.setAdjustPalletNum(0.0d);
			}
			if(null != bpps && null != bpps.getPalletNum() && bpps.getPalletNum()<0){
				setMessage(infoList, lineNo,"托数不能小于0!");
			}
			//商家
			if(null != bpps && StringUtils.isNotBlank(bpps.getCustomerName())){
				if(customerMap.containsKey(bpps.getCustomerName()) && StringUtils.isNotBlank(customerMap.get(bpps.getCustomerName()))){
					bpps.setCustomerName(bpps.getCustomerName());
					bpps.setCustomerId(customerMap.get(bpps.getCustomerName()));
				}else{
					setMessage(infoList, lineNo,"该商家没有在表中维护!");
				}
			}else {
				setMessage(infoList, lineNo,"商家不能为空!");
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
	
	public void queryCustomer(Page<CustomerVo> page,Map<String,Object> parameter) {
		if(null==parameter){
			parameter=new HashMap<String,Object>();
		}
		String userid=JAppContext.currentUserID();
		parameter.put("userid", userid);
		PageInfo<CustomerVo> tmpPageInfo = customerService.query(parameter, page.getPageNo(), page.getPageSize());
		if (tmpPageInfo != null) {
			page.setEntities(tmpPageInfo.getList());
			page.setEntityCount((int) tmpPageInfo.getTotal());
		}
	}
	/*
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
	*/
	@DataResolver
	public @ResponseBody Object reCount(List<BizProductPalletStorageEntity> list){
	
		ReturnData result = new ReturnData();
		
		List<String>  arr = new  ArrayList<String>();
		
		Map<String,Object> aCondition=new HashMap<>();
		
		CalculateVo calcuVo = null;
		
		for (BizProductPalletStorageEntity entity : list)
		{
			calcuVo = new CalculateVo();
			
			aCondition.put("customerid", entity.getCustomerId());
			aCondition.put("contractTypeCode", "CUSTOMER_CONTRACT");
			List<PriceContractInfoEntity> contractEntity = contractService.queryContract(aCondition);
			if(contractEntity == null || StringUtils.isEmpty(contractEntity.get(0).getContractCode())){
				continue;
			}
			
				calcuVo.setContractCode(contractEntity.get(0).getContractCode());
				calcuVo.setBizTypeCode("STORAGE");
				calcuVo.setSubjectId("wh_product_pallet_storage");
				calcuVo.setObj(entity);
				if(calcuVo.getSuccess()&& calcuVo.getPrice()!=null){
					//仓储费用表
					try {
						FeesReceiveStorageEntity storageFeeEntity = new FeesReceiveStorageEntity();		
						storageFeeEntity.setCreator(JAppContext.currentUserName());
						//费用表的创建时间应为业务表的创建时间
						//2017-08-03:应收费用--仓储费明细中业务类型“商品按托库存”中的[创建时间]取业务表商品按托库存的[库存日期] -- 帮君
						storageFeeEntity.setCreateTime(entity.getStockTime());
						
						storageFeeEntity.setOperateTime(entity.getCreateTime());
						storageFeeEntity.setCustomerId(entity.getCustomerId());		//商家ID
						storageFeeEntity.setCustomerName(entity.getCustomerName());		//商家名称
						storageFeeEntity.setWarehouseCode(entity.getWarehouseCode());	//仓库ID
						storageFeeEntity.setWarehouseName(entity.getWarehouseName());	//仓库名称
						storageFeeEntity.setCostType("FEE_TYPE_GENEARL");			//费用类别 FEE_TYPE_GENEARL-通用 FEE_TYPE_MATERIAL-耗材 FEE_TYPE_ADD-增值
						storageFeeEntity.setSubjectCode(calcuVo.getSubjectId());		//费用科目
						storageFeeEntity.setProductType("");							//商品类型
						storageFeeEntity.setQuantity((new Double(entity.getPalletNum())).intValue());//商品数量
						storageFeeEntity.setStatus("0");			
						storageFeeEntity.setQuantity((new Double(entity.getPalletNum())).intValue());
						storageFeeEntity.setUnit("PALLETS");
						storageFeeEntity.setTempretureType(entity.getTemperatureTypeCode());//设置温度类型  zhangzw
						storageFeeEntity.setCost(calcuVo.getPrice());	//入仓金额
						storageFeeEntity.setRuleNo(calcuVo.getRuleno());
						storageFeeEntity.setUnitPrice(calcuVo.getUnitPrice());//生成单价
						storageFeeEntity.setSubjectCode(calcuVo.getSubjectId());
						
						storageFeeEntity.setBizId(String.valueOf(entity.getId()));//业务数据主键
						
						boolean isInsert = StringUtils.isEmpty(entity.getFeesNo())?true:false; //true-新增  false-更新
						String feesNo =StringUtils.isEmpty(entity.getFeesNo())?
								sequenceService.getBillNoOne(FeesReceiveStorageEntity.class.getName(), "STO", "0000000000")
								:entity.getFeesNo();
						storageFeeEntity.setFeesNo(feesNo);//费用编号
						entity.setFeesNo(feesNo);
						boolean ret = false;
						if(isInsert){
							feesReceiveService.save(storageFeeEntity);
						}
						else{
							feesReceiveStorageService.update(storageFeeEntity);
						}
						entity.setFeesNo(feesNo);
						
						entity.setIsCalculated("1");
						bizProductPalletStorageService.update(entity);
					} catch (Exception e) {
						 arr.add(entity.getFeesNo());
						 logger.error(e.getMessage());
						
					}
				}else{
					arr.add(entity.getFeesNo());
				}
			
		}
		
		if(arr.size()>0){
			result.setCode("fail");
			StringBuffer buf = new StringBuffer();
			for(int i=0;i<arr.size();i++)
			{
				buf.append(arr.get(i));
				if(i!=arr.size()-1){
					buf.append(",");
				}
			}	
			if(arr.size()==list.size()){
				result.setData("全部未更新成功！");
			}else{
				result.setData("未更新成功的出库单号是："+buf.toString());
			}
					
		}else{
			result.setCode("SUCCESS");
		}
		return result;
		
	}
	
	/**
	 * 查询
	 * 
	 * @param page
	 * @param param
	 */
	@DataProvider
	public void queryGroup(Page<BizProductPalletStorageEntity> page, Map<String, Object> param) {
		if(param!=null && param.containsKey("isCalculated")){
			if(StringUtils.equalsIgnoreCase(String.valueOf(param.get("isCalculated")), "ALL")){
				param.put("isCalculated", "");
			}else{
				param.put("isCalculated", String.valueOf(param.get("isCalculated")));
			}
		}
		PageInfo<BizProductPalletStorageEntity> pageInfo = bizProductPalletStorageService.queryGroup(param, page.getPageNo(), page.getPageSize());
		if (pageInfo != null) {
			page.setEntities(pageInfo.getList());
			page.setEntityCount((int) pageInfo.getTotal());
		}
	}
	
	@Expose
	public Properties validRetry(Map<String, Object> param) {
		Properties ret = bizProductPalletStorageService.validRetry(param);
		return ret;
	}
	
	@Expose
	public String reCalculate(Map<String, Object> param){
		if(bizProductPalletStorageService.reCalculate(param) == 0){
			return "重算异常";
		}
		return "操作成功! 正在重算...";
	}
	
	@DataResolver
	public String deleteBatch(List<BizProductPalletStorageEntity> dataList) {
		List<BizProductPalletStorageEntity> data = new ArrayList<BizProductPalletStorageEntity>();
		PageInfo<FeesReceiveStorageEntity> pageInfo = null;
		FeesReceiveStorageEntity feesReceiveStorageEntity = null;
		
		for(BizProductPalletStorageEntity entity:dataList){
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
		

		if(data.size()>0){
			String str="";
			for(int i=0;i<data.size();i++){
				if(i==data.size()-1){
					str+=data.get(i).getFeesNo();
					continue;
				}
				str+=data.get(i).getFeesNo()+",";
			}		
			return str;
		}
		
		dataList.removeAll(data);
		
		int i = bizProductPalletStorageService.deleteList(dataList);
		
		if(i>0)
		{
			for(BizProductPalletStorageEntity entity:dataList)
			{
				if(StringUtils.isNotEmpty(entity.getFeesNo()))
				{
					feesReceiveService.deleteEntity(entity.getFeesNo());
				}
			}
		}
				
		return "sucess";
	}
	
	@FileResolver
	public Map<String, Object> importProductPalletTemplate(final UploadFile file,final Map<String, Object> parameter) throws Exception {
		// 校验信息（报错提示）
		final List<ErrorMessageVo> infoList = new ArrayList<ErrorMessageVo>();

		String userId=ContextHolder.getLoginUserName();
		String lockString=Tools.getMd5(userId + "BMS_QUE_PRODUCT_PACK_STORAGE_IMPORT");
		Map<String, Object> remap = lock.lock(lockString, 300, new LockCallback<Map<String, Object>>() {

			@Override
			public Map<String, Object> handleObtainLock() {
				Map<String, Object> map = Maps.newHashMap();
				try {
				   map = importFileAsyn(file,parameter);
				   return map;
				} catch (Exception e) {
					ErrorMessageVo errorVo = new ErrorMessageVo();
					errorVo.setMsg(e.getMessage());
					infoList.add(errorVo);		
					map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
					return map;
				}
			}

			@Override
			public Map<String, Object> handleNotObtainLock() throws LockCantObtainException {
				Map<String, Object> map = Maps.newHashMap();
				ErrorMessageVo errorVo = new ErrorMessageVo();
				errorVo.setMsg("耗材出库明细导入功能已被其他用户占用，请稍后重试；");
				infoList.add(errorVo);
				map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
				return map;
			}

			@Override
			public Map<String, Object> handleException(LockInsideExecutedException e)
					throws LockInsideExecutedException {
				Map<String, Object> map = Maps.newHashMap();
				ErrorMessageVo errorVo = new ErrorMessageVo();
				errorVo.setMsg("系统异常，请稍后重试!");
				infoList.add(errorVo);
				map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
				return map;
			}
		});
		return remap;
	}
	
	/**
	 * 异步导入处理文件
	 * @param file
	 * @param parameter
	 * @return
	 * @throws IOException
	 */
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
	public Map<String,Object> importFileAsyn(UploadFile file, Map<String, Object> parameter) throws Exception{
		setProgress(0);
		Map<String, Object> map = Maps.newHashMap();
		List<ErrorMessageVo> infoList = new ArrayList<ErrorMessageVo>();
		setProgress(1);
		String extendFileName="";
		if(file.getFileName().contains("xlsx")){
			extendFileName="xlsx";
		}else{
			extendFileName="xls";
		}
		
		String fileName = file.getFileName();
		// 校验文件名称
//		if (!checkRegFileName(fileName)) {
//			setProgress("6");
//			infoList.add(new ErrorMessageVo(1, "Excel文件名称【"+fileName+"】不符合规范,请参考【上海01仓201805耗材导入-1.xlsx】"));
//			map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
//			return map;
//		}
				
		double maxFileSize=getMaxFileSize();
		double importFileSize=BigDecimal.valueOf(file.getSize()).divide(BigDecimal.valueOf(1024*1024)).setScale(2,BigDecimal.ROUND_HALF_DOWN).doubleValue();
		if(importFileSize>maxFileSize){
			infoList.add(new ErrorMessageVo(1, "Excel 导入文件过大,最多能导入"+maxFileSize+"M,请分批次导入"));
			map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
			return map;
		}
		// 保存导入文件到fastDFS，获取文件路径
		StorePath storePath = storageClient.uploadFile(file.getInputStream(), file.getSize(), extendFileName);
		StorePath resultStorePath = storageClient.uploadFile(file.getInputStream(), file.getSize(), extendFileName);
		String fullPath = storePath.getFullPath();
		String resultFullPath = resultStorePath.getFullPath();
		if (StringUtils.isBlank(fullPath) || StringUtils.isBlank(resultFullPath)) {
			setProgress(6);
			infoList.add(new ErrorMessageVo(1, "Excel 导入数据上传文件系统失败，请稍后重试"));
			map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
			return map;
		}
		
		// 生成任务，写入任务表
		String taskId =sequenceService.getBillNoOne(BmsFileAsynTaskEntity.class.getName(), "AT", "0000000000");
		
		BmsFileAsynTaskVo taskEntity = new BmsFileAsynTaskVo();
		taskEntity.setTaskId(taskId);
		taskEntity.setTaskName(fileName.substring(0, fileName.lastIndexOf(".")));
		taskEntity.setTaskRate(0);
		taskEntity.setTaskStatus(FileAsynTaskStatusEnum.WAIT.getCode());
		taskEntity.setTaskType(BmsPackmaterialTaskTypeNewEnum.IMPORT_PACK.getCode());
		taskEntity.setBizType(ExeclOperateTypeEnum.IMPORT.getCode());
		taskEntity.setFileRows(0);
		taskEntity.setOriginFileName(fileName);
		taskEntity.setOriginFilePath(fullPath);
		taskEntity.setResultFileName(fileName);
		taskEntity.setResultFilePath(resultFullPath);
		taskEntity.setCreator(JAppContext.currentUserName());
		taskEntity.setCreatorId(JAppContext.currentUserID());
		taskEntity.setCreateTime(JAppContext.currentTimestamp());
		int saveNum = bmsFileAsynTaskService.save(taskEntity);
		if (saveNum <= 0) {
			setProgress(6);
			infoList.add(new ErrorMessageVo(1, "Excel 导入数据生成任务失败"));
			map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
			return map;
		}
		
		// 写入MQ
		final String msg = taskId;
		jmsQueueTemplate.send(BmsPackmaterialTaskTypeNewEnum.IMPORT_PACK.getCode(), new MessageCreator() {
			@Override
			public Message createMessage(Session session) throws JMSException {
				return session.createTextMessage(msg);
			}
		});
		
		setProgress(5);
		map.put(ConstantInterface.ImportExcelStatus.IMP_SUCC, "操作成功");
		return map;
	}
	
	/**
	 * 校验文件名称
	 * @param fileName
	 * @return
	 */
	private boolean checkRegFileName(String fileName) {
		if (StringUtils.isBlank(fileName)) {
			return false;
		}
		
		// 上海仓201805耗材导入-01.xlsx
		String regEx = "^[a-zA-Z0-9\\u4e00-\\u9fa5]+仓[0-9]{6}耗材导入[-0-9]*(.xls|.xlsx)$";
		Pattern pattern = Pattern.compile(regEx);
		Matcher matcher = pattern.matcher(fileName);
		return matcher.matches();
	}
	
	private double getMaxFileSize(){
		double fileSize=50.0;
		try{
			SystemCodeEntity code=systemCodeService.getSystemCode("GLOABL_PARAM", "IMPORT_FILE_SIZE");
			fileSize=Double.valueOf(code.getExtattr1());
		}catch(Exception e){
			logger.info("未配置系统参数IMPORT_FILE_SIZE");
			System.out.println("未配置系统参数IMPORT_FILE_SIZE");
		}
		return fileSize;
	}
	
	@FileResolver
	public Map<String, Object> updateProductPalletTemplate(final UploadFile file,final Map<String, Object> parameter) throws Exception {
		//String deliver=(String)parameter.get("deliver");
		 Map<String, Object> remap=new HashMap<String, Object>();
		// 校验信息（报错提示）
		final List<ErrorMessageVo> infoList = new ArrayList<ErrorMessageVo>();
		
		String lockString=Tools.getMd5("BMS_QUO_UPDATE_PRODUCT_PALLET_STORAGE"+JAppContext.currentUserID());
		remap=lock.lock(lockString, 300, new LockCallback<Map<String, Object>>() {

			@Override
			public Map<String, Object> handleObtainLock() {
				Map<String, Object> map=new HashMap<String, Object>();
				try {
					map=updateStorageNew(file,parameter);
				} catch (Exception e) {
					e.printStackTrace();
					ErrorMessageVo errorVo = new ErrorMessageVo();
					errorVo.setMsg("系统错误:"+e.getMessage());
					infoList.add(errorVo);
					map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
					//写入日志
					BmsErrorLogInfoEntity bmsErrorLogInfoEntity=new BmsErrorLogInfoEntity(this.getClass().getSimpleName(),Thread.currentThread().getStackTrace()[1].getMethodName(), "", e.toString());
					bmsErrorLogInfoService.log(bmsErrorLogInfoEntity);
				}
				return map;
			}

			@Override
			public Map<String, Object> handleNotObtainLock()
					throws LockCantObtainException {
				Map<String, Object> map=new HashMap<String, Object>();
				ErrorMessageVo errorVo = new ErrorMessageVo();
				errorVo.setMsg("商品按托报价导入功能已被其他用户占用，请稍后重试；");
				infoList.add(errorVo);
				map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
				return map;
			}

			@Override
			public Map<String, Object> handleException(
					LockInsideExecutedException e)
					throws LockInsideExecutedException {
				Map<String, Object> map=new HashMap<String, Object>();
				ErrorMessageVo errorVo = new ErrorMessageVo();
				errorVo.setMsg("系统异常，请稍后重试!");
				infoList.add(errorVo);
				map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
				return map;
			}
			
		});
		return remap;
	}
	
	/**
	 * 批量更新
	 * @param file
	 * @param parameter
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> updateStorageNew(UploadFile file, Map<String, Object> parameter) throws Exception{
		
		DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 10);
		Map<String, Object> map = new HashMap<String, Object>();
		String[] str = {"库存日期","仓库","商家全称","商品冷冻","商品冷藏","商品常温","商品恒温","耗材冷冻","耗材冷藏","耗材常温","耗材恒温"};
		// 校验信息（报错提示）
		List<ErrorMessageVo> infoList = new ArrayList<ErrorMessageVo>();
		ErrorMessageVo errorVo = null;
		
		// 检查导入模板是否正确
		boolean isTemplate = ExportUtil.checkTitle(file,str);
		if (!isTemplate) 
		{
			DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 999);
			errorVo = new ErrorMessageVo();
			errorVo.setMsg("Excel导入格式错误请参考标准模板检查!");
			infoList.add(errorVo);
			map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
			return map;
		}
		
		//start
		Map<String, String> wareHouseMap = new HashMap<String, String>();
		Map<String, String> customerMap = new HashMap<String, String>();
		Map<String, String> temperatureMap = new HashMap<String, String>();
		//仓库
		List<WarehouseVo> wareHouseList = warehouseService.queryAllWarehouse();
		if(wareHouseList != null && wareHouseList.size()>0)
		{
			for(WarehouseVo wareHouse : wareHouseList){
				wareHouseMap.put(wareHouse.getWarehousename().trim(),wareHouse.getWarehouseid());
			}
		}
		//商家
		PageInfo<CustomerVo> tmpPageInfo = customerService.query(null, 0, Integer.MAX_VALUE);
		if (tmpPageInfo != null && tmpPageInfo.getList() != null && tmpPageInfo.getList().size()>0) 
		{
			for(CustomerVo customer : tmpPageInfo.getList()){
				if(customer != null){
					customerMap.put(customer.getCustomername().trim(), customer.getCustomerid());
				}
			}
		}
		//温度类型
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("typeCode", "TEMPERATURE_TYPE");
		List<SystemCodeEntity> temperatureList = systemCodeService.queryCodeList(param);
		if(temperatureList != null && temperatureList.size()>0){
		    for(SystemCodeEntity scEntity : temperatureList){
		    	temperatureMap.put(scEntity.getCodeName().trim(), scEntity.getCode());
		    }
		}
	   //end
		
		XSSFWorkbook xssfWorkbook = new XSSFWorkbook(file.getInputStream());
		XSSFFormulaEvaluator  evaluator = new XSSFFormulaEvaluator(xssfWorkbook); 
		
		XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(0);
		if(xssfSheet==null)
			return null;
		
		// 库存数据
		List<BizProductPalletStorageEntity> palletList = new ArrayList<BizProductPalletStorageEntity>();
		//耗材数据
		List<BizPackStorageEntity> packList = new ArrayList<BizPackStorageEntity>();
		
		Timestamp currentTime = JAppContext.currentTimestamp();
		String userName = JAppContext.currentUserName();
		//插入正式表
		int insertNum = 0;
		int dupNum = 0;
        for (int rowNum = 1;  rowNum <= xssfSheet.getLastRowNum(); rowNum++)
        {
        	String errorMsg="";
			XSSFRow xssfRow = xssfSheet.getRow(rowNum); 
			if(null==xssfRow)
			{
				if(rowNum!=xssfSheet.getLastRowNum()){
					errorMsg+="空行！";
				}else{
					break;
				}
				
			}
				
			String rkrq = ExportUtil.getValue(xssfRow.getCell(0), evaluator);//入库日期
			String warehouseName = ExportUtil.getValue(xssfRow.getCell(1), evaluator);
			String customerName = ExportUtil.getValue(xssfRow.getCell(2), evaluator);
			Timestamp time = null;
			
			if(StringUtils.isEmpty(rkrq))
			{
			    int lieshu = 1;
			    errorMsg+="第"+lieshu+"列入库日期不能为空！";
			}
			
			try {
				  time = Timestamp.valueOf(rkrq);
			  } catch (Exception e){
				  try {
					  String[] dataPatterns = new String[] { "yyyy-MM-dd hh:mm:ss", "yyyy-MM-dd", "dd-MM月 -yy","yyyy/MM/dd","yyyy/MM/dd HH:mm:ss","yyyy年MM月dd日" };
					  Date date = DateUtils.parseDate(rkrq, dataPatterns);
					  time = new Timestamp(date.getTime());
				} catch (Exception e1) {
					e1.printStackTrace();
					int lieshu = 1;
					 errorMsg+="第"+lieshu+"列日期格式不对！";
						//写入日志
						BmsErrorLogInfoEntity bmsErrorLogInfoEntity=new BmsErrorLogInfoEntity(this.getClass().getSimpleName(),Thread.currentThread().getStackTrace()[1].getMethodName(), "第"+lieshu+"列日期格式不对！", e.toString());
						bmsErrorLogInfoService.log(bmsErrorLogInfoEntity);
				}
				 e.printStackTrace();
				//写入日志
				BmsErrorLogInfoEntity bmsErrorLogInfoEntity=new BmsErrorLogInfoEntity(this.getClass().getSimpleName(),Thread.currentThread().getStackTrace()[1].getMethodName(), "", e.toString());
				bmsErrorLogInfoService.log(bmsErrorLogInfoEntity);
			}
			
			if(StringUtils.isEmpty(warehouseName))
			{
			    int lieshu = 2;
			    errorMsg+="第"+lieshu+"列仓库名称为空！";
			}
			
			if(StringUtils.isEmpty(customerName))
			{
			    int lieshu = 3;
			    errorMsg+="第"+lieshu+"列商家名称为空！";
		    }
			
			warehouseName = ExportUtil.replaceBlank(warehouseName).trim();
			customerName  = ExportUtil.replaceBlank(customerName).trim();
			
			String warehouseCode = "";
			if(wareHouseMap.get(warehouseName)==null) {
				 errorMsg+="仓库名["+warehouseName+"]没有在仓库表中维护!";
		    }else {
		    	warehouseCode = wareHouseMap.get(warehouseName).trim();
			}
			
			String customerid = "";
		    if(customerMap.get(customerName)==null) {
		    	 errorMsg+="商家["+customerName+"]没有在表中维护!";
		    }else {
		    	customerid = customerMap.get(customerName).trim();
			}
		    
			
			String pLd = ExportUtil.getValue(xssfRow.getCell(3), evaluator);//商品冷冻数量	
			String pLc = ExportUtil.getValue(xssfRow.getCell(4), evaluator);//商品冷藏数量
			String pCw = ExportUtil.getValue(xssfRow.getCell(5), evaluator);//商品常温数量
			String pHw = ExportUtil.getValue(xssfRow.getCell(6), evaluator);//商品恒温数量
			
			String mLd = ExportUtil.getValue(xssfRow.getCell(7), evaluator);//耗材冷冻数量
			String mLc = ExportUtil.getValue(xssfRow.getCell(8), evaluator);//耗材冷藏数量
			String mCw = ExportUtil.getValue(xssfRow.getCell(9), evaluator);//耗材常温数量
			String mHw = ExportUtil.getValue(xssfRow.getCell(10), evaluator);//耗材恒温数量
			
			boolean ispLd =false;
			boolean ispLc =false;
			boolean ispCw =false;
			boolean ispHw =false;
			
			boolean ismLd =false;
			boolean ismLc =false;
			boolean ismCw =false;
			boolean ismHw =false;
			
			if(StringUtils.isNotEmpty(pLd))	{
				if(ExportUtil.isNumber(pLd)) {
					ispLd = true;
				}else{
					 errorMsg+="第"+4+"列为非数字！";
				}
			}
			
			if(StringUtils.isNotEmpty(pLc))
			{
				if(ExportUtil.isNumber(pLc))
				{
					ispLc = true;
				}else{
					 errorMsg+="第"+5+"列为非数字！";
				}
			}
			
			if(StringUtils.isNotEmpty(pCw))
			{
				if(ExportUtil.isNumber(pCw))
				{
					ispCw = true;
				}else{
					 errorMsg+="第"+6+"列为非数字！";
				}
			}
			
			if(StringUtils.isNotEmpty(pHw))
			{
				if(ExportUtil.isNumber(pHw))
				{
					ispHw = true;
				}else{
					 errorMsg+="第"+7+"列为非数字！";
				}
			}
			
			if(StringUtils.isNotEmpty(mLd))
			{
				if(ExportUtil.isNumber(mLd))
				{
					ismLd = true;
				}else{
					 errorMsg+="第"+8+"列为非数字！";
				}
			}
			
			if(StringUtils.isNotEmpty(mLc))
			{
				if(ExportUtil.isNumber(mLc))
				{
					ismLc = true;
				}else{
					errorMsg+="第"+9+"列为非数字！";
				}
			}
			
			if(StringUtils.isNotEmpty(mCw))
			{
				if(ExportUtil.isNumber(mCw))
				{
					ismCw = true;
				}else{
					errorMsg+="第"+10+"列为非数字！";
				}
			}
			
			if(StringUtils.isNotEmpty(mHw))
			{
				if(ExportUtil.isNumber(mHw))
				{
					ismHw = true;
				}else{
					errorMsg+="第"+11+"列为非数字！";
				}
			}
			
			if(ispLd)
			{
				BizProductPalletStorageEntity pallet = new BizProductPalletStorageEntity();
				pallet.setWarehouseCode(warehouseCode);
				pallet.setWarehouseName(warehouseName);
				pallet.setCustomerId(customerid);
				pallet.setCustomerName(customerName);
				pallet.setPalletNum(Double.valueOf(pLd));//托数
				pallet.setAdjustPalletNum(Double.valueOf(pLd));
				pallet.setStockTime(time);
				pallet.setTemperatureTypeName("冷冻");
				pallet.setTemperatureTypeCode(temperatureMap.get("冷冻"));
				
				pallet.setCreateTime(currentTime);
				pallet.setCreator(userName);
				pallet.setLastModifier(userName);
				pallet.setLastModifyTime(currentTime);
				pallet.setDelFlag("0");
				pallet.setIsCalculated(String.valueOf(ConstantInterface.Calculate.CALCULATE_NO));
				
				palletList.add(pallet);
			}
			
			if(ispLc)
			{
				BizProductPalletStorageEntity pallet = new BizProductPalletStorageEntity();
				pallet.setWarehouseCode(warehouseCode);
				pallet.setWarehouseName(warehouseName);
				pallet.setCustomerId(customerid);
				pallet.setCustomerName(customerName);
				pallet.setPalletNum(Double.valueOf(pLc));
				pallet.setAdjustPalletNum(Double.valueOf(pLc));
				pallet.setStockTime(time);
				pallet.setTemperatureTypeName("冷藏");
				pallet.setTemperatureTypeCode(temperatureMap.get("冷藏"));
				
				pallet.setCreateTime(currentTime);
				pallet.setCreator(userName);
				pallet.setLastModifier(userName);
				pallet.setLastModifyTime(currentTime);
				pallet.setDelFlag("0");
				pallet.setIsCalculated(String.valueOf(ConstantInterface.Calculate.CALCULATE_NO));
				
				palletList.add(pallet);
			}
			
			if(ispCw)
			{
				BizProductPalletStorageEntity pallet = new BizProductPalletStorageEntity();
				pallet.setWarehouseCode(warehouseCode);
				pallet.setWarehouseName(warehouseName);
				pallet.setCustomerId(customerid);
				pallet.setCustomerName(customerName);
				pallet.setPalletNum(Double.valueOf(pCw));
				pallet.setAdjustPalletNum(Double.valueOf(pCw));
				pallet.setStockTime(time);
				pallet.setTemperatureTypeName("常温");
				pallet.setTemperatureTypeCode(temperatureMap.get("常温"));
				
				pallet.setCreateTime(currentTime);
				pallet.setCreator(userName);
				pallet.setLastModifier(userName);
				pallet.setLastModifyTime(currentTime);
				pallet.setDelFlag("0");
				pallet.setIsCalculated(String.valueOf(ConstantInterface.Calculate.CALCULATE_NO));
				
				palletList.add(pallet);
			}
			
			if(ispHw)
			{
				BizProductPalletStorageEntity pallet = new BizProductPalletStorageEntity();
				pallet.setWarehouseCode(warehouseCode);
				pallet.setWarehouseName(warehouseName);
				pallet.setCustomerId(customerid);
				pallet.setCustomerName(customerName);
				pallet.setPalletNum(Double.valueOf(pHw));
				pallet.setAdjustPalletNum(Double.valueOf(pHw));
				pallet.setStockTime(time);
				pallet.setTemperatureTypeName("恒温");
				pallet.setTemperatureTypeCode(temperatureMap.get("恒温"));
				
				pallet.setCreateTime(currentTime);
				pallet.setCreator(userName);
				pallet.setLastModifier(userName);
				pallet.setLastModifyTime(currentTime);
				pallet.setDelFlag("0");
				pallet.setIsCalculated(String.valueOf(ConstantInterface.Calculate.CALCULATE_NO));
				
				palletList.add(pallet);
			}
			
			if(ismLd)
			{
				BizPackStorageEntity  pack  =  new BizPackStorageEntity();
				pack.setCustomerid(customerid);
				pack.setCustomerName(customerName);
				pack.setWarehouseCode(warehouseCode);
				pack.setWarehouseName(warehouseName);
				pack.setCurTime(time);
				pack.setPalletNum(Double.valueOf(mLd));
				pack.setAdjustPalletNum(Double.valueOf(mLd));
				pack.setTemperatureTypeName("冷冻");
				pack.setTemperatureTypeCode(temperatureMap.get("冷冻"));
				
				pack.setCreateTime(currentTime);
				pack.setCreator(userName);
				pack.setLastModifier(userName);
				pack.setLastModifyTime(currentTime);
				pack.setDelFlag("0");
				pack.setIsCalculated(String.valueOf(ConstantInterface.Calculate.CALCULATE_NO));
				
				packList.add(pack);
			}
									
			if(ismLc)
			{
				BizPackStorageEntity  pack  =  new BizPackStorageEntity();
				pack.setCustomerid(customerid);
				pack.setCustomerName(customerName);
				pack.setWarehouseCode(warehouseCode);
				pack.setWarehouseName(warehouseName);
				pack.setCurTime(time);
				pack.setPalletNum(Double.valueOf(mLc));
				pack.setAdjustPalletNum(Double.valueOf(mLc));
				pack.setTemperatureTypeName("冷藏");
				pack.setTemperatureTypeCode(temperatureMap.get("冷藏"));
				
				pack.setCreateTime(currentTime);
				pack.setCreator(userName);
				pack.setLastModifier(userName);
				pack.setLastModifyTime(currentTime);
				pack.setDelFlag("0");
				pack.setIsCalculated(String.valueOf(ConstantInterface.Calculate.CALCULATE_NO));
				
				packList.add(pack);
			}
			
			if(ismCw)
			{
				BizPackStorageEntity  pack  =  new BizPackStorageEntity();
				pack.setCustomerid(customerid);
				pack.setCustomerName(customerName);
				pack.setWarehouseCode(warehouseCode);
				pack.setWarehouseName(warehouseName);
				pack.setCurTime(time);
				pack.setPalletNum(Double.valueOf(mCw));
				pack.setAdjustPalletNum(Double.valueOf(mCw));
				pack.setTemperatureTypeName("常温");
				pack.setTemperatureTypeCode(temperatureMap.get("常温"));
				
				pack.setCreateTime(currentTime);
				pack.setCreator(userName);
				pack.setLastModifier(userName);
				pack.setLastModifyTime(currentTime);
				pack.setDelFlag("0");
				pack.setIsCalculated(String.valueOf(ConstantInterface.Calculate.CALCULATE_NO));
				
				packList.add(pack);
			}
			
			if(ismHw)
			{
				BizPackStorageEntity  pack  =  new BizPackStorageEntity();
				pack.setCustomerid(customerid);
				pack.setCustomerName(customerName);
				pack.setWarehouseCode(warehouseCode);
				pack.setWarehouseName(warehouseName);
				pack.setCurTime(time);
				pack.setPalletNum(Double.valueOf(mHw));
				pack.setAdjustPalletNum(Double.valueOf(mHw));
				pack.setTemperatureTypeName("恒温");
				pack.setTemperatureTypeCode(temperatureMap.get("恒温"));
				
				pack.setCreateTime(currentTime);
				pack.setCreator(userName);
				pack.setLastModifier(userName);
				pack.setLastModifyTime(currentTime);
				pack.setDelFlag("0");
				pack.setIsCalculated(String.valueOf(ConstantInterface.Calculate.CALCULATE_NO));
				
				packList.add(pack);
			}
			if(!StringUtils.isBlank(errorMsg)){
				setMessage(infoList, rowNum+1,errorMsg);
			}
        }

        if(infoList!=null&&infoList.size()>0){
        	map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
			return map;
        }
        
        DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 700);
        
        //st 产品提的新需求 基本上混合了 添加和更新
		List<BizProductPalletStorageEntity> palletaddList = new ArrayList<BizProductPalletStorageEntity>();
		List<BizPackStorageEntity> packaddList = new ArrayList<BizPackStorageEntity>();
		List<BizProductPalletStorageEntity> palletupdateList = new ArrayList<BizProductPalletStorageEntity>();
	    List<BizPackStorageEntity> packupdateList = new ArrayList<BizPackStorageEntity>();
	   
		int palletRowNum = 0;
		for(BizProductPalletStorageEntity entity:palletList)
		{   palletRowNum = palletRowNum +1;
			BizProductPalletStorageEntity temp = bizProductPalletStorageService.queryOneByParam(entity);
			if(temp!=null)
			{
				String feesNo = temp.getFeesNo();
				if(StringUtils.isNotBlank(feesNo))
				{
					Map<String,Object> param0 = new  HashMap<String,Object>();
					param0.put("feesNo", feesNo);
					List<FeesReceiveStorageEntity> feeList = feesReceiveStorageService.queryAll(param0);
					if (null!=feeList&&feeList.size()>0) 
					{
						//获取此时的费用状态
						String status = String.valueOf(feeList.get(0).getStatus());
						if("1".equals(status)){
							setMessage(infoList, palletRowNum+1,"已过账商品存储数据不能更新！");
							map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
							return map;
						}else{
							entity.setId(temp.getId());
							palletupdateList.add(entity);
						}
					}
				}else{
					entity.setId(temp.getId());
					palletupdateList.add(entity);
				}
				
			}else{
				palletaddList.add(entity);
			}
		}
		
		int packRowNum = 0;
		for(BizPackStorageEntity entity:packList)
		{   packRowNum = packRowNum +1;
			BizPackStorageEntity packTemp = packStorageService.queryOneByParam(entity);
			if(null!=packTemp)
			{
				String feesNo = packTemp.getFeesNo();
				if(StringUtils.isNotBlank(feesNo))
				{
					Map<String,Object> param0 = new  HashMap<String,Object>();
					param0.put("feesNo", feesNo);
					List<FeesReceiveStorageEntity> feeList = feesReceiveStorageService.queryAll(param0);
					if (null!=feeList&&feeList.size()>0) 
					{
						//获取此时的费用状态
						String status = String.valueOf(feeList.get(0).getStatus());
						if("1".equals(status)){
							setMessage(infoList, packRowNum+1,"已过账耗材存储数据不能更新！");
							map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
							return map;
						}else{
							entity.setId(packTemp.getId());
							packupdateList.add(entity);
						}
					}
				}else{
					entity.setId(packTemp.getId());
					packupdateList.add(entity);
				}
				
			}else{
				packaddList.add(entity);
			}
		}
        //end
		
		
		try {
			insertNum = bizProductPalletStorageService.saveListAll(palletaddList, packaddList,palletList,packList);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			//写入日志
			BmsErrorLogInfoEntity bmsErrorLogInfoEntity=new BmsErrorLogInfoEntity(this.getClass().getSimpleName(),Thread.currentThread().getStackTrace()[1].getMethodName(), "", e.toString());
			bmsErrorLogInfoService.log(bmsErrorLogInfoEntity);
		}
		
		DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 950);
        
        if (insertNum <= 0) 
		{
			DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 999);
			errorVo = new ErrorMessageVo();			
			errorVo.setMsg("存档商品按托库存失败!");
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
		 * 获取处理进度 0-开始处理 1-验证模板 2-读取Excel 3-开始验证数据 4-开始保存数据 5-保存结束 6-异常结束
		 * @return
		 */
	    @Expose
		public int getProgress(){
	    	if(redisClient.exists(sessionId, nameSpace)){
	    		return Integer.valueOf(redisClient.get(sessionId, nameSpace, null));
	    	}else{
	    		return 0;
	    	}
		}
		/**
		 *重置处理进度
		 */
	    @Expose
		public void resetProgress() {
	    	redisClient.del(sessionId, nameSpace);
		}
		/**
		 * 过期时间 1小时
		 * @param progress
		 */
		private void setProgress(Object obj){
			
			redisClient.set(sessionId, nameSpace, obj,10*60);
		}
	
	/**
	 * 批量导入
	 * @param file
	 * @param parameter
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> importStorageNew(UploadFile file, Map<String, Object> parameter) throws Exception{
		
		DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 10);
		Map<String, Object> map = new HashMap<String, Object>();
		String[] str = {"库存日期","仓库","商家全称","商品冷冻","商品冷藏","商品常温","商品恒温","耗材冷冻","耗材冷藏","耗材常温","耗材恒温"};
		// 校验信息（报错提示）
		List<ErrorMessageVo> infoList = new ArrayList<ErrorMessageVo>();
		ErrorMessageVo errorVo = null;
		
		// 检查导入模板是否正确
		boolean isTemplate = ExportUtil.checkTitle(file,str);
		if (!isTemplate) 
		{
			setProgress(999);
			errorVo = new ErrorMessageVo();
			errorVo.setMsg("Excel导入格式错误请参考标准模板检查!");
			infoList.add(errorVo);
			map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
			return map;
		}
		setProgress(100);
		//start
		Map<String, String> wareHouseMap = new HashMap<String, String>();
		Map<String, String> customerMap = new HashMap<String, String>();
		Map<String, String> temperatureMap = new HashMap<String, String>();
		//仓库
		List<WarehouseVo> wareHouseList = warehouseService.queryAllWarehouse();
		if(wareHouseList != null && wareHouseList.size()>0)
		{
			for(WarehouseVo wareHouse : wareHouseList){
				wareHouseMap.put(wareHouse.getWarehousename().trim(),wareHouse.getWarehouseid());
			}
		}
		//商家
		PageInfo<CustomerVo> tmpPageInfo = customerService.query(null, 0, Integer.MAX_VALUE);
		if (tmpPageInfo != null && tmpPageInfo.getList() != null && tmpPageInfo.getList().size()>0) 
		{
			for(CustomerVo customer : tmpPageInfo.getList()){
				if(customer != null){
					customerMap.put(customer.getCustomername().trim(), customer.getCustomerid());
				}
			}
		}
		//温度类型
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("typeCode", "TEMPERATURE_TYPE");
		List<SystemCodeEntity> temperatureList = systemCodeService.queryCodeList(param);
		if(temperatureList != null && temperatureList.size()>0){
		    for(SystemCodeEntity scEntity : temperatureList){
		    	temperatureMap.put(scEntity.getCodeName().trim(), scEntity.getCode());
		    }
		}
	   //end
		
		XSSFWorkbook xssfWorkbook = new XSSFWorkbook(file.getInputStream());
		XSSFFormulaEvaluator  evaluator = new XSSFFormulaEvaluator(xssfWorkbook); 
		
		XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(0);
		if(xssfSheet==null)
			return null;
		
		// 库存数据
		List<BizProductPalletStorageEntity> palletList = new ArrayList<BizProductPalletStorageEntity>();
		//耗材数据
		List<BizPackStorageEntity> packList = new ArrayList<BizPackStorageEntity>();
		setProgress(200);
		Timestamp currentTime = JAppContext.currentTimestamp();
		String userName = JAppContext.currentUserName();
		//插入正式表
		int insertNum = 0;
		int dupNum = 0;
        for (int rowNum = 1;  rowNum <= xssfSheet.getLastRowNum(); rowNum++)
        {
        	String errorMsg="";
			XSSFRow xssfRow = xssfSheet.getRow(rowNum); 
			if(null==xssfRow)
			{
				if(rowNum!=xssfSheet.getLastRowNum()){
					errorMsg+="空行！";
				}else{
					break;
				}
				
			}
				
			String rkrq = ExportUtil.getValue(xssfRow.getCell(0), evaluator);//入库日期
			String warehouseName = ExportUtil.getValue(xssfRow.getCell(1), evaluator);
			String customerName = ExportUtil.getValue(xssfRow.getCell(2), evaluator);
			Timestamp time = null;
			
			if(StringUtils.isEmpty(rkrq))
			{
			    int lieshu = 1;
			    errorMsg+="第"+lieshu+"列入库日期不能为空！";
			}
			
			try {
				  time = Timestamp.valueOf(rkrq);
			  } catch (Exception e){
				  try {
					  String[] dataPatterns = new String[] { "yyyy-MM-dd hh:mm:ss", "yyyy-MM-dd", "dd-MM月 -yy","yyyy/MM/dd","yyyy/MM/dd HH:mm:ss","yyyy年MM月dd日" };
					  Date date = DateUtils.parseDate(rkrq, dataPatterns);
					  time = new Timestamp(date.getTime());
				} catch (Exception e1) {
					int lieshu = 1;
					 errorMsg+="第"+lieshu+"列日期格式不对！";
					//写入日志
					BmsErrorLogInfoEntity bmsErrorLogInfoEntity=new BmsErrorLogInfoEntity(this.getClass().getSimpleName(),Thread.currentThread().getStackTrace()[1].getMethodName(), "第"+lieshu+"列日期格式不对！", e.toString());
					bmsErrorLogInfoService.log(bmsErrorLogInfoEntity);
					e1.printStackTrace();
				}
				  e.printStackTrace();
				//写入日志
				BmsErrorLogInfoEntity bmsErrorLogInfoEntity=new BmsErrorLogInfoEntity(this.getClass().getSimpleName(),Thread.currentThread().getStackTrace()[1].getMethodName(), "", e.toString());
				bmsErrorLogInfoService.log(bmsErrorLogInfoEntity);
			}
			
			if(StringUtils.isEmpty(warehouseName))
			{
			    int lieshu = 2;
			    errorMsg+="第"+lieshu+"列仓库名称为空！";
			}
			
			if(StringUtils.isEmpty(customerName))
			{
			    int lieshu = 3;
			    errorMsg+="第"+lieshu+"列商家名称为空！";
		    }
			
			warehouseName = ExportUtil.replaceBlank(warehouseName).trim();
			customerName  = ExportUtil.replaceBlank(customerName).trim();
			
			
			String warehouseCode="";
			if(wareHouseMap.get(warehouseName)==null)
			{
				errorMsg+="仓库名["+warehouseName+"]没有在仓库表中维护!";
		    }else{
		    	warehouseCode= wareHouseMap.get(warehouseName).trim();
		    }
			
			String customerid="";
		    if(customerMap.get(customerName)==null)
		    {
		    	 errorMsg+="商家["+customerName+"]没有在表中维护!";
		    }else{
		    	customerid= customerMap.get(customerName).trim();
		    }
		    	
			String pLd = ExportUtil.getValue(xssfRow.getCell(3), evaluator);//商品冷冻数量	
			String pLc = ExportUtil.getValue(xssfRow.getCell(4), evaluator);
			String pCw = ExportUtil.getValue(xssfRow.getCell(5), evaluator);
			String pHw = ExportUtil.getValue(xssfRow.getCell(6), evaluator);
			
			String mLd = ExportUtil.getValue(xssfRow.getCell(7), evaluator);//耗材冷冻数量
			String mLc = ExportUtil.getValue(xssfRow.getCell(8), evaluator);
			String mCw = ExportUtil.getValue(xssfRow.getCell(9), evaluator);
			String mHw = ExportUtil.getValue(xssfRow.getCell(10), evaluator);
			
			Map<String,Object> pMap=new HashMap<>();
			Map<String,Object> mMap=new HashMap<>();
					
			if(StringUtils.isNotEmpty(pLd)){
				if(ExportUtil.isNumber(pLd)){
					//ispLd = true;
					pMap.put("pLd", pLd);
				}else{
					 errorMsg+="第"+4+"列为非数字！";
				}
			}
			
			if(StringUtils.isNotEmpty(pLc)){
				if(ExportUtil.isNumber(pLc)){
					//ispLc = true;
					pMap.put("pLc", pLc);
				}else{
					 errorMsg+="第"+5+"列为非数字！";
				}
			}
			
			if(StringUtils.isNotEmpty(pCw)){
				if(ExportUtil.isNumber(pCw)){
					//ispCw = true;
					pMap.put("pCw", pCw);
				}else{
					 errorMsg+="第"+6+"列为非数字！";
				}
			}
			if(StringUtils.isNotEmpty(pHw)){
				if(ExportUtil.isNumber(pHw)){
					 //ispHw = true;
					pMap.put("pHw", pHw);
				}else{
					 errorMsg+="第"+7+"列为非数字！";
				}
			}
			
			
			if(StringUtils.isNotEmpty(mLd)){
				if(ExportUtil.isNumber(mLd)){
					//ismLd = true;
					mMap.put("mLd", mLd);
				}else{
					 errorMsg+="第"+8+"列为非数字！";
				}
			}
			
			if(StringUtils.isNotEmpty(mLc)){
				if(ExportUtil.isNumber(mLc)){
					//ismLc = true;
					mMap.put("mLc", mLc);
				}else{
					errorMsg+="第"+9+"列为非数字！";
				}
			}
			
			if(StringUtils.isNotEmpty(mCw)){
				if(ExportUtil.isNumber(mCw)){
					//ismCw = true;
					mMap.put("mCw", mCw);
				}else{
					errorMsg+="第"+10+"列为非数字！";
				}
			}
			if(StringUtils.isNotEmpty(mHw)){
				if(ExportUtil.isNumber(mHw)){
					//ismHw = true;
					mMap.put("mHw", mHw);
				}else{
					errorMsg+="第"+11+"列为非数字！";
				}
			}
			
			//商品按托存储的			
			for (String key : pMap.keySet()) {
				BizProductPalletStorageEntity pallet = new BizProductPalletStorageEntity();
				pallet.setWarehouseCode(warehouseCode);
				pallet.setWarehouseName(warehouseName);
				pallet.setCustomerId(customerid);
				pallet.setCustomerName(customerName);
				pallet.setStockTime(time);
				if(key=="pLd"){
					pallet.setPalletNum(Double.valueOf(pLd));//托数
					pallet.setAdjustPalletNum(Double.valueOf(pLd));
					pallet.setTemperatureTypeName("冷冻");
					pallet.setTemperatureTypeCode(temperatureMap.get("冷冻"));
				}else if(key=="pLc"){
					pallet.setPalletNum(Double.valueOf(pLc));//托数
					pallet.setAdjustPalletNum(Double.valueOf(pLc));
					pallet.setTemperatureTypeName("冷藏");
					pallet.setTemperatureTypeCode(temperatureMap.get("冷藏"));
				}else if(key=="pCw"){
					pallet.setPalletNum(Double.valueOf(pCw));//托数
					pallet.setAdjustPalletNum(Double.valueOf(pCw));
					pallet.setTemperatureTypeName("常温");
					pallet.setTemperatureTypeCode(temperatureMap.get("常温"));
				}else if(key=="pHw"){
					pallet.setPalletNum(Double.valueOf(pHw));//托数
					pallet.setAdjustPalletNum(Double.valueOf(pHw));
					pallet.setTemperatureTypeName("恒温");
					pallet.setTemperatureTypeCode(temperatureMap.get("恒温"));
				}
				pallet.setWriteTime(currentTime);
				pallet.setCreateTime(time);
				pallet.setCreator(userName);
				pallet.setLastModifier(userName);
				pallet.setLastModifyTime(currentTime);
				pallet.setDelFlag("0");
				pallet.setIsCalculated(String.valueOf(ConstantInterface.Calculate.CALCULATE_NO));
				
				palletList.add(pallet);
		    } 
			
			//耗材按托存储的
			for(String key : mMap.keySet()){
				BizPackStorageEntity  pack  =  new BizPackStorageEntity();
				pack.setCustomerid(customerid);
				pack.setCustomerName(customerName);
				pack.setWarehouseCode(warehouseCode);
				pack.setWarehouseName(warehouseName);
				pack.setCurTime(time);			
				if(key=="mLd"){
					pack.setPalletNum(Double.valueOf(mLd));
					pack.setAdjustPalletNum(Double.valueOf(mLd));
					pack.setTemperatureTypeName("冷冻");
					pack.setTemperatureTypeCode(temperatureMap.get("冷冻"));					
				}else if(key=="mLc"){
					pack.setPalletNum(Double.valueOf(mLc));
					pack.setAdjustPalletNum(Double.valueOf(mLc));
					pack.setTemperatureTypeName("冷藏");
					pack.setTemperatureTypeCode(temperatureMap.get("冷藏"));
				}else if(key=="mCw"){
					pack.setPalletNum(Double.valueOf(mCw));
					pack.setAdjustPalletNum(Double.valueOf(mCw));
					pack.setTemperatureTypeName("常温");
					pack.setTemperatureTypeCode(temperatureMap.get("常温"));
				}else if(key=="mHw"){
					pack.setPalletNum(Double.valueOf(mHw));
					pack.setAdjustPalletNum(Double.valueOf(mHw));
					pack.setTemperatureTypeName("恒温");
					pack.setTemperatureTypeCode(temperatureMap.get("恒温"));
				}
				
				pack.setWriteTime(currentTime);
				pack.setCreateTime(time);
				pack.setCreator(userName);
				pack.setLastModifier(userName);
				pack.setLastModifyTime(currentTime);
				pack.setDelFlag("0");
				pack.setIsCalculated(String.valueOf(ConstantInterface.Calculate.CALCULATE_NO));
				
				packList.add(pack);
			}
			if(!StringUtils.isBlank(errorMsg)){
				setMessage(infoList, rowNum+1,errorMsg);
			}
        }

        if(infoList!=null&&infoList.size()>0){
        	map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
			return map;
        }
        
        Map<String,Object> mapCheck=new HashMap<String,Object>();
        if(palletList!=null&&palletList.size()>0){
        	  mapCheck=checkPallet(palletList,infoList,map);
        		if (mapCheck.get(ConstantInterface.ImportExcelStatus.IMP_ERROR) != null) {//验证数据重复
        			return mapCheck;
        		}	
        }
  		if(packList!=null&&packList.size()>0){
  			mapCheck=checkPack(packList,infoList,map);
  	  		if (mapCheck.get(ConstantInterface.ImportExcelStatus.IMP_ERROR) != null) {//验证数据重复
  	  			return mapCheck;
  	  		}	
  		}
  		setProgress(700);
        //DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 700);
        
        //st 产品提的新需求 基本上混合了 添加和更新
		List<BizProductPalletStorageEntity> palletaddList = new ArrayList<BizProductPalletStorageEntity>();
		List<BizPackStorageEntity> packaddList = new ArrayList<BizPackStorageEntity>();
		List<BizProductPalletStorageEntity> palletupdateList = new ArrayList<BizProductPalletStorageEntity>();
	    List<BizPackStorageEntity> packupdateList = new ArrayList<BizPackStorageEntity>();
		
	    palletaddList.addAll(palletList);
	    packaddList.addAll(packList);
	    
		/*int packRowNum = 0;
		for(BizPackStorageEntity entity:packList){   
			packRowNum = packRowNum +1;
			BizPackStorageEntity packTemp = packStorageService.queryOneByParam(entity);
			// 2018-03-19 add by yangss 耗材按托导入时默认将托数写入调整托数，修改只写入调整托数
			entity.setAdjustPalletNum(entity.getPalletNum());
			if(null!=packTemp){
				String feesNo = packTemp.getFeesNo();
				if(StringUtils.isNotBlank(feesNo)){
					Map<String,Object> param0 = new  HashMap<String,Object>();
					param0.put("feesNo", feesNo);
					List<FeesReceiveStorageEntity> feeList = feesReceiveStorageService.queryAll(param0);
					if (null!=feeList&&feeList.size()>0) {
						//获取此时的费用状态
						String status = String.valueOf(feeList.get(0).getStatus());
						if("1".equals(status)){
							setMessage(infoList, packRowNum+1,"已过账耗材存储数据不能更新！");
							map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
							return map;
						}else{
							entity.setId(packTemp.getId());
							packupdateList.add(entity);
						}
					}
				}else{
					entity.setId(packTemp.getId());
					packupdateList.add(entity);
				}
				
			}else{
				packaddList.add(entity);
			}
		}*/
        //end
		
		
		/*int palletRowNum = 0;
		for(BizProductPalletStorageEntity entity:palletList){
			palletRowNum = palletRowNum +1;
			BizProductPalletStorageEntity temp = bizProductPalletStorageService.queryOneByParam(entity);
			// 2018-03-19 add by yangss 商品按托导入时默认将托数写入调整托数，修改只写入调整托数
			entity.setAdjustPalletNum(entity.getPalletNum());
			if(temp!=null){
				String feesNo = temp.getFeesNo();
				if(StringUtils.isNotBlank(feesNo)){
					Map<String,Object> param0 = new  HashMap<String,Object>();
					param0.put("feesNo", feesNo);
					List<FeesReceiveStorageEntity> feeList = feesReceiveStorageService.queryAll(param0);
					if (null!=feeList&&feeList.size()>0) {
						//获取此时的费用状态
						String status = String.valueOf(feeList.get(0).getStatus());
						if("1".equals(status)){
							setMessage(infoList, palletRowNum+1,"已过账商品存储数据不能更新！");
							map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
							return map;
						}else{
							entity.setId(temp.getId());
							palletupdateList.add(entity);
						}
					}
				}else{
					entity.setId(temp.getId());
					palletupdateList.add(entity);
				}
				
			}else{
				palletaddList.add(entity);
			}
		}*/
			
		try {
			insertNum = bizProductPalletStorageService.saveListAll(palletaddList, packaddList,palletupdateList,packupdateList);
		} catch (Exception e) {
			e.printStackTrace();
			if((e.getMessage().indexOf("Duplicate entry"))>0){
				dupNum = 1;
			}
			logger.error(e.getMessage());
			//写入日志
			BmsErrorLogInfoEntity bmsErrorLogInfoEntity=new BmsErrorLogInfoEntity(this.getClass().getSimpleName(),Thread.currentThread().getStackTrace()[1].getMethodName(), "", e.toString());
			bmsErrorLogInfoService.log(bmsErrorLogInfoEntity);
		}
		setProgress(950);
		//DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 950);
        
        if (insertNum <= 0) {
        	setProgress(999);
			//DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 999);
			errorVo = new ErrorMessageVo();
			if(dupNum>0){
				errorVo.setMsg("违反唯一性校验,重复的数据不会导入");
			}else{
				errorVo.setMsg("存档商品按托库存失败!");
			}
			infoList.add(errorVo);
			map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
			return map;
		}else{
			setProgress(1000);
			//DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 1000);
			map.put(ConstantInterface.ImportExcelStatus.IMP_SUCC, "0");
			return map;
		}
		
	}

	
	@DataProvider
	public int queryDelete(Page<BizProductPalletStorageEntity> page,Map<String, Object> param) {
		List<BizProductPalletStorageEntity> list = bizProductPalletStorageService.queryList(param);
		bizProductPalletStorageService.deleteFees(param);
		int i = bizProductPalletStorageService.deleteList(list);
		return i;
	}
	
	@DataProvider
	public void queryTj(Page<Map<String,String>> page, Map<String, Object> param) {
		PageInfo<Map<String,String>> pageInfo = null;
		if("1".equals(param.get("isCalculatedF"))){
			 pageInfo = bizProductPalletStorageService.queryCustomeridF(param, page.getPageNo(), page.getPageSize());
		}else{
			pageInfo = bizProductPalletStorageService.queryByMonth(param, page.getPageNo(), page.getPageSize());
		}
		
		if (pageInfo != null) {
			page.setEntities(pageInfo.getList());
			page.setEntityCount((int) pageInfo.getTotal());
		}
	}
	
	
	
	protected List<BizProductPalletStorageEntity> getOrgPalletList(
			Map<String, Object> parameter) {
		return bizProductPalletStorageService.queryAllBystockTime(parameter);
	}

	private List<BizPackStorageEntity> getOrgPackList(Map<String,Object> map){
		return bizPackStorageService.queryAllBycurTime(map);
	}
	private Map<String,Object> checkPack(List<BizPackStorageEntity> packList,List<ErrorMessageVo> infoList,Map<String, Object> map){		
		Timestamp startTime=null;
		Timestamp endTime=null;
		Map<String,Object> queryMap=new HashMap<String,Object>();
		for(BizPackStorageEntity packEntity:packList){
	        	if(startTime==null||startTime.compareTo(packEntity.getCurTime())>0){
	        		startTime=packEntity.getCurTime();
	        	}
	        	if(endTime==null||endTime.compareTo(packEntity.getCurTime())<0){
	        		endTime=packEntity.getCurTime();
	        	}
	    }
		queryMap.put("startTime", startTime);
		queryMap.put("endTime", endTime);
		List<BizPackStorageEntity> orgList=getOrgPackList(queryMap);
		Map<String,Object> mapCheck=comparePack(orgList, packList);
		return mapCheck;
	}
	private String objToString(Object obj){
		if(obj==null){
			return "";
		}else{
			return obj.toString();
		}
	}
	private String getPackKey(BizPackStorageEntity dataEntity){
		String key=objToString(dataEntity.getWarehouseCode())+objToString(dataEntity.getCustomerid())+objToString(dataEntity.getCurTime()+objToString(dataEntity.getTemperatureTypeCode()));
		return key;
	}
	private String getPackDataKey(BizPackStorageEntity dataEntity){
		String dataKey=objToString(dataEntity.getWarehouseCode())+"&"+objToString(dataEntity.getCustomerid())+"&"+objToString(dataEntity.getCurTime())+"&"+objToString(dataEntity.getTemperatureTypeCode());
		return dataKey;
	}
	private String getPackDataInfo(BizPackStorageEntity dataEntity){
		String info=objToString(dataEntity.getCurTime())+"-"+objToString(dataEntity.getWarehouseName())+"-"+objToString(dataEntity.getCustomerName())+"-"+objToString(dataEntity.getTemperatureTypeName());
		return info;
	}
	private Map<String,Object> comparePack(List<BizPackStorageEntity> orgList,List<BizPackStorageEntity> dataList){
		Map<String,Object> mapCompare=new HashMap<String,Object>();
		List<ErrorMessageVo> infoList=new ArrayList<ErrorMessageVo>();
		//验证导入数据有重复
		List<String> dataKeyList=new ArrayList<String>();
	    List<String> keyList=new ArrayList<String>();
		int index=0;
		int lineNo=0;
		for(BizPackStorageEntity dataEntity:dataList){
			String key=getPackKey(dataEntity);
			lineNo++;
			String dataKey=getPackDataKey(dataEntity);
			if(!keyList.contains(key)){//index 设置
				keyList.add(key);
				index++;
			}
			if(!dataKeyList.contains(dataKey)){//excel数据无重复 验证与数据库对比
				dataKeyList.add(dataKey);
				boolean f=true;
				for(BizPackStorageEntity orgEntity:orgList){
					String orgDataKey=getPackDataKey(orgEntity);
					if(orgDataKey.equals(dataKey)){
						f=false;
						break;
					}
				}
				if(!f){//与表中数据有重复
					setMessage(infoList, lineNo,"第"+index+"行,系统已存在【"+getPackDataInfo(dataEntity)+"】耗材库存明细");
				}
			}else{
				setMessage(infoList, lineNo,"Excel中第"+index+"行数据重复");
			}
		}
		//重复出库单号
		if(infoList.size()>0){
			mapCompare.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
		}else{
			mapCompare.put(ConstantInterface.ImportExcelStatus.IMP_SUCC, dataList); // 无基本错误信息
		}
		return mapCompare;
	}
	private Map<String,Object> checkPallet(List<BizProductPalletStorageEntity> palletList,List<ErrorMessageVo> infoList,Map<String, Object> map){
		//验证重复导入 todo
		
        Timestamp startTime=null;
        Timestamp endTime=null;
        for(BizProductPalletStorageEntity palletEntity:palletList){
        	if(startTime==null||startTime.compareTo(palletEntity.getStockTime())>0){
        		startTime=palletEntity.getStockTime();
        	}
        	if(endTime==null||endTime.compareTo(palletEntity.getStockTime())<0){
        		endTime=palletEntity.getStockTime();
        	}
        }
		Map<String,Object> querymap=new HashMap<String,Object>();
        querymap.put("startTime", startTime);
        querymap.put("endTime", endTime);
        
        List<BizProductPalletStorageEntity> orgList=getOrgPalletList(querymap);
	    Map<String,Object> mapCheck=comparePallet(orgList, palletList);
	    return mapCheck;
	}
	private String getPalletKey(BizProductPalletStorageEntity dataEntity){
		String key=objToString(dataEntity.getWarehouseCode())+objToString(dataEntity.getCustomerId())+objToString(dataEntity.getStockTime());
		return key;
	}
	private String getPalletDataKey(BizProductPalletStorageEntity dataEntity){
		String dataKey=objToString(dataEntity.getWarehouseCode())+"&"+objToString(dataEntity.getCustomerId())+"&"+objToString(dataEntity.getStockTime())+"&"+objToString(dataEntity.getTemperatureTypeCode());
		return dataKey;
	}
	private String getPalletInfo(BizProductPalletStorageEntity dataEntity){
		String dataKey=objToString(dataEntity.getStockTime())+"-"+objToString(dataEntity.getWarehouseName())+"-"+objToString(dataEntity.getCustomerName())+"-"+objToString(dataEntity.getTemperatureTypeName());
		return dataKey;
	}
	private Map<String,Object> comparePallet(List<BizProductPalletStorageEntity> orgList,List<BizProductPalletStorageEntity> dataList){
		Map<String,Object> mapCompare=new HashMap<String,Object>();
		List<ErrorMessageVo> infoList=new ArrayList<ErrorMessageVo>();
		//验证导入数据有重复
		List<String> dataKeyList=new ArrayList<String>();
	    List<String> keyList=new ArrayList<String>();
		int index=0;
		int lineNo=0;
		for(BizProductPalletStorageEntity dataEntity:dataList){
			String key=getPalletKey(dataEntity);
			lineNo++;
			String dataKey=getPalletDataKey(dataEntity);
			if(!keyList.contains(key)){//index 设置
				keyList.add(key);
				index++;
			}
			if(!dataKeyList.contains(dataKey)){//excel数据无重复 验证与数据库对比
				dataKeyList.add(dataKey);
				boolean f=true;
				for(BizProductPalletStorageEntity orgEntity:orgList){
					String orgDataKey=getPalletDataKey(orgEntity);
					if(orgDataKey.equals(dataKey)){
						f=false;
						break;
					}
				}
				if(!f){//与表中数据有重复
					setMessage(infoList, lineNo,"第"+index+"行,系统已存在【"+getPalletInfo(dataEntity)+"】按托库存明细");
				}
			}else{
				setMessage(infoList, lineNo,"Excel中第"+index+"行数据重复");
			}
		}
		//重复出库单号
		if(infoList.size()>0){
			mapCompare.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
		}else{
			mapCompare.put(ConstantInterface.ImportExcelStatus.IMP_SUCC, dataList); // 无基本错误信息
		}
		return mapCompare;
	}
	protected List<String> getpackKeyProperty() {
		List<String> list=new ArrayList<String>();
		list.add("curTime");//当前日期
		list.add("warehouseCode");//仓库Id
		list.add("customerid");//商家ID
		list.add("temperatureTypeCode");//温度
		return null;
	}
	private List<String> getpalletKeyProperty(){
		List<String> list=new ArrayList<String>();
		list.add("warehouseCode");//仓库编码
		list.add("customerId");//商家ID
		list.add("temperatureTypeCode");//温度类型
		list.add("stockTime");//库存日志
		return list;
	}

		  
	
}
