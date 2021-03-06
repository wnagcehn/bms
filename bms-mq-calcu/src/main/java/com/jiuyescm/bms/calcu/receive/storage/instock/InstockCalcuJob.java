package com.jiuyescm.bms.calcu.receive.storage.instock;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import com.jiuyescm.bms.asyn.service.IBmsCalcuTaskService;
import com.jiuyescm.bms.asyn.vo.BmsCalcuTaskVo;
import com.jiuyescm.bms.calcu.CalcuLog;
import com.jiuyescm.bms.calcu.base.ICalcuService;
import com.jiuyescm.bms.calcu.receive.CommonService;
import com.jiuyescm.bms.calcu.receive.ContractCalcuService;
import com.jiuyescm.bms.calcu.receive.BmsContractBase;
import com.jiuyescm.bms.calculate.api.IBmsCalcuService;
import com.jiuyescm.bms.calculate.vo.BmsFeesQtyVo;
import com.jiuyescm.bms.calculate.vo.CalcuContractVo;
import com.jiuyescm.bms.calculate.vo.CalcuInfoVo;
import com.jiuyescm.bms.common.enumtype.CalculateState;
import com.jiuyescm.bms.general.entity.BmsBizInstockInfoEntity;
import com.jiuyescm.bms.general.entity.FeesReceiveStorageEntity;
import com.jiuyescm.bms.general.service.IBmsBizInstockInfoRepository;
import com.jiuyescm.bms.general.service.IFeesReceiveStorageService;
import com.jiuyescm.bms.general.service.IStorageQuoteFilterService;
import com.jiuyescm.bms.quotation.storage.entity.PriceGeneralQuotationEntity;
import com.jiuyescm.bms.quotation.storage.entity.PriceStepQuotationEntity;
import com.jiuyescm.bms.quotation.storage.repository.IPriceGeneralQuotationRepository;
import com.jiuyescm.bms.quotation.storage.repository.IPriceStepQuotationRepository;
import com.jiuyescm.cfm.common.JAppContext;
import com.jiuyescm.common.utils.DoubleUtil;
import com.jiuyescm.constants.CalcuNodeEnum;
import com.jiuyescm.contract.quote.vo.ContractBizTypeEnum;
import com.jiuyescm.contract.quote.vo.ContractQuoteQueryInfoVo;

@Component("instockCalcuJob")
@Scope("prototype")
public class InstockCalcuJob extends BmsContractBase implements ICalcuService<BmsBizInstockInfoEntity,FeesReceiveStorageEntity> {

	private Logger logger = LoggerFactory.getLogger(InstockCalcuJob.class);
		
	@Autowired private IBmsBizInstockInfoRepository bmsBizInstockInfoRepository;
	@Autowired private IPriceStepQuotationRepository repository;
	@Autowired private IStorageQuoteFilterService storageQuoteFilterService;
	@Autowired private IPriceGeneralQuotationRepository priceGeneralQuotationRepository;
	@Autowired private IFeesReceiveStorageService feesReceiveStorageService;
	@Autowired private ContractCalcuService contractCalcuService;
	@Autowired private CommonService commonService;
	@Autowired IBmsCalcuTaskService bmsCalcuTaskService;
	@Autowired IBmsCalcuService bmsCalcuService;


	//private String quoTempleteCode = null;
	private PriceGeneralQuotationEntity quoTemplete = null;
	private Map<String, Object> errorMap = null;
	private List<BmsBizInstockInfoEntity> bizList =null;
	private List<FeesReceiveStorageEntity> fees=null;

	public void process(BmsCalcuTaskVo taskVo,String contractAttr){
		super.process(taskVo, contractAttr);
		getQuoTemplete();
		serviceSubjectCode = subjectCode;
		initConf();
	}
	
	@Override
	public void getQuoTemplete(){
	    /*Map<String, Object> map = new HashMap<>();
		if(quoTempleteCode!=null){
			map.put("subjectId",serviceSubjectCode);
			map.put("quotationNo", quoTempleteCode);
			quoTemplete = priceGeneralQuotationRepository.query(map);
		}*/
	}
	
	@Override
	public void initConf(){
		
	}
	
