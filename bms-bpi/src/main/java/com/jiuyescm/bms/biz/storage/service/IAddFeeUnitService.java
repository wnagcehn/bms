/*
 * Copyright (c) 2019, Jiuye SCM and/or its affiliates. All rights reserved.
 */
package com.jiuyescm.bms.biz.storage.service;

import java.util.List;

/**
  * WMS增值费二级类目和单位映射关系
  * 
  * @author wangchen
  * @date 2019年7月4日 上午11:50:12
  */
public interface IAddFeeUnitService {
    
    /**
     * WMS单位查询接口
     * @param param 二级类目
     * @return
     */
    List<String> queryUnitBySecondSubject(String secondSubject);
    
}


