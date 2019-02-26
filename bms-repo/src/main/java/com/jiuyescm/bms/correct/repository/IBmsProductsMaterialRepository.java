package com.jiuyescm.bms.correct.repository;

import java.util.List;
import java.util.Map;

import com.jiuyescm.bms.biz.storage.entity.BizOutstockPackmaterialEntity;
import com.jiuyescm.bms.correct.BmsMarkingMaterialEntity;
import com.jiuyescm.bms.correct.BmsProductsMaterialAccountEntity;

public interface IBmsProductsMaterialRepository {
	//查询出所有占比最高的
	List<BmsProductsMaterialAccountEntity> queyAllMax(Map<String,Object> condition);
	
	//查询出所有占比最高的保温箱
	List<BmsProductsMaterialAccountEntity> queyAllBwxMax(Map<String,Object> condition);
	
	//统计占比
	List<BmsProductsMaterialAccountEntity> queyMaterialCount(Map<String,Object> condition);
	
	//获取最多使用的保温袋
	BizOutstockPackmaterialEntity queryBwdMaterial(Map<String,Object> condition);
	
	//保存统计数据
	int saveList(List<BmsProductsMaterialAccountEntity> list);
	
	//保存耗材统计数据
	int saveMaterial(Map<String,Object> condition);
	
	//保存保温袋统计数据
	int saveBwd(Map<String,Object> condition);
	
	//查询出所有占比最高的
	List<BmsMarkingMaterialEntity> queyNotMax(Map<String,Object> condition);
	
	//查询出所有占比不是最高的保温袋
	List<BizOutstockPackmaterialEntity> queyNotMaxBwd(Map<String,Object> condition);
		
	//根据商品明细查询出所有的运单
	List<BmsMarkingMaterialEntity> queryMark(Map<String,Object> condition);
	
	BmsMarkingMaterialEntity queryOneMark(Map<String,Object> condition);
	
	//批量更新打标记录
	int updateMarkList(List<BmsMarkingMaterialEntity> list);
	
	//根据条件查询打标记录
	BmsMarkingMaterialEntity queryMarkVo(Map<String,Object> condition);
	
	// 根据批次号查询运单中耗材明细，打标
	int markMaterial(Map<String,Object> condition);
	
	// 根据批次号查询运单中耗材明细，打标保温袋
	int markBwd(Map<String,Object> condition);

	int deleteMarkMaterialByWaybillNo(List<String> waybillNoList);
	
	//更新汇总表
	int updateMaterialAccount(Map<String,Object> condition);
	
	//根据耗材标得到对应得耗材编码
	Map<String,String> getMaterialMap(Map<String,Object> condition);
}
