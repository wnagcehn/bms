package com.jiuyescm.bms.biz.storage.service;

import java.util.Map;
import java.util.List;
import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.biz.storage.entity.BmsBizInstockInfoEntity;
import com.jiuyescm.bms.biz.storage.entity.BmsBizInstockRecordEntity;

/**
 * ..Service
 * @author wangchen
 * 
 */
public interface IBmsBizInstockRecordService {

    PageInfo<BmsBizInstockRecordEntity> query(Map<String, Object> condition, int pageNo,
            int pageSize);

	List<BmsBizInstockRecordEntity> query(Map<String, Object> condition);

	BmsBizInstockInfoEntity save(BmsBizInstockInfoEntity entity);

    BmsBizInstockRecordEntity update(BmsBizInstockRecordEntity entity);

    void delete(Long id);

	int saveBatch(List<BmsBizInstockInfoEntity> list);

}
