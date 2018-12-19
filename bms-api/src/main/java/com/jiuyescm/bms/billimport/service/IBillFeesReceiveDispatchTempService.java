/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.billimport.service;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.billimport.entity.BillFeesReceiveDispatchTempEntity;

/**
 * 
 * @author stevenl
 * 
 */
public interface IBillFeesReceiveDispatchTempService {

    PageInfo<BillFeesReceiveDispatchTempEntity> query(Map<String, Object> condition, int pageNo,
            int pageSize);

    BillFeesReceiveDispatchTempEntity findById(Long id);

    BillFeesReceiveDispatchTempEntity save(BillFeesReceiveDispatchTempEntity entity);
    
    int insertBatch(List<BillFeesReceiveDispatchTempEntity> list);

    BillFeesReceiveDispatchTempEntity update(BillFeesReceiveDispatchTempEntity entity);

    void delete(Long id);

    /**
     * 批量删除
     * @param condition
     * @return
     */
	int deleteBatchTemp(String billNo);
    
	/**
	 * 从临时表保存数据到正式表
	 * @param billNo
	 * @return
	 */
	int saveDataFromTemp(String billNo);
	/**
	 * 导入金额汇总
	 * @param billNo
	 * @return
	 */
	Double getImportDispatchAmount(String billNo);
}
