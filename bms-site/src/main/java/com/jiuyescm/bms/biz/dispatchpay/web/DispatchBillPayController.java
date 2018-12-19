
package com.jiuyescm.bms.biz.dispatchpay.web;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

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
import com.jiuyescm.bms.biz.dispatch.entity.BizDispatchBillPayEntity;
import com.jiuyescm.bms.biz.dispatch.service.IBizDispatchBillPayService;
import com.jiuyescm.bms.common.constants.ExceptionConstant;
import com.jiuyescm.bms.common.constants.MessageConstant;
import com.jiuyescm.bms.common.entity.ErrorMessageVo;
import com.jiuyescm.bms.common.log.entity.BmsErrorLogInfoEntity;
import com.jiuyescm.bms.common.log.service.IBmsErrorLogInfoService;
import com.jiuyescm.bms.common.sequence.service.SequenceService;
import com.jiuyescm.bms.common.tool.Session;
import com.jiuyescm.bms.common.tool.Tools;
import com.jiuyescm.bms.fees.out.dispatch.entity.FeesPayDispatchEntity;
import com.jiuyescm.bms.fees.out.dispatch.service.IFeesPayDispatchService;
import com.jiuyescm.bms.quotation.contract.service.IPriceContractService;
import com.jiuyescm.bms.quotation.dispatch.service.IPriceDispatchService;
import com.jiuyescm.bms.quotation.dispatch.service.IPriceDispatchTemplateService;
import com.jiuyescm.bs.util.ExportUtil;
import com.jiuyescm.cfm.common.JAppContext;
import com.jiuyescm.common.ConstantInterface;
import com.jiuyescm.common.utils.BeanToMapUtil;
import com.jiuyescm.common.utils.FileOperationUtil;
import com.jiuyescm.common.utils.excel.FileReaderFactory;
import com.jiuyescm.common.utils.excel.IFileReader;
import com.jiuyescm.common.utils.upload.BaseDataType;
import com.jiuyescm.common.utils.upload.DataProperty;
import com.jiuyescm.common.utils.upload.DispatchWayBillDataType;
import com.jiuyescm.exception.BizException;
import com.jiuyescm.framework.lock.Lock;
import com.jiuyescm.framework.lock.LockCallback;
import com.jiuyescm.framework.lock.LockCantObtainException;
import com.jiuyescm.framework.lock.LockInsideExecutedException;
import com.jiuyescm.mdm.carrier.api.ICarrierService;
import com.jiuyescm.mdm.carrier.vo.CarrierVo;
import com.jiuyescm.mdm.customer.api.IAddressService;
import com.jiuyescm.mdm.customer.api.ICustomerService;
import com.jiuyescm.mdm.customer.vo.CustomerVo;
import com.jiuyescm.mdm.customer.vo.ErrorMsgVo;
import com.jiuyescm.mdm.customer.vo.RegionEncapVo;
import com.jiuyescm.mdm.customer.vo.RegionVo;
import com.jiuyescm.mdm.deliver.api.IDeliverService;
import com.jiuyescm.mdm.deliver.vo.DeliverVo;
import com.jiuyescm.mdm.warehouse.api.IWarehouseService;
import com.jiuyescm.mdm.warehouse.vo.WarehouseVo;

@Controller("dispatchBillPayController")
public class DispatchBillPayController{
	
	private static final Logger logger = Logger.getLogger(DispatchBillPayController.class.getName());
	
	@Resource
	private IBizDispatchBillPayService bizDispatchBillPayService;
	
	@Resource
	private ISystemCodeService systemCodeService;
	
	@Resource
	private IPriceContractService priceContractService;
	
	@Resource
	private IPriceDispatchTemplateService priceDispatchTemplateService;
	
	@Resource
	private IPriceDispatchService priceDispatchService;
	
	@Resource 
	private SequenceService sequenceService;
	
	@Autowired
	private IFeesPayDispatchService service;
	
	@Resource
	private IAddressService addressService;
	
	@Autowired
	private IWarehouseService warehouseService;
	
	@Autowired 
	private ICustomerService customerService;
	
	@Resource 
	private ICarrierService carrierService;
	
	@Resource
	private IDeliverService deliverService;
	
	@Resource
	private Lock lock;
	
	@Resource
	private IBmsErrorLogInfoService bmsErrorLogInfoService;
	
	@Autowired 
	private IAddressService omsAddressService;
	
