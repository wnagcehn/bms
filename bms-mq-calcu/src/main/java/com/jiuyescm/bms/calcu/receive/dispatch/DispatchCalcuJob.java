package com.jiuyescm.bms.calcu.receive.dispatch;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import com.jiuyescm.bms.asyn.service.IBmsCalcuTaskService;
import com.jiuyescm.bms.asyn.vo.BmsCalcuTaskVo;
import com.jiuyescm.bms.base.calcu.vo.CalcuReqVo;
import com.jiuyescm.bms.base.dictionary.entity.SystemCodeEntity;
import com.jiuyescm.bms.base.group.service.IBmsGroupCustomerService;
import com.jiuyescm.bms.base.group.service.IBmsGroupService;
import com.jiuyescm.bms.base.group.vo.BmsGroupCustomerVo;
import com.jiuyescm.bms.base.group.vo.BmsGroupVo;
import com.jiuyescm.bms.base.monthFeeCount.service.IPubMonthFeeCountService;
import com.jiuyescm.bms.base.monthFeeCount.vo.PubMonthFeeCountVo;
import com.jiuyescm.bms.biz.dispatch.entity.BizDispatchBillEntity;
import com.jiuyescm.bms.biz.storage.repository.IBizOutstockPackmaterialRepository;
import com.jiuyescm.bms.calcu.base.ICalcuService;
import com.jiuyescm.bms.calcu.receive.BmsContractBase;
import com.jiuyescm.bms.calcu.receive.CommonService;
import com.jiuyescm.bms.calcu.receive.ContractCalcuService;
import com.jiuyescm.bms.calculate.api.IBmsCalcuService;
import com.jiuyescm.bms.calculate.vo.BmsFeesQtyVo;
import com.jiuyescm.bms.calculate.vo.CalcuContractVo;
import com.jiuyescm.bms.common.enumtype.CalculateState;
import com.jiuyescm.bms.correct.BmsMarkingProductsEntity;
import com.jiuyescm.bms.correct.repository.IBmsProductsWeightRepository;
import com.jiuyescm.bms.general.entity.BizDispatchCarrierChangeEntity;
import com.jiuyescm.bms.general.entity.FeesReceiveDispatchEntity;
import com.jiuyescm.bms.general.service.IFeesReceiveDispatchService;
import com.jiuyescm.bms.general.service.IPriceContractInfoService;
import com.jiuyescm.bms.general.service.ISystemCodeService;
import com.jiuyescm.bms.quotation.dispatch.entity.vo.BmsQuoteDispatchDetailVo;
import com.jiuyescm.bms.quotation.dispatch.repository.IPriceDispatchDao;
import com.jiuyescm.bms.receivable.dispatch.service.IBizDispatchBillService;
import com.jiuyescm.bs.util.StringUtil;
import com.jiuyescm.cfm.common.JAppContext;
import com.jiuyescm.common.utils.DoubleUtil;
import com.jiuyescm.contract.quote.vo.ContractBizTypeEnum;
import com.jiuyescm.contract.quote.vo.ContractQuoteQueryInfoVo;
import com.jiuyescm.mdm.carrier.api.ICarrierService;
import com.jiuyescm.mdm.carrier.vo.CarrierVo;
import com.jiuyescm.mdm.customer.api.IAddressService;
import com.jiuyescm.mdm.customer.vo.RegionVo;
import com.jiuyescm.mdm.warehouse.api.IWarehouseService;
import com.jiuyescm.mdm.warehouse.vo.WarehouseVo;

@Service("dispatchCalcuJob")
@Scope("prototype")
public class DispatchCalcuJob  extends BmsContractBase implements ICalcuService<BizDispatchBillEntity,FeesReceiveDispatchEntity> {

	private Logger logger = LoggerFactory.getLogger(DispatchCalcuJob.class);
	
	@Autowired private ISystemCodeService systemCodeService;
	@Autowired private IPubMonthFeeCountService pubMonthFeeCountService;
	@Autowired private IBmsGroupService bmsGroupService;
	@Autowired private IBmsGroupCustomerService bmsGroupCustomerService;
	@Autowired private ICarrierService carrierService;
	@Autowired private IWarehouseService warehouseService;
	@Autowired private IBizDispatchBillService bizDispatchBillService;
	@Autowired private CommonService commonService;
	@Autowired private IPriceDispatchDao iPriceDispatchDao;
	@Autowired private IBmsProductsWeightRepository bmsProductsWeightRepository;
	@Autowired private IBizOutstockPackmaterialRepository bizOutstockPackmaterialRepository;
	@Autowired private IAddressService omsAddressService;
	@Autowired private IPriceContractInfoService jobPriceContractInfoService;
	@Autowired private ContractCalcuService contractCalcuService;
	@Autowired IBmsCalcuTaskService bmsCalcuTaskService;
	
	@Autowired private IFeesReceiveDispatchService feesReceiveDispatchService;
	@Autowired IBmsCalcuService bmsCalcuService;

	//private String quoTempleteCode = null;
	private Map<String, Object> errorMap = null;
	private Map<String, String> dispatchSubjectMap = null; 	//物流商与配送科目映射
	private Map<String, String> deliverNoCalcuMap = null;	//不计费宅配商列表
	private List<String> monthCountList=null;
	//private Map<String, String> throwWeightMap=null;
	private List<String> changeCusList=null;
	private List<String> cancelCusList=null;
	private Map<String, String> carrierMap=null;
	private Map<String,WarehouseVo> wareMap=null;
	private List<SystemCodeEntity> throwWeightList=null;
	private Map<String, String> contractItemMap = null;
    private CalcuContractVo contract=null;
	
	
	public void process(BmsCalcuTaskVo taskVo,String contractAttr){
		super.process(taskVo, contractAttr);
		serviceSubjectCode = subjectCode;//配送签约服务初始化
		initConf();
	}
	
	@Override
	public void getQuoTemplete() {
		
	}

