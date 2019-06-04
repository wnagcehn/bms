/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.fees.abnormal.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.data.provider.Page;
import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.calculate.api.IBmsCalcuExceptionService;
import com.jiuyescm.bms.calculate.vo.ExceptionDetailVo;
import com.jiuyescm.exception.BizException;

/**
 * 计算异常数据监控
 * <功能描述>
 * 
 * @author wangchen870
 * @date 2019年6月4日 上午11:01:39
 */
@Controller("calcuExceptionController")
public class CalcuExceptionController {
    
    private static final Logger logger = LoggerFactory.getLogger(CalcuExceptionController.class.getName());
    
    @Autowired
    private IBmsCalcuExceptionService bmsCalcuExceptionService;
    
    /**
     * 分页查询
     * @param page
     * @param param
     * @throws ParseException 
     */
    @DataProvider
    public void query(Page<ExceptionDetailVo> page, Map<String, Object> param){
        if (param == null) {
            param = new HashMap<String, Object>();
        }
        if (param.get("startDate") == null) {
            throw new BizException("开始日期不能为空!");
        }
        if (param.get("endDate") == null) {
            throw new BizException("结束日期不能为空!");
        }
        
        //给结束日期加一天
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Calendar c1 = Calendar.getInstance();
            Calendar c2 = Calendar.getInstance();
            c1.setTime((Date) param.get("startDate"));
            c2.setTime((Date) param.get("endDate"));
            c2.add(Calendar.DAY_OF_MONTH, 1);
            param.put("startDate", sdf.format(c1.getTime()));
            param.put("endDate", sdf.format(c2.getTime()));
        } catch (Exception e) {
            logger.error("日期转换异常：", e);
            throw new BizException("日期转换异常!");
        }
    
        PageInfo<ExceptionDetailVo> pageInfo = bmsCalcuExceptionService.query(param, page.getPageNo(), page.getPageSize());
        if (pageInfo != null) {
            page.setEntities(pageInfo.getList());
            page.setEntityCount((int) pageInfo.getTotal());
        }
    }
    
}
