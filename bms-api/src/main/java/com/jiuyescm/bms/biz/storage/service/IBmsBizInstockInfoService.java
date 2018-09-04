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

    BmsBizInstockInfoEntity findById(Long id);
	
    PageInfo<BmsBizInstockInfoEntity> query(Map<String, Object> condition, int pageNo,
            int pageSize);

	List<BmsBizInstockInfoEntity> query(Map<String, Object> condition);

    BmsBizInstockInfoEntity save(BmsBizInstockInfoEntity entity);

    BmsBizInstockInfoEntity update(BmsBizInstockInfoEntity entity);

	BmsBizInstockInfoEntity delete(BmsBizInstockInfoEntity entity);

}
