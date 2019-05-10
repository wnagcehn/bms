package com.jiuyescm.bms.asyn.service;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.biz.discount.entity.BmsDiscountAsynTaskEntity;

/**
 * ..Service
 * @author wangchen
 * 
 */
public interface IBmsDiscountAsynTaskService {

    BmsDiscountAsynTaskEntity findById(Long id);
	
    PageInfo<BmsDiscountAsynTaskEntity> query(Map<String, Object> condition, int pageNo,
            int pageSize);

	List<BmsDiscountAsynTaskEntity> query(Map<String, Object> condition);
	
	BmsDiscountAsynTaskEntity queryTask(Map<String,Object> condition);

    BmsDiscountAsynTaskEntity save(BmsDiscountAsynTaskEntity entity);

    BmsDiscountAsynTaskEntity update(BmsDiscountAsynTaskEntity entity);

    void delete(Long id);
    
    /**
     * 是否存在任务
     * @param voEntity
     * @return
     * @throws Exception
     */
	boolean existTask(BmsDiscountAsynTaskEntity voEntity) throws Exception;
	
	/**
	 * 批量保存
	 * @param voList
	 * @return
	 * @throws Exception
	 */
	int saveBatch(List<BmsDiscountAsynTaskEntity> voList) throws Exception;
	
	String sendTask(Map<String,Object> map);

}
