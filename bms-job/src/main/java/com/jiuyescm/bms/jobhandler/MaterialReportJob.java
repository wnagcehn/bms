package com.jiuyescm.bms.jobhandler;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyescm.bms.receivable.storage.service.IMaterialReportService;
import com.jiuyescm.bms.report.month.entity.BmsMaterialReportEntity;
import com.jiuyescm.common.utils.DateUtil;
import com.jiuyescm.mdm.customer.api.IPubMaterialInfoService;
import com.jiuyescm.mdm.customer.vo.PubMaterialInfoVo;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHander;
import com.xxl.job.core.log.XxlJobLogger;

@JobHander(value="materialReportJob")
@Service
public class MaterialReportJob extends IJobHandler{

	@Autowired private IMaterialReportService materialdiffReportService;
	
	@Autowired						
	private IPubMaterialInfoService pubMaterialInfoService;
	
	@Override
	public ReturnT<String> execute(String... params) throws Exception {
		XxlJobLogger.log("materialReportJob start.");
        return CalcJob(params);
	}
	
	
	private ReturnT<String> CalcJob(String[] params) {
		
		int _defalut = 5; //默认一次查询的天数  查询天数过多会导致索引失效
		
		//当前的时间
		Calendar cal = Calendar.getInstance();
		int year1 = cal.get(Calendar.YEAR);
		int month2 = cal.get(Calendar.MONTH );
		String newDate=formatMonth(year1, month2);
		//年份
		String year=newDate.substring(0, 4);
		String month=newDate.substring(5, 7);
		
		//根据条件查询bms耗材结算明细并将其写入临时表
		//删除所有的bms耗材结算明细表的所有数据
		materialdiffReportService.deleteBmsMaterailTemp();
		materialdiffReportService.deleteWmsMaterailTemp();

		Map<String, Object> condition=new HashMap<String, Object>();
		
		
        String lastMonthFirstDay = year.toString()+"-"+month.toString()+"-01 00:00:00";
        Timestamp startTime = Timestamp.valueOf(lastMonthFirstDay);
        Timestamp endTime = DateUtil.addMonths(startTime, 1);

        XxlJobLogger.log("开始时间：【{0}】",startTime.toString());
        XxlJobLogger.log("结束时间：【{0}】",endTime.toString());
		
		while(DateUtil.getDays(startTime, endTime)>0){
			
			if(DateUtil.getDays(startTime, endTime)<=_defalut){//  <=5 天 
				condition.put("startTime", startTime);
				condition.put("endTime",endTime);
				report(condition);
				break;
			}
			else{
				condition.put("startTime", startTime);
				condition.put("endTime", DateUtil.addDays(startTime, _defalut));
				report(condition);
				startTime = DateUtil.addDays(startTime, _defalut);
			}
			
			XxlJobLogger.log("开始时间：【{0}】",startTime.toString());
			XxlJobLogger.log("结束时间：【{0}】",condition.get("endTime"));
		}
			
		
		List<BmsMaterialReportEntity> bmsTempList = materialdiffReportService.queryBmsMaterialReportTemp(null);
		List<BmsMaterialReportEntity> wmsTempList = materialdiffReportService.queryWmsMaterialReportTemp(null);
		
		Map<String, BmsMaterialReportEntity> rtn_Map = new HashMap<String, BmsMaterialReportEntity>();
		for (BmsMaterialReportEntity entity : bmsTempList) {
			String key = entity.getWarehouseCode()+entity.getCustomerid()+entity.getConsumerMaterialCode();
			if(rtn_Map.containsKey(key)){
				rtn_Map.get(key).setBmsNum(rtn_Map.get(key).getBmsNum().add(entity.getBmsNum()));
			}
			else{
				rtn_Map.put(key, entity);
			}
		}
		
		for (BmsMaterialReportEntity entity : wmsTempList) {
			String key = entity.getWarehouseCode()+entity.getCustomerid()+entity.getConsumerMaterialCode();
			if(rtn_Map.containsKey(key)){
				rtn_Map.get(key).setWmsNum(rtn_Map.get(key).getWmsNum().add(entity.getWmsNum()));
			}
			else{
				rtn_Map.put(key, entity);
			}
		}
		
		Map<String, String> getMaterial=getAllMaterial();
		List<BmsMaterialReportEntity> reportList =  new ArrayList<BmsMaterialReportEntity>();
		for (Map.Entry<String, BmsMaterialReportEntity> entry : rtn_Map.entrySet()) { 
			BmsMaterialReportEntity entity = entry.getValue();
			entity.setDifferentNum(entity.getWmsNum().subtract(entity.getBmsNum()));
			entity.setDelFlag("0");
			entity.setConsumerMaterialType(getMaterial.get(entity.getConsumerMaterialCode()));
			entity.setReportYear(year);
			entity.setReportMonth(month);
			reportList.add(entity);
		}
		
		condition.put("year", year);
		condition.put("month", month);
		materialdiffReportService.deleteMaterialReport(condition);
		int result3=materialdiffReportService.insertMaterialReport(reportList);
		if(result3<=0){
			XxlJobLogger.log("未写入任何数据到耗材差异报表");
		}else{
			XxlJobLogger.log("写入【{0}】条数据到bms耗材差异报表",result3);
		}
		return ReturnT.SUCCESS;
		
		
		
		/*List<BmsMaterialReportEntity> reportList=materialdiffReportService.queryMaterialReportList(condition);
		List<BmsMaterialReportEntity> insertList=new ArrayList<>();
		//插入耗材差异的数据
		if(reportList.size()>0){
			Map<String, String> getMaterial=getAllMaterial();
			for(BmsMaterialReportEntity entity:reportList){
				if(entity!=null){
					entity.setDelFlag("0");
					entity.setConsumerMaterialType(getMaterial.get(entity.getConsumerMaterialCode()));
					entity.setReportYear(year);
					entity.setReportMonth(month);
					insertList.add(entity);
				}
				
			}
			
			//先删除耗材差异表当月的数据
			condition.put("year", year);
			condition.put("month", month);
			materialdiffReportService.deleteMaterialReport(condition);
			int result3=materialdiffReportService.insertMaterialReport(insertList);
			if(result3<=0){
				XxlJobLogger.log("耗材写入耗材差异表失败");
			}else{
				XxlJobLogger.log("耗材写入耗材差异表成功");
			}
		}else{
			XxlJobLogger.log("没有需要插入耗材差异的数据");
		}	*/
		
	}
	