	/**
	 * 分页查询
	 * 
	 * @param page
	 * @param param
	 */
	@DataProvider
	public void queryAll(Page<BizDispatchBillPayEntity> page, Map<String, Object> param) {
		if (param == null){
			param = new HashMap<String, Object>();
		}
		if (param.get("createTime") == null) {
			throw new BizException("创建时间不能为空！");
		}
		if (param.get("endTime") == null) {
			throw new BizException("结束时间不能位空！");
		}
		//物流商
		if(param.get("logistics") != null && StringUtils.equalsIgnoreCase(param.get("logistics").toString(), "ALL")){
			param.put("logistics", null);
		}
		PageInfo<BizDispatchBillPayEntity> pageInfo = bizDispatchBillPayService.queryAll(param, page.getPageNo(), page.getPageSize());
		if (pageInfo != null) {
			page.setEntities(pageInfo.getList());
			page.setEntityCount((int) pageInfo.getTotal());
		}
	}
	
	
	/**
	 * 作废表示
	 * @return
	 */
	@DataProvider
	public Map<String, String> getInvalidCalculate(String all) {
		Map<String, String> mapValue = new LinkedHashMap<String, String>();
		if ("ALL".equalsIgnoreCase(all)) {
			mapValue.put("999", "全部");
		}
		mapValue.put(ConstantInterface.Calculate.CALCULATE_NO, "未计算");
		mapValue.put(ConstantInterface.Calculate.CALCULATE_YES, "已计算");
		return mapValue;
	}
	
