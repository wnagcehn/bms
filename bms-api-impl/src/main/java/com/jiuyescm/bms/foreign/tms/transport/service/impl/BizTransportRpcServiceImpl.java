package com.jiuyescm.bms.foreign.tms.transport.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.base.airport.entity.PubAirportEntity;
import com.jiuyescm.bms.base.airport.service.IPubAirportService;
import com.jiuyescm.bms.base.dictionary.entity.SystemCodeEntity;
import com.jiuyescm.bms.base.dictionary.service.ISystemCodeService;
import com.jiuyescm.bms.base.ewarehouse.entity.PubElecWarehouseEntity;
import com.jiuyescm.bms.base.ewarehouse.service.IEwareHouseService;
import com.jiuyescm.bms.biz.transport.entity.BizGanxianWayBillEntity;
import com.jiuyescm.bms.biz.transport.service.IBizGanxianWayBillService;
import com.jiuyescm.bms.biz.transport.service.IBizTransportRpcService;
import com.jiuyescm.bms.biz.transport.vo.BizGanxianWaybillReturnVo;
import com.jiuyescm.bms.biz.transport.vo.BizGanxianWaybillVo;
import com.jiuyescm.bms.common.constants.ExceptionConstant;
import com.jiuyescm.bms.common.constants.MessageConstant;
import com.jiuyescm.bms.common.constants.TransportMessageConstant;
import com.jiuyescm.bms.common.entity.CalculateVo;
import com.jiuyescm.bms.common.enumtype.CalculateState;
import com.jiuyescm.bms.common.enumtype.CharageTypeEnum;
import com.jiuyescm.bms.common.enumtype.InterfaceTypeEnum;
import com.jiuyescm.bms.common.enumtype.TransportWayBillTypeEnum;
import com.jiuyescm.bms.common.sequence.service.SequenceService;
import com.jiuyescm.bms.fees.IFeesReceiverDeliverService;
import com.jiuyescm.bms.fees.entity.FeesReceiveDeliverEntity;
import com.jiuyescm.bms.quotation.contract.entity.PriceContractInfoEntity;
import com.jiuyescm.bms.quotation.contract.service.IPriceContractService;
import com.jiuyescm.cfm.common.JAppContext;
import com.jiuyescm.common.ConstantInterface;
import com.jiuyescm.common.utils.ArithUtil;
import com.jiuyescm.mdm.customer.api.IAddressService;
import com.jiuyescm.mdm.customer.api.ICustomerService;
import com.jiuyescm.mdm.warehouse.api.IWarehouseService;
import com.jiuyescm.mdm.warehouse.vo.WarehouseVo;

/**
 * 运输业务数据Rpc接口实现类
 * @author yangss
 */
@Service("bizTransportRpcService")
public class BizTransportRpcServiceImpl implements IBizTransportRpcService{

	private static final Logger logger = Logger.getLogger(BizTransportRpcServiceImpl.class.getName());
	
	@Autowired
	private ISystemCodeService systemCodeService;
	@Autowired 
	private SequenceService sequenceService;
	@Resource 
	private ICustomerService customerService;
	@Autowired 
	private IWarehouseService warehouseService;
	@Autowired 
	private IEwareHouseService elecWareHouseService;
	@Autowired
	private IAddressService addressService;
	@Autowired
	private IPubAirportService airportService;
	@Autowired
	private IBizGanxianWayBillService bizGanxianWayBillService;
	/*@Autowired 
	private IFeesCalculateRpcService feesCalculateRpcServiceImpl;*/
	@Autowired
	private IPriceContractService priceContractService;
	@Autowired 
	private IFeesReceiverDeliverService feesReceiveTransportService;
	
	//系统参数
	private Map<String, String> wareHouseMap = null;
	private Map<String, String> elecWareHouseMap = null;
	private Map<String, String> carModelMap = null;
	private Map<String, PubAirportEntity> airportMap = null;
	private final double DEFAULTAMOUNT = 0.0;
		
