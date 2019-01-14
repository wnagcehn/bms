package com.jiuyescm.bms.jobhandler;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyescm.bms.base.calcu.vo.CalcuReqVo;
import com.jiuyescm.bms.base.calcu.vo.CalcuResultVo;
import com.jiuyescm.bms.base.dictionary.entity.SystemCodeEntity;
import com.jiuyescm.bms.biz.dispatch.entity.BizDispatchBillPayEntity;
import com.jiuyescm.bms.drools.IFeesCalcuService;
import com.jiuyescm.bms.chargerule.payrule.entity.BillRulePayEntity;
import com.jiuyescm.bms.common.enumtype.CalculateState;
import com.jiuyescm.bms.common.enumtype.TemplateTypeEnum;
import com.jiuyescm.bms.general.entity.FeesPayDispatchEntity;
import com.jiuyescm.bms.general.service.IFeesPayDispatchService;
import com.jiuyescm.bms.general.service.IPriceContractInfoService;
import com.jiuyescm.bms.general.service.ISystemCodeService;
import com.jiuyescm.bms.general.service.SequenceService;
import com.jiuyescm.bms.payable.dispatch.service.IBizDispatchBillPayService;
import com.jiuyescm.bms.quotation.contract.entity.PriceContractInfoEntity;
import com.jiuyescm.bms.quotation.out.dispatch.entity.vo.PriceOutMainDispatchEntity;
import com.jiuyescm.bms.quotation.out.dispatch.repository.IPriceOutDispatchDao;
import com.jiuyescm.bms.rule.payRule.repository.IPayRuleRepository;
import com.jiuyescm.cfm.common.JAppContext;
import com.jiuyescm.common.utils.DoubleUtil;
import com.jiuyescm.mdm.customer.api.IAddressService;
import com.jiuyescm.mdm.customer.vo.RegionVo;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.JobHander;
import com.xxl.job.core.log.XxlJobLogger;

@JobHander(value="dispatchBillPayCalcJob")
@Service
public class DispatchBillPayCalcJob extends CommonCalcJob<BizDispatchBillPayEntity,FeesPayDispatchEntity>{

	@Autowired private IBizDispatchBillPayService bizDispatchBillPayService;
	@Autowired private SequenceService sequenceService;
	@Autowired private IFeesPayDispatchService feesPayDispatchService;
	@Autowired private IPriceContractInfoService jobPriceContractInfoService;
	@Autowired private ISystemCodeService systemCodeService;
	@Autowired private IAddressService omsAddressService;
	@Autowired private IFeesCalcuService feesCalcuService;
	@Autowired private IPriceOutDispatchDao outPriceDispatchDao;
	@Autowired private IPayRuleRepository payRuleRepositoryImp;
	
	private String quoType = "C";//默认使用常规报价
	List<SystemCodeEntity> scList = null;
	List<SystemCodeEntity> feeList=null;
	Map<String,PriceContractInfoEntity> mapContact=null;
	private String subjectCode="";//费用类型
	
	@Override
	public ReturnT<String> execute(String... params) throws Exception {
		XxlJobLogger.log("dispatchBillPayCalcJob start.");
		return super.CalcJob(params);
	}
	
	@Override
	protected List<BizDispatchBillPayEntity> queryBillList(
			Map<String, Object> map) {
		XxlJobLogger.log("dispatchBillPayCalcJob查询条件map:【{0}】  ",map);
		return bizDispatchBillPayService.query(map);
	}

	@Override
	protected void initConf(List<BizDispatchBillPayEntity> billList) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("typeCode", "DISPATCH_COMPANY");
		scList = systemCodeService.querySysCodes(map);
		if(scList == null || scList.size()<1){
			throw new Exception("未查询到任何物流商");
		}
		//查询所有的月结账号
		Map<String, Object> feemap = new HashMap<String, Object>();
		feemap.put("typeCode", "MONTH_FEE_COUNT");
		feeList = systemCodeService.querySysCodes(feemap);
		
