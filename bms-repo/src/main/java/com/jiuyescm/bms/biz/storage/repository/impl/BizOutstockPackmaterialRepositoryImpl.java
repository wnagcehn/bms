package com.jiuyescm.bms.biz.storage.repository.impl;

import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.jiuyescm.bms.biz.storage.entity.BizOutstockPackmaterialEntity;
import com.jiuyescm.bms.biz.storage.repository.IBizOutstockPackmaterialRepository;
import com.jiuyescm.bms.fees.storage.vo.FeesReceiveMaterial;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;

@SuppressWarnings("unchecked")
@Repository("bizOutstockPackmaterialRepositoryImpl")
public class BizOutstockPackmaterialRepositoryImpl extends MyBatisDao implements IBizOutstockPackmaterialRepository{

	private static final Logger logger = Logger.getLogger(BizOutstockPackmaterialRepositoryImpl.class.getName());
	
	@Override
	public PageInfo<BizOutstockPackmaterialEntity> query(Map<String, Object> condition,
			int pageNo, int pageSize) {
		List<BizOutstockPackmaterialEntity> list = selectList("com.jiuyescm.bms.biz.storage.mapper.BizOutstockPackmaterialMapper.query",
				condition, new RowBounds(pageNo, pageSize));
		return new PageInfo<BizOutstockPackmaterialEntity>(list);
	}

	@Override
	public int update(BizOutstockPackmaterialEntity entity) {
		return super.update("com.jiuyescm.bms.biz.storage.mapper.BizOutstockPackmaterialMapper.update", entity);
	}

	@Override
	public int saveList(List<BizOutstockPackmaterialEntity> list) {
		return insertBatch("com.jiuyescm.bms.biz.storage.mapper.BizOutstockPackmaterialMapper.save", list);
	}

	@Override
	public List<BizOutstockPackmaterialEntity> getBizstockPack(Map<String, Object> condition) {
		try{
			List<BizOutstockPackmaterialEntity> list = selectList("com.jiuyescm.bms.biz.storage.mapper.BizOutstockPackmaterialMapper.query",condition);
			return list;
		}
		catch(Exception ex){
			logger.error("数据库操作失败", ex);
			return null;
		}
	}

	@Override
	public PageInfo<BizOutstockPackmaterialEntity> queryWarehouseGroupCount(
			Map<String, Object> condition, int pageNo, int pageSize) {
		List<BizOutstockPackmaterialEntity> list = 
				selectList("com.jiuyescm.bms.biz.storage.mapper.BizOutstockPackmaterialMapper.queryWarehouseGroupCount",
				condition, new RowBounds(pageNo, pageSize));
		return new PageInfo<BizOutstockPackmaterialEntity>(list);
	}

	@Override
	public PageInfo<BizOutstockPackmaterialEntity> queryPriceGroupCount(
			Map<String, Object> condition, int pageNo, int pageSize) {
		List<BizOutstockPackmaterialEntity> list = 
				selectList("com.jiuyescm.bms.biz.storage.mapper.BizOutstockPackmaterialMapper.queryPriceGroupCount",
				condition, new RowBounds(pageNo, pageSize));
		return new PageInfo<BizOutstockPackmaterialEntity>(list);
	}
	
	@Override
	public PageInfo<BizOutstockPackmaterialEntity> queryErrorCal(
			Map<String, Object> condition, int pageNo, int pageSize) {
		List<BizOutstockPackmaterialEntity> list = 
				selectList("com.jiuyescm.bms.biz.storage.mapper.BizOutstockPackmaterialMapper.queryErrorCal",
				condition, new RowBounds(pageNo, pageSize));
		return new PageInfo<BizOutstockPackmaterialEntity>(list);
	}

