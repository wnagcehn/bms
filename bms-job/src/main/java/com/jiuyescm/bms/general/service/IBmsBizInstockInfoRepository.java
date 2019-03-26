package com.jiuyescm.bms.general.service;

import java.util.Map;
import java.util.List;
import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.general.entity.BmsBizInstockInfoEntity;
import com.jiuyescm.bms.general.entity.FeesReceiveStorageEntity;

/**
 * ..Repository
 * @author wangchen
 * 
 */
public interface IBmsBizInstockInfoRepository {

	PageInfo<BmsBizInstockInfoEntity> query(Map<String, Object> condition,
		int pageNo, int pageSize);

	List<BmsBizInstockInfoEntity> query(Map<String, Object> condition);

    BmsBizInstockInfoEntity save(BmsBizInstockInfoEntity entity);

    BmsBizInstockInfoEntity update(BmsBizInstockInfoEntity entity);

	BmsBizInstockInfoEntity delete(BmsBizInstockInfoEntity entity);
	
	/**
	 * 批量更新
	 * @param list
	 * @return
	 */
	int updateBatch(List<Map<String, Object>> list);
	
	/**
	 * 重算
	 * @param param
	 * @return
	 */
	int reCalculate(List<BmsBizInstockInfoEntity> list);
	
	/**
	 * 分组统计
	 * @param condition
	 * @return
	 */
	PageInfo<BmsBizInstockInfoEntity> groupCount(Map<String, Object> condition, int pageNo, int pageSize);
	
	/**
	 * 为预账单查询
	 * @param condition
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	PageInfo<FeesReceiveStorageEntity> queryForBill(Map<String, Object> condition, int pageNo, int pageSize);
	
	/**
	 * 业务数据查询
	 * @param condition
	 * @return
	 */
	List<BmsBizInstockInfoEntity> getInStockInfoList(Map<String, Object> condition);
	
	/**
	 * 批量更新
	 * @param entities
	 */
	void updateInstockBatchByFees(List<FeesReceiveStorageEntity> entities);

	void updatebizInstockInfoById(List<FeesReceiveStorageEntity> entities);

}
