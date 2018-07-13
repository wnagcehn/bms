package com.jiuyescm.bms.correct.service;

import java.util.List;
import java.util.Map;

import com.jiuyescm.bms.correct.vo.BmsMarkingMaterialVo;
import com.jiuyescm.bms.correct.vo.BmsProductsMaterialAccountVo;

public interface IBmsProductsMaterialService {
	
	//查询出所有占比最高的
	List<BmsProductsMaterialAccountVo> queyAllMax(Map<String,Object> condition);
	
	//统计占比
	List<BmsProductsMaterialAccountVo> queyMaterialCount(Map<String,Object> condition);
	
	//保存统计数据
	int saveList(List<BmsProductsMaterialAccountVo> list);
	
	int saveMaterial(Map<String,Object> condition);
	
	//查询出所有占比不是最高的
	List<BmsMarkingMaterialVo> queyNotMax(Map<String,Object> condition);
	
	//根据商品明细查询出所有的运单
	List<BmsMarkingMaterialVo> queryMark(Map<String,Object> condition);
	
	BmsMarkingMaterialVo queryOneMark(Map<String,Object> condition);
	
	//批量更新打标记录
	int updateMarkList(List<BmsMarkingMaterialVo> list);
	
	//根据条件查询打标记录
	BmsMarkingMaterialVo queryMarkVo(Map<String,Object> condition);
	
	// 根据批次号查询运单中耗材明细，打标
	int markMaterial(Map<String,Object> condition);

	int deleteMarkMaterialByWaybillNo(List<String> waybillNoList);
	
	//更新汇总表
	int updateMaterialAccount(Map<String,Object> condition);
}
