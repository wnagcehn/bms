/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.quotation.transport.web;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
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
import com.jiuyescm.bms.base.dictionary.entity.SystemCodeEntity;
import com.jiuyescm.bms.base.dictionary.service.ISystemCodeService;
import com.jiuyescm.bms.common.entity.ErrorMessageVo;
import com.jiuyescm.bms.common.enumtype.RecordLogBizTypeEnum;
import com.jiuyescm.bms.common.enumtype.RecordLogOperateType;
import com.jiuyescm.bms.common.enumtype.RecordLogUrlNameEnum;
import com.jiuyescm.bms.common.log.service.IBmsErrorLogInfoService;
import com.jiuyescm.bms.common.sequence.service.SequenceService;
import com.jiuyescm.bms.common.tool.Session;
import com.jiuyescm.bms.properties.util.PropertiesUtil;
import com.jiuyescm.bms.pub.IPubRecordLogService;
import com.jiuyescm.bms.pub.PubRecordLogEntity;
import com.jiuyescm.bms.quotation.transport.entity.PriceTransportTemplateEntity;
import com.jiuyescm.bms.quotation.transport.entity.PriceTransportValueAddedEntity;
import com.jiuyescm.bms.quotation.transport.service.IPriceTransportValueAddedService;
import com.jiuyescm.cfm.common.JAppContext;
import com.jiuyescm.cfm.common.sequence.SequenceGenerator;
import com.jiuyescm.common.ConstantInterface;
import com.jiuyescm.common.utils.BeanToMapUtil;
import com.jiuyescm.common.utils.FileOperationUtil;
import com.jiuyescm.common.utils.excel.FileReaderFactory;
import com.jiuyescm.common.utils.excel.IFileReader;
import com.jiuyescm.common.utils.upload.BaseDataType;
import com.jiuyescm.common.utils.upload.DataProperty;
import com.jiuyescm.common.utils.upload.TransportValueAddedImportTemplateDataType;

/**
 * 
 * @author wubangjun
 * 
 */
@Controller("transportValueAddedController")
public class PriceTransportValueAddedController {

	private static final Logger logger = Logger.getLogger(PriceTransportValueAddedController.class.getName());

	@Resource
	private IPriceTransportValueAddedService priceTransportValueAddedService;


	@Resource 
	private SequenceService sequenceService;
	
	@Resource
	private ISystemCodeService systemCodeService; //业务类型
	
	@Resource
	private IBmsErrorLogInfoService bmsErrorLogInfoService;
	@Resource
	private IPubRecordLogService pubRecordLogService;
	/**
	 * 分页查询
	 * 
	 * @param page
	 * @param param
	 */
	@DataProvider
	public void query(Page<PriceTransportValueAddedEntity> page, Map<String, Object> param) {
		if(param != null){
			param.put("delFlag", "0");
		}
		PageInfo<PriceTransportValueAddedEntity> pageInfo = priceTransportValueAddedService.query(param, page.getPageNo(), page.getPageSize());
		if (pageInfo != null) {
			page.setEntities(pageInfo.getList());
			page.setEntityCount((int) pageInfo.getTotal());
		}
	}