	/**
	 * 推送运输业务数据，返回BMS系统计算费用金额
	 */
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
	public synchronized List<BizGanxianWaybillReturnVo> pushTransportBizBatch(List<BizGanxianWaybillVo> list) {
		List<BizGanxianWaybillReturnVo> returnList = new ArrayList<BizGanxianWaybillReturnVo>();
		if (null == list || list.isEmpty()) {
			encapErrReturnMsg(returnList, null, TransportMessageConstant.BIZ_NULL_MSG);
			return returnList;
		}
		
		String tmsId = "";
		try{
			init();
			for (BizGanxianWaybillVo vo : list) {
				//非空，格式校验
				//tmsId
				checkNullTmsId(vo.getTmsId(), returnList);
				tmsId = vo.getTmsId();
				//计费方式
				checkNullChargeType(vo, returnList);
				if (null != returnList && returnList.size() > 0) {
					continue;
				}
				
				String chargeType = vo.getChargeType();
				if (CharageTypeEnum.REC.getCode().equals(chargeType)) {
					//应收
					checkReceiveBiz(vo, returnList);
					if (null != returnList && returnList.size() > 0) {
						continue;
					}
					//业务数据是否存在
					BizGanxianWayBillEntity bizEntity = queryByTmsId(vo.getTmsId(), returnList);
					if (null != bizEntity) {
						encapErrReturnMsg(returnList, vo.getTmsId(), TransportMessageConstant.BIZ_DATA_EXIST_MSG);
						continue;
					}
					
					//计算费用
					FeesReceiveDeliverEntity feeEntity = calculateReceive(vo);
					if (CalculateState.Finish.getCode().equals(vo.getIsCalculated())) {
						if (InterfaceTypeEnum.PUT.getCode().equals(vo.getInterfaceType())) {
							//接口类型为推送才保存费用，其他的直接返回
							feesReceiveTransportService.addFeesReceiveDeliver(feeEntity);
							//保存业务数据
							BizGanxianWayBillEntity entity = new BizGanxianWayBillEntity();
							BeanUtils.copyProperties(vo, entity);
							entity.setCreator(JAppContext.currentUserName());
							entity.setCreateTime(JAppContext.currentTimestamp());
							entity.setDelFlag(ConstantInterface.DelFlag.NO);
							bizGanxianWayBillService.save(entity);
						}
						
						encapSucReturnMsg(returnList, tmsId, vo.getIsLight(), vo.getSysAmount());
					}else {
						encapErrReturnMsg(returnList, tmsId, vo.getRemark());
					}
				}else if (CharageTypeEnum.PAY.getCode().equals(chargeType)) {
					//应付
				}
			}
		} catch (Exception e) {
			logger.error(ExceptionConstant.WAYBILL_SAVE_EXCEPTION_MSG, e);
			encapErrReturnMsg(returnList, tmsId, MessageConstant.SYSTEM_ERROR_MSG);
		}
		
		return returnList;
	}

	/**
	 * 作废运输业务数据
	 */
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
	public synchronized List<BizGanxianWaybillReturnVo> invalidTransportBizBatch(List<String> list) {
		List<BizGanxianWaybillReturnVo> returnList = new ArrayList<BizGanxianWaybillReturnVo>();
		if (null == list || list.isEmpty()) {
			encapErrReturnMsg(returnList, null, TransportMessageConstant.BIZ_NULL_MSG);
			return returnList;
		}
		
		for (String tmsId : list) {
			//非空校验
			checkNullTmsId(tmsId, returnList);
			if (null != returnList && returnList.size() > 0) {
				continue;
			}
			//是否有此业务数据
			BizGanxianWayBillEntity bizEntity = queryByTmsId(tmsId, returnList);
			if (null == bizEntity) {
				encapErrReturnMsg(returnList, tmsId, TransportMessageConstant.BIZ_DATA_NOT_EXIST_MSG);
				continue;
			}
			//是否生成账单，生成账单不允许删除
			if (StringUtils.isEmpty(bizEntity.getFeesNo())) {
				encapErrReturnMsg(returnList, tmsId, TransportMessageConstant.BIZ_FEESNO_NOT_EXIST_MSG);
				continue;
			}else {
				Map<String, Object> condition = new HashMap<String, Object>();
				condition.put("feesNo", bizEntity.getFeesNo());
				FeesReceiveDeliverEntity feeEntity = feesReceiveTransportService.query(condition);
				if (StringUtils.isNotEmpty(feeEntity.getBillno())) {
					encapErrReturnMsg(returnList, tmsId, TransportMessageConstant.FEES_BILLNO_EXIST_MSG);
					continue;
				}
			}
			
			//删除业务数据和费用
			Map<String, Object> condition = new HashMap<String, Object>();
			condition.put("tmsId", tmsId);
			int delNum = bizGanxianWayBillService.deleteBizAndFees(condition);
			if (delNum > 0) {
				encapSucReturnMsg(returnList, tmsId, null, 0.0);
			}else {
				encapErrReturnMsg(returnList, tmsId, TransportMessageConstant.BIZ_CANCEL_FAIL_MSG);
			}
		}
		
		return returnList;
	}

