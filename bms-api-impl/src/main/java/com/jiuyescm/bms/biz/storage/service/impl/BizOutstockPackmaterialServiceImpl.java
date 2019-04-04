package com.jiuyescm.bms.biz.storage.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.biz.storage.entity.BizOutstockPackmaterialEntity;
import com.jiuyescm.bms.biz.storage.repository.IBizOutstockPackmaterialRepository;
import com.jiuyescm.bms.biz.storage.service.IBizOutstockPackmaterialService;
import com.jiuyescm.bms.fees.storage.repository.IFeesReceiveStorageRepository;
import com.jiuyescm.bms.fees.storage.vo.FeesReceiveMaterial;

@Service("bizOutstockPackmaterialService")
public class BizOutstockPackmaterialServiceImpl implements IBizOutstockPackmaterialService{
	private static final Logger logger = Logger.getLogger(BizOutstockPackmaterialServiceImpl.class.getName());

	@Autowired
	private IBizOutstockPackmaterialRepository repository;
	
	@Autowired
	private IFeesReceiveStorageRepository feesReceiveStorageRepository;
		
	@Override
	public PageInfo<BizOutstockPackmaterialEntity> query(Map<String, Object> condition, int pageNo, int pageSize) {
		return repository.query(condition, pageNo, pageSize);
	}

