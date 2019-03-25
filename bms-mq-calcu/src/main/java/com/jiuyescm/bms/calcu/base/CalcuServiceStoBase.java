package com.jiuyescm.bms.calcu.base;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.jiuyescm.bms.calculate.api.IBmsCalcuService;
import com.jiuyescm.bms.calculate.vo.BmsFeesQtyVo;

public class CalcuServiceStoBase<T, F> extends CalcuServiceBase<T, F>{

	@Autowired IBmsCalcuService bmsCalcuService;

	@Override
	public BmsFeesQtyVo feesCountReport(String customerId, String subjectCode,Integer creMonth) {
		BmsFeesQtyVo vo = bmsCalcuService.queryFeesQtyForSto(customerId, subjectCode, creMonth);
		return vo;
	}

	@Override
	public List<T> queryBizList(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateFees(List<F> list) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public F contractCalcu(T t) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public F bmsCalcu(T t) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isCalcu(T t) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public F initChargeParam(T t) {
		// TODO Auto-generated method stub
		return null;
	}

}