	@Override
	public Properties validRetry(Map<String, Object> param) {
		Properties ret = new Properties();
		try{
			@SuppressWarnings("unchecked")
			Object waybillno = selectOne("com.jiuyescm.bms.biz.storage.mapper.BizOutstockPackmaterialMapper.validBillForRetry", param);
			if(waybillno != null){
				ret.setProperty("key", "Billed");
				ret.setProperty("value", "部分数据已在账单中存在,不能重算,建议删除账单后重试");//已在账单中存在,不能重算,建议删除账单后重试;
				return ret;
			}
			
			waybillno = selectOne("com.jiuyescm.bms.biz.storage.mapper.BizOutstockPackmaterialMapper.validCalcuForRetry", param);
			if(waybillno != null){
				ret.setProperty("key", "Calculated");
				ret.setProperty("value", "存在已计算的数据");//已在账单中存在,不能重算,建议删除账单后重试;
				return ret;
			}
			ret.setProperty("key", "OK");
			ret.setProperty("value", "");
			return ret;
		}
		catch(Exception ex){
			logger.error("数据库操作失败", ex);
			ret.setProperty("key", "Error");
			ret.setProperty("value", "系统异常-验证重算异常");
			return ret;
		}
	}

	@Override
	public int reCalculate(Map<String, Object> param) {
		try{
			update("com.jiuyescm.bms.biz.storage.mapper.BizOutstockPackmaterialMapper.retryForCalcu", param);
			return 1;
		}
		catch(Exception ex){
			logger.error("数据库操作失败", ex);
			return 0;
		}
	}
	
	@Override
	public int deleteList(List<BizOutstockPackmaterialEntity> list) {
		return deleteBatch("com.jiuyescm.bms.biz.storage.mapper.BizOutstockPackmaterialMapper.delete",list);
	}

	@Override
	public List<BizOutstockPackmaterialEntity> queryList(Map<String, Object> condition) {
		return  selectList("com.jiuyescm.bms.biz.storage.mapper.BizOutstockPackmaterialMapper.queryDelete",condition);
	}

	@Override
	public int deleteFees(Map<String, Object> condition) {
		return delete("com.jiuyescm.bms.biz.storage.mapper.BizOutstockPackmaterialMapper.deleteFees",condition);
	}

	@Override
	public PageInfo<Map<String, String>> queryByMonth(
			Map<String, Object> condition, int pageNo, int pageSize) {
		List<Map<String, String>> list = selectList("com.jiuyescm.bms.biz.storage.mapper.BizOutstockPackmaterialMapper.queryByMonth", condition, new RowBounds(
                pageNo, pageSize));
        PageInfo<Map<String, String>> pageInfo = new PageInfo<Map<String, String>>(list);
        return pageInfo;
	}

	@Override
	public PageInfo<Map<String, String>> queryCustomeridF(
			Map<String, Object> condition, int pageNo, int pageSize) {
		
		List<Map<String, String>> list = selectList("com.jiuyescm.bms.biz.storage.mapper.BizOutstockPackmaterialMapper.queryCustomeridF", condition, new RowBounds(
                pageNo, pageSize));
        PageInfo<Map<String, String>> pageInfo = new PageInfo<Map<String, String>>(list);
        return pageInfo;
	}
	
	@Override
	public BizOutstockPackmaterialEntity queryExceptionOne(
			Map<String, Object> condition) {
		// TODO Auto-generated method stub
		return (BizOutstockPackmaterialEntity) selectOne("com.jiuyescm.bms.biz.storage.mapper.BizOutstockPackmaterialMapper.queryExceptionOne", condition);
	}

	@Override
	public List<Map<String, String>> getMaterialCode(Map<String, Object> param) {
		return selectList("com.jiuyescm.bms.biz.storage.mapper.BizOutstockPackmaterialMapper.queryMaterialCodeByBillNo",param);
	}

	@Override
	public List<BizOutstockPackmaterialEntity> getOrgPackMaterialList(
			Map<String, Object> querymap) {
		return this.selectList("com.jiuyescm.bms.biz.storage.mapper.BizOutstockPackmaterialMapper.queryPackMaterialByOutstockNo", querymap);
	}

	@Override
	public List<String> queryBillWarehouse(Map<String, Object> param) {
		return (List<String>)selectList("com.jiuyescm.bms.biz.storage.mapper.BizOutstockPackmaterialMapper.queryBillWarehouse", param);
	}

	@Override
	public List<BizOutstockPackmaterialEntity> queryAllByWaybillNoList(
			List<String> waybillNoList) {
		Map<String,Object> map=Maps.newHashMap();
		map.put("waybillNoList", waybillNoList);
		return this.selectList("com.jiuyescm.bms.biz.storage.mapper.BizOutstockPackmaterialMapper.queryAllByWaybillNoList", map);
	}

