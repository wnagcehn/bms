package com.jiuyescm.bms.quotation.storage.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

import com.alibaba.fastjson.JSON;
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
import com.jiuyescm.bms.common.enumtype.RecordLogBizTypeEnum;
import com.jiuyescm.bms.common.enumtype.RecordLogOperateType;
import com.jiuyescm.bms.common.enumtype.RecordLogUrlNameEnum;
import com.jiuyescm.bms.common.log.service.IBmsErrorLogInfoService;
import com.jiuyescm.bms.common.sequence.service.SequenceService;
import com.jiuyescm.bms.common.tool.Session;
import com.jiuyescm.bms.common.web.CommonComparePR;
import com.jiuyescm.bms.pub.IPubRecordLogService;
import com.jiuyescm.bms.pub.PubRecordLogEntity;
import com.jiuyescm.bms.quotation.storage.entity.PriceMaterialQuotationEntity;
import com.jiuyescm.bms.quotation.storage.service.IPriceMaterialQuotationService;
import com.jiuyescm.bms.quotation.transport.entity.GenericTemplateEntity;
import com.jiuyescm.bms.quotation.transport.service.IGenericTemplateService;
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
import com.jiuyescm.common.utils.upload.MaterialTemplateDataType;
import com.jiuyescm.exception.BizException;
import com.jiuyescm.framework.lock.Lock;
import com.jiuyescm.framework.lock.LockCallback;
import com.jiuyescm.framework.lock.LockCantObtainException;
import com.jiuyescm.framework.lock.LockInsideExecutedException;
import com.jiuyescm.mdm.customer.api.IPubMaterialInfoService;
import com.jiuyescm.mdm.customer.vo.SystemCodeVo;
import com.jiuyescm.mdm.warehouse.api.IWarehouseService;
import com.jiuyescm.mdm.warehouse.vo.WarehouseVo;
import com.thoughtworks.xstream.core.util.Base64Encoder;


@Controller("stroageMaterialQuoteController")
public class StroageMaterialQuoteController extends CommonComparePR<PriceMaterialQuotationEntity>{
	
	private static final Logger logger = LoggerFactory.getLogger(StroageMaterialQuoteController.class);
	
	
	@Resource
	private IPriceMaterialQuotationService  service;
	
	@Resource 
	private SequenceService sequenceService;
	
	@Resource
	private IGenericTemplateService genericTemplateService;
	
	@Resource
	private IWarehouseService  warehouseService;
	
	@Resource
	private ISystemCodeService systemCodeService;
	
	@Resource
	private IPubMaterialInfoService pubMaterialInfoService;
	
	@Resource
	private Lock lock;
	
	@Resource
	private IBmsErrorLogInfoService bmsErrorLogInfoService;
	@Resource
	private IPubRecordLogService pubRecordLogService;
	@DataProvider
	public void query(Page<PriceMaterialQuotationEntity> page, Map<String, Object> param) {
		/*if(StringUtils.isNotEmpty((String)param.get("materialType")))
		{
			List<SystemCodeVo> tmscodels = pubMaterialInfoService.findEnumList("");
			List<SystemCodeEntity> tmscodels = systemCodeService.findEnumList("PACKMAGERIAL_TYPE");
			for (SystemCodeVo SystemCodeVo : tmscodels)
			{
				if(SystemCodeVo.getCodeName().equals(param.get("materialType")))
				{
					param.put("materialType", SystemCodeVo.getCode());
					break;
				}
			}
		}*/
		
		PageInfo<PriceMaterialQuotationEntity> pageInfo = service.query(param, page.getPageNo(), page.getPageSize());
		if (pageInfo != null) {
			page.setEntities(pageInfo.getList());
			page.setEntityCount((int) pageInfo.getTotal());
		}
	}

