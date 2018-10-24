package com.jiuyescm.bms.jobhandler;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.base.dictionary.entity.SystemCodeEntity;
import com.jiuyescm.bms.base.group.service.IBmsGroupCustomerService;
import com.jiuyescm.bms.base.group.service.IBmsGroupService;
import com.jiuyescm.bms.base.group.vo.BmsGroupCustomerVo;
import com.jiuyescm.bms.base.group.vo.BmsGroupVo;
import com.jiuyescm.bms.general.service.ISystemCodeService;
import com.jiuyescm.bms.report.month.entity.ReportReceiptTargetEntity;
import com.jiuyescm.bms.report.month.repository.IReportReceiptTargetRepository;
import com.jiuyescm.mdm.customer.api.ICustomerService;
import com.jiuyescm.mdm.customer.vo.CustomerVo;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHander;
import com.xxl.job.core.log.XxlJobLogger;

/**
 * 收款指标入统计(每个月20号20点保存一次)
 * @author yangss
 */
@JobHander(value="reportReceiptTargetJob")
@Service
public class ReportReceiptTargetJob extends IJobHandler{
	
	@Resource
	private IReportReceiptTargetRepository receiptTargetRepository;
	
	@Autowired 
	private ISystemCodeService systemCodeService;
	
	@Resource
	private ICustomerService customerService;
	
	@Autowired 
	private IBmsGroupService bmsGroupService;
	@Autowired 
	private IBmsGroupCustomerService bmsGroupCustomerService;

	
	@Override
	public ReturnT<String> execute(String... params) throws Exception {
		XxlJobLogger.log("ReportReceiptTargetJob start.");
        return CalcJob(params);
	}
	
	private ReturnT<String> CalcJob(String[] params) {
		try {
			int preMonth=0;
			if(params != null && params.length > 0) { 
				
				if (StringUtils.isNotBlank(params[0])) {
					// 指定月份
					preMonth = Integer.parseInt(params[0]);
				}
			}
			
			Map<String,Object> parameter=new HashMap<String, Object>();
			
			String date="";
			
			Calendar cal = Calendar.getInstance();
			int year=cal.get(Calendar.YEAR);
			int month = cal.get(Calendar.MONTH)-preMonth;
			parameter.put("year", year);
			parameter.put("month", month);
			
			if(month<10){
				date=year+"0"+month;
			}else{
				date=year+""+month;
			}
			parameter.put("createMonth", date.substring(2));

			//所有的指标系数
			parameter.put("typeCode", "RECEIPT_TARGET");
			List<SystemCodeEntity> systemCodeList = systemCodeService.querySysCodes(parameter);
			for(SystemCodeEntity code:systemCodeList){
				parameter.put(code.getCode(), code.getExtattr1());
			}
			
			//指定的异常商家
			try {			
				Map<String,String> customerMap=customerMap();;
				Map<String, Object> map= new HashMap<String, Object>();
				map.put("groupCode", "error_customer");
				map.put("bizType", "group_customer");
				BmsGroupVo bmsGroup=bmsGroupService.queryOne(map);
				if(bmsGroup!=null){			
					List<BmsGroupCustomerVo> custList=bmsGroupCustomerService.queryAllByGroupId(bmsGroup.getId());
					List<String> billList=new ArrayList<String>();
					for(BmsGroupCustomerVo vo:custList){
						billList.add(customerMap.get(vo.getCustomerid()));
					}
					if(billList.size()>0){
						parameter.put("billList", billList);
					}
				}	
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			//时间转换
			createMonthTran2(parameter);
			createMonthTran1To2(parameter);
			createMonthTran1(parameter);
			receiptDateTran(parameter);
			
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
	
    public Map<String,String> customerMap(){
    	Map<String,String> map=new HashMap<String,String>();
		List<CustomerVo> cusList=customerService.queryAll();
		for(CustomerVo vo:cusList){
			map.put(vo.getCustomerid(), vo.getMkInvoiceName());
		}
		
		return map;

    }
}
