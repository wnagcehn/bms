/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.biz.trunkwaybill.web;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
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
import com.jiuyescm.bms.base.airport.entity.PubAirportEntity;
import com.jiuyescm.bms.base.airport.service.IPubAirportService;
import com.jiuyescm.bms.base.dictionary.entity.SystemCodeEntity;
import com.jiuyescm.bms.base.dictionary.service.ISystemCodeService;
import com.jiuyescm.bms.base.ewarehouse.entity.PubElecWarehouseEntity;
import com.jiuyescm.bms.base.ewarehouse.service.IEwareHouseService;
import com.jiuyescm.bms.biz.transport.entity.BizGanxianWayBillEntity;
import com.jiuyescm.bms.biz.transport.service.IBizGanxianWayBillService;
import com.jiuyescm.bms.common.constants.ExceptionConstant;
import com.jiuyescm.bms.common.constants.MessageConstant;
import com.jiuyescm.bms.common.constants.TransportMessageConstant;
import com.jiuyescm.bms.common.entity.ErrorMessageVo;
import com.jiuyescm.bms.common.enumtype.CargoIsLightEnum;
import com.jiuyescm.bms.common.enumtype.TransportWayBillTypeEnum;
import com.jiuyescm.bms.common.log.entity.BmsErrorLogInfoEntity;
import com.jiuyescm.bms.common.log.service.IBmsErrorLogInfoService;
import com.jiuyescm.bms.common.sequence.service.SequenceService;
import com.jiuyescm.bms.common.tool.Session;
import com.jiuyescm.bms.fees.IFeesReceiverDeliverService;
import com.jiuyescm.bms.fees.entity.FeesReceiveDeliverEntity;
import com.jiuyescm.bms.fees.entity.FeesReceiveDeliverQueryEntity;
import com.jiuyescm.cfm.common.JAppContext;
import com.jiuyescm.common.ConstantInterface;
import com.jiuyescm.common.utils.BeanToMapUtil;
import com.jiuyescm.common.utils.FileOperationUtil;
import com.jiuyescm.common.utils.excel.FileReaderFactory;
import com.jiuyescm.common.utils.excel.IFileReader;
import com.jiuyescm.common.utils.upload.BaseDataType;
import com.jiuyescm.common.utils.upload.DataProperty;
import com.jiuyescm.common.utils.upload.TransportWayBillDataType;
import com.jiuyescm.mdm.customer.api.IAddressService;
import com.jiuyescm.mdm.customer.api.ICustomerService;
import com.jiuyescm.mdm.customer.vo.CustomerVo;
import com.jiuyescm.mdm.customer.vo.ErrorMsgVo;
import com.jiuyescm.mdm.customer.vo.RegionEncapVo;
import com.jiuyescm.mdm.customer.vo.RegionVo;
import com.jiuyescm.mdm.warehouse.api.IWarehouseService;
import com.jiuyescm.mdm.warehouse.vo.WarehouseVo;

/**
 * 
 * @author wubangjun
 * 
 */
@Controller("trunkWaybillController")
public class BizGanxianWayBillController {

	private static final Logger logger = Logger.getLogger(BizGanxianWayBillController.class.getName());

	@Resource
	private IBizGanxianWayBillService bizGanxianWayBillService;
	
	@Autowired
	private IFeesReceiverDeliverService deliverService;
	
	@Autowired 
	private ICustomerService customerService;
	@Autowired 
	private IWarehouseService warehouseService;
	@Resource 
	private IEwareHouseService elecWareHouseService;
	@Resource 
	private SequenceService sequenceService;
	@Resource
	private ISystemCodeService systemCodeService;
	@Resource
	private IAddressService addressService;
	@Resource
	private IPubAirportService airportService;
	@Resource
	private IBmsErrorLogInfoService bmsErrorLogInfoService;

	//省、市、区校验
	private List<RegionVo> sendCJPCList = null;
	private List<RegionVo> receiveCJPCList = null;
	private List<RegionVo> sendTCPCDList = null;
	private List<RegionVo> receiveTCPCDList = null;
	//系统参数
	private Map<String, String> wareHouseMap = null;
	private Map<String, String> elecWareHouseMap = null;
	private Map<String, String> customerMap = null;
	private Map<String, String> carModelMap = null;
	private Map<String, String> cargoIsLightMap = null;
	private Map<String, String> bizTypeMap = null;
	private Map<String, PubAirportEntity> airportMap = null;
	
	@DataProvider
	public BizGanxianWayBillEntity findById(Long id) throws Exception {
		BizGanxianWayBillEntity entity = null;
		entity = bizGanxianWayBillService.findById(id);
		return entity;
	}

	/**
	 * 分页查询
	 * 
	 * @param page
	 * @param param
	 */
	@DataProvider
	public void query(Page<BizGanxianWayBillEntity> page, Map<String, Object> param) {
		if (null == param) {
			param = new HashMap<String, Object>();
		}else if(null != param && param.containsKey("isCalculated")){
			if(StringUtils.equalsIgnoreCase(String.valueOf(param.get("isCalculated")), "ALL")){
				param.put("isCalculated", "");
			}else{
				param.put("isCalculated", String.valueOf(param.get("isCalculated")));
			}
		}
		param.put("delFlag", "0");
		PageInfo<BizGanxianWayBillEntity> pageInfo = bizGanxianWayBillService.query(param, page.getPageNo(), page.getPageSize());
		if (pageInfo != null) {
			page.setEntities(pageInfo.getList());
			page.setEntityCount((int) pageInfo.getTotal());
		}
	}

