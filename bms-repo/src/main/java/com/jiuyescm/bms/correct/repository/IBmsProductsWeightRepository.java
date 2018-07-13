package com.jiuyescm.bms.correct.repository;

import java.util.List;
import java.util.Map;

import com.jiuyescm.bms.correct.BmsMarkingProductsEntity;
import com.jiuyescm.bms.correct.BmsProductsWeightAccountEntity;

public interface IBmsProductsWeightRepository {
	//查询出所有占比最高的
	List<BmsProductsWeightAccountEntity> queyAllMax(Map<String,Object> condition);

	//统计占比
	List<BmsProductsWeightAccountEntity> queyWeightCount(Map<String,Object> condition);
	
	//保存统计数据
	int saveList(List<BmsProductsWeightAccountEntity> list);
	
	int saveWeight(Map<String,Object> condition);
	
	//根据商品明细查询出所有的运单
	List<BmsMarkingProductsEntity> queryMark(Map<String,Object> condition);
	
	//批量更新打标记录
	int updateMarkList(List<BmsMarkingProductsEntity> list);
	
	//根据条件查询打标记录
	BmsMarkingProductsEntity queryMarkVo(Map<String,Object> condition);
	
	//根据条件批量更新打标记录
	int updateMark(Map<String,Object> condition);
}
