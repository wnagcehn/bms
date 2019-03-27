package com.jiuyescm.bms.receivable.storage.service;

import java.util.List;
import java.util.Map;

import com.jiuyescm.bms.report.month.entity.BmsMaterialReportEntity;

public interface IMaterialReportService {
	
	//将BMS耗材明细写入BMS耗材临时表
	public int insertBmsMaterailTemp(Map<String, Object> param);
	
	//删除BMS耗材临时表的数据
	public int deleteBmsMaterailTemp();
	
	//将WMS耗材明细写入WMS耗材临时表
	public int insertWmsMaterailTemp(Map<String, Object> param);
	
	//删除WMS耗材临时表的数据
	public int deleteWmsMaterailTemp();
	
	//删除耗材差异
	public int deleteMaterialReport(Map<String, Object> param);
	
	//查询耗材差异
	public List<BmsMaterialReportEntity> queryMaterialReportList(Map<String, Object> param);
	
	//插入耗材差异表
	public int insertMaterialReport(List<BmsMaterialReportEntity> list);
	
	//对bms耗材统计临时表进行汇总
	public List<BmsMaterialReportEntity> queryBmsMaterialReportTemp(Map<String, Object> param);
	
	//对wms耗材统计临时表进行汇总
	public List<BmsMaterialReportEntity> queryWmsMaterialReportTemp(Map<String, Object> param);
	
}
