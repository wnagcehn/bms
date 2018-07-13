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
import com.jiuyescm.bms.base.address.service.IPubAddressService;
import com.jiuyescm.bms.base.dictionary.entity.SystemCodeEntity;
import com.jiuyescm.bms.base.dictionary.service.ISystemCodeService;
import com.jiuyescm.bms.biz.dispatch.entity.BizDispatchBillEntity;
import com.jiuyescm.bms.biz.storage.entity.BizOutstockPackmaterialEntity;
import com.jiuyescm.bms.biz.transport.entity.BizGanxianWayBillEntity;
import com.jiuyescm.bms.chargerule.receiverule.entity.BillRuleReceiveEntity;
import com.jiuyescm.bms.chargerule.receiverule.service.IReceiveRuleService;
import com.jiuyescm.bms.common.entity.CalculateVo;
import com.jiuyescm.bms.common.enumtype.TemplateTypeEnum;
import com.jiuyescm.bms.common.log.service.IBmsErrorLogInfoService;
import com.jiuyescm.bms.fees.calculate.service.IFeesCalculateService;
import com.jiuyescm.bms.quotation.contract.entity.PriceContractItemEntity;
import com.jiuyescm.bms.quotation.contract.service.IPriceContractService;
import com.jiuyescm.bms.quotation.dispatch.entity.PriceDispatchTemplateEntity;
import com.jiuyescm.bms.quotation.dispatch.entity.vo.PriceMainDispatchEntity;
import com.jiuyescm.bms.quotation.dispatch.service.IPriceDispatchService;
import com.jiuyescm.bms.quotation.dispatch.service.IPriceDispatchTemplateService;
import com.jiuyescm.bms.quotation.storage.entity.PriceGeneralQuotationEntity;
import com.jiuyescm.bms.quotation.storage.entity.PriceMaterialQuotationEntity;
import com.jiuyescm.bms.quotation.storage.entity.PriceStepQuotationEntity;
import com.jiuyescm.bms.quotation.storage.service.IPriceGeneralQuotationService;
import com.jiuyescm.bms.quotation.storage.service.IPriceMaterialQuotationService;
import com.jiuyescm.bms.quotation.storage.service.IPriceStepQuotationService;
import com.jiuyescm.bms.quotation.storage.vo.PriceGeneralQuotationVo;
import com.jiuyescm.bms.quotation.transport.entity.PriceTransportLineEntity;
import com.jiuyescm.bms.quotation.transport.entity.PriceTransportLineRangeEntity;
import com.jiuyescm.bms.quotation.transport.entity.PriceTransportTemplateEntity;
import com.jiuyescm.bms.quotation.transport.entity.vo.GenericTemplateVo;
import com.jiuyescm.bms.quotation.transport.service.IPriceTransportLineRangeService;
import com.jiuyescm.bms.quotation.transport.service.IPriceTransportLineService;
import com.jiuyescm.bms.quotation.transport.service.IPriceTransportTemplateService;
import com.jiuyescm.bms.trial.storage.service.IStorageDroolsService;
import com.jiuyescm.mdm.customer.api.IAddressService;
import com.jiuyescm.mdm.warehouse.api.IWarehouseService;
import com.jiuyescm.mdm.warehouse.vo.WarehouseVo;


@Service("feesCalculateServiceImpl")
public class FeesCalculateServiceImpl implements IFeesCalculateService {

	private static final Logger logger = Logger.getLogger(FeesCalculateServiceImpl.class.getName());

