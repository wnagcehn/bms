package com.jiuyescm.bms.fees.calculate.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bstek.dorado.annotation.DataProvider;
import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.base.dictionary.entity.SystemCodeEntity;
import com.jiuyescm.bms.base.dictionary.service.ISystemCodeService;
import com.jiuyescm.bms.biz.dispatch.entity.BizDispatchBillPayEntity;
import com.jiuyescm.bms.biz.dispatch.entity.BizTihuoBillEntity;
import com.jiuyescm.bms.biz.transport.entity.BizGanxianRoadBillEntity;
import com.jiuyescm.bms.chargerule.payrule.entity.BillRulePayEntity;
import com.jiuyescm.bms.chargerule.payrule.service.IPayRuleService;
import com.jiuyescm.bms.common.entity.CalculateVo;
import com.jiuyescm.bms.common.enumtype.TemplateTypeEnum;
import com.jiuyescm.bms.common.log.service.IBmsErrorLogInfoService;
import com.jiuyescm.bms.fees.calculate.service.IFeesCalculatePayService;
import com.jiuyescm.bms.quotation.contract.entity.PriceContractItemEntity;
import com.jiuyescm.bms.quotation.contract.service.IPriceContractService;
import com.jiuyescm.bms.quotation.out.dispatch.entity.PriceOutDispacthTemplateEntity;
import com.jiuyescm.bms.quotation.out.dispatch.entity.PriceOutDispatchOtherDetailEntity;
import com.jiuyescm.bms.quotation.out.dispatch.entity.PriceOutDispatchOtherTemplateEntity;
import com.jiuyescm.bms.quotation.out.dispatch.entity.vo.PriceOutMainDispatchEntity;
import com.jiuyescm.bms.quotation.out.dispatch.service.IPriceOutDispatchOtherService;
import com.jiuyescm.bms.quotation.out.dispatch.service.IPriceOutDispatchOtherTemplateService;
import com.jiuyescm.bms.quotation.out.dispatch.service.IPriceOutDispatchService;
import com.jiuyescm.bms.quotation.out.dispatch.service.IPriceOutDispatchTemplateService;
import com.jiuyescm.bms.quotation.out.transport.entity.PriceTransportPayLineEntity;
import com.jiuyescm.bms.quotation.out.transport.entity.PriceTransportPayLineRangeEntity;
import com.jiuyescm.bms.quotation.out.transport.entity.PriceTransportPayTemplateEntity;
import com.jiuyescm.bms.quotation.out.transport.service.IPriceTransportPayLineRangeService;
import com.jiuyescm.bms.quotation.out.transport.service.IPriceTransportPayLineService;
import com.jiuyescm.bms.quotation.out.transport.service.IPriceTransportPayTemplateService;
import com.jiuyescm.bms.quotation.storage.service.IPriceGeneralQuotationService;
import com.jiuyescm.bms.quotation.storage.service.IPriceMaterialQuotationService;
import com.jiuyescm.bms.quotation.storage.service.IPriceStepQuotationService;
import com.jiuyescm.bms.trial.storage.service.IStorageDroolsService;
/*import com.jiuyescm.mdm.warehouse.api.IWarehouseService;
import com.jiuyescm.mdm.warehouse.vo.WarehouseVo;*/

@Service("feesCalculatePayServiceImpl")
public class FeesCalculatePayServiceImpl implements IFeesCalculatePayService {

	private static final Logger logger = Logger.getLogger

	(FeesCalculatePayServiceImpl.class.getName());

	@Resource
	private IPayRuleService payRuleService;
	@Resource
	private IStorageDroolsService orderService;
	@Resource
	private ISystemCodeService systemCodeService;
	@Resource
	private IPriceContractService priceContractService;
	@Resource
	private IBmsErrorLogInfoService bmsErrorLogInfoService;

