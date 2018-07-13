/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.fees.abnormal.repository;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.bill.receive.entity.BmsBillInfoEntity;
import com.jiuyescm.bms.bill.receive.entity.BmsBillSubjectInfoEntity;
import com.jiuyescm.bms.fees.abnormal.entity.FeesAbnormalEntity;
import com.jiuyescm.bms.fees.bill.out.entity.FeesPayBillEntity;
import com.jiuyescm.bms.fees.entity.FeesBillEntity;
import com.jiuyescm.bms.fees.entity.FeesBillWareHouseEntity;

/**
 * 
 * @author stevenl
 * 
 */
public interface IFeesAbnormalRepository {

	public PageInfo<FeesAbnormalEntity> query(Map<String, Object> condition,
            int pageNo, int pageSize);
	
	// 根据费用标号集合查询异常费用
    List<FeesAbnormalEntity> queryByFeesNos(Map<String, Object> param);
    
	//查询异常费用信息 应收
  	public PageInfo<FeesAbnormalEntity> queryAll(Map<String, Object> param,
            int pageNo, int pageSize);
  	
  	//查询异常费用信息 应付
  	public PageInfo<FeesAbnormalEntity> queryPayAll(Map<String, Object> param,
  			int pageNo, int pageSize);

    public FeesAbnormalEntity findById(Long id);

    public FeesAbnormalEntity save(FeesAbnormalEntity entity);

    public int update(FeesAbnormalEntity entity);

    public void delete(Long id);
    
    /** 应付账单使用方法开始 **/
    //查询异常 费用 仓库 汇总信息
  	public List<FeesBillWareHouseEntity> querywarehouseAbnormalAmount(String billNo);
  	
  	//异常费明细
  	public PageInfo<FeesAbnormalEntity> queryAbnormalDetailGroupPage(
  			FeesBillWareHouseEntity queryEntity, int pageNo, int pageSize);
  	
  	//查询符合账单条件的费用数量 应收
  	public int queryCountByFeesBillInfo(FeesBillEntity entity);
  	
  	//查询符合账单条件的费用数量 应付
  	public int queryCountByFeesPayBillInfo(FeesPayBillEntity entity);
  	
  	//查询符合账单条件的费用金额 应收
  	public double queryAmountByFeesBillInfo(FeesBillEntity entity);
  	
  	//查询符合账单条件的费用金额 应付
  	public double queryAmountByFeesPayBillInfo(FeesPayBillEntity entity);
  	
  	//更新符合条件的费用信息 应收
  	public int updateByFeesBillInfo(FeesBillEntity entity);
  	
  	//更新符合条件的费用信息 应付
  	public int updateByFeesPayBillInfo(FeesPayBillEntity entity);
  	
  	//导出-根据账单查询异常明细
  	public List<FeesAbnormalEntity> queryabnormalDetailByBillNo(String billno);
  	public PageInfo<FeesAbnormalEntity> queryabnormalDetailByBillNo(String billno,int pageNo,int pageSize);
  	
  	int updateBatchBillNo(List<FeesAbnormalEntity> abnormalList);
  	
  	//核销账单 应收
	void confirmFeesBill(FeesBillEntity entity);

	//删除账单 应收
	int deleteFeesBill(FeesBillEntity entity);
	
  	//核销账单 应付
	int confirmFeesBill(FeesPayBillEntity entity);

	//删除账单 应付
	int deleteFeesBill(FeesPayBillEntity entity);
	/** 应付账单使用方法结束 **/
	
	public Double queryTotalPay(Map<String, Object> condition);
	
	List<FeesAbnormalEntity> queryCountByFeesPayBillInfoData(Map<String, Object> param);

	public List<FeesBillWareHouseEntity> queryGroupAbnormalAmountByDeliver(
			String billNo, String deliverid);

	public PageInfo<FeesAbnormalEntity> queryAbnormalDetailByBillNoAndDeliver(
			FeesPayBillEntity parameter, int pageNo, int pageSize);

	public List<FeesBillWareHouseEntity> queryGroupAbnormalAmountSelect(
			String billno, List<String> deliverIdList);

	public PageInfo<FeesAbnormalEntity> queryAbnormalDetailBatch(
			List<FeesPayBillEntity> list, int pageNo, int pageSize);
	
	
	//new查询符合账单条件的费用数量 应收
  	public int queryCountByBillInfo(BmsBillInfoEntity entity);
  	
	//new查询符合账单条件的费用金额
  	public double queryAmountByBillInfo(BmsBillInfoEntity entity);
  	//new查询符合账单条件的费用金额 承运商原因
  	public double queryCYSAmountByBillInfo(BmsBillInfoEntity entity);
  	
  	//new更新符合条件的费用信息 应收
  	public int updateByBillInfo(BmsBillInfoEntity entity);
  	
  	//查询账单科目费用new
  	public List<BmsBillSubjectInfoEntity> queryFeesBillSubjectInfo(BmsBillInfoEntity entity);
  	public List<BmsBillSubjectInfoEntity> queryFeesBillSubjectInfoDis(BmsBillInfoEntity entity);
  	public List<BmsBillSubjectInfoEntity> queryFeesBillSubjectInfoDisChange(BmsBillInfoEntity entity);

	public PageInfo<FeesAbnormalEntity> queryAbnormalDetailPage(
			Map<String, Object> parameter, int pageNo, int pageSize);
  	
  	int deleteFeesBill(BmsBillInfoEntity entity);

	public void deleteBatchFees(List<FeesAbnormalEntity> list);

	public void derateBatchAmount(List<FeesAbnormalEntity> list);

	public void deleteAbnormalBill(String billNo, String warehouseCode,
			String subjectCode) throws Exception;

	public List<FeesAbnormalEntity> queryAllByBillSubjectInfo(
			BmsBillSubjectInfoEntity bill) throws Exception;

	public List<FeesAbnormalEntity> queryAllByBillSubjectInfoCondition(
			BmsBillSubjectInfoEntity bill, BmsBillInfoEntity billInfoEntity);

	public void reSetBatchBillNo(List<FeesAbnormalEntity> list);
	
	public PageInfo<FeesAbnormalEntity> queryAbnormalByBillNo(Map<String,Object> param,int pageNo,int pageSize);

	//统计总金额和减免金额
	public BmsBillSubjectInfoEntity sumSubjectMoney(Map<String,Object> param);
	
	//根据条件批量更新费用数据
	public int updateFeeByParam(Map<String,Object> param);
	
	//根据账单科目查询费用
	public List<FeesAbnormalEntity> queryFeeBySubject(Map<String,Object> param);

	public PageInfo<FeesAbnormalEntity> queryAbnormalChangeByBillNo(
			Map<String, Object> param, int pageNo, int pageSize);

	public FeesAbnormalEntity sumDispatchAmount(String billNo);

	public FeesAbnormalEntity sumDispatchChangeAmount(String billNo);
	
	/**
	 * 预账单理赔
	 * @param param
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public PageInfo<FeesAbnormalEntity> queryPreBillAbnormal(Map<String, Object> param,
            int pageNo, int pageSize);
	
	/**
	 * 预账单改地址
	 * @param param
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public PageInfo<FeesAbnormalEntity> queryPreBillAbnormalChange(Map<String, Object> param,
            int pageNo, int pageSize);
}
