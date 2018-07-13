package com.jiuyescm.bms.fees.out.transport.repository.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.fees.bill.out.entity.FeesPayBillEntity;
import com.jiuyescm.bms.fees.entity.FeesBillWareHouseEntity;
import com.jiuyescm.bms.fees.out.transport.entity.FeesPayTransportEntity;
import com.jiuyescm.bms.fees.out.transport.entity.FeesPayTransportQueryEntity;
import com.jiuyescm.bms.fees.out.transport.repository.IFeesPayTransportDao;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;

/**
 * 
 * @author Wuliangfeng 20170527
 *
 */
@Repository("feesPayTransportDao")
public class FeesPayTransportDaoImpl extends MyBatisDao<FeesPayTransportEntity> implements IFeesPayTransportDao {

	@Override
	public PageInfo<FeesPayTransportEntity> query(
			FeesPayTransportQueryEntity condition, int pageNo, int pageSize) {
		List<FeesPayTransportEntity> list=this.selectList("com.jiuyescm.bms.fees.out.transport.mapper.FeesPayTransportMapper.query",
				condition,new RowBounds(pageNo,pageSize));
		
		SqlSession session = getSqlSessionTemplate();
		Double totalCost = (Double)session.selectOne("com.jiuyescm.bms.fees.out.transport.mapper.FeesPayTransportMapper.querySum", condition);
		for (FeesPayTransportEntity entity : list)
			entity.setTotalCost(totalCost);
		PageInfo<FeesPayTransportEntity> pList=new PageInfo<FeesPayTransportEntity>(list);
		return pList;
	}

	@Override
	public int AddFeesReceiveDeliver(FeesPayTransportEntity entity) {
		return this.insert("com.jiuyescm.bms.fees.out.transport.mapper.FeesPayTransportMapper.save", entity);
	}

	@Override
	public String queryunitPrice(Map<String, String> condition) {	
		SqlSession session = getSqlSessionTemplate();
		List<Double> list=session.selectList("com.jiuyescm.bms.fees.out.transport.mapper.FeesPayTransportMapper.queryunitPrice", condition);
		if(list==null||list.size()==0)
			return "";
		return list.get(0).toString();
	}

	/** 应付账单使用方法开始 **/
	@Override
	public List<FeesBillWareHouseEntity> queryGroupTransportAmount(String billNo) {
		 SqlSession session = getSqlSessionTemplate();
		 Map<String,Object> parameter=new HashMap<String,Object>();
		 parameter.put("billNo", billNo);
		 return session.selectList("com.jiuyescm.bms.fees.out.transport.mapper.FeesPayTransportMapper.queryGroupTransportAmount", parameter);
	}
	
	@Override
	public PageInfo<FeesPayTransportEntity> queryTransportDetailGroupPage(
			FeesBillWareHouseEntity queryEntity, int pageNo, int pageSize) {
		/*
		Map<String,Object> parameter=new HashMap<String,Object>();
		parameter.put("billNo", queryEntity.getBillNo());
		if (StringUtils.isNotBlank(queryEntity.getSubjectCode())) {
			parameter.put("subjectCode", queryEntity.getSubjectCode());
		}
		parameter.put("feesNo", queryEntity.getFeesNo());*/
		List<FeesPayTransportEntity> list = selectList("com.jiuyescm.bms.fees.out.transport.mapper.FeesPayTransportMapper.queryTransportDetailGroupPage",
				queryEntity, new RowBounds(pageNo, pageSize));
		return new PageInfo<FeesPayTransportEntity>(list);
	}
	
	@Override
	public int queryCountByFeesBillInfo(FeesPayBillEntity entity) {
		return selectOneForInt("com.jiuyescm.bms.fees.out.transport.mapper.FeesPayTransportMapper.queryCountByFeesBillInfo", entity);
	}

	@Override
	public double queryAmountByFeesBillInfo(FeesPayBillEntity entity) {
		double sumAmount = 0;
		Object  object= selectOneForObject("com.jiuyescm.bms.fees.out.transport.mapper.FeesPayTransportMapper.queryAmountByFeesBillInfo", entity);
		if (null != object) {
			sumAmount = (double)object;
		}
		return sumAmount;
	}

	@Override
	public int updateByFeesBillInfo(FeesPayBillEntity entity) {
		SqlSession session = getSqlSessionTemplate();
		return session.update("com.jiuyescm.bms.fees.out.transport.mapper.FeesPayTransportMapper.updateByFeesBillInfo", entity);
	}
	
	/*
	 * 结算账单
	 */
	@Override
	public int confirmFeesBill(FeesPayBillEntity entity) {
		 SqlSession session = getSqlSessionTemplate();
		 Map<String,Object> parameter=new HashMap<String,Object>();
		 parameter.put("billNo", entity.getBillNo());
		 parameter.put("lastModifier", entity.getLastModifier());
		 parameter.put("lastModifyTime", entity.getLastModifyTime());
		 parameter.put("status", entity.getStatus());
		 return session.update("com.jiuyescm.bms.fees.out.transport.mapper.FeesPayTransportMapper.confirmFeesBill", parameter);
	}