	// 应付运输相关
	@Resource
	private IPriceTransportPayTemplateService priceTransportPayTemplateService;
	@Resource
	private IPriceTransportPayLineService priceTransportPayLineService;
	@Resource
	private IPriceTransportPayLineRangeService priceTransportPayLineRangeService;

	// 配送相关
	@Resource
	private IPriceOutDispatchTemplateService priceOutDispatchTemplateService;
	
	@Resource
	private IPriceOutDispatchOtherTemplateService priceOutDispatchOtherTemplateService;
	
	@Resource
	private IPriceOutDispatchService priceOutDispatchService;
	
	@Resource
	private IPriceOutDispatchOtherService priceOutDispatchOtherService;

	@Resource
	private IPriceStepQuotationService stepService;
	@Resource
	private IPriceGeneralQuotationService generalService;
	@Resource
	private IPriceMaterialQuotationService materialService;
	//@Autowired private IWarehouseService warehouseService;

	@Override
	public CalculateVo calculate(CalculateVo vo) {
		
		vo.setIsCalculate(false);
		// *************************计算对象必须要求各项数据*************************
		if (vo == null || StringUtils.isEmpty(vo.getSubjectId())
				|| StringUtils.isEmpty(vo.getBizTypeCode()) || vo.getObj() == null) {
			return getMsgVo(vo, false, "计算数据有缺失【业务类型，费用科目，合同编码，业务数据均不能为空】");
		}

		// ******************************查询规则******************************
		if (StringUtils.isEmpty(vo.getContractCode())
				&& StringUtils.isEmpty(vo.getMobanCode())) {
			return getMsgVo(vo, false, "计算数据有缺失【合同编号，模板编号】不能同时为空");
		}

		BillRulePayEntity ruleEntity = null;
		Map<String, Object> ruleParam = new HashMap<String, Object>();
		ruleParam.put("contractCode", vo.getContractCode());//设置合同 不要再删除了
		try {
			ruleParam.put("bizTypeCode", vo.getBizTypeCode());
			if("DISPATCH_OTHER_SUBJECT_TYPE".equals(vo.getSubjectId())){
				ruleParam.put("subjectId", vo.getExtter1());
			}else{
				ruleParam.put("subjectId", vo.getSubjectId());
			}
			logger.info(String.format("rpc-contractCode【%s】 ,bizTypeCode【%s】,subjectId【%s】", 
					vo.getContractCode(),vo.getBizTypeCode(),vo.getSubjectId()));
			ruleEntity = payRuleService.queryByCustomer(ruleParam);
		} catch (Exception ex) {
			//写入日志
			logger.error(ex);
			return getMsgVo(vo, false, "规则获取异常");
		}
		if (ruleEntity == null || StringUtils.isEmpty(ruleEntity.getRule())) {
			return getMsgVo(vo, false, "未查询到规则");
		}

		// ******************************查询报价******************************
		Object quateEntity = null;
		try {
			switch (vo.getBizTypeCode()) {
			case "TRANSPORT": // 运输费报价
				quateEntity = TransportCalcu(vo);
				break;
			case "DISPATCH": // 配送费报价
				quateEntity = DispatchCalcu(vo,ruleEntity);
				break;
			default:
				return getMsgVo(vo, false, "业务类型【" + vo.getBizTypeCode()
						+ "】不存在...");
			}
		} catch (Exception ex) {
			logger.error(ex);
			return getMsgVo(vo, false, "费用计算异常-- " + ex.getMessage());
		}

		// ******************************费用计算******************************
		if (quateEntity == null) {
			return getMsgVo(vo, false,"获取费用报价失败--业务类型【" +vo.getBizTypeCode() + "】 费用科目【" + vo.getSubjectId()+ "】合同编码【" + vo.getContractCode() + "】");
		}
		try {
			logger.info(" *******************是否代码计算- "+vo.getIsCalculate()+" ******************* ");
			
			if (vo.getIsCalculate() == true){
				logger.info(" ******************* java代码计算 ******************* ");
				vo.setQuateEntity(quateEntity);
				vo.setRuleno(ruleEntity.getQuotationNo()); //报价编号作为规则编号使用
				
			}else{
				logger.info(" ******************* 调用规则 ******************* ");
				orderService.excute(vo, quateEntity, ruleEntity.getRule());
				vo.setQuateEntity(quateEntity);
				vo.setRuleno(ruleEntity.getQuotationNo()); //报价编号作为规则编号使用
				logger.info(" ******************* 规则编号【"+vo.getRuleno()+"】 ******************* ");
			}
			if(vo.getPrice() == null){
				return getMsgVo(vo, true, "规则调用失败");
			}
			else{
				return getMsgVo(vo, true, "计算完成。。。");
			}
		} catch (Exception ex) {
			logger.error(ex);
			return getMsgVo(vo, false, "执行规则异常--业务类型【" + vo.getBizTypeCode() +"】 费用科目【" + vo.getSubjectId() + "】合同编码【" + vo.getContractCode()+ "】");
		}
	}

