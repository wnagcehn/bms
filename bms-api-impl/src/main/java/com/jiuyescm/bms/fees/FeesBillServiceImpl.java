package com.jiuyescm.bms.fees;

import java.sql.Timestamp;
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
import com.jiuyescm.bms.fees.abnormal.entity.FeesAbnormalEntity;
import com.jiuyescm.bms.fees.abnormal.repository.IFeesAbnormalRepository;
import com.jiuyescm.bms.fees.bill.repository.IFeesBillRepository;
import com.jiuyescm.bms.fees.dispatch.entity.FeesReceiveDispatchEntity;
import com.jiuyescm.bms.fees.dispatch.repository.IFeesReceiveDispatchRepository;
import com.jiuyescm.bms.fees.entity.FeesBillEntity;
import com.jiuyescm.bms.fees.entity.FeesBillQueryEntity;
import com.jiuyescm.bms.fees.entity.FeesBillReceiveEntity;
import com.jiuyescm.bms.fees.entity.FeesBillWareHouseEntity;
import com.jiuyescm.bms.fees.entity.FeesReceiveDeliverEntity;
import com.jiuyescm.bms.fees.feesreceivedeliver.repository.IFeesReceiveDeliverDao;
import com.jiuyescm.bms.fees.storage.entity.FeesReceiveStorageEntity;
import com.jiuyescm.bms.fees.storage.repository.IFeesReceiveStorageRepository;
import com.jiuyescm.cfm.common.JAppContext;
import com.jiuyescm.common.ConstantInterface;

/**
 * 
 * @author wuliangfeng 20170601 账单业务层Service
 *
 */
@Service("feesBillService")
public class FeesBillServiceImpl implements IFeesBillService {

	@Resource
    private IFeesBillRepository feesBillRepository;
	
	@Resource
	private IFeesReceiveDeliverDao feesReceiveDeliverDao;
	
	@Autowired
    private IFeesReceiveStorageRepository feesReceiveStorageRepository;
	
	@Autowired
	private IFeesReceiveDispatchRepository feeInDistributionRepository;
	@Autowired
    private IFeesAbnormalRepository feesAbnormalRepository;
	@Resource
	private SequenceDao sequenceDao;
	
	@Override
	public PageInfo<FeesBillEntity> query(FeesBillQueryEntity queryEntity,
			int pageNo, int pageSize) {
		
		return feesBillRepository.query(queryEntity, pageNo, pageSize);
	}

	@Override
	public FeesBillEntity queryBillInfo(FeesBillQueryEntity queryEntity) {
		return feesBillRepository.queryBillInfo(queryEntity);
	}
	
	@Override
	public void save(FeesBillEntity entity) {
		 feesBillRepository.save(entity);
	}

