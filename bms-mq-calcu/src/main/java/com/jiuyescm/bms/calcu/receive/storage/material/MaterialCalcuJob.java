package com.jiuyescm.bms.calcu.receive.storage.material;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StopWatch;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.jiuyescm.bms.asyn.service.IBmsCalcuTaskService;
import com.jiuyescm.bms.asyn.vo.BmsCalcuTaskVo;
import com.jiuyescm.bms.base.dict.api.IMaterialDictService;
import com.jiuyescm.bms.base.dict.api.IPubPackageDictService;
import com.jiuyescm.bms.base.dict.entity.PubPackageDictEntity;
import com.jiuyescm.bms.base.dictionary.entity.SystemCodeEntity;
import com.jiuyescm.bms.base.dictionary.repository.ISystemCodeRepository;
import com.jiuyescm.bms.base.group.service.IBmsGroupSubjectService;
import com.jiuyescm.bms.biz.dispatch.entity.BizDispatchBillEntity;
import com.jiuyescm.bms.biz.dispatch.entity.BizDispatchPackageEntity;
import com.jiuyescm.bms.biz.storage.entity.BizOutstockPackmaterialEntity;
import com.jiuyescm.bms.calcu.CalcuLog;
import com.jiuyescm.bms.calcu.base.ICalcuService;
import com.jiuyescm.bms.calcu.receive.BmsContractBase;
import com.jiuyescm.bms.calcu.receive.CommonService;
import com.jiuyescm.bms.calcu.receive.ContractCalcuService;
import com.jiuyescm.bms.calculate.api.IBmsCalcuService;
import com.jiuyescm.bms.calculate.vo.BmsFeesQtyVo;
import com.jiuyescm.bms.calculate.vo.CalcuContractVo;
import com.jiuyescm.bms.common.enumtype.CalculateState;
import com.jiuyescm.bms.common.enumtype.TemplateTypeEnum;
import com.jiuyescm.bms.drools.IFeesCalcuService;
import com.jiuyescm.bms.general.entity.FeesReceiveStorageEntity;
import com.jiuyescm.bms.general.service.IFeesReceiveStorageService;
import com.jiuyescm.bms.general.service.IPriceContractInfoService;
import com.jiuyescm.bms.general.service.IStorageQuoteFilterService;
import com.jiuyescm.bms.general.service.SequenceService;
import com.jiuyescm.bms.quotation.contract.repository.imp.IPriceContractDao;
import com.jiuyescm.bms.quotation.contract.repository.imp.IPriceContractItemRepository;
import com.jiuyescm.bms.quotation.storage.entity.PriceGeneralQuotationEntity;
import com.jiuyescm.bms.quotation.storage.entity.PriceMaterialQuotationEntity;
import com.jiuyescm.bms.quotation.storage.repository.IPriceMaterialQuotationRepository;
import com.jiuyescm.bms.quotation.transport.repository.IGenericTemplateRepository;
import com.jiuyescm.bms.receivable.dispatch.service.IBizDispatchBillService;
import com.jiuyescm.bms.receivable.storage.service.IBizDispatchPackageService;
import com.jiuyescm.bms.receivable.storage.service.IBizOutstockPackmaterialService;
import com.jiuyescm.bms.rule.receiveRule.repository.IReceiveRuleRepository;
import com.jiuyescm.cfm.common.JAppContext;
import com.jiuyescm.common.utils.DoubleUtil;
import com.jiuyescm.constants.CalcuNodeEnum;
import com.jiuyescm.contract.quote.api.IContractQuoteInfoService;
import com.jiuyescm.contract.quote.vo.ContractBizTypeEnum;
import com.jiuyescm.contract.quote.vo.ContractQuoteQueryInfoVo;
import com.jiuyescm.mdm.customer.api.IPubMaterialInfoService;
import com.jiuyescm.mdm.customer.vo.PubMaterialInfoVo;

@Component("materialCalcuJob")
@Scope("prototype")
public class MaterialCalcuJob extends BmsContractBase implements ICalcuService<BizOutstockPackmaterialEntity,FeesReceiveStorageEntity> {