	@Override
	public void calcu(Map<String, Object> map) {
		
		List<BizDispatchBillEntity> bizList = bizDispatchBillService.query(map);
		List<FeesReceiveDispatchEntity> fees = new ArrayList<>();
		if(bizList == null || bizList.size() == 0){
			commonService.taskCountReport(taskVo, "DISPATCH");
			return;
		}
		logger.info("taskId={} 查询行数【{}】",taskVo.getTaskId(),bizList.size());
		for (BizDispatchBillEntity entity : bizList) {
		    errorMap = new HashMap<String, Object>();
			FeesReceiveDispatchEntity fee = initFee(entity);			
			try {
				fees.add(fee);
				if(isNoExe(entity, fee)){
					continue; //如果不计算费用,后面的逻辑不在执行，只是在最后更新数据库状态
				}
				//优先合同在线计算
                calcuForContract(entity,fee);
                //如果返回的是合同缺失，则继续BMS计算
                if("CONTRACT_LIST_NULL".equals(errorMap.get("code"))){
                    calcuForBms(entity,fee);
                }
			} catch (Exception e) {
				// TODO: handle exception
				fee.setIsCalculated(CalculateState.Sys_Error.getCode());
				fee.setCalcuMsg("系统异常");
				logger.error("计算异常",e);
			}
		}
		updateBatch(bizList,fees);
		calceCount += bizList.size();
		//更新任务计算各字段
		updateTask(taskVo,calceCount);	
		int taskRate = (int)Math.floor((calceCount*100)/unCalcuCount);
		try {
			if(unCalcuCount!=0){
				bmsCalcuTaskService.updateRate(taskVo.getTaskId(), taskRate);
			}
		} catch (Exception e) {
			logger.error("更新任务进度异常",e);
		}
		if(bizList!=null && bizList.size() == 1000){
			calcu(map);
		}
		
	}

	@Override
	public void initConf() {
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		//获取物流商id与配送科目的映射关系  <物流商ID,费用科目>
		dispatchSubjectMap = new HashMap<>();
		map.put("typeCode", "DISPATCH_COMPANY");
		List<SystemCodeEntity> scList = systemCodeService.querySysCodes(map);
		for (SystemCodeEntity systemCodeEntity : scList) {
			dispatchSubjectMap.put(systemCodeEntity.getExtattr1(), systemCodeEntity.getCode());
		}
		
		//获取不计费的宅配商 <宅配商ID,宅配商名称>
		deliverNoCalcuMap = new HashMap<>();
		map.clear();
		map.put("typeCode", "NO_FEES_DELIVER");
		List<SystemCodeEntity> noFeesDelivers = systemCodeService.querySysCodes(map);//查询不计算费用的宅配商列表，如客户自提，商家自提等
		for (SystemCodeEntity systemCodeEntity : noFeesDelivers) {
			deliverNoCalcuMap.put(systemCodeEntity.getExtattr1(), systemCodeEntity.getCodeName());
		}
		//获取月结账号
		map.clear();
		map.put("ownflag", "0");
		map.put("delflag", "0");
		List<PubMonthFeeCountVo> monthFeeList=pubMonthFeeCountService.query(map);
		monthCountList=new ArrayList<>();
		if(monthFeeList.size()>0){
			for(PubMonthFeeCountVo en:monthFeeList){
				monthCountList.add(en.getCarrierId()+"&"+en.getMonthFeeCount());
			}
		}
		
		//获取计抛物流商
	/*	throwWeightMap = new HashMap<>();
		map.clear();
		map.put("typeCode", "THROW_WEIGHT_CARRIER");
		List<SystemCodeEntity> throwList = systemCodeService.querySysCodes(map);//计抛的物流商
		for (SystemCodeEntity systemCodeEntity : throwList) {
			throwWeightMap.put(systemCodeEntity.getCode(), systemCodeEntity.getCodeName());
		}*/
		throwWeightList=new ArrayList<>();
		map.clear();
        map.put("typeCode", "THROW_WEIGHT_CARRIER");
		throwWeightList = systemCodeService.querySysCodes(map);//计抛的物流商
		
		//指定的商家
		map.clear();
		map.put("groupCode", "jiuye_to_shunfeng");
		map.put("bizType", "group_customer");
		BmsGroupVo bmsGroup=bmsGroupService.queryOne(map);
		if(bmsGroup!=null){
			changeCusList=bmsGroupCustomerService.queryCustomerByGroupId(bmsGroup.getId());
		}
		
		//指定需要计算取消状态单子的商家
		map= new HashMap<String, Object>();
		map.put("groupCode", "calculate_cancel_customer");
		map.put("bizType", "group_customer");
		BmsGroupVo bmsCancelCus=bmsGroupService.queryOne(map);
		if(bmsCancelCus!=null){
			cancelCusList=bmsGroupCustomerService.queryCustomerByGroupId(bmsCancelCus.getId());
		}	
		
		//查询所有物流商信息 <物流商ID,物流商名称>
		carrierMap = new HashMap<>();
		List<CarrierVo> carrierList=carrierService.queryAllCarrier();
		for(CarrierVo vo:carrierList){
			carrierMap.put(vo.getCarrierid(), vo.getName());
		}
		
		//仓库
		wareMap=new HashMap<String,WarehouseVo>();
		List<WarehouseVo> wareList=warehouseService.queryAllWarehouse();
		for(WarehouseVo vo:wareList){
			wareMap.put(vo.getWarehouseid(), vo);
		}
	}

