package com.jiuyescm.bms.jobhandler;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyescm.bms.biz.dispatch.entity.BizDispatchBillEntity;
import com.jiuyescm.bms.biz.storage.entity.BizOutstockPackmaterialEntity;
import com.jiuyescm.bms.calculate.base.IFeesCalcuService;
import com.jiuyescm.bms.chargerule.receiverule.entity.BillRuleReceiveEntity;
import com.jiuyescm.bms.common.enumtype.CalculateState;
import com.jiuyescm.bms.common.enumtype.TemplateTypeEnum;
import com.jiuyescm.bms.general.entity.FeesReceiveStorageEntity;
import com.jiuyescm.bms.general.service.IFeesReceiveStorageService;
import com.jiuyescm.bms.general.service.IPriceContractInfoService;
import com.jiuyescm.bms.general.service.SequenceService;
import com.jiuyescm.bms.quotation.contract.entity.PriceContractInfoEntity;
import com.jiuyescm.bms.quotation.contract.entity.PriceContractItemEntity;
import com.jiuyescm.bms.quotation.contract.repository.imp.IPriceContractDao;
import com.jiuyescm.bms.quotation.contract.repository.imp.IPriceContractItemRepository;
import com.jiuyescm.bms.quotation.storage.entity.PriceMaterialQuotationEntity;
import com.jiuyescm.bms.quotation.storage.repository.IPriceMaterialQuotationRepository;
import com.jiuyescm.bms.quotation.transport.repository.IGenericTemplateRepository;
import com.jiuyescm.bms.receivable.dispatch.service.IBizDispatchBillService;
import com.jiuyescm.bms.receivable.storage.service.IBizOutstockPackmaterialService;
import com.jiuyescm.bms.rule.receiveRule.repository.IReceiveRuleRepository;
import com.jiuyescm.cfm.common.JAppContext;
import com.jiuyescm.common.utils.DoubleUtil;
import com.jiuyescm.contract.quote.api.IContractQuoteInfoService;
import com.jiuyescm.contract.quote.vo.ContractBizTypeEnum;
import com.jiuyescm.contract.quote.vo.ContractQuoteInfoVo;
import com.jiuyescm.contract.quote.vo.ContractQuoteQueryInfoVo;
import com.jiuyescm.exception.BizException;
import com.xxl.job.core.handler.annotation.JobHander;
import com.xxl.job.core.log.XxlJobLogger;

@JobHander(value="materialUseNewCalcJob")
@Service
public class MaterialUseNewCalcJob extends CommonJobHandler<BizOutstockPackmaterialEntity,FeesReceiveStorageEntity> {

	private String SubjectId = "wh_material_use";//费用类型-耗材使用费编码
	
	@Autowired private IBizOutstockPackmaterialService bizOutstockPackmaterialService;
	@Autowired private IContractQuoteInfoService contractQuoteInfoService;
	@Autowired private IBizDispatchBillService bizDispatchBillService;
	@Autowired private IFeesCalcuService feesCalcuService;
	@Autowired private IReceiveRuleRepository receiveRuleRepository;
	@Autowired private IPriceContractDao priceContractService;
	@Autowired private IPriceContractInfoService jobPriceContractInfoService;
	@Autowired private IPriceContractItemRepository priceContractItemRepository;
	@Autowired private IPriceMaterialQuotationRepository priceMaterialQuotationRepository;
	@Autowired private IGenericTemplateRepository genericTemplateRepository;
	@Autowired private IFeesReceiveStorageService feesReceiveStorageService;
	@Autowired private SequenceService sequenceService;
	
	Map<String,PriceContractInfoEntity> mapContact=null;
	Map<String,BillRuleReceiveEntity> mapRule=null;
	Map<String,List<PriceMaterialQuotationEntity>> mapCusPrice=null;
	
