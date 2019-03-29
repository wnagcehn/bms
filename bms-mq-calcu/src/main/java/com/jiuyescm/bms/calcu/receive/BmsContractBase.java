package com.jiuyescm.bms.calcu.receive;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jiuyescm.bms.asyn.vo.BmsCalcuTaskVo;
import com.jiuyescm.bms.calculate.vo.CalcuBaseInfoVo;
import com.jiuyescm.bms.general.service.IPriceContractInfoService;
import com.jiuyescm.bms.quotation.contract.entity.PriceContractInfoEntity;
import com.jiuyescm.bms.quotation.contract.entity.PriceContractItemEntity;
import com.jiuyescm.bms.quotation.contract.repository.imp.IPriceContractItemRepository;
import com.jiuyescm.cfm.common.JAppContext;

public class BmsContractBase {
	
	private Logger logger = LoggerFactory.getLogger(BmsContractBase.class);

	@Autowired private IPriceContractInfoService jobPriceContractInfoService;
	@Autowired private IPriceContractItemRepository priceContractItemRepository;
	
	
	protected PriceContractInfoEntity contractInfo;
	protected BmsCalcuTaskVo taskVo;
	protected CalcuBaseInfoVo cbiVo;
	protected String subjectCode;
	protected String serviceSubjectCode;
	protected String contractAttr;
	protected String quoTempleteCode = null;
	
	public BmsContractBase(BmsCalcuTaskVo taskVo,String contractAttr){
		this.taskVo = taskVo;
		this.contractAttr = contractAttr;
		subjectCode = taskVo.getSubjectCode();
		cbiVo = new CalcuBaseInfoVo(taskVo.getTaskId(),"bizinfo","",taskVo.getSubjectCode(),JAppContext.currentTimestamp());
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
			contractInfo = jobPriceContractInfoService.queryContractByCustomer(map);
			
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
	public void serviceIsExist(String contractCode,String subjectId){
		Map<String,Object> contractItems_map=new HashMap<String,Object>();
		contractItems_map.put("contractCode", contractCode);
		contractItems_map.put("subjectId", subjectId);
		List<PriceContractItemEntity> contractItems = priceContractItemRepository.query(contractItems_map);
		if(contractItems == null || contractItems.size() == 0 || StringUtils.isEmpty(contractItems.get(0).getTemplateId())) {
			quoTempleteCode = null;
		}
		else{
			quoTempleteCode = contractItems.get(0).getTemplateId();
			if(StringUtils.isEmpty(quoTempleteCode)){
				quoTempleteCode = null;
			}
		}
	}
}
