package com.jiuyescm.bms.correct.service;

import java.util.List;
import java.util.Map;

import com.jiuyescm.bms.correct.vo.BmsMarkingProductsVo;
import com.jiuyescm.bms.correct.vo.BmsProductsWeightAccountVo;

public interface IBmsProductsWeightService {
	
	//查询出所有占比最高的
	List<BmsProductsWeightAccountVo> queyAllMax(Map<String,Object> condition);
	
	//统计占比
	List<BmsProductsWeightAccountVo> queyWeightCount(Map<String,Object> condition);
	
	//保存统计数据
	int saveList(List<BmsProductsWeightAccountVo> list);
	
	int saveWeight(Map<String,Object> condition);
	
	//根据商品明细查询出所有的运单
	List<BmsMarkingProductsVo> queryMark(Map<String,Object> condition);
	
	//批量更新打标记录
	int updateMarkList(List<BmsMarkingProductsVo> list);
	
	//根据条件查询打标记录
	BmsMarkingProductsVo queryMarkVo(Map<String,Object> condition);
	
	//根据条件批量更新打标记录
	int updateMark(Map<String,Object> condition);
	
	//查询条件查询出单个耗材明细
	BmsMarkingProductsVo queryOneMaterial(Map<String,Object> condition);
}
