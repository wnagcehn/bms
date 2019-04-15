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
import com.jiuyescm.bms.biz.storage.entity.BizOutstockPackmaterialEntity;
import com.jiuyescm.bms.drools.IFeesCalcuService;
import com.jiuyescm.bms.chargerule.receiverule.entity.BillRuleReceiveEntity;
import com.jiuyescm.bms.common.enumtype.CalculateState;
import com.jiuyescm.bms.fees.storage.entity.FeesSaleReceiveStorageEntity;
import com.jiuyescm.bms.general.service.IFeesSaleReceiveStorageService;
import com.jiuyescm.bms.general.service.IPriceContractInfoService;
import com.jiuyescm.bms.general.service.SequenceService;
import com.jiuyescm.bms.quotation.contract.entity.PriceContractInfoEntity;
import com.jiuyescm.bms.quotation.contract.entity.PriceContractItemEntity;
import com.jiuyescm.bms.quotation.contract.repository.imp.IPriceContractItemRepository;
import com.jiuyescm.bms.quotation.storage.entity.PriceMaterialQuotationEntity;
import com.jiuyescm.bms.quotation.storage.repository.IPriceMaterialQuotationRepository;
import com.jiuyescm.bms.receivable.storage.service.IBizWmsOutstockPackmaterialService;
import com.jiuyescm.bms.rule.receiveRule.repository.IReceiveRuleRepository;
import com.jiuyescm.cfm.common.JAppContext;
import com.xxl.job.core.handler.annotation.JobHander;
import com.xxl.job.core.log.XxlJobLogger;

@JobHander(value="materialWmsUseCalcJob")
@Service
public class MaterialWmsUseCalcJob extends CommonCalcJob<BizOutstockPackmaterialEntity,FeesSaleReceiveStorageEntity> {

	@Autowired private IBizWmsOutstockPackmaterialService bizWmsOutstockPackmaterialService;
	@Autowired private IFeesSaleReceiveStorageService feesSaleReceiveStorageService;
	@Autowired private IPriceContractInfoService jobPriceContractInfoService;
	@Autowired private SequenceService sequenceService;
	@Autowired private IPriceContractItemRepository priceContractItemRepository;
	@Autowired private IPriceMaterialQuotationRepository priceMaterialQuotationRepository;
	@Autowired private IReceiveRuleRepository receiveRuleRepository;
	@Autowired private IFeesCalcuService feesCalcuService;
	
	private final String startName="FSRS";
	private final String SubjectId = "wh_material_use";		//费用类型-耗材使用费编码  1009原编码
	
	Map<String,PriceContractInfoEntity> mapContact=null;
	Map<String,BillRuleReceiveEntity> mapRule=null;
	Map<String,List<PriceMaterialQuotationEntity>> mapCusPrice=null;
	
	@Override
	protected List<BizOutstockPackmaterialEntity> queryBillList(Map<String, Object> map) {
		XxlJobLogger.log("materialWmsUseCalcJob查询条件map:【{0}】  ",map);
		long operateTime = System.currentTimeMillis();
		List<BizOutstockPackmaterialEntity> bizList=bizWmsOutstockPackmaterialService.query(map);
		if(bizList!=null&&bizList.size()>0){
			try{
				List<String> feesNos = new ArrayList<String>();
				Map<String, Object> feesMap = new HashMap<String, Object>();
				for (BizOutstockPackmaterialEntity entity : bizList) {
					if(StringUtils.isNotEmpty(entity.getFeesNo())){
						feesNos.add(entity.getFeesNo());
					}
					else{
						entity.setFeesNo(sequenceService.getBillNoOne(FeesSaleReceiveStorageEntity.class.getName(), startName, "00000000000"));
					}
				}
				if(feesNos.size()>0){
					feesMap.put("feesNos", feesNos);
					feesSaleReceiveStorageService.deleteBatch(feesMap);
					long current = System.currentTimeMillis();// 系统开始时间
					XxlJobLogger.log("批量删除费用成功 耗时【{0}】毫秒 删除条数【{1}】",(current-operateTime),feesNos.size());
				}
			}catch(Exception e){
				XxlJobLogger.log(e.getMessage());
			}
		}
		return bizList;
	}

	@Override
	protected void initConf(List<BizOutstockPackmaterialEntity> billList) throws Exception {
		mapContact=new HashMap<String,PriceContractInfoEntity>();
		mapRule=new HashMap<String,BillRuleReceiveEntity>();	
		mapCusPrice=new HashMap<String,List<PriceMaterialQuotationEntity>>();
	}
	
