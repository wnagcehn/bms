/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.fees.storage.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.bill.receive.repository.IBmsBillSubjectInfoRepository;
import com.jiuyescm.bms.biz.storage.entity.BizInStockMasterEntity;
import com.jiuyescm.bms.biz.storage.entity.BizOutstockMasterEntity;
import com.jiuyescm.bms.biz.storage.entity.BizOutstockPackmaterialEntity;
import com.jiuyescm.bms.biz.storage.entity.BizPackStorageEntity;
import com.jiuyescm.bms.biz.storage.entity.BizProductPalletStorageEntity;
import com.jiuyescm.bms.biz.storage.entity.BizProductStorageEntity;
import com.jiuyescm.bms.biz.storage.entity.ReturnData;
import com.jiuyescm.bms.biz.storage.repository.IBizInStockMasterRepository;
import com.jiuyescm.bms.biz.storage.repository.IBizOutstockMasterRepository;
import com.jiuyescm.bms.biz.storage.repository.IBizOutstockPackmaterialRepository;
import com.jiuyescm.bms.biz.storage.repository.IBizPackStorageRepository;
import com.jiuyescm.bms.biz.storage.repository.IBizProductPalletStorageRepository;
import com.jiuyescm.bms.biz.storage.repository.IBizProductStorageRepository;
import com.jiuyescm.bms.common.enumtype.BillFeesSubjectStatusEnum;
import com.jiuyescm.bms.common.log.service.IBmsErrorLogInfoService;
import com.jiuyescm.bms.fees.storage.entity.FeesReceiveStorageEntity;
import com.jiuyescm.bms.fees.storage.repository.IFeesReceiveStorageRepository;
import com.jiuyescm.bms.fees.storage.service.IFeesReceiveStorageService;
import com.jiuyescm.cfm.common.JAppContext;

/**
 * 
 * @author stevenl
 * 
 */
@Service("feesReceiveStorageService")
public class FeesReceiveStorageServiceImpl implements IFeesReceiveStorageService {

	private static final Logger logger = Logger.getLogger(FeesReceiveStorageServiceImpl.class.getName());
	
	@Autowired
    private IFeesReceiveStorageRepository feesReceiveStorageRepository;
	
	@Autowired
	private IBizOutstockMasterRepository  outStockMasterRepository;
	
	@Autowired
	private IBizInStockMasterRepository inStockMasterRepository;
	
	@Autowired
	private  IBizProductStorageRepository  productStorageRepository;
	
	@Autowired
    private IBizProductPalletStorageRepository bizProductPalletStorageRepository;
	
	@Autowired
    private IBizPackStorageRepository bizPackStorageRepository;
	
	@Autowired
	private  IBizOutstockPackmaterialRepository  outStockPackMaterial;
		
	@Autowired
    private IBmsBillSubjectInfoRepository bmsBillSubjectInfoRepository;
	
	@Resource
	private IBmsErrorLogInfoService bmsErrorLogInfoService;
	
    @Override
    public PageInfo<FeesReceiveStorageEntity> query(Map<String, Object> condition,
            int pageNo, int pageSize) {
        return feesReceiveStorageRepository.query(condition, pageNo, pageSize);
    }

    @Override
    public FeesReceiveStorageEntity findById(Long id) {
        return feesReceiveStorageRepository.findById(id);
    }

    @Override
    public FeesReceiveStorageEntity save(FeesReceiveStorageEntity entity) {
        return feesReceiveStorageRepository.save(entity);
    }

    @Override
    public FeesReceiveStorageEntity update(FeesReceiveStorageEntity entity) {
        return feesReceiveStorageRepository.update(entity);
    }

    @Override
    public void delete(Long id) {
        feesReceiveStorageRepository.delete(id);
    }

	@Override
	public List<FeesReceiveStorageEntity> queryAll(Map<String, Object> parameter) {
		return feesReceiveStorageRepository.queryAll(parameter);
	}

