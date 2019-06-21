/*
 * Copyright (c) 2019, Jiuye SCM and/or its affiliates. All rights reserved.
 */
package com.jiuyescm.bms.feeclaim;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.feeclaim.service.IFeesClaimService;
import com.jiuyescm.bms.feeclaim.vo.FeesClaimsVo;
import com.jiuyescm.bms.fees.claim.FeesClaimsEntity;
import com.jiuyescm.bms.fees.claim.repository.IFeesClaimsRepository;

/**
 * <功能描述>
 * 
 * @author zhaofeng
 * @date 2019年6月20日 下午2:34:13
 */
@Service("feesClaimService")
public class FeeClaimServiceImpl implements IFeesClaimService{
    private static final Logger logger = Logger.getLogger(FeeClaimServiceImpl.class.getName());

    @Resource
    IFeesClaimsRepository feesClaimsRepository;
    
    @Override
    public PageInfo<FeesClaimsVo> query(Map<String, Object> condition, int pageNo, int pageSize) {
        // TODO Auto-generated method stub
        PageInfo<FeesClaimsVo> result=new PageInfo<FeesClaimsVo>();

        try {
            PageInfo<FeesClaimsEntity> pageInfo=feesClaimsRepository.query(condition, pageNo, pageSize);           
            List<FeesClaimsVo> voList = new ArrayList<FeesClaimsVo>();
            for(FeesClaimsEntity entity : pageInfo.getList()) {
                FeesClaimsVo vo = new FeesClaimsVo();         
                PropertyUtils.copyProperties(vo, entity);          
                voList.add(vo);
            }
            
            PropertyUtils.copyProperties(result, pageInfo); 
            result.setList(voList);
            return result;
        } catch (Exception ex) {
            logger.error("转换失败:{0}",ex);
        }
        
        return result;
    }


    @Override
    public int update(FeesClaimsVo vo) {
        // TODO Auto-generated method stub
        try {
            FeesClaimsEntity entity=new FeesClaimsEntity();
            PropertyUtils.copyProperties(entity, vo);
            return feesClaimsRepository.update(entity);
        } catch (Exception ex) {
            logger.error("转换失败:{0}",ex);
        }
        return 0;

    }

}


