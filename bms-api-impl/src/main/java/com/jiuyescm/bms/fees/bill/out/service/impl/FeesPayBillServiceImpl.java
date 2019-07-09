package com.jiuyescm.bms.fees.bill.out.service.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.bstek.dorado.web.DoradoContext;
import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.common.sequence.repository.SequenceDao;
import com.jiuyescm.bms.fees.abnormal.entity.FeesPayAbnormalEntity;
import com.jiuyescm.bms.fees.abnormal.repository.IFeesPayAbnormalRepository;
import com.jiuyescm.bms.fees.bill.out.entity.FeesPayBillEncapEntity;
import com.jiuyescm.bms.fees.bill.out.entity.FeesPayBillEntity;
import com.jiuyescm.bms.fees.bill.out.service.IFeesPayBillService;
import com.jiuyescm.bms.fees.bill.repository.IFeesPayBillRepository;
import com.jiuyescm.bms.fees.entity.FeesBillEntity;
import com.jiuyescm.bms.fees.entity.FeesBillWareHouseEntity;
import com.jiuyescm.bms.fees.out.dispatch.entity.FeesPayDispatchEntity;
import com.jiuyescm.bms.fees.out.dispatch.repository.IFeesPayDispatchRepository;
import com.jiuyescm.bms.fees.out.transport.entity.FeesPayTransportEntity;
import com.jiuyescm.bms.fees.out.transport.repository.IFeesPayTransportDao;
import com.jiuyescm.bms.pub.deliver.entity.DeliverEntity;
import com.jiuyescm.cfm.common.JAppContext;
import com.jiuyescm.common.ConstantInterface;
import com.jiuyescm.exception.BizException;

/**
 * 应付账单service实现类
 * @author yangshuaishuai
 *
 */
@Service("feesOutBillService")
public class FeesPayBillServiceImpl implements IFeesPayBillService {
	
	@Autowired
    private IFeesPayBillRepository feesPayBillRepository;
	@Autowired
	private IFeesPayTransportDao feesPayTransportDao;
	@Autowired
	private IFeesPayDispatchRepository feesPayDispatchRepository;
	@Autowired
	private IFeesPayAbnormalRepository feesPayAbnormalRepository;
	@Resource
	private SequenceDao sequenceDao;

    @Override
    public PageInfo<FeesPayBillEntity> query(Map<String, Object> condition,
            int pageNo, int pageSize) {
        return feesPayBillRepository.query(condition, pageNo, pageSize);
    }
    
	@Override
	public FeesPayBillEntity queryBillInfo(Map<String, Object> condition) {
		return feesPayBillRepository.queryBillInfo(condition);
	}
	
    @Override
	public List<FeesBillWareHouseEntity> queryGroupTransportAmount(String billNo) {
		return feesPayTransportDao.queryGroupTransportAmount(billNo);
	}

	@Override
	public List<FeesBillWareHouseEntity> queryGroupDispatchAmount(String billNo) {
		return feesPayDispatchRepository.queryGroupDispatchAmount(billNo);
	}

	@Override
	public List<FeesBillWareHouseEntity> queryGroupAbnormalAmount(String billNo) {
		return feesPayAbnormalRepository.querywarehouseAbnormalAmount(billNo);
	}
	
	@Override
	public PageInfo<FeesPayTransportEntity> queryTransportDetailGroupPage(
			FeesBillWareHouseEntity queryEntity, int pageNo, int pageSize) {
		return feesPayTransportDao.queryTransportDetailGroupPage(queryEntity, pageNo, pageSize);
	}

	@Override
	public PageInfo<FeesPayDispatchEntity> queryDispatchDetailGroupPage(
			FeesBillWareHouseEntity queryEntity, int pageNo, int pageSize) {
		return feesPayDispatchRepository.queryDispatchDetailGroupPage(queryEntity, pageNo, pageSize);
	}

