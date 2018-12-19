package com.jiuyescm.bms.trial.dispatch.web;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;

import com.bstek.dorado.annotation.DataResolver;
import com.jiuyescm.bms.biz.dispatch.entity.BizDispatchBillEntity;
import com.jiuyescm.bms.biz.storage.entity.ReturnData;
import com.jiuyescm.bms.chargerule.receiverule.service.IReceiveRuleService;
import com.jiuyescm.bms.common.entity.CalculateVo;
import com.jiuyescm.bms.quotation.contract.service.IPriceContractService;
import com.jiuyescm.bms.quotation.dispatch.service.IPriceDispatchService;
import com.jiuyescm.bms.quotation.dispatch.service.IPriceDispatchTemplateService;

@Controller("tryDispatchDroolsController")
public class TryDispatchDroolsController {
	
	private static final Logger logger = Logger.getLogger(TryDispatchDroolsController.class.getName());

	
	@Resource
	private IPriceDispatchService priceDispatchService;
	
	@Resource
	private IPriceDispatchTemplateService priceDispatchTemplateService;
	
	@Resource
	private IReceiveRuleService receiveRuleService;
	
	
	@Resource
	private IPriceContractService priceContractService;
	
	@DataResolver
	public Object tryOrderOperate(BizDispatchBillEntity data){
		String provinceName=data.getReceiveProvinceName();
		String cityName=data.getReceiveCityName();
		String areaName=data.getReceiveDistrictName();
		
		if(StringUtils.isNotBlank(provinceName)){
			data.setReceiveProvinceId(provinceName);
		}
		if(StringUtils.isNotBlank(cityName)){
			data.setReceiveCityId(cityName);
		}
		if(StringUtils.isNotBlank(areaName)){
			data.setReceiveDistrictId(areaName);
		}
			
		//顺丰进0.5，其余进1
		if(data.getWeight()!=null && data.getCarrierId()!=null){
			if("SHUNFENG_DISPATCH".equals(data.getCarrierId())){
				double result=getResult(data.getWeight());
				data.setTotalWeight(result);	
			}else{
				data.setTotalWeight(Math.ceil(data.getWeight()));
			}
				
		}
		
		CalculateVo calculateVo=new CalculateVo();
		calculateVo.setBizTypeCode("DISPATCH");
		calculateVo.setSubjectId(data.getCarrierId());
		//calculateVo.setMobanCode(data.getMobanId());
		calculateVo.setContractCode(data.getMobanId());
		calculateVo.setObj(data);
		
		//CalculateVo vo=feesCalculateService.calculate(calculateVo);
		CalculateVo vo = new CalculateVo();
		if(vo.getSuccess()){
			if(vo.getPrice()!=null){
				data.setCollectMoney(vo.getPrice().doubleValue());
			}			
		}
		
		data.setReceiveProvinceName(provinceName);
		data.setReceiveCityName(cityName);
		data.setReceiveDistrictName(areaName);
	
		ReturnData result = new ReturnData();
		
		result.setCode("SUCCESS");
		result.setData(vo.getPrice()==null?"无匹配价格":vo.getPrice());

		return result;
	}	
	
	
	public double getResult(double weight){
		//原重
		double a=weight;	
		//现重
		double c=0.0;	
		int b=(int)a;
		double min=a-b;

		if(0<min && min <0.5){
			c=b+0.5;
		}
		
		if(0.5<min && min<1){
			c=b+1;
		}
		if(min==0 || min==0.5){
			c=a;
		}
		return c;
	}
}