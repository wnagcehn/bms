package com.jiuyescm.bms.calcu.receive.storage.instock;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.kie.internal.task.api.TaskIdentityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.jiuyescm.bms.asyn.vo.BmsCalcuTaskVo;
import com.jiuyescm.bms.calcu.base.CalcuTaskListener;
import com.jiuyescm.bms.calculate.api.IBmsCalcuService;
import com.jiuyescm.bms.calculate.vo.BmsFeesQtyVo;
import com.jiuyescm.bms.chargerule.receiverule.entity.BillRuleReceiveEntity;
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
import com.jiuyescm.bms.quotation.transport.entity.GenericTemplateEntity;
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
		if(contractEntity == null || StringUtils.isEmpty(contractEntity.getContractCode())){
			logger.info("bms合同缺失");
			feeEntity.setIsCalculated(CalculateState.Contract_Miss.getCode());
			feeEntity.setCalcuMsg("bms合同缺失");
			return;
		}
		//验证签约服务
		Map<String,Object> contractItems_map=new HashMap<String,Object>();
		contractItems_map.put("contractCode", contractEntity.getContractCode());
		contractItems_map.put("subjectId", SubjectId);
		List<PriceContractItemEntity> contractItems = priceContractItemRepository.query(contractItems_map);
		if(contractItems == null || contractItems.size() == 0 || StringUtils.isEmpty(contractItems.get(0).getTemplateId())) {
			logger.info("未签约服务");
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
			logger.info("报价模板缺失");
			feeEntity.setIsCalculated(CalculateState.Quote_Miss.getCode());
			feeEntity.setCalcuMsg("报价模板缺失");
			return;
		}
		logger.info("合同信息");
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
		
		logger.info("taskId={} feesNo={} subjectCode={} contractNo={} quoModelNo={} descrip={}",
				vo.getTaskId(),entity.getFeesNo(),vo.getSubjectCode(),contractNo,quoModelNo,"");
		
		
		
		String priceType = quoTemplete.getPriceType();
		String unit = quoTemplete.getFeeUnitCode();//计费单位 
		double num = feeEntity.getQuantity();
		double weight = 0d;
		if ((double)feeEntity.getWeight()/1000 < 1) {
			weight = 1d;
		}else {
			weight = (double)feeEntity.getWeight()/1000;
		}
		//计算方法
		double amount=0d;
		switch(priceType){
		case "PRICE_TYPE_NORMAL"://一口价				
            //如果计费单位是 件 -> 费用 = 商品数量*模板单价
			//如果计费单位是 箱 -> 费用 = 商品箱数*模板单价
			//如果计费单位是 吨 -> 费用 = 商品重量*模板单价/1000
			//如果计费单位是 千克 -> 费用 = 商品重量*模板单价
			if ("ITEMS".equals(unit)) {
				amount=feeEntity.getQuantity()*quoTemplete.getUnitPrice();
			}else if ("CARTON".equals(unit)) {
				amount=feeEntity.getBox()*quoTemplete.getUnitPrice();
			}else if ("TONS".equals(unit)) {
				feeEntity.setWeight(weight);
				amount = weight*quoTemplete.getUnitPrice();					
			}else if ("KILOGRAM".equals(unit)) {
				amount=feeEntity.getWeight()*quoTemplete.getUnitPrice();
			}
			feeEntity.setUnitPrice(quoTemplete.getUnitPrice());
			feeEntity.setParam3(quoTemplete.getId().toString());
			break;
		case "PRICE_TYPE_STEP"://阶梯价						
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
				logger.info("阶梯报价未配置");
				feeEntity.setIsCalculated(CalculateState.Quote_Miss.getCode());
				feeEntity.setCalcuMsg("阶梯报价未配置");
				return;
			}
			logger.info("筛选后得到的报价结果【{0}】",JSONObject.fromObject(stepQuoEntity));
			
            // 如果计费单位是 件
			if ("ITEMS".equals(unit)) {//按件
				if(!DoubleUtil.isBlank(stepQuoEntity.getUnitPrice())){
					feeEntity.setUnitPrice(stepQuoEntity.getUnitPrice());
					amount=num*stepQuoEntity.getUnitPrice();
				}else{
					amount=stepQuoEntity.getFirstNum()<num?stepQuoEntity.getFirstPrice()+(num-stepQuoEntity.getFirstNum())/stepQuoEntity.getContinuedItem()*stepQuoEntity.getContinuedPrice():stepQuoEntity.getFirstPrice();
				}
			}else if ("TONS".equals(unit)) {//按吨
				if(!DoubleUtil.isBlank(stepQuoEntity.getUnitPrice())){
					amount=weight * stepQuoEntity.getUnitPrice();
					feeEntity.setUnitPrice(stepQuoEntity.getUnitPrice());
				}else{
					amount=(double)(stepQuoEntity.getFirstNum()<weight?stepQuoEntity.getFirstPrice()+(weight-stepQuoEntity.getFirstNum())/stepQuoEntity.getContinuedItem()*stepQuoEntity.getContinuedPrice():stepQuoEntity.getFirstPrice());
				}		
			}else if("CARTON".equals(unit)){//按箱
				if(!DoubleUtil.isBlank(stepQuoEntity.getUnitPrice())){
					amount=feeEntity.getBox()*stepQuoEntity.getUnitPrice();
					feeEntity.setUnitPrice(stepQuoEntity.getUnitPrice());
				}else{
					amount=stepQuoEntity.getFirstNum()<feeEntity.getBox()?stepQuoEntity.getFirstPrice()+(feeEntity.getBox()-stepQuoEntity.getFirstNum())/stepQuoEntity.getContinuedItem()*stepQuoEntity.getContinuedPrice():stepQuoEntity.getFirstPrice();
				}
			}
			//判断封顶价
			if(!DoubleUtil.isBlank(stepQuoEntity.getCapPrice())){
				if(stepQuoEntity.getCapPrice()<amount){
					amount=stepQuoEntity.getCapPrice();
				}
			}
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
	protected FeesReceiveStorageEntity initFeeEntity(BmsBizInstockInfoEntity instock) {
		
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

	@Override
	protected void printLog(BmsCalcuTaskVo vo,BmsBizInstockInfoEntity t,FeesReceiveStorageEntity f,String descrip,Object obj,String nodeName) {
		if(StringUtil.isEmpty(nodeName)){
			logger.info("taskId={} feesNo={} subjectName={} {} {}",vo.getTaskId(),t.getFeesNo(),vo.getSubjectName(),descrip,JSONObject.fromObject(obj));
		}
		else {
			logger.info("taskId={} feesNo={} subjectName={} nameNode={} msg={}",vo.getTaskId(),t.getFeesNo(),vo.getSubjectName(),nodeName,JSONObject.fromObject(obj));
		}
		
	}
	
	protected boolean validateData(BmsCalcuTaskVo vo,BizAddFeeEntity entity,FeesReceiveStorageEntity storageFeeEntity) {
		
		
		return true;
	}


	
}
