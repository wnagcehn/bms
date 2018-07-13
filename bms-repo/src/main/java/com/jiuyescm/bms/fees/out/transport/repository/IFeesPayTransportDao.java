package com.jiuyescm.bms.fees.out.transport.repository;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.fees.bill.out.entity.FeesPayBillEntity;
import com.jiuyescm.bms.fees.entity.FeesBillWareHouseEntity;
import com.jiuyescm.bms.fees.out.transport.entity.FeesPayTransportEntity;
import com.jiuyescm.bms.fees.out.transport.entity.FeesPayTransportQueryEntity;
/**
 * 
 * @author Wuliangfeng 20170527
 *
 */
public interface IFeesPayTransportDao {
	//分页查找
	public PageInfo<FeesPayTransportEntity> query(FeesPayTransportQueryEntity condition, int pageNo, int pageSize);
	
	public PageInfo<FeesPayTransportEntity> queryAll(Map<String,Object> condition, int pageNo, int pageSize);
	
	//增加
	public int AddFeesReceiveDeliver(FeesPayTransportEntity entity);
	
	public String queryunitPrice(Map<String,String> condition);
	
	/** 应付账单使用方法开始 **/
	//查询运输费用 费用科目汇总信息
  	public List<FeesBillWareHouseEntity> queryGroupTransportAmount(String billNo);
  	
  	//运输费明细 分页
  	public PageInfo<FeesPayTransportEntity> queryTransportDetailGroupPage(
  			FeesBillWareHouseEntity queryEntity, int pageNo, int pageSize);
  	
	//查询符合账单条件的费用数量
	public int queryCountByFeesBillInfo(FeesPayBillEntity entity);
	
	//查询符合账单条件的费用金额
	public double queryAmountByFeesBillInfo(FeesPayBillEntity entity);
	
	public int updateBatchBillNo(List<FeesPayTransportEntity> feesTransportList);
	
	//更新符合条件的费用信息
	public int updateByFeesBillInfo(FeesPayBillEntity entity);
	
	public int confirmFeesBill(FeesPayBillEntity entity);
	
	public int deleteFeesBill(FeesPayBillEntity entity);
	
	public PageInfo<FeesPayTransportEntity> queryTransportDetailByBillNo(String billno,int pageNo,int pageSize);
	/** 应付账单使用方法结束 **/

	public List<FeesPayTransportEntity> queryByImport(List<String> waybillnoList);

	public int saveDataBatch(List<FeesPayTransportEntity> list);

	public List<FeesPayTransportEntity> queryByWaybillNo(String waybillno);

	public PageInfo<FeesPayTransportEntity> queryTransportDetailByBillNoAndDeliver(
			FeesPayBillEntity entity, int pageNo, int pageSize);
	
	//根据运单号和时间查询
	public List<FeesPayTransportEntity> queryList(Map<String,Object> condition);

	public List<FeesBillWareHouseEntity> queryAbnormalGroupAmount(String billNo);

	public PageInfo<FeesPayTransportEntity> queryAbnormalDetailByBillNo(
			String billno, int pageNo, int pageSize);
}