	@DataResolver
	public String adjustAll(Collection<BizDispatchBillPayEntity> datas){
		if(Session.isMissing()){
			return "长时间未操作，用户已失效，请重新登录再试！";
		}
		try{
			Timestamp nowdate = JAppContext.currentTimestamp();
			String userid=JAppContext.currentUserName();
			for(BizDispatchBillPayEntity temp:datas){
				//判断是否生成费用，判断费用的状态是否为未过账
				String feeid=temp.getFeesNo();
				if(StringUtils.isNotBlank(feeid)){
					Map<String,Object> aCondition=new HashMap<>();
					aCondition.put("feesNo", feeid);
					FeesPayDispatchEntity feesPayDispatchEntity = service.queryOne(aCondition);
					if (null != feesPayDispatchEntity ) {
						//获取此时的费用状态
						String status=String.valueOf(feesPayDispatchEntity.getStatus());
						if(status.equals("1")){
							return "该费用已过账，无法调整重量";
						}
					}
				}
				//此为修改业务数据  
				//业务数据有生成过费用，且没有过账的话,则允许调整重量,且调整完后,状态重置为未计算
				//如果没有过账的话,就允许调整重量,且调整完后,状态重置为未计算,定时任务重新扫描到并重新生成应收费用.
				temp.setIsCalculated("99");
				temp.setLastModifier(userid);
				temp.setLastModifyTime(nowdate);
				bizDispatchBillPayService.adjustBillPayEntity(temp);
			}
			return "保存成功";
		}catch(Exception e){
			//写入日志
			BmsErrorLogInfoEntity bmsErrorLogInfoEntity=new BmsErrorLogInfoEntity();
			bmsErrorLogInfoEntity.setClassName("DispatchBillPayController");
			bmsErrorLogInfoEntity.setMethodName("saveAll");
			bmsErrorLogInfoEntity.setErrorMsg(e.toString());
			bmsErrorLogInfoEntity.setCreateTime(JAppContext.currentTimestamp());
			bmsErrorLogInfoService.log(bmsErrorLogInfoEntity);	
			return "保存失败";
		}
	}
	/**
	 * 保存调整重量
	 * @param datas
	 */
	@DataResolver
	public String saveAll(Collection<BizDispatchBillPayEntity> datas){
		
		if(Session.isMissing()){
			return "长时间未操作，用户已失效，请重新登录再试！";
		}
		try {
			
			Timestamp nowdate = JAppContext.currentTimestamp();
			String userid=JAppContext.currentUserName();
			for(BizDispatchBillPayEntity temp:datas){
				//对操作类型进行判断
				//此为新增业务数据
				if(EntityState.NEW.equals(EntityUtils.getState(temp))){
					//判断该计费规则是否已存在
				}else if(EntityState.MODIFIED.equals(EntityUtils.getState(temp))){
				//判断是否生成费用，判断费用的状态是否为未过账
				String feeid=temp.getFeesNo();
				if(StringUtils.isNotBlank(feeid)){
					Map<String,Object> aCondition=new HashMap<>();
					aCondition.put("feesNo", feeid);
					FeesPayDispatchEntity feesPayDispatchEntity = service.queryOne(aCondition);
					if (null != feesPayDispatchEntity ) {
						//获取此时的费用状态
						String status=String.valueOf(feesPayDispatchEntity.getStatus());
						if(status.equals("1")){
							return "该费用已过账，无法调整重量";
						}
					}
				}
				//此为修改业务数据  
				//业务数据有生成过费用，且没有过账的话,则允许调整重量,且调整完后,状态重置为未计算
				//如果没有过账的话,就允许调整重量,且调整完后,状态重置为未计算,定时任务重新扫描到并重新生成应收费用.
				temp.setIsCalculated("99");
				temp.setLastModifier(userid);
				temp.setLastModifyTime(nowdate);
				bizDispatchBillPayService.updateBillEntity(temp);
				}
				
			}
			
			return "调整成功";
			
		} catch (Exception e) {
			//写入日志
			BmsErrorLogInfoEntity bmsErrorLogInfoEntity=new BmsErrorLogInfoEntity();
			bmsErrorLogInfoEntity.setClassName("DispatchBillPayController");
			bmsErrorLogInfoEntity.setMethodName("saveAll");
			bmsErrorLogInfoEntity.setErrorMsg(e.toString());
			bmsErrorLogInfoEntity.setCreateTime(JAppContext.currentTimestamp());
			bmsErrorLogInfoService.log(bmsErrorLogInfoEntity);	
			return "调整失败";
		}
		
	
	}
	
	
	/**
	 * 保存配送业务数据地址
	 * @param datas
	 */
	@DataResolver
	public String saveAddress(Collection<BizDispatchBillPayEntity> datas){
		
		if(Session.isMissing()){
			return "长时间未操作，用户已失效，请重新登录再试！";
		}
		try {
			
			Timestamp nowdate = JAppContext.currentTimestamp();
			String userid=JAppContext.currentUserName();
			for(BizDispatchBillPayEntity temp:datas){
				//对操作类型进行判断
				//此为新增业务数据
				if(EntityState.NEW.equals(EntityUtils.getState(temp))){
					//判断该计费规则是否已存在
				}else if(EntityState.MODIFIED.equals(EntityUtils.getState(temp))){
				//判断是否生成费用，判断费用的状态是否为未过账
				String feeid=temp.getFeesNo();
				if(StringUtils.isNotBlank(feeid)){
					Map<String,Object> aCondition=new HashMap<>();
					aCondition.put("feesNo", feeid);
					FeesPayDispatchEntity feesPayDispatchEntity = service.queryOne(aCondition);
					if (null != feesPayDispatchEntity) {
						//获取此时的费用状态
						String status=String.valueOf(feesPayDispatchEntity.getStatus());
						if(status.equals("1")){
							return "该费用已过账，无法调整省市区";
						}
					}
				}
				//此为修改业务数据  
				//业务数据有生成过费用，且没有过账的话,则允许调整重量,且调整完后,状态重置为未计算
				//如果没有过账的话,就允许调整重量,且调整完后,状态重置为未计算,定时任务重新扫描到并重新生成应收费用.
				temp.setIsCalculated("0");
				temp.setLastModifier(userid);
				temp.setLastModifyTime(nowdate);
				int result=bizDispatchBillPayService.updateBillEntity(temp);
				if(result<=0){
					return "调整省市区失败";
				}
			  }
				
			}
			
			return "调整省市区成功";
			
		} catch (Exception e) {
			//写入日志
			BmsErrorLogInfoEntity bmsErrorLogInfoEntity=new BmsErrorLogInfoEntity();
			bmsErrorLogInfoEntity.setClassName("DispatchBillPayController");
			bmsErrorLogInfoEntity.setMethodName("saveAddress");
			bmsErrorLogInfoEntity.setErrorMsg(e.toString());
			bmsErrorLogInfoEntity.setCreateTime(JAppContext.currentTimestamp());
			bmsErrorLogInfoService.log(bmsErrorLogInfoEntity);	
			return "调整省市区失败";
		}
	}
	
	
	/**
	 * 
	 * @param param
	 * @return Billed-已在账单中存在,不能重算,建议删除账单后重试;Calculated-已计算,是否继续重算;Error-系统错误;OK-可重算
	 */
	@Expose
	public Properties validRetry(Map<String, Object> param) {
		Properties ret = bizDispatchBillPayService.validRetry(param);
		return ret;
	}
	
	@Expose
	public String reCalculate(Map<String, Object> param){
		if(bizDispatchBillPayService.reCalculate(param) == 0){
			return "重算异常";
		}
		return "操作成功! 正在重算...";
	}
	
	/**
	 * 查询出所有的分组报价
	 * @param page
	 * @param parameter
	 */
	@DataProvider
	public void queryPriceCount(Page<BizDispatchBillPayEntity> page,Map<String,Object> parameter){
		PageInfo<BizDispatchBillPayEntity> pageInfo = bizDispatchBillPayService.queryAllPrice(parameter, page.getPageNo(), page.getPageSize());
		if (pageInfo != null) {
			page.setEntities(pageInfo.getList());
			page.setEntityCount((int) pageInfo.getTotal());
		}
	}
	
