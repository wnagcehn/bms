/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.quotation.out.transport.web;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.sql.Timestamp;
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
import com.jiuyescm.bms.quotation.out.transport.entity.PriceTransportPayLineEntity;
import com.jiuyescm.bms.quotation.out.transport.entity.PriceTransportPayLineRangeEntity;
import com.jiuyescm.bms.quotation.out.transport.service.IPriceTransportPayLineRangeService;
import com.jiuyescm.bms.quotation.out.transport.service.IPriceTransportPayLineService;
import com.jiuyescm.cfm.common.JAppContext;
import com.jiuyescm.cfm.common.sequence.SequenceGenerator;
import com.jiuyescm.common.ConstantInterface;
import com.jiuyescm.common.utils.BeanToMapUtil;
import com.jiuyescm.common.utils.FileOperationUtil;
import com.jiuyescm.common.utils.excel.FileReaderFactory;
import com.jiuyescm.common.utils.excel.IFileReader;
import com.jiuyescm.common.utils.upload.BaseDataType;
import com.jiuyescm.common.utils.upload.DataProperty;
import com.jiuyescm.common.utils.upload.TransportLineRangeImportTemplateDataType;

/**
 * 
 * @author wubangjun
 * 
 */
@Controller("transportPayLineController")
public class PriceTransportPayLineController {

	private static final Logger logger = Logger.getLogger(PriceTransportPayLineController.class.getName());

	@Resource
	private IPriceTransportPayLineService priceTransportPayLineService;
	
	@Resource
	private IPriceTransportPayLineRangeService priceTransportPayLineRangeService;
	
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
	public void query(Page<PriceTransportPayLineEntity> page, Map<String, Object> param) {
		if(param != null){
			param.put("delFlag", "0");
		}
		PageInfo<PriceTransportPayLineEntity> pageInfo = priceTransportPayLineService.query(param, page.getPageNo(), page.getPageSize());
		if (pageInfo != null) {
			page.setEntities(pageInfo.getList());
			page.setEntityCount((int) pageInfo.getTotal());
		}
	}
	
	/**
	 * 分页查询
	 * 
	 * @param page
	 * @param param
	 */
	@DataProvider
	public void query(Page<PriceTransportPayLineEntity> page, PriceTransportPayLineEntity lineEntity) {
		if(lineEntity != null){
			lineEntity.setDelFlag("0");
		}
		Map<String, Object> lineParam = this.convertObjToMap(lineEntity);
		PageInfo<PriceTransportPayLineEntity> pageInfo = priceTransportPayLineService.query(lineParam, page.getPageNo(), page.getPageSize());
		if (pageInfo != null) {
			page.setEntities(pageInfo.getList());
			page.setEntityCount((int) pageInfo.getTotal());
		}
		
	}
	
	/** 
     * 实体类对象转换成Map 
     * @param obj 
     * @return 
     */  
    public static Map<String, Object> convertObjToMap(Object obj) {  
        Map<String, Object> resultMap = new HashMap<String, Object>();  
        if (obj == null){  
            return null;
        }
        try{
        	 BeanInfo info = Introspector.getBeanInfo(obj.getClass());  
             for (PropertyDescriptor pd : info.getPropertyDescriptors()) {  
                 Method reader = pd.getReadMethod();  
                 //内容为null的过滤掉  
                 if (reader == null || reader.invoke(obj) == null) {  
                     continue;  
                 }  
                //默认继承Object类的属性，过滤掉  
                 if (pd.getName().equalsIgnoreCase("class")) {  
                     continue;  
                 }  
                 resultMap.put(pd.getName(), reader.invoke(obj));  
             }  
	        printOut(resultMap);
        }catch(Exception e){
        	logger.error(obj.getClass(), e);
        	return null;
        }
        return resultMap;  
    } 
    /** 
     * 打印结果 
     * */  
    private static void printOut(Map<String, Object> resMap) {  
        for (String key : resMap.keySet()) {  
            logger.info("Key:" + key + ";Value:" + resMap.get(key));  
        }  
    }  

