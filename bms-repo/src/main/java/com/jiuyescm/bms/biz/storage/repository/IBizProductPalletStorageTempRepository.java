package com.jiuyescm.bms.biz.storage.repository;

import java.util.List;

import com.jiuyescm.bms.biz.storage.entity.BizProductPalletStorageTempEntity;

public interface IBizProductPalletStorageTempRepository {

	/**
	 * 查询临时表与业务表均存在的数据
	 * @param condition
	 * @return
	 */
    List<BizProductPalletStorageTempEntity> queryInBiz(String taskId);

    /**
     * 批量写入临时表
     * @param list
     */
    void saveBatch(List<BizProductPalletStorageTempEntity> list);
    
    /**
     * 根据批次号删除数据
     * @param taskId
     * @return
     */
    public int deleteBybatchNum(String taskId);

}