	/**
	 * 查询出所有的分组数据
	 * @param page
	 * @param parameter
	 */
	@DataProvider
	public void queryDataCount(Page<BizDispatchBillPayEntity> page,Map<String,Object> parameter){
		PageInfo<BizDispatchBillPayEntity> pageInfo = bizDispatchBillPayService.queryAllData(parameter, page.getPageNo(), page.getPageSize());
		if (pageInfo != null) {
			page.setEntities(pageInfo.getList());
			page.setEntityCount((int) pageInfo.getTotal());
		}
	}
	
	
	/**
	 * 导入模板下载
	 * @throws IOException
	 */
	@FileProvider
	public DownloadFile downloadTemplate(Map<String, String> parameter) throws IOException {
		InputStream is = DoradoContext.getCurrent().getServletContext().getResourceAsStream("/WEB-INF/templates/dispatch/dispatch_waybill_template.xlsx");
		return new DownloadFile("配送运单导入模板.xlsx", is);
	}
	
	
	/**
	 * 导入数据
	 * @param file
	 * @param parameter
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@FileResolver
	public Map<String, Object> importWayBill(UploadFile file, Map<String, Object> parameter) {
		DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 10);
		Map<String, Object> map = new HashMap<String, Object>();
		// 校验信息（报错提示）
		List<ErrorMessageVo> errorList = new ArrayList<ErrorMessageVo>();
		ErrorMessageVo errorVo = null;
		// 导入成功返回模板信息
		List<BizDispatchBillPayEntity> dataList = new ArrayList<BizDispatchBillPayEntity>();
		try {
			BaseDataType bs = new DispatchWayBillDataType();
			// 检查导入模板是否正确
			boolean isTemplate = FileOperationUtil.checkExcelTitle(file, bs);
			if (!isTemplate) {
				DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 999);
				errorVo = new ErrorMessageVo();
				errorVo.setMsg(MessageConstant.EXCEL_FORMAT_ERROR_MSG);
				errorList.add(errorVo);
				map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, errorList);
				return map;
			}
			DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 100);
			
			// 解析Excel
			dataList = readExcel(errorList,file,bs);
			if (null == dataList || dataList.size() <= 0) {
				DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 999);
				errorVo = new ErrorMessageVo();
				errorVo.setMsg(MessageConstant.EXCEL_NULL_MSG);
				errorList.add(errorVo);
				map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, errorList);
				return map;
			}
			DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 300);
			
			// 模板信息必填项校验
			map = impExcelCheckInfo(errorList, dataList, map);
			if (map.get(ConstantInterface.ImportExcelStatus.IMP_ERROR) != null) { // 有基本的错误信息(必填，数据类型不正确)
				DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 999);
				return map;
			}
			DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 700);
			
			// 获得初步校验后和转换后的数据
			dataList = (List<BizDispatchBillPayEntity>) map.get(ConstantInterface.ImportExcelStatus.IMP_SUCC);
			DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 800);
			
			//插入正式表
			int insertNum = bizDispatchBillPayService.saveList(dataList);
			DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 950);
			
			if (insertNum <= 0) {
				DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 999);
				errorVo = new ErrorMessageVo();
				errorVo.setMsg("插入正式表失败!");
				errorList.add(errorVo);
				map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, errorList);
				return map;
			}else{
				DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 1000);
				map.put(ConstantInterface.ImportExcelStatus.IMP_SUCC, "0");
				return map;
			}
			
		} catch (Exception e) {
			logger.error(ExceptionConstant.WAYBILL_SAVE_EXCEPTION_MSG, e);
			errorVo = new ErrorMessageVo();
			errorVo.setMsg("宅配运单数据操作异常");
			errorList.add(errorVo);
			map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, errorList);
			DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 999);
			//写入日志
			BmsErrorLogInfoEntity bmsErrorLogInfoEntity=new BmsErrorLogInfoEntity();
			bmsErrorLogInfoEntity.setClassName("DispatchBillPayController");
			bmsErrorLogInfoEntity.setMethodName("importWayBill");
			bmsErrorLogInfoEntity.setErrorMsg(e.toString());
			bmsErrorLogInfoEntity.setCreateTime(JAppContext.currentTimestamp());
			bmsErrorLogInfoService.log(bmsErrorLogInfoEntity);	
		} 
		
		return map;
	}
	
	
	/**
	 * 读取Excel中的数据（不包含列名）
	 * @param file
	 * @return
	 */
	private List<BizDispatchBillPayEntity> readExcel(List<ErrorMessageVo> errorList, UploadFile file,BaseDataType bs) {
		
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
			List<BizDispatchBillPayEntity> dataList = Lists.newArrayList();
			for (Map<String, String> data : datas) {
				BizDispatchBillPayEntity p = (BizDispatchBillPayEntity) BeanToMapUtil.convertMapNull(BizDispatchBillPayEntity.class, data);
				dataList.add(p);
			}
			return dataList;
		} catch (Exception e) {
			setMessage(errorList, 0, e.getMessage());
			//写入日志
			BmsErrorLogInfoEntity bmsErrorLogInfoEntity=new BmsErrorLogInfoEntity();
			bmsErrorLogInfoEntity.setClassName("DispatchBillPayController");
			bmsErrorLogInfoEntity.setMethodName("saveAll");
			bmsErrorLogInfoEntity.setErrorMsg(e.toString());
			bmsErrorLogInfoEntity.setCreateTime(JAppContext.currentTimestamp());
			bmsErrorLogInfoService.log(bmsErrorLogInfoEntity);	
			
		}
		return null;
	}
	
	
	/**
	 * 校验Excel里面的内容合法性
	 * @param errorList
	 * @param list
	 * @param map
	 * @param objList
	 * @param currentNo
	 * @return
	 */
	private Map<String, Object> impExcelCheckInfo(List<ErrorMessageVo> errorList, 
			List<BizDispatchBillPayEntity> dataList, Map<String, Object> map)throws Exception {
		//初始化参数
		int line=1;
		//仓库信息
		List<WarehouseVo>  wareHouselist  = warehouseService.queryAllWarehouse();
		//获取所有的温度类型
		List<SystemCodeEntity> temperatureList = systemCodeService.findEnumList("TEMPERATURE_TYPE");
		//获取所有的商家名称
		List<CustomerVo> customerList=getCustomerList();
		//获取所有的物流商信息
		List<CarrierVo> carrierList=carrierService.queryAllCarrier();
		//获取所有的宅配商信息
		List<DeliverVo> deliverList=deliverService.queryAllDeliver();
		
		//========================必填项校验=============================
		checkDataValidate(errorList,line,dataList);
		if (errorList != null && errorList.size() > 0) { // 有错误信息
			map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, errorList);
			return map;
		}
		
		//=======================校验该运单是否已存在==============================
		checkWaybill(errorList,line,dataList);
		if (errorList != null && errorList.size() > 0) { // 有错误信息
			map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, errorList);
			return map;
		}
		//===================================目的校验======================================================================
		//判断省市区存不存在
		List<RegionVo> regionList=new ArrayList<RegionVo>();
		for (int i = 0; i < dataList.size(); i++) {
			//当前行号
			line=line+1;	
			BizDispatchBillPayEntity p=dataList.get(i);
			RegionVo regionVo=new RegionVo();
			String provinceName="";
			String cityName="";
			String districtName="";
			provinceName=p.getReceiveProvinceName();
			cityName=p.getReceiveCityName();
			districtName=p.getReceiveDistrictName();
			regionVo.setProvince(provinceName);
			regionVo.setCity(cityName);
			regionVo.setDistrict(districtName);
			regionVo.setLineNo(line);
			regionList.add(regionVo);
		}
		
		RegionEncapVo regionEncapVo=addressService.matchStandardByAlias(regionList);
