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

	/**
	 * 根据pullType更新lastTime
	 * <功能描述>
	 * 
	 * @author wangchen870
	 * @date 2019年4月19日 下午2:12:37
	 *
	 * @param condition
	 * @return
	 */
    int updateByPullType(Map<String, Object> condition);

}
