package com.jiuyescm.bms.trial.transport.web;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bstek.dorado.annotation.DataResolver;
import com.jiuyescm.bms.biz.storage.entity.ReturnData;
import com.jiuyescm.bms.biz.transport.entity.BizGanxianWayBillEntity;
import com.jiuyescm.bms.common.entity.CalculateVo;
import com.jiuyescm.bms.fees.calculate.service.IFeesCalculateService;

@Controller("transportFeeTrialCalculator")
public class TransportFeeTrialCalculateController {

	@Resource private IFeesCalculateService feesCalculateServiceImpl;
	
	@DataResolver
	public  @ResponseBody Object tryCalculateTransportFee(BizGanxianWayBillEntity data){
		data.setCalResult(null);
		
		CalculateVo calculateVo = new CalculateVo();
		calculateVo.setSubjectId(data.getTransportType());
		calculateVo.setBizTypeCode(data.getBizTypeCode());
		calculateVo.setContractCode(data.getContractCode());
		if (StringUtils.isNotEmpty(data.getValidProvince()) &&
				"0".equals(data.getValidProvince())) {
			data.setSendProvinceId(null);
			data.setSendProvinceName(null);
			data.setReceiverProvinceId(null);
			data.setReceiverProvinceName(null);
		}
		calculateVo.setObj(data);
		
		calculateVo = feesCalculateServiceImpl.calculate(calculateVo);
		if(calculateVo != null){
			data.setCalResult(calculateVo.getPrice());
		}
		ReturnData result = new ReturnData();
		
		result.setCode("SUCCESS");
		result.setData(data.getCalResult() ==null ? "无匹配价格" : data.getCalResult().toString());
		
		return result;
		
	}
}
