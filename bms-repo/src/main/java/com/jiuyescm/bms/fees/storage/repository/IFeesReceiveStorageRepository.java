/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.fees.storage.repository;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.bill.receive.entity.BmsBillInfoEntity;
import com.jiuyescm.bms.bill.receive.entity.BmsBillSubjectInfoEntity;
import com.jiuyescm.bms.fees.entity.FeesBillEntity;
import com.jiuyescm.bms.fees.entity.FeesBillWareHouseEntity;
import com.jiuyescm.bms.fees.storage.entity.FeesReceiveStorageEntity;
import com.jiuyescm.bms.fees.storage.vo.FeesReceiveMaterial;

/**
 * 
 * @author stevenl
 * 
 */
public interface IFeesReceiveStorageRepository {

	public PageInfo<FeesReceiveStorageEntity> query(Map<String, Object> condition,
            int pageNo, int pageSize);

    public FeesReceiveStorageEntity findById(Long id);

    public FeesReceiveStorageEntity save(FeesReceiveStorageEntity entity);

    public FeesReceiveStorageEntity update(FeesReceiveStorageEntity entity);
    
	/**
	 * 根据条件查询单个费用
	 */
    FeesReceiveStorageEntity queryOne(Map<String, Object> condition);

    public void delete(Long id);

	public List<FeesReceiveStorageEntity> queryAll(Map<String, Object> parameter);

	public String getUnitPrice(Map<String, Object> param);

	public int updateBatchBillNo(List<FeesReceiveStorageEntity> feesStorageList);

	//查询符合账单条件的费用数量
	public int queryCountByFeesBillInfo(FeesBillEntity entity);
	
	//查询符合账单条件的费用金额
	public double queryAmountByFeesBillInfo(FeesBillEntity entity);
	
	//更新符合条件的费用信息
	public int updateByFeesBillInfo(FeesBillEntity entity);

	public List<FeesBillWareHouseEntity> querywarehouseStorageAmount(
			String billNo);

	public PageInfo<FeesReceiveStorageEntity> queryStorageDetailGroupPage(
			FeesBillWareHouseEntity queryEntity, int pageNo, int pageSize);

	public void confirmFeesBill(FeesBillEntity entity);

	public int deleteFeesBill(FeesBillEntity entity);

