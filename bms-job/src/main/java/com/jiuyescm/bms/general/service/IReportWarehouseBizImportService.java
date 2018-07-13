/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.general.service;

import java.util.Map;

import com.jiuyescm.bms.general.entity.ReportWarehouseBizImportEntity;

/**
 * 
 * @author stevenl
 * 
 */
public interface IReportWarehouseBizImportService {

    int save(ReportWarehouseBizImportEntity entity);

    int update(ReportWarehouseBizImportEntity entity);
    
    /**
     * 商品托数
     * 不存在新增，存在更新时间
     * @param map
     * @return
     */
    int upsertPalletStorage(Map<String, Object> param);
    
    /**
     * 耗材
     * 不存在新增，存在更新时间
     * @param map
     * @return
     */
    int upsertPackMaterial(Map<String, Object> param);

}
