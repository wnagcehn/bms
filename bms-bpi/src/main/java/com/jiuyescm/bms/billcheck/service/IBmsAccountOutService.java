package com.jiuyescm.bms.billcheck.service;

import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.billcheck.vo.BillAccountOutVo;

/**
 * 
 * @author liuzhicheng
 * 
 */
public interface IBmsAccountOutService {

	PageInfo<BillAccountOutVo> query(Map<String, Object> condition,int pageNo, int pageSize);

	//账单冲抵
	String save(Map<String, Object> param);
	
}
