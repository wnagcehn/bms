/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.bill.receive.repository;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.bill.receive.entity.BmsBillInfoEntity;
import com.jiuyescm.bms.bill.receive.entity.BmsBillInvoceInfoEntity;

/**
 * 
 * @author stevenl
 * 
 */
public interface IBmsBillInvoceInfoRepository {

	public PageInfo<BmsBillInvoceInfoEntity> query(Map<String, Object> condition,
            int pageNo, int pageSize);
	
	List<BmsBillInvoceInfoEntity> queryInvoce(Map<String, Object> condition);
	
	public List<BmsBillInvoceInfoEntity> query(Map<String, Object> condition);

	public BmsBillInvoceInfoEntity queryCountInvoceInfo(Map<String, Object> condition);
	
	public BmsBillInvoceInfoEntity queryCountReceiptInfo(Map<String, Object> condition);
	
    public BmsBillInvoceInfoEntity findById(Long id);

    public int save(BmsBillInvoceInfoEntity entity);

    public int update(BmsBillInvoceInfoEntity entity);
    
    public int deleteFeesBill(BmsBillInfoEntity entity);

}
