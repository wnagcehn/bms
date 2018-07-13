/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.quotation.out.transport.repository;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.quotation.out.transport.entity.PriceTransportPayTemplateEntity;

/**
 * 
 * @author wubangjun
 * 
 */
public interface IPriceTransportPayTemplateRepository {

	public PageInfo<PriceTransportPayTemplateEntity> query(Map<String, Object> condition,
            int pageNo, int pageSize);

    public PriceTransportPayTemplateEntity findById(Long id);

    public PriceTransportPayTemplateEntity save(PriceTransportPayTemplateEntity entity);
    
    int saveList(List<PriceTransportPayTemplateEntity> transportPayTemplateList);

    public PriceTransportPayTemplateEntity update(PriceTransportPayTemplateEntity entity);

    int updateList(List<PriceTransportPayTemplateEntity> transportPayTemplateList);
    
    public void delete(Long id);

    public Integer findIdByTemplateCode(String templateCode);
}