	/**
	 * 查询出库费用
	 * @param parameter
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	PageInfo<FeesReceiveStorageEntity> queryOutStockPage(
			Map<String, Object> parameter, int pageNo, int pageSize);
	
	public PageInfo<FeesReceiveStorageEntity> querystorageDetailByBillNo(
			String billno,int pageNo,int pageSize);
	
	public PageInfo<FeesReceiveStorageEntity> queryStorageByBillNoSubjectId(
			String billno,String subjectId,int pageNo,int pageSize);
	
	//导出-根据账单查询耗材使用
	public PageInfo<FeesReceiveStorageEntity> queryPackMaterialByBillNo(String billno,int pageNo,int pageSize);
	
	public void insertBatch(List<FeesReceiveStorageEntity> list) throws Exception;
	
	PageInfo<FeesReceiveStorageEntity> queryFees(Map<String, Object> condition, int pageNo,
            int pageSize);
	
	
	int deleteEntity(String feesNo);

	public List<FeesReceiveStorageEntity> queryAllByBillSubject(
			BmsBillSubjectInfoEntity bill);

	public void updateDiscountAmountBatch(
			List<FeesReceiveStorageEntity> feesList);

	public PageInfo<FeesReceiveStorageEntity> queryStoragePage(
			Map<String, Object> parameter, int pageNo, int pageSize);

	public PageInfo<FeesReceiveStorageEntity> queryMaterialPage(
			Map<String, Object> parameter, int pageNo, int pageSize);
	
	PageInfo<FeesReceiveStorageEntity> queryStorageAddFeePage(
			Map<String, Object> parameter, int pageNo, int pageSize);
	
	/**
	 * 预账单增值费
	 * @param parameter
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	PageInfo<FeesReceiveStorageEntity> queryPreBillStorageAddFee(
			Map<String, Object> parameter, int pageNo, int pageSize);
	
	//查询符合账单条件的费用数量new
	public int queryCountByBillInfo(BmsBillInfoEntity entity);
	
	//查询符合账单条件的费用金额new
	public double queryAmountByBillInfo(BmsBillInfoEntity entity);
	
	//更新符合条件的费用信息new
	public int updateByBillInfo(BmsBillInfoEntity entity);
	
	//查询账单科目费用new
	public List<BmsBillSubjectInfoEntity> queryFeesBillSubjectInfo(BmsBillInfoEntity entity);
	
	//删除账单
	public int deleteFeesBill(BmsBillInfoEntity entity);
	
	//删除账单-理赔
	public int deleteFeesBillAbnormal(BmsBillInfoEntity entity);
	
	//删除账单-理赔
	public int deleteFeesBillAbnormal(Map<String, Object> condition);

	public void deleteStorageBill(String billNo, String warehouseCode,
			String feesType);

	public List<FeesReceiveStorageEntity> queryAllByBillSubjectCondition(
			BmsBillSubjectInfoEntity bill, BmsBillInfoEntity billInfoEntity);

	public int updateBatchBillNoById(List<FeesReceiveStorageEntity> list);

	public List<FeesReceiveStorageEntity> queryAllByBillSubjectInfo(
			BmsBillSubjectInfoEntity bill, BmsBillInfoEntity billInfoEntity);

	public void derateBatchAmount(List<FeesReceiveStorageEntity> list);

	public void deleteBatchFees(List<FeesReceiveStorageEntity> list);
	
	int updateBatch(List<FeesReceiveStorageEntity> list);
	
	List<FeesReceiveStorageEntity> queryFeesData(Map<String,Object> param);
	
	public PageInfo<FeesReceiveMaterial> queryNewMaterialByBillNo(Map<String,Object> param,int pageNo,int pageSize);
	
	List<FeesReceiveStorageEntity>  queryStorageAdd(Map<String,Object> param);
	
	
	//统计总金额和减免金额
	public BmsBillSubjectInfoEntity sumSubjectMoney(Map<String,Object> param);
	
	//根据条件批量更新费用数据
	public int updateFeeByParam(Map<String,Object> param);

	public List<Map<String, Object>> queryGroup(Map<String, Object> param);
	
	public int updateByFeeNoList(Map<String, Object> condition);

	public List<FeesReceiveStorageEntity> queryBizFeesData(
			Map<String, Object> condition);
	
	public int deleteBatch(Map<String, Object> feesNos);
	
	/**
	 * 查询预账单仓库
	 * @param param
	 * @return
	 */
	public List<String> queryPreBillWarehouse(Map<String,Object> param);
	
	/**
	 * 预账单仓储费(按托)
	 * @param condition
	 * @return
	 */
	public List<FeesReceiveStorageEntity> queryPreBillStorage(Map<String, Object> condition);
	
	/**
	 * 预账单耗材存储费
	 * @param condition
	 * @return
	 */
	public List<FeesReceiveStorageEntity> queryPreBillMaterialStorage(Map<String, Object> condition);
	
	/**
	 * 根据FeesNo查数据
	 * @param FeesNo
	 * @return
	 */
	List<FeesReceiveStorageEntity> queryByFeesNo(String FeesNo);
	
	/**
	 * 预账单仓储费(按件)
	 * @param condition
	 * @return
	 */
	List<FeesReceiveStorageEntity> queryPreBillStorageByItems(Map<String, Object> parameter);
	
	/**
	 * 预账单处置费
	 * @param parameter
	 * @return
	 */
	List<FeesReceiveStorageEntity> queryPreBillPallet(Map<String, Object> parameter);
	
	/**
	 * 查询计算失败的费用
	 * @param parameter
	 * @return
	 */
	List<FeesReceiveStorageEntity> queryCalculateFail(Map<String, Object> parameter);
	
	/**
	 * 删除泡沫箱和纸箱对应得费用
	 * @param parameter
	 * @return
	 */
	int deleteMaterialFee(Map<String,Object> parameter);
	
	/**
	 * 删除泡沫箱对应得费用
	 * @param parameter
	 * @return
	 */
	int deletePmxFee(Map<String,Object> parameter);
	
	/**
	 * 删除纸箱对应得费用
	 * @param parameter
	 * @return
	 */
	int deleteZxFee(Map<String,Object> parameter);
	
	/**
	 * 删除保温袋对应得费用
	 * @param parameter
	 * @return
	 */
	int deleteBwdFee(Map<String,Object> parameter);
	
	/**
	 * 根据费用编号修改费用的数量
	 * @param map
	 * @return
	 */
	int updateQuantityByFeesNo(Map<String, Object> map);
}
