package com.jiuyescm.bms.calcu.receive.storage.instock;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.jiuyescm.bms.asyn.vo.BmsCalcuTaskVo;
import com.jiuyescm.bms.calcu.CalcuLog;
import com.jiuyescm.bms.calcu.receive.BmsContractBase;
import com.jiuyescm.bms.calculate.vo.CalcuBaseInfoVo;
import com.jiuyescm.bms.calculate.vo.CalcuInfoVo;
import com.jiuyescm.bms.common.enumtype.CalculateState;
import com.jiuyescm.bms.general.entity.BmsBizInstockInfoEntity;
import com.jiuyescm.bms.general.entity.FeesReceiveStorageEntity;
import com.jiuyescm.bms.general.service.IBmsBizInstockInfoRepository;
import com.jiuyescm.bms.general.service.IStorageQuoteFilterService;
import com.jiuyescm.bms.quotation.storage.entity.PriceGeneralQuotationEntity;
import com.jiuyescm.bms.quotation.storage.entity.PriceStepQuotationEntity;
import com.jiuyescm.bms.quotation.storage.repository.IPriceGeneralQuotationRepository;
import com.jiuyescm.bms.quotation.storage.repository.IPriceStepQuotationRepository;
import com.jiuyescm.cfm.common.JAppContext;
import com.jiuyescm.common.utils.DoubleUtil;
import com.jiuyescm.contract.quote.vo.ContractBizTypeEnum;
import com.jiuyescm.contract.quote.vo.ContractQuoteQueryInfoVo;

public class InstockCalcuJob extends BmsContractBase {

	private Logger logger = LoggerFactory.getLogger(InstockCalcuJob.class);
	
	@Autowired private IBmsBizInstockInfoRepository bmsBizInstockInfoRepository;
	@Autowired private IPriceStepQuotationRepository repository;
	@Autowired private IStorageQuoteFilterService storageQuoteFilterService;
	@Autowired private IPriceGeneralQuotationRepository priceGeneralQuotationRepository;
	
	
	
	private String quoTempleteCode = null;
	private PriceGeneralQuotationEntity quoTemplete = null;
	private Map<String, Object> errorMap = null;
	
	
	public InstockCalcuJob(BmsCalcuTaskVo taskVo,String contractAttr){
		super(taskVo, contractAttr);
		serviceSubjectCode = subjectCode;
		errorMap = new HashMap<String, Object>();
		initConf();
	}
	
	public void getQuoTemplete(){
		Map<String, Object> map = new HashMap<>();
		if(quoTempleteCode!=null){
			map.put("subjectId",serviceSubjectCode);
			map.put("quotationNo", quoTempleteCode);
			quoTemplete = priceGeneralQuotationRepository.query(map);
		}
	}
	
	public void initConf(){
		
	}
	
	public void calcu(){
		
	}
	
	private List<BmsBizInstockInfoEntity> queryList(Map<String, Object> map){
		List<BmsBizInstockInfoEntity> bizList = bmsBizInstockInfoRepository.getInStockInfoList(map);
		return bizList;
	}
	
	public FeesReceiveStorageEntity initFee(BmsBizInstockInfoEntity entity){
		
		FeesReceiveStorageEntity fee = new FeesReceiveStorageEntity();
		double num=DoubleUtil.isBlank(entity.getAdjustQty())?entity.getTotalQty():entity.getAdjustQty();
		if(!DoubleUtil.isBlank(num)){
			fee.setQuantity(num);//商品数量
		}
		//重量
		Double weight = DoubleUtil.isBlank(entity.getAdjustWeight())?entity.getTotalWeight():entity.getAdjustWeight();
		fee.setWeight(weight);
		
		//塞箱数
		Double box=DoubleUtil.isBlank(entity.getAdjustBox())?entity.getTotalBox():entity.getTotalBox();
		if(!DoubleUtil.isBlank(box)){
			fee.setBox(box.intValue());
		}
		fee.setCostType("FEE_TYPE_GENEARL");
		fee.setUnitPrice(0d);
		fee.setCost(new BigDecimal(0));	
		fee.setDelFlag("0");
		fee.setFeesNo(entity.getFeesNo());
		fee.setCalculateTime(JAppContext.currentTimestamp());
		cbiVo = new CalcuBaseInfoVo(taskVo.getTaskId(),"bizinfo",entity.getFeesNo(),taskVo.getSubjectCode(),fee.getCalculateTime());
		cbiVo.setData(entity);
		CalcuLog.printLog(cbiVo);//打印业务数据日志
		cbiVo.setData(fee);
		CalcuLog.printLog(cbiVo);//打印计费参数日志
		return fee;
		
	}
	
