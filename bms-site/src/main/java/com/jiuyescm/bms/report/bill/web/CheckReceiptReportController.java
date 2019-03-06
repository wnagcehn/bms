package com.jiuyescm.bms.report.bill.web;

import java.math.BigDecimal;
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

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller("checkReceiptReportController")
public class CheckReceiptReportController {
	private static final Logger logger = Logger.getLogger(CheckReceiptReportController.class.getName());
	
	@Resource
	private ISystemCodeService systemCodeService;
	@Autowired
	private IBillCheckInfoService billCheckInfoService;

	@DataProvider
	public void query(Page<CheckReceiptEntity> page, Map<String, Object> parameter) {
		String deptName = (String) parameter.get("deptName");
		Date startDate = (Date) parameter.get("startDate");
		Date endDate = (Date) parameter.get("endDate");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String startString = format.format(startDate);
        String endString = format.format(endDate);
        List<String> dateList = getBetweenDate(startString,endString);
        //查询区域
		Map<String, Object> map = new HashMap<>();
		map.put("typeCode", "SALE_AREA");
		map.put("deptName", deptName);
		List<SystemCodeEntity> codeEntities = systemCodeService.queryExtattr1(map);
		//所有区域
		List<String> deptList = new ArrayList<>();
		//区域模型
		ArrayList<CheckReceiptEntity> checkList = new ArrayList<CheckReceiptEntity>();
//		PageInfo<CheckReceiptEntity> pageInfo = null;
		if (CollectionUtils.isNotEmpty(codeEntities)) {
			//查询快照
			map.put("startDate", startString);
			map.put("endDate", endString);
			List<BillCheckInfoEntity> checkInfoEntities =  billCheckInfoService.querySnapshot(map);
			Map<String,Object> expectMap = new HashMap<>();
			for (BillCheckInfoEntity billCheckInfoEntity : checkInfoEntities) {
				StringBuilder stringBuilder = new StringBuilder(billCheckInfoEntity.getArea());
				stringBuilder.append("-").append(format.format(billCheckInfoEntity.getExpectReceiptDate()));
				String key = stringBuilder.toString();
				if(expectMap.containsKey(key)){
					BigDecimal expectAmount = (BigDecimal) expectMap.get(key);
					expectAmount.add(billCheckInfoEntity.getExpectAmount());
					expectMap.put(key, expectAmount);
				}
				
			}
			
			
			
			
			//初始化区域模型
			for (String dateString : dateList) {
				for (SystemCodeEntity entity : codeEntities) {
					
					CheckReceiptEntity checkReceiptEntity = new CheckReceiptEntity();
					try {
						checkReceiptEntity.setExpectDate(format.parse(dateString));
					} catch (ParseException e) {
						e.printStackTrace();
					}
					checkReceiptEntity.setArea(entity.getExtattr1());
					checkList.add(checkReceiptEntity);
				}
			}
			
			
			//page.getPageNo(), page.getPageSize()
		
			
			
//			page.setEntities(pageInfo.getList());
//			page.setEntityCount(pageInfo.);
		}
	}
	
	
    private static List<String> getBetweenDate(String begin,String end){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        List<String> betweenList = new ArrayList<String>();
        try{
            Calendar startDay = Calendar.getInstance();
            startDay.setTime(format.parse(begin));
            startDay.add(Calendar.DATE, -1);
            while(true){
                startDay.add(Calendar.DATE, 1);
                Date newDate = startDay.getTime();
                String newend=format.format(newDate);
                betweenList.add(newend);
                if(end.equals(newend)){
                    break;
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return betweenList;
    }
}