	@Override
	protected List<BizOutstockPackmaterialEntity> queryBillList(Map<String, Object> map) {
		/*List<BizOutstockPackmaterialEntity> bizList = bizOutstockPackmaterialService.query(map);
		return bizList;*/
		
		long operateTime = System.currentTimeMillis();
		List<String> feesNos = new ArrayList<String>();
		Map<String, Object> feesMap = new HashMap<String, Object>();
		List<BizOutstockPackmaterialEntity> bizList = bizOutstockPackmaterialService.query(map);
		if(bizList == null || bizList.size() == 0){
			
		}
		else{
			for (BizOutstockPackmaterialEntity entity : bizList) {
				if(StringUtils.isNotEmpty(entity.getFeesNo())){
					feesNos.add(entity.getFeesNo());
				}
				else{
					entity.setFeesNo(sequenceService.getBillNoOne(FeesReceiveStorageEntity.class.getName(), "STO", "0000000000"));
				}
			}
			try{
				if(feesNos.size()>0){
					feesMap.put("feesNos", feesNos);
					feesReceiveStorageService.deleteBatch(feesMap);
					long current = System.currentTimeMillis();// 系统开始时间
					XxlJobLogger.log("批量删除费用成功 耗时【{0}】毫秒 删除条数【{1}】",(current-operateTime),feesNos.size());
				}
			}
			catch(Exception ex){
				XxlJobLogger.log("批量删除费用失败-- {1}",ex.getMessage());
			}
		}
		return bizList;
	}
	
	@Override
	protected FeesReceiveStorageEntity initFeeEntity(BizOutstockPackmaterialEntity entity) {
		FeesReceiveStorageEntity storageFeeEntity = new FeesReceiveStorageEntity();	
		storageFeeEntity.setCreator("system");
		storageFeeEntity.setCreateTime(entity.getCreateTime());
		storageFeeEntity.setOperateTime(entity.getCreateTime());
		storageFeeEntity.setCustomerId(entity.getCustomerId());			//商家ID
		storageFeeEntity.setCustomerName(entity.getCustomerName());		//商家名称
		storageFeeEntity.setWarehouseCode(entity.getWarehouseCode());	//仓库ID
		storageFeeEntity.setWarehouseName(entity.getWarehouseName());	//仓库名称
		storageFeeEntity.setCostType("FEE_TYPE_MATERIAL");				//费用类别 FEE_TYPE_GENEARL-通用 FEE_TYPE_MATERIAL-耗材 FEE_TYPE_ADD-增值
		storageFeeEntity.setSubjectCode(SubjectId);						//费用科目
		storageFeeEntity.setOtherSubjectCode(SubjectId);
		storageFeeEntity.setProductType("");							//商品类型
		storageFeeEntity.setQuantity(DoubleUtil.isBlank(entity.getAdjustNum())?entity.getNum():entity.getAdjustNum());//计费数量
		storageFeeEntity.setStatus("0");								//状态
		storageFeeEntity.setOrderNo(entity.getOutstockNo());
		//根据测试的建议 吧耗材编码设置成商品编号和商品名称 zhangzw
		storageFeeEntity.setProductNo(entity.getConsumerMaterialCode());
		storageFeeEntity.setProductName(entity.getConsumerMaterialName());
		storageFeeEntity.setBizId(String.valueOf(entity.getId()));						//业务数据主键
		storageFeeEntity.setWeight(entity.getWeight());					//设置重量
		entity.setCalculateTime(JAppContext.currentTimestamp());
		storageFeeEntity.setCalculateTime(JAppContext.currentTimestamp());
		storageFeeEntity.setCost(new BigDecimal(0));					//入仓金额
		storageFeeEntity.setFeesNo(entity.getFeesNo());
		storageFeeEntity.setParam1(TemplateTypeEnum.COMMON.getCode());
		storageFeeEntity.setDelFlag("0");
		return storageFeeEntity;
	}
	
	@Override
	protected boolean isNoExe(BizOutstockPackmaterialEntity entity,FeesReceiveStorageEntity feeEntity) {
		if(entity.getConsumerMaterialCode()!=null && entity.getConsumerMaterialCode().contains("FSD")){
			BizDispatchBillEntity biz=bizDispatchBillService.getDispatchEntityByWaybillNo(entity.getWaybillNo());
			if(biz!=null && "1500000015".equals(biz.getCarrierId())){
				XxlJobLogger.log(String.format("顺丰防水袋，不收钱", entity.getId()));
				entity.setIsCalculated(CalculateState.No_Exe.getCode());
				feeEntity.setIsCalculated(CalculateState.No_Exe.getCode());
				entity.setRemark(String.format("顺丰防水袋，不收钱", entity.getId()));
				return true;
			}
		}
		return false;
	}