	public void bmsCalcu(BmsBizInstockInfoEntity entity,FeesReceiveStorageEntity fee){
		//合同校验
		if(contractInfo == null){
			fee.setIsCalculated(CalculateState.Contract_Miss.getCode());
			fee.setCalcuMsg("bms合同缺失");
			return;
		}
		
		if("fail".equals(quoTempleteCode)){
			fee.setIsCalculated(CalculateState.Contract_Miss.getCode());
			fee.setCalcuMsg("未签约服务");
		}
		
		if(quoTemplete == null){
			fee.setIsCalculated(CalculateState.Quote_Miss.getCode());
			fee.setCalcuMsg("报价模板缺失");
			return;
		}
		
		String priceType = quoTemplete.getPriceType();
		String unit = quoTemplete.getFeeUnitCode();//计费单位 
		double num = fee.getQuantity();
		CalcuInfoVo civo = new CalcuInfoVo();
		civo.setRuleNo("");
		civo.setChargeUnit(unit);
		switch (unit) {
		case "ITEMS":
			num = fee.getQuantity();
			break;
		case "CARTON":
			num = fee.getBox();
			break;
		case "TONS":
			double weight = 0d;
			if ((double)fee.getWeight()/1000 < 1) {
				weight = 1d;
			}else {
				weight = (double)fee.getWeight()/1000;
			}
			num = weight;
			break;
		case "KILOGRAM":
			num = fee.getWeight();
			break;
		default:
			break;
		}
		
		//计算方法
		double amount=0d;
		switch(priceType){
			case "PRICE_TYPE_NORMAL"://一口价		
				//打印报价
				//printLog(taskVo.getTaskId(), "quoteInfo", entity.getFeesNo(), taskVo.getSubjectName(), "", quoTemplete);
				civo.setChargeType("unitPrice");
				civo.setChargeDescrip("金额=单价*数量");
				amount=num*quoTemplete.getUnitPrice();
				fee.setUnitPrice(quoTemplete.getUnitPrice());
				fee.setParam3(quoTemplete.getId().toString());
				//printLog(vo.getTaskId(), "ruleInfo", entity.getFeesNo(), vo.getSubjectName(), "", civo);
				break;
			case "PRICE_TYPE_STEP"://阶梯价	
				civo.setChargeType("stepPrice");
				Map<String,Object> map=new HashMap<String,Object>();
				map.put("quotationId", quoTemplete.getId());
				map.put("num", fee.getQuantity());//根据报价单位判断			
				//查询出的所有子报价
				List<PriceStepQuotationEntity> list=repository.queryPriceStepByQuatationId(map);
				
				if(list==null || list.size() == 0){
					logger.info(entity.getId()+"阶梯报价未配置");
					fee.setIsCalculated(CalculateState.Quote_Miss.getCode());
					fee.setCalcuMsg("阶梯报价未配置");
					return;
				}
				
				//封装数据的仓库和温度
				map.clear();
				map.put("warehouse_code", entity.getWarehouseCode());
				PriceStepQuotationEntity stepQuoEntity=storageQuoteFilterService.quoteFilter(list, map);			
				
				if(stepQuoEntity==null){
					//printLog(vo.getTaskId(), "quoteInfo", entity.getFeesNo(), vo.getSubjectName(), "阶梯报价未配置", null);
					fee.setIsCalculated(CalculateState.Quote_Miss.getCode());
					fee.setCalcuMsg("阶梯报价未配置");
					return;
				}
				//printLog(vo.getTaskId(), "quoteInfo", entity.getFeesNo(), vo.getSubjectName(), "", stepQuoEntity);
				
				if(!DoubleUtil.isBlank(stepQuoEntity.getUnitPrice())){
					civo.setChargeType("unitPrice");
					civo.setChargeDescrip("金额=单价*数量");
					fee.setUnitPrice(stepQuoEntity.getUnitPrice());
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
				//printLog(vo.getTaskId(), "ruleInfo", entity.getFeesNo(), vo.getSubjectName(), "", civo);
				fee.setParam3(stepQuoEntity.getId()+"");
				break;
			default:
				break;
		}
		fee.setCost(BigDecimal.valueOf(amount));
		fee.setParam4(priceType);
		entity.setRemark(entity.getRemark()+"计算成功;");
		entity.setIsCalculated(CalculateState.Finish.getCode());
		fee.setCalcuMsg("计算成功");
		fee.setIsCalculated(CalculateState.Finish.getCode());
	}
	
	protected ContractQuoteQueryInfoVo getCtConditon(BmsCalcuTaskVo vo,BmsBizInstockInfoEntity entity) {
		ContractQuoteQueryInfoVo queryVo = new ContractQuoteQueryInfoVo();
		queryVo.setCustomerId(entity.getCustomerId());
		queryVo.setBizTypeCode(ContractBizTypeEnum.STORAGE.getCode());
		queryVo.setSubjectCode(vo.getSubjectCode());
		queryVo.setCurrentTime(entity.getCreateTime());
		queryVo.setWarehouseCode(entity.getWarehouseCode());
		return queryVo;

	}
	
}