	private void report(Map<String, Object> condition){
		
		int result1=materialdiffReportService.insertBmsMaterailTemp(condition);
		if(result1<=0){
			XxlJobLogger.log("未写入任何数据到bms耗材统计临时表");
		}
		XxlJobLogger.log("写入【{0}】条数据到bms耗材统计临时表",result1);
		
		int result2=materialdiffReportService.insertWmsMaterailTemp(condition);
		if(result2<=0){
			XxlJobLogger.log("未写入任何数据到wms耗材统计临时表");
		}
		XxlJobLogger.log("写入【{0}】条数据到bms耗材统计临时表",result2);
	}
	
	public Map<String, String> getAllMaterial(){
		Map<String, Object> con=new HashMap<String, Object>();
		List<PubMaterialInfoVo> materialList=pubMaterialInfoService.queryList(con);		
		Map<String, String> returnMap=new HashMap<String, String>();		
		for(PubMaterialInfoVo en:materialList){
			returnMap.put(en.getBarcode(), en.getMaterialType());
		}		
		return returnMap;
	}
	
	
	private String formatMonth(int year,int month){
		String mString="";	
		if(month<=0){
			mString=(12+month)+"";
			 year=year-1;
		}
		if (month >= 1 && month <= 9) {
			mString = "0" + month;
	    }
		return year+"-"+mString;
	}
}
