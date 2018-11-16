/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.billcheck.service;

import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.billcheck.vo.BillAccountInVo;
import com.jiuyescm.exception.BizException;

/**
 * 
 * @author liuzhicheng
 * 
 */
public interface IBmsBillAccountInService {

	/**
	 * 分页查询预收款录入信息 仅查询未作废
	 * @param condition
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
    PageInfo<BillAccountInVo> query(Map<String, Object> condition, int pageNo,int pageSize);

    BillAccountInVo findById(Long id);

    BillAccountInVo save(BillAccountInVo entity);

    /**
     * 预收款录入更新  仅能对未确认的状态进行更新
     * @param entity
     * @return
     */
    void update(BillAccountInVo entity) throws BizException;
    
    /**
     * 预收款录入确认
     * @param id 录入信息主键
     * @return
     */
    void confirm(BillAccountInVo vo) throws BizException;

    /**
     * 作废预收款录入 仅能对未确认的状态进行作废
     * @param id 录入信息主键
     */
    void delete(BillAccountInVo vo) throws BizException;

}