	@Override
	public FeesReceiveDispatchEntity initFee(BizDispatchBillEntity entity) {
		FeesReceiveDispatchEntity fee = new FeesReceiveDispatchEntity();
		fee.setFeesNo(entity.getFeesNo());
		try {
	        //费用初始化
            fee.setCreator("system");
            fee.setStatus("0");
            fee.setDelFlag("0");
            fee.setAmount(0.0d);                    //入仓金额
            fee.setWeightLimit(0.0d);
            fee.setUnitPrice(0.0d);
            fee.setHeadWeight(0.0d);
            fee.setHeadPrice(0.0d);
            fee.setContinuedWeight(0.0d);
            fee.setContinuedPrice(0.0d);
            fee.setPriceId("");
            fee.setServiceTypeCode(StringUtils.isEmpty(entity.getAdjustServiceTypeCode())?entity.getServiceTypeCode():entity.getAdjustServiceTypeCode());
            fee.setCalcuMsg("");
			
			//计费参数获取
	        //1)***********************获取计费物流商******************************
			dispatchSubjectMap.get(getCarrierId(entity));
			String carrierId=entity.getChargeCarrierId();
			//2)***********************获取新泡重************************
			//新增逻辑，若要按抛重计算，此时的泡重需要我们自己去计算运单中所有耗材中最大的体积/6000
			double newThrowWeight=getNewThrowWeight(entity);
			//将新泡重赋值
			entity.setCorrectThrowWeight(newThrowWeight);	
			//3)***********************获取省市区和计费重量***********************
			String provinceId="";
			String cityId="";
			String districtId="";
			//调整省市区不为空 优先取调整省市区
			if(StringUtils.isNotBlank(entity.getAdjustProvinceId())||StringUtils.isNotBlank(entity.getAdjustCityId())||StringUtils.isNotBlank(entity.getAdjustDistrictId())){
				provinceId=entity.getAdjustProvinceId();
				cityId=entity.getAdjustCityId();
				districtId=entity.getAdjustDistrictId();
			}else{
				provinceId=entity.getReceiveProvinceId();
				cityId=entity.getReceiveCityId();
				districtId=entity.getReceiveDistrictId();
			}
			
			boolean isThowCount= false;            
            for(SystemCodeEntity s:throwWeightList){
                if(s.getCode().equals(carrierId)){
                    if(StringUtils.isBlank(s.getExtattr1())){
                        isThowCount = true;//false-此单参与计算计抛
                    }else{
                        if(s.getExtattr1().equals(fee.getServiceTypeCode())){
                            isThowCount=true;
                        }
                    }
                }
            }           
            //是否计抛
            if(isThowCount){//计抛
				RegionVo vo = new RegionVo(ReplaceChar(provinceId), ReplaceChar(cityId),ReplaceChar(districtId));
				RegionVo matchVo = omsAddressService.queryNameByAlias(vo);
				if ((StringUtils.isNotBlank(provinceId) && StringUtils.isBlank(matchVo.getProvince())) ||
						StringUtils.isNotBlank(cityId) && StringUtils.isBlank(matchVo.getCity()) ||
						StringUtils.isNotBlank(districtId) && StringUtils.isBlank(matchVo.getDistrict())) {
					//XxlJobLogger.log("-->"+entity.getId()+"收件人地址存在空值  省【{0}】市【{1}】区【{2}】 运单号【{3}】:"+ provinceId,cityId,districtId,entity.getWaybillNo());
				}else{
					entity.setReceiveProvinceId(matchVo.getProvince());
					entity.setReceiveCityId(matchVo.getCity());
					entity.setReceiveDistrictId(matchVo.getDistrict());
					fee.setToProvinceName(matchVo.getProvince());
					fee.setToCityName(matchVo.getCity());
					fee.setToDistrictName(matchVo.getDistrict());
				}
				
				//如果存在调整重量，优先取
				if(!DoubleUtil.isBlank(entity.getAdjustWeight())){
					double dd = getResult(entity.getAdjustWeight(),carrierId);
					entity.setWeight(dd);
					entity.setTotalWeight(entity.getAdjustWeight());
				}else{
					//新增逻辑判断，判断是否有修正重量，用运单号去修正表里去查询
					boolean hasCorrect = false;
					double correctWeight = 0.0;
					Map<String,Object> condition=new HashMap<String,Object>();
					condition.put("waybillNo", entity.getWaybillNo());
					BmsMarkingProductsEntity markEntity=bmsProductsWeightRepository.queryMarkVo(condition);
					if(markEntity!=null && markEntity.getCorrectWeight()!=null && !DoubleUtil.isBlank(markEntity.getCorrectWeight().doubleValue())){
						hasCorrect = true;
						correctWeight = markEntity.getCorrectWeight().doubleValue();
					}
							
					//纠正泡重不存在
					if(DoubleUtil.isBlank(entity.getCorrectThrowWeight())){
						if (hasCorrect) {
							//有纠正重量时，直接比较纠正重量和物流商重量
							if(entity.getCarrierWeight()>=correctWeight){
								//物流商重量大于等于纠正重量
								double dd = getResult(entity.getCarrierWeight(),carrierId);
								entity.setWeight(dd);
								entity.setTotalWeight(entity.getCarrierWeight());
							}else{
								//物流商重量小于纠正重量
								double dd = getResult(correctWeight,carrierId);
								entity.setWeight(dd);
						/*		double resultWeight = compareWeight(entity.getTotalWeight(), 
										getResult(entity.getTotalWeight(),carrierId), correctWeight);*/
								double resultWeight = compareTotalWeight(entity.getTotalWeight(),correctWeight,carrierId);
								entity.setTotalWeight(resultWeight);
							}
						}else {
							//没有纠正重量，物流商重量和运单重量比较
							if(entity.getCarrierWeight()>=entity.getTotalWeight()){
								//物流商重量大于等于运单重量
								double dd = getResult(entity.getCarrierWeight(),carrierId);
								entity.setWeight(dd);
								entity.setTotalWeight(entity.getCarrierWeight());
							}else{
								//物流商重量小于重量
								double dd = getResult(entity.getTotalWeight(),carrierId);
								entity.setWeight(dd);
								entity.setTotalWeight(entity.getTotalWeight());
							}
						}
						
					}else{
						//泡重存在时
						if (hasCorrect) {
							// 有纠正重量时比较 泡重和 纠正重量
							if(correctWeight >= entity.getCorrectThrowWeight()){
								// 纠正重量大于抛重重量，按纠正重量算
								double dd = getResult(correctWeight,carrierId);
								entity.setWeight(dd);
								// 有纠正重量，比较纠正重量和运单重量是否相等
							/*	double resultWeight = compareWeight(entity.getTotalWeight(), 
										getResult(entity.getTotalWeight(),carrierId), correctWeight);
								*/
                                double resultWeight = compareTotalWeight(entity.getTotalWeight(),correctWeight,carrierId);	
								entity.setTotalWeight(resultWeight);
							}else if(correctWeight < entity.getCorrectThrowWeight()){
								// 纠正重量小于抛重时，按抛重算
								double dd = getResult(entity.getCorrectThrowWeight(),carrierId);
								entity.setWeight(dd);//计费重量
								entity.setTotalWeight(entity.getCorrectThrowWeight()); //实际重量 eg:5.1
							}
						}else {
							if(!DoubleUtil.isBlank(entity.getTotalWeight())){
								// 没有纠正重量时比较 泡重和 实际重量
								if(entity.getTotalWeight() >= entity.getCorrectThrowWeight()){
									// 运单重量大于抛重时，按运单重量算
									double dd = getResult(entity.getTotalWeight(),carrierId);
									entity.setWeight(dd);
									entity.setTotalWeight(entity.getTotalWeight());
								}else if(entity.getTotalWeight() < entity.getCorrectThrowWeight()){
									// 运单重量小于抛重时，按抛重算
									double dd = getResult(entity.getCorrectThrowWeight(),carrierId);
									entity.setWeight(dd);//计费重量
									entity.setTotalWeight(entity.getCorrectThrowWeight()); //实际重量 eg:5.1
								}
							}else{
								//实际重量为空时，直接取泡重
								double dd = getResult(entity.getCorrectThrowWeight(),carrierId);
								entity.setWeight(dd);
								entity.setTotalWeight(entity.getCorrectThrowWeight());
							}
						}
					}	
				}
			}else{
				RegionVo vo = new RegionVo(ReplaceChar(provinceId), ReplaceChar(cityId),ReplaceChar(districtId));
				RegionVo matchVo = omsAddressService.queryNameByAlias(vo);
				if ((StringUtils.isNotBlank(provinceId) && StringUtils.isBlank(matchVo.getProvince())) ||
						StringUtils.isNotBlank(cityId) && StringUtils.isBlank(matchVo.getCity())) {
					//XxlJobLogger.log("-->"+entity.getId()+"收件人地址存在空值  省【{0}】市【{1}】区【{2}】 运单号【{3}】:"+ provinceId,cityId,districtId,entity.getWaybillNo());
				}else{
					if(StringUtils.isNotBlank(entity.getAdjustProvinceId())||
							StringUtils.isNotBlank(entity.getAdjustCityId())||
							StringUtils.isNotBlank(entity.getAdjustDistrictId())){
						entity.setAdjustProvinceId(matchVo.getProvince());
						entity.setAdjustCityId(matchVo.getCity());
						entity.setReceiveProvinceId(matchVo.getProvince());
						entity.setReceiveCityId(matchVo.getCity());
					}else{
						entity.setReceiveProvinceId(matchVo.getProvince());
						entity.setReceiveCityId(matchVo.getCity());
					}
					fee.setToProvinceName(matchVo.getProvince());
					fee.setToCityName(matchVo.getCity());
				}
				
				if(!DoubleUtil.isBlank(entity.getAdjustWeight())){
					entity.setWeight(Math.ceil(entity.getAdjustWeight())); //计费重量 : 6
					entity.setTotalWeight(entity.getAdjustWeight()); //实际重量 eg:5.1
				}else{
					//新增逻辑判断，判断是否有修正重量，用运单号去修正表里去查询
					Map<String,Object> condition=new HashMap<String,Object>();
					condition.put("waybillNo", entity.getWaybillNo());
					BmsMarkingProductsEntity markEntity=bmsProductsWeightRepository.queryMarkVo(condition);
					if(markEntity!=null && markEntity.getCorrectWeight()!=null && !DoubleUtil.isBlank(markEntity.getCorrectWeight().doubleValue())){
						double weight=markEntity.getCorrectWeight().doubleValue();
						entity.setWeight(Math.ceil(weight));//计费重量 : 6
						// 比较重量
						/*double resultWeight = compareWeight(entity.getTotalWeight(), 
								Math.ceil(entity.getTotalWeight()), weight);*/
                        double resultWeight = compareTotalWeight(entity.getTotalWeight(),weight,carrierId);

						entity.setTotalWeight(resultWeight);//实际重量 eg:5.1
					}else{
						entity.setWeight(Math.ceil(entity.getTotalWeight()));//计费重量 : 6
						entity.setTotalWeight(entity.getTotalWeight());//实际重量 eg:5.1
					}
	
				}
			}
			
            //此时费用写入计费重量
            fee.setChargedWeight(entity.getWeight());
            fee.setToProvinceName(provinceId);// 目的省
            fee.setToCityName(cityId);          // 目的市          
            fee.setToDistrictName(districtId);// 目的区
            fee.setCarrierid(entity.getChargeCarrierId());
            fee.setCarrierName(carrierMap.get(entity.getChargeCarrierId()));
            fee.setTotalWeight(entity.getTotalWeight());//实际重量
            fee.setChargedWeight(entity.getWeight());   //从weight中取出计费重量
            fee.setCalculateTime(JAppContext.currentTimestamp());//计算时间
            fee.setCreateTime(entity.getCreateTime());
			return fee;
		
		} catch (Exception e) {
			// TODO: handle exception
			fee.setIsCalculated(CalculateState.Sys_Error.getCode());
			fee.setCalcuMsg("系统异常");
			logger.info("初始化费用失败【{}】",e);	
		}
		return fee;
		
	}

