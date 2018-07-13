/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.fees.abnormal.service.impl;

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
import com.jiuyescm.bms.fees.abnormal.entity.FeesPayAbnormalEntity;
import com.jiuyescm.bms.fees.abnormal.repository.IFeesPayAbnormalRepository;
import com.jiuyescm.bms.fees.abnormal.service.IFeesPayAbnormalService;
import com.jiuyescm.bms.fees.out.dispatch.entity.FeesPayDispatchEntity;
import com.jiuyescm.bms.fees.out.dispatch.service.IFeesPayDispatchService;

/**
 * 
 * @author stevenl
 * 
 */
@Service("feesPayAbnormalService")
public class FeesPayAbnormalServiceImpl implements IFeesPayAbnormalService {

	private static final Logger logger = Logger.getLogger(FeesPayAbnormalServiceImpl.class.getName());
	
	@Autowired
    private IFeesPayAbnormalRepository feesAbnormalRepository;
	
	@Autowired
	private IFeesPayDispatchService  payDispatchService;
	
	@Autowired
    private IBmsBillSubjectInfoRepository bmsBillSubjectInfoRepository;

    @Override
    public PageInfo<FeesPayAbnormalEntity> query(Map<String, Object> condition,
            int pageNo, int pageSize) {
        return feesAbnormalRepository.query(condition, pageNo, pageSize);
    }

    @Override
    public FeesPayAbnormalEntity findById(Long id) {
        return feesAbnormalRepository.findById(id);
    }

    @Override
    public FeesPayAbnormalEntity save(FeesPayAbnormalEntity entity) {
        return feesAbnormalRepository.save(entity);
    }

    @Override
    public int update(FeesPayAbnormalEntity entity) {
        return feesAbnormalRepository.update(entity);
    }

    @Override
    public void delete(Long id) {
        feesAbnormalRepository.delete(id);
    }
    
	@Override
	public void handlFeesAbnormal(Map<String, Object> param) {
		List<FeesPayAbnormalEntity> list = feesAbnormalRepository.queryCountByFeesPayBillInfoData(param);
		Map<String,Object> condition = new HashMap<String,Object>();
		try {
			for(FeesPayAbnormalEntity entity:list)
			{
				if("0".equals(entity.getIsDeliveryFree()))
				{//0是免运费
					condition.put("waybillNo", entity.getExpressnum());
					FeesPayDispatchEntity feesEntity = payDispatchService.queryOne(condition);
					//feesEntity.getAmount() 代表运费
					if(null != feesEntity){
						Double amount = (null == feesEntity.getAmount())? 0 : feesEntity.getAmount();
						entity.setDeliveryCost(amount);
					}
					feesAbnormalRepository.update(entity);					
				}
			}
		} catch (Exception e) {
			logger.error(e);
		}
		
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
	public void deleteDispatchBatchFees(List<FeesPayAbnormalEntity> list) {
		if(list!=null&&list.size()>0){
			FeesPayAbnormalEntity entity=list.get(0);
			//批量更新费用表减免费用
			feesAbnormalRepository.deleteBatchFees(list);
			String status=BillFeesSubjectStatusEnum.UPDATE.getCode();
			//更新账单明细状态为 已更新
			bmsBillSubjectInfoRepository.updateStatus(entity.getBillNo(),entity.getWarehouseId(),BillFeesSubjectAbormalEnum.Abnormal_Dispatch.getCode(),status);
		}
		
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
	public void derateDispatchBatchAmount(List<FeesPayAbnormalEntity> list) {
		if(list!=null&&list.size()>0){
			FeesPayAbnormalEntity entity=list.get(0);
			//批量更新费用表减免费用
			feesAbnormalRepository.derateBatchAmount(list);
			String status=BillFeesSubjectStatusEnum.UPDATE.getCode();
			//更新账单明细状态为 已更新
			bmsBillSubjectInfoRepository.updateStatus(entity.getBillNo(),entity.getWarehouseId(),BillFeesSubjectAbormalEnum.Abnormal_Dispatch.getCode(),status);
		}
		
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
	public void derateStorageBatchAmount(List<FeesPayAbnormalEntity> list) {
		if(list!=null&&list.size()>0){
			FeesPayAbnormalEntity entity=list.get(0);
			//批量更新费用表减免费用
			feesAbnormalRepository.derateBatchAmount(list);
			String status=BillFeesSubjectStatusEnum.UPDATE.getCode();
			//更新账单明细状态为 已更新
			bmsBillSubjectInfoRepository.updateStatus(entity.getBillNo(),entity.getWarehouseId(),BillFeesSubjectAbormalEnum.Abnormal_Stroage.getCode(),status);
		}
		
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
	public void deleteStorageBatchFees(List<FeesPayAbnormalEntity> list) {
		if(list!=null&&list.size()>0){
			FeesPayAbnormalEntity entity=list.get(0);
			//批量更新费用表减免费用
			feesAbnormalRepository.deleteBatchFees(list);
			String status=BillFeesSubjectStatusEnum.UPDATE.getCode();
			
			double totlederateAmount=0.00;
			for(FeesPayAbnormalEntity fee:list){
				if(fee.getDerateAmount()!=null){
					totlederateAmount+=fee.getDerateAmount();
				}
			}
			Map<String, Object> map=new HashMap<String, Object>();
			
			//更新账单明细状态为 已更新
			bmsBillSubjectInfoRepository.updateStatus(entity.getBillNo(),entity.getWarehouseId(),BillFeesSubjectAbormalEnum.Abnormal_Stroage.getCode(),status);
		}
		
	}

	@Override
	public int updateBatchBillNo(List<FeesPayAbnormalEntity> list) {
		// TODO Auto-generated method stub
		return feesAbnormalRepository.updateBatchBillNo(list);
	}

	@Override
	public List<FeesPayAbnormalEntity> queryFeeByParam(Map<String, Object> param) {
		// TODO Auto-generated method stub
		return feesAbnormalRepository.queryFeeByParam(param);
	}
	
}
