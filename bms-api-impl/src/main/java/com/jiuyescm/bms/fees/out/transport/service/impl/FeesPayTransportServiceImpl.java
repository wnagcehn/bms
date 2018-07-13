package com.jiuyescm.bms.fees.out.transport.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.fees.out.transport.entity.FeesPayTransportEntity;
import com.jiuyescm.bms.fees.out.transport.entity.FeesPayTransportQueryEntity;
import com.jiuyescm.bms.fees.out.transport.repository.IFeesPayTransportDao;
import com.jiuyescm.bms.fees.out.transport.service.IFeesPayTransportService;

/**
 * 
 * @author Wuliangfeng 20170527
 *
 */
@Service("feesPayTransportService")
public class FeesPayTransportServiceImpl implements IFeesPayTransportService{

	@Resource
	private IFeesPayTransportDao feesPayTransportDao;
	
	@Override
	public PageInfo<FeesPayTransportEntity> query(
			FeesPayTransportQueryEntity condition, int pageNo, int pageSize) {
		
		return feesPayTransportDao.query(condition, pageNo, pageSize);
	}

	@Override
	public int AddFeesReceiveDeliver(FeesPayTransportEntity entity) {
		
		return feesPayTransportDao.AddFeesReceiveDeliver(entity);
	}

	@Override
	public String queryunitPrice(Map<String, String> condition) {

		return feesPayTransportDao.queryunitPrice(condition);
	}

	@Override
	public List<FeesPayTransportEntity> queryByImport(List<String> waybillnoList) {
		return feesPayTransportDao.queryByImport(waybillnoList);
	}

	@Override
	public List<FeesPayTransportEntity> queryByWaybillNo(String waybillno) {
		return feesPayTransportDao.queryByWaybillNo(waybillno);
	}

	/**
	 * 事物处理
	 */
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED,rollbackFor=Exception.class)
	public int saveDataBatch(List<FeesPayTransportEntity> list) {
		
		return feesPayTransportDao.saveDataBatch(list);
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })		
	@SuppressWarnings("unchecked")
	@Override
	public int insertOrUpdate(Map<String, Object> param) {
		// TODO Auto-generated method stub
		int insertNum=0;
		int updateNum=0;
		List<FeesPayTransportEntity> insertList=(List<FeesPayTransportEntity>) param.get("insert");

		if(insertList.size()>0){
		   //插入正式表				
		   insertNum= feesPayTransportDao.saveDataBatch(insertList);
				   //saveFeesList(insertList);						
	    }
	    List<FeesPayTransportEntity> updateList=(List<FeesPayTransportEntity>) param.get("update");
	    if(updateList.size()>0){
		   //插入正式表				
		   updateNum= feesPayTransportDao.updateBatchBillNo(updateList);							
		}
		return insertNum+updateNum;
	}

	@Override
	public List<FeesPayTransportEntity> queryList(
			Map<String, Object> condition) {
		// TODO Auto-generated method stub
		return feesPayTransportDao.queryList(condition);
	}

}
