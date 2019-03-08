package com.jiuyescm.bms.correct.repository;

import java.util.Map;
import java.util.List;

import com.jiuyescm.bms.correct.ElConditionEntity;

/**
 * ..Repository
 * @author liuzhicheng
 * 
 */
public interface ElConditionRepository {

	List<ElConditionEntity> query(Map<String, Object> condition);

	int save(Map<String, Object> condition);

	ElConditionEntity update(ElConditionEntity entity);

}
