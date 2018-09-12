package com.jiuyescm.bms.quotation.storage.web;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.alibaba.fastjson.JSON;
import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.annotation.Expose;
import com.bstek.dorado.data.provider.Page;
import com.bstek.dorado.uploader.UploadFile;
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
import com.jiuyescm.bms.common.log.service.IBmsErrorLogInfoService;
import com.jiuyescm.bms.common.sequence.service.SequenceService;
import com.jiuyescm.bms.common.tool.Session;
import com.jiuyescm.bms.pub.IPubRecordLogService;
import com.jiuyescm.bms.pub.PubRecordLogEntity;
import com.jiuyescm.bms.quotation.storage.entity.PriceGeneralQuotationEntity;
import com.jiuyescm.bms.quotation.storage.entity.PriceStepQuotationEntity;
import com.jiuyescm.bms.quotation.storage.service.IPriceGeneralQuotationService;
import com.jiuyescm.bms.quotation.storage.service.IPriceStepQuotationService;
import com.jiuyescm.cfm.common.JAppContext;
import com.jiuyescm.cfm.common.sequence.SequenceGenerator;
import com.jiuyescm.common.ConstantInterface;
import com.jiuyescm.common.utils.BeanToMapUtil;
import com.jiuyescm.common.utils.DoubleUtil;
import com.jiuyescm.common.utils.FileOperationUtil;
import com.jiuyescm.common.utils.excel.FileReaderFactory;
import com.jiuyescm.common.utils.excel.IFileReader;
import com.jiuyescm.common.utils.upload.BaseDataType;
import com.jiuyescm.common.utils.upload.DataProperty;
import com.jiuyescm.common.utils.upload.StorageStepTemplateDataType;
import com.jiuyescm.mdm.warehouse.api.IWarehouseService;
import com.jiuyescm.mdm.warehouse.vo.WarehouseVo;

@Controller("stroageStepQuoteController")
public class StroageStepQuoteController {
	
	private static final Logger logger = Logger.getLogger(StroageStepQuoteController.class.getName());
	
	
	@Resource private IPriceStepQuotationService  service;	
	@Resource private SequenceService sequenceService;	
	@Resource private IPriceGeneralQuotationService priceGeneralQuotationService;
	@Resource private ISystemCodeService systemCodeService;
	
	@Resource
	private IBmsErrorLogInfoService bmsErrorLogInfoService;
	
	@Resource
	private IPubRecordLogService pubRecordLogService;
	
	@Autowired
	private IWarehouseService warehouseService;
	
	@DataProvider
	public void query(Page<PriceStepQuotationEntity> page, Map<String, Object> param) {
		PageInfo<PriceStepQuotationEntity> pageInfo = service.query(param, page.getPageNo(), page.getPageSize());
		if (pageInfo != null) {
			page.setEntities(pageInfo.getList());
			page.setEntityCount((int) pageInfo.getTotal());
		}
	}

