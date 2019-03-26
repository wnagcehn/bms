package com.jiuyescm.bms.calcu.base;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.jiuyescm.bms.calculate.api.IBmsCalcuService;
import com.jiuyescm.bms.calculate.vo.BmsFeesQtyVo;

public class CalcuServiceDisBase<T> extends CalcuServiceBase<T> {

	@Autowired IBmsCalcuService bmsCalcuService;
	
	@Override
	public BmsFeesQtyVo feesCountReport(String customerId, String subjectCode,Integer creMonth) {
		BmsFeesQtyVo vo = bmsCalcuService.queryFeesQtyForDis(customerId, subjectCode, creMonth);
		return vo;
	}

	@Override
	public List<T> queryBizList(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateFees(List<T> list) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public T contractCalcu(T t) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public T bmsCalcu(T t) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isCalcu(T t) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public T initChargeParam(T t) {
		// TODO Auto-generated method stub
		return null;
	}

}
