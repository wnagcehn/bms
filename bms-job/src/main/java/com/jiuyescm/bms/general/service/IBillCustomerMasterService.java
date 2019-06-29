package com.jiuyescm.bms.general.service;


import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.base.file.entity.BillPrepareExportTaskEntity;
import com.jiuyescm.bms.bill.customer.BillCustomerDetailEntity;
import com.jiuyescm.bms.billcheck.BillCheckInfoEntity;
import com.jiuyescm.bms.general.entity.BillCustomerMasterEntity;

/**
 * ..Repository
 * @author wangchen
 * 
 */
public interface IBillCustomerMasterService {
	
	PageInfo<BillCustomerMasterEntity> query(Map<String, Object> condition,
		int pageNo, int pageSize);

	List<BillCustomerMasterEntity> query(Map<String, Object> condition);

    BillCustomerMasterEntity save(BillCustomerMasterEntity entity);

    void delete(Long id);

    /**
     * 查询计算日志表和账单日志表，进行汇总
     * <功能描述>
     * 
     * @author wangchen870
     * @date 2019年6月19日 上午11:30:07
     *
     * @param condition
     * @return
     */
    List<BillCustomerMasterEntity> queryCalcuTaskLogAndCheckLog(Map<String, Object> condition);

    /**
     * 有就更新，没有就新增
     * <功能描述>
     * 
     * @author wangchen870
     * @date 2019年6月19日 下午1:17:49
     *
     * @param masterList
     * @return
     */
    void saveOrUpdate(List<BillCustomerMasterEntity> masterList);

    /**
     * 批量更新状态和时间
     * <功能描述>
     * 
     * @author wangchen870
     * @date 2019年6月19日 下午2:18:15
     *
     * @param list
     */
    void updateBatch(List<BillCustomerMasterEntity> list);

    /**
     * 查询bms计算的数据
     * <功能描述>
     * 
     * @author wangchen870
     * @date 2019年6月19日 下午3:00:31
     *
     * @param condition
     * @return
     */
    BillCustomerDetailEntity queryBmsCalcuData(Map<String, Object> condition);

    /**
     * 查询预账单的数据
     * <功能描述>
     * 
     * @author wangchen870
     * @date 2019年6月19日 下午3:45:11
     *
     * @param condition
     * @return
     */
    List<BillPrepareExportTaskEntity> queryPrepareData(Map<String, Object> condition);

    /**
     * 查询账单导入的数据
     * <功能描述>
     * 
     * @author wangchen870
     * @date 2019年6月19日 下午5:24:52
     *
     * @param condition
     * @return
     */
    List<BillCheckInfoEntity> queryBillImportData(Map<String, Object> condition);
    
    /**
     * 查询预账单总金额
     * <功能描述>
     * 
     * @author wangchen870
     * @date 2019年6月19日 下午6:14:10
     *
     * @param condition
     * @return
     */
    BillCustomerDetailEntity queryPrepareAmount(Map<String, Object> condition);

    /**
     * 查询预账单按子商家生成的数据
     * <功能描述>
     * 
     * @author wangchen870
     * @date 2019年6月20日 下午1:54:50
     *
     * @param condition
     * @return
     */
    List<BillPrepareExportTaskEntity> queryPrepareIsChildData(Map<String, Object> condition);

}
