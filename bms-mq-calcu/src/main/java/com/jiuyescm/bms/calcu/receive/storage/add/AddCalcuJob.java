package com.jiuyescm.bms.calcu.receive.storage.add;

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
import com.jiuyescm.bms.base.dictionary.entity.SystemCodeEntity;
import com.jiuyescm.bms.base.group.service.IBmsGroupSubjectService;
import com.jiuyescm.bms.calcu.CalcuLog;
import com.jiuyescm.bms.calcu.base.ICalcuService;
import com.jiuyescm.bms.calcu.receive.BmsContractBase;
import com.jiuyescm.bms.calcu.receive.CommonService;
import com.jiuyescm.bms.calcu.receive.ContractCalcuService;
import com.jiuyescm.bms.calculate.api.IBmsCalcuService;
import com.jiuyescm.bms.calculate.vo.BmsFeesQtyVo;
import com.jiuyescm.bms.calculate.vo.CalcuContractVo;
import com.jiuyescm.bms.common.enumtype.CalculateState;
import com.jiuyescm.bms.drools.IFeesCalcuService;
import com.jiuyescm.bms.general.entity.BizAddFeeEntity;
import com.jiuyescm.bms.general.entity.FeesReceiveStorageEntity;
import com.jiuyescm.bms.general.service.IBizAddFeeService;
import com.jiuyescm.bms.general.service.IFeesReceiveStorageService;
import com.jiuyescm.bms.general.service.IPriceContractInfoService;
import com.jiuyescm.bms.general.service.ISystemCodeService;
import com.jiuyescm.bms.general.service.SequenceService;
import com.jiuyescm.bms.quotation.contract.repository.imp.IPriceContractItemRepository;
import com.jiuyescm.bms.quotation.storage.entity.PriceExtraQuotationEntity;
import com.jiuyescm.bms.quotation.storage.repository.IPriceExtraQuotationRepository;
import com.jiuyescm.bms.quotation.transport.entity.GenericTemplateEntity;
import com.jiuyescm.bms.quotation.transport.repository.IGenericTemplateRepository;
import com.jiuyescm.bms.rule.receiveRule.repository.IReceiveRuleRepository;
import com.jiuyescm.cfm.common.JAppContext;
import com.jiuyescm.common.utils.DoubleUtil;
import com.jiuyescm.constants.CalcuNodeEnum;
import com.jiuyescm.contract.quote.api.IContractQuoteInfoService;
import com.jiuyescm.contract.quote.vo.ContractBizTypeEnum;
import com.jiuyescm.contract.quote.vo.ContractQuoteQueryInfoVo;

@Component("addCalcuJob")
@Scope("prototype")
public class AddCalcuJob extends BmsContractBase implements ICalcuService<BizAddFeeEntity,FeesReceiveStorageEntity> {

	private Logger logger = LoggerFactory.getLogger(AddCalcuJob.class);
		
	@Autowired private IContractQuoteInfoService contractQuoteInfoService;
	@Autowired private IPriceContractInfoService jobPriceContractInfoService;
	@Autowired private IFeesReceiveStorageService feesReceiveStorageService;
	@Autowired private IReceiveRuleRepository receiveRuleRepository;
	@Autowired private IFeesCalcuService feesCalcuService;
	@Autowired private IPriceContractItemRepository priceContractItemRepository;
	@Autowired private ContractCalcuService contractCalcuService;
	@Autowired private SequenceService sequenceService;
	@Autowired private IBizAddFeeService bizAddFeeService;
	@Autowired private IGenericTemplateRepository genericTemplateRepository;
	@Autowired private IPriceExtraQuotationRepository priceExtraQuotationRepository;
	@Autowired private IBmsGroupSubjectService bmsGroupSubjectService;
	@Autowired private CommonService commonService;
	@Autowired IBmsCalcuTaskService bmsCalcuTaskService;
	@Autowired IBmsCalcuService bmsCalcuService;
	@Autowired private ISystemCodeService systemCodeService;


