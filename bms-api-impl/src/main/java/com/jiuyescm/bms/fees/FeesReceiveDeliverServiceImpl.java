package com.jiuyescm.bms.fees;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.bill.receive.repository.IBmsBillSubjectInfoRepository;
import com.jiuyescm.bms.common.enumtype.BillFeesSubjectStatusEnum;
import com.jiuyescm.bms.fees.entity.FeesReceiveDeliverEntity;
import com.jiuyescm.bms.fees.entity.FeesReceiveDeliverQueryEntity;
import com.jiuyescm.bms.fees.feesreceivedeliver.repository.IFeesReceiveDeliverDao;

/**
 * 
 * @author Wuliangfeng 20170527
 *
 */
@Service("feesReceiveDeliverService")
public class FeesReceiveDeliverServiceImpl implements IFeesReceiverDeliverService{

	@Resource
	private IFeesReceiveDeliverDao feesReceiveDeliverDao;
	
	@Autowired
    private IBmsBillSubjectInfoRepository bmsBillSubjectInfoRepository;
	
	@Override
	public PageInfo<FeesReceiveDeliverEntity> query(
			FeesReceiveDeliverQueryEntity condition, int pageNo, int pageSize) {
		
		return feesReceiveDeliverDao.query(condition, pageNo, pageSize);
	}
	
	@Override
	public FeesReceiveDeliverEntity query(Map<String, Object> condition) {
		return feesReceiveDeliverDao.query(condition);
	}

	@Override
	public int addFeesReceiveDeliver(FeesReceiveDeliverEntity entity) {
		
		return feesReceiveDeliverDao.addFeesReceiveDeliver(entity);
	}

	@Override
	public String queryunitPrice(Map<String, String> condition) {

		return feesReceiveDeliverDao.queryunitPrice(condition);
	}

	@Override
	public int saveFeesList(List<FeesReceiveDeliverEntity> feesList) {
		return feesReceiveDeliverDao.saveFeesList(feesList);
	}

	@Override
	public List<FeesReceiveDeliverEntity> queryList(
			Map<String, Object> condition) {
		// TODO Auto-generated method stub
		return feesReceiveDeliverDao.queryList(condition);
	}

	@SuppressWarnings("unchecked")
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })		
	@Override
	public int insertOrUpdate(Map<String, Object> param) {
		// TODO Auto-generated method stub
		int insertNum=0;
		int updateNum=0;
		List<FeesReceiveDeliverEntity> insertList=(List<FeesReceiveDeliverEntity>) param.get("insert");

		if(insertList.size()>0){
		   //插入正式表				
		   insertNum= feesReceiveDeliverDao.saveFeesList(insertList);						
	    }
	    List<FeesReceiveDeliverEntity> updateList=(List<FeesReceiveDeliverEntity>) param.get("update");
	    if(updateList.size()>0){
		   //插入正式表				
		   updateNum= feesReceiveDeliverDao.updateBatchBillNo(updateList);							
		}
		return insertNum+updateNum;
	}

	@Override
	public PageInfo<FeesReceiveDeliverEntity> queryFeeByGroup(
			Map<String, Object> param, int pageNo, int pageSize) {
		// TODO Auto-generated method stub
		return feesReceiveDeliverDao.queryFeeByGroup(param, pageNo, pageSize);
	}

	@Override
	public List<FeesReceiveDeliverEntity> queryFeeDetail(
			Map<String, Object> param) {
		return feesReceiveDeliverDao.queryFeeDetail(param);
	}

	@Override
	public PageInfo<FeesReceiveDeliverEntity> queryTransportPage(
			Map<String, Object> parameter, int pageNo, int pageSize) {
		// TODO Auto-generated method stub
		return feesReceiveDeliverDao.queryTransportPage(parameter, pageNo, pageSize);
	}

	@Override
	public int updateBatch(List<FeesReceiveDeliverEntity> list) {
		// TODO Auto-generated method stub
		return feesReceiveDeliverDao.updateBatch(list);
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
	public void derateBatchAmount(List<FeesReceiveDeliverEntity> list) {
		// TODO Auto-generated method stub
		if(list!=null&&list.size()>0){
			FeesReceiveDeliverEntity entity=list.get(0);
			//批量更新费用表减免费用
			feesReceiveDeliverDao.derateBatchAmount(list);
			String status=BillFeesSubjectStatusEnum.UPDATE.getCode();
			//更新账单明细状态为 已更新
			bmsBillSubjectInfoRepository.updateStatus(entity.getBillno(),entity.getWarehouseCode(),entity.getSubjectCode(),status);
		}
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
	public void deleteBatchFees(List<FeesReceiveDeliverEntity> list) {
		if(list!=null&&list.size()>0){
			FeesReceiveDeliverEntity entity=list.get(0);
			if(entity.getSubjectCode().equals("ts_abnormal_pay")){
				feesReceiveDeliverDao.deleteBatchAbnormalFees(list);
			}else{
				feesReceiveDeliverDao.deleteBatchFees(list);
			}
			//批量更新费用表减免费用
			String status=BillFeesSubjectStatusEnum.UPDATE.getCode();
			//更新账单明细状态为 已更新
			bmsBillSubjectInfoRepository.updateStatus(entity.getBillno(),entity.getWarehouseCode(),entity.getSubjectCode(),status);
		}
	}

	@Override
	public int updateBatchFees(List<FeesReceiveDeliverEntity> list) {
		return feesReceiveDeliverDao.updateBatchFees(list);
	}

	@Override
	public PageInfo<FeesReceiveDeliverEntity> queryAbnormalFeeByGroup(
			Map<String, Object> parameter, int pageNo, int pageSize) {
		return feesReceiveDeliverDao.queryAbnormalFeeByGroup(parameter,pageNo,pageSize);
	}

	@Override
	public List<FeesReceiveDeliverEntity> queryAbnormalFeeDetail(
			Map<String, Object> parameter) {
		return feesReceiveDeliverDao.queryAbnormalFeeDetail(parameter);
	}


}
