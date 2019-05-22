package com.jiuyescm.bms.biz.storage.service;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.biz.storage.entity.BizOutstockPackmaterialCancelEntity;
import com.jiuyescm.bms.biz.storage.entity.BizOutstockPackmaterialEntity;

/**
 * ..Service
 * @author wangchen
 * 
 */
public interface IBizOutstockPackmaterialCancelService {

    BizOutstockPackmaterialCancelEntity findById(Long id);
	
    PageInfo<BizOutstockPackmaterialCancelEntity> query(Map<String, Object> condition, int pageNo,
            int pageSize);

	List<BizOutstockPackmaterialCancelEntity> query(Map<String, Object> condition);

    BizOutstockPackmaterialCancelEntity save(BizOutstockPackmaterialCancelEntity entity);

    BizOutstockPackmaterialCancelEntity update(BizOutstockPackmaterialCancelEntity entity);

    void delete(Long id);

    /**
     * 有就更新，没有就新增
     * <功能描述>
     * 
     * @author wangchen870
     * @date 2019年5月17日 上午10:54:07
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
     * @date 2019年5月17日 下午4:55:26
     *
     * @return
     */
    List<BizOutstockPackmaterialCancelEntity> queryNeedCancel();

    /**
     * 批量更新状态
     * <功能描述>
     * 
     * @author wangchen870
     * @date 2019年5月17日 下午5:38:48
     *
     * @param list
     * @return
     */
    int updateBatchStatus(List<BizOutstockPackmaterialCancelEntity> list);

    /**
     * 作废耗材&批量更新状态
     * <功能描述>
     * 
     * @author wangchen870
     * @date 2019年5月18日 下午4:56:11
     *
     * @param cancelList
     * @param materialList
     */
    void updateBatchStatusAndDelMaterial(List<BizOutstockPackmaterialCancelEntity> cancelList,
            List<BizOutstockPackmaterialEntity> materialList);

}
