package com.jiuyescm.bms.quotation.contract.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.data.provider.Page;
import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.base.dictionary.entity.SystemCodeTypeEntity;
import com.jiuyescm.bms.base.dictionary.service.ISystemCodeTypeService;
import com.jiuyescm.bms.common.enumtype.ContractAbnormalTypeEnum;
import com.jiuyescm.bms.quotation.contract.entity.ContractManageEntity;
import com.jiuyescm.bms.quotation.contract.service.IPriceContractService;
import com.jiuyescm.mdm.customer.api.ICustomerService;
import com.jiuyescm.mdm.customer.vo.CustomerVo;

@Controller("customerContractAbnormalController")
public class CustomerContractAbnormalController {
	@Resource
	private IPriceContractService priceContractService;
	@Autowired 
	private ICustomerService customerService;
	@Resource
	private ISystemCodeTypeService systemCodeTypeService;
	@DataProvider
	public void queryAll(Page<ContractManageEntity> page,Map<String,Object> parameter){
		
		if(parameter==null){
			parameter=new HashMap<String, Object>();
		}

		//查询出系统设置的超市时间进行判断
		SystemCodeTypeEntity systemEntity=systemCodeTypeService.findByTypeCode("CONTRACT_EXPRI_DATE");
		
		Map<String,Object> conditionMap=new HashMap<>();
		conditionMap.put("time",systemEntity.getTypeDesc());
		
		List<ContractManageEntity> queryList=priceContractService.queryContractManageEntity(conditionMap);
		
		//获取所有商家
		Map<String, String> cumMap=getJyCustomer();
		
		//查询出所有未签约合同的商家
		//查询出所有已经签约的商家
		List<String> customerList=priceContractService.queryCustomerList();
		//将已签约的商家封装为map
		Map<String, Object> cusSignMap=new HashMap<>();
		for(String customerId:customerList){
			cusSignMap.put(customerId, cumMap.get(customerId));
		}
		//遍历所有的商家
		for(String key:cumMap.keySet()){
			if(!cusSignMap.containsKey(key)){
				ContractManageEntity contractManageEntity=new ContractManageEntity();
				contractManageEntity.setCustomerId(key);
				contractManageEntity.setCustomerName(cumMap.get(key));
				contractManageEntity.setAbnormalType(ContractAbnormalTypeEnum.NOTPRESERVE.getCode());
				queryList.add(contractManageEntity);
			}
		}
		
		List<ContractManageEntity> returnList=new ArrayList<>();
		//根据查询条件判断（商家id和异常类型）
		if(parameter.get("customerId")!=null && (parameter.get("abnormalType")==null || parameter.get("abnormalType")=="")){
			String customerId=parameter.get("customerId").toString();
			for(ContractManageEntity en:queryList){
				if(en.getCustomerId().equals(customerId)){
					returnList.add(en);
				}
			}
		}
		if((parameter.get("customerId")==null || parameter.get("customerId")=="") && parameter.get("abnormalType")!=null){
			String abnormalType=parameter.get("abnormalType").toString();
			for(ContractManageEntity en:queryList){
				if(en.getAbnormalType().equals(abnormalType)){
					returnList.add(en);
				}
			}
		}
		if(parameter.get("customerId")!=null && parameter.get("abnormalType")!=null){
			String customerId=parameter.get("customerId").toString();
			String abnormalType=parameter.get("abnormalType").toString();
			for(ContractManageEntity en:queryList){
				if(en.getCustomerId().equals(customerId) && en.getAbnormalType().equals(abnormalType)){
					returnList.add(en);
				}
			}
		}
		if((parameter.get("customerId")==null || parameter.get("customerId").toString()=="") && (parameter.get("abnormalType")==null || parameter.get("abnormalType")=="")){
			returnList.addAll(queryList);
		}
		page.setEntities(returnList);
		page.setEntityCount(returnList.size());
		
	}
	
	@DataProvider
	public Map<String, String> getJyCustomer() {
		PageInfo<CustomerVo> customerList=customerService.query(null, 0, Integer.MAX_VALUE);
		List<CustomerVo> cList=customerList.getList();
		Map<String, String> map = new LinkedHashMap<String,String>();
		for (CustomerVo vo : cList) {
			map.put(vo.getCustomerid(), vo.getCustomername());
		}
		return map;
	}
}