	@Override
	protected ContractQuoteInfoVo getContractForWhat(BizOutstockPackmaterialEntity entity) {
		
		
		/*ContractQuoteQueryInfoVo queryVo1=new ContractQuoteQueryInfoVo();
		queryVo1.setBizTypeCode(ContractBizTypeEnum.DISTRIBUTION.getCode());
		queryVo1.setCarrierId("1500000019");
		queryVo1.setCurrentTime(Timestamp.valueOf("2018-08-28 00:00:00"));
		queryVo1.setCustomerId("SubSJ000014");
		queryVo1.setSubjectCode("de_delivery_amount");
		queryVo1.setWarehouseCode("");
		ContractQuoteInfoVo quoteInfoVo=contractQuoteInfoService.queryUniqueColumns(queryVo1);
		Map<String,Object> mapCondition=Maps.newHashMap();
		mapCondition.put("e1", "北京01仓");
		mapCondition.put("e2", "北京");
		quoteInfoVo=contractQuoteInfoService.queryQuotes(quoteInfoVo, mapCondition);*/
		
		
		ContractQuoteQueryInfoVo queryVo = new ContractQuoteQueryInfoVo();
		queryVo.setCustomerId(entity.getCustomerId());
		queryVo.setBizTypeCode(ContractBizTypeEnum.STORAGE.getCode());
		queryVo.setSubjectCode(SubjectId);
		queryVo.setCurrentTime(entity.getCreateTime());
		XxlJobLogger.log("查询合同在线参数",JSONObject.fromObject(queryVo));
		ContractQuoteInfoVo modelEntity = new ContractQuoteInfoVo();
		try{
			modelEntity = contractQuoteInfoService.queryUniqueColumns(queryVo);
			XxlJobLogger.log("查询出的合同在线结果",JSONObject.fromObject(modelEntity));

		}
		catch(BizException ex){
			XxlJobLogger.log("合同在线无此合同",ex);
		}
		return modelEntity;
	}
	
	@Override
	protected void calcuForBms(BizOutstockPackmaterialEntity entity,FeesReceiveStorageEntity feeEntity){
		//合同报价校验  false-不通过  true-通过
		XxlJobLogger.log("bms计算");
		try{
			if(validateData(entity, feeEntity)){
				if(mapCusPrice.containsKey(entity.getCustomerId())){
					List<PriceMaterialQuotationEntity> list=mapCusPrice.get(entity.getCustomerId());
					String id="";	

					if(StringUtils.isNotBlank(feeEntity.getWarehouseCode())){
						for(PriceMaterialQuotationEntity vo:list){
							if(feeEntity.getWarehouseCode().equals(vo.getWarehouseId())){				
								feeEntity.setCost(new BigDecimal(vo.getUnitPrice()*feeEntity.getQuantity()).setScale(2,BigDecimal.ROUND_HALF_UP));
								feeEntity.setUnitPrice(vo.getUnitPrice());
								feeEntity.setParam2(vo.getUnitPrice().toString()+"*"+feeEntity.getQuantity().toString());
								id=vo.getId()+"";
								break;
							}
						}
					}
						
					if(StringUtils.isBlank(id)){
						for(PriceMaterialQuotationEntity vo:list){
							if(StringUtils.isBlank(vo.getWarehouseId())){
								feeEntity.setCost(new BigDecimal(vo.getUnitPrice()*feeEntity.getQuantity()).setScale(2,BigDecimal.ROUND_HALF_UP));
								feeEntity.setUnitPrice(vo.getUnitPrice());
								feeEntity.setParam2(vo.getUnitPrice().toString()+"*"+feeEntity.getQuantity().toString());
								id=vo.getId()+"";
							}
						}
					}
					feeEntity.setParam3(id);
					if(feeEntity.getCost().compareTo(BigDecimal.ZERO) == 1){
						feeEntity.setIsCalculated(CalculateState.Finish.getCode());
						entity.setIsCalculated(CalculateState.Finish.getCode());
						XxlJobLogger.log("计算成功，费用【{0}】",feeEntity.getCost());
					}else{
						feeEntity.setIsCalculated(CalculateState.Quote_Miss.getCode());
						entity.setIsCalculated(CalculateState.Finish.getCode());
						XxlJobLogger.log("计算不成功，费用【0】");
					}
				}
			}
		}
		catch(Exception ex){
			feeEntity.setIsCalculated(CalculateState.Sys_Error.getCode());
			entity.setIsCalculated(CalculateState.Sys_Error.getCode());
			XxlJobLogger.log("系统异常，费用【0】");
		}
	}
	