	// 运输费报价
	private Object TransportCalcu(CalculateVo vo) {
		String mobanCode = vo.getMobanCode();
		if(StringUtils.isEmpty(mobanCode) && StringUtils.isNotEmpty(vo.getContractCode())){
			//判断参数中费用科目是否在运输类中
			Map<String, String> transportSubjectTypeMap = this.getTransportSubjectTypeMap();
			if (StringUtils.isNotEmpty(vo.getSubjectId()) && !transportSubjectTypeMap.containsKey(vo.getSubjectId())) {
				logger.error(String.format("运输类型的费用科目中不包含【%s】的科目.",vo.getSubjectId()));
				return null;
			}
			// 查询合同对应的运输报价中对应费用科目的报价.(也可以是对应合同下的所有报价)
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("templateTypeCode", "TRANSPORT_FEE");// 运输
			param.put("contractCode", vo.getContractCode());
			param.put("subjectId", vo.getSubjectId());
			List<PriceContractItemEntity> contractItem = priceContractService.findTransportPayFeesContractItem(param);
			if (contractItem != null) {
				if (contractItem.size() == 0) {
					logger.error(String.format("合同编号【%s】下的不存在费用科目为【%s】的运输报价,请检查!",vo.getContractCode(), vo.getSubjectId()));
					return null;
				} else if (contractItem.size() == 1) {
					PriceContractItemEntity item = contractItem.get(0);
					if (item != null && StringUtils.isNotEmpty(item.getTemplateId())) {
						mobanCode = item.getTemplateId();
					}
				} else {
					logger.error(String.format("合同编号【%s】下的费用科目为【%s】的运输报价存在【%s】个,无法确定使用哪个报价,请检查!",
							vo.getContractCode(), vo.getSubjectId(), contractItem.size()));
					return null;
				}
			}else {
				logger.error(String.format("合同编号【%s】下的不存在费用科目为【%s】的运输报价,请检查!",vo.getContractCode(), vo.getSubjectId()));
				return null;
			}
		}
		// 以下统一使用模板编号
		if(StringUtils.isNotEmpty(mobanCode)){
			vo.setMobanCode(mobanCode);
			return queryTransportPayTemplate(vo, mobanCode);
		}
		return null;
	}

