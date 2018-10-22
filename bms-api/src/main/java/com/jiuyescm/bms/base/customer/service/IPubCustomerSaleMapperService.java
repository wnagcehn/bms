package com.jiuyescm.bms.base.customer.service;

import java.util.Map;
import java.util.List;
import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.base.customer.entity.PubCustomerSaleMapperEntity;

/**
 * ..Service
 * @author wangchen
 * 
 */
public interface IPubCustomerSaleMapperService {

    PubCustomerSaleMapperEntity findById(Long id);
	
    PageInfo<PubCustomerSaleMapperEntity> query(Map<String, Object> condition, int pageNo,
            int pageSize);

	List<PubCustomerSaleMapperEntity> query(Map<String, Object> condition);

    PubCustomerSaleMapperEntity save(PubCustomerSaleMapperEntity entity);

    PubCustomerSaleMapperEntity update(PubCustomerSaleMapperEntity entity);

    void delete(Long id);
    
    /**
     * 批量写入
     * @param list
     * @return
     */
	int insertBatchTmp(List<PubCustomerSaleMapperEntity> list);
	
	/**
	 * 批量更新
	 * @param list
	 * @return
	 */
	int updateBatchTmp(List<PubCustomerSaleMapperEntity> list);

}
