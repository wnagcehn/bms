/*
 * Copyright (c) 2019, Jiuye SCM and/or its affiliates. All rights reserved.
 */
package com.jiuyescm.bms.calculate;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.jiuyescm.bms.appconfig.DruidConfig;
import com.jiuyescm.bms.appconfig.TenantConfig;
import com.jiuyescm.bms.calculate.api.IBmsCalcuExceptionService;
import com.jiuyescm.bms.calculate.vo.ExceptionDetailVo;
import com.jiuyescm.bs.util.HttpPostUtil;
import com.jiuyescm.exception.BizException;

/**
 * <功能描述>
 * 分页查询计算异常信息
 * @author caojianwei
 * @date 2019年6月3日 下午5:20:32
 */
@Service("bmsCalcuExceptionService")
public class BmsCalcuExceptionServiceImpl implements IBmsCalcuExceptionService {

    private Logger logger = LoggerFactory.getLogger(BmsCalcuExceptionServiceImpl.class);
    
    @Autowired private DruidConfig druidConfig;
    
    @Autowired private TenantConfig tenantConfig;
    
    @Override
    public PageInfo<ExceptionDetailVo> query(Map<String, Object> condition, int pageNo, int pageSize) throws BizException {
        String url = druidConfig.getUrl();
        condition.put("pageNo", pageNo);
        condition.put("pageSize", pageSize);
        String json = JSON.toJSONString(condition);
        Map<String,String> headMap=Maps.newHashMap();
        headMap.put("Authorization", "Basic aml1eWVzY206SnkxMjM0NTY=");
        try{
            String jsonString = HttpPostUtil.post(url,json,headMap);
            JSONObject jb0 = JSONObject.parseObject(jsonString);
            String code = jb0.getString("code");
            logger.info("code:{}",code);
            if(!"SUCCESS".equals(code)){
                logger.info("message:{}",jb0.getString("message"));
                throw new BizException(jb0.getString("message"));
            }
            JSONObject jb = jb0.getJSONObject("items");
            PageInfo<ExceptionDetailVo> pageInfo =
                    (PageInfo<ExceptionDetailVo>) JSON.parseObject(jb.toJSONString(), new TypeReference<PageInfo<ExceptionDetailVo>>(){});
            return pageInfo;
        }
        catch(Exception ex){
            logger.error("druid rest api 接口异常：",ex);
            throw new BizException("druid rest api 接口异常："+ex.getMessage());
        }
    }

}


