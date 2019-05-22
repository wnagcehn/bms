/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.receivable.storage.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.jiuyescm.bms.biz.dispatch.entity.BizDispatchPackageEntity;
import com.jiuyescm.bms.receivable.storage.service.IBizDispatchPackageService;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;

/**
 * 标准耗材dao层
 * @author cjw
 * 
 */
@Repository("bizDispatchPackageService")
public class BizDispatchPackageServiceImpl extends MyBatisDao<BizDispatchPackageEntity> implements IBizDispatchPackageService {

	private static final Logger logger = Logger.getLogger(BizDispatchPackageServiceImpl.class.getName());

	public BizDispatchPackageServiceImpl() {
		super();
	}

	@Override
	public List<BizDispatchPackageEntity> query(Map<String, Object> condition) {
		List<BizDispatchPackageEntity> list = selectList("com.jiuyescm.bms.receivable.storage.mapper.BizDispatchPackageMapper.query", condition);
		return list;
	}


	@Override
	public void updateBatch(List<BizDispatchPackageEntity> list) {
		try{
			this.updateBatch("com.jiuyescm.bms.receivable.storage.mapper.BizDispatchPackageMapper.updateBatch", list);
		}catch(Exception ex){
			logger.error("【标准耗材使用费任务】批量更新主表异常"+ex.getMessage());
		}
	}

	/**
     * 通过运单号批量查询
     * <功能描述>
     * 
     * @author wangchen870
     * @date 2019年5月17日 下午6:36:14
     *
     * @param list
     * @return
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<BizDispatchPackageEntity> queryByWaybillNo(List<String> list){
        Map<String, Object> cond = new HashMap<String, Object>();
        cond.put("list", list);
        return this.selectList("com.jiuyescm.bms.receivable.storage.mapper.BizDispatchPackageMapper.queryByWaybillNo", cond);
    }
	
}