	@DataResolver
	public String save(PriceStepQuotationEntity entity){
		try{
			if(Session.isMissing()){
				return "长时间未操作，用户已失效，请重新登录再试！";
			}else if(entity == null){
				return "页面传递参数有误！";
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
					model.setOperateDesc("新增阶梯报价,报价模板编号:【"+entity.getQuotationId()+"】");
					model.setOperatePerson(JAppContext.currentUserName());
					model.setOperateTable("price_general_quotation");
					model.setOperateTime(JAppContext.currentTimestamp());
					model.setOperateType(RecordLogOperateType.INSERT.getCode());
					model.setRemark("");
					model.setOperateTableKey(entity.getQuotationId());
					model.setUrlName("");
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
	public String update(PriceStepQuotationEntity entity){
		try{
			if(Session.isMissing()){
				return "长时间未操作，用户已失效，请重新登录再试！";
			}else if(entity == null){
				return "页面传递参数有误！";
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
					model.setOperateDesc("更新阶梯报价,报价编号:【"+entity.getId().toString()+"】");
					model.setOperatePerson(JAppContext.currentUserName());
					model.setOperateTable("price_general_quotation");
					model.setOperateTime(JAppContext.currentTimestamp());
					model.setOperateType(RecordLogOperateType.UPDATE.getCode());
					model.setRemark("");
					model.setOperateTableKey(entity.getId().toString());
					model.setUrlName("");
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
	public String delete(PriceStepQuotationEntity entity){
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
				i =	service.update(entity);
			
			if(i>0){
				try{
					PubRecordLogEntity model=new PubRecordLogEntity();
					model.setBizType(RecordLogBizTypeEnum.PRICE.getCode());
					model.setNewData("");
					model.setOldData(JSON.toJSONString(entity));
					model.setOperateDesc("删除阶梯报价,报价编号:【"+entity.getId().toString()+"】");
					model.setOperatePerson(JAppContext.currentUserName());
					model.setOperateTable("price_general_quotation");
					model.setOperateTime(JAppContext.currentTimestamp());
					model.setOperateType(RecordLogOperateType.CANCEL.getCode());
					model.setRemark("");
					model.setOperateTableKey(entity.getId().toString());
					model.setUrlName("");
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
	public String remove(PriceGeneralQuotationEntity entity){
		try{
			Map<String,Object> map=new HashMap<String,Object>();
			map.put("templateId", entity.getId().toString());
			map.put("lastModifier", JAppContext.currentUserName());
			map.put("lastModifyTime", JAppContext.currentTimestamp());
			service.remove(map);
			try{
				PubRecordLogEntity model=new PubRecordLogEntity();
				model.setBizType(RecordLogBizTypeEnum.PRICE.getCode());
				model.setNewData("");
				model.setOldData(JSON.toJSONString(entity));
				model.setOperateDesc("删除报价,报价编号:【"+entity.getId()+"】");
				model.setOperatePerson(JAppContext.currentUserName());
				model.setOperateTable("price_general_quotation");
				model.setOperateTime(JAppContext.currentTimestamp());
				model.setOperateType(RecordLogOperateType.DELETE.getCode());
				model.setRemark("");
				model.setOperateTableKey(entity.getId().toString());
				model.setUrlName("");
				pubRecordLogService.AddRecordLog(model);
			}catch(Exception e){
				logger.error("记录日志失败,失败原因:"+e.getMessage());
			}
			return "succ";
		}catch(Exception e){
			//写入日志
			bmsErrorLogInfoService.insertLog(this.getClass().getSimpleName(),Thread.currentThread().getStackTrace()[1].getMethodName(), "", e.toString());

			return e.getMessage();
		}
	}
	@DataResolver
	public String removeAll(PriceGeneralQuotationEntity entity){
		try{
			Map<String,Object> map=new HashMap<String,Object>();
			map.put("templateId", entity.getId().toString());
			map.put("lastModifier", JAppContext.currentUserName());
			map.put("lastModifyTime", JAppContext.currentTimestamp());
			service.removeAll(map);
			try{
				PubRecordLogEntity model=new PubRecordLogEntity();
				model.setBizType(RecordLogBizTypeEnum.PRICE.getCode());
				model.setNewData("");
				model.setOldData(JSON.toJSONString(entity));
				model.setOperateDesc("作废报价,报价编号:【"+entity.getId()+"】");
				model.setOperatePerson(JAppContext.currentUserName());
				model.setOperateTable("price_general_quotation");
				model.setOperateTime(JAppContext.currentTimestamp());
				model.setOperateType(RecordLogOperateType.DELETE.getCode());
				model.setRemark("");
				model.setOperateTableKey(entity.getId().toString());
				model.setUrlName("");
				pubRecordLogService.AddRecordLog(model);
			}catch(Exception e){
				logger.error("记录日志失败,失败原因:"+e.getMessage());
			}
			return "succ";
		}catch(Exception e){
			//写入日志
			bmsErrorLogInfoService.insertLog(this.getClass().getSimpleName(),Thread.currentThread().getStackTrace()[1].getMethodName(), "", e.toString());

			return e.getMessage();
		}
	}
	@FileResolver
	public Map<String, Object> importDispatchTemplate(UploadFile file, Map<String, Object> parameter) throws Exception {
		
		DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 10);
		long starTime=System.currentTimeMillis();	//开始时间
		Map<String, Object> map = new HashMap<String, Object>();
		String id=(String)parameter.get("id");//获取当前的id
		String unit=(String)parameter.get("unit");//获取当前的计费单位
		List<ErrorMessageVo> infoList = new ArrayList<ErrorMessageVo>();// 校验信息（报错提示）
		ErrorMessageVo errorVo = null;
		PriceGeneralQuotationEntity generEn = priceGeneralQuotationService.findById(Long.parseLong(id));
		
		if(null!=generEn&&"PRICE_TYPE_NORMAL".equals(generEn.getPriceType())){
			DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 1000);
        	errorVo = new ErrorMessageVo();
            errorVo.setMsg("当前登录信息异常（丢失），请重新登录（刷新页面）");
            infoList.add(errorVo);
            map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
            return map;
		}
		logger.info("验证报价耗时："+FileOperationUtil.getOperationTime(starTime));
		DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 50);
		starTime=System.currentTimeMillis();	//开始时间
				
		// 导入成功返回模板信息
		List<PriceStepQuotationEntity> templateList = new ArrayList<PriceStepQuotationEntity>();
		// 当期时间
		Timestamp currentTime = JAppContext.currentTimestamp();
		String userName = JAppContext.currentUserName();
		try {
			BaseDataType bs=new StorageStepTemplateDataType();
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
			
			List<SystemCodeEntity> sysCodeList = systemCodeService.findEnumList("TEMPERATURE_TYPE");
			String currentNo = SequenceGenerator.uuidOf36String("t");// 当前操作ID
			// 模板信息必填项校验
			map = impExcelCheckInfoProduct(infoList, templateList, map, currentNo,sysCodeList,unit);
			if (map.get(ConstantInterface.ImportExcelStatus.IMP_ERROR) != null) { // 有基本的错误信息(必填，数据类型不正确)
				return map;
			}
			logger.info("必填项验证耗时："+FileOperationUtil.getOperationTime(starTime));
			DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 700);
			starTime=System.currentTimeMillis();	//开始时间
				
			//设置属性
			for(int i=0;i<templateList.size();i++){
				PriceStepQuotationEntity p=templateList.get(i);
				p.setQuotationId(id);
				p.setDelFlag("0");// 设置为未作废
				p.setCreator(userName);
				p.setCreateTime(currentTime);
				p.setLastModifier(userName);
				p.setLastModifyTime(currentTime);
			}
			//插入正式表
			int insertNum = service.insertBatchTmp(templateList);
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
			bmsErrorLogInfoService.insertLog(this.getClass().getSimpleName(),Thread.currentThread().getStackTrace()[1].getMethodName(), "", e.toString());

		}
		return map;
	}
	
	@FileResolver
	public Map<String, Object> importDispatchTemplate1(UploadFile file, Map<String, Object> parameter) throws Exception {
		
		Map<String, Object> map = new HashMap<String, Object>();
		String id=(String)parameter.get("id");//获取当前的id
		String unit=(String)parameter.get("unit");//获取当前的id
		List<ErrorMessageVo> infoList = new ArrayList<ErrorMessageVo>();// 校验信息（报错提示）
		ErrorMessageVo errorVo = null;
		PriceGeneralQuotationEntity generEn = priceGeneralQuotationService.findById(Long.parseLong(id));
		
		if(null!=generEn&&"PRICE_TYPE_NORMAL".equals(generEn.getPriceType())){
			map.put("validateData", "0");
			return map;
		}
				
		// 导入成功返回模板信息
		List<PriceStepQuotationEntity> templateList = new ArrayList<PriceStepQuotationEntity>();
		// 当期时间
		Timestamp currentTime = JAppContext.currentTimestamp();
		String userName = JAppContext.currentUserName();
		try {
			BaseDataType bs=null;
			bs=new StorageStepTemplateDataType();
			// 检查导入模板是否正确
			boolean isTemplate = FileOperationUtil.checkExcelTitle(file, bs);
			if (!isTemplate) {
				errorVo = new ErrorMessageVo();
				errorVo.setMsg("Excel导入格式错误请参考标准模板检查!");
				infoList.add(errorVo);
				map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
				return map;
			}
			
			// 解析Excel
			templateList = readExcelProduct(file,bs);
			
			if (null == templateList || templateList.size() <= 0) {
				errorVo = new ErrorMessageVo();
				errorVo.setMsg("导入的Excel数据为空或者数据格式不对!");
				infoList.add(errorVo);
				map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
				return map;
			}
			
			List<SystemCodeEntity> sysCodeList = systemCodeService.findEnumList("TEMPERATURE_TYPE");
			
			String currentNo = SequenceGenerator.uuidOf36String("t");// 当前操作ID
			
			// 模板信息必填项校验
			map = impExcelCheckInfoProduct(infoList, templateList, map, currentNo,sysCodeList,unit);
			
			if (map.get(ConstantInterface.ImportExcelStatus.IMP_ERROR) != null) { // 有基本的错误信息(必填，数据类型不正确)
				return map;
			}
			//设置属性
			for(int i=0;i<templateList.size();i++){
				PriceStepQuotationEntity p=templateList.get(i);
				p.setQuotationId(id);
				p.setDelFlag("0");// 设置为未作废
				p.setCreator(userName);
				p.setCreateTime(currentTime);
				p.setLastModifier(userName);
				p.setLastModifyTime(currentTime);
			}
			//插入正式表
			int insertNum = service.insertBatchTmp(templateList);
			if (insertNum <= 0) {
				errorVo = new ErrorMessageVo();
				errorVo.setMsg("插入正式表失败!");
				infoList.add(errorVo);
				map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
				return map;
			}else{
				map.put(ConstantInterface.ImportExcelStatus.IMP_SUCC, "0");
				return map;
			}
			
		} catch (Exception e) {
			//写入日志
			bmsErrorLogInfoService.insertLog(this.getClass().getSimpleName(),Thread.currentThread().getStackTrace()[1].getMethodName(), "", e.toString());
			e.printStackTrace();
		}
		return map;
	}
	
	private List<PriceStepQuotationEntity> readExcelProduct(UploadFile file,BaseDataType bs) {
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
			List<PriceStepQuotationEntity> productList = Lists.newArrayList();
			for (Map<String, String> data : datas) {
				PriceStepQuotationEntity p = (PriceStepQuotationEntity) BeanToMapUtil.convertMapNull(PriceStepQuotationEntity.class, data);
				productList.add(p);
			}
			return productList;
		} catch (Exception e) {
			//写入日志
			bmsErrorLogInfoService.insertLog(this.getClass().getSimpleName(),Thread.currentThread().getStackTrace()[1].getMethodName(), "", e.toString());

			e.printStackTrace();
		}
		return null;
	}
	
	private Map<String, Object> impExcelCheckInfoProduct(List<ErrorMessageVo> infoList, List<PriceStepQuotationEntity> prodList, Map<String, Object> map, String currentNo,List<SystemCodeEntity> sysCodeList,String unit) {
		List<WarehouseVo>  wareHouselist  = warehouseService.queryAllWarehouse();	
		
		int lineNo = 0;
		for (int i = 0; i < prodList.size(); i++) {
			PriceStepQuotationEntity p=prodList.get(i);
			lineNo=lineNo+1;
			
			if(StringUtils.isNotBlank(p.getWarehouseCode())){
				//判断仓库id是否在仓库表中维护 并将此仓库返回,将id回填			
				for(WarehouseVo entity:wareHouselist){
					if(entity.getWarehousename().equals(p.getWarehouseCode())){
						p.setWarehouseCode(entity.getWarehouseid());
						break;
					}
				}
			}
			
			if(p.getNumLower()==null){
				setMessage(infoList, lineNo,"下限不能为空");
				continue;
			}

			if(p.getNumUpper()==null){
				setMessage(infoList, lineNo,"上限不能为空");
				continue;
			}						
			
			if(p.getNumUpper().doubleValue()<=p.getNumLower().doubleValue()){
				setMessage(infoList, lineNo,"上限必须大于下限");
			}

			//模板中单位为单时，单价必填
			if(StringUtils.isNotBlank(unit)){
				if("BILL".equals(unit)){
					if(DoubleUtil.isBlank(p.getUnitPrice())){
						setMessage(infoList, lineNo,"模板单位为单时，单价不能为空");
					}
				}else{
					//其他单位    
					if(DoubleUtil.isBlank(p.getUnitPrice())){
						if(DoubleUtil.isBlank(p.getFirstNum())|| DoubleUtil.isBlank(p.getFirstPrice()) ||DoubleUtil.isBlank(p.getContinuedItem()) || DoubleUtil.isBlank(p.getContinuedPrice())){
							setMessage(infoList, lineNo,"单价为空时，首量、首价、续量、续价不能为空!");
						}
					}
				}
			}
	
			//温度类型判断
			if(StringUtils.isEmpty(p.getTemperatureTypeCode())){
				setMessage(infoList, lineNo,"温度类型为空!");
			}
			
			boolean temperatureCheck = false;
			
			for(SystemCodeEntity entity:sysCodeList)
			{
				if(entity.getCodeName().equals(p.getTemperatureTypeCode()))
				{
					p.setTemperatureTypeCode(entity.getCode());
					temperatureCheck = true;
					break;
				}
			}
			
			if(!temperatureCheck){
				setMessage(infoList, lineNo,"系统没有维护该温度类型!");
			}
			
		}
		
		if (infoList != null && infoList.size() > 0) { // 有错误信息
			map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
		} else {
			map.put(ConstantInterface.ImportExcelStatus.IMP_SUCC, prodList); // 无基本错误信息
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

}
 