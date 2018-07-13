package com.jiuyescm.bms.fees.feesreceivedeliver.repository;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.bill.receive.entity.BmsBillInfoEntity;
import com.jiuyescm.bms.bill.receive.entity.BmsBillSubjectInfoEntity;
import com.jiuyescm.bms.fees.entity.FeesBillEntity;
import com.jiuyescm.bms.fees.entity.FeesBillWareHouseEntity;
import com.jiuyescm.bms.fees.entity.FeesReceiveDeliverEntity;
import com.jiuyescm.bms.fees.entity.FeesReceiveDeliverQueryEntity;
/**
 * 
 * @author Wuliangfeng20170527
 *
 */
public interface IFeesReceiveDeliverDao {
	//分页查找
	public PageInfo<FeesReceiveDeliverEntity> query(FeesReceiveDeliverQueryEntity condition, int pageNo, int pageSize);
	
	public FeesReceiveDeliverEntity query(Map<String, Object> condition);
	
	public PageInfo<FeesReceiveDeliverEntity> queryAll(Map<String,Object> condition, int pageNo, int pageSize);
	//增加
	public int addFeesReceiveDeliver(FeesReceiveDeliverEntity entity);
	
	public int saveFeesList(List<FeesReceiveDeliverEntity> feesList);
	
	public String queryunitPrice(Map<String,String> condition);
	
	public int updateBatchBillNo(List<FeesReceiveDeliverEntity> feesDeliverList);
	
	//查询符合账单条件的费用数量
	public int queryCountByFeesBillInfo(FeesBillEntity entity);
	
	//查询符合账单条件的费用金额
	public double queryAmountByFeesBillInfo(FeesBillEntity entity);
	
	//更新符合条件的费用信息
	public int updateByFeesBillInfo(FeesBillEntity entity);
	
	public List<FeesBillWareHouseEntity> querywarehouseDeliverAmount(String billNo);
	
	public PageInfo<FeesReceiveDeliverEntity> queryDeliverDetailGroupPage(
			FeesBillWareHouseEntity queryEntity, int pageNo, int pageSize);
	
	public void confirmFeesBill(FeesBillEntity entity);
	
	public int deleteFeesBill(FeesBillEntity entity);
	
	public PageInfo<FeesReceiveDeliverEntity> querydeliverDetailByBillNo(String billno,int pageNo,int pageSize);
	
	
	//查询符合账单条件的费用数量new
	public int queryCountByBillInfo(BmsBillInfoEntity entity);
	
	//查询符合账单条件的费用金额new
	public double queryAmountByBillInfo(BmsBillInfoEntity entity);
	
	//更新符合条件的费用信息new
	public int updateByBillInfo(BmsBillInfoEntity entity);
	
	// 查询账单科目费用new
	public List<BmsBillSubjectInfoEntity> queryFeesBillSubjectInfo(BmsBillInfoEntity entity);
	public List<BmsBillSubjectInfoEntity> queryFeesBillSubjectInfoAbnormal(BmsBillInfoEntity entity);
	
	//删除账单
	public int deleteFeesBill(BmsBillInfoEntity entity);
	
	//删除账单-理赔
	public int deleteFeesBillAbnormal(BmsBillInfoEntity entity);
	
	public PageInfo<FeesReceiveDeliverEntity> querydeliverDetailByBillNoNew(String billno,int pageNo,int pageSize);

	//根据运单号和时间查询
	public List<FeesReceiveDeliverEntity> queryList(Map<String,Object> condition);
	
	//统计总金额和减免金额
	public BmsBillSubjectInfoEntity sumSubjectMoney(Map<String,Object> param);
	
	//分组统计费用
	public PageInfo<FeesReceiveDeliverEntity> queryFeeByGroup(Map<String,Object> param, int pageNo, int pageSize);
	
	//查询费用明细
	public List<FeesReceiveDeliverEntity> queryFeeDetail(Map<String,Object> param);
	
	//删除账单费用
	public void deleteTransportBill(String billNo);
	
	//根据条件批量更新费用数据
	public int updateFeeByParam(Map<String,Object> param);
	
	PageInfo<FeesReceiveDeliverEntity> queryTransportPage(
			Map<String, Object> parameter, int pageNo, int pageSize);
	
	int updateBatch(List<FeesReceiveDeliverEntity> list);
	
	void derateBatchAmount(List<FeesReceiveDeliverEntity> list);
	
	void deleteBatchFees(List<FeesReceiveDeliverEntity> list);
	
	int updateBatchFees(List<FeesReceiveDeliverEntity> list);
	
	public List<String> getAllSubject(Map<String, Object> param);

	public PageInfo<FeesReceiveDeliverEntity> queryAbnormalFeeByGroup(
			Map<String, Object> parameter, int pageNo, int pageSize);

	public List<FeesReceiveDeliverEntity> queryAbnormalFeeDetail(
			Map<String, Object> parameter);

	public BmsBillSubjectInfoEntity sumAbnormalSubjectMoney(
			Map<String, Object> param);

	public void deleteBatchAbnormalFees(List<FeesReceiveDeliverEntity> list);

	public void deleteAbnormalTransportBill(String billNo);
}