	/**
	 * 生成账单 默认
	 */
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
	@Override
	public void generReceiverBill(FeesBillEntity entity) throws Exception {
		//是否存在费用信息（仓储、运输、配送、异常）
		int stroageNum = feesReceiveStorageRepository.queryCountByFeesBillInfo(entity);
		int deliverNum = feesReceiveDeliverDao.queryCountByFeesBillInfo(entity);
		int dispatchNum = feeInDistributionRepository.queryCountByFeesBillInfo(entity);
		int abnormalNum = feesAbnormalRepository.queryCountByFeesBillInfo(entity);
		if (0 == deliverNum && 0 == dispatchNum && 0 == stroageNum && 0 == abnormalNum) {
			DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 0);
			throw new Exception("未找到 商家相关仓储,运输,配送,异常账单信息！");
		}
		DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 200);
		
		//统计金额（仓储、运输、配送、异常）
		double sumAmount = 0;
		sumAmount += feesReceiveStorageRepository.queryAmountByFeesBillInfo(entity);
		sumAmount += feesReceiveDeliverDao.queryAmountByFeesBillInfo(entity);
		sumAmount += feeInDistributionRepository.queryAmountByFeesBillInfo(entity);
		sumAmount += feesAbnormalRepository.queryAmountByFeesBillInfo(entity);
		DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 300);
		
		//生成账单编号
		String billNo = sequenceDao.getBillNoOne(FeesBillEntity.class.getName(), "Z", "0000000000");
		if (StringUtils.isBlank(billNo)) {
			DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 0);
			throw new Exception("生成账单编号失败,请稍后重试!");
		}
		DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 400);
		
		//生成账单，更新费用数据
		String operatorId = JAppContext.currentUserID();
		String operatorName = JAppContext.currentUserName();
		Timestamp operatorTime = JAppContext.currentTimestamp();
		entity.setBillno(billNo);
		entity.setTotleprice(sumAmount);
		entity.setCreperson(operatorId);
		entity.setCrepersonname(operatorName);
		entity.setCretime(operatorTime);
		entity.setDelflag(ConstantInterface.InvalidInterface.INVALID_0);//0 正常
				
		//账单状态为0
		int k=feesBillRepository.save(entity);
		if(k<=0){
			DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 0);
			throw new Exception("生成过账单失败");
		}
		//费用状态为1
		entity.setBillstatus(ConstantInterface.InvalidInterface.INVALID_1);
		entity.setModperson(operatorId);
		entity.setModpersonname(operatorName);
		entity.setModtime(operatorTime);
		int updStorageNum = feesReceiveStorageRepository.updateByFeesBillInfo(entity);
		if (stroageNum != updStorageNum) {
			DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 0);
			throw new Exception("更新仓储费 过账单编号失败！");
		}
		DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 600);
		int updDeliverNum = feesReceiveDeliverDao.updateByFeesBillInfo(entity);
		if (deliverNum != updDeliverNum) {
			DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 0);
			throw new Exception("更新运输费 过账单编号失败！");
		}
		DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 700);
		int updDispatchNum = feeInDistributionRepository.updateByFeesBillInfo(entity);
		if (dispatchNum != updDispatchNum) {
			DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 0);
			throw new Exception("更新配送费 过账单编号失败！");
		}
		DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 800);
		int updAbnormalNum = feesAbnormalRepository.updateByFeesBillInfo(entity);
		if (abnormalNum != updAbnormalNum) {
			DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 0);
			throw new Exception("更新异常费 过账单编号失败！");
		}
		DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 900);
	}
	
	/**
	 * 自定义生成账单
	 */
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
	@Override
	public void saveAll(FeesBillEntity entity,
			FeesBillReceiveEntity feesBillReceiveEntity) throws Exception {
		List<FeesReceiveDeliverEntity> feesDeliverList=feesBillReceiveEntity.getFeesDeliverList();
		if(null != feesDeliverList && feesDeliverList.size()>0){
			int updDeliverNum = feesReceiveDeliverDao.updateBatchBillNo(feesDeliverList);
			if(updDeliverNum != feesDeliverList.size())
				throw new Exception("更新运输费 过账单编号失败,数据有变动！");
		}
		if(null != feesBillReceiveEntity.getFeesStorageList() && 
				feesBillReceiveEntity.getFeesStorageList().size()>0){
			int updStroageNum = feesReceiveStorageRepository.updateBatchBillNo(feesBillReceiveEntity.getFeesStorageList());
			if(updStroageNum != feesBillReceiveEntity.getFeesStorageList().size())
				throw new Exception("更新仓储费  过账单编号失败,数据有变动！");
		}
		if(null != feesBillReceiveEntity.getFeesDispatchList() && 
				feesBillReceiveEntity.getFeesDispatchList().size()>0){
			int updDispatchNum = feeInDistributionRepository.updateBatchBillNo(feesBillReceiveEntity.getFeesDispatchList());
			if(updDispatchNum != feesBillReceiveEntity.getFeesDispatchList().size())
				throw new Exception("更新配送费  过账单编号失败,数据有变动！");
		}
		if (null != feesBillReceiveEntity.getFeesAbnormalList() && 
				feesBillReceiveEntity.getFeesAbnormalList().size() > 0) {
			int updAbnormalNum = feesAbnormalRepository.updateBatchBillNo(feesBillReceiveEntity.getFeesAbnormalList());
			if(updAbnormalNum != feesBillReceiveEntity.getFeesAbnormalList().size())
				throw new Exception("更新异常费  过账单编号失败,数据有变动！");
		}
		int k=feesBillRepository.save(entity);
		if(k<=0)
			throw new Exception("生成过账单失败");
	}
	
	/**
	 * 添加账单
	 */
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
	@Override
	public void addBill(FeesBillEntity entity,
			FeesBillReceiveEntity feesBillReceiveEntity) throws Exception {
		List<FeesReceiveDeliverEntity> feesDeliverList=feesBillReceiveEntity.getFeesDeliverList();
		if(null != feesDeliverList && feesDeliverList.size()>0){
			int updDeliverNum =feesReceiveDeliverDao.updateBatchBillNo(feesDeliverList);
			if(updDeliverNum != feesDeliverList.size())
				throw new Exception("更新运输费 过账单编号失败,数据有变动！");
		}
		if(null != feesBillReceiveEntity.getFeesStorageList() && 
				feesBillReceiveEntity.getFeesStorageList().size()>0){
			int updStroageNum =feesReceiveStorageRepository.updateBatchBillNo(feesBillReceiveEntity.getFeesStorageList());
			if(updStroageNum != feesBillReceiveEntity.getFeesStorageList().size())
				throw new Exception("更新仓储费  过账单编号失败,数据有变动！");
		}
		if(null != feesBillReceiveEntity.getFeesDispatchList() && 
				feesBillReceiveEntity.getFeesDispatchList().size()>0){
			int updDispatchNum = feeInDistributionRepository.updateBatchBillNo(feesBillReceiveEntity.getFeesDispatchList());
			if(updDispatchNum != feesBillReceiveEntity.getFeesDispatchList().size())
				throw new Exception("更新配送费  过账单编号失败,数据有变动！");
		}
		if (null != feesBillReceiveEntity.getFeesAbnormalList() && 
				feesBillReceiveEntity.getFeesAbnormalList().size() > 0) {
			int updAbnormalNum = feesAbnormalRepository.updateBatchBillNo(feesBillReceiveEntity.getFeesAbnormalList());
			if(updAbnormalNum != feesBillReceiveEntity.getFeesAbnormalList().size())
				throw new Exception("更新异常费  过账单编号失败,数据有变动！");
		}
		//更新账单信息
		int k=feesBillRepository.update(entity);
		if(k<=0)
			throw new Exception("更新账单失败");
	}
	
	/**
	 * 剔除账单
	 */
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
	@Override
	public void delBill(FeesBillEntity entity,
			FeesBillReceiveEntity feesBillReceiveEntity) throws Exception {
		if(null != feesBillReceiveEntity.getFeesDeliverList() && 
				feesBillReceiveEntity.getFeesDeliverList().size() > 0){
			int delDeliverNum = feesReceiveDeliverDao.deleteFeesBill(entity);
			if (delDeliverNum != feesBillReceiveEntity.getFeesDeliverList().size())
				throw new Exception("剔除运输费用失败,可能运输费用已过账或已作废,请刷新界面再次查看运输费用状态!");
		}
		if(null != feesBillReceiveEntity.getFeesStorageList() && 
				feesBillReceiveEntity.getFeesStorageList().size()>0){
			int delStroageNum = feesReceiveStorageRepository.deleteFeesBill(entity);
			if (delStroageNum != feesBillReceiveEntity.getFeesStorageList().size())
				throw new Exception("剔除仓储费用失败,可能仓储费用已过账或已作废,请刷新界面再次查看仓储费用状态!");
		}
		if(null != feesBillReceiveEntity.getFeesDispatchList() && 
				feesBillReceiveEntity.getFeesDispatchList().size()>0){
			int delDispatchNum = feeInDistributionRepository.deleteFeesBill(entity);
			if (delDispatchNum != feesBillReceiveEntity.getFeesDispatchList().size())
				throw new Exception("剔除配送费用失败,可能配送费用已过账或已作废,请刷新界面再次查看配送费用状态!");
		}
		if (null != feesBillReceiveEntity.getFeesAbnormalList() && 
				feesBillReceiveEntity.getFeesAbnormalList().size() > 0) {
			int delAbnormalNum = feesAbnormalRepository.deleteFeesBill(entity);
			if (delAbnormalNum != feesBillReceiveEntity.getFeesAbnormalList().size())
				throw new Exception("剔除异常费用失败,可能异常费用已过账或已作废,请刷新界面再次查看异常费用状态!");
		}
		//更新账单信息
		int k=feesBillRepository.update(entity);
		if(k<=0)
			throw new Exception("更新账单失败");
	}
	
	/**
	 * 核销账单
	 */
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
	@Override
	public void confirmFeesBill(FeesBillEntity entity) throws Exception {
		feesReceiveDeliverDao.confirmFeesBill(entity);
		feesReceiveStorageRepository.confirmFeesBill(entity);
		feeInDistributionRepository.confirmFeesBill(entity);
		feesAbnormalRepository.confirmFeesBill(entity);
		int k=feesBillRepository.confirmFeesBill(entity);
		if(k<=0)
			throw new Exception("结算账单失败,可能改账单已被结算,请刷新界面再次查看该账单状态!");
	}

	/**
	 * 删除账单
	 */
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
	@Override
	public void deleteFeesBill(FeesBillEntity entity) throws Exception {
		feesReceiveDeliverDao.deleteFeesBill(entity);
		feesReceiveStorageRepository.deleteFeesBill(entity);
		feeInDistributionRepository.deleteFeesBill(entity);
		feesAbnormalRepository.deleteFeesBill(entity);
		int k=feesBillRepository.deleteFeesBill(entity);
		if(k<=0)
			throw new Exception("删除账单失败,可能该账单已被结算或已作废,请刷新界面再次查看该账单状态!");
	}
	
	@Override
	public List<FeesBillWareHouseEntity> querywarehouseDeliverAmount(
			String parameter) {
		return feesReceiveDeliverDao.querywarehouseDeliverAmount(parameter);
	}

	@Override
	public PageInfo<FeesReceiveDeliverEntity> queryDeliverDetailGroupPage(
			FeesBillWareHouseEntity parameter, int pageNo, int pageSize) {
		return feesReceiveDeliverDao.queryDeliverDetailGroupPage(parameter, pageNo, pageSize);
	}

	@Override
	public List<FeesBillWareHouseEntity> querywarehouseStorageAmount(
			String billNo) {
		return feesReceiveStorageRepository.querywarehouseStorageAmount(billNo);
	}


	
	@Override
	public PageInfo<FeesReceiveStorageEntity> queryStorageDetailGroupPage(
			FeesBillWareHouseEntity queryEntity, int pageNo, int pageSize) {
		return feesReceiveStorageRepository.queryStorageDetailGroupPage(queryEntity, pageNo, pageSize);
	}
	
	@Override
	public List<FeesBillWareHouseEntity> querywarehouseDistributionAmount(
			String billNo) {
		return feeInDistributionRepository.querywarehouseDistributionAmount(billNo);
	}

	@Override
	public PageInfo<FeesReceiveDispatchEntity> queryDispatchDetailGroupPage(
			FeesBillWareHouseEntity queryEntity, int pageNo, int pageSize) {
		return feeInDistributionRepository.queryDispatchDetailGroupPage(queryEntity, pageNo, pageSize);
	}
	
	@Override
	public List<FeesBillWareHouseEntity> querywarehouseAbnormalAmount(
			String billNo) {
		return feesAbnormalRepository.querywarehouseAbnormalAmount(billNo);
	}

	@Override
	public PageInfo<FeesAbnormalEntity> queryAbnormalDetailGroupPage(
			FeesBillWareHouseEntity queryEntity, int pageNo, int pageSize) {
		return feesAbnormalRepository.queryAbnormalDetailGroupPage(queryEntity, pageNo, pageSize);
	}
	
	@Override
	public PageInfo<FeesReceiveDeliverEntity> querydeliverDetailByBillNo(
			String billno,int pageNo,int pageSize) {
		return feesReceiveDeliverDao.querydeliverDetailByBillNo(billno, pageNo, pageSize);
	}

	@Override
	public PageInfo<FeesReceiveStorageEntity> querystorageDetailByBillNo(
			String billno,int pageNo,int pageSize) {
		return feesReceiveStorageRepository.querystorageDetailByBillNo(billno, pageNo, pageSize);
	}
	
	@Override
	public PageInfo<FeesReceiveStorageEntity> queryStorageByBillNoSubjectId(
			String billno,String subjectId,int pageNo,int pageSize) {
		return feesReceiveStorageRepository.queryStorageByBillNoSubjectId(billno, subjectId, pageNo, pageSize);
	}
	
	/**
	 * 导出-耗材使用费
	 */
	@Override
	public PageInfo<FeesReceiveStorageEntity> queryPackMaterialByBillNo(String billno,int pageNo,int pageSize) {
		return feesReceiveStorageRepository.queryPackMaterialByBillNo(billno, pageNo, pageSize);
	}

	@Override
	public List<FeesReceiveDispatchEntity> querydistributionDetailByBillNo(
			String billno) {
		return feeInDistributionRepository.querydistributionDetailByBillNo(billno);
	}
	
	@Override
	public PageInfo<FeesReceiveDispatchEntity> querydistributionDetailByBillNo(
			String billno,int pageNo,int pageSize) {
		return feeInDistributionRepository.querydistributionDetailByBillNo(billno, pageNo, pageSize);
	}

	@Override
	public List<FeesAbnormalEntity> queryabnormalDetailByBillNo(String billno) {
		return feesAbnormalRepository.queryabnormalDetailByBillNo(billno);
	}
	
	@Override
	public PageInfo<FeesAbnormalEntity> queryabnormalDetailByBillNo(String billno,int pageNo,int pageSize) {
		return feesAbnormalRepository.queryabnormalDetailByBillNo(billno, pageNo, pageSize);
	}
	
	@Override
	public List<FeesBillEntity> getlastBillTime(Map<String, String> maps) {
		return feesBillRepository.getlastBillTime(maps);
	}
	
	@Override
	public PageInfo<FeesReceiveStorageEntity> queryStorageInfoPage(
			Map<String, Object> param, int pageNo, int pageSize) {
		return feesReceiveStorageRepository.query(param, pageNo, pageSize);
	}

	@Override
	public PageInfo<FeesReceiveDeliverEntity> queryDeliverInfoPage(
			Map<String, Object> param, int pageNo, int pageSize) {
		return feesReceiveDeliverDao.queryAll(param, pageNo, pageSize);
	}

	@Override
	public PageInfo<FeesReceiveDispatchEntity> queryDispatchInfoPage(
			Map<String, Object> param, int pageNo, int pageSize) {
		return feeInDistributionRepository.query(param, pageNo, pageSize);
	}

	@Override
	public PageInfo<FeesAbnormalEntity> queryAbnormalInfoPage(
			Map<String, Object> param, int pageNo, int pageSize) {
		return feesAbnormalRepository.queryAll(param, pageNo, pageSize);
	}

	@Override
	public List<FeesReceiveStorageEntity> queryFeesData(Map<String, Object> param) {
		return feesReceiveStorageRepository.queryFeesData(param);
	}

}
