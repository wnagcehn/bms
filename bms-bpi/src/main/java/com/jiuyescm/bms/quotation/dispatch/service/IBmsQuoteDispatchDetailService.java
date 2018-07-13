/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.quotation.dispatch.service;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.quotation.dispatch.entity.vo.BmsQuoteDispatchDetailVo;

/**
 * 宅配报价明细
 * @author yangss
 */
public interface IBmsQuoteDispatchDetailService {

    PageInfo<BmsQuoteDispatchDetailVo> query(Map<String, Object> condition, int pageNo,
            int pageSize) throws Exception;

    List<BmsQuoteDispatchDetailVo> queryAllById(
			Map<String, Object> parameter) throws Exception;
    
    /**
	  * 通过temid查找对应的id
	  * @param TempId
	  * @return
	  */
	Integer getId(String temid);
	 
    int save(BmsQuoteDispatchDetailVo entity) throws Exception;

    int update(BmsQuoteDispatchDetailVo entity) throws Exception;

    int removePriceDistribution(BmsQuoteDispatchDetailVo entity) throws Exception;
    
    int removeDispatchByMap(Map<String,Object> condition);
    
    /**
     * 批量插入模板信息
     * @param list
     * @return
     */
	int insertBatchTmp(List<BmsQuoteDispatchDetailVo> list) throws Exception;
	 
    void delete(Long id) throws Exception;

}
