package com.jiuyescm.bms.calculate.base;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.jiuyescm.bms.base.calcu.vo.CalcuReqVo;
import com.jiuyescm.bms.base.calcu.vo.CalcuResultVo;


@Service("feesCalcuService")
public class FeesCalcuServiceImpl implements IFeesCalcuService {

	private static final Logger logger = Logger.getLogger(FeesCalcuServiceImpl.class.getName());

	
	@Resource private IDroolsCalcuService droolsCalcuServiceImpl;
	
	@Override
	public CalcuResultVo FeesCalcuService(CalcuReqVo reqVo) {
		
		long start = System.currentTimeMillis();
		
		CalcuResultVo resultVo = new CalcuResultVo();
		if(reqVo.getBizData() == null){
			return new CalcuResultVo("fail","","业务数据必须传入",null);
		}
		if(reqVo.getQuoEntites() == null && reqVo.getQuoEntity()==null){
			return new CalcuResultVo("fail","","报价数据必须传入",null);
		}
		if(StringUtils.isEmpty(reqVo.getRuleNo()) || StringUtils.isEmpty(reqVo.getRuleStr())){
			return new CalcuResultVo("fail","","规则代码和规则编号必提供",null);
		}
		droolsCalcuServiceImpl.excute(resultVo,reqVo,reqVo.getBizData(),reqVo.getRuleStr(),reqVo.getRuleNo());
		long current = System.currentTimeMillis();
		
		if(resultVo.getPrice() == null){
			logger.info("【费用计算执行失败】,耗时："+ (current - start) + "毫秒");
			return new CalcuResultVo("fail","","规则计算不成功",resultVo.getPrice(),resultVo.getQuoId(),resultVo.getMethod(),resultVo.getUnitPrice());
		}
		else{
			logger.info("【费用计算执行成功】,耗时：【"+ (current - start) + "】毫秒，费用【"+resultVo.getPrice()+"】");
			return new CalcuResultVo("succ","","计算成功",resultVo.getPrice(),resultVo.getQuoId(),resultVo.getMethod(),resultVo.getUnitPrice());
		}
	}

}