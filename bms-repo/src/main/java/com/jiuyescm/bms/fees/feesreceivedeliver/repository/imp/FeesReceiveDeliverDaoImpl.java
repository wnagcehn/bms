package com.jiuyescm.bms.fees.feesreceivedeliver.repository.imp;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.bill.receive.entity.BmsBillInfoEntity;
import com.jiuyescm.bms.bill.receive.entity.BmsBillSubjectInfoEntity;
import com.jiuyescm.bms.fees.entity.FeesBillEntity;
import com.jiuyescm.bms.fees.entity.FeesBillWareHouseEntity;
import com.jiuyescm.bms.fees.entity.FeesReceiveDeliverEntity;
import com.jiuyescm.bms.fees.entity.FeesReceiveDeliverQueryEntity;
import com.jiuyescm.bms.fees.feesreceivedeliver.repository.IFeesReceiveDeliverDao;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;

/**
 * 
 * @author Wuliangfeng20170527
 *
 */
@Repository("feesReceiveDeliverDao")
public class FeesReceiveDeliverDaoImpl extends MyBatisDao<FeesReceiveDeliverEntity> implements IFeesReceiveDeliverDao {

	@Override
	public PageInfo<FeesReceiveDeliverEntity> query(FeesReceiveDeliverQueryEntity condition, int pageNo, int pageSize) {
		List<FeesReceiveDeliverEntity> list=this.selectList("com.jiuyescm.bms.fees.feesreceivedeliver.mapper.FeesReceiveDeliverMapper.query",
				condition,new RowBounds(pageNo,pageSize));
		
		SqlSession session = getSqlSessionTemplate();
		Double totalCost = (Double)session.selectOne("com.jiuyescm.bms.fees.feesreceivedeliver.mapper.FeesReceiveDeliverMapper.querySum",condition);
		for (FeesReceiveDeliverEntity entity : list)
			entity.setTotalCost(totalCost);
		PageInfo<FeesReceiveDeliverEntity> pList=new PageInfo<FeesReceiveDeliverEntity>(list);
		return pList;
	}
	
	@Override
	public FeesReceiveDeliverEntity query(Map<String, Object> condition) {
		SqlSession session = getSqlSessionTemplate();
		return session.selectOne("com.jiuyescm.bms.fees.feesreceivedeliver.mapper.FeesReceiveDeliverMapper.queryDetail", condition);
	}
	
	@Override
	public PageInfo<FeesReceiveDeliverEntity> queryAll(Map<String, Object> condition, int pageNo, int pageSize) {
		List<FeesReceiveDeliverEntity> list=selectList("com.jiuyescm.bms.fees.feesreceivedeliver.mapper.FeesReceiveDeliverMapper.queryDetail",
				condition,new RowBounds(pageNo, pageSize));
		return new PageInfo<FeesReceiveDeliverEntity>(list);
	}


	@Override
	public int addFeesReceiveDeliver(FeesReceiveDeliverEntity entity) {
		return this.insert("com.jiuyescm.bms.fees.feesreceivedeliver.mapper.FeesReceiveDeliverMapper.save", entity);
	}

	@Override
	public String queryunitPrice(Map<String, String> condition) {	
		SqlSession session = getSqlSessionTemplate();
		List<Double> list=session.selectList("com.jiuyescm.bms.fees.feesreceivedeliver.mapper.FeesReceiveDeliverMapper.queryunitPrice", condition);
		if(list==null||list.size()==0)
			return "";
		return list.get(0).toString();
	}

	@Override
	public int updateBatchBillNo(List<FeesReceiveDeliverEntity> feesDeliverList) {
		
		return this.updateBatch("com.jiuyescm.bms.fees.feesreceivedeliver.mapper.FeesReceiveDeliverMapper.updateBillNo", feesDeliverList);
	}

	@Override
	public List<FeesBillWareHouseEntity> querywarehouseDeliverAmount(
			String billNo) {
		 SqlSession session = getSqlSessionTemplate();
		 Map<String,Object> parameter=new HashMap<String,Object>();
		 parameter.put("billNo", billNo);
		 return session.selectList("com.jiuyescm.bms.fees.feesreceivedeliver.mapper.FeesReceiveDeliverMapper.querywarehouseDeliverAmount", parameter);
	}

	@Override
	public PageInfo<FeesReceiveDeliverEntity> queryDeliverDetailGroupPage(
			FeesBillWareHouseEntity queryEntity, int pageNo, int pageSize) {
		Map<String,Object> parameter=new HashMap<String,Object>();
		parameter.put("billNo", queryEntity.getBillNo());
		if (StringUtils.isNotBlank(queryEntity.getSubjectCode())) {
			parameter.put("subjectCode", queryEntity.getSubjectCode());
		}
		parameter.put("feesNo", queryEntity.getFeesNo());
		List<FeesReceiveDeliverEntity> list = selectList("com.jiuyescm.bms.fees.feesreceivedeliver.mapper.FeesReceiveDeliverMapper.queryDeliverDetailByWarehouseidAndBillNo",
			parameter, new RowBounds(pageNo, pageSize));
		return new PageInfo<FeesReceiveDeliverEntity>(list);
	}