	@Override
	protected void calcuService(BizOutstockPackmaterialEntity entity,List<FeesSaleReceiveStorageEntity> feesList) {
		
		FeesSaleReceiveStorageEntity saleStorageFeeEntity=initFeeEntity(entity);
		try{
			entity.setCalculateTime(JAppContext.currentTimestamp());
			saleStorageFeeEntity.setCalculateTime(entity.getCalculateTime());
			String customerId=entity.getCustomerId();
			CalcuReqVo<PriceMaterialQuotationEntity> reqVo = new CalcuReqVo<PriceMaterialQuotationEntity>();
			reqVo.setBizData(entity);
			BillRuleReceiveEntity ruleEntity=mapRule.get(customerId);
			reqVo.setRuleNo(ruleEntity.getQuotationNo());
			reqVo.setRuleStr(ruleEntity.getRule());
			saleStorageFeeEntity.setRuleNo(ruleEntity.getQuotationNo());
			
			if(mapCusPrice.containsKey(customerId)){
				reqVo.setQuoEntites(mapCusPrice.get(customerId));
			}
		
			long start = System.currentTimeMillis();// 系统开始时间
			long current = 0L;// 当前系统时间
			//CalcuResultVo resultVo = feesCalcuService.FeesCalcuService(reqVo);
			CalcuResultVo resultVo = new CalcuResultVo();
			current = System.currentTimeMillis();
			if("succ".equals(resultVo.getSuccess())){
				XxlJobLogger.log("调用规则引擎   耗时【{0}】毫秒  费用【{1}】 ",(current - current),resultVo.getPrice());	
				PriceMaterialQuotationEntity matchEntity=queryMatchEntityById(mapCusPrice.get(customerId),resultVo.getQuoId());
				saleStorageFeeEntity.setUnitPrice(BigDecimal.valueOf(matchEntity.getUnitPrice()));
				saleStorageFeeEntity.setCost(resultVo.getPrice());
				entity.setRemark(entity.getRemark()+"计算成功;");
				saleStorageFeeEntity.setParam2(resultVo.getMethod());
				saleStorageFeeEntity.setParam3(resultVo.getQuoId());
				saleStorageFeeEntity.setIsCalculated(CalculateState.Finish.getCode());
				entity.setIsCalculated(CalculateState.Finish.getCode());
				feesList.add(saleStorageFeeEntity);
			}else{
				XxlJobLogger.log("调用规则引擎失败   耗时【{0}】毫秒   ",(current - start));
				XxlJobLogger.log("费用计算失败--"+resultVo.getMsg());
				saleStorageFeeEntity.setParam2(resultVo.getMethod());
				entity.setIsCalculated(CalculateState.Quote_Miss.getCode());
				saleStorageFeeEntity.setIsCalculated(CalculateState.Quote_Miss.getCode());
				entity.setRemark(entity.getRemark()+"费用计算失败:"+resultVo.getMsg()+";");
				feesList.add(saleStorageFeeEntity);
			}
			XxlJobLogger.log(String.format("====================================[%s]计算完毕============================================",entity.getId()));
			
		}catch(Exception ex){
			entity.setIsCalculated(CalculateState.Sys_Error.getCode());
			saleStorageFeeEntity.setIsCalculated(CalculateState.Sys_Error.getCode());
			entity.setRemark(entity.getRemark()+"费用计算异常:"+ex.getMessage()+";");
			feesList.add(saleStorageFeeEntity);
		}
		
	}
	
	private PriceMaterialQuotationEntity queryMatchEntityById(List<PriceMaterialQuotationEntity> materialList, String priceId) {
		PriceMaterialQuotationEntity matchEntity=null;
		for(PriceMaterialQuotationEntity entity:materialList){
			if(entity.getId().toString().equals(priceId)){
				matchEntity=entity;
				break;
			}
		}
		return matchEntity;
	}
	
	@Override
	protected void saveBatchData(List<BizOutstockPackmaterialEntity> billList,List<FeesSaleReceiveStorageEntity> feesList) {
		
		long start = System.currentTimeMillis();// 系统开始时间
		long current = 0L;// 当前系统时间
		bizWmsOutstockPackmaterialService.updateBatch(billList);
		feesSaleReceiveStorageService.insertBatch(feesList);
		current = System.currentTimeMillis();
		XxlJobLogger.log("保存费用数据耗时：【{0}】毫秒  ",(current - start));
	}

