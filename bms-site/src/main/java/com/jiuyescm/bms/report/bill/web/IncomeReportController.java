package com.jiuyescm.bms.report.bill.web;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.data.provider.Page;
import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.base.dictionary.entity.SystemCodeEntity;
import com.jiuyescm.bms.base.dictionary.service.ISystemCodeService;
import com.jiuyescm.bms.billcheck.BillCheckInfoEntity;
import com.jiuyescm.bms.billcheck.service.IBillCheckInfoService;
import com.jiuyescm.bms.report.bill.CheckReceiptEntity;
import com.jiuyescm.bms.report.vo.BizWarehouseNotImportVo;
import com.jiuyescm.common.tool.ListTool;
import com.jiuyescm.common.utils.DateUtil;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller("incomeReportController")
public class IncomeReportController {
	private static final Logger logger = Logger.getLogger(IncomeReportController.class.getName());

	@Autowired
	private IBillCheckInfoService billCheckInfoService;

	@DataProvider
	public void query(Page<BillCheckInfoEntity> page, Map<String, Object> parameter) {
		String deptName = (String) parameter.get("deptName");
		String startDate = (String) parameter.get("startDate");
		String endDate =  (String) parameter.get("endDate");
		List<String> dateList =  getMonth(startDate,endDate);
		//报表数据list
		List<BillCheckInfoEntity> reportList = new ArrayList<>();
		//查询条件
		Map<String, Object> map = new HashMap<>();
		map.put("deptName", deptName);
		for (int i = 0; i < dateList.size()-1; i++) {
			map.put("startBizDate", dateList.get(i));
			String calculateFeeDate = dateList.get(i+1);
			map.put("calculateFeeDate",calculateFeeDate );
			//单月份查出来的数据
			List<BillCheckInfoEntity> entities =  billCheckInfoService.queryIncomeReport(map);
//			String year = "20"+calculateFeeDate.substring(0,2);
			//去掉开头的0
//			Integer monthString = Integer.valueOf(calculateFeeDate.substring(2,2));
//			String month = String.valueOf(monthString);
			//放置到全局list
			for (BillCheckInfoEntity billCheckInfoEntity : entities) {
//				billCheckInfoEntity.setBizYear(year);
//				billCheckInfoEntity.setBizMonth(month);
				reportList.add(billCheckInfoEntity);
			}
		}
        //物理分页
		List<List<BillCheckInfoEntity>> list = ListTool.split(reportList, page.getPageSize());
		List<BillCheckInfoEntity> pageList =list.get(page.getPageNo()-1);
		page.setEntities(pageList);
		page.setEntityCount(reportList.size());
		}

	@DataProvider
	public void queryDetail(Page<BillCheckInfoEntity> page, Map<String, Object> param) {

	}
	
	private static List<String> getMonth(String start,String end){
		List<String> list = new ArrayList<String>();
		try{
            SimpleDateFormat format = new SimpleDateFormat("yyMM");
            Date d1 = format.parse(start);//定义起始日期
            Date d2 = format.parse(end);//定义结束日期  可以去当前月也可以手动写日期。
            
            //获取三个月时间
            Calendar d = Calendar.getInstance();
            d.setTime(d1);
            d.add(Calendar.MONTH, -1);
            list.add(format.format(d.getTime()));
            
            Calendar dd = Calendar.getInstance();//定义日期实例
            dd.setTime(d1);//设置日期起始时间
            while (dd.getTime().before(d2)) {//判断是否到结束日期
                String str = format.format(dd.getTime());
                System.out.println(str);//输出日期结果
                dd.add(Calendar.MONTH, 1);//进行当前日期月份加1
                list.add(str);
            }
            list.add(end);
        }catch (Exception e){
            System.out.println("异常"+e.getMessage());
        }
		return list;
	}
}