	@Override
	public boolean isNoExe(BizDispatchBillEntity entity,FeesReceiveDispatchEntity fee) {
		//如果初始化的时候已经更新计算状态了，则直接返回
		if(StringUtils.isNotBlank(fee.getIsCalculated())){
			return true;
		}
		
		long start = System.currentTimeMillis();// 系统开始时间
		long current = 0L;// 当前系统时间
		
		String subjectId= dispatchSubjectMap.get(getCarrierId(entity));
		
		Map<String, Object> map = new HashMap<String, Object>();
		//*********************************判断不计算的宅配商*********************************
		//如果业务数据的宅配商 是不计费的宅配商，则费用不计算
        if(deliverNoCalcuMap.containsKey(entity.getDeliverid())){
        	fee.setAmount(0.0d);
			fee.setTotalWeight(getBizTotalWeight(entity));
			fee.setChargedWeight(0d);
			fee.setIsCalculated(CalculateState.No_Exe.getCode());
			fee.setCalcuMsg("该运单配送类型是【"+entity.getDeliverName()+"】,金额置0");
			return true;
        }
	
		//********************************九曳自己的顺丰月结账号才计算费用*********************************
        if(StringUtils.isNotBlank(entity.getMonthFeeCount())){
        	 boolean ismouthCount= false;
     		String feeCountKey=fee.getCarrierid()+"&"+entity.getMonthFeeCount();//获取月结账号
     		
     		if(monthCountList.contains(feeCountKey)){
     			ismouthCount = true;//false-此单参与计算费用
     			//XxlJobLogger.log("-->"+entity.getId()+entity.getRemark());
     		}
     		
     		if(!ismouthCount){
     			fee.setAmount(0.0d);
     			fee.setTotalWeight(getBizTotalWeight(entity));
     			fee.setChargedWeight(0d);
     			fee.setIsCalculated(CalculateState.No_Exe.getCode());
     			fee.setCalcuMsg("该运单不是九曳的月结账号，金额置0");
     			return true;
     		}
        }
       
		//=*******************************判断取消的单子是否继续计算*******************************
		//指定需要计算取消状态单子的商家
		if(StringUtils.isNotBlank(entity.getOrderStatus()) && "CLOSE".equals(entity.getOrderStatus())){
			if(cancelCusList.contains(entity.getCustomerid())){ // 在cancelCusList中存在，不计费
				fee.setCalcuMsg("该运单是不需要计算的商家取消运单，金额置0");
				fee.setAmount(0.0d);
				fee.setTotalWeight(getBizTotalWeight(entity));
				fee.setChargedWeight(0d);
				fee.setIsCalculated(CalculateState.No_Exe.getCode());
				return true;
			}
		}
		current = System.currentTimeMillis();
		//XxlJobLogger.log("-->"+entity.getId()+"不计算判断  耗时【{0}】毫秒 ",(current - start));
		return false;		
	}

