package com.jiuyescm.bms.calcu.receive.storage.instock;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.jiuyescm.bms.asyn.vo.BmsCalcuTaskVo;
import com.jiuyescm.bms.calcu.base.CalcuTaskListener;
import com.jiuyescm.bms.calculate.api.IBmsCalcuService;
import com.jiuyescm.bms.calculate.vo.BmsFeesQtyVo;
import com.jiuyescm.bms.calculate.vo.CalcuContractVo;
import com.jiuyescm.bms.calculate.vo.CalcuInfoVo;
import com.jiuyescm.bms.common.enumtype.CalculateState;
import com.jiuyescm.bms.general.entity.BizAddFeeEntity;
import com.jiuyescm.bms.general.entity.BmsBizInstockInfoEntity;
import com.jiuyescm.bms.general.entity.FeesReceiveStorageEntity;
import com.jiuyescm.bms.general.service.IBmsBizInstockInfoRepository;
import com.jiuyescm.bms.general.service.IFeesReceiveStorageService;
import com.jiuyescm.bms.general.service.IPriceContractInfoService;
import com.jiuyescm.bms.general.service.IStorageQuoteFilterService;
import com.jiuyescm.bms.quotation.contract.entity.PriceContractInfoEntity;
import com.jiuyescm.bms.quotation.contract.entity.PriceContractItemEntity;
import com.jiuyescm.bms.quotation.contract.repository.imp.IPriceContractItemRepository;
import com.jiuyescm.bms.quotation.storage.entity.PriceGeneralQuotationEntity;
import com.jiuyescm.bms.quotation.storage.entity.PriceStepQuotationEntity;
import com.jiuyescm.bms.quotation.storage.repository.IPriceGeneralQuotationRepository;
import com.jiuyescm.bms.quotation.storage.repository.IPriceStepQuotationRepository;
import com.jiuyescm.bms.quotation.transport.repository.IGenericTemplateRepository;
import com.jiuyescm.bs.util.StringUtil;
import com.jiuyescm.cfm.common.JAppContext;
import com.jiuyescm.common.utils.DoubleUtil;
import com.jiuyescm.contract.quote.api.IContractQuoteInfoService;
import com.jiuyescm.contract.quote.vo.ContractBizTypeEnum;
import com.jiuyescm.contract.quote.vo.ContractQuoteQueryInfoVo;

public class InstockCalcuBase extends CalcuTaskListener<BmsBizInstockInfoEntity,FeesReceiveStorageEntity>{

	private Logger logger = LoggerFactory.getLogger(InstockCalcuBase.class);
	
	@Autowired private IPriceGeneralQuotationRepository priceGeneralQuotationRepository;
	@Autowired private IBmsBizInstockInfoRepository bmsBizInstockInfoRepository;
	@Autowired private IPriceStepQuotationRepository  repository;
	@Autowired private IStorageQuoteFilterService storageQuoteFilterService;
	@Autowired private IContractQuoteInfoService contractQuoteInfoService;
	@Autowired private IFeesReceiveStorageService feesReceiveStorageService;
	@Autowired IBmsCalcuService bmsCalcuService;
	@Autowired private IPriceContractInfoService jobPriceContractInfoService;
	@Autowired private IPriceContractItemRepository priceContractItemRepository;
	@Autowired private IGenericTemplateRepository genericTemplateRepository;
	
	
	@Override
	protected List<BmsBizInstockInfoEntity> queryBillList(Map<String, Object> map) {
		List<BmsBizInstockInfoEntity> bizList = bmsBizInstockInfoRepository.getInStockInfoList(map);
		return bizList;
	}
	