	@DataResolver
	public String save(PriceMaterialQuotationEntity entity){
		try{
			if(Session.isMissing()){
				return "长时间未操作，用户已失效，请重新登录再试！";
			}else if(entity == null){
				return "页面传递参数有误！";
			}
			//重复性校验
			if (!"PLATIC_BOX".equals(entity.getMaterialType())) {
				Map<String, Object> parameter = new HashMap<>();
				parameter.put("templateId", entity.getTemplateId());
				List<PriceMaterialQuotationEntity> list = service.queryByTemplateId(parameter);
				for (PriceMaterialQuotationEntity priceMaterialQuotationEntity : list) {
					if ((priceMaterialQuotationEntity.getMaterialCode()+priceMaterialQuotationEntity.getWarehouseId()).equals(entity.getMaterialCode()+entity.getWarehouseId())) {
						return "非泡沫箱的耗材不允许重复录入!";
					}
				}
			}
			
			entity.setCreateTime(JAppContext.currentTimestamp());
			entity.setCreator(JAppContext.currentUserName());
			entity.setLastModifier(JAppContext.currentUserName());
			entity.setLastModifyTime(JAppContext.currentTimestamp());
			entity.setDelFlag("0");
			Integer i = 0;
				i =	service.save(entity);
			
			if(i>0){
				try{
					PubRecordLogEntity model=new PubRecordLogEntity();
					model.setBizType(RecordLogBizTypeEnum.PRICE.getCode());
					model.setNewData(JSON.toJSONString(entity));
					model.setOldData("");
					model.setOperateDesc("新增耗材明细报价,模板编码【"+entity.getTemplateId()+"】");
					model.setOperatePerson(JAppContext.currentUserName());
					model.setOperateTable("price_storage_material");
					model.setOperateTime(JAppContext.currentTimestamp());
					model.setOperateType(RecordLogOperateType.INSERT.getCode());
					model.setRemark("");
					model.setOperateTableKey(entity.getTemplateId());
					model.setUrlName(RecordLogUrlNameEnum.IN_STORAGE_MATERIAL_PRICE.getCode());
					pubRecordLogService.AddRecordLog(model);
				}catch(Exception e){
					logger.error("记录日志失败,失败原因:"+e.getMessage());
				}
				return "SUCCESS";
			}else{
				return "fail";
			}
			
		}
		catch(Exception ex){
			//写入日志
			bmsErrorLogInfoService.insertLog(this.getClass().getSimpleName(),Thread.currentThread().getStackTrace()[1].getMethodName(), "", ex.toString());

//			if((ex.getMessage().indexOf("Duplicate entry"))>0){
//				return "违反唯一性校验";
//			}
			return "数据库操作失败";
		}
	}
	
	@DataResolver
	public String update(PriceMaterialQuotationEntity entity){
		try{
			if(Session.isMissing()){
				return "长时间未操作，用户已失效，请重新登录再试！";
			}else if(entity == null){
				return "页面传递参数有误！";
			}
			if (!"PLATIC_BOX".equals(entity.getMaterialType())) {
				Map<String, Object> parameter = new HashMap<>();
				parameter.put("templateId", entity.getTemplateId());
				parameter.put("id", entity.getId());
				List<PriceMaterialQuotationEntity> list = service.queryByTemplateId(parameter);
				for (PriceMaterialQuotationEntity priceMaterialQuotationEntity : list) {
					if ((priceMaterialQuotationEntity.getMaterialCode()+priceMaterialQuotationEntity.getWarehouseId()).equals(entity.getMaterialCode()+entity.getWarehouseId())) {
						return "非泡沫箱的耗材不允许重复录入!";
					}
				}
			}
			entity.setLastModifier(JAppContext.currentUserName());
			entity.setLastModifyTime(JAppContext.currentTimestamp());
			entity.setDelFlag("0");
			Integer i = 0;
				i =	service.update(entity);
			
			if(i>0){
				try{
					PubRecordLogEntity model=new PubRecordLogEntity();
					model.setBizType(RecordLogBizTypeEnum.PRICE.getCode());
					model.setNewData(JSON.toJSONString(entity));
					model.setOldData("");
					model.setOperateDesc("更新耗材明细报价,模板编码【"+entity.getTemplateId()+"】");
					model.setOperatePerson(JAppContext.currentUserName());
					model.setOperateTable("price_storage_material");
					model.setOperateTime(JAppContext.currentTimestamp());
					model.setOperateType(RecordLogOperateType.UPDATE.getCode());
					model.setRemark("");
					model.setOperateTableKey(entity.getTemplateId());
					model.setUrlName(RecordLogUrlNameEnum.IN_STORAGE_MATERIAL_PRICE.getCode());
					pubRecordLogService.AddRecordLog(model);
				}catch(Exception e){
					logger.error("记录日志失败,失败原因:"+e.getMessage());
				}
				return "SUCCESS";
			}else{
				return "fail";
			}
			
		}
		catch(Exception ex){
			//写入日志
			bmsErrorLogInfoService.insertLog(this.getClass().getSimpleName(),Thread.currentThread().getStackTrace()[1].getMethodName(), "", ex.toString());
//			if((ex.getMessage().indexOf("Duplicate entry"))>0){
//				return "违反唯一性校验";
//			}
			return "数据库操作失败";
		}
	}
	