	@Resource private IReceiveRuleService receiveRuleService;
	@Resource private IStorageDroolsService orderService;
	@Resource private ISystemCodeService systemCodeService;
	@Resource private IPriceContractService priceContractService;
	// 运输相关
	@Resource private IPriceTransportTemplateService priceTransportTemplateService;
	@Resource private IPriceTransportLineService priceTransportLineService;
	@Resource private IPriceTransportLineRangeService priceTransportLineRangeService;
	// 配送相关
	@Resource private IPriceDispatchTemplateService priceDispatchTemplateService;
	@Resource private IPriceDispatchService priceDispatchService;
	@Resource private IPriceStepQuotationService stepService;
	@Resource private IPriceGeneralQuotationService generalService;
	@Resource private IPriceMaterialQuotationService materialService;	
	@Resource private IPubAddressService pubAddressService;	
	@Resource private IAddressService addressService;
	@Autowired private IWarehouseService warehouseService;
	@Resource
	private IBmsErrorLogInfoService bmsErrorLogInfoService;
	private static final String CONTRACTCODE="contractCode";
	private static final String SUBJECTID="subjectId";

	@Override
	public CalculateVo calculate(CalculateVo vo) {
		
		vo.setIsCalculate(false);
		// *************************计算对象必须要求各项数据*************************
		if (vo == null || StringUtils.isEmpty(vo.getSubjectId())|| StringUtils.isEmpty(vo.getBizTypeCode()) || vo.getObj() == null) {
			return getMsgVo(vo, false, "计算数据有缺失【业务类型，费用科目，合同编码，业务数据均不能为空】");
		}

		// ******************************查询规则******************************
		if (StringUtils.isEmpty(vo.getContractCode())&& StringUtils.isEmpty(vo.getMobanCode())) {
			return getMsgVo(vo, false, "计算数据有缺失【合同编号，模板编号】不能同时为空");
		}
		
		BillRuleReceiveEntity ruleEntity = null;
		Map<String, Object> ruleParam = new HashMap<String, Object>();
		ruleParam.put(CONTRACTCODE, vo.getContractCode());//设置合同 不要再删除了
		try {
			ruleParam.put("bizTypeCode", vo.getBizTypeCode());
			ruleParam.put(SUBJECTID, vo.getSubjectId());
			logger.info(String.format("rpc-contractCode【%s】 ,bizTypeCode【%s】,subjectId【%s】", 
					vo.getContractCode(),vo.getBizTypeCode(),vo.getSubjectId()));
			ruleEntity = receiveRuleService.queryByCustomer(ruleParam);		
		} catch (Exception ex) {
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
				case "STORAGE": // 存储费报价
					quateEntity = storageCalcu(vo, ruleParam,ruleEntity);
					break;
				case "TRANSPORT": // 运输费报价
					quateEntity = transportCalcu(vo);
					break;
				case "DISPATCH": // 配送费报价
					quateEntity = DispatchCalcu(vo,ruleEntity);
					break;
				default:
					return getMsgVo(vo, false, "业务类型【" + vo.getBizTypeCode() + "】不存在...");
			}
		} catch (Exception ex) {
			logger.error("费用计算异常-- ", ex);
			return getMsgVo(vo, false, "费用计算异常-- " + ex.getMessage());
		}
		
