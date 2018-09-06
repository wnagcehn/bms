package com.jiuyescm.bms.biz.storage.repository;

import java.util.Map;
import java.util.List;
import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.biz.storage.entity.BmsBizInstockInfoEntity;
import com.jiuyescm.bms.biz.storage.entity.BmsBizInstockRecordEntity;

/**
 * ..Repository
 * @author wangchen
 * 
 */
public interface IBmsBizInstockRecordRepository {

	PageInfo<BmsBizInstockRecordEntity> query(Map<String, Object> condition,
		int pageNo, int pageSize);

	List<BmsBizInstockRecordEntity> query(Map<String, Object> condition);

    BmsBizInstockInfoEntity save(BmsBizInstockInfoEntity entity);

    BmsBizInstockRecordEntity update(BmsBizInstockRecordEntity entity);

    void delete(Long id);
    
    /**
     * 批量保存
     * @param list
     * @return
     */
	int saveBatch(List<BmsBizInstockInfoEntity> list);

}