//		获取此时的正确信息和报错信息
		List<ErrorMsgVo> errorMsgList=regionEncapVo.getErrorMsgList();
		List<RegionVo> correctRegionList=regionEncapVo.getRegionList();
		
		//如匹配到到名称则将code设值
		if(errorMsgList.size()<=0){
			for(RegionVo region:correctRegionList){
				for(BizDispatchBillPayEntity bizEntity:dataList){
					if(StringUtils.isNotBlank(region.getProvince()) && StringUtils.isNotBlank(bizEntity.getReceiveProvinceName()) && region.getProvince().equals(bizEntity.getReceiveProvinceName())){
						bizEntity.setReceiveProvinceId(region.getProvincecode());
						if(StringUtils.isNotBlank(region.getCity()) && StringUtils.isNotBlank(bizEntity.getReceiveCityName()) && region.getCity().equals(bizEntity.getReceiveCityName())){
							bizEntity.setReceiveCityId(region.getCitycode());
							if(StringUtils.isNotBlank(region.getDistrict()) && StringUtils.isNotBlank(bizEntity.getReceiveDistrictName()) && region.getDistrict().equals(bizEntity.getReceiveDistrictName())){
								bizEntity.setReceiveDistrictId(region.getDistrictcode());		
							}
						}
					}
				}
			}
		}else{
			for(ErrorMsgVo error:errorMsgList){
				setMessage(errorList, error.getLineNo(),error.getErrorMsg());
			}
			
		}
			
		//==============================================================================================================
		int lineNo = 1;
		//获取到此时的温度类型
		for (int i = 0; i < dataList.size(); i++) {
			
			BizDispatchBillPayEntity p=dataList.get(i);
			//置为未计算
			p.setBigstatus("终结");
			p.setSmallstatus("已签收");
			p.setIsCalculated("0");
			p.setDelFlag("0");
			//将调整重量设置为重量
			p.setAdjustWeight(p.getTotalWeight());
			
			lineNo=lineNo+1;
			//=======================校验商家名称开始=======================
			for(CustomerVo entity:customerList){
				if(entity.getCustomername().equals(p.getCustomerName())){
					p.setCustomerid(entity.getCustomerid());
					break;
				}
			}
			if(StringUtils.isBlank(p.getCustomerid())){
				setMessage(errorList, lineNo,"商家名称"+p.getCustomerName()+"没有在商家表中维护!");
			}
			//=======================校验商家名称结束=======================
			
			
			//=======================校验物流商名称开始=======================
			for(CarrierVo entity:carrierList){
				if(entity.getName().equals(p.getCarrierName())){
					p.setCarrierId(entity.getCarrierid());
					break;
				}
			}
			if(StringUtils.isBlank(p.getCarrierId())){
				setMessage(errorList, lineNo,"物流商名称"+p.getCarrierName()+"没有在物流商表中维护!");
			}
			//=======================校验物流商名称结束=======================
			
			//=======================校验宅配商名称开始=======================
			for(DeliverVo entity:deliverList){
				if(p.getDeliverName().equals(entity.getDelivername())){
					p.setDeliverid(entity.getDeliverid());
					break;	
				}
			}
			if(StringUtils.isBlank(p.getDeliverid())){
				setMessage(errorList, lineNo,"宅配商名称"+p.getDeliverName()+"没有在宅配商表中维护!");
			}
			//=======================校验宅配商名称结束=======================
			
			
			//判断仓库id是否在仓库表中维护 并将此仓库返回,将id回填			
			for(WarehouseVo entity:wareHouselist){
				if(entity.getWarehousename().equals(p.getWarehouseName())){
					p.setWarehouseCode(entity.getWarehouseid());
					break;
				}
			}
			
			if(StringUtils.isBlank(p.getWarehouseCode())){
				setMessage(errorList, lineNo,"仓库名称"+p.getWarehouseName()+"没有在仓库表中维护!");
			}
			
			//判断此时的温度
			String temperature=p.getTemperatureTypeCode();
			if(temperature!=null && StringUtils.isNotBlank(temperature)){
				for(SystemCodeEntity entity:temperatureList){
					if(entity.getCodeName().equals(temperature)){
						p.setTemperatureTypeCode(entity.getCode());
						break;
					}
				}
				
				if(p.getTemperatureTypeCode().equals(temperature)){
					setMessage(errorList, lineNo,"温度["+p.getTemperatureTypeCode()+"]没有在表中维护!");
				}
			}
			
			if (errorList != null && errorList.size() > 0) { // 有错误信息
				map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, errorList);
			} else {
				map.put(ConstantInterface.ImportExcelStatus.IMP_SUCC, dataList); // 无基本错误信息
			}
		}
		return map;
	}
	
	/**
	 * 所有的商家信息
	 */
	public List<CustomerVo> getCustomerList(){
		//获取所有的商家名称
		PageInfo<CustomerVo> customerList=customerService.query(null, 0, Integer.MAX_VALUE);
		List<CustomerVo> cList=customerList.getList();
		return cList;	
	}
	
	public void checkDataValidate(List<ErrorMessageVo> errorList, int line,List<BizDispatchBillPayEntity> dataList){
		for(int i=0;i<dataList.size();i++){
			line++;
			BizDispatchBillPayEntity p=dataList.get(i);			
			//非空校验
			if(StringUtils.isBlank(p.getWarehouseName())){
				setMessage(errorList, line,"仓库名称不能为空!");
			}
			if(StringUtils.isBlank(p.getWaybillNo())){
				setMessage(errorList, line,"运单号不能为空!");
			}
			if(StringUtils.isBlank(p.getCustomerName())){
				setMessage(errorList, line,"商家名称不能为空!");
			}
			if(StringUtils.isBlank(p.getCarrierName())){
				setMessage(errorList, line,"物流商不能为空!");
			}
			if(StringUtils.isBlank(p.getDeliverName())){
				setMessage(errorList, line,"宅配商不能为空!");
			}
			if(p.getCreateTime()==null){
				setMessage(errorList, line,"创建时间不能为空!");
			}
			if(p.getTotalWeight()==null){
				setMessage(errorList, line,"重量不能为空!");
			}
			if(StringUtils.isBlank(p.getReceiveProvinceName())){
				setMessage(errorList, line,"始发省份不能为空!");
			}
		}
	}
	
	
	public boolean checkWaybill(List<ErrorMessageVo> errorList, int line,List<BizDispatchBillPayEntity> dataList){
		List<String> waybillNos=new ArrayList<String>();
		line=1;
		for(BizDispatchBillPayEntity bizEntity:dataList){
			line++;
			if(!waybillNos.contains(bizEntity.getWaybillNo())){
				waybillNos.add(bizEntity.getWaybillNo());
			}else{
				setMessage(errorList, line,"导入Excel中运单号"+bizEntity.getWaybillNo()+"重复!");
				return false;
			}
		}
		
		//判断是否已存在运单表中
		line=1;
		List<BizDispatchBillPayEntity> list=bizDispatchBillPayService.queryBizData(waybillNos);
		if(list!=null && list.size()>0){
			for(BizDispatchBillPayEntity b:list){
				line++;
				setMessage(errorList, line,"运单号"+b.getWaybillNo()+"重复!");
			}
			return false;
		}
		
		return true;
	}
	
	private void setMessage(List<ErrorMessageVo> errorList, int lineNo,String msg) {
		ErrorMessageVo errorVo;
		errorVo =new ErrorMessageVo();
		errorVo.setLineNo(lineNo);
		errorVo.setMsg(msg);
		errorList.add(errorVo);
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
	
	@FileResolver
	public Map<String, Object> importUpdateWeightLock(UploadFile file, Map<String, Object> parameter){
		DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 10);
		// 校验信息（报错提示）
		List<ErrorMessageVo> infoList = new ArrayList<ErrorMessageVo>();
		ErrorMessageVo errorVo = null;
		Map<String, Object> map = new HashMap<String, Object>();
		
		XSSFWorkbook xssfWorkbook = null;
		try {
			xssfWorkbook = new XSSFWorkbook(file.getInputStream());
		} catch (IOException e) {
			//写入日志
			BmsErrorLogInfoEntity bmsErrorLogInfoEntity=new BmsErrorLogInfoEntity();
			bmsErrorLogInfoEntity.setClassName("DispatchBillPayController");
			bmsErrorLogInfoEntity.setMethodName("importUpdateWeightLock");
			bmsErrorLogInfoEntity.setErrorMsg(e.toString());
			bmsErrorLogInfoEntity.setCreateTime(JAppContext.currentTimestamp());
			bmsErrorLogInfoService.log(bmsErrorLogInfoEntity);	
		}
		XSSFFormulaEvaluator  evaluator = new XSSFFormulaEvaluator(xssfWorkbook); 
		XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(0);
		
		if(xssfSheet==null)
			return null;
		
		int cols = xssfSheet.getRow(0).getPhysicalNumberOfCells();
		
		if(cols>7){
			DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 999);
			errorVo = new ErrorMessageVo();
			errorVo.setMsg("Excel导入格式错误请参考标准模板检查!");
			infoList.add(errorVo);
			map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
			return map;
		}
		
		DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 100);
		
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		Timestamp nowdate = JAppContext.currentTimestamp();
		String userid=JAppContext.currentUserName();
		
        for (int rowNum = 1;  rowNum <= xssfSheet.getLastRowNum(); rowNum++)
        {
			XSSFRow xssfRow = xssfSheet.getRow(rowNum);
			Map<String,Object> map0 = new HashMap<String,Object>();
			if(xssfRow==null){
				int lieshu = rowNum + 1;
				setMessage(infoList, rowNum+1,"第"+lieshu+"列空行！");
				map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
				return map;
			}
			String waybillNo = ExportUtil.getValue(xssfRow.getCell(0), evaluator);
			String adjustWeight = ExportUtil.getValue(xssfRow.getCell(4), evaluator);
			String adjustProvince=ExportUtil.getValue(xssfRow.getCell(1), evaluator);
			String adjustCity=ExportUtil.getValue(xssfRow.getCell(2), evaluator);
			String adjustDistrict=ExportUtil.getValue(xssfRow.getCell(3), evaluator);
			if(StringUtils.isBlank(adjustProvince)&&StringUtils.isBlank(adjustCity)&&StringUtils.isBlank(adjustDistrict)){
				
			}else{
				RegionVo vo = new RegionVo(replaceChar(adjustProvince), replaceChar(adjustCity),replaceChar(adjustDistrict));
				RegionVo matchVo = omsAddressService.queryNameByAlias(vo);
				if(StringUtils.isBlank(matchVo.getProvince())&&
						StringUtils.isBlank(matchVo.getCity())&&
						StringUtils.isBlank(matchVo.getDistrict())){
					String address=adjustProvince+"-"+adjustCity+"-"+adjustDistrict;
					setMessage(infoList, rowNum+1,"未匹配到地址:"+address+"");
					map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
					return map;
				}else{
					map0.put("adjustProvince", matchVo.getProvince());
					map0.put("adjustCity", matchVo.getCity());
					map0.put("adjustDistrict", matchVo.getDistrict());
				}	
			}
			if(StringUtils.isEmpty(waybillNo))
			{
				int lieshu = rowNum + 1;
				setMessage(infoList, rowNum+1,"第"+lieshu+"列运单号为空！");
				map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
				return map;
			}
			
			boolean isNumber = ExportUtil.isNumber(adjustWeight);
			
			if(!isNumber)
			{
				int lieshu = rowNum + 1;
				setMessage(infoList, rowNum+1,"第"+lieshu+"列非数字类型数据！");
				map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
				return map;
			}
			
			boolean isGz = validateWillNo(ExportUtil.replaceBlank(waybillNo));//是否过账
			
			if(isGz){
				int lieshu = rowNum + 1;
				setMessage(infoList, rowNum+1,"第"+lieshu+"列已过账的数据不能修改！");
				map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
				return map;
			}else{
				
				map0.put("waybillNo", ExportUtil.replaceBlank(waybillNo));
				map0.put("adjustWeight", adjustWeight);
				map0.put("isCalculated", "99");
				map0.put("lastModifier", userid);
				map0.put("lastModifyTime", nowdate);
				list.add(map0);
			}
			
        }
        DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 800);
        
        int num = 0;
        String message = null;
        try {
			num = bizDispatchBillPayService.updateBatchWeight(list);
		} catch (Exception e) {
		     message = e.getMessage();
		     logger.error(e.getMessage());
		     //写入日志
			 BmsErrorLogInfoEntity bmsErrorLogInfoEntity=new BmsErrorLogInfoEntity();
		 	 bmsErrorLogInfoEntity.setClassName("DispatchBillPayController");
			 bmsErrorLogInfoEntity.setMethodName("saveAll");
			 bmsErrorLogInfoEntity.setErrorMsg(e.toString());
			 bmsErrorLogInfoEntity.setCreateTime(JAppContext.currentTimestamp());
			 bmsErrorLogInfoEntity.setIdentify("批量更新重量失败");
			 bmsErrorLogInfoService.log(bmsErrorLogInfoEntity);	
		}
        if(num==0){
        	DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 999);
			errorVo = new ErrorMessageVo();
			if(message!=null){
				errorVo.setMsg(message);
			}else{
				errorVo.setMsg("更新失败!");
				errorVo.setLineNo(2);
			}
			infoList.add(errorVo);
			map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
			
			return map;
        }else{
        	DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 1000);
			map.put(ConstantInterface.ImportExcelStatus.IMP_SUCC, "0");
			return map;
        }
	}
	
	boolean validateWillNo(String willNo)
	{
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("waybillNo", willNo);
		int num = bizDispatchBillPayService.queryDispatch(param);
		if(num>0)
			return true;
		return false;
	}
	
	private String replaceChar(String str){
		if(StringUtils.isNotBlank(str)){
			String[] arr={" ","\\","/",",",".","-"};
			for(String a:arr){
				str=str.replace(a, "");
			}
		}
		return str;
	}
	
	@FileProvider
	public DownloadFile downloadTemplateUpdate(Map<String, String> parameter) throws IOException {
		InputStream is = DoradoContext.getCurrent().getServletContext().getResourceAsStream("/WEB-INF/templates/dispatch/dispatch_waybill_template_update.xlsx");
		return new DownloadFile("配送运单调整重量更新模板.xlsx", is);
	}
	
	@FileResolver
	public Map<String, Object> importUpdateWeight(final UploadFile file,final Map<String, Object> parameter) throws Exception {
		//String deliver=(String)parameter.get("deliver");
		 Map<String, Object> remap=new HashMap<String, Object>();
		// 校验信息（报错提示）
		final List<ErrorMessageVo> infoList = new ArrayList<ErrorMessageVo>();
		
		String lockString=Tools.getMd5("BMS_QUO_IMPORT_DISPATCH_PAY_UPDATE");
		remap=lock.lock(lockString, 300, new LockCallback<Map<String, Object>>() {

			@Override
			public Map<String, Object> handleObtainLock() {
				Map<String, Object> map=new HashMap<String, Object>();
				try {
					map=importUpdateWeightLock(file,parameter);
				} catch (Exception e) {
					ErrorMessageVo errorVo = new ErrorMessageVo();
					errorVo.setMsg("系统错误:"+e.getMessage());
					infoList.add(errorVo);
					map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
					 //写入日志
					 BmsErrorLogInfoEntity bmsErrorLogInfoEntity=new BmsErrorLogInfoEntity();
				 	 bmsErrorLogInfoEntity.setClassName("DispatchBillPayController");
					 bmsErrorLogInfoEntity.setMethodName("importUpdateWeight");
					 bmsErrorLogInfoEntity.setErrorMsg(e.toString());
					 bmsErrorLogInfoEntity.setCreateTime(JAppContext.currentTimestamp());
					 bmsErrorLogInfoService.log(bmsErrorLogInfoEntity);	
				}
				return map;
			}

			@Override
			public Map<String, Object> handleNotObtainLock()
					throws LockCantObtainException {
				Map<String, Object> map=new HashMap<String, Object>();
				ErrorMessageVo errorVo = new ErrorMessageVo();
				errorVo.setMsg("宅配导入功能已被其他用户占用，请稍后重试；");
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
		
}