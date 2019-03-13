
package com.jiuyescm.bms.receivable.dispatch.service;

import java.util.List;
import java.util.Map;

import com.jiuyescm.bms.biz.dispatch.entity.BizDispatchBillEntity;
import com.jiuyescm.bms.general.entity.BizDispatchCarrierChangeEntity;
import com.jiuyescm.bms.general.entity.PubMonthFeeCountEntity;

public interface IBizDispatchBillService {
	
	/**
	 * 查询出所有的宅配运单
	 * @param condition
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
    List<BizDispatchBillEntity> query(Map<String, Object> condition);
		
    /**
     * 单个更新业务数据
     * @param aCondition
     * @return
     */
    public int update(BizDispatchBillEntity entity);
    
    /**
     * 根据条件更新运单
     * @param condition
     * @return
     */
    public int updateByParam(Map<String, Object> condition);
    
    /**
     * 批量更新业务数据
     * @param aCondition
     * @return
     */
	public int updateBatch(List<BizDispatchBillEntity> list);
	
	/**
	 * 新更新
	 * @param list
	 * @return
	 */
	public int newUpdateBatch(List<BizDispatchBillEntity> list);

	public Map<String, Object> InitReqVo(BizDispatchBillEntity entity,Map<String, String> param);
	
	/**
	 * 查询所有指定修改物流商的商家
	 * @return
	 */
	public List<String> queryChangeCusList();
	
	public BizDispatchCarrierChangeEntity queryCarrierChangeEntity(Map<String, Object> param);
	
	/**
	 * 更新物流商修改表
	 * @param entity
	 * @return
	 */
	public int updateCarrierChange(BizDispatchCarrierChangeEntity entity);

	/**
	 * 根据运单号移除配送业务数据
	 * @param waybillNo
	 * @return
	 */
	int deleteByWayBillNo(String waybillNo);

	/**
	 * 向配送业务数据表写入配送数据
	 * @param entity
	 * @return
	 */
	int insertDispatchBillEntity(BizDispatchBillEntity entity);
	
	/**
	 * 根据运单号获取配送业务数据
	 * @param waybillNo
	 * @return
	 */
	BizDispatchBillEntity getDispatchEntityByWaybillNo(String waybillNo);
	
	/**
	 * 查询所有的月结账号
	 * @param condition
	 * @return
	 */
	public List<PubMonthFeeCountEntity> queryMonthCount(Map<String, Object> condition);
}