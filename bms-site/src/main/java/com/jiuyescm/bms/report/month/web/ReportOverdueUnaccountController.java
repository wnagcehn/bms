/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.report.month.web;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;

import com.bstek.dorado.annotation.DataProvider;
import com.jiuyescm.bms.base.group.service.IBmsGroupUserService;
import com.jiuyescm.bms.base.group.vo.BmsGroupUserVo;
import com.jiuyescm.bms.report.month.entity.ReportIncomesSubjectEntity;
import com.jiuyescm.bms.report.month.entity.ReportOverdueUnaccountEntity;
import com.jiuyescm.bms.report.month.service.IReportIncomesSubjectService;
import com.jiuyescm.bms.report.month.service.IReportOverdueUnaccountService;
import com.jiuyescm.cfm.common.JAppContext;

/**
 * 
 * @author stevenl
 * 
 */
@Controller("reportOverdueUnaccountController")
public class ReportOverdueUnaccountController {

	private static final Logger logger = Logger.getLogger(ReportOverdueUnaccountController.class.getName());

	@Resource
	private IReportOverdueUnaccountService reportOverdueUnaccountService;
	
	@Resource
	private IBmsGroupUserService bmsGroupUserService;
	
	@DataProvider
	public List<ReportOverdueUnaccountEntity> queryAll(Map<String, Object> param){
		//超期未收款日期转换
		unCreateMonthTran(param);
		receiptDateTran(param);

		ReportOverdueUnaccountEntity newEntity = null;
		List<ReportOverdueUnaccountEntity> newList = new ArrayList<ReportOverdueUnaccountEntity>();
		BmsGroupUserVo groupUser = bmsGroupUserService.queryEntityByUserId(JAppContext.currentUserID());
		if (null != groupUser) {
			List<String> userIds = bmsGroupUserService.queryContainUserIds(groupUser);
			param.put("userIds", userIds);
			//超期未收款金额
			List<ReportOverdueUnaccountEntity> unList = reportOverdueUnaccountService.queryTotalAmount(param);
			
			//应收款日期转换
			createMonthTran(param);
			//应收款总额
			List<ReportOverdueUnaccountEntity> list = reportOverdueUnaccountService.queryTotalAmount(param);
			for (ReportOverdueUnaccountEntity unEntity : unList) {
				for (ReportOverdueUnaccountEntity entity : list) {
					if (unEntity.getSellerName().equals(entity.getSellerName())) {
						newEntity = new ReportOverdueUnaccountEntity();
						newEntity.setSellerId(unEntity.getSellerId());
						newEntity.setSellerName(unEntity.getSellerName());
						newEntity.setUnReceiptAmount(unEntity.getUnReceiptAmount());
						newEntity.setReceiptAmount(entity.getReceiptAmount());
						newEntity.setOverdueUnaccountRatio(new BigDecimal(unEntity.getUnReceiptAmount()).divide(new BigDecimal(entity.getReceiptAmount()), BigDecimal.ROUND_HALF_UP).doubleValue()+"%");
						newList.add(newEntity);
					}
				}
			}
			return newList;
		}
		return newList;		
	}

	private void unCreateMonthTran(Map<String, Object> param) {
		Integer month = Integer.parseInt(param.get("month").toString())-2;
		if (month < 0) {
			Integer year = Integer.parseInt(param.get("year").toString())-1;
			param.put("createMonth", String.valueOf(year).substring(2,3) + "-0" + month);
		}else if (month > 10) {
			param.put("createMonth", param.get("year").toString().substring(2, 3) + "-" + param.get("month").toString());
		}else {
			param.put("createMonth", param.get("year").toString().substring(2, 3) + "-0" + param.get("month").toString());
		}
	}
	
	private void createMonthTran(Map<String, Object> param) {
		Integer month = Integer.parseInt(param.get("month").toString())+1;
		if (month > 12) {
			Integer year = Integer.parseInt(param.get("year").toString())+1;
			param.put("createMonth", String.valueOf(year).substring(2,3) + "-0" + (month-12));
		}else if (month < 10) {
			param.put("createMonth", param.get("year").toString().substring(2, 3) + "-0" + param.get("month").toString());
		}else {
			param.put("createMonth", param.get("year").toString().substring(2, 3) + "-" + param.get("month").toString());
		}
	}
	
	private void receiptDateTran(Map<String, Object> param) {
		Integer month = Integer.parseInt(param.get("month").toString())+1;
		if (month > 12) {
			Integer year = Integer.parseInt(param.get("year").toString())+1;
			param.put("receiptDate", String.valueOf(year) + "-0" + (month-12));
		}else if(month < 10){
			param.put("receiptDate", param.get("year").toString() + "-0" + param.get("month").toString());
		}else {
			param.put("receiptDate", param.get("year").toString() + "-" + param.get("month").toString());
		}
	}

}
