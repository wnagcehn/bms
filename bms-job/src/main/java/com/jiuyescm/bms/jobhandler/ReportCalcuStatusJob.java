package com.jiuyescm.bms.jobhandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyescm.bms.base.customer.entity.PubCustomerEntity;
import com.jiuyescm.bms.base.customer.repository.IPubCustomerRepository;
import com.jiuyescm.common.utils.DateUtil;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHander;
import com.xxl.job.core.log.XxlJobLogger;

/**
 * 商家费用计算状态统计报表
 * @author caojianwei
 *
 */
@JobHander(value="reportCalcuStatusJob")
@Service
public class ReportCalcuStatusJob extends IJobHandler{

	@Autowired IPubCustomerRepository pubCustomerRepository;
	
	@Override
	public ReturnT<String> execute(String... params) throws Exception {
		XxlJobLogger.log("ReportCalcuStatusJob start.");
		XxlJobLogger.log("开始商家费用计算状态统计");
		ReturnT<String> rtn = ReturnT.SUCCESS;
		try{
			long starttime= System.currentTimeMillis();// 系统开始时间
			rtn =CalcJob(params);
			XxlJobLogger.log("job结束 总耗时【{0}】",(System.currentTimeMillis() - starttime));
		}
		catch(Exception ex){
			XxlJobLogger.log("处理异常：{0}",ex);
			rtn = ReturnT.FAIL;
		}
        return rtn;
	}
	
	private ReturnT<String> CalcJob(String[] params) {
		
		String lastMonthFirstDay = "";
		String curMonthFirstDay = "";
		String preMonthFirstDay = "";
		String dateformat = "yyyy-MM-dd";
		//如果指定了当前时间
		if(params!=null && params.length>0 && DateUtil.isValidDate(params[0], dateformat)){
			XxlJobLogger.log("收到客户端指定的当前时间{0}",params[0]);
			lastMonthFirstDay = DateUtil.getFirstDayOfGivenMonth(params[0], -1, dateformat); 
			curMonthFirstDay =  DateUtil.getFirstDayOfGivenMonth(params[0], 0, dateformat);
			preMonthFirstDay = DateUtil.getFirstDayOfGivenMonth(params[0], 1, dateformat); 
		}
		else{
			XxlJobLogger.log("使用当前日期");
			lastMonthFirstDay = DateUtil.getFirstDayOfMonth(1,dateformat);
			curMonthFirstDay = DateUtil.getFirstDayOfMonth(0,dateformat);
			preMonthFirstDay = DateUtil.getFirstDayOfMonth(-1,dateformat);
		}
		Map<String, Object> condition = new HashMap<String, Object>();
		condition.put("delFlag", "0");
		List<PubCustomerEntity> custList = pubCustomerRepository.query(condition);
		if(custList == null || custList.size() == 0){
			XxlJobLogger.log("未查询到任何商家");
			return ReturnT.SUCCESS;
		}
		XxlJobLogger.log("商家数量【{0}】",custList.size());
		for (PubCustomerEntity cust : custList) {
			report(lastMonthFirstDay,curMonthFirstDay,cust.getCustomerId());
			report(curMonthFirstDay,preMonthFirstDay,cust.getCustomerId());
		}
		return ReturnT.SUCCESS;
	}
	
	//
	private void report(String startDate,String endDate,String custID){
		long starttime= System.currentTimeMillis();// 系统开始时间
		XxlJobLogger.log("**********统计商家【{0}】数据,开始时间【{1}】 结束时间【{2}】",custID,startDate,endDate);
		
		XxlJobLogger.log("统计完毕 耗时【{0}】",(System.currentTimeMillis() - starttime));
	}

}
