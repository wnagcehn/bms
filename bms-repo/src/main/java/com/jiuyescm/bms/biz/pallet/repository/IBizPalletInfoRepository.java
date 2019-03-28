package com.jiuyescm.bms.biz.pallet.repository;

import java.util.Map;
import java.util.List;
import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.biz.pallet.entity.BizPalletInfoEntity;

/**
 * ..Repository
 * @author wangchen
 * 
 */
public interface IBizPalletInfoRepository {
	
	PageInfo<BizPalletInfoEntity> query(Map<String, Object> condition,
		int pageNo, int pageSize);

	List<BizPalletInfoEntity> query(Map<String, Object> condition);

    BizPalletInfoEntity save(BizPalletInfoEntity entity);

    int update(BizPalletInfoEntity entity);

	int delete(List<BizPalletInfoEntity> lists);	
	/**
	 * 重算
	 * @param list
	 * @return
	 */
	int reCalculate(List<BizPalletInfoEntity> list);
	
	/**
	 * 批量更新
	 * @param list
	 * @return
	 */
	int updateBatch(List<Map<String, Object>> list);
	
	/**
	 * 分组统计
	 * @param condition
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	PageInfo<BizPalletInfoEntity> groupCount(Map<String, Object> condition, int pageNo, int pageSize);
	
	/**
	 * 批量更新导入托数
	 * @param list
	 * @return
	 */
	int updatePalletNumBatch(List<BizPalletInfoEntity> list);
	
	/**
	 * 重算（新）
	 * @param list
	 * @return
	 */
	int retryCalculate(List<BizPalletInfoEntity> list);
	
	/**
	 * 批量更新费用表计算状态(99)和调整数量
	 * @param list
	 * @return
	 */
	int updateBatchFees(List<Map<String, Object>> list);



}