	@Override
	public void calcuForBms(BizDispatchBillEntity entity,FeesReceiveDispatchEntity fee) {
	    fee.setContractAttr("1");
		//初始化合同
	    contract=null;
	    //合同校验
		if(contractList.size()<=0){
			fee.setIsCalculated(CalculateState.Contract_Miss.getCode());
			fee.setCalcuMsg("bms合同缺失");
			return;
		}
		//业务时间和合同时间进行匹配
		//合同
		for(CalcuContractVo con:contractList){
		    if(con.getStartDate().getTime()<=entity.getCreateTime().getTime() && entity.getCreateTime().getTime()<=con.getExpireDate().getTime()){
		        contract=con;
		        break;
		    }
		}
		
		if(contract==null){
		    fee.setIsCalculated(CalculateState.Contract_Miss.getCode());
            fee.setCalcuMsg("bms合同缺失");
            return;
		}
		
	    logger.info("合同信息{}",contract.getContractNo());

		
		contractItemMap=contract.getItemMap();
		//根据计费物流商 获取 物流商配送科目 SHUNFENG_DISPATCH    JIUYE_DISPATCH
		String carrierSubjectCode = dispatchSubjectMap.get(fee.getCarrierid());
		if(contractItemMap==null || !contractItemMap.containsKey(carrierSubjectCode)){
			fee.setIsCalculated(CalculateState.No_Dinggou.getCode());
			fee.setCalcuMsg("商家【"+taskVo.getCustomerName()+"】物流商【"+fee.getCarrierName()+"】未订购科目【配送费】的服务项!");
			return;
		}
		
		try{
			BmsQuoteDispatchDetailVo price=new BmsQuoteDispatchDetailVo();
			List<BmsQuoteDispatchDetailVo> priceList=getQuoEntites(entity,carrierSubjectCode);		
			if(priceList == null || priceList.size()==0){ //
				//XxlJobLogger.log("-->"+entity.getId()+"商家【{0}】,仓库【{1}】,省份【{2}】报价未配置",entity.getCustomerid(),entity.getWarehouseName(),entity.getReceiveProvinceId());
				fee.setCalcuMsg(String.format("商家【%s】,仓库【%s】,省份【%s】报价未配置",entity.getCustomerid(),entity.getWarehouseName(),entity.getReceiveProvinceId()));			
				fee.setIsCalculated(CalculateState.Quote_Miss.getCode());
				return;
			}
			
			if(priceList.size()>1){ //			
				//XxlJobLogger.log("-->"+entity.getId()+"商家【{0}】,仓库【{1}】,省份【{2}】报价未配置",entity.getCustomerid(),entity.getWarehouseName(),entity.getReceiveProvinceId());
				fee.setCalcuMsg(String.format("商家【%s】,仓库【%s】,省份【%s】报价存在多条",entity.getCustomerid(),entity.getWarehouseName(),entity.getReceiveProvinceId()));				
				fee.setIsCalculated(CalculateState.Quote_Miss.getCode());
				return;
			}
			
			if(priceList.size()==1){
				price=priceList.get(0);
			}
			
			CalcuReqVo<BmsQuoteDispatchDetailVo> reqVo = new CalcuReqVo<BmsQuoteDispatchDetailVo>();
			reqVo.setBizData(entity);
			
			//XxlJobLogger.log("-->"+entity.getId()+"验证报价耗时：【{0}】毫秒  ",(current - start));
		
			//开始进行计算
			if(!DoubleUtil.isBlank(price.getUnitPrice())){
				fee.setAmount(price.getUnitPrice());
				fee.setParam2(price.getUnitPrice()+"");
			}else{
				fee.setAmount(price.getFirstWeight()<entity.getWeight()?price.getFirstWeightPrice()+price.getContinuedPrice()*((entity.getWeight()-price.getFirstWeight())/price.getContinuedWeight()):price.getFirstWeightPrice());	        
				if(price.getFirstWeight()<entity.getWeight()){
					fee.setParam2(price.getFirstWeightPrice()+"+"+price.getContinuedPrice()+"*(("+((entity.getWeight()+"-"+price.getFirstWeight())+")/"+price.getContinuedWeight())+")");
				}else{
					fee.setParam2(price.getFirstWeightPrice()+"");
				}
			}
			fee.setWeightLimit(price.getWeightLimit());   	  //重量界限
			fee.setUnitPrice(price.getUnitPrice());			  //单价
			fee.setHeadWeight(price.getFirstWeight());    	  //首重
			fee.setHeadPrice(price.getFirstWeightPrice());	  //首重价格
			fee.setContinuedWeight(price.getContinuedWeight()); //续重
			fee.setContinuedPrice(price.getContinuedPrice());   //续重价格	
			fee.setPriceId(price.getId()+"");
			fee.setBizType(entity.getExtattr1());//判断是否是遗漏数据
			fee.setServiceTypeCode(StringUtils.isEmpty(entity.getAdjustServiceTypeCode())?entity.getServiceTypeCode():entity.getAdjustServiceTypeCode());
			fee.setIsCalculated(CalculateState.Finish.getCode());
			if(fee.getAmount()>0){
                fee.setCalcuMsg("计算成功");
                logger.info("计算成功，费用【{}】",fee.getAmount());
            }
            else{
                logger.info("计算不成功，费用【{}】",fee.getAmount());
                fee.setCalcuMsg("单价配置为0或者计费数量/重量为0");
            }
		}catch(Exception ex){
			
		}
	}

	@Override
	public void calcuForContract(BizDispatchBillEntity entity,FeesReceiveDispatchEntity fee) {
	    fee.setContractAttr("2");
		ContractQuoteQueryInfoVo queryVo = getCtConditon(entity);
		contractCalcuService.calcuForContract(entity, fee, taskVo, errorMap, queryVo,cbiVo,fee.getFeesNo());
		if("succ".equals(errorMap.get("success").toString())){
            fee.setIsCalculated(CalculateState.Finish.getCode());
			if(fee.getAmount()>0){
				fee.setCalcuMsg("计算成功");
				logger.info("计算成功，费用【{}】",fee.getAmount());
			}
			else{
				logger.info("计算不成功，费用【{}】",fee.getAmount());
				fee.setCalcuMsg("单价配置为0或者计费数量/重量为0");
			}
		}
		else{
			fee.setIsCalculated(errorMap.get("is_calculated").toString());
			fee.setCalcuMsg(errorMap.get("msg").toString());
		}
	}

