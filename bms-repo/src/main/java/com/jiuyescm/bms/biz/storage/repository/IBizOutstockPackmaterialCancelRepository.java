package com.jiuyescm.bms.biz.storage.repository;

import java.util.Map;
import java.util.List;
import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.biz.storage.entity.BizOutstockPackmaterialCancelEntity;

/**
 * ..Repository
 * @author wangchen
 * 
 */
public interface IBizOutstockPackmaterialCancelRepository {

    BizOutstockPackmaterialCancelEntity findById(Long id);
	
	PageInfo<BizOutstockPackmaterialCancelEntity> query(Map<String, Object> condition,
		int pageNo, int pageSize);

	List<BizOutstockPackmaterialCancelEntity> query(Map<String, Object> condition);

    BizOutstockPackmaterialCancelEntity save(BizOutstockPackmaterialCancelEntity entity);

    BizOutstockPackmaterialCancelEntity update(BizOutstockPackmaterialCancelEntity entity);

    void delete(Long id);

    /**
     * 有就更新 没有就新增
     * <功能描述>
     * 
     * @author wangchen870
     * @date 2019年5月17日 上午10:51:42
     *
     * @param cancelList
     * @return
     */
    int saveOrUpdate(List<BizOutstockPackmaterialCancelEntity> cancelList);

    /**
     * 查询需要作废的运单
     * <功能描述>
     * 
     * @author wangchen870
     * @date 2019年5月17日 下午4:54:18
     *
     * @return
     */
    List<BizOutstockPackmaterialCancelEntity> queryNeedCancel(Map<String, Object> map);

    /**
     * 批量更新状态
     * <功能描述>
     * 
     * @author wangchen870
     * @date 2019年5月17日 下午5:36:54
     *
     * @param list
     * @return
     */
    int updateBatchStatus(List<BizOutstockPackmaterialCancelEntity> list);

}