	@Override
	public PageInfo<FeesPayAbnormalEntity> queryAbnormalDetailGroupPage(
			FeesBillWareHouseEntity queryEntity, int pageNo, int pageSize) {
		return feesPayAbnormalRepository.queryAbnormalDetailGroupPage(queryEntity, pageNo, pageSize);
	}
	

	@Override
	public PageInfo<FeesPayTransportEntity> queryTransportInfoPage(
			Map<String, Object> param, int pageNo, int pageSize) {
		return feesPayTransportDao.queryAll(param, pageNo, pageSize);
	}

	@Override
	public PageInfo<FeesPayDispatchEntity> queryDispatchInfoPage(
			Map<String, Object> param, int pageNo, int pageSize) {
		return feesPayDispatchRepository.query(param, pageNo, pageSize);
	}

	@Override
	public PageInfo<FeesPayAbnormalEntity> queryAbnormalInfoPage(
			Map<String, Object> param, int pageNo, int pageSize) {
		return feesPayAbnormalRepository.queryPayAll(param, pageNo, pageSize);
	}
	
    /**
	 * 生成账单 默认（宅配、异常）
	 */
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
    @Override
	public void generPayBill(FeesPayBillEntity entity) throws Exception {
		//是否存在费用信息（配送、异常）
		DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 200);
		int dispatchNum = feesPayDispatchRepository.queryCountByFeesBillInfo(entity);
		int abnormalNum = feesPayAbnormalRepository.queryCountByFeesPayBillInfo(entity);
		if (0 == dispatchNum && 0 == abnormalNum) {
			DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 0);
			throw new Exception("未查询到可生成账单的费用数据！");
		}
		
		DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 300);
		
		//生成账单编号
		String billNo = sequenceDao.getBillNoOne(FeesBillEntity.class.getName(), "Z", "0000000000");
		if (StringUtils.isBlank(billNo)) {
			DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 0);
			throw new Exception("生成账单编号失败,请稍后重试!");
		}
		DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 400);
		
		//生成账单，更新费用数据
		String operatorName = JAppContext.currentUserName();
		Timestamp operatorTime = JAppContext.currentTimestamp();
		entity.setBillNo(billNo);
		entity.setCreator(operatorName);
		entity.setCreateTime(operatorTime);
		entity.setType(ConstantInterface.FeeBillType.TYPE_1);//宅配、异常
		entity.setDelFlag(ConstantInterface.InvalidInterface.INVALID_0+"");//0 正常
		for(DeliverEntity deiver : entity.getDeliverList()){
			entity.setDeliveryid(deiver.getDeliverid());
			entity.setDeliverName(deiver.getDelivername());
			//统计金额（配送、异常）
			double sumAmount = 0;
			sumAmount += feesPayDispatchRepository.queryAmountByFeesBillInfo(entity);
			
			//获取异常费用总金额逻辑修改（暂不处理）
			sumAmount += getAbnormalMoney(billNo, entity);
			//sumAmount += feesPayAbnormalRepository.queryAmountByFeesPayBillInfo(entity);
			entity.setTotleprice(sumAmount);
			int k = feesPayBillRepository.save(entity);
			if(k <= 0){
				DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 0);
				throw new Exception("生成账单失败");
			}
		}
		//账单状态为0
		
		DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 500);
		//费用状态为1
		entity.setStatus(ConstantInterface.FeeStatus.STATUS_1);
		entity.setLastModifier(operatorName);
		entity.setLastModifyTime(operatorTime);
		DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 600);
		int updDispatchNum = feesPayDispatchRepository.updateByFeesBillInfo(entity);
		if (dispatchNum != updDispatchNum) {
			DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 0);
			throw new Exception("更新配送费账单编号失败！");
		}
		// 理赔
		DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 800);
		int updAbnormalNum = feesPayAbnormalRepository.updateByFeesPayBillInfo(entity);
		if (abnormalNum != updAbnormalNum) {
			DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 0);
			throw new Exception("更新异常费账单编号失败！");
		}
		DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 900);
	}
    
    /**
	 * 自定义生成账单（宅配、异常）
	 */
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
	@Override
	public void customPayBill(FeesPayBillEntity entity,
			FeesPayBillEncapEntity feesPayBillEncapEntity) throws Exception {
		if(null != feesPayBillEncapEntity.getFeesDispatchList() && 
				feesPayBillEncapEntity.getFeesDispatchList().size()>0){
			int updDispatchNum = feesPayDispatchRepository.updateBatchBillNo(feesPayBillEncapEntity.getFeesDispatchList());
			if(updDispatchNum != feesPayBillEncapEntity.getFeesDispatchList().size())
				throw new Exception("更新配送费账单编号失败！");
		}
		// 异常
		if (null != feesPayBillEncapEntity.getFeesAbnormalList() && 
				feesPayBillEncapEntity.getFeesAbnormalList().size() > 0) {
			int updAbnormalNum = feesPayAbnormalRepository.updateBatchBillNo(feesPayBillEncapEntity.getFeesAbnormalList());
			if(updAbnormalNum != feesPayBillEncapEntity.getFeesAbnormalList().size())
				throw new Exception("更新异常费账单编号失败！");
		}
		for(DeliverEntity deiver:entity.getDeliverList()){
			entity.setDeliveryid(deiver.getDeliverid());
			entity.setDeliverName(deiver.getDelivername());
			double totlePrice=0.0;
			for(FeesPayDispatchEntity feesPayDispatchEntity:feesPayBillEncapEntity.getFeesDispatchList()){
				if(feesPayDispatchEntity.getDeliveryid().equals(entity.getDeliveryid())){
					if(feesPayDispatchEntity.getAmount() != null){
						totlePrice += feesPayDispatchEntity.getAmount();
					}
				}
			}
			// 异常
			for(FeesPayAbnormalEntity abnormalEntity:feesPayBillEncapEntity.getFeesAbnormalList()){
				if(abnormalEntity.getDeliverId().equals(entity.getDeliveryid())){
					if(abnormalEntity.getPayMoney()!=null && "0".equals(abnormalEntity.getIsDeliveryFree())){
						totlePrice -= (abnormalEntity.getPayMoney() + abnormalEntity.getDeliveryCost() + abnormalEntity.getAmerceAmount());
					}else {
						totlePrice -= (abnormalEntity.getPayMoney() + abnormalEntity.getAmerceAmount());
					}
				}
			}
			entity.setTotleprice(totlePrice);
			int k=feesPayBillRepository.save(entity);
			if(k<=0){
				throw new Exception("生成账单失败");
			}
		}
	}

	/**
	 * 生成账单 默认（运输）
	 */
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
	@Override
	public void generTransportPayBill(FeesPayBillEntity entity)
			throws Exception {
		//是否存在费用信息（运输）
		int deliverNum = feesPayTransportDao.queryCountByFeesBillInfo(entity);
		if (0 == deliverNum) {
			DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 0);
			throw new Exception("未查询到可生成账单的费用数据！");
		}
		DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 200);
		
		//统计金额（运输）
		double sumAmount = feesPayTransportDao.queryAmountByFeesBillInfo(entity);
		DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 400);
		
		//生成账单编号
		String billNo = sequenceDao.getBillNoOne(FeesBillEntity.class.getName(), "Z", "0000000000");
		if (StringUtils.isBlank(billNo)) {
			DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 0);
			throw new Exception("生成账单编号失败,请稍后重试!");
		}
		DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 600);
		
		//生成账单，更新费用数据
		String operatorName = JAppContext.currentUserName();
		Timestamp operatorTime = JAppContext.currentTimestamp();
		entity.setBillNo(billNo);
		entity.setTotleprice(sumAmount);
		entity.setCreator(operatorName);
		entity.setCreateTime(operatorTime);
		entity.setType(ConstantInterface.FeeBillType.TYPE_2);//运输
		entity.setDelFlag(ConstantInterface.InvalidInterface.INVALID_0+"");//0 正常
				
		//账单状态为0
		int k=feesPayBillRepository.save(entity);
		if(k<=0){
			DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 0);
			throw new Exception("生成账单失败");
		}
		DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 800);
		//费用状态为1
		entity.setStatus(ConstantInterface.FeeStatus.STATUS_1);
		entity.setLastModifier(operatorName);
		entity.setLastModifyTime(operatorTime);
		int updDeliverNum = feesPayTransportDao.updateByFeesBillInfo(entity);
		if (deliverNum != updDeliverNum) {
			DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 0);
			throw new Exception("更新运输费账单编号失败！");
		}
		DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 900);
	}

	/**
	 * 自定义生成账单（运输）
	 */
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
	@Override
	public void customTransportPayBill(FeesPayBillEntity entity,
			FeesPayBillEncapEntity feesPayBillEncapEntity) throws Exception {
		List<FeesPayTransportEntity> feesDeliverList = feesPayBillEncapEntity.getFeesTransportList();
		if(null != feesDeliverList && feesDeliverList.size()>0){
			int updDeliverNum = feesPayTransportDao.updateBatchBillNo(feesDeliverList);
			if(updDeliverNum != feesDeliverList.size())
				throw new Exception("更新运输费账单编号失败！");
		}
		int k = feesPayBillRepository.save(entity);
		if(k<=0)
			throw new Exception("生成账单失败");
	}
	
	@Override
	public void confirmFeesBill(FeesPayBillEntity entity) throws Exception {
		/**
		 * 目前生成账单的时候状态已改为已过账，不需要再修改为已过账
		 * 2017/7/11
		 */
//		//运输
//		feesPayTransportDao.confirmFeesBill(entity);
//		//配送
//		feesPayDispatchRepository.confirmFeesBill(entity);
//		//异常
//		feesPayAbnormalRepository.confirmFeesBill(entity);
		int k = feesPayBillRepository.confirmFeesBill(entity);
		if(k<=0)
			throw new Exception("结算账单失败,可能该账单已被结算,请刷新界面再次查看该账单状态!");
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
	@Override
	public void deleteFeesBill(FeesPayBillEntity entity) throws Exception {
		// 删除宅配费用表中的理赔费用
		Map<String,Object> condition = new HashMap<String,Object>();
		condition.put("billNo", entity.getBillNo());
		feesPayDispatchRepository.deleteFeesBillAbnormal(condition);
		
		//运输
		feesPayTransportDao.deleteFeesBill(entity);
		//配送
		feesPayDispatchRepository.deleteFeesBill(entity);
		//异常
		feesPayAbnormalRepository.deleteFeesBill(entity);
		int k = feesPayBillRepository.deleteFeesBill(entity);
		if(k<=0)
			throw new Exception("删除账单失败,可能该账单已被结算或已作废,请刷新界面再次查看该账单状态!");
	}

	@Override
	public List<FeesPayBillEntity> getlastBillTime(Map<String, String> maps) {
		return feesPayBillRepository.getlastBillTime(maps);
	}

	@Override
	public List<FeesPayBillEntity> getLastBillTimeDelivery(
			Map<String, Object> maps) {
		return feesPayBillRepository.getLastBillTimeDelivery(maps);
	}
	
	@Override
	public PageInfo<FeesPayTransportEntity> queryTransportDetailByBillNo(
			String billno,int pageNo,int pageSize) {
		return feesPayTransportDao.queryTransportDetailByBillNo(billno, pageNo, pageSize);
	}

	@Override
	public PageInfo<FeesPayDispatchEntity> queryDispatchDetailByBillNo(String billno,int pageNo,int pageSize) {
		return feesPayDispatchRepository.queryDispatchDetailByBillNo(billno, pageNo, pageSize);
	}

	@Override
	public PageInfo<FeesPayAbnormalEntity> queryAbnormalDetailByBillNo(String billno,int pageNo,int pageSize) {
		return feesPayAbnormalRepository.queryabnormalDetailByBillNo(billno, pageNo, pageSize);
	}

	/**
	 * 添加账单
	 */
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
	@Override
	public void addBill(FeesPayBillEntity entity,
			FeesPayBillEncapEntity feesPayBillEncapEntity) throws Exception {
		List<FeesPayTransportEntity> feesDeliverList=feesPayBillEncapEntity.getFeesTransportList();
		if(null != feesDeliverList && feesDeliverList.size()>0){
			int updDeliverNum =feesPayTransportDao.updateBatchBillNo(feesDeliverList);
			if(updDeliverNum != feesDeliverList.size())
				throw new Exception("更新运输费账单编号失败！");
		}
		if(null != feesPayBillEncapEntity.getFeesDispatchList() && 
				feesPayBillEncapEntity.getFeesDispatchList().size()>0){
			int updDispatchNum = feesPayDispatchRepository.updateBatchBillNo(feesPayBillEncapEntity.getFeesDispatchList());
			if(updDispatchNum != feesPayBillEncapEntity.getFeesDispatchList().size())
				throw new Exception("更新配送费账单编号失败！");
		}
		// 异常
		if (null != feesPayBillEncapEntity.getFeesAbnormalList() && 
				feesPayBillEncapEntity.getFeesAbnormalList().size() > 0) {
			int updAbnormalNum = feesPayAbnormalRepository.updateBatchBillNo(feesPayBillEncapEntity.getFeesAbnormalList());
			if(updAbnormalNum != feesPayBillEncapEntity.getFeesAbnormalList().size())
				throw new Exception("更新异常费账单编号失败！");
		}
		//更新账单信息
		int k = feesPayBillRepository.update(entity);
		if(k <= 0)
			throw new Exception("更新账单失败");
	}

	/**
	 * 剔除账单
	 */
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
	@Override
	public void delBill(FeesPayBillEntity entity,
			FeesPayBillEncapEntity feesPayBillEncapEntity) throws Exception {
		if(null != feesPayBillEncapEntity.getFeesTransportList() && 
				feesPayBillEncapEntity.getFeesTransportList().size() > 0){
			int delDeliverNum = feesPayTransportDao.deleteFeesBill(entity);
			if (delDeliverNum != feesPayBillEncapEntity.getFeesTransportList().size())
				throw new Exception("剔除运输费用失败,可能运输费用已过账或已作废,请刷新界面再次查看运输费用状态!");
		}
		if(null != feesPayBillEncapEntity.getFeesDispatchList() && 
				feesPayBillEncapEntity.getFeesDispatchList().size()>0){
			int delDispatchNum = feesPayDispatchRepository.deleteFeesBill(entity);
			if (delDispatchNum != feesPayBillEncapEntity.getFeesDispatchList().size())
				throw new Exception("剔除配送费用失败,可能配送费用已过账或已作废,请刷新界面再次查看配送费用状态!");
		}
		// 异常
		if (null != feesPayBillEncapEntity.getFeesAbnormalList() && 
				feesPayBillEncapEntity.getFeesAbnormalList().size() > 0) {
			int delAbnormalNum = feesPayAbnormalRepository.deleteFeesBill(entity);
			if (delAbnormalNum != feesPayBillEncapEntity.getFeesAbnormalList().size())
				throw new Exception("剔除异常费用失败,可能异常费用已过账或已作废,请刷新界面再次查看异常费用状态!");
		}
		if(null!=feesPayBillEncapEntity.getFeesAbnormalTransportList()&&
				feesPayBillEncapEntity.getFeesAbnormalTransportList().size()>0){
			int delAbnormalTransportNum=feesPayTransportDao.deleteFeesBill(entity);
			if(delAbnormalTransportNum!=feesPayBillEncapEntity.getFeesAbnormalTransportList().size()){
				throw new Exception("剔除运输理赔费用失败,可能异常费用已过账或已作废,请刷新界面再次查看异常费用状态!");
			}
		}
		//更新账单信息
		int k = feesPayBillRepository.update(entity);
		if(k<=0)
			throw new Exception("更新账单失败");
	}

	@Override
	public List<FeesBillWareHouseEntity> queryGroupDispatchAmountByDeliver(
			String billno, String deliverid) {
		return feesPayDispatchRepository.queryGroupDispatchAmountByDeliver(billno,deliverid);
	}

	@Override
	public List<FeesBillWareHouseEntity> queryGroupAbnormalAmountByDeliver(
			String billNo, String deliverid) {
		return feesPayAbnormalRepository.queryGroupAbnormalAmountByDeliver(billNo,deliverid);
	}

	@Override
	public PageInfo<FeesPayAbnormalEntity> queryAbnormalDetailByBillNoAndDeliver(
			FeesPayBillEntity parameter, int pageNo, int pageSize) {
	
		return feesPayAbnormalRepository.queryAbnormalDetailByBillNoAndDeliver(parameter, pageNo, pageSize);
	}

	@Override
	public PageInfo<FeesPayDispatchEntity> queryDispatchDetailByBillNoAndDeliver(
			FeesPayBillEntity parameter, int pageNo, int pageSize) {
		return feesPayDispatchRepository.queryDispatchDetailByBillNoAndDeliver(parameter, pageNo, pageSize);
	}

	@Override
	public List<FeesBillWareHouseEntity> queryGroupDispatchAmountSelect(
			String billno, List<String> deliverIdList) {
		return feesPayDispatchRepository.queryGroupDispatchAmountSelect(billno,deliverIdList);
	}

	@Override
	public List<FeesBillWareHouseEntity> queryGroupAbnormalAmountSelect(
			String billno, List<String> deliverIdList) {
		return feesPayAbnormalRepository.queryGroupAbnormalAmountSelect(billno,deliverIdList);
	}

	@Override
	public PageInfo<FeesPayDispatchEntity> queryDispatchDetailBatch(
			List<FeesPayBillEntity> list, int pageNo, int pageSize) {
		return feesPayDispatchRepository.queryDispatchDetailBatch(list,pageNo,pageSize);
	}

	@Override
	public PageInfo<FeesPayAbnormalEntity> queryAbnormalDetailBatch(
			List<FeesPayBillEntity> list, int pageNo, int pageSize) {
		return feesPayAbnormalRepository.queryAbnormalDetailBatch(list,pageNo,pageSize);
	}

	public double getAbnormalMoney(String billNo, FeesPayBillEntity entity) throws Exception{
		Double totalMoney = 0d;
		Map<String, Object> paramMap=new HashMap<>();
		paramMap.put("deliveryid", entity.getDeliveryid());
		paramMap.put("startTime", entity.getStartTime());
		paramMap.put("endTime", entity.getEndTime());
		paramMap.put("isCalculated", "0");
		List<String> reasonIds=new ArrayList<>();
		reasonIds.add("2");
//		reasonIds.add("4");
		paramMap.put("reasonIds", reasonIds);
		List<FeesPayAbnormalEntity> feeList=feesPayAbnormalRepository.queryFeeByParam(paramMap);
		
		List<FeesPayDispatchEntity> feesPayDispatchList = new ArrayList<FeesPayDispatchEntity>();
		/*
		 * 如果账单总额为100元，某单赔偿金额为10运，运费5元(免运费），罚款5元，且为承运商原因
		 * 最终账单金额 = 100 - （10+5+5）
		 * 如果账单总额为100元，某单赔偿金额为10元，运费5元（免运费 ）， 且为商家原因
		 * 最终账单金额 = 100+10 （无论是否免运费，无论有无罚款金额）
		 * 原因归属为商家原因，获取的金额为赔偿金额；目前涉及承运商有顺丰和中通
		 */
		//仓储理赔
		for(FeesPayAbnormalEntity fee:feeList){
			Double feeAmount = 0d;// 每条理赔费用金额
			//商家原因
			if(4==fee.getReasonId() && ("1500000015".equals(fee.getDeliverId()) || "1500000018".equals(fee.getDeliverId()) || "1400000027".equals(fee.getDeliverId()))){
				totalMoney += fee.getPayMoney();
			}else if(2==fee.getReasonId()){
				//承运商原因
				if("0".equals(fee.getIsDeliveryFree())){
					//免运费
					//根据运单号查询费用
					totalMoney -= (fee.getPayMoney() + fee.getDeliveryCost() + fee.getAmerceAmount());
					feeAmount -= (fee.getPayMoney() + fee.getDeliveryCost() + fee.getAmerceAmount());
				}else{
					//不免运费
					totalMoney -= (fee.getPayMoney() + fee.getAmerceAmount());
					feeAmount -= (fee.getPayMoney() + fee.getAmerceAmount());
				}
			}
			
			// 把异常费用写入到配送费用表中
			FeesPayDispatchEntity dispatchEntity = encapPayDisEntity(billNo, feeAmount, fee);
			if (null != dispatchEntity) {
				feesPayDispatchList.add(dispatchEntity);
			}
		}
		
		if (null != feesPayDispatchList && feesPayDispatchList.size() > 0) {
			int updDispatchNum = feesPayDispatchRepository.insertBatchTmp(feesPayDispatchList);
			if (updDispatchNum != feesPayDispatchList.size()) {
				throw new BizException("理赔费用添加到宅配费用表失败!");
			}
		}
		
		return totalMoney;
	}
	
	/**
	 * 根据异常费用封装成应付宅配费用实体
	 * @param billNo
	 * @param feeAmount
	 * @param fee
	 * @return
	 */
	private FeesPayDispatchEntity encapPayDisEntity(String billNo, double feeAmount, FeesPayAbnormalEntity fee){
		FeesPayDispatchEntity dispatchEntity = null;
		if (null != fee) {
			String operatorName = JAppContext.currentUserName();
	    	Timestamp operatorTime = JAppContext.currentTimestamp();
	    	
			dispatchEntity = new FeesPayDispatchEntity();
			dispatchEntity.setBillNo(billNo);
			dispatchEntity.setFeesNo(fee.getFeeNo());
			dispatchEntity.setWaybillNo(fee.getExpressnum());// 运单号
			dispatchEntity.setExternalNo(fee.getReference());// 外部单号
			dispatchEntity.setCustomerid(fee.getCustomerId());
			dispatchEntity.setCustomerName(fee.getCustomerName());
			dispatchEntity.setWarehouseCode(fee.getWarehouseId());
			dispatchEntity.setWarehouseName(fee.getWarehouseName());
			dispatchEntity.setCarrierid(fee.getCarrierId());
			dispatchEntity.setCarrierName(fee.getCarrierName());
			dispatchEntity.setDeliveryid(fee.getDeliverId());
			dispatchEntity.setDeliverName(fee.getDeliverName());
			dispatchEntity.setAmount(feeAmount);// 金额
			dispatchEntity.setDerateAmount(BigDecimal.ZERO);// 减免金额
			dispatchEntity.setSubjectCode("de_abnormal_pay");
			dispatchEntity.setOtherSubjectCode("de_abnormal_pay");
			dispatchEntity.setStatus("1");
			dispatchEntity.setIsCalculated("1");
			dispatchEntity.setCreator(fee.getCreatePersonName());
			dispatchEntity.setCreateTime(fee.getCreateTime());
			dispatchEntity.setLastModifier(operatorName);
			dispatchEntity.setLastModifyTime(operatorTime);
			dispatchEntity.setDelFlag(ConstantInterface.DelFlag.NO);
		}
		return dispatchEntity;
	}

	@Override
	public List<FeesBillWareHouseEntity> queryAbnormalGroupAmount(String billNo) {
		return feesPayTransportDao.queryAbnormalGroupAmount(billNo);
	}

	@Override
	public PageInfo<FeesPayTransportEntity> queryAbnormalByBillNo(
			String billno, int pageNo, int pageSize) {
		return feesPayTransportDao.queryAbnormalDetailByBillNo(billno,pageNo,pageSize);
	}
}
