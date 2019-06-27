package com.jiuyescm.bms.bill.customer.repository;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.bill.customer.BillCustomerDetailEntity;

/**
 * ..Repository
 * @author wangchen
 * 
 */
public interface IBillCustomerDetailRepository {

    BillCustomerDetailEntity findById(Long id);
	
	PageInfo<BillCustomerDetailEntity> query(Map<String, Object> condition,
		int pageNo, int pageSize);

	List<BillCustomerDetailEntity> query(Map<String, Object> condition);

    BillCustomerDetailEntity save(BillCustomerDetailEntity entity);

    BillCustomerDetailEntity update(BillCustomerDetailEntity entity);

    void delete(Long id);
    
    /**
     * 对月份进行合并
     * <功能描述>
     * 
     * @author wangchen870
     * @date 2019年6月17日 下午6:34:31
     *
     * @param condition
     * @return
     */
    List<BillCustomerDetailEntity> queryGroupByMonth(Map<String, Object> condition);

    /**
     * 批量写入
     * <功能描述>
     * 
     * @author wangchen870
     * @date 2019年6月19日 下午4:57:42
     *
     * @param list
     */
    void saveBatch(List<BillCustomerDetailEntity> list);
    
    /**
     * 根据主表删除子表数据
     * <功能描述>
     * 
     * @author wangchen870
     * @date 2019年6月20日 上午10:33:30
     *
     * @param param
     */
    void deleteByMaster(Map<String, Object> param);

}