	@DataResolver
	public String delete(PriceMaterialQuotationEntity entity){
		try{
			if(Session.isMissing()){
				return "长时间未操作，用户已失效，请重新登录再试！";
			}else if(entity == null){
				return "页面传递参数有误！";
			}
			entity.setLastModifier(JAppContext.currentUserName());
			entity.setLastModifyTime(JAppContext.currentTimestamp());
			entity.setDelFlag("1");
			Integer i = 0;
				i =	service.delete(entity);
			
			if(i>0){
				try{
					PubRecordLogEntity model=new PubRecordLogEntity();
					model.setBizType(RecordLogBizTypeEnum.PRICE.getCode());
					model.setNewData(JSON.toJSONString(entity));
					model.setOldData("");
					model.setOperateDesc("作废耗材明细报价,模板编码【"+entity.getTemplateId()+"】");
					model.setOperatePerson(JAppContext.currentUserName());
					model.setOperateTable("price_storage_material");
					model.setOperateTime(JAppContext.currentTimestamp());
					model.setOperateType(RecordLogOperateType.CANCEL.getCode());
					model.setRemark("");
					model.setOperateTableKey(entity.getTemplateId());
					model.setUrlName(RecordLogUrlNameEnum.IN_STORAGE_MATERIAL_PRICE.getCode());
					pubRecordLogService.AddRecordLog(model);
				}catch(Exception e){
					logger.error("记录日志失败,失败原因:"+e.getMessage());
				}
				return "SUCCESS";
			}else{
				return "fail";
			}
			
		}
		catch(Exception ex){
			//写入日志
			bmsErrorLogInfoService.insertLog(this.getClass().getSimpleName(),Thread.currentThread().getStackTrace()[1].getMethodName(), "", ex.toString());

			return "数据库操作失败";
		}
	}
	@DataResolver
	public String removeDetail(GenericTemplateEntity entity){
		try{
			Map<String,Object> map=new HashMap<String,Object>();
			map.put("templateId", entity.getId().toString());
			map.put("lastModifier", JAppContext.currentUserName());
			map.put("lastModifyTime", JAppContext.currentTimestamp());
			service.removeDetail(map);
			try{
				PubRecordLogEntity model=new PubRecordLogEntity();
				model.setBizType(RecordLogBizTypeEnum.PRICE.getCode());
				model.setNewData(JSON.toJSONString(entity));
				model.setOldData("");
				model.setOperateDesc("作废耗材明细报价");
				model.setOperatePerson(JAppContext.currentUserName());
				model.setOperateTable("price_storage_material");
				model.setOperateTime(JAppContext.currentTimestamp());
				model.setOperateType(RecordLogOperateType.CANCEL.getCode());
				model.setRemark("");
				model.setOperateTableKey(entity.getId().toString());
				model.setUrlName(RecordLogUrlNameEnum.IN_STORAGE_MATERIAL_PRICE.getCode());
				pubRecordLogService.AddRecordLog(model);
			}catch(Exception e){
				logger.error("记录日志失败,失败原因:"+e.getMessage());
			}
			return "succ";
		}
		catch(Exception ex){
			//写入日志
			bmsErrorLogInfoService.insertLog(this.getClass().getSimpleName(),Thread.currentThread().getStackTrace()[1].getMethodName(), "", ex.toString());

			return "数据库操作失败:"+ex.getMessage();
		}
	}
	@DataResolver
	public String removeAll(GenericTemplateEntity entity){
		try{
			Map<String,Object> map=new HashMap<String,Object>();
			map.put("templateId", entity.getId().toString());
			map.put("lastModifier", JAppContext.currentUserName());
			map.put("lastModifyTime", JAppContext.currentTimestamp());
			service.removeAll(map);
			try{
				PubRecordLogEntity model=new PubRecordLogEntity();
				model.setBizType(RecordLogBizTypeEnum.PRICE.getCode());
				model.setNewData(JSON.toJSONString(entity));
				model.setOldData("");
				model.setOperateDesc("作废耗材明细报价");
				model.setOperatePerson(JAppContext.currentUserName());
				model.setOperateTable("price_storage_material");
				model.setOperateTime(JAppContext.currentTimestamp());
				model.setOperateType(RecordLogOperateType.CANCEL.getCode());
				model.setRemark("");
				model.setOperateTableKey(entity.getId().toString());
				model.setUrlName(RecordLogUrlNameEnum.IN_STORAGE_MATERIAL_PRICE.getCode());
				pubRecordLogService.AddRecordLog(model);
			}catch(Exception e){
				logger.error("记录日志失败,失败原因:"+e.getMessage());
			}
			return "succ";
		}
		catch(Exception ex){
			//写入日志
			bmsErrorLogInfoService.insertLog(this.getClass().getSimpleName(),Thread.currentThread().getStackTrace()[1].getMethodName(), "", ex.toString());

			return "数据库操作失败:"+ex.getMessage();
		}
	}
	@FileProvider
	public DownloadFile getDispatchTemplate(Map<String, String> parameter) throws IOException {
		InputStream is = DoradoContext.getCurrent().getServletContext().getResourceAsStream("/WEB-INF/templates/storage/materialStorage_template.xlsx");
		return new DownloadFile("九曳耗材报价模板.xlsx", is);
		
	}
	
