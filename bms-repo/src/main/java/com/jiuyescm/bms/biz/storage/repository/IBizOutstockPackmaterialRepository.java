package com.jiuyescm.bms.biz.storage.repository;

import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.biz.storage.entity.BizOutstockPackmaterialEntity;
import com.jiuyescm.bms.fees.storage.vo.FeesReceiveMaterial;

public interface IBizOutstockPackmaterialRepository {

	PageInfo<BizOutstockPackmaterialEntity> query(Map<String, Object> condition, int pageNo, int pageSize);
	
	List<BizOutstockPackmaterialEntity> getBizstockPack(Map<String, Object> condition);
	
	public int update(BizOutstockPackmaterialEntity entity);
	
	int updateList(List<BizOutstockPackmaterialEntity> list);
	
	int saveList(List<BizOutstockPackmaterialEntity> list);
	
	/*
	 * 仓库维度统计
	 */
	PageInfo<BizOutstockPackmaterialEntity> queryWarehouseGroupCount(Map<String, Object> condition,int pageNo, int pageSize);
	
	/*
	 * 财务维度统计
	 */
	PageInfo<BizOutstockPackmaterialEntity> queryPriceGroupCount(Map<String, Object> condition,int pageNo, int pageSize);
	
    Properties validRetry(Map<String, Object> param);
	 
	 int reCalculate(Map<String, Object> param);
	 
	 int deleteList(List<BizOutstockPackmaterialEntity> list);
		
	List<BizOutstockPackmaterialEntity> queryList(Map<String, Object> condition);
	
	int deleteFees(Map<String, Object> condition);
	
	PageInfo<Map<String,String>> queryByMonth(Map<String, Object> condition,int pageNo, int pageSize);
	 	 
    PageInfo<Map<String,String>>  queryCustomeridF(Map<String, Object> condition,int pageNo, int pageSize);
	
	 /**
	  * 判断是否有计算异常的数据
	  * @param condition
	  * @return
	  */
	 public BizOutstockPackmaterialEntity queryExceptionOne(Map<String,Object> condition);
	 
	 List<Map<String,String>> getMaterialCode(Map<String,Object> param);

	List<BizOutstockPackmaterialEntity> getOrgPackMaterialList(
			Map<String, Object> querymap);
	
	List<String> queryBillWarehouse(Map<String,Object> param);
	
	List<BizOutstockPackmaterialEntity> queryAllByWaybillNoList(
			List<String> waybillNoList);

	int updateCostIsCalculated(Map<String, Object> param, String isCalculated);

	List<String> queryCosthasBill(Map<String, Object> param);

	int saveDataFromTemp(String batchNum);

	boolean checkDataExist(String waybillNoExcel, String materialCode);

	List<BizOutstockPackmaterialEntity> getMaterialCodeFromBizData(
			Map<String, Object> condition);

	PageInfo<FeesReceiveMaterial> queryMaterialFromBizData(
			Map<String, Object> condition, int pageNo, int size);

	List<BizOutstockPackmaterialEntity> queryAllWarehouseFromBizData(
			Map<String, Object> condition);
	
	List<BizOutstockPackmaterialEntity> queryMaterial(Map<String, Object> condition);
	public List<String> queryWayBillNo(Map<String,Object> condition);
	
	Double getMaxVolum(Map<String,Object> condition);
	
	Double getMaxPmxVolum(Map<String,Object> condition);
	
	Double getMaxZxVolum(Map<String,Object> condition);
	
	Double getMaxVolumByMap(Map<String,Object> condition);

	int deleteAllByWayBillNo(List<String> waybillNoList);
	
	/**
	 * 非成功统计
	 * @param condition
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	PageInfo<BizOutstockPackmaterialEntity> queryErrorCal(Map<String, Object> condition, int pageNo, int pageSize);
	
	/**
	 * 查询原始耗材
	 * @param condition
	 * @return
	 */
	List<BizOutstockPackmaterialEntity> queryOriginMaterialFromBizData(Map<String, Object> condition);
	
	/**
	 * 获取最高的体积
	 * @param condition
	 * @return
	 */
	public String getMaxBwdVolumn(Map<String, Object> condition);
	
	/**
	 * 删除运单号对应的泡沫箱和纸箱
	 * @param wayList
	 * @return
	 */
	public int deleteOldMaterial(Map<String, Object> condition);
	
	/**
	 * 删除运单号对应的泡沫箱
	 * @param wayList
	 * @return
	 */
	public int deleteOldPmx(Map<String,Object> condition);
	
	/**
	 * 删除运单号对应的纸箱
	 * @param wayList
	 * @return
	 */
	public int deleteOldZx(Map<String,Object> condition);
	
	/**
	 * 删除运单号对应的保温袋
	 * @param wayList
	 * @return
	 */
	public int deleteOldBwd(Map<String,Object> condition);
}