	@Override
	public ContractQuoteQueryInfoVo getCtConditon(BizDispatchBillEntity entity) {
		ContractQuoteQueryInfoVo queryVo = new ContractQuoteQueryInfoVo();
		queryVo.setCustomerId(entity.getCustomerid());
		queryVo.setBizTypeCode(ContractBizTypeEnum.DISTRIBUTION.getCode());
		queryVo.setCurrentTime(entity.getCreateTime());
		queryVo.setWarehouseCode(entity.getWarehouseCode());
		queryVo.setCarrierId(entity.getCarrierId());
		queryVo.setSubjectCode("de_delivery_amount");
		queryVo.setCarrierServiceType(StringUtils.isBlank(entity.getAdjustServiceTypeCode())?entity.getServiceTypeCode():entity.getAdjustServiceTypeCode());
		return queryVo;
	}

	@Override
	public void updateBatch(List<BizDispatchBillEntity> bizList,List<FeesReceiveDispatchEntity> feeList) {
		try {
			StopWatch sw = new StopWatch();
			sw.start();
			bizDispatchBillService.newUpdateBatch(bizList);
			sw.stop();
			logger.info("taskId={} 更新配送业务数据行数【{}】 耗时【{}】",taskVo.getTaskId(),bizList.size(),sw.getLastTaskTimeMillis());
			
			sw.start();
			feesReceiveDispatchService.updateBatch(feeList);
			sw.stop();
			logger.info("taskId={} 更新配送费用数据行数【{}】 耗时【{}】",taskVo.getTaskId(),feeList.size(),sw.getLastTaskTimeMillis());
			
		} catch (Exception e) {
			logger.info("-->批量保存异常"+e.getMessage());
		}
	}
	
	
	
	/**
	 * 获取计算的物流商id
	 * @param carrierId
	 * @return
	 */
	 private String getCarrierId(BizDispatchBillEntity entity){
		 	String carrierId="";
			//物流商的判断
			//1.有调整物流商时直接按调整物流商计算
			//2.无调整物流商时判断是否是指定的商家  获取 此时的实际物流商是否为顺丰，如果是顺丰则需去物流商调整查询该运单，判断其修改的原因
			//根据责任方判断计费物流商:商家的责任就按实际物流商顺丰计费;我们的责任就按订单物流商九曳计费
			//不为顺丰的继续按实际物流商计算
		 	if(StringUtils.isBlank(entity.getAdjustCarrierId())){
		 		//调整物流商为空时 判断实际物流商是否是顺丰
		 		if("1500000015".equals(entity.getCarrierId()) && changeCusList.size()>0 && changeCusList.contains(entity.getCustomerid())){
		 			//判断是否是指定的商家
 					//根据运单号和物流商顺丰去物流商修改表里查询
 					Map<String, Object> param=new HashMap<String, Object>();
 					param.put("carrierId", entity.getCarrierId());
 					param.put("waybillNo", entity.getWaybillNo());	
 					param.put("warehouseCode", entity.getWarehouseCode());
 					param.put("customerid", entity.getCustomerid());
 					BizDispatchCarrierChangeEntity changeEntity=bizDispatchBillService.queryCarrierChangeEntity(param);
 					if(changeEntity!=null){
 						//判断其修改的原因
 						if("customer_reason".equals(changeEntity.getUpdateReasonTypeCode())){
 							//商家原因 按实际物流商顺丰计费
 							carrierId=entity.getCarrierId();
 							changeEntity.setDutyType("商家原因");
 						}else{							
 							//获取商家仓库对应的省配送范围
 							List<String> proviceList=getProviceList(entity);	
 							//在九曳配送范围内的按九曳
 							if(proviceList.contains(entity.getReceiveProvinceId())){
 								//配送范围内的按原始物流商算，且更新为商家原因
 	 							carrierId=changeEntity.getOldCarrierId();
 	 							changeEntity.setDutyType("我司原因");
 							}else{
 								//不在九曳范围内的按顺丰
 								carrierId=entity.getCarrierId();					
 								//第一次为空时保存原修改原因，后面不修改
 								if(StringUtils.isBlank(changeEntity.getOldUpdateReason())){
 	 								changeEntity.setOldUpdateReason(changeEntity.getUpdateReasonTypeCode()+","+changeEntity.getUpdateReasonType()+","+changeEntity.getUpdateReasonDetailCode()+","+changeEntity.getUpdateReasonDetail());
 								}
 								//修改原因
 	 							changeEntity.setDutyType("商家原因");
 	 							changeEntity.setUpdateReasonTypeCode("customer_reason");
 	 							changeEntity.setUpdateReasonType("商家原因");
 	 							changeEntity.setUpdateReasonDetailCode("order_out_of_range");
 	 							changeEntity.setUpdateReasonDetail("订单地址超配送范围"); 							
 							}							
 						}	
 						//更新责任原因至物流商修改表
 						bizDispatchBillService.updateCarrierChange(changeEntity);	
 						//修改原始物流商
 						entity.setOriginCarrierId("1500000016");
 						entity.setOriginCarrierName("JY");
 					}else{
 						carrierId=entity.getCarrierId();
 					}	 		
		 		}else{
		 			carrierId=entity.getCarrierId();
		 		}
		 		
			}else{
				carrierId=entity.getAdjustCarrierId();
			}
		 	entity.setChargeCarrierId(carrierId);
		 	return entity.getChargeCarrierId();
		}
	 
	 private List<String> getProviceList(BizDispatchBillEntity entity){
		 
		 	List<String> reList=new ArrayList<String>();	 	

		    Map<String, Object> map= new HashMap<String, Object>();
			map.put("groupCode", "bms_jy_area");
			map.put("bizType", "group_customer");
			BmsGroupVo bmsGroup=bmsGroupService.queryOne(map);
			if(bmsGroup!=null){			
				List<BmsGroupCustomerVo> list;
				try {
					list = bmsGroupCustomerService.queryAllByGroupId(bmsGroup.getId());
					if(list.size()>0){
						BmsGroupCustomerVo vo=list.get(0);
						if(vo!=null){
							//查询九曳配送范围
							map.put("customerId", vo.getCustomerid());
							map.put("subjectId", "JIUYE_DISPATCH");
							map.put("wareHouseId", entity.getWarehouseCode());
							reList =iPriceDispatchDao.queryJiuYeArea(map);
						}
					}			
				} catch (Exception e) {
					e.printStackTrace();
				}
				
		    }	
			return reList;
	 }
	 
