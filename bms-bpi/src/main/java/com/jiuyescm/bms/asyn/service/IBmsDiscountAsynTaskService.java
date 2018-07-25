package com.jiuyescm.bms.asyn.service;

import java.util.Map;
import java.util.List;
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

    BmsDiscountAsynTaskEntity save(BmsDiscountAsynTaskEntity entity);

    BmsDiscountAsynTaskEntity update(BmsDiscountAsynTaskEntity entity);

    void delete(Long id);

}
