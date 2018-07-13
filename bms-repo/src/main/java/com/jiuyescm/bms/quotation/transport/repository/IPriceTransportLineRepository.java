/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.quotation.transport.repository;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.quotation.transport.entity.PriceTransportLineEntity;

/**
 * 
 * @author wubangjun
 * 
 */
public interface IPriceTransportLineRepository {

	public PageInfo<PriceTransportLineEntity> query(Map<String, Object> condition,
            int pageNo, int pageSize);
	
	public List<PriceTransportLineEntity> query(Map<String, Object> condition);

    public PriceTransportLineEntity findById(Long id);

    public PriceTransportLineEntity save(PriceTransportLineEntity entity);
    
    public int saveList(List<PriceTransportLineEntity> lineList);

    public PriceTransportLineEntity update(PriceTransportLineEntity entity);

    public void delete(Long id);
    
    public Integer findIdByLineNo(String lineNo);
    
    public int deleteBatch(Long id);
    
    public int deleteBatchRange(Long id);
    
    public int deleteBatchList(List<PriceTransportLineEntity> lineList);
    
    public List<PriceTransportLineEntity> queryToCity(Map<String, Object> condition);
    
    public List<PriceTransportLineEntity> queryToCityByProductType(Map<String, Object> condition);
    
    //查询标准报价的路线
    public List<PriceTransportLineEntity> queryStandardTemplateLine(Map<String, Object> condition);
    
    //根据合同编号查询商家的报价
	public List<PriceTransportLineEntity> queryTransportQuos(Map<String, Object> parameter);
	
	//根据商家id查询商家的报价
	public List<PriceTransportLineEntity> queryTemplateLine(Map<String, Object> parameter);
}