	@Override
	public void calcu(Map<String, Object> map){
	    int count=(int) map.get("num");
        //原始进来的数量
        int recount=count;
        while(count == recount){
          count = calcuDetail(map);
        }
        calcuDetail(map);   
	}
	
	
	private int calcuDetail(Map<String, Object> map){
        bizList = bmsBizInstockInfoRepository.getInStockInfoList(map);
        fees = new ArrayList<>();
        if(bizList == null || bizList.size() == 0){
            commonService.taskCountReport(taskVo, "STORAGE");
            return 0;
        }
        logger.info("taskId={} 查询行数【{}】",taskVo.getTaskId(),bizList.size());
        for (BmsBizInstockInfoEntity entity : bizList) {
            errorMap = new HashMap<String, Object>();
            FeesReceiveStorageEntity fee = initFee(entity);
            try {
                fees.add(fee);
                if(isNoExe(entity, fee)){
                    continue; //如果不计算费用,后面的逻辑不在执行，只是在最后更新数据库状态
                }       
                //优先合同在线计算
                calcuForContract(entity,fee);
                //如果返回的是合同缺失，则继续BMS计算
                if("CONTRACT_LIST_NULL".equals(errorMap.get("code"))){
                    calcuForBms(entity,fee);
                    //报价缺失或者未订购服务的走标准报价
                    if("4".equals(fee.getIsCalculated()) || "7".equals(fee.getIsCalculated())){
                        calcuForStand(entity, fee);
                    }
                }
            } catch (Exception e) {
                // TODO: handle exception
                fee.setIsCalculated(CalculateState.Sys_Error.getCode());
                fee.setCalcuMsg("系统异常");
                logger.error("计算异常",e);
            }
        }
        updateBatch(bizList,fees);
        calceCount += bizList.size();
        //更新任务计算各字段
        updateTask(taskVo,calceCount);  
        int taskRate = (int)Math.floor((calceCount*100)/unCalcuCount);
        try {
            if(unCalcuCount!=0){
                bmsCalcuTaskService.updateRate(taskVo.getTaskId(), taskRate);
            }
        } catch (Exception e) {
            logger.error("更新任务进度异常",e);
        }
        return bizList.size();
	}
	
	private void updateTask(BmsCalcuTaskVo taskVo,int calcuCount){
		try {
			BmsFeesQtyVo feesQtyVo = bmsCalcuService.queryFeesQtyForStoInstock(taskVo.getCustomerId(), taskVo.getSubjectCode(), taskVo.getCreMonth());
			taskVo.setUncalcuCount(feesQtyVo.getUncalcuCount()==null?0:feesQtyVo.getUncalcuCount());//本次待计算的费用数
			taskVo.setCalcuCount(calcuCount);
			taskVo.setBeginCount(feesQtyVo.getBeginCount()==null?0:feesQtyVo.getBeginCount());//未计算费用总数
			taskVo.setFinishCount(feesQtyVo.getFinishCount()==null?0:feesQtyVo.getFinishCount());//计算成功总数
			taskVo.setSysErrorCount(feesQtyVo.getSysErrorCount()==null?0:feesQtyVo.getSysErrorCount());//系统错误用总数
			taskVo.setContractMissCount(feesQtyVo.getContractMissCount()==null?0:feesQtyVo.getContractMissCount());//合同缺失总数
			taskVo.setQuoteMissCount(feesQtyVo.getQuoteMissCount()==null?0:feesQtyVo.getQuoteMissCount());//报价缺失总数
			taskVo.setNoExeCount(feesQtyVo.getNoExeCount()==null?0:feesQtyVo.getNoExeCount());//不计算费用总数
			taskVo.setCalcuStatus(feesQtyVo.getCalcuStatus());
			bmsCalcuTaskService.update(taskVo);
		} catch (Exception e) {
			logger.error("更新任务统计信息异常",e);
		}
	}
	
