package com.jiuyescm.bms.biz.storage.repository;

import java.util.List;

import com.jiuyescm.bms.biz.storage.entity.BizPackStorageTempEntity;

public interface IBizPackStorageTempRepository {

	/**
	 * 查询临时表与业务表均存在的数据
	 * @param condition
	 * @return
	 */
    List<BizPackStorageTempEntity> queryInBiz(String taskId);

    /**
     * 批量写入临时表
     * @param list
     */
    void saveBatch(List<BizPackStorageTempEntity> list);

    /**
     * 根据批次号删除数据
     * @param taskId
     * @return
     */
    public int deleteBybatchNum(String taskId);
}
