package com.jiuyescm.bms.report;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.jiuyescm.bms.report.month.entity.MaterialImportReportEntity;
import com.jiuyescm.bms.report.month.repository.IMaterialReportRepo;
import com.jiuyescm.bms.report.service.IMaterialReportService;
import com.jiuyescm.bms.report.vo.MaterailOutReportVo;
import com.jiuyescm.bms.report.vo.MaterialImportReportVo;
import com.jiuyescm.bs.util.HttpPostUtil;
import com.jiuyescm.common.utils.DateUtil;
import com.jiuyescm.exception.BizException;

@Service("materialReportService")
public class MaterialReportServiceImpl implements IMaterialReportService {
	
	private static final Logger logger = LoggerFactory.getLogger(MaterialReportServiceImpl.class.getName());

	@Autowired IMaterialReportRepo materialReportRepo;
	
	@Override
	public List<MaterialImportReportVo> materialImportReport(Map<String, Object> map) throws Exception {
		
		int _defalut = 5; //默认一次查询的天数  查询天数过多会导致索引失效
		Timestamp startTime = null;
		Timestamp endTime = null;
		if(map.containsKey("startTime")){
			startTime =new Timestamp(((java.util.Date)map.get("startTime")).getTime());
		}
		if(map.containsKey("endTime")){
			endTime =new Timestamp(((java.util.Date)map.get("endTime")).getTime());
		}
		int days = DateUtil.getDays(startTime, endTime);
		if(days<0){//开始时间大于结束时间
			 throw new Exception("查询开始时间不能大于结束时间");
		}
		if(days>31){
			 throw new Exception("查询时间间隔不能大于一个月");
		}
		
		
		List<MaterialImportReportEntity> dispatchNumReport = new ArrayList<MaterialImportReportEntity>();
		List<MaterialImportReportEntity> materialNumReport = new ArrayList<MaterialImportReportEntity>();
		while(DateUtil.getDays(startTime, endTime)>0){
			
			if(DateUtil.getDays(startTime, endTime)<=_defalut){//  <=5 天 
				map.put("startTime", startTime);
				map.put("endTime",endTime);
				dispatchNumReport.addAll(materialReportRepo.dispatchNumReport(map));
				materialNumReport.addAll(materialReportRepo.materialNumReport(map));
				break;
			}
			else{
				map.put("startTime", startTime);
				map.put("endTime", DateUtil.addDays(startTime, _defalut));
				dispatchNumReport.addAll(materialReportRepo.dispatchNumReport(map));
				materialNumReport.addAll(materialReportRepo.materialNumReport(map));
				startTime = DateUtil.addDays(startTime, _defalut);
			}
		}
		
		
		/*if(DateUtil.getDays(startTime, endTime)<_defalut){
			//dispatchNumReport.addAll(materialReportRepo.dispatchNumReport(map));
			//materialNumReport.addAll(materialReportRepo.materialNumReport(map));
			startTime = DateUtil.addDays(startTime, _defalut);
			map.put("startTime", startTime);
		}
		List<MaterialImportReportEntity> dispatchNumReport =materialReportRepo.dispatchNumReport(map);
		List<MaterialImportReportVo> rtnVos=new ArrayList<MaterialImportReportVo>();
		for(MaterialImportReportEntity entity:dispatchNumReport){
			MaterialImportReportVo vo=new MaterialImportReportVo();
			PropertyUtils.copyProperties(vo, entity);
			rtnVos.add(vo);
		}
		return rtnVos;*/
	
		
		Map<String, MaterialImportReportVo> rtn_Map = new HashMap<String, MaterialImportReportVo>();
		try{
			for (MaterialImportReportEntity entity : dispatchNumReport) {
				MaterialImportReportVo voEntity = new MaterialImportReportVo();
				PropertyUtils.copyProperties(voEntity,entity);
				String key = entity.getWarehouseCode()+entity.getCustomerId();
				if(rtn_Map.containsKey(key)){
					rtn_Map.get(key).setDispatchNum(rtn_Map.get(key).getDispatchNum()+voEntity.getDispatchNum());
				}
				else{
					rtn_Map.put(key, voEntity);
				}
			}
			
			for (MaterialImportReportEntity entity : materialNumReport) {
				MaterialImportReportVo voEntity = new MaterialImportReportVo();
				PropertyUtils.copyProperties(voEntity,entity);
				String key = entity.getWarehouseCode()+entity.getCustomerId();
				if(rtn_Map.containsKey(key)){
					rtn_Map.get(key).setMaterialNum(rtn_Map.get(key).getMaterialNum()+voEntity.getMaterialNum());
				}
				else{
					rtn_Map.put(key, voEntity);
				}
			}
		}
		catch(Exception ex){
			logger.error("PropertyUtils.copyProperties Exception   ",ex);
			throw ex;
		}
		List<MaterialImportReportVo> rtnVos = new ArrayList<MaterialImportReportVo>();
		for (Map.Entry<String, MaterialImportReportVo> entry : rtn_Map.entrySet()) { 
			MaterialImportReportVo vo = entry.getValue();
			vo.setDiffNum(vo.getMaterialNum() - vo.getDispatchNum()); //差异量 = 耗材单量 - 出库单量
			rtnVos.add(vo);
		}
		return rtnVos;
		
	}

    @Override
    public PageInfo<MaterailOutReportVo> query(Map<String, Object> condition, int pageNo, int pageSize) throws BizException {
        String url = "http://192.168.17.39:8080/bms_materail/query_list";
        condition.put("pageNo", pageNo);
        condition.put("pageSize", pageSize);
        Integer billYear = Integer.valueOf(condition.get("year").toString());
        Integer billMonth = Integer.valueOf(condition.get("month").toString());
        String startDate = DateUtil.getFirstDayOfMonth(billYear, billMonth);
        String endDate = DateUtil.getFirstDayOfGivenMonth(startDate,1,"yyyy-MM-dd");
        condition.put("startDate", startDate);
        condition.put("endDate", endDate);
        String json = JSON.toJSONString(condition);
        Map<String,String> headMap=Maps.newHashMap();
        headMap.put("Authorization", "Basic aml1eWVzY206SnkxMjM0NTY=");
        try{
            String jsonString = HttpPostUtil.post(url,json,headMap);
            JSONObject jb0 = JSONObject.parseObject(jsonString);
            String code = jb0.getString("code");
            logger.info("code:{}",code);
            if(!"SUCCESS".equals(code)){
                logger.info("message:{}",jb0.getString("message"));
                throw new BizException(jb0.getString("message"));
            }
            JSONObject jb = jb0.getJSONObject("items");
            PageInfo<MaterailOutReportVo> pageInfo =
                    (PageInfo<MaterailOutReportVo>) JSON.parseObject(jb.toJSONString(), new TypeReference<PageInfo<MaterailOutReportVo>>(){});
            return pageInfo;
        }
        catch(Exception ex){
            logger.error("druid rest api 接口异常：",ex);
            throw new BizException("druid rest api 接口异常："+ex.getMessage());
        }
    }
	
	

}