	@Override
	public String getUnitPrice(Map<String, Object> param) {
		return feesReceiveStorageRepository.getUnitPrice(param);
	}
	
	

	
	@Override
	public ReturnData reCount(List<FeesReceiveStorageEntity> list) {
		
		ReturnData result = new ReturnData();
		
		Map<String,String> map = new HashMap<String,String>();
		
		result.setCode("SUCCESS");
				
		for(FeesReceiveStorageEntity entity:list)
		{
			if("1".equals(entity.getStatus()))
			{  //1代表已过账
				result.setCode("fail");
				map.put("msg", "有未过账记录不能批量重算");
				result.setData(map);
				return result;
			}
			
		}
				
		try {
			for(FeesReceiveStorageEntity entity:list){
				
				if("wh_instock_service".equals(entity.getSubjectCode())||"wh_return_storage".equals(entity.getSubjectCode())){ //入仓费和退货费 
					BizInStockMasterEntity inStock = new BizInStockMasterEntity();
					inStock.setId(Long.valueOf(entity.getBizId()));
					inStock.setIsCalculated("0");
					inStock.setLastModifier(JAppContext.currentUserName());
					inStock.setLastModifyTime(JAppContext.currentTimestamp());
					inStockMasterRepository.update(inStock);
				}else if("wh_product_out".equals(entity.getSubjectCode())||"wh_b2c_work".equals(entity.getSubjectCode())||"wh_b2b_work".equals(entity.getSubjectCode())){ //退仓费和订单操作费 和B2B出库费
					BizOutstockMasterEntity outStock = new BizOutstockMasterEntity();
					outStock.setId(Long.valueOf(entity.getBizId()));
					outStock.setIsCalculated("0");
					outStock.setLastModifier(JAppContext.currentUserName());
					outStock.setLastModifyTime(JAppContext.currentTimestamp());
					outStockMasterRepository.update(outStock);
				}else if("wh_product_storage".equals(entity.getSubjectCode())){//商品存储费 按件
					BizProductStorageEntity  productStorage = new BizProductStorageEntity();
					productStorage.setId(Long.valueOf(entity.getBizId()));
					productStorage.setIsCalculated("0");
					productStorage.setLastModifier(JAppContext.currentUserName());
					productStorage.setLastModifyTime(JAppContext.currentTimestamp());
					productStorageRepository.update(productStorage);
				}else if("wh_product_pallet_storage".equals(entity.getSubjectCode())){//商品存储费 按托
					BizProductPalletStorageEntity palletData = new BizProductPalletStorageEntity();
					palletData.setId(Long.valueOf(entity.getBizId()));
					palletData.setIsCalculated("0");
					palletData.setLastModifier(JAppContext.currentUserName());
					palletData.setLastModifyTime(JAppContext.currentTimestamp());
					bizProductPalletStorageRepository.update(palletData);
				}else if("wh_material_storage".equals(entity.getSubjectCode())){//耗材存储费
					BizPackStorageEntity packStorage = new  BizPackStorageEntity();
					packStorage.setId(Long.valueOf(entity.getBizId()));
					packStorage.setIsCalculated("0");
					packStorage.setLastModifier(JAppContext.currentUserName());
					packStorage.setLastModifyTime(JAppContext.currentTimestamp());
					bizPackStorageRepository.update(packStorage);
				}else if("wh_material_use".equals(entity.getSubjectCode())){
					BizOutstockPackmaterialEntity  outStockPack = new BizOutstockPackmaterialEntity();
					outStockPack.setId(Long.valueOf(entity.getBizId()));
					outStockPack.setIsCalculated("0");
					outStockPack.setLastModifier(JAppContext.currentUserName());
					outStockPack.setLastModifyTime(JAppContext.currentTimestamp());
					outStockPackMaterial.update(outStockPack);
				}
				
				
			}
		} catch (Exception e) {
			logger.error(e);
			result.setCode("fail");
			map.put("msg", "重算失败");
			result.setData(map);
			//写入日志
			bmsErrorLogInfoService.insertLog(this.getClass().getSimpleName(),Thread.currentThread().getStackTrace()[1].getMethodName(), "", e.toString());

		}
		
		return result;
		
	}

	@Override
	public int insertBatchTmp(List<FeesReceiveStorageEntity> list) {
		
		int i = 0;
		
		try {
			feesReceiveStorageRepository.insertBatch(list);
			i = 1;
		} catch (Exception e) {
			//写入日志
			bmsErrorLogInfoService.insertLog(this.getClass().getSimpleName(),Thread.currentThread().getStackTrace()[1].getMethodName(), "", e.toString());

			logger.error(e);
		}
		
		return i;
	}

	@Override
	public PageInfo<FeesReceiveStorageEntity> queryFees(
			Map<String, Object> condition, int pageNo, int pageSize) {
		// TODO Auto-generated method stub
		return feesReceiveStorageRepository.queryFees(condition, pageNo, pageSize);
	}


	@Override
	public int deleteEntity(String feesNo) {
		return feesReceiveStorageRepository.deleteEntity(feesNo);
	}

