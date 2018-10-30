package com.jiuyescm.bms.billcheck.service;

import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.billcheck.BillAccountInEntity;
import com.jiuyescm.bms.billcheck.BillAccountOutEntity;
import com.jiuyescm.bms.billcheck.BillCheckInfoEntity;

/**
 * 
 * @author liuzhicheng
 * 
 */
public interface IBmsAccountOutService {

	PageInfo<BillAccountOutEntity> query(Map<String, Object> condition,int pageNo, int pageSize);

	String save(Map<String, Object> param);
	
}