	private Logger logger = LoggerFactory.getLogger(MaterialCalcuJob.class);
		
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
	@Autowired private IBmsGroupSubjectService bmsGroupSubjectService;
	@Autowired private IStorageQuoteFilterService storageQuoteFilterService;
	@Autowired private IPubMaterialInfoService pubMaterialInfoService;
	@Autowired private ISystemCodeRepository systemCodeRepository;
	@Autowired private IMaterialDictService materialDictService;
	@Autowired private ContractCalcuService contractCalcuService;
	@Autowired private CommonService commonService;
	@Autowired IBmsCalcuTaskService bmsCalcuTaskService;
	@Autowired IBmsCalcuService bmsCalcuService;
	@Autowired private IBizDispatchPackageService bizDispatchPackageService;
    @Autowired
    private IPubPackageDictService pubPackageDictService;


	
	private PriceGeneralQuotationEntity quoTemplete = null;
	private Map<String, Object> errorMap = null;
	private Map<String,PubMaterialInfoVo> materialMap = null;
	private List<SystemCodeEntity> noCarrierMap=null;
	private Map<String,SystemCodeEntity> noFeesMap=null;
	private List<BizOutstockPackmaterialEntity> bizList=null;
	private List<FeesReceiveStorageEntity> fees = null;
	
	public void process(BmsCalcuTaskVo taskVo,String contractAttr){
		super.process(taskVo, contractAttr);
		getQuoTemplete();
		serviceSubjectCode = subjectCode;
		initConf();
	}
	
	@Override
	public void getQuoTemplete(){
		//Map<String, Object> map = new HashMap<>();
		/*if(quoTempleteCode!=null){
			map.put("subjectId",serviceSubjectCode);
			map.put("quotationNo", quoTempleteCode);
			//quoTemplete = priceGeneralQuotationRepository.query(map);
		}*/
	}
	
	@Override
	public void initConf(){
		Map<String,Object> condition=Maps.newHashMap();
		List<PubMaterialInfoVo> tmscodels = pubMaterialInfoService.queryList(condition);
		materialMap=Maps.newLinkedHashMap();
		for(PubMaterialInfoVo materialVo:tmscodels){
			if(!StringUtils.isBlank(materialVo.getBarcode())){
				materialMap.put(materialVo.getBarcode().trim(),materialVo);
			}
		}
		
		noCarrierMap=new ArrayList<SystemCodeEntity>();
		condition.clear();
		condition.put("typeCode", "CARRIER_FREE");
		PageInfo<SystemCodeEntity> page = systemCodeRepository.query(condition, 1, 999999);
		if(page.getList()!=null||page.getList().size()>0){
			noCarrierMap = page.getList();
		}
		
		condition.clear();
		condition.put("typeCode", "NO_FEES_MATERIAL");
		page = systemCodeRepository.query(condition, 1, 999999);
		noFeesMap=new HashMap<String, SystemCodeEntity>();
		if(page.getList()!=null||page.getList().size()>0){
			List<SystemCodeEntity> list = page.getList();
			for(SystemCodeEntity entity:list){
				noFeesMap.put(entity.getCode(),entity);
			}
		}
	}
	