	@Override
	protected void calcuForBms(BmsCalcuTaskVo vo,BmsBizInstockInfoEntity entity,FeesReceiveStorageEntity feeEntity){
		feeEntity.setSubjectCode(vo.getSubjectCode());
		PriceContractInfoEntity contractEntity = null;
		PriceGeneralQuotationEntity quoTemplete = null;
		
		//查询合同
		String customerId=entity.getCustomerId();
		String SubjectId = vo.getSubjectCode();
		Map<String,Object> map=new HashMap<String,Object>();
		map.clear();
		map.put("customerid", customerId);
		map.put("contractTypeCode", "CUSTOMER_CONTRACT");
		contractEntity = jobPriceContractInfoService.queryContractByCustomer(map);
		
		CalcuContractVo cVo = new CalcuContractVo();
		cVo.setContractAttr("BMS");
		if(contractEntity == null || StringUtils.isEmpty(contractEntity.getContractCode())){
			//打印合同归属
			printLog(vo.getTaskId(), "contractInfo", entity.getFeesNo(), vo.getSubjectName(), "bms合同缺失", cVo);
			feeEntity.setIsCalculated(CalculateState.Contract_Miss.getCode());
			feeEntity.setCalcuMsg("bms合同缺失");
			return;
		}
		cVo.setContractNo(contractEntity.getContractCode());
		//验证签约服务
		Map<String,Object> contractItems_map=new HashMap<String,Object>();
		contractItems_map.put("contractCode", contractEntity.getContractCode());
		contractItems_map.put("subjectId", SubjectId);
		List<PriceContractItemEntity> contractItems = priceContractItemRepository.query(contractItems_map);
		if(contractItems == null || contractItems.size() == 0 || StringUtils.isEmpty(contractItems.get(0).getTemplateId())) {
			printLog(vo.getTaskId(), "contractInfo", entity.getFeesNo(), vo.getSubjectName(), "未签约服务", cVo);
			feeEntity.setIsCalculated(CalculateState.Contract_Miss.getCode());
			feeEntity.setCalcuMsg("未签约服务");
			return;
		}
		
		/*验证报价 报价*/
		map.clear();
		map.put("subjectId",SubjectId);
		map.put("quotationNo", contractItems.get(0).getTemplateId());
		quoTemplete = priceGeneralQuotationRepository.query(map);
		if(quoTemplete==null){
			printLog(vo.getTaskId(), "contractInfo", entity.getFeesNo(), vo.getSubjectName(), "报价模板缺失", cVo);
			feeEntity.setIsCalculated(CalculateState.Quote_Miss.getCode());
			feeEntity.setCalcuMsg("报价模板缺失");
			return;
		}
		printLog(vo.getTaskId(), "contractInfo", entity.getFeesNo(), vo.getSubjectName(), "合同存在", cVo);
		String contractNo = contractEntity.getContractCode();
		String quoModelNo = quoTemplete.getQuotationNo();
		//1：业务数据查询
		//任务ID,费用编号，科目名称，节点-业务数据（商家名称，仓库名称，原始数量，调整数量，业务时间）
		//2：计费数据
		//任务ID,费用编号，科目名称，节点-计费数据（商家名称，仓库名称，计费数量，业务时间）
		//3:不计算
		//任务ID,费用编号，科目名称，不计算原因
		//4：节点-合同查询
		//任务ID,费用编号，科目名称，节点-合同信息查询（合同归属，合同编号，报价模板编号，报价类型，计费单位，计费单价，节点描述）
		//5：节点-报价查询
		//任务ID,费用编号，科目名称，节点-报价信息（报价列表）
		//6:节点-报价帅选
		
		//任务ID,费用编号，科目名称，节点-报价信息（报价类型，计费单位，计费单价）
		//7：节点-费用计算
		//任务ID,费用编号，科目名称，计算公式
		
		String priceType = quoTemplete.getPriceType();
		String unit = quoTemplete.getFeeUnitCode();//计费单位 
		double num = feeEntity.getQuantity();
		CalcuInfoVo civo = new CalcuInfoVo();
		civo.setRuleNo("");
		civo.setChargeUnit(unit);
		switch (unit) {
		case "ITEMS":
			num = feeEntity.getQuantity();
			break;
		case "CARTON":
			num = feeEntity.getBox();
			break;
		case "TONS":
			double weight = 0d;
			if ((double)feeEntity.getWeight()/1000 < 1) {
				weight = 1d;
			}else {
				weight = (double)feeEntity.getWeight()/1000;
			}
			num = weight;
			break;
		case "KILOGRAM":
			num = feeEntity.getWeight();
			break;
		default:
			break;
		}
		
		//计算方法
		double amount=0d;
		switch(priceType){
			case "PRICE_TYPE_NORMAL"://一口价		
				//打印报价
				printLog(vo.getTaskId(), "quoteInfo", entity.getFeesNo(), vo.getSubjectName(), "", quoTemplete);
				civo.setChargeType("unitPrice");
				civo.setChargeDescrip("金额=单价*数量");
				amount=num*quoTemplete.getUnitPrice();
				feeEntity.setUnitPrice(quoTemplete.getUnitPrice());
				feeEntity.setParam3(quoTemplete.getId().toString());
				printLog(vo.getTaskId(), "ruleInfo", entity.getFeesNo(), vo.getSubjectName(), "", civo);
				break;
			case "PRICE_TYPE_STEP"://阶梯价	
				civo.setChargeType("stepPrice");
				map.clear();
				map.put("quotationId", quoTemplete.getId());
				map.put("num", feeEntity.getQuantity());//根据报价单位判断			
				//查询出的所有子报价
				List<PriceStepQuotationEntity> list=repository.queryPriceStepByQuatationId(map);
				
				if(list==null || list.size() == 0){
					logger.info(entity.getId()+"阶梯报价未配置");
					feeEntity.setIsCalculated(CalculateState.Quote_Miss.getCode());
					feeEntity.setCalcuMsg("阶梯报价未配置");
					return;
				}
				
				//封装数据的仓库和温度
				map.clear();
				map.put("warehouse_code", entity.getWarehouseCode());
				PriceStepQuotationEntity stepQuoEntity=storageQuoteFilterService.quoteFilter(list, map);			
				
				if(stepQuoEntity==null){
					printLog(vo.getTaskId(), "quoteInfo", entity.getFeesNo(), vo.getSubjectName(), "阶梯报价未配置", null);
					feeEntity.setIsCalculated(CalculateState.Quote_Miss.getCode());
					feeEntity.setCalcuMsg("阶梯报价未配置");
					return;
				}
				printLog(vo.getTaskId(), "quoteInfo", entity.getFeesNo(), vo.getSubjectName(), "", stepQuoEntity);
				
				if(!DoubleUtil.isBlank(stepQuoEntity.getUnitPrice())){
					civo.setChargeType("unitPrice");
					civo.setChargeDescrip("金额=单价*数量");
					feeEntity.setUnitPrice(stepQuoEntity.getUnitPrice());
					amount=num*stepQuoEntity.getUnitPrice();
				}else{
					civo.setChargeType("stepPrice");
					civo.setChargeDescrip("金额=首量价/首量价+续价");
					amount=stepQuoEntity.getFirstNum()<num?stepQuoEntity.getFirstPrice()+(num-stepQuoEntity.getFirstNum())/stepQuoEntity.getContinuedItem()*stepQuoEntity.getContinuedPrice():stepQuoEntity.getFirstPrice();
				}
				//判断封顶价
				if(!DoubleUtil.isBlank(stepQuoEntity.getCapPrice())){
					if(stepQuoEntity.getCapPrice()<amount){
						civo.setChargeType("topPrice");
						amount=stepQuoEntity.getCapPrice();
					}
				}
				//打印计费规则
				printLog(vo.getTaskId(), "ruleInfo", entity.getFeesNo(), vo.getSubjectName(), "", civo);
				feeEntity.setParam3(stepQuoEntity.getId()+"");
				break;
			default:
				break;
		}
		feeEntity.setCost(BigDecimal.valueOf(amount));
		feeEntity.setParam4(priceType);
		entity.setRemark(entity.getRemark()+"计算成功;");
		entity.setIsCalculated(CalculateState.Finish.getCode());
		feeEntity.setCalcuMsg("计算成功");
		feeEntity.setIsCalculated(CalculateState.Finish.getCode());
	}