	@DataResolver
	public String save(PriceTransportValueAddedEntity entity) {
		if(Session.isMissing()){
			return "长时间未操作，用户已失效，请重新登录再试！";
		}else if(entity == null){
			return "页面传递参数有误！";
		}
		if (entity.getId() == null) {
			String quotationNo =sequenceService.getBillNoOne(PriceTransportValueAddedEntity.class.getName(), "VA", "00000");
			entity.setQuotationNo(quotationNo);
			entity.setCreateTime(JAppContext.currentTimestamp());
			entity.setCreator(JAppContext.currentUserName());
			entity.setLastModifier(JAppContext.currentUserName());
			entity.setLastModifyTime(JAppContext.currentTimestamp());
			entity.setDelFlag("0");
			priceTransportValueAddedService.save(entity);
			try{
				PubRecordLogEntity model=new PubRecordLogEntity();
				model.setBizType(RecordLogBizTypeEnum.CONTACT.getCode());
				model.setNewData(JSON.toJSONString(entity));
				model.setOldData("");
				model.setOperateDesc("新增运输其他报价模板,模板编码【"+quotationNo+"】");
				model.setOperatePerson(JAppContext.currentUserName());
				model.setOperateTable("price_transport_valueadded");
				model.setOperateTime(JAppContext.currentTimestamp());
				model.setOperateType(RecordLogOperateType.INSERT.getCode());
				model.setRemark("");
				model.setOperateTableKey(quotationNo);
				model.setUrlName(RecordLogUrlNameEnum.IN_TRANSPORT_OTHER_PRICE.getCode());
				pubRecordLogService.AddRecordLog(model);
			}catch(Exception e){
				logger.error("记录日志失败,失败原因:", e);
			}
		} else {
			entity.setLastModifier(JAppContext.currentUserName());
			entity.setLastModifyTime(JAppContext.currentTimestamp());
			priceTransportValueAddedService.update(entity);
		}
		return null;
	}
	
	@DataResolver
	public void saveAll(Collection<PriceTransportValueAddedEntity> datas) {
		if (datas == null) {
			return;
		}
		for (PriceTransportValueAddedEntity temp : datas) {
			temp.setLastModifier(JAppContext.currentUserName());
			temp.setLastModifyTime(JAppContext.currentTimestamp());
			if (EntityState.NEW.equals(EntityUtils.getState(temp))) {
				this.save(temp);
			} else if (EntityState.MODIFIED.equals(EntityUtils.getState(temp))) {
				priceTransportValueAddedService.update(temp);
				try{
					PubRecordLogEntity model=new PubRecordLogEntity();
					model.setBizType(RecordLogBizTypeEnum.CONTACT.getCode());
					model.setNewData(JSON.toJSONString(temp));
					model.setOldData("");
					model.setOperateDesc("更新运输其他报价模板,模板编码【"+temp.getQuotationNo()+"】");
					model.setOperatePerson(JAppContext.currentUserName());
					model.setOperateTable("price_transport_valueadded");
					model.setOperateTime(JAppContext.currentTimestamp());
					model.setOperateType(RecordLogOperateType.UPDATE.getCode());
					model.setRemark("");
					model.setOperateTableKey(temp.getQuotationNo());
					model.setUrlName(RecordLogUrlNameEnum.IN_TRANSPORT_OTHER_PRICE.getCode());
					pubRecordLogService.AddRecordLog(model);
				}catch(Exception e){
					logger.error("记录日志失败,失败原因:", e);
				}
			} else if (EntityState.DELETED.equals(EntityUtils.getState(temp))) {
				priceTransportValueAddedService.delete(temp.getId());
				try{
					PubRecordLogEntity model=new PubRecordLogEntity();
					model.setBizType(RecordLogBizTypeEnum.CONTACT.getCode());
					model.setNewData(JSON.toJSONString(temp));
					model.setOldData("");
					model.setOperateDesc("删除运输其他报价模板,模板编码【"+temp.getQuotationNo()+"】");
					model.setOperatePerson(JAppContext.currentUserName());
					model.setOperateTable("price_transport_valueadded");
					model.setOperateTime(JAppContext.currentTimestamp());
					model.setOperateType(RecordLogOperateType.DELETE.getCode());
					model.setRemark("");
					model.setOperateTableKey(temp.getQuotationNo());
					model.setUrlName(RecordLogUrlNameEnum.IN_TRANSPORT_OTHER_PRICE.getCode());
					pubRecordLogService.AddRecordLog(model);
				}catch(Exception e){
					logger.error("记录日志失败,失败原因:", e);
				}
			} else {
				// do nothing;
			}
		}
	}
	
