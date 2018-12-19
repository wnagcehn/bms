package com.jiuyescm.bms.billimport.repository;

import java.util.Map;
import java.util.List;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.billimport.entity.BillFeesReceiveStorageTempEntity;

/**
 * ..Repository
 * @author wangchen
 * 
 */
public interface IBillFeesReceiveStorageTempRepository {

    BillFeesReceiveStorageTempEntity findById(Long id);
	
	PageInfo<BillFeesReceiveStorageTempEntity> query(Map<String, Object> condition,
		int pageNo, int pageSize);

	List<BillFeesReceiveStorageTempEntity> query(Map<String, Object> condition);

    BillFeesReceiveStorageTempEntity save(BillFeesReceiveStorageTempEntity entity);

    BillFeesReceiveStorageTempEntity update(BillFeesReceiveStorageTempEntity entity);

    void delete(Long id);
    
    /**
     * 批量写入
     * @param list
     * @return
     * @throws Exception
     */
	int insertBatch(List<BillFeesReceiveStorageTempEntity> list) throws Exception;
	
	/**
	 * 批量删除
	 * @param condition
	 * @return
	 */
	int deleteBatch(String billNo);
	
	/**
	 * 批量删除正式表的费用
	 * @param condition
	 * @return
	 */
	int delete(String billNo);

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
	Double getImportTotalAmount(String billNo);
	
	/**
	 * 导入金额汇总
	 * @param billNo
	 * @return
	 */
	Double getImportStorageAmount(String billNo);

}