	@Override
	public int updateCostIsCalculated(Map<String, Object> param,
			String isCalculated) {
		param.put("costIsCalculated", isCalculated);
		param.put("costRemark", "待重算");
		return this.update("com.jiuyescm.bms.biz.storage.mapper.BizOutstockPackmaterialMapper.updateCostIsCalculated", param);
	}

	@Override
	public List<String> queryCosthasBill(Map<String, Object> param) {
		return this.selectList("com.jiuyescm.bms.biz.storage.mapper.BizOutstockPackmaterialMapper.queryCosthasBill", param);
	}

	@Override
	public int saveDataFromTemp(String batchNum) {
		 SqlSession session = getSqlSessionTemplate();
		 Map<String,String> map=Maps.newHashMap();

		 map.put("batchNum", batchNum);

		 return session.insert("com.jiuyescm.bms.biz.storage.mapper.BizOutstockPackmaterialMapper.saveDataFromTemp", map);
	}

	@Override
	public boolean checkDataExist(String waybillNoExcel, String materialCode) {
		Map<String,String> map=Maps.newHashMap();
		map.put("waybillNo", waybillNoExcel);
		map.put("materialCode", materialCode);
		Object obj=this.selectOne("com.jiuyescm.bms.biz.storage.mapper.BizOutstockPackmaterialMapper.checkDataExist", map);
		if(obj!=null){
			int k=Integer.valueOf(obj.toString());
			if(k>0){
				return false;
			}else{
				return true;
			}
		}
		return true;
	}

	@Override
	public List<BizOutstockPackmaterialEntity> getMaterialCodeFromBizData(
			Map<String, Object> condition) {
		
		return this.selectList("com.jiuyescm.bms.biz.storage.mapper.BizOutstockPackmaterialMapper.getMaterialCodeFromBizData", condition);
	}
	
	@Override
	public List<BizOutstockPackmaterialEntity> queryOriginMaterialFromBizData(
			Map<String, Object> condition) {
		return this.selectList("com.jiuyescm.bms.biz.storage.mapper.BizOutstockPackmaterialMapper.queryOriginMaterialFromBizData", condition);
	}

	@Override
	public PageInfo<FeesReceiveMaterial> queryMaterialFromBizData(
			Map<String, Object> condition, int pageNo, int size) {
		List<FeesReceiveMaterial> list=this.selectList("com.jiuyescm.bms.biz.storage.mapper.BizOutstockPackmaterialMapper.queryMaterialFromBizData", condition,new RowBounds(pageNo,size));
		return new PageInfo<FeesReceiveMaterial>(list);
	}

	@Override
	public List<BizOutstockPackmaterialEntity> queryAllWarehouseFromBizData(
			Map<String, Object> condition) {
		return this.selectList("com.jiuyescm.bms.biz.storage.mapper.BizOutstockPackmaterialMapper.queryAllWarehouseFromBizData", condition);
	}

	@Override
	public List<BizOutstockPackmaterialEntity> queryMaterial(
			Map<String, Object> condition) {
		// TODO Auto-generated method stub
		return this.selectList("com.jiuyescm.bms.biz.storage.mapper.BizOutstockPackmaterialMapper.queryMaterial", condition);
	}

	@Override
	public int updateList(List<BizOutstockPackmaterialEntity> list) {
		// TODO Auto-generated method stub
		return  updateBatch("com.jiuyescm.bms.biz.storage.mapper.BizOutstockPackmaterialMapper.update", list);
	}

	@Override
	public List<String> queryWayBillNo(Map<String, Object> condition) {
		// TODO Auto-generated method stub
		return this.selectList("com.jiuyescm.bms.biz.storage.mapper.BizOutstockPackmaterialMapper.queryWayBillNo", condition);
	}

	@Override
	public Double getMaxVolum(Map<String, Object> condition) {
		// TODO Auto-generated method stub
		Double v=(Double) selectOne("com.jiuyescm.bms.biz.storage.mapper.BizOutstockPackmaterialMapper.getMaxVolum", condition);
		return v;
	}

	@Override
	public int deleteAllByWayBillNo(List<String> waybillNoList) {
		Map<String,Object> map=Maps.newHashMap();
		map.put("waybillNoList", waybillNoList);
		return this.delete("com.jiuyescm.bms.biz.storage.mapper.BizOutstockPackmaterialMapper.deleteAllByWayBillNo", map);
	}

}
