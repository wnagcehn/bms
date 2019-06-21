package com.jiuyescm.bms.fees.claim.repository.impl;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.fees.claim.FeesClaimsEntity;
import com.jiuyescm.bms.fees.claim.repository.IFeesClaimsRepository;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;

@SuppressWarnings("rawtypes")
@Repository("feesClaimsRepository")
public class FeesClaimsRepositoryImpl extends MyBatisDao implements IFeesClaimsRepository {

	@SuppressWarnings("unchecked")
	public PageInfo<FeesClaimsEntity> query(Map<String, Object> condition, int pageNo, int pageSize) {
		List<FeesClaimsEntity> list = selectList(
				"com.jiuyescm.bms.fees.claim.mapper.FeesClaimsMapper.query",
				condition, new RowBounds(pageNo, pageSize));
		
		return new PageInfo<FeesClaimsEntity>(list);
	}

    @Override
    public int update(FeesClaimsEntity entity) {
        // TODO Auto-generated method stub
        return update("com.jiuyescm.bms.fees.claim.mapper.FeesClaimsMapper.update", entity);
    }

    @SuppressWarnings("unchecked")
    @Override
    public PageInfo<FeesClaimsEntity> queryPreBillClaim(Map<String, Object> condition, int pageNo, int pageSize) {
        // TODO Auto-generated method stub
        List<FeesClaimsEntity> list = selectList(
                "com.jiuyescm.bms.fees.claim.mapper.FeesClaimsMapper.queryPreBillClaim",
                condition, new RowBounds(pageNo, pageSize));
        
        return new PageInfo<FeesClaimsEntity>(list);
    }

    @SuppressWarnings("unchecked")
    @Override
    public PageInfo<FeesClaimsEntity> queryPreBillClaimChange(Map<String, Object> condition, int pageNo, int pageSize) {
        // TODO Auto-generated method stub
        List<FeesClaimsEntity> list = selectList(
                "com.jiuyescm.bms.fees.claim.mapper.FeesClaimsMapper.queryPreBillClaimChange",
                condition, new RowBounds(pageNo, pageSize));
        
        return new PageInfo<FeesClaimsEntity>(list);
    }

	
}
