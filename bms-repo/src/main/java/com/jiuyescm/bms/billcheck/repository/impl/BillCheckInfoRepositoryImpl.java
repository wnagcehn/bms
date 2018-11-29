package com.jiuyescm.bms.billcheck.repository.impl;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.jiuyescm.bms.billcheck.BillCheckAdjustInfoEntity;
import com.jiuyescm.bms.billcheck.BillCheckInfoEntity;
import com.jiuyescm.bms.billcheck.BillReceiptFollowEntity;
import com.jiuyescm.bms.billcheck.repository.IBillCheckInfoRepository;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;

@SuppressWarnings("rawtypes")
@Repository("billCheckInfoRepository")
public class BillCheckInfoRepositoryImpl extends MyBatisDao implements IBillCheckInfoRepository{

	@SuppressWarnings("unchecked")
	@Override
	public PageInfo<BillCheckInfoEntity> query(Map<String, Object> condition,
			int pageNo, int pageSize) {
		// TODO Auto-generated method stub
		List<BillCheckInfoEntity> list=selectList("com.jiuyescm.bms.billcheck.mapper.BillCheckInfoMapper.query", condition,new RowBounds(pageNo,pageSize));
		PageInfo<BillCheckInfoEntity> page=new PageInfo<>(list);
		return page;
	}

