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
import com.jiuyescm.bms.biz.storage.entity.BizOutstockPackmaterialEntity;
import com.jiuyescm.bms.drools.IFeesCalcuService;
import com.jiuyescm.bms.common.enumtype.CalculateState;
import com.jiuyescm.bms.general.entity.FeesPayStorageEntity;
import com.jiuyescm.bms.general.entity.PackCostReportEntity;
import com.jiuyescm.bms.general.service.IFeesPayStorageService;
import com.jiuyescm.bms.general.service.IPackCostReportService;
import com.jiuyescm.bms.general.service.SequenceService;
import com.jiuyescm.bms.receivable.storage.service.IBizOutstockPackmaterialService;
import com.jiuyescm.cfm.common.JAppContext;
import com.jiuyescm.common.utils.DateUtil;
import com.xxl.job.core.handler.annotation.JobHander;
import com.xxl.job.core.log.XxlJobLogger;

/**
 * 
 * @author wuliangfeng 耗材成本计算任务
 *
 */
@JobHander(value="materialCostCalcJob")
@Service
public class MaterialCostCalcJob extends CommonCalcJob<BizOutstockPackmaterialEntity,FeesPayStorageEntity>{

	@Autowired private IBizOutstockPackmaterialService bizOutstockPackmaterialService;
	@Autowired private IPackCostReportService packCostReportService;
	@Autowired private IFeesPayStorageService feesPayStorageService;
	@Autowired private SequenceService sequenceService;
	@Autowired private IFeesCalcuService feesCalcuService;
	
	private String BizTypeCode = "STORAGE"; //仓储费编码
	private String SubjectId = "wh_material";//费用类型-耗材使用成本
	Timestamp currentTime = null;
	
	@Override
	protected List<BizOutstockPackmaterialEntity> queryBillList(
			Map<String, Object> map) {
		XxlJobLogger.log("materialCostCalcJob查询条件map:【{0}】  ",map);
		List<BizOutstockPackmaterialEntity> bizList = bizOutstockPackmaterialService.queryCost(map);
		if(bizList!=null && bizList.size()>0){
			List<String> feesNos = new ArrayList<String>();
			for(BizOutstockPackmaterialEntity entity:bizList){
				if(StringUtils.isNotEmpty(entity.getCostFeesNo())){
					feesNos.add(entity.getCostFeesNo());
				}else{
					entity.setCostFeesNo(sequenceService.getBillNoOne(FeesPayStorageEntity.class.getName(), "PSTO", "0000000000"));
				}
			}
			if(feesNos.size()>0){
				feesPayStorageService.deleteByFeesNo(feesNos);
			}
		}
		return bizList;
	}

	
	@Override
	protected void initConf(List<BizOutstockPackmaterialEntity> billList) throws Exception {
		/*Calendar ca = Calendar.getInstance();
		ca.setTime(new Date());
		ca.add(Calendar.MONTH, -1);
		int year=ca.get(Calendar.YEAR);
		int month=ca.get(Calendar.MONTH)+1;
		materialCostList=packCostReportService.queryAllByYearMonth(year, month);
		
		currentTime = new Timestamp(System.currentTimeMillis());*/
	}

	@Override
	protected void calcuService(BizOutstockPackmaterialEntity t,List<FeesPayStorageEntity> feesList) {
		XxlJobLogger.log("仓库【{0}】 商家【{1}】 运单号【{2}】耗材【{3}】",t.getWarehouseName(),t.getCustomerName(),t.getWaybillNo(),t.getConsumerMaterialCode());
		FeesPayStorageEntity feesPayStorageEntity=getFeesPayStorageEntity(t);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("warehouseNo", t.getWarehouseCode());
		map.put("year",DateUtil.getYear(t.getCreateTime()));
		map.put("monthNum", DateUtil.getMonth(t.getCreateTime()));
		map.put("materialNo", t.getConsumerMaterialCode());
		
		try{
			List<PackCostReportEntity> packCostList = packCostReportService.queryCostReportEntity(map);
			if(packCostList==null||packCostList.size()==0){
				feesPayStorageEntity.setIsCalculated(CalculateState.Quote_Miss.getCode());
				feesPayStorageEntity.setCalculateTime(currentTime);
				t.setCostIsCalculated(CalculateState.Quote_Miss.getCode());
				t.setCostRemark("无报价");
				t.setCostCalculateTime(currentTime);
				feesList.add(feesPayStorageEntity);
				XxlJobLogger.log("未查询到报价");
				return;
			}
			if(packCostList.size()>1){//重复成本价
				feesPayStorageEntity.setIsCalculated(CalculateState.Quote_More.getCode());
				feesPayStorageEntity.setCalculateTime(currentTime);
				
				t.setCostIsCalculated(CalculateState.Quote_More.getCode());
				t.setCostCalculateTime(currentTime);
				t.setCostRemark("重复成本价格");
				feesList.add(feesPayStorageEntity);
				XxlJobLogger.log("查询到多个报价");
				return;
			}
			PackCostReportEntity costEntity=packCostList.get(0);
			double totalCost=0L;
			//有重量
			if(t.getConsumerMaterialCode().endsWith("-GB")){
				totalCost=t.getWeight()*costEntity.getCost().doubleValue();//重量*单价
				feesPayStorageEntity.setParam2(""+t.getWeight()+"*"+costEntity.getCost().doubleValue()+"");
			}else{
				totalCost=t.getNum()*costEntity.getCost().doubleValue();//数量*单价
				feesPayStorageEntity.setParam2(""+t.getNum()+"*"+costEntity.getCost().doubleValue()+"");
			}
			feesPayStorageEntity.setCost(BigDecimal.valueOf(totalCost));
			feesPayStorageEntity.setUnitPrice(costEntity.getCost());
			feesPayStorageEntity.setExternalProductNo(costEntity.getMaterialNo());
		
			feesPayStorageEntity.setParam3(String.valueOf(costEntity.getId()));
			feesPayStorageEntity.setIsCalculated(CalculateState.Finish.getCode());
			feesPayStorageEntity.setCalculateTime(currentTime);
			feesPayStorageEntity.setUnit(costEntity.getUnit());
			
			t.setCostIsCalculated(CalculateState.Finish.getCode());
			t.setCostCalculateTime(currentTime);
			t.setCostRemark("计算成功");
			feesList.add(feesPayStorageEntity);

		}catch(Exception ex){
			t.setCostIsCalculated(CalculateState.Sys_Error.getCode());
			t.setCostCalculateTime(currentTime);
			feesPayStorageEntity.setIsCalculated(CalculateState.Sys_Error.getCode());
			t.setCostRemark("费用计算异常:"+ex.getMessage());
			feesList.add(feesPayStorageEntity);
		}
	}