	/*
	 * 结算账单
	 * @see com.jiuyescm.bms.fees.feesreceivedeliver.repository.IFeesReceiveDeliverDao#confirmFeesBill(com.jiuyescm.bms.fees.entity.FeesBillEntity)
	 */
	@Override
	public void confirmFeesBill(FeesBillEntity entity) {
		 SqlSession session = getSqlSessionTemplate();
		 Map<String,Object> parameter=new HashMap<String,Object>();
		 parameter.put("billNo", entity.getBillno());
		 parameter.put("modperson", entity.getModperson());
		 parameter.put("modpersonname", entity.getModpersonname());
		 parameter.put("modtime", entity.getModtime());
		 parameter.put("status", entity.getBillstatus());
		 session.update("com.jiuyescm.bms.fees.feesreceivedeliver.mapper.FeesReceiveDeliverMapper.confirmFeesBill", parameter);
	}

	/*
	 * 删除账单
	 * @see com.jiuyescm.bms.fees.feesreceivedeliver.repository.IFeesReceiveDeliverDao#deleteFeesBill(com.jiuyescm.bms.fees.entity.FeesBillEntity)
	 */
	@Override
	public int deleteFeesBill(FeesBillEntity entity) {
		 SqlSession session = getSqlSessionTemplate();
		 Map<String,Object> parameter=new HashMap<String,Object>();
		 parameter.put("billNo", entity.getBillno());
		 parameter.put("modperson", entity.getModperson());
		 parameter.put("modpersonname", entity.getModpersonname());
		 parameter.put("modtime", entity.getModtime());
		 parameter.put("status", entity.getBillstatus());
		 parameter.put("feesNo", entity.getFeesNo());//剔除
		 return session.update("com.jiuyescm.bms.fees.feesreceivedeliver.mapper.FeesReceiveDeliverMapper.deleteFeesBill", parameter);
	}

	@Override
	public PageInfo<FeesReceiveDeliverEntity> querydeliverDetailByBillNo(
			String billno,int pageNo,int pageSize) {
		 Map<String,Object> parameter=new HashMap<String,Object>();
		 parameter.put("billNo", billno);
		 List<FeesReceiveDeliverEntity> list = this.selectList("com.jiuyescm.bms.fees.feesreceivedeliver.mapper.FeesReceiveDeliverMapper.querydeliverDetailByBillNo",
				 parameter, new RowBounds(pageNo, pageSize));
		 return new PageInfo<FeesReceiveDeliverEntity>(list);
	}

	@Override
	public int queryCountByFeesBillInfo(FeesBillEntity entity) {
		return selectOneForInt("com.jiuyescm.bms.fees.feesreceivedeliver.mapper.FeesReceiveDeliverMapper.queryCountByFeesBillInfo", entity);
	}

	@Override
	public double queryAmountByFeesBillInfo(FeesBillEntity entity) {
		double sumAmount = 0;
		Object  object= selectOneForObject("com.jiuyescm.bms.fees.feesreceivedeliver.mapper.FeesReceiveDeliverMapper.queryAmountByFeesBillInfo", entity);
		if (null != object) {
			sumAmount = (double)object;
		}
		return sumAmount;
	}

	@Override
	public int updateByFeesBillInfo(FeesBillEntity entity) {
		SqlSession session = getSqlSessionTemplate();
		return session.update("com.jiuyescm.bms.fees.feesreceivedeliver.mapper.FeesReceiveDeliverMapper.updateByFeesBillInfo", entity);
	}

	@Override
	public int saveFeesList(List<FeesReceiveDeliverEntity> feesList) {
		return insertBatch("com.jiuyescm.bms.fees.feesreceivedeliver.mapper.FeesReceiveDeliverMapper.save", feesList);
	}

	@Override
	public int queryCountByBillInfo(BmsBillInfoEntity entity) {
		return selectOneForInt("com.jiuyescm.bms.fees.feesreceivedeliver.mapper.FeesReceiveDeliverMapper.queryCountByBillInfo", entity);
	}

	@Override
	public double queryAmountByBillInfo(BmsBillInfoEntity entity) {
		double sumAmount = 0;
		Object  object= selectOneForObject("com.jiuyescm.bms.fees.feesreceivedeliver.mapper.FeesReceiveDeliverMapper.queryAmountByBillInfo", entity);
		if (null != object) {
			sumAmount = (double)object;
		}
		return sumAmount;
	}