	/**
	 * 初始化参数
	 */
	private void init(){
		//系统参数
		wareHouseMap = new HashMap<String, String>();
		elecWareHouseMap = new HashMap<String, String>();
		carModelMap = new HashMap<String, String>();
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
	}
	
	/**
	 * 校验应收业务数据
	 * @param vo
	 * @param errList
	 */
	private void checkReceiveBiz(BizGanxianWaybillVo vo, List<BizGanxianWaybillReturnVo> returnList){
		//业务类型
		checkNullBizTypeCode(vo, returnList);
		//商家Id
		checkNullCustomerId(vo, returnList);
		//订单号
		checkNullOrderNo(vo, returnList);
		//发货日期
		checkNullSendTime(vo, returnList);
		//根据业务类型校验
		String bizTypeCode = vo.getBizTypeCode();
		if (TransportWayBillTypeEnum.TC.getCode().equals(bizTypeCode)) {
			checkNullInfoTC(vo, returnList);
		}else if (TransportWayBillTypeEnum.CJ.getCode().equals(bizTypeCode)) {
			checkNullInfoCJ(vo, returnList);
		}else if (TransportWayBillTypeEnum.DSZL.getCode().equals(bizTypeCode)) {
			checkNullInfoDSZL(vo, returnList);
		}else if (TransportWayBillTypeEnum.HXD.getCode().equals(bizTypeCode)) {
			checkNullInfoHXD(vo, returnList);
		}
		//接口类型（是否需要持久化）
		checkNullInterfaceType(vo, returnList);
	}
	
	/**
	 * 非空校验-同城
	 */
	private void checkNullInfoTC(BizGanxianWaybillVo vo, List<BizGanxianWaybillReturnVo> errList){
		//始发省份、始发城市 、目的省份、目的城市、 车型
		checkNullSendPC(vo, errList);
		checkNullReceivePC(vo, errList);
		//车型
		checkNullCarmodel(vo, errList);
	}
	
	/**
	 * 非空校验-城际
	 */
	private void checkNullInfoCJ(BizGanxianWaybillVo vo, List<BizGanxianWaybillReturnVo> errList){
		//始发省份、始发城市、 目的省份、 目的城市、重量、体积
		checkNullSendPC(vo, errList);
		checkNullReceivePC(vo, errList);
		//重量
		checkNullWeight(vo, errList);
		//体积
		checkNullVolume(vo, errList);
	}
	
	/**
	 * 非空校验-电商专列
	 */
	private void checkNullInfoDSZL(BizGanxianWaybillVo vo, List<BizGanxianWaybillReturnVo> errList){
		//始发仓 、 电商仓、 车型
		//仓库
		String warehouseName = vo.getStartStation();
		if(StringUtils.isEmpty(vo.getStartStationCode()) || StringUtils.isEmpty(warehouseName)){
			encapErrReturnMsg(errList, vo.getTmsId(), TransportMessageConstant.FROMWAREHOUSE_NULL_MSG);
		}else {
			vo.setWarehouseCode(vo.getStartStationCode());
			vo.setWarehouseName(warehouseName);
		}
		//电商仓
		String elecWarehouseName = vo.getEndStation();
		if(StringUtils.isEmpty(elecWarehouseName)){
			encapErrReturnMsg(errList, vo.getTmsId(), TransportMessageConstant.ENDSTATION_NULL_MSG);
		}
		
		//车型
		checkNullCarmodel(vo, errList);
	}
	
