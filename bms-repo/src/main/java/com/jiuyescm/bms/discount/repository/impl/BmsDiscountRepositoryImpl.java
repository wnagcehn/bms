package com.jiuyescm.bms.discount.repository.impl;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.discount.BmsDiscountAccountEntity;
import com.jiuyescm.bms.discount.FeesReceiveDispatchDiscountEntity;
import com.jiuyescm.bms.discount.repository.IBmsDiscountRepository;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;

@SuppressWarnings("rawtypes")
@Repository("bmsDiscountRepository")
public class BmsDiscountRepositoryImpl extends MyBatisDao implements IBmsDiscountRepository{

	@SuppressWarnings("unchecked")
	@Override
	public BmsDiscountAccountEntity queryAccount(Map<String, Object> condition) {
		// TODO Auto-generated method stub
		return (BmsDiscountAccountEntity) selectOne("com.jiuyescm.bms.discount.mapper.BmsDiscountMapper.queryAccount", condition);
	}

	@SuppressWarnings("unchecked")
	@Override
	public int updateFeeDiscountTask(Map<String, Object> condition) {
		// TODO Auto-generated method stub
		return update("com.jiuyescm.bms.discount.mapper.BmsDiscountMapper.updateFeeDiscountTask", condition);
	}

	@SuppressWarnings("unchecked")
	@Override
	public PageInfo<FeesReceiveDispatchDiscountEntity> queryAll(
			Map<String, Object> condition, int pageNo, int pageSize) {
		// TODO Auto-generated method stub
		List<FeesReceiveDispatchDiscountEntity> list=selectList("com.jiuyescm.bms.discount.mapper.BmsDiscountMapper.queryAll", condition,new RowBounds(pageNo,pageSize));
		PageInfo<FeesReceiveDispatchDiscountEntity> page=new PageInfo<FeesReceiveDispatchDiscountEntity>(list);
		return page;
	}

	@SuppressWarnings("unchecked")
	@Override
	public int updateList(List<FeesReceiveDispatchDiscountEntity> list) {
		// TODO Auto-generated method stub
		return updateBatch("com.jiuyescm.bms.discount.mapper.BmsDiscountMapper.updateList", list);
	}

}
