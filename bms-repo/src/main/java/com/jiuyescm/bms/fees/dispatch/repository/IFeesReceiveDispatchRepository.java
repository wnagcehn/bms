package com.jiuyescm.bms.fees.dispatch.repository;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.bill.receive.entity.BmsBillInfoEntity;
import com.jiuyescm.bms.bill.receive.entity.BmsBillSubjectInfoEntity;
import com.jiuyescm.bms.fees.dispatch.entity.FeesReceiveDispatchEntity;
import com.jiuyescm.bms.fees.dispatch.vo.FeesReceiveDispatchVo;
import com.jiuyescm.bms.fees.entity.FeesBillEntity;
import com.jiuyescm.bms.fees.entity.FeesBillWareHouseEntity;
/**
 * 应收费用-配送费 接口
 * 
 * @author yangshuaishuai
 *
 */
public interface IFeesReceiveDispatchRepository {
	
	PageInfo<FeesReceiveDispatchEntity> query(Map<String, Object> condition, int pageNo, int pageSize);
	
	/**
	 * 配送应付费用导出
	 */
	PageInfo<FeesReceiveDispatchVo> queryFeesImport(Map<String, Object> condition, int pageNo, int pageSize);
	
	PageInfo<FeesReceiveDispatchVo> query1(Map<String, Object> condition, int pageNo, int pageSize);
	
	FeesReceiveDispatchEntity queryById(Long id);
	
	List<FeesReceiveDispatchEntity> queryList(Map<String, Object> condition);
	
	/**
	 * 根据条件查询单个费用
	 */
	FeesReceiveDispatchEntity queryOne(Map<String, Object> condition);
	
	int updateBatchTmp(List<FeesReceiveDispatchEntity> list);

	int updateBatchBillNo(
			List<FeesReceiveDispatchEntity> feesDistributionList);

	//查询符合账单条件的费用数量
	int queryCountByFeesBillInfo(FeesBillEntity entity);
	
	//查询符合账单条件的费用金额
	double queryAmountByFeesBillInfo(FeesBillEntity entity);
	
	//更新符合条件的费用信息
	public int updateByFeesBillInfo(FeesBillEntity entity);

	List<FeesBillWareHouseEntity> querywarehouseDistributionAmount(String billNo);

	public PageInfo<FeesReceiveDispatchEntity> queryDispatchDetailGroupPage(
			FeesBillWareHouseEntity queryEntity, int pageNo, int pageSize);

	void confirmFeesBill(FeesBillEntity entity);

	int deleteFeesBill(FeesBillEntity entity);

	List<FeesReceiveDispatchEntity> querydistributionDetailByBillNo(String billno);
	
	PageInfo<FeesReceiveDispatchEntity> querydistributionDetailByBillNo(
			String billno,int pageNo,int pageSize);
	
	/**
	 * 批量插入账单数据
	 * @param list
	 * @return
	 */
	int insertBatchTmp(List<FeesReceiveDispatchEntity> list);
	
	/**
	 * 单条插入账单数据
	 * @param entity
	 * @return
	 */
	int insertOne(FeesReceiveDispatchEntity entity);
	
	
	//查询符合账单条件的费用数量
	int queryCountByBillInfo(BmsBillInfoEntity entity);
	
	//查询符合账单条件的费用金额
	double queryAmountByBillInfo(BmsBillInfoEntity entity);
	
	//更新符合条件的费用信息
	public int updateByBillInfo(BmsBillInfoEntity entity);
	
	//查询账单科目费用new
	public List<BmsBillSubjectInfoEntity> queryFeesBillSubjectInfo(BmsBillInfoEntity entity);
	
	//删除账单
	int deleteFeesBill(BmsBillInfoEntity entity);
	
	//删除账单-理赔
	int deleteFeesBillAbnormal(BmsBillInfoEntity entity);
	
	//删除账单-理赔
	int deleteFeesBillAbnormal(Map<String, Object> condition);

	void deleteDispatchBill(String billNo, String warehouseCode,
			String subjectCode);

	List<FeesReceiveDispatchEntity> queryAllByBillSubjectCondition(
			BmsBillSubjectInfoEntity bill, BmsBillInfoEntity billInfoEntity);

	List<FeesReceiveDispatchEntity> queryAllByBillSubjectInfo(
			BmsBillSubjectInfoEntity bill);

	PageInfo<FeesReceiveDispatchEntity> queryDispatchPage(
			Map<String, Object> parameter, int pageNo, int pageSize);

	void deleteBatchFees(List<FeesReceiveDispatchEntity> list);

	void derateBatchAmount(List<FeesReceiveDispatchEntity> list);

	List<FeesReceiveDispatchEntity> queryAllByBillSubjectInfoMap(
			Map<String, Object> parameter);

	//统计总金额和减免金额
	public BmsBillSubjectInfoEntity sumSubjectMoney(Map<String,Object> param);
	
	//根据条件批量更新费用数据
	public int updateFeeByParam(Map<String,Object> param);

	List<Map<String, Object>> queryGroup(Map<String, Object> param);

	PageInfo<FeesReceiveDispatchEntity> querydistributionDetailByBizData(
			Map<String, Object> condition,int pageNo, int pageSize);
}