	 /**
		 * 根据物流商判断进位规则
		 * 1.除顺丰外 1.1kg或者1.6kg，重量进位为2
		 * 2.顺丰运费计算规则（超重1.4kg时 重量进位为1.5, ;超重1.6kg时 重量进位为*2计算）
		 * @param weight
		 * @return
		 */
		public double getResult(double weight,String carrierId){
			double a=weight;//原重
			double c=0.0;//现重
			if("1500000015".equals(carrierId)){
				//顺丰				
				int b=(int)a;
				double min=a-b;
				if(0<min && min <0.5){
					c=b+0.5;
				}
				if(0.5<min && min<1){
					c=b+1;
				}
				if(min==0 || min==0.5){
					c=a;
				}
			}else{
				c=Math.ceil(a);//除顺丰外
			}
			return c;
		}
		
		
	      /**
         * 若 运单重量进位 = 纠正重量进位，则返回 运单重量
         * 若 运单重量进位 != 纠正重量进位，则返回纠正重量
         * @param waybillWeight 运单重量
         * @param corrWeight    纠正重量
         * @return
         */
       private double compareTotalWeight(double waybillWeight, double corrWeight,String carrierId){            
           if (DoubleUtil.isBlank(waybillWeight)) {
               // 如果运单重量没有，当做0处理
               waybillWeight = 0.0;
           }          
            //进位后的运单重量
            double newWaybillWeight=getResult(waybillWeight, carrierId);
            //进位后的纠正重量
            double newCorrectWeight=getResult(corrWeight, carrierId);
            
            return newWaybillWeight == newCorrectWeight ? waybillWeight : corrWeight;
        }
		
		
		/**
		 * 若 进位重量 = 纠正重量，则返回 运单重量
		 * 若 进位重量 != 纠正重量，则返回纠正重量
		 * @param waybillWeight	运单重量
		 * @param carryWeight	进位重量
		 * @param corrWeight	纠正重量
		 * @return
		 */
	/*	private double compareWeight(double waybillWeight, double carryWeight, double corrWeight){
			if (DoubleUtil.isBlank(waybillWeight)) {
				// 如果运单重量没有，当做0处理
				waybillWeight = 0.0;
			}
			return carryWeight == corrWeight ? waybillWeight : corrWeight;
		}*/
		
		public double getNewThrowWeight(BizDispatchBillEntity entity){
			double throwWeight=0d;
			try{
				Map<String,Object> condition=new HashMap<String,Object>();
				condition.put("waybillNo", entity.getWaybillNo());
				//获取耗材明细表里的最高体积
				Double maxVolumn=bizOutstockPackmaterialRepository.getMaxVolumByMap(condition);
				if(!DoubleUtil.isBlank(maxVolumn)){
				    
				    //判断是航空还是陆运
				    if(StringUtils.isBlank(entity.getTransportType())){
	                    throwWeight=(double)maxVolumn/6000;
				    }else{
				      //判断是航空还是陆运
	                    if("BY-LAND".equals(entity.getTransportType())){
	                        throwWeight=(double)maxVolumn/12000;//陆运
	                    }else{
	                        throwWeight=(double)maxVolumn/6000;//空运
	                    }
				    }
					throwWeight=(double)Math.round(throwWeight*100)/100;
				}			
			}catch(Exception ex){ 
				logger.error("-->"+entity.getId()+"获取泡重失败:{1}",ex.getMessage());
			}
			return throwWeight;
		}

		private String ReplaceChar(String str){
			if(StringUtils.isNotBlank(str)){
				String[] arr={" ","\\","/",",",".","-"};
				for(String a:arr){
					str=str.replace(a, "");
				}
			}
			return str;
		}
		
		public double getBizTotalWeight(BizDispatchBillEntity entity){	
	 		
	 		double totalWeight=0;
	 		
			if(!DoubleUtil.isBlank(entity.getAdjustWeight())){
				totalWeight=entity.getAdjustWeight(); //实际重量 eg:5.1
			}
			else{
				//新增逻辑判断，判断是否有修正重量，用运单号去修正表里去查询
				Map<String,Object> condition=new HashMap<String,Object>();
				condition.put("waybillNo", entity.getWaybillNo());
				BmsMarkingProductsEntity markEntity=bmsProductsWeightRepository.queryMarkVo(condition);
				if(markEntity!=null && markEntity.getCorrectWeight()!=null && !DoubleUtil.isBlank(markEntity.getCorrectWeight().doubleValue())){
					double weight=markEntity.getCorrectWeight().doubleValue();
					// 比较重量
				/*	double resultWeight = compareWeight(entity.getTotalWeight(), 
							getResult(entity.getTotalWeight(),entity.getChargeCarrierId()), weight);
					*/
                    double resultWeight = compareTotalWeight(entity.getTotalWeight(),weight,entity.getChargeCarrierId());

					totalWeight=resultWeight;//实际重量 eg:5.1
				}else{
					totalWeight=entity.getTotalWeight();//实际重量 eg:5.1
				}

			}
			
			return totalWeight;
	 	}
		
		/**
		 * 获取报价
		 * @param entity
		 * @param subjectId
		 * @return
		 */
		private List<BmsQuoteDispatchDetailVo> getQuoEntites(BizDispatchBillEntity entity,String subjectId){
			//查询报价
			List<BmsQuoteDispatchDetailVo> list=queryPriceByCustomer(entity,subjectId);

			//如果有多条,走筛选规则
			if(list.size()>0){
				//1.走物流产品类型筛选  2.走温度筛选 3.走地址筛选
				//XxlJobLogger.log("-->"+entity.getId()+"走筛选 筛选前报价条数【{0}】",list.size());
				list=handNewBizDispatch(list, entity);
				//XxlJobLogger.log("-->"+entity.getId()+"筛选后报价条数【{0}】",list.size());
				if(null==list||list.size()==0){
					return list;
				}
				for (BmsQuoteDispatchDetailVo bmsQuoteDispatchDetailVo : list) {
					//XxlJobLogger.log("-->"+entity.getId()+"筛选后报价明细【{0}】",JSONObject.fromObject(bmsQuoteDispatchDetailVo));
				}		
				return list;
			}	
			return list;
		}
		