	@Override
	public void calcu(Map<String, Object> map){
	    
	    try {
            //处理同时存在的导入耗材和系统耗材
            handMaterail(map);
        } catch (Exception e) {
            // TODO: handle exception
            logger.error("作废耗材失败",e);
        }

	    
	    int count=(int) map.get("num");
        //原始进来的数量
        int recount=count;
        while(count == recount){
          count = calcuDetail(map);
        }
        calcuDetail(map);   
	}
	
	
	private int calcuDetail(Map<String, Object> map){
       bizList = bizOutstockPackmaterialService.query(map);
        fees = new ArrayList<>();
        if(bizList == null || bizList.size() == 0){
            commonService.taskCountReport(taskVo, "STORAGE");
            return 0;
        }
        logger.info("taskId={} 查询行数【{}】",taskVo.getTaskId(),bizList.size());
        for (BizOutstockPackmaterialEntity entity : bizList) {
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
			BmsFeesQtyVo feesQtyVo = bmsCalcuService.queryFeesQtyForStoMaterial(taskVo.getCustomerId(), taskVo.getSubjectCode(), taskVo.getCreMonth());
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
	public FeesReceiveStorageEntity initFee(BizOutstockPackmaterialEntity entity){
		//打印业务数据日志
		CalcuLog.printLog(CalcuNodeEnum.BIZ.getCode().toString(), "", entity, cbiVo);
		FeesReceiveStorageEntity fee = new FeesReceiveStorageEntity();	
		fee.setCostType("FEE_TYPE_MATERIAL");				//费用类别 FEE_TYPE_GENEARL-通用 FEE_TYPE_MATERIAL-耗材 FEE_TYPE_ADD-增值
		fee.setSubjectCode(subjectCode);					//费用科目
		fee.setOtherSubjectCode(subjectCode);
		fee.setProductType("");//商品类型
		fee.setProductNo(entity.getConsumerMaterialCode());
		fee.setProductName(entity.getConsumerMaterialName());
		fee.setQuantity(DoubleUtil.isBlank(entity.getAdjustNum())?entity.getNum():entity.getAdjustNum());//计费数量
		if(materialMap!=null && materialMap.containsKey(entity.getConsumerMaterialCode())){
			String materialType=materialMap.get(entity.getConsumerMaterialCode()).getMaterialType();
			if("干冰".equals(materialType)){
				fee.setQuantity(DoubleUtil.isBlank(entity.getAdjustNum())?entity.getWeight():entity.getAdjustNum());//计费数量
			}else{
				fee.setQuantity(DoubleUtil.isBlank(entity.getAdjustNum())?entity.getNum():entity.getAdjustNum());//计费重量
			}
		}
		fee.setStatus("0");								//状态
		fee.setWeight(entity.getWeight());				//设置重量
		fee.setCalculateTime(JAppContext.currentTimestamp());
		fee.setCost(new BigDecimal(0));					//入仓金额
		fee.setFeesNo(entity.getFeesNo());
		fee.setParam1(TemplateTypeEnum.COMMON.getCode());
		fee.setCreateTime(entity.getCreateTime());
		return fee;
		
	}
	
	@Override
	public boolean isNoExe(BizOutstockPackmaterialEntity entity,FeesReceiveStorageEntity fee){
		//运单
		BizDispatchBillEntity biz=bizDispatchBillService.getDispatchEntityByWaybillNo(entity.getWaybillNo());
		//耗材类型
		String materialType = "";
		if(materialMap!=null && materialMap.containsKey(entity.getConsumerMaterialCode())){
			materialType=materialMap.get(entity.getConsumerMaterialCode()).getMaterialType();
		}

		//物流商对应得耗材不计费
		if(biz!=null && StringUtils.isNotBlank(materialType)){
			for (SystemCodeEntity scEntity : noCarrierMap) {
				if(scEntity.getCode().equals(biz.getCarrierId()) && scEntity.getExtattr1().equals(materialType)){
					//XxlJobLogger.log("-->"+entity.getId()+String.format("该物流商使用的耗材不收钱", entity.getId()));
					fee.setIsCalculated(CalculateState.No_Exe.getCode());
					fee.setCalcuMsg("该物流商使用的耗材不收钱");
					return true;
				}
			}
			
		}
		
		//指定耗材不计费
		if(entity.getConsumerMaterialCode()!=null && noFeesMap.containsKey(entity.getConsumerMaterialCode())){
			fee.setIsCalculated(CalculateState.No_Exe.getCode());
			fee.setCalcuMsg("耗材不收钱");
			return true;
		}
		
		//标准包装方案对应的耗材不计费
        handPackage(entity,fee);

		
		return false;		
	}
	
	@Override
	public void calcuForBms(BizOutstockPackmaterialEntity entity,FeesReceiveStorageEntity fee){
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
		
		String quoTempleteCode=contract.getModelNo();
		
		if("fail".equals(quoTempleteCode)){
			fee.setIsCalculated(CalculateState.No_Dinggou.getCode());
			fee.setCalcuMsg("商家【"+taskVo.getCustomerName()+"】未订购科目【"+taskVo.getSubjectName()+"】的服务项!");
			CalcuLog.printLog(CalcuNodeEnum.CONTRACT.getCode().toString(), "商家【"+taskVo.getCustomerName()+"】未订购科目【"+taskVo.getSubjectName()+"】的服务项!", contract, cbiVo);
			return;
		}
		
		try{
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("contractCode", contract.getContractNo());
			map.put("subjectId",subjectCode);
			map.put("materialCode",entity.getConsumerMaterialCode());
			List<PriceMaterialQuotationEntity> list=priceMaterialQuotationRepository.queryMaterialQuatationByContract(map);
			
			if(list==null||list.size()==0){
				//XxlJobLogger.log("-->"+entity.getId()+"报价未配置");
				entity.setIsCalculated(CalculateState.Quote_Miss.getCode());
				fee.setIsCalculated(CalculateState.Quote_Miss.getCode());
				fee.setCalcuMsg("BMS报价未配置");
				entity.setRemark(entity.getRemark()+"报价未配置;");
				return;
			}
			
			PriceMaterialQuotationEntity stepQuoEntity = null;
			
			//判断耗材类型是否为泡沫箱
			PubMaterialInfoVo materialInfoVo = materialDictService.getMaterialByCode(entity.getConsumerMaterialCode());
			if (null != materialInfoVo && "泡沫箱".equals(materialInfoVo.getMaterialType())) {
				//获取当前月份
				Calendar cale = Calendar.getInstance();
				Timestamp creTime = entity.getCreateTime();
				cale.setTime((Date)creTime);
				int month = cale.get(Calendar.MONTH) + 1;				
				//当前月份在夏季，筛出全国仓和其他仓最低报价
				List<SystemCodeEntity> pmxTimes = systemCodeRepository.findEnumList("PMX_TIME");
				String season = "";//季节
				boolean exe = false;
				for (SystemCodeEntity time : pmxTimes) {
					if (month >= Integer.parseInt(time.getExtattr1()) && month <= Integer.parseInt(time.getExtattr2())) {
						season = time.getCode();
						exe = true;
						//仓库过滤
						map.clear();
						map.put("warehouse_code", entity.getWarehouseCode());
						List<PriceMaterialQuotationEntity> pmxPriceList = storageQuoteFilterService.quotePMXmaterialFilter(list, map);
						if ("SUMMER".equals(season)) {
							//夏季
							if (CollectionUtils.isNotEmpty(pmxPriceList)) {
								if (pmxPriceList.size() == 2) {
									if (pmxPriceList.get(0).getUnitPrice() > pmxPriceList.get(1).getUnitPrice()) {
										stepQuoEntity = pmxPriceList.get(0);
									}else {
										stepQuoEntity = pmxPriceList.get(1);
									}
								}else {
									stepQuoEntity = pmxPriceList.get(0);
								}
							}else {
								stepQuoEntity = null;
							}
						}else {
							//其余季节
							if (CollectionUtils.isNotEmpty(pmxPriceList)) {
								if (pmxPriceList.size() == 2) {
									if (pmxPriceList.get(0).getUnitPrice() > pmxPriceList.get(1).getUnitPrice()) {
										stepQuoEntity = pmxPriceList.get(1);
									}else {
										stepQuoEntity = pmxPriceList.get(0);
									}
								}else {
									stepQuoEntity = pmxPriceList.get(0);
								}					
							}else {
								stepQuoEntity = null;
							}
						}
					}
				}
				if (!exe) {
					//封装数据的仓库
					map.clear();
					map.put("warehouse_code", entity.getWarehouseCode());
					stepQuoEntity=storageQuoteFilterService.quoteMaterialFilter(list, map);
				}
			}else {
				//封装数据的仓库
				map.clear();
				map.put("warehouse_code", entity.getWarehouseCode());
				stepQuoEntity=storageQuoteFilterService.quoteMaterialFilter(list, map);
			}

			if(stepQuoEntity==null){
				//XxlJobLogger.log("-->"+entity.getId()+"报价未配置");
				entity.setIsCalculated(CalculateState.Quote_Miss.getCode());
				fee.setIsCalculated(CalculateState.Quote_Miss.getCode());
				entity.setRemark(entity.getRemark()+"报价未配置;");
				fee.setCalcuMsg("报价未配置");
				return;
			}
			//XxlJobLogger.log("筛选后得到的报价结果【{0}】",JSONObject.fromObject(stepQuoEntity));
						
			fee.setCost(new BigDecimal(stepQuoEntity.getUnitPrice()*fee.getQuantity()).setScale(2,BigDecimal.ROUND_HALF_UP));
			fee.setUnitPrice(stepQuoEntity.getUnitPrice());
			fee.setParam2(stepQuoEntity.getUnitPrice().toString()+"*"+fee.getQuantity().toString());
			fee.setParam3(stepQuoEntity.getId()+"");
            fee.setIsCalculated(CalculateState.Finish.getCode());
			if(fee.getCost().compareTo(BigDecimal.ZERO) == 1){
				fee.setCalcuMsg(CalculateState.Finish.getDesc());
                logger.info("计算成功，费用【{}】",fee.getCost());
			}else{
                fee.setCalcuMsg("单价配置为0或者计费数量/重量为0");
                logger.info("计算不成功，费用【{}】",fee.getCost());
			}
		}catch(Exception ex){
			fee.setIsCalculated(CalculateState.Sys_Error.getCode());
			fee.setCalcuMsg("计算错误");
			logger.error("计算错误",ex);
			//XxlJobLogger.log("-->"+entity.getId()+"系统异常，费用【0】");
		}
		
		
	}
	
	@Override
	public void calcuForContract(BizOutstockPackmaterialEntity entity,FeesReceiveStorageEntity fee){
		fee.setContractAttr("2");
	    ContractQuoteQueryInfoVo queryVo = getCtConditon(entity);
		contractCalcuService.calcuForContract(entity, fee, taskVo, errorMap, queryVo,cbiVo,fee.getFeesNo());
		if("succ".equals(errorMap.get("success").toString())){
            fee.setIsCalculated(CalculateState.Finish.getCode());
		    if(fee.getCost().compareTo(BigDecimal.ZERO) == 1){
				fee.setCalcuMsg(CalculateState.Finish.getDesc());
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
	}
	
	@Override
	public ContractQuoteQueryInfoVo getCtConditon(BizOutstockPackmaterialEntity entity) {
		ContractQuoteQueryInfoVo queryVo = new ContractQuoteQueryInfoVo();
		queryVo.setCustomerId(entity.getCustomerId());
		queryVo.setBizTypeCode(ContractBizTypeEnum.STORAGE.getCode());
		queryVo.setSubjectCode(subjectCode);
		queryVo.setCurrentTime(entity.getCreateTime());
		queryVo.setWarehouseCode(entity.getWarehouseCode());
		return queryVo;

	}
	
	@Override
	public void updateBatch(List<BizOutstockPackmaterialEntity> bizList,List<FeesReceiveStorageEntity> feeList) {
		StopWatch sw = new StopWatch();
		sw.start();
		feesReceiveStorageService.updateBatch(feeList);
		sw.stop();
		logger.info("taskId={} 更新仓储费用行数【{}】 耗时【{}】",taskVo.getTaskId(),feeList.size(),sw.getLastTaskTimeMillis());
	}

	private boolean handPackage(BizOutstockPackmaterialEntity entity,FeesReceiveStorageEntity fee){
        Map<String, Object> con = new HashMap<String, Object>();
        con.put("delFlag", "0");
        List<PubPackageDictEntity> packDickList = pubPackageDictService.query(con);
        Map<String, List<String>> dicMap = new HashMap<String, List<String>>();  //key为mark，value为需要作废的耗材
        for (PubPackageDictEntity dictEntity : packDickList) {
            if (dicMap.containsKey(dictEntity.getPackMark())) {
                dicMap.get(dictEntity.getPackMark()).add(dictEntity.getMaterialType());
            }else {
                List<String> materialList = new LinkedList<String>();
                materialList.add(dictEntity.getMaterialType());
                dicMap.put(dictEntity.getPackMark(), materialList);
            }
        }
       
        //配置表一定会配置数据，这一步可能多余
        if (CollectionUtils.isEmpty(packDickList)) {
            return false;
        }      
       
       BizDispatchPackageEntity bizEntity=bizDispatchPackageService.queryOne(entity.getWaybillNo());
       if(bizEntity!=null){
           //获取所有level和对应的marks
           TreeMap<Integer, Set<String>> levelMap = getAllLevel(packDickList, bizEntity);  
           
           logger.info("运单：{0}，匹配分数为：{1}", bizEntity.getWaybillNo(), levelMap.lastKey());
            
           //取出最大level对应的marks，来获取需要作废的耗材（去重）
           Set<String> marks = levelMap.get(levelMap.lastKey());
           Set<String> materialTypes = new HashSet<String>();
           for (String mark : marks) {
               List<String> materialTypeList = dicMap.get(mark);
               for (String materialType : materialTypeList) {
                   materialTypes.add(materialType);
               }
           }

           if(materialTypes.contains(entity.getMaterialType())){
               fee.setIsCalculated(CalculateState.No_Exe.getCode());
               fee.setCalcuMsg("标准包装方案不计费");
               return true;
           }
       }
       
       return false;
    }

	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    private void handMaterail(Map<String, Object> map){
        StopWatch sw = new StopWatch();
        sw.start();
        feesReceiveStorageService.updateImportFee(map);
        sw.stop();
        logger.info("taskId={} 作废导入费用 耗时【{}】",taskVo.getTaskId(),sw.getLastTaskTimeMillis());
        sw.start();
        bizOutstockPackmaterialService.updateImportMaterial(map);
        sw.stop();
        logger.info("taskId={} 作废导入耗材 耗时【{}】",taskVo.getTaskId(),sw.getLastTaskTimeMillis());      
    }
    
    /**
     * 获取所有level和对应的marks
     * <功能描述>
     * 
     * @author wangchen870
     * @date 2019年5月18日 下午5:29:37
     *
     * @param packDickList
     * @param bizEntity
     * @param level
     * @return
     */
    private TreeMap<Integer, Set<String>> getAllLevel(List<PubPackageDictEntity> packDickList, BizDispatchPackageEntity bizEntity) {
        TreeMap<Integer, Set<String>> levelMap = new TreeMap<Integer, Set<String>>();
        for (PubPackageDictEntity dicEntity : packDickList) {
            int level = 0;
            //季节
            if (bizEntity.getSeason().equals(dicEntity.getSeason())) {
                level += 1;
            }else if (StringUtils.isNotBlank(dicEntity.getSeason()) && !bizEntity.getSeason().equals(dicEntity.getSeason())) {
                continue;
            }
            //保温时效
            if (bizEntity.getHoldingTime().equals(dicEntity.getHoldingTime())) {
                level += 1;
            }else if (StringUtils.isNotBlank(dicEntity.getHoldingTime()) && !bizEntity.getHoldingTime().equals(dicEntity.getHoldingTime())) {
                continue;
            }
            //配送温区
            if (bizEntity.getTransportTemperatureType().equals(dicEntity.getTransportTemperatureType())) {
                level += 1;
            }else if (StringUtils.isNotBlank(dicEntity.getTransportTemperatureType()) && !bizEntity.getTransportTemperatureType().equals(dicEntity.getTransportTemperatureType())) {
                continue;
            }
            //运输方式
            if (bizEntity.getTransportType().equals(dicEntity.getTransportType())) {
                level += 1;
            }else if (StringUtils.isNotBlank(dicEntity.getTransportType()) && !bizEntity.getTransportType().equals(dicEntity.getTransportType())) {
                continue;
            }
            //操作分类
            if (bizEntity.getPackOperateType().equals(dicEntity.getPackOperateType())) {
                level += 1;
            }else if (StringUtils.isNotBlank(dicEntity.getPackOperateType()) && !bizEntity.getPackOperateType().equals(dicEntity.getPackOperateType())) {
                continue;
            }
            
            //可能存在一个运单，有2个一样打分等级的mark，2个mark对应的耗材都作废
            if (levelMap.containsKey(level)) {
                levelMap.get(level).add(dicEntity.getPackMark());
            }else {
                Set<String> marks = new HashSet<String>();
                marks.add(dicEntity.getPackMark());
                levelMap.put(level, marks);
            }  
        }     
        return levelMap;
    }

	
	
}
