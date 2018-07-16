package com.jiuyescm.bms.billcheck.repository;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.billcheck.BillCheckAdjustInfoEntity;
import com.jiuyescm.bms.billcheck.BillCheckInfoEntity;
import com.jiuyescm.bms.billcheck.BillReceiptFollowEntity;

public interface IBillCheckInfoRepository {
	PageInfo<BillCheckInfoEntity> query(Map<String, Object> condition, int pageNo,
            int pageSize);
	
	PageInfo<BillCheckInfoEntity> queryByInvoiceNo(Map<String, Object> condition, int pageNo,
            int pageSize);
	
	List<BillCheckInfoEntity> queryList(Map<String, Object> condition);
	
	/**
	 * 对账主表的信息
	 * @param condition
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public List<BillCheckAdjustInfoEntity> queryAdjust(Map<String, Object> condition);
	
	/**
	 * 对账主表调整的信息
	 * @param condition
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public BillCheckAdjustInfoEntity queryOneAdjust(Map<String, Object> condition);
	/**
	 * 对账主表的回款反馈信息
	 * @param condition
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	PageInfo<BillReceiptFollowEntity> queryReceiptFollow(Map<String, Object> condition, int pageNo,
            int pageSize);
	
	public int update(BillCheckInfoEntity billCheckInfoEntity);

	/**
	 * 根据条件查询
	 */
	BillCheckInfoEntity queryOne(Map<String, Object> condition);
	
	/**
	 * 根据条件查询
	 */
	BillCheckInfoEntity queryBillCheck(Map<String, Object> condition);
	
	/**
	 * 保存数据
	 * @param list
	 * @return
	 */
	public int saveList(List<BillCheckInfoEntity> list);
	
	/**
	 * 保存回款反馈
	 * @param entity
	 * @return
	 */
	public int saveReceiptFollow(BillReceiptFollowEntity entity);
	
	/**
	 * 批量保存回款反馈
	 * @param entity
	 * @return
	 */
	public int saveReceiptFollowList(List<BillReceiptFollowEntity> list);
	
	/**
	 * 批量保存收款调整信息
	 * @param list
	 * @return
	 */
	public int saveAjustList(List<BillCheckAdjustInfoEntity> list);
	
	/**
	 * 批量更新收款调整信息
	 * @param list
	 * @return
	 */
	public int updateAjustList(List<BillCheckAdjustInfoEntity> list);

	String getBillCheckStatus(int id);

	List<BillCheckInfoEntity> queryAllByBillName(List<String> billNameList);

	List<BillCheckInfoEntity> queryAllByBillCheckId(List<Integer> checkIdList);

	int updateInvoiceStatus(List<BillCheckInfoEntity> infoList);

	int updateReceiptStatus(List<BillCheckInfoEntity> infoList);
	
	public BillReceiptFollowEntity queryReceiptFollow(Map<String, Object> condition);
	
	/**
	 * 对账收款明细的信息
	 * @param condition
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	PageInfo<BillCheckInfoEntity> queryReceiptDetail(Map<String, Object> condition, int pageNo,
            int pageSize); 

}