	@Override
	public PageInfo<FeesReceiveStorageEntity> queryProductStoragePage(
			Map<String, Object> parameter, int pageNo, int pageSize) {
		
		return feesReceiveStorageRepository.queryStoragePage(parameter,pageNo,pageSize);
	}

	@Override
	public PageInfo<FeesReceiveStorageEntity> queryMaterialPage(
			Map<String, Object> parameter, int pageNo, int pageSize) {
		return feesReceiveStorageRepository.queryMaterialPage(parameter,pageNo,pageSize);
	}

	@Override
	public PageInfo<FeesReceiveStorageEntity> queryStorageAddFeePage(
			Map<String, Object> parameter, int pageNo, int pageSize) {
		return feesReceiveStorageRepository.queryStorageAddFeePage(parameter, pageNo, pageSize);
	}
	
	@Override
	public PageInfo<FeesReceiveStorageEntity> queryPreBillStorageAddFee(
			Map<String, Object> parameter, int pageNo, int pageSize) {
		return feesReceiveStorageRepository.queryPreBillStorageAddFee(parameter, pageNo, pageSize);
	}

	
	public int updateBatch(List<FeesReceiveStorageEntity> list){
		return feesReceiveStorageRepository.updateBatch(list);
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
	public void derateBatchAmount(List<FeesReceiveStorageEntity> list) {
		if(list!=null&&list.size()>0){
			FeesReceiveStorageEntity entity=list.get(0);
			//批量更新费用表减免费用
			feesReceiveStorageRepository.derateBatchAmount(list);
			String status=BillFeesSubjectStatusEnum.UPDATE.getCode();
			//更新账单明细状态为 已更新
			bmsBillSubjectInfoRepository.updateStatus(entity.getBillNo(),entity.getWarehouseCode(),entity.getSubjectCode(),status);
		}
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
	public void deleteBatchFees(List<FeesReceiveStorageEntity> list) {
		if(list!=null&&list.size()>0){
			FeesReceiveStorageEntity entity=list.get(0);
			//批量更新费用表减免费用
			feesReceiveStorageRepository.deleteBatchFees(list);
			String status=BillFeesSubjectStatusEnum.UPDATE.getCode();
			//更新账单明细状态为 已更新
			bmsBillSubjectInfoRepository.updateStatus(entity.getBillNo(),entity.getWarehouseCode(),entity.getSubjectCode(),status);
		}
		
	}

	/**
	 * jira BMS-187 wuliangfeng
	 * 分组统计应收仓储费用
	 */
	@Override
	public List<Map<String, Object>> queryGroup(Map<String, Object> param) {
		return feesReceiveStorageRepository.queryGroup(param);
	}

	@Override
	public int updateByFeeNoList(Map<String, Object> condition) {
		return feesReceiveStorageRepository.updateByFeeNoList(condition);
	}

	@Override
	public List<FeesReceiveStorageEntity> queryFeesData(
			Map<String, Object> condition) {
		return feesReceiveStorageRepository.queryBizFeesData(condition);
	}

	@Override
	public int deleteBatch(Map<String, Object> feesNos) {
		return feesReceiveStorageRepository.deleteBatch(feesNos);
	}

	@Override
	public List<String> queryPreBillWarehouse(Map<String, Object> param) {
		return feesReceiveStorageRepository.queryPreBillWarehouse(param);
	}

	@Override
	public List<FeesReceiveStorageEntity> queryPreBillStorage(Map<String, Object> condition) {
		return feesReceiveStorageRepository.queryPreBillStorage(condition);
	}
	
	@Override
	public List<FeesReceiveStorageEntity> queryPreBillStorageByItems(Map<String, Object> condition) {
		return feesReceiveStorageRepository.queryPreBillStorageByItems(condition);
	}
	
	@Override
	public List<FeesReceiveStorageEntity> queryPreBillPallet(Map<String, Object> condition) {
		return feesReceiveStorageRepository.queryPreBillPallet(condition);
	}
	
	@Override
	public List<FeesReceiveStorageEntity> queryPreBillMaterialStorage(Map<String, Object> condition) {
		return feesReceiveStorageRepository.queryPreBillMaterialStorage(condition);
	}
	
	@Override
	public List<FeesReceiveStorageEntity> queryByFeesNo(String FeesNo){
		return feesReceiveStorageRepository.queryByFeesNo(FeesNo);
	}

	@Override
	public PageInfo<FeesReceiveStorageEntity> queryOutStockPage(
			Map<String, Object> parameter, int pageNo, int pageSize) {
		// TODO Auto-generated method stub
		return feesReceiveStorageRepository.queryOutStockPage(parameter, pageNo, pageSize);
	}

}
