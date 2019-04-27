
package com.jiuyescm.bms.biz.dispatch.repository;

import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.asyn.entity.BmsAsynCalcuTaskEntity;
import com.jiuyescm.bms.biz.dispatch.entity.BizDispatchBillEntity;
import com.jiuyescm.bms.biz.dispatch.vo.BizDispatchBillVo;

public interface IBizDispatchBillRepository{
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
     * 更新业务数据
     * @param aCondition
     * @return
     */
	public int updateBill(List<BizDispatchBillEntity> aCondition);
	
	/**
	 * 分组统计报价查询
	 */
	public PageInfo<BizDispatchBillEntity> queryAllPrice(Map<String, Object> condition, int pageNo,
            int pageSize);
	
	/**
	 * 分组统计数据查询
	 */
	public PageInfo<BizDispatchBillEntity> queryAllData(Map<String, Object> condition, int pageNo,
            int pageSize);
	
	//分组统计数据计算状态
	public PageInfo<BizDispatchBillEntity> queryAllCalculate(Map<String, Object> condition, int pageNo,
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
	 
	 public int queryDispatch(Map<String,Object> param);
	 
	 int updateBatchWeight(List<Map<String, Object>> list);


	int adjustBillEntity(BizDispatchBillEntity temp);

	public int retryByWaybillNo(List<String> aCondition);
	
	public int retryByMaterialMark(Map<String,Object> condition);
	
	public List<String> queryWayBillNo(Map<String,Object> condition);
	 
	public List<BizDispatchBillEntity> queryNotCalculate(Map<String,Object> condition);
	
	/**
	 * 根据运单号查询
	 * @param aCondition
	 * @return
	 */
	BizDispatchBillEntity queryByWayNo(Map<String, String> aCondition);
	
	/**
	 * 查询发生业务的商家
	 * @param condition
	 * @return
	 */
	List<BizDispatchBillEntity> queryBizCustomerid(Map<String, Object> condition);
	
	/**
	 * 通过费用编号改费用表计算状态为99
	 * @param condition
	 * @return
	 */
	int updateIsCalcuByFeesNo(Map<String, Object> condition);
	
	/**
	 * 通过批量导入的运单号去更新费用表计算状态
	 * @param list
	 * @return
	 */
	int updateIsCalcuByWaybillNo(List<Map<String, Object>> list);
	
	public List<BmsAsynCalcuTaskEntity> queryTask(Map<String, Object> condition);

	/**
	 * 汇总耗材需要分仓的仓库
	 * <功能描述>
	 * 
	 * @author wangchen870
	 * @date 2019年4月17日 下午7:08:55
	 *
	 * @param condition
	 * @return
	 */
    List<BizDispatchBillEntity> queryAllWarehouseFromBizData(Map<String, Object> condition);

    List<BizDispatchBillEntity> queryBizByCusid(Map<String, Object> condition);

}