	@DataResolver
	public String update(PriceTransportValueAddedEntity entity){
		try{
			if(entity != null){
				entity.setLastModifier(JAppContext.currentUserName());
				entity.setLastModifyTime(JAppContext.currentTimestamp());
			}
			priceTransportValueAddedService.update(entity);
			try{
				PubRecordLogEntity model=new PubRecordLogEntity();
				model.setBizType(RecordLogBizTypeEnum.CONTACT.getCode());
				model.setNewData(JSON.toJSONString(entity));
				model.setOldData("");
				model.setOperateDesc("更新运输其他报价模板,模板编码【"+entity.getQuotationNo()+"】");
				model.setOperatePerson(JAppContext.currentUserName());
				model.setOperateTable("price_transport_valueadded");
				model.setOperateTime(JAppContext.currentTimestamp());
				model.setOperateType(RecordLogOperateType.UPDATE.getCode());
				model.setRemark("");
				model.setOperateTableKey(entity.getQuotationNo());
				model.setUrlName(RecordLogUrlNameEnum.IN_TRANSPORT_OTHER_PRICE.getCode());
				pubRecordLogService.AddRecordLog(model);
			}catch(Exception e){
				logger.error("记录日志失败,失败原因:", e);
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

	@DataResolver
	public String delete(PriceTransportValueAddedEntity entity) {
		try{
			entity.setDelFlag("1");
			entity.setLastModifier(JAppContext.currentUserName());
			entity.setLastModifyTime(JAppContext.currentTimestamp());
			priceTransportValueAddedService.update(entity);
			try{
				PubRecordLogEntity model=new PubRecordLogEntity();
				model.setBizType(RecordLogBizTypeEnum.CONTACT.getCode());
				model.setNewData(JSON.toJSONString(entity));
				model.setOldData("");
				model.setOperateDesc("作废运输其他报价模板,模板编码【"+entity.getQuotationNo()+"】");
				model.setOperatePerson(JAppContext.currentUserName());
				model.setOperateTable("price_transport_valueadded");
				model.setOperateTime(JAppContext.currentTimestamp());
				model.setOperateType(RecordLogOperateType.UPDATE.getCode());
				model.setRemark("");
				model.setOperateTableKey(entity.getQuotationNo());
				model.setUrlName(RecordLogUrlNameEnum.IN_TRANSPORT_OTHER_PRICE.getCode());
				pubRecordLogService.AddRecordLog(model);
			}catch(Exception e){
				logger.error("记录日志失败,失败原因:", e);
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
	
	/**
	 * 运输增值费报价模板下载
	 * @param parameter
	 * @return
	 * @throws IOException
	 */
	@FileProvider
	public DownloadFile accquireTransportValueAddedTemplate(Map<String, String> parameter) throws IOException {
		InputStream is = DoradoContext.getCurrent().getServletContext().getResourceAsStream("/WEB-INF/templates/transport/transport_valueadded_template.xlsx");
		return new DownloadFile("运输增值费报价模板.xlsx", is);
	}
	
	
	//---------------------------------------------------------------------------------------------------------------------------------
	/**
	 * 导入运输增值费报价模板
	 * @param file
	 * @param parameter
	 * @return
	 * @throws Exception
	 */
	@FileResolver
	public Map<String, Object> importTransportValueAdded(UploadFile file, Map<String, Object> parameter) throws Exception {
		DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 10);
		//获取当前的id
		String id=(String)parameter.get("id");
		
		Map<String, Object> map = new HashMap<String, Object>();
		// 校验信息（报错提示）
		List<ErrorMessageVo> infoList = new ArrayList<ErrorMessageVo>();
		ErrorMessageVo errorVo = null;
		
		// 导入成功返回模板信息
		List<PriceTransportValueAddedEntity> valueAddedList = new ArrayList<PriceTransportValueAddedEntity>();
		// 当期时间
		Timestamp currentTime = JAppContext.currentTimestamp();
		String userName = JAppContext.currentUserName();
		try {
			BaseDataType bs = new TransportValueAddedImportTemplateDataType();
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
			valueAddedList = readExcelProduct(file,bs);
			if (null == valueAddedList || valueAddedList.size() <= 0) {
				DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 999);
				errorVo = new ErrorMessageVo();
				errorVo.setMsg("导入的Excel数据为空或者数据格式不对!");
				infoList.add(errorVo);
				map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
				return map;
			}
			DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 300);
			
			String currentNo = SequenceGenerator.uuidOf36String("t");// 当前操作ID
			/*condition.put("tempid", currentNo);*/
			
			// 模板信息必填项校验
			map = impExcelCheckInfoProduct(infoList, valueAddedList, map, currentNo);
			if (map.get(ConstantInterface.ImportExcelStatus.IMP_ERROR) != null) { // 有基本的错误信息(必填，数据类型不正确)
				DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 999);
				return map;
			}
			DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 700);
			
			// 获得初步校验后和转换后的数据
			valueAddedList = (List<PriceTransportValueAddedEntity>) map.get(ConstantInterface.ImportExcelStatus.IMP_SUCC);
			
			//设置属性
			//String lineNo = null;
			for(int i=0;i<valueAddedList.size();i++){
				PriceTransportValueAddedEntity valueAddedEntity  = valueAddedList.get(i);
				String billNo = id;
				valueAddedEntity.setTemplateId(billNo);
				String quotationNo = sequenceService.getBillNoOne(PriceTransportValueAddedEntity.class.getName(), "VA", "00000");
				valueAddedEntity.setQuotationNo(quotationNo);
				valueAddedEntity.setDelFlag("0");// 设置为未作废
				valueAddedEntity.setCreator(userName);
				valueAddedEntity.setCreateTime(currentTime);
				valueAddedEntity.setLastModifier(userName);
				valueAddedEntity.setLastModifyTime(currentTime);
			}
			DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 800);
			
			//插入正式表
			int insertNum = priceTransportValueAddedService.saveList(valueAddedList);
			DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 950);
			
			if (insertNum <= 0) {
				DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 999);
				errorVo = new ErrorMessageVo();
				errorVo.setMsg("存档运输增值费报价失败!");
				infoList.add(errorVo);
				map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
				return map;
			}else{
				DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 1000);
				map.put(ConstantInterface.ImportExcelStatus.IMP_SUCC, "0");
				try{
					PubRecordLogEntity model=new PubRecordLogEntity();
					model.setBizType(RecordLogBizTypeEnum.PRICE.getCode());
					model.setNewData("");
					model.setOldData("");
					model.setOperateDesc("导入运输其他报价模板,共计【"+insertNum+"】条");
					model.setOperatePerson(JAppContext.currentUserName());
					model.setOperateTable("price_transport_valueadded");
					model.setOperateTime(JAppContext.currentTimestamp());
					model.setOperateType(RecordLogOperateType.IMPORT.getCode());
					model.setRemark("");
					model.setOperateTableKey("");
					model.setUrlName(RecordLogUrlNameEnum.IN_TRANSPORT_OTHER_PRICE.getCode());
					pubRecordLogService.AddRecordLog(model);
				}catch(Exception e){
					logger.error("记录日志失败,失败原因:", e);
				}
				return map;
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			//写入日志
			bmsErrorLogInfoService.insertLog(this.getClass().getSimpleName(),Thread.currentThread().getStackTrace()[1].getMethodName(), "", e.toString());
			logger.error("异常:", e);
			DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 999);
		} finally{
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
		

		return map;
	}
	
	
	/**
	 * 读取Excel中的数据（不包含列名）
	 * 
	 * @param file
	 * @return
	 */
	private List<PriceTransportValueAddedEntity> readExcelProduct(UploadFile file,BaseDataType bs) {
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
			List<PriceTransportValueAddedEntity> valueAddedList = Lists.newArrayList();
			for (Map<String, String> data : datas) {
				PriceTransportValueAddedEntity p = (PriceTransportValueAddedEntity) BeanToMapUtil.convertMapNull(PriceTransportValueAddedEntity.class, data);
				valueAddedList.add(p);
			}
			return valueAddedList;
		} catch (Exception e) {
			//写入日志
			bmsErrorLogInfoService.insertLog(this.getClass().getSimpleName(),Thread.currentThread().getStackTrace()[1].getMethodName(), "", e.toString());
			logger.error("异常:", e);
		}
		return null;
	}
	
	
	/**
	 * 校验Excel里面的内容合法性
	 * 
	 * @param infoList
	 * @param list
	 * @param map
	 * @param objList
	 * @param currentNo
	 * @return
	 */
	private Map<String, Object> impExcelCheckInfoProduct(List<ErrorMessageVo> infoList, List<PriceTransportValueAddedEntity> valueAddedList, Map<String, Object> map, String currentNo) {
		int lineNo = 0;
		Map<String, String> carModelMap = new HashMap<String, String>();
		Map<String, String> feeUnitMap = new HashMap<String, String>();
		Map<String, String> transportSubjectMap = new HashMap<String, String>();
		//当导入数据量达到多少时,先一次加载再判断;否则数据量小的时候每次检索校验.
		if(valueAddedList != null && valueAddedList.size()>0){
			//以下是基础数据字典
			//车型
			Map<String, Object> carModelParam = new HashMap<String, Object>();
			carModelParam.put("typeCode", "MOTORCYCLE_TYPE");
			List<SystemCodeEntity> carModelList = systemCodeService.queryCodeList(carModelParam);
			if(carModelList != null && carModelList.size()>0){
				for(SystemCodeEntity scEntity : carModelList){
					carModelMap.put(scEntity.getCodeName(), scEntity.getCode());
				}
			}
			//计费单位
			Map<String, Object> feeUnitParam = new HashMap<String, Object>();
			feeUnitParam.put("typeCode", "CHARGE_UNIT");
			List<SystemCodeEntity> feeUnitList = systemCodeService.queryCodeList(feeUnitParam);
			if(feeUnitList != null && feeUnitList.size()>0){
				for(SystemCodeEntity scEntity : feeUnitList){
					feeUnitMap.put(scEntity.getCodeName(), scEntity.getCode());
				}
			}
			//运输增值费用科目
			Map<String, Object> subjectParam = new HashMap<String, Object>();
			subjectParam.put("typeCode", "ts_value_add_subject");
			List<SystemCodeEntity> subjectList = systemCodeService.queryCodeList(subjectParam);
			if(subjectList != null && subjectList.size()>0){
				for(SystemCodeEntity scEntity : subjectList){
					transportSubjectMap.put(scEntity.getCodeName(), scEntity.getCode());
				}
			}
		}
		for (int i = 0; i < valueAddedList.size(); i++) {
			PriceTransportValueAddedEntity valueAdded = valueAddedList.get(i);
			lineNo = lineNo+1;
			//车型、重量界限、计费单位、单价、费用科目
			
			//判断车型是否在字典表中维护
			String carModelName = valueAdded.getCarModelName();
			if(StringUtils.isNotBlank(carModelName)){
				//从缓存carModelMap中直接取
				if(carModelMap.containsKey(carModelName) && StringUtils.isNotBlank(carModelMap.get(carModelName))){
					logger.info(String.format("从车型缓存Map中取数据[%s]", carModelName));
					valueAdded.setCarModelCode(carModelMap.get(carModelName));
				}else{
					Map<String, Object> param=new HashMap<String, Object>();
					param.put("typeCode", "MOTORCYCLE_TYPE");
					param.put("codeName", carModelName);
					List<SystemCodeEntity> carModelList = systemCodeService.queryCodeList(param);
					if(carModelList==null || carModelList.size()==0){
						setMessage(infoList, lineNo,"该车型没有在表中维护!");
					}else{
						valueAdded.setCarModelCode(carModelList.get(0).getCode());
						//放入缓存carModelMap
						carModelMap.put(carModelName, carModelList.get(0).getCode());
					}
				}
			}
			
			//判断重量界限不能为非数字
			if(valueAdded.getWeightLimit()==null){
				setMessage(infoList, lineNo,"重量界限不能为空且不能为非数字!");
			}
			
			//判断计费单位是否在字典表中维护
			String feeUnitName = valueAdded.getFeeUnitName();
			if(StringUtils.isNotBlank(feeUnitName)){
				//从缓存feeUnitMap中直接取
				if(feeUnitMap.containsKey(feeUnitName) && StringUtils.isNotBlank(feeUnitMap.get(feeUnitName))){
					logger.info(String.format("从计费单位缓存Map中取数据[%s]", feeUnitName));
					valueAdded.setFeeUnitCode(feeUnitMap.get(feeUnitName));
				}else{
					Map<String, Object> param=new HashMap<String, Object>();
					param.put("typeCode", "CHARGE_UNIT");
					param.put("codeName", feeUnitName);
					List<SystemCodeEntity> feeUnitList = systemCodeService.queryCodeList(param);
					if(feeUnitList==null || feeUnitList.size()==0){
						setMessage(infoList, lineNo,"该计费单位没有在表中维护!");
					}else{
						valueAdded.setFeeUnitCode(feeUnitList.get(0).getCode());
						//放入缓存feeUnitMap
						feeUnitMap.put(feeUnitName, feeUnitList.get(0).getCode());
					}
				}
			}
			
			//判断单价不能为非数字
			if(valueAdded.getUnitPrice()==null){
				setMessage(infoList, lineNo,"单价不能为空且不能为非数字!");
			}

			//运输增值费用科目ts_value_add_subject：提货费、送货费、装卸费、逆向物流费、延时等待费、缠绕膜费、理货费 ； 
			//判断费用科目是否在字典表中维护
			String subjectName = valueAdded.getSubjectName();
			if(StringUtils.isNotBlank(subjectName)){
				//从缓存transportSubjectMap中直接取
				if(transportSubjectMap.containsKey(subjectName) && StringUtils.isNotBlank(transportSubjectMap.get(subjectName))){
					logger.info(String.format("从运输增值费用科目缓存Map中取数据[%s]", subjectName));
					valueAdded.setSubjectCode(transportSubjectMap.get(subjectName));
				}else{
					Map<String, Object> param = new HashMap<String, Object>();
					param.put("typeCode", "ts_value_add_subject");
					param.put("codeName", subjectName);
					List<SystemCodeEntity> subjectList = systemCodeService.queryCodeList(param);
					if(subjectList==null || subjectList.size()==0){
						setMessage(infoList, lineNo,"该费用科目没有在表中维护!");
					}else{
						valueAdded.setSubjectCode(subjectList.get(0).getCode());
						//放入缓存transportSubjectMap
						transportSubjectMap.put(subjectName, subjectList.get(0).getCode());
					}
				}
			}
			
			
			if (infoList != null && infoList.size() > 0) { // 有错误信息
				map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
			} else {
				map.put(ConstantInterface.ImportExcelStatus.IMP_SUCC, valueAddedList); // 无基本错误信息
			}
			
			DecimalFormat decimalFormat=new DecimalFormat("0");
			double addNum = ((double)(i+1)/valueAddedList.size())*400;
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
	
	
	//------------------------------------------------------------------------
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
	

	@DataResolver
	public String deleteBatch(PriceTransportTemplateEntity entity) {
		try{
			int i = priceTransportValueAddedService.deleteBatch(entity.getId());
			if(i>0){
				return "SUCCESS";
			}else{
				return "FAIL";
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
	public int deleteBatchList(List<PriceTransportValueAddedEntity> list){
		int k= priceTransportValueAddedService.deleteBatchList(list);
		try{
			PubRecordLogEntity model=new PubRecordLogEntity();
			model.setBizType("报价");
			model.setNewData("");
			model.setOldData("");
			model.setOperateDesc("批量作废运输其他报价模板,共计【"+list.size()+"】条");
			model.setOperatePerson(JAppContext.currentUserName());
			model.setOperateTable("price_transport_valueadded");
			model.setOperateTime(JAppContext.currentTimestamp());
			model.setOperateType("删除");
			model.setRemark("");
			pubRecordLogService.AddRecordLog(model);
		}catch(Exception e){
			logger.error("记录日志失败,失败原因:", e);
		}
		return k;
	}
}
