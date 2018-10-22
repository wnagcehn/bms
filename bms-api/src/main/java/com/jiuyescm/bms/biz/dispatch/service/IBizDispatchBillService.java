
package com.jiuyescm.bms.biz.dispatch.service;

import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.biz.dispatch.entity.BizDispatchBillEntity;
import com.jiuyescm.bms.biz.dispatch.vo.BizDispatchBillVo;

public interface IBizDispatchBillService {
	
	/**
	 * 查询出所有的宅配运单
	 * @param condition
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
    PageInfo<BizDispatchBillEntity> queryAll(Map<String, Object> condition, int pageNo,
            int pageSize);
    
	/**
	 * 查询出所有的导出宅配运单
	 * @param condition
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
    PageInfo<BizDispatchBillVo> queryData(Map<String, Object> condition, int pageNo,
            int pageSize);
    /**
     * 单个更新业务数据
     * @param aCondition
     * @return
     */
    public int updateBillEntity(BizDispatchBillEntity aCondition);
    
    /**
     * 批量更新业务数据
     * @param aCondition
     * @return
     */
	public int updateBill(List<BizDispatchBillEntity> aCondition);
	
	/**
	 * 分组统计报价查询
	 */
	 PageInfo<BizDispatchBillEntity> queryAllPrice(Map<String, Object> condition, int pageNo,
	            int pageSize);  
	 
	/**
	 * 分组统计数据查询
	 */
	 PageInfo<BizDispatchBillEntity> queryAllData(Map<String, Object> condition, int pageNo,
	            int pageSize); 
	 
	/**
	 * 分组统计数据计算状态查询
	 */
	 PageInfo<BizDispatchBillEntity> queryAllCalculate(Map<String, Object> condition, int pageNo,
	            int pageSize); 
	 
	 Properties validRetry(Map<String, Object> param);
	 
	 int reCalculate(Map<String, Object> param);
	 
	 /**
	  * 批量插入数据
	  * @param list
	  */
	 public int saveList(List<BizDispatchBillEntity> list);
	 
	 /**
	  * 根据运单号批量查询
	  * @param aCondition
	  * @return
	  */
	 public List<BizDispatchBillEntity> queryBizData(List<String> aCondition);
	 
	 /**
	  * 判断是否有计算异常的数据
	  * @param condition
	  * @return
	  */
	 public BizDispatchBillEntity queryExceptionOne(Map<String,Object> condition);
	 
	 int queryDispatch(Map<String, Object> param);
	 
	 public int updateBatchWeight(List<Map<String, Object>> list);

	int adjustBillEntity(BizDispatchBillEntity temp);

	 public int retryByWaybillNo(List<String> aCondition);
	 
	 public int retryByMaterialMark(Map<String,Object> condition);
	 
	 public List<String> queryWayBillNo(Map<String,Object> condition);
	 
	 public List<BizDispatchBillEntity> queryNotCalculate(Map<String,Object> condition);
	 
	 /**
	  * 根据运单号查询
	  * @param condition
	  * @return
	  */
	BizDispatchBillEntity queryByWayNo(Map<String, String> condition);
	 
}