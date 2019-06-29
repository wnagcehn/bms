package com.jiuyescm.bms.biz.pallet.repository;

import java.util.Map;
import java.util.List;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.asyn.entity.BmsAsynCalcuTaskEntity;
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
    int retryCalculate(Map<String, Object> param);
	
	/**
	 * 批量更新费用表计算状态(99)和调整数量
	 * @param list
	 * @return
	 */
	int updateBatchFees(List<Map<String, Object>> list);
	
	/**
	 * 查询托数需要发的任务
	 * @param condition
	 * @return
	 */
	List<BmsAsynCalcuTaskEntity> queryPalletTask(Map<String, Object> condition);

	/**
	 * 重算(为了重算商家下所有的)
	 * <功能描述>
	 * 
	 * @author wangchen870
	 * @date 2019年5月29日 下午5:36:43
	 *
	 * @param param
	 * @return
	 */
    int reCalculate(Map<String, Object> param);

    int cancalCustomerBiz(Map<String,Object> map);
    
    int restoreCustomerBiz(Map<String,Object> map);

}
