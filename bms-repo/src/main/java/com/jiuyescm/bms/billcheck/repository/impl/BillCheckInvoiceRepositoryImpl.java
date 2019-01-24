package com.jiuyescm.bms.billcheck.repository.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.jiuyescm.bms.billcheck.BillCheckInvoiceEntity;
import com.jiuyescm.bms.billcheck.entity.BillInvoiceEntity;
import com.jiuyescm.bms.billcheck.repository.IBillCheckInvoiceRepository;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;

@Repository("billCheckInvoiceRepository")
public class BillCheckInvoiceRepositoryImpl extends MyBatisDao<BillCheckInvoiceEntity> implements IBillCheckInvoiceRepository{

	@Override
	public PageInfo<BillCheckInvoiceEntity> query(Map<String, Object> condition,
			int pageNo, int pageSize) {
		// TODO Auto-generated method stub
		List<BillCheckInvoiceEntity> list=selectList("com.jiuyescm.bms.billcheck.mapper.BillCheckInvoiceMapper.query", condition,new RowBounds(pageNo,pageSize));
		PageInfo<BillCheckInvoiceEntity> page=new PageInfo<>(list);
		return page;
	}

	@Override
	public BillCheckInvoiceEntity queryInvoice(Map<String, Object> condition) {
		// TODO Auto-generated method stub
		BillCheckInvoiceEntity entity=(BillCheckInvoiceEntity) selectOne("com.jiuyescm.bms.billcheck.mapper.BillCheckInvoiceMapper.queryInvoice", condition);
		return entity;
	}

	@Override
	public int saveList(List<BillCheckInvoiceEntity> list) {
		// TODO Auto-generated method stub
		return insertBatch("com.jiuyescm.bms.billcheck.mapper.BillCheckInvoiceMapper.save", list);
	}
	
	@Override
	public int save(BillCheckInvoiceEntity entity) {
		// TODO Auto-generated method stub
		return insert("com.jiuyescm.bms.billcheck.mapper.BillCheckInvoiceMapper.save", entity);
	}

	@Override
	public List<BillCheckInvoiceEntity> queryByParam(
			Map<String, Object> condition) {
		// TODO Auto-generated method stub
		List<BillCheckInvoiceEntity> list=selectList("com.jiuyescm.bms.billcheck.mapper.BillCheckInvoiceMapper.query", condition);
		return list;
	}

	@Override
	public List<BillCheckInvoiceEntity> queryAllBillInvoice(
			List<Integer> checkIdList) {
		Map<String,Object> map=Maps.newHashMap();
		map.put("checkIdList", checkIdList);
		return this.selectList("com.jiuyescm.bms.billcheck.mapper.BillCheckInvoiceMapper.queryAllBillInvoice", map);
	}

	@Override
	public int update(BillCheckInvoiceEntity vo) {
		// TODO Auto-generated method stub
		return update("com.jiuyescm.bms.billcheck.mapper.BillCheckInvoiceMapper.update", vo);
	}

	@Override
	public PageInfo<BillInvoiceEntity> queryReport(
			Map<String, Object> condition, int pageNo, int pageSize) {
		// TODO Auto-generated method stub
		SqlSession session = getSqlSessionTemplate();
		List<BillInvoiceEntity> list=session.selectList("com.jiuyescm.bms.billcheck.mapper.BillCheckInvoiceMapper.queryReport", condition,new RowBounds(pageNo,pageSize));
		BigDecimal totalPrice=session.selectOne("com.jiuyescm.bms.billcheck.mapper.BillCheckInvoiceMapper.querySum", condition);
		for (BillInvoiceEntity entity : list)
			entity.setTotalPrice(totalPrice);
		PageInfo<BillInvoiceEntity> page=new PageInfo<>(list);
		return page;
	}

}
