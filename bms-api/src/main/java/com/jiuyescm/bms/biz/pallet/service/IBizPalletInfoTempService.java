package com.jiuyescm.bms.biz.pallet.service;

import java.util.Map;
import java.util.List;
import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.biz.pallet.entity.BizPalletInfoTempEntity;

/**
 * ..Service
 * @author wangchen
 * 
 */
public interface IBizPalletInfoTempService {

    BizPalletInfoTempEntity findById(Long id);
	
    PageInfo<BizPalletInfoTempEntity> query(Map<String, Object> condition, int pageNo,
            int pageSize);

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
	List<BizPalletInfoTempEntity> queryInBiz(String taskId, int errorNum);
	
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
	
	/**
	 * 查询需要新增的entity
	 * @param taskId
	 * @return
	 */
	List<BizPalletInfoTempEntity> queryNeedInsert(String taskId);
	
	/**
	 * 批量更新和保存到正式表
	 * @param insertList
	 * @param updateList
	 * @return
	 */
	int saveData(List<BizPalletInfoTempEntity> insertList, List<BizPalletInfoTempEntity> updateList);
	
	/**
	 * 校验唯一性，无limit
	 * @param taskId
	 * @return
	 */
	List<BizPalletInfoTempEntity> queryInBizNotLimit(String taskId);
	
}