	@DataResolver
	public String save(BizGanxianWayBillEntity entity) {
		try{
			if (entity.getId() == null) {
				bizGanxianWayBillService.save(entity);
			} else {
				Timestamp nowdate = JAppContext.currentTimestamp();
				String userid=JAppContext.currentUserName();
				if(entity != null && StringUtils.isNotBlank(entity.getFeesNo())){
					FeesReceiveDeliverQueryEntity paramEntity = new FeesReceiveDeliverQueryEntity();
					paramEntity.setFeesNo(entity.getFeesNo());
					PageInfo<FeesReceiveDeliverEntity> pageInfo = deliverService.query(paramEntity, 0, Integer.MAX_VALUE);
					if (null != pageInfo && null!=pageInfo.getList() && pageInfo.getList().size()>0) {
						FeesReceiveDeliverEntity feesReceiveDeliverEntity=pageInfo.getList().get(0);
						//获取此时的费用状态
						String status = String.valueOf(feesReceiveDeliverEntity.getState());
						if(status.equals("1")){
							return "该费用已过账,无法调整重量!";
						}
					}
				}
				//开始校验业务数据
				init();
				String re=checkUpdateData(entity);
				if(StringUtils.isNotBlank(re)){
					return re;
				}
				//如果没有过账的话,就允许调整重量,且调整完后,状态重置为未计算,定时任务重新扫描到并重新生成应收费用.
				entity.setIsCalculated("0");
					
				//此为修改业务数据
				//根据名字查出对应的id
				entity.setLastModifier(userid);
				entity.setLastModifyTime(nowdate);
				bizGanxianWayBillService.update(entity);
				
			}
			return "保存成功!";
		}catch (Exception e) {
			//写入日志
			BmsErrorLogInfoEntity bmsErrorLogInfoEntity=new BmsErrorLogInfoEntity(this.getClass().getSimpleName(),Thread.currentThread().getStackTrace()[1].getMethodName(), "", e.toString());
			bmsErrorLogInfoService.log(bmsErrorLogInfoEntity);
			return "保存失败!";
		}
	}
	
	@DataResolver
	public void saveAll(Collection<BizGanxianWayBillEntity> datas) {
		if (datas == null) {
			return;
		}
		//批量修改,暂时没有考虑业务数据是否已经过账,如果考虑是否已过账,就不能批量修改了,否则就无法提示.
		List<BizGanxianWayBillEntity> modifyList = new ArrayList<BizGanxianWayBillEntity>(datas.size());
		for (BizGanxianWayBillEntity temp : datas) {
			temp.setLastModifier(JAppContext.currentUserName());
			temp.setLastModifyTime(JAppContext.currentTimestamp());
			if (EntityState.MODIFIED.equals(EntityUtils.getState(temp))) {
				modifyList.add(temp);
			}
		}
		if(modifyList != null && modifyList.size()>0){
			bizGanxianWayBillService.updateList(modifyList);
		}
	}
	
	@DataResolver
	public String adjustWeight(Collection<BizGanxianWayBillEntity> datas) {
		if(Session.isMissing()){
			return "长时间未操作，用户已失效，请重新登录再试！";
		}
		try {
			Timestamp nowdate = JAppContext.currentTimestamp();
			String userid=JAppContext.currentUserName();
			for(BizGanxianWayBillEntity bizData:datas){
				//对操作类型进行判断
				//此为新增业务数据
				if(EntityState.NEW.equals(EntityUtils.getState(bizData))){
					//判断该计费规则是否已存在
				}else if(EntityState.MODIFIED.equals(EntityUtils.getState(bizData))){
					
					//判断是否生成费用，判断费用的状态是否为未过账
					String feeId = bizData.getFeesNo();
					if(StringUtils.isNotBlank(feeId)){
						FeesReceiveDeliverQueryEntity paramEntity = new FeesReceiveDeliverQueryEntity();
						paramEntity.setFeesNo(feeId);
						PageInfo<FeesReceiveDeliverEntity> pageInfo = deliverService.query(paramEntity, 0, Integer.MAX_VALUE);
						if (null != pageInfo && null!=pageInfo.getList() && pageInfo.getList().size()>0) {
							FeesReceiveDeliverEntity feesReceiveDeliverEntity=pageInfo.getList().get(0);
							//获取此时的费用状态
							String status = String.valueOf(feesReceiveDeliverEntity.getState());
							if(status.equals("1")){
								return "该费用已过账,无法调整重量!";
							}
						}
					}
					//如果没有过账的话,就允许调整重量,且调整完后,状态重置为未计算,定时任务重新扫描到并重新生成应收费用.
					bizData.setIsCalculated("0");
						
					//此为修改业务数据
					//根据名字查出对应的id
					bizData.setLastModifier(userid);
					bizData.setLastModifyTime(nowdate);
					bizGanxianWayBillService.update(bizData);
				}
			}
			return "调整重量成功";
		} catch (Exception e) {
			logger.error("调整重量失败", e);
			//写入日志
			BmsErrorLogInfoEntity bmsErrorLogInfoEntity=new BmsErrorLogInfoEntity(this.getClass().getSimpleName(),Thread.currentThread().getStackTrace()[1].getMethodName(), "", e.toString());
			bmsErrorLogInfoService.log(bmsErrorLogInfoEntity);
			return "调整重量失败";
		}
	}
	
	/**
	 * 导入模板下载
	 * @throws IOException
	 */
	@FileProvider
	public DownloadFile downloadTemplate(Map<String, String> parameter) throws IOException {
		InputStream is = DoradoContext.getCurrent().getServletContext().getResourceAsStream("/WEB-INF/templates/transport/transport_waybill_template.xlsx");
		return new DownloadFile("九曳干线运单导入模板.xlsx", is);
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
		List<BizGanxianWayBillEntity> dataList = new ArrayList<BizGanxianWayBillEntity>();
		try {
			BaseDataType bs = new TransportWayBillDataType();
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
			dataList = (List<BizGanxianWayBillEntity>) map.get(ConstantInterface.ImportExcelStatus.IMP_SUCC);
			DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 800);
			
			//插入正式表
			int insertNum = bizGanxianWayBillService.saveList(dataList);
			DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 950);
			