	@SuppressWarnings("unchecked")
	@Override
	public PageInfo<BillCheckInfoEntity> queryWarn(
			Map<String, Object> condition, int pageNo, int pageSize) {
		// TODO Auto-generated method stub
		List<BillCheckInfoEntity> list=selectList("com.jiuyescm.bms.billcheck.mapper.BillCheckInfoMapper.queryWarn", condition,new RowBounds(pageNo,pageSize));
		PageInfo<BillCheckInfoEntity> page=new PageInfo<>(list);
		return page;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public PageInfo<BillCheckInfoEntity> queryWarnList(
			Map<String, Object> condition, int pageNo, int pageSize) {
		// TODO Auto-generated method stub
		List<BillCheckInfoEntity> list=selectList("com.jiuyescm.bms.billcheck.mapper.BillCheckInfoMapper.queryWarnList", condition,new RowBounds(pageNo,pageSize));
		PageInfo<BillCheckInfoEntity> page=new PageInfo<>(list);
		return page;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public PageInfo<BillCheckInfoEntity> queryByInvoiceNo(
			Map<String, Object> condition, int pageNo, int pageSize) {
		// TODO Auto-generated method stub
		List<BillCheckInfoEntity> list=selectList("com.jiuyescm.bms.billcheck.mapper.BillCheckInfoMapper.queryByInvoiceNo", condition,new RowBounds(pageNo,pageSize));
		PageInfo<BillCheckInfoEntity> page=new PageInfo<>(list);
		return page;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public PageInfo<BillCheckInfoEntity> queryByFollowType(
			Map<String, Object> condition, int pageNo, int pageSize) {
		// TODO Auto-generated method stub
		List<BillCheckInfoEntity> list=selectList("com.jiuyescm.bms.billcheck.mapper.BillCheckInfoMapper.queryByFollowType", condition,new RowBounds(pageNo,pageSize));
		PageInfo<BillCheckInfoEntity> page=new PageInfo<>(list);
		return page;
	}

	@Override
	public List<BillCheckAdjustInfoEntity> queryAdjust(
			Map<String, Object> condition) {
		// TODO Auto-generated method stub
		SqlSession session=this.getSqlSessionTemplate();
		List<BillCheckAdjustInfoEntity> list=session.selectList("com.jiuyescm.bms.billcheck.mapper.BillCheckInfoMapper.queryAdjust", condition);
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public BillCheckInfoEntity queryOne(Map<String, Object> condition) {
		// TODO Auto-generated method stub
		BillCheckInfoEntity entity=(BillCheckInfoEntity) selectOne("com.jiuyescm.bms.billcheck.mapper.BillCheckInfoMapper.queryOne", condition);
		return entity;
	}

	@SuppressWarnings("unchecked")
	@Override
	public int update(BillCheckInfoEntity billCheckInfoEntity) {
		// TODO Auto-generated method stub
		return update("com.jiuyescm.bms.billcheck.mapper.BillCheckInfoMapper.update", billCheckInfoEntity);
	}

	@SuppressWarnings("unchecked")
	@Override
	public int saveList(List<BillCheckInfoEntity> list) {
		// TODO Auto-generated method stub
		return insertBatch("com.jiuyescm.bms.billcheck.mapper.BillCheckInfoMapper.save", list);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public int save(BillCheckInfoEntity entity) {
		// TODO Auto-generated method stub
		int k = insert("com.jiuyescm.bms.billcheck.mapper.BillCheckInfoMapper.save", entity);
		return k;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public int saveNew(BillCheckInfoEntity entity) {
		// TODO Auto-generated method stub
		int k = insert("com.jiuyescm.bms.billcheck.mapper.BillCheckInfoMapper.saveNew", entity);
		return k;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<BillCheckInfoEntity> queryList(Map<String, Object> condition) {
		// TODO Auto-generated method stub
		List<BillCheckInfoEntity> list=selectList("com.jiuyescm.bms.billcheck.mapper.BillCheckInfoMapper.queryList", condition);

		return list;
	}

	@Override
	public int saveReceiptFollow(BillReceiptFollowEntity entity) {
		// TODO Auto-generated method stub
		SqlSession session=this.getSqlSessionTemplate();
		return session.insert("com.jiuyescm.bms.billcheck.mapper.BillCheckInfoMapper.saveReceiptFollow", entity);
	}

	@Override
	public PageInfo<BillReceiptFollowEntity> queryReceiptFollow(
			Map<String, Object> condition, int pageNo, int pageSize) {
		// TODO Auto-generated method stub
		SqlSession session=this.getSqlSessionTemplate();
		List<BillReceiptFollowEntity> list=session.selectList("com.jiuyescm.bms.billcheck.mapper.BillCheckInfoMapper.queryReceiptFollow", condition,new RowBounds(pageNo,pageSize));
		PageInfo<BillReceiptFollowEntity> page=new PageInfo<>(list);
		return page;
	}

	@SuppressWarnings("unchecked")
	@Override
	public int saveAjustList(List<BillCheckAdjustInfoEntity> list) {
		// TODO Auto-generated method stub
		return insertBatch("com.jiuyescm.bms.billcheck.mapper.BillCheckInfoMapper.saveAjustList", list);
	}

	@SuppressWarnings("unchecked")
	@Override
	public BillCheckAdjustInfoEntity queryOneAdjust(
			Map<String, Object> condition) {
		// TODO Auto-generated method stub
		return (BillCheckAdjustInfoEntity) selectOne("com.jiuyescm.bms.billcheck.mapper.BillCheckInfoMapper.queryOneAdjust", condition);
	}

	@SuppressWarnings("unchecked")
	@Override
	public int updateAjustList(List<BillCheckAdjustInfoEntity> list) {
		// TODO Auto-generated method stub	
		return updateBatch("com.jiuyescm.bms.billcheck.mapper.BillCheckInfoMapper.updateAjustList", list);
	}

	@SuppressWarnings("unchecked")
	@Override
	public int saveReceiptFollowList(List<BillReceiptFollowEntity> list) {
		// TODO Auto-generated method stub
		return insertBatch("com.jiuyescm.bms.billcheck.mapper.BillCheckInfoMapper.saveReceiptFollow", list);
	}

	@Override
	public String getBillCheckStatus(int id) {
		Object obj=this.selectOneForObject("com.jiuyescm.bms.billcheck.mapper.BillCheckInfoMapper.queryBillCheckStatus", id);
		String billStatus="";
		if(obj!=null){
			billStatus=obj.toString();
		}
		return billStatus;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<BillCheckInfoEntity> queryAllByBillName(
			List<String> billNameList) {
		Map<String,Object> map=Maps.newHashMap();
		map.put("billNameList", billNameList);
		return this.selectList("com.jiuyescm.bms.billcheck.mapper.BillCheckInfoMapper.queryAllByBillName", map);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<BillCheckInfoEntity> queryAllByBillCheckId(
			List<Integer> checkIdList) {
		Map<String,Object> map=Maps.newHashMap();
		map.put("checkIdList", checkIdList);
		return this.selectList("com.jiuyescm.bms.billcheck.mapper.BillCheckInfoMapper.queryAllByBillCheckId", map);
	}

	@SuppressWarnings("unchecked")
	@Override
	public int updateInvoiceStatus(List<BillCheckInfoEntity> infoList) {
		return this.updateBatch("com.jiuyescm.bms.billcheck.mapper.BillCheckInfoMapper.updateInvoiceStatus",infoList);
	}

	@SuppressWarnings("unchecked")
	@Override
	public int updateReceiptStatus(List<BillCheckInfoEntity> infoList) {
		return this.updateBatch("com.jiuyescm.bms.billcheck.mapper.BillCheckInfoMapper.updateReceiptStatus",infoList);
	}

	@SuppressWarnings("unchecked")
	@Override
	public BillReceiptFollowEntity queryReceiptFollow(
			Map<String, Object> condition) {
		// TODO Auto-generated method stub
		return (BillReceiptFollowEntity) selectOne("com.jiuyescm.bms.billcheck.mapper.BillCheckInfoMapper.queryReceiptFollowOne", condition);
	}

	@SuppressWarnings("unchecked")
	@Override
	public PageInfo<BillCheckInfoEntity> queryReceiptDetail(
			Map<String, Object> condition, int pageNo, int pageSize) {
		// TODO Auto-generated method stub
		List<BillCheckInfoEntity> list=selectList("com.jiuyescm.bms.billcheck.mapper.BillCheckInfoMapper.queryReceiptDetail", condition,new RowBounds(pageNo,pageSize));
		PageInfo<BillCheckInfoEntity> page=new PageInfo<>(list);
		return page;
	}

	@Override
	public BillCheckInfoEntity queryBillCheck(Map<String, Object> condition) {
		// TODO Auto-generated method stub
		BillCheckInfoEntity entity=(BillCheckInfoEntity) selectOne("com.jiuyescm.bms.billcheck.mapper.BillCheckInfoMapper.queryBillCheck", condition);
		return entity;
	}

	@Override
	public List<BillCheckInfoEntity> queryAllUnreceipt(Map<String,Object> condition) {
		// TODO Auto-generated method stub
		List<BillCheckInfoEntity> list=selectList("com.jiuyescm.bms.billcheck.mapper.BillCheckInfoMapper.queryAllUnreceipt",condition);
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public PageInfo<BillCheckInfoEntity> queryForOut(Map<String, Object> condition,
			int pageNo, int pageSize) {
		// TODO Auto-generated method stub
		List<BillCheckInfoEntity> list=selectList("com.jiuyescm.bms.billcheck.mapper.BillCheckInfoMapper.queryForOut", condition,new RowBounds(pageNo,pageSize));
		PageInfo<BillCheckInfoEntity> page=new PageInfo<>(list);
		return page;
	}

	@SuppressWarnings("unchecked")
	@Override
	public BillCheckInfoEntity getLatestBill(Map<String, Object> condition) {
		// TODO Auto-generated method stub
		BillCheckInfoEntity entity=(BillCheckInfoEntity) selectOne("com.jiuyescm.bms.billcheck.mapper.BillCheckInfoMapper.getLatestBill", condition);
		return entity;
	}
}
