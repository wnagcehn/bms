package com.jiuyescm.bms.rpc.calculate.service.impl;

/*import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;*/
import org.springframework.stereotype.Service;

import com.jiuyescm.bms.common.entity.CalculateVo;
/*import com.jiuyescm.bms.fees.calculate.service.IFeesCalculatePayService;
import com.jiuyescm.bms.fees.calculate.service.IFeesCalculateService;*/
import com.jiuyescm.bms.rpc.calculate.service.IFeesCalculateRpcService;


@Service("feesCalculateRpcServiceImpl")
public class FeesCalculateRpcServiceImpl implements IFeesCalculateRpcService{
	
	/*@Resource private IFeesCalculateService feesCalculateServiceImpl;
	
	@Resource private IFeesCalculatePayService feesCalculatePayServiceImpl;*/
	
	@Override
	public CalculateVo calculate(CalculateVo vo) {
		
		/*if(vo!=null && StringUtils.isBlank(vo.getType())){
			return feesCalculateServiceImpl.calculate(vo);
		}else{
			return feesCalculatePayServiceImpl.calculate(vo);
		}*/
		return null;
	}
	
}
