/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.bill.receive.service;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.bill.receive.entity.BmsBillInvoceInfoEntity;
import com.jiuyescm.bms.common.system.ResponseVo;

/**
 * 
 * @author stevenl
 * 
 */
public interface IBmsBillInvoceInfoService {

    PageInfo<BmsBillInvoceInfoEntity> query(Map<String, Object> condition, int pageNo,
            int pageSize);
    
    List<BmsBillInvoceInfoEntity> queryInvoce(Map<String, Object> condition);
    
    BmsBillInvoceInfoEntity findById(Long id);

    int save(BmsBillInvoceInfoEntity entity);

    int update(BmsBillInvoceInfoEntity entity);

    //统计开票信息
    BmsBillInvoceInfoEntity queryCountInvoceInfo(Map<String, Object> param);
    
    //开票
    ResponseVo openInvoce(BmsBillInvoceInfoEntity entity);
    
    //删除发票
    ResponseVo delInvoce(BmsBillInvoceInfoEntity entity);
    
    //统计收款信息
    BmsBillInvoceInfoEntity queryCountReceiptInfo(Map<String, Object> param);
    
    //收款
    ResponseVo receipt(BmsBillInvoceInfoEntity entity);
}
