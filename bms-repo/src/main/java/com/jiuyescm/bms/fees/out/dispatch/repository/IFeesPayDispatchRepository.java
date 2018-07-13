package com.jiuyescm.bms.fees.out.dispatch.repository;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.fees.bill.out.entity.FeesPayBillEntity;
import com.jiuyescm.bms.fees.entity.FeesBillWareHouseEntity;
import com.jiuyescm.bms.fees.out.dispatch.entity.FeesPayDispatchEntity;
/**
 * 应收费用-配送费 接口
 * 
 * @author yangshuaishuai
 *
 */
public interface IFeesPayDispatchRepository {
	
	PageInfo<FeesPayDispatchEntity> query(Map<String, Object> condition, int pageNo, int pageSize);
	
	List<FeesPayDispatchEntity> queryList(Map<String, Object> condition);
	
	/**
	 * 根据条件查询单个费用
	 */
	FeesPayDispatchEntity queryOne(Map<String, Object> condition);
	
	/** 应付账单使用方法开始 **/
	//查询配送费用 物流商汇总信息
	List<FeesBillWareHouseEntity> queryGroupDispatchAmount(String billNo);
	
	//配送费明细 分页
	public PageInfo<FeesPayDispatchEntity> queryDispatchDetailGroupPage(
			FeesBillWareHouseEntity queryEntity, int pageNo, int pageSize);

	//查询符合账单条件的费用数量
	int queryCountByFeesBillInfo(FeesPayBillEntity entity);
	
	//查询符合账单条件的费用金额
	double queryAmountByFeesBillInfo(FeesPayBillEntity entity);
	
	//更新符合条件的费用信息
	public int updateByFeesBillInfo(FeesPayBillEntity entity);
	
	public int updateBatchBillNo(List<FeesPayDispatchEntity> feesDispatchList);
	
	//核销账单
	int confirmFeesBill(FeesPayBillEntity entity);

	//删除账单
	int deleteFeesBill(FeesPayBillEntity entity);
	
	//删除账单-理赔
	int deleteFeesBillAbnormal(Map<String, Object> parameter);
	
	//导出-根据账单查询配送明细
	public List<FeesPayDispatchEntity> queryDispatchDetailByBillNo(String billno);
	public PageInfo<FeesPayDispatchEntity> queryDispatchDetailByBillNo(String billno,int pageNo,int pageSize);
	/** 应付账单使用方法结束 **/
	
	/**
	 * 单个写入配送费用表
	 * @param entity
	 */
	public boolean Insert(FeesPayDispatchEntity entity);
	
	int insertBatchTmp(List<FeesPayDispatchEntity> list);
	
	FeesPayDispatchEntity validFeesNo(FeesPayDispatchEntity entity);
	
	/**
	 * 根据条件删除费用
	 * @param condition
	 * @return
	 */
	int deleteFeesByMap(Map<String, Object> condition);

	List<FeesBillWareHouseEntity> queryGroupDispatchAmountByDeliver(
			String billno, String deliverid);


	PageInfo<FeesPayDispatchEntity> queryDispatchDetailByBillNoAndDeliver(
			FeesPayBillEntity parameter, int pageNo, int pageSize);

	List<FeesBillWareHouseEntity> queryGroupDispatchAmountSelect(String billno,
			List<String> deliverIdList);

	PageInfo<FeesPayDispatchEntity> queryDispatchDetailBatch(
			List<FeesPayBillEntity> list, int pageNo, int pageSize);
	
	/**
	 * 单条写入配送费用表
	 * @param entity
	 */
	public boolean InsertOne(FeesPayDispatchEntity entity);
}