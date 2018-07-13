/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.fees.abnormal.repository;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.fees.abnormal.entity.FeesPayAbnormalEntity;
import com.jiuyescm.bms.fees.bill.out.entity.FeesPayBillEntity;
import com.jiuyescm.bms.fees.entity.FeesBillWareHouseEntity;

/**
 * 
 * @author stevenl
 * 
 */
public interface IFeesPayAbnormalRepository {

	public PageInfo<FeesPayAbnormalEntity> query(Map<String, Object> condition,
            int pageNo, int pageSize);
  	
  	//查询异常费用信息 应付
  	public PageInfo<FeesPayAbnormalEntity> queryPayAll(Map<String, Object> param,
  			int pageNo, int pageSize);

    public FeesPayAbnormalEntity findById(Long id);

    public FeesPayAbnormalEntity save(FeesPayAbnormalEntity entity);

    public int update(FeesPayAbnormalEntity entity);

    public void delete(Long id);
    
    /** 应付账单使用方法开始 **/
    //查询异常 费用 仓库 汇总信息
  	public List<FeesBillWareHouseEntity> querywarehouseAbnormalAmount(String billNo);
  	
  	//异常费明细
  	public PageInfo<FeesPayAbnormalEntity> queryAbnormalDetailGroupPage(
  			FeesBillWareHouseEntity queryEntity, int pageNo, int pageSize);
  	
  	//查询符合账单条件的费用数量 应付
  	public int queryCountByFeesPayBillInfo(FeesPayBillEntity entity);
  	
  	//更新符合条件的费用信息 应付
  	public int updateByFeesPayBillInfo(FeesPayBillEntity entity);
  	
  	//导出-根据账单查询异常明细
  	public PageInfo<FeesPayAbnormalEntity> queryabnormalDetailByBillNo(String billno,int pageNo,int pageSize);
  	
  	int updateBatchBillNo(List<FeesPayAbnormalEntity> abnormalList);

	//删除账单 应付
	int deleteFeesBill(FeesPayBillEntity entity);
	/** 应付账单使用方法结束 **/
	
	public Double queryTotalPay(Map<String, Object> condition);
	
	List<FeesPayAbnormalEntity> queryCountByFeesPayBillInfoData(Map<String, Object> param);

	public List<FeesBillWareHouseEntity> queryGroupAbnormalAmountByDeliver(
			String billNo, String deliverid);

	public PageInfo<FeesPayAbnormalEntity> queryAbnormalDetailByBillNoAndDeliver(
			FeesPayBillEntity parameter, int pageNo, int pageSize);

	public List<FeesBillWareHouseEntity> queryGroupAbnormalAmountSelect(
			String billno, List<String> deliverIdList);

	public PageInfo<FeesPayAbnormalEntity> queryAbnormalDetailBatch(
			List<FeesPayBillEntity> list, int pageNo, int pageSize);
  	
	public void deleteBatchFees(List<FeesPayAbnormalEntity> list);

	public void derateBatchAmount(List<FeesPayAbnormalEntity> list);
	
	//根据条件查询应付异常费用
	public List<FeesPayAbnormalEntity> queryFeeByParam(Map<String,Object> param);

}