	@FileResolver
	public Map<String, Object> importDispatchTemplate(final UploadFile file, final Map<String, Object> parameter) throws Exception {
		final Map<String, Object> map = new HashMap<String, Object>();
		// 校验信息（报错提示）
		final List<ErrorMessageVo> infoList = new ArrayList<ErrorMessageVo>();
		
		//获取此时的商家id
		final String customerId=(String)parameter.get("customerId");//获取当前的id	
		final String customerName=(String)parameter.get("customerName");//获取当前的id	
		String lockString=getMd5("BMS_QUO_IMPORT_STORAGE_MATERIAL"+customerId);
		lock.lock(lockString, 300, new LockCallback<Map<String, Object>>() {
			@Override
			public Map<String, Object> handleObtainLock() {
				// TODO Auto-generated method stub
				try {
				   Map<String, Object> re=importMetraTemplate(file,parameter,infoList,map);
				   return re;
				} catch (Exception e) {
					//写入日志
					bmsErrorLogInfoService.insertLog(this.getClass().getSimpleName(),Thread.currentThread().getStackTrace()[1].getMethodName(), "", e.toString());

					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return null;
			}

			@Override
			public Map<String, Object> handleNotObtainLock() throws LockCantObtainException {
				// TODO Auto-generated method stub
				ErrorMessageVo errorVo = new ErrorMessageVo();
				errorVo.setMsg("商家【"+customerName+"】的报价导入功能已被其他用户占用，请稍后重试；");
				infoList.add(errorVo);
				map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
				return map;
			}

			@Override
			public Map<String, Object> handleException(LockInsideExecutedException e)
					throws LockInsideExecutedException {
				// TODO Auto-generated method stub
				ErrorMessageVo errorVo = new ErrorMessageVo();
				errorVo.setMsg("系统异常，请稍后重试!");
				infoList.add(errorVo);
				map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
				return map;
			}
		});
		return map;
		
	}
	
	@FileResolver
	public Map<String, Object> importMetraTemplate(UploadFile file, Map<String, Object> parameter,List<ErrorMessageVo> infoList,Map<String, Object> map) throws Exception {
		
		DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 10);
		long starTime=System.currentTimeMillis();	//开始时间
	
		//获取当前的id
		String id=(String)parameter.get("id");
						
		ErrorMessageVo errorVo = null;
		
		// 导入成功返回模板信息
		List<PriceMaterialQuotationEntity> templateList = new ArrayList<PriceMaterialQuotationEntity>();
		// 当期时间
		Timestamp currentTime = JAppContext.currentTimestamp();
		String userName = JAppContext.currentUserName();
		
		//仓库信息
		List<WarehouseVo>  wareHouselist  = warehouseService.queryAllWarehouse();
		
		try {
			BaseDataType bs=null;
			bs=new MaterialTemplateDataType();
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
			DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 50);
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
			
			String currentNo = SequenceGenerator.uuidOf36String("t");// 当前操作ID
			
			// 模板信息必填项校验
			map = impExcelCheckInfoProduct(infoList, templateList, map, currentNo,wareHouselist);
			
			if (map.get(ConstantInterface.ImportExcelStatus.IMP_ERROR) != null) { // 有基本的错误信息(必填，数据类型不正确)
				return map;
			}
			
			logger.info("必填项验证耗时："+FileOperationUtil.getOperationTime(starTime));
			DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 700);
			starTime=System.currentTimeMillis();	//开始时间
			
			
			//重复校验(包括Excel校验和数据库校验)
			Map<String, Object> parame=new HashMap<String, Object>();
			parame.put("templateId", id);
			List<PriceMaterialQuotationEntity> orgList=getOrgList(parame);
			List<PriceMaterialQuotationEntity> teList=templateList;
			