	@Override
	public FeesReceiveStorageEntity initFee(BmsBizInstockInfoEntity entity){
		//打印业务数据日志
		cbiVo.setFeesNo(entity.getFeesNo());
		CalcuLog.printLog(CalcuNodeEnum.BIZ.getCode().toString(), "", entity, cbiVo);
		FeesReceiveStorageEntity fee = new FeesReceiveStorageEntity();
		fee.setQuantity(0d);
		fee.setWeight(0d);
		fee.setBox(0);
		double num=DoubleUtil.isBlank(entity.getAdjustQty())?entity.getTotalQty():entity.getAdjustQty();
		if(!DoubleUtil.isBlank(num)){
			fee.setQuantity(num);//商品数量
		}
		//重量
		Double weight = DoubleUtil.isBlank(entity.getAdjustWeight())?entity.getTotalWeight():entity.getAdjustWeight();
		fee.setWeight(weight);
		
		//箱数
		Double box=DoubleUtil.isBlank(entity.getAdjustBox())?entity.getTotalBox():entity.getTotalBox();
		if(!DoubleUtil.isBlank(box)){
			fee.setBox(box.intValue());
		}
		fee.setCostType("FEE_TYPE_GENEARL");
		fee.setUnitPrice(0d);
		fee.setCost(new BigDecimal(0));	
		fee.setDelFlag("0");
		fee.setFeesNo(entity.getFeesNo());
		fee.setSubjectCode(subjectCode);
		fee.setCalculateTime(JAppContext.currentTimestamp());
		fee.setCreateTime(entity.getCreateTime());
		CalcuLog.printLog(CalcuNodeEnum.CHARGE.getCode().toString(), "", fee, cbiVo);
		return fee;
		
	}
	
	@Override
	public boolean isNoExe(BmsBizInstockInfoEntity entity,FeesReceiveStorageEntity fee){
		return false;
	}
	