		// ******************************费用计算******************************
		if (quateEntity == null) {
			return getMsgVo(vo, false,"获取费用报价失败--业务类型【" +vo.getBizTypeCode() + "】 费用科目【" + vo.getSubjectId()+ "】合同编码【" + vo.getContractCode() + "】");
		}
		try {
			//未计算过的才会进计费规则算		
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
		} 
		catch (Exception ex) {
			String str = "执行规则异常--业务类型【" + vo.getBizTypeCode() +"】 费用科目【" + vo.getSubjectId() + "】合同编码【" + vo.getContractCode()+ "】";
			logger.error(str, ex);
			return getMsgVo(vo, false, str);
		}
	}

	   // 仓储费报价
       private Object storageCalcu(CalculateVo vo,Map<String, Object> param,BillRuleReceiveEntity ruleEntity){
    	   try {
    		 //查询直接走代码的计费规则
    		 List<SystemCodeEntity> systemCodeList = systemCodeService.findEnumList("RULE_CALCULATED");
    		 //如果是不进规则引擎的直接代码中计算，若异常了直接走原路径				
			 boolean isExistRule=false;
			 for(SystemCodeEntity systemCodeEntity:systemCodeList){
				if(systemCodeEntity.getExtattr1().equals(ruleEntity.getQuotationNo())){
					isExistRule=true;
					break;
				}
			 }
    		 
			 if("wh_material_use".equals(vo.getSubjectId())){//耗材报价
				logger.info("耗材出库费用进入代码计算");
				GenericTemplateVo general = new GenericTemplateVo();
							
				if(systemCodeList!=null && systemCodeList.size()>0 && ruleEntity!=null && isExistRule==true){
					 Map<String,Object> aCondition=new HashMap<String,Object>();
					 aCondition.put(CONTRACTCODE, param.get(CONTRACTCODE));
					 BizOutstockPackmaterialEntity entity=(BizOutstockPackmaterialEntity)vo.getObj();
					 aCondition.put("materialCode", entity.getConsumerMaterialCode());
					 aCondition.put("warehouseId", entity.getWarehouseCode());
					 //此时业务数据对应的报价
					 logger.info("查询输入参数"+aCondition);
					 PriceMaterialQuotationEntity priceMaterialQuotationEntity = materialService.queryOneMaterial(aCondition);
					 //判断当前业务数据是干冰还是其他
					 logger.info("查询返回结果"+JSON.toJSON(priceMaterialQuotationEntity));
					 if(priceMaterialQuotationEntity!=null && StringUtils.isNotBlank(entity.getConsumerMaterialCode())){
						 //此时为干冰
						 if(entity.getConsumerMaterialCode().endsWith("-GB")){
							 logger.info("进入干冰计算");
							 vo.setPrice(new BigDecimal(priceMaterialQuotationEntity.getUnitPrice()*entity.getWeight()).setScale(4,BigDecimal.ROUND_HALF_UP));
						 }else{
							 logger.info("进入其他耗材计算");
							 vo.setPrice(new BigDecimal(priceMaterialQuotationEntity.getUnitPrice()*entity.getNum()).setScale(4,BigDecimal.ROUND_HALF_UP));
						 }
						 vo.setUnitPrice(priceMaterialQuotationEntity.getUnitPrice());
						 vo.setExtter1(TemplateTypeEnum.COMMON.getDesc());
					 }else{
						 /**********未找到符合条件的报价，走标准标价**********/
						 aCondition=new HashMap<String,Object>();
						 aCondition.put("storageTemplateType", "STORAGE_MATERIAL_PRICE_TEMPLATE");//耗材
						 aCondition.put("materialCode", entity.getConsumerMaterialCode());
						 aCondition.put("warehouseId", entity.getWarehouseCode());
						 logger.info("查询标准报价输入参数"+aCondition);
						 List<PriceMaterialQuotationEntity> materialQuotationList = 
								 materialService.queryStandardMaterial(aCondition);
						 vo.setExtter1(TemplateTypeEnum.STANDARD.getDesc());
						 if(null != materialQuotationList && materialQuotationList.size()>0
								 && StringUtils.isNotBlank(entity.getConsumerMaterialCode())){
							 priceMaterialQuotationEntity = materialQuotationList.get(0);
							 if(entity.getConsumerMaterialCode().endsWith("-GB")){
								 logger.info("标准报价,进入干冰计算");
								 vo.setPrice(new BigDecimal(priceMaterialQuotationEntity.getUnitPrice()*entity.getWeight()).setScale(4,BigDecimal.ROUND_HALF_UP));
							 }else{
								 logger.info("标准报价,进入其他耗材计算");
								 vo.setPrice(new BigDecimal(priceMaterialQuotationEntity.getUnitPrice()*entity.getNum()).setScale(4,BigDecimal.ROUND_HALF_UP));
							 }
							 vo.setUnitPrice(priceMaterialQuotationEntity.getUnitPrice());
						}else {
							 vo.setSuccess(false);
							 vo.setMsg("报价缺失");
						}
					 }
					
					 vo.setIsCalculate(true);
					 return vo;
		
				}else{
					logger.info("耗材出库进入计费规则计算");
					List<PriceMaterialQuotationEntity> list = materialService.queryStepMaterial(param);
					if(null == list || list.size() <= 0) {
						logger.info( String.format("合同编号【%s】下的不存在费用科目为【%s】的仓储报价,通过标准报价计算。。。", vo.getContractCode(), vo.getSubjectId()));
						 /**********未找到符合条件的报价，走标准标价**********/
						Map<String, Object> aCondition = new HashMap<String,Object>();
						aCondition.put("storageTemplateType", "STORAGE_MATERIAL_PRICE_TEMPLATE");//耗材
						list = materialService.queryStandardMaterial(aCondition);
						vo.setExtter1(TemplateTypeEnum.STANDARD.getDesc());
						if(null == list || list.size() <= 0){
							logger.info( String.format("合同编号【%s】下的不存在费用科目为【%s】的仓储标准报价,请检查!", vo.getContractCode(), vo.getSubjectId()));
							return null;
						}
					}else {
						vo.setExtter1(TemplateTypeEnum.COMMON.getDesc());
					}
					general.setChild(list);
					return general;
				}
			}else{//其他
				//逻辑是查上层的对象，看是否是阶梯，如果不是则直接返回，是阶梯则返回阶梯对象
				List<PriceGeneralQuotationEntity>  genrealList = generalService.queryPriceGeneral(param);
				if(null == genrealList || genrealList.size() <= 0){
					logger.info( String.format("合同编号【%s】下的不存在费用科目为【%s】的仓储报价,通过标准报价计算。。。", vo.getContractCode(), vo.getSubjectId()));
					 /**********未找到符合条件的报价，走标准标价**********/
					genrealList = generalService.queryPriceStandardGeneral(param);
					vo.setExtter1(TemplateTypeEnum.STANDARD.getDesc());
					if(null == genrealList || genrealList.size() <= 0){
						logger.info( String.format("合同编号【%s】下的不存在费用科目为【%s】的仓储标准报价,请检查!", vo.getContractCode(), vo.getSubjectId()));
						return null;
					}
				}else {
					vo.setExtter1(TemplateTypeEnum.COMMON.getDesc());
				}
				
				if("PRICE_TYPE_STEP".equals(genrealList.get(0).getPriceType()))
				{   //如果是阶梯报价则查询阶梯数据
					PriceGeneralQuotationVo general = new  PriceGeneralQuotationVo();
					List<PriceStepQuotationEntity> list = stepService.queryPriceStep(param);
					if(null == list || list.size() <= 0) {
						logger.info(String.format("合同编号【%s】下的不存在费用科目为【%s】的仓储报价,通过标准报价计算。。。", vo.getContractCode(), vo.getSubjectId()));
						 /**********未找到符合条件的报价，走标准标价**********/
						list = stepService.queryPriceStandardStep(param);
						vo.setExtter1(TemplateTypeEnum.STANDARD.getDesc());
						if(null == list || list.size() <= 0) {
							logger.info(String.format("合同编号【%s】下的不存在费用科目为【%s】的仓储标准报价,请检查!", vo.getContractCode(), vo.getSubjectId()));
							return null;
						}
					}else {
						vo.setExtter1(TemplateTypeEnum.COMMON.getDesc());
					}
					general.setChild(list);
					return general;
				}else{		
					//如果业务类型为订单操作费
					if("wh_b2c_work".equals(vo.getSubjectId())){
						logger.info("订单操作费进入代码计算");					
						if(systemCodeList!=null && systemCodeList.size()>0 && ruleEntity!=null && isExistRule){
							PriceGeneralQuotationEntity price=genrealList.get(0);
							if(price!=null){
								vo.setPrice(new BigDecimal(price.getUnitPrice()).setScale(2,BigDecimal.ROUND_HALF_UP));
								vo.setUnitPrice(price.getUnitPrice());
								vo.setSuccess(true);
							}else{
								vo.setSuccess(false);
								vo.setMsg("报价缺失");
							}							
							vo.setIsCalculate(true);
							return vo;					
						}else{
							return genrealList.get(0);
						}
					}else{
						return genrealList.get(0);
					}									
				}
			}
    	 } catch (Exception e) {
    		 logger.info("系统异常，仓储计算失败",e);
    		 return getMsgVo(vo, true, "系统异常，仓储计算失败。。。");
   		}
		
	}

	// 运输费报价
	private Object transportCalcu(CalculateVo vo) {
		String mobanCode = vo.getMobanCode();
		if(StringUtils.isEmpty(mobanCode) && StringUtils.isNotEmpty(vo.getContractCode())){
			//判断参数中费用科目是否在运输类中
			Map<String, String> transportSubjectTypeMap = this.getTransportSubjectTypeMap();
			if (StringUtils.isNotEmpty(vo.getSubjectId()) && !transportSubjectTypeMap.containsKey(vo.getSubjectId())) {
				logger.info(String.format("运输类型的费用科目中不包含【%s】的科目.",vo.getSubjectId()));
				return null;
			}
			// 查询合同对应的运输报价中对应费用科目的报价.(也可以是对应合同下的所有报价)
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("templateTypeCode", "TRANSPORT_FEE");// 运输
			param.put(CONTRACTCODE, vo.getContractCode());
			param.put(SUBJECTID, "TRANSPORT_FEE");//签合同的时候为运输费，具体业务类型在线路上面区分
			List<PriceContractItemEntity> contractItem = priceContractService.findAllTransportContractItem(param);
			logger.info("合同信息》》》》"+JSONObject.toJSONString(param));
			if (contractItem != null) {
				if (contractItem.size() == 0) {
					logger.info(String.format("合同编号【%s】下的不存在费用科目为【%s】的运输报价,请检查!",vo.getContractCode(), vo.getSubjectId()));
					return null;
				} else if (contractItem.size() == 1) {
					PriceContractItemEntity item = contractItem.get(0);
					if (item != null && StringUtils.isNotEmpty(item.getTemplateId())) {
						mobanCode = item.getTemplateId();
					}
				} else {
					logger.info(String.format("合同编号【%s】下的费用科目为【%s】的运输报价存在【%s】个,无法确定使用哪个报价,请检查!",
							vo.getContractCode(), vo.getSubjectId(), contractItem.size()));
					return null;
				}
			}else {
				logger.info(String.format("合同编号【%s】下的不存在费用科目为【%s】的运输报价,请检查!",vo.getContractCode(), vo.getSubjectId()));
				return null;
			}
		}
		// 以下统一使用模板编号
		if(StringUtils.isNotEmpty(mobanCode)){
			vo.setMobanCode(mobanCode);
			return queryTransportTemplate(vo, mobanCode);
		}
		return null;
	}

	// 配送费报价
	private Object DispatchCalcu(CalculateVo vo,BillRuleReceiveEntity ruleEntity) {
		try {
			String mobanCode=vo.getMobanCode();
			if(StringUtils.isEmpty(mobanCode) && StringUtils.isNotEmpty(vo.getContractCode())){
				// 如果传入的模板编号为空，使用合同找出模板编号,再用模板编号进行操作
				// 查询合同对应的运输报价中对应费用科目的报价.(也可以是对应合同下的所有报价)
				Map<String, Object> param = new HashMap<String, Object>();
				param.put(CONTRACTCODE, vo.getContractCode());
				param.put(SUBJECTID, vo.getSubjectId());
				List<PriceContractItemEntity> contractItem =priceContractService.findAllDispatchContractItem(param);
				if (contractItem != null) {
					if (contractItem.size() == 0) {
						logger.info(String.format("合同编号【%s】下的不存在费用科目为【%s】的配送报价,请检查!",vo.getContractCode(), vo.getSubjectId()));
						return null;
		
					} else if (contractItem.size() == 1) {
						PriceContractItemEntity item = contractItem.get(0);
						if (item != null && StringUtils.isNotEmpty(item.getTemplateId())) {
							mobanCode=item.getTemplateId();
							//return this.queryDispatchTemplate(item);
						}
					} else {
						logger.info(String.format("合同编号【%s】下的费用科目为【%s】的配送报价存在【%s】个,无法确定使用哪个报价,请检查!",vo.getContractCode(), vo.getSubjectId(),contractItem.size()));
						return null;
					}
				} else {
					logger.info(String.format("合同编号【%s】下的不存在费用科目为【%s】的配送报价,请检查!",vo.getContractCode(), vo.getSubjectId()));
					return null;
				}
			}
		
			// 以下统一使用模板编号
			if(StringUtils.isNotEmpty(mobanCode)){
				vo.setMobanCode(mobanCode);
				return queryDispatchTemplate(mobanCode,vo,ruleEntity);
			}
			return null;
		} catch (Exception e) {
			logger.info("系统异常，配送计算失败:{0}",e);
			return getMsgVo(vo, true, "系统异常，配送计算失败。。。");
		}
		
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
			logger.info(msg);
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

	/**
	 * 查询运输报价模版
	 * @param vo
	 * @param mobanCode
	 * @return
	 */
	private PriceTransportTemplateEntity queryTransportTemplate(CalculateVo vo, String mobanCode) {
		BizGanxianWayBillEntity bizData = (BizGanxianWayBillEntity)vo.getObj();
		Map<String, Object> condition = new HashMap<String, Object>();
		condition.put("templateTypeCode", "TRANSPORT_FEE");// 运输
		condition.put("templateCode", mobanCode);// 选择的运输模板编码
		condition.put("delFlag", "0");

		PriceTransportTemplateEntity transportTemplate = null;
		logger.info("运输模版》》》"+JSONObject.toJSONString(condition));
		PageInfo<PriceTransportTemplateEntity> templatePageInfo = priceTransportTemplateService.query(condition, 0, Integer.MAX_VALUE);
		if (templatePageInfo != null && templatePageInfo.getList() != null && templatePageInfo.getList().size() > 0) {
			transportTemplate = templatePageInfo.getList().get(0);
			vo.setExtter1(TemplateTypeEnum.COMMON.getDesc());
		}else {
			 /**********未找到符合条件的报价，走标准标价**********/
			condition = new HashMap<String, Object>();
			condition.put("templateTypeCode", "TRANSPORT_FEE");// 运输
			condition.put("templateType", TemplateTypeEnum.STANDARD.getCode());
			condition.put("delFlag", "0");
			templatePageInfo = priceTransportTemplateService.query(condition, 0, Integer.MAX_VALUE);
			vo.setExtter1(TemplateTypeEnum.STANDARD.getDesc());
			if (templatePageInfo != null && templatePageInfo.getList() != null && templatePageInfo.getList().size() > 0) {
				transportTemplate = templatePageInfo.getList().get(0);
			}
		}

		if (transportTemplate == null || transportTemplate.getId() == null) {
			return transportTemplate;
		}
		
		if (vo == null || vo.getObj() == null) {
			return transportTemplate;
		}

		//根据业务类型匹配路线报价参数
		Map<String, Object> lineParam = handTransportBizTypeParam(transportTemplate.getId(), bizData);
		logger.info("查询商家路线报价参数>>>>" + JSONObject.toJSONString(lineParam));
		List<PriceTransportLineEntity> passLineList = priceTransportLineService.query(lineParam);
		if (passLineList == null || passLineList.size() <= 0) {
			/**********未找到符合条件的报价，走标准标价**********/
			condition = new HashMap<String, Object>();
			condition.put("templateTypeCode", "TRANSPORT_FEE");// 运输
			condition.put("templateType", TemplateTypeEnum.STANDARD.getCode());
			condition.put("delFlag", "0");
			templatePageInfo = priceTransportTemplateService.query(condition, 0, Integer.MAX_VALUE);
			vo.setExtter1(TemplateTypeEnum.STANDARD.getDesc());
			if (templatePageInfo != null && templatePageInfo.getList() != null && templatePageInfo.getList().size() > 0) {
				transportTemplate = templatePageInfo.getList().get(0);
				if (transportTemplate == null || transportTemplate.getId() == null) {
					return transportTemplate;
				}else {
					//根据业务类型匹配路线报价参数
					lineParam = handTransportBizTypeParam(transportTemplate.getId(), bizData);
					logger.info("查询商家路线报价参数>>>>" + JSONObject.toJSONString(lineParam));
					passLineList = priceTransportLineService.query(lineParam);
				}
			}
		}
		
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
			}
			transportTemplate.setChild(passLineList);
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
	

		return transportTemplate;
	}
	
	/**
	 * 处理运输根据业务类型匹配路线报价参数
	 * @param param
	 * @return
	 */
	private Map<String, Object> handTransportBizTypeParam(Long templateId, BizGanxianWayBillEntity bizData){
		Map<String, Object> lineParam = new HashMap<String, Object>();
		lineParam.put("templateId", templateId);
		lineParam.put("delFlag", "0");
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
	
	/**
	 * 使用合同编号查询配送报价
	 * 
	 * @param contractItem
	 * @return
	 */
	private PriceDispatchTemplateEntity queryDispatchTemplate(String mobanId,CalculateVo vo,BillRuleReceiveEntity ruleEntity) {
		Map<String, Object> condition = new HashMap<String, Object>();
		condition.put("templateCode", mobanId);// 获取此时的模板id
		logger.info("此时的模板"+mobanId);
		// 查询出对应的模板
		PageInfo<PriceDispatchTemplateEntity> pageInfo = priceDispatchTemplateService.query(condition,0, Integer.MAX_VALUE);
		PriceDispatchTemplateEntity priceDispatchTemplateEntity = pageInfo.getList().get(0);
		//获取此时所有的仓库
		Map<String,String> warehouseMap= getPubWareHouse();
		//获取此时的业务数据
		BizDispatchBillEntity bizEntity = (BizDispatchBillEntity)vo.getObj();
		
		// 查询出该模板下的所有的报价模板信息
		Map<String, Object> mainCondition = new HashMap<String, Object>();
		mainCondition.put("number", priceDispatchTemplateEntity.getId());
		mainCondition.put("startWarehouseId", bizEntity.getWarehouseCode());
		mainCondition.put("provinceId", bizEntity.getReceiveProvinceId());
		
		//获取此时所有的报价
		PageInfo<PriceMainDispatchEntity> tmpPageInfo = priceDispatchService.queryAll(mainCondition, 0, Integer.MAX_VALUE);
		List<PriceMainDispatchEntity> priceList=new ArrayList<>();
		if(null != tmpPageInfo && null != tmpPageInfo.getList() && tmpPageInfo.getList().size() > 0){
			priceList=tmpPageInfo.getList();
			for(int i=0;i<priceList.size();i++){
				PriceMainDispatchEntity entity=priceList.get(i);
				
				String warseHouseId=entity.getStartWarehouseId();
				String warseHouseName=warehouseMap.get(warseHouseId);
				entity.setStartWarehouseName(warseHouseName);
			}
			vo.setExtter1(TemplateTypeEnum.COMMON.getDesc());
		}else {
			 /**********未找到符合条件的报价，走标准标价**********/
			Map<String, Object> standardCondition = new HashMap<String, Object>();
			standardCondition.put("templateType", TemplateTypeEnum.STANDARD.getCode());
			standardCondition.put("deliver", vo.getSubjectId());//物流商
			PageInfo<PriceDispatchTemplateEntity> standPageInfo = priceDispatchTemplateService.query(standardCondition,0, Integer.MAX_VALUE);
			vo.setExtter1(TemplateTypeEnum.STANDARD.getDesc());
			if (null != standPageInfo && null != standPageInfo.getList() && standPageInfo.getList().size() > 0) {
				priceDispatchTemplateEntity = standPageInfo.getList().get(0);
				//标准报价阶梯
				mainCondition = new HashMap<String, Object>();
				mainCondition.put("number", priceDispatchTemplateEntity.getId());
				mainCondition.put("startWarehouseId", bizEntity.getWarehouseCode());
				mainCondition.put("provinceId", bizEntity.getReceiveProvinceId());
				tmpPageInfo = priceDispatchService.queryAll(mainCondition, 0, Integer.MAX_VALUE);
				if(tmpPageInfo != null && tmpPageInfo.getList() != null && tmpPageInfo.getList().size() > 0){
					priceList=tmpPageInfo.getList();
					for(int i=0;i<priceList.size();i++){
						PriceMainDispatchEntity entity=priceList.get(i);
						
						String warseHouseId=entity.getStartWarehouseId();
						String warseHouseName=warehouseMap.get(warseHouseId);
						entity.setStartWarehouseName(warseHouseName);
					}
				}
			}
		}
		
		//判断该地址是否存在于报价中，为空时则为空
		//获取此时业务数据的市区
		String bizCity=bizEntity.getReceiveCityId();
		String bizArea=bizEntity.getReceiveDistrictId();
		
		boolean city=false;
		boolean area=false;
		for(PriceMainDispatchEntity mainDispatchEntity : priceList){				
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
					//此时先判断区
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
				aCondition.put("number", priceDispatchTemplateEntity.getId());
				aCondition.put("startWarehouseId", bizEntity.getWarehouseCode());
				aCondition.put("provinceId", bizEntity.getReceiveProvinceId());
				aCondition.put("cityId", bizEntity.getReceiveCityId());
				aCondition.put("areaId", bizEntity.getReceiveDistrictId());
				logger.info("查询条件="+JSONObject.toJSONString(aCondition));
				PriceMainDispatchEntity p=priceDispatchService.queryOne(aCondition);
				if(p!=null){
					//计费规则，首重加续重
					vo.setPrice(new BigDecimal(p.getFirstWeight()<bizEntity.getTotalWeight()?p.getFirstWeightPrice()+p.getContinuedPrice()*((bizEntity.getTotalWeight()-p.getFirstWeight())/p.getContinuedWeight()):p.getFirstWeightPrice()).setScale(2,BigDecimal.ROUND_HALF_UP));
					bizEntity.setDispatchId(p.getId().toString());
					vo.setSuccess(true);
				}else {
					logger.info("==================报价查询为空==================");
					vo.setSuccess(false);
					vo.setMsg("报价缺失");
				}
				vo.setIsCalculate(true);	
			}else{
				logger.info("==================进入计费规则计算==================");
				priceDispatchTemplateEntity.setChild(priceList);
			}				
		}else{
			//此时的报价list
			logger.info("===================进入计费规则计算====================");
			priceDispatchTemplateEntity.setChild(priceList);
		}
		
		
		logger.info("判断后此时的业务数据"+JSON.toJSON(bizEntity));
		return priceDispatchTemplateEntity;
	}

	
	/**
	 * 查询所有的电仓仓库
	 * @return
	 */
	@DataProvider
	public Map<String, String> getPubWareHouse(){
		List<WarehouseVo> warehouseVos = warehouseService.queryAllWarehouse();
		Map<String, String> map = new LinkedHashMap<String,String>();
		for (WarehouseVo warehouseVo : warehouseVos) {
			map.put(warehouseVo.getWarehouseid(), warehouseVo.getWarehousename());
		}
		return map;
	}

}