			if (insertNum <= 0) {
				DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 999);
				errorVo = new ErrorMessageVo();
				errorVo.setMsg(TransportMessageConstant.WAYBILL_SAVE_FAIL_MSG);
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
			errorVo.setMsg(ExceptionConstant.WAYBILL_SAVE_EXCEPTION_MSG);
			errorList.add(errorVo);
			map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, errorList);
			DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 999);
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
	private List<BizGanxianWayBillEntity> readExcel(List<ErrorMessageVo> errorList, UploadFile file,BaseDataType bs) {
		
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
			List<BizGanxianWayBillEntity> dataList = Lists.newArrayList();
			for (Map<String, String> data : datas) {
				BizGanxianWayBillEntity p = (BizGanxianWayBillEntity) BeanToMapUtil.convertMapNull(BizGanxianWayBillEntity.class, data);
				dataList.add(p);
			}
			return dataList;
		} catch (Exception e) {
			setMessage(errorList, 0, e.getMessage());
			//写入日志
			BmsErrorLogInfoEntity bmsErrorLogInfoEntity=new BmsErrorLogInfoEntity(this.getClass().getSimpleName(),Thread.currentThread().getStackTrace()[1].getMethodName(), "", e.toString());
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
			List<BizGanxianWayBillEntity> dataList, Map<String, Object> map)throws Exception {
		int lineNo = 1;
		//初始化参数
		init(dataList);
		DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 400);
		for (int i = 0; i < dataList.size(); i++) {
			lineNo = lineNo+1;
			BizGanxianWayBillEntity bgpe = dataList.get(i);
			if (null == bgpe) {
				continue;
			}
			
			//***************************非空校验***************************
			//业务类型
			if (isNullInfo(bgpe.getBizTypeCode())) {
				setMessage(errorList, lineNo, TransportMessageConstant.TRANSPORTTYPE_NULL_MSG);
			}else {
				//商家非空校验
				checkNullInfoCustomer(lineNo, errorList, bgpe);
				//业务类型校验
				checkBizType(lineNo, errorList, bgpe);
				//订单号非空校验
				checkOrderNo(lineNo, errorList, bgpe);
				//根据业务类型非空校验
				if (TransportWayBillTypeEnum.TC.getCode().equals(bgpe.getBizTypeCode())) {
					checkNullInfoTC(lineNo, errorList, bgpe);
				}else if (TransportWayBillTypeEnum.CJ.getCode().equals(bgpe.getBizTypeCode())) {
					checkNullInfoCJ(lineNo, errorList, bgpe);
				}else if (TransportWayBillTypeEnum.DSZL.getCode().equals(bgpe.getBizTypeCode())) {
					checkNullInfoDSZL(lineNo, errorList, bgpe);
				}else if (TransportWayBillTypeEnum.HXD.getCode().equals(bgpe.getBizTypeCode())) {
					checkNullInfoHXD(lineNo, errorList, bgpe);
				}
				//发货日期
				checkSendTime(lineNo, errorList, bgpe);
			}
			
			bgpe.setCreator(JAppContext.currentUserName());
			bgpe.setCreateTime(JAppContext.currentTimestamp());
			bgpe.setIsCalculated(String.valueOf(ConstantInterface.Calculate.CALCULATE_NO));
			bgpe.setDelFlag(String.valueOf(ConstantInterface.InvalidInterface.INVALID_0));// 设置为未作废
			
			DecimalFormat decimalFormat=new DecimalFormat("0");
			double addNum = ((double)(i+1)/dataList.size())*400;
			DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 300+Integer.valueOf(decimalFormat.format(addNum)));
		}
		DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 500);
		//校验地址
		checkStandardAddress(errorList, map);
		if (null != errorList && errorList.size() > 0) { // 有错误信息
			map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, errorList);
			return map;
		}
		DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 600);
		//校验成功，匹配地址
		matchStandardAddress(dataList);
		
		if (null != errorList && errorList.size() > 0) { // 有错误信息
			map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, errorList);
		} else {
			map.put(ConstantInterface.ImportExcelStatus.IMP_SUCC, dataList); // 无基本错误信息
		}
		
		return map;
	}
	
	/**
	 * 初始化参数
	 */
	private void init(List<BizGanxianWayBillEntity> dataList){
		//省、市、区校验
		sendCJPCList = new ArrayList<RegionVo>();
		receiveCJPCList = new ArrayList<RegionVo>();
		sendTCPCDList = new ArrayList<RegionVo>();
		receiveTCPCDList = new ArrayList<RegionVo>();
		//系统参数
		wareHouseMap = new HashMap<String, String>();
		elecWareHouseMap = new HashMap<String, String>();
		customerMap = new HashMap<String, String>();
		carModelMap = new HashMap<String, String>();
		cargoIsLightMap = new HashMap<String, String>();
		bizTypeMap = new HashMap<String, String>();
		airportMap = new HashMap<String, PubAirportEntity>();
				
		//电商仓库
		PageInfo<PubElecWarehouseEntity> ewareHousePageInfo = elecWareHouseService.queryAll(null, 0, Integer.MAX_VALUE);
		if (null != ewareHousePageInfo && ewareHousePageInfo.getList().size() > 0) {
			for (PubElecWarehouseEntity elecWareHouse : ewareHousePageInfo.getList()) {
				if (null != elecWareHouse) {
					elecWareHouseMap.put(elecWareHouse.getWarehouseName(), elecWareHouse.getWarehouseCode());
					wareHouseMap.put(elecWareHouse.getWarehouseName(), elecWareHouse.getWarehouseCode());
				}
			}
		}
		//仓库
		List<WarehouseVo> wareHouseList = warehouseService.queryAllWarehouse();
		if(null != wareHouseList && wareHouseList.size()>0){
			for(WarehouseVo wareHouse : wareHouseList){
				wareHouseMap.put(wareHouse.getWarehousename(), wareHouse.getWarehouseid());
			}
		}
		//车型
		List<SystemCodeEntity> carModelList = systemCodeService.findEnumList("MOTORCYCLE_TYPE");
		if (null != carModelList && carModelList.size() > 0) {
			for (SystemCodeEntity sysEntity : carModelList) {
				carModelMap.put(sysEntity.getCodeName(), sysEntity.getCode());
			}
		}
		//当导入数据量达到多少时,先一次加载再判断;否则数据量小的时候每次检索校验.
		if(null != dataList && dataList.size()>50){
			//商家
			PageInfo<CustomerVo> tmpPageInfo = customerService.query(null, 0, Integer.MAX_VALUE);
			if (null != tmpPageInfo && null != tmpPageInfo.getList() && tmpPageInfo.getList().size()>0) {
				for(CustomerVo customer : tmpPageInfo.getList()){
					if(null != customer){
						customerMap.put(customer.getCustomername(), customer.getCustomerid());
					}
				}
			}
		}
		//机场
		List<PubAirportEntity> airportList = airportService.query(null);
		if (null != airportList && airportList.size() > 0) {
			for (PubAirportEntity entity : airportList) {
				airportMap.put(entity.getAirportName(), entity);
			}
		}
		//是否轻货
		cargoIsLightMap.put(CargoIsLightEnum.YES.getDesc(), CargoIsLightEnum.YES.getCode());
		cargoIsLightMap.put(CargoIsLightEnum.NO.getDesc(), CargoIsLightEnum.NO.getCode());
		
		//业务类型范围
		bizTypeMap.put(TransportWayBillTypeEnum.TC.getDesc(), TransportWayBillTypeEnum.TC.getCode());
		bizTypeMap.put(TransportWayBillTypeEnum.CJ.getDesc(), TransportWayBillTypeEnum.CJ.getCode());
		bizTypeMap.put(TransportWayBillTypeEnum.DSZL.getDesc(), TransportWayBillTypeEnum.DSZL.getCode());
		bizTypeMap.put(TransportWayBillTypeEnum.HXD.getDesc(), TransportWayBillTypeEnum.HXD.getCode());
	}
	
	/**
	 * 商家非空校验
	 */
	private void checkNullInfoCustomer(int lineNo, List<ErrorMessageVo> errorList, BizGanxianWayBillEntity bgpe){
		//验证商家
		if(isNullInfo(bgpe.getCustomerName())){
			setMessage(errorList, lineNo, MessageConstant.CUSTOMER_INFO_ISNULL_MSG);
		}else {
			if(customerMap.containsKey(bgpe.getCustomerName()) && 
					!isNullInfo(customerMap.get(bgpe.getCustomerName()))){
				bgpe.setCustomerName(bgpe.getCustomerName());
				bgpe.setCustomerId(customerMap.get(bgpe.getCustomerName()));
			}else{
				Map<String,Object> parameter = new HashMap<String,Object>();
				parameter.put("customername", bgpe.getCustomerName().trim());
				PageInfo<CustomerVo> tmpPageInfo = customerService.query(parameter, 0, Integer.MAX_VALUE);
				if (tmpPageInfo == null || tmpPageInfo.getList() == null || tmpPageInfo.getList().size()==0) {
					setMessage(errorList, lineNo,"商家["+bgpe.getCustomerName()+"]没有在表中维护!");
				}else{
					CustomerVo customer = tmpPageInfo.getList().get(0);
					if(customer != null){
						bgpe.setCustomerId(customer.getCustomerid());
						bgpe.setCustomerName(customer.getCustomername());
						//放入缓存customerMap
						customerMap.put(customer.getCustomername(), customer.getCustomerid());
					}
				}
			}
		}
	}
	
	/**
	 * 校验业务类型范围
	 * @param lineNo
	 * @param errorList
	 * @param bgpe
	 */
	private void checkBizType(int lineNo, List<ErrorMessageVo> errorList, BizGanxianWayBillEntity bgpe){
		if (bizTypeMap.containsKey(bgpe.getBizTypeCode()) ||
				!isNullInfo(bizTypeMap.get(bgpe.getBizTypeCode()))) {
			bgpe.setBizTypeCode(bizTypeMap.get(bgpe.getBizTypeCode()));
		}else {
			setMessage(errorList, lineNo, bgpe.getBizTypeCode() + TransportMessageConstant.TRANSPORTTYPE_ERROR_MSG);
		}
	}
	
	/**
	 * 订单号非空校验
	 * @param lineNo
	 * @param errorList
	 * @param bgpe
	 */
	private void checkOrderNo(int lineNo, List<ErrorMessageVo> errorList, BizGanxianWayBillEntity bgpe){
		if(isNullInfo(bgpe.getOrderNo())){
			setMessage(errorList, lineNo, TransportMessageConstant.ORDERNO_NULL_MSG);
		}
	}
	
	/**
	 * 发货日期非空校验
	 * @param lineNo
	 * @param errorList
	 * @param bgpe
	 */
	private void checkSendTime(int lineNo, List<ErrorMessageVo> errorList, BizGanxianWayBillEntity bgpe){
		logger.info(">>>>>>>>>getSendTime" + bgpe.getSendTime());
		if(null == bgpe.getSendTime()){
			setMessage(errorList, lineNo, TransportMessageConstant.SENDTIME_NULL_MSG);
		}
	}
	
	/**
	 * 校验业务数据修改
	 * @param entity
	 */
	private String checkUpdateData(BizGanxianWayBillEntity entity){
		if("TC".equals(entity.getBizTypeCode())){
			//始发省份、始发城市 、目的省份、目的城市、 车型
			if (isNullInfo(entity.getSendProvinceId())) {
				return TransportMessageConstant.FROMPROVINCE_NULL_MSG;
			}
			if (isNullInfo(entity.getSendCityId())) {
				return TransportMessageConstant.FROMCITY_NULL_MSG;
			}
			if (isNullInfo(entity.getReceiverProvinceId())) {
				return TransportMessageConstant.TOPROVINCE_NULL_MSG;
			}
			if (isNullInfo(entity.getReceiverCityId())) {
				return TransportMessageConstant.TOCITY_NULL_MSG;
			}
			if (isNullInfo(entity.getCarModel())) {
				return TransportMessageConstant.CARMODEL_NULL_MSG;
			}else {
				if (carModelMap.containsKey(entity.getCarModel()) ||
						!isNullInfo(carModelMap.get(entity.getCarModel()))) {
					entity.setCarModel(carModelMap.get(entity.getCarModel()));
				}else {
					return TransportMessageConstant.CARMODEL_ERROR_MSG.toString();
				}
			}			
		}else if("CJ".equals(entity.getBizTypeCode())){
			//始发省份、始发城市、 目的省份、 目的城市、是否泡货、重量/体积
			if (isNullInfo(entity.getSendProvinceId())) {
				return TransportMessageConstant.FROMPROVINCE_NULL_MSG;
			}
			if (isNullInfo(entity.getSendCityId())) {
				return TransportMessageConstant.FROMCITY_NULL_MSG;
			}
			if (isNullInfo(entity.getReceiverProvinceId())) {
				return TransportMessageConstant.TOPROVINCE_NULL_MSG;
			}
			if (isNullInfo(entity.getReceiverCityId())) {
				return TransportMessageConstant.TOCITY_NULL_MSG;
			}
			if (isNullInfo(entity.getIsLight())) {
				return TransportMessageConstant.ISLIGHT_NULL_MSG;
			}
			if (CargoIsLightEnum.YES.getCode().equals(entity.getIsLight())) {
				if (null == entity.getTotalVolume()) {
					return TransportMessageConstant.VOLUME_NULL_MSG;
				}else if (0.0 >= entity.getTotalVolume()) {
					return TransportMessageConstant.VOLUME_GT0_MSG;
				}
			}else if (CargoIsLightEnum.NO.getCode().equals(entity.getIsLight())) {
				if (null == entity.getTotalWeight()) {
					return TransportMessageConstant.WEIGHT_NULL_MSG;
				}else if (0.0 >= entity.getTotalWeight()) {
					return TransportMessageConstant.WEIGHT_GT0_MSG;
				}
			}
		}else if("DSZL".equals(entity.getBizTypeCode())){
			//始发仓 、 电商仓、 车型
			//仓库
			String warehouseName= entity.getWarehouseName().trim();
			if(isNullInfo(warehouseName)){
				return TransportMessageConstant.FROMWAREHOUSE_NULL_MSG;
			}else {
				if(wareHouseMap.containsKey(warehouseName) && StringUtils.isNotBlank(wareHouseMap.get(warehouseName))){
					//从缓存wareHouseMap中直接取
					entity.setWarehouseName(warehouseName);
					entity.setWarehouseCode(wareHouseMap.get(warehouseName));
				}else{
					return "[" +warehouseName + "]" +TransportMessageConstant.WAREHOUSE_ERROR_MSG;
				}
			}
			
			//电商仓
			String elecWarehouseName= entity.getEndStation().trim();
			if(isNullInfo(elecWarehouseName)){
				return TransportMessageConstant.ENDSTATION_NULL_MSG;
			}else {
				if(!elecWareHouseMap.containsKey(elecWarehouseName) || isNullInfo(elecWareHouseMap.get(elecWarehouseName))){
					return "目的站点["+elecWarehouseName+"]没有在电商仓库表中维护!";
				}
			}
			
			if (isNullInfo(entity.getCarModel())) {
				return TransportMessageConstant.CARMODEL_NULL_MSG;
			}else {
				if (!carModelMap.containsKey(entity.getCarModel()) ||
						StringUtils.isEmpty(carModelMap.get(entity.getCarModel()))) {
					return TransportMessageConstant.CARMODEL_ERROR_MSG;
				}
			}
		}else if("HXD".equals(entity.getBizTypeCode())){
			//机场、  目的省份、目的城市、 重量
			String airport = entity.getStartStation();
			if (isNullInfo(airport)) {
				return TransportMessageConstant.STARTSTATION_NULL_MSG;
			}else {
				if(!airportMap.containsKey(airport) || null == airportMap.get(airport)){
					return "始发站点["+airport+"]没有在机场表中维护!";
				}else {
					PubAirportEntity airportEntity = airportMap.get(airport);
					entity.setReceiverProvinceId(airportEntity.getProvince());
					entity.setReceiverCityId(airportEntity.getCity());
				}
			}
			if (null == entity.getTotalWeight()) {
				return TransportMessageConstant.WEIGHT_NULL_MSG;
			}else if (0.0 >= entity.getTotalWeight()) {
				return TransportMessageConstant.WEIGHT_GT0_MSG;
			}
		}
		
		return "";
	}
	
	/**
	 * 非空校验-同城
	 */
	private void checkNullInfoTC(int lineNo, List<ErrorMessageVo> errorList, BizGanxianWayBillEntity bgpe){
		//始发省份、始发城市 、目的省份、目的城市、 车型
		if (isNullInfo(bgpe.getSendProvinceId())) {
			setMessage(errorList, lineNo, TransportMessageConstant.FROMPROVINCE_NULL_MSG);
		}
		if (isNullInfo(bgpe.getSendCityId())) {
			setMessage(errorList, lineNo, TransportMessageConstant.FROMCITY_NULL_MSG);
		}
//		if (isNullInfo(bgpe.getSendDistrictId())) {
//			setMessage(errorList, lineNo, TransportMessageConstant.FROMDISTRICT_NULL_MSG);
//		}
		if (isNullInfo(bgpe.getReceiverProvinceId())) {
			setMessage(errorList, lineNo, TransportMessageConstant.TOPROVINCE_NULL_MSG);
		}
		if (isNullInfo(bgpe.getReceiverCityId())) {
			setMessage(errorList, lineNo, TransportMessageConstant.TOCITY_NULL_MSG);
		}
//		if (isNullInfo(bgpe.getReceiverDistrictId())) {
//			setMessage(errorList, lineNo, TransportMessageConstant.TODISTRICT_NULL_MSG);
//		}
		if (isNullInfo(bgpe.getCarModel())) {
			setMessage(errorList, lineNo, TransportMessageConstant.CARMODEL_NULL_MSG);
		}else {
			if (carModelMap.containsKey(bgpe.getCarModel()) &&
					!isNullInfo(carModelMap.get(bgpe.getCarModel()))) {
				bgpe.setCarModel(carModelMap.get(bgpe.getCarModel()));
			}else {
				setMessage(errorList, lineNo, TransportMessageConstant.CARMODEL_ERROR_MSG);
			}
		}
		
		if (!isNullInfo(bgpe.getSendProvinceId()) && !isNullInfo(bgpe.getSendCityId()) &&
				!isNullInfo(bgpe.getReceiverProvinceId()) && !isNullInfo(bgpe.getReceiverCityId())) {
			RegionVo svo = new RegionVo(bgpe.getSendProvinceId(), bgpe.getSendCityId(), null);
			svo.setLineNo(lineNo);
			sendTCPCDList.add(svo);
			
			RegionVo rvo = new RegionVo(bgpe.getReceiverProvinceId(), bgpe.getReceiverCityId(), null);
			rvo.setLineNo(lineNo);
			receiveTCPCDList.add(rvo);
		}
	}
	
	/**
	 * 非空校验-城际
	 */
	private void checkNullInfoCJ(int lineNo, List<ErrorMessageVo> errorList, BizGanxianWayBillEntity bgpe){
		//始发省份、始发城市、 目的省份、 目的城市、是否泡货、重量/体积
		if (isNullInfo(bgpe.getSendProvinceId())) {
			setMessage(errorList, lineNo, TransportMessageConstant.FROMPROVINCE_NULL_MSG);
		}
		if (isNullInfo(bgpe.getSendCityId())) {
			setMessage(errorList, lineNo, TransportMessageConstant.FROMCITY_NULL_MSG);
		}
		if (isNullInfo(bgpe.getReceiverProvinceId())) {
			setMessage(errorList, lineNo, TransportMessageConstant.TOPROVINCE_NULL_MSG);
		}
		if (isNullInfo(bgpe.getReceiverCityId())) {
			setMessage(errorList, lineNo, TransportMessageConstant.TOCITY_NULL_MSG);
		}
		if (isNullInfo(bgpe.getIsLight())) {
			setMessage(errorList, lineNo, TransportMessageConstant.ISLIGHT_NULL_MSG);
		}else {
			if (cargoIsLightMap.containsKey(bgpe.getIsLight()) && 
					!isNullInfo(cargoIsLightMap.get(bgpe.getIsLight()))) {
				bgpe.setIsLight(cargoIsLightMap.get(bgpe.getIsLight()));
			}else {
				setMessage(errorList, lineNo, TransportMessageConstant.ISLIGHT_ERROR_MSG);
			}
		}
		if (CargoIsLightEnum.YES.getDesc().equals(bgpe.getIsLight())) {
			if (null == bgpe.getTotalVolume()) {
				setMessage(errorList, lineNo, TransportMessageConstant.VOLUME_NULL_MSG);
			}else if (0.0 >= bgpe.getTotalVolume()) {
				setMessage(errorList, lineNo, TransportMessageConstant.VOLUME_GT0_MSG);
			}
		}else if (CargoIsLightEnum.NO.getDesc().equals(bgpe.getIsLight())) {
			if (null == bgpe.getTotalWeight()) {
				setMessage(errorList, lineNo, TransportMessageConstant.WEIGHT_NULL_MSG);
			}else if (0.0 >= bgpe.getTotalWeight()) {
				setMessage(errorList, lineNo, TransportMessageConstant.WEIGHT_GT0_MSG);
			}
		}
		
		if (!isNullInfo(bgpe.getSendProvinceId()) && !isNullInfo(bgpe.getSendCityId()) &&
				!isNullInfo(bgpe.getReceiverProvinceId()) && !isNullInfo(bgpe.getReceiverCityId())) {
			RegionVo svo = new RegionVo(bgpe.getSendProvinceId(), bgpe.getSendCityId(), null);
			svo.setLineNo(lineNo);
			sendCJPCList.add(svo);
			
			RegionVo rvo = new RegionVo(bgpe.getReceiverProvinceId(), bgpe.getReceiverCityId(), null);
			rvo.setLineNo(lineNo);
			receiveCJPCList.add(rvo);
		}
	}
	
	/**
	 * 非空校验-电商专列
	 */
	private void checkNullInfoDSZL(int lineNo, List<ErrorMessageVo> errorList, BizGanxianWayBillEntity bgpe){
		//始发仓 、 电商仓、 车型
		//仓库
		String warehouseName= bgpe.getWarehouseName().trim();
		if(isNullInfo(warehouseName)){
			setMessage(errorList, lineNo, TransportMessageConstant.FROMWAREHOUSE_NULL_MSG);
		}else {
			if(wareHouseMap.containsKey(warehouseName) && StringUtils.isNotBlank(wareHouseMap.get(warehouseName))){
				//从缓存wareHouseMap中直接取
				bgpe.setWarehouseName(warehouseName);
				bgpe.setWarehouseCode(wareHouseMap.get(warehouseName));
			}else{
				setMessage(errorList, lineNo, "[" +warehouseName + "]" +TransportMessageConstant.WAREHOUSE_ERROR_MSG);
			}
		}
		//电商仓
		String elecWarehouseName= bgpe.getEndStation().trim();
		if(isNullInfo(elecWarehouseName)){
			setMessage(errorList, lineNo, TransportMessageConstant.ENDSTATION_NULL_MSG);
		}else {
			if(!elecWareHouseMap.containsKey(elecWarehouseName) || isNullInfo(elecWareHouseMap.get(elecWarehouseName))){
				setMessage(errorList, lineNo,"目的站点["+elecWarehouseName+"]没有在电商仓库表中维护!");
			}
		}
		
		if (isNullInfo(bgpe.getCarModel())) {
			setMessage(errorList, lineNo, TransportMessageConstant.CARMODEL_NULL_MSG);
		}else {
			if (!carModelMap.containsKey(bgpe.getCarModel()) ||
					StringUtils.isEmpty(carModelMap.get(bgpe.getCarModel()))) {
				setMessage(errorList, lineNo, TransportMessageConstant.CARMODEL_ERROR_MSG);
			}
		}
	}
	
	/**
	 * 非空校验-航鲜达
	 * 省市从数据库表中获取
	 */
	private void checkNullInfoHXD(int lineNo, List<ErrorMessageVo> errorList, BizGanxianWayBillEntity bgpe){
		//机场、  目的省份、目的城市、 重量
		String airport = bgpe.getStartStation();
		if (isNullInfo(airport)) {
			setMessage(errorList, lineNo, TransportMessageConstant.STARTSTATION_NULL_MSG);
		}else {
			if(!airportMap.containsKey(airport) || null == airportMap.get(airport)){
				setMessage(errorList, lineNo,"始发站点["+airport+"]没有在机场表中维护!");
			}else {
				PubAirportEntity airportEntity = airportMap.get(airport);
				bgpe.setReceiverProvinceId(airportEntity.getProvince());
				bgpe.setReceiverCityId(airportEntity.getCity());
			}
		}
		if (null == bgpe.getTotalWeight()) {
			setMessage(errorList, lineNo, TransportMessageConstant.WEIGHT_NULL_MSG);
		}else if (0.0 >= bgpe.getTotalWeight()) {
			setMessage(errorList, lineNo, TransportMessageConstant.WEIGHT_GT0_MSG);
		}
	}
	
	/**
	 * 校验地址
	 * @param errorList
	 */
	private void checkStandardAddress(List<ErrorMessageVo> errorList, Map<String, Object> map)throws Exception{
		//始发省、市、区
		checkStandardPC(sendTCPCDList, errorList, map);
		
		//目的市、区
		checkStandardPC(receiveTCPCDList, errorList, map);
		
		//始发省、市
		checkStandardPC(sendCJPCList, errorList, map);
		
		//目的省、市
		checkStandardPC(receiveCJPCList, errorList, map);
	}
	
	/**
	 * 校验省、市、区
	 * @param list
	 * @param errorList
	 * @param map
	 */
	private void checkStandardPCD(List<RegionVo> list, 
			List<ErrorMessageVo> errorList, Map<String, Object> map){
		if (null != list && list.size() > 0) {
			RegionEncapVo matchCityDistrict = addressService.matchStandardByAlias(list);
			if (null != matchCityDistrict.getErrorMsgList() && matchCityDistrict.getErrorMsgList().size() > 0) {
				for (ErrorMsgVo error : matchCityDistrict.getErrorMsgList()) {
					setMessage(errorList, error.getLineNo(), error.getErrorMsg());
				}
				if (null != errorList && errorList.size() > 0) { // 有错误信息
					map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, errorList);
				}
			}else {
				//没有错误信息，匹配市、区
				for (RegionVo regionVo : list) {
					for (RegionVo matchVo : matchCityDistrict.getRegionList()) {
						if (regionVo.getProvince().equals(matchVo.getProvince()) && 
								regionVo.getCity().equals(matchVo.getCity()) &&
								regionVo.getDistrict().equals(matchVo.getDistrict())) {
							regionVo.setProvincecode(matchVo.getProvincecode());
							regionVo.setCitycode(matchVo.getCitycode());
							regionVo.setDistrictcode(matchVo.getDistrictcode());
						}
					}
				}
			}
		}
	}
	
	/**
	 * 校验省、市
	 * @param list
	 * @param errorList
	 * @param map
	 */
	private void checkStandardPC(List<RegionVo> list, 
			List<ErrorMessageVo> errorList, Map<String, Object> map){
		if (null != list && list.size() > 0) {
			RegionEncapVo province = addressService.matchStandardByAlias(list);
			if (null != province.getErrorMsgList() && province.getErrorMsgList().size() > 0) {
				for (ErrorMsgVo error : province.getErrorMsgList()) {
					setMessage(errorList, error.getLineNo(), error.getErrorMsg());
				}
				if (null != errorList && errorList.size() > 0) { // 有错误信息
					map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, errorList);
				}
			}else {
				//没有错误信息，匹配市
				for (RegionVo regionVo : list) {
					for (RegionVo matchVo : province.getRegionList()) {
						if (regionVo.getProvince().equals(matchVo.getProvince()) &&
								regionVo.getCity().equals(matchVo.getCity())) {
							regionVo.setProvincecode(matchVo.getProvincecode());
							regionVo.setCitycode(matchVo.getCitycode());
						}
					}
				}
			}
		}
	}
	
	/**
	 * 匹配标准地址
	 */
	private void matchStandardAddress(List<BizGanxianWayBillEntity> dataList){
		if (null == dataList || dataList.size() <= 0) {
			return;
		}
		for (BizGanxianWayBillEntity entity : dataList) {
			//根据业务类型非空校验
			if (TransportWayBillTypeEnum.TC.getCode().equals(entity.getBizTypeCode())) {
				matchAddressTC(entity);
			}else if (TransportWayBillTypeEnum.CJ.getCode().equals(entity.getBizTypeCode())) {
				matchAddressCJ(entity);
			}
		}
	}
	
	/**
	 * 同城匹配-始发市、区，目的市、区
	 * @param entity
	 */
	private void matchAddressTC(BizGanxianWayBillEntity entity){
		if (null != sendTCPCDList && sendTCPCDList.size() > 0) {
			for (RegionVo sendCityDistrict : sendTCPCDList) {
				if (entity.getSendProvinceId().equals(sendCityDistrict.getProvince()) &&
						entity.getSendCityId().equals(sendCityDistrict.getCity()) &&
						entity.getSendDistrictId().equals(sendCityDistrict.getDistrict())) {
					entity.setSendProvinceId(sendCityDistrict.getProvincecode());
					entity.setSendCityId(sendCityDistrict.getCitycode());
					entity.setSendDistrictId(sendCityDistrict.getDistrictcode());
					break;
				}
			}
		}
		if (null != receiveTCPCDList && receiveTCPCDList.size() > 0) {
			for (RegionVo receiveCityDistrict : receiveTCPCDList) {
				if (entity.getReceiverProvinceId().equals(receiveCityDistrict.getProvince()) &&
						entity.getReceiverCityId().equals(receiveCityDistrict.getCity()) &&
						entity.getReceiverDistrictId().equals(receiveCityDistrict.getDistrict())) {
					entity.setReceiverProvinceId(receiveCityDistrict.getProvincecode());
					entity.setReceiverCityId(receiveCityDistrict.getCitycode());
					entity.setReceiverDistrictId(receiveCityDistrict.getDistrictcode());
					break;
				}
			}
		}
	}
	
	/**
	 * 城际匹配-始发城市、目的城市
	 * @param entity
	 */
	private void matchAddressCJ(BizGanxianWayBillEntity entity){
		if (null != sendCJPCList && sendCJPCList.size() >0) {
			for (RegionVo sendCity : sendCJPCList) {
				if (entity.getSendProvinceId().equals(sendCity.getProvince()) &&
						entity.getSendCityId().equals(sendCity.getCity())) {
					entity.setSendProvinceId(sendCity.getProvincecode());
					entity.setSendCityId(sendCity.getCitycode());
					break;
				}
			}
		}
		if (null != receiveCJPCList && receiveCJPCList.size() >0) {
			for (RegionVo receiveCity : receiveCJPCList) {
				if (entity.getReceiverProvinceId().equals(receiveCity.getProvince()) &&
						entity.getReceiverCityId().equals(receiveCity.getCity())) {
					entity.setReceiverProvinceId(receiveCity.getProvincecode());
					entity.setReceiverCityId(receiveCity.getCitycode());
					break;
				}
			}
		}
	}
	
	/**
	 * 判断是否为空
	 * @param info
	 * @return
	 */
	private boolean isNullInfo(String info){
		if (StringUtils.isEmpty(info)) {
			return true;
		}
		return false;
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
	
	
	/**
	 * 
	 * @param param
	 * @return Billed-已在账单中存在,不能重算,建议删除账单后重试;Calculated-已计算,是否继续重算;Error-系统错误;OK-可重算
	 */
	@Expose
	public Properties validRetry(Map<String, Object> param) {
		Properties ret = bizGanxianWayBillService.validRetry(param);
		return ret;
	}
	
	@Expose
	public String reCalculate(Map<String, Object> param){
		if(bizGanxianWayBillService.reCalculate(param) == 0){
			return "重算异常";
		}
		return "操作成功! 正在重算...";
	}
	
	
	/**
	 * 初始化参数
	 */
	private void init(){
		//系统参数
		wareHouseMap = new HashMap<String, String>();
		elecWareHouseMap = new HashMap<String, String>();
		customerMap = new HashMap<String, String>();
		carModelMap = new HashMap<String, String>();
		cargoIsLightMap = new HashMap<String, String>();
		bizTypeMap = new HashMap<String, String>();
		airportMap = new HashMap<String, PubAirportEntity>();
				
		//电商仓库
		PageInfo<PubElecWarehouseEntity> ewareHousePageInfo = elecWareHouseService.queryAll(null, 0, Integer.MAX_VALUE);
		if (null != ewareHousePageInfo && ewareHousePageInfo.getList().size() > 0) {
			for (PubElecWarehouseEntity elecWareHouse : ewareHousePageInfo.getList()) {
				if (null != elecWareHouse) {
					elecWareHouseMap.put(elecWareHouse.getWarehouseName(), elecWareHouse.getWarehouseCode());
					wareHouseMap.put(elecWareHouse.getWarehouseName(), elecWareHouse.getWarehouseCode());
				}
			}
		}
		
		//仓库
		List<WarehouseVo> wareHouseList = warehouseService.queryAllWarehouse();
		if(null != wareHouseList && wareHouseList.size()>0){
			for(WarehouseVo wareHouse : wareHouseList){
				wareHouseMap.put(wareHouse.getWarehousename(), wareHouse.getWarehouseid());
			}
		}
		
		//车型
		List<SystemCodeEntity> carModelList = systemCodeService.findEnumList("MOTORCYCLE_TYPE");
		if (null != carModelList && carModelList.size() > 0) {
			for (SystemCodeEntity sysEntity : carModelList) {
				carModelMap.put(sysEntity.getCodeName(), sysEntity.getCode());
			}
		}
		
		//机场
		List<PubAirportEntity> airportList = airportService.query(null);
		if (null != airportList && airportList.size() > 0) {
			for (PubAirportEntity entity : airportList) {
				airportMap.put(entity.getAirportName(), entity);
			}
		}
		//是否轻货
		cargoIsLightMap.put(CargoIsLightEnum.YES.getDesc(), CargoIsLightEnum.YES.getCode());
		cargoIsLightMap.put(CargoIsLightEnum.NO.getDesc(), CargoIsLightEnum.NO.getCode());
		
		//业务类型范围
		bizTypeMap.put(TransportWayBillTypeEnum.TC.getDesc(), TransportWayBillTypeEnum.TC.getCode());
		bizTypeMap.put(TransportWayBillTypeEnum.CJ.getDesc(), TransportWayBillTypeEnum.CJ.getCode());
		bizTypeMap.put(TransportWayBillTypeEnum.DSZL.getDesc(), TransportWayBillTypeEnum.DSZL.getCode());
		bizTypeMap.put(TransportWayBillTypeEnum.HXD.getDesc(), TransportWayBillTypeEnum.HXD.getCode());
	}
	
	@DataProvider
	public void queryGroup(Page<BizGanxianWayBillEntity> page, Map<String, Object> param) {
		if (null == param) {
			param = new HashMap<String, Object>();
		}else if(null != param && param.containsKey("isCalculated")){
			if(StringUtils.equalsIgnoreCase(String.valueOf(param.get("isCalculated")), "ALL")){
				param.put("isCalculated", "");
			}else{
				param.put("isCalculated", String.valueOf(param.get("isCalculated")));
			}
		}
		
		PageInfo<BizGanxianWayBillEntity> pageInfo = bizGanxianWayBillService.queryGroup(param, page.getPageNo(), page.getPageSize());
		if (pageInfo != null) {
			page.setEntities(pageInfo.getList());
			page.setEntityCount((int) pageInfo.getTotal());
		}
	}
	
	@DataProvider
	public String queryDelete(Page<BizGanxianWayBillEntity> page,Map<String, Object> param){
		try {
			List<BizGanxianWayBillEntity> list = bizGanxianWayBillService.queryDelete(param);
			bizGanxianWayBillService.deleteFees(param);
			bizGanxianWayBillService.deleteBatch(list);
		} catch (Exception e) {
			logger.error(e.getMessage());
			//写入日志
			BmsErrorLogInfoEntity bmsErrorLogInfoEntity=new BmsErrorLogInfoEntity(this.getClass().getSimpleName(),Thread.currentThread().getStackTrace()[1].getMethodName(), "", e.toString());
			bmsErrorLogInfoService.log(bmsErrorLogInfoEntity);
			return "fail";
		}
		
		return "success";
		
	}
	
	@DataResolver
	public int deleteBatch(List<BizGanxianWayBillEntity> list){
		return bizGanxianWayBillService.deleteBatch(list);
	}
	
	
}
