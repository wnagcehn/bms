package com.jiuyescm.bms.quotation.storage.web;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
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
import com.jiuyescm.bms.pub.IPubRecordLogService;
import com.jiuyescm.bms.pub.PubRecordLogEntity;
import com.jiuyescm.bms.quotation.storage.entity.PriceExtraQuotationEntity;
import com.jiuyescm.bms.quotation.storage.service.IPriceExtraQuotationService;
import com.jiuyescm.bms.quotation.transport.entity.GenericTemplateEntity;
import com.jiuyescm.bms.quotation.transport.service.IGenericTemplateService;
import com.jiuyescm.cfm.common.JAppContext;
import com.jiuyescm.cfm.common.sequence.SequenceGenerator;
import com.jiuyescm.common.ConstantInterface;
import com.jiuyescm.common.utils.BeanToMapUtil;
import com.jiuyescm.common.utils.FileOperationUtil;
import com.jiuyescm.common.utils.excel.FileReaderFactory;
import com.jiuyescm.common.utils.excel.IFileReader;
import com.jiuyescm.common.utils.upload.BaseDataType;
import com.jiuyescm.common.utils.upload.DataProperty;
import com.jiuyescm.common.utils.upload.ExtraStorageTemplateDataType;

@Controller("stroageExtraQuoteController")
public class StroageExtraQuoteController {
	
	private static final Logger logger = Logger.getLogger(StroageExtraQuoteController.class);
	
	@Resource
	private IPriceExtraQuotationService  service;
	
	@Resource 
	private SequenceService sequenceService;
	
	@Resource
	private IGenericTemplateService genericTemplateService;
	
	@Resource
	private ISystemCodeService systemCodeService;
	
	@Resource
	private IBmsErrorLogInfoService bmsErrorLogInfoService;
	
	@Resource
	private IPubRecordLogService pubRecordLogService;
	
	@DataProvider
	public void query(Page<PriceExtraQuotationEntity> page, Map<String, Object> param) {
		PageInfo<PriceExtraQuotationEntity> pageInfo = service.query(param, page.getPageNo(), page.getPageSize());
		if (pageInfo != null) {
			page.setEntities(pageInfo.getList());
			page.setEntityCount((int) pageInfo.getTotal());
		}
	}

