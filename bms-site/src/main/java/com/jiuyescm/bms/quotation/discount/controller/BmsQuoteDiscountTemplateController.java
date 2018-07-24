package com.jiuyescm.bms.quotation.discount.controller;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.data.provider.Page;
import com.bstek.dorado.uploader.UploadFile;
import com.bstek.dorado.uploader.annotation.FileResolver;
import com.bstek.dorado.web.DoradoContext;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jiuyescm.bms.base.dictionary.entity.SystemCodeEntity;
import com.jiuyescm.bms.common.entity.ErrorMessageVo;
import com.jiuyescm.bms.common.enumtype.RecordLogBizTypeEnum;
import com.jiuyescm.bms.common.enumtype.RecordLogOperateType;
import com.jiuyescm.bms.common.enumtype.RecordLogUrlNameEnum;
import com.jiuyescm.bms.common.tool.Session;
import com.jiuyescm.bms.pub.PubRecordLogEntity;
import com.jiuyescm.bms.quotation.discount.entity.BmsQuoteDiscountDetailEntity;
import com.jiuyescm.bms.quotation.discount.entity.BmsQuoteDiscountTemplateEntity;
import com.jiuyescm.bms.quotation.discount.service.IBmsQuoteDiscountDetailService;
import com.jiuyescm.bms.quotation.discount.service.IBmsQuoteDiscountTemplateService;
import com.jiuyescm.bms.quotation.dispatch.entity.vo.BmsQuoteDispatchDetailVo;
import com.jiuyescm.bms.quotation.dispatch.service.IBmsQuoteDispatchDetailService;
import com.jiuyescm.cfm.common.JAppContext;
import com.jiuyescm.cfm.common.sequence.SequenceGenerator;
import com.jiuyescm.common.ConstantInterface;
import com.jiuyescm.common.utils.BeanToMapUtil;
import com.jiuyescm.common.utils.FileOperationUtil;
import com.jiuyescm.common.utils.excel.FileReaderFactory;
import com.jiuyescm.common.utils.excel.IFileReader;
import com.jiuyescm.common.utils.upload.BaseDataType;
import com.jiuyescm.common.utils.upload.DataProperty;
import com.jiuyescm.common.utils.upload.DispatchQuoteTemplateDataType;
import com.jiuyescm.framework.lock.LockCallback;
import com.jiuyescm.framework.lock.LockCantObtainException;
import com.jiuyescm.framework.lock.LockInsideExecutedException;
import com.jiuyescm.mdm.customer.vo.ErrorMsgVo;
import com.jiuyescm.mdm.customer.vo.RegionEncapVo;
import com.jiuyescm.mdm.customer.vo.RegionVo;
import com.jiuyescm.mdm.warehouse.api.IWarehouseService;
import com.jiuyescm.mdm.warehouse.vo.WarehouseVo;

/**
 * ..Controller
 * @author wangchen
 * 
 */
@Controller("bmsQuoteDiscountTemplateController")
public class BmsQuoteDiscountTemplateController {

	private static final Logger logger = LoggerFactory.getLogger(BmsQuoteDiscountTemplateController.class.getName());

	@Autowired
	private IBmsQuoteDiscountTemplateService bmsQuoteDiscountTemplateService;
	
	@Autowired
	private IBmsQuoteDiscountDetailService bmsQuoteDiscountDetailService;
	
	@Autowired
	private IWarehouseService warehouseService;

	/**
	 * 根据id查询
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@DataProvider
	public BmsQuoteDiscountTemplateEntity findById(Long id) throws Exception {
		return bmsQuoteDiscountTemplateService.findById(id);
	}

	/**
	 * 分页查询
	 * @param page
	 * @param param
	 */
	@DataProvider
	public void query(Page<BmsQuoteDiscountTemplateEntity> page, Map<String, Object> param) {
		PageInfo<BmsQuoteDiscountTemplateEntity> pageInfo = bmsQuoteDiscountTemplateService.query(param, page.getPageNo(), page.getPageSize());
		if (pageInfo != null) {
			page.setEntities(pageInfo.getList());
			page.setEntityCount((int) pageInfo.getTotal());
		}
	}