	/*
	 * 删除账单
	 */
	@Override
	public int deleteFeesBill(FeesPayBillEntity entity) {
		 SqlSession session = getSqlSessionTemplate();
		 Map<String,Object> parameter=new HashMap<String,Object>();
		 parameter.put("billNo", entity.getBillNo());
		 parameter.put("lastModifier", entity.getLastModifier());
		 parameter.put("lastModifyTime", entity.getLastModifyTime());
		 parameter.put("status", entity.getStatus());
		 parameter.put("feesNo", entity.getFeesNo());//剔除
		 return session.update("com.jiuyescm.bms.fees.out.transport.mapper.FeesPayTransportMapper.deleteFeesBill", parameter);
	}
	
	@Override
	public int updateBatchBillNo(List<FeesPayTransportEntity> feesTransportList) {
		return updateBatch("com.jiuyescm.bms.fees.out.transport.mapper.FeesPayTransportMapper.updateBillNo", feesTransportList);
	}

	@Override
	public PageInfo<FeesPayTransportEntity> queryTransportDetailByBillNo(
			String billno,int pageNo,int pageSize) {
		 Map<String,Object> parameter=new HashMap<String,Object>();
		 parameter.put("billNo", billno);
		 List<FeesPayTransportEntity> list = this.selectList("com.jiuyescm.bms.fees.out.transport.mapper.FeesPayTransportMapper.queryTransportDetailByBillNo", 
				 parameter, new RowBounds(pageNo, pageSize));
		 return new PageInfo<FeesPayTransportEntity>(list);
	}

	@Override
	public PageInfo<FeesPayTransportEntity> queryAll(
			Map<String, Object> condition, int pageNo, int pageSize) {
		List<FeesPayTransportEntity> list=selectList("com.jiuyescm.bms.fees.out.transport.mapper.FeesPayTransportMapper.queryDetail",
				condition,new RowBounds(pageNo, pageSize));
		return new PageInfo<FeesPayTransportEntity>(list);
	}

	@Override
	public List<FeesPayTransportEntity> queryByImport(List<String> waybillnoList) {
		return null;
	}

	@Override
	public int saveDataBatch(List<FeesPayTransportEntity> list) {
		
		int k=this.insertBatch("com.jiuyescm.bms.fees.out.transport.mapper.FeesPayTransportMapper.save", list);
		return k;
	}

	@Override
	public List<FeesPayTransportEntity> queryByWaybillNo(String waybillno) {
		return this.selectList("com.jiuyescm.bms.fees.out.transport.mapper.FeesPayTransportMapper.queryByWaybillNo", waybillno);
	}

	@Override
	public PageInfo<FeesPayTransportEntity> queryTransportDetailByBillNoAndDeliver(
			FeesPayBillEntity model, int pageNo, int pageSize) {
		 Map<String,String> parameter=new HashMap<String,String>();
		 parameter.put("billNo", model.getBillNo());
		 parameter.put("deliverid",model.getDeliveryid());
		 List<FeesPayTransportEntity> list = this.selectList("com.jiuyescm.bms.fees.out.transport.mapper.FeesPayTransportMapper.queryTransportDetailByBillNoAndDeliver", 
				 parameter, new RowBounds(pageNo, pageSize));
		 return new PageInfo<FeesPayTransportEntity>(list);
	}

	@Override
	public List<FeesPayTransportEntity> queryList(
			Map<String, Object> condition) {
		// TODO Auto-generated method stub
		List<FeesPayTransportEntity> list=this.selectList("com.jiuyescm.bms.fees.out.transport.mapper.FeesPayTransportMapper.queryList", condition);
		return list;
	}

	@Override
	public List<FeesBillWareHouseEntity> queryAbnormalGroupAmount(String billNo) {
		 SqlSession session = getSqlSessionTemplate();
		 Map<String,Object> parameter=new HashMap<String,Object>();
		 parameter.put("billNo", billNo);
		 return session.selectList("com.jiuyescm.bms.fees.out.transport.mapper.FeesPayTransportMapper.queryAbnormalGroupAmount", parameter);
	}

	@Override
	public PageInfo<FeesPayTransportEntity> queryAbnormalDetailByBillNo(
			String billno, int pageNo, int pageSize) {
		 Map<String,Object> parameter=new HashMap<String,Object>();
		 parameter.put("billNo", billno);
		 List<FeesPayTransportEntity> list = this.selectList("com.jiuyescm.bms.fees.out.transport.mapper.FeesPayTransportMapper.queryAbnormalDetailByBillNo", 
				 parameter, new RowBounds(pageNo, pageSize));
		 return new PageInfo<FeesPayTransportEntity>(list);
	}
	
}
