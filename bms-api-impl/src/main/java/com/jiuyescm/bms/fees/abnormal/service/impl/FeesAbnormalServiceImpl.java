/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.fees.abnormal.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.bill.receive.repository.IBmsBillSubjectInfoRepository;
import com.jiuyescm.bms.common.enumtype.BillFeesSubjectAbormalEnum;
import com.jiuyescm.bms.common.enumtype.BillFeesSubjectStatusEnum;
import com.jiuyescm.bms.fees.abnormal.entity.FeesAbnormalEntity;
import com.jiuyescm.bms.fees.abnormal.repository.IFeesAbnormalRepository;
import com.jiuyescm.bms.fees.abnormal.service.IFeesAbnormalService;
import com.jiuyescm.bms.fees.dispatch.repository.IFeesReceiveDispatchRepository;
import com.jiuyescm.bms.fees.out.dispatch.entity.FeesPayDispatchEntity;
import com.jiuyescm.bms.fees.out.dispatch.service.IFeesPayDispatchService;
import com.jiuyescm.bms.fees.storage.repository.IFeesReceiveStorageRepository;
import com.jiuyescm.exception.BizException;

/**
 * 
 * @author stevenl
 * 
 */
@Service("feesAbnormalService")
public class FeesAbnormalServiceImpl implements IFeesAbnormalService {

	private static final Logger logger = Logger.getLogger(FeesAbnormalServiceImpl.class.getName());
	
	@Autowired
    private IFeesAbnormalRepository feesAbnormalRepository;
	
	@Autowired
	private IFeesPayDispatchService  payDispatchService;
	
	@Autowired
    private IBmsBillSubjectInfoRepository bmsBillSubjectInfoRepository;
	@Autowired
    private IFeesReceiveStorageRepository feesReceiveStorageRepository;
	@Autowired
	private IFeesReceiveDispatchRepository feeInDistributionRepository;

    @Override
    public PageInfo<FeesAbnormalEntity> query(Map<String, Object> condition,
            int pageNo, int pageSize) {
        return feesAbnormalRepository.query(condition, pageNo, pageSize);
    }
    
    @Override
    public List<FeesAbnormalEntity> queryByFeesNos(Map<String, Object> condition) {
    	return feesAbnormalRepository.queryByFeesNos(condition);
    }

    @Override
    public FeesAbnormalEntity findById(Long id) {
        return feesAbnormalRepository.findById(id);
    }

    @Override
    public FeesAbnormalEntity save(FeesAbnormalEntity entity) {
        return feesAbnormalRepository.save(entity);
    }

    @Override
    public int update(FeesAbnormalEntity entity) {
        return feesAbnormalRepository.update(entity);
    }

    @Override
    public void delete(Long id) {
        feesAbnormalRepository.delete(id);
    }
    
	@Override
	public void handlFeesAbnormal(Map<String, Object> param) {
		List<FeesAbnormalEntity> list = feesAbnormalRepository.queryCountByFeesPayBillInfoData(param);
		Map<String,Object> condition = new HashMap<String,Object>();
		try {
			for(FeesAbnormalEntity entity:list)
			{
				if("0".equals(entity.getIsDeliveryFree()))
				{//0是免运费
					condition.put("waybillNo", entity.getExpressnum());
					FeesPayDispatchEntity feesEntity = payDispatchService.queryOne(condition);
					//feesEntity.getAmount() 代表运费
					Double amount = feesEntity.getAmount();
					entity.setDeliveryCost(amount);
					
					feesAbnormalRepository.update(entity);
					
				}
			}
		} catch (Exception e) {
			logger.error(e);
		}
		
	}