	/**
	 * 非空校验-航鲜达
	 * 省市从数据库表中获取
	 */
	private void checkNullInfoHXD(BizGanxianWaybillVo vo, List<BizGanxianWaybillReturnVo> errList){
		//机场、  目的省份、目的城市、 重量
		if (StringUtils.isEmpty(vo.getStartStation())) {
			encapErrReturnMsg(errList, vo.getTmsId(), TransportMessageConstant.STARTSTATION_NULL_MSG);
		}else {
			String airport = vo.getStartStation();
			if(!airportMap.containsKey(airport) || null == airportMap.get(airport)){
				encapErrReturnMsg(errList, vo.getTmsId(), "始发站点["+airport+"]没有在机场表中维护!");
			}
		}
		
		//重量
		checkNullWeight(vo, errList);
		//目的省、市
		checkNullReceivePC(vo, errList);
	}
	
	/**
	 * TmsId非空校验
	 */
	private void checkNullTmsId(String tmsId, List<BizGanxianWaybillReturnVo> errList){
		if (StringUtils.isEmpty(tmsId)) {
			encapErrReturnMsg(errList, null, TransportMessageConstant.TMS_ID_NULL_MSG);
		}
	}
	
	/**
	 * 计费方式非空校验
	 */
	private void checkNullChargeType(BizGanxianWaybillVo vo, List<BizGanxianWaybillReturnVo> errList){
		if (StringUtils.isEmpty(vo.getChargeType())) {
			encapErrReturnMsg(errList, vo.getTmsId(), TransportMessageConstant.CHARGETYPE_NULL_MSG);
		}else {
			if (!CharageTypeEnum.getMap().containsKey(vo.getChargeType())) {
				encapErrReturnMsg(errList, vo.getTmsId(), TransportMessageConstant.CHARGETYPE_ERR_MSG);
			}
		}
	}
	
	/**
	 * 业务类型非空校验
	 */
	private void checkNullBizTypeCode(BizGanxianWaybillVo vo, List<BizGanxianWaybillReturnVo> errList){
		if (StringUtils.isEmpty(vo.getBizTypeCode())) {
			encapErrReturnMsg(errList, vo.getTmsId(), TransportMessageConstant.TRANSPORTTYPE_NULL_MSG);
		}else {
			if (!TransportWayBillTypeEnum.getMap().containsKey(vo.getBizTypeCode())) {
				encapErrReturnMsg(errList, vo.getTmsId(), TransportMessageConstant.TRANSPORTTYPE_ERROR_MSG);
			}
		}
	}
	
	/**
	 * 商家非空校验
	 * @param param
	 * @return
	 */
	private void checkNullCustomerId(BizGanxianWaybillVo vo, List<BizGanxianWaybillReturnVo> errList){
		if (StringUtils.isEmpty(vo.getCustomerId()) || StringUtils.isEmpty(vo.getCustomerName())) {
			encapErrReturnMsg(errList, vo.getTmsId(), MessageConstant.CUSTOMER_INFO_ISNULL_MSG);
		}
	}
	
	/**
	 * 订单号非空校验
	 */
	private void checkNullOrderNo(BizGanxianWaybillVo vo, List<BizGanxianWaybillReturnVo> errList){
		if (StringUtils.isEmpty(vo.getOrderNo())) {
			encapErrReturnMsg(errList, vo.getTmsId(), TransportMessageConstant.ORDERNO_NULL_MSG);
		}
	}
	
	/**
	 * 发货日期非空校验
	 */
	private void checkNullSendTime(BizGanxianWaybillVo vo, List<BizGanxianWaybillReturnVo> errList){
		if (null == vo.getSendTime()) {
			encapErrReturnMsg(errList, vo.getTmsId(), TransportMessageConstant.SENDTIME_NULL_MSG);
		}
	}
	
	/**
	 * 重量非空校验
	 */
	private void checkNullWeight(BizGanxianWaybillVo vo, List<BizGanxianWaybillReturnVo> errList){
		if (null == vo.getTotalWeight()) {
			encapErrReturnMsg(errList, vo.getTmsId(), TransportMessageConstant.WEIGHT_NULL_MSG);
		}else if (0.0 >= vo.getTotalWeight()) {
			encapErrReturnMsg(errList, vo.getTmsId(), TransportMessageConstant.WEIGHT_GT0_MSG);
		}
	}
	
