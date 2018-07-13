package com.jiuyescm.bms.biz.dispatch.service;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.biz.dispatch.entity.BizTihuoBillEntity;

public interface IBizDispatchTihuoBillService {
    PageInfo<BizTihuoBillEntity> query(Map<String, Object> condition, int pageNo,
            int pageSize);

    BizTihuoBillEntity findById(Long id);

    BizTihuoBillEntity save(BizTihuoBillEntity entity);

    BizTihuoBillEntity update(BizTihuoBillEntity entity);

    void delete(Long id);
    
    /**
     * 根据条件统计每日的单量
     * @param condition
     * @return
     */
    List<BizTihuoBillEntity> countByDate(Map<String,Object> condition);
    
	  /**
	   * 批量插入业务数据
	   * @param list
	   * @return
	   */
	 int insertBillList(List<BizTihuoBillEntity> list);
	 
	 /**
	  * 根据Map条件查询提货单量数据
	  * @param condition
	  * @return
	  */
	 List<BizTihuoBillEntity> queryData(Map<String, Object> condition);
	 
	 /**
	  * 根据map条件批量更新数据
	  */
	 int deleteBizByMap(Map<String, Object> condition);
}
