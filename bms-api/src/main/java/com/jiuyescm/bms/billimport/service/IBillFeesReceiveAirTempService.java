package com.jiuyescm.bms.billimport.service;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.billimport.entity.BillFeesReceiveAirTempEntity;

/** 
 * ..Service
 * @author liuzhicheng
 * 
 */
public interface IBillFeesReceiveAirTempService {

    BillFeesReceiveAirTempEntity findById(Long id);
	
    PageInfo<BillFeesReceiveAirTempEntity> query(Map<String, Object> condition, int pageNo,
            int pageSize);

	List<BillFeesReceiveAirTempEntity> query(Map<String, Object> condition);

    BillFeesReceiveAirTempEntity save(BillFeesReceiveAirTempEntity entity);

    BillFeesReceiveAirTempEntity update(BillFeesReceiveAirTempEntity entity);

    void delete(Long id);
    
    /**
     * 批量写入
     * @param list
     * @return
     */
    int insertBatchTemp(List<BillFeesReceiveAirTempEntity> list);
    
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
	Double getImportAirAmount(String billNo);
}
