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
	
	@Resource
	private ISystemCodeService systemCodeService;
	@Autowired
	private IBillCheckInfoService billCheckInfoService;

	@DataProvider
	public void query(Page<BillCheckInfoEntity> page, Map<String, Object> parameter) {
/*		String deptName = (String) parameter.get("deptName");
		Date startDate = (Date) parameter.get("startDate");
		Date endDate = (Date) parameter.get("endDate");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String startString = format.format(startDate);
        String endString = format.format(endDate);
        List<String> dateList = DateUtil.getBetweenDate(startString,endString);
        //查询区域
		Map<String, Object> map = new HashMap<>();
		map.put("typeCode", "SALE_AREA");
		map.put("deptName", deptName);
		List<SystemCodeEntity> codeEntities = systemCodeService.queryExtattr1(map);*/

		}

	@DataProvider
	public void queryDetail(Page<BillCheckInfoEntity> page, Map<String, Object> param) {

	}

}