	@Override
	public int updateByBillInfo(BmsBillInfoEntity entity) {
		SqlSession session = getSqlSessionTemplate();
		return session.update("com.jiuyescm.bms.fees.feesreceivedeliver.mapper.FeesReceiveDeliverMapper.updateByBillInfo", entity);
	}

	@Override
	public List<BmsBillSubjectInfoEntity> queryFeesBillSubjectInfo(BmsBillInfoEntity entity) {
		SqlSession session = getSqlSessionTemplate();
		return session.selectList("com.jiuyescm.bms.fees.feesreceivedeliver.mapper.FeesReceiveDeliverMapper.queryFeesBillSubjectInfo", entity);
	}
	@Override
	public List<BmsBillSubjectInfoEntity> queryFeesBillSubjectInfoAbnormal(BmsBillInfoEntity entity) {
		SqlSession session = getSqlSessionTemplate();
		return session.selectList("com.jiuyescm.bms.fees.feesreceivedeliver.mapper.FeesReceiveDeliverMapper.queryFeesBillSubjectInfoAbnormal", entity);
	}

	@Override
	public int deleteFeesBill(BmsBillInfoEntity entity) {
		 SqlSession session = getSqlSessionTemplate();
		 Map<String,Object> parameter=new HashMap<String,Object>();
		 parameter.put("billNo", entity.getBillNo());
		 parameter.put("modperson", entity.getLastModifier());
		 parameter.put("modpersonname", entity.getLastModifier());
		 parameter.put("modtime", entity.getLastModifyTime());
		 parameter.put("status", entity.getStatus());
		 return session.update("com.jiuyescm.bms.fees.feesreceivedeliver.mapper.FeesReceiveDeliverMapper.deleteFeesBill", parameter);
	}
	
	@Override
	public int deleteFeesBillAbnormal(BmsBillInfoEntity entity) {
		SqlSession session = getSqlSessionTemplate();
		Map<String,Object> parameter=new HashMap<String,Object>();
		parameter.put("billNo", entity.getBillNo());
		return session.update("com.jiuyescm.bms.fees.feesreceivedeliver.mapper.FeesReceiveDeliverMapper.deleteFeesBillAbnormal", parameter);
	}

	@Override
	public PageInfo<FeesReceiveDeliverEntity> querydeliverDetailByBillNoNew(
			String billno, int pageNo, int pageSize) {
		 Map<String,Object> parameter=new HashMap<String,Object>();
		 parameter.put("billNo", billno);
		 List<FeesReceiveDeliverEntity> list = this.selectList("com.jiuyescm.bms.fees.feesreceivedeliver.mapper.FeesReceiveDeliverMapper.querydeliverDetailByBillNoNew",
				 parameter, new RowBounds(pageNo, pageSize));
		 return new PageInfo<FeesReceiveDeliverEntity>(list);
	}

	@Override
	public List<FeesReceiveDeliverEntity> queryList(
			Map<String, Object> condition) {
		List<FeesReceiveDeliverEntity> list=this.selectList("com.jiuyescm.bms.fees.feesreceivedeliver.mapper.FeesReceiveDeliverMapper.queryList", condition);
		return list;
	}

	@Override
	public BmsBillSubjectInfoEntity sumSubjectMoney(Map<String, Object> param) {
		BmsBillSubjectInfoEntity entity=(BmsBillSubjectInfoEntity) selectOne("com.jiuyescm.bms.fees.feesreceivedeliver.mapper.FeesReceiveDeliverMapper.sumSubjectMoney", param);
		/*
		List<FeesReceiveDeliverEntity> list=selectList("com.jiuyescm.bms.fees.feesreceivedeliver.mapper.FeesReceiveDeliverMapper.queryFeeByGroup",param);
		entity.setNum((double)list.size());*/
		return entity;

	}

	@Override
	public PageInfo<FeesReceiveDeliverEntity> queryFeeByGroup(
			Map<String, Object> param, int pageNo, int pageSize) {
		List<FeesReceiveDeliverEntity> list=selectList("com.jiuyescm.bms.fees.feesreceivedeliver.mapper.FeesReceiveDeliverMapper.queryFeeByGroup",
				param,new RowBounds(pageNo, pageSize));
		return new PageInfo<FeesReceiveDeliverEntity>(list);
	}

	@Override
	public List<FeesReceiveDeliverEntity> queryFeeDetail(
			Map<String, Object> param) {
		List<FeesReceiveDeliverEntity> list=selectList("com.jiuyescm.bms.fees.feesreceivedeliver.mapper.FeesReceiveDeliverMapper.queryFeeDetail",param);

		return list;
	}

