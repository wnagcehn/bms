package com.jiuyescm.bms.biz.discount.repository;

import java.util.Map;
import java.util.List;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.biz.discount.entity.BmsDiscountAsynTaskEntity;

/**
 * ..Repository
 * @author wangchen
 * 
 */
public interface IBmsDiscountAsynTaskRepository {

    BmsDiscountAsynTaskEntity findById(Long id);
	
	PageInfo<BmsDiscountAsynTaskEntity> query(Map<String, Object> condition,
		int pageNo, int pageSize);

	List<BmsDiscountAsynTaskEntity> query(Map<String, Object> condition);

    BmsDiscountAsynTaskEntity save(BmsDiscountAsynTaskEntity entity);

    BmsDiscountAsynTaskEntity update(BmsDiscountAsynTaskEntity entity);

    void delete(Long id);
    
	BmsDiscountAsynTaskEntity queryTask(Map<String,Object> condition);
	
	/**
	 * 是否存在任务
	 * @param entity
	 * @return
	 */
	boolean existTask(BmsDiscountAsynTaskEntity entity);
	
	/**
	 * 批量保存
	 * @param list
	 * @return
	 */
	int saveBatch(List<BmsDiscountAsynTaskEntity> list);


}