	/**
	 * 体积非空校验
	 */
	private void checkNullVolume(BizGanxianWaybillVo vo, List<BizGanxianWaybillReturnVo> errList){
		if (null == vo.getTotalVolume()) {
			encapErrReturnMsg(errList, vo.getTmsId(), TransportMessageConstant.VOLUME_NULL_MSG);
		}else if (0.0 >= vo.getTotalVolume()) {
			encapErrReturnMsg(errList, vo.getTmsId(), TransportMessageConstant.VOLUME_GT0_MSG);
		}
	}
	
	/**
	 * 车型非空校验
	 */
	private void checkNullCarmodel(BizGanxianWaybillVo vo, List<BizGanxianWaybillReturnVo> errList){
		if (StringUtils.isEmpty(vo.getCarModel())) {
			encapErrReturnMsg(errList, vo.getTmsId(), TransportMessageConstant.CARMODEL_NULL_MSG);
		}else {
			if (!carModelMap.containsKey(vo.getCarModel()) || 
					StringUtils.isEmpty(carModelMap.get(vo.getCarModel()))) {
				encapErrReturnMsg(errList, vo.getTmsId(), TransportMessageConstant.CARMODEL_ERROR_MSG);
			}
		}
	}
	
	/**
	 * 校验接口类型
	 * @param param
	 * @return
	 */
	private void checkNullInterfaceType(BizGanxianWaybillVo vo, List<BizGanxianWaybillReturnVo> errList){
		if (StringUtils.isEmpty(vo.getInterfaceType())) {
			encapErrReturnMsg(errList, vo.getTmsId(), TransportMessageConstant.INTERFACETYPE_NULL_MSG);
		}else {
			if (!InterfaceTypeEnum.getMap().containsKey(vo.getInterfaceType())) {
				encapErrReturnMsg(errList, vo.getTmsId(), TransportMessageConstant.INTERFACETYPE_ERR_MSG);
			}
		}
	}
	
	/**
	 * 校验始发省、市非空
	 * @param param
	 * @return
	 */
	private void checkNullSendPC(BizGanxianWaybillVo vo, List<BizGanxianWaybillReturnVo> errList){
		//始发省份、始发城市
		if (StringUtils.isEmpty(vo.getSendProvinceName())) {
			encapErrReturnMsg(errList, vo.getTmsId(), TransportMessageConstant.FROMPROVINCE_NULL_MSG);
		}else {
			vo.setSendProvinceId(vo.getSendProvinceName());
			vo.setSendProvinceName(null);
		}
		if (StringUtils.isEmpty(vo.getSendCityName())) {
			encapErrReturnMsg(errList, vo.getTmsId(), TransportMessageConstant.FROMCITY_NULL_MSG);
		}else {
			vo.setSendCityId(vo.getSendCityName());
			vo.setSendCityName(null);
		}
	}
	
	/**
	 * 校验目的省、市非空
	 * @param param
	 * @return
	 */
	private void checkNullReceivePC(BizGanxianWaybillVo vo, List<BizGanxianWaybillReturnVo> errList){
		//目的省份、目的城市
		if (StringUtils.isEmpty(vo.getReceiverProvinceName())) {
			encapErrReturnMsg(errList, vo.getTmsId(), TransportMessageConstant.TOPROVINCE_NULL_MSG);
		}else {
			vo.setReceiverProvinceId(vo.getReceiverProvinceName());
			vo.setReceiverProvinceName(null);
		}
		if (StringUtils.isEmpty(vo.getReceiverCityName())) {
			encapErrReturnMsg(errList, vo.getTmsId(), TransportMessageConstant.TOCITY_NULL_MSG);
		}else {
			vo.setReceiverCityId(vo.getReceiverCityName());
			vo.setReceiverCityName(null);
		}
	}
	
	/**
	 * 业务数据是否已存在
	 */
	private BizGanxianWayBillEntity queryByTmsId(String tmsId, List<BizGanxianWaybillReturnVo> errList){
		//是否有此业务数据，有直接返回
		Map<String, Object> condition = new HashMap<String, Object>();
		condition.put("tmsId", tmsId);
		condition.put("delFlag", ConstantInterface.DelFlag.NO);
		BizGanxianWayBillEntity bizEntity = bizGanxianWayBillService.query(condition);
		return bizEntity;
	}
	
