package com.jiuyescm.bms.receivable.storage.service;

import java.util.List;
import java.util.Map;

import com.jiuyescm.bms.biz.storage.entity.BizOutstockDetailEntity;
import com.jiuyescm.bms.biz.storage.entity.BizOutstockMasterEntity;
/**
 * 
 * @author cjw
 * 
 */
public interface IBizOutstockMasterService {

	/**
	 * 查询待计算的出库单数据  默认1000条
	 * @param condition
	 * @return
	 */
	public List<BizOutstockMasterEntity> query(Map<String, Object> condition);

	/**
	 * 更新出库单计算状态，时间等
	 * @param entity 出库单对象
	 */
    public void update(BizOutstockMasterEntity entity);
    
    /**
     * 根据出库单号查询字表数据
     * @param outstockNo 出库单号
     * @return
     */
    public List<BizOutstockDetailEntity> getInstockDetailByMaster(String outstockNo);
    
    public void updateOutstockBatch(List<BizOutstockMasterEntity> OutStocks);
    
    List<Map<String,String>>  getTypeList(String type);

}