	/**
	 * 批量剔除费用-宅配
	 */
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
	public void deleteDispatchBatchFees(List<FeesAbnormalEntity> list) throws Exception {
		if(null != list && list.size() > 0){
			FeesAbnormalEntity entity=list.get(0);
			//批量更新费用表减免费用
			feesAbnormalRepository.deleteBatchFees(list);
			String status=BillFeesSubjectStatusEnum.UPDATE.getCode();
			String subjectCode="";
			if(entity.getReasonId()==4){//改地址和退件费
				subjectCode=BillFeesSubjectAbormalEnum.Abnormal_DisChange.getCode();
			}else if(entity.getReasonId()==2){
				subjectCode=BillFeesSubjectAbormalEnum.Abnormal_Dispatch.getCode();
			}else{
				throw new Exception("未知的异常原因【"+entity.getReasonId()+"】");
			}
			
			List<String> feeNos = new ArrayList<String>();
			for(FeesAbnormalEntity fee:list){
				feeNos.add(fee.getFeeNo());
			}
			// 从仓储费用表中删除理赔费用
			Map<String, Object> condition = new HashMap<String, Object>();
			condition.put("billNo", entity.getBillNo());
			condition.put("feeNos", feeNos);
			
			int delNum = feeInDistributionRepository.deleteFeesBillAbnormal(condition);
			if (delNum != list.size()) {
				throw new BizException("删除配送理赔费用失败!");
			}
			
			//更新账单明细状态为 已更新
			bmsBillSubjectInfoRepository.updateStatus(entity.getBillNo(),entity.getWarehouseId(),subjectCode,status);
		}
		
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
	public void derateDispatchBatchAmount(List<FeesAbnormalEntity> list) {
		if(list!=null&&list.size()>0){
			FeesAbnormalEntity entity=list.get(0);
			//批量更新费用表减免费用
			feesAbnormalRepository.derateBatchAmount(list);
			String status=BillFeesSubjectStatusEnum.UPDATE.getCode();
			//更新账单明细状态为 已更新
			bmsBillSubjectInfoRepository.updateStatus(entity.getBillNo(),entity.getWarehouseId(),BillFeesSubjectAbormalEnum.Abnormal_Dispatch.getCode(),status);
		}
		
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
	public void derateStorageBatchAmount(List<FeesAbnormalEntity> list) {
		if(list!=null&&list.size()>0){
			FeesAbnormalEntity entity=list.get(0);
			//批量更新费用表减免费用
			feesAbnormalRepository.derateBatchAmount(list);
			String status=BillFeesSubjectStatusEnum.UPDATE.getCode();
			//更新账单明细状态为 已更新
			bmsBillSubjectInfoRepository.updateStatus(entity.getBillNo(),entity.getWarehouseId(),BillFeesSubjectAbormalEnum.Abnormal_Stroage.getCode(),status);
		}
		
	}

	/**
	 * 批量剔除费用-仓储
	 */
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
	public void deleteStorageBatchFees(List<FeesAbnormalEntity> list) {
		if(null != list && list.size() > 0){
			FeesAbnormalEntity entity = list.get(0);
			//批量更新费用表减免费用
			feesAbnormalRepository.deleteBatchFees(list);
			String status = BillFeesSubjectStatusEnum.UPDATE.getCode();
			
			List<String> feeNos = new ArrayList<String>();
			for(FeesAbnormalEntity fee:list){
				feeNos.add(fee.getFeeNo());
			}
			
			// 从仓储费用表中删除理赔费用
			Map<String, Object> condition = new HashMap<String, Object>();
			condition.put("billNo", entity.getBillNo());
			condition.put("feeNos", feeNos);
			int delNum = feesReceiveStorageRepository.deleteFeesBillAbnormal(condition);
			if (delNum != list.size()) {
				throw new BizException("删除仓储理赔费用失败!");
			}
			
			//更新账单明细状态为 已更新
			bmsBillSubjectInfoRepository.updateStatus(entity.getBillNo(),entity.getWarehouseId(),BillFeesSubjectAbormalEnum.Abnormal_Stroage.getCode(),status);
		}
		
	}

	@Override
	public int updateBatchBillNo(List<FeesAbnormalEntity> list) {
		return feesAbnormalRepository.updateBatchBillNo(list);
	}

	@Override
	public void deleteAbnormalBill(String billNo, String warehouseCode,
			String subjectCode) throws Exception {
		feesAbnormalRepository.deleteAbnormalBill(billNo,warehouseCode,subjectCode);
		
	}

	@Override
	public FeesAbnormalEntity sumDispatchAmount(String billNo) {
		return feesAbnormalRepository.sumDispatchAmount(billNo);
	}

	@Override
	public FeesAbnormalEntity sumDispatchChangeAmount(String billNo) {
		return feesAbnormalRepository.sumDispatchChangeAmount(billNo);
	}

	@Override
	public PageInfo<FeesAbnormalEntity> queryPreBillAbnormal(
			Map<String, Object> condition, int pageNo, int pageSize) {
		return feesAbnormalRepository.queryPreBillAbnormal(condition, pageNo, pageSize);
	}

	@Override
	public PageInfo<FeesAbnormalEntity> queryPreBillAbnormalChange(
			Map<String, Object> condition, int pageNo, int pageSize) {
		return feesAbnormalRepository.queryPreBillAbnormalChange(condition, pageNo, pageSize);
	}
	
}
