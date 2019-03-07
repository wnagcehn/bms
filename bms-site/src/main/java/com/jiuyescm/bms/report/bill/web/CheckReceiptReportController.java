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
		if (CollectionUtils.isNotEmpty(codeEntities)) {
			//查询快照
			map.put("startDate", startString);
			map.put("endDate", endString);
			List<BillCheckInfoEntity> checkInfoEntities =  billCheckInfoService.querySnapshot(map);
			//预计金额map
			Map<String,Object> expectMap = new HashMap<>();
			for (BillCheckInfoEntity billCheckInfoEntity : checkInfoEntities) {
				StringBuilder stringBuilder = new StringBuilder(billCheckInfoEntity.getArea());
				stringBuilder.append("-").append(format.format(billCheckInfoEntity.getExpectReceiptDate()));
				String key = stringBuilder.toString();
				if(expectMap.containsKey(key)){
					BigDecimal expectAmount = (BigDecimal) expectMap.get(key);
					BigDecimal add = expectAmount.add(billCheckInfoEntity.getExpectAmount());
					expectMap.put(key, add);
					continue;
				}
				expectMap.put(key, billCheckInfoEntity.getExpectAmount());
			}
			//查询回款
			List<BillCheckInfoEntity> receiptEntities =  billCheckInfoService.queryReceipt(map);
			//实际金额map
			Map<String,Object> receiptMap = new HashMap<>();
			for (BillCheckInfoEntity billCheckInfoEntity : receiptEntities) {
				StringBuilder stringBuilder = new StringBuilder(billCheckInfoEntity.getArea());
				String time = format.format(billCheckInfoEntity.getReceiptDate());
				stringBuilder.append("-").append(time);
				String key = stringBuilder.toString();
				if(receiptMap.containsKey(key)){
					BigDecimal receiptAmount = (BigDecimal) receiptMap.get(key);
					BigDecimal add = receiptAmount.add(billCheckInfoEntity.getReceiptAmount());
					receiptMap.put(key, add);
					continue;
				}
				receiptMap.put(key, billCheckInfoEntity.getReceiptAmount());
			}
			
			//初始化区域模型
			for (String dateString : dateList) {
				for (SystemCodeEntity entity : codeEntities) {
					CheckReceiptEntity checkReceiptEntity = new CheckReceiptEntity();
					checkReceiptEntity.setDeptName(deptName);
					try {
						checkReceiptEntity.setExpectDate(format.parse(dateString));
					} catch (ParseException e) {
						e.printStackTrace();
					}
					String areaString = entity.getCodeName();
					checkReceiptEntity.setArea(areaString);
					//通过key找出对应的预计金额和实际金额
					StringBuilder key = new StringBuilder(areaString);
					key.append("-").append(dateString);
					String keyString = key.toString();
					BigDecimal expectBigDecimal;
					BigDecimal receiptBigDecimal;
					if(expectMap.containsKey(keyString)){
						expectBigDecimal=(BigDecimal) expectMap.get(keyString);
					}else{
						expectBigDecimal=new BigDecimal(0);
					}
					if(receiptMap.containsKey(keyString)){
						receiptBigDecimal=(BigDecimal) receiptMap.get(keyString);
					}else{
						receiptBigDecimal=new BigDecimal(0);
					}
					checkReceiptEntity.setExpectAmount(expectBigDecimal);
					checkReceiptEntity.setFinishAmount(receiptBigDecimal);
					//计算完成率
					String finish ="";
					if(expectBigDecimal.equals(BigDecimal.ZERO)){
						finish = "100%";
					}else if(receiptBigDecimal.equals(BigDecimal.ZERO)) {
						finish = "0%";
					}else {
						BigDecimal div = receiptBigDecimal.divide(expectBigDecimal,6, RoundingMode.HALF_UP);
						BigDecimal mul = div.multiply(new BigDecimal(100));
						String num = mul.toString();
						String numString = num.substring(0,num.length()-2);
						finish = numString+"%";
					}
					checkReceiptEntity.setFinishRate(finish);
					checkList.add(checkReceiptEntity);
				}
			}
			//物理分页
			List<List<CheckReceiptEntity>> list = ListTool.split(checkList, page.getPageSize());
			List<CheckReceiptEntity> pageList =list.get(page.getPageNo()-1);
			page.setEntities(pageList);
			page.setEntityCount(checkList.size());
		}

	}
	
	@DataProvider
	public void queryDetail(Page<BillCheckInfoEntity> page, Map<String, Object> param) {
		List<BillCheckInfoEntity> checkList = billCheckInfoService.queryCheckReceipt(param);
		if(CollectionUtils.isNotEmpty(checkList)){
			List<BillCheckInfoEntity> snapshotList = billCheckInfoService.querySnapshotExpect(param);
			Map<String,BillCheckInfoEntity> snapshotMap = new HashMap<>();
			//防止有回款，但快照中没有账单，加此判断
			if(CollectionUtils.isNotEmpty(snapshotList)){
				for (BillCheckInfoEntity entity : snapshotList) {
					snapshotMap.put(entity.getId()+"", entity);
				}
			}
			//初始化返回明细
			List<BillCheckInfoEntity> pageEntities = new ArrayList<>();
			//如果快照后账单及回款作废，则无此明细，所以使用快照数据过滤实际账单数据
			for (BillCheckInfoEntity check : checkList) {
				String keyString = check.getId()+"";
				//如果账单及快照都存在数据，使用快照的预计金额，如果不是置为0
				if(snapshotMap.containsKey(keyString)){
					check.setExpectAmount(snapshotMap.get(keyString).getExpectAmount());
				}else {
					check.setExpectAmount(new BigDecimal(0));
				}
				pageEntities.add(check);
			}
			//物理分页
			List<List<BillCheckInfoEntity>> list = ListTool.split(pageEntities, page.getPageSize());
			List<BillCheckInfoEntity> pageList =list.get(page.getPageNo()-1);
			page.setEntities(pageList);
			page.setEntityCount(checkList.size());
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
