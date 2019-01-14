/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.general.service;

import java.util.List;
import java.util.Map;

import com.jiuyescm.bms.base.reportWarehouse.ReportWarehouseCustomerEntity;
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
    
    List<ReportWarehouseCustomerEntity> queryWareList(Map<String, Object> param);
    /**
     * 删除已经设置了的商家仓库
     * @param param
     * @return
     */
    int updateReport(List<ReportWarehouseCustomerEntity> list);
    
    /**
     * 插入应导入的商家仓库
     * @param param
     * @return
     */
    int insertReport(Map<String, Object> param);
    
    /**
     * 删除各仓导入的数据
     * @param param	
     * @return
     */
    int deletetReport(Map<String, Object> param);
    
    List<ReportWarehouseBizImportEntity> queryImport(Map<String, Object> param);

}
