package com.jiuyescm.bms.calcu.receive.storage.dispatchpackage;

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
import com.jiuyescm.bms.biz.dispatch.entity.BizDispatchPackageEntity;
import com.jiuyescm.bms.calcu.CalcuLog;
import com.jiuyescm.bms.calcu.base.ICalcuService;
import com.jiuyescm.bms.calcu.receive.BmsContractBase;
import com.jiuyescm.bms.calcu.receive.CommonService;
import com.jiuyescm.bms.calcu.receive.ContractCalcuService;
import com.jiuyescm.bms.calculate.api.IBmsCalcuService;
import com.jiuyescm.bms.calculate.vo.BmsFeesQtyVo;
import com.jiuyescm.bms.common.enumtype.CalculateState;
import com.jiuyescm.bms.common.enumtype.TemplateTypeEnum;
import com.jiuyescm.bms.general.entity.FeesReceiveStorageEntity;
import com.jiuyescm.bms.general.service.IFeesReceiveStorageService;
import com.jiuyescm.bms.receivable.storage.service.IBizDispatchPackageService;
import com.jiuyescm.cfm.common.JAppContext;
import com.jiuyescm.constants.CalcuNodeEnum;
import com.jiuyescm.contract.quote.vo.ContractBizTypeEnum;
import com.jiuyescm.contract.quote.vo.ContractQuoteQueryInfoVo;

@Component("dispatchPackageCalcuJob")
@Scope("prototype")
public class DispatchPackageCalcuJob extends BmsContractBase implements ICalcuService<BizDispatchPackageEntity,FeesReceiveStorageEntity> {

	private Logger logger = LoggerFactory.getLogger(DispatchPackageCalcuJob.class);
		
	@Autowired private IBizDispatchPackageService bizDispatchPackageService;
	@Autowired private IFeesReceiveStorageService feesReceiveStorageService;
	@Autowired private ContractCalcuService contractCalcuService;
	@Autowired private CommonService commonService;
	@Autowired IBmsCalcuTaskService bmsCalcuTaskService;
	@Autowired IBmsCalcuService bmsCalcuService;

	
	private Map<String, Object> errorMap = null;

	public void process(BmsCalcuTaskVo taskVo,String contractAttr){
		super.process(taskVo, contractAttr);
		initConf();
	}
	
	@Override
	public void getQuoTemplete(){
		
	}
	
	@Override
	public void initConf(){
	    
	}
	
	@Override
	public void calcu(Map<String, Object> map){
		
		List<BizDispatchPackageEntity> bizList = bizDispatchPackageService.query(map);
		List<FeesReceiveStorageEntity> fees = new ArrayList<>();
		if(bizList == null || bizList.size() == 0){
			commonService.taskCountReport(taskVo, "STORAGE");
			return;
		}
		logger.info("taskId={} 查询行数【{}】",taskVo.getTaskId(),bizList.size());
		for (BizDispatchPackageEntity entity : bizList) {
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
		calcu(map);
		
	}
	
	private void updateTask(BmsCalcuTaskVo taskVo,int calcuCount){
		try {
			BmsFeesQtyVo feesQtyVo = bmsCalcuService.queryFeesQtyForStoStandMaterial(taskVo.getCustomerId(), taskVo.getSubjectCode(), taskVo.getCreMonth());
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
	public FeesReceiveStorageEntity initFee(BizDispatchPackageEntity entity){
		//打印业务数据日志
		CalcuLog.printLog(CalcuNodeEnum.BIZ.getCode().toString(), "", entity, cbiVo);
		FeesReceiveStorageEntity fee = new FeesReceiveStorageEntity();	
		fee.setCostType("FEE_TYPE_MATERIAL");				//费用类别 FEE_TYPE_GENEARL-通用 FEE_TYPE_MATERIAL-耗材 FEE_TYPE_ADD-增值
		fee.setSubjectCode(subjectCode);					//费用科目
		fee.setOtherSubjectCode(subjectCode);
		fee.setProductType("");//商品类型
		fee.setStatus("0");								//状态
		fee.setCalculateTime(JAppContext.currentTimestamp());
		fee.setCost(new BigDecimal(0));					//入仓金额
		fee.setFeesNo(entity.getFeesNo());
		fee.setParam1(TemplateTypeEnum.COMMON.getCode());
		fee.setCreateTime(entity.getCreTime());
		return fee;
		
	}
	

	@Override
	public void calcuForContract(BizDispatchPackageEntity entity,FeesReceiveStorageEntity fee){
        fee.setContractAttr("2");
	    ContractQuoteQueryInfoVo queryVo = getCtConditon(entity);
		contractCalcuService.calcuForContract(entity, fee, taskVo, errorMap, queryVo,cbiVo,fee.getFeesNo());
		if("succ".equals(errorMap.get("success").toString())){
            fee.setCalcuMsg(CalculateState.Finish.getDesc());
			if(fee.getCost().compareTo(BigDecimal.ZERO) == 1){
				logger.info("计算成功，费用【{}】",fee.getCost());
                fee.setCalcuMsg("计算成功");
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
	public ContractQuoteQueryInfoVo getCtConditon(BizDispatchPackageEntity entity) {
		ContractQuoteQueryInfoVo queryVo = new ContractQuoteQueryInfoVo();
		queryVo.setCustomerId(entity.getCustomerid());
		queryVo.setBizTypeCode(ContractBizTypeEnum.STORAGE.getCode());
		queryVo.setSubjectCode(subjectCode);
		queryVo.setCurrentTime(entity.getCreTime());
		queryVo.setWarehouseCode(entity.getWarehouseCode());
		return queryVo;

	}
	
	@Override
	public void updateBatch(List<BizDispatchPackageEntity> bizList,List<FeesReceiveStorageEntity> feeList) {
		StopWatch sw = new StopWatch();
		sw.start();
		feesReceiveStorageService.updateBatch(feeList);
		sw.stop();
		logger.info("taskId={} 更新仓储费用行数【{}】 耗时【{}】",taskVo.getTaskId(),feeList.size(),sw.getLastTaskTimeMillis());
	}

    @Override
    public boolean isNoExe(BizDispatchPackageEntity entity, FeesReceiveStorageEntity fee) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void calcuForBms(BizDispatchPackageEntity entity, FeesReceiveStorageEntity fee) {
        // TODO Auto-generated method stub
        fee.setCalcuMsg("标准包装方案只支持合同在线计费");                    
        fee.setIsCalculated(CalculateState.Quote_Miss.getCode());
        fee.setContractAttr("1");
        return;
    }


	

	
	
}
