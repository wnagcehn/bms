package com.jiuyescm.bms.receivable.storage.service;

import java.util.List;
import java.util.Map;

import com.jiuyescm.bms.biz.dispatch.entity.BizDispatchPackageEntity;


/**
 * 
 * @author cjw
 * 
 */
public interface IBizDispatchPackageService {

	public List<BizDispatchPackageEntity> query(Map<String, Object> condition);

    public void updateBatch(List<BizDispatchPackageEntity> list);

    /**
     * 通过运单号批量查询
     * <功能描述>
     * 
     * @author wangchen870
     * @date 2019年5月17日 下午6:43:40
     *
     * @param contractList
     * @return
     */
    List<BizDispatchPackageEntity> queryByWaybillNo(List<String> contractList);
    
}
