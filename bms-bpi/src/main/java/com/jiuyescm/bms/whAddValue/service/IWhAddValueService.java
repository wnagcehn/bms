/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.whAddValue.service;

import java.util.List;

import com.jiuyescm.bms.whAddValue.vo.ResultVo;
import com.jiuyescm.bms.whAddValue.vo.WhAddValueVo;

public interface IWhAddValueService {
	public List<ResultVo> insertWhaddValue(List<WhAddValueVo> list);
}
