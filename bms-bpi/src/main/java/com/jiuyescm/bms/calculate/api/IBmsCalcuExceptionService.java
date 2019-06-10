/*
 * Copyright (c) 2019, Jiuye SCM and/or its affiliates. All rights reserved.
 */
package com.jiuyescm.bms.calculate.api;

import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.calculate.vo.ExceptionDetailVo;
import com.jiuyescm.exception.BizException;

/**
 * <功能描述>
 * 
 * @author caojianwei
 * @date 2019年6月3日 下午5:11:26
 */
public interface IBmsCalcuExceptionService {

    /**
     * 
     * <功能描述>
     * 分页查询计算异常数据
     * @author caojianwei
     * @date 2019年6月3日 下午5:12:09
     *
     * @param condition
     * @param pageNo
     * @param pageSize
     * @return
     */
    PageInfo<ExceptionDetailVo> query(Map<String, Object> condition, int pageNo,int pageSize) throws BizException;
}


