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

    /**
     * 查出所有理论导入商家
     * <功能描述>
     * 
     * @author wangchen870
     * @date 2019年7月15日 下午6:41:13
     *
     * @param param
     * @return
     */
    List<ReportWarehouseBizImportEntity> queryCusByTheory(Map<String, Object> param);

    /**
     * 通过商家查询是否全部使用新方案
     * <功能描述>
     * 
     * @author wangchen870
     * @date 2019年7月15日 下午7:05:20
     *
     * @param param
     * @return
     */
    List<String> queryIsNewPlanByCustomer(Map<String, Object> param);

    /**
     * 新方案写入
     * <功能描述>
     * 
     * @author wangchen870
     * @date 2019年7月15日 下午7:30:18
     *
     * @param list
     * @return
     */
    int upsertPackMaterialByNewPlan(ReportWarehouseBizImportEntity entity);

    /**
     * 查看商家是否是免导入商家
     * <功能描述>
     * 
     * @author wangchen870
     * @date 2019年7月15日 下午8:35:11
     *
     * @param entity
     * @return
     */
    ReportWarehouseCustomerEntity queryCusImportType(ReportWarehouseBizImportEntity entity);

    /**
     * 作废免导入的
     * <功能描述>
     * 
     * @author wangchen870
     * @date 2019年7月15日 下午8:43:45
     *
     * @param entity
     * @return
     */
    int deleteMaterialReport(ReportWarehouseBizImportEntity entity);

    /**
     * 查询是否导入了耗材
     * <功能描述>
     * 
     * @author wangchen870
     * @date 2019年7月16日 上午9:52:38
     *
     * @param entity
     * @return
     */
    List<String> queryIsImportMaterial(ReportWarehouseBizImportEntity entity);

}
