package com.jiuyescm.bms.fees.bill.repository.impl;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.fees.bill.out.entity.FeesPayBillEntity;
import com.jiuyescm.bms.fees.bill.repository.IFeesPayBillRepository;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;

@Repository("feesPayBillRepository")
public class FeesPayBillRepositoryImpl extends MyBatisDao<FeesPayBillEntity>
		implements IFeesPayBillRepository {

	@Override
	public PageInfo<FeesPayBillEntity> query(Map<String, Object> condition,
			int pageNo, int pageSize) {
		List<FeesPayBillEntity> list = selectList(
				"com.jiuyescm.bms.fees.bill.mapper.FeesPayBillMapper.query",
				condition, new RowBounds(pageNo, pageSize));
		return new PageInfo<FeesPayBillEntity>(list);
	}
	
	@Override
	public FeesPayBillEntity queryBillInfo(Map<String, Object> condition) {
		List<FeesPayBillEntity> list = selectList(
				"com.jiuyescm.bms.fees.bill.mapper.FeesPayBillMapper.query", condition);
		if (null != list && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	@Override
	public int save(FeesPayBillEntity entity) {
		return insert("com.jiuyescm.bms.fees.bill.mapper.FeesPayBillMapper.save", entity);
		}
	
	@Override
	public int confirmFeesBill(FeesPayBillEntity entity) {
		return update("com.jiuyescm.bms.fees.bill.mapper.FeesPayBillMapper.confirmFeesBill", entity);
	}

	@Override
	public int deleteFeesBill(FeesPayBillEntity entity) {
		return update("com.jiuyescm.bms.fees.bill.mapper.FeesPayBillMapper.deleteFeesBill", entity);
	}

	@Override
	public List<FeesPayBillEntity> getlastBillTime(Map<String, String> maps) {
		return selectList("com.jiuyescm.bms.fees.bill.mapper.FeesPayBillMapper.getlastBillTime", maps);
	}

	@Override
	public List<FeesPayBillEntity> getLastBillTimeDelivery(Map<String, Object> maps) {
		return selectList("com.jiuyescm.bms.fees.bill.mapper.FeesPayBillMapper.getLastBillTimeDelivery", maps);
	}
	
	@Override
	public int update(FeesPayBillEntity entity) {
		return update("com.jiuyescm.bms.fees.bill.mapper.FeesPayBillMapper.update", entity);
	}

}
