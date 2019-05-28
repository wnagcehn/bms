package com.jiuyescm.bms.biz.storage.service;

import java.util.List;
import java.util.Map;

import com.jiuyescm.bms.biz.storage.vo.BizAddFeeVo;

/**
 * oms对接保存接口
 * @author liuzhicheng
 */
public interface IAddFeeService {

	/**
	 * oms对接保存接口
	 * @param param
	 * @return
	 */
    Map<String, String> save(List<BizAddFeeVo> listVo);

}