			//非泡沫箱
			List<PriceMaterialQuotationEntity> noPmxList = new ArrayList<PriceMaterialQuotationEntity>();
			for (PriceMaterialQuotationEntity teEntity : teList) {
				if (!"PLATIC_BOX".equals(teEntity.getMaterialType())) {
					noPmxList.add(teEntity);
				}
			}
			//非泡沫箱走这面这段代码(校验Excel和DB是否重复)，泡沫箱不需要校验
			Map<String,Object> mapCheck=null;
			if (noPmxList.size() > 0) {
				mapCheck=super.compareWithImportLineData(orgList, noPmxList, infoList,getKeyDataProperty(), map);
			}else {
				mapCheck = new HashMap<String,Object>();
			}
			if (mapCheck.get(ConstantInterface.ImportExcelStatus.IMP_ERROR) != null) { // 有基本的错误信息(必填，数据类型不正确)
				return map;
			}			
			
			//设置属性
			for(int i=0;i<templateList.size();i++){
				PriceMaterialQuotationEntity p=templateList.get(i);
				p.setTemplateId(id);
				p.setDelFlag("0");// 设置为未作废
				p.setCreator(userName);
				p.setCreateTime(currentTime);
				p.setLastModifier(userName);
				p.setLastModifyTime(currentTime);
			}
			//插入正式表
			int insertNum = 0;
			try {
				insertNum = service.insertBatchTmp(templateList);
			} catch (Exception e) {
				//写入日志
				bmsErrorLogInfoService.insertLog(this.getClass().getSimpleName(),Thread.currentThread().getStackTrace()[1].getMethodName(), "", e.toString());

				if((e.getMessage().indexOf("Duplicate entry"))>0){
					errorVo = new ErrorMessageVo();
					errorVo.setMsg("违反唯一性约束,插入数据失败!");
					infoList.add(errorVo);
					map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
					return map;
				}
				
			}
			if (insertNum <= 0) {
				errorVo = new ErrorMessageVo();
				errorVo.setMsg("插入数据失败!");
				infoList.add(errorVo);
				map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
				return map;
			}else{
				map.put(ConstantInterface.ImportExcelStatus.IMP_SUCC, "0");
				
				logger.info("写入表耗时："+FileOperationUtil.getOperationTime(starTime));
				DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 1000);
				try{
					PubRecordLogEntity model=new PubRecordLogEntity();
					model.setBizType(RecordLogBizTypeEnum.PRICE.getCode());
					model.setNewData("");
					model.setOldData("");
					model.setOperateDesc("导入耗材报价,共计【"+templateList.size()+"】条数据");
					model.setOperatePerson(JAppContext.currentUserName());
					model.setOperateTable("price_storage_material");
					model.setOperateTime(JAppContext.currentTimestamp());
					model.setOperateType(RecordLogOperateType.IMPORT.getCode());
					model.setRemark("");
					model.setOperateTableKey("");
					model.setUrlName(RecordLogUrlNameEnum.IN_STORAGE_MATERIAL_PRICE.getCode());
					pubRecordLogService.AddRecordLog(model);
				}catch(Exception e){
					logger.error("记录日志失败,失败原因:"+e.getMessage());
				}
				return map;
			}
			
			
			
		} catch (Exception e) {
			
			errorVo = new ErrorMessageVo();
			errorVo.setMsg("程序异常--:"+e.getMessage());
			infoList.add(errorVo);
			map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
			//写入日志
			bmsErrorLogInfoService.insertLog(this.getClass().getSimpleName(),Thread.currentThread().getStackTrace()[1].getMethodName(), "", e.toString());

		}
		

		return map;
	}
	
	private List<PriceMaterialQuotationEntity> readExcelProduct(UploadFile file,BaseDataType bs) {
		String fileSuffix = StringUtils.substringAfterLast(file.getFileName(), ".");
		IFileReader reader = FileReaderFactory.getFileReader(fileSuffix);
		try {
			List<Map<String, String>> list = reader.getFileContent(file.getInputStream());
			List<Map<String, String>> datas = Lists.newArrayList();
		
			List<DataProperty> props = bs.getDataProps();
			for (Map<String, String> map : list) {
				Map<String, String> data = Maps.newHashMap();
				for (DataProperty prop : props) {
					
					data.put(prop.getPropertyId(), map.get(prop.getPropertyName().toLowerCase()));
				}
				datas.add(data);
			}
			List<PriceMaterialQuotationEntity> productList = Lists.newArrayList();
			for (Map<String, String> data : datas) {
				PriceMaterialQuotationEntity p = (PriceMaterialQuotationEntity) BeanToMapUtil.convertMapNull(PriceMaterialQuotationEntity.class, data);
				String materialCode = p.getMaterialCode();
				String warehouseId = p.getWarehouseId();
				if(StringUtils.isNotEmpty(materialCode)){
					p.setMaterialCode(materialCode.trim());
				}
				if(StringUtils.isNotEmpty(warehouseId)){
					p.setWarehouseId(warehouseId.trim());
				}
				productList.add(p);
			}
			return productList;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@DataResolver
	public String copyTemplate(GenericTemplateEntity entity){
		try{
			if(Session.isMissing()){
				return "长时间未操作，用户已失效，请重新登录再试！";
			}else if(entity == null){
				return "页面传递参数有误！";
			}
			
			GenericTemplateEntity newEntity = new GenericTemplateEntity();
			//先复制主模板
			
			String templateNo =sequenceService.getBillNoOne(GenericTemplateEntity.class.getName(), "MB", "00000");
			
			newEntity.setTemplateCode(templateNo);
			newEntity.setBizTypeCode("STORAGE");
			newEntity.setSubjectId(entity.getSubjectId());
			newEntity.setRemark(entity.getRemark());
			newEntity.setStorageTemplateType("STORAGE_MATERIAL_PRICE_TEMPLATE");
			newEntity.setTemplateName(entity.getTemplateName());
			newEntity.setCustomerId(entity.getCustomerId());
			newEntity.setCustomerName(entity.getCustomerName());
			newEntity.setQuotationNo(entity.getQuotationNo());
			//newEntity.setQuotationName(entity.getQuotationName());
			newEntity.setTemplateType(entity.getTemplateType());
			newEntity.setCreateTime(JAppContext.currentTimestamp());
			newEntity.setCreator(JAppContext.currentUserName());
			newEntity.setLastModifier(JAppContext.currentUserName());
			newEntity.setLastModifyTime(JAppContext.currentTimestamp());
			newEntity.setDelFlag("0");
			
			genericTemplateService.save(newEntity); 
			
			Map<String, Object> condition   = new HashMap<String,Object>();
			condition.put("templateCode", templateNo);
			PageInfo<GenericTemplateEntity>  list1 = genericTemplateService.query(condition, 1,1);
			
			Long templateId = list1.getList().get(0).getId();
			
			Map<String, Object> params = new HashMap<String,Object>();
			params.put("templateId",entity.getId());
			
			PageInfo<PriceMaterialQuotationEntity> page = service.query(params, 0, Integer.MAX_VALUE);
			
			if(null!=page){
				List<PriceMaterialQuotationEntity> list = page.getList();
				
				for(PriceMaterialQuotationEntity vo:list){
					vo.setId(null);
					vo.setTemplateId(templateId.toString());
				}
				
				service.insertBatchTmp(list);
			}
 			
			return "SUCCESS";
		}
		catch(Exception ex){
			return "数据库操作失败";
		}
		
	}
	
	
	private Map<String, Object> impExcelCheckInfoProduct(List<ErrorMessageVo> infoList, List<PriceMaterialQuotationEntity> prodList, Map<String, Object> map, String currentNo,List<WarehouseVo>  wareHouselist) {
		int lineNo = 1;
		Map<String, String> mapValue = getType();
		for (int i = 0; i < prodList.size(); i++) {
			PriceMaterialQuotationEntity p=prodList.get(i);
			lineNo=lineNo+1;
			
			if(null==p.getUnitPrice()){
				setMessage(infoList, lineNo,"单价为空!");
			}
			
			if(StringUtils.isEmpty(p.getMaterialCode())){
				setMessage(infoList, lineNo,"材料编码为空!");
			}
			
			if(StringUtils.isEmpty(p.getMaterialType())){
				setMessage(infoList, lineNo,"材料类型为空!");
			}
			
			boolean warehouseCheck = false;
			//如果仓库为空并且耗材类型不为泡沫箱，不允许重复录入。不为空需要校验
			if(StringUtils.isEmpty(p.getWarehouseId()) && !"泡沫箱".equals(p.getMaterialType())){
				for (WarehouseVo warehouseVo : wareHouselist) {
					if (warehouseVo.getWarehousename().equals(p.getWarehouseId())) {
						setMessage(infoList, lineNo, "未填写仓库的耗材不允许重复录入!");
						break;
					}
				}
			}else {
				for(WarehouseVo entity:wareHouselist)
				{
					if(entity.getWarehousename().equals(p.getWarehouseId()))
					{
						p.setWarehouseId(entity.getWarehouseid());
						warehouseCheck = true;
						break;
					}
				}
				
				if(!warehouseCheck){
					setMessage(infoList, lineNo,"仓库信息不对!");
				}
			}
		
			boolean materailTypeCheck = false;
			
			if(!StringUtils.isEmpty(p.getMaterialType())&&mapValue.containsValue(p.getMaterialType()))
				materailTypeCheck = true;
			
			if(!materailTypeCheck&&!StringUtils.isEmpty(p.getMaterialType())){
				setMessage(infoList, lineNo,"耗材类型信息不对!");
			}
			
            for(String key:mapValue.keySet())
            {
            	if(!StringUtils.isEmpty(p.getMaterialType())&&p.getMaterialType().equals(mapValue.get(key)))
            	{
            		p.setMaterialType(key);
            	}
            }
				
			
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
	
	@FileProvider
	public DownloadFile exportStorageMaterial(Map<String, Object> parameter){
				
		PageInfo<PriceMaterialQuotationEntity> pageInfo = service.query(parameter, 1,20000);
		
		List<PriceMaterialQuotationEntity>  dataList = pageInfo.getList();
		
		List<Map<String, Object>> headMapList=getFeeHead();
		
		List<Map<String, Object>> dataMapList = new ArrayList<Map<String,Object>>();
		
		//仓库信息
		List<WarehouseVo>  wareHouselist  = warehouseService.queryAllWarehouse();
		
		Map<String,Object> wareHouseMap = new HashMap<String,Object>();
		
		for(WarehouseVo vo:wareHouselist){
			wareHouseMap.put(vo.getWarehouseid(),vo.getWarehousename());
		}
		
		Map<String, String> mapValue = getType();
		
		Map<String,Object> map = null;
		
		for(PriceMaterialQuotationEntity entity:dataList){
			
			map = new HashMap<String,Object>();
			
			map.put("materialCode", entity.getMaterialCode());
			map.put("materialType", mapValue.get(entity.getMaterialType())==null?entity.getMaterialType():mapValue.get(entity.getMaterialType()));
			map.put("specName", entity.getSpecName());
			map.put("outsideDiameter", entity.getOutsideDiameter());
			map.put("innerDiameter", entity.getInnerDiameter());
			map.put("wallThickness", entity.getWallThickness());
			map.put("warehouseId", wareHouseMap.get(entity.getWarehouseId()));
			map.put("unitPrice", entity.getUnitPrice());
			map.put("remark", entity.getRemark());
			
			dataMapList.add(map);
			
		}
		
		POIUtil poiUtil = new POIUtil();
		
		HSSFWorkbook hssfWorkbook = poiUtil.getHSSFWorkbook();
		String path = getPath();
		String temp = SequenceGenerator.uuidOf36String("exp");
				
		
		try {
			
			poiUtil.exportExcelFilePath(poiUtil, hssfWorkbook, "报价耗材", path+File.separator+"bmsMaterial_"+temp+".xls", headMapList, dataMapList);
					
		} catch (IOException e) {
			logger.error(e.getMessage());
			//写入日志
			bmsErrorLogInfoService.insertLog(this.getClass().getSimpleName(),Thread.currentThread().getStackTrace()[1].getMethodName(), "", e.toString());

		}
		
		InputStream is = null;
		  
		try {
			is = new FileInputStream(path+File.separator+"bmsMaterial_"+temp+".xls");
		} catch (FileNotFoundException e) {
			logger.error(e.getMessage());
			//写入日志
			bmsErrorLogInfoService.insertLog(this.getClass().getSimpleName(),Thread.currentThread().getStackTrace()[1].getMethodName(), "", e.toString());

		}
		
		return new DownloadFile("报价耗材"+temp+".xls", is);
		
		
	}
	
	@SuppressWarnings("deprecation")
	private String getPath(){
		SystemCodeEntity systemCodeEntity = systemCodeService.getSystemCode("GLOABL_PARAM", "EXPORT_PATH_STORAGE_FEES");
		if(systemCodeEntity == null){
			throw new BizException("请在系统参数中配置文件上传路径,参数GLOABL_PARAM,EXPORT_PATH_STORAGE_FEES");
		}
		return systemCodeEntity.getExtattr1();
	}
	
	public List<Map<String,Object>> getFeeHead(){
		List<Map<String, Object>> headMapList=new ArrayList<Map<String, Object>>();
		Map<String, Object> itemMap = new HashMap<String, Object>();
		itemMap.put("title", "耗材编码");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "materialCode");
        headMapList.add(itemMap);
        itemMap = new HashMap<String, Object>();
    	itemMap.put("title", "耗材类型");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "materialType");
        headMapList.add(itemMap);
        itemMap = new HashMap<String, Object>();
    	itemMap.put("title", "规格名称");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "specName");
        headMapList.add(itemMap);
        itemMap = new HashMap<String, Object>();
    	itemMap.put("title", "外径");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "outsideDiameter");
        headMapList.add(itemMap);
        itemMap = new HashMap<String, Object>();
    	itemMap.put("title", "内径");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "innerDiameter");
        headMapList.add(itemMap);
        itemMap = new HashMap<String, Object>();
    	itemMap.put("title", "壁厚");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "wallThickness");
        headMapList.add(itemMap);
        itemMap = new HashMap<String, Object>();
    	itemMap.put("title", "仓库名称");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "warehouseId");
        headMapList.add(itemMap);
        itemMap = new HashMap<String, Object>();
    	itemMap.put("title", "单价");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "unitPrice");
        headMapList.add(itemMap);
        itemMap = new HashMap<String, Object>();
    	itemMap.put("title", "备注");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "remark");
        headMapList.add(itemMap);
        return headMapList;
	}
	
	private Map<String, String>  getType(){
		
		/*Map<String, String> mapValue = new LinkedHashMap<String, String>();
		
		List<SystemCodeEntity> tmscodels = systemCodeService.findEnumList(typeCode);
		
		
		for (SystemCodeEntity SystemCodeEntity : tmscodels) {
			
			mapValue.put(SystemCodeEntity.getCode(), SystemCodeEntity.getCodeName());
		}
		
		return mapValue;*/
		Map<String, String> mapValue = new LinkedHashMap<String, String>();
		List<SystemCodeVo> codeList = pubMaterialInfoService.findEnumList("");
		for (SystemCodeVo SystemCodeVo : codeList) {
			
			mapValue.put(SystemCodeVo.getCode(), SystemCodeVo.getCodeName());
		}		
		return mapValue;

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

	public List<PriceMaterialQuotationEntity> getOrgList(
			Map<String, Object> parameter) {
		List<PriceMaterialQuotationEntity> list=service.queryById(parameter);
		return list;
	}

	public List<String> getKeyDataProperty() {
		// TODO Auto-generated method stub
		List<String> list=new ArrayList<String>();
		list.add("materialCode");//耗材编码
		list.add("materialType");//耗材类型
		list.add("warehouseId");//仓库
		return list;
	}
	
	private String getMd5(String str){
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			Base64Encoder base64en = new Base64Encoder();
			String newstr = base64en.encode(md5.digest(str.getBytes("utf-8")));
			return newstr;
		} catch (Exception e) {
			//写入日志
			bmsErrorLogInfoService.insertLog(this.getClass().getSimpleName(),Thread.currentThread().getStackTrace()[1].getMethodName(), "", e.toString());
			e.printStackTrace();
			return null;
		}
	}
	


}
 