	@Override
	protected boolean validateData(BizOutstockPackmaterialEntity entity,List<FeesSaleReceiveStorageEntity> feesList) {
		XxlJobLogger.log("数据主键ID:【{0}】  ",entity.getId());
		long start = System.currentTimeMillis();// 系统开始时间
		long current = 0L;// 当前系统时间
		
		Timestamp time=JAppContext.currentTimestamp();
		entity.setCalculateTime(time);
		Map<String,Object> map=new HashMap<String,Object>();
		String customerId=entity.getCustomerId();
		FeesSaleReceiveStorageEntity saleStorageFeeEntity = initFeeEntity(entity);
		saleStorageFeeEntity.setCalculateTime(time);
		
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
			saleStorageFeeEntity.setIsCalculated(CalculateState.Contract_Miss.getCode());
			entity.setRemark(entity.getRemark()+String.format("未查询到合同  订单号【%s】--商家【%s】;", entity.getId(),customerId));
			feesList.add(saleStorageFeeEntity);
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
			saleStorageFeeEntity.setIsCalculated(CalculateState.Contract_Miss.getCode());
			entity.setRemark(entity.getRemark()+String.format("未签约服务")+";");
			feesList.add(saleStorageFeeEntity);
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
			saleStorageFeeEntity.setIsCalculated(CalculateState.Quote_Miss.getCode());
			entity.setRemark(entity.getRemark()+"报价未配置;");
			feesList.add(saleStorageFeeEntity);
			return false;
		}
		current = System.currentTimeMillis();
		XxlJobLogger.log("验证报价耗时：【{0}】毫秒  ",(current - start));
		start = System.currentTimeMillis();// 系统开始时间
		/*查找商家 规则*/
		map.clear();
		BillRuleReceiveEntity ruleEntity=null;
		if(mapRule.containsKey(customerId)){
			ruleEntity=mapRule.get(customerId);
		}else{
			map.put("customerid",customerId);
			map.put("subjectId", SubjectId);
		    ruleEntity=receiveRuleRepository.queryByCustomerId(map);
		    mapRule.put(customerId, ruleEntity);
		}
		
		if(ruleEntity == null){
			XxlJobLogger.log("规则未配置");
			entity.setIsCalculated(CalculateState.Quote_Miss.getCode());
			saleStorageFeeEntity.setIsCalculated(CalculateState.Quote_Miss.getCode());
			entity.setRemark(entity.getRemark()+"规则未配置;");
			feesList.add(saleStorageFeeEntity);
			return  false;
		}
		current = System.currentTimeMillis();
		XxlJobLogger.log("验证规则耗时：【{0}】毫秒  ",(current - start));
		return true;
	}
	
	private FeesSaleReceiveStorageEntity initFeeEntity(BizOutstockPackmaterialEntity entity){
		
		FeesSaleReceiveStorageEntity saleStorageFeeEntity = new FeesSaleReceiveStorageEntity();	
		saleStorageFeeEntity.setCreator("system");
		//费用表的创建时间应为业务表的创建时间
		saleStorageFeeEntity.setCreateTime(entity.getCreateTime());
		saleStorageFeeEntity.setOperateTime(entity.getCreateTime());
		saleStorageFeeEntity.setCustomerId(entity.getCustomerId());			//商家ID
		saleStorageFeeEntity.setCustomerName(entity.getCustomerName());		//商家名称
		saleStorageFeeEntity.setWarehouseCode(entity.getWarehouseCode());		//仓库ID
		saleStorageFeeEntity.setWarehouseName(entity.getWarehouseName());	//仓库名称
		saleStorageFeeEntity.setCostType("FEE_TYPE_MATERIAL");				//费用类别 FEE_TYPE_GENEARL-通用 FEE_TYPE_MATERIAL-耗材 FEE_TYPE_ADD-增值
		saleStorageFeeEntity.setSubjectCode(SubjectId);						//费用科目
		saleStorageFeeEntity.setOtherSubjectCode(SubjectId);
		if(entity.getNum()!=null){
			saleStorageFeeEntity.setQuantity(entity.getNum().intValue());	//商品数量
		}
		if(entity.getWeight()!=null){
			saleStorageFeeEntity.setWeight(BigDecimal.valueOf(entity.getWeight()));				//商品数量
		}
		saleStorageFeeEntity.setStatus("0");								//状态
		saleStorageFeeEntity.setWaybillNo(entity.getWaybillNo());
		entity.setCalculateTime(JAppContext.currentTimestamp());
		saleStorageFeeEntity.setCalculateTime(JAppContext.currentTimestamp());
		saleStorageFeeEntity.setFeesNo(entity.getFeesNo());
		saleStorageFeeEntity.setDelFlag("0");
		return saleStorageFeeEntity;
	}

	@Override
	protected void calcuStandardService(List<BizOutstockPackmaterialEntity> billList) {
		// TODO Auto-generated method stub
	}

}