	@Override
	public void deleteTransportBill(String billNo) {
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("billNo", billNo);
		SqlSession session = getSqlSessionTemplate();
		session.update("com.jiuyescm.bms.fees.feesreceivedeliver.mapper.FeesReceiveDeliverMapper.deleteTransportBill", map);
	}

	@SuppressWarnings("unchecked")
	@Override
	public int updateFeeByParam(Map<String, Object> param) {
		SqlSession session = getSqlSessionTemplate();
		return session.update("com.jiuyescm.bms.fees.feesreceivedeliver.mapper.FeesReceiveDeliverMapper.updateFeeByParam", param);
	}

	@Override
	public PageInfo<FeesReceiveDeliverEntity> queryTransportPage(
			Map<String, Object> parameter, int pageNo, int pageSize) {
		SqlSession session = getSqlSessionTemplate();
		List<FeesReceiveDeliverEntity> list = session.selectList(
				"com.jiuyescm.bms.fees.feesreceivedeliver.mapper.FeesReceiveDeliverMapper.queryTransportPage",
				parameter, new RowBounds(pageNo, pageSize));
		
		return new PageInfo<FeesReceiveDeliverEntity>(list);
	}

	@Override
	public int updateBatch(List<FeesReceiveDeliverEntity> list) {
		return updateBatch("com.jiuyescm.bms.fees.feesreceivedeliver.mapper.FeesReceiveDeliverMapper.updateBillNo", list);

	}

	@Override
	public void derateBatchAmount(List<FeesReceiveDeliverEntity> list) {
		this.updateBatch("com.jiuyescm.bms.fees.feesreceivedeliver.mapper.FeesReceiveDeliverMapper.derateBatchAmount", list);
	}

	@Override
	public void deleteBatchFees(List<FeesReceiveDeliverEntity> list) {
		this.updateBatch("com.jiuyescm.bms.fees.feesreceivedeliver.mapper.FeesReceiveDeliverMapper.deleteBatchFees", list);		
	}

	@Override
	public int updateBatchFees(List<FeesReceiveDeliverEntity> list) {
		return this.updateBatch("com.jiuyescm.bms.fees.feesreceivedeliver.mapper.FeesReceiveDeliverMapper.updateBatchFees", list);
	}

	@Override
	public List<String> getAllSubject(Map<String, Object> param) {
		SqlSession session = getSqlSessionTemplate();
		return session.selectList("com.jiuyescm.bms.fees.feesreceivedeliver.mapper.FeesReceiveDeliverMapper.getAllSubject", param);
	}

	@Override
	public PageInfo<FeesReceiveDeliverEntity> queryAbnormalFeeByGroup(
			Map<String, Object> parameter, int pageNo, int pageSize) {
		List<FeesReceiveDeliverEntity> list=selectList("com.jiuyescm.bms.fees.feesreceivedeliver.mapper.FeesReceiveDeliverMapper.queryAbnormalFeeByGroup",
				parameter,new RowBounds(pageNo, pageSize));
		return new PageInfo<FeesReceiveDeliverEntity>(list);
	}

	@Override
	public List<FeesReceiveDeliverEntity> queryAbnormalFeeDetail(
			Map<String, Object> parameter) {
		List<FeesReceiveDeliverEntity> list=selectList("com.jiuyescm.bms.fees.feesreceivedeliver.mapper.FeesReceiveDeliverMapper.queryAbnormalFeeDetail",parameter);
		return list;
	}

	@Override
	public BmsBillSubjectInfoEntity sumAbnormalSubjectMoney(
			Map<String, Object> param) {
		BmsBillSubjectInfoEntity entity=(BmsBillSubjectInfoEntity) selectOne("com.jiuyescm.bms.fees.feesreceivedeliver.mapper.FeesReceiveDeliverMapper.sumAbnormalSubjectMoney", param);
		/*
		List<FeesReceiveDeliverEntity> list=selectList("com.jiuyescm.bms.fees.feesreceivedeliver.mapper.FeesReceiveDeliverMapper.queryFeeByGroup",param);
		entity.setNum((double)list.size());*/
		return entity;
	}

	@Override
	public void deleteBatchAbnormalFees(List<FeesReceiveDeliverEntity> list) {
		this.updateBatch("com.jiuyescm.bms.fees.feesreceivedeliver.mapper.FeesReceiveDeliverMapper.deleteBatchAbnormalFees", list);		
	}

	@Override
	public void deleteAbnormalTransportBill(String billNo) {
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("billNo", billNo);
		SqlSession session = getSqlSessionTemplate();
		session.update("com.jiuyescm.bms.fees.feesreceivedeliver.mapper.FeesReceiveDeliverMapper.deleteAbnormalTransportBill", map);
	}
}
