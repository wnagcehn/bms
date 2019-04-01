package com.jiuyescm.bms.general.service;

import java.util.Map;
import java.util.List;
import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.general.entity.BizDispatchCarrierChangeDetailEntity;

/**
 * ..Service
 * @author wangchen
 * 
 */
public interface IBizDispatchCarrierChangeDetailService {

    BizDispatchCarrierChangeDetailEntity findById(Long id);
	
	PageInfo<BizDispatchCarrierChangeDetailEntity> query(Map<String, Object> condition,
		int pageNo, int pageSize);

	List<BizDispatchCarrierChangeDetailEntity> query(Map<String, Object> condition);

    BizDispatchCarrierChangeDetailEntity save(BizDispatchCarrierChangeDetailEntity entity);

    BizDispatchCarrierChangeDetailEntity update(BizDispatchCarrierChangeDetailEntity entity);

    void delete(Long id);
    /**
     * 查询状态为0的全部数据
     * @return
     */
	List<BizDispatchCarrierChangeDetailEntity> queryAll();
	
	/**
	 * 根据运单号查询
	 * @param waybillNo
	 * @return
	 */
	BizDispatchCarrierChangeDetailEntity queryByWayBillNo(String waybillNo);

}
