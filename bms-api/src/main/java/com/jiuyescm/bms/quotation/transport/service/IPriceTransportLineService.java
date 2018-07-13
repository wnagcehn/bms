/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.quotation.transport.service;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.base.calcu.vo.CalcuResultVo;
import com.jiuyescm.bms.biz.transport.entity.BizGanxianWayBillEntity;
import com.jiuyescm.bms.quotation.transport.entity.PriceTransportLineEntity;

/**
 * 运输路线表API
 * @author wubangjun
 */
public interface IPriceTransportLineService {

    PageInfo<PriceTransportLineEntity> query(Map<String, Object> condition, int pageNo,
            int pageSize);
    
    List<PriceTransportLineEntity> query(Map<String, Object> condition);

    PriceTransportLineEntity findById(Long id);

    PriceTransportLineEntity save(PriceTransportLineEntity entity);
    
    int saveList(List<PriceTransportLineEntity> lineList);

    PriceTransportLineEntity update(PriceTransportLineEntity entity);

    void delete(Long id);
    
    Integer findIdByLineNo(String lineNo);

	int saveListWithChild(List<PriceTransportLineEntity> lineList);
	
	int deleteBatch(Long id);
	
	int deleteBatchList(List<PriceTransportLineEntity> lineList);
	
	
	//根据产品类型查询目的省、市
	List<PriceTransportLineEntity> queryToCityByProductType(Map<String, Object> condition);
	
	//查询标准报价的路线
	List<PriceTransportLineEntity> queryStandardTemplateLine(Map<String, Object> condition);

	//试算
	CalcuResultVo trial(BizGanxianWayBillEntity data);
	
	//查询商家报价的路线
	List<PriceTransportLineEntity> queryTemplateLine(Map<String, Object> condition);
}
