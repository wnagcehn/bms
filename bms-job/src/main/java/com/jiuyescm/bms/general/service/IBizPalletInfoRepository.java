package com.jiuyescm.bms.general.service;

import java.util.Map;
import java.util.List;
import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.general.entity.BizPalletInfoEntity;

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

	/**
	 * 业务数据查询
	 * @param condition
	 * @return
	 */
	List<BizPalletInfoEntity> querybizPallet(Map<String, Object> condition);
	
	/**
	 * 处置费批量更新业务数据
	 * @param list
	 */
	void updatebizPallet(List<BizPalletInfoEntity> list);



}