	@Override
	public int update(BizOutstockPackmaterialEntity entity) {
		int result=0;
		Map<String, Object> map = new HashMap<String, Object>();
    	map.put("feesNo", entity.getFeesNo());
    	try {
    		result=repository.update(entity);
        	feesReceiveStorageRepository.updateIsCalcuByFeesNo(map);
		} catch (Exception e) {
			logger.error("更新异常!", e);
			return result;
		}
    	return result;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
	public int saveList(List<BizOutstockPackmaterialEntity> list) {
		return repository.saveList(list);
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
	public int updateList(List<BizOutstockPackmaterialEntity> list) {
		int result=repository.updateList(list);
		if(result>0){
			List<String> feesNos = new ArrayList<String>();
			Map<String, Object> feesMap = new HashMap<String, Object>();
			for(BizOutstockPackmaterialEntity entity:list){
				feesNos.add(entity.getFeesNo());
			}
			feesMap.put("feesNos", feesNos);
			feesReceiveStorageRepository.deleteBatch(feesMap);
		}	
		return result;
	}
	
	@Override
	public List<BizOutstockPackmaterialEntity> getBizstockPack(Map<String, Object> condition) {
		return repository.getBizstockPack(condition);
	}

	@Override
	public PageInfo<BizOutstockPackmaterialEntity> queryWarehouseGroupCount(
			Map<String, Object> condition, int pageNo, int pageSize) {
		return repository.queryWarehouseGroupCount(condition, pageNo, pageSize);
	}
	
	@Override
	public PageInfo<BizOutstockPackmaterialEntity> queryErrorCal(
			Map<String, Object> condition, int pageNo, int pageSize) {
		return repository.queryErrorCal(condition, pageNo, pageSize);
	}

	@Override
	public PageInfo<BizOutstockPackmaterialEntity> queryPriceGroupCount(
			Map<String, Object> condition, int pageNo, int pageSize) {
		PageInfo<BizOutstockPackmaterialEntity> pageInfo = repository.queryPriceGroupCount(condition, pageNo, pageSize);
		List<BizOutstockPackmaterialEntity> list = pageInfo.getList();
		
		for(BizOutstockPackmaterialEntity entity:list)
		{
			 if(StringUtils.isBlank(entity.getUnitPrice())){
				 entity.setUnitPrice("报价缺失");
			 }
			
		}
		return pageInfo;
	}

	@Override
	public Properties validRetry(Map<String, Object> param) {
		return repository.validRetry(param);
	}

	@Override
	public int reCalculate(Map<String, Object> param) {
		return repository.reCalculate(param);
	}

	@Override
	public int deleteList(List<BizOutstockPackmaterialEntity> list) {
		return repository.deleteList(list);
	}

	@Override
	public List<BizOutstockPackmaterialEntity> queryList(
			Map<String, Object> condition) {
		return repository.queryList(condition);
	}

	@Override
	public int deleteFees(Map<String, Object> condition) {
		return  repository.deleteFees(condition);
	}

	@Override
	public PageInfo<Map<String, String>> queryByMonth(
			Map<String, Object> condition, int pageNo, int pageSize) {
		return repository.queryByMonth(condition, pageNo, pageSize);
	}

	@Override
	public PageInfo<Map<String, String>> queryCustomeridF(
			Map<String, Object> condition, int pageNo, int pageSize) {
		Object startTime = condition.get("startTime");
		Object endTime = condition.get("endTime");
		
		Calendar stcalendar = Calendar.getInstance();//日历对象
		Calendar encalendar = Calendar.getInstance();//日历对象
			
		Date stTime = (Date)startTime;
		Date enTime = (Date)endTime;
		stcalendar.setTime(stTime);
		encalendar.setTime(enTime);
		
		stcalendar.add(Calendar.MONTH, -1);
		encalendar.add(Calendar.MONTH, -1);
		
		condition.put("bstartTime", stcalendar.getTime());
		condition.put("bendTime", encalendar.getTime());
		
		return repository.queryCustomeridF(condition, pageNo, pageSize);
	}
	
	@Override
	public BizOutstockPackmaterialEntity queryExceptionOne(
			Map<String, Object> condition) {
		return repository.queryExceptionOne(condition);
	}

	@Override
	public List<Map<String, String>> getMaterialCode(Map<String, Object> param) {
		return repository.getMaterialCode(param);
	}

	@Override
	public List<BizOutstockPackmaterialEntity> getOrgPackMaterialList(
			Map<String, Object> querymap) {
		return repository.getOrgPackMaterialList(querymap);
	}

	@Override
	public List<BizOutstockPackmaterialEntity> queryAllByWaybillNoList(
			List<String> waybillNoList) {
		
		return repository.queryAllByWaybillNoList(waybillNoList);
	}

	@Override
	public int updateCostIsCalculated(Map<String, Object> param,
			String isCalculated) {
		return repository.updateCostIsCalculated(param,isCalculated);
	}

	@Override
	public List<String> queryCosthasBill(Map<String, Object> param) {
		return repository.queryCosthasBill(param);
	}

	@Override
	public int saveDataFromTemp(String batchNum) {
		return repository.saveDataFromTemp(batchNum);
	}

	@Override
	public boolean checkDataExist(String waybillNoExcel, String materialCode) {
		return repository.checkDataExist(waybillNoExcel,materialCode);
	}

	@Override
	public List<BizOutstockPackmaterialEntity> getMaterialCodeFromBizData(
			Map<String, Object> condition) {
		
		return repository.getMaterialCodeFromBizData(condition);
	}
	
	@Override
	public List<BizOutstockPackmaterialEntity> queryOriginMaterialFromBizData(
			Map<String, Object> condition) {
		return repository.queryOriginMaterialFromBizData(condition);
	}

	@Override
	public PageInfo<FeesReceiveMaterial> queryMaterialFromBizData(
			Map<String, Object> condition, int pageNo, int size) {
		return repository.queryMaterialFromBizData(condition,pageNo,size);
	}

	@Override
	public List<BizOutstockPackmaterialEntity> queryAllWarehouseFromBizData(
			Map<String, Object> condition) {
		return repository.queryAllWarehouseFromBizData(condition);
	}

	@Override
	public List<BizOutstockPackmaterialEntity> queryMaterial(
			Map<String, Object> condition) {
		return repository.queryMaterial(condition);
	}

	@Override
	public List<String> queryWayBillNo(Map<String, Object> condition) {
		return repository.queryWayBillNo(condition);
	}

	@Override
	public Double getMaxVolum(Map<String, Object> condition) {
		return repository.getMaxVolum(condition);
	}

	@Override
	public Double getMaxPmxVolum(Map<String, Object> condition) {
		// TODO Auto-generated method stub
		return repository.getMaxPmxVolum(condition);
	}

	@Override
	public Double getMaxZxVolum(Map<String, Object> condition) {
		// TODO Auto-generated method stub
		return repository.getMaxZxVolum(condition);
	}
	
	@Override
	public int deleteAllByWayBillNo(List<String> waybillNoList) {
		return repository.deleteAllByWayBillNo(waybillNoList);
	}

	@Override
	public String getMaxBwdVolumn(Map<String, Object> condition) {
		// TODO Auto-generated method stub
		return repository.getMaxBwdVolumn(condition);
	}

	@Override
	public int deleteOldMaterial(Map<String,Object> condition) {
		// TODO Auto-generated method stub
		//根据运单号查出所有的费用编号
		List<String> materialList=new ArrayList<String>();
		materialList.add("泡沫箱");
		materialList.add("纸箱");
		condition.put("materialList", materialList);
		List<String> feeNos=repository.queryFeeNo(condition);
		if(feeNos.size()>0){
			condition.put("feeNos", feeNos);
			int result=repository.deleteOldMaterial(condition);
			logger.info("删除泡沫箱纸箱"+JSONObject.fromObject(condition));
			if(result>0){
				feesReceiveStorageRepository.deleteMaterialFee(condition);
				logger.info("删除泡沫箱费用纸箱"+JSONObject.fromObject(condition));
			}
			return result;
		}else{
			logger.info("未查询到费用编号"+JSONObject.fromObject(condition));
			return 0;
		}
		
	}

	@Override
	public int deleteOldBwd(Map<String, Object> condition) {
		// TODO Auto-generated method stub
		//根据运单号查出所有的费用编号
		List<String> materialList=new ArrayList<String>();
		materialList.add("保温袋");
		condition.put("materialList", materialList);
		List<String> feeNos=repository.queryFeeNo(condition);
		if(feeNos.size()>0){
			condition.put("feeNos", feeNos);
			logger.info("删除保温袋"+JSONObject.fromObject(condition));
			int result=repository.deleteOldBwd(condition);
			/*if(result>0){
				feesReceiveStorageRepository.deleteBwdFee(condition);
				logger.info("删除保温袋费用"+JSONObject.fromObject(condition));

			}*/
			return result;
		}else{
			logger.info("未查询到费用编号"+JSONObject.fromObject(condition));
			return 0;
		}
		
	}

	@Override
	public int deleteOldPmx(Map<String, Object> condition) {
		// TODO Auto-generated method stub
		//根据运单号查出所有的费用编号
		List<String> materialList=new ArrayList<String>();
		materialList.add("泡沫箱");
		condition.put("materialList", materialList);
		List<String> feeNos=repository.queryFeeNo(condition);
		if(feeNos.size()>0){
			condition.put("feeNos", feeNos);
			logger.info("删除泡沫箱"+JSONObject.fromObject(condition));
			int result=repository.deleteOldMaterial(condition);
		/*	if(result>0){
				feesReceiveStorageRepository.deleteMaterialFee(condition);
			}*/
			return result;
		}else{
			logger.info("未查询到费用编号"+JSONObject.fromObject(condition));
			return 0;
		}
	}

	@Override
	public int deleteOldZx(Map<String, Object> condition) {
		// TODO Auto-generated method stub
		//根据运单号查出所有的费用编号
		List<String> materialList=new ArrayList<String>();
		materialList.add("纸箱");
		condition.put("materialList", materialList);
		List<String> feeNos=repository.queryFeeNo(condition);
		if(feeNos.size()>0){
			condition.put("feeNos", feeNos);
			logger.info("删除纸箱"+JSONObject.fromObject(condition));
			int result=repository.deleteOldMaterial(condition);
			/*if(result>0){
				feesReceiveStorageRepository.deleteMaterialFee(condition);
			}*/
			return result;
		}else{
			logger.info("未查询到费用编号"+JSONObject.fromObject(condition));
			return 0;
		}
	}

}
