/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.systemCode.service.api;

import java.util.List;
import java.util.Map;

import com.jiuyescm.bms.systemCode.service.vo.SystemCodeVo;

public interface ISystemCodeService {

	List<SystemCodeVo> queryValueAddList(Map<String,Object> mapCondition);
}
