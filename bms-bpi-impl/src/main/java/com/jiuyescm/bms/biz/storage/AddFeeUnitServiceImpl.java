/*
 * Copyright (c) 2019, Jiuye SCM and/or its affiliates. All rights reserved.
 */
package com.jiuyescm.bms.biz.storage;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Maps;
import com.jiuyescm.bms.base.addunit.repository.IPubAddvalueUnitRepository;
import com.jiuyescm.bms.biz.storage.service.IAddFeeUnitService;
import com.jiuyescm.utils.JsonUtils;

/**
  * <功能描述>
  * 
  * @author wangchen
  * @date 2019年7月4日 上午11:48:42
  */
@Service("addFeeUnitService")
public class AddFeeUnitServiceImpl implements IAddFeeUnitService {

    private Logger logger = LoggerFactory.getLogger(AddFeeUnitServiceImpl.class);
    
    @Autowired
    private IPubAddvalueUnitRepository pubAddvalueUnitRepository;
    
    @Override
    public List<String> queryUnitBySecondSubject(String secondSubject) {
        List<String> unitList = null;
        logger.info("WMS传入二级科目：{}", secondSubject);
        if (StringUtils.isBlank(secondSubject)) {
            return null;
        }
        try {
            Map<String, Object> param = Maps.newLinkedHashMap();
            param.put("secondSubjectCode", secondSubject);
            param.put("delFlag", "0");
            unitList = pubAddvalueUnitRepository.queryUnitBySecondSubject(param);
            logger.info("通过二级科目：{}, 对应的单位有：{}", secondSubject, JsonUtils.toJson(unitList));
        } catch (Exception e) {
            logger.error("查询异常：", e);
        }
        return unitList;
    }

}