	/**
	 * 保存
	 * @param entity
	 * @return
	 */
	@DataResolver
	public String save(BmsQuoteDiscountTemplateEntity entity) {
		if(Session.isMissing()){
			return "长时间未操作，用户已失效，请重新登录再试！";
		}else if(entity == null){
			return "页面传递参数有误！";
		}
		String username = JAppContext.currentUserName();
		Timestamp currentTime = new Timestamp(System.currentTimeMillis());
		if (null == entity.getId()) {
			try {			
				entity.setCreator(username);
				entity.setCreateTime(currentTime);
				entity.setDelFlag("0");
				bmsQuoteDiscountTemplateService.save(entity);
			} catch (Exception e) {
				logger.error("保存失败, ", e.getMessage());
				return "fail";
			}		
		} else {
			try {
				entity.setLastModifier(username);
				entity.setLastModifyTime(currentTime);
				bmsQuoteDiscountTemplateService.update(entity);
			} catch (Exception e) {
				logger.error("数据修改失败, ", e.getMessage());
				return "fail";
			}
			
		}
		return "SUCCESS";
	}

	/**
	 * 删除
	 * @param entity
	 */
	@DataResolver
	public void delete(BmsQuoteDiscountTemplateEntity entity) {
		List<BmsQuoteDiscountDetailEntity> boddList = bmsQuoteDiscountDetailService.queryByTemplateCode(entity.getTemplateCode());
		if (!boddList.isEmpty()) {
			for (BmsQuoteDiscountDetailEntity bmsQuoteDiscountDetailEntity : boddList) {
				bmsQuoteDiscountDetailEntity.setDelFlag("1");
				bmsQuoteDiscountDetailEntity.setLastModifier(JAppContext.currentUserName());
				bmsQuoteDiscountDetailEntity.setLastModifyTime(JAppContext.currentTimestamp());
				bmsQuoteDiscountDetailService.delete(bmsQuoteDiscountDetailEntity);
			}
		}
		try {
			entity.setDelFlag("1");
			entity.setLastModifier(JAppContext.currentUserName());
			entity.setLastModifyTime(JAppContext.currentTimestamp());
			bmsQuoteDiscountTemplateService.delete(entity);
		} catch (Exception e) {
			logger.error("删除失败，", e.getMessage());
		}		
	}
	
//	/**
//	 * 导入模板
//	 */
//	@FileResolver
//	public Map<String, Object> importExcelTemplate(UploadFile file, Map<String, Object> parameter) throws Exception {
//		final Map<String, Object> map = new HashMap<String, Object>();
//		// 校验信息（报错提示）
//		final List<ErrorMessageVo> infoList = new ArrayList<ErrorMessageVo>();
//		Map<String, Object> re=importTemplate(file,parameter,infoList,map);
//		return re;
//	}
//	
//	@SuppressWarnings("unchecked")
//	private Map<String, Object> importTemplate(UploadFile file, Map<String, Object> parameter,List<ErrorMessageVo> infoList,Map<String, Object> map){
//		DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 10);
//		long starTime=System.currentTimeMillis();	//开始时间
//		
//		ErrorMessageVo errorVo = null;
//		//获取当前的id
//		String templateCode = (String)parameter.get("templateCode");
//
//		// 导入成功返回模板信息
//		List<BmsQuoteDispatchDetailVo> templateList = new ArrayList<BmsQuoteDispatchDetailVo>();
//		// 当期时间
//		Timestamp currentTime = JAppContext.currentTimestamp();
//		String userName = JAppContext.currentUserName();
//		try {
//			BaseDataType bs = new DispatchQuoteTemplateDataType();
//			// 检查导入模板是否正确
//			boolean isTemplate = FileOperationUtil.checkExcelTitle(file, bs);
//			if (!isTemplate) {
//				errorVo = new ErrorMessageVo();
//				errorVo.setMsg("Excel导入格式错误请参考标准模板检查!");
//				infoList.add(errorVo);
//				map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
//				return map;
//			}
//
//			logger.info("验证列名耗时："+FileOperationUtil.getOperationTime(starTime));
//			DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 100);
//			starTime=System.currentTimeMillis();	//开始时间
//			
//			// 解析Excel
//			templateList = readExcelProduct(file,bs);
//			if (null == templateList || templateList.size() <= 0) {
//				errorVo = new ErrorMessageVo();
//				errorVo.setMsg("导入的Excel数据为空或者数据格式不对!");
//				infoList.add(errorVo);
//				map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
//				return map;
//			}
//			
//			logger.info("excel解析耗时："+FileOperationUtil.getOperationTime(starTime));
//			DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 600);
//			starTime=System.currentTimeMillis();	//开始时间
//			
//			String currentNo = SequenceGenerator.uuidOf36String("t");// 当前操作ID
//			/*condition.put("tempid", currentNo);*/
//			
//			//仓库信息
//			List<WarehouseVo>  wareHouselist  = warehouseService.queryAllWarehouse();		
//			Map<String,Object> param = new HashMap<String,Object>();			
//			param.put("wareHouselist", wareHouselist);
//
//			// 模板信息必填项校验
//			map = impExcelCheckInfoProduct(infoList, templateList, map, currentNo,param);
//			if (map.get(ConstantInterface.ImportExcelStatus.IMP_ERROR) != null) { // 有基本的错误信息(必填，数据类型不正确)
//				return map;
//			}
//			
//			logger.info("必填项验证耗时："+FileOperationUtil.getOperationTime(starTime));
//			DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 700);
//			starTime=System.currentTimeMillis();	//开始时间
//			
//			// 获得初步校验后和转换后的数据
//			templateList = (List<BmsQuoteDispatchDetailVo>) map.get(ConstantInterface.ImportExcelStatus.IMP_SUCC);
//			
//			//重复校验(包括Excel校验和数据库校验)
//			Map<String, Object> parame=new HashMap<String, Object>();
//			parame.put("templateCode", templateCode);
//			List<BmsQuoteDispatchDetailVo> orgList=getOrgList(parame);
//			List<BmsQuoteDispatchDetailVo> teList=templateList;
//			if (null != orgList) {
//				Map<String,Object> mapCheck=super.compareWithImportLineData(orgList, teList, infoList,getKeyDataProperty(), map);
//				if (mapCheck.get(ConstantInterface.ImportExcelStatus.IMP_ERROR) != null) {
//					return map;
//				}
//			}
//			DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 800);
//			
//			//设置属性
//			for(BmsQuoteDispatchDetailVo p : templateList){
//				p.setTemplateCode(templateCode);
////				p.setMark(dealPriceRuleNo(p));// 报价规则编号
//				p.setDelFlag("0");// 设置为未作废
//				p.setCreator(userName);
//				p.setCreateTime(currentTime);
//			}
//			//插入正式表
//			int insertNum = 0;
//			try {
//				insertNum = bmsQuoteDispatchDetailService.insertBatchTmp(templateList);
//				DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 900);
//			} catch (Exception e) {
//				if((e.getMessage().indexOf("Duplicate entry"))>0){
//					errorVo = new ErrorMessageVo();
//					errorVo.setMsg("违反唯一性约束,插入数据失败!");
//					infoList.add(errorVo);
//					map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
//					return map;
//				}
//				//写入日志
//				bmsErrorLogInfoService.insertLog(this.getClass().getSimpleName(),Thread.currentThread().getStackTrace()[1].getMethodName(), "", e.toString());
//			}
//			
//			if (insertNum <= 0) {
//				errorVo = new ErrorMessageVo();
//				errorVo.setMsg("插入正式表失败!");
//				infoList.add(errorVo);
//				map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
//				return map;
//			}else{
//				DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 1000);
//				map.put(ConstantInterface.ImportExcelStatus.IMP_SUCC, "0");
//				logger.info("写入表耗时："+FileOperationUtil.getOperationTime(starTime));
//				try{
//					PubRecordLogEntity model=new PubRecordLogEntity();
//					model.setBizType(RecordLogBizTypeEnum.PRICE.getCode());
//					model.setNewData("");
//					model.setOldData("");
//					model.setOperateDesc("导入宅配报价对应关系,共计【"+insertNum+"】条");
//					model.setOperatePerson(JAppContext.currentUserName());
//					model.setOperateTable("price_dispatch_detail");
//					model.setOperateTime(JAppContext.currentTimestamp());
//					model.setOperateType(RecordLogOperateType.IMPORT.getCode());
//					model.setRemark("");
//					model.setOperateTableKey("");
//					model.setUrlName(RecordLogUrlNameEnum.IN_DELIVER_BASE_PRICE.getCode());
//					pubRecordLogService.AddRecordLog(model);
//				}catch(Exception e){
//					logger.error("记录日志失败,失败原因:"+e.getMessage());
//				}
//			}
//		} catch (Exception e) {
//			//写入日志
//			bmsErrorLogInfoService.insertLog(this.getClass().getSimpleName(),Thread.currentThread().getStackTrace()[1].getMethodName(), "", e.toString());
//			e.printStackTrace();
//		}
//	
//		return map;
//	}
//	
//	/**
//	 * 读取Excel中的数据（不包含列名）
//	 * 
//	 * @param file
//	 * @return
//	 */
//	private List<BmsQuoteDispatchDetailVo> readExcelProduct(UploadFile file,BaseDataType bs) {
//		String fileSuffix = StringUtils.substringAfterLast(file.getFileName(), ".");
//		IFileReader reader = FileReaderFactory.getFileReader(fileSuffix);
//		try {
//			List<Map<String, String>> list = reader.getFileContent(file.getInputStream());
//			List<Map<String, String>> datas = Lists.newArrayList();
//			//BaseDataType ddt = new DispatchTemplateDataType();
//			List<DataProperty> props = bs.getDataProps();
//			for (Map<String, String> map : list) {
//				Map<String, String> data = Maps.newHashMap();
//				for (DataProperty prop : props) {
//					
//					data.put(prop.getPropertyId(), map.get(prop.getPropertyName().toLowerCase()));
//				}
//				datas.add(data);
//			}
//			List<BmsQuoteDispatchDetailVo> productList = Lists.newArrayList();
//			for (Map<String, String> data : datas) {
//				BmsQuoteDispatchDetailVo p = (BmsQuoteDispatchDetailVo) BeanToMapUtil.convertMapNull(BmsQuoteDispatchDetailVo.class, data);
//				productList.add(p);
//			}
//			return productList;
//		} catch (Exception e) {
//			//写入日志
//			bmsErrorLogInfoService.insertLog(this.getClass().getSimpleName(),Thread.currentThread().getStackTrace()[1].getMethodName(), "", e.toString());
//			
//			e.printStackTrace();
//		}
//		return null;
//	}
//	
//	/**
//	 * 校验Excel里面的内容合法性
//	 * 
//	 * @param infoList
//	 * @param list
//	 * @param map
//	 * @param objList
//	 * @param currentNo
//	 * @return
//	 */
//	@SuppressWarnings("unchecked")
//	private Map<String, Object> impExcelCheckInfoProduct(List<ErrorMessageVo> infoList, List<BmsQuoteDispatchDetailVo> prodList, Map<String, Object> map, String currentNo,Map<String,Object> param) {
//		String priceType = (String)param.get("priceType");
//		//===================================目的校验======================================================================
//		//判断省市区存不存在
//		List<RegionVo> regionList=new ArrayList<RegionVo>();
//		int line=1;
//		for (int i = 0; i < prodList.size(); i++) {
//			//当前行号
//			line=line+1;	
//			BmsQuoteDispatchDetailVo p = prodList.get(i);
//			RegionVo regionVo=new RegionVo();
//			regionVo.setProvince(p.getProvinceName());
//			regionVo.setCity(p.getCityName());
//			regionVo.setDistrict(p.getAreaName());
//			regionVo.setLineNo(line);
//			
//			regionList.add(regionVo);
//		}
//		
//		RegionEncapVo regionEncapVo=addressService.matchStandardByAlias(regionList);
//		//获取此时的正确信息和报错信息
//		List<ErrorMsgVo> errorMsgList=regionEncapVo.getErrorMsgList();
//		List<RegionVo> correctRegionList=regionEncapVo.getRegionList();
//		
//		//如匹配到到名称则将code设值
//		if(errorMsgList.size()<=0){
//			for(RegionVo region:correctRegionList){
//				for(BmsQuoteDispatchDetailVo price:prodList){
//					if(StringUtils.isNotBlank(region.getProvince()) && StringUtils.isNotBlank(price.getProvinceName()) && region.getProvince().equals(price.getProvinceName())){
//						price.setProvinceId(region.getProvincecode());
//						if(StringUtils.isNotBlank(region.getCity()) && StringUtils.isNotBlank(price.getCityName()) && region.getCity().equals(price.getCityName())){
//							price.setCityId(region.getCitycode());
//							if(StringUtils.isNotBlank(region.getDistrict()) && StringUtils.isNotBlank(price.getAreaName()) && region.getDistrict().equals(price.getAreaName())){
//								price.setAreaId(region.getDistrictcode());		
//							}
//						}
//					}
//				}
//			}
//		}else{
//			for(ErrorMsgVo error:errorMsgList){
//				setMessage(infoList, error.getLineNo(),error.getErrorMsg());
//			}
//		}
//		
//		if (infoList != null && infoList.size() > 0) { // 有错误信息
//			map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
//			return map;
//		} 
//		
//		//==============================================================================================================
//		int lineNo = 1;
//		List<WarehouseVo> wareHouselist = (List<WarehouseVo>)param.get("wareHouselist");
//		//获取到此时的温度类型
//		List<SystemCodeEntity> temperatureList = systemCodeService.findEnumList("TEMPERATURE_TYPE");
//		for (int i = 0; i < prodList.size(); i++) {
//			BmsQuoteDispatchDetailVo p=prodList.get(i);
//			lineNo=lineNo+1;				
//			//判断仓库id是否在仓库表中维护 并将此仓库返回,将id回填			
//			for(WarehouseVo entity:wareHouselist){
//				if(entity.getWarehousename().equals(p.getStartWarehouseName())){
//					p.setStartWarehouseId(entity.getWarehouseid());
//					break;
//				}
//			}
//			if(StringUtils.isBlank(p.getStartWarehouseId())){
//				setMessage(infoList, lineNo,"仓库id没有在仓库表中维护!");
//			}
//			
//			// 非空校验
//			String infoNull = checkInfoNull(p);
//			if (StringUtils.isNotBlank(infoNull)) {
//				setMessage(infoList, lineNo, infoNull);
//			}
//			// 单价、首重、续重校验
//			String weightUnitPrice = checkWeightUnitPrice(p);
//			if (StringUtils.isNotBlank(weightUnitPrice)) {
//				setMessage(infoList, lineNo, weightUnitPrice);
//			}
//			
//			// 报价内容校验
//			if ("GENERAL".equals(priceType)) {// 常规
//				String commonInfo = checkCommonNotNull(p);
//				if (StringUtils.isNotBlank(commonInfo)) {
//					setMessage(infoList, lineNo, commonInfo);
//				}
//			}else if ("PRODUCT_CASE".equals(priceType)) {// 特殊
//				String specialInfo = checkSpecialNotNull(p);
//				if (StringUtils.isNotBlank(specialInfo)) {
//					setMessage(infoList, lineNo, specialInfo);
//				}
//			}else if ("TEMPERATURE_CASE".equals(priceType)) {// 温度
//				String temperatureInfo = checkTempertureNotNull(p);
//				if (StringUtils.isNotBlank(temperatureInfo)) {
//					setMessage(infoList, lineNo, temperatureInfo);
//				}
//				
//				//判断此时的温度
//				String temperature = p.getTemperatureTypeCode();
//				if(StringUtils.isNotBlank(temperature)){
//					for(SystemCodeEntity entity:temperatureList){
//						if(entity.getCodeName().equals(temperature)){
//							p.setTemperatureTypeCode(entity.getCode());
//							break;
//						}
//					}
//					
//					if(p.getTemperatureTypeCode().equals(temperature)){
//						setMessage(infoList, lineNo,"温度["+p.getTemperatureTypeCode()+"]没有在表中维护!");
//					}
//				}
//			}
//			
//			/*//判断时效信息是否在时效表中维护
//			String time=p.getTimeliness();
//			if(StringUtils.isNotBlank(time)){
//				Map<String, Object> param1=new HashMap<String, Object>();
//				param1.put("typeCode", "DISPATCH_TIME");
//				param1.put("codeName", time);
//				List<SystemCodeEntity> timelessList=systemCodeService.queryCodeList(param1);
//				if(timelessList==null || timelessList.size()==0){
//					setMessage(infoList, lineNo,"时效没有在表中维护!");
//				}else{
//					p.setTimeliness(timelessList.get(0).getCode());
//				}
//			}
//			//判断地域类型是否在字典表中维护
//			//如果是顺丰配送
//			String areaTypeCode=p.getAreaTypeCode();
//			if(StringUtils.isNotBlank(areaTypeCode)){
//				Map<String, Object> areaTypeparam=new HashMap<String, Object>();
//				areaTypeparam.put("typeCode", "DISPATCH_AREA_TYPE");
//				areaTypeparam.put("codeName", areaTypeCode);
//				List<SystemCodeEntity> areaTypeList=systemCodeService.queryCodeList(areaTypeparam);
//				if(areaTypeList==null || areaTypeList.size()==0){
//					setMessage(infoList, lineNo,"地域类型没有在表中维护!");
//				}else{
//					p.setAreaTypeCode(areaTypeList.get(0).getCode());
//				}
//			}
//			
//			// 判断宅配商信息
//			Map<String, String> deliveryMap = getDeliverMap();
//			if (StringUtils.isNotBlank(p.getDeliverid())) {
//				if (deliveryMap.containsKey(p.getDeliverid())) {
//					String deliverId = deliveryMap.get(p.getDeliverid());
//					p.setDeliverid(deliverId);
//				}else {
//					setMessage(infoList, lineNo, "宅配商简称没有在表中维护!");
//				}
//			}*/
//				
//			if (infoList != null && infoList.size() > 0) { // 有错误信息
//				map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
//			} else {
//				map.put(ConstantInterface.ImportExcelStatus.IMP_SUCC, prodList); // 无基本错误信息
//			}
//			
//		}
//		return map;
//	}
//	
//	public List<String> getKeyDataProperty() {
//		List<String> list=new ArrayList<String>();
//		list.add("startWarehouseId");//仓库名称
//		list.add("provinceId");//省份
//		list.add("cityId");//市
//		list.add("areaId");//区
//		list.add("weightDown");
//		list.add("weightUp");
//		list.add("unitPrice");
//		list.add("firstWeight");
//		list.add("firstWeightPrice");
//		list.add("continuedWeight");
//		list.add("continuedPrice");
//		list.add("timeliness");//时效
//		list.add("temperatureTypeCode");//温度类型
//		list.add("areaTypeCode");//地域类型
//		list.add("productCase");
//		list.add("deliverid");
//		return list;
//	}
}
