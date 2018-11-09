
package com.jiuyescm.bms.biz.dispatch.service;

import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.biz.dispatch.entity.BizDispatchBillPayEntity;

public interface IBizDispatchBillPayService {
	
	/**
	 * 查询出所有的宅配运单
	 * @param condition
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
    PageInfo<BizDispatchBillPayEntity> queryAll(Map<String, Object> condition, int pageNo,
            int pageSize);
	
    /**
     * 单个更新业务数据
     * @param aCondition
     * @return
     */
    public int updateBillEntity(BizDispatchBillPayEntity aCondition);
    
    /**
     * 批量更新业务数据
     * @param aCondition
     * @return
     */
	public int updateBill(List<BizDispatchBillPayEntity> aCondition);
	
	/**
	 * 分组统计报价查询
	 */
	 PageInfo<BizDispatchBillPayEntity> queryAllPrice(Map<String, Object> condition, int pageNo,
	            int pageSize);  
	 
	/**
	 * 分组统计数据查询
	 */
	 PageInfo<BizDispatchBillPayEntity> queryAllData(Map<String, Object> condition, int pageNo,
	            int pageSize); 
	 
	 Properties validRetry(Map<String, Object> param);
	 
	 int reCalculate(Map<String, Object> param);
    
	 /**
	  * 批量插入数据
	  * @param list
	  */
	 public int saveList(List<BizDispatchBillPayEntity> list);
	 
	 /**
	  * 根据运单号批量查询
	  * @param aCondition
	  * @return
	  */
	 public List<BizDispatchBillPayEntity> queryBizData(List<String> aCondition);
	 
	 int queryDispatch(Map<String, Object> param);
	 
	 int updateBatchWeight(List<Map<String, Object>> list);
	 
	 /**
	  * 判断是否有计算异常的数据
	  * @param condition
	  * @return
	  */
	 public BizDispatchBillPayEntity queryExceptionOne(Map<String,Object> condition);

	int adjustBillPayEntity(BizDispatchBillPayEntity temp);
	
	/**
	 * 导出时查询的数据
	 * @param condition
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	PageInfo<BizDispatchBillPayEntity> queryAllToExport(Map<String, Object> condition, int pageNo, int pageSize);
}