package com.jiuyescm.bms.jobhandler;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONObject;
import net.sf.json.util.JSONUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.base.dictionary.entity.SystemCodeEntity;
import com.jiuyescm.bms.general.service.ISystemCodeService;
import com.jiuyescm.bms.report.month.entity.ReportReceiptTargetEntity;
import com.jiuyescm.bms.report.month.repository.IReportReceiptTargetRepository;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHander;
import com.xxl.job.core.log.XxlJobLogger;

/**
 * 收款指标入统计
 * @author yangss
 */
@JobHander(value="reportReceiptTargetJob")
@Service
public class ReportReceiptTargetJob extends IJobHandler{
	
	@Resource
	private IReportReceiptTargetRepository receiptTargetRepository;
	
	@Autowired 
	private ISystemCodeService systemCodeService;

	
	@Override
	public ReturnT<String> execute(String... params) throws Exception {
		XxlJobLogger.log("ReportReceiptTargetJob start.");
        return CalcJob(params);
	}
	
	private ReturnT<String> CalcJob(String[] params) {
		try {
			Map<String,Object> parameter=new HashMap<String, Object>();
			
			String date="";
			
			Calendar cal = Calendar.getInstance();
			int year=cal.get(Calendar.YEAR);
			int month = cal.get(Calendar.MONTH);
			parameter.put("year", year);
			parameter.put("month", month);
			
			if(month<10){
				date=year+"0"+month;
			}else{
				date=year+""+month;
			}
			parameter.put("createMonth", date.substring(2));

			//时间转换
			createMonthTran2(parameter);
			createMonthTran1To2(parameter);
			createMonthTran1(parameter);
			receiptDateTran(parameter);
			parameter.put("typeCode", "RECEIPT_TARGET");
			List<SystemCodeEntity> systemCodeList = systemCodeService.querySysCodes(parameter);
			for(SystemCodeEntity code:systemCodeList){
				parameter.put(code.getCode(), code.getExtattr1());
			}
			
			XxlJobLogger.log("查询参数【{0}】",JSONObject.fromObject(parameter));
			
			//先删除原有的统计
			receiptTargetRepository.delete(parameter);
			
			//插入新统计
			PageInfo<ReportReceiptTargetEntity> tmpPageInfo =receiptTargetRepository.queryAll(parameter,0, Integer.MAX_VALUE);
			if(tmpPageInfo!=null && tmpPageInfo.getList().size()>0){
				for(ReportReceiptTargetEntity entity:tmpPageInfo.getList()){
					entity.setCreateMonth(Integer.parseInt(date.substring(2)));
				}
				receiptTargetRepository.saveList(tmpPageInfo.getList());
			} 
			return ReturnT.SUCCESS;
		} catch (Exception e) {
			// TODO: handle exception
			XxlJobLogger.log("统计异常"+e.getMessage());
			return ReturnT.FAIL;
		}
		
	}
	
	
	
	private void createMonthTran2(Map<String, Object> param) {
		Integer month = Integer.parseInt(param.get("month").toString())-2;
		if (month < 0) {
			Integer year = Integer.parseInt(param.get("year").toString())-1;
			param.put("createMonthTran2", String.valueOf(year).substring(2,4) + "0" + month);
		}else if (month >= 10) {
			param.put("createMonthTran2", param.get("year").toString().substring(2, 4) + month.toString());
		}else {
			param.put("createMonthTran2", param.get("year").toString().substring(2, 4) + "0" + month.toString());
		}
	}
	
	private void createMonthTran1To2(Map<String, Object> param) {
		Integer month = Integer.parseInt(param.get("month").toString())-1;
		if (month < 0) {
			Integer year = Integer.parseInt(param.get("year").toString())-1;
			param.put("createMonthTran1To2", String.valueOf(year).substring(2,4) + "0" + month);
		}else if (month >= 10) {
			param.put("createMonthTran1To2", param.get("year").toString().substring(2, 4) + month.toString());
		}else {
			param.put("createMonthTran1To2", param.get("year").toString().substring(2, 4) + "0" + month.toString());
		}
	}
	private void createMonthTran1(Map<String, Object> param) {
		Integer month = Integer.parseInt(param.get("month").toString());
		if(month>=10){
			param.put("createMonthTran1", param.get("year").toString().substring(2, 4) + month);
		}else{
			param.put("createMonthTran1", param.get("year").toString().substring(2, 4) +"0"+ month);
		}
	}

	
	private void receiptDateTran(Map<String, Object> param) {
		Integer month = Integer.parseInt(param.get("month").toString())+1;
		if (month > 12) {
			Integer year = Integer.parseInt(param.get("year").toString())+1;
			param.put("receiptDate", String.valueOf(year) + "-0" + (month-12));
		}else if(month < 10){
			param.put("receiptDate", param.get("year").toString() + "-0" + month.toString());
		}else {
			param.put("receiptDate", param.get("year").toString() + "-" + month.toString());
		}
	}
	
}