		/**
		 * 顺丰以外物流商的报价
		 * @param entity
		 * @return
		 */
		private List<BmsQuoteDispatchDetailVo> queryPriceByCustomer(BizDispatchBillEntity entity,String subjectId) {
			Map<String,Object> map=new HashMap<String,Object>();
			map.put("contractCode",contract.getContractNo());
			map.put("templateCode", contractItemMap.get(subjectId));
			//map.put("subjectId",subjectId);
			map.put("wareHouseId", entity.getWarehouseCode());
			map.put("province", entity.getReceiveProvinceId());
			map.put("weight", entity.getWeight());
			List<BmsQuoteDispatchDetailVo> price = jobPriceContractInfoService.queryAllByTemplateId(map);
			if(price==null || price.size() ==0){
				//XxlJobLogger.log("-->"+entity.getId()+"数据库未查询到报价 查询条件{0}",map);
			}
			else{
				//XxlJobLogger.log("-->"+entity.getId()+"报价条数【{0}】 查询条件{1}",price.size(),map);
			}
			return price;
		}
		
		/**
		 * 根据省市区获取报价
		 * @param priceList
		 * @param bizEntity
		 * @return
		 */
		public List<BmsQuoteDispatchDetailVo> handNewBizDispatch(List<BmsQuoteDispatchDetailVo> priceList,BizDispatchBillEntity bizEntity){
			
			List<BmsQuoteDispatchDetailVo> list=new ArrayList<BmsQuoteDispatchDetailVo>();
			
			//获取此时业务数据的市区
			String bizCity=bizEntity.getReceiveCityId();
			String bizArea=bizEntity.getReceiveDistrictId();
			
			//物流产品类型
			String bizServiceCode=StringUtils.isNotBlank(bizEntity.getAdjustServiceTypeCode())?bizEntity.getAdjustServiceTypeCode():bizEntity.getServiceTypeCode();
			bizServiceCode=StringUtils.isNotBlank(bizServiceCode)?bizServiceCode:"";
			
			//温度
			String bizTemperatureCode = StringUtil.isEmpty(bizEntity.getTemperatureTypeCode())?"":bizEntity.getTemperatureTypeCode();
			
			Map<Long,BmsQuoteDispatchDetailVo> map=new HashMap<>();
			
			Map<Integer,String> newPrice=new HashMap<Integer,String>();
			
			//报价形式
			//匹配上          1
			//空值             2
			//匹配不上      3
			for(BmsQuoteDispatchDetailVo mainDispatchEntity : priceList){				
				map.put(mainDispatchEntity.getId(), mainDispatchEntity);
				String biaoshi="";
				
				//物流产品类型
				String dispatchServiceCode=mainDispatchEntity.getServiceTypeCode();
				//温度类型
				String dispatchTemetureCode=mainDispatchEntity.getTemperatureTypeCode();
				//获取报价此时的市ID
				String dispatchCityId=mainDispatchEntity.getCityId();
				//获取报价此时的区ID
				String dispatchAreaId=mainDispatchEntity.getAreaId();
						
				//判断物流产品类型
				if(StringUtils.isNotBlank(dispatchServiceCode)){
					if(dispatchServiceCode.equals(bizServiceCode)){
						biaoshi+="1";
					}else{
						biaoshi+="3";
					}
				}else{
					biaoshi+="2";
				}
				
				//判断区
				if(StringUtils.isNotBlank(dispatchAreaId)){
					if(dispatchAreaId.equals(bizArea)){
						biaoshi+="1";
					}else{
						biaoshi+="3";
					}
				}else{
					biaoshi+="2";
				}
				
				
				//判断市
				if(StringUtils.isNotBlank(dispatchCityId)){
					if(dispatchCityId.equals(bizCity)){
						biaoshi+="1";
					}else{
						biaoshi+="3";
					}
				}else{
					biaoshi+="2";
				}
				
				//判断温度
				if(StringUtils.isNotBlank(dispatchTemetureCode)){
					if(dispatchTemetureCode.equals(bizTemperatureCode)){
						biaoshi+="1";
					}else{
						biaoshi+="3"; 
					}
				}else{
					biaoshi+="2";
				}
				
				if(!biaoshi.contains("3")){
					int b=Integer.parseInt(biaoshi);
					if(newPrice.containsKey(b)){
						newPrice.put(b, newPrice.get(b)+","+mainDispatchEntity.getId());
					}else{
						newPrice.put(b, mainDispatchEntity.getId().toString());
					}	
				}
			}
			
			
			Integer minValue=new Integer(0);
			for(Integer num:newPrice.keySet()){
				if(minValue==0 || num<minValue){
					minValue=num;
				}	
			}
			
			BmsQuoteDispatchDetailVo vo=new BmsQuoteDispatchDetailVo();
			
			if(!(minValue+"").contains("3")){
				String result=newPrice.get(minValue);
				if(StringUtils.isEmpty(result)){
					return null;
				}
				if(result.contains(",")){
					String[] array=result.split(",");
					for (int a = 0; a < array.length; a++) {		
						Long value=Long.valueOf(array[a]);
						vo=map.get(value);
						list.add(vo);				
				     }		
				}else{
					Long value=Long.valueOf(result);
					vo=map.get(value);
					list.add(vo);			
				}
			}
			
			return list;	
		}
		
		private void updateTask(BmsCalcuTaskVo taskVo,int calcuCount){
			try {
				BmsFeesQtyVo feesQtyVo = bmsCalcuService.queryFeesQtyForDis(taskVo.getCustomerId(), taskVo.getSubjectCode(), taskVo.getCreMonth());
				taskVo.setUncalcuCount(feesQtyVo.getUncalcuCount()==null?0:feesQtyVo.getUncalcuCount());//本次待计算的费用数
				taskVo.setCalcuCount(calcuCount);
				taskVo.setBeginCount(feesQtyVo.getBeginCount()==null?0:feesQtyVo.getBeginCount());//未计算费用总数
				taskVo.setFinishCount(feesQtyVo.getFinishCount()==null?0:feesQtyVo.getFinishCount());//计算成功总数
				taskVo.setSysErrorCount(feesQtyVo.getSysErrorCount()==null?0:feesQtyVo.getSysErrorCount());//系统错误用总数
				taskVo.setContractMissCount(feesQtyVo.getContractMissCount()==null?0:feesQtyVo.getContractMissCount());//合同缺失总数
				taskVo.setQuoteMissCount(feesQtyVo.getQuoteMissCount()==null?0:feesQtyVo.getQuoteMissCount());//报价缺失总数
				taskVo.setNoExeCount(feesQtyVo.getNoExeCount()==null?0:feesQtyVo.getNoExeCount());//不计算费用总数
				taskVo.setCalcuStatus(feesQtyVo.getCalcuStatus());
				bmsCalcuTaskService.update(taskVo);
			} catch (Exception e) {
				logger.error("更新任务统计信息异常",e);
			}
		}
}
