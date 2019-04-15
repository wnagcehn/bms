package com.jiuyescm.bms.general.service;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.general.entity.BizDispatchCarrierChangeEntity;

/**
 * ..Service
 * @author wangchen
 * 
 */
public interface IBizDispatchCarrierChangeService {

    BizDispatchCarrierChangeEntity findById(Long id);
	
	PageInfo<BizDispatchCarrierChangeEntity> query(Map<String, Object> condition,
		int pageNo, int pageSize);

	List<BizDispatchCarrierChangeEntity> query(Map<String, Object> condition);
	
	/**
	 * 保存并更新状态
	 * @param entity
	 */
    void saveAndUpdateState(BizDispatchCarrierChangeEntity entity);

    BizDispatchCarrierChangeEntity update(BizDispatchCarrierChangeEntity entity);

    void delete(Long id);
    
    /**
     * 根据运单号查询
     * @param waybillNo
     * @return
     */
	BizDispatchCarrierChangeEntity findByWaybillNo(String waybillNo);

}