	@Override
	protected void calcuForContract(BizOutstockPackmaterialEntity entity,FeesReceiveStorageEntity feeEntity){
		XxlJobLogger.log("合同在线计算");
		try{
			Map<String, Object> con = new HashMap<>();
			con.put("quotationNo", contractQuoteInfoVo.getRuleCode());
			BillRuleReceiveEntity ruleEntity = receiveRuleRepository.queryOne(con);
			if (null == ruleEntity) {
				entity.setRemark("合同在线规则未绑定");
				feeEntity.setIsCalculated(CalculateState.Quote_Miss.getCode());
				entity.setIsCalculated(CalculateState.Quote_Miss.getCode());
				XxlJobLogger.log("计算不成功，合同在线规则未绑定");
			}
			//获取合同在线查询条件
			Map<String, Object> cond = new HashMap<String, Object>();
			feesCalcuService.ContractCalcuService(entity, cond, ruleEntity.getRule(), ruleEntity.getQuotationNo());
			XxlJobLogger.log("获取报价参数"+cond);
			ContractQuoteInfoVo rtnQuoteInfoVo = contractQuoteInfoService.queryQuotes(contractQuoteInfoVo, cond);
			XxlJobLogger.log("获取合同在线报价结果",JSONObject.fromObject(rtnQuoteInfoVo));
			for (Map<String, String> map : rtnQuoteInfoVo.getQuoteMaps()) {
				XxlJobLogger.log("报价信息 -- "+map);
			}
			//调用规则计算费用
			Map<String, Object> feesMap = feesCalcuService.ContractCalcuService(feeEntity, rtnQuoteInfoVo.getQuoteMaps(), ruleEntity.getRule(), ruleEntity.getQuotationNo());

			if(feeEntity.getCost().compareTo(BigDecimal.ZERO) == 1){
				feeEntity.setIsCalculated(CalculateState.Finish.getCode());
				entity.setIsCalculated(CalculateState.Finish.getCode());
				XxlJobLogger.log("计算成功，费用【{0}】",feeEntity.getCost());
			}
			else{
				feeEntity.setIsCalculated(CalculateState.Quote_Miss.getCode());
				entity.setIsCalculated(CalculateState.Quote_Miss.getCode());
				XxlJobLogger.log("计算不成功，费用【0】");
			}
		}
		catch(Exception ex){
			feeEntity.setIsCalculated(CalculateState.Sys_Error.getCode());
			entity.setIsCalculated(CalculateState.Sys_Error.getCode());
			XxlJobLogger.log("计算不成功，费用0",ex);
		}
		
	}
	