	// 配送费报价
	private Object DispatchCalcu(CalculateVo vo,BillRulePayEntity ruleEntity) {
		String mobanCode=vo.getMobanCode();
		if(StringUtils.isEmpty(mobanCode) && StringUtils.isNotEmpty(vo.getContractCode())){
			// 如果传入的模板编号为空，使用合同找出模板编号,再用模板编号进行操作
			// 查询合同对应的运输报价中对应费用科目的报价.(也可以是对应合同下的所有报价)
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("contractCode", vo.getContractCode());
			param.put("subjectId", vo.getSubjectId());
			List<PriceContractItemEntity> contractItem =priceContractService.findAllDispatchContractItem(param);
			if (contractItem != null) {
				if (contractItem.size() == 0) {
					logger.error(String.format("合同编号【%s】下的不存在费用科目为【%s】的配送报价,请检查!",vo.getContractCode(), vo.getSubjectId()));
					return null;

				} else if (contractItem.size() == 1) {
					PriceContractItemEntity item = contractItem.get(0);
					if (item != null && StringUtils.isNotEmpty(item.getTemplateId())) {
						mobanCode=item.getTemplateId();
						//return this.queryDispatchTemplate(item);
					}
				} else {
					logger.error(String.format("合同编号【%s】下的费用科目为【%s】的配送报价存在【%s】个,无法确定使用哪个报价,请检查!",vo.getContractCode(), vo.getSubjectId(),contractItem.size()));
					return null;
				}
			} else {
				logger.error(String.format("合同编号【%s】下的不存在费用科目为【%s】的配送报价,请检查!",vo.getContractCode(), vo.getSubjectId()));
				return null;
			}
		}

		// 以下统一使用模板编号
		if(StringUtils.isNotEmpty(mobanCode)){
			vo.setMobanCode(mobanCode);
			if("DISPATCH_OTHER_SUBJECT_TYPE".equals(vo.getSubjectId())){
				//此处为增值报价模板
				return queryDispatchOtherTemplate(mobanCode,vo,ruleEntity);
			}else{
				return queryDispatchTemplate(mobanCode,vo,ruleEntity);
			}
			
		}
		return null;
	}

	/**
	 * 返回计算结果
	 */
	private CalculateVo getMsgVo(CalculateVo vo, Boolean success, String msg) {
		if (vo == null) {
			vo = new CalculateVo();
		}
		vo.setSuccess(success);
		vo.setMsg(msg);
		if (success) {
			logger.info(msg);
		} else {
			logger.error(msg);
		}
		return vo;
	}

	/**
	 * 运输产品类型TRANSPORT_PRODUCT_TYPE： 城际专列、同城专列、电商专列、航线达 ；
	 */
	@DataProvider
	public Map<String, String> getTransportSubjectTypeMap() {
		List<SystemCodeEntity> systemCodeList = systemCodeService.findEnumList("TRANSPORT_PRODUCT_TYPE");
		Map<String, String> map = new LinkedHashMap<String, String>();
		if (systemCodeList != null && systemCodeList.size() > 0) {
			for (int i = 0; i < systemCodeList.size(); i++) {
				map.put(systemCodeList.get(i).getCode(), systemCodeList.get(i).getCodeName());
			}
		}
		return map;
	}