	@DataResolver
	public String save(PriceTransportPayLineEntity entity) {
		if(Session.isMissing()){
			return "长时间未操作，用户已失效，请重新登录再试！";
		}else if(entity == null){
			return "页面传递参数有误！";
		}
		if (entity.getId() == null) {
			String templateNo = sequenceService.getBillNoOne(PriceTransportPayLineEntity.class.getName(), "TPL", "00000");
			entity.setTransportLineNo(templateNo);
			entity.setCreateTime(JAppContext.currentTimestamp());
			entity.setCreator(JAppContext.currentUserName());
			entity.setLastModifier(JAppContext.currentUserName());
			entity.setLastModifyTime(JAppContext.currentTimestamp());
			entity.setDelFlag("0");
			priceTransportPayLineService.save(entity);
			try{
				PubRecordLogEntity model=new PubRecordLogEntity();
				model.setBizType(RecordLogBizTypeEnum.PRICE.getCode());
				model.setNewData(JSON.toJSONString(entity));
				model.setOldData("");
				model.setOperateDesc("新增应付运输报价,模板编码【"+templateNo+"】");
				model.setOperatePerson(JAppContext.currentUserName());
				model.setOperateTable("price_out_transport_line");
				model.setOperateTime(JAppContext.currentTimestamp());
				model.setOperateType(RecordLogOperateType.INSERT.getCode());
				model.setRemark("");
				model.setOperateTableKey(templateNo);
				model.setUrlName(RecordLogUrlNameEnum.OUT_TRANSPORT_BASE_PRICE.getCode());
				pubRecordLogService.AddRecordLog(model);
			}catch(Exception e){
				logger.error("记录日志失败,失败原因:", e);
			}
		} else {
			entity.setLastModifier(JAppContext.currentUserName());
			entity.setLastModifyTime(JAppContext.currentTimestamp());
			priceTransportPayLineService.update(entity);
			try{
				PubRecordLogEntity model=new PubRecordLogEntity();
				model.setBizType(RecordLogBizTypeEnum.PRICE.getCode());
				model.setNewData(JSON.toJSONString(entity));
				model.setOldData("");
				model.setOperateDesc("更新应付运输报价,模板编码【"+entity.getTransportLineNo()+"】");
				model.setOperatePerson(JAppContext.currentUserName());
				model.setOperateTable("price_out_transport_line");
				model.setOperateTime(JAppContext.currentTimestamp());
				model.setOperateType(RecordLogOperateType.UPDATE.getCode());
				model.setRemark("");
				model.setOperateTableKey(entity.getTransportLineNo());
				model.setUrlName(RecordLogUrlNameEnum.OUT_TRANSPORT_BASE_PRICE.getCode());
				pubRecordLogService.AddRecordLog(model);
			}catch(Exception e){
				logger.error("记录日志失败,失败原因:", e);
			}
		}
		return null;
	}
	
