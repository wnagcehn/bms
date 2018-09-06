package com.jiuyescm.bms.biz.storage.service;

import java.util.Map;
import java.util.List;
import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.biz.storage.entity.BmsBizInstockInfoEntity;

/**
 * ..Service
 * @author wangchen
 * 
 */
public interface IBmsBizInstockInfoService {

    PageInfo<BmsBizInstockInfoEntity> query(Map<String, Object> condition, int pageNo,
            int pageSize);

	List<BmsBizInstockInfoEntity> query(Map<String, Object> condition);

    BmsBizInstockInfoEntity save(BmsBizInstockInfoEntity entity);

    void update(BmsBizInstockInfoEntity entity);

	BmsBizInstockInfoEntity delete(BmsBizInstockInfoEntity entity);
	
	/**
	 * 批量更新
	 * @param list
	 * @return
	 */
	int updateBatch(List<Map<String, Object>> list);

}