	@Override
	protected void calcuForContract(FeesReceiveStorageEntity fee,Map<String, Object> errorMap) {
		if("succ".equals(errorMap.get("success").toString())){
			if(fee.getCost().compareTo(BigDecimal.ZERO) == 1){
				fee.setIsCalculated(CalculateState.Finish.getCode());
				logger.info("计算成功，费用【{}】",fee.getCost());
			}
			else{
				fee.setIsCalculated(CalculateState.Sys_Error.getCode());
				logger.info("计算不成功，费用【{}】",fee.getCost());
				fee.setCalcuMsg("未计算出金额");
			}
		}
		else{
			fee.setIsCalculated(errorMap.get("is_calculated").toString());
			fee.setCalcuMsg(errorMap.get("msg").toString());
		}
	}

	

	@Override
	protected void updateBatch(List<FeesReceiveStorageEntity> fees) {
		feesReceiveStorageService.updateBatch(fees);
	}

	@Override
	protected BmsFeesQtyVo feesCountReport(String customerId,String subjectCode, Integer creMonth) {
		BmsFeesQtyVo vo = bmsCalcuService.queryFeesQtyForSto(customerId, subjectCode, creMonth);
		return vo;
	}

	@Override
	protected void queryQuoModel(BmsCalcuTaskVo vo, Map<String, Object> bmsMap) {
		PriceContractItemEntity contractItem = (PriceContractItemEntity) bmsMap.get("ContractInfoItem");
		Map<String, Object> cond = new HashMap<String, Object>();
		cond.put("subjectId",vo.getSubjectCode());
		cond.put("quotationNo", contractItem.getTemplateId());
		PriceGeneralQuotationEntity quoTemplete = priceGeneralQuotationRepository.query(cond);
		if(quoTemplete == null){
			logger.info("taskId={} 报价模板缺失",vo.getTaskId());
			bmsMap.put("success", "fail");
			bmsMap.put("is_calculated", CalculateState.Contract_Miss.getCode());
			bmsMap.put("msg", "报价模板缺失");
			return;
		}
		bmsMap.put("QuoModelInfo", quoTemplete);
		bmsMap.put("QuoModelNo", quoTemplete.getQuotationNo());
		logger.info("taskId={} 报价模板编号:",quoTemplete.getQuotationNo());
	}
	
	
	