	@DataResolver
	public void saveAll(Collection<PriceTransportPayLineEntity> datas) {
		if (datas == null) {
			return;
		}
		for (PriceTransportPayLineEntity temp : datas) {
			temp.setLastModifier(JAppContext.currentUserName());
			temp.setLastModifyTime(JAppContext.currentTimestamp());
			if (EntityState.NEW.equals(EntityUtils.getState(temp))) {
				this.save(temp);
			} else if (EntityState.MODIFIED.equals(EntityUtils.getState(temp))) {
				priceTransportPayLineService.update(temp);
				try{
					PubRecordLogEntity model=new PubRecordLogEntity();
					model.setBizType(RecordLogBizTypeEnum.PRICE.getCode());
					model.setNewData(JSON.toJSONString(temp));
					model.setOldData("");
					model.setOperateDesc("更新应付运输报价,模板编码【"+temp.getTransportLineNo()+"】");
					model.setOperatePerson(JAppContext.currentUserName());
					model.setOperateTable("price_out_transport_line");
					model.setOperateTime(JAppContext.currentTimestamp());
					model.setOperateType(RecordLogOperateType.UPDATE.getCode());
					model.setRemark("");
					model.setOperateTableKey(temp.getTransportLineNo());
					model.setUrlName(RecordLogUrlNameEnum.OUT_TRANSPORT_BASE_PRICE.getCode());
					pubRecordLogService.AddRecordLog(model);
				}catch(Exception e){
					logger.error("记录日志失败,失败原因:", e);
				}
			} else if (EntityState.DELETED.equals(EntityUtils.getState(temp))) {
				priceTransportPayLineService.delete(temp.getId());
				try{
					PubRecordLogEntity model=new PubRecordLogEntity();
					model.setBizType(RecordLogBizTypeEnum.PRICE.getCode());
					model.setNewData(JSON.toJSONString(temp));
					model.setOldData("");
					model.setOperateDesc("删除应付运输报价,模板编码【"+temp.getTransportLineNo()+"】");
					model.setOperatePerson(JAppContext.currentUserName());
					model.setOperateTable("price_out_transport_line");
					model.setOperateTime(JAppContext.currentTimestamp());
					model.setOperateType(RecordLogOperateType.DELETE.getCode());
					model.setRemark("");
					model.setOperateTableKey(temp.getTransportLineNo());
					model.setUrlName(RecordLogUrlNameEnum.OUT_TRANSPORT_BASE_PRICE.getCode());
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
	public String update(PriceTransportPayLineEntity entity){
		try{
			if(entity != null){
				entity.setLastModifier(JAppContext.currentUserName());
				entity.setLastModifyTime(JAppContext.currentTimestamp());
			}
			priceTransportPayLineService.update(entity);
			try{
				PubRecordLogEntity model=new PubRecordLogEntity();
				model.setBizType(RecordLogBizTypeEnum.PRICE.getCode());
				model.setNewData(JSON.toJSONString(entity));
				model.setOldData("");
				model.setOperateDesc("更新应付运输报价,模板编码【"+entity.getTransportLineNo()+"】");
				model.setOperatePerson(JAppContext.currentUserName());
				model.setOperateTable("price_out_transport_line");
				model.setOperateTime(JAppContext.currentTimestamp());
				model.setOperateType(RecordLogOperateType.UPDATE.getCode());
				model.setRemark("");
				model.setOperateTableKey(entity.getTransportLineNo());
				model.setUrlName(RecordLogUrlNameEnum.OUT_TRANSPORT_BASE_PRICE.getCode());
				pubRecordLogService.AddRecordLog(model);
			}catch(Exception e){
				logger.error("记录日志失败,失败原因:", e);
			}
			return "SUCCESS";
		}
		catch(Exception ex){
			//写入日志
			bmsErrorLogInfoService.insertLog(this.getClass().getSimpleName(),Thread.currentThread().getStackTrace()[1].getMethodName(), "", ex.toString());
			logger.error("异常：", ex);
			return "数据库操作失败";
		}
	}

	@DataResolver
	public String delete(PriceTransportPayLineEntity entity) {
		try{
			entity.setDelFlag("1");
			entity.setLastModifier(JAppContext.currentUserName());
			entity.setLastModifyTime(JAppContext.currentTimestamp());
			priceTransportPayLineService.update(entity);
			//找到运输路线对应的梯度报价
			Map<String, Object> rangeParam = new HashMap<String, Object>();
			rangeParam.put("lineId", entity.getId());
			rangeParam.put("delFlag", "0");
			List<PriceTransportPayLineRangeEntity> rangeList = priceTransportPayLineRangeService.query(rangeParam);
			if(rangeList != null && rangeList.size()>0){
				for(PriceTransportPayLineRangeEntity range: rangeList){
					range.setDelFlag("1");
					range.setLastModifier(JAppContext.currentUserName());
					range.setLastModifyTime(JAppContext.currentTimestamp());
					//删除梯度报价
					priceTransportPayLineRangeService.update(range);
				}
			}
			try{
				PubRecordLogEntity model=new PubRecordLogEntity();
				model.setBizType(RecordLogBizTypeEnum.PRICE.getCode());
				model.setNewData(JSON.toJSONString(entity));
				model.setOldData("");
				model.setOperateDesc("作废应付运输报价,模板编码【"+entity.getTransportLineNo()+"】");
				model.setOperatePerson(JAppContext.currentUserName());
				model.setOperateTable("price_out_transport_line");
				model.setOperateTime(JAppContext.currentTimestamp());
				model.setOperateType(RecordLogOperateType.UPDATE.getCode());
				model.setRemark("");
				model.setOperateTableKey(entity.getTransportLineNo());
				model.setUrlName(RecordLogUrlNameEnum.OUT_TRANSPORT_BASE_PRICE.getCode());
				pubRecordLogService.AddRecordLog(model);
			}catch(Exception e){
				logger.error("记录日志失败,失败原因:", e);
			}
			return "SUCCESS";
		}
		catch(Exception ex){
			//写入日志
			bmsErrorLogInfoService.insertLog(this.getClass().getSimpleName(),Thread.currentThread().getStackTrace()[1].getMethodName(), "", ex.toString());
			logger.error("异常：", ex);
			return "数据库操作失败";
		}
	}
	
	@FileProvider
	public DownloadFile accquireTransportLineRangeTemplate(Map<String, String> parameter) throws IOException {
		InputStream is = DoradoContext.getCurrent().getServletContext().getResourceAsStream("/WEB-INF/templates/transport/transport_pay_line_range_template.xlsx");
		return new DownloadFile("运输路线梯度报价导入模板.xlsx", is);
	}
	
	//---------------------------------------------------------------------------------------------------------------------------------
	/**
	 * 导入运输梯度模板
	 * @param file
	 * @param parameter
	 * @return
	 * @throws Exception
	 */
	@FileResolver
	public Map<String, Object> importTransportLineRange(UploadFile file, Map<String, Object> parameter) throws Exception {
		
		//获取当前的id
		String id=(String)parameter.get("id");
		
		Map<String, Object> map = new HashMap<String, Object>();
		// 校验信息（报错提示）
		List<ErrorMessageVo> infoList = new ArrayList<ErrorMessageVo>();
		ErrorMessageVo errorVo = null;
		
		// 导入成功返回模板信息
		List<PriceTransportPayLineRangeEntity> lineRangeList = new ArrayList<PriceTransportPayLineRangeEntity>();
		// 当期时间
		Timestamp currentTime = JAppContext.currentTimestamp();
		String userName = JAppContext.currentUserName();
		try {
			BaseDataType bs = new TransportLineRangeImportTemplateDataType();
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
			lineRangeList = readExcelProduct(file,bs);
			if (null == lineRangeList || lineRangeList.size() <= 0) {
				errorVo = new ErrorMessageVo();
				errorVo.setMsg("导入的Excel数据为空或者数据格式不对!");
				infoList.add(errorVo);
				map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
				return map;
			}
			
			String currentNo = SequenceGenerator.uuidOf36String("t");// 当前操作ID
			/*condition.put("tempid", currentNo);*/
			
			// 模板信息必填项校验
			map = impExcelCheckInfoProduct(infoList, lineRangeList, map, currentNo);
			if (map.get(ConstantInterface.ImportExcelStatus.IMP_ERROR) != null) { // 有基本的错误信息(必填，数据类型不正确)
				return map;
			}
			// 获得初步校验后和转换后的数据
			lineRangeList = (List<PriceTransportPayLineRangeEntity>) map.get(ConstantInterface.ImportExcelStatus.IMP_SUCC);
			
			
			//设置属性
			for(int i=0;i<lineRangeList.size();i++){
				PriceTransportPayLineRangeEntity lineRange = lineRangeList.get(i);
				String billNo = id;
				lineRange.setLineId(billNo);
				lineRange.setDelFlag("0");// 设置为未作废
				lineRange.setCreator(userName);
				lineRange.setCreateTime(currentTime);
				lineRange.setLastModifier(userName);
				lineRange.setLastModifyTime(currentTime);
			}
			//插入正式表
			int insertNum = priceTransportPayLineRangeService.saveList(lineRangeList);
			if (insertNum <= 0) {
				errorVo = new ErrorMessageVo();
				errorVo.setMsg("存档运输路线梯度报价失败!");
				infoList.add(errorVo);
				map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
				return map;
			}else{
				map.put(ConstantInterface.ImportExcelStatus.IMP_SUCC, "0");
				try{
					PubRecordLogEntity model=new PubRecordLogEntity();
					model.setBizType(RecordLogBizTypeEnum.PRICE.getCode());
					model.setNewData("");
					model.setOldData("");
					model.setOperateDesc("导入应付运输报价,共计【"+insertNum+"】条");
					model.setOperatePerson(JAppContext.currentUserName());
					model.setOperateTable("price_out_transport_line");
					model.setOperateTime(JAppContext.currentTimestamp());
					model.setOperateType(RecordLogOperateType.IMPORT.getCode());
					model.setRemark("");
					model.setOperateTableKey("");
					model.setUrlName(RecordLogUrlNameEnum.OUT_TRANSPORT_BASE_PRICE.getCode());
					pubRecordLogService.AddRecordLog(model);
				}catch(Exception e){
					logger.error("记录日志失败,失败原因:", e);
				}
				return map;
			}
			
		} catch (Exception e) {
		    logger.error("异常：", e);
			//写入日志
			bmsErrorLogInfoService.insertLog(this.getClass().getSimpleName(),Thread.currentThread().getStackTrace()[1].getMethodName(), "", e.toString());
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
	private List<PriceTransportPayLineRangeEntity> readExcelProduct(UploadFile file,BaseDataType bs) {
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
			List<PriceTransportPayLineRangeEntity> lineList = Lists.newArrayList();
			for (Map<String, String> data : datas) {
				PriceTransportPayLineRangeEntity p = (PriceTransportPayLineRangeEntity) BeanToMapUtil.convertMapNull(PriceTransportPayLineRangeEntity.class, data);
				lineList.add(p);
			}
			return lineList;
		} catch (Exception e) {
			//写入日志
			bmsErrorLogInfoService.insertLog(this.getClass().getSimpleName(),Thread.currentThread().getStackTrace()[1].getMethodName(), "", e.toString());
			logger.error("异常：", e);
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
	private Map<String, Object> impExcelCheckInfoProduct(List<ErrorMessageVo> infoList, List<PriceTransportPayLineRangeEntity> rangeList, Map<String, Object> map, String currentNo) {
		int lineNo = 0;
		for (int i = 0; i < rangeList.size(); i++) {
			PriceTransportPayLineRangeEntity range = rangeList.get(i);
			lineNo = lineNo+1;
			
			//判断温度类型是否在时效表中维护
			String temperatureTypeName= range.getTemperatureTypeName();
			if(StringUtils.isNotBlank(temperatureTypeName)){
				Map<String, Object> param = new HashMap<String, Object>();
				param.put("typeCode", "TEMPERATURE_TYPE");
				param.put("codeName", temperatureTypeName);
				List<SystemCodeEntity> temperatureList = systemCodeService.queryCodeList(param);
				if(temperatureList==null || temperatureList.size()==0){
					setMessage(infoList, lineNo,"该温度类型没有在表中维护!");
				}else{
					range.setTemperatureTypeCode(temperatureList.get(0).getCode());
				}
			}
			
			//判断品类是否在字典表中维护
			String productTypeName=range.getProductTypeName();
			if(StringUtils.isNotBlank(productTypeName)){
				Map<String, Object> param=new HashMap<String, Object>();
				param.put("typeCode", "PRODUCT_CATEGORY_TYPE");
				param.put("codeName", productTypeName);
				List<SystemCodeEntity> productTypeList = systemCodeService.queryCodeList(param);
				if(productTypeList==null || productTypeList.size()==0){
					setMessage(infoList, lineNo,"该品类没有在表中维护!");
				}else{
					range.setProductTypeCode(productTypeList.get(0).getCode());
				}
			}
			
			//判断车型是否在字典表中维护
			String carModelName=range.getCarModelName();
			if(StringUtils.isNotBlank(carModelName)){
				Map<String, Object> param=new HashMap<String, Object>();
				param.put("typeCode", "MOTORCYCLE_TYPE");
				param.put("codeName", carModelName);
				List<SystemCodeEntity> carModelList = systemCodeService.queryCodeList(param);
				if(carModelList==null || carModelList.size()==0){
					setMessage(infoList, lineNo,"该车型没有在表中维护!");
				}else{
					range.setCarModelCode(carModelList.get(0).getCode());
				}
			}
			
			//判断【最低起运】不能为非数字
			if(range.getMinWeightShipment() !=null && !isNumber(range.getMinWeightShipment())){
				setMessage(infoList, lineNo,"【最低起运】不能为非数字!");
			}
			//判断【距离下限】不能为非数字
			if(range.getMinDistance() !=null && !isNumber(range.getMinDistance())){
				setMessage(infoList, lineNo,"【距离下限】不能为非数字!");
			}
			//判断【距离上限】不能为非数字
			if(range.getMaxDistance() !=null && !isNumber(range.getMaxDistance())){
				setMessage(infoList, lineNo,"【距离上限】不能为非数字!");
			}
			
			//判断【时间下限】不能为非数字
			if(range.getMinTime() !=null && !isNumber(range.getMinTime())){
				setMessage(infoList, lineNo,"【时间下限】不能为非数字!");
			}
			//判断【时间上限】不能为非数字
			if(range.getMaxTime() !=null && !isNumber(range.getMaxTime())){
				setMessage(infoList, lineNo,"【时间上限】不能为非数字!");
			}
			
			//判断【重量下限】不能为非数字
			if(range.getWeightLowerLimit() !=null && !isNumber(range.getWeightLowerLimit())){
				setMessage(infoList, lineNo,"【重量下限】不能为非数字!");
			}
			//判断【重量上限】不能为非数字
			if(range.getWeightUpperLimit() !=null && !isNumber(range.getWeightUpperLimit())){
				setMessage(infoList, lineNo,"【重量上限】不能为非数字!");
			}
			
			//判断【数量下限】不能为非数字
			if(range.getNumLowerLimit()!=null && !isNumber(range.getNumLowerLimit())){
				setMessage(infoList, lineNo,"【数量下限】不能为非数字!");
			}
			
			//判断【数量上限】不能为非数字
			if(range.getNumUpperLimit()!=null && !isNumber(range.getNumUpperLimit())){
				setMessage(infoList, lineNo,"【数量上限】不能为非数字!");
			}
			//判断【SKU下限】不能为非数字
			if(range.getSkuLowerLimit()!=null && !isNumber(range.getSkuLowerLimit())){
				setMessage(infoList, lineNo,"【SKU下限】不能为非数字!");
			}
			//判断【SKU上限】不能为非数字
			if(range.getSkuUpperLimit()!=null && !isNumber(range.getSkuUpperLimit())){
				setMessage(infoList, lineNo,"【SKU上限】不能为非数字!");
			}
			
			//判断【体积下限】不能为非数字
			if(range.getVolumeLowerLimit()!=null && !isNumber(range.getVolumeLowerLimit())){
				setMessage(infoList, lineNo,"【体积下限】不能为非数字!");
			}
			//判断【体积上限】不能为非数字
			if(range.getVolumeUpperLimit()!=null && !isNumber(range.getVolumeUpperLimit())){
				setMessage(infoList, lineNo,"【体积上限】不能为非数字!");
			}
			//判断【泡重】不能为非数字
			if(range.getBubbleWeight()!=null && !isNumber(range.getBubbleWeight())){
				setMessage(infoList, lineNo,"【泡重】为非数字!");
			}
			//判断【单价】不能为非数字
			if(range.getUnitPrice()!=null && !isNumber(range.getUnitPrice())){
				setMessage(infoList, lineNo,"【单价不能】为非数字!");
			}

			
			if (infoList != null && infoList.size() > 0) { // 有错误信息
				map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
			} else {
				map.put(ConstantInterface.ImportExcelStatus.IMP_SUCC, rangeList); // 无基本错误信息
			}
			
		}
		return map;
	}
	
	public static boolean isNumber(String str){  
        String reg = "^[0-9]+(.[0-9]+)?$";  
        return str.matches(reg);  
    } 
	
	public static boolean isNumber(Double data){  
		String str = String.valueOf(data);
        String reg = "^[0-9]+(.[0-9]+)?$";  
        return str.matches(reg);  
    }
	
	private void setMessage(List<ErrorMessageVo> infoList, int lineNo,String msg) {
		ErrorMessageVo errorVo;
		errorVo =new ErrorMessageVo();
		errorVo.setLineNo(lineNo);
		errorVo.setMsg(msg);
		infoList.add(errorVo);
	}
}
