package com.jiuyescm.bms.calcu.receive;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.jiuyescm.bms.asyn.vo.BmsCalcuTaskVo;
import com.jiuyescm.bms.calculate.vo.CalcuBaseInfoVo;
import com.jiuyescm.bms.calculate.vo.CalcuContractVo;
import com.jiuyescm.bms.general.service.IPriceContractInfoService;
import com.jiuyescm.bms.quotation.contract.entity.PriceContractInfoEntity;
import com.jiuyescm.bms.quotation.contract.entity.PriceContractItemEntity;
import com.jiuyescm.bms.quotation.contract.repository.imp.IPriceContractItemRepository;
import com.jiuyescm.cfm.common.JAppContext;
import com.jiuyescm.constants.CalcuNodeEnum;

public class BmsContractBase {
	
	private Logger logger = LoggerFactory.getLogger(BmsContractBase.class);

	@Autowired private IPriceContractInfoService jobPriceContractInfoService;
	@Autowired private IPriceContractItemRepository priceContractItemRepository;
	
	//protected PriceContractInfoEntity contractInfo; //bms合同信息
	protected BmsCalcuTaskVo taskVo;				//计算任务
	protected CalcuBaseInfoVo cbiVo;				//日志对象
	protected String subjectCode;					//费用科目
	protected String serviceSubjectCode;			//签约服务编码
	protected String contractAttr;					//合同归属
	//protected String quoTempleteCode = null;		//报价模板编码
	//protected CalcuContractVo contractInfo;
	//protected Map<String, String> contractItemMap = null; //合同签约服务集合 用于配送 <科目编码，报价模板编号>
	protected int unCalcuCount = 0; 				//初始未计算单量
	protected int calceCount = 0;					//已计算单量
	protected List<CalcuContractVo> contractList;   //合同集合
	
	public void process(BmsCalcuTaskVo taskVo,String contractAttr){
		this.taskVo = taskVo;
		this.contractAttr = contractAttr;
		subjectCode = taskVo.getSubjectCode();
		cbiVo = new CalcuBaseInfoVo(taskVo.getTaskId(),CalcuNodeEnum.BIZ.toString(),"",taskVo.getSubjectCode(),JAppContext.currentTimestamp());
		
		if(taskVo.getUncalcuCount() == null){
			unCalcuCount = 0;
		}
		else{
			unCalcuCount = taskVo.getUncalcuCount();
		}
		getBmsContractInfo(taskVo);
	}
	
	/**
	 * 查询bms合同
	 * @param vo
	 * @return
	 */
	public void getBmsContractInfo(BmsCalcuTaskVo vo){
		try{
			String customerId = vo.getCustomerId();
			Map<String,Object> map=new HashMap<String,Object>();
			map.put("customerid", customerId);
			map.put("contractTypeCode", "CUSTOMER_CONTRACT");
			
			String creMonth=vo.getCreMonth()+"";
			
	        String year = creMonth.substring(0,4);
	        String month =creMonth.substring(4);

	        if (StringUtils.isNotBlank(year) && StringUtils.isNotBlank(month)) {
	            String startDateStr = year + "-" + month + "-01 00:00:00";
	            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	            Date startDate = sdf.parse(startDateStr);
	            Date endDate = DateUtils.addMonths(startDate, 1);
	            map.put("startDate", startDate);
	            map.put("endDate", endDate);
	        }
	        
			map.put("creMonth", vo.getCreMonth());
			List<PriceContractInfoEntity> bmsContractList = jobPriceContractInfoService.queryContract(map);
			if(bmsContractList.size()<=0){
				return;
			}
			else{
			    //合同集合
			    contractList=new ArrayList<CalcuContractVo>();
			    //签约服务的集合
			    for(PriceContractInfoEntity contract:bmsContractList){
			        logger.info("taskId={} 合同编码{}",vo.getTaskId(),contract.getContractCode());
			        CalcuContractVo contractVo=new CalcuContractVo();
			        contractVo.setContractNo(contract.getContractCode());
			        contractVo.setStartDate(contract.getStartDate());
			        contractVo.setExpireDate(contract.getExpireDate());
			        
	                //配送单独处理 因为配送科目（de_delivery_amount）对应多个签约服务项
	                if(!"de_delivery_amount".equals(vo.getSubjectCode())){
	                    initBmsService(contractVo);
	                }
	                else{
	                    initContractService(contractVo);
	                }	                
	                contractList.add(contractVo);
			    }
			    
				
			}
			
		}catch(Exception ex){
			logger.error("bms合同查询异常",ex);
		}
	}
	
	/**
	 * 校验bms合同签约服务
	 * @param contractCode 合同编号
	 * @param subjectId 科目编号  注：配送应该是 JIUYE-DISPATCH...
	 * @return 模板编码-存在签约服务 fail-不存在签约服务
	 */
	public void initBmsService(CalcuContractVo contrat){
		Map<String,Object> contractItems_map=new HashMap<String,Object>();
		contractItems_map.put("contractCode", contrat.getContractNo());
		contractItems_map.put("subjectId", subjectCode);
		List<PriceContractItemEntity> contractItems = priceContractItemRepository.query(contractItems_map);
		if(contractItems == null || contractItems.size() == 0 || StringUtils.isEmpty(contractItems.get(0).getTemplateId())) {
			contrat.setModelNo("fail");
		}
		else{
			String quoTempleteCode = contractItems.get(0).getTemplateId();
			if(StringUtils.isEmpty(quoTempleteCode)){
	            contrat.setModelNo("fail");
			}
			else{
			    contrat.setModelNo(quoTempleteCode);
			}
		}
	}
	
	/**
	 * 查看合同对应的配送签约服务
	 * @param contractCode
	 * @param subjectId 
	 */
	public void initContractService(CalcuContractVo contrat){
		//合同签约服务集合 用于配送 <科目编码，报价模板编号>
		Map<String,String> contractItemMap = new HashMap<String, String>();
		Map<String,Object> contractItems_map=new HashMap<String,Object>();
		contractItems_map.put("contractCode", contrat.getContractNo());
		contractItems_map.put("bizTypeCode", "DISPATCH");
		List<PriceContractItemEntity> contractItems = priceContractItemRepository.query(contractItems_map);
		for (PriceContractItemEntity item : contractItems) {
			contractItemMap.put(item.getSubjectId(), item.getTemplateId());
		}
		contrat.setItemMap(contractItemMap);
		
	}
	
	
}
