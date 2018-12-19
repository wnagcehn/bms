package com.jiuyescm.bms.jobhandler;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyescm.bms.base.calcu.vo.CalcuReqVo;
import com.jiuyescm.bms.base.calcu.vo.CalcuResultVo;
import com.jiuyescm.bms.base.dictionary.entity.SystemCodeEntity;
import com.jiuyescm.bms.biz.transport.entity.BizGanxianWayBillEntity;
import com.jiuyescm.bms.drools.IFeesCalcuService;
import com.jiuyescm.bms.chargerule.receiverule.entity.BillRuleReceiveEntity;
import com.jiuyescm.bms.common.enumtype.CalculateState;
import com.jiuyescm.bms.common.enumtype.TemplateTypeEnum;
import com.jiuyescm.bms.general.entity.FeesReceiveDispatchEntity;
import com.jiuyescm.bms.general.entity.FeesReceiveTransportEntity;
import com.jiuyescm.bms.general.service.IFeesReceiveTransportService;
import com.jiuyescm.bms.general.service.IPriceContractInfoService;
import com.jiuyescm.bms.general.service.ISystemCodeService;
import com.jiuyescm.bms.general.service.SequenceService;
import com.jiuyescm.bms.quotation.contract.entity.PriceContractInfoEntity;
import com.jiuyescm.bms.quotation.contract.entity.PriceContractItemEntity;
import com.jiuyescm.bms.quotation.contract.repository.imp.IPriceContractItemRepository;
import com.jiuyescm.bms.quotation.transport.entity.PriceTransportLineEntity;
import com.jiuyescm.bms.quotation.transport.entity.PriceTransportLineRangeEntity;
import com.jiuyescm.bms.quotation.transport.repository.IPriceTransportLineRangeRepository;
import com.jiuyescm.bms.quotation.transport.repository.IPriceTransportLineRepository;
import com.jiuyescm.bms.receivable.transport.service.IBizGanxianWayBillService;
import com.jiuyescm.bms.rule.receiveRule.repository.IReceiveRuleRepository;
import com.jiuyescm.cfm.common.JAppContext;
import com.jiuyescm.mdm.customer.api.IAddressService;
import com.jiuyescm.mdm.customer.vo.RegionVo;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.JobHander;
import com.xxl.job.core.log.XxlJobLogger;

@JobHander(value="ganXianWayBillCalcJob")
@Service
public class GanXianWayBillCalcJob extends CommonCalcJob<BizGanxianWayBillEntity,FeesReceiveTransportEntity>{

	@Autowired private IBizGanxianWayBillService bizGanxianWayBillService;
	@Autowired private IPriceContractInfoService jobPriceContractInfoService;
	@Autowired private SequenceService sequenceService;
	@Autowired private IFeesReceiveTransportService feesReceiveTransportService;
	@Autowired private ISystemCodeService systemCodeService;
	@Autowired private IAddressService omsAddressService;
	@Autowired private IPriceContractItemRepository priceContractItemRepository;
	@Autowired private IPriceTransportLineRepository priceTransportLineService;
	@Autowired private IPriceTransportLineRangeRepository priceTransportLineRangeService;
	@Autowired private IReceiveRuleRepository receiveRuleRepository;
	@Autowired private IFeesCalcuService feesCalcuService;
	
	private String BizTypeCode = "TRANSPORT"; //运输费费编码
	private String quoType = "C";//默认使用常规报价
	
	List<SystemCodeEntity> scList=null;
	List<SystemCodeEntity> carModelList=null;
	
	Map<String,PriceContractInfoEntity> mapContact=null;
	@Override
	public ReturnT<String> execute(String... params) throws Exception {
		XxlJobLogger.log("ganXianWayBillCalcJob start.");
		return super.CalcJob(params);
	}
	
	@Override
	protected List<BizGanxianWayBillEntity> queryBillList(Map<String, Object> map) {
		return bizGanxianWayBillService.getGanxianWayBillList(map);
	}

	@Override
	protected void initConf(List<BizGanxianWayBillEntity> billList)
			throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("typeCode", "TRANSPORT_PRODUCT_TYPE");
		scList = systemCodeService.querySysCodes(map);
		if(scList == null || scList.size()==0){
			XxlJobLogger.log("未查询到任何物流商");
			throw new Exception("未查询到任何物流商");
		}	
		//*********************************查询所有车型*********************************	
		map.clear();
		map.put("typeCode", "MOTORCYCLE_TYPE");
		carModelList = systemCodeService.querySysCodes(map);	
		if(carModelList==null || carModelList.size()<1){
			XxlJobLogger.log("未查询到任何车型");
			throw new Exception("未查询到任何车型");
		}
		