	/*private List<PackCostReportEntity> matchPackCost(BizOutstockPackmaterialEntity t) {
		List<PackCostReportEntity> list=new ArrayList<PackCostReportEntity>();
		for(PackCostReportEntity entity:materialCostList){
			if(entity.getWarehouseNo().equals(t.getWarehouseCode())&&
					entity.getBarcode().equals(t.getConsumerMaterialCode())){
				list.add(entity);
			}
		}
		return list;
	}*/

	@Override
	protected void saveBatchData(List<BizOutstockPackmaterialEntity> billList,
			List<FeesPayStorageEntity> feesList) {
		bizOutstockPackmaterialService.updateBatch(billList);
		feesPayStorageService.addBatch(feesList);
	}

	@Override
	protected boolean validateData(BizOutstockPackmaterialEntity t,List<FeesPayStorageEntity> feesList) {
		
		/*FeesPayStorageEntity feesPayStorageEntity=getFeesPayStorageEntity(t);
		//验证成本报价是否存在
		if(materialCostList==null){
			feesPayStorageEntity.setIsCalculated(CalculateState.Quote_Miss.getCode());
			feesPayStorageEntity.setCalculateTime(currentTime);
			
			t.setCostIsCalculated(CalculateState.Quote_Miss.getCode());
			t.setCostCalculateTime(currentTime);
			t.setCostRemark("报价列表为空");
			feesList.add(feesPayStorageEntity);
			return false;
		}*/
		//验证获取规则
		return true;
	}

	private FeesPayStorageEntity getFeesPayStorageEntity(BizOutstockPackmaterialEntity entity){
		FeesPayStorageEntity model=new FeesPayStorageEntity();
		model.setBillNo("");
		model.setBizId(entity.getId().toString());
		model.setBizType(BizTypeCode);
		model.setCalculateTime(JAppContext.currentTimestamp());
		model.setContinuedPrice(null);
		model.setCost(null);
		model.setCostType("FEE_TYPE_MATERIAL");
		model.setSubjectCode(SubjectId);
		model.setOtherSubjectCode(SubjectId);
		model.setCreateTime(entity.getCreateTime());
		model.setCreator("system");
		model.setCustomerId(entity.getCustomerId());
		model.setCustomerName(entity.getCustomerName());
		model.setDelFlag("0");
		model.setDerateAmount(BigDecimal.ZERO);
		model.setExternalProductNo(entity.getConsumerMaterialCode());
		model.setFeesNo(entity.getCostFeesNo());
		//model.setIsCalculated(CalculateState.Begin.getCode());
		model.setOperateTime(entity.getCreateTime());
		model.setOrderNo(entity.getOutstockNo());
		model.setProductNo(entity.getConsumerMaterialCode());
		model.setProductName(entity.getConsumerMaterialName());
		model.setWarehouseCode(entity.getWarehouseCode());
		model.setWarehouseName(entity.getWarehouseName());
		model.setWaybillNo(entity.getWaybillNo());
		model.setWeight(BigDecimal.valueOf(entity.getWeight()));
		model.setCost(BigDecimal.ZERO);
		model.setQuantity(entity.getNum().intValue());
		return model;
	}
	@Override
	protected void calcuStandardService(List<BizOutstockPackmaterialEntity> billList) {
		// TODO Auto-generated method stub
	}

}