	//private String quoTempleteCode = null;
	private Map<String, Object> errorMap = null;
	private GenericTemplateEntity addQuoTemplete;
	private List<String> noCalList=null;
	private List<BizAddFeeEntity> bizList =null;
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
			map.put("templateCode", quoTempleteCode);
			addQuoTemplete = genericTemplateRepository.query(map);
		}*/
	}
	
	@Override
	public void initConf(){
	    noCalList = new ArrayList<>();
	    Map<String, Object> map = new HashMap<String, Object>();	        
        //获取物流商id与配送科目的映射关系  <物流商ID,费用科目>
        map.put("typeCode", "NO_CALCULATE_STORAGE_ADD");
        List<SystemCodeEntity> scList = systemCodeService.querySysCodes(map);
        for (SystemCodeEntity systemCodeEntity : scList) {
            noCalList.add(systemCodeEntity.getCode());
        }
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
        bizList = bizAddFeeService.querybizAddFee(map);
        fees = new ArrayList<>();
        if(bizList == null || bizList.size() == 0){
            commonService.taskCountReport(taskVo, "STORAGE");
            return 0;
        }
        logger.info("taskId={} 查询行数【{}】",taskVo.getTaskId(),bizList.size());
        for (BizAddFeeEntity entity : bizList) {
            errorMap = new HashMap<String, Object>();
            FeesReceiveStorageEntity fee = initFee(entity);
            fees.add(fee);
            try {
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
			BmsFeesQtyVo feesQtyVo = bmsCalcuService.queryFeesQtyForStoAdd(taskVo.getCustomerId(), taskVo.getSubjectCode(), taskVo.getCreMonth());
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
	public FeesReceiveStorageEntity initFee(BizAddFeeEntity entity){
		//打印业务数据日志
		CalcuLog.printLog(CalcuNodeEnum.BIZ.getCode().toString(), "", entity, cbiVo);
		FeesReceiveStorageEntity fee = new FeesReceiveStorageEntity();
    	fee.setFeesNo(entity.getFeesNo());
    	fee.setCost(new BigDecimal(0));
    	fee.setCalculateTime(JAppContext.currentTimestamp());
		fee.setUnitPrice(entity.getUnitPrice());
		fee.setSubjectCode("wh_value_add_subject");
		fee.setOtherSubjectCode(entity.getFeesType());
		fee.setUnit(entity.getFeesUnit());
		double num=DoubleUtil.isBlank(entity.getAdjustNum())?entity.getNum():entity.getAdjustNum();
		if(!DoubleUtil.isBlank(num)){
			fee.setQuantity(num);
		}
        fee.setParam1(entity.getItem());
		fee.setCostType("FEE_TYPE_GENEARL");
		fee.setCreateTime(entity.getCreateTime());
		return fee;
	}
	
	@Override
	public boolean isNoExe(BizAddFeeEntity entity,FeesReceiveStorageEntity fee){
	    
	    //不计算的费用类型
	    if(noCalList.contains(entity.getFeesType())){
            fee.setIsCalculated(CalculateState.No_Exe.getCode());           
            fee.setCalcuMsg("此费用类型是【"+entity.getFeesTypeName()+"】不计费,金额置0");
            return true;
	    }    
		return false;
	}
	
	@Override
	public void calcuForBms(BizAddFeeEntity entity,FeesReceiveStorageEntity fee){
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
        

        //模板编号
        String quoTempleteCode=contract.getModelNo();
        
		if("fail".equals(quoTempleteCode)){
			fee.setIsCalculated(CalculateState.No_Dinggou.getCode());
			fee.setCalcuMsg("商家【"+taskVo.getCustomerName()+"】未订购任何增值服务!");
			CalcuLog.printLog(CalcuNodeEnum.CONTRACT.getCode().toString(), "商家【"+taskVo.getCustomerName()+"】未订购任何增值服务!", contract, cbiVo);
			return;
		}
		
	    //查询报价模板
		Map<String, Object> map = new HashMap<>();
        if(quoTempleteCode!=null){
            map.put("templateCode", quoTempleteCode);
            addQuoTemplete = genericTemplateRepository.query(map);
        }
		
		if(addQuoTemplete == null){
			fee.setIsCalculated(CalculateState.Quote_Miss.getCode());
			fee.setCalcuMsg("报价模板缺失");
			CalcuLog.printLog(CalcuNodeEnum.CONTRACT.getCode().toString(), "报价模板缺失", contract, cbiVo);
			return;
		}
		
		try{
			//数量
			double num=DoubleUtil.isBlank(entity.getAdjustNum())?entity.getNum():entity.getAdjustNum();
			//计算方法
			double amount=0d;
			//一口价				
            //费用 = 商品数量*模板单价
			Map<String, Object> param = new HashMap<>();
			param.put("templateId", addQuoTemplete.getId());
			param.put("subjectId", entity.getFeesType());
			List<PriceExtraQuotationEntity> extraList= priceExtraQuotationRepository.queryPriceByParam(param);
			if (extraList == null || extraList.size() <= 0) {
				fee.setIsCalculated(CalculateState.Quote_Miss.getCode());
				fee.setCalcuMsg(entity.getRemark()+"没有维护增值费一口价报价;");
			}else {
				amount=num*extraList.get(0).getUnitPrice();							
				fee.setUnitPrice(extraList.get(0).getUnitPrice());
				fee.setParam3(addQuoTemplete.getId()+"");
				fee.setCost(BigDecimal.valueOf(amount));
				//fee.setParam4(priceType);
				fee.setCalcuMsg(entity.getRemark()+"计算成功;");
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
		}catch(Exception ex){
			fee.setIsCalculated(CalculateState.Sys_Error.getCode());
			fee.setCalcuMsg("费用计算异常");
			logger.error("费用计算异常",ex);
		}
	}
	
	@Override
	public void calcuForContract(BizAddFeeEntity entity,FeesReceiveStorageEntity fee){
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
        }
	}
	
	  @Override
    public void calcuForStand(BizAddFeeEntity entity,FeesReceiveStorageEntity fee){
        fee.setContractAttr("3");
        ContractQuoteQueryInfoVo queryVo = getCtConditon(entity);
        contractCalcuService.calcuForStand(entity, fee, taskVo, errorMap, queryVo,cbiVo,fee.getFeesNo());
        if("succ".equals(errorMap.get("success").toString())){
            fee.setIsCalculated(CalculateState.Finish.getCode());
            if(fee.getCost().compareTo(BigDecimal.ZERO) == 1){
                fee.setCalcuMsg("标准报价计算成功");
                logger.info("标准报价计算成功，费用【{}】",fee.getCost());
            }
            else{
                logger.info("标准报价计算不成功，费用【{}】",fee.getCost());
                fee.setCalcuMsg("标准报价单价配置为0或者计费数量/重量为0");
            }
        }
        else{
            fee.setIsCalculated(errorMap.get("is_calculated").toString());
            fee.setCalcuMsg(errorMap.get("msg").toString());
        }
    }
	
	@Override
	public ContractQuoteQueryInfoVo getCtConditon(BizAddFeeEntity entity) {
		ContractQuoteQueryInfoVo queryVo = new ContractQuoteQueryInfoVo();
		queryVo.setCustomerId(entity.getCustomerid());
		queryVo.setBizTypeCode(ContractBizTypeEnum.STORAGE.getCode());
		queryVo.setSubjectCode(entity.getFeesType());
		queryVo.setCurrentTime(entity.getCreateTime());
		queryVo.setWarehouseCode(entity.getWarehouseCode());
		return queryVo;

	}
	
	@Override
	public void updateBatch(List<BizAddFeeEntity> bizList,List<FeesReceiveStorageEntity> feeList) {
		StopWatch sw = new StopWatch();
		sw.start();
		feesReceiveStorageService.updateBatch(feeList);
		sw.stop();
		logger.info("taskId={} 更新仓储费用行数【{}】 耗时【{}】",taskVo.getTaskId(),feeList.size(),sw.getLastTaskTimeMillis());
	}


	

	
	
}
