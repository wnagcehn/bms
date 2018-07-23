
package com.jiuyescm.bms.receivable.dispatch.service.imp;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.jiuyescm.bms.biz.dispatch.entity.BizDispatchBillEntity;
import com.jiuyescm.bms.general.entity.BizDispatchCarrierChangeEntity;
import com.jiuyescm.bms.receivable.dispatch.service.IBizDispatchBillService;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;
import com.xxl.job.core.log.XxlJobLogger;

@Service("bizDispatchBillService")
@SuppressWarnings("rawtypes")
public class BizDispatchBillServiceImpl extends MyBatisDao implements IBizDispatchBillService{
	
	@Override
	@SuppressWarnings("unchecked")
	public List<BizDispatchBillEntity> query(Map<String, Object> condition) {
		try{
			List<BizDispatchBillEntity> list=selectList("com.jiuyescm.bms.receivable.dispatch.BizDispatchBillMapper.querybizDispatchBillMap", condition);
			return list;
		}
		catch(Exception ex){
			XxlJobLogger.log("查询运单异常"+ex.getMessage());
			return null;
		}
		
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public int updateBatch(List<BizDispatchBillEntity> list) {
		return updateBatch("com.jiuyescm.bms.receivable.dispatch.BizDispatchBillMapper.updatebizDispatchBillMap", list);
	}

	@Override
	@SuppressWarnings("unchecked")
	public int update(BizDispatchBillEntity entity) {
		return update("com.jiuyescm.bms.receivable.dispatch.BizDispatchBillMapper.updatebizDispatchBillMap", entity);
	}

	@Override
	public Map<String, Object> InitReqVo(BizDispatchBillEntity entity,Map<String, String> param) {
		
		//List<PriceMainDispatchEntity> priceList = null;
		
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> queryChangeCusList() {
		return selectList("com.jiuyescm.bms.receivable.dispatch.BizDispatchBillMapper.queryChangeCusList", "");
	}

	@SuppressWarnings("unchecked")
	@Override
	public BizDispatchCarrierChangeEntity queryCarrierChangeEntity(Map<String, Object> param) {
		return (BizDispatchCarrierChangeEntity) selectOne("com.jiuyescm.bms.receivable.dispatch.BizDispatchBillMapper.queryCarrierChangeEntity", param);
	}

	@SuppressWarnings("unchecked")
	@Override
	public int updateCarrierChange(BizDispatchCarrierChangeEntity entity) {

		return update("com.jiuyescm.bms.receivable.dispatch.BizDispatchBillMapper.updateCarrierChange", entity);
	}

	@SuppressWarnings("unchecked")
	@Override
	public int deleteByWayBillNo(String waybillNo) {
		int ret = this.update("com.jiuyescm.bms.receivable.dispatch.BizDispatchBillMapper.deleteByWayBillNo",waybillNo);
		return ret;
	}

	@SuppressWarnings("unchecked")
	@Override
	public int insertDispatchBillEntity(BizDispatchBillEntity entity) {
		int ret = this.insert("com.jiuyescm.bms.receivable.dispatch.BizDispatchBillMapper.save", entity);
		return ret;
	}

	@Override
	public BizDispatchBillEntity getDispatchEntityByWaybillNo(String waybillNo) {
		return (BizDispatchBillEntity) selectOne("com.jiuyescm.bms.receivable.dispatch.BizDispatchBillMapper.getDispatchEntityByWaybillNo", waybillNo);
	}

	@SuppressWarnings("unchecked")
	@Override
	public int updateByParam(Map<String, Object> condition) {
		// TODO Auto-generated method stub
		return this.update("com.jiuyescm.bms.receivable.dispatch.BizDispatchBillMapper.updateByParam", condition);
	}

}