	//应付费用模板报价
	private PriceTransportPayTemplateEntity queryTransportPayTemplate(CalculateVo vo, String mobanCode) {
		
		Map<String, Object> condition = new HashMap<String, Object>();
		//condition.put("transportTypeCode", contractItem.getSubjectId());// 运输类型
		condition.put("templateTypeCode", "TRANSPORT_FEE");// 运输
		condition.put("templateCode", mobanCode);// 选择的运输模板编码
		condition.put("delFlag", "0");

		PriceTransportPayTemplateEntity transportPayTemplate = null;

		PageInfo<PriceTransportPayTemplateEntity> templatePageInfo = priceTransportPayTemplateService.query(condition, 0, Integer.MAX_VALUE);
		if (templatePageInfo != null && templatePageInfo.getList() != null && templatePageInfo.getList().size() > 0) {
			transportPayTemplate = templatePageInfo.getList().get(0);
		}

		if (transportPayTemplate != null && transportPayTemplate.getId() != null) {
			Map<String, Object> lineParam = new HashMap<String, Object>();
			lineParam.put("templateId", transportPayTemplate.getId());
			lineParam.put("delFlag", "0");
			
			List<PriceTransportPayLineEntity> payLineList = priceTransportPayLineService.query(lineParam);
			List<PriceTransportPayLineEntity> passPayLineList = null;
			if (payLineList != null && payLineList.size() > 0) {
				passPayLineList = new ArrayList<PriceTransportPayLineEntity>(payLineList.size());
				for (PriceTransportPayLineEntity payLine : payLineList) {
					if (vo != null && vo.getObj() != null) {
						BizGanxianRoadBillEntity bizData = (BizGanxianRoadBillEntity)vo.getObj();
						if(bizData != null){
							if(check(bizData.getSendCityId(), payLine.getFromCityId(), payLine.getFromCityName())
							   && check(bizData.getSendDistrictId(), payLine.getFromDistrictId(), payLine.getFromDistrictName())
							   && check(bizData.getReceiverCityId(), payLine.getToCityId(), payLine.getToCityName())
							   && check(bizData.getReceiverDistrictId(), payLine.getToDistrictId(), payLine.getToDistrictName())
							)
							passPayLineList.add(payLine);
						}
					}
				}
			}
			if(passPayLineList != null && passPayLineList.size()>0){
				transportPayTemplate.setChild(passPayLineList);
				for (PriceTransportPayLineEntity passLine : passPayLineList) {
					// 找到运输路线对应的梯度报价
					Map<String, Object> rangeParam = new HashMap<String,Object>();
					rangeParam.put("lineId", passLine.getId());
					rangeParam.put("delFlag", "0");
					List<PriceTransportPayLineRangeEntity> payRangeList = priceTransportPayLineRangeService.query(rangeParam);
					if (payRangeList != null && payRangeList.size() > 0) {
						passLine.setChild(payRangeList);
					}
				}
			}
		}

		return transportPayTemplate;
	}
	
