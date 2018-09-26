package com.jiuyescm.bms.biz.pallet.repository;

import java.util.Map;
import java.util.List;
import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.biz.pallet.entity.BizPalletInfoTempEntity;

/**
 * ..Repository
 * @author wangchen
 * 
 */
public interface IBizPalletInfoTempRepository {

    BizPalletInfoTempEntity findById(Long id);
	
	PageInfo<BizPalletInfoTempEntity> query(Map<String, Object> condition,
		int pageNo, int pageSize);

	List<BizPalletInfoTempEntity> query(Map<String, Object> condition);

    BizPalletInfoTempEntity save(BizPalletInfoTempEntity entity);

    BizPalletInfoTempEntity update(BizPalletInfoTempEntity entity);

    void delete(Long id);
    
    /**
     * 批量保存
     * @param list
     */
	void saveBatch(List<BizPalletInfoTempEntity> list);
	
	/**
	 * 校验唯一性
	 * @param taskId
	 * @return
	 */
	List<BizPalletInfoTempEntity> queryInBiz(String taskId);
	
	/**
	 * 从临时表保存到业务表
	 * @param taskId
	 * @return
	 */
	int saveTempData(String taskId);
	
	/**
	 * 批量删除
	 * @param taskId
	 * @return
	 */
	int deleteBybatchNum(String taskId);

}
