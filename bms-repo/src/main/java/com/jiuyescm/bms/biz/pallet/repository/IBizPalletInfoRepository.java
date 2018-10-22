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

    BizPalletInfoEntity update(BizPalletInfoEntity entity);

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



}