	protected boolean validateData(BizOutstockPackmaterialEntity entity,FeesReceiveStorageEntity feeEntity) {
		
		mapContact=new HashMap<String,PriceContractInfoEntity>();
		mapRule=new HashMap<String,BillRuleReceiveEntity>();	
		mapCusPrice=new HashMap<String,List<PriceMaterialQuotationEntity>>();
		
		long start = System.currentTimeMillis();// 开始时间
		XxlJobLogger.log("数据主键ID:【{0}】  ",entity.getId());
		entity.setCalculateTime(feeEntity.getCalculateTime());
		Map<String,Object> map=new HashMap<String,Object>();
		long current = 0l;// 当前系统时间
		String customerId = entity.getCustomerId();
		
		//验证商家合同
		PriceContractInfoEntity contractEntity =null;
		if(mapContact.containsKey(customerId)){
			contractEntity=mapContact.get(customerId);
		}else{
			map.clear();
			map.put("customerid", customerId);
			map.put("contractTypeCode", "CUSTOMER_CONTRACT");
		    contractEntity = jobPriceContractInfoService.queryContractByCustomer(map);
		    mapContact.put(customerId, contractEntity);
		}
		if(contractEntity == null || StringUtils.isEmpty(contractEntity.getContractCode())){
			XxlJobLogger.log(String.format("未查询到合同  订单号【%s】--商家【%s】", entity.getId(),customerId));
			entity.setIsCalculated(CalculateState.Contract_Miss.getCode());
			feeEntity.setIsCalculated(CalculateState.Contract_Miss.getCode());
			entity.setRemark(String.format("未查询到合同  订单号【%s】--商家【%s】", entity.getId(),customerId));
			return false;
		}
		current = System.currentTimeMillis();
		XxlJobLogger.log("验证合同   耗时【{0}】毫秒  合同编号 【{1}】",(current - start),contractEntity.getContractCode());
		
		
		//----验证签约服务
		start = System.currentTimeMillis();// 系统开始时间
		Map<String,Object> contractItems_map=new HashMap<String,Object>();
		contractItems_map.put("contractCode", contractEntity.getContractCode());
		contractItems_map.put("subjectId", SubjectId);
		List<PriceContractItemEntity> contractItems = priceContractItemRepository.query(contractItems_map);
		if(contractItems == null || contractItems.size() == 0 || StringUtils.isEmpty(contractItems.get(0).getTemplateId())) {
			XxlJobLogger.log(String.format("未签约服务  主键【%s】--商家【%s】", entity.getId(),entity.getCustomerId()));
			entity.setIsCalculated(CalculateState.Contract_Miss.getCode());
			feeEntity.setIsCalculated(CalculateState.Contract_Miss.getCode());
			entity.setRemark(String.format("未签约服务"));
			return false;
		}
		current = System.currentTimeMillis();
		XxlJobLogger.log("验证签约服务耗时：【{0}】毫秒  ",(current - start));		
		start = System.currentTimeMillis();// 系统开始时间
		
		
		//商家耗材报价
		map.clear();
		map.put("contractCode", contractEntity.getContractCode());
		map.put("subjectId",SubjectId);
		map.put("materialCode",entity.getConsumerMaterialCode());
		map.put("warehouseId", entity.getWarehouseCode());
		List<PriceMaterialQuotationEntity> materialList=priceMaterialQuotationRepository.queryMaterialQuatationByContract(map);
		mapCusPrice.put(customerId, materialList);
		if(materialList==null||materialList.size()==0){
			XxlJobLogger.log("报价未配置");
			entity.setIsCalculated(CalculateState.Quote_Miss.getCode());
			feeEntity.setIsCalculated(CalculateState.Quote_Miss.getCode());
			entity.setRemark("报价未配置");
			return false;
		}
		current = System.currentTimeMillis();
		XxlJobLogger.log("验证报价耗时：【{0}】毫秒  ",(current - start));
		start = System.currentTimeMillis();// 系统开始时间
		return true;
	}

	@Override
	protected void updateBatch(List<BizOutstockPackmaterialEntity> ts,List<FeesReceiveStorageEntity> fs) {

		long start = System.currentTimeMillis();// 系统开始时间
		long current = 0l;// 当前系统时间
		bizOutstockPackmaterialService.updateBatch(ts);
		current = System.currentTimeMillis();
		XxlJobLogger.log("更新业务数据耗时：【{0}】毫秒",(current - start));
		start = System.currentTimeMillis();// 系统开始时间
		feesReceiveStorageService.InsertBatch(fs);
		current = System.currentTimeMillis();
		XxlJobLogger.log("新增费用数据耗时：【{0}】毫秒  ",(current - start));
		
	}

	

	

	
	
	

}