	@DataResolver
	public String save(PriceExtraQuotationEntity entity){
		try{
			if(Session.isMissing()){
				return "长时间未操作，用户已失效，请重新登录再试！";
			}else if(entity == null){
				return "页面传递参数有误！";
			}
			
			
			//一口价：PRICE_TYPE_NORMAL 阶梯价： PRICE_TYPE_STEP
			//新增判断限制，同一个费用科目，不能同时存在阶梯价和一口价
			Map<String, Object> param=new HashMap<String, Object>();
			param.put("templateId", entity.getTemplateId());
			param.put("subjectId", entity.getSubjectId());	
			if("PRICE_TYPE_STEP".equals(entity.getPriceType())){
				param.put("priceType", entity.getPriceType());
			}		
			List<PriceExtraQuotationEntity> pList=service.queryPriceByParam(param);
			if(pList.size()>0){
				return "同一个费用科目，只能存在一条报价";
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
					model.setOperateDesc("新增增值报价,模板编码【"+entity.getTemplateId()+"】");
					model.setOperatePerson(JAppContext.currentUserName());
					model.setOperateTable("price_storage_extra");
					model.setOperateTime(JAppContext.currentTimestamp());
					model.setOperateType(RecordLogOperateType.INSERT.getCode());
					model.setRemark("");
					model.setOperateTableKey(entity.getTemplateId());
					model.setUrlName(RecordLogUrlNameEnum.IN_STORAGE_OTHER_PRICE.getCode());
					pubRecordLogService.AddRecordLog(model);
				}catch(Exception e){
					logger.error("记录日志失败,失败原因:", e);
				}
				return "SUCCESS";
			}else{
				return "保存报价失败";
			}
			
		}
		catch(Exception ex){
			//写入日志
			bmsErrorLogInfoService.insertLog(this.getClass().getSimpleName(),Thread.currentThread().getStackTrace()[1].getMethodName(), "", ex.toString());
			logger.error("异常:", ex);
			return "数据库操作失败";
		}
	}
	
	@DataResolver
	public String update(PriceExtraQuotationEntity entity){
		try{
			if(Session.isMissing()){
				return "长时间未操作，用户已失效，请重新登录再试！";
			}else if(entity == null){
				return "页面传递参数有误！";
			}
			
			//新增判断限制，同一个费用科目，不能同时存在阶梯价和一口价
			Map<String, Object> param=new HashMap<String, Object>();
			param.put("templateId", entity.getTemplateId());
			param.put("subjectId", entity.getSubjectId());
			if("PRICE_TYPE_STEP".equals(entity.getPriceType())){
				param.put("priceType", entity.getPriceType());
			}	
			List<PriceExtraQuotationEntity> pList=service.queryPriceByParam(param);
			if(pList.size()>0){
				if(pList.size()==1 && pList.get(0).getId().equals(entity.getId())){
					
				}else{
					return "同一个费用科目，只能存在一条报价";
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
					model.setOperateDesc("更新增值报价,模板编码【"+entity.getTemplateId()+"】");
					model.setOperatePerson(JAppContext.currentUserName());
					model.setOperateTable("price_storage_extra");
					model.setOperateTime(JAppContext.currentTimestamp());
					model.setOperateType(RecordLogOperateType.UPDATE.getCode());
					model.setRemark("");
					model.setOperateTableKey(entity.getTemplateId());
					model.setUrlName(RecordLogUrlNameEnum.IN_STORAGE_OTHER_PRICE.getCode());
					pubRecordLogService.AddRecordLog(model);
				}catch(Exception e){
					logger.error("记录日志失败,失败原因:", e);
				}
				return "SUCCESS";
			}else{
				return "fail";
			}
			
		}
		catch(Exception ex){
			//写入日志
			bmsErrorLogInfoService.insertLog(this.getClass().getSimpleName(),Thread.currentThread().getStackTrace()[1].getMethodName(), "", ex.toString());
			logger.error("异常:", ex);
			return "数据库操作失败";
		}
	}
	
	@DataResolver
	public String delete(PriceExtraQuotationEntity entity){
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
					model.setNewData(JSON.toJSONString(entity));
					model.setOldData("");
					model.setOperateDesc("作废增值报价,模板编码【"+entity.getTemplateId()+"】");
					model.setOperatePerson(JAppContext.currentUserName());
					model.setOperateTable("price_storage_extra");
					model.setOperateTime(JAppContext.currentTimestamp());
					model.setOperateType(RecordLogOperateType.UPDATE.getCode());
					model.setRemark("");
					model.setOperateTableKey(entity.getTemplateId());
					model.setUrlName(RecordLogUrlNameEnum.IN_STORAGE_OTHER_PRICE.getCode());
					pubRecordLogService.AddRecordLog(model);
				}catch(Exception e){
					logger.error("记录日志失败,失败原因:", e);
				}
				return "SUCCESS";
			}else{
				return "fail";
			}
			
		}
		catch(Exception ex){
			//写入日志
			bmsErrorLogInfoService.insertLog(this.getClass().getSimpleName(),Thread.currentThread().getStackTrace()[1].getMethodName(), "", ex.toString());
			logger.error("异常:", ex);
			return "数据库操作失败";
		}
	}
	@DataResolver
	public String remove(GenericTemplateEntity entity){
		try{
			Map<String,Object> map=new HashMap<String,Object>();
			map.put("templateId", entity.getId().toString());
			map.put("lastModifier", JAppContext.currentUserName());
			map.put("lastModifyTime", JAppContext.currentTimestamp());
			service.remove(map);
			return "succ";
		}
		catch(Exception ex){
			//写入日志
			bmsErrorLogInfoService.insertLog(this.getClass().getSimpleName(),Thread.currentThread().getStackTrace()[1].getMethodName(), "", ex.toString());
			logger.error("异常:", ex);
			return "数据库操作失败:"+ex.getMessage();
		}
	}
	@DataResolver
	public String removeAll(GenericTemplateEntity entity){
		try{
			if(Session.isMissing()){
				return "长时间未操作，用户已失效，请重新登录再试！";
			}else if(entity == null){
				return "页面传递参数有误！";
			}
			Map<String,Object> map=new HashMap<String,Object>();
			map.put("templateId", entity.getId().toString());
			map.put("lastModifier", JAppContext.currentUserName());
			map.put("lastModifyTime", JAppContext.currentTimestamp());
			service.removeAll(map);
			return "succ";
		}
		catch(Exception ex){
			//写入日志
			bmsErrorLogInfoService.insertLog(this.getClass().getSimpleName(),Thread.currentThread().getStackTrace()[1].getMethodName(), "", ex.toString());
			logger.error("异常:", ex);
			return "数据库操作失败:"+ex.getMessage();
		}
	}
	
	@FileProvider
	public DownloadFile getDispatchTemplate(Map<String, String> parameter) throws IOException {
		InputStream is = DoradoContext.getCurrent().getServletContext().getResourceAsStream("/WEB-INF/templates/storage/extraStorage_template.xlsx");
		return new DownloadFile("仓储其他报价模板.xlsx", is);
		
	}
	
	
	@FileResolver
	public Map<String, Object> importDispatchTemplate(UploadFile file, Map<String, Object> parameter) throws Exception {
		
		DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 10);
		long starTime=System.currentTimeMillis();	//开始时间
		
		Map<String, Object> map = new HashMap<String, Object>();
		//获取当前的id
		String id=(String)parameter.get("id");
						
		// 校验信息（报错提示）
		List<ErrorMessageVo> infoList = new ArrayList<ErrorMessageVo>();
		ErrorMessageVo errorVo = null;
		
		// 导入成功返回模板信息
		List<PriceExtraQuotationEntity> templateList = new ArrayList<PriceExtraQuotationEntity>();
		// 当期时间
		Timestamp currentTime = JAppContext.currentTimestamp();
		String userName = JAppContext.currentUserName();
		try {
			BaseDataType bs=null;
			bs=new ExtraStorageTemplateDataType();
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
			
            String currentNo = SequenceGenerator.uuidOf36String("t");// 当前操作ID
			
            List<SystemCodeEntity> sysCodeList = systemCodeService.findEnumList("wh_value_add_subject");
            
            List<SystemCodeEntity> sysCodeList2 = systemCodeService.findEnumList("CHARGE_UNIT");
				
			// 模板信息必填项校验
			map = impExcelCheckInfoProduct(infoList, templateList, map, currentNo,sysCodeList,sysCodeList2);
			
			logger.info("必填项验证耗时："+FileOperationUtil.getOperationTime(starTime));
			DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 700);
			starTime=System.currentTimeMillis();	//开始时间
			
			if (map.get(ConstantInterface.ImportExcelStatus.IMP_ERROR) != null) { // 有基本的错误信息(必填，数据类型不正确)
				return map;
			}
						
			//设置属性
			for(int i=0;i<templateList.size();i++){
				PriceExtraQuotationEntity p=templateList.get(i);
				p.setTemplateId(id);
				p.setDelFlag("0");// 设置为未作废
				p.setCreator(userName);
				p.setCreateTime(currentTime);
				p.setLastModifier(userName);
				p.setLastModifyTime(currentTime);
				p.setPriceType("PRICE_TYPE_NORMAL");
			}
			//插入正式表
			int insertNum = 0;
			
			try {
				insertNum = service.insertBatchTmp(templateList);
			} catch (Exception e){
				if((e.getMessage().indexOf("Duplicate entry"))>0){
					errorVo = new ErrorMessageVo();
					errorVo.setMsg("违反唯一性约束,插入数据失败!");
					infoList.add(errorVo);
					map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
					return map;
				}
				//写入日志
				bmsErrorLogInfoService.insertLog(this.getClass().getSimpleName(),Thread.currentThread().getStackTrace()[1].getMethodName(), "", e.toString());

			}
				
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
	
	private List<PriceExtraQuotationEntity> readExcelProduct(UploadFile file,BaseDataType bs) {
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
			List<PriceExtraQuotationEntity> productList = Lists.newArrayList();
			for (Map<String, String> data : datas) {
				PriceExtraQuotationEntity p = (PriceExtraQuotationEntity) BeanToMapUtil.convertMapNull(PriceExtraQuotationEntity.class, data);
				productList.add(p);
			}
			return productList;
		} catch (Exception e) {
		    logger.error("异常:", e);
			//写入日志
			bmsErrorLogInfoService.insertLog(this.getClass().getSimpleName(),Thread.currentThread().getStackTrace()[1].getMethodName(), "", e.toString());

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
			
			newEntity.setCustomerId(entity.getCustomerId());
			newEntity.setCustomerName(entity.getCustomerName());
			newEntity.setTemplateCode(templateNo);
			newEntity.setTemplateName(entity.getTemplateName());
			newEntity.setBizTypeCode("STORAGE");
			newEntity.setSubjectId(entity.getSubjectId());
			newEntity.setRemark(entity.getRemark());
			newEntity.setTemplateType(entity.getTemplateType());
			newEntity.setStorageTemplateType("STORAGE_EXTRA_PRICE_TEMPLATE");
			
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
			
			PageInfo<PriceExtraQuotationEntity> page = service.query(params, 0, Integer.MAX_VALUE);
			
			if(null!=page){
				List<PriceExtraQuotationEntity> list = page.getList();
				
				for(PriceExtraQuotationEntity vo:list){
					vo.setId(null);
					vo.setTemplateId(templateId.toString());
				}
				
				service.insertBatchTmp(list);
			}
 			
			return "SUCCESS";
		}
		catch(Exception ex){
			//写入日志
			bmsErrorLogInfoService.insertLog(this.getClass().getSimpleName(),Thread.currentThread().getStackTrace()[1].getMethodName(), "", ex.toString());
			logger.error("异常:", ex);
			return "数据库操作失败";
		}
		
	}
	
	private Map<String, Object> impExcelCheckInfoProduct(List<ErrorMessageVo> infoList, List<PriceExtraQuotationEntity> prodList, Map<String, Object> map, String currentNo,List<SystemCodeEntity> sysCodeList, List<SystemCodeEntity> sysCodeList2) {
		int lineNo = 0;
		for (int i = 0; i < prodList.size(); i++) {
			PriceExtraQuotationEntity p=prodList.get(i);
			lineNo=lineNo+1;
			//导入的默认一口价
			p.setPriceType("PRICE_TYPE_NORMAL");
			
			if(null==p.getUnitPrice()){
				setMessage(infoList, lineNo,"单价为空!");
			}
			
			if(StringUtils.isEmpty(p.getFeeUnitCode())){
				setMessage(infoList, lineNo,"费用单位为空!");
			}
			
			boolean temperatureCheck = false;
			
			for(SystemCodeEntity entity:sysCodeList)
			{
				if(entity.getCodeName().equals(p.getSubjectId()))
				{
					p.setSubjectId(entity.getCode());
					temperatureCheck = true;
					break;
				}
			}
			
			if(!temperatureCheck){
				setMessage(infoList, lineNo,"系统没有维护该费用科目!");
			}
			
             boolean temperatureCheck2 = false;
			
			for(SystemCodeEntity entity:sysCodeList2)
			{
				if(entity.getCodeName().equals(p.getFeeUnitCode()))
				{
					p.setFeeUnitCode(entity.getCode());
					temperatureCheck2 = true;
					break;
				}
			}
			
			if(!temperatureCheck2){
				setMessage(infoList, lineNo,"系统没有维护该计费单位!");
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
 