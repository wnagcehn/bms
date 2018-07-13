/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.quotation.transport.repository;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.quotation.transport.entity.PriceTransportTemplateEntity;

/**
 * 
 * @author wubangjun
 * 
 */
public interface IPriceTransportTemplateRepository {

	public PageInfo<PriceTransportTemplateEntity> query(Map<String, Object> condition,
            int pageNo, int pageSize);
	
	public PriceTransportTemplateEntity query(Map<String, Object> condition);

    public PriceTransportTemplateEntity findById(Long id);

    public PriceTransportTemplateEntity save(PriceTransportTemplateEntity entity);
    
    int saveList(List<PriceTransportTemplateEntity> transportTemplateList);

    public PriceTransportTemplateEntity update(PriceTransportTemplateEntity entity);

    int updateList(List<PriceTransportTemplateEntity> transportTemplateList);
    
    public void delete(Long id);

    public Integer findIdByTemplateCode(String templateCode);
}
