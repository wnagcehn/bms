package com.jiuyescm.bms.fees.bill.repository.impl;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.fees.bill.repository.IFeesBillRepository;
import com.jiuyescm.bms.fees.entity.FeesBillEntity;
import com.jiuyescm.bms.fees.entity.FeesBillQueryEntity;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;

@Repository("feesBillRepository")
public class FeesBillRepositoryImpl  extends MyBatisDao<FeesBillEntity> implements IFeesBillRepository{

	@Override
	public PageInfo<FeesBillEntity> query(FeesBillQueryEntity queryEntity,
			int pageNo, int pageSize) {
		List<FeesBillEntity> list = selectList(
				"com.jiuyescm.bms.fees.bill.mapper.FeesBillMapper.query",
				queryEntity, new RowBounds(pageNo, pageSize));
		return new PageInfo<FeesBillEntity>(list);
	}

	@Override
	public FeesBillEntity queryBillInfo(FeesBillQueryEntity queryEntity) {
		return selectOne("com.jiuyescm.bms.fees.bill.mapper.FeesBillMapper.query", queryEntity);
	}
	
	@Override
	public int save(FeesBillEntity entity) {
		return this.insert("com.jiuyescm.bms.fees.bill.mapper.FeesBillMapper.save", entity);
	}

	@Override
	public int confirmFeesBill(FeesBillEntity entity) {
		return this.update("com.jiuyescm.bms.fees.bill.mapper.FeesBillMapper.confirmFeesBill", entity);
	}

	@Override
	public int deleteFeesBill(FeesBillEntity entity) {
		return this.update("com.jiuyescm.bms.fees.bill.mapper.FeesBillMapper.deleteFeesBill", entity);
	}

	@Override
	public List<FeesBillEntity> getlastBillTime(Map<String, String> maps) {
		return selectList("com.jiuyescm.bms.fees.bill.mapper.FeesBillMapper.getlastBillTime", maps);
	}

	@Override
	public int update(FeesBillEntity entity) {
		return update("com.jiuyescm.bms.fees.bill.mapper.FeesBillMapper.update", entity);
	}

}