	/**
	 * 计算费用
	 * @return
	 */
	private FeesReceiveDeliverEntity calculateReceive(BizGanxianWaybillVo vo){
		FeesReceiveDeliverEntity transportFeeEntity = new FeesReceiveDeliverEntity();
		try {
			//查询合同
			Map<String,Object> aCondition=new HashMap<>();
			aCondition.put("customerId", vo.getCustomerId());
			aCondition.put("contractState", "1");
			aCondition.put("contractTypeCode", "CUSTOMER_CONTRACT");
			List<PriceContractInfoEntity> contractList = priceContractService.queryContract(aCondition);
			if(contractList == null || contractList.size() <= 0 
					|| StringUtils.isEmpty(contractList.get(0).getContractCode())){
				logger.info(String.format("运输费计算未查询到合同  订单号【%s】--商家【%s】", 
						vo.getOrderNo(), vo.getCustomerId()));
				vo.setIsCalculated(CalculateState.Contract_Miss.getCode());
				vo.setRemark(String.format("运输费计算未查询到合同  订单号【%s】--商家【%s】", 
						vo.getOrderNo(), vo.getCustomerId()));
			}
			
			CalculateVo calcuVo = new CalculateVo();
			calcuVo.setBizTypeCode("TRANSPORT");
			calcuVo.setSubjectId(vo.getBizTypeCode());
			calcuVo.setContractCode(contractList.get(0).getContractCode());//设置合同编号
			calcuVo.setObj(vo);
			//calcuVo = feesCalculateRpcServiceImpl.calculate(calcuVo);
			if(calcuVo.getSuccess()){
				//新增费用
				transportFeeEntity.setCreperson("system"); // 创建人ID
				transportFeeEntity.setCrepersonname("system"); // 创建人姓名
				//费用表的创建时间应为业务表的创建时间
				transportFeeEntity.setCretime(vo.getSendTime());
				transportFeeEntity.setOperationtime(vo.getCreateTime());	// 操作时间
				transportFeeEntity.setWarehouseCode(vo.getWarehouseCode());		// 仓库编码
				transportFeeEntity.setWarehouseName(vo.getWarehouseName());
				transportFeeEntity.setCustomerid(vo.getCustomerId());				// 商家Id
				transportFeeEntity.setCustomerName(vo.getCustomerName()); 			// 商家名称
				transportFeeEntity.setCostType("TRANSPORT_FEE");//运输类型(不是增值类型ADD_FEE)	// 费用类型
				transportFeeEntity.setSubjectCode(calcuVo.getSubjectId());				// 费用科目
				transportFeeEntity.setLinename(null);									// 线路名称
				transportFeeEntity.setOrderno(vo.getOrderNo());					// 订单号
				transportFeeEntity.setWaybillNo(vo.getWaybillNo());				// 运单号
				transportFeeEntity.setOriginProvince(vo.getSendProvinceId());
				transportFeeEntity.setOriginatingcity(vo.getSendCityId());			// 始发市
				transportFeeEntity.setOriginatingdistrict(vo.getSendDistrictId());	// 始发区
				transportFeeEntity.setTargetwarehouse(vo.getWarehouseCode());		// 目的仓
				transportFeeEntity.setTargetProvince(vo.getReceiverProvinceId());
				transportFeeEntity.setTargetcity(vo.getReceiverCityId()); 			// 目的市
				transportFeeEntity.setTargetdistrict(vo.getReceiverDistrictId()); 	// 目的区
				transportFeeEntity.setTemperaturetype(vo.getTemperatureTypeCode());// 温度类型
				transportFeeEntity.setCategory(null);									// 品类
				transportFeeEntity.setWeight(ArithUtil.add(vo.getTotalWeight(), vo.getAdjustWeight()));// 重量
				transportFeeEntity.setVolume(vo.getTotalVolume()); 				// 体积
				transportFeeEntity.setKilometers(null); 								// 公里数
				transportFeeEntity.setSpendtime(null); 									// 花费时间
				transportFeeEntity.setCarmodel(vo.getCarModel());					// 车型
				if(calcuVo != null && StringUtils.isNotBlank(calcuVo.getMobanCode())){
					transportFeeEntity.setTemplatenum(calcuVo.getMobanCode());			// 模板编号
				}else{
					transportFeeEntity.setTemplatenum(StringUtils.EMPTY);
				}
				if(vo != null && StringUtils.isNotBlank(vo.getIsLight())){
					// 是否轻货  1是;0不是
					if(StringUtils.equalsIgnoreCase(vo.getIsLight(), "Y")){
						transportFeeEntity.setIslight(1);
					}else{
						transportFeeEntity.setIslight(0);
					}
				}
				transportFeeEntity.setUnitprice(calcuVo.getUnitPrice());// 单价
				logger.info("计算完成单价》》》:" + calcuVo.getUnitPrice());
				logger.info("计算完成金额》》》:" + calcuVo.getPrice());
				if(null != calcuVo && null != calcuVo.getPrice()){
					transportFeeEntity.setTotleprice(calcuVo.getPrice().doubleValue());// 金额
				}else{
					vo.setIsCalculated(CalculateState.Quote_Miss.getCode());
					transportFeeEntity.setTotleprice(0.0d);
				}
				transportFeeEntity.setAccepttime(vo.getSendTime());// 发货时间
				transportFeeEntity.setSigntime(vo.getSignTime());// 收货时间
				transportFeeEntity.setBillno(null); // 账单编号
				transportFeeEntity.setRuleno(calcuVo.getRuleno()); // 规则编号
				transportFeeEntity.setExtarr1(null);// 扩展字段1
				transportFeeEntity.setExtarr2(null);// 扩展字段2
				transportFeeEntity.setExtarr3(null);// 扩展字段3
				transportFeeEntity.setExtarr4(null);// 扩展字段4
				transportFeeEntity.setExtarr5(null);// 扩展字段5
				transportFeeEntity.setQuantity(null); // 数量
				transportFeeEntity.setState("0");//未过账 状态
				
				if (null == calcuVo.getPrice()) {
					vo.setIsCalculated(CalculateState.Quote_Miss.getCode());
					vo.setRemark("报价缺失!");
				}else {
					//计算成功
					vo.setSysAmount(calcuVo.getPrice().doubleValue());
					vo.setIsCalculated(CalculateState.Finish.getCode());
					vo.setRemark("计算成功，新增费用表成功");
					if (StringUtils.isNotEmpty(vo.getInterfaceType()) &&
							InterfaceTypeEnum.PUT.getCode().equals(vo.getInterfaceType())) {
						//后面更改：2017-08-01将业务数据的【操作时间】写入费用表的【创建时间】字段.
						String feesNo = sequenceService.getBillNoOne("com.jiuyescm.bms.general.entity.FeesReceiveTransportEntity", "YSFY", "0000000000");
						if(StringUtils.isNotBlank(feesNo)){
							transportFeeEntity.setFeesNo(feesNo);// 费用编号
						}
						vo.setFeesNo(feesNo);
					}
				}
				//新增计算时间
				vo.setCalculateTime(JAppContext.currentTimestamp());
			}
			else{
				logger.info("运输费用计算失败"+calcuVo.getMsg());
				vo.setIsCalculated(CalculateState.Quote_Miss.getCode());
				vo.setRemark("费用计算失败"+calcuVo.getMsg());
			}
		} catch (Exception e) {
			logger.error(ExceptionConstant.MATCH_ADDRESS_RPC_EX_MSG, e);
			vo.setIsCalculated(CalculateState.Sys_Error.getCode());
			vo.setRemark("费用计算异常"+e.getMessage());
		}
		
		return transportFeeEntity;
	}
	
	/**
	 * 封装失败的返回信息
	 */
	private void encapErrReturnMsg(List<BizGanxianWaybillReturnVo> errorList, 
			String tmsId, String returnMsg){
		BizGanxianWaybillReturnVo returnVo = new BizGanxianWaybillReturnVo(tmsId, null, 
				DEFAULTAMOUNT, ConstantInterface.ReturnCode.FAIL, returnMsg);
		errorList.add(returnVo);
	}
	

	/**
	 * 封装成功的返回信息
	 */
	private void encapSucReturnMsg(List<BizGanxianWaybillReturnVo> errorList, 
			String tmsId, String isLight, double sysAmount){
		BizGanxianWaybillReturnVo returnVo = new BizGanxianWaybillReturnVo(tmsId, isLight, 
				sysAmount, ConstantInterface.ReturnCode.SUCCESS, MessageConstant.OPERATOR_SUCCESS_MSG);
		errorList.add(returnVo);
	}

}