		mapContact=new HashMap<String,PriceContractInfoEntity>();
		
	}

	@Override
	protected void calcuService(BizGanxianWayBillEntity entity,
			List<FeesReceiveTransportEntity> feesList) {
		String customerId=entity.getCustomerId();
		String subjectId=getSubjectId(entity.getBizTypeCode());
		FeesReceiveTransportEntity transportFeeEntity=initfeeEntity(entity);
		entity.setCalculateTime(JAppContext.currentTimestamp());
		transportFeeEntity.setCalculateTime(entity.getCalculateTime());
		long start = System.currentTimeMillis();// 系统开始时间
		long current = 0l;// 当前系统时间
		try{
			//验证报价 验证规则
			//查询规则
			Map<String,Object> map=new HashMap<>();
			map.put("customerid", customerId);
			map.put("subjectId",subjectId);
			BillRuleReceiveEntity ruleEntity=receiveRuleRepository.queryByCustomerId(map);
			if(ruleEntity == null){ //常规报价不存在,找标准报价
				XxlJobLogger.log("规则未配置");
				entity.setIsCalculated(CalculateState.Quote_Miss.getCode());
				transportFeeEntity.setIsCalculated(CalculateState.Quote_Miss.getCode());
				entity.setRemark("规则未配置");
				feesList.add(transportFeeEntity);
				return;
			}
			current= System.currentTimeMillis();// 系统开始时间
			XxlJobLogger.log("验证规则   耗时【{0}】毫秒 ",(current - start));
		    start = System.currentTimeMillis();// 系统开始时间
		    //查询报价
		    transportFeeEntity.setRuleno(ruleEntity.getQuotationNo());
		  //查询报价
		    PriceContractInfoEntity contractEntity=mapContact.get(customerId);
			Map<String, Object> lineParam = handTransportBizTypeParam(entity);
			lineParam.put("contractCode", contractEntity.getContractCode());
			lineParam.put("templateTypeCode", "TRANSPORT_FEE");// 运输
			List<PriceTransportLineEntity> passLineList=priceTransportLineService.queryTransportQuos(lineParam);
			if(passLineList == null || passLineList.size() == 0){ //常规报价不存在,找标准报价
				String mes=getPriceMes(entity);
				XxlJobLogger.log("{0}报价未配置",mes);
				entity.setIsCalculated(CalculateState.Quote_Miss.getCode());
				transportFeeEntity.setIsCalculated(CalculateState.Quote_Miss.getCode());
				entity.setRemark(String.format("{0}报价未配置", mes));
				feesList.add(transportFeeEntity);
				return;	
			}
			//过滤路线
			handTransportBizLine(passLineList,entity);
			current= System.currentTimeMillis();// 系统开始时间
			XxlJobLogger.log("验证报价   耗时【{0}】毫秒 ",(current - start));
		    start = System.currentTimeMillis();// 系统开始时间
			CalcuReqVo<PriceTransportLineEntity> reqVo = new CalcuReqVo<PriceTransportLineEntity>();
			reqVo.setQuoEntites(passLineList);
			reqVo.setRuleNo(ruleEntity.getQuotationNo());
			reqVo.setRuleStr(ruleEntity.getRule());
			//传入业务数据
			reqVo.setBizData(entity);
			//调用计算方法
			//CalcuResultVo resultVo = feesCalcuService.FeesCalcuService(reqVo);
			CalcuResultVo resultVo = new CalcuResultVo();
			current = System.currentTimeMillis();
			if("succ".equals(resultVo.getSuccess())){
				XxlJobLogger.log("调用规则引擎   耗时【{0}】毫秒  费用【{1}】 ",(current - start),resultVo.getPrice());	
				transportFeeEntity.setTotleprice(resultVo.getPrice().doubleValue()); //价格
				transportFeeEntity.setUnitprice(resultVo.getUnitPrice());
				transportFeeEntity.setExtarr1(TemplateTypeEnum.COMMON.getCode());
				transportFeeEntity.setExtarr2(resultVo.getMethod());//计算方式
				entity.setIsCalculated(CalculateState.Finish.getCode());
				transportFeeEntity.setIsCalculated(CalculateState.Finish.getCode());
				entity.setRemark("计算成功");
				feesList.add(transportFeeEntity);
			}
			else{
				XxlJobLogger.log("调用规则引擎   耗时【{0}】毫秒 ",(current - start));	
				XxlJobLogger.log("费用计算失败--"+resultVo.getMsg());
				transportFeeEntity.setTotleprice(0.0d);
				transportFeeEntity.setExtarr1(TemplateTypeEnum.COMMON.getCode());
				entity.setIsCalculated(CalculateState.Quote_Miss.getCode());
				transportFeeEntity.setIsCalculated(CalculateState.Quote_Miss.getCode());
				entity.setRemark("运输费用计算失败:"+resultVo.getMsg());
				feesList.add(transportFeeEntity);
			}
		}catch(Exception ex){
			XxlJobLogger.log("运输费用计算失败--{0}",ex.getMessage());
			entity.setIsCalculated(CalculateState.Sys_Error.getCode());
			transportFeeEntity.setIsCalculated(CalculateState.Sys_Error.getCode());
			entity.setRemark("运输计算异常:"+ex.getMessage());
			feesList.add(transportFeeEntity);
		}
		
	}


	/**
	 * 根据报价处理业务数据，使其支持模糊匹配报价
	 * @param passLineList
	 * @param bizData
	 */
	public void handTransportBizLine(List<PriceTransportLineEntity> passLineList,BizGanxianWayBillEntity bizData){
		if(passLineList != null && passLineList.size()>0){
			if ("TC".equals(bizData.getBizTypeCode().trim()) || "CJ".equals(bizData.getBizTypeCode().trim())) {
				boolean fromArea = false;
				boolean toArea = false;
				String sendArea = bizData.getSendDistrictId();
				String receiveArea = bizData.getReceiverDistrictId();
				//过滤同城和城际的区
				for (PriceTransportLineEntity priceEntity : passLineList) {
					String priceSendArea = priceEntity.getFromDistrictId();
					String priceReceiverArea = priceEntity.getToDistrictId();
					
					if (StringUtils.isNotEmpty(sendArea) && StringUtils.isNotEmpty(receiveArea)) {
						//业务数据始发区、目的区都不为空
						if (StringUtils.isNotEmpty(priceSendArea) && StringUtils.isNotEmpty(priceReceiverArea)) {
							//报价始发区、目的区都不为空
							if (sendArea.equals(priceSendArea) && receiveArea.equals(priceReceiverArea)) {
								fromArea = true;
								toArea = true;
								break;
							}
						}else if (StringUtils.isNotEmpty(priceSendArea) && StringUtils.isEmpty(priceReceiverArea)) {
							//报价始发区不为空、目的区为空
							toArea = false;
							if (sendArea.equals(priceSendArea)) {
								fromArea = true;
								break;
							}
						}else if (StringUtils.isEmpty(priceSendArea) && StringUtils.isNotEmpty(priceReceiverArea)) {
							//报价始发区为空、目的区不为空
							fromArea = false;
							if (receiveArea.equals(priceReceiverArea)) {
								toArea = true;
								break;
							}
						}
					}else if (StringUtils.isEmpty(sendArea) && StringUtils.isNotEmpty(receiveArea)) {
						//业务数据始发区为空，目的区不为空
						if (StringUtils.isNotEmpty(priceReceiverArea) && receiveArea.equals(priceReceiverArea)) {
							//目的区不为空
							toArea = true;
							break;
						}
					}else if (StringUtils.isNotEmpty(sendArea) && StringUtils.isEmpty(receiveArea)) {
						//业务数据始发区不为空，目的区为空
						if (StringUtils.isNotEmpty(priceSendArea) && sendArea.equals(priceSendArea)) {
							//始发区不为空
							fromArea = true;
							break;
						}
					}
				}
				
				if(fromArea == false){
					bizData.setSendDistrictId("");
				}
				if(toArea == false){
					bizData.setReceiverDistrictId("");
				}
				
				for (PriceTransportLineEntity passLine : passLineList) {
					// 找到运输路线对应的梯度报价
					Map<String, Object> rangeParam = new HashMap<String,Object>();
					rangeParam.put("lineId", passLine.getId());
					rangeParam.put("delFlag", "0");
					List<PriceTransportLineRangeEntity> rangeList = priceTransportLineRangeService.query(rangeParam);
					if (rangeList != null && rangeList.size() > 0) {
						passLine.setChild(rangeList);
					}
				}
			}
		}
	}
	@Override
	protected void saveBatchData(List<BizGanxianWayBillEntity> billList,List<FeesReceiveTransportEntity> feesList) {
		long start = System.currentTimeMillis();// 系统开始时间
		long current = 0l;// 当前系统时间
		bizGanxianWayBillService.updateBatch(billList);
		current = System.currentTimeMillis();
		XxlJobLogger.log("更新业务数据耗时：【{0}】毫秒  ",(current - start));
		start = System.currentTimeMillis();// 系统开始时间
		for(FeesReceiveTransportEntity feeEntity:feesList){
			feesReceiveTransportService.Insert(feeEntity);
		}
		current = System.currentTimeMillis();
		XxlJobLogger.log("新增费用数据耗时：【{0}】毫秒 ",(current - start));
	}

	private boolean validateRegion(BizGanxianWayBillEntity bizData,FeesReceiveTransportEntity transportFeeEntity){
		if ("TC".equals(bizData.getBizTypeCode()) || "CJ".equals(bizData.getBizTypeCode())) {
			RegionVo fromVo = new RegionVo(bizData.getSendProvinceId(), bizData.getSendCityId(), bizData.getSendDistrictId());
			RegionVo matchFromVo = omsAddressService.queryNameByAlias(fromVo);
			if ((StringUtils.isNotBlank(bizData.getSendProvinceId()) && StringUtils.isBlank(matchFromVo.getProvince())) 
					|| StringUtils.isNotBlank(bizData.getSendCityId()) && StringUtils.isBlank(matchFromVo.getCity()) 
					|| StringUtils.isNotBlank(bizData.getSendDistrictId()) && StringUtils.isBlank(matchFromVo.getDistrict())) {
				XxlJobLogger.log(String.format("【起始】省份:%s,城市:%s,区:%s 没有在地址库中维护!", bizData.getSendProvinceId(),bizData.getSendCityId(),bizData.getSendDistrictId()));
				
				bizData.setIsCalculated(CalculateState.Other.getCode());
				transportFeeEntity.setIsCalculated(CalculateState.Other.getCode());
				bizData.setRemark(String.format("【起始】省份:%s,城市:%s,区:%s 没有在地址库中维护!", bizData.getSendProvinceId(),bizData.getSendCityId(),bizData.getSendDistrictId()));
				return false;
			}
			bizData.setSendProvinceId(matchFromVo.getProvince());
			bizData.setSendCityId(matchFromVo.getCity());
			bizData.setSendDistrictId(matchFromVo.getDistrict());
			
			//转换【目的】省市区成标准名称，匹配不到计算失败
			RegionVo destVo = new RegionVo(bizData.getReceiverProvinceId(), bizData.getReceiverCityId(), bizData.getReceiverDistrictId());
			RegionVo matchDestVo = omsAddressService.queryNameByAlias(destVo);
			if ((StringUtils.isNotBlank(bizData.getReceiverProvinceId()) && StringUtils.isBlank(matchDestVo.getProvince())) 
					|| StringUtils.isNotBlank(bizData.getReceiverCityId()) && StringUtils.isBlank(matchDestVo.getCity()) 
					|| StringUtils.isNotBlank(bizData.getReceiverDistrictId()) && StringUtils.isBlank(matchDestVo.getDistrict())) {
				XxlJobLogger.log(String.format("【目的】省份:%s,城市:%s,区:%s 没有在地址库中维护!", bizData.getReceiverProvinceId(),bizData.getReceiverCityId(),bizData.getReceiverDistrictId()));
				bizData.setIsCalculated(CalculateState.Other.getCode());
				transportFeeEntity.setIsCalculated(CalculateState.Other.getCode());
				bizData.setRemark(String.format("【目的】省份:%s,城市:%s,区:%s 没有在地址库中维护!", bizData.getReceiverProvinceId(),bizData.getReceiverCityId(),bizData.getReceiverDistrictId()));
				return false;
			}
			bizData.setReceiverProvinceId(matchDestVo.getProvince());
			bizData.setReceiverCityId(matchDestVo.getCity());
			bizData.setReceiverDistrictId(matchDestVo.getDistrict());
			XxlJobLogger.log("地址匹配》》》:" + matchDestVo.getProvince() + matchDestVo.getCity() + matchDestVo.getDistrict());
		}
		return true;
	}
	
	private boolean validateContract(BizGanxianWayBillEntity entity,FeesReceiveTransportEntity transportFeeEntity){
		String customerId=entity.getCustomerId();
		PriceContractInfoEntity contractEntity=null;
		if(mapContact.containsKey(customerId)){
			contractEntity=mapContact.get(customerId);
		}else{
			Map<String,Object> map=new HashMap<String,Object>();
			map.put("customerid",customerId);
			map.put("contractTypeCode", "CUSTOMER_CONTRACT");
		    contractEntity = jobPriceContractInfoService.queryContractByCustomer(map);
		    mapContact.put(customerId, contractEntity);
		}
	
		if(contractEntity == null || StringUtils.isEmpty(contractEntity.getContractCode())){
			XxlJobLogger.log(String.format("运输费计算未查询到合同  订单号【%s】--商家【%s】", entity.getOrderNo(),entity.getCustomerId()));
			entity.setIsCalculated(CalculateState.Contract_Miss.getCode());
			transportFeeEntity.setIsCalculated(CalculateState.Contract_Miss.getCode());
			entity.setRemark(String.format("运输费计算未查询到合同  订单号【%s】--商家【%s】", entity.getOrderNo(),entity.getCustomerId()));
			return false;
		}
		return true;
	}
	@Override
	protected boolean validateData(BizGanxianWayBillEntity entity,List<FeesReceiveTransportEntity> feesList) {
		XxlJobLogger.log("数据主键ID:【{0}】  ",entity.getId());
		long start = System.currentTimeMillis();// 系统开始时间
		long current = 0l;// 当前系统时间
		Timestamp time=JAppContext.currentTimestamp();
		entity.setCalculateTime(time);//更新计算时间
		String customerId=entity.getCustomerId();
		Map<String,Object> map=new HashMap<>();
		
		FeesReceiveTransportEntity transportFeeEntity=initfeeEntity(entity);
		transportFeeEntity.setCalculateTime(time);
		boolean isInsert = StringUtils.isEmpty(transportFeeEntity.getFeesNo())?true:false; //true-新增  false-更新
		if(isInsert){
			String feesNo =sequenceService.getBillNoOne(FeesReceiveDispatchEntity.class.getName(), "PSFY", "0000000000");
			transportFeeEntity.setFeesNo(feesNo);
			entity.setFeesNo(feesNo);
		}
		String subjectId=getSubjectId(entity.getBizTypeCode());
		XxlJobLogger.log("subjectID》》》:" + subjectId);
		if(StringUtils.isEmpty(subjectId)){
			XxlJobLogger.log(String.format("干线单号【%s】执行失败--原因：干线业务类型【%s】未在数据字典中配置", entity.getWaybillNo(),entity.getBizTypeCode()));
			entity.setIsCalculated(CalculateState.Sys_Error.getCode());
			transportFeeEntity.setIsCalculated(CalculateState.Sys_Error.getCode());
			entity.setRemark(String.format("干线单号【%s】执行失败--原因：干线业务类型【%s】未在数据字典中配置", entity.getWaybillNo(),entity.getBizTypeCode()));
			feesList.add(transportFeeEntity);
			return false;
		}
		transportFeeEntity.setSubjectCode(subjectId);
		start = System.currentTimeMillis();// 系统开始时间
		if(!validateRegion(entity,transportFeeEntity)){
			 feesList.add(transportFeeEntity);
			 return false;
		}
		current = System.currentTimeMillis();
		XxlJobLogger.log("地址匹配   耗时【{0}】毫秒 ",(current - start));	
		start = System.currentTimeMillis();// 操作开始时间
		//验证合同
		if(!validateContract(entity,transportFeeEntity)){
			feesList.add(transportFeeEntity);
			return false;
		}
		current = System.currentTimeMillis();
		PriceContractInfoEntity contractEntity=mapContact.get(customerId);
		XxlJobLogger.log("验证合同   耗时【{0}】毫秒  合同编号 【{1}】",(current - start),contractEntity.getContractCode());	
		start = System.currentTimeMillis();// 操作开始时间
		
		//验证是否存在签约服务
		Map<String, Object> contractItemsMap = new HashMap<String, Object>();
		contractItemsMap.put("contractCode", contractEntity.getContractCode());
		contractItemsMap.put("bizTypeCode", BizTypeCode);
		contractItemsMap.put("subjectId", "TRANSPORT_FEE");
		List<PriceContractItemEntity> contractItemEntities = priceContractItemRepository.query(contractItemsMap);
	    if(contractItemEntities == null || contractItemEntities.size()==0){
	    	XxlJobLogger.log(String.format("合同【%s】未签约服务", contractEntity.getContractCode()));
	    	entity.setIsCalculated(CalculateState.Quote_Miss.getCode());
	    	transportFeeEntity.setIsCalculated(CalculateState.Quote_Miss.getCode());
	    	entity.setRemark(String.format("合同【%s】未签约服务", contractEntity.getContractCode()));
	    	feesList.add(transportFeeEntity);
			return false;
	    }
	    current = System.currentTimeMillis();
		XxlJobLogger.log("验证签约服务耗时【{0}】毫秒 ",(current - start));	
		return true;
	}
	
	private String getSubjectId(String bizTypeCode){
		String subjectID = null;
		for (SystemCodeEntity scEntity : scList) {
			if(scEntity.getCode().equals(bizTypeCode)){
				subjectID = scEntity.getCode();
				break;
			}
		}
		return subjectID;
	}
	private String objToString(Object obj){
		if(obj==null){
			return "";
		}else{
			return obj.toString();
		}
	}
	private String getPriceMes(BizGanxianWayBillEntity entity) {
		String bizTypeCode=entity.getBizTypeCode();
		String mes="";
		mes+="业务类型【"+bizTypeCode+"】,";
		switch(bizTypeCode){
		case "TC":
		case "CJ":
			if(StringUtils.isNotEmpty(entity.getSendProvinceId())){
				mes+="始发省市【"+objToString(entity.getSendProvinceId())+"-"+objToString(entity.getSendCityId())+"】,";
			}
			if(StringUtils.isNotEmpty(entity.getReceiverProvinceId())){
				mes+="目的省市【"+objToString(entity.getReceiverProvinceId())+"-"+objToString(entity.getReceiverCityId())+"】,";
			}
			break;
		case "DSZL":
			mes+="始发仓【"+objToString(entity.getWarehouseName())+"】,目的站【"+objToString(entity.getEndStation())+"】";
			break;
		case "HXD":
			mes+="始发站点【"+objToString(entity.getStartStation())+"】,目的省市【"+objToString(entity.getReceiverProvinceId())+"-"+objToString(entity.getReceiverCityId())+"】";
			break;
			default:
				break;
		}
		return mes;
	}
	/**
	 * 处理运输根据业务类型匹配路线报价参数
	 * @param param
	 * @return
	 */
	private Map<String, Object> handTransportBizTypeParam(BizGanxianWayBillEntity bizData){
		Map<String, Object> lineParam = new HashMap<String, Object>();
		//过滤掉不满足的路线
		if(bizData != null && StringUtils.isNotEmpty(bizData.getBizTypeCode())){
			lineParam.put("transportTypeCode", bizData.getBizTypeCode());
		}
		if ("TC".equals(bizData.getBizTypeCode().trim()) || "CJ".equals(bizData.getBizTypeCode().trim())) {
			if(bizData != null && StringUtils.isNotEmpty(bizData.getSendProvinceId())){
				lineParam.put("fromProvinceId", bizData.getSendProvinceId());
			}
			if(bizData != null && StringUtils.isNotEmpty(bizData.getSendCityId())) {
				lineParam.put("fromCityId", bizData.getSendCityId());
			}
			if(bizData != null && StringUtils.isNotEmpty(bizData.getReceiverProvinceId())){
				lineParam.put("toProvinceId", bizData.getReceiverProvinceId());
			}
			if(bizData != null && StringUtils.isNotEmpty(bizData.getReceiverCityId())) {
				lineParam.put("toCityId", bizData.getReceiverCityId());
			}
		}else if ("DSZL".equals(bizData.getBizTypeCode().trim())) {
			if(bizData != null && StringUtils.isNotEmpty(bizData.getWarehouseCode())){
				lineParam.put("fromWarehouseId", bizData.getWarehouseCode());
			}
			if(bizData != null && StringUtils.isNotEmpty(bizData.getEndStation())){
				lineParam.put("endStation", bizData.getEndStation());
			}
		}else if ("HXD".equals(bizData.getBizTypeCode().trim())) {
			if(bizData != null && StringUtils.isNotEmpty(bizData.getStartStation())){
				lineParam.put("startStation", bizData.getStartStation());
			}
			if(bizData != null && StringUtils.isNotEmpty(bizData.getReceiverProvinceId())){
				lineParam.put("toProvinceId", bizData.getReceiverProvinceId());
			}
			if(bizData != null && StringUtils.isNotEmpty(bizData.getReceiverCityId())) {
				lineParam.put("toCityId", bizData.getReceiverCityId());
			}
		}
		return lineParam;
	}
	private FeesReceiveTransportEntity initfeeEntity(BizGanxianWayBillEntity bizData){	
		FeesReceiveTransportEntity transportFeeEntity = new FeesReceiveTransportEntity();
		//费用表的创建时间应为业务表的创建时间
		transportFeeEntity.setCreperson("system"); // 创建人ID
		transportFeeEntity.setCrepersonname("system"); // 创建人姓名
		transportFeeEntity.setCretime(bizData.getSendTime());
		transportFeeEntity.setOperationtime(bizData.getCreateTime());	// 操作时间
		transportFeeEntity.setWarehouseCode(bizData.getWarehouseCode());		// 仓库编码
		//transportFeeEntity.setWarehouseName(bizData.GETW);
		transportFeeEntity.setCustomerid(bizData.getCustomerId());				// 商家Id
		transportFeeEntity.setCustomerName(bizData.getCustomerName()); 			// 商家名称
		transportFeeEntity.setCostType("TRANSPORT_FEE");//运输类型(不是增值类型ADD_FEE)	// 费用类型
		//transportFeeEntity.setSubjectCode(calcuVo.getSubjectId());				// 费用科目
		transportFeeEntity.setLinename(null);									// 线路名称
		transportFeeEntity.setOrderno(bizData.getOrderNo());					// 订单号
		transportFeeEntity.setWaybillNo(bizData.getWaybillNo());				// 运单号
		transportFeeEntity.setOriginatingcity(bizData.getSendCityId());			// 始发市
		transportFeeEntity.setOriginatingdistrict(bizData.getSendDistrictId());	// 始发区
		transportFeeEntity.setTargetwarehouse(bizData.getWarehouseCode());		// 目的仓
		transportFeeEntity.setTargetcity(bizData.getReceiverCityId()); 			// 目的市
		transportFeeEntity.setTargetdistrict(bizData.getReceiverDistrictId()); 	// 目的区
		transportFeeEntity.setTemperaturetype(bizData.getTemperatureTypeCode());// 温度类型
		transportFeeEntity.setCategory(null);									// 品类
		transportFeeEntity.setWeight(add(bizData.getTotalWeight(), bizData.getAdjustWeight()));// 重量
		transportFeeEntity.setVolume(bizData.getTotalVolume()); 				// 体积
		transportFeeEntity.setKilometers(null); 								// 公里数
		transportFeeEntity.setSpendtime(null); 									// 花费时间
		transportFeeEntity.setCarmodel(bizData.getCarModel());					// 车型
		transportFeeEntity.setFeesNo(bizData.getFeesNo());                      //费用编号
		if(bizData != null && StringUtils.isNotBlank(bizData.getIsLight())){
			// 是否轻货  1是;0不是
			if(StringUtils.equalsIgnoreCase(bizData.getIsLight(), "Y")){
				transportFeeEntity.setIslight(Long.valueOf("1"));
			}else{
				transportFeeEntity.setIslight(Long.valueOf("0"));
			}
		}
		transportFeeEntity.setState("0");
		transportFeeEntity.setExtarr1(TemplateTypeEnum.COMMON.getCode());
		return transportFeeEntity;
		
	}
	private double add(double param1, double param2) {  
        BigDecimal num1 = new BigDecimal(Double.toString(param1));  
        BigDecimal num2 = new BigDecimal(Double.toString(param2));  
        return num1.add(num2).doubleValue();  
	}

	@Override
	protected void calcuStandardService(List<BizGanxianWayBillEntity> billList) {
		// TODO Auto-generated method stub
		
	}
}