	@Override
	protected FeesReceiveStorageEntity initFeeEntity(BmsCalcuTaskVo vo,BmsBizInstockInfoEntity instock) {
		//打印业务数据日志
		printLog(vo.getTaskId(), "bizInfo", instock.getFeesNo(), vo.getSubjectName(), "", instock);
		FeesReceiveStorageEntity storageFeeEntity = new FeesReceiveStorageEntity();
		double num=DoubleUtil.isBlank(instock.getAdjustQty())?instock.getTotalQty():instock.getAdjustQty();
		if(!DoubleUtil.isBlank(num)){
			storageFeeEntity.setQuantity(num);//商品数量
		}
		//重量
		Double weight = DoubleUtil.isBlank(instock.getAdjustWeight())?instock.getTotalWeight():instock.getAdjustWeight();
		storageFeeEntity.setWeight(weight);
		
		//塞箱数
		Double box=DoubleUtil.isBlank(instock.getAdjustBox())?instock.getTotalBox():instock.getTotalBox();
		if(!DoubleUtil.isBlank(box)){
			storageFeeEntity.setBox(box.intValue());
		}
		storageFeeEntity.setCostType("FEE_TYPE_GENEARL");
		storageFeeEntity.setUnitPrice(0d);
		storageFeeEntity.setCost(new BigDecimal(0));	
		storageFeeEntity.setDelFlag("0");
		storageFeeEntity.setFeesNo(instock.getFeesNo());
		storageFeeEntity.setCalculateTime(JAppContext.currentTimestamp());
		printLog(vo.getTaskId(), "chargeInfo", instock.getFeesNo(), vo.getSubjectName(), "", storageFeeEntity);
		return storageFeeEntity;
	}



	@Override
	protected boolean isNoExe(BmsBizInstockInfoEntity t,FeesReceiveStorageEntity f,Map<String, Object> errorMap) {
		/*if(!"succ".equals(errorMap.get("success"))){
			f.setIsCalculated(errorMap.get("is_calculated").toString());
			f.setCalcuMsg(errorMap.get("msg").toString());
			return true;
		}*/
		return false;
	}

	@Override
	protected ContractQuoteQueryInfoVo getCtConditon(BmsCalcuTaskVo vo,BmsBizInstockInfoEntity entity) {
		ContractQuoteQueryInfoVo queryVo = new ContractQuoteQueryInfoVo();
		queryVo.setCustomerId(entity.getCustomerId());
		queryVo.setBizTypeCode(ContractBizTypeEnum.STORAGE.getCode());
		queryVo.setSubjectCode(vo.getSubjectCode());
		queryVo.setCurrentTime(entity.getCreateTime());
		queryVo.setWarehouseCode(entity.getWarehouseCode());
		return queryVo;

	}

	/*@Override
	protected void printLog(BmsCalcuTaskVo vo,BmsBizInstockInfoEntity t,FeesReceiveStorageEntity f,String descrip,Object obj,String nodeName) {
		if(StringUtil.isEmpty(nodeName)){
			logger.info("taskId={} feesNo={} subjectName={} {} {}",vo.getTaskId(),t.getFeesNo(),vo.getSubjectName(),descrip,JSONObject.fromObject(obj));
		}
		else {
			logger.info("taskId={} feesNo={} subjectName={} nameNode={} msg={}",vo.getTaskId(),t.getFeesNo(),vo.getSubjectName(),nodeName,JSONObject.fromObject(obj));
		}
		
	}*/

	
}
