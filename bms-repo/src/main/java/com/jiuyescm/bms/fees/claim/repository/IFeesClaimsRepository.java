package com.jiuyescm.bms.fees.claim.repository;

import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.fees.claim.FeesClaimsEntity;
/**
 * 应收费用-客诉 接口
 * 
 * @author zhaofeng
 *
 */
public interface IFeesClaimsRepository {
	
	PageInfo<FeesClaimsEntity> query(Map<String, Object> condition, int pageNo, int pageSize);
	int update(FeesClaimsEntity entity);
	
    //客诉理赔
    PageInfo<FeesClaimsEntity> queryPreBillClaim(Map<String, Object> condition, int pageNo,
            int pageSize);
    
    //改地址退件费
    PageInfo<FeesClaimsEntity> queryPreBillClaimChange(Map<String, Object> condition, int pageNo,
            int pageSize);
}
