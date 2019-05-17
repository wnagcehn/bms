package com.jiuyescm.bms.biz.storage.service;

import java.util.List;

import com.jiuyescm.bms.biz.storage.entity.BizAddFeeEntity;

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
	String save(List<BizAddFeeEntity> list);
}