	@Override
	public void calcuForBms(BmsBizInstockInfoEntity entity,FeesReceiveStorageEntity fee){
	    fee.setContractAttr("1");
	    //合同校验
		if(contractList.size()<=0){
			fee.setIsCalculated(CalculateState.Contract_Miss.getCode());
			fee.setCalcuMsg("bms合同缺失");
			CalcuLog.printLog(CalcuNodeEnum.CONTRACT.getCode().toString(), "bms合同缺失", null, cbiVo);
			return;
		}
		
		  //业务时间和合同时间进行匹配
        //合同
        CalcuContractVo contract=null;
        for(CalcuContractVo con:contractList){
            if(con.getStartDate().getTime()<=entity.getCreateTime().getTime() && entity.getCreateTime().getTime()<=con.getExpireDate().getTime()){
                contract=con;
                break;
            }
        }
        
        if(contract==null){
            fee.setIsCalculated(CalculateState.Contract_Miss.getCode());
            fee.setCalcuMsg("bms合同缺失");
            return;
        }
       
        logger.info("合同信息{}",contract.getContractNo());

        
        //模板编号
        String quoTempleteCode=contract.getModelNo();
        		
	
		if("fail".equals(quoTempleteCode)){
			fee.setIsCalculated(CalculateState.No_Dinggou.getCode());
			
			fee.setCalcuMsg("商家【"+taskVo.getCustomerName()+"】未订购科目【"+taskVo.getSubjectName()+"】的服务项!");
			CalcuLog.printLog(CalcuNodeEnum.CONTRACT.getCode().toString(), "商家【"+taskVo.getCustomerName()+"】未订购科目【"+taskVo.getSubjectName()+"】的服务项!", contract, cbiVo);
			return;
		}
		
	      
        //查询报价模板
        Map<String, Object> con = new HashMap<>();
        con.put("subjectId",serviceSubjectCode);
        con.put("quotationNo", quoTempleteCode);
        quoTemplete = priceGeneralQuotationRepository.query(con);   
		
		if(quoTemplete == null){
			fee.setIsCalculated(CalculateState.Quote_Miss.getCode());
			fee.setCalcuMsg("报价模板缺失");
			CalcuLog.printLog(CalcuNodeEnum.CONTRACT.getCode().toString(), "报价模板缺失", contract, cbiVo);
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
				CalcuLog.printLog(CalcuNodeEnum.QUOTE.getCode().toString(), "模板报价", quoTemplete, cbiVo);
				civo.setChargeType("unitPrice");
				civo.setChargeDescrip("金额=单价*数量");
				amount=num*quoTemplete.getUnitPrice();
				fee.setUnitPrice(quoTemplete.getUnitPrice());
				fee.setParam3(quoTemplete.getId().toString());
				CalcuLog.printLog(CalcuNodeEnum.CALCU.getCode().toString(), "模板单价计算", civo, cbiVo);
				break;
			case "PRICE_TYPE_STEP"://阶梯价
				if (DoubleUtil.isBlank(num)) {
					fee.setIsCalculated(CalculateState.Sys_Error.getCode());
					fee.setCalcuMsg("计费数据缺失");
					return;
				}
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
						civo.setChargeType("capPrice");
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
		fee.setCalcuMsg("计算成功");
		fee.setIsCalculated(CalculateState.Finish.getCode());
		fee.setCalculateTime(JAppContext.currentTimestamp());
		
		if(fee.getCost().compareTo(BigDecimal.ZERO) == 1){
            fee.setCalcuMsg("计算成功");
            logger.info("计算成功，费用【{}】",fee.getCost());
        }
        else{
            logger.info("计算不成功，费用【{}】",fee.getCost());
            fee.setCalcuMsg("单价配置为0或者计费数量/重量为0");
        }
	}
	
	@Override
	public void calcuForContract(BmsBizInstockInfoEntity entity,FeesReceiveStorageEntity fee){
		fee.setContractAttr("2");
	    ContractQuoteQueryInfoVo queryVo = getCtConditon(entity);
		contractCalcuService.calcuForContract(entity, fee, taskVo, errorMap, queryVo,cbiVo,fee.getFeesNo());
		if("succ".equals(errorMap.get("success").toString())){
            fee.setIsCalculated(CalculateState.Finish.getCode());
			if(fee.getCost().compareTo(BigDecimal.ZERO) == 1){
				fee.setCalcuMsg("计算成功");
				logger.info("计算成功，费用【{}】",fee.getCost());
			}
			else{
				logger.info("计算不成功，费用【{}】",fee.getCost());
				fee.setCalcuMsg("单价配置为0或者计费数量/重量为0");
			}
		}
		else{
			fee.setIsCalculated(errorMap.get("is_calculated").toString());
			fee.setCalcuMsg(errorMap.get("msg").toString());
		}
		if(errorMap.get("isStandard")!=null){
            fee.setContractAttr("3");
            if("succ".equals(errorMap.get("success").toString()) && fee.getCost().compareTo(BigDecimal.ZERO) == 1){     
                fee.setCalcuMsg("采用标准报价计费");
                logger.info("采用标准报价计费计算成功，费用【{}】",fee.getCost());                 
            }
		}
	}
	
	
	@Override
    public void calcuForStand(BmsBizInstockInfoEntity entity,FeesReceiveStorageEntity fee){
        fee.setContractAttr("1");
        ContractQuoteQueryInfoVo queryVo = getCtConditon(entity);
        contractCalcuService.calcuForStand(entity, fee, taskVo, errorMap, queryVo,cbiVo,fee.getFeesNo());
        if("succ".equals(errorMap.get("success").toString())){
            fee.setIsCalculated(CalculateState.Finish.getCode());
            if(fee.getCost().compareTo(BigDecimal.ZERO) == 1){
                fee.setCalcuMsg("标准报价计算成功");
                logger.info("标准报价计算成功，费用【{}】",fee.getCost());
            }
            else{
                logger.info("计算不成功，费用【{}】",fee.getCost());
                fee.setCalcuMsg("单价配置为0或者计费数量/重量为0");
            }
        }
        else{
            fee.setIsCalculated(errorMap.get("is_calculated").toString());
            fee.setCalcuMsg(errorMap.get("msg").toString());
        }
    }
	
	@Override
	public ContractQuoteQueryInfoVo getCtConditon(BmsBizInstockInfoEntity entity) {
		ContractQuoteQueryInfoVo queryVo = new ContractQuoteQueryInfoVo();
		queryVo.setCustomerId(entity.getCustomerId());
		queryVo.setBizTypeCode(ContractBizTypeEnum.STORAGE.getCode());
		queryVo.setSubjectCode(subjectCode);
		queryVo.setCurrentTime(entity.getCreateTime());
		queryVo.setWarehouseCode(entity.getWarehouseCode());
		return queryVo;

	}
	
	@Override
	public void updateBatch(List<BmsBizInstockInfoEntity> bizList,List<FeesReceiveStorageEntity> feeList) {
		
		StopWatch sw = new StopWatch();
		sw.start();
		feesReceiveStorageService.updateFee(feeList);
		sw.stop();
		logger.info("taskId={} 更新仓储费用行数【{}】 耗时【{}】",taskVo.getTaskId(),feeList.size(),sw.getLastTaskTimeMillis());
	}


	

	
	
}
