package com.jiuyescm.bms.billcheck.repository.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.billcheck.BillCheckReceiptEntity;
import com.jiuyescm.bms.billcheck.entity.BillReceiptEntity;
import com.jiuyescm.bms.billcheck.repository.IBillCheckReceiptRepository;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;

@Repository("billCheckReceiptRepository")
public class BillCheckReceiptRepositoryImpl extends MyBatisDao<BillCheckReceiptEntity> implements IBillCheckReceiptRepository{

	@Override
	public PageInfo<BillCheckReceiptEntity> query(Map<String, Object> condition,
			int pageNo, int pageSize) {
		// TODO Auto-generated method stub
		List<BillCheckReceiptEntity> list=selectList("com.jiuyescm.bms.billcheck.mapper.BillCheckReceiptMapper.query", condition,new RowBounds(pageNo,pageSize));
		PageInfo<BillCheckReceiptEntity> page=new PageInfo<>(list);
		return page;
	}

	@Override
	public List<BillCheckReceiptEntity> queryByParam(
			Map<String, Object> condition) {
		// TODO Auto-generated method stub
		List<BillCheckReceiptEntity> list=selectList("com.jiuyescm.bms.billcheck.mapper.BillCheckReceiptMapper.query",condition);
		return list;
	}
	
	
	@Override
	public BillCheckReceiptEntity queyReceipt(Map<String, Object> condition) {
		// TODO Auto-generated method stub
		BillCheckReceiptEntity entity=(BillCheckReceiptEntity) selectOne("com.jiuyescm.bms.billcheck.mapper.BillCheckReceiptMapper.queyReceipt", condition);
		return entity;
	}

	@Override
	public int saveList(List<BillCheckReceiptEntity> list) {
		// TODO Auto-generated method stub
		return insertBatch("com.jiuyescm.bms.billcheck.mapper.BillCheckReceiptMapper.save", list);
	}
	
	@Override
	public int save(BillCheckReceiptEntity entity) {
		// TODO Auto-generated method stub
		return insert("com.jiuyescm.bms.billcheck.mapper.BillCheckReceiptMapper.save", entity);
	}

	@Override
	public int update(BillCheckReceiptEntity entity) {
		// TODO Auto-generated method stub
		return update("com.jiuyescm.bms.billcheck.mapper.BillCheckReceiptMapper.update", entity);
	}

	@Override
	public PageInfo<BillReceiptEntity> queryReport(
			Map<String, Object> condition, int pageNo, int pageSize) {
		// TODO Auto-generated method stub
		SqlSession session = getSqlSessionTemplate();
		List<BillReceiptEntity> list=session.selectList("com.jiuyescm.bms.billcheck.mapper.BillCheckReceiptMapper.queryReport", condition,new RowBounds(pageNo,pageSize));
		BigDecimal totalPrice=session.selectOne("com.jiuyescm.bms.billcheck.mapper.BillCheckReceiptMapper.querySum", condition);
		for (BillReceiptEntity entity : list)
			entity.setTotalPrice(totalPrice);
		PageInfo<BillReceiptEntity> page=new PageInfo<>(list);
		return page;
	}

    @Override
    public List<BillCheckReceiptEntity> queryReceiptToCrm(Map<String, Object> condition) {
        List<BillCheckReceiptEntity> list = selectList("com.jiuyescm.bms.billcheck.mapper.BillCheckReceiptMapper.queryReceiptToCrm", condition);
        return list;
    }

}