	public boolean check(String bizDataId, String dataId, String dataName){
		boolean result = false;
		if(StringUtils.isNotBlank(bizDataId)){
			if(StringUtils.equalsIgnoreCase(bizDataId, dataId) || StringUtils.equalsIgnoreCase(bizDataId, dataName)){
				result = true;
			}
		}
		return result;
	}
	/**
	 * 使用合同编号查询配送报价
	 * 
	 * @param contractItem
	 * @return
	 */
	private PriceOutDispacthTemplateEntity queryDispatchTemplate(String mobanId,CalculateVo vo,BillRulePayEntity ruleEntity) {	
		Map<String, Object> condition = new HashMap<String, Object>();
		// 获取此时的模板id
		condition.put("templateCode", mobanId);
		
		// 查询出对应的模板
		PageInfo<PriceOutDispacthTemplateEntity> pageInfo = priceOutDispatchTemplateService.query(condition,0, Integer.MAX_VALUE);
		PriceOutDispacthTemplateEntity priceOutDispatchTemplateEntity = pageInfo.getList().get(0);
		//获取此时所有的仓库
		Map<String,String> warehouseMap= getPubWareHouse();
		//此时的业务数据
		BizDispatchBillPayEntity bizEntity = (BizDispatchBillPayEntity)vo.getObj();
		
		// 查询出该模板下的所有的报价模板信息
		Map<String, Object> mainCondition = new HashMap<String, Object>();
		mainCondition.put("number", priceOutDispatchTemplateEntity.getId());
		mainCondition.put("startWarehouseId", bizEntity.getWarehouseCode());
		mainCondition.put("provinceId", bizEntity.getReceiveProvinceId());
		
		PageInfo<PriceOutMainDispatchEntity> tmpPageInfo = priceOutDispatchService.queryAll(mainCondition, 0, Integer.MAX_VALUE);
		List<PriceOutMainDispatchEntity> priceList=new ArrayList<>();
		if(null != tmpPageInfo && null != tmpPageInfo.getList() && tmpPageInfo.getList().size() > 0){
			priceList=tmpPageInfo.getList();
			for(int i=0;i<priceList.size();i++){
				PriceOutMainDispatchEntity entity=priceList.get(i);		
				String warseHouseId=entity.getStartWarehouseId();
				String warseHouseName=warehouseMap.get(warseHouseId);
				entity.setStartWarehouseName(warseHouseName);
			}
		}else {
			/**********未找到符合条件的报价，走标准标价**********/
			Map<String, Object> standardCondition = new HashMap<String, Object>();
			standardCondition.put("templateType", TemplateTypeEnum.STANDARD.getCode());
			PageInfo<PriceOutDispacthTemplateEntity> standardPageInfo = priceOutDispatchTemplateService.query(standardCondition,0, Integer.MAX_VALUE);
			vo.setExtter1(TemplateTypeEnum.STANDARD.getDesc());
			if (null != standardPageInfo && null != standardPageInfo.getList() && standardPageInfo.getList().size() > 0) {
				priceOutDispatchTemplateEntity = standardPageInfo.getList().get(0);
				//标准报价阶梯
				mainCondition = new HashMap<String, Object>();
				mainCondition.put("number", priceOutDispatchTemplateEntity.getId());
				mainCondition.put("startWarehouseId", bizEntity.getWarehouseCode());
				mainCondition.put("provinceId", bizEntity.getReceiveProvinceId());
				tmpPageInfo = priceOutDispatchService.queryAll(mainCondition, 0, Integer.MAX_VALUE);
				if(null != tmpPageInfo && null != tmpPageInfo.getList()){
					priceList=tmpPageInfo.getList();
					for(int i=0;i<priceList.size();i++){
						PriceOutMainDispatchEntity entity=priceList.get(i);		
						String warseHouseId=entity.getStartWarehouseId();
						String warseHouseName=warehouseMap.get(warseHouseId);
						entity.setStartWarehouseName(warseHouseName);
					}
				}
			}
		}
				
		//判断该地址是否存在于报价中，为空时则为空
		logger.debug("判断前此时的业务数据"+JSON.toJSON(bizEntity));
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
		
		//如果是顺丰或者九曳的直接代码计算
		if("SHUNFENG_DISPATCH".equals(vo.getSubjectId()) || "JIUYE_DISPATCH".equals(vo.getSubjectId())){	
			//如果是不进规则引擎的直接代码中计算，若异常了直接走原路径
			List<SystemCodeEntity> systemCodeList = systemCodeService.findEnumList("RULE_CALCULATED");
			boolean isExistRule=false;
			for(SystemCodeEntity systemCodeEntity:systemCodeList){
				if(systemCodeEntity.getExtattr1().equals(ruleEntity.getQuotationNo())){
					isExistRule=true;
					break;
				}
			}
			
			if(systemCodeList!=null && systemCodeList.size()>0 && ruleEntity!=null && isExistRule==true){
				logger.info("====================进入代码计算==================");
				//通过仓库加地址查询对应的报价
				Map<String,Object> aCondition=new HashMap<>();
				aCondition.put("number", priceOutDispatchTemplateEntity.getId());
				aCondition.put("startWarehouseId", bizEntity.getWarehouseCode());
				aCondition.put("provinceId", bizEntity.getReceiveProvinceId());
				aCondition.put("cityId", bizEntity.getReceiveCityId());
				aCondition.put("areaId", bizEntity.getReceiveDistrictId());
				logger.info("查询条件="+JSONObject.toJSONString(aCondition));
				PriceOutMainDispatchEntity p=priceOutDispatchService.queryOne(aCondition);
				if(p!=null){
					//计费规则，首重加续重
					vo.setPrice(new BigDecimal(p.getFirstWeight()<bizEntity.getTotalWeight()?p.getFirstWeightPrice()+p.getContinuedPrice()*((bizEntity.getTotalWeight()-p.getFirstWeight())/p.getContinuedWeight()):p.getFirstWeightPrice()).setScale(2,BigDecimal.ROUND_HALF_UP));
					bizEntity.setDispatchId(p.getId().toString());
					vo.setSuccess(true);
				}else{
					logger.info("==================报价查询为空==================");
					vo.setSuccess(false);
					vo.setMsg("报价缺失");
				}			
				vo.setIsCalculate(true);	
			}else{
				logger.info("==================进入计费规则计算==================");
				priceOutDispatchTemplateEntity.setChild(priceList);			}				
		}else{
			//此时的报价list
			logger.info("===================进入计费规则计算====================");
			priceOutDispatchTemplateEntity.setChild(priceList);		
		}
		
		
		
		logger.debug("判断后此时的业务数据"+JSON.toJSON(bizEntity));
		
		
		return priceOutDispatchTemplateEntity;
	}

	
	
	
	
	
	
	
	/**
	 * 使用合同编号查询配送报价（配送提货报价）
	 * 
	 * @param contractItem
	 * @return
	 */
	private PriceOutDispatchOtherTemplateEntity queryDispatchOtherTemplate(String mobanId,CalculateVo vo,BillRulePayEntity ruleEntity) {	
		Map<String, Object> condition = new HashMap<String, Object>();
		// 获取此时的模板id
		condition.put("templateCode", mobanId);
		
		// 查询出对应的模板
		List<PriceOutDispatchOtherTemplateEntity> tempList = priceOutDispatchOtherTemplateService.queryDeliverTemplate(condition);
		PriceOutDispatchOtherTemplateEntity priceOutDispatchOtherTemplateEntity = tempList.get(0);
		// 查询出该模板下的所有的报价模板信息
		Map<String, Object> mainCondition = new HashMap<String, Object>();
		mainCondition.put("templateId", priceOutDispatchOtherTemplateEntity.getId());
		mainCondition.put("subjectCode", vo.getExtter1());
	
		PageInfo<PriceOutDispatchOtherDetailEntity> tmpPageInfo = priceOutDispatchOtherService.queryAll(mainCondition, 0, Integer.MAX_VALUE);
		
		//如果是不进规则引擎的直接代码中计算，若异常了直接走原路径
		List<SystemCodeEntity> systemCodeList = systemCodeService.findEnumList("RULE_CALCULATED");
		boolean isExistRule=false;
		for(SystemCodeEntity systemCodeEntity:systemCodeList){
			if(systemCodeEntity.getExtattr1().equals(ruleEntity.getQuotationNo())){
				isExistRule=true;
				break;
			}
		}
		
		if(systemCodeList!=null && systemCodeList.size()>0 && ruleEntity!=null && isExistRule==true){
			logger.info("====================进入代码计算==================");
			//获取所有的报价
			List<PriceOutDispatchOtherDetailEntity> priceList=tmpPageInfo.getList();
			//获取此时的业务数据
			BizTihuoBillEntity entity=(BizTihuoBillEntity)vo.getObj();
			
			if(priceList.size()>0){
				for(int i=0;i<priceList.size();i++){
					PriceOutDispatchOtherDetailEntity price=priceList.get(i);
					if(price.getLower()<entity.getTotalNum() && entity.getTotalNum()<=price.getUpper()){
						vo.setPrice(new BigDecimal(price.getUnitPrice()).setScale(2,BigDecimal.ROUND_HALF_UP));
						vo.setSuccess(true);
						break;
					}
				}
				
			}else{
				logger.info("==================报价查询为空==================");
				vo.setSuccess(false);
				vo.setMsg("报价缺失");
			}
			vo.setIsCalculate(true);
		}else{
			logger.info("====================进入规则计算==================");
			priceOutDispatchOtherTemplateEntity.setChild(tmpPageInfo.getList());
		}
				
		return priceOutDispatchOtherTemplateEntity;
	}

	
	
	/**
	 * 查询所有的电仓仓库
	 * @return
	 */
	@DataProvider
	public Map<String, String> getPubWareHouse(){
		/*List<WarehouseVo> warehouseVos = warehouseService.queryAllWarehouse();
		
		for (WarehouseVo warehouseVo : warehouseVos) {
			map.put(warehouseVo.getWarehouseid(), warehouseVo.getWarehousename());
		}*/
		Map<String, String> map = new LinkedHashMap<String,String>();
		return map;
	}

}