		mapContact=new HashMap<String,PriceContractInfoEntity>();
		delete(billList);
	}
	
	private void delete(List<BizDispatchBillPayEntity> billList){
		List<String> feesNos = new ArrayList<String>();
		Map<String, Object> feesMap = new HashMap<String, Object>();
		for (BizDispatchBillPayEntity entity : billList) {
			if(StringUtils.isNotEmpty(entity.getFeesNo())){
				feesNos.add(entity.getFeesNo());
			}
		}
		try{
			if(feesNos.size()>0){
				feesMap.put("feesNos", feesNos);
				long operateTime = System.currentTimeMillis();
				feesPayDispatchService.deleteBatch(feesMap);
				long current = System.currentTimeMillis();// 系统开始时间
				XxlJobLogger.log("批量删除费用成功 耗时【{0}】毫秒 删除条数【{1}】",(current-operateTime),feesNos.size());
			}
		}
		catch(Exception ex){
			XxlJobLogger.log("批量删除费用失败-- {1}",ex.getMessage());
		}
	}

	private List<PriceOutMainDispatchEntity> getQuoEntites(BizDispatchBillPayEntity entity){
		String subjectId=getSubjectId(entity.getCarrierId());
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("deliverId", entity.getDeliverid());
		map.put("subjectId", subjectId);
		map.put("wareHouseId", entity.getWarehouseCode());
		map.put("province", entity.getReceiveProvinceId());
		List<PriceOutMainDispatchEntity> priceOutList=outPriceDispatchDao.queryAllOutDispatch(map);
		return priceOutList;
	}
	@Override
	protected void calcuService(BizDispatchBillPayEntity entity,List<FeesPayDispatchEntity> feesList) {
		entity.setReceiveProvinceId(entity.getReceiveProvinceId().trim());
		entity.setReceiveCityId(entity.getReceiveCityId().trim());
		entity.setReceiveDistrictId(entity.getReceiveDistrictId().trim());
		FeesPayDispatchEntity feePayEntity = initfeeEntity(entity);
		long start = System.currentTimeMillis();// 系统开始时间
		long current = 0l;// 当前系统时间
		try{
			entity.setCalculateTime(JAppContext.currentTimestamp());
			feePayEntity.setCalculateTime(entity.getCalculateTime());
			String deliverId=entity.getDeliverid();
			//验证报价
			List<PriceOutMainDispatchEntity> priceOutList=getQuoEntites(entity);
			if(priceOutList==null||priceOutList.size()==0){
				XxlJobLogger.log("宅配商【{0}】,仓库【{1}】，省份【{2}】报价未配置",deliverId,entity.getWarehouseName(),entity.getReceiveProvinceId());
				entity.setIsCalculated(CalculateState.Quote_Miss.getCode());
				feePayEntity.setIsCalculated(CalculateState.Quote_Miss.getCode());
				entity.setRemark(entity.getRemark()+String.format("宅配商【{0}】,仓库【{1}】，省份【{2}】报价未配置;", deliverId,entity.getWarehouseName(),entity.getReceiveProvinceId()));
				feesList.add(feePayEntity);
				return;
			}
			handBizDispatch(priceOutList,entity);
			current = System.currentTimeMillis();
			XxlJobLogger.log("验证报价耗时：【{0}】毫秒  ",(current - start));
			start = System.currentTimeMillis();// 系统开始时间
			
			BillRulePayEntity  ruleEntity=getRule(entity);
			if(ruleEntity == null){
				XxlJobLogger.log("规则未配置");
				entity.setIsCalculated(CalculateState.Quote_Miss.getCode());
				feePayEntity.setIsCalculated(CalculateState.Quote_Miss.getCode());
				entity.setRemark(entity.getRemark()+"规则未配置;");
				feesList.add(feePayEntity);
				return ;
			}
			current = System.currentTimeMillis();
			XxlJobLogger.log("验证规则耗时：【{0}】毫秒  ,规则编号【{1}】",(current - start),ruleEntity.getQuotationNo());
			feePayEntity.setRuleNo(ruleEntity.getQuotationNo());
			CalcuReqVo<PriceOutMainDispatchEntity> reqVo = new CalcuReqVo<PriceOutMainDispatchEntity>();
			reqVo.setBizData(entity);//传入业务数据
			reqVo.setQuoEntites(priceOutList);
			reqVo.setRuleNo(ruleEntity.getQuotationNo());
			reqVo.setRuleStr(ruleEntity.getRule());
			//CalcuResultVo resultVo = feesCalcuService.FeesCalcuService(reqVo);
			CalcuResultVo resultVo = new CalcuResultVo();
			current = System.currentTimeMillis();
			if("succ".equals(resultVo.getSuccess())){
				XxlJobLogger.log("调用规则引擎   耗时【{0}】毫秒  费用【{1}】 ",(current - current),resultVo.getPrice());	
				feePayEntity.setAmount(resultVo.getPrice().doubleValue()); //价格
				String priceId="";
				String typePrice="";
				
				if(resultVo.getQuoId().indexOf("-")!=-1){
					priceId= resultVo.getQuoId().substring(0, resultVo.getQuoId().indexOf("-"));
					typePrice=resultVo.getQuoId().substring( resultVo.getQuoId().indexOf("-")+1);
				}else{
					priceId= resultVo.getQuoId();
				}
				PriceOutMainDispatchEntity price=queryDispatchPriceById(priceOutList,resultVo.getQuoId());
				
				if(StringUtils.isNotBlank(priceId) && price!=null){
					XxlJobLogger.log("PriceNo:"+ price.getId());
					if(StringUtils.isNotBlank(typePrice) && typePrice!=null){
						feePayEntity.setWeightLimit(price.getWeightLimit());   	  //重量界限
						feePayEntity.setUnitPrice(price.getUnitPrice());			  //单价
						feePayEntity.setHeadWeight(Double.valueOf(price.getExtra1()));    	  //首重
						feePayEntity.setHeadPrice(Double.valueOf(price.getExtra2()));	  //首重价格
						feePayEntity.setContinuedWeight(Double.valueOf(price.getExtra3())); //续重
						feePayEntity.setContinuedPrice(Double.valueOf(price.getExtra4()));   //续重价格

					}else{
						feePayEntity.setWeightLimit(price.getWeightLimit());   	  //重量界限
						feePayEntity.setUnitPrice(price.getUnitPrice());			  //单价
						feePayEntity.setHeadWeight(price.getFirstWeight());    	  //首重
						feePayEntity.setHeadPrice(price.getFirstWeightPrice());	  //首重价格
						feePayEntity.setContinuedWeight(price.getContinuedWeight()); //续重
						feePayEntity.setContinuedPrice(price.getContinuedPrice());   //续重价格
					}
				}else{
					feePayEntity.setWeightLimit(0.0d);   	  //重量界限
					feePayEntity.setUnitPrice(0.0d);			  //单价
					feePayEntity.setHeadWeight(0.0d);    	  //首重
					feePayEntity.setHeadPrice(0.0d);	  //首重价格
					feePayEntity.setContinuedWeight(0.0d); //续重
					feePayEntity.setContinuedPrice(0.0d);   //续重价格
				}
				feePayEntity.setPriceId(priceId);
				feePayEntity.setParam2(resultVo.getMethod());//计算方式
				feePayEntity.setParam3(resultVo.getQuoId());
				feePayEntity.setBizType(entity.getExtattr1());//判断是否是遗漏数据
				entity.setIsCalculated(CalculateState.Finish.getCode());
				feePayEntity.setIsCalculated(CalculateState.Finish.getCode());
				entity.setRemark(entity.getRemark()+"计算成功;");
				feesList.add(feePayEntity);
			}
			else{
				XxlJobLogger.log("调用规则引擎失败   耗时【{0}】毫秒   ",(current - start));
				XxlJobLogger.log("费用计算失败--"+resultVo.getMsg());
				feePayEntity.setAmount(0.0d);
				entity.setIsCalculated(CalculateState.Quote_Miss.getCode());
				feePayEntity.setIsCalculated(CalculateState.Quote_Miss.getCode());
				entity.setRemark(entity.getRemark()+"费用计算失败:"+resultVo.getMsg()+";");
				feesList.add(feePayEntity);
			}
		}catch(Exception ex){
			XxlJobLogger.log("配送费用计算失败--{0}",ex.getMessage());
			feePayEntity.setAmount(0.0d);
			entity.setIsCalculated(CalculateState.Sys_Error.getCode());
			feePayEntity.setIsCalculated(CalculateState.Sys_Error.getCode());
			entity.setRemark(entity.getRemark()+"费用计算异常:"+ex.getMessage()+";");
			feesList.add(feePayEntity);
		}
	}
	private BillRulePayEntity getRule(BizDispatchBillPayEntity entity) {
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("deliveryid", entity.getDeliverid());
		map.put("subjectId",getSubjectId(entity.getCarrierId()));
	    return payRuleRepositoryImp.queryByDeliverId(map);
	}

	/**
	 * 批量保存数据
	 */
	@Override
	protected void saveBatchData(List<BizDispatchBillPayEntity> billList,
			List<FeesPayDispatchEntity> feesList) {
		long start = System.currentTimeMillis();// 系统开始时间
		long current = 0l;// 当前系统时间
		bizDispatchBillPayService.updateBatch(billList);
		current = System.currentTimeMillis();
		XxlJobLogger.log("更新业务数据耗时：【{0}】毫秒  ",(current - start));
		start = System.currentTimeMillis();// 系统开始时间
		feesPayDispatchService.InsertBatch(feesList);
		current = System.currentTimeMillis();
		XxlJobLogger.log("新增费用数据耗时：【{0}】毫秒 ",(current - start));
	}

	//根据ID 找到对应的报价
	private PriceOutMainDispatchEntity queryDispatchPriceById(List<PriceOutMainDispatchEntity> priceList,String id){
		PriceOutMainDispatchEntity dispatchEntity=null;
		for(PriceOutMainDispatchEntity entity:priceList){
			if(entity.getId().toString().equals(id)){
				dispatchEntity=entity;
				break;
			}
		}
		return dispatchEntity;
	}
	//验证业务数据
	@Override
	protected boolean validateData(BizDispatchBillPayEntity entity,
			List<FeesPayDispatchEntity> feesList) {
		XxlJobLogger.log("数据主键ID:【{0}】  ",entity.getId());
		Timestamp time=JAppContext.currentTimestamp();
		entity.setCalculateTime(time);
		Map<String,Object> map=new HashMap<String,Object>();
		
		//获取新的重量
		entity.setNewTotalWeight(getNewTotalWeight(entity.getOriginWeight()));
		entity.setTotalWeight(entity.getNewTotalWeight());
		
		String deliverid=entity.getDeliverid();
		entity.setCalculateTime(time);
		boolean isInsert = StringUtils.isEmpty(entity.getFeesNo())?true:false; //true-新增  false-更新
		if(isInsert){
			String feesNo =sequenceService.getBillNoOne(FeesPayDispatchEntity.class.getName(), "PSFYF", "0000000000");
			entity.setFeesNo(feesNo);
		}
		FeesPayDispatchEntity feePayEntity = initfeeEntity(entity);
		feePayEntity.setCalculateTime(time);
		long start = System.currentTimeMillis();// 系统开始时间
		long current = 0l;// 当前系统时间
		String subjectID=getSubjectId(entity.getCarrierId());
		if(StringUtils.isEmpty(subjectID)){
			XxlJobLogger.log(String.format("运单号【%s】执行失败--原因：物流商【%s】未在数据字段中配置", entity.getWaybillNo(),entity.getCarrierId()));
			entity.setIsCalculated(CalculateState.Sys_Error.getCode());
			feePayEntity.setIsCalculated(CalculateState.Sys_Error.getCode());
			entity.setRemark(entity.getRemark()+String.format("运单号【%s】执行失败--原因：物流商【%s】未在数据字段中配置;", entity.getWaybillNo(),entity.getCarrierId()));
			feesList.add(feePayEntity);
			return false;
		}
		
		subjectCode=subjectID;
		feePayEntity.setSubjectCode(subjectID);
		
		entity.setCalculateTime(JAppContext.currentTimestamp());
		String provinceId="";
		String cityId="";
		String districtId="";
		boolean isAdjust=false;
		Double throwWeight = null;
		//调整省市区不为空
		if(StringUtils.isNotBlank(entity.getAdjustProvinceId())||
				StringUtils.isNotBlank(entity.getAdjustCityId())||
				StringUtils.isNotBlank(entity.getAdjustDistrictId())){
			provinceId=entity.getAdjustProvinceId();
			cityId=entity.getAdjustCityId();
			districtId=entity.getAdjustDistrictId();
			isAdjust=true;
		}else{
			provinceId=entity.getReceiveProvinceId();
			cityId=entity.getReceiveCityId();
			districtId=entity.getReceiveDistrictId();
		}
		//获取标准省市区名称，匹配不到计算失败  顺丰匹配到区县，其他只匹配到城市 
		if("SHUNFENG_DISPATCH".equals(subjectID)){
			RegionVo vo = new RegionVo(ReplaceChar(provinceId), ReplaceChar(cityId), ReplaceChar(districtId));
			RegionVo matchVo = omsAddressService.queryNameByAlias(vo);
			if ((StringUtils.isNotBlank(provinceId) && StringUtils.isBlank(matchVo.getProvince())) ||
					StringUtils.isNotBlank(cityId) && StringUtils.isBlank(matchVo.getCity()) ||
					StringUtils.isNotBlank(districtId) && StringUtils.isBlank(matchVo.getDistrict()))
			{
				XxlJobLogger.log("收件人地址存在空值  省【{0}】市【{1}】区【{2}】 运单号【{3}】:"+ provinceId,cityId,districtId,entity.getWaybillNo());
			}else{
				entity.setReceiveProvinceId(matchVo.getProvince());
				entity.setReceiveCityId(matchVo.getCity());
				entity.setReceiveDistrictId(matchVo.getDistrict());
				
				feePayEntity.setToProvinceName(matchVo.getProvince());// 目的省
				feePayEntity.setToCityName(matchVo.getCity());			// 目的市			
				feePayEntity.setToDistrictName(matchVo.getDistrict());// 目的区
			}
			
			
			//如果存在调整重量，则无需判断是否顺丰同城与非同城
			if(!DoubleUtil.isBlank(entity.getAdjustWeight())){
				double dd = getResult(entity.getAdjustWeight());
				entity.setWeight(dd);
				//entity.setTotalWeight(entity.getAdjustWeight());
				
			}
			else{
				//顺丰非同城按抛重计费  如果发件人【省】【市】有值 并且与收件人省市一致  则为顺丰同城
				//如果发件人【省】【市】有值
				if(StringUtils.isNotBlank(entity.getSendProvinceId()) && StringUtils.isNotBlank(entity.getSendCityId())){
					RegionVo vo1 = new RegionVo(ReplaceChar(entity.getSendProvinceId()), ReplaceChar(entity.getSendCityId()),ReplaceChar(null));
					RegionVo matchVo1 = omsAddressService.queryNameByAlias(vo);
					//发件人省市 与收件人省市相等
					if(matchVo1.getProvince().equals(entity.getReceiveProvinceId()) && matchVo1.getCity().equals(entity.getReceiveCityId())){
						XxlJobLogger.log("--------此单为顺丰同城  按普通重量计费--------");
						double dd = getResult(entity.getTotalWeight());
						entity.setWeight(dd);
						//entity.setTotalWeight(entity.getTotalWeight());
					}
					else{
						XxlJobLogger.log("--------此单为顺丰非同城  按泡重计费--------");
						double dd = getResult(entity.getThrowWeight());
						entity.setWeight(dd);
						//entity.setTotalWeight(entity.getThrowWeight());
					}
				}
				else{
					XxlJobLogger.log("--------顺丰发件人省或市为空 计算失败--------");
					entity.setIsCalculated(CalculateState.Sys_Error.getCode());
					feePayEntity.setIsCalculated(CalculateState.Sys_Error.getCode());
					entity.setRemark(entity.getRemark()+"顺丰发件人省或市为空 计算失败;");
					feesList.add(feePayEntity);
					return false;
				}
			}
			
			
			
			
		}else{
			RegionVo vo = new RegionVo(ReplaceChar(provinceId), ReplaceChar(cityId), ReplaceChar(districtId));
			RegionVo matchVo = omsAddressService.queryNameByAlias(vo);
			if ((StringUtils.isNotBlank(provinceId) && StringUtils.isBlank(matchVo.getProvince())) ||
					StringUtils.isNotBlank(cityId) && StringUtils.isBlank(matchVo.getCity())) 
			{
				XxlJobLogger.log("收件人地址存在空值  省【{0}】市【{1}】区【{2}】 运单号【{3}】:"+ provinceId,cityId,districtId,entity.getWaybillNo());
			}else{
				if(isAdjust){
					entity.setAdjustProvinceId(matchVo.getProvince());
					entity.setAdjustCityId(matchVo.getCity());
					entity.setReceiveProvinceId(matchVo.getProvince());
					entity.setReceiveCityId(matchVo.getCity());
				}else{
					entity.setReceiveProvinceId(matchVo.getProvince());
					entity.setReceiveCityId(matchVo.getCity());
				}
				feePayEntity.setToProvinceName(matchVo.getProvince());	// 目的省
				feePayEntity.setToCityName(matchVo.getCity());			// 目的市			
			}	
			
			if(!DoubleUtil.isBlank(entity.getAdjustWeight())){
				double mm=getTeshu(entity.getAdjustWeight());
				entity.setWeight(mm);
				//entity.setTotalWeight(entity.getAdjustWeight());
			}
			else{
				double mm=getTeshu(entity.getTotalWeight());
				entity.setWeight(mm);
				//entity.setTotalWeight(entity.getTotalWeight());
			}
			if("1400000036".equals(deliverid) || "1400000047".equals(deliverid) || "1400000658".equals(deliverid) || "1400000040".equals(deliverid) || "1500000019".equals(deliverid) || "1500000018".equals(deliverid)){
				double dd = DoubleUtil.isBlank(entity.getAdjustWeight())?entity.getTotalWeight():entity.getAdjustWeight();
				entity.setWeight(dd);
				//entity.setTotalWeight(dd);
			}
			
		}
		
		if(DoubleUtil.isBlank(entity.getWeight())){
			XxlJobLogger.log("--------未获取到重量 计算失败--------");
			entity.setIsCalculated(CalculateState.Sys_Error.getCode());
			feePayEntity.setIsCalculated(CalculateState.Sys_Error.getCode());
			entity.setRemark(entity.getRemark()+"未获取到重量 计算失败;");
			feesList.add(feePayEntity);
			return false;
		}
		
		
		//应付的如果是顺丰的进0.5，其他的进1
		/*Double totalWeight =entity.getTotalWeight();
		if(null!=entity.getAdjustWeight()){
			totalWeight = entity.getAdjustWeight();
		}*/
		
		//获取此时的宅配商
		//南方传媒(1400000036)  河北报业(1400000658)  品骏控股（重庆(1400000047)、福建(1400000040)）   成都鑫盛通达（圆通）(1500000019)    昆明捷美佳（中通）(1500000018) 应付不进位的
		/*if("1400000036".equals(deliverid) || "1400000047".equals(deliverid) || "1400000658".equals(deliverid) || "1400000040".equals(deliverid) || "1500000019".equals(deliverid) || "1500000018".equals(deliverid)){
			entity.setWeight(totalWeight);
		}else{
			if("SHUNFENG_DISPATCH".equals(subjectID)){
				totalWeight = entity.getAdjustWeight()==null?(throwWeight==null?entity.getTotalWeight():throwWeight):entity.getAdjustWeight();
				double dd = getResult(totalWeight);
				entity.setWeight(dd);
			}else{	
				double mm=getTeshu(totalWeight);
				entity.setWeight(mm);
			}	
		}*/
		feePayEntity.setTotalWeight(entity.getTotalWeight()==null?entity.getAdjustWeight():entity.getTotalWeight());
		feePayEntity.setChargedWeight(entity.getWeight());
		//九曳自己的顺丰月结账号才计算费用，如果是商家自己的月结账号，则不计算
		if("SHUNFENG_DISPATCH".equals(subjectID)){
			String feeCount=entity.getMonthFeeCount();
			boolean isjyMouth = false;
			for (SystemCodeEntity mouthfeeEntity : feeList) {
				if(mouthfeeEntity.getCode().equals(feeCount)){
					isjyMouth = true;
					XxlJobLogger.log(entity.getRemark());
					break;
				}
			}
			if(!isjyMouth){
				entity.setRemark(entity.getRemark()+"该顺丰运单不是九曳的月结账号，金额置0;");						
				feePayEntity.setAmount(0.0d);
				entity.setIsCalculated(CalculateState.No_Exe.getCode());
				feePayEntity.setIsCalculated(CalculateState.No_Exe.getCode());
				feesList.add(feePayEntity);
				return false;
			}
		}
		
		//如果不是妥投的则不计算
		if(!"终结".equals(entity.getBigstatus()) || !"已签收".equals(entity.getSmallstatus())){
			entity.setRemark(entity.getRemark()+"该运单不是签收状态，金额置0;");						
			feePayEntity.setAmount(0.0d);
			entity.setIsCalculated(CalculateState.No_Exe.getCode());
			feePayEntity.setIsCalculated(CalculateState.No_Exe.getCode());
			feesList.add(feePayEntity);
			return false;
		}
		current = System.currentTimeMillis();
		XxlJobLogger.log("验证运单状态，匹配省市区耗时：【{0}】毫秒  ",(current - start));
		start = System.currentTimeMillis();// 系统开始时间
		//验证商家合同
		PriceContractInfoEntity contractEntity =null;
		if(mapContact.containsKey(deliverid)){
			contractEntity=mapContact.get(deliverid);
		}else{
			map.clear();
			map.put("customerid", deliverid);
			map.put("contractTypeCode", "DELIVER_CONTRACT");
		    contractEntity = jobPriceContractInfoService.queryContractByCustomer(map);
		    mapContact.put(deliverid, contractEntity);
		}
		if(contractEntity == null || StringUtils.isEmpty(contractEntity.getContractCode())){
			XxlJobLogger.log(String.format("未查询到合同  运单号【%s】--宅配商【%s】", entity.getWaybillNo(),entity.getDeliverName()));
			entity.setIsCalculated(CalculateState.Contract_Miss.getCode());
			feePayEntity.setIsCalculated(CalculateState.Contract_Miss.getCode());
			entity.setRemark(entity.getRemark()+String.format("未查询到合同  运单号【%s】--宅配商【%s】;", entity.getWaybillNo(),entity.getDeliverName()));
			feesList.add(feePayEntity);
			return false;
		}
		current = System.currentTimeMillis();
		XxlJobLogger.log("验证合同   耗时【{0}】毫秒  合同编号 【{1}】",(current - start),contractEntity.getContractCode());
		start = System.currentTimeMillis();// 系统开始时间
		return true;
	}
	private String getSubjectId(String carrierId){
		String subjectID = null;
		for (SystemCodeEntity scEntity : scList) {
			if(scEntity.getExtattr1().equals(carrierId)){
				subjectID = scEntity.getCode();
				break;
			}
		}
		return subjectID;
	}
	//初始化费用
	private FeesPayDispatchEntity initfeeEntity(BizDispatchBillPayEntity entity){
		FeesPayDispatchEntity feeEntity = new FeesPayDispatchEntity();
		feeEntity.setCreator("system");
		feeEntity.setCreateTime(entity.getCreateTime());//费用表的创建时间应为业务表的创建时间
		feeEntity.setOperateTime(entity.getCreateTime()); //操作时间
		//feeEntity.setDeliveryid(entity);
		//feeEntity.(feeEntity.getCreateTime());
		feeEntity.setOutstockNo(entity.getOutstockNo());		// 出库单号
		feeEntity.setWarehouseCode(entity.getWarehouseCode());	// 仓库ID
		feeEntity.setCustomerid(entity.getCustomerid());		// 商家ID
		feeEntity.setCustomerName(entity.getCustomerName());	//商家名称
		feeEntity.setDeliveryid(entity.getDeliverid());         // 宅配ID
		feeEntity.setDeliverName(entity.getDeliverName());				
		feeEntity.setCarrierid(entity.getCarrierId());   	   // 物流商ID
		feeEntity.setCarrierName(entity.getCarrierName());
		feeEntity.setWaybillNo(entity.getWaybillNo());			// 运单号
		
		String provinceId="";
		String cityId="";
		String districtId="";
		//调整省市区不为空
		if(StringUtils.isNotBlank(entity.getAdjustProvinceId())||
				StringUtils.isNotBlank(entity.getAdjustCityId())||
				StringUtils.isNotBlank(entity.getAdjustDistrictId())){
			provinceId=entity.getAdjustProvinceId();
			cityId=entity.getAdjustCityId();
			districtId=entity.getAdjustDistrictId();
		}else{
			provinceId=entity.getReceiveProvinceId();
			cityId=entity.getReceiveCityId();
			districtId=entity.getReceiveDistrictId();
		}
		feeEntity.setToProvinceName(provinceId);// 目的省
		feeEntity.setToCityName(cityId);			// 目的市			
		feeEntity.setToDistrictName(districtId);// 目的区
		
		feeEntity.setExternalNo(entity.getExternalNo());        //外部单号
		feeEntity.setAcceptTime(entity.getAcceptTime());        //揽收时间
		feeEntity.setSignTime(entity.getSignTime());
		feeEntity.setSubjectType("DISPATCH_SUBJECT_TYPE");
		feeEntity.setSubjectCode(subjectCode);
		feeEntity.setOtherSubjectCode("de_delivery_sum");
		feeEntity.setStatus("0");
		feeEntity.setDelFlag("0");
		feeEntity.setAmount(0.0d);
		feeEntity.setFeesNo(entity.getFeesNo());
		feeEntity.setTemperatureType(entity.getTemperatureTypeCode());
		feeEntity.setTotalWeight(entity.getTotalWeight());
		feeEntity.setParam1(TemplateTypeEnum.COMMON.getCode());
		feeEntity.setBigstatus(entity.getBigstatus());
		feeEntity.setSmallstatus(entity.getSmallstatus());
		feeEntity.setChargedWeight(entity.getWeight());
		feeEntity.setWeightLimit(0.0d);
		feeEntity.setUnitPrice(0.0d);
		feeEntity.setHeadWeight(0.0d);
		feeEntity.setHeadPrice(0.0d);
		feeEntity.setContinuedWeight(0.0d);
		feeEntity.setContinuedPrice(0.0d);	
		return feeEntity;	
	}
	
	/**
	 * 获取新的实际重量
	 * @param originWeight
	 * @return
	 */
	public double getNewTotalWeight(double originWeight){
		// 小数为大于0.05 保留小数， 不更改。应付重量=实际重量。
		// 小数位小于等于0.05 则应付重量=实际重量整数位+0
		
		double totalWeight=0d;
		if(!DoubleUtil.isBlank(originWeight)){
			BigDecimal   a1   =BigDecimal.valueOf(originWeight);
			BigDecimal   a2   =BigDecimal.valueOf(Math.floor(originWeight));
			BigDecimal result=a1.subtract(a2);		
			double s=result.doubleValue();
			if(s>0.05){
				totalWeight=originWeight;
			}else if(s<=0.05){
				totalWeight=Math.floor(originWeight);
			}
		}
		return totalWeight;
	}
	
	
	/**
	 * 计算此时的重量
	 * @param weight
	 * @return
	 */
	public double getResult(double weight){
		//原重
		double a=weight;	
		//现重
		double c=0.0;	
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
		return c;
	}
	
	/**
	 * 对于除顺丰之外的宅配商，重量先舍去小数点2位以上的数据，再四舍五入(不用处理了)
	 * @param weightTeshu
	 * @return
	 * （eg: 1.009->1.00->1   1.901->1.90->2）
	 */
	public double getTeshu(double weightTeshu){
		double weight=weightTeshu;
		return weight;
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
	public void handBizDispatch(List<PriceOutMainDispatchEntity> priceList,BizDispatchBillPayEntity bizEntity){
		//判断该地址是否存在于报价中，为空时则为空
		//获取此时业务数据的市区
		String bizCity=bizEntity.getReceiveCityId();
		String bizArea=bizEntity.getReceiveDistrictId();
		
		boolean city=false;
		boolean area=false;
	
		for(PriceOutMainDispatchEntity mainDispatchEntity : priceList){
			//获取到此时的仓库
			if(!mainDispatchEntity.getStartWarehouseId().equals(bizEntity.getWarehouseCode())){
				continue;
			}
			
			//获取报价此时的市ID
			String dispatchCityId=mainDispatchEntity.getCityId();
			//获取报价此时的区ID
			String dispatchAreaId=mainDispatchEntity.getAreaId();

			//业务数据市县区都不为空情况
			if(StringUtils.isNotBlank(bizCity) && StringUtils.isNotBlank(bizArea)){
				//取此时报价中市区不为空的情况
				if(StringUtils.isNotBlank(dispatchCityId) && StringUtils.isNotBlank(dispatchAreaId)){
					//直接判断省市是否相等
					if(dispatchAreaId.trim().equals(bizArea) && dispatchCityId.trim().equals(bizCity)){
						area=true;		
						city=true;
						break;					
					}
				}
				
				//再判断市
				//此时取出报价区不为空的
				if(StringUtils.isNotBlank(dispatchCityId) && StringUtils.isBlank(dispatchAreaId)){
					if(dispatchCityId.trim().equals(bizCity)){
						city=true;
					}
				}
			}
								
			//业务数据中市不为空，县为空的情况
			if(StringUtils.isNotBlank(bizCity) && StringUtils.isBlank(bizArea)){
				//此时取报价中市不为空，县为空的情况
				if(StringUtils.isNotBlank(dispatchCityId) && StringUtils.isBlank(dispatchAreaId)){
					if(dispatchCityId.trim().equals(bizEntity.getReceiveCityId().trim())){
						city=true;
						break;
					}
				}
			}
		}
		
	
		if(city==false){
			bizEntity.setReceiveCityId("");
		}
		
		if(area==false){
			bizEntity.setReceiveDistrictId("");
		}			
	}

	@Override
	protected void calcuStandardService(List<BizDispatchBillPayEntity> billList) {
		// TODO Auto-generated method stub
		
	}
	
}
/*
public class DispatchBillPayCalcJob extends IJobHandler{

	@Autowired private IBizDispatchBillPayService bizDispatchBillPayService;
	@Autowired private SequenceService sequenceService;
	@Autowired private IFeesPayDispatchService feesPayDispatchService;
	@Autowired private IPriceContractInfoService priceContractInfoService;
	@Autowired private ISystemCodeService systemCodeService;
	@Autowired private IAddressService omsAddressService;
	@Autowired private IFeesCalcuService feesCalcuService;
	@Autowired private IPriceOutDispatchDao outPriceDispatchDao;
	@Autowired private IPayRuleRepository payRuleRepositoryImp;
	
	@Override
    public ReturnT<String> execute(String... params) throws Exception {
        XxlJobLogger.log("dispatchBillPayCalcJob start.");
        return CalcJob(params);
    }
	
	private ReturnT<String> CalcJob(String[] params) {
		long start = System.currentTimeMillis();// 系统开始时间
		long current = 0l;// 当前系统时间
		int num = 100;
		
		Map<String, Object> cond = new HashMap<String, Object>();
		if(params != null && params.length > 0) {
		    try {
		    	//处理定时任务参数
		    	cond = JobParameterHandler.handler(params);
		    } catch (Exception e) {
	            current = System.currentTimeMillis();
	            XxlJobLogger.log("【终止异常】,解析Job配置的参数出现错误,原因:" + e.getMessage() + ",耗时："+ ((current - start) / 1000) + "秒");
	            return ReturnT.FAIL;
	        }
		}else {
			cond.put("num", num);//单次执行最多计算多少入库单
		}
		
		//********************************** 查询待计算的宅配运单主表数据 **********************************
		List<BizDispatchBillPayEntity> billList = bizDispatchBillPayService.query(cond);
		if (billList == null || billList.size() < 1) {
			XxlJobLogger.log("未查询到任何行");
			return ReturnT.SUCCESS;	
		}
		
		//*********************************查询所有物流商*********************************		
		List<SystemCodeEntity> scList = null;
		try{
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("typeCode", "DISPATCH_COMPANY");
			scList = systemCodeService.querySysCodes(map);
		}
		catch(Exception ex){
			current = System.currentTimeMillis();
			XxlJobLogger.log("【终止异常】,查询物流商出现错误,原因:" + ex.getMessage() + ",耗时："+ ((current - start) / 1000) + "秒");
			return ReturnT.FAIL;	
		}
		if(scList == null || scList.size()<1){
			XxlJobLogger.log("未查询到任何物流商");
			return ReturnT.SUCCESS;	
		}
		
		//查询所有的月结账号
		Map<String, Object> feemap = new HashMap<String, Object>();
		feemap.put("typeCode", "MONTH_FEE_COUNT");
		List<SystemCodeEntity> feeList = systemCodeService.querySysCodes(feemap);	
		
		List<BizDispatchBillPayEntity> failList=new ArrayList<BizDispatchBillPayEntity>();
		for(BizDispatchBillPayEntity entity : billList) {
			entity.setCalculateTime(JAppContext.currentTimestamp());//更新计算时间
			XxlJobLogger.log(String.format("====================================开始计算[%s]============================================",entity.getId()));
			String subjectID = null;
			for (SystemCodeEntity scEntity : scList) {
				if(scEntity.getExtattr1().equals(entity.getCarrierId())){
					subjectID = scEntity.getCode();
					break;
				}
			}
			if(StringUtils.isEmpty(subjectID)){
				XxlJobLogger.log(String.format("运单号【%s】执行失败--原因：物流商【%s】未在数据字段中配置", entity.getWaybillNo(),entity.getCarrierId()));
				entity.setIsCalculated(CalculateState.Sys_Error.getCode());
				entity.setRemark(String.format("运单号【%s】执行失败--原因：物流商【%s】未在数据字段中配置", entity.getWaybillNo(),entity.getCarrierId()));
				failList.add(entity);
				continue;
			}
			
			XxlJobLogger.log("费用科目编号:"+ subjectID);
			
			
			//********************获合同编号********************
			PriceContractInfoEntity contractEntity = null;
			Map<String,Object> aCondition=new HashMap<>();
			aCondition.put("customerid", entity.getDeliverid());
			aCondition.put("contractTypeCode", "DELIVER_CONTRACT");
			contractEntity = priceContractInfoService.queryContractByCustomer(aCondition);
			
			if(contractEntity == null || StringUtils.isEmpty(contractEntity.getContractCode())){
				XxlJobLogger.log(String.format("未查询到合同  运单号【%s】--宅配商【%s】", entity.getWaybillNo(),entity.getDeliverName()));
				entity.setIsCalculated(CalculateState.Contract_Miss.getCode());
				entity.setRemark(String.format("未查询到合同  运单号【%s】--宅配商【%s】", entity.getWaybillNo(),entity.getDeliverName()));
				failList.add(entity);
				continue;
			}
			
			XxlJobLogger.log("合同编号:"+contractEntity.getContractCode());
			
			//获取标准省市区名称，匹配不到计算失败  顺丰匹配到区县，其他只匹配到城市 
			if("SHUNFENG_DISPATCH".equals(subjectID)){
				RegionVo vo = new RegionVo(ReplaceChar(entity.getReceiveProvinceId()), ReplaceChar(entity.getReceiveCityId()), ReplaceChar(entity.getReceiveDistrictId()));
				RegionVo matchVo = omsAddressService.queryNameByAlias(vo);
				if ((StringUtils.isNotBlank(ReplaceChar(entity.getReceiveProvinceId())) && StringUtils.isBlank(ReplaceChar(matchVo.getProvince()))) ||
						StringUtils.isNotBlank(ReplaceChar(entity.getReceiveCityId())) && StringUtils.isBlank(ReplaceChar(matchVo.getCity())) ||
						StringUtils.isNotBlank(ReplaceChar(entity.getReceiveDistrictId())) && StringUtils.isBlank(ReplaceChar(matchVo.getDistrict())))
				{
					
				}else{
					entity.setReceiveProvinceId(matchVo.getProvince());
					entity.setReceiveCityId(matchVo.getCity());
					entity.setReceiveDistrictId(matchVo.getDistrict());
				}
				
			}else{
				RegionVo vo = new RegionVo(ReplaceChar(entity.getReceiveProvinceId()), ReplaceChar(entity.getReceiveCityId()));
				RegionVo matchVo = omsAddressService.queryNameByAlias(vo);
				if ((StringUtils.isNotBlank(entity.getReceiveProvinceId()) && StringUtils.isBlank(matchVo.getProvince())) ||
						StringUtils.isNotBlank(entity.getReceiveCityId()) && StringUtils.isBlank(matchVo.getCity())) 
				{
				
				}else{
					entity.setReceiveProvinceId(matchVo.getProvince());
					entity.setReceiveCityId(matchVo.getCity());
				}	
			}
			
			//应付的如果是顺丰的进0.5，其他的进1
			Double totalWeight =entity.getTotalWeight()+entity.getAdjustWeight(); 
			
			//获取此时的宅配商
			//南方传媒(1400000036)  河北报业(1400000658)  品骏控股（重庆(1400000047)、福建(1400000040)）   成都鑫盛通达（圆通）(1500000019)    昆明捷美佳（中通）(1500000018) 应付不进位的
			String deliverid=entity.getDeliverid();
			if("1400000036".equals(deliverid) || "1400000047".equals(deliverid) || "1400000658".equals(deliverid) || "1400000040".equals(deliverid) || "1500000019".equals(deliverid) || "1500000018".equals(deliverid)){
				entity.setTotalWeight(totalWeight);
			}else{
				if("SHUNFENG_DISPATCH".equals(subjectID)){
					double dd = getResult(totalWeight);
					entity.setTotalWeight(dd);
				}else{	
					double mm=getTeshu(totalWeight);
					entity.setTotalWeight(mm);
				}	
			}
			
			FeesPayDispatchEntity feePayEntity = initfeeEntity(entity,totalWeight,subjectID);
			
			boolean isInsert = StringUtils.isEmpty(entity.getFeesNo())?true:false; //true-新增  false-更新
			if(isInsert){
				String feesNo =sequenceService.getBillNoOne(FeesPayDispatchEntity.class.getName(), "PSFYF", "0000000000");
				feePayEntity.setFeesNo(feesNo);
				entity.setFeesNo(feesNo);
			}
			
			//********************调用规则********************
			try{	
				//九曳自己的顺丰月结账号才计算费用，如果是商家自己的月结账号，则不计算
				if("SHUNFENG_DISPATCH".equals(subjectID)){
					String feeCount=entity.getMonthFeeCount();
					boolean isjyMouth = false;
					for (SystemCodeEntity mouthfeeEntity : feeList) {
						if(mouthfeeEntity.getCode().equals(feeCount)){
							isjyMouth = true;
							XxlJobLogger.log(entity.getRemark());
							break;
						}
					}
					if(!isjyMouth){
						entity.setRemark("该顺丰运单不是九曳的月结账号，金额置0");						
						feePayEntity.setAmount(0.0d);
						entity.setIsCalculated(CalculateState.No_Exe.getCode());
						insertToFee(feePayEntity, entity);
						continue;
					}
				}
				
				//如果不是妥投的则不计算
				if(!"终结".equals(entity.getBigstatus()) || !"已签收".equals(entity.getSmallstatus())){
					entity.setRemark("该运单不是签收状态，金额置0");						
					feePayEntity.setAmount(0.0d);
					entity.setIsCalculated(CalculateState.No_Exe.getCode());
					insertToFee(feePayEntity, entity);
					continue;
				}
			    String quoType = "C";//默认使用常规报价
				CalcuReqVo<PriceOutMainDispatchEntity> reqVo = new CalcuReqVo<PriceOutMainDispatchEntity>();
				reqVo.setBizData(entity);//传入业务数据
				Map<String,Object> map=new HashMap<String,Object>();
				map.put("deliverId", entity.getDeliverid());
				map.put("subjectId", subjectID);
				List<PriceOutMainDispatchEntity> priceOutList=outPriceDispatchDao.queryAllOutDispatch(map);
				BillRulePayEntity ruleEntity=null;
				if(priceOutList == null || priceOutList.size() == 0){ //常规报价不存在,找标准报价
					XxlJobLogger.log("报价未配置");
					entity.setIsCalculated(CalculateState.Quote_Miss.getCode());
					entity.setRemark("报价未配置");
					failList.add(entity);
					continue;
					
				}else{
					//新增根据报价处理业务数据，使其支持模糊匹配
					handBizDispatch(priceOutList,entity);
					
					map=new HashMap<String,Object>();
					map.put("deliveryid", entity.getDeliverid());
					map.put("subjectId",subjectID);
					ruleEntity=payRuleRepositoryImp.queryByDeliverId(map);
					if(ruleEntity == null){
						XxlJobLogger.log("规则未配置");
						entity.setIsCalculated(CalculateState.Quote_Miss.getCode());
						entity.setRemark("规则未配置");
						failList.add(entity);
						continue;
					}
					reqVo.setQuoEntites(priceOutList);
					reqVo.setRuleNo(ruleEntity.getQuotationNo());
					reqVo.setRuleStr(ruleEntity.getRule());
				}
				feePayEntity.setRuleNo(ruleEntity.getQuotationNo());
				CalcuResultVo resultVo = feesCalcuService.FeesCalcuService(reqVo);
				if("succ".equals(resultVo.getSuccess())){
					feePayEntity.setAmount(resultVo.getPrice().doubleValue()); //价格
					String priceId="";
					String typePrice="";
					
					if(resultVo.getQuoId().indexOf("-")!=-1){
						priceId= resultVo.getQuoId().substring(0, resultVo.getQuoId().indexOf("-"));
						typePrice=resultVo.getQuoId().substring( resultVo.getQuoId().indexOf("-")+1);
					}else{
						priceId= resultVo.getQuoId();
					}
					feePayEntity.setPriceId(priceId);
					feePayEntity.setParam1(TemplateTypeEnum.getDesc(quoType));
					feePayEntity.setParam2(resultVo.getMethod());//计算方式
					entity.setIsCalculated(CalculateState.Finish.getCode());
					entity.setRemark("计算成功");
					Map<String,Object> acondition=new HashMap<String,Object>();
					acondition.put("id", priceId);
					XxlJobLogger.log("此时的报价id"+priceId);
					PriceMainDispatchEntity price=priceContractInfoService.queryPayOne(acondition);
					if(StringUtils.isNotBlank(priceId) && price!=null){
						XxlJobLogger.log("PriceNo:"+ price.getId());
						if(StringUtils.isNotBlank(typePrice) && typePrice!=null){
							feePayEntity.setWeightLimit(price.getWeightLimit());   	  //重量界限
							feePayEntity.setUnitPrice(price.getUnitPrice());			  //单价
							feePayEntity.setHeadWeight(Double.valueOf(price.getExtra1()));    	  //首重
							feePayEntity.setHeadPrice(Double.valueOf(price.getExtra2()));	  //首重价格
							feePayEntity.setContinuedWeight(Double.valueOf(price.getExtra3())); //续重
							feePayEntity.setContinuedPrice(Double.valueOf(price.getExtra4()));   //续重价格

						}else{
							feePayEntity.setWeightLimit(price.getWeightLimit());   	  //重量界限
							feePayEntity.setUnitPrice(price.getUnitPrice());			  //单价
							feePayEntity.setHeadWeight(price.getFirstWeight());    	  //首重
							feePayEntity.setHeadPrice(price.getFirstWeightPrice());	  //首重价格
							feePayEntity.setContinuedWeight(price.getContinuedWeight()); //续重
							feePayEntity.setContinuedPrice(price.getContinuedPrice());   //续重价格
						}
					}else{
						feePayEntity.setWeightLimit(0.0d);   	  //重量界限
						feePayEntity.setUnitPrice(0.0d);			  //单价
						feePayEntity.setHeadWeight(0.0d);    	  //首重
						feePayEntity.setHeadPrice(0.0d);	  //首重价格
						feePayEntity.setContinuedWeight(0.0d); //续重
						feePayEntity.setContinuedPrice(0.0d);   //续重价格
					}
					insertToFee(feePayEntity, entity);
				}
				else{
					XxlJobLogger.log("配送费用计算失败--"+resultVo.getMsg());
					feePayEntity.setAmount(0.0d);
					feePayEntity.setParam1(TemplateTypeEnum.getDesc(quoType));
					entity.setIsCalculated(CalculateState.Quote_Miss.getCode());
					entity.setRemark("费用计算失败:"+resultVo.getMsg());
					insertToFee(feePayEntity, entity);
				}
				/*
				CalculateVo calcuVo = new CalculateVo();
				calcuVo.setBizTypeCode("DISPATCH");
				calcuVo.setType("pay");
				calcuVo.setSubjectId(subjectID);
				calcuVo.setContractCode(contractEntity.getContractCode());
				calcuVo.setObj(entity);
				
				XxlJobLogger.log("计算前业务数据中的省为：["+entity.getReceiveProvinceId()+"],市为:["+entity.getReceiveCityId()+"],区为:["+entity.getReceiveDistrictId()+"]");
				calcuVo = feesCalculateRpcServiceImpl.calculate(calcuVo);*/
				
				
				//BizDispatchBillPayEntity r1=(BizDispatchBillPayEntity)calcuVo.getObj();
				//XxlJobLogger.log("计算后业务数据中的省为：["+r1.getReceiveProvinceId()+"],市为:["+r1.getReceiveCityId()+"],区为:["+r1.getReceiveDistrictId()+"]");
				//feePayEntity.setRuleNo(calcuVo.getRuleno());
				// 新方案  1:业务数据  2:报价集合  3:规则编号/代码
				/*
				if(calcuVo.getSuccess() && StringUtils.isNotBlank(r1.getDispatchId())){
					XxlJobLogger.log("此时的物流商"+calcuVo.getSubjectId());
					if(calcuVo.getPrice()!=null){
						feePayEntity.setAmount(calcuVo.getPrice().doubleValue()); //价格
					}else{
						feePayEntity.setAmount(0.0d);
					}				
					BizDispatchBillPayEntity r=(BizDispatchBillPayEntity)calcuVo.getObj();
					String priceId="";
					String typePrice="";
					
					if(r.getDispatchId().indexOf("-")!=-1){
						priceId=r.getDispatchId().substring(0,r.getDispatchId().indexOf("-"));
						typePrice=r.getDispatchId().substring(r.getDispatchId().indexOf("-")+1);
					}else{
						priceId=r.getDispatchId();
					}
					
					//根据模板id查找
					//String number=calcuVo.getMobanCode();
					//通过模板id查找对应的id
					//Integer id=priceContractInfoService.getId(number);
					Map<String,Object> acondition=new HashMap<String,Object>();
					acondition.put("id", priceId);
					XxlJobLogger.log("此时的报价id"+priceId);
					PriceMainDispatchEntity price=priceContractInfoService.queryPayOne(acondition);
					if(StringUtils.isNotBlank(priceId) && price!=null){
						XxlJobLogger.log("PriceNo:"+ price.getId());
						if(StringUtils.isNotBlank(typePrice) && typePrice!=null){
							feePayEntity.setWeightLimit(price.getWeightLimit());   	  //重量界限
							feePayEntity.setUnitPrice(price.getUnitPrice());			  //单价
							feePayEntity.setHeadWeight(Double.valueOf(price.getExtra1()));    	  //首重
							feePayEntity.setHeadPrice(Double.valueOf(price.getExtra2()));	  //首重价格
							feePayEntity.setContinuedWeight(Double.valueOf(price.getExtra3())); //续重
							feePayEntity.setContinuedPrice(Double.valueOf(price.getExtra4()));   //续重价格

						}else{
							feePayEntity.setWeightLimit(price.getWeightLimit());   	  //重量界限
							feePayEntity.setUnitPrice(price.getUnitPrice());			  //单价
							feePayEntity.setHeadWeight(price.getFirstWeight());    	  //首重
							feePayEntity.setHeadPrice(price.getFirstWeightPrice());	  //首重价格
							feePayEntity.setContinuedWeight(price.getContinuedWeight()); //续重
							feePayEntity.setContinuedPrice(price.getContinuedPrice());   //续重价格
						}
					}else{
						feePayEntity.setWeightLimit(0.0d);   	  //重量界限
						feePayEntity.setUnitPrice(0.0d);			  //单价
						feePayEntity.setHeadWeight(0.0d);    	  //首重
						feePayEntity.setHeadPrice(0.0d);	  //首重价格
						feePayEntity.setContinuedWeight(0.0d); //续重
						feePayEntity.setContinuedPrice(0.0d);   //续重价格
					}
					feePayEntity.setParam1(calcuVo.getExtter1());
					entity.setIsCalculated(CalculateState.Finish.getCode());
					insertToFee(feePayEntity, entity);
				}
				else{
					XxlJobLogger.log("配送费用计算失败--"+calcuVo.getMsg());
					feePayEntity.setAmount(0.0d);
					feePayEntity.setWeightLimit(0.0d);   	  //重量界限
					feePayEntity.setUnitPrice(0.0d);			  //单价
					feePayEntity.setHeadWeight(0.0d);    	  //首重
					feePayEntity.setHeadPrice(0.0d);	  //首重价格
					feePayEntity.setContinuedWeight(0.0d); //续重
					feePayEntity.setContinuedPrice(0.0d);   //续重价格
					feePayEntity.setParam1(calcuVo.getExtter1());
					entity.setIsCalculated(CalculateState.Quote_Miss.getCode());
					entity.setRemark("费用计算失败:"+calcuVo.getMsg());
					insertToFee(feePayEntity, entity);
				}*//*
			}
			catch(Exception ex){
				XxlJobLogger.log("配送费用计算失败--{0}",ex.getMessage());
				entity.setIsCalculated(CalculateState.Sys_Error.getCode());
				entity.setRemark("费用计算异常:"+ex.getMessage());
				insertToFee(feePayEntity, entity);
			}
			XxlJobLogger.log(String.format("====================================[%s]计算完毕============================================",entity.getId()));
		}
		
		//****************************更新计算失败的入库单****************************
		try{
			if(failList.size()>0){
				bizDispatchBillPayService.updateBatch(failList);
			}
			current = System.currentTimeMillis();
			XxlJobLogger.log("【宅配费用计算执行完毕】,耗时："+ ((current - start) / 1000) + "秒");
			return ReturnT.SUCCESS;	
		}
		catch(Exception ex){
			XxlJobLogger.log("【宅配费用计算执行异常】,原因："+ ex.getMessage());
			return ReturnT.FAIL;// 出现异常直接终止
		}
	}		
	
	private void insertToFee(FeesPayDispatchEntity feeEntity,BizDispatchBillPayEntity entity){
		if(feesPayDispatchService.Insert(feeEntity)){
		}
		else{
			entity.setIsCalculated(CalculateState.Sys_Error.getCode());
			entity.setRemark("计算成功，操作配送费用表表失败");
			XxlJobLogger.log("写入配送费用表失败");
		}
		bizDispatchBillPayService.update(entity);
	}
	
	private FeesPayDispatchEntity initfeeEntity(BizDispatchBillPayEntity entity,Double totalWeight,String subjectID){
		FeesPayDispatchEntity feeEntity = new FeesPayDispatchEntity();
		feeEntity.setCreator("system");
		feeEntity.setCreateTime(entity.getCreateTime());//费用表的创建时间应为业务表的创建时间
		feeEntity.setOperateTime(entity.getCreateTime()); //操作时间
		//feeEntity.setDeliveryid(entity);
		//feeEntity.(feeEntity.getCreateTime());
		feeEntity.setOutstockNo(entity.getOutstockNo());		// 出库单号
		feeEntity.setWarehouseCode(entity.getWarehouseCode());	// 仓库ID
		feeEntity.setCustomerid(entity.getCustomerid());		// 商家ID
		feeEntity.setCustomerName(entity.getCustomerName());	//商家名称
		feeEntity.setDeliveryid(entity.getDeliverid());         // 宅配ID
		feeEntity.setDeliverName(entity.getDeliverName());				
		feeEntity.setCarrierid(entity.getCarrierId());   	   // 物流商ID
		feeEntity.setCarrierName(entity.getCarrierName());
		feeEntity.setWaybillNo(entity.getWaybillNo());			// 运单号
		feeEntity.setTotalWeight(totalWeight);	//总重量	
		feeEntity.setToProvinceName(entity.getReceiveProvinceId());// 目的省
		feeEntity.setToCityName(entity.getReceiveCityId());			// 目的市			
		feeEntity.setToDistrictName(entity.getReceiveDistrictId());// 目的区
		feeEntity.setExternalNo(entity.getExternalNo());        //外部单号
		feeEntity.setAcceptTime(entity.getAcceptTime());        //揽收时间
		feeEntity.setSignTime(entity.getSignTime());
		feeEntity.setSubjectType("DISPATCH_SUBJECT_TYPE");
		feeEntity.setSubjectCode(subjectID);
		feeEntity.setStatus("0");
		feeEntity.setDelFlag("0");
		feeEntity.setAmount(0.0d);
		feeEntity.setFeesNo(entity.getFeesNo());
		feeEntity.setTemperatureType(entity.getTemperatureTypeCode());
		return feeEntity;
		
	}
	
	public void handBizDispatch(List<PriceOutMainDispatchEntity> priceList,BizDispatchBillPayEntity bizEntity){
		//判断该地址是否存在于报价中，为空时则为空
		//获取此时业务数据的市区
		String bizCity=bizEntity.getReceiveCityId();
		String bizArea=bizEntity.getReceiveDistrictId();
		
		boolean city=false;
		boolean area=false;
	
		for(PriceOutMainDispatchEntity mainDispatchEntity : priceList){
			//获取到此时的仓库
			if(!mainDispatchEntity.getStartWarehouseId().equals(bizEntity.getWarehouseCode())){
				continue;
			}
			
			//获取报价此时的市ID
			String dispatchCityId=mainDispatchEntity.getCityId();
			//获取报价此时的区ID
			String dispatchAreaId=mainDispatchEntity.getAreaId();

			//业务数据市县区都不为空情况
			if(StringUtils.isNotBlank(bizCity) && StringUtils.isNotBlank(bizArea)){
				//取此时报价中市区不为空的情况
				if(StringUtils.isNotBlank(dispatchCityId) && StringUtils.isNotBlank(dispatchAreaId)){
					//直接判断省市是否相等
					if(dispatchAreaId.trim().equals(bizArea) && dispatchCityId.trim().equals(bizCity)){
						area=true;		
						city=true;
						break;					
					}
				}
				
				//再判断市
				//此时取出报价区不为空的
				if(StringUtils.isNotBlank(dispatchCityId) && StringUtils.isBlank(dispatchAreaId)){
					if(dispatchCityId.trim().equals(bizCity)){
						city=true;
					}
				}
			}
								
			//业务数据中市不为空，县为空的情况
			if(StringUtils.isNotBlank(bizCity) && StringUtils.isBlank(bizArea)){
				//此时取报价中市不为空，县为空的情况
				if(StringUtils.isNotBlank(dispatchCityId) && StringUtils.isBlank(dispatchAreaId)){
					if(dispatchCityId.trim().equals(bizEntity.getReceiveCityId().trim())){
						city=true;
						break;
					}
				}
			}
		}
		
	
		if(city==false){
			bizEntity.setReceiveCityId("");
		}
		
		if(area==false){
			bizEntity.setReceiveDistrictId("");
		}
				
	}
	
	*/
