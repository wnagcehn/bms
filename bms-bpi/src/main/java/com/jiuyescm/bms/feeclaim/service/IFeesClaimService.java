/*
 * Copyright (c) 2019, Jiuye SCM and/or its affiliates. All rights reserved.
 */
package com.jiuyescm.bms.feeclaim.service;

import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.feeclaim.vo.FeesClaimsVo;
import com.jiuyescm.bms.fees.abnormal.entity.FeesAbnormalEntity;

/**
 * <功能描述>
 * 
 * @author zhaofeng
 * @date 2019年6月20日 下午2:22:11
 */
public interface IFeesClaimService {
    PageInfo<FeesClaimsVo> query(Map<String, Object> condition, int pageNo,
            int pageSize);
    
    int update(FeesClaimsVo vo);
    
    //客诉理赔
    PageInfo<FeesClaimsVo> queryPreBillClaim(Map<String, Object> condition, int pageNo,
            int pageSize);
    
    //改地址退件费
    PageInfo<FeesClaimsVo> queryPreBillClaimChange(Map<String, Object> condition, int pageNo,
            int pageSize);
}


